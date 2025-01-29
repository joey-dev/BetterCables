package com.emorn.bettercables.objects.gateway.blocks;

import com.emorn.bettercables.objects.api.forge.common.Logger;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConnectorSettingsDefaultFilter extends ConnectorSettingsFilter
{
    @Override
    public boolean isOverwriteEnabled()
    {
        Logger.error("override is not a option for default filter settings");
        return false;
    }

    @Override
    public void changeOverwriteEnabled(boolean overwriteEnabled)
    {
        Logger.error("override is not a option for default filter settings");
    }
}