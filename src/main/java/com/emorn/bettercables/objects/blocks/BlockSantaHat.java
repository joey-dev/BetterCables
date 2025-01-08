
package com.emorn.bettercables.objects.blocks;

import com.emorn.bettercables.utils.IHasModel;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockSantaHat extends BlockBase implements IHasModel
{
    private static final double OFFSET = (double) 3 / 16;

    public static final AxisAlignedBB SANTA_HAT_AABB = new AxisAlignedBB(
            OFFSET,
            0,
            OFFSET,
            1 - OFFSET,
            (double) 10 / 16,
            1 - OFFSET
    );

    public BlockSantaHat(String name)
    {
        super(name, Material.CLOTH);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return SANTA_HAT_AABB;
    }
}
