package com.icthss.smasher.item;

import com.icthss.smasher.Smasher;
import com.icthss.smasher.item.ModFuelItems;
import com.icthss.smasher.item.ModHarmfulFoodItem;
import com.icthss.smasher.item.ModFoods;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Smasher.MODID);

    public static final DeferredItem<Item> HOLLOW_WOOD = 
        ITEMS.register("hollow_wood", () -> new ModFuelItems(new Item.Properties(),1600));

    public static final DeferredItem<Item> HOLLOW_WOOD_COAL = 
        ITEMS.register("hollow_wood_coal", () -> new ModFuelItems(new Item.Properties(),3200));

    public static final DeferredItem<Item> QIAOLEZI_ORIGIN = 
        ITEMS.register("qiaolezi_origin", () -> new ModHarmfulFoodItem(new Item.Properties().food(ModFoods.QIAOLEZI_ORIGIN),ResourceLocation.fromNamespaceAndPath(Smasher.MODID, "qiaolezi_origin_penalty"), 2.0D));

    public static final DeferredItem<Item> QIAOLEZI = 
        ITEMS.register("qiaolezi", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> QIAOLEZI_DEEPEN =
        ITEMS.register("qiaolezi_deepen", () -> new ModHarmfulFoodItem(new Item.Properties().food(ModFoods.QIAOLEZI_DEEPEN),ResourceLocation.fromNamespaceAndPath(Smasher.MODID, "qiaolezi_deepen_penalty"), 2.0D ));

    public static final DeferredItem<Item> QIAOLEZI_VANILLA =
        ITEMS.register("qiaolezi_vanilla", () -> new ModHarmfulFoodItem(new Item.Properties().food(ModFoods.QIAOLEZI_VANILLA),ResourceLocation.fromNamespaceAndPath(Smasher.MODID, "qiaolezi_vanilla_penalty"), 2.0D));

    public static final DeferredItem<Item> QIAOLEZI_BLUEBERRY =
        ITEMS.register("qiaolezi_blueberry", () -> new ModHarmfulFoodItem(new Item.Properties().food(ModFoods.QIAOLEZI_BLUEBERRY),ResourceLocation.fromNamespaceAndPath(Smasher.MODID, "qiaolezi_blueberry_penalty"), 2.0D));

    public static final DeferredItem<Item> QIAOLEZI_STRAWBERRY =
        ITEMS.register("qiaolezi_strawberry", () -> new ModHarmfulFoodItem(new Item.Properties().food(ModFoods.QIAOLEZI_STRAWBERRY),ResourceLocation.fromNamespaceAndPath(Smasher.MODID, "qiaolezi_strawberry_penalty"), 2.0D));

    public static final DeferredItem<Item> QIAOLEZI_MATCHA =
        ITEMS.register("qiaolezi_matcha", () -> new ModHarmfulFoodItem(new Item.Properties().food(ModFoods.QIAOLEZI_MATCHA),ResourceLocation.fromNamespaceAndPath(Smasher.MODID, "qiaolezi_matcha_penalty"), 2.0D));

    public static final DeferredItem<Item> QIAOLEZI_ENERGY =
        ITEMS.register("qiaolezi_energy", () -> new ModHarmfulFoodItem(new Item.Properties().food(ModFoods.QIAOLEZI_ENERGY),ResourceLocation.fromNamespaceAndPath(Smasher.MODID, "qiaolezi_energy_penalty"), 2.0D));

    public static final DeferredItem<Item> QIAOLEZI_SPRITE =
        ITEMS.register("qiaolezi_sprite", () -> new ModHarmfulFoodItem(new Item.Properties().food(ModFoods.QIAOLEZI_SPRITE),ResourceLocation.fromNamespaceAndPath(Smasher.MODID, "qiaolezi_sprite_penalty"), 2.0D));

    public static final DeferredItem<Item> QIAOLEZI_FATAL = 
        ITEMS.register("qiaolezi_fatal", () -> new ModHarmfulFoodItem(new Item.Properties().food(ModFoods.QIAOLEZI_FATAL),ResourceLocation.fromNamespaceAndPath(Smasher.MODID, "qiaolezi_fatal_penalty"), 2.0D));

    public static final DeferredItem<Item> SPRITE = 
        ITEMS.register("sprite", () -> new Item(new Item.Properties().food(ModFoods.SPRITE)));

    public static final DeferredItem<Item> SPRITE_BOTTLE = 
        ITEMS.register("sprite_bottle", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> FAKE_SPRITE = 
        ITEMS.register("fake_sprite", () -> new Item(new Item.Properties().food(ModFoods.FAKE_SPRITE)));

    public static final DeferredItem<Item> FAKE_SPRITE_BOTTLE = 
        ITEMS.register("fake_sprite_bottle", () -> new Item(new Item.Properties()));
        
    public static final DeferredItem<Item> QIAOLEZI_PACKAGE = 
        ITEMS.register("qiaolezi_package", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> REAL_HOLLOW_WOOD =
        ITEMS.register("real_hollow_wood", () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}