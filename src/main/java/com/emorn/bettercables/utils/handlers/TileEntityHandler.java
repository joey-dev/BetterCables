package com.emorn.bettercables.utils.handlers;

import com.emorn.bettercables.objects.api.forge.blocks.connector.TileEntityConnector;
import com.emorn.bettercables.utils.Reference;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class TileEntityHandler
{
    public static void registerTileEntities()
    {
        GameRegistry.registerTileEntity(
            TileEntityConnector.class,
            new ResourceLocation(Reference.MODID + ":connector")
        );
    }
}