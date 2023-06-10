// Made with Blockbench 4.6.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class ModelProtoTypeMob<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "prototypemob"), "main");
	private final ModelPart RightLeg;
	private final ModelPart LeftLeg;
	private final ModelPart Head;
	private final ModelPart Torso;
	private final ModelPart RightArm;
	private final ModelPart LeftArm;

	public ModelProtoTypeMob(ModelPart root) {
		this.RightLeg = root.getChild("RightLeg");
		this.LeftLeg = root.getChild("LeftLeg");
		this.Head = root.getChild("Head");
		this.Torso = root.getChild("Torso");
		this.RightArm = root.getChild("RightArm");
		this.LeftArm = root.getChild("LeftArm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(14, 51)
				.addBox(-2.0F, 12.0F, -1.75F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-2.5F, 10.0F, 0.0F));

		PartDefinition RightLowerLeg_r1 = RightLeg.addOrReplaceChild("RightLowerLeg_r1",
				CubeListBuilder.create().texOffs(50, 0).addBox(-2.0F, -7.5F, -1.0F, 4.0F, 6.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 14.25F, -1.25F, -0.5236F, 0.0F, 0.0F));

		PartDefinition RightMidLeg_r1 = RightLeg.addOrReplaceChild("RightMidLeg_r1",
				CubeListBuilder.create().texOffs(40, 11).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.25F, -1.5F, 0.5672F, 0.0F, 0.0F));

		PartDefinition RightUpperLeg_r1 = RightLeg.addOrReplaceChild(
				"RightUpperLeg_r1", CubeListBuilder.create().texOffs(16, 41).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F,
						4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(50, 32)
				.addBox(-2.0F, 12.0F, -1.75F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offset(2.5F, 10.0F, 0.0F));

		PartDefinition LeftLowerLeg_r1 = LeftLeg.addOrReplaceChild("LeftLowerLeg_r1",
				CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, -7.5F, -1.0F, 4.0F, 6.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 14.25F, -1.25F, -0.5236F, 0.0F, 0.0F));

		PartDefinition LeftMidLeg_r1 = LeftLeg.addOrReplaceChild("LeftMidLeg_r1",
				CubeListBuilder.create().texOffs(34, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.25F, -1.5F, 0.5672F, 0.0F, 0.0F));

		PartDefinition LeftUpperLeg_r1 = LeftLeg
				.addOrReplaceChild("LeftUpperLeg_r1",
						CubeListBuilder.create().texOffs(40, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition Head = partdefinition.addOrReplaceChild("Head",
				CubeListBuilder.create().texOffs(0, 0)
						.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(24, 0)
						.addBox(-2.0F, -3.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(32, 11)
						.addBox(-1.5F, -1.0F, -5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition Snout_r1 = Head
				.addOrReplaceChild("Snout_r1",
						CubeListBuilder.create().texOffs(4, 0).addBox(-1.0F, -29.625F, -0.95F, 1.0F, 1.0F, 1.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.5F, 26.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

		PartDefinition Hair = Head.addOrReplaceChild("Hair", CubeListBuilder.create(),
				PartPose.offset(0.52F, -8.5F, -4.06F));

		PartDefinition Hair_r1 = Hair.addOrReplaceChild("Hair_r1",
				CubeListBuilder.create().texOffs(52, 9).addBox(-0.3877F, -1.4412F, -0.94F, 2.0F, 2.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 3.0324F, -0.3277F, 2.1787F));

		PartDefinition Hair_r2 = Hair.addOrReplaceChild("Hair_r2",
				CubeListBuilder.create().texOffs(0, 57).addBox(0.7914F, -1.8676F, -0.88F, 1.0F, 3.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.8742F, -0.2202F, 0.9528F));

		PartDefinition Hair_r3 = Hair
				.addOrReplaceChild("Hair_r3",
						CubeListBuilder.create().texOffs(0, 57).addBox(-0.2086F, -1.8676F, -1.88F, 3.0F, 4.0F, 3.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(2.0F, 0.0F, 5.0F, 0.0F, 0.0F, 0.0436F));

		PartDefinition Hair_r4 = Hair
				.addOrReplaceChild("Hair_r4",
						CubeListBuilder.create().texOffs(52, 9).addBox(-1.3877F, -1.4412F, -1.94F, 4.0F, 3.0F, 3.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(2.0F, 0.0F, 5.0F, 0.0F, 0.0F, -1.1345F));

		PartDefinition Hair_r5 = Hair.addOrReplaceChild("Hair_r5",
				CubeListBuilder.create().texOffs(12, 57).addBox(2.64F, -11.64F, -1.88F, 3.0F, 4.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-8.04F, 10.0F, 5.0F, 0.0F, 0.0F, -0.0436F));

		PartDefinition Hair_r6 = Hair.addOrReplaceChild("Hair_r6",
				CubeListBuilder.create().texOffs(53, 38).addBox(-8.0F, -10.0F, -2.0F, 4.0F, 3.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-8.52F, 8.5F, 5.06F, 0.0F, 0.0F, 1.1345F));

		PartDefinition Hair_r7 = Hair.addOrReplaceChild("Hair_r7",
				CubeListBuilder.create().texOffs(52, 18).addBox(-2.0F, -7.0F, -3.0F, 3.0F, 3.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.52F, 9.5F, 5.06F, 0.0F, 0.0F, -0.6109F));

		PartDefinition Hair_r8 = Hair.addOrReplaceChild("Hair_r8",
				CubeListBuilder.create().texOffs(52, 57).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 3.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.52F, 9.5F, 5.06F, 0.0F, 0.0F, -1.0036F));

		PartDefinition Hair_r9 = Hair.addOrReplaceChild("Hair_r9",
				CubeListBuilder.create().texOffs(24, 60).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 3.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.52F, 9.5F, 5.06F, 0.0F, 0.0F, 1.0036F));

		PartDefinition Hair_r10 = Hair.addOrReplaceChild("Hair_r10",
				CubeListBuilder.create().texOffs(52, 50).addBox(-1.0F, -7.0F, -3.0F, 3.0F, 3.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.52F, 9.5F, 5.06F, 0.0F, 0.0F, 0.6109F));

		PartDefinition Torso = partdefinition.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(0, 16)
				.addBox(-4.0F, -25.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Tail = Torso.addOrReplaceChild("Tail", CubeListBuilder.create(),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Base_r1 = Tail.addOrReplaceChild("Base_r1",
				CubeListBuilder.create().texOffs(16, 32).addBox(-2.5F, 5.1914F, -4.4483F, 5.0F, 5.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -12.0F, 6.0F, 1.8326F, 0.0F, 0.0F));

		PartDefinition Base_r2 = Tail.addOrReplaceChild("Base_r2",
				CubeListBuilder.create().texOffs(32, 0).addBox(-2.5F, 0.1914F, -2.4483F, 5.0F, 7.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -12.0F, 6.0F, 1.4835F, 0.0F, 0.0F));

		PartDefinition Base_r3 = Tail.addOrReplaceChild("Base_r3",
				CubeListBuilder.create().texOffs(32, 43).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 8.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -14.0F, 0.0F, 1.1751F, 0.1209F, -0.0503F));

		PartDefinition Base_r4 = Tail.addOrReplaceChild("Base_r4",
				CubeListBuilder.create().texOffs(44, 43).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 8.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -14.0F, 0.0F, 1.1751F, -0.1209F, 0.0503F));

		PartDefinition Fur = Torso.addOrReplaceChild("Fur", CubeListBuilder.create(),
				PartPose.offset(0.52F, -33.5F, -4.06F));

		PartDefinition Fur_r1 = Fur.addOrReplaceChild("Fur_r1",
				CubeListBuilder.create().texOffs(41, 54).addBox(0.0F, -2.0F, -3.0F, 4.0F, 3.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.52F, 11.5F, 5.06F, 2.6285F, 0.3463F, -2.6761F));

		PartDefinition Fur_r2 = Fur.addOrReplaceChild("Fur_r2",
				CubeListBuilder.create().texOffs(44, 60).addBox(-2.0F, -6.0F, -1.0F, 2.0F, 2.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.52F, 11.5F, 6.06F, 2.8965F, 0.1841F, -2.5098F));

		PartDefinition Fur_r3 = Fur.addOrReplaceChild("Fur_r3",
				CubeListBuilder.create().texOffs(0, 4).addBox(-1.0F, -5.0F, -1.0F, 1.0F, 2.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.52F, 11.5F, 6.06F, 2.9852F, 0.2635F, -2.1152F));

		PartDefinition Fur_r4 = Fur.addOrReplaceChild("Fur_r4",
				CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -5.0F, -1.0F, 1.0F, 2.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.52F, 11.5F, 6.06F, 2.9852F, -0.2635F, 2.1152F));

		PartDefinition Fur_r5 = Fur.addOrReplaceChild("Fur_r5",
				CubeListBuilder.create().texOffs(34, 60).addBox(0.0F, -6.0F, -1.0F, 2.0F, 2.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.52F, 11.5F, 6.06F, 2.8965F, -0.1841F, 2.5098F));

		PartDefinition Fur_r6 = Fur.addOrReplaceChild("Fur_r6",
				CubeListBuilder.create().texOffs(27, 54).addBox(-4.0F, -2.0F, -3.0F, 4.0F, 3.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.52F, 11.5F, 5.06F, 2.6285F, -0.3463F, 2.6761F));

		PartDefinition Fur_r7 = Fur.addOrReplaceChild("Fur_r7",
				CubeListBuilder.create().texOffs(56, 25).addBox(-4.0F, -2.0F, 0.0F, 4.0F, 3.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.52F, 11.5F, 3.06F, -2.6285F, 0.3463F, 2.6761F));

		PartDefinition Fur_r8 = Fur.addOrReplaceChild("Fur_r8",
				CubeListBuilder.create().texOffs(56, 44).addBox(0.0F, -2.0F, 0.0F, 4.0F, 3.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.52F, 11.5F, 3.06F, -2.6285F, -0.3463F, -2.6761F));

		PartDefinition Fur_r9 = Fur.addOrReplaceChild("Fur_r9",
				CubeListBuilder.create().texOffs(59, 60).addBox(-2.0F, -6.0F, -2.0F, 2.0F, 2.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.52F, 11.5F, 2.06F, -2.8965F, -0.1841F, -2.5098F));

		PartDefinition Fur_r10 = Fur.addOrReplaceChild("Fur_r10",
				CubeListBuilder.create().texOffs(20, 16).addBox(-1.0F, -5.0F, -1.0F, 1.0F, 2.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.52F, 11.5F, 2.06F, -2.9852F, -0.2635F, -2.1152F));

		PartDefinition Fur_r11 = Fur.addOrReplaceChild("Fur_r11",
				CubeListBuilder.create().texOffs(24, 4).addBox(0.0F, -5.0F, -1.0F, 1.0F, 2.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.52F, 11.5F, 2.06F, -2.9852F, 0.2635F, 2.1152F));

		PartDefinition Fur_r12 = Fur.addOrReplaceChild("Fur_r12",
				CubeListBuilder.create().texOffs(62, 15).addBox(0.0F, -6.0F, -2.0F, 2.0F, 2.0F, 3.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.52F, 11.5F, 2.06F, -2.8965F, 0.1841F, 2.5098F));

		PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm",
				CubeListBuilder.create().texOffs(0, 32)
						.addBox(-4.0F, 0.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(35, 14)
						.addBox(-1.0F, 10.75F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 34)
						.addBox(-4.0F, 10.75F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(16, 34)
						.addBox(-4.0F, 10.75F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 34)
						.addBox(-4.0F, 10.75F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-4.0F, -1.0F, 0.0F));

		PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm",
				CubeListBuilder.create().texOffs(24, 16)
						.addBox(0.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(40, 11)
						.addBox(0.0F, 10.75F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(38, 13)
						.addBox(3.0F, 10.75F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(36, 18)
						.addBox(3.0F, 10.75F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(36, 16)
						.addBox(3.0F, 10.75F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(4.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		RightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		LeftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Torso.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		RightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		LeftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
		this.LeftLeg.xRot = Mth.cos(limbSwing * 1.0F) * -1.0F * limbSwingAmount;
		this.RightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
		this.Head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.Head.xRot = headPitch / (180F / (float) Math.PI);
		this.RightLeg.xRot = Mth.cos(limbSwing * 1.0F) * 1.0F * limbSwingAmount;
		this.LeftArm.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
	}
}