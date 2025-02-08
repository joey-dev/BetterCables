package com.emorn.bettercables.core.blocks.connector;

import com.emorn.bettercables.common.performance.PerformanceTester;
import com.emorn.bettercables.contract.*;
import com.emorn.bettercables.core.common.Direction;
import com.emorn.bettercables.core.common.EmptyItemStack;
import com.emorn.bettercables.objects.api.forge.blocks.connector.ConnectorNetwork;
import com.emorn.bettercables.objects.api.forge.common.Logger;
import com.emorn.bettercables.objects.application.blocks.connector.ConnectorSide;
import com.emorn.bettercables.objects.gateway.blocks.ConnectorSettings;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConnectorUpdateHandler
{
    private final Map<Direction, Integer> directionToIndexMap = new HashMap<>(); // todo maybe something else
    private final ConnectorSide north = new ConnectorSide();
    private final ConnectorSide east = new ConnectorSide();
    private final ConnectorSide south = new ConnectorSide();
    private final ConnectorSide west = new ConnectorSide();
    private final ConnectorSide up = new ConnectorSide();
    private final ConnectorSide down = new ConnectorSide();
    private final IPositionInWorld positionInWorld;
    private final IWorld world;
    @Nullable
    private ConnectorNetwork network = null;

    public ConnectorUpdateHandler(
        IPositionInWorld positionInWorld,
        IWorld world
    )
    {
        this.positionInWorld = positionInWorld;
        this.world = world;
    }

    public void invoke(
        boolean isClient
    )
    {
        PerformanceTester.printResults();
        if (!isClient) {
            return;
        }

        if (this.network == null || this.network.isDisabled()) {
            return;
        }

        if (this.network.isRemoved()) {
            this.network = this.network.mergeToNetwork(positionInWorld);
        }

        PerformanceTester.start("ConnectorBlockEntity.tick");

        this.tick();

        if (north.canExport()) {
            this.exportItem(Direction.NORTH);
        }

        if (east.canExport()) {
            this.exportItem(Direction.EAST);
        }

        if (south.canExport()) {
            this.exportItem(Direction.SOUTH);
        }

        if (west.canExport()) {
            PerformanceTester.start("export west");
            this.exportItem(Direction.WEST);
            PerformanceTester.end("export west");
        }

        if (up.canExport()) {
            this.exportItem(Direction.UP);
        }

        if (down.canExport()) {
            this.exportItem(Direction.DOWN);
        }

        PerformanceTester.end("ConnectorBlockEntity.tick");
    }

    private void tick()
    {
        north.tick();
        east.tick();
        south.tick();
        west.tick();
        up.tick();
        down.tick();
    }

    public void exportItem(Direction direction) // todo might want to extract this to another class?
    {
        if (this.network == null || this.network.isDisabled()) {
            return;
        }

        this.directionToIndexMap.putIfAbsent(direction, -1);

        int currentIndex = this.directionToIndexMap.get(direction);
        Integer nextIndex = this.network.findNextIndex(currentIndex);
        if (nextIndex == null) {
            return;
        }

        this.directionToIndexMap.put(direction, nextIndex);

        IPositionInWorld inventoryPosition = this.network.findInventoryPositionBy(nextIndex);

        if (inventoryPosition == null) {
            Logger.error("No inventory found at: " + direction);
            return;
        }

        ConnectorSettings inventorySettings = this.network.findInsertSettingsBy(nextIndex);

        if (inventorySettings == null) {
            Logger.error("No settings found at: " + direction);
            return;
        }

        IPositionInWorld exportInventoryPosition = this.findPositionByDirection(direction);

        IInventory exportInventory = this.findInventoryEntityByPosition(
            this.world,
            exportInventoryPosition
        );
        if (exportInventory == null) {
            Logger.error("Failed to get inventory inventory for export.");
            return;
        }

        IInventory importInventory = this.findInventoryEntityByPosition(this.world, inventoryPosition);
        if (importInventory == null) {
            Logger.error("Failed to get inventory inventory for import.");
            return;
        }

        ConnectorSide connectorSide = this.findConnectorByDirection(direction);
        if (connectorSide == null) {
            return;
        }

        if (this.network == null || this.network.isDisabled()) {
            return;
        }

        ConnectorSettings exportConnectorSettings = settings(direction);

        if (exportConnectorSettings == null) {
            return;
        }

        List<List<Integer>> possibleIndexes = this.network.getPossibleSlots(
            connectorSide.connectorSettings,
            inventorySettings
        );

        exportItemFromSlots(possibleIndexes, exportInventory, importInventory);
    }

    private IPositionInWorld findPositionByDirection(Direction direction)
    {
        return this.positionInWorld.offset(direction);
    }

    @Nullable
    private IInventory findInventoryEntityByPosition(
        IWorld world,
        IPositionInWorld inventoryPosition
    )
    {
        ITileEntity tileEntity = world.getTileEntity(inventoryPosition);
        if (!tileEntity.isInventory()) {
            Logger.error("No inventory found at: " + inventoryPosition);
            return null;
        }

        return tileEntity.getInventory();
    }

    @Nullable
    private ConnectorSide findConnectorByDirection(Direction direction)
    {
        switch (direction) {
            case NORTH:
                return this.north;
            case EAST:
                return this.east;
            case SOUTH:
                return this.south;
            case WEST:
                return this.west;
            case UP:
                return this.up;
            case DOWN:
                return this.down;
            default:
                return null;
        }
    }

    @Nullable
    public ConnectorSettings settings(Direction direction)
    {
        return this.findConnectorSettingsByDirection(direction);
    }

    private void exportItemFromSlots(
        List<List<Integer>> possibleIndexes,
        IInventory exportInventory,
        IInventory importInventory
    )
    {
        // todo maybe a queue
        Map<Integer, Boolean> cannotExtractPositions = new HashMap<>();

        for (List<Integer> possibleIndex : possibleIndexes) {
            if (Boolean.TRUE.equals(cannotExtractPositions.get(possibleIndex.get(1)))) {
                continue;
            }

            IItemStack items = this.extractItemFromInventory(exportInventory, possibleIndex.get(1), 1);
            if (items.isEmpty()) {
                cannotExtractPositions.put(possibleIndex.get(1), true);
                continue;
            }

            IItemStack itemsNotInserted = this.insertItemIntoInventory(importInventory, possibleIndex.get(0), items);
            if (itemsNotInserted.isEmpty()) {
                return;
            }

            this.insertItemIntoInventory(exportInventory, possibleIndex.get(1), itemsNotInserted);

            if (itemsNotInserted.getCount() != items.getCount()) {
                return;
            }
        }
    }

    @Nullable
    private ConnectorSettings findConnectorSettingsByDirection(Direction direction)
    {
        ConnectorSide connectorSide = this.findConnectorByDirection(direction);
        if (connectorSide == null) {
            return null;
        }

        return connectorSide.connectorSettings;
    }

    private IItemStack extractItemFromInventory(
        IInventory inventory,
        Integer slot,
        int amount
    )
    {
        IItemHandler exportInventory = inventory.getItemHandler();

        if (exportInventory == null) {
            Logger.error("Failed to get inventory inventory.");
            return new EmptyItemStack();
        }

        IItemStack extracted;
        try {
            // todo might want to check with simulate on first?
            extracted = exportInventory.extractItem(slot, amount, false);
        } catch (ArrayIndexOutOfBoundsException e) {
            Logger.error("Failed to extract item from inventory.");
            return new EmptyItemStack();
        }

        if (!extracted.isEmpty()) {
            inventory.markDirty();
        }

        return extracted;
    }

    private IItemStack insertItemIntoInventory(
        IInventory inventory,
        Integer slot,
        IItemStack items
    )
    {
        IItemHandler insertInventory = inventory.getItemHandler();

        if (insertInventory == null) {
            Logger.error("Failed to get inventory.");
            return new EmptyItemStack();
        }

        IItemStack itemsLeft;

        try {
            itemsLeft = insertInventory.insertItem(slot, items, false);
            /**
             * todo does not auto update when
             * there was a large chest with full slots
             * large chest is changed to small chest
             */
        } catch (ArrayIndexOutOfBoundsException e) {
            Logger.error("Failed to insert item from inventory.");
            return items;
        }

        if (itemsLeft.getCount() != items.getCount()) {
            inventory.markDirty();
        }

        return itemsLeft;
    }

    public ConnectorNetwork getNetwork()
    {
        if (this.network == null) {
            throw new IllegalStateException("Network is null");
        }
        return this.network;
    }

    public void setNetwork(ConnectorNetwork connectorNetwork)
    {
        this.network = connectorNetwork;
    }
}
