package com.icthss.smasher.block;

import com.icthss.smasher.Smasher;
import com.icthss.smasher.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS = 
        DeferredRegister.createBlocks(Smasher.MODID);

    public static final DeferredBlock<Block> SMASHER_BLOCK = 
        registerBlocks("smasher_block", () -> new SmasherBlock(
                BlockBehaviour.Properties.of()
                        .strength(1.0f)
        ));


    private static <T extends Block> void registerBlockItems(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
    
    private static <T extends Block> DeferredBlock<T> registerBlocks(String name, Supplier<T> block) {
        DeferredBlock<T> blocks = BLOCKS.register(name, block);
        registerBlockItems(name, blocks); 
        return blocks;
    }
}
