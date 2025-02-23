package com.emorn.bettercables.api.v1_12_2.common;

import com.emorn.bettercables.contract.common.IInventory;
import com.emorn.bettercables.contract.common.IItemHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public class Inventory implements IInventory
{
    private final net.minecraft.inventory.IInventory forgeInventory;
    private final net.minecraft.tileentity.TileEntity forgeInventoryEntity;

    public Inventory(net.minecraft.inventory.IInventory forgeInventory)
    {
        this.forgeInventory = forgeInventory;
        this.forgeInventoryEntity = (net.minecraft.tileentity.TileEntity) forgeInventory;
    }

    public void markDirty()
    {
        //this.forgeInventory.markDirty();
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
