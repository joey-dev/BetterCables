package com.emorn.bettercables.objects.application.blocks.connector;

import com.emorn.bettercables.objects.gateway.blocks.AxisAlignedBoundingBox;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConnectorAxisAlignedBoundingBox
{
    public static final AxisAlignedBoundingBox NORTH = new AxisAlignedBoundingBox(
        4,
        8,
        4,
        8,
        0,
        1
    );
    public static final AxisAlignedBoundingBox EAST = new AxisAlignedBoundingBox(
        15,
        1,
        4,
        8,
        4,
        8
    );
    public static final AxisAlignedBoundingBox SOUTH = new AxisAlignedBoundingBox(
        4,
        8,
        4,
        8,
        15,
        1
    );
    public static final AxisAlignedBoundingBox WEST = new AxisAlignedBoundingBox(
        0,
        1,
        4,
        8,
        4,
        8
    );
    public static final AxisAlignedBoundingBox UP = new AxisAlignedBoundingBox(
        4,
        8,
        15,
        1,
        4,
        8
    );
    public static final AxisAlignedBoundingBox DOWN = new AxisAlignedBoundingBox(
        4,
        8,
        0,
        1,
        4,
        8
    );

    private ConnectorAxisAlignedBoundingBox()
    {
    }
}
