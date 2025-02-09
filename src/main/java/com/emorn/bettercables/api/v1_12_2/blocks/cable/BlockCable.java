package com.emorn.bettercables.api.v1_12_2.blocks.cable;

import com.emorn.bettercables.api.v1_12_2.blocks.BaseCable;
import com.emorn.bettercables.api.v1_12_2.blocks.connector.BlockConnector;
import com.emorn.bettercables.api.v1_12_2.common.PositionInWorld;
import com.emorn.bettercables.core.blocks.cable.CableAxisAlignedBoundingBox;
import com.emorn.bettercables.core.blocks.connector.network.NetworkManager;
import com.emorn.bettercables.api.v1_12_2.init.BlockInit;
import com.emorn.bettercables.objects.api.forge.common.AxisAlignedBoundingBoxConverter;
import com.emorn.bettercables.utils.IHasModel;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockCable extends BaseCable implements IHasModel
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
        super(name);
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
    public void onBlockAdded(
        World worldIn,
        BlockPos pos,
        IBlockState state
    )
    {
        IBlockState actualState = getActualState(state, worldIn, pos);
        NetworkManager.mergeNetworks(
            new com.emorn.bettercables.api.v1_12_2.common.World(worldIn),
            new PositionInWorld(pos.getX(), pos.getY(), pos.getZ()),
            findTotalConnections(actualState)
        );

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

    @Override
    protected List<AxisAlignedBB> retrieveAllBoxes(IBlockState state)
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

    @Override
    protected AxisAlignedBB baseAABB()
    {
        return BASE_AABB;
    }

    @Override
    protected Block currentBlock()
    {
        return BlockInit.CABLE;
    }

    private boolean hasConnection(
        IBlockState state,
        PropertyBool facing
    )
    {
        return state.getValue(facing).equals(true);
    }

}