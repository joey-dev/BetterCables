package com.emorn.bettercables.contract.gui;

import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public enum ComparisonOperator
{
    LESS_THAN,
    GREATER_THAN,
    EQUALS;

    @Nonnull
    public String getName()
    {
        return this.name().toLowerCase();
    }
}
