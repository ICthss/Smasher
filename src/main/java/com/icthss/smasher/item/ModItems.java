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

    public static final DeferredItem<Item> QIAOLEZI_ORIGIN = 
        ITEMS.register("qiaolezi_origin", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> AIRPODS = 
        ITEMS.register("airpods", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> QIAOLEZI = 
        ITEMS.register("qiaolezi", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> MACBOOK = 
        ITEMS.register("macbook", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> QIAOLEZI_DEEPEN =
        ITEMS.register("qiaolezi_deepen", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> QIAOLEZI_VANILLA =
        ITEMS.register("qiaolezi_vanilla", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> QIAOLEZI_BLUEBERRY =
        ITEMS.register("qiaolezi_blueberry", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> QIAOLEZISTRAWBERRY =
        ITEMS.register("qiaolezi_strawberry", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> QIAOLEZI_MATCHA =
        ITEMS.register("qiaolezi_matcha", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> QIAOLEZI_ENERGY =
        ITEMS.register("qiaolezi_energy", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> QIAOLEZI_SPRITE =
        ITEMS.register("qiaolezi_sprite", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> QIAOLEZI_FATAL = 
        ITEMS.register("qiaolezi_fatal", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SPRITE = 
        ITEMS.register("sprite", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SPRITE_BOTTLE = 
        ITEMS.register("sprite_bottle", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> FAKE_SPRITE = 
        ITEMS.register("fake_sprite", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> FAKE_SPRITE_BOTTLE = 
        ITEMS.register("fake_sprite_bottle", () -> new Item(new Item.Properties()));
        

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}