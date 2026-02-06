package net.foxyas.changedaddon.mixins.items;

import net.foxyas.changedaddon.variant.VariantExtraStats;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ItemStack.class, priority = 1001)
public abstract class ItemStackMixin implements IForgeItemStack {

    @Shadow public abstract Item getItem();

    public boolean canElytraFly(LivingEntity entity) {
        ItemStack self = (ItemStack) (Object) this;
        boolean variantCanFly = ProcessTransfur.getPlayerTransfurVariantSafe(EntityUtil.playerOrNull(entity)).map((variant) -> {
            if (variant.getChangedEntity() instanceof VariantExtraStats extraStats) {
                return extraStats.getFlyType().canGlide();
            }

            return variant.getParent().canGlide;
        }).orElse(false);
        return variantCanFly || this.getItem().canElytraFly(self, entity);
    }

    public boolean elytraFlightTick(LivingEntity entity, int flightTicks) {
        ItemStack self = (ItemStack) (Object) this;
        boolean variantCanFly = ProcessTransfur.getPlayerTransfurVariantSafe(EntityUtil.playerOrNull(entity)).map((variant) -> {
            if (variant.getChangedEntity() instanceof VariantExtraStats extraStats) {
                return extraStats.getFlyType().canGlide();
            }

            return variant.getParent().canGlide;
        }).orElse(false);
        return variantCanFly || this.getItem().elytraFlightTick(self, entity, flightTicks);
    }
}
