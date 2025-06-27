// Made with Blockbench 4.10.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class ModelAccessories<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			ResourceLocation.parse("modid", "accessories"), "main");
	private final ModelPart Colar;
	private final ModelPart Necklace;
	private final ModelPart Bracelets;
	private final ModelPart RightArmBracelet;
	private final ModelPart LeftArmBracelet;

	public ModelAccessories(ModelPart root) {
		this.Colar = root.getChild("Colar");
		this.Necklace = Colar.getChild("Necklace");
		this.Bracelets = root.getChild("Bracelets");
		this.RightArmBracelet = Bracelets.getChild("RightArmBracelet");
		this.LeftArmBracelet = Bracelets.getChild("LeftArmBracelet");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Colar = partdefinition.addOrReplaceChild("Colar", CubeListBuilder.create().texOffs(1, 2).addBox(
				-4.5F, -0.2F, -3.0F, 9.0F, 2.0F, 6.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, -0.5F, 0.0F));

		PartDefinition Necklace = Colar.addOrReplaceChild("Necklace",
				CubeListBuilder.create().texOffs(0, 23)
						.addBox(-3.25F, -1.95F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(0, 23)
						.addBox(-3.25F, -1.45F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(0, 23)
						.addBox(2.25F, -1.95F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(0, 23)
						.addBox(2.25F, -1.45F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(0, 25)
						.addBox(1.75F, -0.95F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(0, 25)
						.addBox(1.75F, -0.45F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(0, 25)
						.addBox(-2.75F, -0.95F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(0, 25)
						.addBox(-2.75F, -0.45F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(0, 27)
						.addBox(-2.25F, 0.05F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(0, 27)
						.addBox(1.25F, 0.05F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(0, 29)
						.addBox(0.75F, 0.55F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(0, 29)
						.addBox(0.5F, 0.55F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(8, 27)
						.addBox(-1.0F, 0.05F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(8, 27)
						.addBox(-1.0F, 1.05F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(8, 29)
						.addBox(-0.5F, 0.05F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(8, 29)
						.addBox(-0.5F, 1.05F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(8, 27)
						.addBox(0.0F, 0.05F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(8, 27)
						.addBox(0.0F, 1.05F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(0, 29)
						.addBox(-1.75F, 0.55F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(0, 29)
						.addBox(-1.5F, 0.55F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(8, 29)
						.addBox(-1.0F, 0.55F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(8, 29)
						.addBox(0.0F, 0.55F, -0.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(4, 27)
						.addBox(-0.75F, 0.3F, -0.975F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(4, 29)
						.addBox(-0.25F, 0.3F, -0.975F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(4, 25)
						.addBox(-0.25F, 0.8F, -0.975F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(4, 29)
						.addBox(-0.75F, 0.8F, -0.975F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(8, 27)
						.addBox(-0.75F, 0.3F, -0.7F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(8, 27)
						.addBox(-0.75F, 0.8F, -0.7F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(8, 29)
						.addBox(-0.475F, 0.525F, -0.675F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(8, 27)
						.addBox(-0.25F, 0.8F, -0.7F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).texOffs(8, 27)
						.addBox(-0.25F, 0.3F, -0.7F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)),
				PartPose.offset(0.0F, 1.75F, -2.6F));

		PartDefinition Bracelets = partdefinition.addOrReplaceChild("Bracelets", CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition RightArmBracelet = Bracelets.addOrReplaceChild("RightArmBracelet",
				CubeListBuilder.create().texOffs(16, 10)
						.addBox(-3.0312F, 6.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.08F)).texOffs(12, 27)
						.addBox(-3.4562F, 6.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.08F)).texOffs(18, 29)
						.addBox(-3.6062F, 6.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.08F)),
				PartPose.offset(-4.9688F, 2.0F, 0.0F));

		PartDefinition LeftArmBracelet = Bracelets.addOrReplaceChild("LeftArmBracelet",
				CubeListBuilder.create().texOffs(18, 27)
						.addBox(2.4063F, 6.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.08F)).texOffs(12, 23)
						.addBox(2.4563F, 6.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.08F)).texOffs(18, 29)
						.mirror().addBox(2.6062F, 6.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.08F))
						.mirror(false),
				PartPose.offset(4.9688F, 2.0F, 0.0F));

		PartDefinition LeftArmBracelet_r1 = LeftArmBracelet.addOrReplaceChild("LeftArmBracelet_r1",
				CubeListBuilder.create().texOffs(0, 10).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F,
						new CubeDeformation(0.08F)),
				PartPose.offsetAndRotation(1.0313F, 7.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		Colar.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Bracelets.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
	}
}