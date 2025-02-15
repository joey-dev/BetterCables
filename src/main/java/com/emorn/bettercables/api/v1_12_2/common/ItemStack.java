package com.emorn.bettercables.api.v1_12_2.common;

import com.emorn.bettercables.api.v1_12_2.blocks.connector.Data;
import com.emorn.bettercables.contract.blocks.connector.IData;
import com.emorn.bettercables.contract.common.IItemStack;

import javax.annotation.Nullable;

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

    @Nullable
    @Override
    public IData serialize()
    {
        return new Data(
            this.forgeItemStack.serializeNBT()
        );
    }
}
