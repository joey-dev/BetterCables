package com.emorn.bettercables;

import com.emorn.bettercables.api.v1_12_2.proxy.CommonProxy;
import com.emorn.bettercables.api.v1_12_2.proxy.ModNetworkHandler;
import com.emorn.bettercables.core.common.Reference;
import com.emorn.bettercables.api.v1_12_2.asyncEventBus.AsyncEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class Main
{
    @Instance
    public static Main instance;

    @SidedProxy(clientSide = Reference.CLIENT, serverSide = Reference.COMMON)
    public static CommonProxy proxy;

    @EventHandler
    public static void preInit(FMLPreInitializationEvent event)
    {
        ModNetworkHandler.registerMessages();
        AsyncEventBus.getInstance();
    }

    @EventHandler
    public static void init(FMLInitializationEvent event)
    {
    }

    @EventHandler
    public static void postInit(FMLPostInitializationEvent event)
    {
    }

    @EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) {
        AsyncEventBus.getInstance().shutdownNow();
    }
}
