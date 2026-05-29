package com.icthss.smasher.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class SmashRecipe implements Recipe<RecipeInput> {
    private final NonNullList<Ingredient> inputs; // 支持 1~2 个输入
    private final NonNullList<ItemStack> outputs; // 支持 1~2 个输出
    private final int smashTime;                  // 粉碎需要的时间（Tick）

    public SmashRecipe(NonNullList<Ingredient> inputs, NonNullList<ItemStack> outputs, int smashTime) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.smashTime = smashTime;
    }

    // 匹配核心：检查方块实体里的物品是否符合该 JSON 配方
    @Override
    public boolean matches(RecipeInput input, Level level) {
        if (inputs.isEmpty()) return false;
        
        // 由于是输入 1~2 个物品，我们需要做双向匹配检查
        ItemStack slot0 = input.getItem(0);
        ItemStack slot1 = input.getItem(1);

        if (inputs.size() == 1) {
            return inputs.get(0).test(slot0) && slot1.isEmpty();
        } else {
            boolean matchNormal = inputs.get(0).test(slot0) && inputs.get(1).test(slot1);
            boolean matchReversed = inputs.get(1).test(slot0) && inputs.get(0).test(slot1);
            return matchNormal || matchReversed;
        }
    }

    public NonNullList<ItemStack> getOutputs() { return this.outputs; }
    public int getSmashTime() { return this.smashTime; }

    @Override
    public ItemStack assemble(RecipeInput input, HolderLookup.Provider provider) {
        return this.outputs.get(0).copy(); // 原版遗留接口，返回主产物即可
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) { return true; }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.outputs.isEmpty() ? ItemStack.EMPTY : this.outputs.get(0);
    }

    @Override
    public RecipeSerializer<?> getSerializer() { return ModRecipes.SMASH_SERIALIZER.get(); }

    @Override
    public RecipeType<?> getType() { return ModRecipes.SMASH_TYPE.get(); }

    // ==================== 1.21.1 核心：编解码器 ====================
    public static class Serializer implements RecipeSerializer<SmashRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        public static final MapCodec<SmashRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.listOf().fieldOf("inputs").xmap(
                        list -> NonNullList.of(Ingredient.EMPTY, list.toArray(new Ingredient[0])),
                        list -> list
                ).forGetter(recipe -> recipe.inputs),
                ItemStack.CODEC.listOf().fieldOf("outputs").xmap(
                        list -> NonNullList.of(ItemStack.EMPTY, list.toArray(new ItemStack[0])),
                        list -> list
                ).forGetter(recipe -> recipe.outputs),
            Codec.INT.optionalFieldOf("smash_time", 200).forGetter(recipe -> recipe.smashTime)
        ).apply(instance, SmashRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, SmashRecipe> STREAM_CODEC = StreamCodec.of(
                (buf, recipe) -> {
                    buf.writeInt(recipe.inputs.size());
                    for (Ingredient ing : recipe.inputs) Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ing);
                    buf.writeInt(recipe.outputs.size());
                    for (ItemStack stack : recipe.outputs) ItemStack.STREAM_CODEC.encode(buf, stack);
                    buf.writeInt(recipe.smashTime);
                },
                buf -> {
                    int inputSize = buf.readInt();
                    NonNullList<Ingredient> inputs = NonNullList.withSize(inputSize, Ingredient.EMPTY);
                    for (int i = 0; i < inputSize; i++) inputs.set(i, Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
                    
                    int outputSize = buf.readInt();
                    NonNullList<ItemStack> outputs = NonNullList.withSize(outputSize, ItemStack.EMPTY);
                    for (int i = 0; i < outputSize; i++) outputs.set(i, ItemStack.STREAM_CODEC.decode(buf));
                    
                    int smashTime = buf.readInt();
                    return new SmashRecipe(inputs, outputs, smashTime);
                }
        );

        @Override
        public MapCodec<SmashRecipe> codec() { return CODEC; }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SmashRecipe> streamCodec() { return STREAM_CODEC; }
    }
}
