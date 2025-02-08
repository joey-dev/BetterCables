package com.emorn.bettercables.contract;

import javax.annotation.Nonnull;


public interface IItemHandler
{
    public IItemStack insertItem(
        int slot,
        @Nonnull IItemStack stack,
        boolean simulate
    );

    public IItemStack extractItem(int slot, int amount, boolean simulate);
}
