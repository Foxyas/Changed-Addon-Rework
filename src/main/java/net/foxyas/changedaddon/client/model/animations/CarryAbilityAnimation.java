package net.foxyas.changedaddon.client.model.animations;

import net.foxyas.changedaddon.ability.CarryAbility;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.util.PlayerUtil;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CarryAbilityAnimation<T extends ChangedEntity, M extends AdvancedHumanoidModel<T>> extends HumanoidAnimator.Animator<T, M> {

    private final ModelPart head;
    private final ModelPart leftArm;
    private final ModelPart rightArm;

    public CarryAbilityAnimation(ModelPart head, ModelPart leftArm, ModelPart rightArm) {
        this.head = head;
        this.leftArm = leftArm;
        this.rightArm = rightArm;
    }

    @Override
    public HumanoidAnimator.AnimateStage preferredStage() {
        return HumanoidAnimator.AnimateStage.BOB; // recomendado para somar animações
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {

        handleCarryAnimation(entity);
    }

    /* =========================
       ======= CORE FLOW =======
       ========================= */

    private void handleCarryAnimation(ChangedEntity entity) {
        boolean carrySelected = hasCarryAbilitySelected(entity);
        if (entity.isSwimming()) {
            return;
        }

        if (hasPassenger(entity)) {
            applyCarryPose(entity);
            return;
        }

        if (carrySelected && canPrepareCarry(entity)) {
            applyAimPose(entity);
        }
    }

    /* =========================
       ======= CONDITIONS ======
       ========================= */

    private boolean hasCarryAbilitySelected(ChangedEntity entity) {
        return IAbstractChangedEntity.forEitherSafe(entity)
                .map(IAbstractChangedEntity::getTransfurVariantInstance)
                .map(TransfurVariantInstance::getSelectedAbility)
                .map(a -> a.ability instanceof CarryAbility)
                .orElse(false);
    }

    private boolean hasPassenger(ChangedEntity entity) {
        if (entity.getUnderlyingPlayer() != null) {
            return entity.getUnderlyingPlayer().getFirstPassenger() != null;
        }
        return entity.getFirstPassenger() != null;
    }

    private boolean canPrepareCarry(ChangedEntity entity) {
        if (entity.getUnderlyingPlayer() == null) return false;

        LivingEntity target = PlayerUtil.getEntityLookingAt(
                entity.getUnderlyingPlayer(), 3,
                PlayerUtil.BLOCK_COLLISION,
                LivingEntity.class
        );

        if (target == null) return false;

        return target.getType().is(ChangedTags.EntityTypes.HUMANOIDS)
                || target.getType().is(ChangedAddonTags.EntityTypes.CAN_CARRY);
    }

    /* =========================
       ====== ANIMATIONS =======
       ========================= */

    private void applyCarryPose(ChangedEntity entity) {
        liftArmIfFree(entity, entity.getMainArm());
        liftArmIfFree(entity, entity.getMainArm().getOpposite());
    }

    private void applyAimPose(ChangedEntity entity) {
        if (entity.getMainHandItem().isEmpty()) {
            aimArm(entity.getMainArm());
        }

        if (entity.getOffhandItem().isEmpty()) {
            aimArm(entity.getMainArm().getOpposite());
        }
    }

    /* =========================
       ===== ARM HELPERS =======
       ========================= */

    private void liftArmIfFree(ChangedEntity entity, HumanoidArm arm) {
        if (isHandFree(entity, arm)) {
            ModelPart armPart = getArm(arm);
            if (armPart == null) {
                return;
            }
            armPart.xRot = (float) Math.PI;
        }
    }

    private void aimArm(HumanoidArm arm) {
        ModelPart part = getArm(arm);
        if (part == null) {
            return;
        }
        part.xRot = -(float) Math.PI / 2F + head.xRot;
        part.yRot = (arm == HumanoidArm.LEFT ? 0.1F : -0.1F) + head.yRot;
    }

    private boolean isHandFree(ChangedEntity entity, HumanoidArm arm) {
        return arm == HumanoidArm.LEFT
                ? entity.getOffhandItem().isEmpty()
                : entity.getMainHandItem().isEmpty();
    }

    /* =========================
       ======= GETTERS =========
       ========================= */


    @Nullable
    public ModelPart getArm(HumanoidArm arm) {
        return arm == HumanoidArm.LEFT ? leftArm : rightArm;
    }

    public ModelPart getHead() {
        return head;
    }
}