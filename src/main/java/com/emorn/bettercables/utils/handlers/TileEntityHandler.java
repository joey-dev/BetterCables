package com.emorn.bettercables.utils.handlers;

import com.emorn.bettercables.objects.blocks.machines.sintering.TileEntitySinteringFurnace;
import com.emorn.bettercables.utils.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityHandler
{
    public static void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TileEntitySinteringFurnace.class, new ResourceLocation(Reference.MODID + ":sintering_furnace"));
        System.out.println("Tile entities registered");
    }
}