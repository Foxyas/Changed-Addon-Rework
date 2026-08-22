package net.foxyas.changedaddon.client.model.animations;

import net.foxyas.changedaddon.init.ChangedAddonAbilities;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator.AnimateStage;
import net.ltxprogrammer.changed.client.renderer.animate.upperbody.AbstractUpperBodyAnimator;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static net.foxyas.changedaddon.ability.WingFlapAbility.*;

@SuppressWarnings("deprecation")
public class AvaliUpperBodyInitAnimator<T extends ChangedEntity, M extends AdvancedHumanoidModel<T>> extends AbstractUpperBodyAnimator<T, M> {

    public static final float BOB_AMPLITUDE = (float) Math.toRadians(4);

    public AvaliUpperBodyInitAnimator(ModelPart head, ModelPart torso, ModelPart leftArm, ModelPart rightArm) {
        super(head, torso, leftArm, rightArm);
    }

    // Função de suavização
    public static float easeInOut(float t) {
        return t * t * (3 - 2 * t);
    }

    public static float easeOutCubic(float t) {
        return 1 - (float) Math.pow(1 - t, 3);
    }

    // Method para limitar o valor entre min e max
    public static float capLevel(float value, float min, float max) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        }
        return value;
    }

    public HumanoidAnimator.AnimateStage preferredStage() {
        return AnimateStage.INIT;
    }

    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        boolean fallFlying = entity.getFallFlyingTicks() > 4;
        this.torso.yRot = 0.0F;
        this.torso.zRot = 0.0F;
        this.rightArm.z = 0.0F;
        this.rightArm.x = -this.core.torsoWidth;
        this.leftArm.z = 0.0F;
        this.leftArm.x = this.core.torsoWidth;
        float f = 1.0F;
        if (fallFlying) {
            this.rightArm.xRot = 0.0f;
            this.leftArm.xRot = 0.0f;
            this.rightArm.yRot = 0.0f;
            this.leftArm.yRot = 0.0f;
            this.rightArm.zRot = 0.0F;
            this.leftArm.zRot = 0.0F;
        } else {
            this.rightArm.zRot = 0.0F;
            this.leftArm.zRot = 0.0F;
            this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F / f;
            this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
            this.rightArm.zRot += Mth.lerp(this.core.reachOut, 0.0F, 0.1745329F);
            this.leftArm.zRot += Mth.lerp(this.core.reachOut, 0.0F, -0.1745329F);
            this.rightArm.xRot = Mth.lerp(this.core.reachOut, this.rightArm.xRot, (-(float) Math.PI / 6F));
            this.leftArm.xRot = Mth.lerp(this.core.reachOut, this.leftArm.xRot, (-(float) Math.PI / 6F));
        }

        if (entity.getUnderlyingPlayer() != null && ProcessTransfur.getPlayerTransfurVariant(entity.getUnderlyingPlayer()) != null) { // Just a Fail Safe Check
            TransfurVariantInstance<?> variantInstance = ProcessTransfur.getPlayerTransfurVariant(entity.getUnderlyingPlayer());
            variantInstance.ifHasAbility(ChangedAddonAbilities.WING_FLAP_ABILITY.get(), (instance) -> {
                if (!instance.canUse()) return;
                if (entity.getUnderlyingPlayer().getAbilities().flying) return;
                if (entity.isFallFlying()) return;

                // Aplicação no cálculo da rotação
                float progress = instance.getController().getHoldTicks() / (float) MAX_TICK_HOLD;
                float easedProgress = easeOutCubic(progress); // Aplica suavização

                // Interpolação suave
                this.rightArm.yRot = Mth.lerp(easedProgress, this.rightArm.yRot, AVALI_WING_FLAP_TARGET_Y);
                this.rightArm.zRot = Mth.lerp(easedProgress, this.rightArm.zRot, AVALI_WING_FLAP_TARGET_Z);

                this.leftArm.yRot = Mth.lerp(easedProgress, this.leftArm.yRot, -AVALI_WING_FLAP_TARGET_Y);
                this.leftArm.zRot = Mth.lerp(easedProgress, this.leftArm.zRot, -AVALI_WING_FLAP_TARGET_Z);

                // aplica bob quando chegou no ready
                if (progress >= 1.0f) {
                    applyBob(this.rightArm, this.leftArm, entity, ageInTicks);
                }
            });
        }

    }

    public void applyBob(ModelPart rightArm, ModelPart leftArm, LivingEntity entity, float ageInTicks) {
        final float BOB_SPEED = 0.25f;

        float time = entity.tickCount + ageInTicks;
        float bob = Mth.sin(time * BOB_SPEED) * BOB_AMPLITUDE;

        // aplica em Z (mais natural pra "tensão")
        rightArm.zRot = capLevel(rightArm.zRot + bob, AVALI_WING_FLAP_TARGET_Z - BOB_AMPLITUDE, AVALI_WING_FLAP_TARGET_Z + BOB_AMPLITUDE);
        leftArm.zRot = capLevel(leftArm.zRot - bob, -AVALI_WING_FLAP_TARGET_Z - BOB_AMPLITUDE, -AVALI_WING_FLAP_TARGET_Z + BOB_AMPLITUDE);
    }
}
