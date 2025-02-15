package com.emorn.bettercables.core.blocks.connector.settings;

import com.emorn.bettercables.core.common.Direction;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConnectorSides
{
    private final ConnectorSide north = new ConnectorSide();
    private final ConnectorSide east = new ConnectorSide();
    private final ConnectorSide south = new ConnectorSide();
    private final ConnectorSide west = new ConnectorSide();
    private final ConnectorSide up = new ConnectorSide();
    private final ConnectorSide down = new ConnectorSide();

    @Nullable
    public ConnectorSide findConnectorByDirection(Direction direction)
    {
        switch (direction) {
            case NORTH:
                return this.north;
            case EAST:
                return this.east;
            case SOUTH:
                return this.south;
            case WEST:
                return this.west;
            case UP:
                return this.up;
            case DOWN:
                return this.down;
            default:
                return null;
        }
    }

    public boolean canNorthExport()
    {
        return this.north.canExport();
    }

    public boolean canEastExport()
    {
        return this.east.canExport();
    }

    public boolean canSouthExport()
    {
        return this.south.canExport();
    }

    public boolean canWestExport()
    {
        return this.west.canExport();
    }

    public boolean canUpExport()
    {
        return this.up.canExport();
    }

    public boolean canDownExport()
    {
        return this.down.canExport();
    }

    public ConnectorSettings connectorSettings(Direction direction)
    {
        return this.connectorSideByDirection(direction).connectorSettings();
    }

    private ConnectorSide connectorSideByDirection(Direction direction)
    {
        switch (direction) {
            case NORTH:
                return this.north;
            case EAST:
                return this.east;
            case SOUTH:
                return this.south;
            case WEST:
                return this.west;
            case UP:
                return this.up;
            case DOWN:
                return this.down;
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
    }

    public void tick()
    {
        this.north.tick();
        this.east.tick();
        this.south.tick();
        this.west.tick();
        this.up.tick();
        this.down.tick();
    }

    public boolean isInsertEnabled(Direction direction)
    {
        ConnectorSide connectorSide = this.findConnectorByDirection(direction);
        if (connectorSide != null) {
            return connectorSide.connectorSettings().isInsertEnabled();
        }
        return false;
    }

    public boolean isExtractEnabled(Direction direction)
    {
        ConnectorSide connectorSide = this.findConnectorByDirection(direction);
        if (connectorSide != null) {
            return connectorSide.connectorSettings().isExtractEnabled();
        }
        return false;
    }
}
