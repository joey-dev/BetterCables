package com.emorn.bettercables.objects.items;

import com.emorn.bettercables.Main;
import com.emorn.bettercables.init.ItemInit;
import com.emorn.bettercables.proxy.ClientProxy;
import com.emorn.bettercables.utils.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel {
    public ItemBase(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabs.MATERIALS);

        ItemInit.ITEMS.add(this);
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
