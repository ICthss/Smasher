package com.icthss.smasher.block_entities;

import com.icthss.smasher.Smasher;
import com.icthss.smasher.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.Supplier;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = 
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Smasher.MODID);

    public static final Supplier<BlockEntityType<SmasherBlockEntity>> SMASHER_BE_TYPE = 
        BLOCK_ENTITIES.register("smasher_be", () -> BlockEntityType.Builder.of(
            SmasherBlockEntity::new,
            new net.minecraft.world.level.block.Block[]{ ModBlocks.SMASHER_BLOCK.get() }
        ).build(null));
}
