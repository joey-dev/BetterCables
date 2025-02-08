package com.emorn.bettercables.contract;

import com.emorn.bettercables.core.common.Direction;

public interface IPositionInWorld
{
    public int getX();

    public int getY();

    public int getZ();

    public IPositionInWorld north();
    public IPositionInWorld south();
    public IPositionInWorld east();
    public IPositionInWorld west();
    public IPositionInWorld up();
    public IPositionInWorld down();
    public IPositionInWorld offset(Direction facing);
    public IPositionInWorld offset(Direction facing, int n);
}
