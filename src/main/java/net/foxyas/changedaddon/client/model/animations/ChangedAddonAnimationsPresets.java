package net.foxyas.changedaddon.client.model.animations;

import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.arm.ArmBobAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.arm.ArmRideAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.arm.ArmSwimAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.upperbody.CatHeadInitAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.upperbody.WolfHeadInitAnimator;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.client.model.geom.ModelPart;

import java.util.List;
import java.util.function.Consumer;

import static net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets.*;

public class ChangedAddonAnimationsPresets {

    public static <T extends ChangedEntity, M extends AdvancedHumanoidModel<T>> Consumer<HumanoidAnimator<T, M>> catLikeMultiTail(
            ModelPart head,
            ModelPart leftEar, ModelPart rightEar,
            ModelPart torso,
            ModelPart leftArm, ModelPart rightArm,
            List<TailSet> tails,
            ModelPart leftLeg, ModelPart leftLegLower, ModelPart leftFoot, ModelPart leftPad,
            ModelPart rightLeg, ModelPart rightLegLower, ModelPart rightFoot, ModelPart rightPad
    ) {
        return (animator) -> {
            animator
                    .addPreset(catBipedal(leftLeg, leftLegLower, leftFoot, leftPad, rightLeg, rightLegLower, rightFoot, rightPad))
                    .addPreset(catUpperBody(head, torso, leftArm, rightArm))
                    .addPreset(catEars(leftEar, rightEar))
                    .addAnimator(new CatHeadInitAnimator<>(head))
                    .addAnimator(new ArmSwimAnimator<>(leftArm, rightArm))
                    .addAnimator(new ArmBobAnimator<>(leftArm, rightArm))
                    .addAnimator(new ArmRideAnimator<>(leftArm, rightArm));

            for (TailSet tail : tails) {
                animator.addPreset(catTail(tail.root(), tail.joints()));
            }
        };
    }

    public static <T extends ChangedEntity, M extends AdvancedHumanoidModel<T>> Consumer<HumanoidAnimator<T, M>> protogenLike(ModelPart head, ModelPart leftEar, ModelPart rightEar, ModelPart torso, ModelPart leftArm, ModelPart rightArm, ModelPart tail, List<ModelPart> tailJoints, ModelPart leftLeg, ModelPart leftLegLower, ModelPart leftFoot, ModelPart leftPad, ModelPart rightLeg, ModelPart rightLegLower, ModelPart rightFoot, ModelPart rightPad) {
        return (animator) -> animator.addPreset(wolfBipedal(leftLeg, leftLegLower, leftFoot, leftPad, rightLeg, rightLegLower, rightFoot, rightPad)).addPreset(wolfUpperBody(head, torso, leftArm, rightArm)).addPreset(wolfTail(tail, tailJoints)).addAnimator(new WolfHeadInitAnimator<>(head)).addAnimator(new ArmSwimAnimator<>(leftArm, rightArm)).addAnimator(new ArmBobAnimator<>(leftArm, rightArm)).addAnimator(new ArmRideAnimator<>(leftArm, rightArm));
    }

    public static <T extends ChangedEntity, M extends AdvancedHumanoidModel<T>> Consumer<HumanoidAnimator<T, M>> wolfLikeMultiTail(ModelPart head,
                                                                                                                                   ModelPart leftEar, ModelPart rightEar,
                                                                                                                                   ModelPart torso,
                                                                                                                                   ModelPart leftArm, ModelPart rightArm,
                                                                                                                                   List<TailSet> tails,
                                                                                                                                   ModelPart leftLeg, ModelPart leftLegLower, ModelPart leftFoot, ModelPart leftPad,
                                                                                                                                   ModelPart rightLeg, ModelPart rightLegLower, ModelPart rightFoot, ModelPart rightPad) {
        return (animator) -> {
            animator
                    .addPreset(wolfBipedal(leftLeg, leftLegLower, leftFoot, leftPad, rightLeg, rightLegLower, rightFoot, rightPad))
                    .addPreset(wolfUpperBody(head, torso, leftArm, rightArm))
                    .addPreset(wolfEars(leftEar, rightEar))
                    .addAnimator(new WolfHeadInitAnimator<>(head))
                    .addAnimator(new ArmSwimAnimator<>(leftArm, rightArm))
                    .addAnimator(new ArmBobAnimator<>(leftArm, rightArm))
                    .addAnimator(new ArmRideAnimator<>(leftArm, rightArm));

            for (TailSet tail : tails) {
                animator.addPreset(wolfTail(tail.root(), tail.joints()));
            }
        };

    }

    public record TailSet(ModelPart root, List<ModelPart> joints) {
    }

}
