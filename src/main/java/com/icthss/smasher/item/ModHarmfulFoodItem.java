import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;

public class ModHarmfulFoodItem extends Item {
    
    private final ResourceLocation hpModifierId; // 已经是唯一的
    private final double hpPenaltyValue;

    // 🔴 关键修改：直接传入完整的 ResourceLocation
    public ModHarmfulFoodItem(Properties properties, ResourceLocation modifierId, double hpPenaltyValue) {
        super(properties);
        this.hpModifierId = modifierId;
        this.hpPenaltyValue = hpPenaltyValue;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack resultStack = super.finishUsingItem(stack, level, entity);

        if (!level.isClientSide() && entity instanceof ServerPlayer player) {
            
            // 瞬时扣血

            // 永久扣除生命值上限（仅限首次食用）
            AttributeInstance maxHealthAttr = player.getAttribute(Attributes.MAX_HEALTH);
            if (maxHealthAttr != null) {
                // 精准检查这个独立唯一的 ID
                if (!maxHealthAttr.hasModifier(this.hpModifierId)) {
                    
                    maxHealthAttr.addPermanentModifier(new AttributeModifier(
                            this.hpModifierId,
                            -this.hpPenaltyValue,
                            AttributeModifier.Operation.ADD_VALUE
                    ));
                    
                    // 动态匹配语言键名：如 "message.your_mod_id.first_eat_poison_apple"
                    player.sendSystemMessage(Component.translatable("message.your_mod_id.first_eat_" + this.hpModifierId.getPath()));
                }
            }
        }

        return resultStack;
    }
}
