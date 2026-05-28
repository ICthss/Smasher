package com.icthss.smasher.item;

import com.icthss.smasher.Smasher;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Smasher.MODID);

    public static final DeferredItem<Item> HOLLOW_WOOD = 
        ITEMS.register("hollow_wood", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> HOLLOW_WOOD_COAL = 
        ITEMS.register("hollow_wood_coal", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> QIAOLEZI = 
        ITEMS.register("qiaolezi", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> AIRPODS = 
        ITEMS.register("airpods", () -> new Item(new Item.Properties()));
        

     public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}