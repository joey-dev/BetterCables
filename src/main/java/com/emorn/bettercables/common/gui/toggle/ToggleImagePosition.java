package com.emorn.bettercables.common.gui.toggle;

import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ToggleImagePosition
{
    public final int x;
    public final int y;
    public final int textureX;
    public final int textureY;
    public final int width;
    public final int height;

    public ToggleImagePosition(
        int x,
        int y,
        int textureX,
        int textureY,
        int width,
        int height
    )
    {
        this.x = x;
        this.y = y;
        this.textureX = textureX;
        this.textureY = textureY;
        this.width = width;
        this.height = height;
    }
}
