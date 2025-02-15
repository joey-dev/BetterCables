package com.emorn.bettercables.contract.common;

public interface IInventory
{
    public void markDirty();
    public IItemHandler getItemHandler();
}
