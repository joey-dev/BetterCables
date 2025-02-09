package com.emorn.bettercables.core.blocks.connector.itemTransfer;

import com.emorn.bettercables.common.performance.PerformanceTester;
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
        IPositionInWorld positionInWorld,
        IWorld world,
        IConnectorNetworkService networkService
    )
    {
        this.networkService = networkService;

        this.exportItemHandler = new ConnectorExportItemHandler(
            positionInWorld,
            world,
            this.connectorSides,
            this.networkService
        );
    }

    public ConnectorSides getConnectorSides()
    {
        return this.connectorSides;
    }

    public void invoke(
        boolean isClient
    )
    {
        PerformanceTester.printResults();
        if (!isClient) {
            return;
        }

        if (!this.networkService.isNetworkEnabled()) {
            return;
        }

        PerformanceTester.start("ConnectorBlockEntity.tick");

        this.connectorSides.tick();

        if (this.connectorSides.canNorthExport()) {
            this.exportItemHandler.invoke(Direction.NORTH);
        }

        if (this.connectorSides.canEastExport()) {
            this.exportItemHandler.invoke(Direction.EAST);
        }

        if (this.connectorSides.canSouthExport()) {
            this.exportItemHandler.invoke(Direction.SOUTH);
        }

        if (this.connectorSides.canWestExport()) {
            PerformanceTester.start("export west");
            this.exportItemHandler.invoke(Direction.WEST);
            PerformanceTester.end("export west");
        }

        if (this.connectorSides.canUpExport()) {
            this.exportItemHandler.invoke(Direction.UP);
        }

        if (this.connectorSides.canDownExport()) {
            this.exportItemHandler.invoke(Direction.DOWN);
        }

        PerformanceTester.end("ConnectorBlockEntity.tick");
    }
}
