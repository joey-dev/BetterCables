package com.emorn.bettercables.objects.api.forge.common;

import com.emorn.bettercables.objects.gateway.blocks.ConnectorSettings;
import net.minecraft.nbt.NBTTagCompound;

public class Settings
{
    private Settings()
    {
    }

    public static void load(
        ConnectorSettings connectorSettings,
        NBTTagCompound compound,
        String key
    )
    {
        connectorSettings.changeInsertEnabled(compound.getBoolean(key + "-isInsertEnabled"));
        connectorSettings.changeExtractEnabled(compound.getBoolean(key + "-isExtractEnabled"));
    }

    public static void save(
        ConnectorSettings connectorSettings,
        NBTTagCompound compound,
        String key
    )
    {
        compound.setBoolean(key + "-isInsertEnabled", connectorSettings.isInsertEnabled());
        compound.setBoolean(key + "-isExtractEnabled", connectorSettings.isExtractEnabled());
    }
}
