package com.emorn.bettercables.core.blocks.connector.itemTransfer;

import com.emorn.bettercables.contract.common.IInventory;
import com.emorn.bettercables.contract.common.IItemHandler;
import com.emorn.bettercables.contract.common.IItemStack;
import com.emorn.bettercables.core.common.EmptyItemStack;
import com.emorn.bettercables.core.common.Logger;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class InventoryService
{
    public IItemStack extractItemFromInventory(
        IInventory inventory,
        Integer slot,
        int amount
    )
    {
        IItemHandler exportInventory = inventory.getItemHandler();

        if (exportInventory == null) {
            Logger.error("Failed to get inventory inventory.");
            return new EmptyItemStack();
        }

        IItemStack extracted;
        try {
            // todo might want to check with simulate on first?
            extracted = exportInventory.extractItem(slot, amount, false);
        } catch (ArrayIndexOutOfBoundsException e) {
            Logger.error("Failed to extract item from inventory.");
            return new EmptyItemStack();
        }

        if (!extracted.isEmpty()) {
            inventory.markDirty();
        }

        return extracted;
    }

    public IItemStack insertItemIntoInventory(
        IInventory inventory,
        Integer slot,
        IItemStack items
    )
    {
        IItemHandler insertInventory = inventory.getItemHandler();

        if (insertInventory == null) {
            Logger.error("Failed to get inventory.");
            return new EmptyItemStack();
        }

        IItemStack itemsLeft;

        try {
            itemsLeft = insertInventory.insertItem(slot, items, false);
            /**
             * todo does not auto update when
             * there was a large chest with full slots
             * large chest is changed to small chest
             */
        } catch (ArrayIndexOutOfBoundsException e) {
            Logger.error("Failed to insert item from inventory.");
            return items;
        }

        if (itemsLeft.getCount() != items.getCount()) {
            inventory.markDirty();
        }

        return itemsLeft;
    }
}
