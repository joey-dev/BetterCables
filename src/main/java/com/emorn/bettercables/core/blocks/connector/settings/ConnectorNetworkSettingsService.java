package com.emorn.bettercables.core.blocks.connector.settings;

import com.emorn.bettercables.core.common.Direction;
import com.emorn.bettercables.core.blocks.connector.network.ConnectorNetwork;
import com.emorn.bettercables.core.common.Logger;
import com.emorn.bettercables.objects.application.blocks.connector.ConnectorSide;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConnectorNetworkSettingsService
{
    private final ConnectorSides connectorSides;

    public ConnectorNetworkSettingsService(
        ConnectorSides connectorSides
    )
    {
        this.connectorSides = connectorSides;
    }

    @Nullable
    public ConnectorSettings findImportSettings(
        Direction direction,
        ConnectorNetwork network,
        Integer nextIndex
    )
    {
        ConnectorSettings inventorySettings = network.findInsertSettingsBy(nextIndex);

        if (inventorySettings == null) {
            Logger.error("No settings found at: " + direction);
            return null;
        }
        return inventorySettings;
    }

    @Nullable
    public ConnectorSettings settings(Direction direction)
    {
        return this.findConnectorSettingsByDirection(direction);
    }

    @Nullable
    private ConnectorSettings findConnectorSettingsByDirection(Direction direction)
    {
        ConnectorSide connectorSide = connectorSides.findConnectorByDirection(direction);
        if (connectorSide == null) {
            return null;
        }

        return connectorSide.connectorSettings;
    }
}
