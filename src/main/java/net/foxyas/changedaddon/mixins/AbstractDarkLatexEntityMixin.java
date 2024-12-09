package net.foxyas.changedaddon.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;

import net.ltxprogrammer.changed.entity.beast.AbstractDarkLatexEntity;

@Mixin(value = AbstractDarkLatexEntity.class, remap = false)
public class AbstractDarkLatexEntityMixin {
	@Inject(method = "targetSelectorTest", at = @At("HEAD"), cancellable = true)
	private void CancelTargetIfNotMoving(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
		if (livingEntity instanceof Player player) {
			if (player.getDeltaMovement().length() == 0 && player.swingTime <= 0) {
				cir.setReturnValue(false);
			}
		}
	}
}
