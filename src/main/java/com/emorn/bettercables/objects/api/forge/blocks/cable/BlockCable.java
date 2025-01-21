package com.emorn.bettercables.objects.api.forge.blocks.cable;

import com.emorn.bettercables.init.BlockInit;
import com.emorn.bettercables.objects.api.forge.blocks.connector.BlockConnector;
import com.emorn.bettercables.objects.api.forge.common.AxisAlignedBoundingBoxConverter;
import com.emorn.bettercables.objects.application.blocks.cable.CableAxisAlignedBoundingBox;
import com.emorn.bettercables.objects.blocks.BlockBase;
import com.emorn.bettercables.objects.blocks.connector.NetworkManager;
import com.emorn.bettercables.utils.IHasModel;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockCable extends BlockBase implements IHasModel
{
    private static final PropertyBool NORTH = PropertyBool.create("north");
    private static final PropertyBool EAST = PropertyBool.create("east");
    private static final PropertyBool SOUTH = PropertyBool.create("south");
    private static final PropertyBool WEST = PropertyBool.create("west");
    private static final PropertyBool UP = PropertyBool.create("up");
    private static final PropertyBool DOWN = PropertyBool.create("down");

    private static final AxisAlignedBB BASE_AABB = AxisAlignedBoundingBoxConverter.from(
        CableAxisAlignedBoundingBox.BASE
    );
    private static final AxisAlignedBB NORTH_CABLE_AABB = AxisAlignedBoundingBoxConverter.from(
        CableAxisAlignedBoundingBox.NORTH
    );
    private static final AxisAlignedBB EAST_CABLE_AABB = AxisAlignedBoundingBoxConverter.from(
        CableAxisAlignedBoundingBox.EAST
    );
    private static final AxisAlignedBB SOUTH_CABLE_AABB = AxisAlignedBoundingBoxConverter.from(
        CableAxisAlignedBoundingBox.SOUTH
    );
    private static final AxisAlignedBB WEST_CABLE_AABB = AxisAlignedBoundingBoxConverter.from(
        CableAxisAlignedBoundingBox.WEST
    );
    private static final AxisAlignedBB UP_CABLE_AABB = AxisAlignedBoundingBoxConverter.from(
        CableAxisAlignedBoundingBox.UP
    );
    private static final AxisAlignedBB DOWN_CABLE_AABB = AxisAlignedBoundingBoxConverter.from(
        CableAxisAlignedBoundingBox.DOWN
    );

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
    public AxisAlignedBB getBoundingBox(
        IBlockState state,
        IBlockAccess source,
        BlockPos pos
    )
    {
        EntityPlayer player = source instanceof World ? ((World) source).getClosestPlayer(
            pos.getX(),
            pos.getY(),
            pos.getZ(),
            10,
            false
        ) : null;
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
    public void addCollisionBoxToList(
        IBlockState state,
        World worldIn,
        BlockPos pos,
        AxisAlignedBB entityBox,
        List<AxisAlignedBB> collidingBoxes,
        @Nullable Entity entityIn,
        boolean isActualState
    )
    {
        IBlockState actualState = getActualState(state, worldIn, pos);
        List<AxisAlignedBB> allBoxes = retrieveAllBoxes(actualState);

        for (AxisAlignedBB box : allBoxes) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, box);
        }
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public void onBlockDestroyedByPlayer(
        World worldIn,
        BlockPos pos,
        IBlockState state
    )
    {
        super.onBlockDestroyedByPlayer(worldIn, pos, state);
        if (!worldIn.isRemote) {
            NetworkManager.reCalculateNetworksAround(pos, worldIn);
        }
    }

    @Override
    public void onBlockAdded(
        World worldIn,
        BlockPos pos,
        IBlockState state
    )
    {
        IBlockState actualState = getActualState(state, worldIn, pos);
        NetworkManager.mergeNetworks(worldIn, pos, findTotalConnections(actualState));

        if (!worldIn.isRemote) {
            worldIn.setBlockState(pos, state, 2);
        }
    }

    private int findTotalConnections(
        IBlockState actualState
    )
    {
        int totalConnections = 0;

        if (Boolean.TRUE.equals(actualState.getValue(NORTH))) {
            totalConnections++;
        }

        if (Boolean.TRUE.equals(actualState.getValue(EAST))) {
            totalConnections++;
        }

        if (Boolean.TRUE.equals(actualState.getValue(SOUTH))) {
            totalConnections++;
        }

        if (Boolean.TRUE.equals(actualState.getValue(WEST))) {
            totalConnections++;
        }

        if (Boolean.TRUE.equals(actualState.getValue(UP))) {
            totalConnections++;
        }

        if (Boolean.TRUE.equals(actualState.getValue(DOWN))) {
            totalConnections++;
        }

        return totalConnections;
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
    public void onBlockDestroyedByExplosion(
        World worldIn,
        BlockPos pos,
        Explosion explosionIn
    )
    {
        super.onBlockDestroyedByExplosion(worldIn, pos, explosionIn);
        if (worldIn.isRemote) {
            NetworkManager.reCalculateNetworksAround(pos, worldIn);
        }
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

    private List<AxisAlignedBB> retrieveAllBoxes(IBlockState state)
    {
        List<AxisAlignedBB> allBoxes = new ArrayList<>();

        allBoxes.add(BASE_AABB);

        if (this.hasConnection(state, NORTH)) {
            allBoxes.add(NORTH_CABLE_AABB);
        }

        if (this.hasConnection(state, EAST)) {
            allBoxes.add(EAST_CABLE_AABB);
        }

        if (this.hasConnection(state, SOUTH)) {
            allBoxes.add(SOUTH_CABLE_AABB);
        }

        if (this.hasConnection(state, WEST)) {
            allBoxes.add(WEST_CABLE_AABB);
        }

        if (this.hasConnection(state, UP)) {
            allBoxes.add(UP_CABLE_AABB);
        }

        if (this.hasConnection(state, DOWN)) {
            allBoxes.add(DOWN_CABLE_AABB);
        }

        return allBoxes;
    }

    private boolean getConnectionType(
        IBlockAccess world,
        BlockPos pos,
        EnumFacing facing
    )
    {
        IBlockState neighborState = world.getBlockState(pos.offset(facing));
        Block neighborBlock = neighborState.getBlock();

        return neighborBlock instanceof BlockConnector || neighborBlock instanceof BlockCable;
    }

    private boolean hasConnection(
        IBlockState state,
        PropertyBool facing
    )
    {
        return state.getValue(facing).equals(true);
    }

}