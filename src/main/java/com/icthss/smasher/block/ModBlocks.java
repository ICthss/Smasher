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
    // 1. 创建 NeoForge 1.21.1 专用的方块延迟注册表
    public static final DeferredRegister.Blocks BLOCKS = 
        DeferredRegister.createBlocks(Smasher.MODID);
 
    // 2. 注册粉碎机方块：这里必须使用你自定义的 SmasherBlock 类
    // 同时采用 1.21.1 规范的方块行为属性类 (BlockBehaviour.Properties)
    public static final DeferredBlock<Block> SMASHER_BLOCK = 
        registerBlocks("smasher_block", () -> new SmasherBlock(
                BlockBehaviour.Properties.of()
                        .strength(3.0f) // 设置硬度，3.0f 相当于铁矿石的挖掘时间
                        .requiresCorrectToolForDrops() // 设置为必须用匹配的工具（镐）挖掘才能掉落本身
        ));

    // 3. 辅助方法：将对应的方块注册为 Item 形态，使其能出现在玩家背包和创造模式物品栏中
    private static <T extends Block> void registerBlockItems(String name, DeferredBlock<T> block) {
        // 利用你项目中现有的 ModItems.ITEMS 注册表，将 BlockItem 绑定到该方块上
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
    
    // 4. 辅助方法：集成化方块和对应物品的同时注册
    private static <T extends Block> DeferredBlock<T> registerBlocks(String name, Supplier<T> block) {
        DeferredBlock<T> blocks = BLOCKS.register(name, block);
        registerBlockItems(name, blocks); // 自动联动注册 BlockItem
        return blocks;
    }
}
