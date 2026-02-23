package net.foxyas.changedaddon.mixins.entity.goals;

import net.minecraft.world.entity.ai.sensing.VillagerHostilesSensor;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(VillagerHostilesSensor.class)
public abstract class VillagerHostilesSensorMixin {

//    @Inject(method = "isClose", at = @At("HEAD"), cancellable = true)
//    private void isClose(LivingEntity villager, LivingEntity hostile, CallbackInfoReturnable<Boolean> callback) {
//        if (hostile instanceof ChangedEntity latex) {
//            if (latex.getType().is(EntityTypes.LATEX) && latex.hasEffect(ChangedAddonMobEffects.PACIFIED.get())) {
//                callback.setReturnValue(false);
//            } else if (latex.getType().is(EntityTypes.LATEX) && ChangedEntityExtension.of(latex).isPacified()) {
//                callback.setReturnValue(false);
//            }
//        }
//    }
//
//    @Inject(method = "isHostile", at = @At("HEAD"), cancellable = true)
//    private void isHostile(LivingEntity hostile, CallbackInfoReturnable<Boolean> callback) {
//        if (hostile instanceof ChangedEntity latex) {
//            if (latex.getType().is(EntityTypes.LATEX) && latex.hasEffect(ChangedAddonMobEffects.PACIFIED.get())) {
//                callback.setReturnValue(false);
//            } else if (latex.getType().is(EntityTypes.LATEX) && ChangedEntityExtension.of(latex).isPacified()) {
//                callback.setReturnValue(false);
//            }
//        }
//
//    }
}
