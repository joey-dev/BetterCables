package com.emorn.bettercables.api.v1_12_2.proxy;

import com.emorn.bettercables.api.v1_12_2.gui.PacketUpdateConnector;
import com.emorn.bettercables.core.common.Reference;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ModNetworkHandler
{
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID);
    private static int id = 0;

    private ModNetworkHandler()
    {
        throw new IllegalStateException();
    }

    public static void registerMessages()
    {
        INSTANCE.registerMessage(
            PacketUpdateConnector.Handler.class,
            PacketUpdateConnector.class,
            id++,
            Side.SERVER
        );
    }
}
