package com.emorn.bettercables.proxy;

import com.emorn.bettercables.objects.blocks.connector.PacketUpdateConnector;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ModNetworkHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID);

    public static void registerMessages() {
        int id = 0;

        INSTANCE.registerMessage(
            PacketUpdateConnector.Handler.class,
            PacketUpdateConnector.class,
            id++,
            Side.SERVER
        );
    }
}
