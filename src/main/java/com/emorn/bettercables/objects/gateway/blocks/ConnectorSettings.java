package com.emorn.bettercables.objects.gateway.blocks;

import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConnectorSettings
{
    public int tickRate = 2;
    public boolean isExtractEnabled = false;
    public boolean isInsertEnabled = false;
    public int insertSlotCount = 27;
    public int extractSlotCount = 27;

    public ConnectorSettings()
    {
    }
}
