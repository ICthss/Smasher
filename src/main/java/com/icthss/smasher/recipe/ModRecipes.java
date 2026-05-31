package com.icthss.smasher.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.Supplier;

public class ModRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, "smasher");
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, "smasher");

    public static final Supplier<RecipeType<SmashRecipe>> SMASH_TYPE = RECIPE_TYPES.register("smash", 
            () -> new RecipeType<SmashRecipe>() {
                @Override
                public String toString() { return "smash"; }
            });

    public static final Supplier<RecipeSerializer<SmashRecipe>> SMASH_SERIALIZER = SERIALIZERS.register("smash", 
            () -> SmashRecipe.Serializer.INSTANCE);

    public static final Supplier<RecipeType<BlendRecipe>> BLEND_TYPE = RECIPE_TYPES.register("blend",
            () -> new RecipeType<BlendRecipe>() {
                @Override
                public String toString() { return "blend"; }
            });
    
    public static final Supplier<RecipeSerializer<BlendRecipe>> BLEND_SERIALIZER = SERIALIZERS.register("blend",
            () -> BlendRecipe.Serializer.INSTANCE);
}
