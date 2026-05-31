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

public class BlendRecipe implements Recipe<RecipeInput> {
    private final NonNullList<Ingredient> inputs; // 支持 1~2 个输入
    private final ItemStack output; // 支持 1~2 个输出
    private final int blendTime;                  // 粉碎需要的时间（Tick）

    public BlendRecipe(NonNullList<Ingredient> inputs, ItemStack output, int blendTime) {
        this.inputs = inputs;
        this.output = output;
        this.blendTime = blendTime;
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

    public NonNullList<ItemStack> getOutputs() { return NonNullList.of(ItemStack.EMPTY, this.output); }
    public int getblendTime() { return this.blendTime; }

    @Override
    public ItemStack assemble(RecipeInput input, HolderLookup.Provider provider) {
        return this.output.copy(); // 原版遗留接口，返回主产物即可
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) { return true; }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.output.isEmpty() ? ItemStack.EMPTY : this.output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() { return ModRecipes.BLEND_SERIALIZER.get(); }

    @Override
    public RecipeType<?> getType() { return ModRecipes.BLEND_TYPE.get(); }

    // ==================== 1.21.1 核心：编解码器 ====================
    public static class Serializer implements RecipeSerializer<BlendRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        // 1. MapCodec 重构：将 outputs 更改为单个 ItemStack.CODEC
        public static final MapCodec<BlendRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.listOf().fieldOf("inputs").xmap(
                        list -> NonNullList.of(Ingredient.EMPTY, list.toArray(new Ingredient[0])),
                        list -> list
                ).forGetter(recipe -> recipe.inputs),
                // 核心修改：这里不再是 listOf()，而是直接使用单个 fieldOf("output")
                ItemStack.CODEC.fieldOf("output").forGetter(recipe -> recipe.output),
                Codec.INT.optionalFieldOf("blend_time", 200).forGetter(recipe -> recipe.blendTime)
        ).apply(instance, BlendRecipe::new));

        // 2. StreamCodec 网络同步重构：不再循环列表，直接点对点读写单个 ItemStack
        public static final StreamCodec<RegistryFriendlyByteBuf, BlendRecipe> STREAM_CODEC = StreamCodec.of(
                (buf, recipe) -> {
                    // 写输入列表大小及内容（保持不变，你的输入依然是主体和注入物2个）
                    buf.writeInt(recipe.inputs.size());
                    for (Ingredient ing : recipe.inputs) Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ing);
                    
                    // 核心修改：直接编码这唯一的一个产物堆叠
                    ItemStack.STREAM_CODEC.encode(buf, recipe.output);
                    
                    buf.writeInt(recipe.blendTime);
                },
                buf -> {
                    int inputSize = buf.readInt();
                    NonNullList<Ingredient> inputs = NonNullList.withSize(inputSize, Ingredient.EMPTY);
                    for (int i = 0; i < inputSize; i++) inputs.set(i, Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
                    
                    // 核心修改：直接解码这唯一的一个产物堆叠
                    ItemStack output = ItemStack.STREAM_CODEC.decode(buf);
                    
                    int blendTime = buf.readInt();
                    return new BlendRecipe(inputs, output, blendTime); // 传入单产物构造函数
                }
        );
        
        @Override
        public MapCodec<BlendRecipe> codec() { return CODEC; }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BlendRecipe> streamCodec() { return STREAM_CODEC; }
    }
}
