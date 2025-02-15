package com.emorn.bettercables.core.common;

import com.emorn.bettercables.contract.blocks.connector.IData;
import com.emorn.bettercables.contract.common.IItemStack;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class EmptyItemStack implements IItemStack
{
    @Override
    public boolean isEmpty()
    {
        return true;
    }

    @Override
    public int getCount()
    {
        return 0;
    }

    @Nullable
    public IData serialize()
    {
        return null;
    }
}
