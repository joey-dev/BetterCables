package com.emorn.bettercables.core.blocks.connector;

import com.emorn.bettercables.contract.common.IPositionInWorld;
import com.emorn.bettercables.core.blocks.connector.network.ConnectorNetwork;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConnectorNetworkHandler implements IConnectorNetworkService
{
    @Nullable
    private ConnectorNetwork network = null;
    private final IPositionInWorld positionInWorld;

    public ConnectorNetworkHandler(
        IPositionInWorld positionInWorld
    )
    {
        this.positionInWorld = positionInWorld;
    }

    public void tick(
        boolean isClient
    )
    {
        if (!isClient) {
            return;
        }

        if (this.network == null || this.network.isDisabled()) {
            return;
        }

        if (this.network.isRemoved()) {
            this.network = this.network.mergeToNetwork(positionInWorld);
        }
    }

    public boolean isNetworkDisabled()
    {
        return this.network == null || this.network.isDisabled();
    }

    public ConnectorNetwork getNetwork()
    {
        if (this.network == null) {
            throw new IllegalStateException("Network is null");
        }
        return this.network;
    }

    @Nullable
    public ConnectorNetwork getNetworkOrNull()
    {
        return this.network;
    }

    public void setNetwork(@Nullable ConnectorNetwork connectorNetwork)
    {
        this.network = connectorNetwork;
    }
}
