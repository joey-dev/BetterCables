package com.emorn.bettercables.api.v1_12_2.common;

import com.emorn.bettercables.contract.common.IItemHandler;
import com.emorn.bettercables.contract.common.IItemStack;

import javax.annotation.Nonnull;

public class ItemHandler implements IItemHandler
{
    private final net.minecraftforge.items.IItemHandler forgeItemHandler;

    public ItemHandler(net.minecraftforge.items.IItemHandler forgeItemHandler)
    {
        this.forgeItemHandler = forgeItemHandler;
    }

    public IItemStack insertItem(
        int slot,
        @Nonnull IItemStack stack,
        boolean simulate
    )
    {
        ItemStack itemStack = (ItemStack) stack;

        return new ItemStack(
            this.forgeItemHandler.insertItem(
                slot,
                itemStack.getForgeItemStack(),
                simulate
            )
        );
    }

    public IItemStack extractItem(
        int slot,
        int amount,
        boolean simulate
    )
    {
        return new ItemStack(
            this.forgeItemHandler.extractItem(slot, amount, simulate)
        );
    }

    @Override
    public int slotCount()
    {
        return this.forgeItemHandler.getSlots();
    }
}
