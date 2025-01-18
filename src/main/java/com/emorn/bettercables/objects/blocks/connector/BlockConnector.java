package com.emorn.bettercables.objects.blocks.connector;

import com.emorn.bettercables.Main;
import com.emorn.bettercables.init.BlockInit;
import com.emorn.bettercables.objects.blocks.BlockBase;
import com.emorn.bettercables.objects.blocks.cable.BlockCable;
import com.emorn.bettercables.utils.IHasModel;
import com.emorn.bettercables.utils.Reference;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockConnector extends BlockBase implements IHasModel
{
    public static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(
        (double) 7 / 16,
        (double) 7 / 16,
        (double) 7 / 16,
        1 - ((double) 7 / 16),
        (double) 9 / 16,
        1 - ((double) 7 / 16)
    );

    public static final AxisAlignedBB NORTH_CABLE_AABB = new AxisAlignedBB(
        (double) 7 / 16,
        (double) 7 / 16,
        0,
        (double) 9 / 16,
        (double) 9 / 16,
        (double) 7 / 16
    );

    public static final AxisAlignedBB EAST_CABLE_AABB = new AxisAlignedBB(
        1,
        (double) 7 / 16,
        (double) 7 / 16,
        (double) 7 / 16,
        (double) 9 / 16,
        (double) 9 / 16
    );

    public static final AxisAlignedBB SOUTH_CABLE_AABB = new AxisAlignedBB(
        (double) 7 / 16,
        (double) 7 / 16,
        1,
        (double) 9 / 16,
        (double) 9 / 16,
        (double) 7 / 16
    );

    public static final AxisAlignedBB WEST_CABLE_AABB = new AxisAlignedBB(
        0,
        (double) 7 / 16,
        (double) 7 / 16,
        (double) 7 / 16,
        (double) 9 / 16,
        (double) 9 / 16
    );

    public static final AxisAlignedBB UP_CABLE_AABB = new AxisAlignedBB(
        (double) 7 / 16,
        (double) 7 / 16,
        (double) 7 / 16,
        (double) 9 / 16,
        1,
        (double) 9 / 16
    );

    public static final AxisAlignedBB DOWN_CABLE_AABB = new AxisAlignedBB(
        (double) 7 / 16,
        0,
        (double) 7 / 16,
        (double) 9 / 16,
        (double) 7 / 16,
        (double) 9 / 16
    );

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
    public void addCollisionBoxToList(
        IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
        List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState
    ) {
        IBlockState actualState = getActualState(state, worldIn, pos);
        List<AxisAlignedBB> allBoxes = retrieveAllBoxes(actualState);

        for (AxisAlignedBB box : allBoxes) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, box);
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        EntityPlayer player = source instanceof World ? ((World) source).getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 10, false) : null;
        if (player == null) {
            return BASE_AABB;
        }

        Vec3d start = player.getPositionEyes(1.0F);
        Vec3d lookVec = player.getLook(1.0F);
        Vec3d end = start.add(lookVec.scale(5));

        IBlockState actualState = getActualState(state, source, pos);
        List<AxisAlignedBB> allBoxes = retrieveAllBoxes(actualState);

        AxisAlignedBB closestBox = BASE_AABB;
        double closestDistance = Double.MAX_VALUE;

        for (AxisAlignedBB box : allBoxes) {
            RayTraceResult result = rayTrace(pos, start, end, box);
            if (result != null) {
                double distance = result.hitVec.distanceTo(start);
                if (distance < closestDistance) {
                    closestBox = box;
                    closestDistance = distance;
                }
            }
        }

        return closestBox;
    }

    @Override
    @Nullable
    public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
        IBlockState actualState = getActualState(blockState, worldIn, pos); // Get the actual state
        List<AxisAlignedBB> allBoxes = retrieveAllBoxes(actualState);

        RayTraceResult closestResult = null;
        double closestDistance = Double.MAX_VALUE;

        for (AxisAlignedBB box : allBoxes) {
            RayTraceResult result = rayTrace(pos, start, end, box);
            if (result != null) {
                double distance = result.hitVec.distanceTo(start);
                if (distance < closestDistance) {
                    closestResult = result;
                    closestDistance = distance;
                }
            }
        }

        return closestResult;
    }

    private List<AxisAlignedBB> retrieveAllBoxes(IBlockState state)
    {
        List<AxisAlignedBB> allBoxes = new ArrayList<>();

        allBoxes.add(BASE_AABB);

        if (state.getValue(NORTH).toString().equals(ConnectionType.CONNECTOR.toString())) {
            allBoxes.add(NORTH_CABLE_AABB);
        }

        if (state.getValue(EAST).toString().equals(ConnectionType.CONNECTOR.toString())) {
            allBoxes.add(EAST_CABLE_AABB);
        }

        if (state.getValue(SOUTH).toString().equals(ConnectionType.CONNECTOR.toString())) {
            allBoxes.add(SOUTH_CABLE_AABB);
        }

        if (state.getValue(WEST).toString().equals(ConnectionType.CONNECTOR.toString())) {
            allBoxes.add(WEST_CABLE_AABB);
        }

        if (state.getValue(UP).toString().equals(ConnectionType.CONNECTOR.toString())) {
            allBoxes.add(UP_CABLE_AABB);
        }

        if (state.getValue(DOWN).toString().equals(ConnectionType.CONNECTOR.toString())) {
            allBoxes.add(DOWN_CABLE_AABB);
        }

        return allBoxes;
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
                Reference.GUI_CONNECTOR_NORTH,
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

        TileEntity te = world.getTileEntity(pos.offset(facing));
        if (te instanceof IInventory) {
            return ConnectionType.INVENTORY;
        }

        if (neighborBlock instanceof BlockConnector || neighborBlock instanceof BlockCable) {
            return ConnectionType.CONNECTOR;
        }

        return ConnectionType.NONE;
    }
}