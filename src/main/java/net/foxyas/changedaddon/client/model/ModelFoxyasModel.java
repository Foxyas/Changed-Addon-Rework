package net.foxyas.changedaddon.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

// Made with Blockbench 4.6.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
public class ModelFoxyasModel<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in
    // the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ChangedAddonMod.resourceLoc("model_foxyas_model"), "main");
    public final ModelPart Head;
    public final ModelPart Torso;
    public final ModelPart RightArm;
    public final ModelPart LeftArm;
    public final ModelPart RightLeg;
    public final ModelPart LeftLeg;

    public ModelFoxyasModel(ModelPart root) {
        this.Head = root.getChild("Head");
        this.Torso = root.getChild("Torso");
        this.RightArm = root.getChild("RightArm");
        this.LeftArm = root.getChild("LeftArm");
        this.RightLeg = root.getChild("RightLeg");
        this.LeftLeg = root.getChild("LeftLeg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition Head = partdefinition.addOrReplaceChild("Head",
                CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F)).texOffs(0, 22)
                        .addBox(-1.5F, -1.0F, -5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 21).addBox(-0.5F, -3.1F, -6.05F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 0)
                        .addBox(-2.0F, -3.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition RightEar = Head.addOrReplaceChild("RightEar",
                CubeListBuilder.create().texOffs(0, 4).addBox(-1.9925F, -0.2F, -0.7125F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F)).texOffs(68, 53).addBox(-1.0025F, -0.8F, 0.1375F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.04F)).texOffs(24, 6)
                        .addBox(-1.0025F, -1.3F, -0.7125F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)).texOffs(20, 34).addBox(-0.0025F, -2.2F, -0.7125F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)),
                PartPose.offsetAndRotation(-2.5813F, -9.6502F, -0.2875F, -0.2533F, 0.7519F, -0.3622F));
        PartDefinition LeftEar = Head.addOrReplaceChild("LeftEar",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -0.225F, -0.7125F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.05F)).texOffs(24, 16).addBox(-1.0F, -0.825F, 0.1375F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.04F)).texOffs(24, 4)
                        .addBox(-1.0F, -1.325F, -0.7125F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)).texOffs(0, 34).addBox(-1.0F, -2.125F, -0.7125F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.05F)),
                PartPose.offsetAndRotation(2.6006F, -9.7236F, -0.2875F, -0.2533F, -0.7519F, 0.3622F));
        PartDefinition Torso = partdefinition.addOrReplaceChild("Torso",
                CubeListBuilder.create().texOffs(0, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(28, 28).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.2F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition Tail = Torso.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offset(0.0F, 11.0F, 0.0F));
        PartDefinition lower_fur_r1 = Tail.addOrReplaceChild("lower_fur_r1",
                CubeListBuilder.create().texOffs(68, 38).addBox(-1.5F, 0.1914F, -1.4483F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.2F)).texOffs(64, 68).addBox(-1.5F, 0.1914F, -1.4483F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 2.125F, 6.0F, 1.4835F, 0.0F, 0.0F));
        PartDefinition tail_fur_r1 = Tail.addOrReplaceChild("tail_fur_r1",
                CubeListBuilder.create().texOffs(32, 16).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.2F)).texOffs(68, 28).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1781F, 0.0F, 0.0F));
        PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm",
                CubeListBuilder.create().texOffs(20, 44).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(32, 0).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.2F)),
                PartPose.offset(-5.0F, 2.0F, 0.0F));
        PartDefinition rightpawbeans = RightArm
                .addOrReplaceChild("rightpawbeans",
                        CubeListBuilder.create().texOffs(0, 16).addBox(-7.25F, -25.025F, -1.675F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.075F)).texOffs(30, 16).addBox(-8.025F, -25.025F, -2.925F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F))
                                .texOffs(0, 32).addBox(-6.75F, -25.025F, -3.175F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).texOffs(27, 22).addBox(-5.45F, -25.025F, -2.925F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)),
                        PartPose.offset(5.25F, 34.0F, 1.3F));
        PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm",
                CubeListBuilder.create().texOffs(36, 44).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(44, 12).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.2F)),
                PartPose.offset(5.0F, 2.0F, 0.0F));
        PartDefinition leftarmpawbeans = LeftArm.addOrReplaceChild(
                "leftarmpawbeans", CubeListBuilder.create().texOffs(0, 16).addBox(4.75F, -25.025F, -1.675F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.075F)).texOffs(27, 22).addBox(6.55F, -25.025F, -2.925F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F))
                        .texOffs(0, 32).addBox(5.25F, -25.025F, -3.175F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).texOffs(30, 16).addBox(3.975F, -25.025F, -2.925F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)),
                PartPose.offset(-4.75F, 34.0F, 1.3F));
        PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offset(-2.25F, 12.0F, 0.0F));
        PartDefinition leg_fur_r1 = RightLeg.addOrReplaceChild("leg_fur_r1",
                CubeListBuilder.create().texOffs(60, 18).addBox(-2.0F, -6.7987F, -2.9677F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.2F)).texOffs(48, 62).addBox(-2.0F, -6.7987F, -2.9677F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 5.275F, 4.9F, 1.2217F, 0.0F, 0.0F));
        PartDefinition thigh_fur_r1 = RightLeg.addOrReplaceChild("thigh_fur_r1",
                CubeListBuilder.create().texOffs(32, 60).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.25F)).texOffs(64, 58).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.05F)),
                PartPose.offsetAndRotation(0.0F, -0.1F, 0.0F, -0.0873F, 0.0F, 0.0F));
        PartDefinition RightLower = RightLeg.addOrReplaceChild("RightLower", CubeListBuilder.create().texOffs(64, 48).addBox(-2.0F, 6.975F, -4.675F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.025F)), PartPose.offset(0.0F, 4.0F, 4.5F));
        PartDefinition leg_fur_r2 = RightLower.addOrReplaceChild("leg_fur_r2",
                CubeListBuilder.create().texOffs(52, 28).addBox(-2.0F, -0.225F, -3.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.225F)).texOffs(52, 40).addBox(-2.0F, -0.225F, -3.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.025F)),
                PartPose.offsetAndRotation(0.0F, 0.15F, -1.0F, -0.0873F, 0.0F, 0.0F));
        PartDefinition RightLowerBeans = RightLower.addOrReplaceChild(
                "RightLowerBeans", CubeListBuilder.create().texOffs(0, 19).addBox(1.0F, -13.025F, 0.325F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.075F)).texOffs(32, 26).addBox(1.5F, -13.025F, -1.175F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F))
                        .texOffs(24, 32).addBox(0.225F, -13.025F, -0.925F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).texOffs(20, 32).addBox(2.8F, -13.025F, -0.925F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)),
                PartPose.offset(-2.0F, 20.0F, -3.2F));
        PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(2.25F, 12.0F, 0.0F));
        PartDefinition leg_fur_r3 = LeftLeg.addOrReplaceChild("leg_fur_r3",
                CubeListBuilder.create().texOffs(52, 52).addBox(-2.0F, -6.7987F, -2.9677F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.2F)).texOffs(0, 60).addBox(-2.0F, -6.7987F, -2.9677F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 5.275F, 4.9F, 1.2217F, 0.0F, 0.0F));
        PartDefinition thigh_fur_r2 = LeftLeg.addOrReplaceChild("thigh_fur_r2",
                CubeListBuilder.create().texOffs(60, 8).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.25F)).texOffs(16, 60).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.05F)),
                PartPose.offsetAndRotation(0.0F, -0.1F, 0.0F, -0.0873F, 0.0F, 0.0F));
        PartDefinition LeftLower = LeftLeg.addOrReplaceChild("LeftLower", CubeListBuilder.create().texOffs(64, 0).addBox(-2.0F, 6.975F, -4.675F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.025F)), PartPose.offset(0.0F, 4.0F, 4.5F));
        PartDefinition leg_fur_r4 = LeftLower.addOrReplaceChild("leg_fur_r4",
                CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, -0.225F, -3.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.225F)).texOffs(48, 0).addBox(-2.0F, -0.225F, -3.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.025F)),
                PartPose.offsetAndRotation(0.0F, 0.15F, -1.0F, -0.0873F, 0.0F, 0.0F));
        PartDefinition LeftLowerBeans2 = LeftLower.addOrReplaceChild(
                "LeftLowerBeans2", CubeListBuilder.create().texOffs(0, 16).addBox(1.0F, -13.025F, 0.325F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.075F)).texOffs(0, 32).addBox(1.5F, -13.025F, -1.175F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F))
                        .texOffs(30, 16).addBox(0.225F, -13.025F, -0.925F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).texOffs(27, 22).addBox(2.8F, -13.025F, -0.925F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)),
                PartPose.offset(-2.0F, 20.0F, -3.2F));
        return LayerDefinition.create(meshdefinition, 128, 128);
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

    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.RightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
        this.LeftLeg.xRot = Mth.cos(limbSwing) * -1.0F * limbSwingAmount;
        this.Head.yRot = netHeadYaw / (180F / (float) Math.PI);
        this.Head.xRot = headPitch / (180F / (float) Math.PI);
        this.LeftArm.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
        this.RightLeg.xRot = Mth.cos(limbSwing) * 1.0F * limbSwingAmount;
    }
}
