package com.icthss.smasher.item;

import net.neoforged.bus.api.IEventBus;
import com.icthss.smasher.Smasher;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.icthss.smasher.block.ModBlocks;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = 
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Smasher.MODID);


    public static final Supplier<CreativeModeTab> SMASHER_TAB = 
        CREATIVE_MODE_TABS.register("smasher", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItems.QIAOLEZI.get()))
            .title(Component.translatable("itemGroup.smasher"))
            .displayItems((parameters, output) -> {
                output.accept(ModItems.QIAOLEZI);
                output.accept(ModItems.QIAOLEZI_ORIGIN);
                output.accept(ModItems.QIAOLEZI_DEEPEN);
                output.accept(ModItems.QIAOLEZI_VANILLA);
                output.accept(ModItems.QIAOLEZI_BLUEBERRY);
                output.accept(ModItems.QIAOLEZI_STRAWBERRY);
                output.accept(ModItems.QIAOLEZI_MATCHA);
                output.accept(ModItems.QIAOLEZI_ENERGY);
                output.accept(ModItems.QIAOLEZI_SPRITE);
                output.accept(ModItems.QIAOLEZI_FATAL);
                output.accept(ModItems.SPRITE);
                output.accept(ModItems.SPRITE_BOTTLE);
                output.accept(ModItems.FAKE_SPRITE);
                output.accept(ModItems.FAKE_SPRITE_BOTTLE);
                output.accept(ModItems.QIAOLEZI_PACKAGE);
                output.accept(ModItems.XUEWANG_HELMET);
                output.accept(ModItems.XUEWANG_CHESTPLATE);
                output.accept(ModItems.XUEWANG_LEGGINGS);
                output.accept(ModItems.XUEWANG_BOOTS);
                output.accept(ModItems.Sprite_HELMET);
                output.accept(ModItems.Sprite_CHESTPLATE);
                output.accept(ModItems.Sprite_LEGGINGS);
                output.accept(ModItems.Sprite_BOOTS);
                output.accept(ModItems.qiaolezi_HELMET);
                output.accept(ModItems.qiaolezi_CHESTPLATE);
                output.accept(ModItems.qiaolezi_LEGGINGS);
                output.accept(ModItems.qiaolezi_BOOTS);
                output.accept(ModItems.Snow_HELMET);
                output.accept(ModItems.Snow_CHESTPLATE);
                output.accept(ModItems.Snow_LEGGINGS);
                output.accept(ModItems.Snow_BOOTS);
                output.accept(ModItems.SNOW_FRAGMENT);
                output.accept(ModItems.SNOW_CORE);
                output.accept(ModBlocks.SMASHER_BLOCK);
                output.accept(ModItems.EMPTY_SNOW_CORE);
                output.accept(ModItems.SNOW_CORE_BASE);
                output.accept(ModItems.SNOW_CORE_STABLELIZER);
                output.accept(ModItems.STRENGTHED_STICK);
                output.accept(ModItems.EMPTY_XUEWANG_HELMET);
                output.accept(ModItems.EMPTY_XUEWANG_CHESTPLATE);
                output.accept(ModItems.EMPTY_XUEWANG_LEGGINGS);
                output.accept(ModItems.EMPTY_XUEWANG_BOOTS);
            })
            .build()
        );
    public static final Supplier<CreativeModeTab> HOLLOW_WOOD_TAB =
        CREATIVE_MODE_TABS.register("hollow_wood", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItems.HOLLOW_WOOD.get()))
            .title(Component.translatable("itemGroup.hollow_wood"))
            .displayItems((parameters, output) -> {
                output.accept(ModItems.HOLLOW_WOOD);
                output.accept(ModItems.HOLLOW_WOOD_COAL);
                output.accept(ModItems.REAL_HOLLOW_WOOD);
            })
            .build()
        );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}