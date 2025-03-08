package com.emorn.bettercables.core.blocks.connector.item_transfer;

import com.emorn.bettercables.contract.common.*;
import com.emorn.bettercables.core.blocks.connector.IConnectorNetworkService;
import com.emorn.bettercables.core.blocks.connector.network.ConnectorNetwork;
import com.emorn.bettercables.core.blocks.connector.network.ExtractSlot;
import com.emorn.bettercables.core.blocks.connector.network.InsertInventory;
import com.emorn.bettercables.core.blocks.connector.network.InsertSlot;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorNetworkSettingsService;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSettings;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSide;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSides;
import com.emorn.bettercables.core.common.Direction;
import com.emorn.bettercables.core.common.Logger;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

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

        ConnectorSettings exportConnectorSettings = this.connectorNetworkSettingsService.settings(direction);

        if (exportConnectorSettings == null) {
            return;
        }

        ConnectorNetwork network = this.connectorNetworkService.getNetwork();

        Integer nextIndex = findNextInsertInventoryIndex(direction, network, exportConnectorSettings);
        if (nextIndex == null) {
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

        IInventory importInventory = this.connectorInventoryLocator.findImportInventory(
            direction,
            network,
            nextIndex,
            world,
            inventorySettings
        );

        if (importInventory == null) {
            return;
        }

        IInventory exportInventory = this.connectorInventoryLocator.findExportInventory(
            direction,
            positionInWorld,
            world,
            network,
            exportConnectorSettings
        );
        if (exportInventory == null) {
            return;
        }
        ConnectorSide connectorSide = connectorSides.findConnectorByDirection(direction);
        if (connectorSide == null) {
            return;
        }

        IItemHandler importInventoryHandler = importInventory.getItemHandler();
        IItemHandler exportInventoryHandler = exportInventory.getItemHandler();
        boolean didSlotCountChange = false;

        if (importInventoryHandler.slotCount() != inventorySettings.inventorySlotCount()) {
            Logger.error("inventory changed...");
            inventorySettings.changeInventorySlotCount(importInventoryHandler.slotCount());
            network.insertSlotCountChanged(inventorySettings);
            didSlotCountChange = true;
        }

        if (exportInventoryHandler.slotCount() != exportConnectorSettings.inventorySlotCount()) {
            exportConnectorSettings.changeInventorySlotCount(exportInventoryHandler.slotCount());
            network.extractSlotCountChanged(exportConnectorSettings);
            didSlotCountChange = true;
        }

        if (didSlotCountChange) {
            return;
        }

        List<ExtractSlot> possibleIndexes = network.getPossibleSlots(
            connectorSide.connectorSettings()
        );

        this.exportItemFromSlots(
            possibleIndexes,
            exportInventoryHandler,
            importInventoryHandler,
            inventorySettings
        );
    }

    @Nullable
    private Integer findNextInsertInventoryIndex(
        Direction direction,
        ConnectorNetwork network,
        ConnectorSettings settings
    )
    {
        this.directionToIndexMap.putIfAbsent(direction, -1);

        int currentIndex = this.directionToIndexMap.get(direction);
        Integer nextIndex = network.findNextIndex(currentIndex, settings);
        if (nextIndex == null) {
            return null;
        }

        this.directionToIndexMap.put(direction, nextIndex);
        return nextIndex;
    }

    private void exportItemFromSlots(
        List<ExtractSlot> possibleIndexes,
        IItemHandler exportInventory,
        IItemHandler importInventory,
        ConnectorSettings importSettings
    )
    {
        Set<Integer> cannotExtractPositions = new HashSet<>();
        Set<Integer> cannotInsertPositions = new HashSet<>();

        for (ExtractSlot possibleIndex : possibleIndexes) {
            if (possibleIndex == null) {
                return;
            }
            int exportSlot = possibleIndex.extractIndex();

            if (cannotExtractPositions.contains(exportSlot)) {
                continue;
            }

            InsertInventory insertInventory = possibleIndex.find(importSettings);

            if (insertInventory == null) {
                continue;
            }

            List<InsertSlot> insertSlots = insertInventory.insertSlots();

            for (InsertSlot insertSlot : insertSlots) {
                int importSlot = insertSlot.insertIndex();

                if (cannotInsertPositions.contains(importSlot)) {
                    continue;
                }

                IItemStack items = this.inventoryService.extractItemFromInventory(
                    exportInventory,
                    exportSlot,
                    1
                );
                if (items.isEmpty()) {
                    cannotExtractPositions.add(exportSlot);
                    continue;
                }

                IItemStack itemsNotInserted = this.inventoryService.insertItemIntoInventory(
                    importInventory,
                    importSlot,
                    items
                );
                if (itemsNotInserted.isEmpty()) {
                    return;
                }
                cannotInsertPositions.add(importSlot);

                this.inventoryService.insertItemIntoInventory(exportInventory, exportSlot, itemsNotInserted);

                if (itemsNotInserted.getCount() != items.getCount()) {
                    return;
                }
            }
        }
    }
}
