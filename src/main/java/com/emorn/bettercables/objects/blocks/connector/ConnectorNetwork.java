package com.emorn.bettercables.objects.blocks.connector;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConnectorNetwork
{
    private static int lastId = 1;
    private final int id;
    private static final Map<Integer, ConnectorNetwork> createdNetworksById = new HashMap<>();
    // key = inventory, value = list of connectors
    private final Map<BlockPos, List<BlockPos>> insertInventoryPositions = new HashMap<>();

    private boolean shouldMerge = false;
    private final Map<BlockPos, ConnectorNetwork> mergeToNetwork = new HashMap<>();

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

    public int findNextIndex(int index) {
        this.cleanInsertInventoryPositions();

        int totalItems = this.insertInventoryPositions.size();
        index++;

        if (index >= totalItems) {
            index = 0;
        }

        return index;
    }

    @Nullable
    public BlockPos findInventoryPositionBy(int index)
    {
        this.cleanInsertInventoryPositions();

        int totalItems = this.insertInventoryPositions.size();

        if (index >= totalItems) {
            return null;
        }

        return (BlockPos) this.insertInventoryPositions.keySet().toArray()[index];
    }

    private void cleanInsertInventoryPositions() // todo this is prob a bug
    {
        this.insertInventoryPositions.entrySet().removeIf(entry -> entry.getValue().isEmpty());
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

    public int id()
    {
        return id;
    }

    public void remove(ConnectorNetwork newNetwork)
    {
        this.shouldMerge = true;
        this.mergeToNetwork.put(new BlockPos(0, 0, 0), newNetwork);
    }

    public void addInsertInventoryPosition(BlockPos inventoryPosition, BlockPos connectorPosition)
    {
        this.insertInventoryPositions.computeIfAbsent(inventoryPosition, k -> new ArrayList<>());

        if (this.insertInventoryPositions.get(inventoryPosition).contains(connectorPosition)) {
            return;
        }
        this.insertInventoryPositions.get(inventoryPosition).add(connectorPosition);
    }

    public void removeInsertInventoryPosition(BlockPos inventoryPosition, BlockPos connectorPosition)
    {
        this.insertInventoryPositions.computeIfAbsent(inventoryPosition, k -> new ArrayList<>());

        this.insertInventoryPositions.get(inventoryPosition).remove(connectorPosition);
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
