package com.emorn.bettercables.api.v1_12_2.common;

import com.emorn.bettercables.contract.IInventory;
import com.emorn.bettercables.contract.IItemHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public class Inventory implements IInventory
{
    private final net.minecraft.inventory.IInventory forgeInventory;
    private final TileEntity forgeInventoryEntity;

    public Inventory(net.minecraft.inventory.IInventory forgeInventory)
    {
        this.forgeInventory = forgeInventory;
        this.forgeInventoryEntity = (TileEntity) forgeInventory;
    }

    public void markDirty()
    {
        this.forgeInventory.markDirty();
    }

    public IItemHandler getItemHandler()
    {
        return new ItemHandler(
                forgeInventoryEntity.getCapability(
                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                null
            )
        );
    }
}
