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

public class ModHarmfulFoodItem extends Item {
    
    private final ResourceLocation hpModifierId; 
    private final double hpPenaltyValue;

    public ModHarmfulFoodItem(Properties properties, ResourceLocation modifierId, double hpPenaltyValue) {
        super(properties);
        this.hpModifierId = modifierId;
        this.hpPenaltyValue = hpPenaltyValue;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack resultStack = super.finishUsingItem(stack, level, entity);

        if (!level.isClientSide() && entity instanceof ServerPlayer player) {

            // 永久扣除生命值上限（仅限首次食用）
            AttributeInstance maxHealthAttr = player.getAttribute(Attributes.MAX_HEALTH);
            if (maxHealthAttr != null) {
                if (!maxHealthAttr.hasModifier(this.hpModifierId)) {
                    
                    maxHealthAttr.addPermanentModifier(new AttributeModifier(
                            this.hpModifierId,
                            -this.hpPenaltyValue,
                            AttributeModifier.Operation.ADD_VALUE
                    ));

                    player.sendSystemMessage(Component.translatable("message.smasher.first_eat_" + this.hpModifierId.getPath()));
                }
            }
        }

        return resultStack;
    }
}
