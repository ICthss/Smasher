package com.icthss.smasher.item;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

public class ModHarmfulFoodItem extends Item {
    // 定义修饰符的唯一 ID
    private static final ResourceLocation HP_MODIFIER_ID = 
            ResourceLocation.fromNamespaceAndPath("smasher", "food_hp_penalty");

    public ModHarmfulFoodItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack resultStack = super.finishUsingItem(stack, level, entity);

        // 确保在服务端运行且目标是玩家
        if (!level.isClientSide() && entity instanceof ServerPlayer player) {


            // 2. 只有首次吃才会触发的逻辑：扣除生命值上限
            AttributeInstance maxHealthAttr = player.getAttribute(Attributes.MAX_HEALTH);
            if (maxHealthAttr != null) {
                // 【核心判断】：检查玩家当前是否已经有了这个修饰符
                if (!maxHealthAttr.hasModifier(HP_MODIFIER_ID)) {
                    
                    // 如果没有，说明是第一次吃，添加永久修饰符（扣除 4 点上限）
                    maxHealthAttr.addPermanentModifier(new AttributeModifier(
                            HP_MODIFIER_ID,
                            -2.0D,
                            AttributeModifier.Operation.ADD_VALUE
                    ));
                    
                    // 可选：发送一条只有第一次吃才会收到的专属提示信息
                    player.sendSystemMessage(Component.translatable("qiaolezi_first_time_eat").withStyle(ChatFormatting.RED));
                }
            }
        }

        return resultStack;
    }
}
