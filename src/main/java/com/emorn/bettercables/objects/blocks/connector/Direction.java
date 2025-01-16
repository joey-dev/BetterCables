package com.emorn.bettercables.objects.blocks.connector;

import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;

public enum Direction implements IStringSerializable
{
    NORTH,
    EAST,
    SOUTH,
    WEST,
    UP,
    DOWN;

    @Override
    @Nonnull
    public String getName()
    {
        return this.name().toLowerCase();
    }
}