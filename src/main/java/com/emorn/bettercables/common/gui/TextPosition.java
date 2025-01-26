package com.emorn.bettercables.common.gui;

import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;

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
