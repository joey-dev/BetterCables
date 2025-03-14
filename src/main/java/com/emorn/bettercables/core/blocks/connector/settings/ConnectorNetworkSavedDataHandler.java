package com.emorn.bettercables.core.blocks.connector.settings;

import com.emorn.bettercables.contract.asyncEventBus.IAsyncEventBus;
import com.emorn.bettercables.contract.blocks.connector.IData;
import com.emorn.bettercables.contract.common.IPositionInWorld;
import com.emorn.bettercables.core.blocks.connector.network.NetworkManager;
import com.emorn.bettercables.core.common.Direction;
import com.emorn.bettercables.core.blocks.connector.network.ConnectorNetwork;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConnectorNetworkSavedDataHandler
{
    public static final String NETWORK_ID = "NetworkId";
    private final ConnectorSides connectorSides;
    private final NetworkManager networkManager;


    public ConnectorNetworkSavedDataHandler(
        ConnectorSides connectorSides
    )
    {
        this.connectorSides = connectorSides;
        this.networkManager = NetworkManager.getInstance();
    }

    @Nullable
    public ConnectorNetwork retrieveNetworkFromNBT(
        IData compound,
        IPositionInWorld positionInWorld,
        IAsyncEventBus eventBus,
        boolean isClient
    )
    {
        int networkId = compound.loadInteger(NETWORK_ID);
        if (networkId == 0) {
            return null;
        }

        ConnectorNetwork foundNetwork = this.networkManager.createNewNetwork(
            compound.loadInteger(NETWORK_ID),
            eventBus
        );

        if (!isClient) {
            this.addInsertConnectorInformationToNetwork(
                foundNetwork,
                positionInWorld
            );
            this.addExtractConnectorInformationToNetwork(
                foundNetwork,
                positionInWorld
            );
        }

        return foundNetwork;
    }

    private void addInsertConnectorInformationToNetwork(
        ConnectorNetwork network,
        IPositionInWorld positionInWorld
    )
    {
        if (connectorSides.isInsertEnabled(Direction.NORTH)) {
            network.addInsert(
                positionInWorld.offset(Direction.NORTH),
                connectorSides.connectorSettings(Direction.NORTH)
            );
        } else {
            network.removeInsert(connectorSides.connectorSettings(Direction.NORTH));
        }

        if (connectorSides.isInsertEnabled(Direction.EAST)) {
            network.addInsert(
                positionInWorld.offset(Direction.EAST),
                connectorSides.connectorSettings(Direction.EAST)
            );
        } else {
            network.removeInsert(connectorSides.connectorSettings(Direction.EAST));
        }

        if (connectorSides.isInsertEnabled(Direction.SOUTH)) {
            network.addInsert(
                positionInWorld.offset(Direction.SOUTH),
                connectorSides.connectorSettings(Direction.SOUTH)
            );
        } else {
            network.removeInsert(connectorSides.connectorSettings(Direction.SOUTH));
        }

        if (connectorSides.isInsertEnabled(Direction.WEST)) {
            network.addInsert(
                positionInWorld.offset(Direction.WEST),
                connectorSides.connectorSettings(Direction.WEST)
            );
        } else {
            network.removeInsert(connectorSides.connectorSettings(Direction.WEST));
        }

        if (connectorSides.isInsertEnabled(Direction.UP)) {
            network.addInsert(
                positionInWorld.offset(Direction.UP),
                connectorSides.connectorSettings(Direction.UP)
            );
        } else {
            network.removeInsert(connectorSides.connectorSettings(Direction.UP));
        }

        if (connectorSides.isInsertEnabled(Direction.DOWN)) {
            network.addInsert(
                positionInWorld.offset(Direction.DOWN),
                connectorSides.connectorSettings(Direction.DOWN)
            );
        } else {
            network.removeInsert(connectorSides.connectorSettings(Direction.DOWN));
        }
    }

    private void addExtractConnectorInformationToNetwork(
        ConnectorNetwork network,
        IPositionInWorld positionInWorld
    )
    {
        if (connectorSides.isExtractEnabled(Direction.NORTH)) {
            network.addExtract(
                positionInWorld.offset(Direction.NORTH),
                connectorSides.connectorSettings(Direction.NORTH)
            );
        } else {
            network.removeExtract(connectorSides.connectorSettings(Direction.NORTH));
        }

        if (connectorSides.isExtractEnabled(Direction.EAST)) {
            network.addExtract(
                positionInWorld.offset(Direction.EAST),
                connectorSides.connectorSettings(Direction.EAST)
            );
        } else {
            network.removeExtract(connectorSides.connectorSettings(Direction.EAST));
        }

        if (connectorSides.isExtractEnabled(Direction.SOUTH)) {
            network.addExtract(
                positionInWorld.offset(Direction.SOUTH),
                connectorSides.connectorSettings(Direction.SOUTH)
            );
        } else {
            network.removeExtract(connectorSides.connectorSettings(Direction.SOUTH));
        }

        if (connectorSides.isExtractEnabled(Direction.WEST)) {
            network.addExtract(
                positionInWorld.offset(Direction.WEST),
                connectorSides.connectorSettings(Direction.WEST)
            );
        } else {
            network.removeExtract(connectorSides.connectorSettings(Direction.WEST));
        }

        if (connectorSides.isExtractEnabled(Direction.UP)) {
            network.addExtract(
                positionInWorld.offset(Direction.UP),
                connectorSides.connectorSettings(Direction.UP)
            );
        } else {
            network.removeExtract(connectorSides.connectorSettings(Direction.UP));
        }

        if (connectorSides.isExtractEnabled(Direction.DOWN)) {
            network.addExtract(
                positionInWorld.offset(Direction.DOWN),
                connectorSides.connectorSettings(Direction.DOWN)
            );
        } else {
            network.removeExtract(connectorSides.connectorSettings(Direction.DOWN));
        }
    }

    public void storeNetworkFromNBT(
        IData compound,
        @Nullable ConnectorNetwork network
    )
    {
        if (network == null) {
            compound.save(NETWORK_ID, 0);
        } else {
            compound.save(NETWORK_ID, network.id());
        }
    }
}
