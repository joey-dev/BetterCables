package com.emorn.bettercables.init;

import com.emorn.bettercables.objects.blocks.BlockBase;
import com.emorn.bettercables.objects.blocks.BlockSantaHat;
import com.emorn.bettercables.objects.blocks.machines.sintering.BlockSinteringFurnace;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import java.util.*;

public class BlockInit
{
    public static final List<Block> BLOCKS = new ArrayList<Block>();

    public static final Block ORE_COPPER = new BlockBase("copper_ore", Material.IRON);
    public static final Block SANTA_HAT = new BlockSantaHat("santa_hat");

    public static final Block SINTERING_FURNACE = new BlockSinteringFurnace("sintering_furnace");
}
