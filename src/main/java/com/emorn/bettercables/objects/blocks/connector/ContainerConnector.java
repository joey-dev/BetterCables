package com.emorn.bettercables.objects.blocks.connector;

import com.emorn.bettercables.objects.api.forge.blocks.connector.TileEntityConnector;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ContainerConnector extends Container
{
    private final TileEntityConnector tileEntity;

    public ContainerConnector(
        InventoryPlayer player,
        TileEntityConnector tileEntity
    )
    {
        this.tileEntity = tileEntity;

        addPlayerInventorySlots(player);
    }

    private void addPlayerInventorySlots(InventoryPlayer player)
    {
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlotToContainer(new Slot(player, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }

        for (int x = 0; x < 9; x++) {
            this.addSlotToContainer(new Slot(player, x, 8 + x * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.tileEntity.isUsableByPlayer(playerIn);
    }

}
