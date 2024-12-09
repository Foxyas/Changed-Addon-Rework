package net.foxyas.changedaddon.mixins;

import net.foxyas.changedaddon.ability.ChangedAddonAbilitys;
import net.foxyas.changedaddon.ability.WingFlapAbility;
import net.ltxprogrammer.changed.client.renderer.animate.wing.AbstractWingAnimatorV2;
import net.ltxprogrammer.changed.client.renderer.animate.wing.DragonWingFallFlyAnimator;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DragonWingFallFlyAnimator.class,remap = false)
public class WingAnimationMixin {

    @Inject(method = "setupAnim",at = @At("TAIL"))
    private void WingAnimation(@NotNull ChangedEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci){
        if (entity.getUnderlyingPlayer() != null && ProcessTransfur.getPlayerTransfurVariant(entity.getUnderlyingPlayer()) != null){
            TransfurVariantInstance<?> variantInstance = ProcessTransfur.getPlayerTransfurVariant(entity.getUnderlyingPlayer());
            if (variantInstance.hasAbility(ChangedAddonAbilitys.WING_FLAP_ABILITY.get())
                    && variantInstance.getAbilityInstance(ChangedAddonAbilitys.WING_FLAP_ABILITY.get()).canUse()){
                WingFlapAbility.AbilityInstance WingFlapAbilityInstance = variantInstance.getAbilityInstance(ChangedAddonAbilitys.WING_FLAP_ABILITY.get());
                float maxRotation = (Math.min(20, 20 * (WingFlapAbilityInstance.getController().getHoldTicks() / WingFlapAbility.TICK_HOLD_NEED)));
                ((DragonWingFallFlyAnimator<?, ?>) (Object) this).leftWingRoot.zRot = -maxRotation * Mth.DEG_TO_RAD;
                ((DragonWingFallFlyAnimator<?, ?>) (Object) this).rightWingRoot.zRot = maxRotation * Mth.DEG_TO_RAD;
            }
        }
    }
}
