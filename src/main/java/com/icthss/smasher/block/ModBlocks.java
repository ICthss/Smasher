package com.icthss.smasher.block;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import com.icthss.smasher.Smasher;
import com.icthss.smasher.item.ModItems;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = 
        DeferredRegister.createBlocks(Smasher.MODID);
    //注册方块
    public static final DeferredBlock<Block> SMASHER_BLOCK = 
        registerBlocks("smasher_block", () -> new Block(Block.Properties.of().strength(3.0f).requiresCorrectToolForDrops()));

    //方块物品注册方法
        private static <T extends Block> void registerBlockItems(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
    //同时注册方块和物品
    private static <T extends Block> DeferredBlock<T> registerBlocks(String name, Supplier<T> block) {
        DeferredBlock<T> blocks = BLOCKS.register(name, block);
        registerBlockItems(name, blocks);
        return blocks;
    }
}
