package net.foxyas.changedaddon.mixins.mods.nean;

import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.animations.vanilla.ElytraAnimation;
import dev.tr7zw.notenoughanimations.versionless.animations.BodyPart;
import net.foxyas.changedaddon.ability.WingFlapAbility;
import net.foxyas.changedaddon.client.model.animations.AvaliFallFlyAnimator;
import net.foxyas.changedaddon.entity.advanced.AvaliEntity;
import net.foxyas.changedaddon.extension.RequiredMods;
import net.foxyas.changedaddon.init.ChangedAddonAbilities;
import net.foxyas.changedaddon.variant.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicReference;

@Mixin(value = ElytraAnimation.class, remap = false)
@RequiredMods("notenoughanimations")
public class AvaliElytraAnimationMixin {

    @Inject(method = "apply", at = @At("HEAD"), cancellable = true)
    private void injectAvaliAnimation(
            AbstractClientPlayer entity,
            PlayerData data,
            PlayerModel<AbstractClientPlayer> model,
            BodyPart part,
            float delta,
            float tickCounter,
            CallbackInfo ci
    ) {
        var tf = ProcessTransfur.getPlayerTransfurVariant(entity);
        if (tf == null || !(tf.getChangedEntity() instanceof AvaliEntity) || !tf.is(ChangedAddonTransfurVariants.AVALI)) {
            return;
        }

        // Só queremos controlar os braços
        if (part != BodyPart.LEFT_ARM && part != BodyPart.RIGHT_ARM) {
            return;
        }

        // Cancela a animação padrão do NEA
        ci.cancel();

        AtomicReference<Float> flapProgress = new AtomicReference<>(0.0f);

        if (tf.hasAbility(ChangedAddonAbilities.WING_FLAP_ABILITY.get())) {
            tf.ifHasAbility(ChangedAddonAbilities.WING_FLAP_ABILITY.get(), ability -> {
                if (!ability.canUse()) return;
                if (entity.getAbilities().flying) return;

                float raw = ability.getController().getHoldTicks()
                        / (float) WingFlapAbility.MAX_TICK_HOLD;

                // clamp + easing leve
                flapProgress.set(Mth.clamp(raw, 0.0f, 1.0f));
            });
        }

        // Quanto o flap influencia (20 → 30 graus)
        float flapDeg = 20.0f + flapProgress.get() * 10.0f;

        float t = Mth.clamp(tickCounter * 0.6662F, 0.0F, 1.0F);
        float flyAmount = changedAddon$smootherStep(t); // Muito mais suave!


        // =========================
        // SEM FLAP (activation <= 0)
        // =========================
        if (flapProgress.get() <= 0.0f) {
            if (part == BodyPart.RIGHT_ARM) {
                model.rightArm.xRot = Mth.lerp(flyAmount, model.rightArm.xRot, Mth.HALF_PI); // 90°
                model.rightArm.yRot = Mth.lerp(flyAmount, model.rightArm.yRot, (float) Math.toRadians(90.0f - AvaliFallFlyAnimator.FALL_FLY_ROTATION));
                model.rightArm.zRot = Mth.lerp(flyAmount, model.rightArm.zRot, (float) Math.toRadians(180.0f));
            } else {
                model.leftArm.xRot = Mth.lerp(flyAmount, model.leftArm.xRot, Mth.HALF_PI); // 90°
                model.leftArm.yRot = Mth.lerp(flyAmount, model.leftArm.yRot, (float) Math.toRadians(-90.0f + AvaliFallFlyAnimator.FALL_FLY_ROTATION));
                model.leftArm.zRot = Mth.lerp(flyAmount, model.leftArm.zRot, (float) Math.toRadians(-180.0f));
            }
            return;
        }

        // =========================
        // COM FLAP (activation > 0)
        // =========================
        model.rightArm.xRot = Mth.lerp(flyAmount, model.rightArm.xRot, Mth.HALF_PI); // 90°
        model.rightArm.yRot = Mth.lerp(flyAmount, model.rightArm.yRot, (float) Math.toRadians(90.0f - flapDeg));
        model.rightArm.zRot = Mth.lerp(flyAmount, model.rightArm.zRot, (float) Math.toRadians(180.0f));
        model.leftArm.xRot = Mth.lerp(flyAmount, model.leftArm.xRot, Mth.HALF_PI); // 90°
        model.leftArm.yRot = Mth.lerp(flyAmount, model.leftArm.yRot, (float) Math.toRadians(-90.0f + flapDeg));
        model.leftArm.zRot = Mth.lerp(flyAmount, model.leftArm.zRot, (float) Math.toRadians(-180.0f));
    }


    @Unique
    private float changedAddon$smootherStep(float t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }
}
