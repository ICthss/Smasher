package com.icthss.smasher;

import com.icthss.smasher.item.ModItems;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = "smasher")
public class PlayerAttributeEventHandler {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        
        // 确保玩家为服务端玩家
        if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
            
            double maxHealth = serverPlayer.getMaxHealth();

            // 判断生命上限
            if (maxHealth <= 2.0D) {
                
                // 获取服务器已注册成就&达成雪人之力成就
                var advancementTree = serverPlayer.server.getAdvancements();
                AdvancementHolder advancement = advancementTree.get(
                        ResourceLocation.fromNamespaceAndPath("smasher", "power_xuefeng")
                );

                if (advancement != null) {
                    // 获取玩家成就进度
                    var progress = serverPlayer.getAdvancements().getOrStartProgress(advancement);
                    // 如果该成就还没有被完全获取
                    if (!progress.isDone()) {
                        //通过.award达成成就条件
                        for (String criterion : progress.getRemainingCriteria()) {
                            serverPlayer.getAdvancements().award(advancement, criterion);
                        }
                    }
                }
                
                // 判断是否装备雪王护甲
                if (isWearingXUEWANGArmor(serverPlayer)) {
                    // 给予抗性提升5效果
                    if (!serverPlayer.hasEffect(MobEffects.DAMAGE_RESISTANCE) ||
                        serverPlayer.getEffect(MobEffects.DAMAGE_RESISTANCE).getDuration() <= 20) {
                            serverPlayer.addEffect(new MobEffectInstance(
                                MobEffects.DAMAGE_RESISTANCE, 40, 4, false, false
                            ));
                        }
                }

                // 给予速度4效果
                if (!serverPlayer.hasEffect(MobEffects.MOVEMENT_SPEED) || 
                    serverPlayer.getEffect(MobEffects.MOVEMENT_SPEED).getDuration() <= 20 ) {
                            serverPlayer.addEffect(new MobEffectInstance(
                            MobEffects.MOVEMENT_SPEED, 40, 3, false, false
                    ));
                }
            }
        }
    }


    // 雪王护甲装备判断方法
    public static boolean isWearingXUEWANGArmor(Player player) {

        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
        
        Boolean rightHelmet = helmet.is(ModItems.XUEWANG_HELMET);
        Boolean rightChestplate = chestplate.is(ModItems.XUEWANG_CHESTPLATE);
        Boolean rightLeggings = leggings.is(ModItems.XUEWANG_LEGGINGS);
        Boolean rightBoots = boots.is(ModItems.XUEWANG_BOOTS);
        
        return rightBoots && rightChestplate && rightHelmet && rightLeggings;

    }
}
