package com.emorn.bettercables.core.common;

import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public enum Direction
{
    NORTH,
    EAST,
    SOUTH,
    WEST,
    UP,
    DOWN;

    @Nonnull
    public String getName()
    {
        return this.name().toLowerCase();
    }
}