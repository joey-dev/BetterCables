package com.emorn.bettercables.contract.common;

import com.emorn.bettercables.core.blocks.connector.network.ConnectorNetwork;

public interface ITileEntity
{
    public boolean isInventory();

    public IInventory getInventory();

    public ConnectorNetwork getNetworkIfConnector();
}
