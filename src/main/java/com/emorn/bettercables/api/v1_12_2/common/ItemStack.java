package com.emorn.bettercables.api.v1_12_2.common;

import com.emorn.bettercables.contract.IItemStack;

public class ItemStack implements IItemStack
{
    private final net.minecraft.item.ItemStack forgeItemStack;

    public ItemStack(net.minecraft.item.ItemStack forgeItemStack)
    {
        this.forgeItemStack = forgeItemStack;
    }

    public net.minecraft.item.ItemStack getForgeItemStack()
    {
        return forgeItemStack;
    }

    public boolean isEmpty()
    {
        return this.forgeItemStack.isEmpty();
    }

    @Override
    public int getCount()
    {
        return this.forgeItemStack.getCount();
    }
}
