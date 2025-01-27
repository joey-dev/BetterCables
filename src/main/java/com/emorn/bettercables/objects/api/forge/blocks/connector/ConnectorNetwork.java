package com.emorn.bettercables.objects.api.forge.blocks.connector;

import com.emorn.bettercables.objects.api.forge.common.Logger;
import com.emorn.bettercables.objects.application.blocks.connector.ConnectorSide;
import com.emorn.bettercables.objects.application.blocks.connector.PossibleSlots;
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
    // key = inventory, value = list of connectors
    private final Map<BlockPos, List<BlockPos>> insertInventoryPositions = new HashMap<>();
    private final Map<BlockPos, ConnectorNetwork> mergeToNetwork = new HashMap<>();
    private boolean shouldMerge = false;
    private boolean isDisabled = false;

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
        int totalItems = this.insertInventoryPositions.size();
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
    public BlockPos findInventoryPositionBy(Integer index)
    {
        int totalItems = this.insertInventoryPositions.size();

        if (index >= totalItems) {
            return null;
        }

        if (this.insertInventoryPositions.keySet().toArray()[index] == null) {
            Logger.error("Tried to get empty");
        }

        return (BlockPos) this.insertInventoryPositions.keySet().toArray()[index];
    }

    public int id()
    {
        return id;
    }

    public void remove(ConnectorNetwork newNetwork)
    {
        this.shouldMerge = true;
        this.mergeToNetwork.put(new BlockPos(0, 0, 0), newNetwork);
    }

    public void addInsertInventoryPosition(
        BlockPos inventoryPosition,
        BlockPos connectorPosition
    )
    {
        this.isDisabled = true;

        this.insertInventoryPositions.computeIfAbsent(inventoryPosition, k -> new ArrayList<>());

        if (this.insertInventoryPositions.get(inventoryPosition).contains(connectorPosition)) {
            return;
        }
        this.insertInventoryPositions.get(inventoryPosition).add(connectorPosition);

        // List<List<Integer>> possibleSlots = PossibleSlots.calculate(); // todo

        this.isDisabled = false;
    }

    public List<List<Integer>> getPossibleSlots(
        ConnectorSide connectorSide
    )
    {
        return PossibleSlots.calculate();
    }

    public void removeInsertInventoryPosition(
        BlockPos inventoryPosition,
        BlockPos connectorPosition
    )
    {
        this.isDisabled = true;

        this.insertInventoryPositions.computeIfAbsent(inventoryPosition, k -> new ArrayList<>());

        this.insertInventoryPositions.get(inventoryPosition).remove(connectorPosition);

        if (this.insertInventoryPositions.get(inventoryPosition).isEmpty()) {
            this.insertInventoryPositions.remove(inventoryPosition);
        }

        // List<List<Integer>> possibleSlots = PossibleSlots.calculate(); // todo

        this.isDisabled = false;
    }

    public boolean isDisabled()
    {
        return isDisabled;
    }

    public void remove(
        BlockPos position,
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
    public ConnectorNetwork mergeToNetwork(BlockPos position)
    {
        if (mergeToNetwork.containsKey(new BlockPos(0, 0, 0))) {
            return mergeToNetwork.get(new BlockPos(0, 0, 0));
        }

        return mergeToNetwork.get(position);
    }
}
