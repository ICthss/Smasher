package com.icthss.smasher;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import com.icthss.smasher.client.ClientSetupBusSubscriber;



import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;


@Mod(Smasher.MODID)
public class Smasher {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "smasher";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public Smasher(IEventBus modEventBus, ModContainer modContainer) {
        // 注册总线
        modEventBus.addListener(this::commonSetup);
        com.icthss.smasher.item.ModItems.register(modEventBus);
        com.icthss.smasher.block.ModBlocks.BLOCKS.register(modEventBus);
        com.icthss.smasher.gui.ModMenuTypes.MENUS.register(modEventBus);
        com.icthss.smasher.recipe.ModRecipes.RECIPE_TYPES.register(modEventBus);
        com.icthss.smasher.recipe.ModRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        com.icthss.smasher.block_entities.ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        com.icthss.smasher.item.ModCreativeModeTabs.register(modEventBus);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ClientSetupBusSubscriber clientSubscriber = new ClientSetupBusSubscriber();
            modEventBus.addListener(clientSubscriber::registerScreens);
        }
        NeoForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("Smasher SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }
   
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
