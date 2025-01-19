package com.emorn.bettercables.objects.blocks.connector;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConnectorNetwork
{
    private static int lastId = 1;
    private static final Map<Integer, ConnectorNetwork> createdNetworksById = new HashMap<>();
    private final int id;

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
