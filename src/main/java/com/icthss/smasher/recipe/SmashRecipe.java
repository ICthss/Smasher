package com.icthss.smasher.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

public class SmashRecipe implements Recipe<SmashRecipeInput> {
    private final SizedIngredient input0;
    private final SizedIngredient input1;
    private final ItemStack mainOutput;
    private final ItemStack secondaryOutput;
    private final int cookTime;

    public SmashRecipe(SizedIngredient input0, SizedIngredient input1, ItemStack mainOutput, ItemStack secondaryOutput, int cookTime) {
        this.input0 = input0;
        this.input1 = input1;
        this.mainOutput = mainOutput;
        this.secondaryOutput = secondaryOutput;
        this.cookTime = cookTime;
    }

    @Override
    public boolean matches(SmashRecipeInput input, Level level) {
        return this.input0.test(input.slot0()) && this.input0.count() <= input.slot0().getCount()
                && this.input1.test(input.slot1()) && this.input1.count() <= input.slot1().getCount();
    }

    @Override
    public ItemStack assemble(SmashRecipeInput input, HolderLookup.Provider registries) {
        return this.mainOutput.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.mainOutput;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.SMASH_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.SMASH_TYPE.get();
    }

    public SizedIngredient getInput0() { return this.input0; }
    public SizedIngredient getInput1() { return this.input1; }
    public ItemStack getMainOutput() { return this.mainOutput; }
    public ItemStack getSecondaryOutput() { return this.secondaryOutput; }
    public int getCookTime() { return this.cookTime; }

    public static class Serializer implements RecipeSerializer<SmashRecipe> {
        // 1.21.1 最完美安全的双物品合并编解码方案
        private static final MapCodec<SmashRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                SizedIngredient.FLAT_CODEC.fieldOf("input_slot_0").forGetter(SmashRecipe::getInput0),
                SizedIngredient.FLAT_CODEC.fieldOf("input_slot_1").forGetter(SmashRecipe::getInput1),
                ItemStack.STRICT_CODEC.fieldOf("main_result").forGetter(SmashRecipe::getMainOutput),
                ItemStack.OPTIONAL_CODEC.optionalFieldOf("secondary_result", ItemStack.EMPTY).forGetter(SmashRecipe::getSecondaryOutput),
                Codec.INT.optionalFieldOf("cookingtime", 200).forGetter(SmashRecipe::getCookTime)
        ).apply(inst, SmashRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, SmashRecipe> STREAM_CODEC = StreamCodec.of(
                (buf, recipe) -> {
                    SizedIngredient.STREAM_CODEC.encode(buf, recipe.input0);
                    SizedIngredient.STREAM_CODEC.encode(buf, recipe.input1);
                    ItemStack.STREAM_CODEC.encode(buf, recipe.mainOutput);
                    ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, recipe.secondaryOutput);
                    buf.writeInt(recipe.cookTime);
                },
                buf -> new SmashRecipe(
                        SizedIngredient.STREAM_CODEC.decode(buf),
                        SizedIngredient.STREAM_CODEC.decode(buf),
                        ItemStack.STREAM_CODEC.decode(buf),
                        ItemStack.OPTIONAL_STREAM_CODEC.decode(buf),
                        buf.readInt()
                )
        );

        @Override
        public MapCodec<SmashRecipe> codec() { return CODEC; }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SmashRecipe> streamCodec() { return STREAM_CODEC; }
    }
}
