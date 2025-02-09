package com.emorn.bettercables.objects.application.blocks.connector;

import com.emorn.bettercables.core.common.Logger;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSettings;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConnectorSide
{
    private static final int TICK_INTERVAL = 20;
    public ConnectorSettings connectorSettings;
    private int currentTick = 0;

    public ConnectorSide()
    {
        this.connectorSettings = new ConnectorSettings();
    }

    public void tick()
    {
        currentTick++;
        if (currentTick >= TICK_INTERVAL) {
            currentTick = 0;
        }
    }

    public boolean canExport()
    {
        if (this.connectorSettings.tickRate() == 0) {
            Logger.error("Tick rate cannot be 0");
            return false;
        }

        if (currentTick % this.connectorSettings.tickRate() != 0) {
            return false;
        }

        return this.connectorSettings.isExtractEnabled();
    }
}
