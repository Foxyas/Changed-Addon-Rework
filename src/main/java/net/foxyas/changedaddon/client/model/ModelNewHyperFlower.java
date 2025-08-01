package net.foxyas.changedaddon.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
public class ModelNewHyperFlower<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in
    // the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("changed_addon", "model_new_hyper_flower"), "main");
    public final ModelPart HyperFlowerModel;

    public ModelNewHyperFlower(ModelPart root) {
        this.HyperFlowerModel = root.getChild("HyperFlowerModel");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition HyperFlowerModel = partdefinition.addOrReplaceChild("HyperFlowerModel", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 0.0F));
        PartDefinition Moon = HyperFlowerModel.addOrReplaceChild("Moon",
                CubeListBuilder.create().texOffs(8, 17).addBox(1.75F, -34.75F, 1.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 17).addBox(1.75F, -34.75F, -1.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 17)
                        .addBox(1.75F, -35.75F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(20, 15).addBox(1.75F, -35.75F, 1.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(16, 15)
                        .addBox(1.74F, -32.75F, 0.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 15).addBox(1.74F, -33.25F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 15)
                        .addBox(1.74F, -33.25F, 1.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(3.75F, 1.75F, -31.5F, -1.309F, 0.0436F, -0.3491F));
        PartDefinition cube_r1 = Moon.addOrReplaceChild("cube_r1",
                CubeListBuilder.create().texOffs(16, 19).addBox(1.75F, -34.5F, 0.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 17).addBox(1.75F, -32.25F, 0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(16, 17)
                        .addBox(1.75F, -32.25F, -0.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(20, 17).addBox(1.75F, -33.25F, 1.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 19)
                        .addBox(1.75F, -33.25F, -1.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 15).addBox(1.75F, -32.75F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 19)
                        .addBox(1.75F, -32.75F, 1.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 19).addBox(1.75F, -33.75F, 1.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 19)
                        .addBox(1.75F, -33.75F, -1.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition Rose = HyperFlowerModel.addOrReplaceChild("Rose", CubeListBuilder.create().texOffs(0, 15).addBox(4.75F, -33.0216F, -4.1481F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(15.0F, 24.75F, 3.0F, 0.0F, 0.2182F, -0.4363F));
        PartDefinition Base_r1 = Rose.addOrReplaceChild("Base_r1", CubeListBuilder.create().texOffs(12, 4).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(5.0F, -32.5216F, -3.6481F, -0.7854F, 0.0F, 0.0F));
        PartDefinition Petals = Rose.addOrReplaceChild("Petals", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition inner1 = Petals.addOrReplaceChild("inner1", CubeListBuilder.create(), PartPose.offsetAndRotation(5.75F, -32.5216F, -3.6481F, -0.7418F, 0.0F, 0.0F));
        PartDefinition cube4_r1 = inner1.addOrReplaceChild("cube4_r1", CubeListBuilder.create().texOffs(6, 21).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.25F, 0.25F, 0.0F, -1.5708F, -0.7854F, 0.0F));
        PartDefinition cube3_r1 = inner1.addOrReplaceChild("cube3_r1", CubeListBuilder.create().texOffs(6, 22).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.25F, -0.25F, 0.0F, -1.5708F, -0.7854F, 0.0F));
        PartDefinition cube2_r1 = inner1.addOrReplaceChild("cube2_r1", CubeListBuilder.create().texOffs(8, 21).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.25F, 0.0F, 0.25F, 0.0F, 0.0F, 0.7854F));
        PartDefinition cube1_r1 = inner1.addOrReplaceChild("cube1_r1", CubeListBuilder.create().texOffs(8, 22).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.25F, 0.0F, -0.25F, 0.0F, 0.0F, 0.7854F));
        PartDefinition inner2 = Petals.addOrReplaceChild("inner2",
                CubeListBuilder.create().texOffs(6, 23).addBox(-0.75F, -0.5F, -0.5F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(4, 23).addBox(-0.75F, -0.5F, 0.5F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offset(5.75F, -32.5216F, -3.6481F));
        PartDefinition cube4_r2 = inner2.addOrReplaceChild("cube4_r2",
                CubeListBuilder.create().texOffs(0, 23).addBox(-0.75F, -0.5F, 0.5F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(12, 21).addBox(-0.75F, -0.5F, -0.5F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));
        PartDefinition cube2_r2 = inner2.addOrReplaceChild("cube2_r2",
                CubeListBuilder.create().texOffs(10, 22).addBox(-0.75F, -0.5F, -0.5F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(10, 21).addBox(-0.75F, -0.5F, 0.5F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.3562F, 0.0F, 0.0F));
        PartDefinition cube4_r3 = inner2.addOrReplaceChild("cube4_r3",
                CubeListBuilder.create().texOffs(2, 23).addBox(-0.75F, -0.5F, 0.5F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(8, 23).addBox(-0.75F, -0.5F, -0.5F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));
        PartDefinition layer1 = Petals.addOrReplaceChild("layer1", CubeListBuilder.create(), PartPose.offsetAndRotation(5.8232F, -32.5216F, -3.6481F, -0.7854F, 0.0F, 0.0F));
        PartDefinition cube_r2 = layer1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(20, 19).addBox(-0.6082F, 0.1547F, -0.6074F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.0328F, -0.1074F, -0.2162F, 1.5708F, 0.7854F, 0.0F));
        PartDefinition cube_r3 = layer1.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 21).addBox(-0.25F, -1.0F, -1.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.1768F, -0.5F, -0.1464F, 1.5708F, -0.7854F, 0.0F));
        PartDefinition cube_r4 = layer1.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(2, 21).addBox(-0.25F, -1.0F, 0.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.1768F, -0.1464F, -0.5F, 0.0F, 0.0F, 0.7854F));
        PartDefinition cube_r5 = layer1.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(4, 21).addBox(-0.65F, 0.7F, 0.4F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.3889F, -0.6314F, -0.9F, 0.0F, 0.0F, -0.7854F));
        PartDefinition layer2 = Petals.addOrReplaceChild("layer2", CubeListBuilder.create(), PartPose.offset(-15.0F, 0.25F, -3.0F));
        PartDefinition cube_r6 = layer2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(14, 8).addBox(-0.8582F, 0.4047F, -1.1074F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(20.7904F, -32.8791F, -0.8643F, 1.5708F, 0.7854F, 0.0F));
        PartDefinition cube_r7 = layer2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 12).addBox(-0.5F, -1.25F, -1.5F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(20.6464F, -33.2716F, -0.7945F, 1.5708F, -0.7854F, 0.0F));
        PartDefinition cube_r8 = layer2.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(8, 12).addBox(-0.5F, -1.25F, -0.5F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(20.6464F, -32.9181F, -1.1481F, 0.0F, 0.0F, 0.7854F));
        PartDefinition cube_r9 = layer2.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(16, 12).addBox(-0.9F, 0.95F, -0.1F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(20.4343F, -33.403F, -1.5481F, 0.0F, 0.0F, -0.7854F));
        PartDefinition layer3 = Petals.addOrReplaceChild("layer3", CubeListBuilder.create(), PartPose.offset(-15.0F, 0.25F, -3.0F));
        PartDefinition cube_r10 = layer3.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(18, 0).addBox(-1.1082F, 0.6547F, -1.6074F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(20.7904F, -32.8791F, -0.8643F, 1.5708F, 0.7854F, 0.0F));
        PartDefinition cube_r11 = layer3.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(0, 4).addBox(-0.75F, -1.5F, -2.0F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(20.6464F, -33.2716F, -0.7945F, 1.5708F, -0.7854F, 0.0F));
        PartDefinition cube_r12 = layer3.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(6, 4).addBox(-0.75F, -1.5F, -1.0F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(20.6464F, -32.9181F, -1.1481F, 0.0F, 0.0F, 0.7854F));
        PartDefinition cube_r13 = layer3.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 8).addBox(-1.15F, 1.2F, -0.6F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(20.4343F, -33.403F, -1.5481F, 0.0F, 0.0F, -0.7854F));
        PartDefinition layer4 = Petals.addOrReplaceChild("layer4", CubeListBuilder.create(), PartPose.offsetAndRotation(5.8232F, -32.5216F, -3.6481F, -0.7854F, 0.0F, 0.0F));
        PartDefinition cube_r14 = layer4.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(6, 8).addBox(-0.8582F, 0.4047F, -1.1074F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.0328F, -0.1074F, -0.2162F, 1.5708F, 0.7854F, 0.0F));
        PartDefinition cube_r15 = layer4.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(10, 8).addBox(-0.5F, -1.25F, -1.5F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.1768F, -0.5F, -0.1464F, 1.5708F, -0.7854F, 0.0F));
        PartDefinition cube_r16 = layer4.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(4, 12).addBox(-0.5F, -1.25F, -0.5F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.1768F, -0.1464F, -0.5F, 0.0F, 0.0F, 0.7854F));
        PartDefinition cube_r17 = layer4.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(12, 12).addBox(-0.9F, 0.95F, -0.1F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.3889F, -0.6314F, -0.9F, 0.0F, 0.0F, -0.7854F));
        PartDefinition layer5 = Petals.addOrReplaceChild("layer5", CubeListBuilder.create(), PartPose.offsetAndRotation(5.8232F, -32.5216F, -3.6481F, -0.7854F, 0.0F, 0.0F));
        PartDefinition cube_r18 = layer5.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(0, 0).addBox(-1.1082F, 0.6547F, -1.6074F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.0328F, -0.1074F, -0.2162F, 1.5708F, 0.7854F, 0.0F));
        PartDefinition cube_r19 = layer5.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(6, 0).addBox(-0.75F, -1.5F, -2.0F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.1768F, -0.5F, -0.1464F, 1.5708F, -0.7854F, 0.0F));
        PartDefinition cube_r20 = layer5.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(12, 0).addBox(-0.75F, -1.5F, -1.0F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.1768F, -0.1464F, -0.5F, 0.0F, 0.0F, 0.7854F));
        PartDefinition cube_r21 = layer5.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(18, 4).addBox(-1.15F, 1.2F, -0.6F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-0.3889F, -0.6314F, -0.9F, 0.0F, 0.0F, -0.7854F));
        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        HyperFlowerModel.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }
}
