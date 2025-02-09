package com.emorn.bettercables.core.blocks.connector;

import com.emorn.bettercables.objects.api.forge.blocks.connector.ConnectorNetwork;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public interface IConnectorNetworkService
{
    public boolean isNetworkEnabled();

    public ConnectorNetwork getNetwork();

    // todo, needed?
    public void setNetwork(ConnectorNetwork connectorNetwork);
}
