package net.foxyas.changedaddon.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.entity.WolfyEntity;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.arm.ArmBobAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.arm.ArmRideAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.arm.ArmSwimAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.upperbody.WolfHeadInitAnimator;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModelInterface;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

import static net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets.*;


class WolfyModelAnimation{
	public WolfyModelAnimation (){
	}
	public static <T extends ChangedEntity, M extends AdvancedHumanoidModel<T>> Consumer<HumanoidAnimator<T, M>> PuroLike(ModelPart head, ModelPart leftEar, ModelPart rightEar, ModelPart torso, ModelPart leftArm, ModelPart rightArm, ModelPart tail, List<ModelPart> tailJoints, ModelPart leftLeg, ModelPart leftLegLower, ModelPart leftFoot, ModelPart leftPad, ModelPart rightLeg, ModelPart rightLegLower, ModelPart rightFoot, ModelPart rightPad) {
		return (animator) -> {
			animator.addPreset(wolfBipedal(leftLeg, leftLegLower, leftFoot, leftPad, rightLeg, rightLegLower, rightFoot, rightPad)).addPreset(wolfUpperBody(head, torso, leftArm, rightArm)).addPreset(catTail(tail, tailJoints)).addPreset(wolfEars(leftEar, rightEar)).addAnimator(new WolfHeadInitAnimator(head)).addAnimator(new ArmSwimAnimator(leftArm, rightArm)).addAnimator(new ArmBobAnimator(leftArm, rightArm)).addAnimator(new ArmRideAnimator(leftArm, rightArm));
		};
	}
}
public class WolfyModel extends AdvancedHumanoidModel<WolfyEntity> implements AdvancedHumanoidModelInterface<WolfyEntity,WolfyModel> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ChangedAddonMod.resourceLoc("wolfy_model"), "main");

	private final ModelPart RightLeg;
    private final ModelPart LeftLeg;
    private final ModelPart RightArm;
    private final ModelPart LeftArm;
    private final ModelPart Head;
    private final ModelPart Torso;
    private final ModelPart Tail;

    private final ModelPart Mask;

	private final HumanoidAnimator<WolfyEntity, WolfyModel> animator;

	public WolfyModel(ModelPart root) {
		super(root);
		this.RightLeg = root.getChild("RightLeg");
        this.LeftLeg = root.getChild("LeftLeg");
        this.Head = root.getChild("Head");
        this.Mask = Head.getChild("Mask");
        this.Torso = root.getChild("Torso");
        this.Tail = Torso.getChild("Tail");
        this.RightArm = root.getChild("RightArm");
        this.LeftArm = root.getChild("LeftArm");


		var tailPrimary = Tail.getChild("TailPrimary");
		var tailSecondary = tailPrimary.getChild("TailSecondary");
		var tailTertiary = tailSecondary.getChild("TailTertiary");

		var leftLowerLeg = LeftLeg.getChild("LeftLowerLeg");
		var leftFoot = leftLowerLeg.getChild("LeftFoot");
		var rightLowerLeg = RightLeg.getChild("RightLowerLeg");
		var rightFoot = rightLowerLeg.getChild("RightFoot");

		animator = HumanoidAnimator.of(this).hipOffset(-1.5f)
				.addPreset(/*AnimatorPresets.wolfLike*/WolfyModelAnimation.PuroLike(
						Head, Head.getChild("LeftEar"), Head.getChild("RightEar"),
						Torso, LeftArm, RightArm,
						Tail, List.of(tailPrimary, tailSecondary, tailTertiary, tailTertiary.getChild("TailQuaternary")),
						LeftLeg, leftLowerLeg, leftFoot, leftFoot.getChild("LeftPad"), RightLeg, rightLowerLeg, rightFoot, rightFoot.getChild("RightPad")));
	}
	public boolean isPartNotMask(ModelPart part) {
		return Mask.getAllParts().noneMatch(part::equals);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offset(-2.5F, 10.5F, 0.0F));

		PartDefinition RightThigh_Labcoat_r1 = RightLeg.addOrReplaceChild("RightThigh_Labcoat_r1", CubeListBuilder.create().texOffs(64, 42).addBox(-0.65F, 0.1F, -2.15F, 1.0F, 7.0F, 4.0F, new CubeDeformation(0.05F))
		.texOffs(68, 40).addBox(-3.0F, 2.0F, -1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.15F))
		.texOffs(64, 53).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.15F))
		.texOffs(32, 44).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition RightLowerLeg = RightLeg.addOrReplaceChild("RightLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

		PartDefinition RightCalf_r1 = RightLowerLeg.addOrReplaceChild("RightCalf_r1", CubeListBuilder.create().texOffs(48, 40).addBox(-1.99F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

		PartDefinition RightFoot = RightLowerLeg.addOrReplaceChild("RightFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

		PartDefinition RightArch_r1 = RightFoot.addOrReplaceChild("RightArch_r1", CubeListBuilder.create().texOffs(32, 85).addBox(-2.0F, -1.0F, -1.5F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(0.0F, 3.5343F, -2.8616F, -2.7925F, 0.0F, -3.1416F));

		PartDefinition RightArch_r2 = RightFoot.addOrReplaceChild("RightArch_r2", CubeListBuilder.create().texOffs(56, 11).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

		PartDefinition RightPad = RightFoot.addOrReplaceChild("RightPad", CubeListBuilder.create().texOffs(52, 32).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

		PartDefinition RightFootPawBeans2 = RightPad.addOrReplaceChild("RightFootPawBeans2", CubeListBuilder.create().texOffs(18, 87).addBox(0.0F, 9.475F, -0.375F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.075F))
		.texOffs(18, 83).addBox(1.8F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F))
		.texOffs(18, 81).addBox(0.5F, 9.475F, -1.875F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F))
		.texOffs(18, 85).addBox(-0.775F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)), PartPose.offset(-1.0F, -8.5F, -0.05F));

		PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(2.5F, 10.5F, 0.0F));

		PartDefinition LeftThigh_Labcoat_r1 = LeftLeg.addOrReplaceChild("LeftThigh_Labcoat_r1", CubeListBuilder.create().texOffs(64, 42).addBox(-0.35F, 0.1F, -2.15F, 1.0F, 7.0F, 4.0F, new CubeDeformation(0.05F))
		.texOffs(72, 35).addBox(2.075F, 2.0F, -1.075F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(72, 35).addBox(2.075F, 2.0F, 0.075F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(68, 40).addBox(2.0F, 2.0F, -1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.15F))
		.texOffs(80, 52).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.15F))
		.texOffs(48, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition LeftThigh_r1 = LeftLeg.addOrReplaceChild("LeftThigh_r1", CubeListBuilder.create().texOffs(0, 90).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(0.0F, 4.3933F, -0.974F, -1.5708F, 1.3526F, -1.5708F));

		PartDefinition LeftLowerLeg = LeftLeg.addOrReplaceChild("LeftLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

		PartDefinition LeftCalf_r1 = LeftLowerLeg.addOrReplaceChild("LeftCalf_r1", CubeListBuilder.create().texOffs(48, 22).addBox(-2.01F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

		PartDefinition LeftFoot = LeftLowerLeg.addOrReplaceChild("LeftFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

		PartDefinition LeftArch_r1 = LeftFoot.addOrReplaceChild("LeftArch_r1", CubeListBuilder.create().texOffs(13, 57).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

		PartDefinition LeftPad = LeftFoot.addOrReplaceChild("LeftPad", CubeListBuilder.create().texOffs(24, 0).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

		PartDefinition LeftFootPawBeans = LeftPad.addOrReplaceChild("LeftFootPawBeans", CubeListBuilder.create().texOffs(24, 88).addBox(0.0F, 9.475F, -0.375F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.075F))
		.texOffs(24, 84).addBox(1.8F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F))
		.texOffs(24, 82).addBox(0.5F, 9.475F, -1.875F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F))
		.texOffs(24, 86).addBox(-0.775F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)), PartPose.offset(-1.0F, -8.5F, -0.05F));

		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(37, 0).addBox(-1.5F, -1.0F, -6.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(15, 72).addBox(-1.0F, -1.5F, -6.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));

		PartDefinition cube_r1 = Head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(21, 95).addBox(-1.0428F, -0.2819F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(-0.25F))
		.texOffs(21, 95).addBox(-0.9572F, -0.7181F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(-1.3577F, -3.8455F, -3.8F, 0.0F, 0.0F, -0.0436F));

		PartDefinition cube_r2 = Head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(21, 95).addBox(-0.9572F, -0.2819F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(-0.25F))
		.texOffs(21, 95).addBox(-1.0428F, -0.7181F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(1.3577F, -3.8455F, -3.8F, 0.0F, 0.0F, 0.0436F));

		PartDefinition cube_r3 = Head.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(17, 95).addBox(1.7F, -3.5F, -3.8F, 2.0F, 1.0F, 0.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(-3.5F, -0.8F, 0.0F, 0.0F, 0.0F, -0.1745F));

		PartDefinition cube_r4 = Head.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(17, 95).addBox(2.0F, -4.0F, -3.8F, 2.0F, 1.0F, 0.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(-3.7F, -0.7F, 0.0F, 0.0F, 0.0F, -0.1745F));

		PartDefinition cube_r5 = Head.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(17, 95).addBox(-3.7F, -3.5F, -3.8F, 2.0F, 1.0F, 0.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(3.5F, -0.8F, 0.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition cube_r6 = Head.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(17, 95).addBox(-4.0F, -4.0F, -3.8F, 2.0F, 1.0F, 0.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(3.7F, -0.7F, 0.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition bandages2 = Head.addOrReplaceChild("bandages2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.4F, 0.0F));

		PartDefinition bnaid_r1 = bandages2.addOrReplaceChild("bnaid_r1", CubeListBuilder.create().texOffs(18, 92).addBox(-1.0F, -0.5F, -0.525F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.425F))
		.texOffs(18, 92).addBox(-1.0F, -0.35F, -0.525F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.425F))
		.texOffs(18, 92).addBox(-1.0F, -0.65F, -0.525F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.425F))
		.texOffs(18, 90).addBox(-1.0F, -0.5F, -0.275F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, -4.075F, -4.975F, 0.0F, 0.0F, -0.1745F));

		PartDefinition orange = Head.addOrReplaceChild("orange", CubeListBuilder.create().texOffs(47, 60).addBox(-1.0F, -10.8F, -1.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Orange_r1 = orange.addOrReplaceChild("Orange_r1", CubeListBuilder.create().texOffs(55, 62).addBox(-2.0F, -12.3F, -2.2F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1046F, 0.0055F, 0.0521F));

		PartDefinition Orange_r2 = orange.addOrReplaceChild("Orange_r2", CubeListBuilder.create().texOffs(55, 62).addBox(-0.1F, -13.0F, -2.2F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1047F, -0.0036F, -0.0347F));

		PartDefinition Orange_r3 = orange.addOrReplaceChild("Orange_r3", CubeListBuilder.create().texOffs(53, 59).addBox(-0.7F, -13.0F, -2.2F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1047F, 0.0018F, 0.0174F));

		PartDefinition RightEar = Head.addOrReplaceChild("RightEar", CubeListBuilder.create(), PartPose.offset(-3.0F, -7.5F, 0.0F));

		PartDefinition RightEarPivot = RightEar.addOrReplaceChild("RightEarPivot", CubeListBuilder.create().texOffs(0, 4).addBox(-1.9F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(0, 16).addBox(-0.9F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
		.texOffs(41, 6).addBox(-0.9F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(24, 3).addBox(0.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(0.5F, -1.25F, 0.0F, -0.1309F, 0.5236F, -0.3491F));

		PartDefinition LeftEar = Head.addOrReplaceChild("LeftEar", CubeListBuilder.create(), PartPose.offset(3.0F, -7.5F, 0.0F));

		PartDefinition LeftEarPivot = LeftEar.addOrReplaceChild("LeftEarPivot", CubeListBuilder.create().texOffs(0, 0).addBox(-1.1F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(0, 20).addBox(-1.1F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
		.texOffs(45, 0).addBox(-1.1F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(0, 35).addBox(-1.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(-0.5F, -1.25F, 0.0F, -0.1309F, -0.5236F, 0.3491F));

		PartDefinition Hair = Head.addOrReplaceChild("Hair", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F))
		.texOffs(24, 8).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Mask = Head.addOrReplaceChild("Mask", CubeListBuilder.create().texOffs(20, 36).addBox(-1.0F, -4.0F, -5.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(32, 26).addBox(-3.0F, -5.0F, -5.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(32, 24).addBox(-3.0F, -2.0F, -5.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(70, 2).addBox(-3.025F, -2.3F, -4.275F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F))
		.texOffs(70, 2).addBox(2.025F, -2.3F, -4.275F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F))
		.texOffs(24, 22).addBox(-4.0F, -1.0F, -5.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(37, 3).addBox(-2.0F, -6.0F, -5.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(37, 3).addBox(1.0F, -6.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(42, 22).addBox(-1.0F, -7.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 32).addBox(3.0F, -4.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 0).addBox(-4.0F, -4.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(15, 32).addBox(-2.0F, -2.0F, -7.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition ExtraFur = Head.addOrReplaceChild("ExtraFur", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Right = ExtraFur.addOrReplaceChild("Right", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r7 = Right.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 67).addBox(-0.5F, -1.0F, -3.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.9107F, -2.1023F, 0.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition cube_r8 = Right.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 67).mirror().addBox(0.25F, -0.75F, -3.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-4.5F, -1.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition Left = ExtraFur.addOrReplaceChild("Left", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r9 = Left.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 67).addBox(-0.5F, -1.0F, -3.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.9107F, -2.1023F, 0.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition cube_r10 = Left.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 67).addBox(-1.25F, -0.75F, -3.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(4.5F, -1.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition Torso = partdefinition.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(28, 28).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));

		PartDefinition LabcoatTorso = Torso.addOrReplaceChild("LabcoatTorso", CubeListBuilder.create().texOffs(69, 80).addBox(-7.1F, -27.0F, -4.9F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.15F))
		.texOffs(78, 34).addBox(-7.1F, -27.0F, -4.9F, 8.0F, 12.0F, 1.0F, new CubeDeformation(0.2F)), PartPose.offset(3.1F, 27.0F, 2.9F));

		PartDefinition Labcoat_r1 = LabcoatTorso.addOrReplaceChild("Labcoat_r1", CubeListBuilder.create().texOffs(34, 72).addBox(-20.4F, -16.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2005F)), PartPose.offsetAndRotation(3.1F, -1.0F, -2.9F, 0.0F, 0.0F, 0.7854F));

		PartDefinition Labcoat_r2 = LabcoatTorso.addOrReplaceChild("Labcoat_r2", CubeListBuilder.create().texOffs(0, 77).addBox(-20.4F, -16.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.101F)), PartPose.offsetAndRotation(3.1F, 0.0F, -3.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition Labcoat_r3 = LabcoatTorso.addOrReplaceChild("Labcoat_r3", CubeListBuilder.create().texOffs(34, 72).addBox(-20.4F, -16.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1005F)), PartPose.offsetAndRotation(3.0F, 1.0F, -3.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition Labcoat_r4 = LabcoatTorso.addOrReplaceChild("Labcoat_r4", CubeListBuilder.create().texOffs(34, 72).addBox(-20.4F, -16.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1005F)), PartPose.offsetAndRotation(-3.0F, 1.0F, -3.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition Labcoat_r5 = LabcoatTorso.addOrReplaceChild("Labcoat_r5", CubeListBuilder.create().texOffs(0, 77).addBox(-20.4F, -16.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.101F)), PartPose.offsetAndRotation(-3.1F, 0.0F, -3.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition Labcoat_r6 = LabcoatTorso.addOrReplaceChild("Labcoat_r6", CubeListBuilder.create().texOffs(34, 72).addBox(-20.4F, -16.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2005F)), PartPose.offsetAndRotation(-3.1F, -1.0F, -2.9F, 0.0F, 0.0F, 0.7854F));

		PartDefinition Bandages = LabcoatTorso.addOrReplaceChild("Bandages", CubeListBuilder.create().texOffs(45, 84).addBox(-4.0F, -1.0F, -2.0F, 8.0F, 2.0F, 4.0F, new CubeDeformation(0.1F))
		.texOffs(36, 90).addBox(-4.0F, -2.9F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.1F))
		.texOffs(52, 90).addBox(3.0F, 0.875F, -2.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(-3.1F, -22.0F, -2.9F));

		PartDefinition Labcoat_r7 = Bandages.addOrReplaceChild("Labcoat_r7", CubeListBuilder.create().texOffs(45, 90).addBox(-4.0F, -1.0F, -2.0F, 8.0F, 2.0F, 4.0F, new CubeDeformation(0.105F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

		PartDefinition voidsignil = LabcoatTorso.addOrReplaceChild("voidsignil", CubeListBuilder.create(), PartPose.offset(0.0F, 0.5F, 0.1F));

		PartDefinition Labcoat_r8 = voidsignil.addOrReplaceChild("Labcoat_r8", CubeListBuilder.create().texOffs(38, 72).addBox(-20.4F, -16.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2F))
		.texOffs(72, 0).addBox(-20.4F, -16.0F, -2.2F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.375F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition Labcoat_r9 = voidsignil.addOrReplaceChild("Labcoat_r9", CubeListBuilder.create().texOffs(38, 72).addBox(-20.4F, -16.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2F))
		.texOffs(72, 0).addBox(-20.4F, -16.0F, -2.2F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.375F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition Labcoat_r10 = voidsignil.addOrReplaceChild("Labcoat_r10", CubeListBuilder.create().texOffs(38, 72).addBox(-20.4F, -16.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2F))
		.texOffs(72, 0).addBox(-20.4F, -16.0F, -2.2F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.375F)), PartPose.offsetAndRotation(-2.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition Labcoat_r11 = voidsignil.addOrReplaceChild("Labcoat_r11", CubeListBuilder.create().texOffs(38, 72).addBox(-20.4F, -16.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2F))
		.texOffs(72, 0).addBox(-20.4F, -16.0F, -2.2F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.375F)), PartPose.offsetAndRotation(2.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition Labcoat_r12 = voidsignil.addOrReplaceChild("Labcoat_r12", CubeListBuilder.create().texOffs(38, 72).addBox(-20.4F, -16.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.2F))
		.texOffs(72, 0).addBox(-20.4F, -16.0F, -2.2F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.375F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition Tail = Torso.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offset(0.0F, 10.5F, 0.0F));

		PartDefinition TailPrimary = Tail.addOrReplaceChild("TailPrimary", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition Base_r1 = TailPrimary.addOrReplaceChild("Base_r1", CubeListBuilder.create().texOffs(2, 75).addBox(-2.0F, 0.75F, -1.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.0908F, 0.0F, 0.0F));

		PartDefinition TailSecondary = TailPrimary.addOrReplaceChild("TailSecondary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.25F, 4.5F));

		PartDefinition Base_r2 = TailSecondary.addOrReplaceChild("Base_r2", CubeListBuilder.create().texOffs(49, 89).addBox(-2.5F, -1.0F, -2.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.0F, 1.0519F, 2.4896F, 1.789F, 0.0F, 3.1416F));

		PartDefinition Base_r3 = TailSecondary.addOrReplaceChild("Base_r3", CubeListBuilder.create().texOffs(18, 70).addBox(-2.5F, -0.45F, -2.5F, 5.0F, 6.0F, 5.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 1.3526F, 0.0F, 0.0F));

		PartDefinition TailTertiary = TailSecondary.addOrReplaceChild("TailTertiary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.75F, 4.5F));

		PartDefinition Base_r4 = TailTertiary.addOrReplaceChild("Base_r4", CubeListBuilder.create().texOffs(49, 83).addBox(-2.5F, 8.55F, -3.3F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.15F))
		.texOffs(42, 65).addBox(-2.5F, 4.55F, -3.3F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, -0.25F, -4.5F, 1.5272F, 0.0F, 0.0F));

		PartDefinition TailQuaternary = TailTertiary.addOrReplaceChild("TailQuaternary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 7.5F));

		PartDefinition Base_r5 = TailQuaternary.addOrReplaceChild("Base_r5", CubeListBuilder.create().texOffs(28, 63).addBox(-2.0F, 5.5F, -3.8F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, -1.0F, -5.5F, 1.7017F, 0.0F, 0.0F));

		PartDefinition NeckFur = Torso.addOrReplaceChild("NeckFur", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.0F));

		PartDefinition cube_r11 = NeckFur.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(9, 68).addBox(-2.0F, 0.75F, -0.75F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.05F))
		.texOffs(9, 68).addBox(-5.0F, 0.75F, -0.75F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.05F))
		.texOffs(9, 68).addBox(-5.0F, 0.75F, -0.75F, 5.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F))
		.texOffs(9, 68).addBox(-4.0F, -0.25F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.05F))
		.texOffs(9, 68).addBox(-5.0F, -0.25F, 0.0F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.9107F, 0.8977F, -2.5F, 0.5672F, 0.0F, 0.0F));

		PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(0, 44).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(63, 64).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.15F))
		.texOffs(80, 47).addBox(-3.0F, 8.2F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(-5.0F, 1.5F, 0.0F));

		PartDefinition RightPawBeans = RightArm.addOrReplaceChild("RightPawBeans", CubeListBuilder.create().texOffs(18, 87).addBox(0.0F, 9.0F, -0.375F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.075F))
		.texOffs(18, 83).addBox(1.8F, 9.0F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F))
		.texOffs(18, 81).addBox(0.5F, 9.0F, -1.875F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F))
		.texOffs(18, 85).addBox(-0.775F, 9.0F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)), PartPose.offset(-2.0F, 0.0F, 0.0F));

		PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(16, 40).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(80, 64).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.15F))
		.texOffs(80, 47).addBox(-1.0F, 8.2F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.2F))
		.texOffs(0, 84).addBox(-1.0F, 1.2F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(5.0F, 1.5F, 0.0F));

		PartDefinition LeftPawBeans = LeftArm.addOrReplaceChild("LeftPawBeans", CubeListBuilder.create().texOffs(24, 88).addBox(0.0F, 9.0F, -0.375F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.075F))
		.texOffs(24, 84).addBox(1.8F, 9.0F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F))
		.texOffs(24, 82).addBox(0.5F, 9.0F, -1.875F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F))
		.texOffs(24, 86).addBox(-0.775F, 9.0F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition ScrappedStuff = partdefinition.addOrReplaceChild("ScrappedStuff", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition T = ScrappedStuff.addOrReplaceChild("T", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Shotgun = T.addOrReplaceChild("Shotgun", CubeListBuilder.create(), PartPose.offset(0.0F, -20.2F, 4.0F));

		PartDefinition cube_r12 = Shotgun.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(74, 6).addBox(6.075F, -2.0F, 2.8F, 1.0F, 3.0F, 2.0F, new CubeDeformation(-0.75F))
		.texOffs(90, 7).addBox(6.075F, -2.0F, 2.3F, 1.0F, 3.0F, 2.0F, new CubeDeformation(-0.75F))
		.texOffs(74, 6).addBox(6.3F, -2.0F, 1.6F, 1.0F, 3.0F, 2.0F, new CubeDeformation(-0.505F))
		.texOffs(86, 7).addBox(4.3F, -2.0F, 1.6F, 3.0F, 3.0F, 2.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 1.0472F));

		PartDefinition cube_r13 = Shotgun.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(86, 2).addBox(3.2F, -2.0F, 0.2F, 3.0F, 3.0F, 2.0F, new CubeDeformation(-0.505F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.7854F));

		PartDefinition cube_r14 = Shotgun.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(74, 0).addBox(0.0F, -2.0F, -0.9F, 5.0F, 3.0F, 2.0F, new CubeDeformation(-0.5F))
		.texOffs(79, 17).addBox(-9.0F, -2.0F, -0.4F, 3.0F, 3.0F, 2.0F, new CubeDeformation(-0.51F))
		.texOffs(72, 12).addBox(-7.0F, -2.0F, -0.9F, 8.0F, 3.0F, 2.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.5236F));

		PartDefinition cube_r15 = Shotgun.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(72, 22).addBox(-9.0F, -1.0F, 0.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, 1.5708F, 0.0F, 0.5236F));

		PartDefinition cube_r16 = Shotgun.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(70, 26).addBox(2.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
		.texOffs(80, 26).addBox(2.0F, -1.0F, 0.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
		.texOffs(70, 26).addBox(0.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
		.texOffs(71, 18).addBox(-9.1F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.55F))
		.texOffs(71, 18).addBox(-9.1F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.55F))
		.texOffs(72, 22).addBox(-9.0F, -1.0F, 0.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
		.texOffs(72, 22).addBox(-9.0F, -2.0F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(0.0F, -0.3F, -0.5F, 0.7854F, 0.0F, 0.5236F));

		PartDefinition cube_r17 = Shotgun.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(70, 30).addBox(2.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
		.texOffs(80, 26).addBox(0.0F, -1.4F, -0.8F, 5.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
		.texOffs(70, 30).addBox(0.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(0.0F, -0.3F, -0.5F, 0.1309F, 0.0F, 0.5236F));

		PartDefinition cube_r18 = Shotgun.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(60, 0).addBox(2.0F, -1.0F, 0.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
		.texOffs(80, 26).addBox(0.0F, -1.2F, -0.6F, 5.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
		.texOffs(80, 30).addBox(2.0F, -1.0F, 0.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
		.texOffs(80, 30).addBox(0.0F, -1.0F, 0.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(0.0F, -0.3F, -0.5F, 1.4399F, 0.0F, 0.5236F));

		PartDefinition cube_r19 = Shotgun.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(70, 30).addBox(2.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
		.texOffs(80, 26).addBox(2.0F, -1.4F, -0.8F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(1.8F, -0.1F, -0.5F, 0.1309F, 0.0F, 0.7854F));

		PartDefinition cube_r20 = Shotgun.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(80, 30).addBox(2.0F, -1.0F, 0.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
		.texOffs(80, 26).addBox(2.0F, -1.2F, -0.6F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(1.8F, -0.1F, -0.5F, 1.4399F, 0.0F, 0.7854F));

		PartDefinition cube_r21 = Shotgun.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(80, 26).addBox(2.0F, -1.0F, 0.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
		.texOffs(70, 26).addBox(2.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(3.5F, 0.6F, -0.5F, 0.7854F, 0.0F, 1.0472F));

		PartDefinition cube_r22 = Shotgun.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(80, 26).addBox(2.0F, -1.2F, -0.6F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
		.texOffs(80, 30).addBox(2.0F, -1.0F, 0.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(3.5F, 0.6F, -0.5F, 1.4399F, 0.0F, 1.0472F));

		PartDefinition cube_r23 = Shotgun.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(80, 26).addBox(2.0F, -1.4F, -0.8F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
		.texOffs(70, 30).addBox(2.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(3.5F, 0.6F, -0.5F, 0.1309F, 0.0F, 1.0472F));

		PartDefinition cube_r24 = Shotgun.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(70, 26).addBox(2.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(1.8F, -0.1F, -0.5F, 0.7854F, 0.0F, 0.7854F));

		PartDefinition cube_r25 = Shotgun.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(80, 26).addBox(0.0F, -1.0F, 0.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(3.2F, 1.3F, -0.5F, 0.7854F, 0.0F, 0.7854F));

		PartDefinition cube_r26 = Shotgun.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(80, 26).addBox(0.0F, -1.0F, 0.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(0.0F, -0.3F, -0.5F, 0.7854F, 0.0F, 0.5236F));

		return LayerDefinition.create(meshdefinition, 96, 96);
	}

	@Override
	public void prepareMobModel (WolfyEntity p_162861, float p_102862, float p_102863, float p_102864_) {
		this.prepareMobModel(animator, p_162861, p_102862, p_102863, p_102864_);
	}

	/* public PoseStack getPlacementCorrectors(CorrectorType type) {
		PoseStack corrector = AdvancedHumanoidModelInterface.super.getPlacementCorrectors(type);
		if (type.isArm())
			corrector.translate(-0.02f, 0.12f, 0.12f);
		return corrector;
	} */
	@Override
	public void setupHand(WolfyEntity entity) {
		animator.setupHand();
	}


	@Override
	public void setupAnim(@NotNull WolfyEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		animator.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		super.setupAnim(entity,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch);
	}

	public ModelPart getArm (HumanoidArm p_102852) {
		return p_102852 == HumanoidArm.LEFT ? this.LeftArm : this.RightArm;
	}

	public ModelPart getHead() {
		return this.Head;
	}

	public ModelPart getTorso() {
		return Torso;
	}

	public ModelPart getLeg(HumanoidArm humanoidArm) {
		return humanoidArm == HumanoidArm.LEFT ? this.LeftLeg : this.RightLeg;
	}



	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		RightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		LeftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Torso.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		RightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		LeftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public HumanoidAnimator<WolfyEntity, WolfyModel> getAnimator(WolfyEntity entity) {
		return animator;
	}
}