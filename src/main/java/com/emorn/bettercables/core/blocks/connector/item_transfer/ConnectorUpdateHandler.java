package com.emorn.bettercables.core.blocks.connector.item_transfer;

import com.emorn.bettercables.core.common.performance.PerformanceTester;
import com.emorn.bettercables.contract.common.IPositionInWorld;
import com.emorn.bettercables.contract.common.IWorld;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSides;
import com.emorn.bettercables.core.blocks.connector.IConnectorNetworkService;
import com.emorn.bettercables.core.common.Direction;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConnectorUpdateHandler
{
    private final ConnectorSides connectorSides = new ConnectorSides();
    private final IConnectorNetworkService networkService;
    private final ConnectorExportItemHandler exportItemHandler;

    public ConnectorUpdateHandler(
        IConnectorNetworkService networkService
    )
    {
        this.networkService = networkService;

        this.exportItemHandler = new ConnectorExportItemHandler(
            this.connectorSides,
            this.networkService
        );
    }

    public ConnectorSides getConnectorSides()
    {
        return this.connectorSides;
    }

    public void invoke(
        boolean isClient,
        IPositionInWorld positionInWorld,
        IWorld world
    )
    {
        PerformanceTester.printResults();
        if (!isClient) {
            return;
        }

        if (this.networkService.isNetworkDisabled()) {
            return;
        }

        PerformanceTester.start("ConnectorBlockEntity.tick");

        this.connectorSides.tick();

        if (this.connectorSides.canNorthExport()) {
            this.exportItemHandler.invoke(
                Direction.NORTH,
                positionInWorld,
                world
            );
        }

        if (this.connectorSides.canEastExport()) {
            this.exportItemHandler.invoke(
                Direction.EAST,
                positionInWorld,
                world
            );
        }

        if (this.connectorSides.canSouthExport()) {
            this.exportItemHandler.invoke(
                Direction.SOUTH,
                positionInWorld,
                world
            );
        }

        if (this.connectorSides.canWestExport()) {
            PerformanceTester.start("export west");
            this.exportItemHandler.invoke(
                Direction.WEST,
                positionInWorld,
                world
            );
            PerformanceTester.end("export west");
        }

        if (this.connectorSides.canUpExport()) {
            this.exportItemHandler.invoke(
                Direction.UP,
                positionInWorld,
                world
            );
        }

        if (this.connectorSides.canDownExport()) {
            this.exportItemHandler.invoke(
                Direction.DOWN,
                positionInWorld,
                world
            );
        }

        PerformanceTester.end("ConnectorBlockEntity.tick");
    }
}
