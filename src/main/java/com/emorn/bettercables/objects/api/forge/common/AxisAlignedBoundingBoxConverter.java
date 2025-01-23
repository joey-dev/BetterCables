package com.emorn.bettercables.objects.api.forge.common;

import com.emorn.bettercables.objects.gateway.blocks.AxisAlignedBoundingBox;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class AxisAlignedBoundingBoxConverter
{
    private AxisAlignedBoundingBoxConverter(
    )
    {
    }

    public static AxisAlignedBB from(
        AxisAlignedBoundingBox boundingBox
    )
    {
        return new AxisAlignedBB(
            convertOffset(boundingBox.offsetFromWest),
            convertOffset(boundingBox.offsetFromBottom),
            convertOffset(boundingBox.offsetFromNorth),
            convertSize(boundingBox.offsetFromWest, boundingBox.westSizeFromOffset),
            convertSize(boundingBox.offsetFromBottom, boundingBox.bottomSizeFromOffset),
            convertSize(boundingBox.offsetFromNorth, boundingBox.northSizeFromOffset)
        );
    }

    private static double convertOffset(int offset)
    {
        if (offset == 0) {
            return 0;
        }

        if (offset == 16) {
            return 1;
        }

        return offset / 16.0;
    }

    private static double convertSize(
        int offset,
        int size
    )
    {
        return convertOffset(offset + size);
    }
}
