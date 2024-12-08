package net.foxyas.changedaddon.mixins;

import net.ltxprogrammer.changed.entity.beast.AbstractDarkLatexEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AbstractDarkLatexEntity.class,remap = false)
public class AbstractDarkLatexEntityMixin {

    @Inject(method = "targetSelectorTest", at = @At("HEAD"), cancellable = true)
    private void CancelTargetIfNotMoving(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir){
        if (livingEntity instanceof Player player && (player.getDeltaMovement().length() == 0)){
            cir.setReturnValue(false);
        }
    }

}
