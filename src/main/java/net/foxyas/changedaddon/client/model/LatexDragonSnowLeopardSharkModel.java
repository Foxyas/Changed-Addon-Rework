package net.foxyas.changedaddon.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.client.model.animations.CustomWingedDragonUpperBodyInitAnimator;
import net.foxyas.changedaddon.entity.LatexDragonSnowLeopardSharkEntity;
import net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.bipedal.DragonBipedalCreativeFlyAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.camera.DragonCameraCreativeFlyAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.tail.DragonTailCreativeFlyAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.upperbody.DragonHeadCreativeFlyAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.upperbody.DragonHeadInitAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.upperbody.DragonUpperBodyCreativeFlyAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.upperbody.DragonUpperBodyCrouchAnimator;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModelInterface;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets.dragonWinged;

public class LatexDragonSnowLeopardSharkModel extends AdvancedHumanoidModel<LatexDragonSnowLeopardSharkEntity> implements AdvancedHumanoidModelInterface<LatexDragonSnowLeopardSharkEntity, LatexDragonSnowLeopardSharkModel> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("changed_addon", "latex_dragon_snep_shark"), "main");
    private final ModelPart RightLeg;
    private final ModelPart LeftLeg;
    private final ModelPart RightArm;
    private final ModelPart LeftArm;
    private final ModelPart Head;
    private final ModelPart Torso;
    private final ModelPart Tail;
    private final HumanoidAnimator<LatexDragonSnowLeopardSharkEntity, LatexDragonSnowLeopardSharkModel> animator;
    private final ModelPart RightWing;
    private final ModelPart LeftWing;

    public static class Animator {

        private static final ModelPart NULL_PART = new ModelPart(List.of(), Map.of());

        public static <T extends ChangedEntity, M extends AdvancedHumanoidModel<T>> Consumer<HumanoidAnimator<T, M>> dragonWingedUpperBody(ModelPart head, ModelPart torso, ModelPart leftArm, ModelPart rightArm) {
            return (animator) -> {
                animator.addAnimator(new CustomWingedDragonUpperBodyInitAnimator(head, torso, leftArm, rightArm)).addAnimator(new DragonUpperBodyCreativeFlyAnimator(head, torso, leftArm, rightArm)).addAnimator(new DragonUpperBodyCrouchAnimator(head, torso, leftArm, rightArm));
            };
        }
        public static <T extends ChangedEntity, M extends AdvancedHumanoidModel<T>>
        Consumer<HumanoidAnimator<T, M>> wingedDragonLike(ModelPart head, ModelPart torso, ModelPart leftArm, ModelPart
                rightArm, ModelPart tail, List<ModelPart> tailJoints, ModelPart leftLeg, ModelPart leftLegLower, ModelPart
                                                                  leftFoot, ModelPart leftPad, ModelPart rightLeg, ModelPart rightLegLower, ModelPart rightFoot, ModelPart
                                                                  rightPad, ModelPart leftWingRoot, ModelPart leftWingBone1, ModelPart leftWingBone2, ModelPart
                                                                  rightWingRoot, ModelPart rightWingBone1, ModelPart rightWingBone2) {
            return (animator) -> {
                animator.addPreset(Animator.dragonWingedUpperBody(head, torso, leftArm, rightArm)).addPreset(dragonWinged(leftWingRoot, leftWingBone1, leftWingBone2, rightWingRoot, rightWingBone1, rightWingBone2)).addAnimator(new DragonBipedalCreativeFlyAnimator(leftLeg, leftLegLower, leftFoot, leftPad, rightLeg, rightLegLower, rightFoot, rightPad)).addAnimator(new DragonTailCreativeFlyAnimator(tail, tailJoints)).addAnimator(new DragonHeadCreativeFlyAnimator(head)).addAnimator(new DragonHeadInitAnimator(head)).addCameraAnimator(new DragonCameraCreativeFlyAnimator());
            };
        }
    }

    public LatexDragonSnowLeopardSharkModel(ModelPart root) {
        super(root);
        this.RightLeg = root.getChild("RightLeg");
        this.LeftLeg = root.getChild("LeftLeg");
        this.Head = root.getChild("Head");
        this.Torso = root.getChild("Torso");
        this.Tail = this.Torso.getChild("Tail");
        this.RightArm = root.getChild("RightArm");
        this.LeftArm = root.getChild("LeftArm");
        this.RightWing = this.Torso.getChild("RightWing");
        this.LeftWing = this.Torso.getChild("LeftWing");
        ModelPart tailPrimary = this.Tail.getChild("TailPrimary");
        ModelPart tailSecondary = tailPrimary.getChild("TailSecondary");
        ModelPart tailTertiary = tailSecondary.getChild("TailTertiary");
        ModelPart leftLowerLeg = this.LeftLeg.getChild("LeftLowerLeg");
        ModelPart leftFoot = leftLowerLeg.getChild("LeftFoot");
        ModelPart rightLowerLeg = this.RightLeg.getChild("RightLowerLeg");
        ModelPart rightFoot = rightLowerLeg.getChild("RightFoot");
        ModelPart leftWingRoot = this.LeftWing.getChild("leftWingRoot");
        ModelPart rightWingRoot = this.RightWing.getChild("rightWingRoot");
        this.animator = HumanoidAnimator.of(this).hipOffset(-1.5F)
                .addPreset(
                        AnimatorPresets.sharkLike(this.Head, this.Torso, this.LeftArm, this.RightArm, this.Tail, List.of(tailPrimary, tailSecondary, tailTertiary), this.LeftLeg, leftLowerLeg, leftFoot, leftFoot.getChild("LeftPad"), this.RightLeg, rightLowerLeg, rightFoot, rightFoot.getChild("RightPad"))
                ).addPreset(Animator.wingedDragonLike(this.Head,
                                this.Torso,
                                this.LeftArm,
                                this.RightArm,
                                this.Tail, List.of(tailPrimary, tailSecondary, tailTertiary),
                                this.LeftLeg, leftLowerLeg, leftFoot, leftFoot.getChild("LeftPad"),
                                this.RightLeg, rightLowerLeg, rightFoot, rightFoot.getChild("RightPad"),
                                leftWingRoot, leftWingRoot.getChild("leftSecondaries"), leftWingRoot.getChild("leftSecondaries").getChild("leftTertiaries"),
                                rightWingRoot, rightWingRoot.getChild("rightSecondaries"), rightWingRoot.getChild("rightSecondaries").getChild("rightTertiaries")
                        )
                )
        ;
    }

    public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offset(-2.5F, 10.5F, 0.0F));

		PartDefinition RightThigh_r1 = RightLeg.addOrReplaceChild("RightThigh_r1", CubeListBuilder.create().texOffs(32, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition RightLowerLeg = RightLeg.addOrReplaceChild("RightLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

		PartDefinition RightCalf_r1 = RightLowerLeg.addOrReplaceChild("RightCalf_r1", CubeListBuilder.create().texOffs(40, 22).addBox(-1.99F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

		PartDefinition RightFoot = RightLowerLeg.addOrReplaceChild("RightFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

		PartDefinition RightArch_r1 = RightFoot.addOrReplaceChild("RightArch_r1", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

		PartDefinition RightPad = RightFoot.addOrReplaceChild("RightPad", CubeListBuilder.create().texOffs(29, 43).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

		PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(2.5F, 10.5F, 0.0F));

		PartDefinition LeftThigh_r1 = LeftLeg.addOrReplaceChild("LeftThigh_r1", CubeListBuilder.create().texOffs(16, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition LeftLowerLeg = LeftLeg.addOrReplaceChild("LeftLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

		PartDefinition LeftCalf_r1 = LeftLowerLeg.addOrReplaceChild("LeftCalf_r1", CubeListBuilder.create().texOffs(40, 12).addBox(-2.01F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

		PartDefinition LeftFoot = LeftLowerLeg.addOrReplaceChild("LeftFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

		PartDefinition LeftArch_r1 = LeftFoot.addOrReplaceChild("LeftArch_r1", CubeListBuilder.create().texOffs(44, 47).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

		PartDefinition LeftPad = LeftFoot.addOrReplaceChild("LeftPad", CubeListBuilder.create().texOffs(11, 43).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(30, 50).addBox(-1.5F, -3.0F, -7.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.01F))
		.texOffs(56, 14).addBox(-1.5F, -1.0F, -6.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));

		PartDefinition Snout_r1 = Head.addOrReplaceChild("Snout_r1", CubeListBuilder.create().texOffs(52, 8).addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -3.01F, -7.0F, 0.0F, -0.2182F, 0.0F));

		PartDefinition Snout_r2 = Head.addOrReplaceChild("Snout_r2", CubeListBuilder.create().texOffs(52, 18).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, -3.01F, -7.0F, 0.0F, 0.2182F, 0.0F));

		PartDefinition Base_r1 = Head.addOrReplaceChild("Base_r1", CubeListBuilder.create().texOffs(32, 12).addBox(-2.1808F, -1.0F, -0.5736F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, -1.4F, -1.5F, -0.5236F, 0.9599F, -3.1416F));

		PartDefinition Base_r2 = Head.addOrReplaceChild("Base_r2", CubeListBuilder.create().texOffs(54, 37).addBox(-0.25F, -1.0F, 0.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -5.75F, -2.5F, 0.5674F, -0.886F, -0.2749F));

		PartDefinition Base_r3 = Head.addOrReplaceChild("Base_r3", CubeListBuilder.create().texOffs(30, 55).addBox(-2.1809F, -1.0F, -0.5736F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -1.4F, -1.5F, 0.5236F, 0.9599F, 0.0F));

		PartDefinition Base_r4 = Head.addOrReplaceChild("Base_r4", CubeListBuilder.create().texOffs(55, 46).addBox(-0.25F, -1.0F, 0.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -5.75F, -2.5F, -0.5674F, -0.886F, -2.8667F));

		PartDefinition bone = Head.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offsetAndRotation(0.05F, -7.675F, -1.925F, 0.1745F, 0.0F, 0.0F));

		PartDefinition Base_r5 = bone.addOrReplaceChild("Base_r5", CubeListBuilder.create().texOffs(24, 43).addBox(-1.25F, -0.8434F, 4.0373F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1872F, 0.1841F, -0.7681F));

		PartDefinition Base_r6 = bone.addOrReplaceChild("Base_r6", CubeListBuilder.create().texOffs(43, 38).addBox(-3.0F, 0.0F, -1.2929F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0643F, -0.7605F, 0.2671F, 0.2849F, 0.274F, -0.7459F));

		PartDefinition Torso = partdefinition.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));

		PartDefinition BackFin_r1 = Torso.addOrReplaceChild("BackFin_r1", CubeListBuilder.create().texOffs(20, 16).addBox(-1.0F, 2.0F, -3.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.5F, 2.0F, 0.5236F, 0.0F, 0.0F));

		PartDefinition Tail = Torso.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offset(0.0F, 10.5F, 0.0F));

		PartDefinition TailPrimary = Tail.addOrReplaceChild("TailPrimary", CubeListBuilder.create(), PartPose.offset(0.0F, -0.5F, 0.75F));

		PartDefinition TailFin_r1 = TailPrimary.addOrReplaceChild("TailFin_r1", CubeListBuilder.create().texOffs(0, 57).addBox(-4.0F, 4.0F, -0.75F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(40, 55).addBox(-4.0F, 0.0F, 0.25F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, 1.75F, 1.0F, 1.789F, 0.0F, 0.0F));

		PartDefinition Base_r7 = TailPrimary.addOrReplaceChild("Base_r7", CubeListBuilder.create().texOffs(49, 33).addBox(-2.0F, -1.075F, 0.375F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.875F, 0.85F, 1.9199F, 0.0F, 0.0F));

		PartDefinition Base_r8 = TailPrimary.addOrReplaceChild("Base_r8", CubeListBuilder.create().texOffs(32, 0).addBox(-2.0F, 0.75F, -0.8F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -1.0F, 1.1781F, 0.0F, 0.0F));

		PartDefinition TailSecondary = TailPrimary.addOrReplaceChild("TailSecondary", CubeListBuilder.create(), PartPose.offset(0.0F, 3.25F, 7.25F));

		PartDefinition Base_r9 = TailSecondary.addOrReplaceChild("Base_r9", CubeListBuilder.create().texOffs(48, 0).addBox(-1.5F, -1.3563F, -0.6088F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.25F, 1.0F, 1.309F, 0.0F, 0.0F));

		PartDefinition TailTertiary = TailSecondary.addOrReplaceChild("TailTertiary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.5F, 4.5F));

		PartDefinition Base_r10 = TailTertiary.addOrReplaceChild("Base_r10", CubeListBuilder.create().texOffs(0, 16).addBox(-0.5F, 5.3462F, -1.8296F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F))
		.texOffs(22, 50).addBox(-0.5F, -2.5538F, -1.8296F, 1.0F, 8.0F, 3.0F, new CubeDeformation(-0.05F)), PartPose.offsetAndRotation(0.0F, 0.5F, 4.25F, 1.1345F, 0.0F, 0.0F));

		PartDefinition Base_r11 = TailTertiary.addOrReplaceChild("Base_r11", CubeListBuilder.create().texOffs(56, 24).addBox(-0.5F, -6.1668F, 0.8821F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(14, 50).addBox(-0.5F, -8.1668F, -2.1179F, 1.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, 4.25F, -0.8727F, 0.0F, 0.0F));

		PartDefinition Base_r12 = TailTertiary.addOrReplaceChild("Base_r12", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -0.3449F, -0.7203F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.25F, 0.25F, 1.4835F, 0.0F, 0.0F));

		PartDefinition RightWing = Torso.addOrReplaceChild("RightWing", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, 5.0F, 2.0F, 0.0F, 0.48F, 0.0F));

		PartDefinition rightWingRoot = RightWing.addOrReplaceChild("rightWingRoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = rightWingRoot.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(71, 80).addBox(-25.975F, -4.475F, 1.65F, 7.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 20.0F, -2.0F, 0.0F, 0.0F, 1.2654F));

		PartDefinition cube_r2 = rightWingRoot.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(49, 92).addBox(-25.075F, -12.7F, 1.2F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 20.0F, -2.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r3 = rightWingRoot.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(63, 92).addBox(-12.775F, -19.75F, 1.2F, 5.0F, 2.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(2.0F, 20.0F, -2.0F, 0.0F, 0.0F, 0.3491F));

		PartDefinition rightSecondaries = rightWingRoot.addOrReplaceChild("rightSecondaries", CubeListBuilder.create().texOffs(18, 87).addBox(-0.2F, -0.475F, -0.3F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(-7.3F, -7.0F, -0.5F, 0.0F, 0.0F, 0.5236F));

		PartDefinition cube_r4 = rightSecondaries.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(26, 88).addBox(1.025F, -22.55F, 1.2F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.3F, 27.0F, -1.5F, 0.0F, 0.0F, -0.48F));

		PartDefinition cube_r5 = rightSecondaries.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(76, 70).addBox(-22.4F, 10.625F, 1.651F, 9.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.3F, 27.0F, -1.5F, 0.0F, 0.0F, 1.8326F));

		PartDefinition cube_r6 = rightSecondaries.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(35, 84).addBox(-24.525F, -13.85F, 1.648F, 9.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.3F, 27.0F, -1.5F, 0.0F, 0.0F, 0.7418F));

		PartDefinition rightTertiaries = rightSecondaries.addOrReplaceChild("rightTertiaries", CubeListBuilder.create(), PartPose.offsetAndRotation(0.3F, 0.0F, 0.0F, 0.0F, 0.0F, 0.9599F));

		PartDefinition cube_r7 = rightTertiaries.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(9, 87).addBox(2.3F, -22.5F, 1.2F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, 27.0F, -1.5F, 0.0F, 0.0F, -0.5236F));

		PartDefinition cube_r8 = rightTertiaries.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(30, 88).addBox(-10.15F, -26.2F, 1.2F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(9.0F, 27.0F, -1.5F, 0.0F, 0.0F, 0.0436F));

		PartDefinition cube_r9 = rightTertiaries.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(71, 85).addBox(-25.125F, -10.525F, 1.64F, 9.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, 27.0F, -1.5F, 0.0F, 0.0F, 0.8727F));

		PartDefinition LeftWing = Torso.addOrReplaceChild("LeftWing", CubeListBuilder.create(), PartPose.offsetAndRotation(2.0F, 5.0F, 2.0F, 0.0F, -0.48F, 0.0F));

		PartDefinition leftWingRoot = LeftWing.addOrReplaceChild("leftWingRoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r10 = leftWingRoot.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(53, 80).addBox(18.975F, -4.475F, 1.65F, 7.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 20.0F, -2.0F, 0.0F, 0.0F, -1.2654F));

		PartDefinition cube_r11 = leftWingRoot.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(35, 92).addBox(19.075F, -12.7F, 1.2F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 20.0F, -2.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r12 = leftWingRoot.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(75, 92).addBox(7.775F, -19.75F, 1.2F, 5.0F, 2.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-2.0F, 20.0F, -2.0F, 0.0F, 0.0F, -0.3491F));

		PartDefinition leftSecondaries = leftWingRoot.addOrReplaceChild("leftSecondaries", CubeListBuilder.create().texOffs(5, 87).addBox(-0.8F, -0.475F, -0.3F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(7.3F, -7.0F, -0.5F, 0.0F, 0.0F, -0.5236F));

		PartDefinition cube_r13 = leftSecondaries.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(13, 88).addBox(-2.025F, -22.55F, 1.2F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.3F, 27.0F, -1.5F, 0.0F, 0.0F, 0.48F));

		PartDefinition cube_r14 = leftSecondaries.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(35, 78).addBox(15.525F, -13.85F, 1.648F, 9.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.3F, 27.0F, -1.5F, 0.0F, 0.0F, -0.7418F));

		PartDefinition cube_r15 = leftSecondaries.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(53, 85).addBox(13.4F, 10.625F, 1.651F, 9.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.3F, 27.0F, -1.5F, 0.0F, 0.0F, -1.8326F));

		PartDefinition leftTertiaries = leftSecondaries.addOrReplaceChild("leftTertiaries", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.3F, 0.0F, 0.0F, 0.0F, 0.0F, -0.9599F));

		PartDefinition cube_r16 = leftTertiaries.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(1, 87).addBox(-3.3F, -22.5F, 1.2F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, 27.0F, -1.5F, 0.0F, 0.0F, 0.5236F));

		PartDefinition cube_r17 = leftTertiaries.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(76, 75).addBox(16.125F, -10.525F, 1.64F, 9.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, 27.0F, -1.5F, 0.0F, 0.0F, -0.8727F));

		PartDefinition cube_r18 = leftTertiaries.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(22, 88).addBox(9.15F, -26.2F, 1.2F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(-9.0F, 27.0F, -1.5F, 0.0F, 0.0F, -0.0436F));

		PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(24, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 1.5F, 0.0F));

		PartDefinition Spike_r1 = RightArm.addOrReplaceChild("Spike_r1", CubeListBuilder.create().texOffs(52, 56).addBox(-0.5F, -1.5F, -1.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(44, 56).addBox(-0.5F, -2.5F, -2.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0789F, 2.8746F, 1.1151F, -2.6425F, 0.8346F, 3.1091F));

		PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(0, 32).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 1.5F, 0.0F));

		PartDefinition Spike_r2 = LeftArm.addOrReplaceChild("Spike_r2", CubeListBuilder.create().texOffs(48, 56).addBox(0.875F, -1.5F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(56, 56).addBox(-0.125F, -0.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.6568F, 2.0711F, 1.6568F, -0.4796F, -0.6979F, 0.7102F));

		return LayerDefinition.create(meshdefinition, 96, 96);
	}

    public void prepareMobModel(LatexDragonSnowLeopardSharkEntity p_102861_, float p_102862_, float p_102863_, float p_102864_) {
        this.prepareMobModel(this.animator, p_102861_, p_102862_, p_102863_, p_102864_);
    }

    public void setupHand() {
        this.animator.setupHand();
    }

    public HumanoidAnimator<LatexDragonSnowLeopardSharkEntity, LatexDragonSnowLeopardSharkModel> getAnimator() {
        return this.animator;
    }

    public void setupAnim(LatexDragonSnowLeopardSharkEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.animator.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    public ModelPart getArm(HumanoidArm p_102852_) {
        return p_102852_ == HumanoidArm.LEFT ? this.LeftArm : this.RightArm;
    }

    public ModelPart getLeg(HumanoidArm p_102852_) {
        return p_102852_ == HumanoidArm.LEFT ? this.LeftLeg : this.RightLeg;
    }

    public ModelPart getHead() {
        return this.Head;
    }

    public ModelPart getTorso() {
        return this.Torso;
    }

    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.RightLeg.render(poseStack, buffer, packedLight, packedOverlay);
        this.LeftLeg.render(poseStack, buffer, packedLight, packedOverlay);
        this.Head.render(poseStack, buffer, packedLight, packedOverlay);
        this.Torso.render(poseStack, buffer, packedLight, packedOverlay);
        this.RightArm.render(poseStack, buffer, packedLight, packedOverlay);
        this.LeftArm.render(poseStack, buffer, packedLight, packedOverlay);
    }
}