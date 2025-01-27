package com.emorn.bettercables;

import com.emorn.bettercables.proxy.CommonProxy;
import com.emorn.bettercables.proxy.ModNetworkHandler;
import com.emorn.bettercables.utils.Reference;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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
    }

    @EventHandler
    public static void init(FMLInitializationEvent event)
    {
    }

    @EventHandler
    public static void postInit(FMLPostInitializationEvent event)
    {
    }
}
