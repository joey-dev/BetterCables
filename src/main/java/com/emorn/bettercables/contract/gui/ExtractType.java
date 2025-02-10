package com.emorn.bettercables.contract.gui;

import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public enum ExtractType
{
    ROUND_ROBIN,
    PRIORITY,
    CLOSEST_FIRST,
    FURTHEST_FIRST;

    @Nonnull
    public String getName()
    {
        return this.name().toLowerCase();
    }
}
