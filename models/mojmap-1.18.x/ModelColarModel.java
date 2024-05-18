// Made with Blockbench 4.10.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class ModelColarModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "colarmodel"), "main");
	private final ModelPart Colar;
	private final ModelPart ColaPart;
	private final ModelPart Necklaces;
	private final ModelPart Necklace3d;

	public ModelColarModel(ModelPart root) {
		this.Colar = root.getChild("Colar");
		this.ColaPart = root.getChild("ColaPart");
		this.Necklaces = root.getChild("Necklaces");
		this.Necklace3d = root.getChild("Necklace3d");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Colar = partdefinition.addOrReplaceChild("Colar", CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition ColaPart = Colar.addOrReplaceChild("ColaPart", CubeListBuilder.create().texOffs(1, 2)
				.addBox(-4.5F, -24.7F, -3.0F, 9.0F, 2.0F, 6.0F, new CubeDeformation(-0.05F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Necklaces = Colar.addOrReplaceChild("Necklaces", CubeListBuilder.create(),
				PartPose.offset(0.0F, -24.5F, 0.0F));

		PartDefinition Necklace3d = Necklaces.addOrReplaceChild("Necklace3d",
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

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		Colar.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
	}
}