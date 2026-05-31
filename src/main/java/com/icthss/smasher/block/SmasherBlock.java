package com.icthss.smasher.block;

import org.jetbrains.annotations.Nullable;

import com.icthss.smasher.block_entities.SmasherBlockEntity;
import com.icthss.smasher.block_entities.ModBlockEntities;
import com.mojang.serialization.MapCodec;

import net.minecraft.world.Containers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

    //掉落内容物品
    @Override
public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
    if (!state.is(newState.getBlock())) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof SmasherBlockEntity smasherBE) {
            var handler = smasherBE.itemHandler; // 获取你的 4 槽位背包

            // 显式点对点轰炸，确保 0, 1, 2, 3 全部掉落
            for (int i = 0; i < 4; i++) {
                ItemStack stack = handler.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
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

    @Nullable 
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        // 只在服务端处理 tick
        if (level.isClientSide()) {
            return null;
        }
         
        return (lvl, blockPos, blockState, blockEntity) -> {
            if (blockEntity instanceof SmasherBlockEntity smasher) {
                SmasherBlockEntity.tick(lvl, blockPos, blockState, smasher);
            }
        };
    }
}
