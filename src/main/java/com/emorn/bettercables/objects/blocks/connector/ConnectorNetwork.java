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
    private static int lastId = 0;
    private final int id;

    private boolean shouldMerge = false;
    private Map<BlockPos, ConnectorNetwork> mergeToNetwork = new HashMap<>();

    public ConnectorNetwork()
    {
        id = lastId++;
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
