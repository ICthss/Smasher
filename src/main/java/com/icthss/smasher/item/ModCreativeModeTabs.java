package com.icthss.smasher.item;

import com.icthss.smasher.Smasher;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = 
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Smasher.MODID);


    public static final Supplier<CreativeModeTab> SMASHER_TAB = 
        CREATIVE_MODE_TABS.register("smasher", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItems.HOLLOW_WOOD.get()))
            .title(Component.translatable("itemGroup.smasher"))
            .displayItems((parameters, output) -> {
                output.accept(ModItems.HOLLOW_WOOD);
                output.accept(ModItems.HOLLOW_WOOD_COAL);
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
            })
            .build()
        );
}