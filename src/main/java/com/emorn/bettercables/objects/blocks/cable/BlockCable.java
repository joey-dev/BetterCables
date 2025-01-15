package com.emorn.bettercables.objects.blocks.cable;

import com.emorn.bettercables.init.BlockInit;
import com.emorn.bettercables.objects.blocks.BlockBase;
import com.emorn.bettercables.objects.blocks.connector.BlockConnector;
import com.emorn.bettercables.utils.IHasModel;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockCable extends BlockBase implements IHasModel
{
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");

    public BlockCable(String name)
    {
        super(name, Material.IRON);
        setSoundType(SoundType.METAL);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

    @Override
    public IBlockState getActualState(
        IBlockState state,
        IBlockAccess world,
        BlockPos pos
    )
    {
        return state
            .withProperty(NORTH, getConnectionType(world, pos, EnumFacing.NORTH))
            .withProperty(EAST, getConnectionType(world, pos, EnumFacing.EAST))
            .withProperty(SOUTH, getConnectionType(world, pos, EnumFacing.SOUTH))
            .withProperty(WEST, getConnectionType(world, pos, EnumFacing.WEST))
            .withProperty(UP, getConnectionType(world, pos, EnumFacing.UP))
            .withProperty(DOWN, getConnectionType(world, pos, EnumFacing.DOWN));
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public void onBlockAdded(
        World worldIn,
        BlockPos pos,
        IBlockState state
    )
    {
        if (!worldIn.isRemote) {
            worldIn.setBlockState(pos, state, 2);
        }
    }

    @Override
    public Item getItemDropped(
        IBlockState state,
        Random rand,
        int fortune
    )
    {
        return Item.getItemFromBlock(BlockInit.CABLE);
    }

    @Override
    public ItemStack getItem(
        World worldIn,
        BlockPos pos,
        IBlockState state
    )
    {
        return new ItemStack(BlockInit.CABLE);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(
            this,
            NORTH,
            EAST,
            SOUTH,
            WEST,
            UP,
            DOWN
        );
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return false;
    }

    private boolean getConnectionType(
        IBlockAccess world,
        BlockPos pos,
        EnumFacing facing
    )
    {
        IBlockState neighborState = world.getBlockState(pos.offset(facing));
        Block neighborBlock = neighborState.getBlock();

        return (neighborBlock instanceof BlockCable || neighborBlock instanceof BlockConnector);
    }
}