package com.emorn.bettercables.objects.blocks.machines.sintering;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerSinteringFurnace extends Container
{
    private final TileEntitySinteringFurnace tileEntity;
    private int cookTime, totalCookTime, burnTime, currentBurnTime;

    public ContainerSinteringFurnace(
        InventoryPlayer player,
        TileEntitySinteringFurnace tileEntity
    )
    {
        this.tileEntity = tileEntity;

        IItemHandler handler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        // x0 y0 = top left of gui
        this.addSlotToContainer(new SlotItemHandler(handler, 0, 26, 11));
        this.addSlotToContainer(new SlotItemHandler(handler, 1, 26, 59));
        this.addSlotToContainer(new SlotItemHandler(handler, 2, 7, 35));
        this.addSlotToContainer(new SlotItemHandler(handler, 3, 81, 36));

        for(int y = 0; y < 3; y++)
        {
            for(int x = 0; x < 9; x++)
            {
                this.addSlotToContainer(new Slot(player, x + y*9 + 9, 8 + x*18, 84 + y*18));
            }
        }

        for(int x = 0; x < 9; x++)
        {
            this.addSlotToContainer(new Slot(player, x, 8 + x * 18, 142));
        }
    }

    @Override
    public ItemStack transferStackInSlot(
        EntityPlayer playerIn,
        int index
    )
    {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = (Slot) this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();

            if (index == 3) {
                if (!this.mergeItemStack(stack1, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(stack1, stack);
            } else if (index != 2 && index != 1 && index != 0) {
                Slot slot1 = (Slot) this.inventorySlots.get(index + 1);

                if (!SinteringFurnaceRecipes.getInstance().getSinteringResult(stack1, slot1.getStack()).isEmpty()) {
                    if (!this.mergeItemStack(stack1, 0, 2, false)) {
                        return ItemStack.EMPTY;
                    } else if (TileEntitySinteringFurnace.isItemFuel(stack1)) {
                        if (!this.mergeItemStack(stack1, 2, 3, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (TileEntitySinteringFurnace.isItemFuel(stack1)) {
                        if (!this.mergeItemStack(stack1, 2, 3, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (TileEntitySinteringFurnace.isItemFuel(stack1)) {
                        if (!this.mergeItemStack(stack1, 2, 3, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (index >= 4 && index < 31) {
                        if (!this.mergeItemStack(stack1, 31, 40, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (index >= 31 && index < 40 && !this.mergeItemStack(stack1, 4, 31, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.mergeItemStack(stack1, 4, 40, false)) {
                return ItemStack.EMPTY;
            }
            if (stack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();

            }
            if (stack1.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, stack1);
        }
        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(
        int id,
        int data
    )
    {
        this.tileEntity.setField(id, data);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.tileEntity.isUsableByPlayer(playerIn);
    }
}
