package net.foxyas.changedaddon.client.model.animations;

import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class MagicAttackCastingAnimator<T extends ChangedEntity, M extends AdvancedHumanoidModel<T>> extends HumanoidAnimator.Animator<T, M> {

    protected final ModelPart head;
    protected final ModelPart rightArm;
    protected final ModelPart leftArm;
    protected final ModelPart leftLeg;
    protected final ModelPart rightLeg;
    private final Predicate<T> predicate;

    public MagicAttackCastingAnimator(ModelPart head, ModelPart rightArm, ModelPart leftArm, ModelPart rightLeg, ModelPart leftLeg, Predicate<T> whenStartUsing) {
        this.head = head;
        this.rightArm = rightArm;
        this.leftArm = leftArm;
        this.rightLeg = rightLeg;
        this.leftLeg = leftLeg;
        this.predicate = whenStartUsing;
    }

    public ModelPart getLeftArm() {
        return leftArm;
    }

    public ModelPart getRightArm() {
        return rightArm;
    }

    public ModelPart getHead() {
        return head;
    }

    @Override
    public HumanoidAnimator.AnimateStage preferredStage() {
        return HumanoidAnimator.AnimateStage.FINAL;
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!this.predicate.test(entity)) return;

//        this.rightArm.z = 0.0F;
//        this.rightArm.x = -5.0F;
//        this.leftArm.z = 0.0F;
//        this.leftArm.x = 5.0F;
//        this.rightArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.25F;
//        this.leftArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.25F;
//        this.rightArm.zRot = 2.3561945F;
//        this.leftArm.zRot = -2.3561945F;
//        this.rightArm.yRot = 0.0F;
//        this.leftArm.yRot = 0.0F;


        this.rightArm.z = 0.0F;
        this.rightArm.x = -5.0F;
        this.rightArm.y = -1;
        this.leftArm.z = 0.0F;
        this.leftArm.x = 5.0F;
        this.leftArm.y = -1;
        this.rightArm.xRot = (float) Math.toRadians(-180) + Mth.cos(ageInTicks * 0.6662F) * 0.25F;
        this.leftArm.xRot = (float) Math.toRadians(-180) + Mth.cos(ageInTicks * 0.6662F) * 0.25F;
        this.rightArm.zRot = (float) Math.toRadians(-45);
        this.leftArm.zRot = (float) Math.toRadians(45);
        this.rightArm.yRot = 0.0F;
        this.leftArm.yRot = 0.0F;

        if (entity.isOnGround()) {
            this.rightLeg.zRot = (float) Math.toRadians(10);
            this.leftLeg.zRot = (float) Math.toRadians(-10);
        }
    }
}
