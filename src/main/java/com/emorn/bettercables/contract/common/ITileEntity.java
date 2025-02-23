package com.emorn.bettercables.contract.common;

import com.emorn.bettercables.core.blocks.connector.network.ConnectorNetwork;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSettings;
import com.emorn.bettercables.core.common.Direction;

public interface ITileEntity
{
    public boolean isInventory();

    public IInventory getInventory();

    public ConnectorNetwork getNetworkIfConnector();

    public ConnectorSettings settings(Direction direction);
}
