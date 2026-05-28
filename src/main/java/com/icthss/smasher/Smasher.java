package com.icthss.smasher;

import org.slf4j.Logger;

import com.icthss.smasher.item.ModItems;
import com.mojang.logging.LogUtils;
import com.icthss.smasher.item.ModCreativeModeTabs;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Smasher.MODID)
public class Smasher {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "smasher";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    
    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Smasher(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        
        ModItems.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.HOLLOW_WOOD);
            event.accept(ModItems.HOLLOW_WOOD_COAL);
            event.accept(ModItems.SPRITE_BOTTLE);
            event.accept(ModItems.FAKE_SPRITE_BOTTLE);
            event.accept(ModItems.QIAOLEZI_PACKAGE);
            event.accept(ModItems.REAL_HOLLOW_WOOD);

        }
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(ModItems.QIAOLEZI_ORIGIN);
            event.accept(ModItems.QIAOLEZI);
            event.accept(ModItems.QIAOLEZI_DEEPEN);
            event.accept(ModItems.QIAOLEZI_VANILLA);
            event.accept(ModItems.QIAOLEZI_BLUEBERRY);
            event.accept(ModItems.QIAOLEZI_STRAWBERRY);
            event.accept(ModItems.QIAOLEZI_MATCHA);
            event.accept(ModItems.QIAOLEZI_ENERGY);
            event.accept(ModItems.QIAOLEZI_SPRITE);
            event.accept(ModItems.QIAOLEZI_FATAL);
            event.accept(ModItems.SPRITE);
            event.accept(ModItems.FAKE_SPRITE);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
