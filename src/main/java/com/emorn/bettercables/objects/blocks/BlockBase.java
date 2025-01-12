
package com.emorn.bettercables.objects.blocks;

import com.emorn.bettercables.Main;
import com.emorn.bettercables.init.BlockInit;
import com.emorn.bettercables.init.ItemInit;
import com.emorn.bettercables.utils.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;

public class BlockBase extends Block implements IHasModel {
    public BlockBase(String name, Material material) {
        super(material);

        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabs.MATERIALS);

        BlockInit.BLOCKS.add(this);
        ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(ItemBlock.getItemFromBlock(this), 0, "inventory");
    }
}
