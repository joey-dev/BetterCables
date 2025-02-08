package com.emorn.bettercables.contract;

public interface IInventory
{
    public void markDirty();
    public IItemHandler getItemHandler();
}
