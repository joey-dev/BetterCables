package com.emorn.bettercables.api.v1_12_2.blocks.connector;

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