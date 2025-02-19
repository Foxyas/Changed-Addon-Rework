package net.foxyas.changedaddon.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.entity.LatexSquidTigerSharkEntity;
import net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.arm.ArmRideAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.arm.ArmSwimAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.arm.DoubleArmBobAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.camera.SharkCameraSwimAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.upperbody.SharkHeadInitAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.upperbody.SharkHeadSwimAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.upperbody.WolfHeadInitAnimator;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModelInterface;
import net.ltxprogrammer.changed.client.renderer.model.CorrectorType;
import net.ltxprogrammer.changed.client.renderer.model.DoubleArmedModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets.*;

public class LatexSquidTigerSharkModel extends AdvancedHumanoidModel<LatexSquidTigerSharkEntity> implements AdvancedHumanoidModelInterface<LatexSquidTigerSharkEntity, LatexSquidTigerSharkModel>, DoubleArmedModel {

    public static class CustomHybridAnimation{

        public static <T extends ChangedEntity, M extends AdvancedHumanoidModel<T>> Consumer<HumanoidAnimator<T, M>> squidTigerSharkLike(
                ModelPart head,
                ModelPart torso,
                ModelPart upperLeftArm,
                ModelPart upperRightArm,
                ModelPart lowerLeftArm,
                ModelPart lowerRightArm,
                ModelPart tail,
                List<ModelPart> tailJoints,
                List<ModelPart> upperLeftTentacle,
                List<ModelPart> upperRightTentacle,
                List<ModelPart> lowerLeftTentacle,
                List<ModelPart> lowerRightTentacle,
                ModelPart leftLeg,
                ModelPart leftLegLower,
                ModelPart leftFoot,
                ModelPart leftPad,
                ModelPart rightLeg,
                ModelPart rightLegLower,
                ModelPart rightFoot,
                ModelPart rightPad) {
            return (animator) -> {
                /*animator.addPreset(wolfBipedal(leftLeg, leftLegLower, leftFoot, leftPad, rightLeg, rightLegLower, rightFoot, rightPad))
                        .addPreset(doubleArmUpperBody(head, torso, upperLeftArm, upperRightArm, lowerLeftArm, lowerRightArm))
                        .addPreset(wolfTail(tail, tailJoints)).addPreset(wolfEars(leftEar, rightEar))
                        .addPreset(squidDogTentacles(upperLeftTentacle, upperRightTentacle, lowerLeftTentacle, lowerRightTentacle))
                        .addAnimator(new WolfHeadInitAnimator(head)).addAnimator(new ArmSwimAnimator(upperLeftArm, upperRightArm))
                        .addAnimator(new DoubleArmBobAnimator(upperLeftArm, upperRightArm, lowerLeftArm, lowerRightArm))
                        .addAnimator(new ArmRideAnimator(upperLeftArm, upperRightArm));*/
                animator.addPreset(sharkBipedal(leftLeg, leftLegLower, leftFoot, leftPad, rightLeg, rightLegLower, rightFoot, rightPad))
                        .addPreset(doubleArmUpperBody(head, torso, upperLeftArm, upperRightArm, lowerLeftArm, lowerRightArm))
                        .addPreset(squidDogTentacles(upperLeftTentacle, upperRightTentacle, lowerLeftTentacle, lowerRightTentacle))
                        .addPreset(sharkTail(tail, tailJoints))
                        .addAnimator(new SharkHeadInitAnimator(head))
                        .addAnimator(new SharkHeadSwimAnimator(head))
                        .addAnimator(new DoubleArmBobAnimator(upperLeftArm, upperRightArm, lowerLeftArm, lowerRightArm))
                        .addAnimator(new ArmRideAnimator(upperLeftArm, upperLeftArm))
                        .addCameraAnimator(new SharkCameraSwimAnimator());
            };

        }

        public static <T extends ChangedEntity, M extends AdvancedHumanoidModel<T>> Consumer<HumanoidAnimator<T, M>> squidTigerSharkArmorLike(
                ModelPart head,
                ModelPart torso,
                ModelPart upperLeftArm,
                ModelPart upperRightArm,
                ModelPart lowerLeftArm,
                ModelPart lowerRightArm,
                /*ModelPart tail,
                List<ModelPart> tailJoints,*/
                List<ModelPart> upperLeftTentacle,
                List<ModelPart> upperRightTentacle,
                List<ModelPart> lowerLeftTentacle,
                List<ModelPart> lowerRightTentacle,
                ModelPart leftLeg,
                ModelPart leftLegLower,
                ModelPart leftFoot,
                ModelPart leftPad,
                ModelPart rightLeg,
                ModelPart rightLegLower,
                ModelPart rightFoot,
                ModelPart rightPad) {
            return (animator) -> {
                /*animator.addPreset(wolfBipedal(leftLeg, leftLegLower, leftFoot, leftPad, rightLeg, rightLegLower, rightFoot, rightPad))
                        .addPreset(doubleArmUpperBody(head, torso, upperLeftArm, upperRightArm, lowerLeftArm, lowerRightArm))
                        .addPreset(wolfTail(tail, tailJoints)).addPreset(wolfEars(leftEar, rightEar))
                        .addPreset(squidDogTentacles(upperLeftTentacle, upperRightTentacle, lowerLeftTentacle, lowerRightTentacle))
                        .addAnimator(new WolfHeadInitAnimator(head)).addAnimator(new ArmSwimAnimator(upperLeftArm, upperRightArm))
                        .addAnimator(new DoubleArmBobAnimator(upperLeftArm, upperRightArm, lowerLeftArm, lowerRightArm))
                        .addAnimator(new ArmRideAnimator(upperLeftArm, upperRightArm));*/
                animator.addPreset(sharkBipedal(leftLeg, leftLegLower, leftFoot, leftPad, rightLeg, rightLegLower, rightFoot, rightPad))
                        .addPreset(doubleArmUpperBody(head, torso, upperLeftArm, upperRightArm, lowerLeftArm, lowerRightArm))
                        .addPreset(squidDogTentacles(upperLeftTentacle, upperRightTentacle, lowerLeftTentacle, lowerRightTentacle))
                        //.addPreset(sharkTail(tail, tailJoints))
                        .addAnimator(new SharkHeadInitAnimator(head))
                        .addAnimator(new SharkHeadSwimAnimator(head))
                        .addAnimator(new DoubleArmBobAnimator(upperLeftArm, upperRightArm, lowerLeftArm, lowerRightArm))
                        .addAnimator(new ArmRideAnimator(upperLeftArm, upperLeftArm))
                        .addCameraAnimator(new SharkCameraSwimAnimator());
            };
        }

    }

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("changed_addon", "squid_tiger_shark"), "main");
    private final ModelPart Head;
    private final ModelPart Torso;
    private final ModelPart LeftArm;
    private final ModelPart RightArm;
    private final ModelPart LeftArm2;
    private final ModelPart RightArm2;
    private final ModelPart LeftLeg;
    private final ModelPart RightLeg;
    private final ModelPart Tail;
    private final HumanoidAnimator<LatexSquidTigerSharkEntity, LatexSquidTigerSharkModel> animator;

    public LatexSquidTigerSharkModel(ModelPart root) {
        super(root);
        this.RightLeg = root.getChild("RightLeg");
        this.LeftLeg = root.getChild("LeftLeg");
        this.Head = root.getChild("Head");
        this.Torso = root.getChild("Torso");
        this.RightArm = root.getChild("RightArm");
        this.RightArm2 = root.getChild("RightArm2");
        this.LeftArm = root.getChild("LeftArm");
        this.LeftArm2 = root.getChild("LeftArm2");
        this.Tail = Torso.getChild("Tail");

        var tailPrimary = Tail.getChild("TailPrimary");
        var tailSecondary = tailPrimary.getChild("TailSecondary");
        var tailTertiary = tailSecondary.getChild("TailTertiary");

        var leftLowerLeg = LeftLeg.getChild("LeftLowerLeg");
        var leftFoot = leftLowerLeg.getChild("LeftFoot");
        var rightLowerLeg = RightLeg.getChild("RightLowerLeg");
        var rightFoot = rightLowerLeg.getChild("RightFoot");

        var upperRightTentacle = new ArrayList<ModelPart>();
        upperRightTentacle.add(Torso.getChild("RightUpperTentacle"));
        upperRightTentacle.add(last(upperRightTentacle).getChild("TentacleSecondaryRU"));
        upperRightTentacle.add(last(upperRightTentacle).getChild("TentacleTertiaryRU"));
        upperRightTentacle.add(last(upperRightTentacle).getChild("TentacleQuaternaryRU"));
        upperRightTentacle.add(last(upperRightTentacle).getChild("TentaclePadRU"));
        var upperLeftTentacle = new ArrayList<ModelPart>();
        upperLeftTentacle.add(Torso.getChild("LeftUpperTentacle"));
        upperLeftTentacle.add(last(upperLeftTentacle).getChild("TentacleSecondaryLU"));
        upperLeftTentacle.add(last(upperLeftTentacle).getChild("TentacleTertiaryLU"));
        upperLeftTentacle.add(last(upperLeftTentacle).getChild("TentacleQuaternaryLU"));
        upperLeftTentacle.add(last(upperLeftTentacle).getChild("TentaclePadLU"));
        var lowerRightTentacle = new ArrayList<ModelPart>();
        lowerRightTentacle.add(Torso.getChild("RightLowerTentacle"));
        lowerRightTentacle.add(last(lowerRightTentacle).getChild("TentacleSecondaryRL"));
        lowerRightTentacle.add(last(lowerRightTentacle).getChild("TentacleTertiaryRL"));
        lowerRightTentacle.add(last(lowerRightTentacle).getChild("TentacleQuaternaryRL"));
        lowerRightTentacle.add(last(lowerRightTentacle).getChild("TentaclePadRL"));
        var lowerLeftTentacle = new ArrayList<ModelPart>();
        lowerLeftTentacle.add(Torso.getChild("LeftLowerTentacle"));
        lowerLeftTentacle.add(last(lowerLeftTentacle).getChild("TentacleSecondaryLL"));
        lowerLeftTentacle.add(last(lowerLeftTentacle).getChild("TentacleTertiaryLL"));
        lowerLeftTentacle.add(last(lowerLeftTentacle).getChild("TentacleQuaternaryLL"));
        lowerLeftTentacle.add(last(lowerLeftTentacle).getChild("TentaclePadLL"));

        animator = HumanoidAnimator.of(this).hipOffset(-0.8f).legLength(13.0f)
                .addPreset(CustomHybridAnimation.squidTigerSharkLike(
                        Head, Torso, LeftArm, RightArm, LeftArm2, RightArm2,
                        Tail, List.of(tailPrimary, tailSecondary, tailTertiary),
                        upperLeftTentacle, upperRightTentacle, lowerLeftTentacle, lowerRightTentacle,
                        LeftLeg, leftLowerLeg, leftFoot, leftFoot.getChild("LeftPad"), RightLeg, rightLowerLeg, rightFoot, rightFoot.getChild("RightPad")));
        animator.torsoWidth = 5.2f;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offset(-2.5F, 10.5F, 0.0F));

        PartDefinition RightThigh_r1 = RightLeg.addOrReplaceChild("RightThigh_r1", CubeListBuilder.create().texOffs(48, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition RightLowerLeg = RightLeg.addOrReplaceChild("RightLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

        PartDefinition RightCalf_r1 = RightLowerLeg.addOrReplaceChild("RightCalf_r1", CubeListBuilder.create().texOffs(48, 40).addBox(-1.99F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

        PartDefinition RightFoot = RightLowerLeg.addOrReplaceChild("RightFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

        PartDefinition RightArch_r1 = RightFoot.addOrReplaceChild("RightArch_r1", CubeListBuilder.create().texOffs(13, 56).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

        PartDefinition RightPad = RightFoot.addOrReplaceChild("RightPad", CubeListBuilder.create().texOffs(48, 50).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

        PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(2.5F, 10.5F, 0.0F));

        PartDefinition LeftThigh_r1 = LeftLeg.addOrReplaceChild("LeftThigh_r1", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition LeftLowerLeg = LeftLeg.addOrReplaceChild("LeftLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

        PartDefinition LeftCalf_r1 = LeftLowerLeg.addOrReplaceChild("LeftCalf_r1", CubeListBuilder.create().texOffs(48, 22).addBox(-2.01F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

        PartDefinition LeftFoot = LeftLowerLeg.addOrReplaceChild("LeftFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

        PartDefinition LeftArch_r1 = LeftFoot.addOrReplaceChild("LeftArch_r1", CubeListBuilder.create().texOffs(56, 11).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

        PartDefinition LeftPad = LeftFoot.addOrReplaceChild("LeftPad", CubeListBuilder.create().texOffs(24, 0).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

        PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));

        PartDefinition Snout_r1 = Head.addOrReplaceChild("Snout_r1", CubeListBuilder.create().texOffs(55, 57).addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -3.01F, -7.0F, 0.0F, -0.2182F, 0.0F));

        PartDefinition Snout_r2 = Head.addOrReplaceChild("Snout_r2", CubeListBuilder.create().texOffs(0, 59).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, -3.01F, -7.0F, 0.0F, 0.2182F, 0.0F));

        PartDefinition Snout_r3 = Head.addOrReplaceChild("Snout_r3", CubeListBuilder.create().texOffs(48, 11).addBox(-1.5F, -27.0F, -6.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(43, 57).addBox(-1.5F, -29.0F, -7.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 26.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        PartDefinition Base_r1 = Head.addOrReplaceChild("Base_r1", CubeListBuilder.create().texOffs(60, 0).addBox(-2.1808F, -1.0F, -0.5736F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, -1.4F, -1.5F, -0.5236F, 0.9599F, -3.1416F));

        PartDefinition Base_r2 = Head.addOrReplaceChild("Base_r2", CubeListBuilder.create().texOffs(60, 20).addBox(-2.1808F, -1.0F, -0.5736F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -1.4F, -1.5F, 0.5236F, 0.9599F, 0.0F));

        PartDefinition Hair = Head.addOrReplaceChild("Hair", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F))
                .texOffs(24, 8).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Fins = Head.addOrReplaceChild("Fins", CubeListBuilder.create(), PartPose.offset(0.0F, 25.5F, 0.0F));

        PartDefinition HeadFin_r1 = Fins.addOrReplaceChild("HeadFin_r1", CubeListBuilder.create().texOffs(32, 22).addBox(-0.25F, -2.0F, 0.0F, 6.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -32.0F, -1.0F, -1.0263F, -0.733F, -2.1817F));

        PartDefinition HeadFin_r2 = Fins.addOrReplaceChild("HeadFin_r2", CubeListBuilder.create().texOffs(52, 32).addBox(-0.25F, -1.0F, 0.0F, 6.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -32.0F, -1.0F, 1.0263F, -0.733F, -0.9599F));

        PartDefinition Torso = partdefinition.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(28, 28).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));

        PartDefinition BackFin_r1 = Torso.addOrReplaceChild("BackFin_r1", CubeListBuilder.create().texOffs(42, 4).addBox(-1.0F, 2.0F, -3.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.5F, 2.0F, 0.5236F, 0.0F, 0.0F));

        PartDefinition Tail = Torso.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offset(0.0F, 10.5F, 0.0F));

        PartDefinition TailPrimary = Tail.addOrReplaceChild("TailPrimary", CubeListBuilder.create(), PartPose.offset(0.0F, -0.5F, 0.75F));

        PartDefinition TailFin_r1 = TailPrimary.addOrReplaceChild("TailFin_r1", CubeListBuilder.create().texOffs(61, 50).addBox(-4.0F, 4.0F, -0.75F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(43, 62).addBox(-4.0F, 0.0F, 0.25F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, 1.75F, 1.0F, 1.789F, 0.0F, 0.0F));

        PartDefinition Base_r3 = TailPrimary.addOrReplaceChild("Base_r3", CubeListBuilder.create().texOffs(37, 0).addBox(-2.0F, -1.075F, 0.375F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.875F, 0.85F, 1.9199F, 0.0F, 0.0F));

        PartDefinition Base_r4 = TailPrimary.addOrReplaceChild("Base_r4", CubeListBuilder.create().texOffs(32, 44).addBox(-2.0F, 0.75F, -0.8F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, -1.0F, 1.1781F, 0.0F, 0.0F));

        PartDefinition TailSecondary = TailPrimary.addOrReplaceChild("TailSecondary", CubeListBuilder.create(), PartPose.offset(0.0F, 3.25F, 7.25F));

        PartDefinition Base_r5 = TailSecondary.addOrReplaceChild("Base_r5", CubeListBuilder.create().texOffs(16, 32).addBox(-1.5F, -1.3563F, -0.6088F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.25F, 1.0F, 1.309F, 0.0F, 0.0F));

        PartDefinition TailTertiary = TailSecondary.addOrReplaceChild("TailTertiary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.5F, 4.5F));

        PartDefinition Base_r6 = TailTertiary.addOrReplaceChild("Base_r6", CubeListBuilder.create().texOffs(24, 22).addBox(-0.5F, 5.3462F, -1.8296F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F))
                .texOffs(35, 56).addBox(-0.5F, -2.5538F, -1.8296F, 1.0F, 8.0F, 3.0F, new CubeDeformation(-0.05F)), PartPose.offsetAndRotation(0.0F, 0.5F, 4.25F, 1.1345F, 0.0F, 0.0F));

        PartDefinition Base_r7 = TailTertiary.addOrReplaceChild("Base_r7", CubeListBuilder.create().texOffs(47, 62).addBox(-0.5F, -6.1668F, 0.8821F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(27, 56).addBox(-0.5F, -8.1668F, -2.1179F, 1.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, 4.25F, -0.8727F, 0.0F, 0.0F));

        PartDefinition Base_r8 = TailTertiary.addOrReplaceChild("Base_r8", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, -0.3449F, -0.7203F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.25F, 0.25F, 1.4835F, 0.0F, 0.0F));

        PartDefinition RightUpperTentacle = Torso.addOrReplaceChild("RightUpperTentacle", CubeListBuilder.create(), PartPose.offset(-2.5F, 1.0F, 1.0F));

        PartDefinition TentaclePart_r1 = RightUpperTentacle.addOrReplaceChild("TentaclePart_r1", CubeListBuilder.create().texOffs(80, 86).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.192F, -0.4363F, -0.0524F));

        PartDefinition TentacleSecondaryRU = RightUpperTentacle.addOrReplaceChild("TentacleSecondaryRU", CubeListBuilder.create(), PartPose.offset(-1.5F, -0.5F, 3.3F));

        PartDefinition TentaclePart_r2 = TentacleSecondaryRU.addOrReplaceChild("TentaclePart_r2", CubeListBuilder.create().texOffs(0, 81).addBox(0.0F, -1.0F, 3.55F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 0.5F, -3.3F, 0.2269F, -0.6981F, -0.1134F));

        PartDefinition TentacleTertiaryRU = TentacleSecondaryRU.addOrReplaceChild("TentacleTertiaryRU", CubeListBuilder.create(), PartPose.offset(-2.4F, -0.7F, 2.7F));

        PartDefinition TentaclePart_r3 = TentacleTertiaryRU.addOrReplaceChild("TentaclePart_r3", CubeListBuilder.create().texOffs(68, 86).addBox(1.8F, -1.0F, 6.75F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.9F, 1.2F, -6.0F, 0.2967F, -0.9425F, -0.2094F));

        PartDefinition TentacleQuaternaryRU = TentacleTertiaryRU.addOrReplaceChild("TentacleQuaternaryRU", CubeListBuilder.create(), PartPose.offset(-3.0F, -0.5F, 2.2F));

        PartDefinition TentaclePart_r4 = TentacleQuaternaryRU.addOrReplaceChild("TentaclePart_r4", CubeListBuilder.create().texOffs(46, 86).addBox(4.45F, -1.0F, 9.375F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.9F, 1.7F, -8.2F, 0.4712F, -1.1868F, -0.4102F));

        PartDefinition TentaclePadRU = TentacleQuaternaryRU.addOrReplaceChild("TentaclePadRU", CubeListBuilder.create(), PartPose.offset(-3.6F, -0.4F, 1.3F));

        PartDefinition TentaclePart_r5 = TentaclePadRU.addOrReplaceChild("TentaclePart_r5", CubeListBuilder.create().texOffs(43, 81).addBox(6.075F, -1.5F, 16.4F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(76, 40).addBox(6.075F, -2.5F, 12.4F, 2.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.5F, 2.1F, -9.5F, 0.6807F, -1.2915F, -0.6283F));

        PartDefinition RightLowerTentacle = Torso.addOrReplaceChild("RightLowerTentacle", CubeListBuilder.create(), PartPose.offset(-2.5F, 5.0F, 1.0F));

        PartDefinition TentaclePart_r6 = RightLowerTentacle.addOrReplaceChild("TentaclePart_r6", CubeListBuilder.create().texOffs(84, 62).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.192F, -0.4276F, 0.0524F));

        PartDefinition TentacleSecondaryRL = RightLowerTentacle.addOrReplaceChild("TentacleSecondaryRL", CubeListBuilder.create(), PartPose.offset(-1.5F, 0.5F, 3.3F));

        PartDefinition TentaclePart_r7 = TentacleSecondaryRL.addOrReplaceChild("TentaclePart_r7", CubeListBuilder.create().texOffs(77, 49).addBox(0.0F, -1.0F, 3.6F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, -0.5F, -3.3F, -0.2182F, -0.6894F, 0.1134F));

        PartDefinition TentacleTertiaryRL = TentacleSecondaryRL.addOrReplaceChild("TentacleTertiaryRL", CubeListBuilder.create(), PartPose.offset(-2.4F, 0.7F, 2.9F));

        PartDefinition TentaclePart_r8 = TentacleTertiaryRL.addOrReplaceChild("TentaclePart_r8", CubeListBuilder.create().texOffs(84, 55).addBox(1.9F, -1.05F, 6.8F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.9F, -1.2F, -6.2F, -0.2967F, -0.9425F, 0.2094F));

        PartDefinition TentacleQuaternaryRL = TentacleTertiaryRL.addOrReplaceChild("TentacleQuaternaryRL", CubeListBuilder.create(), PartPose.offset(-2.9F, 0.4F, 2.0F));

        PartDefinition TentaclePart_r9 = TentacleQuaternaryRL.addOrReplaceChild("TentaclePart_r9", CubeListBuilder.create().texOffs(84, 36).addBox(4.525F, -1.0F, 9.3F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.8F, -1.6F, -8.2F, -0.4712F, -1.1868F, 0.4102F));

        PartDefinition TentaclePadRL = TentacleQuaternaryRL.addOrReplaceChild("TentaclePadRL", CubeListBuilder.create(), PartPose.offset(-3.7F, 0.3F, 1.3F));

        PartDefinition TentaclePart_r10 = TentaclePadRL.addOrReplaceChild("TentaclePart_r10", CubeListBuilder.create().texOffs(12, 91).addBox(6.15F, -1.5F, 16.4F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(76, 66).addBox(6.15F, -2.5F, 12.4F, 2.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.5F, -1.9F, -9.5F, -0.6807F, -1.2915F, 0.6283F));

        PartDefinition LeftUpperTentacle = Torso.addOrReplaceChild("LeftUpperTentacle", CubeListBuilder.create(), PartPose.offset(2.5F, 1.0F, 1.0F));

        PartDefinition TentaclePart_r11 = LeftUpperTentacle.addOrReplaceChild("TentaclePart_r11", CubeListBuilder.create().texOffs(80, 24).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.192F, 0.4363F, 0.0524F));

        PartDefinition TentacleSecondaryLU = LeftUpperTentacle.addOrReplaceChild("TentacleSecondaryLU", CubeListBuilder.create(), PartPose.offset(1.5F, -0.5F, 3.3F));

        PartDefinition TentaclePart_r12 = TentacleSecondaryLU.addOrReplaceChild("TentaclePart_r12", CubeListBuilder.create().texOffs(16, 81).addBox(-2.0F, -1.0F, 3.55F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 0.5F, -3.3F, 0.2269F, 0.6981F, 0.1134F));

        PartDefinition TentacleTertiaryLU = TentacleSecondaryLU.addOrReplaceChild("TentacleTertiaryLU", CubeListBuilder.create(), PartPose.offset(2.4F, -0.7F, 2.7F));

        PartDefinition TentaclePart_r13 = TentacleTertiaryLU.addOrReplaceChild("TentaclePart_r13", CubeListBuilder.create().texOffs(80, 18).addBox(-3.8F, -1.0F, 6.75F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.9F, 1.2F, -6.0F, 0.2967F, 0.9425F, 0.2094F));

        PartDefinition TentacleQuaternaryLU = TentacleTertiaryLU.addOrReplaceChild("TentacleQuaternaryLU", CubeListBuilder.create(), PartPose.offset(3.0F, -0.5F, 2.2F));

        PartDefinition TentaclePart_r14 = TentacleQuaternaryLU.addOrReplaceChild("TentaclePart_r14", CubeListBuilder.create().texOffs(34, 85).addBox(-6.45F, -1.0F, 9.375F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.9F, 1.7F, -8.2F, 0.4712F, 1.1868F, 0.4102F));

        PartDefinition TentaclePadLU = TentacleQuaternaryLU.addOrReplaceChild("TentaclePadLU", CubeListBuilder.create(), PartPose.offset(3.6F, -0.4F, 1.3F));

        PartDefinition TentaclePart_r15 = TentaclePadLU.addOrReplaceChild("TentaclePart_r15", CubeListBuilder.create().texOffs(51, 81).addBox(-8.075F, -1.5F, 16.4F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(60, 80).addBox(-8.075F, -2.5F, 12.4F, 2.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5F, 2.1F, -9.5F, 0.6807F, 1.2915F, 0.6283F));

        PartDefinition LeftLowerTentacle = Torso.addOrReplaceChild("LeftLowerTentacle", CubeListBuilder.create(), PartPose.offset(2.5F, 5.0F, 1.0F));

        PartDefinition TentaclePart_r16 = LeftLowerTentacle.addOrReplaceChild("TentaclePart_r16", CubeListBuilder.create().texOffs(84, 30).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.192F, 0.4276F, -0.0524F));

        PartDefinition TentacleSecondaryLL = LeftLowerTentacle.addOrReplaceChild("TentacleSecondaryLL", CubeListBuilder.create(), PartPose.offset(1.5F, 0.5F, 3.3F));

        PartDefinition TentaclePart_r17 = TentacleSecondaryLL.addOrReplaceChild("TentaclePart_r17", CubeListBuilder.create().texOffs(26, 83).addBox(-2.0F, -1.0F, 3.6F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -0.5F, -3.3F, -0.2182F, 0.6894F, -0.1134F));

        PartDefinition TentacleTertiaryLL = TentacleSecondaryLL.addOrReplaceChild("TentacleTertiaryLL", CubeListBuilder.create(), PartPose.offset(2.4F, 0.7F, 2.9F));

        PartDefinition TentaclePart_r18 = TentacleTertiaryLL.addOrReplaceChild("TentaclePart_r18", CubeListBuilder.create().texOffs(54, 89).addBox(-3.9F, -1.05F, 6.8F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.9F, -1.2F, -6.2F, -0.2967F, 0.9425F, -0.2094F));

        PartDefinition TentacleQuaternaryLL = TentacleTertiaryLL.addOrReplaceChild("TentacleQuaternaryLL", CubeListBuilder.create(), PartPose.offset(2.9F, 0.4F, 2.0F));

        PartDefinition TentaclePart_r19 = TentacleQuaternaryLL.addOrReplaceChild("TentaclePart_r19", CubeListBuilder.create().texOffs(22, 89).addBox(-6.525F, -1.0F, 9.3F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.8F, -1.6F, -8.2F, -0.4712F, 1.1868F, -0.4102F));

        PartDefinition TentaclePadLL = TentacleQuaternaryLL.addOrReplaceChild("TentaclePadLL", CubeListBuilder.create(), PartPose.offset(3.7F, 0.3F, 1.3F));

        PartDefinition TentaclePart_r20 = TentaclePadLL.addOrReplaceChild("TentaclePart_r20", CubeListBuilder.create().texOffs(84, 75).addBox(-8.15F, -1.5F, 16.4F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(76, 57).addBox(-8.15F, -2.5F, 12.4F, 2.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5F, -1.9F, -9.5F, -0.6807F, 1.2915F, -0.6283F));

        PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(0, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 1.5F, 0.0F));

        PartDefinition Spike_r1 = RightArm.addOrReplaceChild("Spike_r1", CubeListBuilder.create().texOffs(24, 0).addBox(-0.5F, -1.5F, -1.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(60, 37).addBox(-0.5F, -2.5F, -2.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0789F, 2.8746F, 1.1151F, -2.6425F, 0.8346F, 3.1091F));

        PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(16, 40).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 1.5F, 0.0F));

        PartDefinition Spike_r2 = LeftArm.addOrReplaceChild("Spike_r2", CubeListBuilder.create().texOffs(51, 62).addBox(0.875F, -1.5F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(48, 50).addBox(-0.125F, -0.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.6568F, 2.0711F, 1.6568F, -0.4796F, -0.6979F, 0.7102F));

        PartDefinition RightArm2 = partdefinition.addOrReplaceChild("RightArm2", CubeListBuilder.create().texOffs(0, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 5.5F, 0.0F));

        PartDefinition Spike_r3 = RightArm2.addOrReplaceChild("Spike_r3", CubeListBuilder.create().texOffs(24, 0).addBox(-0.5F, -1.5F, -1.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(60, 37).addBox(-0.5F, -2.5F, -2.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0789F, 2.8746F, 1.1151F, -2.6425F, 0.8346F, 3.1091F));

        PartDefinition LeftArm2 = partdefinition.addOrReplaceChild("LeftArm2", CubeListBuilder.create().texOffs(16, 40).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 5.5F, 0.0F));

        PartDefinition Spike_r4 = LeftArm2.addOrReplaceChild("Spike_r4", CubeListBuilder.create().texOffs(51, 62).addBox(0.875F, -1.5F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(48, 50).addBox(-0.125F, -0.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.6568F, 2.0711F, 1.6568F, -0.4796F, -0.6979F, 0.7102F));

        return LayerDefinition.create(meshdefinition, 96, 96);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green,
                               float blue, float alpha) {
        Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Torso.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        LeftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        RightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        LeftArm2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        RightArm2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        LeftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        RightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void prepareMobModel(LatexSquidTigerSharkEntity p_102861_, float p_102862_, float p_102863_, float p_102864_) {
        this.prepareMobModel(animator, p_102861_, p_102862_, p_102863_, p_102864_);
    }

    @Override
    public void setupAnim(LatexSquidTigerSharkEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animator.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    public PoseStack getPlacementCorrectors(CorrectorType type) {
        PoseStack corrector = AdvancedHumanoidModelInterface.super.getPlacementCorrectors(type);
        /*if (type.isArm())
            corrector.translate(0.0f, -6f / 16.0f, 0.0f);*/
        if (type == CorrectorType.HAIR)
            corrector.translate(0.0f, -1f / 16.0f, 0.0f);
        return corrector;
    }

    @Override
    public ModelPart getArm(HumanoidArm humanoidArm) {
        return switch (humanoidArm) {
            case LEFT -> LeftArm;
            case RIGHT -> RightArm;
        };
    }

    @Override
    public ModelPart getOtherArm(HumanoidArm humanoidArm) {
        return switch (humanoidArm) {
            case LEFT -> LeftArm2;
            case RIGHT -> RightArm2;
        };
    }

    public ModelPart getLeg(HumanoidArm p_102852_) {
        return p_102852_ == HumanoidArm.LEFT ? this.LeftLeg : this.RightLeg;
    }

    @Override
    public void translateToUpperHand(HumanoidArm arm, PoseStack poseStack) {
        this.getArm(arm).translateAndRotate(poseStack);
        poseStack.translate(0.0, (this.animator.armLength - 12.0f) / 20.0, 0.0);
    }

    @Override
    public void translateToLowerHand(HumanoidArm arm, PoseStack poseStack) {
        this.getOtherArm(arm).translateAndRotate(poseStack);
        poseStack.translate(0.0, (this.animator.armLength - 12.0f) / 20.0, 0.0);
    }

    @Override
    public void setupHand() {
        animator.setupHand();
    }

    @Override
    public HumanoidAnimator<LatexSquidTigerSharkEntity, LatexSquidTigerSharkModel> getAnimator() {
        return animator;
    }

    @Override
    public ModelPart getHead() {
        return Head;
    }

    public ModelPart getTorso() {
        return Torso;
    }
}