package com.emorn.bettercables.common.gui;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public enum ComparisonOperator implements IStringSerializable
{
    LESS_THAN,
    GREATER_THAN,
    EQUALS;

    @Override
    @Nonnull
    public String getName()
    {
        return this.name().toLowerCase();
    }
}
