package net.foxyas.changedaddon.client.model;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.foxyas.changedaddon.entity.LatexSnepEntity;
//import net.foxyas.changedaddon.process.DEBUG;
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
import net.minecraft.world.entity.Pose;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LatexSnepModel extends AdvancedHumanoidModel<LatexSnepEntity> implements AdvancedHumanoidModelInterface<LatexSnepEntity, LatexSnepModel> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("changed_addon", "latex_snep"), "main");
    // Grupo principal: Animal e corpo

    private final ModelPart Animal;
    private final ModelPart Head;
    private final ModelPart Torso;

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

    private final HumanoidAnimator<LatexSnepEntity, LatexSnepModel> animator;

    public LatexSnepModel(ModelPart root) {
        super(root);

        this.Animal = root.getChild("Animal");
        this.Head = Animal.getChild("Head");
        this.Torso = Animal.getChild("Torso");

        this.Tail = this.Torso.getChild("Tail");
        this.TailPrimary = this.Tail.getChild("TailPrimary");
        this.TailSecondary = this.TailPrimary.getChild("TailSecondary");
        this.TailTertiary = this.TailSecondary.getChild("TailTertiary");
        this.TailQuaternary = this.TailTertiary.getChild("TailQuaternary");
        this.TailQuinternary = this.TailQuaternary.getChild("TailQuinternary");

        this.animator = HumanoidAnimator.of(this)
                .addPreset(AnimatorPresets.wolfTail(Tail, List.of(TailPrimary, TailSecondary, TailTertiary, TailQuaternary, TailQuinternary)));


        this.LegFrontRight = Animal.getChild("LegFrontRight").getChild("RightArm");
        /*ModelPart rightLowerLeg = this.LegFrontRight.getChild("RightLowerLeg");
        ModelPart rightFoot = rightLowerLeg.getChild("RightFoot");
        ModelPart rightPad = rightFoot.getChild("RightPad");*/

        this.LegBackRight = Animal.getChild("LegBackRight");
        ModelPart rightLowerLeg = this.LegBackRight.getChild("RightLowerLeg");
        ModelPart rightFoot = rightLowerLeg.getChild("RightFoot");
        ModelPart rightPad = rightFoot.getChild("RightPad");

        this.LegFrontLeft = Animal.getChild("LegFrontLeft").getChild("LeftArm");
        /*ModelPart leftLowerLeg = this.LegFrontLeft.getChild("LeftLowerLeg");
        ModelPart leftFoot = leftLowerLeg.getChild("LeftFoot");
        ModelPart leftPad = leftFoot.getChild("LeftPad");*/

        this.LegBackLeft = Animal.getChild("LegBackLeft");
        ModelPart leftLowerLeg = this.LegBackLeft.getChild("LeftLowerLeg");
        ModelPart leftFoot = leftLowerLeg.getChild("LeftFoot");
        ModelPart leftPad = leftFoot.getChild("LeftPad");


    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Animal = partdefinition.addOrReplaceChild("Animal", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.5F));

        PartDefinition Head = Animal.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -2.0F, -3.0F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.05F))
                .texOffs(0, 24).addBox(-1.5F, -0.02F, -4.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.05F))
                .texOffs(0, 10).addBox(-2.0F, -3.0F, 0.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.05F))
                .texOffs(6, 10).addBox(1.0F, -3.0F, 0.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.05F)), PartPose.offset(0.0F, -10.0F, -8.25F));

        PartDefinition Torso = Animal.addOrReplaceChild("Torso", CubeListBuilder.create(), PartPose.offset(0.0F, -9.0F, -0.5F));

        PartDefinition Torso_r1 = Torso.addOrReplaceChild("Torso_r1", CubeListBuilder.create().texOffs(50, 12).addBox(-3.0F, 3.0F, -2.0F, 6.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, -4.75F, 1.5708F, 0.0F, 0.0F));

        PartDefinition Torso_r2 = Torso.addOrReplaceChild("Torso_r2", CubeListBuilder.create().texOffs(48, 1).addBox(-3.5F, -2.0F, -2.5F, 7.0F, 5.0F, 6.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, 0.5F, -4.5F, 1.5708F, 0.0F, 0.0F));

        PartDefinition Tail = Torso.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -0.5F, 7.75F, 0.0436F, 0.0F, 0.0F));

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

        PartDefinition LegFrontRight = Animal.addOrReplaceChild("LegFrontRight", CubeListBuilder.create(), PartPose.offset(-1.0F, -6.0F, -4.5F));

        PartDefinition RightArm = LegFrontRight.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(16, 35).mirror().addBox(-1.5F, -2.0F, -1.75F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition RightThigh_r1 = RightArm.addOrReplaceChild("RightThigh_r1", CubeListBuilder.create().texOffs(0, 34).addBox(0.0F, 2.075F, -2.1F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(-1.5F, -4.5F, 1.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition LegFrontLeft = Animal.addOrReplaceChild("LegFrontLeft", CubeListBuilder.create(), PartPose.offset(1.0F, -5.75F, -4.75F));

        PartDefinition LeftArm = LegFrontLeft.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(16, 35).addBox(-0.5F, -2.25F, -1.5F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition LeftThigh_r1 = LeftArm.addOrReplaceChild("LeftThigh_r1", CubeListBuilder.create().texOffs(0, 34).mirror().addBox(-2.0F, 2.075F, -2.1F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.001F)).mirror(false), PartPose.offsetAndRotation(1.5F, -4.75F, 1.25F, -0.2182F, 0.0F, 0.0F));

        PartDefinition LegBackRight = Animal.addOrReplaceChild("LegBackRight", CubeListBuilder.create(), PartPose.offset(-2.0F, -6.0F, 6.5F));

        PartDefinition RightThigh_r2 = LegBackRight.addOrReplaceChild("RightThigh_r2", CubeListBuilder.create().texOffs(0, 34).addBox(0.25F, 2.075F, -2.1F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(-1.5F, -4.5F, 1.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition RightLowerLeg = LegBackRight.addOrReplaceChild("RightLowerLeg", CubeListBuilder.create(), PartPose.offset(-0.5F, 2.875F, -2.45F));

        PartDefinition RightCalf_r1 = RightLowerLeg.addOrReplaceChild("RightCalf_r1", CubeListBuilder.create().texOffs(0, 28).addBox(-0.75F, -0.625F, -2.15F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

        PartDefinition RightFoot = RightLowerLeg.addOrReplaceChild("RightFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.3F, 4.675F));

        PartDefinition RightArch_r1 = RightFoot.addOrReplaceChild("RightArch_r1", CubeListBuilder.create().texOffs(8, 28).addBox(0.25F, -5.45F, -0.725F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(-1.0F, 3.575F, -3.225F, -0.3491F, 0.0F, 0.0F));

        PartDefinition RightPad = RightFoot.addOrReplaceChild("RightPad", CubeListBuilder.create().texOffs(7, 34).addBox(-0.75F, -1.0F, -0.85F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.825F, -2.675F));

        PartDefinition LegBackLeft = Animal.addOrReplaceChild("LegBackLeft", CubeListBuilder.create(), PartPose.offset(1.75F, -6.0F, 6.5F));

        PartDefinition LeftThigh_r2 = LegBackLeft.addOrReplaceChild("LeftThigh_r2", CubeListBuilder.create().texOffs(0, 34).mirror().addBox(-2.0F, 2.075F, -2.1F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.001F)).mirror(false), PartPose.offsetAndRotation(1.5F, -4.5F, 1.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition LeftLowerLeg = LegBackLeft.addOrReplaceChild("LeftLowerLeg", CubeListBuilder.create(), PartPose.offset(0.5F, 2.875F, -2.45F));

        PartDefinition LeftCalf_r1 = LeftLowerLeg.addOrReplaceChild("LeftCalf_r1", CubeListBuilder.create().texOffs(0, 28).mirror().addBox(-1.01F, -0.625F, -2.15F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -3.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

        PartDefinition LeftFoot = LeftLowerLeg.addOrReplaceChild("LeftFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.3F, 4.675F));

        PartDefinition LeftArch_r1 = LeftFoot.addOrReplaceChild("LeftArch_r1", CubeListBuilder.create().texOffs(8, 28).mirror().addBox(-2.0F, -5.45F, -0.725F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.005F)).mirror(false), PartPose.offsetAndRotation(1.0F, 3.575F, -3.225F, -0.3491F, 0.0F, 0.0F));

        PartDefinition LeftPad = LeftFoot.addOrReplaceChild("LeftPad", CubeListBuilder.create().texOffs(7, 34).mirror().addBox(-1.0F, -1.0F, -0.85F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 2.825F, -2.675F));

        return LayerDefinition.create(meshdefinition, 96, 96);
    }

    @Override
    public void prepareMobModel(LatexSnepEntity entity, float limbSwing, float limbSwingAmount, float partialTicks) {
        this.prepareMobModel(animator, entity, limbSwing, limbSwingAmount, partialTicks);

        this.Torso.xRot = 0.0F;
        this.Torso.yRot = 0.0F;
        this.Torso.zRot = 0.0F;
        this.Tail.xRot = -0.05235988F;
        if (entity.isSprinting()){
            this.LegFrontRight.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            this.LegFrontLeft.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            this.LegBackRight.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
 //+ (float) Math.PI
            this.LegBackLeft.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
 //+ (float) Math.PI
            this.LegBackRight.yRot = 0f;
            this.LegBackRight.zRot = 0f;
            this.LegBackLeft.yRot = 0f;
            this.LegBackLeft.zRot = 0f;
            this.LegFrontRight.yRot = 0f;
            this.LegFrontRight.zRot = 0f;
            this.LegFrontLeft.yRot = 0f;
            this.LegFrontLeft.zRot = 0f;
        } else {
            this.LegBackRight.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.LegBackRight.yRot = 0f;
            this.LegBackRight.zRot = 0f;
            this.LegBackLeft.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            this.LegBackLeft.yRot = 0f;
            this.LegBackLeft.zRot = 0f;
            this.LegFrontRight.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            this.LegFrontRight.yRot = 0f;
            this.LegFrontRight.zRot = 0f;
            this.LegFrontLeft.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.LegFrontLeft.yRot = 0f;
            this.LegFrontLeft.zRot = 0f;
        }

        //this.Head.setPos(DEBUG.HeadPosT, DEBUG.HeadPosV, DEBUG.HeadPosB);
        this.Head.setPos(0.0F, 14.0F, -7.2F);
        this.Head.yRot = 0.0F;
        this.LegBackRight.visible = true;
        this.LegBackLeft.visible = true;
        this.LegFrontRight.visible = true;
        this.LegFrontLeft.visible = true;
        this.Torso.zRot = 0.0F;
        this.LegBackRight.setPos(-2.0F, 18.0F, 7.0F);
        this.LegBackLeft.setPos(2.0F - 0.25F, 18.0F, 7.0F);
        this.LegFrontRight.setPos(-2.0F + 0.25f, 18.0F, -3f);
        this.LegFrontLeft.setPos(2.0f + -0.25f, 18.0F, -3f);


        if (entity.isCrouching()) {
            //this.Body.xRot = 3.246312F;
            float f = entity.getCrouchAmount(partialTicks);
            this.Torso.setPos(0.0F, 15.0F + entity.getCrouchAmount(partialTicks) - (entity.WantToLoaf() ? 0 : 2f), 0.0F);
            this.Head.setPos(0.0F, 14.0F + f - (entity.WantToLoaf() ? 0F : 2f), -7.2F);
        } else if (entity.isSleeping()) {
            this.LegBackRight.visible = false;
            this.LegBackLeft.visible = false;
            this.LegFrontRight.visible = false;
            this.LegFrontLeft.visible = false;
            this.Torso.xRot = -90 * Mth.DEG_TO_RAD;
            this.Torso.yRot = 180 * Mth.DEG_TO_RAD;
            this.Torso.setPos(0,22.5F,-2.5F);
            this.Head.setPos(0,14F,-1.75F);
            this.Head.xRot = -90 * Mth.DEG_TO_RAD;
            this.Head.yRot = 180F * Mth.DEG_TO_RAD;
        } else if (!entity.isShiftKeyDown()) {
            this.Torso.setPos(0.0F, 15.0F, 0.0F);
        }

    }

    @Override
    public void setupHand() {
        animator.setupHand();
    }


    @Override
    public void setupAnim(@NotNull LatexSnepEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

        if (!entity.isSleeping()) {
            this.Head.xRot = headPitch * ((float)Math.PI / 180F);
            this.Head.yRot = netHeadYaw * ((float)Math.PI / 180F);
            this.Head.zRot = 0.0F;
            this.Torso.yRot = 0.0F;
        }

        if (entity.isSleeping()) {
            /*this.Head.xRot = 0.0F;
            this.Head.yRot = -2.0943952F;
            this.Head.zRot = Mth.cos(ageInTicks * 0.027F) / 22.0F;*/
        }

        if (entity.isCrouching()) {
            if (entity.WantToLoaf()) {
                float f = Mth.cos(ageInTicks) * 0.01F;
                this.Torso.yRot = f;
                this.LegBackRight.zRot = f;
                this.LegBackLeft.zRot = f;
                this.LegFrontRight.zRot = f / 2.0F;
                this.LegFrontLeft.zRot = f / 2.0F;
            }
        }
        animator.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        if (entity.isCrouching()){
            this.Tail.xRot = 20f * ((float)Math.PI / 180F);
        }
        if (entity.isSleeping()) {
            this.Tail.xRot = 0 * Mth.DEG_TO_RAD;
        }

        if (entity.getPose() == Pose.SWIMMING || entity.getPose() == Pose.FALL_FLYING) {
            this.Tail.xRot = 0 * Mth.DEG_TO_RAD;
        }

        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
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
        Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Torso.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        LegFrontRight.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        LegBackRight.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        LegBackLeft.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        LegFrontLeft.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
