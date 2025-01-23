package com.emorn.bettercables.objects.api.forge.common;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
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