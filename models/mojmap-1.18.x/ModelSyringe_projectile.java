// Made with Blockbench 4.7.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class ModelSyringe_projectile<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "syringe_projectile"), "main");
	private final ModelPart syringe;

	public ModelSyringe_projectile(ModelPart root) {
		this.syringe = root.getChild("syringe");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition syringe = partdefinition.addOrReplaceChild("syringe",
				CubeListBuilder.create().texOffs(0, 0)
						.addBox(-2.25F, -1.0F, -1.0F, 4.25F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(5, 4)
						.addBox(2.0F, -0.6F, -0.35F, 1.5F, 0.25F, 0.75F, new CubeDeformation(0.0F)).texOffs(0, 3)
						.addBox(-3.25F, -0.75F, -1.5F, 1.0F, 0.5F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		syringe.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}