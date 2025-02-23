package com.emorn.bettercables.core.blocks.connector.item_transfer;

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
        IItemHandler exportInventory,
        Integer slot,
        int amount
    )
    {
        IItemStack extracted;
        try {
            IItemStack triedToExtract = exportInventory.extractItem(slot, amount, true);

            if (triedToExtract.isEmpty()) {
                return new EmptyItemStack();
            }

            extracted = exportInventory.extractItem(slot, amount, false);
        } catch (ArrayIndexOutOfBoundsException e) {
            Logger.error("Failed to extract item from inventory.");
            return new EmptyItemStack();
        }

        return extracted;
    }

    public IItemStack insertItemIntoInventory(
        IItemHandler insertInventory,
        Integer slot,
        IItemStack items
    )
    {
        IItemStack itemsLeft;

        try {
            itemsLeft = insertInventory.insertItem(slot, items, false);
        } catch (ArrayIndexOutOfBoundsException e) {
            Logger.error("Failed to insert item from inventory.");
            return items;
        }

        return itemsLeft;
    }
}
