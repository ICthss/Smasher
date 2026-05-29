package com.icthss.smasher.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class WrappedChocolateBarItem extends Item {

    public WrappedChocolateBarItem(Properties properties) {
        super(properties);
    }

    // 1. 设置蓄力（拆包装）时播放的动画：这里使用 EAT（吃/撕开）或者 BOW（弓箭蓄力）
    // 如果想要更像拆包装，可以使用 UseAnim.EAT，它会发出类似撕咬/揉搓纸张的碎屑粒子和声音
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW; 
    }

    // 2. 设置蓄力需要多少时间（Tick）。20 Tick = 1秒。
    // 这里设置为 32 刻（约 1.6 秒），与原版吃食物时间一致。
    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    // 3. 当玩家按下右键时，触发开始蓄力
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        // 让玩家开始进入蓄力/使用状态
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemstack);
    }

    // 4. 当蓄力时间满了（32刻结束），触发拆开逻辑
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        // 先克隆一份当前物品，因为 super 方法可能会把数量减 1
        ItemStack resultStack = super.finishUsingItem(stack, level, entity);

        if (entity instanceof Player player) {
            // 获取你的“空包装”和“原味巧乐兹”物品
            // 假设你已经在 ModItems 里注册了它们
            ItemStack emptyWrapper = new ItemStack(ModItems.QIAOLEZI_PACKAGE.get());
            ItemStack chocolateBar = new ItemStack(ModItems.QIAOLEZI_ORIGIN.get());

            // 给予玩家原味巧乐兹
            if (!player.getInventory().add(chocolateBar)) {
                player.drop(chocolateBar, false); // 背包满则掉落
            }

            // 给予玩家空包装（处理生存模式下手里物品变空的情况）
            if (resultStack.isEmpty()) {
                return emptyWrapper; // 如果手里只有1个包装，拆完直接变成空包装
            }

            // 如果手里有多叠（虽然通常不能叠加），把空包装塞进背包
            if (!player.getInventory().add(emptyWrapper)) {
                player.drop(emptyWrapper, false);
            }
        }

        return resultStack;
    }
}
