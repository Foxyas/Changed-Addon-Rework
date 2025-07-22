package net.foxyas.changedaddon.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.entity.LynxEntity;
import net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModelInterface;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LynxModel extends AdvancedHumanoidModel<LynxEntity> implements AdvancedHumanoidModelInterface<LynxEntity, LynxModel> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("changed_addon", "lynx_model"), "main");
    private final ModelPart RightLeg;
    private final ModelPart LeftLeg;
    private final ModelPart Head;
    private final ModelPart CheekFur;
    private final ModelPart Torso;
    private final ModelPart RightArm;
    private final ModelPart LeftArm;
    private final ModelPart Tail;
    private final HumanoidAnimator<LynxEntity, LynxModel> animator;

    public LynxModel(ModelPart root) {
        super(root);
        this.RightLeg = root.getChild("RightLeg");
        this.LeftLeg = root.getChild("LeftLeg");
        this.Head = root.getChild("Head");
        this.CheekFur = this.Head.getChild("CheekFur");
        this.Torso = root.getChild("Torso");
        this.Tail = Torso.getChild("Tail");
        this.RightArm = root.getChild("RightArm");
        this.LeftArm = root.getChild("LeftArm");

        var tailPrimary = Tail.getChild("TailPrimary");

        var leftLowerLeg = LeftLeg.getChild("LeftLowerLeg");
        var leftFoot = leftLowerLeg.getChild("LeftFoot");
        var rightLowerLeg = RightLeg.getChild("RightLowerLeg");
        var rightFoot = rightLowerLeg.getChild("RightFoot");

        animator = HumanoidAnimator.of(this).hipOffset(-1.5f)
                .addPreset(AnimatorPresets.catLike(
                        Head, Head.getChild("LeftEar"), Head.getChild("RightEar"),
                        Torso, LeftArm, RightArm,
                        Tail, List.of(tailPrimary),
                        LeftLeg, leftLowerLeg, leftFoot, leftFoot.getChild("LeftPad"), RightLeg, rightLowerLeg, rightFoot, rightFoot.getChild("RightPad")));
    }


    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offset(-2.5F, 10.5F, 0.0F));

        PartDefinition RightThigh_r1 = RightLeg.addOrReplaceChild("RightThigh_r1", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition RightLowerLeg = RightLeg.addOrReplaceChild("RightLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

        PartDefinition RightCalf_r1 = RightLowerLeg.addOrReplaceChild("RightCalf_r1", CubeListBuilder.create().texOffs(48, 50).addBox(-1.99F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

        PartDefinition RightFoot = RightLowerLeg.addOrReplaceChild("RightFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

        PartDefinition RightArch_r1 = RightFoot.addOrReplaceChild("RightArch_r1", CubeListBuilder.create().texOffs(44, 60).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

        PartDefinition RightPad = RightFoot.addOrReplaceChild("RightPad", CubeListBuilder.create().texOffs(52, 32).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

        PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(2.5F, 10.5F, 0.0F));

        PartDefinition LeftThigh_r1 = LeftLeg.addOrReplaceChild("LeftThigh_r1", CubeListBuilder.create().texOffs(32, 44).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition LeftLowerLeg = LeftLeg.addOrReplaceChild("LeftLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

        PartDefinition LeftCalf_r1 = LeftLowerLeg.addOrReplaceChild("LeftCalf_r1", CubeListBuilder.create().texOffs(48, 40).addBox(-2.01F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

        PartDefinition LeftFoot = LeftLowerLeg.addOrReplaceChild("LeftFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

        PartDefinition LeftArch_r1 = LeftFoot.addOrReplaceChild("LeftArch_r1", CubeListBuilder.create().texOffs(13, 56).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

        PartDefinition LeftPad = LeftFoot.addOrReplaceChild("LeftPad", CubeListBuilder.create().texOffs(24, 0).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

        PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.5F, -1.0F, -5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(32, 22).addBox(-2.0F, -3.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));

        PartDefinition Snout_r1 = Head.addOrReplaceChild("Snout_r1", CubeListBuilder.create().texOffs(0, 2).addBox(-1.0F, -29.625F, -0.95F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 26.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition RightEar = Head.addOrReplaceChild("RightEar", CubeListBuilder.create(), PartPose.offset(-2.5F, -5.0F, 0.0F));

        PartDefinition rightear_r1 = RightEar.addOrReplaceChild("rightear_r1", CubeListBuilder.create().texOffs(2, 81).mirror().addBox(-0.5F, -2.5F, -1.5F, 1.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.8213F, -3.3561F, 0.6959F, -0.4274F, -0.3564F, -0.6232F));

        PartDefinition rightear_r2 = RightEar.addOrReplaceChild("rightear_r2", CubeListBuilder.create().texOffs(13, 68).addBox(6.25F, -31.25F, -18.25F, 0.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 29.0F, 0.65F, -0.5236F, -0.1745F, -0.2618F));

        PartDefinition rightear_r3 = RightEar.addOrReplaceChild("rightear_r3", CubeListBuilder.create().texOffs(0, 59).addBox(4.25F, -31.25F, -18.25F, 2.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 30.0F, 0.0F, -0.5236F, -0.1745F, -0.2618F));

        PartDefinition LeftEar = Head.addOrReplaceChild("LeftEar", CubeListBuilder.create(), PartPose.offset(2.5F, -5.0F, 0.0F));

        PartDefinition leftear_r1 = LeftEar.addOrReplaceChild("leftear_r1", CubeListBuilder.create().texOffs(13, 68).mirror().addBox(-6.25F, -31.25F, -18.25F, 0.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 29.0F, 0.65F, -0.5236F, 0.1745F, 0.2618F));

        PartDefinition leftear_r2 = LeftEar.addOrReplaceChild("leftear_r2", CubeListBuilder.create().texOffs(2, 81).addBox(-0.5F, -2.5F, -1.5F, 1.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.8213F, -3.3561F, 0.6959F, -0.4274F, 0.3564F, 0.6232F));

        PartDefinition leftear_r3 = LeftEar.addOrReplaceChild("leftear_r3", CubeListBuilder.create().texOffs(16, 32).addBox(-6.25F, -31.25F, -18.25F, 2.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 30.0F, 0.0F, -0.5236F, 0.1745F, 0.2618F));

        PartDefinition Hair = Head.addOrReplaceChild("Hair", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, -34.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F))
                .texOffs(24, 8).addBox(-4.5F, -34.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.3F)), PartPose.offset(0.5F, 26.0F, 0.0F));

        PartDefinition CheekFur = Head.addOrReplaceChild("CheekFur", CubeListBuilder.create(), PartPose.offset(0.0F, 0.15F, -0.5F));

        PartDefinition Right = CheekFur.addOrReplaceChild("Right", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = Right.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 74).addBox(0.0F, -1.0F, -2.5F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(-4.25F, -1.05F, 0.5F, 0.0F, 0.0F, -0.9599F));

        PartDefinition cube_r2 = Right.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 67).addBox(0.0F, -1.0F, -2.5F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(-4.0F, -2.05F, 0.5F, 0.0F, 0.0F, -0.7854F));

        PartDefinition Left = CheekFur.addOrReplaceChild("Left", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r3 = Left.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 74).mirror().addBox(-1.0F, -1.0F, -2.5F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.2F)).mirror(false), PartPose.offsetAndRotation(4.25F, -1.05F, 0.5F, 0.0F, 0.0F, 0.9599F));

        PartDefinition cube_r4 = Left.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 67).mirror().addBox(-1.0F, -1.0F, -2.5F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(4.0F, -2.05F, 0.5F, 0.0F, 0.0F, 0.7854F));

        PartDefinition Torso = partdefinition.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(28, 28).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));

        PartDefinition Torso_r1 = Torso.addOrReplaceChild("Torso_r1", CubeListBuilder.create().texOffs(25, 65).addBox(-2.5F, -2.5F, 0.0F, 6.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 5.0F, -1.9F, 0.2618F, 0.0F, 0.0F));

        PartDefinition Torso_r2 = Torso.addOrReplaceChild("Torso_r2", CubeListBuilder.create().texOffs(13, 65).addBox(-2.5F, -2.5F, 0.0F, 6.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 2.5F, -2.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition Tail = Torso.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offset(0.0F, 10.5F, 0.0F));

        PartDefinition TailPrimary = Tail.addOrReplaceChild("TailPrimary", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition Base_r1 = TailPrimary.addOrReplaceChild("Base_r1", CubeListBuilder.create().texOffs(19, 76).addBox(-2.0F, -2.5F, 0.0F, 4.0F, 5.0F, 0.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(0.0F, 0.3185F, 3.8098F, 2.0508F, 0.0F, 0.0F));

        PartDefinition Base_r2 = TailPrimary.addOrReplaceChild("Base_r2", CubeListBuilder.create().texOffs(19, 81).addBox(-2.0F, -2.2F, -0.4F, 4.0F, 5.0F, 0.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(0.0F, 1.5185F, 4.5098F, 1.0908F, 0.0F, 0.0F));

        PartDefinition Base_r3 = TailPrimary.addOrReplaceChild("Base_r3", CubeListBuilder.create().texOffs(19, 71).addBox(-2.0F, -2.5F, 0.0F, 4.0F, 5.0F, 0.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(0.0F, -0.2815F, 2.6098F, 2.1817F, 0.0F, 0.0F));

        PartDefinition Base_r4 = TailPrimary.addOrReplaceChild("Base_r4", CubeListBuilder.create().texOffs(28, 55).addBox(-2.0F, 0.75F, -1.4F, 4.0F, 5.0F, 4.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.0908F, 0.0F, 0.0F));

        PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(0, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 1.5F, 0.0F));

        PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(16, 40).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 1.5F, 0.0F));

        return LayerDefinition.create(meshdefinition, 96, 96);
    }

    @Override
    public void prepareMobModel(LynxEntity p_162861, float p_102862, float p_102863, float p_102864_) {
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
    public void setupHand(LynxEntity entity) {
        animator.setupHand();
    }


    @Override
    public void setupAnim(@NotNull LynxEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animator.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
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
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Torso.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        RightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        LeftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        RightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        LeftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public HumanoidAnimator<LynxEntity, LynxModel> getAnimator(LynxEntity entity) {
        return animator;
    }
}