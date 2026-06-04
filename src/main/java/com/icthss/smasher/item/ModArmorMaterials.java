package com.icthss.smasher.item;

import com.icthss.smasher.Smasher;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.List;

public class ModArmorMaterials {
    
    public static final Holder<ArmorMaterial> XUEWANG = Registry.registerForHolder(
            BuiltInRegistries.ARMOR_MATERIAL,
            ResourceLocation.fromNamespaceAndPath(Smasher.MODID, "xuewang"),
            new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 10);
                        map.put(ArmorItem.Type.LEGGINGS, 20);
                        map.put(ArmorItem.Type.CHESTPLATE, 20);
                        map.put(ArmorItem.Type.HELMET, 20);
                    }),
                    15, // 附魔亲和性
                    SoundEvents.ARMOR_EQUIP_IRON, // 装备音效
                    () -> Ingredient.of(ModItems.SNOW_FRAGMENT.get()), // 修复材料
                    List.of(new ArmorMaterial.Layer(
                            ResourceLocation.fromNamespaceAndPath(Smasher.MODID, "xuewang")
                    )),
                    1.0F, // 韧性
                    1.0F  // 击退抗性
            )
    );
    public static final Holder<ArmorMaterial> Snow = Registry.registerForHolder(
            BuiltInRegistries.ARMOR_MATERIAL,
            ResourceLocation.fromNamespaceAndPath(Smasher.MODID, "snowfragment"),
            new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 10);
                        map.put(ArmorItem.Type.LEGGINGS, 20);
                        map.put(ArmorItem.Type.CHESTPLATE, 20);
                        map.put(ArmorItem.Type.HELMET, 20);
                    }),
                    15, // 附魔亲和性
                    SoundEvents.ARMOR_EQUIP_IRON, // 装备音效
                    () -> Ingredient.of(ModItems.SNOW_FRAGMENT.get()), // 修复材料
                    List.of(new ArmorMaterial.Layer(
                            ResourceLocation.fromNamespaceAndPath(Smasher.MODID, "snow")
                    )),
                    1.0F, // 韧性
                    1.0F  // 击退抗性
            )
    );
    public static final Holder<ArmorMaterial> Sprite = Registry.registerForHolder(
            BuiltInRegistries.ARMOR_MATERIAL,
            ResourceLocation.fromNamespaceAndPath(Smasher.MODID, "sprite"),
            new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 10);
                        map.put(ArmorItem.Type.LEGGINGS, 20);
                        map.put(ArmorItem.Type.CHESTPLATE, 20);
                        map.put(ArmorItem.Type.HELMET, 20);
                    }),
                    15, // 附魔亲和性
                    SoundEvents.ARMOR_EQUIP_IRON, // 装备音效
                    () -> Ingredient.of(ModItems.SPRITE_BOTTLE.get()), // 修复材料
                    List.of(new ArmorMaterial.Layer(
                            ResourceLocation.fromNamespaceAndPath(Smasher.MODID, "sprite")
                    )),
                    1.0F, // 韧性
                    1.0F  // 击退抗性
            )
    );
     public static final Holder<ArmorMaterial> qiaolezi_armor = Registry.registerForHolder(
            BuiltInRegistries.ARMOR_MATERIAL,
            ResourceLocation.fromNamespaceAndPath(Smasher.MODID, "qiaolezi_package"),
            new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 10);
                        map.put(ArmorItem.Type.LEGGINGS, 20);
                        map.put(ArmorItem.Type.CHESTPLATE, 20);
                        map.put(ArmorItem.Type.HELMET, 20);
                    }),
                    15, // 附魔亲和性
                    SoundEvents.ARMOR_EQUIP_IRON, // 装备音效
                    () -> Ingredient.of(ModItems.QIAOLEZI_PACKAGE.get()), // 修复材料
                    List.of(new ArmorMaterial.Layer(
                            ResourceLocation.fromNamespaceAndPath(Smasher.MODID, "qiaolezi_package")
                    )),
                    1.0F, // 韧性
                    1.0F  // 击退抗性
            )
    );
}