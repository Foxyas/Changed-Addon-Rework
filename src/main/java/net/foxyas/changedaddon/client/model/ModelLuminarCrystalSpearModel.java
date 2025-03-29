package net.foxyas.changedaddon.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.EntityModel;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

// Made with Blockbench 4.12.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
public class ModelLuminarCrystalSpearModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("changed_addon", "model_luminar_crystal_spear_model"), "main");
	public final ModelPart IceSpikeBottom;
	public final ModelPart Ice_Spike2;
	public final ModelPart Ice_Spike3;
	public final ModelPart IceSpike;
	public final ModelPart Gaurd;
	public final ModelPart Grip2;
	public final ModelPart Grip;
	public final ModelPart bone;

	public ModelLuminarCrystalSpearModel(ModelPart root) {
		this.IceSpikeBottom = root.getChild("IceSpikeBottom");
		this.Ice_Spike2 = root.getChild("Ice_Spike2");
		this.Ice_Spike3 = root.getChild("Ice_Spike3");
		this.IceSpike = root.getChild("IceSpike");
		this.Gaurd = root.getChild("Gaurd");
		this.Grip2 = root.getChild("Grip2");
		this.Grip = root.getChild("Grip");
		this.bone = root.getChild("bone");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition IceSpikeBottom = partdefinition.addOrReplaceChild("IceSpikeBottom",
				CubeListBuilder.create().texOffs(52, 62).addBox(0.0F, 10.6F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F)).texOffs(52, 60).addBox(0.0F, 11.3F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F)).texOffs(48, 62)
						.addBox(0.0F, 11.8F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.3F)).texOffs(44, 61).addBox(0.0F, 11.5F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.4F)),
				PartPose.offset(-0.5F, 20.25F, -0.5F));
		PartDefinition Ice_Spike2 = partdefinition.addOrReplaceChild("Ice_Spike2",
				CubeListBuilder.create().texOffs(56, 60).addBox(-1.0F, 6.55F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.25F)).texOffs(56, 56).addBox(-1.0F, 5.15F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.35F)).texOffs(56, 52)
						.addBox(-1.0F, 3.95F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.45F)).texOffs(56, 48).addBox(-1.0F, 2.95F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.55F)).texOffs(56, 44)
						.addBox(-1.0F, 2.15F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.65F)).texOffs(56, 39).addBox(-1.0F, 1.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.75F)).texOffs(56, 34)
						.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.85F)),
				PartPose.offset(-3.0F, -4.05F, 0.0F));
		PartDefinition Ice_Spike3 = partdefinition.addOrReplaceChild("Ice_Spike3",
				CubeListBuilder.create().texOffs(40, 56).addBox(5.0F, 6.55F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.25F)).texOffs(40, 52).addBox(5.0F, 5.15F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.35F)).texOffs(40, 48)
						.addBox(5.0F, 3.95F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.45F)).texOffs(40, 44).addBox(5.0F, 2.95F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.55F)).texOffs(40, 40)
						.addBox(5.0F, 2.15F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.65F)).texOffs(40, 35).addBox(5.0F, 1.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.75F)).texOffs(40, 30)
						.addBox(5.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.85F)),
				PartPose.offset(-3.0F, -4.05F, 0.0F));
		PartDefinition IceSpike = partdefinition.addOrReplaceChild("IceSpike",
				CubeListBuilder.create().texOffs(48, 56).addBox(2.0F, 5.55F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.25F)).texOffs(48, 52).addBox(2.0F, 4.15F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.35F)).texOffs(48, 48)
						.addBox(2.0F, 2.95F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.45F)).texOffs(48, 44).addBox(2.0F, 1.95F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.55F)).texOffs(48, 40)
						.addBox(2.0F, 1.15F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.65F)).texOffs(48, 35).addBox(2.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.75F)).texOffs(48, 30)
						.addBox(2.0F, -1.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.85F)),
				PartPose.offset(-3.0F, -4.05F, 0.0F));
		PartDefinition Gaurd = partdefinition.addOrReplaceChild("Gaurd",
				CubeListBuilder.create().texOffs(0, 60).addBox(-3.5F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(26, 60).addBox(-3.5F, -3.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 57)
						.addBox(-0.5F, -2.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 54).addBox(-6.5137F, -2.0137F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(8, 60)
						.addBox(-4.5F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(8, 56).addBox(-1.5F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(2.5F, 5.0F, 0.0F));
		PartDefinition cube_r1 = Gaurd.addOrReplaceChild("cube_r1",
				CubeListBuilder.create().texOffs(20, 57).addBox(1.7092F, -1.5512F, -1.001F, 2.0F, 1.0F, 2.002F, new CubeDeformation(0.0F)).texOffs(14, 56).addBox(-2.0761F, 0.6105F, -1.003F, 1.0F, 2.0F, 2.006F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-2.5068F, -1.0F, 0.0F, 0.0F, 0.0F, 0.3927F));
		PartDefinition cube_r2 = Gaurd.addOrReplaceChild("cube_r2",
				CubeListBuilder.create().texOffs(14, 60).addBox(1.0887F, 0.6157F, -1.003F, 1.0F, 2.0F, 2.006F, new CubeDeformation(0.0F)).texOffs(20, 54).addBox(-3.6966F, -1.546F, -1.001F, 2.0F, 1.0F, 2.002F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-2.5068F, -1.0F, 0.0F, 0.0F, 0.0F, -0.3927F));
		PartDefinition cube_r3 = Gaurd.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(8, 52).addBox(0.9287F, 1.2159F, -1.002F, 1.0F, 2.0F, 2.004F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-2.5068F, -1.0F, 0.0F, 0.0F, 0.0F, -0.7854F));
		PartDefinition cube_r4 = Gaurd.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(20, 60).addBox(-1.9191F, 1.2063F, -1.002F, 1.0F, 2.0F, 2.004F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-2.5068F, -1.0F, 0.0F, 0.0F, 0.0F, 0.7854F));
		PartDefinition Grip2 = partdefinition.addOrReplaceChild("Grip2",
				CubeListBuilder.create().texOffs(0, 38).addBox(-1.0F, 2.75F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.3F)).texOffs(0, 42).addBox(-1.0F, 4.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.2F)).texOffs(0, 42)
						.addBox(-1.0F, 1.5F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.2F)).texOffs(0, 38).addBox(-1.0F, 0.25F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.3F)).texOffs(0, 42)
						.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.2F)),
				PartPose.offset(0.0F, 20.5F, 0.0F));
		PartDefinition Grip = partdefinition.addOrReplaceChild("Grip",
				CubeListBuilder.create().texOffs(0, 50).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.2F)).texOffs(0, 46).addBox(-1.0F, -4.75F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.3F)).texOffs(0, 50)
						.addBox(-1.0F, -3.5F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.2F)).texOffs(0, 46).addBox(-1.0F, -2.25F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.3F)).texOffs(0, 50)
						.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.2F)),
				PartPose.offset(0.0F, 12.5F, 0.0F));
		PartDefinition bone = partdefinition.addOrReplaceChild("bone",
				CubeListBuilder.create().texOffs(60, 0).addBox(-8.5F, -18.0F, 7.5F, 1.0F, 25.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(56, 0).addBox(-8.5F, -19.0F, 7.5F, 1.0F, 25.0F, 1.0F, new CubeDeformation(0.1F)),
				PartPose.offset(8.0F, 24.0F, -8.0F));
		PartDefinition cube_r5 = bone.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -0.48F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.35F)),
				PartPose.offsetAndRotation(-7.55F, -14.0F, 7.325F, 0.0F, 0.0F, -0.7854F));
		PartDefinition cube_r6 = bone.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -0.48F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.35F)),
				PartPose.offsetAndRotation(-8.45F, -14.0F, 7.325F, 0.0F, 0.0F, -0.7854F));
		PartDefinition cube_r7 = bone.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -0.48F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.35F)),
				PartPose.offsetAndRotation(-8.0F, -14.45F, 7.325F, 0.0F, 0.0F, -0.7854F));
		PartDefinition cube_r8 = bone.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -0.48F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.35F)),
				PartPose.offsetAndRotation(-8.0F, -13.55F, 7.325F, 0.0F, 0.0F, -0.7854F));
		PartDefinition cube_r9 = bone.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -0.48F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.35F)),
				PartPose.offsetAndRotation(-8.0F, -14.0F, 7.325F, 0.0F, 0.0F, -0.7854F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		IceSpikeBottom.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Ice_Spike2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Ice_Spike3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		IceSpike.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Gaurd.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Grip2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Grip.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
