package net.foxyas.changedaddon.client.model.advanced;// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.client.model.animations.ChangedAddonAnimationsPresets;
import net.foxyas.changedaddon.client.model.animations.ChangedAddonAnimationsPresets.TailSet;
import net.foxyas.changedaddon.client.renderer.layers.animation.CarryAbilityAnimation;
import net.foxyas.changedaddon.entity.advanced.LatexKitsuneFemaleEntity;
import net.ltxprogrammer.changed.client.animations.Limb;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModelInterface;
import net.ltxprogrammer.changed.client.tfanimations.HelperModel;
import net.ltxprogrammer.changed.client.tfanimations.TransfurHelper;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LatexKitsuneFemaleModel extends AdvancedHumanoidModel<LatexKitsuneFemaleEntity> implements AdvancedHumanoidModelInterface<LatexKitsuneFemaleEntity, LatexKitsuneFemaleModel> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("changed_addon", "kitsune_female"), "main");

    private final ModelPart Hair;
    private final ModelPart Head;
    private final ModelPart RightEar;
    private final ModelPart RightEarPivot;
    private final ModelPart LeftEar;
    private final ModelPart LeftEarPivot;

    private final ModelPart Torso;
    private final ModelPart neckFur;
    private final ModelPart Plantoids;
    private final ModelPart Tails;

    private final ModelPart TailUpperRight;
    private final ModelPart TailUpperRightPrimary;
    private final ModelPart TailUpperRightSecondary;
    private final ModelPart TailUpperRightTertiary;
    private final ModelPart TailUpperRightQuaternary;

    private final ModelPart TailUpperLeft;
    private final ModelPart TailUpperLeftPrimary;
    private final ModelPart TailUpperLeftSecondary;
    private final ModelPart TailUpperLeftTertiary;
    private final ModelPart TailUpperLeftQuaternary;

    private final ModelPart TailLowerRight;
    private final ModelPart TailLowerRightPrimary;
    private final ModelPart TailLowerRightSecondary;
    private final ModelPart TailLowerRightTertiary;
    private final ModelPart TailLowerRightQuaternary;

    private final ModelPart TailLowerLeft;
    private final ModelPart TailLowerLeftPrimary;
    private final ModelPart TailLowerLeftSecondary;
    private final ModelPart TailLowerLeftTertiary;
    private final ModelPart TailLowerLeftQuaternary;

    private final ModelPart TailMiddleMiddle;
    private final ModelPart TailMiddleMiddlePrimary;
    private final ModelPart TailMiddleMiddleSecondary;
    private final ModelPart TailMiddleMiddleTertiary;
    private final ModelPart TailMiddleMiddleQuaternary;

    private final ModelPart TailMiddleUpper;
    private final ModelPart TailMiddleUpperPrimary;
    private final ModelPart TailMiddleUpperSecondary;
    private final ModelPart TailMiddleUpperTertiary;
    private final ModelPart TailMiddleUpperQuaternary;

    private final ModelPart TailMiddleLower;
    private final ModelPart TailMiddleLowerPrimary;
    private final ModelPart TailMiddleLowerSecondary;
    private final ModelPart TailMiddleLowerTertiary;
    private final ModelPart TailMiddleLowerQuaternary;

    private final ModelPart RightArm;
    private final ModelPart RightArmPawBeans;
    private final ModelPart RightArmFur;

    private final ModelPart LeftArm;
    private final ModelPart LeftArmPawBeans;
    private final ModelPart LeftArmFur;

    private final ModelPart RightLeg;
    private final ModelPart RightLowerLeg;
    private final ModelPart RightFoot;
    private final ModelPart RightPad;
    private final ModelPart RightPawBeans;

    private final ModelPart LeftLeg;
    private final ModelPart LeftLowerLeg;
    private final ModelPart LeftFoot;
    private final ModelPart LeftPad;
    private final ModelPart LeftPawBeans;

    private final HumanoidAnimator<LatexKitsuneFemaleEntity, LatexKitsuneFemaleModel> animator;

    public LatexKitsuneFemaleModel(ModelPart root) {
        super(root);
        this.Head = root.getChild("Head");
        this.RightEar = this.Head.getChild("RightEar");
        this.RightEarPivot = this.RightEar.getChild("RightEarPivot");
        this.LeftEar = this.Head.getChild("LeftEar");
        this.LeftEarPivot = this.LeftEar.getChild("LeftEarPivot");
        this.Hair = this.Head.getChild("Hair");
        this.Torso = root.getChild("Torso");
        this.neckFur = this.Torso.getChild("NeckFur");
        this.Tails = this.Torso.getChild("Tails");
        this.TailUpperRight = this.Tails.getChild("TailUpperRight");
        this.TailUpperRightPrimary = this.TailUpperRight.getChild("TailUpperRightPrimary");
        this.TailUpperRightSecondary = this.TailUpperRightPrimary.getChild("TailUpperRightSecondary");
        this.TailUpperRightTertiary = this.TailUpperRightSecondary.getChild("TailUpperRightTertiary");
        this.TailUpperRightQuaternary = this.TailUpperRightTertiary.getChild("TailUpperRightQuaternary");
        this.TailUpperLeft = this.Tails.getChild("TailUpperLeft");
        this.TailUpperLeftPrimary = this.TailUpperLeft.getChild("TailUpperLeftPrimary");
        this.TailUpperLeftSecondary = this.TailUpperLeftPrimary.getChild("TailUpperLeftSecondary");
        this.TailUpperLeftTertiary = this.TailUpperLeftSecondary.getChild("TailUpperLeftTertiary");
        this.TailUpperLeftQuaternary = this.TailUpperLeftTertiary.getChild("TailUpperLeftQuaternary");
        this.TailLowerRight = this.Tails.getChild("TailLowerRight");
        this.TailLowerRightPrimary = this.TailLowerRight.getChild("TailLowerRightPrimary");
        this.TailLowerRightSecondary = this.TailLowerRightPrimary.getChild("TailLowerRightSecondary");
        this.TailLowerRightTertiary = this.TailLowerRightSecondary.getChild("TailLowerRightTertiary");
        this.TailLowerRightQuaternary = this.TailLowerRightTertiary.getChild("TailLowerRightQuaternary");
        this.TailLowerLeft = this.Tails.getChild("TailLowerLeft");
        this.TailLowerLeftPrimary = this.TailLowerLeft.getChild("TailLowerLeftPrimary");
        this.TailLowerLeftSecondary = this.TailLowerLeftPrimary.getChild("TailLowerLeftSecondary");
        this.TailLowerLeftTertiary = this.TailLowerLeftSecondary.getChild("TailLowerLeftTertiary");
        this.TailLowerLeftQuaternary = this.TailLowerLeftTertiary.getChild("TailLowerLeftQuaternary");
        this.TailMiddleMiddle = this.Tails.getChild("TailMiddleMiddle");
        this.TailMiddleMiddlePrimary = this.TailMiddleMiddle.getChild("TailMiddleMiddlePrimary");
        this.TailMiddleMiddleSecondary = this.TailMiddleMiddlePrimary.getChild("TailMiddleMiddleSecondary");
        this.TailMiddleMiddleTertiary = this.TailMiddleMiddleSecondary.getChild("TailMiddleMiddleTertiary");
        this.TailMiddleMiddleQuaternary = this.TailMiddleMiddleTertiary.getChild("TailMiddleMiddleQuaternary");
        this.TailMiddleUpper = this.Tails.getChild("TailMiddleUpper");
        this.TailMiddleUpperPrimary = this.TailMiddleUpper.getChild("TailMiddleUpperPrimary");
        this.TailMiddleUpperSecondary = this.TailMiddleUpperPrimary.getChild("TailMiddleUpperSecondary");
        this.TailMiddleUpperTertiary = this.TailMiddleUpperSecondary.getChild("TailMiddleUpperTertiary");
        this.TailMiddleUpperQuaternary = this.TailMiddleUpperTertiary.getChild("TailMiddleUpperQuaternary");
        this.TailMiddleLower = this.Tails.getChild("TailMiddleLower");
        this.TailMiddleLowerPrimary = this.TailMiddleLower.getChild("TailMiddleLowerPrimary");
        this.TailMiddleLowerSecondary = this.TailMiddleLowerPrimary.getChild("TailMiddleLowerSecondary");
        this.TailMiddleLowerTertiary = this.TailMiddleLowerSecondary.getChild("TailMiddleLowerTertiary");
        this.TailMiddleLowerQuaternary = this.TailMiddleLowerTertiary.getChild("TailMiddleLowerQuaternary");
        this.Plantoids = this.Torso.getChild("Plantoids");
        this.RightArm = root.getChild("RightArm");
        this.RightArmPawBeans = this.RightArm.getChild("RightArmPawBeans");
        this.RightArmFur = this.RightArm.getChild("RightArmFur");
        this.LeftArm = root.getChild("LeftArm");
        this.LeftArmPawBeans = this.LeftArm.getChild("LeftArmPawBeans");
        this.LeftArmFur = this.LeftArm.getChild("LeftArmFur");
        this.RightLeg = root.getChild("RightLeg");
        this.RightLowerLeg = this.RightLeg.getChild("RightLowerLeg");
        this.RightFoot = this.RightLowerLeg.getChild("RightFoot");
        this.RightPad = this.RightFoot.getChild("RightPad");
        this.RightPawBeans = this.RightPad.getChild("RightPawBeans");
        this.LeftLeg = root.getChild("LeftLeg");
        this.LeftLowerLeg = this.LeftLeg.getChild("LeftLowerLeg");
        this.LeftFoot = this.LeftLowerLeg.getChild("LeftFoot");
        this.LeftPad = this.LeftFoot.getChild("LeftPad");
        this.LeftPawBeans = this.LeftPad.getChild("LeftPawBeans");

        List<TailSet> tails = List.of(
                new TailSet(TailUpperRight, List.of(TailUpperRightPrimary, TailUpperRightSecondary, TailUpperRightTertiary, TailUpperRightQuaternary)),
                new TailSet(TailUpperLeft, List.of(TailUpperLeftPrimary, TailUpperLeftSecondary, TailUpperLeftTertiary, TailUpperLeftQuaternary)),
                new TailSet(TailLowerRight, List.of(TailLowerRightPrimary, TailLowerRightSecondary, TailLowerRightTertiary, TailLowerRightQuaternary)),
                new TailSet(TailLowerLeft, List.of(TailLowerLeftPrimary, TailLowerLeftSecondary, TailLowerLeftTertiary, TailLowerLeftQuaternary)),
                new TailSet(TailMiddleMiddle, List.of(TailMiddleMiddlePrimary, TailMiddleMiddleSecondary, TailMiddleMiddleTertiary, TailMiddleMiddleQuaternary)),
                new TailSet(TailMiddleUpper, List.of(TailMiddleUpperPrimary, TailMiddleUpperSecondary, TailMiddleUpperTertiary, TailMiddleUpperQuaternary)),
                new TailSet(TailMiddleLower, List.of(TailMiddleLowerPrimary, TailMiddleLowerSecondary, TailMiddleLowerTertiary, TailMiddleLowerQuaternary))
        );

        animator = HumanoidAnimator.of(this).hipOffset(-1.5f)
                .addPreset(ChangedAddonAnimationsPresets.wolfLikeMultiTail(
                        Head, Head.getChild("LeftEar"), Head.getChild("RightEar"),
                        Torso, LeftArm, RightArm,
                        tails,
                        LeftLeg, LeftLowerLeg, LeftFoot, LeftPad, RightLeg, RightLowerLeg, RightFoot, RightPad));
    }

    @SuppressWarnings("unused")
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(32, 8).addBox(-2.0F, -3.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(24, 6).addBox(-1.5F, -1.0F, -5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));

        PartDefinition Snout_r1 = Head.addOrReplaceChild("Snout_r1", CubeListBuilder.create().texOffs(24, 2).addBox(-1.0F, -29.625F, -0.95F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 26.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition RightEar = Head.addOrReplaceChild("RightEar", CubeListBuilder.create(), PartPose.offset(-3.0F, -7.7F, 0.0F));

        PartDefinition RightEarPivot = RightEar.addOrReplaceChild("RightEarPivot", CubeListBuilder.create().texOffs(0, 4).addBox(-1.9F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(28, 0).addBox(-0.9F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
                .texOffs(28, 4).addBox(-0.9F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(24, 0).addBox(0.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0501F)), PartPose.offsetAndRotation(0.5F, -1.25F, 0.0F, -0.1309F, 0.5236F, -0.3491F));

        PartDefinition LeftEar = Head.addOrReplaceChild("LeftEar", CubeListBuilder.create(), PartPose.offset(3.0F, -7.7F, 0.0F));

        PartDefinition LeftEarPivot = LeftEar.addOrReplaceChild("LeftEarPivot", CubeListBuilder.create().texOffs(0, 0).addBox(-1.1F, -1.2F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(34, 0).addBox(-1.1F, -1.6F, -0.4F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.04F))
                .texOffs(32, 6).addBox(-1.1F, -2.3F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F))
                .texOffs(24, 4).addBox(-1.1F, -3.1F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0501F)), PartPose.offsetAndRotation(-0.5F, -1.25F, 0.0F, -0.1309F, -0.5236F, 0.3491F));

        PartDefinition Hair = Head.addOrReplaceChild("Hair", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 11.0F, 8.0F, new CubeDeformation(0.2F))
                .texOffs(32, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.35F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Torso = partdefinition.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(32, 35).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(58, 58).addBox(-4.0F, 4.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(-0.3F))
                .texOffs(32, 44).addBox(-4.0F, 8.6F, -2.0F, 8.0F, 3.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -0.5F, 0.0F));

        PartDefinition NeckFur = Torso.addOrReplaceChild("NeckFur", CubeListBuilder.create(), PartPose.offset(0.0F, 4.2F, 0.4F));

        PartDefinition NeckFur_r1 = NeckFur.addOrReplaceChild("NeckFur_r1", CubeListBuilder.create().texOffs(11, 71).addBox(-2.5F, -2.5F, 0.0F, 6.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 5.0F, -1.9F, 0.2618F, 0.0F, 0.0F));

        PartDefinition NeckFur_r2 = NeckFur.addOrReplaceChild("NeckFur_r2", CubeListBuilder.create().texOffs(11, 66).addBox(-2.5F, -2.5F, 0.0F, 6.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 2.5F, -2.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition Tails = Torso.addOrReplaceChild("Tails", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 10.0F, 0.4F, -0.0436F, 0.0F, 0.0F));

        PartDefinition TailUpperRight = Tails.addOrReplaceChild("TailUpperRight", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.19F, -0.2865F, -0.1074F));

        PartDefinition TailUpperRightPrimary = TailUpperRight.addOrReplaceChild("TailUpperRightPrimary", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.1F, 0.1309F, 0.0F, 0.0F));

        PartDefinition Base_r1 = TailUpperRightPrimary.addOrReplaceChild("Base_r1", CubeListBuilder.create().texOffs(110, 0).addBox(-2.0F, 0.75F, -1.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.45F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1781F, 0.0F, 0.0F));

        PartDefinition TailUpperRightSecondary = TailUpperRightPrimary.addOrReplaceChild("TailUpperRightSecondary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.25F, 5.6F));

        PartDefinition Base_r2 = TailUpperRightSecondary.addOrReplaceChild("Base_r2", CubeListBuilder.create().texOffs(106, 76).addBox(-2.5F, -0.45F, -2.0F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.55F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 1.4835F, 0.0F, 0.0F));

        PartDefinition TailUpperRightTertiary = TailUpperRightSecondary.addOrReplaceChild("TailUpperRightTertiary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.75F, 7.1F));

        PartDefinition Base_r3 = TailUpperRightTertiary.addOrReplaceChild("Base_r3", CubeListBuilder.create().texOffs(94, 0).addBox(-2.0F, -0.7F, -1.95F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 1.8326F, 0.0F, 0.0F));

        PartDefinition TailUpperRightQuaternary = TailUpperRightTertiary.addOrReplaceChild("TailUpperRightQuaternary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 1.0F));

        PartDefinition Base_r4 = TailUpperRightQuaternary.addOrReplaceChild("Base_r4", CubeListBuilder.create().texOffs(78, 0).addBox(-2.0F, -1.2F, -1.95F, 4.0F, 4.0F, 4.0F, new CubeDeformation(-0.45F)), PartPose.offsetAndRotation(0.0F, -0.7F, 3.0F, 2.0071F, 0.0F, 0.0F));

        PartDefinition TailUpperLeft = Tails.addOrReplaceChild("TailUpperLeft", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1917F, 0.314F, 0.0067F));

        PartDefinition TailUpperLeftPrimary = TailUpperLeft.addOrReplaceChild("TailUpperLeftPrimary", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.1F, 0.1309F, 0.0F, 0.0F));

        PartDefinition Base_r5 = TailUpperLeftPrimary.addOrReplaceChild("Base_r5", CubeListBuilder.create().texOffs(110, 9).addBox(-2.0F, 0.75F, -1.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.45F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1781F, 0.0F, 0.0F));

        PartDefinition TailUpperLeftSecondary = TailUpperLeftPrimary.addOrReplaceChild("TailUpperLeftSecondary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.25F, 5.6F));

        PartDefinition Base_r6 = TailUpperLeftSecondary.addOrReplaceChild("Base_r6", CubeListBuilder.create().texOffs(86, 56).addBox(-2.5F, -0.45F, -2.0F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.55F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 1.4835F, 0.0F, 0.0F));

        PartDefinition TailUpperLeftTertiary = TailUpperLeftSecondary.addOrReplaceChild("TailUpperLeftTertiary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.75F, 7.1F));

        PartDefinition Base_r7 = TailUpperLeftTertiary.addOrReplaceChild("Base_r7", CubeListBuilder.create().texOffs(94, 8).addBox(-2.0F, -0.7F, -1.95F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 1.8326F, 0.0F, 0.0F));

        PartDefinition TailUpperLeftQuaternary = TailUpperLeftTertiary.addOrReplaceChild("TailUpperLeftQuaternary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 1.0F));

        PartDefinition Base_r8 = TailUpperLeftQuaternary.addOrReplaceChild("Base_r8", CubeListBuilder.create().texOffs(78, 8).addBox(-2.0F, -1.2F, -1.95F, 4.0F, 4.0F, 4.0F, new CubeDeformation(-0.45F)), PartPose.offsetAndRotation(0.0F, -0.7F, 3.0F, 2.0071F, 0.0F, 0.0F));

        PartDefinition TailLowerRight = Tails.addOrReplaceChild("TailLowerRight", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1745F, -0.3054F, 0.0F));

        PartDefinition TailLowerRightPrimary = TailLowerRight.addOrReplaceChild("TailLowerRightPrimary", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.1F, 0.1309F, 0.0F, 0.0F));

        PartDefinition Base_r9 = TailLowerRightPrimary.addOrReplaceChild("Base_r9", CubeListBuilder.create().texOffs(110, 18).addBox(-2.0F, 0.75F, -1.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.45F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1781F, 0.0F, 0.0F));

        PartDefinition TailLowerRightSecondary = TailLowerRightPrimary.addOrReplaceChild("TailLowerRightSecondary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.25F, 5.6F));

        PartDefinition Base_r10 = TailLowerRightSecondary.addOrReplaceChild("Base_r10", CubeListBuilder.create().texOffs(106, 89).addBox(-2.5F, -0.45F, -2.0F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.55F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 1.4835F, 0.0F, 0.0F));

        PartDefinition TailLowerRightTertiary = TailLowerRightSecondary.addOrReplaceChild("TailLowerRightTertiary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.75F, 7.1F));

        PartDefinition Base_r11 = TailLowerRightTertiary.addOrReplaceChild("Base_r11", CubeListBuilder.create().texOffs(94, 16).addBox(-2.0F, -0.7F, -1.95F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 1.8326F, 0.0F, 0.0F));

        PartDefinition TailLowerRightQuaternary = TailLowerRightTertiary.addOrReplaceChild("TailLowerRightQuaternary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 1.0F));

        PartDefinition Base_r12 = TailLowerRightQuaternary.addOrReplaceChild("Base_r12", CubeListBuilder.create().texOffs(78, 16).addBox(-2.0F, -1.2F, -1.95F, 4.0F, 4.0F, 4.0F, new CubeDeformation(-0.45F)), PartPose.offsetAndRotation(0.0F, -0.7F, 3.0F, 2.0071F, 0.0F, 0.0F));

        PartDefinition TailLowerLeft = Tails.addOrReplaceChild("TailLowerLeft", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1741F, 0.2967F, -0.1043F));

        PartDefinition TailLowerLeftPrimary = TailLowerLeft.addOrReplaceChild("TailLowerLeftPrimary", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.1F, 0.1309F, 0.0F, 0.0F));

        PartDefinition Base_r13 = TailLowerLeftPrimary.addOrReplaceChild("Base_r13", CubeListBuilder.create().texOffs(110, 27).addBox(-2.0F, 0.75F, -1.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.45F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1781F, 0.0F, 0.0F));

        PartDefinition TailLowerLeftSecondary = TailLowerLeftPrimary.addOrReplaceChild("TailLowerLeftSecondary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.25F, 5.6F));

        PartDefinition Base_r14 = TailLowerLeftSecondary.addOrReplaceChild("Base_r14", CubeListBuilder.create().texOffs(86, 95).addBox(-2.5F, -0.45F, -2.0F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.55F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 1.4835F, 0.0F, 0.0F));

        PartDefinition TailLowerLeftTertiary = TailLowerLeftSecondary.addOrReplaceChild("TailLowerLeftTertiary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.75F, 7.1F));

        PartDefinition Base_r15 = TailLowerLeftTertiary.addOrReplaceChild("Base_r15", CubeListBuilder.create().texOffs(94, 24).addBox(-2.0F, -0.7F, -1.95F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 1.8326F, 0.0F, 0.0F));

        PartDefinition TailLowerLeftQuaternary = TailLowerLeftTertiary.addOrReplaceChild("TailLowerLeftQuaternary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 1.0F));

        PartDefinition Base_r16 = TailLowerLeftQuaternary.addOrReplaceChild("Base_r16", CubeListBuilder.create().texOffs(78, 24).addBox(-2.0F, -1.2F, -1.95F, 4.0F, 4.0F, 4.0F, new CubeDeformation(-0.45F)), PartPose.offsetAndRotation(0.0F, -0.7F, 3.0F, 2.0071F, 0.0F, 0.0F));

        PartDefinition TailMiddleMiddle = Tails.addOrReplaceChild("TailMiddleMiddle", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition TailMiddleMiddlePrimary = TailMiddleMiddle.addOrReplaceChild("TailMiddleMiddlePrimary", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.1F, 0.1309F, 0.0F, 0.0F));

        PartDefinition Base_r17 = TailMiddleMiddlePrimary.addOrReplaceChild("Base_r17", CubeListBuilder.create().texOffs(110, 36).addBox(-2.0F, 0.75F, -1.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.45F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1781F, 0.0F, 0.0F));

        PartDefinition TailMiddleMiddleSecondary = TailMiddleMiddlePrimary.addOrReplaceChild("TailMiddleMiddleSecondary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.25F, 5.6F));

        PartDefinition Base_r18 = TailMiddleMiddleSecondary.addOrReplaceChild("Base_r18", CubeListBuilder.create().texOffs(106, 63).addBox(-2.5F, -0.45F, -2.0F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.55F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 1.4835F, 0.0F, 0.0F));

        PartDefinition TailMiddleMiddleTertiary = TailMiddleMiddleSecondary.addOrReplaceChild("TailMiddleMiddleTertiary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.75F, 7.1F));

        PartDefinition Base_r19 = TailMiddleMiddleTertiary.addOrReplaceChild("Base_r19", CubeListBuilder.create().texOffs(94, 32).addBox(-2.0F, -0.7F, -1.95F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 1.8326F, 0.0F, 0.0F));

        PartDefinition TailMiddleMiddleQuaternary = TailMiddleMiddleTertiary.addOrReplaceChild("TailMiddleMiddleQuaternary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 1.0F));

        PartDefinition Base_r20 = TailMiddleMiddleQuaternary.addOrReplaceChild("Base_r20", CubeListBuilder.create().texOffs(78, 32).addBox(-2.0F, -1.2F, -1.95F, 4.0F, 4.0F, 4.0F, new CubeDeformation(-0.45F)), PartPose.offsetAndRotation(0.0F, -0.7F, 3.0F, 2.0071F, 0.0F, 0.0F));

        PartDefinition TailMiddleUpper = Tails.addOrReplaceChild("TailMiddleUpper", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3491F, 0.0F, 0.0F));

        PartDefinition TailMiddleUpperPrimary = TailMiddleUpper.addOrReplaceChild("TailMiddleUpperPrimary", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.1F, 0.1309F, 0.0F, 0.0F));

        PartDefinition Base_r21 = TailMiddleUpperPrimary.addOrReplaceChild("Base_r21", CubeListBuilder.create().texOffs(110, 45).addBox(-2.0F, 0.75F, -1.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.45F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1781F, 0.0F, 0.0F));

        PartDefinition TailMiddleUpperSecondary = TailMiddleUpperPrimary.addOrReplaceChild("TailMiddleUpperSecondary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.25F, 5.6F));

        PartDefinition Base_r22 = TailMiddleUpperSecondary.addOrReplaceChild("Base_r22", CubeListBuilder.create().texOffs(86, 69).addBox(-2.5F, -0.45F, -2.0F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.55F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 1.4835F, 0.0F, 0.0F));

        PartDefinition TailMiddleUpperTertiary = TailMiddleUpperSecondary.addOrReplaceChild("TailMiddleUpperTertiary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.75F, 7.1F));

        PartDefinition Base_r23 = TailMiddleUpperTertiary.addOrReplaceChild("Base_r23", CubeListBuilder.create().texOffs(94, 40).addBox(-2.0F, -0.7F, -1.95F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 1.8326F, 0.0F, 0.0F));

        PartDefinition TailMiddleUpperQuaternary = TailMiddleUpperTertiary.addOrReplaceChild("TailMiddleUpperQuaternary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 1.0F));

        PartDefinition Base_r24 = TailMiddleUpperQuaternary.addOrReplaceChild("Base_r24", CubeListBuilder.create().texOffs(78, 40).addBox(-2.0F, -1.2F, -1.95F, 4.0F, 4.0F, 4.0F, new CubeDeformation(-0.45F)), PartPose.offsetAndRotation(0.0F, -0.7F, 3.0F, 2.0071F, 0.0F, 0.0F));

        PartDefinition TailMiddleLower = Tails.addOrReplaceChild("TailMiddleLower", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3491F, 0.0F, 0.0F));

        PartDefinition TailMiddleLowerPrimary = TailMiddleLower.addOrReplaceChild("TailMiddleLowerPrimary", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.1F, 0.1309F, 0.0F, 0.0F));

        PartDefinition Base_r25 = TailMiddleLowerPrimary.addOrReplaceChild("Base_r25", CubeListBuilder.create().texOffs(110, 54).addBox(-2.0F, 0.75F, -1.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.45F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1781F, 0.0F, 0.0F));

        PartDefinition TailMiddleLowerSecondary = TailMiddleLowerPrimary.addOrReplaceChild("TailMiddleLowerSecondary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.25F, 5.6F));

        PartDefinition Base_r26 = TailMiddleLowerSecondary.addOrReplaceChild("Base_r26", CubeListBuilder.create().texOffs(86, 82).addBox(-2.5F, -0.45F, -2.0F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.55F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 1.4835F, 0.0F, 0.0F));

        PartDefinition TailMiddleLowerTertiary = TailMiddleLowerSecondary.addOrReplaceChild("TailMiddleLowerTertiary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.75F, 7.1F));

        PartDefinition Base_r27 = TailMiddleLowerTertiary.addOrReplaceChild("Base_r27", CubeListBuilder.create().texOffs(94, 48).addBox(-2.0F, -0.7F, -1.95F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 1.8326F, 0.0F, 0.0F));

        PartDefinition TailMiddleLowerQuaternary = TailMiddleLowerTertiary.addOrReplaceChild("TailMiddleLowerQuaternary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 1.0F));

        PartDefinition Base_r28 = TailMiddleLowerQuaternary.addOrReplaceChild("Base_r28", CubeListBuilder.create().texOffs(78, 48).addBox(-2.0F, -1.2F, -1.95F, 4.0F, 4.0F, 4.0F, new CubeDeformation(-0.45F)), PartPose.offsetAndRotation(0.0F, -0.7F, 3.0F, 2.0071F, 0.0F, 0.0F));

        PartDefinition Plantoids = Torso.addOrReplaceChild("Plantoids", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -2.0F));

        PartDefinition RightPlantoid_r1 = Plantoids.addOrReplaceChild("RightPlantoid_r1", CubeListBuilder.create().texOffs(44, 0).addBox(-4.25F, -1.7F, -0.8F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.1F))
                .texOffs(60, 0).addBox(0.25F, -1.7F, -0.8F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, -0.2793F, 0.0F, 0.0F));

        PartDefinition Center_r1 = Plantoids.addOrReplaceChild("Center_r1", CubeListBuilder.create().texOffs(56, 0).addBox(-0.5F, -1.3F, -0.1F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, -0.192F, 0.0F, 0.0F));

        PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(16, 35).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 1.5F, 0.0F));

        PartDefinition RightArmPawBeans = RightArm.addOrReplaceChild("RightArmPawBeans", CubeListBuilder.create().texOffs(11, 84).mirror().addBox(-2.0F, 9.475F, -0.375F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.075F)).mirror(false)
                .texOffs(11, 80).mirror().addBox(-2.8F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false)
                .texOffs(11, 78).mirror().addBox(-1.5F, 9.475F, -1.875F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false)
                .texOffs(11, 82).mirror().addBox(-0.225F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false), PartPose.offset(0.0F, -0.5F, 0.0F));

        PartDefinition RightArmFur = RightArm.addOrReplaceChild("RightArmFur", CubeListBuilder.create(), PartPose.offset(-2.0F, 7.5F, -2.4F));

        PartDefinition RightArmFur_r1 = RightArmFur.addOrReplaceChild("RightArmFur_r1", CubeListBuilder.create().texOffs(0, 91).addBox(-3.0F, -3.0F, 0.025F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(3.3F, 0.0F, 3.4F, 0.0F, -1.5708F, 0.0873F));

        PartDefinition RightArmFur_r2 = RightArmFur.addOrReplaceChild("RightArmFur_r2", CubeListBuilder.create().texOffs(0, 86).mirror().addBox(-1.0F, -3.0F, 0.025F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offsetAndRotation(-1.3F, 0.0F, 3.4F, 0.0F, 1.5708F, -0.0873F));

        PartDefinition RightArmFur_r3 = RightArmFur.addOrReplaceChild("RightArmFur_r3", CubeListBuilder.create().texOffs(0, 81).mirror().addBox(-1.0F, -3.0F, 0.025F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 3.7F, -0.0873F, 0.0F, 0.0F));

        PartDefinition RightArmFur_r4 = RightArmFur.addOrReplaceChild("RightArmFur_r4", CubeListBuilder.create().texOffs(0, 76).mirror().addBox(-1.0F, -3.0F, 0.025F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.1F, 0.0873F, 0.0F, 0.0F));

        PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(0, 35).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 1.5F, 0.0F));

        PartDefinition LeftArmPawBeans = LeftArm.addOrReplaceChild("LeftArmPawBeans", CubeListBuilder.create().texOffs(19, 84).addBox(0.0F, 9.475F, -0.375F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.075F))
                .texOffs(19, 80).addBox(1.8F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F))
                .texOffs(19, 78).addBox(0.5F, 9.475F, -1.875F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F))
                .texOffs(19, 82).addBox(-0.775F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)), PartPose.offset(0.0F, -0.5F, 0.0F));

        PartDefinition LeftArmFur = LeftArm.addOrReplaceChild("LeftArmFur", CubeListBuilder.create(), PartPose.offset(2.0F, 7.5F, -2.4F));

        PartDefinition LeftArmFur_r1 = LeftArmFur.addOrReplaceChild("LeftArmFur_r1", CubeListBuilder.create().texOffs(0, 71).mirror().addBox(-1.0F, -3.0F, 0.025F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offsetAndRotation(-3.3F, 0.0F, 3.4F, 0.0F, 1.5708F, -0.0873F));

        PartDefinition LeftArmFur_r2 = LeftArmFur.addOrReplaceChild("LeftArmFur_r2", CubeListBuilder.create().texOffs(0, 66).addBox(-3.0F, -3.0F, 0.025F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(1.3F, 0.0F, 3.4F, 0.0F, -1.5708F, 0.0873F));

        PartDefinition LeftArmFur_r3 = LeftArmFur.addOrReplaceChild("LeftArmFur_r3", CubeListBuilder.create().texOffs(0, 61).addBox(-3.0F, -3.0F, 0.025F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 3.7F, -0.0873F, 0.0F, 0.0F));

        PartDefinition LeftArmFur_r4 = LeftArmFur.addOrReplaceChild("LeftArmFur_r4", CubeListBuilder.create().texOffs(0, 56).addBox(-3.0F, -3.0F, 0.025F, 4.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.1F, 0.0873F, 0.0F, 0.0F));

        PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offset(-2.5F, 10.5F, 0.0F));

        PartDefinition RightThigh_r1 = RightLeg.addOrReplaceChild("RightThigh_r1", CubeListBuilder.create().texOffs(41, 54).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition RightLowerLeg = RightLeg.addOrReplaceChild("RightLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

        PartDefinition RightCalf_r1 = RightLowerLeg.addOrReplaceChild("RightCalf_r1", CubeListBuilder.create().texOffs(41, 65).addBox(-1.99F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

        PartDefinition RightFoot = RightLowerLeg.addOrReplaceChild("RightFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

        PartDefinition RightArch_r1 = RightFoot.addOrReplaceChild("RightArch_r1", CubeListBuilder.create().texOffs(42, 75).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

        PartDefinition RightPad = RightFoot.addOrReplaceChild("RightPad", CubeListBuilder.create().texOffs(40, 84).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

        PartDefinition RightPawBeans = RightPad.addOrReplaceChild("RightPawBeans", CubeListBuilder.create().texOffs(19, 93).mirror().addBox(-2.0F, 9.475F, -0.375F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.075F)).mirror(false)
                .texOffs(19, 89).mirror().addBox(-2.8F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false)
                .texOffs(19, 87).mirror().addBox(-1.5F, 9.475F, -1.875F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false)
                .texOffs(19, 91).mirror().addBox(-0.225F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false), PartPose.offset(1.0F, -8.5F, -0.05F));

        PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(2.5F, 10.5F, 0.0F));

        PartDefinition LeftThigh_r1 = LeftLeg.addOrReplaceChild("LeftThigh_r1", CubeListBuilder.create().texOffs(25, 56).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition LeftLowerLeg = LeftLeg.addOrReplaceChild("LeftLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

        PartDefinition LeftCalf_r1 = LeftLowerLeg.addOrReplaceChild("LeftCalf_r1", CubeListBuilder.create().texOffs(25, 67).addBox(-2.01F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

        PartDefinition LeftFoot = LeftLowerLeg.addOrReplaceChild("LeftFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

        PartDefinition LeftArch_r1 = LeftFoot.addOrReplaceChild("LeftArch_r1", CubeListBuilder.create().texOffs(26, 77).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

        PartDefinition LeftPad = LeftFoot.addOrReplaceChild("LeftPad", CubeListBuilder.create().texOffs(24, 86).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

        PartDefinition LeftPawBeans = LeftPad.addOrReplaceChild("LeftPawBeans", CubeListBuilder.create().texOffs(11, 93).addBox(0.0F, 9.475F, -0.375F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.075F))
                .texOffs(11, 89).addBox(1.8F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F))
                .texOffs(11, 87).addBox(0.5F, 9.475F, -1.875F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F))
                .texOffs(11, 91).addBox(-0.775F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)), PartPose.offset(-1.0F, -8.5F, -0.05F));

        return LayerDefinition.create(meshdefinition, 126, 126);
    }


    @Override
    public void prepareMobModel(LatexKitsuneFemaleEntity p_162861, float p_102862, float p_102863, float p_102864_) {
        this.prepareMobModel(animator, p_162861, p_102862, p_102863, p_102864_);
    }

    /* public PoseStack getPlacementCorrectors(CorrectorType type) {
         PoseStack corrector = AdvancedHumanoidModelInterface.super.getPlacementCorrectors(type);
         if (type.isArm())
             corrector.translate(-0.02f, 0.12f, 0.12f);
         return corrector;
     }
  */
    @Override
    public void setupHand(LatexKitsuneFemaleEntity entity) {
        animator.setupHand();
    }


    @Override
    public void setupAnim(@NotNull LatexKitsuneFemaleEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animator.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        CarryAbilityAnimation.playAnimation(entity, this);
        this.applyAllTailRotations();
    }

    public void applyTailRotations(ModelPart tail, float xRot, float yRot, float zRot) {
        tail.xRot += xRot * Mth.DEG_TO_RAD;
        tail.yRot += yRot * Mth.DEG_TO_RAD;
        tail.zRot += zRot * Mth.DEG_TO_RAD;
    }

    public void applyAllTailRotations() {
        this.applyTailRotations(this.TailUpperRight, -10.8885f, 16.4137f, -6.1549f);
        this.applyTailRotations(this.TailUpperLeft, -10.9827f, -17.9886f, 0.3862f);
        this.applyTailRotations(this.TailLowerRight, 10, 17.5f, 0);
        this.applyTailRotations(this.TailLowerLeft, 9.9727f, -17.0014f, -5.9783f);
        //this.applyTailRotations(TailMiddleMiddle, 0, 0, 0);
        this.applyTailRotations(this.TailMiddleLower, 20, 0, 0);
        this.applyTailRotations(this.TailMiddleUpper, -20, 0, 0);
    }

    public ModelPart getArm(HumanoidArm p_102852) {
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
    public HelperModel getTransfurHelperModel(Limb limb) {
        if (limb == Limb.TORSO)
            return TransfurHelper.getFeminineTorsoAlt();
        return super.getTransfurHelperModel(limb);
    }


    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Torso.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        RightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        LeftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        RightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        LeftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public HumanoidAnimator<LatexKitsuneFemaleEntity, LatexKitsuneFemaleModel> getAnimator(LatexKitsuneFemaleEntity entity) {
        return animator;
    }
}