package com.icthss.smasher.item;

import com.icthss.smasher.Smasher;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents; // 修复了这里的复数 s
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.List;

public class ModArmorMaterials {
   
    public static final Holder<ArmorMaterial> XUEWANG = BuiltInRegistries.ARMOR_MATERIAL.register(
            "xuewang", 
            new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 10);
                        map.put(ArmorItem.Type.LEGGINGS, 20);
                        map.put(ArmorItem.Type.CHESTPLATE, 20);
                        map.put(ArmorItem.Type.HELMET, 20);
                    }),
                    15, 
                    SoundEvents.ARMOR_EQUIP_IRON, 
                    () -> Ingredient.EMPTY, // 修复材料
                    
                    List.of(new ArmorMaterial.Layer(
                            ResourceLocation.fromNamespaceAndPath(Smasher.MODID, "xuewang")
                    )),
                    1.0F,
                    1.0F  
            )
    );
}