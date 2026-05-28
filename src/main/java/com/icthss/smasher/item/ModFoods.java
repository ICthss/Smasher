package com.icthss.smasher.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;


public class ModFoods {
    public static final FoodProperties QIAOLEZI_ORIGIN = new FoodProperties.Builder()
        .nutrition(4)
        .saturationModifier(0.3f)
        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 5 ), 1f)
        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2400, 5 ), 1f)
        .build();

    public static final FoodProperties QIAOLEZI = new FoodProperties.Builder()
        .nutrition(6)
        .saturationModifier(0.5f)        
        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 5 ), 1f)
        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2400, 5 ), 1f)
        .build();

    public static final FoodProperties QIAOLEZI_DEEPEN = new FoodProperties.Builder()
        .nutrition(8)
        .saturationModifier(0.7f)
        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 5 ), 1f)
        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2400, 5 ), 1f)
        .build();

    public static final FoodProperties QIAOLEZI_VANILLA = new FoodProperties.Builder()
        .nutrition(10)
        .saturationModifier(0.9f)
        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 5 ), 1f)
        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2400, 5 ), 1f)
        .build();

    public static final FoodProperties QIAOLEZI_BLUEBERRY = new FoodProperties.Builder()
        .nutrition(12)
        .saturationModifier(1.1f)
        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 5 ), 1f)
        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2400, 5 ), 1f)
        .build();

    public static final FoodProperties QIAOLEZI_STRAWBERRY = new FoodProperties.Builder()
        .nutrition(14)
        .saturationModifier(1.3f)
        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 5 ), 1f)
        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2400, 5 ), 1f)
        .build();

    public static final FoodProperties QIAOLEZI_MATCHA = new FoodProperties.Builder()
        .nutrition(16)
        .saturationModifier(1.5f)
        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 5 ), 1f)
        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2400, 5 ), 1f)
        .build();

    public static final FoodProperties QIAOLEZI_ENERGY = new FoodProperties.Builder()
        .nutrition(18)
        .saturationModifier(1.7f)
        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 5 ), 1f)
        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2400, 5 ), 1f)
        .build();

    public static final FoodProperties QIAOLEZI_SPRITE = new FoodProperties.Builder()
        .nutrition(20)
        .saturationModifier(1.9f)
        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 5 ), 1f)
        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2400, 5 ), 1f)
        .build();

    public static final FoodProperties QIAOLEZI_FATAL = new FoodProperties.Builder()
        .nutrition(-1)
        .saturationModifier(-1f)
        .effect(() -> new MobEffectInstance(MobEffects.WITHER, 1200, 10 ), 0.5f)
        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 5 ), 1f)
        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2400, 5 ), 1f)
        .build();

    public static final FoodProperties SPRITE = new FoodProperties.Builder()
        .nutrition(2)
        .saturationModifier(0.1f)
        .build();
    
    public static final FoodProperties FAKE_SPRITE = new FoodProperties.Builder()
        .nutrition(0)
        .saturationModifier(0f)
        .effect(() -> new MobEffectInstance(MobEffects.WITHER, 800, 2), 1f)
        .build();
}
