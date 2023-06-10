// Made with Blockbench 4.6.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class ModelHyper_Flower<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "hyper_flower"), "main");
	private final ModelPart Lunar_Rose_model;

	public ModelHyper_Flower(ModelPart root) {
		this.Lunar_Rose_model = root.getChild("Lunar_Rose_model");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Lunar_Rose_model = partdefinition.addOrReplaceChild("Lunar_Rose_model", CubeListBuilder.create(),
				PartPose.offset(0.0F, -4.0F, 0.0F));

		PartDefinition Moon = Lunar_Rose_model.addOrReplaceChild("Moon",
				CubeListBuilder.create().texOffs(13, 12)
						.addBox(1.75F, -34.75F, 1.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(10, 13)
						.addBox(1.75F, -34.75F, -1.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(13, 5)
						.addBox(1.75F, -35.75F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(13, 3)
						.addBox(1.75F, -35.75F, 1.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 9)
						.addBox(1.74F, -32.75F, 0.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(11, 7)
						.addBox(1.74F, -33.25F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(10, 11)
						.addBox(1.74F, -33.25F, 1.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(3.75F, 2.75F, -31.5F, -1.309F, 0.0436F, -0.3491F));

		PartDefinition cube_r1 = Moon.addOrReplaceChild("cube_r1",
				CubeListBuilder.create().texOffs(16, 2)
						.addBox(1.75F, -34.5F, 0.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(14, 0)
						.addBox(1.75F, -32.25F, 0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(13, 14)
						.addBox(1.75F, -32.25F, -0.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 15)
						.addBox(1.75F, -33.25F, 1.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 15)
						.addBox(1.75F, -33.25F, -1.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(10, 4)
						.addBox(1.75F, -32.75F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(15, 7)
						.addBox(1.75F, -32.75F, 1.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 15)
						.addBox(1.75F, -33.75F, 1.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(15, 10)
						.addBox(1.75F, -33.75F, -1.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition Rose = Lunar_Rose_model.addOrReplaceChild("Rose",
				CubeListBuilder.create().texOffs(0, 0).addBox(4.5F, -34.0216F, -5.1481F, 1.0F, 3.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(15.0F, 25.75F, 3.0F, 0.0F, 0.2182F, -0.4363F));

		PartDefinition OuterPetals1 = Rose.addOrReplaceChild("OuterPetals1",
				CubeListBuilder.create().texOffs(5, 5)
						.addBox(5.5F, -31.5216F, -5.1481F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(4, 11)
						.addBox(5.5F, -34.0216F, -2.6481F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(11, 14)
						.addBox(5.5F, -34.2716F, -2.6481F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(7, 14)
						.addBox(5.5F, -34.5216F, -2.8981F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(5, 4)
						.addBox(5.5F, -34.5216F, -5.1481F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(14, 6)
						.addBox(5.5F, -34.5216F, -5.3981F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(3, 14)
						.addBox(5.5F, -34.2716F, -5.6481F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 5)
						.addBox(5.5F, -34.0216F, -5.6481F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(14, 1)
						.addBox(5.5F, -31.7716F, -5.6481F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(13, 10)
						.addBox(5.5F, -31.5216F, -5.3981F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 1)
						.addBox(5.5F, -31.5216F, -2.8981F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
						.addBox(5.5F, -31.7716F, -2.6481F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.25F, 0.0F, 0.0F));

		PartDefinition OuterPetals2 = Rose.addOrReplaceChild("OuterPetals2",
				CubeListBuilder.create().texOffs(0, 7)
						.addBox(5.5F, -31.5216F, -5.1481F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(6, 11)
						.addBox(5.5F, -34.0216F, -2.6481F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 16)
						.addBox(5.5F, -34.2716F, -2.6481F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(16, 11)
						.addBox(5.5F, -34.5216F, -3.1481F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 7)
						.addBox(5.5F, -34.5216F, -4.8981F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(14, 16)
						.addBox(5.5F, -34.5216F, -5.3981F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(16, 13)
						.addBox(5.5F, -34.2716F, -5.6481F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 11)
						.addBox(5.5F, -34.0216F, -5.6481F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(16, 15)
						.addBox(5.5F, -31.7716F, -5.6481F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(16, 16)
						.addBox(5.5F, -31.5216F, -5.3981F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 16)
						.addBox(5.5F, -31.5216F, -2.8981F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(10, 16)
						.addBox(5.5F, -31.7716F, -2.6481F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Petals = Rose.addOrReplaceChild("Petals",
				CubeListBuilder.create().texOffs(5, 0)
						.addBox(4.75F, -33.0216F, -4.1481F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 11)
						.addBox(5.0F, -34.0216F, -5.3981F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(10, 0)
						.addBox(5.0F, -34.0216F, -2.8981F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 6)
						.addBox(5.0F, -34.2716F, -5.1481F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(5, 3)
						.addBox(5.0F, -31.7716F, -5.1481F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(5, 3)
						.addBox(5.0F, -32.0216F, -3.1481F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(5, 3)
						.addBox(5.0F, -32.0216F, -5.1481F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(5, 3)
						.addBox(5.0F, -34.0216F, -3.1481F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(5, 3)
						.addBox(5.0F, -34.0216F, -5.1481F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Leaves = Rose.addOrReplaceChild("Leaves", CubeListBuilder.create(),
				PartPose.offset(5.0F, -31.0F, -3.75F));

		PartDefinition Cube_r2 = Leaves.addOrReplaceChild("Cube_r2",
				CubeListBuilder.create().texOffs(5, 0).addBox(0.0F, -1.0F, 1.0F, 0.0F, 1.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.25F, -1.75F, -0.75F, -2.2253F, 0.0F, 0.2182F));

		PartDefinition Cube_r3 = Leaves.addOrReplaceChild("Cube_r3",
				CubeListBuilder.create().texOffs(12, 15)
						.addBox(-0.01F, -2.25F, 0.5F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(15, 8)
						.addBox(-0.01F, -2.75F, 1.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.25F, -1.75F, -0.75F, -3.0107F, 0.0F, 0.2182F));

		PartDefinition Cube_r4 = Leaves.addOrReplaceChild("Cube_r4",
				CubeListBuilder.create().texOffs(8, 7).addBox(0.0F, -1.0F, 1.0F, 0.0F, 1.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.25F, -1.5F, -1.0F, -1.6144F, 0.0F, 0.3491F));

		PartDefinition Cube_r5 = Leaves.addOrReplaceChild("Cube_r5",
				CubeListBuilder.create().texOffs(0, 16)
						.addBox(-0.01F, -2.25F, 0.5F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(14, 15)
						.addBox(-0.01F, -2.75F, 1.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.25F, -1.5F, -1.0F, -2.3998F, 0.0F, 0.3491F));

		PartDefinition Cube_r6 = Leaves.addOrReplaceChild("Cube_r6",
				CubeListBuilder.create().texOffs(4, 16)
						.addBox(-0.01F, -2.75F, 1.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(2, 16)
						.addBox(-0.01F, -2.25F, 0.5F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, -1.5F, 1.25F, 1.0036F, 0.0F, -0.4363F));

		PartDefinition Cube_r7 = Leaves.addOrReplaceChild("Cube_r7",
				CubeListBuilder.create().texOffs(3, 9).addBox(0.0F, -1.0F, 1.0F, 0.0F, 1.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, -1.5F, 1.25F, 1.789F, 0.0F, -0.4363F));

		PartDefinition Cube_r8 = Leaves.addOrReplaceChild("Cube_r8",
				CubeListBuilder.create().texOffs(16, 4)
						.addBox(-0.01F, -2.25F, 0.5F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 16)
						.addBox(-0.01F, -2.75F, 1.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.25F, -0.5F, 0.0F, -1.0472F, 0.0F, 0.0873F));

		PartDefinition Cube_r9 = Leaves.addOrReplaceChild("Cube_r9",
				CubeListBuilder.create().texOffs(7, 9).addBox(0.0F, -1.0F, 1.0F, 0.0F, 1.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.25F, -0.5F, 0.0F, -0.2618F, 0.0F, 0.0873F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		Lunar_Rose_model.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
		this.Lunar_Rose_model.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.Lunar_Rose_model.xRot = headPitch / (180F / (float) Math.PI);
	}
}