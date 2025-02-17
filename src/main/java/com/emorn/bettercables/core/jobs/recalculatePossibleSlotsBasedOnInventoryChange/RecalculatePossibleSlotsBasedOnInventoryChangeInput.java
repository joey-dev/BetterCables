package com.emorn.bettercables.core.jobs.recalculatePossibleSlotsBasedOnInventoryChange;

import com.emorn.bettercables.api.v1_12_2.blocks.connector.ForgeTileEntityConnector;
import com.emorn.bettercables.core.common.Direction;
import com.emorn.bettercables.core.jobs.IJobInput;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class RecalculatePossibleSlotsBasedOnInventoryChangeInput implements IJobInput
{
    private final ForgeTileEntityConnector connector;
    private final IInventory neighborTileEntity;
    private final Direction direction;
    private final IBlockAccess world;
    private final BlockPos neighborPosition;

    public RecalculatePossibleSlotsBasedOnInventoryChangeInput(
        ForgeTileEntityConnector connector,
        IInventory neighborTileEntity,
        Direction direction,
        IBlockAccess world,
        BlockPos neighborPosition
    )
    {
        this.connector = connector;
        this.neighborTileEntity = neighborTileEntity;
        this.direction = direction;
        this.world = world;
        this.neighborPosition = neighborPosition;
    }

    @Override
    public String identifier()
    {
        return "x=" + connector.getPos().getX() + ",y=" + connector.getPos().getY() + ",z=" + connector.getPos().getZ() +
            " x=" + neighborPosition.getX() + ",y=" + neighborPosition.getY() + ",z=" + neighborPosition.getZ();
    }

    public ForgeTileEntityConnector connector()
    {
        return this.connector;
    }

    public IInventory neighborTileEntity()
    {
        return this.neighborTileEntity;
    }

    public Direction direction()
    {
        return this.direction;
    }

    public IBlockAccess world()
    {
        return this.world;
    }

    public BlockPos neighborPosition()
    {
        return this.neighborPosition;
    }
}
