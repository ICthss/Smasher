package com.icthss.smasher;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = "smasher")
public class PlayerAttributeEventHandler {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        
        // 核心：必须确保是服务端玩家（ServerPlayer），因为成就系统完全运行在服务端
        if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
            
            double maxHealth = serverPlayer.getMaxHealth();

            // 1. 判定：生命值上限小于或等于 2.0 点（1颗心）
            if (maxHealth <= 2.0D) {
                
                // 【新增逻辑】：在满足条件时，为玩家解锁最终成就
                // 获取当前服务器的成就注册表
                var advancementTree = serverPlayer.server.getAdvancements();
                // 寻找我们刚才写好的隐藏成就文件（请将 your_mod_id 替换为您真实的 Mod ID）
                AdvancementHolder advancement = advancementTree.get(
                        ResourceLocation.fromNamespaceAndPath("smasher", "power_xuefeng")
                );

                if (advancement != null) {
                    // 获取玩家该成就的进度对象
                    var progress = serverPlayer.getAdvancements().getOrStartProgress(advancement);
                    // 如果该成就还没有被完全获取
                    if (!progress.isDone()) {
                        // 循环点亮该成就中的所有条件（我们刚才在 JSON 里写了 "reached_low_max_hp"）
                        for (String criterion : progress.getRemainingCriteria()) {
                            serverPlayer.getAdvancements().award(advancement, criterion);
                        }
                    }
                }

                // 2. 原有的逻辑：给予速度提升效果
                if (!serverPlayer.hasEffect(MobEffects.MOVEMENT_SPEED) || 
                    serverPlayer.getEffect(MobEffects.MOVEMENT_SPEED).getDuration() <= 5) {
                    
                    serverPlayer.addEffect(new MobEffectInstance(
                            MobEffects.MOVEMENT_SPEED, 21, 1, false, false
                    ));
                }
            }
        }
    }
}
