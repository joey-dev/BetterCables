package com.emorn.bettercables.init;

import com.emorn.bettercables.objects.blocks.BlockBase;
import com.emorn.bettercables.objects.blocks.BlockSantaHat;
import com.emorn.bettercables.objects.blocks.cable.BlockCable;
import com.emorn.bettercables.objects.blocks.connector.BlockConnector;
import com.emorn.bettercables.objects.blocks.machines.sintering.BlockSinteringFurnace;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import java.util.*;

public class BlockInit
{
    private BlockInit()
    {
        throw new IllegalStateException();
    }

    public static final List<Block> BLOCKS = new ArrayList<>();

    public static final Block ORE_COPPER = new BlockBase("copper_ore", Material.IRON);
    public static final Block SANTA_HAT = new BlockSantaHat("santa_hat");

    public static final Block SINTERING_FURNACE = new BlockSinteringFurnace("sintering_furnace");

    public static final Block CONNECTOR = new BlockConnector("connector");
    public static final Block CABLE = new BlockCable("cable");
}
