package com.icthss.smasher.block_entities;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.icthss.smasher.gui.SmasherMenu;
import com.icthss.smasher.recipe.ModRecipes;
import com.icthss.smasher.recipe.SmashRecipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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

public class SmasherBlockEntity extends BlockEntity implements MenuProvider {

    // 1. 创建高度自动化的物品槽处理器（4个格子：0,1 为输入槽，2,3 为输出槽）
    // 通过使用 ItemStackHandler 代替原版繁琐的 Container 接口，可以天然支持漏斗、管道以及外部自动化模组的抽送
    public final ItemStackHandler itemHandler = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged(); // 只要格子里的物品发生了改变（玩家放了物品或漏斗漏了物品），立即告诉游戏当前方块区块数据不干净，需要写入硬盘存档中
        }
    };

    private int progress = 0;
    private int maxProgress = 200; // 默认需要熔炼 200 刻（即 10 秒），后续会被数据包 JSON 里的时间实时覆盖

    // 2. 界面数据同步网桥（ContainerData）：负责将服务端的当前进度同步发给玩家客户端的进度条箭头
    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return index == 0 ? SmasherBlockEntity.this.progress : SmasherBlockEntity.this.maxProgress;
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) SmasherBlockEntity.this.progress = value; 
            else SmasherBlockEntity.this.maxProgress = value;
        }

        @Override
        public int getCount() {
            return 2; // 总共追踪并同步 2 个关键变量
        }
    };

    // 3. 构造函数：利用 super 将坐标和状态向上反馈，同时精准扣合新类 ModBlockEntities 中的身份证
    public SmasherBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.SMASHER_BE_TYPE.get(), pos, blockState);
    }

    @Override
    public Component getDisplayName() {
        // 这是显示在玩家 GUI 屏幕正上方中央的文本键名（可以在你的 zh_cn.json 里配置它的翻译文本）
        return Component.translatable("container.smasher.smasher_machine");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        // 当玩家成功右键点击方块时，创建并拉起你的 SmasherMenu，将当前机器格子和同步进度一同输送过去
        return new SmasherMenu(containerId, playerInventory, this.itemHandler, this.data);
    }

    // ==================== 🛠️ 1.21.1 核心：处理每一游戏刻（Tick）的核心粉碎机引擎逻辑 ====================
    public static void tick(Level level, BlockPos pos, BlockState state, SmasherBlockEntity blockEntity) {
        if (level.isClientSide()) return; // 物理隔离：只准在服务端跑计算逻辑，绝对不允许客户端越权

        // 将当前槽位 0 和 1 里的实时 ItemStack 包装进 1.21 新版专属的简易 RecipeInput 中供配方管理器扫描
        RecipeInput recipeInput = new RecipeInput() {
            @Override public ItemStack getItem(int index) { return blockEntity.itemHandler.getStackInSlot(index); }
            @Override public int size() { return 2; }
        };

        // 从当前世界唯一的配方总管中，去捞取看有没有任何一个自定义 JSON 文件的条件能够契合当前的 recipeInput
        Optional<RecipeHolder<SmashRecipe>> recipeOpt = level.getRecipeManager()
                .getRecipeFor(ModRecipes.SMASH_TYPE.get(), recipeInput, level);

        if (recipeOpt.isPresent()) {
            SmashRecipe recipe = recipeOpt.get().value();
            blockEntity.maxProgress = recipe.getSmashTime(); // 从匹配成功的 JSON 数据中动态提取这次加工应耗费的时间

            // 检查输出格子 2 和 3 有没有足够空闲的空间把产物完美塞进去
            if (blockEntity.canInsertOutputs(recipe.getOutputs())) {
                blockEntity.progress++; // 进度往前推进 1 刻
                setChanged(level, pos, state); // 标记这一刻的进度更新需要被记录

                // 进度充能完毕，正式生成物品并执行原材料消耗
                if (blockEntity.progress >= blockEntity.maxProgress) {
                    blockEntity.craftItem(recipe);
                    blockEntity.progress = 0; // 重置进度，等待下一次循环
                }
            } else {
                // 如果产物箱子满了塞不下了，进度条以每秒减 40 刻的速度平滑倒退，给予玩家极其写实的视觉反馈
                blockEntity.progress = Math.max(0, blockEntity.progress - 2);
            }
        } else {
            blockEntity.progress = 0; // 如果把里面的物品抽干了、或者放了错误的材料（配方失效），进度立即强行归零
        }
    }

    // 辅助验证逻辑：判断配方产出的 1~2 种不同 ItemStack 能否安全塞入槽位 2 和 3 中
    private boolean canInsertOutputs(NonNullList<ItemStack> outputs) {
        for (int i = 0; i < outputs.size(); i++) {
            ItemStack output = outputs.get(i);
            int targetSlot = i + 2; // 0,1输入对应映射到 2,3输出
            ItemStack existingStack = this.itemHandler.getStackInSlot(targetSlot);

            if (!existingStack.isEmpty()) {
                // 🟢 1.21+ 数据成分安全判别标准方法：isSameItemSameComponents 
                // 能够完美判断带有附魔、耐久、自定义命名的复杂产物。如果类型对不上或者堆叠数要撑爆 64 个，立刻返回 false 阻断粉碎。
                if (!ItemStack.isSameItemSameComponents(existingStack, output) || 
                    existingStack.getCount() + output.getCount() > existingStack.getMaxStackSize()) {
                    return false;
                }
            }
        }
        return true;
    }

    // 原材料真正执行损耗与产物爆出的结算动作
    private void craftItem(SmashRecipe recipe) {
        // 1. 扣除输入格 0 和 1 的物品各 1 个数量
        this.itemHandler.getStackInSlot(0).shrink(1);
        this.itemHandler.getStackInSlot(1).shrink(1);

        // 2. 将配方里的 1~2 种不同产物推向槽位 2 和 3
        NonNullList<ItemStack> outputs = recipe.getOutputs();
        for (int i = 0; i < outputs.size(); i++) {
            ItemStack output = outputs.get(i).copy();
            int targetSlot = i + 2;
            ItemStack existingStack = this.itemHandler.getStackInSlot(targetSlot);

            if (existingStack.isEmpty()) {
                // 如果格子原本就是空明状态，直接覆盖注入新产物
                this.itemHandler.setStackInSlot(targetSlot, output);
            } else {
                // 如果格子里早就有上一轮加工好的同类产物，直接执行数量向上叠加增加
                existingStack.grow(output.getCount());
            }
        }
    }

    // ==================== 🛠️ 1.21.1 现代世界读写存档管理 ====================
    // 旧版无参数的 readNbt 和 writeNbt 已在 1.21 被全线淘汰并报错。
    // 现在必须强制重写这两个接收 HolderLookup.Provider 注册表解析器的全新方法。
    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        // 从玩家存档世界文件夹中把序列化的“Inventory”NBT数据重新灌入我们内存的物品处理器中
        this.itemHandler.deserializeNBT(registries, tag.getCompound("Inventory"));
        // 抓取并还原玩家退出游戏那一刻的精密进度数值
        this.progress = tag.getInt("Progress");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        // 将内存中 4 个格子当下的物品状态转变为 NBT 复合标签，等待写入世界的 chunk 存档文件中
        tag.put("Inventory", this.itemHandler.serializeNBT(registries));
        // 将当前的进度条保存
        tag.putInt("Progress", this.progress);
    }
}
