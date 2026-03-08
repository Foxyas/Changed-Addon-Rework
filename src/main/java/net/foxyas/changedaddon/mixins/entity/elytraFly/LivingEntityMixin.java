package net.foxyas.changedaddon.mixins.entity.elytraFly;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.foxyas.changedaddon.variant.VariantExtraStats;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = LivingEntity.class, priority = 1001)
public class LivingEntityMixin {

//    @WrapOperation(
//            method = "updateFallFlying",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/world/item/ItemStack;canElytraFly(Lnet/minecraft/world/entity/LivingEntity;)Z",
//                    remap = false
//            )
//    )
//    private boolean changedaddon$canElytraFlyRedirect(ItemStack instance, LivingEntity living, Operation<Boolean> original) {
//        LivingEntity self = (LivingEntity) (Object) this;
//        TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(EntityUtil.playerOrNull(self));
//        if (transfurVariant == null) return original.call(instance, living);
//        if (instance.getItem() instanceof ElytraItem) {
//            return original.call(instance, living);
//        }
//
//        if (transfurVariant.getChangedEntity() instanceof VariantExtraStats variantExtraStats) {
//            return variantExtraStats.getFlyType().canGlide();
//        }
//
//        return original.call(instance, living);
//    } // Todo Delete this in 0.15.1
//
//    @WrapOperation(
//            method = "updateFallFlying",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/world/item/ItemStack;elytraFlightTick(Lnet/minecraft/world/entity/LivingEntity;I)Z",
//                    remap = false
//            )
//    )
//    private boolean changedaddon$elytraFlightTickRedirect(
//            ItemStack instance, LivingEntity living, int fallFlyTicks, Operation<Boolean> original
//    ) {
//        LivingEntity self = (LivingEntity) (Object) this;
//        TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(EntityUtil.playerOrNull(self));
//        if (transfurVariant == null) return original.call(instance, living, fallFlyTicks);
//        if (instance.getItem() instanceof ElytraItem) {
//            return original.call(instance, living, fallFlyTicks);
//        }
//
//        if (transfurVariant.getChangedEntity() instanceof VariantExtraStats variantExtraStats) {
//            return variantExtraStats.getFlyType().canGlide();
//        }
//
//        return original.call(instance, living, fallFlyTicks);
//    } // Todo Delete this in 0.15.1
}
