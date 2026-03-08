package net.foxyas.changedaddon.mixins.entity.elytraFly;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.foxyas.changedaddon.variant.VariantExtraStats;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = Player.class, priority = 1001)
public class PlayerMixin {

//    @WrapOperation(
//            method = "tryToStartFallFlying",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/world/item/ItemStack;canElytraFly(Lnet/minecraft/world/entity/LivingEntity;)Z",
//                    remap = false
//            )
//    )
//    private boolean changedaddon$canElytraFlyRedirect(ItemStack instance, LivingEntity living, Operation<Boolean> original) {
//        Player self = (Player) (Object) this;
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

    @WrapOperation(method = "causeFallDamage", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Abilities;mayfly:Z", opcode = Opcodes.GETFIELD))
    public boolean changed$shouldIgnoreFallDamage(Abilities instance, Operation<Boolean> original) {
        var self = (Player) (Object) this;
        TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(self);
        if (transfurVariant != null && transfurVariant.getChangedEntity() instanceof VariantExtraStats variantExtraStats)
            return original.call(instance) && !variantExtraStats.shouldTakeFallDamage();
        return original.call(instance);
    }

}
