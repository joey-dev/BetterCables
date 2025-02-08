package com.emorn.bettercables.api.v1_12_2.common;

import com.emorn.bettercables.contract.IPositionInWorld;
import com.emorn.bettercables.core.common.Direction;
import net.minecraft.util.EnumFacing;

public class PositionInWorld implements IPositionInWorld
{
    private int x;
    private int y;
    private int z;

    public PositionInWorld(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getZ()
    {
        return z;
    }

    public IPositionInWorld up()
    {
        return this.up(1);
    }

    public IPositionInWorld up(int n)
    {
        return this.offset(Direction.UP, n);
    }

    public IPositionInWorld down()
    {
        return this.down(1);
    }

    public IPositionInWorld down(int n)
    {
        return this.offset(Direction.DOWN, n);
    }

    public IPositionInWorld north()
    {
        return this.north(1);
    }

    public IPositionInWorld north(int n)
    {
        return this.offset(Direction.NORTH, n);
    }

    public IPositionInWorld south()
    {
        return this.south(1);
    }

    public IPositionInWorld south(int n)
    {
        return this.offset(Direction.SOUTH, n);
    }

    public IPositionInWorld west()
    {
        return this.west(1);
    }

    public IPositionInWorld west(int n)
    {
        return this.offset(Direction.WEST, n);
    }

    public IPositionInWorld east()
    {
        return this.east(1);
    }

    public IPositionInWorld east(int n)
    {
        return this.offset(Direction.EAST, n);
    }

    public IPositionInWorld offset(Direction facing)
    {
        return this.offset(facing, 1);
    }

    public IPositionInWorld offset(Direction facing, int n)
    {
        if (n == 0) {
            return this;
        }

        EnumFacing enumFacing  = EnumFacing.byName(facing.name());
        return new PositionInWorld(
            this.getX() + enumFacing.getFrontOffsetX() * n,
            this.getY() + enumFacing.getFrontOffsetY() * n,
            this.getZ() + enumFacing.getFrontOffsetZ() * n
        );
    }
}
