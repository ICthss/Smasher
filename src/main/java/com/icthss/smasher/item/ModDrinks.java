package com.icthss.smasher.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import java.util.function.Supplier;

public class ModDrinks extends Item {
    private final Supplier<? extends Item> emptyContainerSupplier;

    public ModDrinks(Properties properties, Supplier<? extends Item> emptyContainerSupplier) {
        super(properties);
        this.emptyContainerSupplier = emptyContainerSupplier;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack resultStack = super.finishUsingItem(stack, level, entity);

        if (entity instanceof Player player) {
            ItemStack emptyContainer = new ItemStack(this.emptyContainerSupplier.get());

            if (resultStack.isEmpty()) {
                return emptyContainer;
            }

            if (!player.getInventory().add(emptyContainer)) {
                player.drop(emptyContainer, false);
            }
        }

        return resultStack;
    }
}
