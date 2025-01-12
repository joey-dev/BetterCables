package com.emorn.bettercables.objects.blocks.connector;

import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;

public enum ConnectionType implements IStringSerializable
{
    NONE,
    INVENTORY,
    CONNECTOR;

    @Override
    @Nonnull
    public String getName()
    {
        return this.name().toLowerCase();
    }
}