package com.icthss.smasher.item;

import com.icthss.smasher.Smasher;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Smasher.MODID);

    public static final DeferredItem<Item> HOLLOW_WOOD = ITEMS.register("hollow_wood", () -> new Item(new Item.Properties()));

}