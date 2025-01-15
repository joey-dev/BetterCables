package com.emorn.bettercables.utils.handlers;

import com.emorn.bettercables.objects.blocks.connector.ContainerConnector;
import com.emorn.bettercables.objects.blocks.connector.GuiConnector;
import com.emorn.bettercables.objects.blocks.connector.TileEntityConnector;
import com.emorn.bettercables.objects.blocks.machines.sintering.ContainerSinteringFurnace;
import com.emorn.bettercables.objects.blocks.machines.sintering.GuiSinteringFurnace;
import com.emorn.bettercables.objects.blocks.machines.sintering.TileEntitySinteringFurnace;
import com.emorn.bettercables.utils.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
    @Override
    public Object getServerGuiElement(
        int id,
        EntityPlayer player,
        World world,
        int x,
        int y,
        int z
    )
    {
        if (id == Reference.GUI_SINTERING_FURNACE) {
            return new ContainerSinteringFurnace(
                player.inventory,
                (TileEntitySinteringFurnace) world.getTileEntity(new BlockPos(x, y, z))
            );
        }

        if (id == Reference.GUI_CONNECTOR) {
            TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
            if (!(tileEntity instanceof TileEntityConnector)) {
                return null;
            }
            return new ContainerConnector(
                player.inventory,
                (TileEntityConnector) tileEntity
            );
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(
        int id,
        EntityPlayer player,
        World world,
        int x,
        int y,
        int z
    )
    {
        if (id == Reference.GUI_SINTERING_FURNACE) {
            return new GuiSinteringFurnace(
                player.inventory,
                (TileEntitySinteringFurnace) world.getTileEntity(new BlockPos(x, y, z))
            );
        }

        if (id == Reference.GUI_CONNECTOR) {
            return new GuiConnector(
                player.inventory,
                (TileEntityConnector) world.getTileEntity(new BlockPos(x, y, z))
            );
        }
        return null;
    }
}