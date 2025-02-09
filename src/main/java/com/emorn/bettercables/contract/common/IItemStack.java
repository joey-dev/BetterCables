package com.emorn.bettercables.contract.common;

import com.emorn.bettercables.contract.blocks.connector.IData;

import javax.annotation.Nullable;

public interface IItemStack
{
    boolean isEmpty();

    public int getCount();

    @Nullable
    public IData serialize();
}
