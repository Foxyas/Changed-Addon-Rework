package net.foxyas.changedaddon.client.model.animations;

import net.foxyas.changedaddon.ability.WingFlapAbility;
import net.foxyas.changedaddon.entity.advanced.AvaliEntity;
import net.foxyas.changedaddon.init.ChangedAddonAbilities;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator.AnimateStage;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

public class AvaliFallFlyAnimator<T extends ChangedEntity, M extends AdvancedHumanoidModel<T>> extends HumanoidAnimator.Animator<T, M> {

    public static final float FALL_FLY_ROTATION = 20f;
    private final ModelPart rightArm;
    private final ModelPart leftArm;

    public AvaliFallFlyAnimator(ModelPart rightArm, ModelPart leftArm) {
        super();
        this.rightArm = rightArm;
        this.leftArm = leftArm;
    }

    @Override
    public AnimateStage preferredStage() {
        return AnimateStage.FALL_FLY;
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float ticks = (float) entity.getFallFlyingTicks();
        // Aplicamos uma curva ease-in-out para suavizar o início e fim
        float t = Mth.clamp(ticks / 15.0F, 0.0F, 1.0F);
        float flyAmount = smootherStep(t); // Muito mais suave!

        AtomicReference<Float> flapProgress = new AtomicReference<>(0.0f);

        Player player = entity.getUnderlyingPlayer();
        if (player != null) {
            var tf = ProcessTransfur.getPlayerTransfurVariant(player);
            if (tf == null || !(tf.getChangedEntity() instanceof AvaliEntity)) {
                return;
            }

            if (tf.hasAbility(ChangedAddonAbilities.WING_FLAP_ABILITY.get())) {
                tf.ifHasAbility(ChangedAddonAbilities.WING_FLAP_ABILITY.get(), ability -> {
                    if (!ability.canUse()) return;
                    if (player.getAbilities().flying) return;

                    float raw = ability.getController().getHoldTicks() / (float) WingFlapAbility.MAX_TICK_HOLD;

                    // clamp + easing leve
                    flapProgress.set(Mth.clamp(raw, 0.0f, 1.0f));
                });
            }
        }

        // Quanto o flap influencia (20° → 30°)
        float flapDeg = FALL_FLY_ROTATION + flapProgress.get() * 10.0f;

        // =========================
        // SEM FLAP (activation <= 0)
        // =========================
        if (flapProgress.get() <= 0.0f) {
            this.rightArm.xRot = Mth.lerp(flyAmount, this.rightArm.xRot, Mth.HALF_PI); // 90°
            this.rightArm.yRot = Mth.lerp(flyAmount, this.rightArm.yRot, (float) Math.toRadians(90.0f - FALL_FLY_ROTATION));
            this.rightArm.zRot = Mth.lerp(flyAmount, this.rightArm.zRot, (float) Math.toRadians(180.0f));
            this.leftArm.xRot = Mth.lerp(flyAmount, this.leftArm.xRot, Mth.HALF_PI); // 90°
            this.leftArm.yRot = Mth.lerp(flyAmount, this.leftArm.yRot, (float) Math.toRadians(-90.0f + FALL_FLY_ROTATION));
            this.leftArm.zRot = Mth.lerp(flyAmount, this.leftArm.zRot, (float) Math.toRadians(-180.0f));
            return;
        }

        // =========================
        // COM FLAP (activation > 0)
        // =========================
        this.rightArm.xRot = Mth.lerp(flyAmount, this.rightArm.xRot, Mth.HALF_PI); // 90°
        this.rightArm.yRot = Mth.lerp(flyAmount, this.rightArm.yRot, (float) Math.toRadians(90.0f - flapDeg));
        this.rightArm.zRot = Mth.lerp(flyAmount, this.rightArm.zRot, (float) Math.toRadians(180.0f));
        this.leftArm.xRot = Mth.lerp(flyAmount, this.leftArm.xRot, Mth.HALF_PI); // 90°
        this.leftArm.yRot = Mth.lerp(flyAmount, this.leftArm.yRot, (float) Math.toRadians(-90.0f + flapDeg));
        this.leftArm.zRot = Mth.lerp(flyAmount, this.leftArm.zRot, (float) Math.toRadians(-180.0f));
    }

    float smootherStep(float t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

}
