package net.foxyas.changedaddon.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import org.jetbrains.annotations.NotNull;

import net.minecraft.util.Mth;

import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.client.renderer.animate.wing.DragonWingFallFlyAnimator;

import net.foxyas.changedaddon.abilities.WingFlapAbility;
import net.foxyas.changedaddon.abilities.ChangedAddonAbilities;

@Mixin(value = DragonWingFallFlyAnimator.class, remap = false)
public class WingFallFlyAnimationMixin {

	@Inject(method = "setupAnim", at = @At("TAIL"))
	private void WingAnimation(@NotNull ChangedEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
		if (entity.getUnderlyingPlayer() != null && ProcessTransfur.getPlayerTransfurVariant(entity.getUnderlyingPlayer()) != null) {
			TransfurVariantInstance<?> variantInstance = ProcessTransfur.getPlayerTransfurVariant(entity.getUnderlyingPlayer());
			if (variantInstance.hasAbility(ChangedAddonAbilities.WING_FLAP_ABILITY.get()) && variantInstance.getAbilityInstance(ChangedAddonAbilities.WING_FLAP_ABILITY.get()).canUse()
					&& variantInstance.getSelectedAbility() instanceof WingFlapAbility.AbilityInstance WingFlapAbilityInstance) {
				if (entity.getUnderlyingPlayer().getAbilities().flying){
					return;
				}

				// Aplicação no cálculo da rotação
				float progress = WingFlapAbilityInstance.getController().getHoldTicks() / (float) WingFlapAbility.MAX_TICK_HOLD;
				float easedProgress = easeOutCubic(progress); // Aplica suavização
				float maxRotation = capLevel(35 * easedProgress, 0, 35); // Aplica o level cap

				// Interpolação suave
				((DragonWingFallFlyAnimator<?, ?>) (Object) this).leftWingRoot.zRot = -maxRotation * Mth.DEG_TO_RAD;
				((DragonWingFallFlyAnimator<?, ?>) (Object) this).rightWingRoot.zRot = maxRotation * Mth.DEG_TO_RAD;
			}
		}
	}

	// Função de suavização
	private static float easeInOut(float t) {
		return t * t * (3 - 2 * t);
	}

	private static float easeOutCubic(float t) {
    return 1 - (float) Math.pow(1 - t, 3);
	}


	// Método para limitar o valor entre min e max
	private static float capLevel(float value, float min, float max) {
		if (value < min) {
			return min;
		} else if (value > max) {
			return max;
		}
		return value;
	}
}