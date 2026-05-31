package com.icthss.smasher.block_entities;

import java.util.Optional;

import org.antlr.v4.runtime.misc.NotNull;
import org.jetbrains.annotations.Nullable;

import com.icthss.smasher.gui.BlenderMenu;
import com.icthss.smasher.recipe.ModRecipes;
import com.icthss.smasher.recipe.BlendRecipe;


import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

public class BlenderBlockEntity extends BlockEntity implements MenuProvider {

    // 通过使用 ItemStackHandler 代替原版繁琐的 Container 接口，可以天然支持漏斗、管道以及外部自动化模组的抽送
    public final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged(); // 只要格子里的物品发生了改变（玩家放了物品或漏斗漏了物品），立即告诉游戏当前方块区块数据不干净，需要写入硬盘存档中
            if (level != null && !level.isClientSide) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(),getBlockState(),3);
            }
        }
    };

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag,registries);
        return tag;
        }

    @Override
    public net.minecraft.network.protocol.Packet<ClientGamePacketListener> getUpdatePacket() {
        return net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket.create(this);
    }

    public ItemStackHandler getInventory() {
            return this.itemHandler;
        }


    private int progress = 0;
    private int maxProgress = 200;

    // 2. 界面数据同步网桥（ContainerData）：负责将服务端的当前进度同步发给玩家客户端的进度条箭头
    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return index == 0 ? BlenderBlockEntity.this.progress : BlenderBlockEntity.this.maxProgress;
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) BlenderBlockEntity.this.progress = value; 
            else BlenderBlockEntity.this.maxProgress = value;
        }

        @Override
        public int getCount() {
            return 2; // 总共追踪并同步 2 个关键变量
        }
    };

    // 3. 构造函数：利用 super 将坐标和状态向上反馈，同时精准扣合新类 ModBlockEntities 中的身份证
    public BlenderBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.BLENDER_BE_TYPE.get(), pos, blockState);
    }

    @Override
    public Component getDisplayName() {
        // 这是显示在玩家 GUI 屏幕正上方中央的文本键名（可以在你的 zh_cn.json 里配置它的翻译文本）
        return Component.translatable("container.smasher.blend_machine");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        // 当玩家成功右键点击方块时，创建并拉起你的 SmasherMenu，将当前机器格子和同步进度一同输送过去
        return new BlenderMenu(containerId, playerInventory, this.itemHandler, this.data);
    }
    //===================搅拌机核心逻辑==================
    public static void tick(Level level, BlockPos pos, BlockState state, BlenderBlockEntity blockEntity) {
        if (level.isClientSide()) return; // 物理隔离

        // 包装 2 个输入槽供配方扫描
        RecipeInput recipeInput = new RecipeInput() {
            @Override public ItemStack getItem(int index) { return blockEntity.itemHandler.getStackInSlot(index); }
            @Override public int size() { return 2; }
        };

        // 捞取自定义配方
        Optional<RecipeHolder<BlendRecipe>> recipeOpt = level.getRecipeManager()
                .getRecipeFor(ModRecipes.BLEND_TYPE.get(), recipeInput, level);

        if (recipeOpt.isPresent()) {
            BlendRecipe recipe = recipeOpt.get().value();
            blockEntity.maxProgress = recipe.getblendTime();

            // 核心修改：检查 0 号槽位当前能否安全地被转化为配方产物
            if (blockEntity.canTransformInput(recipe.getOutputs())) {
                blockEntity.progress++; // 进度推进
                setChanged(level, pos, state);

                // 进度充能完毕，正式执行“注入转化”
                if (blockEntity.progress >= blockEntity.maxProgress) {
                    blockEntity.craftItemTransform(recipe);
                    blockEntity.progress = 0; // 重置进度
                }
            } else {
                // 空间不足或类型不匹配时，进度条平滑倒退
                blockEntity.progress = Math.max(0, blockEntity.progress - 2);
            }
        } else {
            blockEntity.progress = 0; // 配方失效进度归零
        }
    }

    /**
     * 辅助验证逻辑：判断 0 号槽位能否被安全转化为产物
     * 因为是一对一转化，必须确保转化的产物不会把 0 号槽位撑爆
     */
    private boolean canTransformInput(NonNullList<ItemStack> outputs) {
        if (outputs.isEmpty()) return false;
        
        ItemStack recipeOutput = outputs.get(0); // 注入转化的唯一产物
        ItemStack currentInput = this.itemHandler.getStackInSlot(0); // 当前 0 号槽位的物品

        // 如果 0 号槽位已经空了（可能在中途被玩家抽走），无法继续转化
        if (currentInput.isEmpty()) return false;

        // 场景 A：如果 0 号槽位只有 1 个物品，它转化后一定能完美放下（直接替换即可）
        if (currentInput.getCount() == 1) {
            return true;
        }

        // 场景 B：如果 0 号槽位是一堆物品（例如 64 个铁锭），
        // 我们只消耗 1 个铁锭并将其转化为产物。此时必须检查 0 号槽位转化出的产物，
        // 能否和原本就是这个产物的物品发生合并（这通常需要有第3个槽位，但既然写死2槽位，此处必须限制单次转化的堆叠安全）
        // 为了绝对安全和防止物品被吞，这里推荐限制：如果输入物品堆叠大于 1，我们需要判定它是否会产生冲突。
        // 最完美的“注入机器”通常建议玩家一次只放 1 个主体物品。
        // 如果你希望支持一堆物品连续转化，最安全的判定是：
        // 只有当 0 号槽位数量为 1，或者输出的产物与当前 0 号槽位完全一致（例如洗练词条）时才允许。
        
        // 简单安全的逻辑：我们假设玩家每次只能转化 1 个主体，如果一堆放进去，只有类型完全一致时允许叠加
        if (ItemStack.isSameItemSameComponents(currentInput, recipeOutput)) {
            return currentInput.getCount() + recipeOutput.getCount() <= currentInput.getMaxStackSize();
        }

        // 如果 0 号槽位有多余 1 个物品，且类型与产物不同（例如 64个铁锭 注入变成 1个钢锭），
        // 在 2 槽位机器中是装不下的（因为铁锭还没消耗完，位置被占了）。此时必须返回 false 阻止加工。
        return false; 
    }

    /**
     * 真正执行注入转化的结算动作
     */
    private void craftItemTransform(BlendRecipe recipe) {
        if (recipe.getOutputs().isEmpty()) return;
        
        ItemStack recipeOutput = recipe.getOutputs().get(0).copy();
        
        // 1. 扣除 1 号槽位的“注入消耗材料” 1 个数量
        this.itemHandler.getStackInSlot(1).shrink(1);

        // 2. 处理 0 号主体原料槽的转化
        ItemStack currentInput = this.itemHandler.getStackInSlot(0);
        
        if (currentInput.getCount() == 1) {
            // 如果只有 1 个，直接把 0 号槽位替换为产物
            this.itemHandler.setStackInSlot(0, recipeOutput);
        } else {
            // 如果大于 1 个（例如一堆铁锭），扣除一个原料，并叠加上新产物
            // 注意：因为前面 canTransformInput 已经过滤了不相同的情况，这里直接 grow 绝对安全
            currentInput.shrink(1);
            if (ItemStack.isSameItemSameComponents(currentInput, recipeOutput)) {
                currentInput.grow(recipeOutput.getCount());
            } else {
                // 防御性代码：如果理论上走到了这里却无法融合，直接将产物喷在世界上，防止吞物品
                BlockPos spawnPos = this.getBlockPos().above(); // 在机器上方喷出
                net.minecraft.world.Containers.dropItemStack(level, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), recipeOutput);
            }
        }
    }
    // ==================== 🛠️ 1.21.1 现代世界读写存档管理 ====================
    // 旧版无参数的 readNbt 和 writeNbt 已在 1.21 被全线淘汰并报错。
    // 现在必须强制重写这两个接收 HolderLookup.Provider 注册表解析器的全新方法。
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("BlenderInv", this.itemHandler.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("BlenderInv")) {
            this.itemHandler.deserializeNBT(registries, tag.getCompound("BlenderInv"));
        }
    }
}
