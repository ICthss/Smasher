package com.icthss.smasher.block;

import org.jetbrains.annotations.Nullable;

import com.icthss.smasher.block_entities.SmasherBlockEntity;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class SmasherBlock extends BaseEntityBlock {
    
    // 1.21.1 编解码器
    public static final MapCodec<SmasherBlock> CODEC = simpleCodec(SmasherBlock::new);

    public SmasherBlock(Properties properties) { 
        super(properties); 
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override 
    public RenderShape getRenderShape(BlockState state) { 
        return RenderShape.MODEL; 
    }

    @Nullable 
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { 
        return new SmasherBlockEntity(pos, state); 
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof SmasherBlockEntity smasherBE) {
                player.openMenu(smasherBE, pos);
            }
        }
        return InteractionResult.SUCCESS;
    }

    // 🟢 终极修正：利用 Class 强转安全剥离开对 ModBlockEntities 注册表的依赖！
    @Nullable 
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        // 只在服务端处理 tick
        if (level.isClientSide()) {
            return null;
        }
        
        // 🌟 核心突破：直接返回一个通用的 Ticker。在里面直接用 instanceof 判断方块实体是否是 SmasherBlockEntity。
        // 这样就不需要去调用 ModBlockEntities.SMASHER_BE_TYPE.get() 了，循环引用当场被切断！
        return (lvl, blockPos, blockState, blockEntity) -> {
            if (blockEntity instanceof SmasherBlockEntity smasher) {
                SmasherBlockEntity.tick(lvl, blockPos, blockState, smasher);
            }
        };
    }
}
