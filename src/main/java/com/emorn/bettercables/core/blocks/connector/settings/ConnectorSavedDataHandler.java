package com.emorn.bettercables.core.blocks.connector.settings;

import com.emorn.bettercables.contract.blocks.connector.IData;
import com.emorn.bettercables.contract.common.IPositionInWorld;
import com.emorn.bettercables.core.blocks.connector.ConnectorNetworkHandler;
import com.emorn.bettercables.core.common.Direction;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConnectorSavedDataHandler
{
    private final ConnectorSides connectorSides;
    private final ConnectorNetworkHandler connectorNetworkHandler;
    private final ConnectorNetworkSavedDataHandler connectorNetworkSavedDataHandler;

    public ConnectorSavedDataHandler(
        ConnectorSides connectorSides,
        ConnectorNetworkHandler connectorNetworkHandler
    )
    {
        this.connectorSides = connectorSides;
        this.connectorNetworkHandler = connectorNetworkHandler;
        this.connectorNetworkSavedDataHandler = new ConnectorNetworkSavedDataHandler(
            connectorSides
        );
    }

    public void readFromNBT(
        IData compound,
        IPositionInWorld positionInWorld
    )
    {
        this.connectorSides.connectorSettings(Direction.NORTH).deserializeNBT(compound, "north");
        this.connectorSides.connectorSettings(Direction.EAST).deserializeNBT(compound, "east");
        this.connectorSides.connectorSettings(Direction.SOUTH).deserializeNBT(compound, "south");
        this.connectorSides.connectorSettings(Direction.WEST).deserializeNBT(compound, "west");
        this.connectorSides.connectorSettings(Direction.UP).deserializeNBT(compound, "up");
        this.connectorSides.connectorSettings(Direction.DOWN).deserializeNBT(compound, "down");

        this.connectorNetworkHandler.setNetwork(
            this.connectorNetworkSavedDataHandler.retrieveNetworkFromNBT(
                compound,
                positionInWorld
            )
        );
    }

    public IData writeToNBT(IData compound)
    {
        this.connectorSides.connectorSettings(Direction.NORTH).serializeNBT(compound, "north");
        this.connectorSides.connectorSettings(Direction.EAST).serializeNBT(compound, "east");
        this.connectorSides.connectorSettings(Direction.SOUTH).serializeNBT(compound, "south");
        this.connectorSides.connectorSettings(Direction.WEST).serializeNBT(compound, "west");
        this.connectorSides.connectorSettings(Direction.UP).serializeNBT(compound, "up");
        this.connectorSides.connectorSettings(Direction.DOWN).serializeNBT(compound, "down");

        this.connectorNetworkSavedDataHandler.storeNetworkFromNBT(
            compound,
            this.connectorNetworkHandler.getNetworkOrNull()
        );

        return compound;
    }
}
