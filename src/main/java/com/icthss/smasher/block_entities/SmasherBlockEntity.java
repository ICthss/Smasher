package com.icthss.smasher.block_entities;

import com.icthss.smasher.recipe.ModRecipes;
import com.icthss.smasher.recipe.SmashRecipe;
import com.icthss.smasher.recipe.SmashRecipeInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SmasherBlockEntity extends BlockEntity implements MenuProvider {
    private int progress = 0;
    private int maxProgress = 200;

    // 4个槽位的独立物品处理器
    public final ItemStackHandler itemHandler = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            // 完美防御：只允许外部（如漏斗、管道、玩家点击）放入到 0、1 号输入槽。
            // 2、3号输出槽返回 false，从而禁止外部主动堆叠或放入物品。
            return slot == 0 || slot == 1;
        }
    };

    // 同步到客户端的进度条数据 (对应你 Menu 中的 this.data)
    public final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> SmasherBlockEntity.this.progress;
                case 1 -> SmasherBlockEntity.this.maxProgress;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> SmasherBlockEntity.this.progress = value;
                case 1 -> SmasherBlockEntity.this.maxProgress = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public SmasherBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SMASHER_BE_TYPE.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.smasher.smasher_machine");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        // 完美接驳你的 Menu 类构造函数
        return new com.icthss.smasher.gui.SmasherMenu(containerId, playerInventory, this.itemHandler, this.data);
    }

    // 1.21.1 规范持久化数据读取，强制要求传入 HolderLookup.Provider
    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.itemHandler.deserializeNBT(registries, tag.getCompound("Inventory"));
        this.progress = tag.getInt("Progress");
        this.maxProgress = tag.getInt("MaxProgress");
    }

    // 1.21.1 规范持久化数据保存，强制要求传入 HolderLookup.Provider
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", this.itemHandler.serializeNBT(registries));
        tag.putInt("Progress", this.progress);
        tag.putInt("MaxProgress", this.maxProgress);
    }

    // 机器每一帧 Tick 的核心轮询处理器
    public static void tick(Level level, BlockPos pos, BlockState state, SmasherBlockEntity blockEntity) {
        if (level.isClientSide) return;

        SmashRecipeInput input = new SmashRecipeInput(
                blockEntity.itemHandler.getStackInSlot(0),
                blockEntity.itemHandler.getStackInSlot(1)
        );

        Optional<RecipeHolder<SmashRecipe>> recipeOpt = level.getRecipeManager()
                .getRecipeFor(ModRecipes.SMASH_TYPE.get(), input, level);

        if (recipeOpt.isPresent()) {
            SmashRecipe recipe = recipeOpt.get().value();
            blockEntity.maxProgress = recipe.getCookTime();

            // 核心安全溢出拦截：验证 2 号槽是否能完美容纳主产物，3 号槽是否能完美容纳副产物
            if (blockEntity.canOutput(recipe)) {
                blockEntity.progress++;
                setChanged(level, pos, state);

                if (blockEntity.progress >= blockEntity.maxProgress) {
                    blockEntity.craftItem(recipe);
                    blockEntity.progress = 0;
                }
            } else {
                blockEntity.progress = 0; // 只要 2 号槽将要溢出或无法容纳，机器立刻停机并将进度重置
            }
        } else {
            blockEntity.progress = 0;
        }
    }

    private boolean canOutput(SmashRecipe recipe) {
        ItemStack mainRes = recipe.getMainOutput();
        ItemStack secRes = recipe.getSecondaryOutput();

        // 1. 2 号槽作为主产物格，必须能装得下配方的主产物，否则提前拦截熔断
        if (!canStackInSlot(2, mainRes)) {
            return false;
        }

        // 2. 3 号槽作为副产物格，若配方存在副产物，必须能完整装下副产物
        if (!secRes.isEmpty()) {
            return canStackInSlot(3, secRes);
        }

        return true;
    }

    private boolean canStackInSlot(int slot, ItemStack result) {
        if (result.isEmpty()) return true;
        ItemStack current = this.itemHandler.getStackInSlot(slot);
        if (current.isEmpty()) return true;
        return ItemStack.isSameItemSameComponents(current, result)
                && (current.getCount() + result.getCount() <= current.getMaxStackSize());
    }

    private void craftItem(SmashRecipe recipe) {
        // 1. 精准缩减 0号 和 1号 槽位的配方材料数量
        this.itemHandler.getStackInSlot(0).shrink(recipe.getInput0().count());
        this.itemHandler.getStackInSlot(1).shrink(recipe.getInput1().count());

        // 2. 主产物绝对并且只强制推入 2 号主产物槽位
        ItemStack mainResult = recipe.getMainOutput().copy();
        if (!mainResult.isEmpty()) {
            forceInsertIntoOutputSlot(2, mainResult);
        }

        // 3. 副产物绝对并且只强制推入 3 号副产物槽位
        ItemStack secResult = recipe.getSecondaryOutput().copy();
        if (!secResult.isEmpty()) {
            forceInsertIntoOutputSlot(3, secResult);
        }

        setChanged();
    }

    private void forceInsertIntoOutputSlot(int slot, ItemStack stack) {
        ItemStack slotStack = this.itemHandler.getStackInSlot(slot);
        if (slotStack.isEmpty()) {
            this.itemHandler.setStackInSlot(slot, stack.copy());
            return;
        }
        if (ItemStack.isSameItemSameComponents(slotStack, stack)) {
            int maxInsert = Math.min(stack.getCount(), slotStack.getMaxStackSize() - slotStack.getCount());
            if (maxInsert > 0) {
                slotStack.grow(maxInsert);
                this.itemHandler.setStackInSlot(slot, slotStack);
            }
        }
    }
}
