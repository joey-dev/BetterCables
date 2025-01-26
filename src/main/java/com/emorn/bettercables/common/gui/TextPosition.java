package com.emorn.bettercables.common.gui;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public enum TextPosition implements IStringSerializable
{
    TOP,
    RIGHT,
    BOTTOM,
    LEFT;

    @Override
    @Nonnull
    public String getName()
    {
        return this.name().toLowerCase();
    }
}
