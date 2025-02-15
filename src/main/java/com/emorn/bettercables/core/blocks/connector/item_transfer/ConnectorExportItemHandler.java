package com.emorn.bettercables.core.blocks.connector.item_transfer;

import com.emorn.bettercables.contract.common.IInventory;
import com.emorn.bettercables.contract.common.IItemStack;
import com.emorn.bettercables.contract.common.IPositionInWorld;
import com.emorn.bettercables.contract.common.IWorld;
import com.emorn.bettercables.core.blocks.connector.IConnectorNetworkService;
import com.emorn.bettercables.core.blocks.connector.network.ConnectorNetwork;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorNetworkSettingsService;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSettings;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSide;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSides;
import com.emorn.bettercables.core.common.Direction;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConnectorExportItemHandler
{
    private final Map<Direction, Integer> directionToIndexMap = new EnumMap<>(Direction.class);
    private final ConnectorSides connectorSides;
    private final IConnectorNetworkService connectorNetworkService;
    private final InventoryService inventoryService;
    private final ConnectorInventoryLocator connectorInventoryLocator;
    private final ConnectorNetworkSettingsService connectorNetworkSettingsService;

    public ConnectorExportItemHandler(
        ConnectorSides connectorSides,
        IConnectorNetworkService connectorNetworkService
    )
    {
        this.connectorSides = connectorSides;
        this.connectorNetworkService = connectorNetworkService;
        this.inventoryService = new InventoryService();
        this.connectorInventoryLocator = new ConnectorInventoryLocator();
        this.connectorNetworkSettingsService = new ConnectorNetworkSettingsService(
            connectorSides
        );
    }

    public void invoke(
        Direction direction,
        IPositionInWorld positionInWorld,
        IWorld world
    )
    {
        if (this.connectorNetworkService.isNetworkDisabled()) {
            return;
        }

        ConnectorNetwork network = this.connectorNetworkService.getNetwork();

        Integer nextIndex = findNextInsertInventoryIndex(direction, network);
        if (nextIndex == null) {
            return;
        }

        IInventory importInventory = this.connectorInventoryLocator.findImportInventory(
            direction,
            network,
            nextIndex,
            world
        );
        if (importInventory == null) {
            return;
        }

        ConnectorSettings inventorySettings = this.connectorNetworkSettingsService.findImportSettings(
            direction,
            network,
            nextIndex
        );

        if (inventorySettings == null) {
            return;
        }

        IInventory exportInventory = this.connectorInventoryLocator.findExportInventory(
            direction,
            positionInWorld,
            world
        );
        if (exportInventory == null) {
            return;
        }

        ConnectorSide connectorSide = connectorSides.findConnectorByDirection(direction);
        if (connectorSide == null) {
            return;
        }

        ConnectorSettings exportConnectorSettings = this.connectorNetworkSettingsService.settings(direction);

        if (exportConnectorSettings == null) {
            return;
        }

        List<List<Integer>> possibleIndexes = network.getPossibleSlots(
            connectorSide.connectorSettings,
            inventorySettings
        );

        exportItemFromSlots(possibleIndexes, exportInventory, importInventory);
    }

    @Nullable
    private Integer findNextInsertInventoryIndex(
        Direction direction,
        ConnectorNetwork network
    )
    {
        this.directionToIndexMap.putIfAbsent(direction, -1);

        int currentIndex = this.directionToIndexMap.get(direction);
        Integer nextIndex = network.findNextIndex(currentIndex);
        if (nextIndex == null) {
            return null;
        }

        this.directionToIndexMap.put(direction, nextIndex);
        return nextIndex;
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

            IItemStack items = this.inventoryService.extractItemFromInventory(exportInventory, possibleIndex.get(1), 1);
            if (items.isEmpty()) {
                cannotExtractPositions.put(possibleIndex.get(1), true);
                continue;
            }

            IItemStack itemsNotInserted = this.inventoryService.insertItemIntoInventory(
                importInventory,
                possibleIndex.get(0),
                items
            );
            if (itemsNotInserted.isEmpty()) {
                return;
            }

            this.inventoryService.insertItemIntoInventory(exportInventory, possibleIndex.get(1), itemsNotInserted);

            if (itemsNotInserted.getCount() != items.getCount()) {
                return;
            }
        }
    }
}
