package net.foxyas.changedaddon.client.model;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.entity.LatexSnepEntity;
import net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModelInterface;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LatexSnepModel extends AdvancedHumanoidModel<LatexSnepEntity> implements AdvancedHumanoidModelInterface<LatexSnepEntity, LatexSnepModel> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("changed_addon", "latex_snep"), "main");
    // Grupo principal: Animal e corpo
    private final ModelPart Animal, Head, Torso;

    // Subpartes da cauda
    private final ModelPart Tail,
            TailPrimary,
            TailSecondary,
            TailTertiary,
            TailQuaternary,
            TailQuinternary;

    // Pernas: Frente direita
    private final ModelPart LegFrontRight;

    // Pernas: Traseira direita
    private final ModelPart LegBackRight;

    // Pernas: Frente esquerda
    private final ModelPart LegFrontLeft;

    // Pernas: Traseira esquerda
    private final ModelPart LegBackLeft;

    private HumanoidAnimator<LatexSnepEntity, LatexSnepModel> animator;

    public LatexSnepModel(ModelPart root) {
        super(root);
        this.Animal = root.getChild("Animal");

        this.Head = this.Animal.getChild("Head");

        this.Torso = this.Animal.getChild("Torso");

        this.Tail = this.Torso.getChild("Tail");
        this.TailPrimary = this.Tail.getChild("TailPrimary");
        this.TailSecondary = this.TailPrimary.getChild("TailSecondary");
        this.TailTertiary = this.TailSecondary.getChild("TailTertiary");
        this.TailQuaternary = this.TailTertiary.getChild("TailQuaternary");
        this.TailQuinternary = this.TailQuaternary.getChild("TailQuinternary");

        this.animator = HumanoidAnimator.of(this)
                .addPreset(AnimatorPresets.wolfTail(Tail, List.of(TailPrimary, TailSecondary, TailTertiary, TailQuaternary, TailQuinternary)));


        this.LegFrontRight = this.Animal.getChild("LegFrontRight");
        ModelPart rightLowerLeg = this.LegFrontRight.getChild("RightLowerLeg");
        ModelPart rightFoot = rightLowerLeg.getChild("RightFoot");
        ModelPart rightPad = rightFoot.getChild("RightPad");

        this.LegBackRight = this.Animal.getChild("LegBackRight");
        ModelPart rightLowerLeg2 = this.LegBackRight.getChild("RightLowerLeg2");
        ModelPart rightFoot2 = rightLowerLeg2.getChild("RightFoot2");
        ModelPart rightPad2 = rightFoot2.getChild("RightPad2");

        this.LegFrontLeft = this.Animal.getChild("LegFrontLeft");
        ModelPart leftLowerLeg = this.LegFrontLeft.getChild("LeftLowerLeg");
        ModelPart leftFoot = leftLowerLeg.getChild("LeftFoot");
        ModelPart leftPad = leftFoot.getChild("LeftPad");

        this.LegBackLeft = this.Animal.getChild("LegBackLeft");
        ModelPart leftLowerLeg2 = this.LegBackLeft.getChild("LeftLowerLeg2");
        ModelPart leftFoot2 = leftLowerLeg2.getChild("LeftFoot2");
        ModelPart leftPad2 = leftFoot2.getChild("LeftPad2");


    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        CreateSnepLayer(partdefinition);

        return LayerDefinition.create(meshdefinition, 96, 96);
    }

    private static void CreateSnepLayer(PartDefinition partdefinition) {
        PartDefinition Animal = partdefinition.addOrReplaceChild("Animal", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.5F));

        PartDefinition Head = Animal.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -2.0F, -3.0F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.2F))
                .texOffs(0, 24).addBox(-1.5F, -0.02F, -4.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.2F))
                .texOffs(0, 10).addBox(-2.0F, -3.0F, 0.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.2F))
                .texOffs(6, 10).addBox(1.0F, -3.0F, 0.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, -9.0F, -9.0F));

        PartDefinition Torso = Animal.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(48, 1).addBox(-3.5F, -6.75F, -4.5F, 7.0F, 5.0F, 6.0F, new CubeDeformation(-0.2F))
                .texOffs(50, 12).addBox(-3.0F, -2.0F, -4.0F, 6.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.0F, -0.5F, 1.5708F, 0.0F, 0.0F));

        PartDefinition Tail = Torso.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 7.25F, -1.25F, -1.3963F, 0.0F, 0.0F));

        PartDefinition TailPrimary = Tail.addOrReplaceChild("TailPrimary", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -0.0173F, -1.0306F, 0.1309F, 0.0F, 0.0F));

        PartDefinition Base_r1 = TailPrimary.addOrReplaceChild("Base_r1", CubeListBuilder.create().texOffs(0, 57).addBox(-2.0F, 0.75F, -1.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.0908F, 0.0F, 0.0F));

        PartDefinition TailSecondary = TailPrimary.addOrReplaceChild("TailSecondary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.25F, 4.5F));

        PartDefinition Base_r2 = TailSecondary.addOrReplaceChild("Base_r2", CubeListBuilder.create().texOffs(0, 46).addBox(-2.5F, -0.45F, -2.5F, 5.0F, 6.0F, 5.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 1.3526F, 0.0F, 0.0F));

        PartDefinition TailTertiary = TailSecondary.addOrReplaceChild("TailTertiary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.75F, 4.5F));

        PartDefinition Base_r3 = TailTertiary.addOrReplaceChild("Base_r3", CubeListBuilder.create().texOffs(27, 32).addBox(-2.5F, 4.55F, -3.3F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.25F, -4.5F, 1.5272F, 0.0F, 0.0F));

        PartDefinition TailQuaternary = TailTertiary.addOrReplaceChild("TailQuaternary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 8.0F));

        PartDefinition Base_r4 = TailQuaternary.addOrReplaceChild("Base_r4", CubeListBuilder.create().texOffs(73, 53).addBox(-2.5F, 4.55F, -3.3F, 5.0F, 8.0F, 5.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(0.0F, -0.75F, -4.75F, 1.5708F, 0.0F, 0.0F));

        PartDefinition TailQuinternary = TailQuaternary.addOrReplaceChild("TailQuinternary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 7.5F));

        PartDefinition Base_r5 = TailQuinternary.addOrReplaceChild("Base_r5", CubeListBuilder.create().texOffs(60, 63).addBox(-2.0F, 5.5F, -3.8F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.15F)), PartPose.offsetAndRotation(0.0F, -1.0F, -5.5F, 1.7017F, 0.0F, 0.0F));

        PartDefinition LegFrontRight = Animal.addOrReplaceChild("LegFrontRight", CubeListBuilder.create(), PartPose.offset(-0.75F, -6.0F, -4.5F));

        PartDefinition RightThigh_r1 = LegFrontRight.addOrReplaceChild("RightThigh_r1", CubeListBuilder.create().texOffs(0, 34).addBox(0.0F, 2.075F, -2.1F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -2.5F, 1.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition RightLowerLeg = LegFrontRight.addOrReplaceChild("RightLowerLeg", CubeListBuilder.create(), PartPose.offset(-0.5F, 2.875F, -2.45F));

        PartDefinition RightCalf_r1 = RightLowerLeg.addOrReplaceChild("RightCalf_r1", CubeListBuilder.create().texOffs(0, 28).addBox(-0.99F, -0.625F, -2.15F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

        PartDefinition RightFoot = RightLowerLeg.addOrReplaceChild("RightFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.3F, 4.675F));

        PartDefinition RightArch_r1 = RightFoot.addOrReplaceChild("RightArch_r1", CubeListBuilder.create().texOffs(8, 28).addBox(0.0F, -5.45F, -0.725F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(-1.0F, 5.575F, -3.225F, -0.3491F, 0.0F, 0.0F));

        PartDefinition RightPad = RightFoot.addOrReplaceChild("RightPad", CubeListBuilder.create().texOffs(7, 34).addBox(-1.0F, 1.0F, -0.85F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.825F, -2.675F));

        PartDefinition LegBackRight = Animal.addOrReplaceChild("LegBackRight", CubeListBuilder.create(), PartPose.offset(-0.75F, -6.0F, 6.5F));

        PartDefinition RightThigh_r2 = LegBackRight.addOrReplaceChild("RightThigh_r2", CubeListBuilder.create().texOffs(0, 34).addBox(0.0F, 2.075F, -2.1F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -2.5F, 1.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition RightLowerLeg2 = LegBackRight.addOrReplaceChild("RightLowerLeg2", CubeListBuilder.create(), PartPose.offset(-0.5F, 2.875F, -2.45F));

        PartDefinition RightCalf_r2 = RightLowerLeg2.addOrReplaceChild("RightCalf_r2", CubeListBuilder.create().texOffs(0, 28).addBox(-0.99F, -0.625F, -2.15F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

        PartDefinition RightFoot2 = RightLowerLeg2.addOrReplaceChild("RightFoot2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.3F, 4.675F));

        PartDefinition RightArch_r2 = RightFoot2.addOrReplaceChild("RightArch_r2", CubeListBuilder.create().texOffs(8, 28).addBox(0.0F, -5.45F, -0.725F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(-1.0F, 5.575F, -3.225F, -0.3491F, 0.0F, 0.0F));

        PartDefinition RightPad2 = RightFoot2.addOrReplaceChild("RightPad2", CubeListBuilder.create().texOffs(7, 34).addBox(-1.0F, 1.0F, -0.85F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.825F, -2.675F));

        PartDefinition LegFrontLeft = Animal.addOrReplaceChild("LegFrontLeft", CubeListBuilder.create(), PartPose.offset(0.75F, -5.75F, -4.75F));

        PartDefinition LeftThigh_r1 = LegFrontLeft.addOrReplaceChild("LeftThigh_r1", CubeListBuilder.create().texOffs(0, 34).mirror().addBox(-2.0F, 2.075F, -2.1F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.5F, -2.75F, 1.25F, -0.2182F, 0.0F, 0.0F));

        PartDefinition LeftLowerLeg = LegFrontLeft.addOrReplaceChild("LeftLowerLeg", CubeListBuilder.create(), PartPose.offset(0.5F, 2.625F, -2.2F));

        PartDefinition LeftCalf_r1 = LeftLowerLeg.addOrReplaceChild("LeftCalf_r1", CubeListBuilder.create().texOffs(0, 28).mirror().addBox(-1.01F, -0.625F, -2.15F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -1.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

        PartDefinition LeftFoot = LeftLowerLeg.addOrReplaceChild("LeftFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.3F, 4.675F));

        PartDefinition LeftArch_r1 = LeftFoot.addOrReplaceChild("LeftArch_r1", CubeListBuilder.create().texOffs(8, 28).mirror().addBox(-2.0F, -5.45F, -0.725F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.005F)).mirror(false), PartPose.offsetAndRotation(1.0F, 5.575F, -3.225F, -0.3491F, 0.0F, 0.0F));

        PartDefinition LeftPad = LeftFoot.addOrReplaceChild("LeftPad", CubeListBuilder.create().texOffs(7, 34).mirror().addBox(-1.0F, 1.0F, -0.85F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 2.825F, -2.675F));

        PartDefinition LegBackLeft = Animal.addOrReplaceChild("LegBackLeft", CubeListBuilder.create(), PartPose.offset(0.75F, -6.0F, 6.5F));

        PartDefinition LeftThigh_r2 = LegBackLeft.addOrReplaceChild("LeftThigh_r2", CubeListBuilder.create().texOffs(0, 34).mirror().addBox(-2.0F, 2.075F, -2.1F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.5F, -2.5F, 1.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition LeftLowerLeg2 = LegBackLeft.addOrReplaceChild("LeftLowerLeg2", CubeListBuilder.create(), PartPose.offset(0.5F, 2.875F, -2.45F));

        PartDefinition LeftCalf_r2 = LeftLowerLeg2.addOrReplaceChild("LeftCalf_r2", CubeListBuilder.create().texOffs(0, 28).mirror().addBox(-1.01F, -0.625F, -2.15F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -1.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

        PartDefinition LeftFoot2 = LeftLowerLeg2.addOrReplaceChild("LeftFoot2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.3F, 4.675F));

        PartDefinition LeftArch_r2 = LeftFoot2.addOrReplaceChild("LeftArch_r2", CubeListBuilder.create().texOffs(8, 28).mirror().addBox(-2.0F, -5.45F, -0.725F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.005F)).mirror(false), PartPose.offsetAndRotation(1.0F, 5.575F, -3.225F, -0.3491F, 0.0F, 0.0F));

        PartDefinition LeftPad2 = LeftFoot2.addOrReplaceChild("LeftPad2", CubeListBuilder.create().texOffs(7, 34).mirror().addBox(-1.0F, 1.0F, -0.85F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 2.825F, -2.675F));
    }

    @Override
    public void prepareMobModel(LatexSnepEntity p_162861, float p_102862, float p_102863, float p_102864_) {
        this.prepareMobModel(animator, p_162861, p_102862, p_102863, p_102864_);
    }

    /* public PoseStack getPlacementCorrectors(CorrectorType type) {
        PoseStack corrector = AdvancedHumanoidModelInterface.super.getPlacementCorrectors(type);
        if (type.isArm())
            corrector.translate(-0.02f, 0.12f, 0.12f);
        return corrector;
    } */
    @Override
    public void setupHand() {
        animator.setupHand();
    }

    @Override
    public void setupAnim(@NotNull LatexSnepEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {



        animator.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    private void setupAnimalAnimation(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        Head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        Head.xRot = headPitch * ((float) Math.PI / 180F);

        LegFrontRight.xRot = Mth.cos(limbSwing) * 1.0F * limbSwingAmount;
        LegFrontLeft.xRot = Mth.cos(limbSwing + (float) Math.PI) * 1.0F * limbSwingAmount;
        LegBackRight.xRot = Mth.cos(limbSwing + (float) Math.PI) * -1.0F * limbSwingAmount;
        LegBackLeft.xRot = Mth.cos(limbSwing) * -1.0F * limbSwingAmount;

        Torso.yRot = Mth.cos(ageInTicks * 0.1F) * 0.05F;
    }

    public ModelPart getArm(HumanoidArm p_102852) {
        return p_102852 == HumanoidArm.LEFT ? this.LegFrontLeft : this.LegFrontRight;
    }

    public ModelPart getLeg(HumanoidArm humanoidArm) {
        return humanoidArm == HumanoidArm.LEFT ? this.LegBackLeft : this.LegBackRight;
    }

    public ModelPart getHead() {
        return this.Head;
    }

    public ModelPart getTorso() {
        return Torso;
    }


    @Override
    public HumanoidAnimator<LatexSnepEntity, LatexSnepModel> getAnimator() {
        return animator;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Animal.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
