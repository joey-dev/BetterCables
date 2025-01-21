package com.emorn.bettercables.objects.application.blocks.cable;

import com.emorn.bettercables.objects.gateway.blocks.AxisAlignedBoundingBox;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CableAxisAlignedBoundingBox
{
    public static final AxisAlignedBoundingBox BASE = new AxisAlignedBoundingBox(
        7,
        2,
        7,
        2,
        7,
        2
    );
    public static final AxisAlignedBoundingBox NORTH = new AxisAlignedBoundingBox(
        7,
        2,
        7,
        2,
        0,
        9
    );
    public static final AxisAlignedBoundingBox EAST = new AxisAlignedBoundingBox(
        7,
        9,
        7,
        2,
        7,
        2
    );
    public static final AxisAlignedBoundingBox SOUTH = new AxisAlignedBoundingBox(
        7,
        2,
        7,
        2,
        7,
        9
    );
    public static final AxisAlignedBoundingBox WEST = new AxisAlignedBoundingBox(
        0,
        7,
        7,
        2,
        7,
        2
    );
    public static final AxisAlignedBoundingBox UP = new AxisAlignedBoundingBox(
        7,
        2,
        7,
        9,
        7,
        2
    );
    public static final AxisAlignedBoundingBox DOWN = new AxisAlignedBoundingBox(
        7,
        2,
        0,
        9,
        7,
        2
    );

    private CableAxisAlignedBoundingBox()
    {
    }
}
