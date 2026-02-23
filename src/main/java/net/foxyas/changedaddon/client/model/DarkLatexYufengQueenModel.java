package net.foxyas.changedaddon.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.model.animations.ChangedAddonAnimationsPresets;
import net.foxyas.changedaddon.entity.simple.DarkLatexYufengQueenEntity;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DarkLatexYufengQueenModel extends AdvancedHumanoidModel<DarkLatexYufengQueenEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = ChangedAddonMod.layerLocation("dark_latex_yufeng_queen", "main");
    private final ModelPart RightLeg;
    private final ModelPart LeftLeg;
    private final ModelPart Head;
    private final ModelPart Torso;
    private final ModelPart LeftWing;
    private final ModelPart RightWing;
    private final ModelPart RightArm;
    private final ModelPart LeftArm;
    private final HumanoidAnimator<DarkLatexYufengQueenEntity, DarkLatexYufengQueenModel> animator;

    public DarkLatexYufengQueenModel(ModelPart root) {
        super(root);
        this.RightLeg = root.getChild("RightLeg");
        this.LeftLeg = root.getChild("LeftLeg");
        this.Head = root.getChild("Head");
        this.Torso = root.getChild("Torso");
        this.LeftWing = this.Torso.getChild("LeftWing");
        this.RightWing = this.Torso.getChild("RightWing");
        this.RightArm = root.getChild("RightArm");
        this.LeftArm = root.getChild("LeftArm");

        ModelPart leftLowerLeg = this.LeftLeg.getChild("LeftLowerLeg");
        ModelPart leftFoot = leftLowerLeg.getChild("LeftFoot");
        ModelPart leftPad = leftFoot.getChild("LeftPad");

        ModelPart rightLowerLeg = this.RightLeg.getChild("RightLowerLeg");
        ModelPart rightFoot = rightLowerLeg.getChild("RightFoot");
        ModelPart rightPad = rightFoot.getChild("RightPad");

        ModelPart leftWingRoot = this.LeftWing.getChild("leftWingRoot");
        ModelPart leftSecondaries = leftWingRoot.getChild("leftSecondaries");
        ModelPart leftTertiaries = leftSecondaries.getChild("leftTertiaries");

        ModelPart rightWingRoot = this.RightWing.getChild("rightWingRoot");
        ModelPart rightSecondaries = rightWingRoot.getChild("rightSecondaries");
        ModelPart rightTertiaries = rightSecondaries.getChild("rightTertiaries");

        ModelPart tail = this.Torso.getChild("Tail");
        ModelPart tailSegment1 = tail.getChild("TailSegment1");
        ModelPart tailSegment2 = tailSegment1.getChild("TailSegment2");
        ModelPart tailSegment3 = tailSegment2.getChild("TailSegment3");
        ModelPart tailSegment4 = tailSegment3.getChild("TailSegment4");
        ModelPart tailSegment5 = tailSegment4.getChild("TailSegment5");
        ModelPart tailSegment6 = tailSegment5.getChild("TailSegment6");
        this.animator = HumanoidAnimator.of(this).hipOffset(-1.5F).addPreset(ChangedAddonAnimationsPresets.bigWingedDragonLike(
                this.Head,
                this.Torso,
                this.LeftArm,
                this.RightArm,
                tail,
                List.of(tailSegment1, tailSegment2, tailSegment3, tailSegment4, tailSegment5, tailSegment6),
                this.LeftLeg,
                leftLowerLeg,
                leftFoot,
                leftPad,
                this.RightLeg,
                rightLowerLeg,
                rightFoot,
                rightPad,
                leftWingRoot, leftSecondaries, leftTertiaries,
                rightWingRoot, rightSecondaries, rightTertiaries
        ));
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offset(-2.4F, 10.5F, 0.0F));

        PartDefinition RightThigh_r1 = RightLeg.addOrReplaceChild("RightThigh_r1", CubeListBuilder.create().texOffs(52, 22).addBox(-1.9F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(-0.1F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition RightLowerLeg = RightLeg.addOrReplaceChild("RightLowerLeg", CubeListBuilder.create(), PartPose.offset(-0.1F, 6.375F, -3.45F));

        PartDefinition RightCalf_r1 = RightLowerLeg.addOrReplaceChild("RightCalf_r1", CubeListBuilder.create().texOffs(52, 43).addBox(-1.9F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

        PartDefinition RightFoot = RightLowerLeg.addOrReplaceChild("RightFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

        PartDefinition RightArch_r1 = RightFoot.addOrReplaceChild("RightArch_r1", CubeListBuilder.create().texOffs(45, 60).addBox(-1.9F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

        PartDefinition RightPad = RightFoot.addOrReplaceChild("RightPad", CubeListBuilder.create().texOffs(11, 57).addBox(-1.9F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

        PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(2.4F, 10.5F, 0.0F));

        PartDefinition LeftThigh_r1 = LeftLeg.addOrReplaceChild("LeftThigh_r1", CubeListBuilder.create().texOffs(0, 47).addBox(-2.1F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.1F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition LeftLowerLeg = LeftLeg.addOrReplaceChild("LeftLowerLeg", CubeListBuilder.create(), PartPose.offset(0.1F, 6.375F, -3.45F));

        PartDefinition LeftCalf_r1 = LeftLowerLeg.addOrReplaceChild("LeftCalf_r1", CubeListBuilder.create().texOffs(52, 33).addBox(-2.1F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

        PartDefinition LeftFoot = LeftLowerLeg.addOrReplaceChild("LeftFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

        PartDefinition LeftArch_r1 = LeftFoot.addOrReplaceChild("LeftArch_r1", CubeListBuilder.create().texOffs(59, 60).addBox(-2.1F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

        PartDefinition LeftPad = LeftFoot.addOrReplaceChild("LeftPad", CubeListBuilder.create().texOffs(47, 53).addBox(-2.1F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

        PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(24, 10).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(15, 35).addBox(-1.5F, -1.0F, -6.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Base_r1 = Head.addOrReplaceChild("Base_r1", CubeListBuilder.create().texOffs(74, 70).addBox(-1.4F, -0.55F, 2.3F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.9F, -5.5F, 0.0F, -1.3673F, 0.7176F, 0.625F));

        PartDefinition Base_r2 = Head.addOrReplaceChild("Base_r2", CubeListBuilder.create().texOffs(85, 70).addBox(-0.1F, -0.85F, -0.3F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.9F, -5.5F, 0.0F, -1.1565F, -0.5372F, -0.7297F));

        PartDefinition Base_r3 = Head.addOrReplaceChild("Base_r3", CubeListBuilder.create().texOffs(85, 78).addBox(-0.6F, -0.55F, 2.3F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.9F, -5.5F, 0.0F, -1.3673F, -0.7176F, -0.625F));

        PartDefinition Base_r4 = Head.addOrReplaceChild("Base_r4", CubeListBuilder.create().texOffs(74, 78).addBox(-1.9F, -0.85F, -0.3F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.9F, -5.5F, 0.0F, -1.1565F, 0.5372F, 0.7297F));

        PartDefinition Crown = Head.addOrReplaceChild("Crown", CubeListBuilder.create(), PartPose.offset(0.0F, -0.1F, 0.0F));

        PartDefinition Base_r5 = Crown.addOrReplaceChild("Base_r5", CubeListBuilder.create().texOffs(12, 87).mirror().addBox(-3.45F, -2.1F, -2.55F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offsetAndRotation(0.0F, -7.3F, -2.4F, -0.5039F, 0.7915F, -0.5105F));

        PartDefinition Base_r6 = Crown.addOrReplaceChild("Base_r6", CubeListBuilder.create().texOffs(8, 86).mirror().addBox(-0.3F, -3.8F, -0.875F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(6, 90).mirror().addBox(-0.8F, -1.9F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-2.2F, -7.3F, -2.0F, -0.4184F, 0.26F, -0.2922F));

        PartDefinition Base_r7 = Crown.addOrReplaceChild("Base_r7", CubeListBuilder.create().texOffs(12, 93).mirror().addBox(-2.4F, -1.6F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.15F)).mirror(false), PartPose.offsetAndRotation(0.0F, -7.3F, -2.4F, -0.354F, 0.1639F, -0.0602F));

        PartDefinition Base_r8 = Crown.addOrReplaceChild("Base_r8", CubeListBuilder.create().texOffs(0, 86).addBox(-0.7F, -3.8F, -0.875F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 90).addBox(-1.2F, -1.9F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(2.2F, -7.3F, -2.0F, -0.4184F, -0.26F, 0.2922F));

        PartDefinition Base_r9 = Crown.addOrReplaceChild("Base_r9", CubeListBuilder.create().texOffs(0, 83).addBox(-0.5F, -4.5F, -1.375F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(4, 85).addBox(-0.5F, -4.8F, -0.875F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.1F))
                .texOffs(0, 93).addBox(-1.0F, -2.2F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.3F, -2.4F, -0.3491F, 0.0F, 0.0F));

        PartDefinition Base_r10 = Crown.addOrReplaceChild("Base_r10", CubeListBuilder.create().texOffs(12, 90).addBox(1.45F, -2.1F, -2.55F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, -7.3F, -2.4F, -0.5039F, -0.7915F, 0.5105F));

        PartDefinition Base_r11 = Crown.addOrReplaceChild("Base_r11", CubeListBuilder.create().texOffs(6, 93).addBox(0.4F, -1.6F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.0F, -7.3F, -2.4F, -0.354F, -0.1639F, 0.0602F));

        PartDefinition Hair = Head.addOrReplaceChild("Hair", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -33.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.2F))
                .texOffs(0, 18).addBox(-4.0F, -33.0F, -4.0F, 8.0F, 9.0F, 8.0F, new CubeDeformation(0.35F)), PartPose.offset(0.0F, 25.0F, 0.0F));

        PartDefinition Mask = Head.addOrReplaceChild("Mask", CubeListBuilder.create().texOffs(46, 28).addBox(-1.0F, -4.0F, -5.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(48, 16).addBox(-3.0F, -5.0F, -5.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(32, 28).addBox(-3.0F, -2.0F, -5.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(32, 26).addBox(-4.0F, -1.0F, -5.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(15, 38).addBox(-2.0F, -6.0F, -5.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(48, 33).addBox(-1.0F, -7.0F, -5.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 4).addBox(3.0F, -4.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 4).addBox(-4.0F, -4.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 0).addBox(-2.0F, -2.0F, -7.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

        PartDefinition LeftEar = Head.addOrReplaceChild("LeftEar", CubeListBuilder.create(), PartPose.offsetAndRotation(2.5F, -8.5F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition Base_r12 = LeftEar.addOrReplaceChild("Base_r12", CubeListBuilder.create().texOffs(0, 0).addBox(-0.3832F, -1.4377F, -3.293F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.0F, 0.0F, -0.2094F, 0.056F, -0.0059F));

        PartDefinition Base_r13 = LeftEar.addOrReplaceChild("Base_r13", CubeListBuilder.create().texOffs(48, 84).addBox(-0.1112F, -6.1063F, -0.668F, 2.0F, 8.0F, 3.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.8736F, 0.2332F, -0.0983F));

        PartDefinition RightEar = Head.addOrReplaceChild("RightEar", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.5F, -8.5F, 0.0F, 0.0F, 0.0F, -0.2618F));

        PartDefinition Base_r14 = RightEar.addOrReplaceChild("Base_r14", CubeListBuilder.create().texOffs(4, 0).addBox(-0.6168F, -1.4377F, -3.293F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, 0.0F, -0.2094F, -0.056F, 0.0059F));

        PartDefinition Base_r15 = RightEar.addOrReplaceChild("Base_r15", CubeListBuilder.create().texOffs(61, 84).addBox(-1.8888F, -6.1063F, -0.668F, 2.0F, 8.0F, 3.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.8736F, -0.2332F, 0.0983F));

        PartDefinition TopHorn = Head.addOrReplaceChild("TopHorn", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -5.6F, 3.7F, 0.829F, 0.0F, 0.0F));

        PartDefinition TopHorn_r1 = TopHorn.addOrReplaceChild("TopHorn_r1", CubeListBuilder.create().texOffs(82, 60).addBox(-1.0F, -1.0F, -2.2F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.15F)), PartPose.offsetAndRotation(0.0F, -4.5F, 0.5F, 0.0F, 0.0F, 0.7854F));

        PartDefinition Torso = partdefinition.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(28, 31).addBox(-4.0F, 4.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(-0.3F))
                .texOffs(48, 9).addBox(-4.0F, 8.6F, -2.0F, 8.0F, 3.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Plantoids = Torso.addOrReplaceChild("Plantoids", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -2.0F));

        PartDefinition RightPlantoid_r1 = Plantoids.addOrReplaceChild("RightPlantoid_r1", CubeListBuilder.create().texOffs(0, 41).addBox(-4.25F, -1.7F, -0.8F, 4.0F, 4.0F, 2.0F, new CubeDeformation(-0.03F))
                .texOffs(0, 35).addBox(0.25F, -1.7F, -0.8F, 4.0F, 4.0F, 2.0F, new CubeDeformation(-0.03F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, -0.2793F, 0.0F, 0.0F));

        PartDefinition Center_r1 = Plantoids.addOrReplaceChild("Center_r1", CubeListBuilder.create().texOffs(0, 4).addBox(-0.5F, -1.3F, -0.1F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, -0.192F, 0.0F, 0.0F));

        PartDefinition Tail = Torso.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offset(0.0F, 10.2F, 0.0F));

        PartDefinition TailSegment1 = Tail.addOrReplaceChild("TailSegment1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = TailSegment1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(26, 93).mirror().addBox(-8.2F, -8.775F, -11.1F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(0.0F, 14.3F, 0.0F, -1.0899F, 0.2775F, 0.6074F));

        PartDefinition Base_r16 = TailSegment1.addOrReplaceChild("Base_r16", CubeListBuilder.create().texOffs(0, 73).addBox(-2.0F, -3.1F, -0.7F, 4.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.6F, 0.0F, -0.7418F, 0.0F, 0.0F));

        PartDefinition cube_r2 = TailSegment1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(30, 93).addBox(7.2F, -8.775F, -11.1F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 14.3F, 0.0F, -1.0899F, -0.2775F, -0.6074F));

        PartDefinition TailSegment2 = TailSegment1.addOrReplaceChild("TailSegment2", CubeListBuilder.create(), PartPose.offset(0.0F, 2.1F, 3.3F));

        PartDefinition cube_r3 = TailSegment2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(26, 90).mirror().addBox(-6.5F, -11.275F, -7.8F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 12.2F, -3.3F, -1.3045F, 0.1207F, 0.5963F));

        PartDefinition Base_r17 = TailSegment2.addOrReplaceChild("Base_r17", CubeListBuilder.create().texOffs(0, 64).addBox(-1.5F, -0.4F, -2.0F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, 0.5F, 2.7F, -0.5672F, 0.0F, 0.0F));

        PartDefinition cube_r4 = TailSegment2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(30, 90).addBox(5.5F, -11.275F, -7.8F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.2F, -3.3F, -1.3045F, -0.1207F, -0.5963F));

        PartDefinition TailSegment3 = TailSegment2.addOrReplaceChild("TailSegment3", CubeListBuilder.create(), PartPose.offset(0.0F, 3.1F, 5.0F));

        PartDefinition cube_r5 = TailSegment3.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(26, 86).mirror().addBox(-6.5F, -15.675F, -6.4F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.15F)).mirror(false), PartPose.offsetAndRotation(0.0F, 9.1F, -8.3F, -1.4689F, 0.1123F, 0.6838F));

        PartDefinition Base_r18 = TailSegment3.addOrReplaceChild("Base_r18", CubeListBuilder.create().texOffs(18, 65).addBox(-1.5F, -12.725F, 4.6F, 3.0F, 3.0F, 7.0F, new CubeDeformation(-0.04F)), PartPose.offsetAndRotation(0.0F, 9.4F, -8.3F, -0.3054F, 0.0F, 0.0F));

        PartDefinition cube_r6 = TailSegment3.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(30, 86).addBox(5.5F, -15.675F, -6.4F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.0F, 9.1F, -8.3F, -1.4689F, -0.1123F, -0.6838F));

        PartDefinition TailSegment4 = TailSegment3.addOrReplaceChild("TailSegment4", CubeListBuilder.create(), PartPose.offset(0.0F, 2.0F, 5.6F));

        PartDefinition Base_r19 = TailSegment4.addOrReplaceChild("Base_r19", CubeListBuilder.create().texOffs(33, 70).addBox(-1.0F, -10.15F, 11.7F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.16F)), PartPose.offsetAndRotation(0.0F, 7.6F, -12.9F, -0.1309F, 0.0F, 0.0F));

        PartDefinition TailSegment5 = TailSegment4.addOrReplaceChild("TailSegment5", CubeListBuilder.create(), PartPose.offset(0.0F, 0.6F, 4.5F));

        PartDefinition Base_r20 = TailSegment5.addOrReplaceChild("Base_r20", CubeListBuilder.create().texOffs(18, 75).addBox(-1.0F, -8.35F, 12.6F, 2.0F, 2.0F, 5.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.0F, 6.8F, -13.1F, -0.0436F, 0.0F, 0.0F));

        PartDefinition TailSegment6 = TailSegment5.addOrReplaceChild("TailSegment6", CubeListBuilder.create(), PartPose.offset(0.0F, 0.2F, 4.0F));

        PartDefinition cube_r7 = TailSegment6.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(34, 93).mirror().addBox(-4.75F, -27.1F, -10.9F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offsetAndRotation(0.0F, 12.0F, -19.9F, -1.6223F, 0.4901F, -0.7371F));

        PartDefinition Base_r21 = TailSegment6.addOrReplaceChild("Base_r21", CubeListBuilder.create().texOffs(27, 77).addBox(-0.5F, -5.6F, 17.7F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.6F, -17.1F, 0.0873F, 0.0F, 0.0F));

        PartDefinition cube_r8 = TailSegment6.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(38, 93).addBox(3.75F, -27.1F, -10.9F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, 12.0F, -19.9F, -1.6223F, -0.4901F, 0.7371F));

        PartDefinition LeftWing = Torso.addOrReplaceChild("LeftWing", CubeListBuilder.create(), PartPose.offsetAndRotation(2.0F, 4.5F, 2.0F, 0.0F, -0.48F, 0.0F));

        PartDefinition leftWingRoot = LeftWing.addOrReplaceChild("leftWingRoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r9 = leftWingRoot.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(82, 35).addBox(18.975F, -4.475F, 1.65F, 7.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 20.0F, -2.0F, 0.0F, 0.0F, -1.2654F));

        PartDefinition cube_r10 = leftWingRoot.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(56, 3).addBox(19.075F, -12.7F, 1.2F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 20.0F, -2.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition cube_r11 = leftWingRoot.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(52, 0).addBox(7.775F, -19.75F, 1.2F, 5.0F, 2.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-2.0F, 20.0F, -2.0F, 0.0F, 0.0F, -0.3491F));

        PartDefinition leftSecondaries = leftWingRoot.addOrReplaceChild("leftSecondaries", CubeListBuilder.create().texOffs(18, 93).addBox(-0.8F, -1.775F, -0.3F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(72, 6).addBox(-0.8F, -0.475F, -0.3F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(7.3F, -7.0F, -0.5F, 0.0F, 0.0F, -0.5236F));

        PartDefinition cube_r12 = leftSecondaries.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(68, 16).addBox(-2.025F, -22.55F, 1.2F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.3F, 27.0F, -1.5F, 0.0F, 0.0F, 0.48F));

        PartDefinition cube_r13 = leftSecondaries.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(78, 40).addBox(15.525F, -13.85F, 1.648F, 9.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.3F, 27.0F, -1.5F, 0.0F, 0.0F, -0.7418F));

        PartDefinition cube_r14 = leftSecondaries.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(78, 46).addBox(13.4F, 10.625F, 1.651F, 9.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.3F, 27.0F, -1.5F, 0.0F, 0.0F, -1.8326F));

        PartDefinition cube_r15 = leftSecondaries.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(18, 90).addBox(-2.025F, 15.25F, -2.2F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(-9.3F, 27.0F, -1.5F, 3.1416F, 0.0F, 0.48F));

        PartDefinition leftTertiaries = leftSecondaries.addOrReplaceChild("leftTertiaries", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.3F, 0.0F, 0.0F, 0.0F, 0.0F, -0.9599F));

        PartDefinition cube_r16 = leftTertiaries.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(18, 87).addBox(-3.3F, 14.2F, -2.2F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(-9.0F, 27.0F, -1.5F, 3.1416F, 0.0F, 0.5236F));

        PartDefinition cube_r17 = leftTertiaries.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(64, 16).addBox(-3.3F, -22.5F, 1.2F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, 27.0F, -1.5F, 0.0F, 0.0F, 0.5236F));

        PartDefinition cube_r18 = leftTertiaries.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(78, 51).addBox(16.125F, -10.525F, 1.64F, 9.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, 27.0F, -1.5F, 0.0F, 0.0F, -0.8727F));

        PartDefinition cube_r19 = leftTertiaries.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(76, 6).addBox(9.15F, -26.2F, 1.2F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(-9.0F, 27.0F, -1.5F, 0.0F, 0.0F, -0.0436F));

        PartDefinition RightWing = Torso.addOrReplaceChild("RightWing", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, 4.5F, 2.0F, 0.0F, 0.48F, 0.0F));

        PartDefinition rightWingRoot = RightWing.addOrReplaceChild("rightWingRoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r20 = rightWingRoot.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(82, 14).addBox(-25.975F, -4.475F, 1.65F, 7.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 20.0F, -2.0F, 0.0F, 0.0F, 1.2654F));

        PartDefinition cube_r21 = rightWingRoot.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(70, 3).addBox(-25.075F, -12.7F, 1.2F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 20.0F, -2.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition cube_r22 = rightWingRoot.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(64, 0).addBox(-12.775F, -19.75F, 1.2F, 5.0F, 2.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(2.0F, 20.0F, -2.0F, 0.0F, 0.0F, 0.3491F));

        PartDefinition rightSecondaries = rightWingRoot.addOrReplaceChild("rightSecondaries", CubeListBuilder.create().texOffs(92, 6).addBox(-0.2F, -0.475F, -0.3F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.01F))
                .texOffs(22, 93).mirror().addBox(-0.2F, -1.775F, -0.3F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offsetAndRotation(-7.3F, -7.0F, -0.5F, 0.0F, 0.0F, 0.5236F));

        PartDefinition cube_r23 = rightSecondaries.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(22, 90).mirror().addBox(1.025F, 15.25F, -2.2F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offsetAndRotation(9.3F, 27.0F, -1.5F, 3.1416F, 0.0F, -0.48F));

        PartDefinition cube_r24 = rightSecondaries.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(88, 6).addBox(1.025F, -22.55F, 1.2F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.3F, 27.0F, -1.5F, 0.0F, 0.0F, -0.48F));

        PartDefinition cube_r25 = rightSecondaries.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(78, 25).addBox(-22.4F, 10.625F, 1.651F, 9.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.3F, 27.0F, -1.5F, 0.0F, 0.0F, 1.8326F));

        PartDefinition cube_r26 = rightSecondaries.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(78, 19).addBox(-24.525F, -13.85F, 1.648F, 9.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.3F, 27.0F, -1.5F, 0.0F, 0.0F, 0.7418F));

        PartDefinition rightTertiaries = rightSecondaries.addOrReplaceChild("rightTertiaries", CubeListBuilder.create(), PartPose.offsetAndRotation(0.3F, 0.0F, 0.0F, 0.0F, 0.0F, 0.9599F));

        PartDefinition cube_r27 = rightTertiaries.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(22, 87).mirror().addBox(2.3F, 14.2F, -2.2F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offsetAndRotation(9.0F, 27.0F, -1.5F, 3.1416F, 0.0F, -0.5236F));

        PartDefinition cube_r28 = rightTertiaries.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(80, 6).addBox(2.3F, -22.5F, 1.2F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, 27.0F, -1.5F, 0.0F, 0.0F, -0.5236F));

        PartDefinition cube_r29 = rightTertiaries.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(84, 6).addBox(-10.15F, -26.2F, 1.2F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(9.0F, 27.0F, -1.5F, 0.0F, 0.0F, 0.0436F));

        PartDefinition cube_r30 = rightTertiaries.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(78, 30).addBox(-25.125F, -10.525F, 1.64F, 9.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, 27.0F, -1.5F, 0.0F, 0.0F, 0.8727F));

        PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(20, 41).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(36, 41).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 2.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 96, 96);
    }

    @Override
    public void setupAnim(@NotNull DarkLatexYufengQueenEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.animator.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    @Override
    public void prepareMobModel(@NotNull DarkLatexYufengQueenEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick) {
        super.prepareMobModel(pEntity, pLimbSwing, pLimbSwingAmount, pPartialTick);
    }

    @Override
    public @NotNull ModelPart getArm(HumanoidArm humanoidArm) {
        return humanoidArm == HumanoidArm.RIGHT ? RightArm : LeftArm;
    }

    @Override
    public ModelPart getLeg(HumanoidArm humanoidArm) {
        return humanoidArm == HumanoidArm.RIGHT ? RightLeg : LeftLeg;
    }

    @Override
    public @NotNull ModelPart getHead() {
        return Head;
    }

    @Override
    public ModelPart getTorso() {
        return Torso;
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        RightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        LeftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);

        setWingsVisibility(false);
        Torso.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        setWingsVisibility(true);

        renderBigWings(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);

        RightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        LeftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    private void renderBigWings(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        poseStack.scale(1.5f, 1.5f, 1.5f);
        poseStack.translate(0f, 0.05f, 0f); // wings are a little too high up normally

        poseStack.pushPose();
        LeftWing.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();

        poseStack.pushPose();
        RightWing.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();

        poseStack.popPose();
    }

    private void setWingsVisibility(boolean wingsVisibility) {
        LeftWing.visible = wingsVisibility;
        RightWing.visible = wingsVisibility;
    }

    @Override
    public void setupHand(DarkLatexYufengQueenEntity changedEntity) {
        animator.setupHand();
    }

    @Override
    public HumanoidAnimator<DarkLatexYufengQueenEntity, DarkLatexYufengQueenModel> getAnimator(DarkLatexYufengQueenEntity changedEntity) {
        return animator;
    }
}
