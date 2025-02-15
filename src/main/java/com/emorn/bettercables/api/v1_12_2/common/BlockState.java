package com.emorn.bettercables.api.v1_12_2.common;

import com.emorn.bettercables.contract.common.IBlock;
import com.emorn.bettercables.contract.common.IBlockState;

public class BlockState implements IBlockState
{
    private final net.minecraft.block.state.IBlockState forgeBlockState;

    public BlockState(net.minecraft.block.state.IBlockState forgeBlockState)
    {
        this.forgeBlockState = forgeBlockState;
    }

    @Override
    public IBlock getBlock()
    {
        return new Block(
            this.forgeBlockState.getBlock()
        );
    }
}
