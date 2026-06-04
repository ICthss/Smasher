package com.icthss.smasher.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record SmashRecipeInput(ItemStack slot0, ItemStack slot1) implements RecipeInput {
    @Override
    public ItemStack getItem(int index) {
        if (index == 0) return this.slot0;
        if (index == 1) return this.slot1;
        throw new IllegalArgumentException("Invalid slot index for Smasher: " + index);
    }

    @Override
    public int size() {
        return 2;
    }

    // 1.21.1 必须实现的接口方法，如果不写会导致外层泛型因类型不全直接使类名报错
    @Override
    public boolean isEmpty() {
        return this.slot0.isEmpty() && this.slot1.isEmpty();
    }
}
