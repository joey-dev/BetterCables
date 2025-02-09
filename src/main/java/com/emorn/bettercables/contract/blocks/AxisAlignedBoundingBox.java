package com.emorn.bettercables.contract.blocks;

import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class AxisAlignedBoundingBox
{
    public final int offsetFromWest;
    public final int westSizeFromOffset;
    public final int offsetFromBottom;
    public final int bottomSizeFromOffset;
    public final int offsetFromNorth;
    public final int northSizeFromOffset;

    public AxisAlignedBoundingBox(
        int offsetFromWest,
        int westSizeFromOffset,
        int offsetFromBottom,
        int bottomSizeFromOffset,
        int offsetFromNorth,
        int northSizeFromOffset
    )
    {
        this.offsetFromWest = offsetFromWest;
        this.westSizeFromOffset = westSizeFromOffset;
        this.offsetFromBottom = offsetFromBottom;
        this.bottomSizeFromOffset = bottomSizeFromOffset;
        this.offsetFromNorth = offsetFromNorth;
        this.northSizeFromOffset = northSizeFromOffset;
    }
}
