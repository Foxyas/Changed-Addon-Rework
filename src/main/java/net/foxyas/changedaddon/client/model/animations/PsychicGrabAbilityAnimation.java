package net.foxyas.changedaddon.client.model.animations;

import net.foxyas.changedaddon.init.ChangedAddonAbilities;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

public class PsychicGrabAbilityAnimation<T extends ChangedEntity, M extends AdvancedHumanoidModel<T>>
        extends HumanoidAnimator.Animator<T, M> {

    private final ModelPart head;
    private final ModelPart leftArm;
    private final ModelPart rightArm;

    public PsychicGrabAbilityAnimation(ModelPart head, ModelPart leftArm, ModelPart rightArm) {
        this.head = head;
        this.leftArm = leftArm;
        this.rightArm = rightArm;
    }

    @Override
    public HumanoidAnimator.AnimateStage preferredStage() {
        return HumanoidAnimator.AnimateStage.BOB; // soma com animações base
    }

    @Override
    public void setupAnim(@NotNull T entity,
                          float limbSwing,
                          float limbSwingAmount,
                          float ageInTicks,
                          float netHeadYaw,
                          float headPitch) {

        if (!shouldAnimate(entity)) {
            return;
        }

        HumanoidArm arm = resolveUsedArm(entity);
        applyPsychicGrabPose(entity, arm);
    }

    /* =========================
       ======= CONDITIONS ======
       ========================= */

    private boolean shouldAnimate(ChangedEntity entity) {
        if (entity.getUnderlyingPlayer() == null) {
            return false;
        }

        return ProcessTransfur.getPlayerTransfurVariantSafe(entity.getUnderlyingPlayer())
                .map(transfur -> {
                    AbstractAbilityInstance ability =
                            transfur.getAbilityInstance(ChangedAddonAbilities.PSYCHIC_GRAB.get());

                    return ability != null && ability.getController().getHoldTicks() > 0;
                })
                .orElse(false);
    }

    /* =========================
       ===== ARM RESOLUTION =====
       ========================= */

    private HumanoidArm resolveUsedArm(ChangedEntity entity) {
        InteractionHand usedHand;

        if (entity.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            usedHand = InteractionHand.MAIN_HAND;
        } else if (entity.getItemInHand(InteractionHand.OFF_HAND).isEmpty()) {
            usedHand = InteractionHand.OFF_HAND;
        } else {
            usedHand = InteractionHand.MAIN_HAND;
        }

        HumanoidArm arm = entity.getMainArm();
        return usedHand == InteractionHand.OFF_HAND ? arm.getOpposite() : arm;
    }

    /* =========================
       ====== ANIMATION ========
       ========================= */

    private void applyPsychicGrabPose(ChangedEntity entity, HumanoidArm arm) {
        float crouchOffset = entity.isCrouching() ? 0.2617994F : 0.0F;

        ModelPart armPart = getArm(arm);
        ModelPart headPart = getHead();
        if (armPart == null || headPart == null) {
            return;
        }

        armPart.xRot =
                headPart.xRot - (float) Math.PI / 2F - crouchOffset;

        armPart.yRot =
                headPart.yRot;
    }

    /* =========================
       ======= GETTERS =========
       ========================= */

    public ModelPart getArm(HumanoidArm arm) {
        return arm == HumanoidArm.LEFT ? leftArm : rightArm;
    }

    public ModelPart getHead() {
        return head;
    }
}