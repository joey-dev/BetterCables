package com.emorn.bettercables.api.v1_12_2.blocks.connector;

import com.emorn.bettercables.Main;
import com.emorn.bettercables.api.v1_12_2.IHasModel;
import com.emorn.bettercables.api.v1_12_2.asyncEventBus.AsyncEventBus;
import com.emorn.bettercables.api.v1_12_2.blocks.AxisAlignedBoundingBoxConverter;
import com.emorn.bettercables.api.v1_12_2.blocks.BaseCable;
import com.emorn.bettercables.api.v1_12_2.blocks.cable.BlockCable;
import com.emorn.bettercables.api.v1_12_2.common.PositionInWorld;
import com.emorn.bettercables.api.v1_12_2.init.BlockInit;
import com.emorn.bettercables.contract.asyncEventBus.IAsyncEventBus;
import com.emorn.bettercables.core.blocks.cable.CableAxisAlignedBoundingBox;
import com.emorn.bettercables.core.blocks.connector.ConnectorAxisAlignedBoundingBox;
import com.emorn.bettercables.core.blocks.connector.network.ConnectorNetwork;
import com.emorn.bettercables.core.blocks.connector.network.NetworkManager;
import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSettings;
import com.emorn.bettercables.core.common.Direction;
import com.emorn.bettercables.core.common.Reference;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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
import java.util.*;


@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockConnector extends BaseCable implements IHasModel
{
    private static final AxisAlignedBB BASE_AABB = AxisAlignedBoundingBoxConverter.from(
        CableAxisAlignedBoundingBox.BASE
    );

    private static final AxisAlignedBB NORTH_CABLE_AABB = AxisAlignedBoundingBoxConverter.from(
        CableAxisAlignedBoundingBox.NORTH
    );
    private static final AxisAlignedBB NORTH_CABLE_CONNECTOR_AABB = AxisAlignedBoundingBoxConverter.from(
        ConnectorAxisAlignedBoundingBox.NORTH
    );

    private static final AxisAlignedBB EAST_CABLE_AABB = AxisAlignedBoundingBoxConverter.from(
        CableAxisAlignedBoundingBox.EAST
    );
    private static final AxisAlignedBB EAST_CABLE_CONNECTOR_AABB = AxisAlignedBoundingBoxConverter.from(
        ConnectorAxisAlignedBoundingBox.EAST
    );

    private static final AxisAlignedBB SOUTH_CABLE_AABB = AxisAlignedBoundingBoxConverter.from(
        CableAxisAlignedBoundingBox.SOUTH
    );
    private static final AxisAlignedBB SOUTH_CABLE_CONNECTOR_AABB = AxisAlignedBoundingBoxConverter.from(
        ConnectorAxisAlignedBoundingBox.SOUTH
    );

    private static final AxisAlignedBB WEST_CABLE_AABB = AxisAlignedBoundingBoxConverter.from(
        CableAxisAlignedBoundingBox.WEST
    );
    private static final AxisAlignedBB WEST_CABLE_CONNECTOR_AABB = AxisAlignedBoundingBoxConverter.from(
        ConnectorAxisAlignedBoundingBox.WEST
    );

    private static final AxisAlignedBB UP_CABLE_AABB = AxisAlignedBoundingBoxConverter.from(
        CableAxisAlignedBoundingBox.UP
    );
    private static final AxisAlignedBB UP_CABLE_CONNECTOR_AABB = AxisAlignedBoundingBoxConverter.from(
        ConnectorAxisAlignedBoundingBox.UP
    );

    private static final AxisAlignedBB DOWN_CABLE_AABB = AxisAlignedBoundingBoxConverter.from(
        CableAxisAlignedBoundingBox.DOWN
    );
    private static final AxisAlignedBB DOWN_CABLE_CONNECTOR_AABB = AxisAlignedBoundingBoxConverter.from(
        ConnectorAxisAlignedBoundingBox.DOWN
    );

    private static final PropertyEnum<ConnectionType> NORTH = PropertyEnum.create("north", ConnectionType.class);
    private static final PropertyEnum<ConnectionType> EAST = PropertyEnum.create("east", ConnectionType.class);
    private static final PropertyEnum<ConnectionType> SOUTH = PropertyEnum.create("south", ConnectionType.class);
    private static final PropertyEnum<ConnectionType> WEST = PropertyEnum.create("west", ConnectionType.class);
    private static final PropertyEnum<ConnectionType> UP = PropertyEnum.create("up", ConnectionType.class);
    private static final PropertyEnum<ConnectionType> DOWN = PropertyEnum.create("down", ConnectionType.class);

    private final Map<BlockPos, Boolean> foundCablePositions = new HashMap<>();
    private final NetworkManager networkManager;

    public BlockConnector(String name)
    {
        super(name);
        this.networkManager = NetworkManager.getInstance();
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
        boolean didMergeCurrentNetwork = this.networkManager.mergeNetworks(
            new com.emorn.bettercables.api.v1_12_2.common.World(worldIn),
            new PositionInWorld(pos.getX(), pos.getY(), pos.getZ()),
            findTotalConnections(getActualState(state, worldIn, pos))
        );
        if (!didMergeCurrentNetwork) {
            this.addNetwork(worldIn, pos, AsyncEventBus.getInstance());
        }
        if (!worldIn.isRemote) {
            worldIn.setBlockState(pos, state, 2);
        }
    }

    @Override
    @Nullable
    public RayTraceResult collisionRayTrace(
        IBlockState blockState,
        World worldIn,
        BlockPos pos,
        Vec3d start,
        Vec3d end
    )
    {
        IBlockState actualState = getActualState(blockState, worldIn, pos);
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
        IBlockState actualState = getActualState(state, worldIn, pos);
        Vec3d hitVec = new Vec3d(pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ);

        List<AxisAlignedBB> allBoxes = retrieveAllBoxes(actualState);

        AxisAlignedBB clickedBox = null;
        for (AxisAlignedBB box : allBoxes) {
            AxisAlignedBB expandedBox = box.offset(pos).grow(0.001);
            if (expandedBox.contains(hitVec)) {
                clickedBox = box;
                break;
            }
        }

        if (clickedBox == null) {
            return false;
        }
        return this.openGui(
            clickedBox,
            actualState,
            worldIn,
            pos,
            playerIn
        );
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
    {
        super.onNeighborChange(world, pos, neighbor);
        Block neighborBlock = world.getBlockState(neighbor).getBlock();

        if (!(neighborBlock instanceof BlockAir)) {
            return;
        }

        TileEntity tileEntity = world.getTileEntity(pos);
        if (!(tileEntity instanceof ForgeTileEntityConnector)) {
            return;
        }

        Direction direction;
        if (neighbor.getX() == pos.getX() + 1) {
            direction = Direction.EAST;
        } else if (neighbor.getX() + 1 == pos.getX()) {
            direction = Direction.WEST;
        } else if (neighbor.getY() + 1 == pos.getY()) {
            direction = Direction.DOWN;
        } else if (neighbor.getY() == pos.getY() + 1) {
            direction = Direction.UP;
        } else if (neighbor.getZ() == pos.getZ() + 1) {
            direction = Direction.SOUTH;
        } else if (neighbor.getZ() + 1 == pos.getZ()) {
            direction = Direction.NORTH;
        } else {
            return;
        }

        ForgeTileEntityConnector connector = (ForgeTileEntityConnector) tileEntity;
        ConnectorNetwork network = connector.getNetwork();
        ConnectorSettings connectorSettings = connector.settings(direction);

        connectorSettings.changeExtractEnabled(false);
        network.removeExtract(connectorSettings);

        connectorSettings.changeInsertEnabled(false);
        network.removeInsert(connectorSettings);
    }

    private boolean openGui(
        AxisAlignedBB clickedBox,
        IBlockState actualState,
        World worldIn,
        BlockPos pos,
        EntityPlayer playerIn
    )
    {
        EnumFacing facing = findClickedCable(clickedBox, actualState);
        if (facing == null) {
            return false;
        }

        return this.openGui(facing, worldIn, pos, playerIn);
    }

    @Nullable
    private EnumFacing findClickedCable(
        AxisAlignedBB clickedBox,
        IBlockState actualState
    )
    {
        if (this.hasClickedOn(clickedBox, actualState, NORTH_CABLE_AABB, NORTH_CABLE_CONNECTOR_AABB, NORTH)) {
            return EnumFacing.NORTH;
        }
        if (this.hasClickedOn(clickedBox, actualState, EAST_CABLE_AABB, EAST_CABLE_CONNECTOR_AABB, EAST)) {
            return EnumFacing.EAST;
        }
        if (this.hasClickedOn(clickedBox, actualState, SOUTH_CABLE_AABB, SOUTH_CABLE_CONNECTOR_AABB, SOUTH)) {
            return EnumFacing.SOUTH;
        }
        if (this.hasClickedOn(clickedBox, actualState, WEST_CABLE_AABB, WEST_CABLE_CONNECTOR_AABB, WEST)) {
            return EnumFacing.WEST;
        }
        if (this.hasClickedOn(clickedBox, actualState, UP_CABLE_AABB, UP_CABLE_CONNECTOR_AABB, UP)) {
            return EnumFacing.UP;
        }
        if (this.hasClickedOn(clickedBox, actualState, DOWN_CABLE_AABB, DOWN_CABLE_CONNECTOR_AABB, DOWN)) {
            return EnumFacing.DOWN;
        }

        return null;
    }

    private boolean openGui(
        EnumFacing facing,
        World worldIn,
        BlockPos pos,
        EntityPlayer playerIn
    )
    {
        if (facing.equals(EnumFacing.NORTH) && !worldIn.isRemote) {
            this.openGui(
                Reference.GUI_CONNECTOR_NORTH,
                worldIn,
                pos,
                playerIn
            );
            return true;
        }
        if (facing.equals(EnumFacing.EAST) && !worldIn.isRemote) {
            this.openGui(
                Reference.GUI_CONNECTOR_EAST,
                worldIn,
                pos,
                playerIn
            );
            return true;
        }
        if (facing.equals(EnumFacing.SOUTH) && !worldIn.isRemote) {
            this.openGui(
                Reference.GUI_CONNECTOR_SOUTH,
                worldIn,
                pos,
                playerIn
            );
            return true;
        }
        if (facing.equals(EnumFacing.WEST) && !worldIn.isRemote) {
            this.openGui(
                Reference.GUI_CONNECTOR_WEST,
                worldIn,
                pos,
                playerIn
            );
            return true;
        }
        if (facing.equals(EnumFacing.UP) && !worldIn.isRemote) {
            this.openGui(
                Reference.GUI_CONNECTOR_UP,
                worldIn,
                pos,
                playerIn
            );
            return true;
        }

        if (facing.equals(EnumFacing.DOWN) && !worldIn.isRemote) {
            this.openGui(
                Reference.GUI_CONNECTOR_DOWN,
                worldIn,
                pos,
                playerIn
            );
            return true;
        }

        return worldIn.isRemote;
    }

    private boolean hasClickedOn(
        AxisAlignedBB clickedBox,
        IBlockState actualState,
        AxisAlignedBB cableBox,
        AxisAlignedBB connectorBox,
        PropertyEnum<ConnectionType> direction
    )
    {
        return (clickedBox.equals(cableBox) || clickedBox.equals(connectorBox)) &&
            this.hasConnectionToInventory(actualState, direction);
    }

    private void openGui(
        int reference,
        World worldIn,
        BlockPos pos,
        EntityPlayer playerIn
    )
    {
        playerIn.openGui(
            Main.instance,
            reference,
            worldIn,
            pos.getX(),
            pos.getY(),
            pos.getZ()
        );
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
        return new ForgeTileEntityConnector();
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

    private List<AxisAlignedBB> retrieveBoxesFor(
        PropertyEnum<ConnectionType> direction,
        IBlockState state,
        AxisAlignedBB cableBox,
        AxisAlignedBB connectorBox
    )
    {
        List<AxisAlignedBB> allBoxes = new ArrayList<>();

        if (this.hasConnectionToInventory(state, direction)) {
            allBoxes.add(connectorBox);
            allBoxes.add(cableBox);

            return allBoxes;
        }

        if (this.hasConnection(state, direction)) {
            allBoxes.add(cableBox);
        }

        return allBoxes;
    }

    private boolean hasConnectionToInventory(
        IBlockState state,
        PropertyEnum<ConnectionType> facing
    )
    {
        return state.getValue(facing).toString().equals(ConnectionType.INVENTORY.toString());
    }

    private boolean hasConnection(
        IBlockState state,
        PropertyEnum<ConnectionType> facing
    )
    {
        return this.hasConnectionToConnector(state, facing)
            || this.hasConnectionToInventory(state, facing);
    }

    private boolean hasConnectionToConnector(
        IBlockState state,
        PropertyEnum<ConnectionType> facing
    )
    {
        return state.getValue(facing).toString().equals(ConnectionType.CONNECTOR.toString());
    }

    @Override
    protected AxisAlignedBB baseAABB()
    {
        return BASE_AABB;
    }

    @Override
    protected List<AxisAlignedBB> retrieveAllBoxes(IBlockState state)
    {
        List<AxisAlignedBB> allBoxes = new ArrayList<>();

        allBoxes.add(BASE_AABB);

        allBoxes.addAll(this.retrieveBoxesFor(NORTH, state, NORTH_CABLE_AABB, NORTH_CABLE_CONNECTOR_AABB));
        allBoxes.addAll(this.retrieveBoxesFor(EAST, state, EAST_CABLE_AABB, EAST_CABLE_CONNECTOR_AABB));
        allBoxes.addAll(this.retrieveBoxesFor(SOUTH, state, SOUTH_CABLE_AABB, SOUTH_CABLE_CONNECTOR_AABB));
        allBoxes.addAll(this.retrieveBoxesFor(WEST, state, WEST_CABLE_AABB, WEST_CABLE_CONNECTOR_AABB));
        allBoxes.addAll(this.retrieveBoxesFor(UP, state, UP_CABLE_AABB, UP_CABLE_CONNECTOR_AABB));
        allBoxes.addAll(this.retrieveBoxesFor(DOWN, state, DOWN_CABLE_AABB, DOWN_CABLE_CONNECTOR_AABB));

        return allBoxes;
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
    protected Block currentBlock()
    {
        return BlockInit.CONNECTOR;
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

    private int findTotalConnections(
        IBlockState state
    )
    {
        int totalConnections = 0;

        if (this.hasConnectionToConnector(state, NORTH)) {
            totalConnections++;
        }

        if (this.hasConnectionToConnector(state, EAST)) {
            totalConnections++;
        }

        if (this.hasConnectionToConnector(state, SOUTH)) {
            totalConnections++;
        }

        if (this.hasConnectionToConnector(state, WEST)) {
            totalConnections++;
        }

        if (this.hasConnectionToConnector(state, UP)) {
            totalConnections++;
        }

        if (this.hasConnectionToConnector(state, DOWN)) {
            totalConnections++;
        }

        return totalConnections;
    }

    private void addNetwork(
        World worldIn,
        BlockPos pos,
        IAsyncEventBus eventBus
    )
    {
        this.foundCablePositions.clear();
        this.foundCablePositions.put(pos, true);
        ForgeTileEntityConnector connector = (
            (ForgeTileEntityConnector) Objects.requireNonNull(
                worldIn.getTileEntity(pos)
            )
        );

        ConnectorNetwork network = this.findNetwork(worldIn, pos);
        if (network == null) {
            connector.setNetwork(this.networkManager.createNewNetwork(eventBus));
            this.foundCablePositions.clear();
            return;
        }

        connector.setNetwork(network);
        this.foundCablePositions.clear();
    }

    @Nullable
    private ConnectorNetwork findNetwork(
        World worldIn,
        BlockPos pos
    )
    {
        List<BlockPos> neighborBlockPositions = this.getPossibleConnectedBlocks(pos);

        for (BlockPos neighborBlockPosition : neighborBlockPositions) {
            if (this.foundCablePositions.containsKey(neighborBlockPosition)) {
                continue;
            }

            this.foundCablePositions.put(neighborBlockPosition, true);
            Block neighborBlock = worldIn.getBlockState(neighborBlockPosition).getBlock();

            if (!(neighborBlock instanceof BlockConnector) && !(neighborBlock instanceof BlockCable)) {
                continue;
            }

            if (neighborBlock instanceof BlockConnector) {
                return (
                    (ForgeTileEntityConnector) Objects.requireNonNull(
                        worldIn.getTileEntity(neighborBlockPosition)
                    )
                ).getNetwork();
            }
            ConnectorNetwork connectorNetwork = this.findNetwork(worldIn, neighborBlockPosition);
            if (connectorNetwork == null) {
                continue;
            }
            return connectorNetwork;
        }

        return null;
    }

    private List<BlockPos> getPossibleConnectedBlocks(
        BlockPos pos
    )
    {
        List<BlockPos> connectedBlocks = new ArrayList<>();
        connectedBlocks.add(new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1));
        connectedBlocks.add(new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ()));
        connectedBlocks.add(new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1));
        connectedBlocks.add(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ()));
        connectedBlocks.add(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()));
        connectedBlocks.add(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ()));

        return connectedBlocks;
    }
}