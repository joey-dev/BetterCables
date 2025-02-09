package com.emorn.bettercables.api.v1_12_2.common;

import com.emorn.bettercables.contract.common.IBlock;
import com.emorn.bettercables.objects.api.forge.blocks.cable.BlockCable;
import com.emorn.bettercables.objects.api.forge.blocks.connector.BlockConnector;
import com.emorn.bettercables.objects.api.forge.common.BaseCable;

public class Block implements IBlock
{
    private final net.minecraft.block.Block forgeBlock;

    public Block(net.minecraft.block.Block forgeBlock)
    {
        this.forgeBlock = forgeBlock;
    }

    @Override
    public boolean isBaseCable()
    {
        return this.forgeBlock instanceof BaseCable;
    }

    @Override
    public boolean isBlockConnector()
    {
        return this.forgeBlock instanceof BlockConnector;
    }

    @Override
    public boolean isBlockCable()
    {
        return this.forgeBlock instanceof BlockCable;
    }
}
