package com.emorn.bettercables.api.v1_12_2.handlers;

import com.emorn.bettercables.api.v1_12_2.blocks.connector.ForgeTileEntityConnector;
import com.emorn.bettercables.core.common.Reference;
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
            ForgeTileEntityConnector.class,
            new ResourceLocation(Reference.MODID + ":connector")
        );
    }
}