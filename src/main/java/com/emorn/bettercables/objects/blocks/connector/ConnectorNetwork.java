package com.emorn.bettercables.objects.blocks.connector;

import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConnectorNetwork
{
    private static int lastId = 0;
    private final int id;

    @Nullable
    private ConnectorNetwork mergeToNetwork = null;

    public ConnectorNetwork()
    {
        id = lastId++;
    }

    public int id()
    {
        return id;
    }

    public void remove(ConnectorNetwork firstNetwork)
    {
        this.mergeToNetwork = firstNetwork;
    }

    public boolean isRemoved()
    {
        return mergeToNetwork != null;
    }

    @Nullable
    public ConnectorNetwork mergeToNetwork()
    {
        return mergeToNetwork;
    }
}
