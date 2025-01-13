package com.emorn.bettercables.objects.blocks.connector;

import com.emorn.bettercables.Main;
import com.emorn.bettercables.init.BlockInit;
import com.emorn.bettercables.objects.blocks.BlockBase;
import com.emorn.bettercables.utils.IHasModel;
import com.emorn.bettercables.utils.Reference;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockConnector extends BlockBase implements IHasModel
{
    public static final PropertyEnum<ConnectionType> NORTH = PropertyEnum.create("north", ConnectionType.class);
    public static final PropertyEnum<ConnectionType> EAST = PropertyEnum.create("east", ConnectionType.class);
    public static final PropertyEnum<ConnectionType> SOUTH = PropertyEnum.create("south", ConnectionType.class);
    public static final PropertyEnum<ConnectionType> WEST = PropertyEnum.create("west", ConnectionType.class);
    public static final PropertyEnum<ConnectionType> UP = PropertyEnum.create("up", ConnectionType.class);
    public static final PropertyEnum<ConnectionType> DOWN = PropertyEnum.create("down", ConnectionType.class);

    public BlockConnector(String name)
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
        return Item.getItemFromBlock(BlockInit.CONNECTOR);
    }

    @Override
    public boolean onBlockActivated(
        World worldIn,
        BlockPos pos,
        IBlockState state,
        EntityPlayer playerIn,
        EnumHand hand,
        EnumFacing facing,
        float hitX,
        float hitY,
        float hitZ
    )
    {
        if (!worldIn.isRemote) {
            playerIn.openGui(
                Main.instance,
                Reference.GUI_CONNECTOR,
                worldIn,
                pos.getX(),
                pos.getY(),
                pos.getZ()
            );
        }

        return true;
    }

    @Override
    public ItemStack getItem(
        World worldIn,
        BlockPos pos,
        IBlockState state
    )
    {
        return new ItemStack(BlockInit.CONNECTOR);
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
        return true;
    }

    @Override
    public TileEntity createTileEntity(
        World world,
        IBlockState state
    )
    {
        return new TileEntityConnector();
    }

    private ConnectionType getConnectionType(
        IBlockAccess world,
        BlockPos pos,
        EnumFacing facing
    )
    {
        IBlockState neighborState = world.getBlockState(pos.offset(facing));
        Block neighborBlock = neighborState.getBlock();

        // Check for inventories first
        TileEntity te = world.getTileEntity(pos.offset(facing));
        if (te instanceof IInventory) {
            return ConnectionType.INVENTORY;
        }

        // Check if neighbor is another connector block
        if (neighborBlock instanceof BlockConnector) {
            return ConnectionType.CONNECTOR;
        }

        return ConnectionType.NONE;
    }
}