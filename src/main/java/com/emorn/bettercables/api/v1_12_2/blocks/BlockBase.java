package com.emorn.bettercables.api.v1_12_2.blocks;

import com.emorn.bettercables.Main;
import com.emorn.bettercables.api.v1_12_2.init.BlockInit;
import com.emorn.bettercables.api.v1_12_2.init.ItemInit;
import com.emorn.bettercables.api.v1_12_2.IHasModel;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockBase extends Block implements IHasModel
{
    public BlockBase(
        String name,
        Material material
    )
    {
        super(material);

        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabs.MATERIALS);

        BlockInit.BLOCKS.add(this);
        if (this.getRegistryName() != null) {
            ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
        }
    }

    @Override
    public void registerModels()
    {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}
