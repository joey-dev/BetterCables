package com.emorn.bettercables.contract.common;

public interface IWorld
{
    public ITileEntity getTileEntity(IPositionInWorld position);

    public IBlockState getBlockState(IPositionInWorld position);
}
