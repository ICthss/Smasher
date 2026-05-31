package com.icthss.smasher.gui;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class SmasherMenu extends AbstractContainerMenu {
    private final ContainerData data;

    // 客户端构造函数（由网络包触发）
    public SmasherMenu(int containerId, Inventory playerInv, FriendlyByteBuf extraData) {
        this(containerId, playerInv, new ItemStackHandler(4), new SimpleContainerData(2));
    }

    // 服务端与客户端共用的真正构造函数
    public SmasherMenu(int containerId, Inventory playerInv, IItemHandler itemHandler, ContainerData data) {
        super(ModMenuTypes.SMASHER_MENU.get(), containerId);
        checkContainerDataCount(data, 2);
        this.data = data;

        // 1. 添加机器的专属格子 (SlotItemHandler)
        // 参数: 处理器, 索引, 屏幕X坐标, 屏幕Y坐标
        this.addSlot(new SlotItemHandler(itemHandler, 0, 45, 18));  // 输入1
        this.addSlot(new SlotItemHandler(itemHandler, 1, 45, 36));  // 输入2
        this.addSlot(new SlotItemHandler(itemHandler, 2, 110, 27) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false; // 输出槽位绝对禁止放入物品
            }
        }); // 输出1
        this.addSlot(new SlotItemHandler(itemHandler, 3, 128, 27){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false; // 输出槽位绝对禁止放入物品
            }
        }); // 输出2

        // 2. 添加玩家的背包格子 (3x9)
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // 3. 添加玩家的快捷栏格子 (1x9)
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
        }

        // 追踪进度条数据
        this.addDataSlots(data);
    }

    // 获取当前粉碎进度的百分比，用于 Screen 渲染箭头
    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        return maxProgress != 0 && progress != 0 ? progress * 24 / maxProgress : 0;
    }

    @Override
    public boolean stillValid(Player player) {
        return true; // 实际开发中应该检查玩家与方块的距离
    }

    // 必须重写：处理 Shift+左键 快速移动物品的逻辑（防止游戏崩溃）
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 4) { // 机器格子
                if (!this.moveItemStackTo(itemstack1, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            }
            else if (index != 2 && index != 3){ // 玩家背包
                if (!this.moveItemStackTo(itemstack1, 0, 2, false)) { // 优先放入输入槽
                    return ItemStack.EMPTY;
                }
            }
            else{
                if (!this.moveItemStackTo(itemstack1, 4, 30, true)) {
                    return ItemStack.EMPTY;
                }
            }
            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }
}
