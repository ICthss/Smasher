package com.icthss.smasher.event;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import com.icthss.smasher.item.ModItems;

/**
 * 盔甲全套特殊效果处理器
 * 适用于 NeoForge 1.21.1
 */
@EventBusSubscriber(modid = "smasher")
public class ModArmorEffectsHandler {

    /**
     * 每 tick 检测玩家穿戴的盔甲，并赋予相应的状态效果
     */
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        
        // 核心安全检查：只在服务端处理逻辑
        if (player.level().isClientSide()) return;

        // 1. 雪饼套：全套提供速度提升 1
        if (hasFullSet(player, 
                ModItems.Snow_HELMET.get(), 
                ModItems.Snow_CHESTPLATE.get(), 
                ModItems.Snow_LEGGINGS.get(), 
                ModItems.Snow_BOOTS.get())) {
            
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 0, false, false, true));
        }
        
        // 2. 雪碧套：全套提供速度提升 2，力量 1
        else if (hasFullSet(player, 
                ModItems.Sprite_HELMET.get(), 
                ModItems.Sprite_CHESTPLATE.get(), 
                ModItems.Sprite_LEGGINGS.get(), 
                ModItems.Sprite_BOOTS.get())) {
            
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 1, false, false, true));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 40, 0, false, false, true));
        }
        
        // 巧乐兹套：全套提供速度提升 2，力量 1
        else if (hasFullSet(player, 
                ModItems.qiaolezi_HELMET.get(), 
                ModItems.qiaolezi_CHESTPLATE.get(), 
                ModItems.qiaolezi_LEGGINGS.get(), 
                ModItems.qiaolezi_BOOTS.get())) {
            
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 1, false, false, true));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 40, 0, false, false, true));
        }
        
        // 3. 雪王套：全套提供速度提升 2，力量 3
        else if (hasFullSet(player, 
                ModItems.XUEWANG_HELMET.get(), 
                ModItems.XUEWANG_CHESTPLATE.get(), 
                ModItems.XUEWANG_LEGGINGS.get(), 
                ModItems.XUEWANG_BOOTS.get())) {
            
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 1, false, false, true));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 40, 2, false, false, true));
        }
    }

    /**
     * 辅助工具方法：统一判断玩家是否穿齐了某一整套盔甲
     */
    private static boolean hasFullSet(Player player, Item helmet, Item chestplate, Item leggings, Item boots) {
        ItemStack headSlot = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chestSlot = player.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack legsSlot = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack feetSlot = player.getItemBySlot(EquipmentSlot.FEET);

        return !headSlot.isEmpty() && headSlot.is(helmet) &&
               !chestSlot.isEmpty() && chestSlot.is(chestplate) &&
               !legsSlot.isEmpty() && legsSlot.is(leggings) &&
               !feetSlot.isEmpty() && feetSlot.is(boots);
    }
}