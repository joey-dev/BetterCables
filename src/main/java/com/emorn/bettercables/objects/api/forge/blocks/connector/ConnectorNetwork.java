package com.emorn.bettercables.objects.api.forge.blocks.connector;

import com.emorn.bettercables.contract.common.IPositionInWorld;
import com.emorn.bettercables.objects.api.forge.common.Logger;
import com.emorn.bettercables.objects.application.blocks.connector.PossibleSlots;
import com.emorn.bettercables.objects.gateway.blocks.ConnectorSettings;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConnectorNetwork
{
    private static final Map<Integer, ConnectorNetwork> createdNetworksById = new HashMap<>();
    private static int lastId = 1;
    private final int id;
    private final Map<IPositionInWorld, ConnectorNetwork> mergeToNetwork = new HashMap<>();
    private boolean shouldMerge = false;
    private boolean isDisabled = false;
    // 1: extract settings, 2: insert settings, 3: insertIndex, 4: extractIndex
    private final Map<ConnectorSettings, Map<ConnectorSettings, List<List<Integer>>>> possibleSlotsToExtract = new HashMap<>();
    // key = settings, value = inventory
    private final Map<ConnectorSettings, BlockPos> insertConnectorSettings = new HashMap<>();
    // key = settings, value = inventory
    private final Map<ConnectorSettings, BlockPos> extractConnectorSettings = new HashMap<>();

    private ConnectorNetwork()
    {
        id = lastId++;
    }

    private ConnectorNetwork(int savedId)
    {
        if (savedId >= lastId) {
            lastId = savedId + 1;
        }

        id = savedId;
    }

    public static ConnectorNetwork create(int savedId)
    {
        if (createdNetworksById.containsKey(savedId)) {
            return createdNetworksById.get(savedId);
        }
        ConnectorNetwork network = new ConnectorNetwork(savedId);
        createdNetworksById.put(network.id, network);
        return network;
    }

    public static ConnectorNetwork create()
    {
        ConnectorNetwork network = new ConnectorNetwork();
        createdNetworksById.put(network.id, network);
        return network;
    }

    @Nullable
    public Integer findNextIndex(int index)
    {
        int totalItems = this.insertConnectorSettings.size();
        if (totalItems == 0) {
            return null;
        }

        index++;

        if (index >= totalItems) {
            index = 0;
        }

        return index;
    }

    @Nullable
    public IPositionInWorld findInventoryPositionBy(Integer index)
    {
        int totalItems = this.insertConnectorSettings.size();

        if (index >= totalItems) {
            return null;
        }

        if (this.insertConnectorSettings.keySet().toArray()[index] == null) {
            Logger.error("Tried to get empty");
        }

        return (IPositionInWorld) this.insertConnectorSettings.values().toArray()[index];
    }

    @Nullable
    public ConnectorSettings findInsertSettingsBy(Integer index)
    {
        int totalItems = this.insertConnectorSettings.size();

        if (index >= totalItems) {
            return null;
        }

        ConnectorSettings settings = (ConnectorSettings) this.insertConnectorSettings.keySet().toArray()[index];

        if (settings == null) {
            Logger.error("Tried to get empty");
        }

        return settings;
    }

    public int id()
    {
        return id;
    }

    public void remove(ConnectorNetwork newNetwork)
    {
        this.shouldMerge = true;
        //this.mergeToNetwork.put(new BlockPos(0, 0, 0), newNetwork); todo fix
    }

    public void addInsert(
        BlockPos inventoryPosition,
        ConnectorSettings settings
    )
    {
        if (this.insertConnectorSettings.containsKey(settings) && this.insertConnectorSettings.get(settings).equals(inventoryPosition)) {
            return;
        }

        this.isDisabled = true;

        this.insertConnectorSettings.put(settings, inventoryPosition);
        this.recalculateInsertsFromPossibleSlots(settings);

        this.isDisabled = false;
    }

    public void addExtract(
        BlockPos inventoryPosition,
        ConnectorSettings settings
    )
    {
        if (this.extractConnectorSettings.containsKey(settings) && this.extractConnectorSettings.get(settings).equals(inventoryPosition)) {
            return;
        }

        this.isDisabled = true;

        this.extractConnectorSettings.put(settings, inventoryPosition);
        this.recalculateExtractsFromPossibleSlots(settings);

        this.isDisabled = false;
    }

    public List<List<Integer>> getPossibleSlots(
        ConnectorSettings exportSettings,
        ConnectorSettings importSettings
    )
    {
        this.possibleSlotsToExtract.computeIfAbsent(exportSettings, k -> new HashMap<>());
        this.possibleSlotsToExtract.get(exportSettings).computeIfAbsent(importSettings, k -> new ArrayList<>());

        return this.possibleSlotsToExtract.get(exportSettings).get(importSettings);
    }

    public void reCalculateAllPossibleSlots()
    {
        this.isDisabled = true;
        this.possibleSlotsToExtract.clear();

        for (Map.Entry<ConnectorSettings, BlockPos> insertEntry : this.insertConnectorSettings.entrySet()) {
            for (Map.Entry<ConnectorSettings, BlockPos> extractEntry : this.extractConnectorSettings.entrySet()) {
                // 1: extract, 2: insert, 3: insertIndex, 4: extractIndex
                this.possibleSlotsToExtract.putIfAbsent(extractEntry.getKey(), new HashMap<>());
                this.possibleSlotsToExtract.get(extractEntry.getKey()).putIfAbsent(insertEntry.getKey(), new ArrayList<>());

                this.possibleSlotsToExtract.get(extractEntry.getKey())
                    .put(insertEntry.getKey(), PossibleSlots.calculate(extractEntry.getKey(), insertEntry.getKey()));
            }
        }

        this.isDisabled = false;
    }

    public void removeInsert(
        ConnectorSettings connectorSettings
    )
    {
        if (!this.insertConnectorSettings.containsKey(connectorSettings)) {
            return;
        }

        this.isDisabled = true;

        this.insertConnectorSettings.remove(connectorSettings);
        this.removeInsertFromPossibleSlots(connectorSettings);

        this.isDisabled = false;
    }

    public void removeExtract(
        ConnectorSettings connectorSettings
    )
    {
        if (!this.extractConnectorSettings.containsKey(connectorSettings)) {
            return;
        }

        this.isDisabled = true;

        this.extractConnectorSettings.remove(connectorSettings);
        this.removeExtractFromPossibleSlots(connectorSettings);

        this.isDisabled = false;
    }

    public boolean isDisabled()
    {
        return isDisabled;
    }

    public void remove(
        IPositionInWorld position,
        ConnectorNetwork newNetwork
    )
    {
        this.shouldMerge = true;

        this.mergeToNetwork.put(position, newNetwork);
    }

    public boolean isRemoved()
    {
        return shouldMerge;
    }

    @Nullable
    public ConnectorNetwork mergeToNetwork(IPositionInWorld position)
    {
        if (mergeToNetwork.containsKey(new BlockPos(0, 0, 0))) {
            return mergeToNetwork.get(new BlockPos(0, 0, 0));
        }

        return mergeToNetwork.get(position);
    }

    public void updateSlotCount(
        int sizeInventory,
        ConnectorSettings connector
    )
    {
        if (connector.inventorySlotCount() == sizeInventory) {
            return;
        }

        connector.changeInventorySlotCount(sizeInventory);

        if (connector.isInsertEnabled()) {
            this.recalculateInsertsFromPossibleSlots(connector);
        }

        if (connector.isExtractEnabled()) {
            this.recalculateExtractsFromPossibleSlots(connector);
        }
    }

    private void removeInsertFromPossibleSlots(
        ConnectorSettings insertSettings
    )
    {
        this.possibleSlotsToExtract.forEach((k, v) -> {
            v.remove(insertSettings);
        });
    }

    private void removeExtractFromPossibleSlots(
        ConnectorSettings extractSettings
    )
    {
        this.possibleSlotsToExtract.remove(extractSettings);
    }

    private void recalculateInsertsFromPossibleSlots(
        ConnectorSettings insertSettings
    )
    {
        this.extractConnectorSettings.forEach((k, v) -> {
            this.possibleSlotsToExtract.putIfAbsent(k, new HashMap<>());

            List<List<Integer>> possibleSlots = PossibleSlots.calculate(k, insertSettings);
            this.possibleSlotsToExtract.get(k).put(insertSettings, possibleSlots);
        });
    }

    private void recalculateExtractsFromPossibleSlots(
        ConnectorSettings extractSettings
    )
    {
        this.insertConnectorSettings.forEach((k, v) -> {
            this.possibleSlotsToExtract.putIfAbsent(extractSettings, new HashMap<>());

            List<List<Integer>> possibleSlots = PossibleSlots.calculate(extractSettings, k);
            this.possibleSlotsToExtract.get(extractSettings).put(k, possibleSlots);
        });
    }

}
