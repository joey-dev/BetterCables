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
        connectorSettings.deserializeNBT(compound, key);
    }

    public static void save(
        ConnectorSettings connectorSettings,
        NBTTagCompound compound,
        String key
    )
    {
        compound.merge(connectorSettings.serializeNBT(key));
    }
}
