package com.emorn.bettercables.api.v1_12_2.gui.elements;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public enum TextPosition implements IStringSerializable
{
    TOP,
    RIGHT;

    @Override
    @Nonnull
    public String getName()
    {
        return this.name().toLowerCase();
    }
}
