package com.emorn.bettercables.init;

import com.emorn.bettercables.objects.blocks.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import java.util.*;

public class BlockInit {
    public static final List<Block> BLOCKS = new ArrayList<Block>();

    public static final Block ORE_COPPER = new BlockBase("copper_ore", Material.IRON);
}
