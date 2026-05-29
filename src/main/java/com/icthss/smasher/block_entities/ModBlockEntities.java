package com.icthss.smasher.block_entities;

import com.icthss.smasher.Smasher;
import com.icthss.smasher.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.Supplier;

public class ModBlockEntities {
    // 1. 声明独立的延迟注册表
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = 
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Smasher.MODID);

    // 2. 🟢 1.21.1 终极彻底解耦写法：
    // 不要在 Builder.of 里直接调用 ModBlocks.SMASHER_BLOCK.get()。
    // 我们将其包进一个延迟运行的 Lambda 表达式，从而切断游戏启动时去调用方块静态块的时机！
    public static final Supplier<BlockEntityType<SmasherBlockEntity>> SMASHER_BE_TYPE = 
        BLOCK_ENTITIES.register("smasher_be", () -> BlockEntityType.Builder.of(
            SmasherBlockEntity::new,
            // 🌟 核心突破：通过包装，让这一行在游戏启动完全结束、开始跑世界方块映射时才执行读取
            new net.minecraft.world.level.block.Block[]{ ModBlocks.SMASHER_BLOCK.get() }
        ).build(null));
}
