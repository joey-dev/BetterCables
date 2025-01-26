package com.emorn.bettercables.common.gui;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public enum ExtractType implements IStringSerializable
{
    ROUND_ROBIN,
    PRIORITY,
    CLOSEST_FIRST,
    FURTHEST_FIRST;

    @Override
    @Nonnull
    public String getName()
    {
        return this.name().toLowerCase();
    }
}
