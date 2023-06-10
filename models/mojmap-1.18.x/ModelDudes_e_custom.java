// Made with Blockbench 4.6.0
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class ModelDudes_e_custom<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "dudes_e_custom"), "main");
	private final ModelPart RightLeg;
	private final ModelPart LeftLeg;
	private final ModelPart Head;
	private final ModelPart Torso;
	private final ModelPart RightArm;
	private final ModelPart LeftArm;
	private final ModelPart LeftWing;
	private final ModelPart RightWing;
	private final ModelPart bone;

	public ModelDudes_e_custom(ModelPart root) {
		this.RightLeg = root.getChild("RightLeg");
		this.LeftLeg = root.getChild("LeftLeg");
		this.Head = root.getChild("Head");
		this.Torso = root.getChild("Torso");
		this.RightArm = root.getChild("RightArm");
		this.LeftArm = root.getChild("LeftArm");
		this.LeftWing = root.getChild("LeftWing");
		this.RightWing = root.getChild("RightWing");
		this.bone = root.getChild("bone");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create(),
				PartPose.offset(-2.75F, 10.0F, 0.0F));

		PartDefinition Toe_r1 = RightLeg.addOrReplaceChild("Toe_r1",
				CubeListBuilder.create().texOffs(32, 34)
						.addBox(-4.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.02F)).texOffs(40, 20)
						.addBox(-2.5F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.02F)).texOffs(40, 22)
						.addBox(-1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.02F)),
				PartPose.offsetAndRotation(2.0F, 13.0F, -3.25F, -0.6109F, 0.0F, 0.0F));

		PartDefinition Toe_r2 = RightLeg.addOrReplaceChild("Toe_r2",
				CubeListBuilder.create().texOffs(16, 43)
						.addBox(-1.0F, -3.0F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.01F)).texOffs(40, 30)
						.addBox(-2.5F, -3.0F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.01F)).texOffs(28, 43)
						.addBox(-4.0F, -3.0F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.01F)),
				PartPose.offsetAndRotation(2.0F, 16.0F, -1.75F, 0.0F, 0.0F, 0.0F));

		PartDefinition RightFoot_r1 = RightLeg
				.addOrReplaceChild("RightFoot_r1",
						CubeListBuilder.create().texOffs(48, 53).addBox(-4.0F, -4.0F, -2.0F, 4.0F, 2.0F, 4.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(2.0F, 16.0F, -0.75F, 0.0F, 0.0F, 0.0F));

		PartDefinition RightLowerLeg_r1 = RightLeg.addOrReplaceChild("RightLowerLeg_r1",
				CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, -7.5F, -1.0F, 4.0F, 6.0F, 3.0F,
						new CubeDeformation(0.01F)),
				PartPose.offsetAndRotation(0.0F, 14.25F, -1.25F, -0.5236F, 0.0F, 0.0F));

		PartDefinition RightMidLeg_r1 = RightLeg.addOrReplaceChild("RightMidLeg_r1",
				CubeListBuilder.create().texOffs(16, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.25F, -1.5F, 0.5672F, 0.0F, 0.0F));

		PartDefinition RightUpperLeg_r1 = RightLeg.addOrReplaceChild("RightUpperLeg_r1",
				CubeListBuilder.create().texOffs(40, 20).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F,
						new CubeDeformation(0.01F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create(),
				PartPose.offset(2.75F, 10.0F, 0.0F));

		PartDefinition Toe_r3 = LeftLeg.addOrReplaceChild("Toe_r3",
				CubeListBuilder.create().texOffs(28, 32)
						.addBox(-2.5F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.02F)).texOffs(32, 32)
						.addBox(-1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.02F)).texOffs(28, 34)
						.addBox(-4.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.02F)),
				PartPose.offsetAndRotation(2.0F, 13.0F, -3.25F, -0.6109F, 0.0F, 0.0F));

		PartDefinition Toe_r4 = LeftLeg.addOrReplaceChild("Toe_r4",
				CubeListBuilder.create().texOffs(0, 34)
						.addBox(-1.0F, -3.0F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.01F)).texOffs(12, 34)
						.addBox(-2.5F, -3.0F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.01F)).texOffs(16, 34)
						.addBox(-4.0F, -3.0F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.01F)),
				PartPose.offsetAndRotation(2.0F, 16.0F, -1.75F, 0.0F, 0.0F, 0.0F));

		PartDefinition LeftFoot_r1 = LeftLeg
				.addOrReplaceChild("LeftFoot_r1",
						CubeListBuilder.create().texOffs(48, 47).addBox(-4.0F, -4.0F, -2.0F, 4.0F, 2.0F, 4.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(2.0F, 16.0F, -0.75F, 0.0F, 0.0F, 0.0F));

		PartDefinition LeftLowerLeg_r1 = LeftLeg.addOrReplaceChild("LeftLowerLeg_r1",
				CubeListBuilder.create().texOffs(48, 34).addBox(-2.0F, -7.5F, -1.0F, 4.0F, 6.0F, 3.0F,
						new CubeDeformation(0.01F)),
				PartPose.offsetAndRotation(0.0F, 14.25F, -1.25F, -0.5236F, 0.0F, 0.0F));

		PartDefinition LeftMidLeg_r1 = LeftLeg.addOrReplaceChild("LeftMidLeg_r1",
				CubeListBuilder.create().texOffs(32, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.25F, -1.5F, 0.5672F, 0.0F, 0.0F));

		PartDefinition LeftUpperLeg_r1 = LeftLeg.addOrReplaceChild("LeftUpperLeg_r1",
				CubeListBuilder.create().texOffs(16, 43).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F,
						new CubeDeformation(0.01F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create(),
				PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition Snout_r1 = Head.addOrReplaceChild("Snout_r1",
				CubeListBuilder.create().texOffs(17, 65).addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-1.5F, -2.0F, -7.0F, 0.0F, -0.2182F, 0.0F));

		PartDefinition Snout_r2 = Head.addOrReplaceChild("Snout_r2",
				CubeListBuilder.create().texOffs(66, 0).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 2.0F, 4.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(1.5F, -2.0F, -7.0F, 0.0F, 0.2182F, 0.0F));

		PartDefinition Snout_r3 = Head.addOrReplaceChild("Snout_r3",
				CubeListBuilder.create().texOffs(70, 11)
						.addBox(-1.5F, -27.0F, -6.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(9, 63)
						.addBox(-1.5F, -29.0F, -7.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.01F)),
				PartPose.offsetAndRotation(0.0F, 27.0F, 0.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition Head_r1 = Head
				.addOrReplaceChild("Head_r1",
						CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition Fins = Head.addOrReplaceChild("Fins", CubeListBuilder.create(),
				PartPose.offset(0.0F, 27.0F, 0.0F));

		PartDefinition HeadFin_r1 = Fins.addOrReplaceChild("HeadFin_r1",
				CubeListBuilder.create().texOffs(66, 30).addBox(-2.1808F, -1.0F, -0.5736F, 3.0F, 2.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(3.5F, -27.75F, -2.0F, -0.5236F, 0.9599F, -3.1416F));

		PartDefinition HeadFin_r2 = Fins.addOrReplaceChild("HeadFin_r2",
				CubeListBuilder.create().texOffs(68, 48).addBox(-2.1808F, -1.0F, -0.5736F, 3.0F, 2.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-3.5F, -27.75F, -2.0F, 0.5236F, 0.9599F, 0.0F));

		PartDefinition HeadFin_r3 = Fins.addOrReplaceChild("HeadFin_r3",
				CubeListBuilder.create().texOffs(56, 12).addBox(-0.25F, -2.0F, 0.0F, 6.0F, 3.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-3.0F, -33.25F, 0.0F, -1.0263F, -0.733F, -2.1817F));

		PartDefinition HeadFin_r4 = Fins.addOrReplaceChild("HeadFin_r4",
				CubeListBuilder.create().texOffs(56, 23).addBox(-0.25F, -1.0F, 0.0F, 6.0F, 3.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(3.0F, -33.25F, 0.0F, 1.0263F, -0.733F, -0.9599F));

		PartDefinition Hair = Head.addOrReplaceChild("Hair",
				CubeListBuilder.create().texOffs(44, 30)
						.addBox(-3.5F, -34.5F, -3.5F, 7.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(62, 43)
						.addBox(2.0F, -35.0F, 0.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(58, 17)
						.addBox(-3.5F, -33.25F, -4.25F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(20, 16)
						.addBox(3.5F, -34.0F, -4.45F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 16)
						.addBox(-4.5F, -34.0F, -4.45F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(60, 49)
						.addBox(-4.0F, -35.0F, 0.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(56, 28)
						.addBox(-4.0F, -34.25F, -4.5F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 0)
						.addBox(-4.0F, -34.0F, 4.0F, 8.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 27.0F, 0.0F));

		PartDefinition HairBase_r1 = Hair.addOrReplaceChild("HairBase_r1",
				CubeListBuilder.create().texOffs(32, 12).addBox(-8.8F, -2.0F, -1.75F, 9.0F, 2.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(4.0F, -31.0F, -4.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition HairBase_r2 = Hair.addOrReplaceChild("HairBase_r2",
				CubeListBuilder.create().texOffs(24, 0)
						.addBox(-0.5F, -4.75F, -2.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(61, 67)
						.addBox(6.5F, -4.75F, -2.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-4.0F, -29.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition HairBase_r3 = Hair.addOrReplaceChild("HairBase_r3",
				CubeListBuilder.create().texOffs(36, 16).addBox(-0.2F, -2.0F, -1.75F, 9.0F, 2.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-4.0F, -31.0F, -4.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition HairBase_r4 = Hair.addOrReplaceChild("HairBase_r4",
				CubeListBuilder.create().texOffs(48, 7).addBox(-4.0F, -2.0F, 8.0F, 8.0F, 4.0F, 1.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -26.0F, 0.0F, 0.0F, -1.5708F, 1.5708F));

		PartDefinition Horns = Head.addOrReplaceChild("Horns", CubeListBuilder.create(),
				PartPose.offset(0.0F, 26.0F, 0.0F));

		PartDefinition righthorn_r1 = Horns.addOrReplaceChild("righthorn_r1",
				CubeListBuilder.create().texOffs(10, 68)
						.addBox(-3.0F, -31.25F, -20.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(30, 0)
						.addBox(-3.0F, -32.25F, -19.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, -4.0F, -0.6545F, -0.1745F, 0.0F));

		PartDefinition lefthorn_r1 = Horns.addOrReplaceChild("lefthorn_r1",
				CubeListBuilder.create().texOffs(0, 32)
						.addBox(2.0F, -32.25F, -19.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(69, 67)
						.addBox(2.0F, -31.25F, -20.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, -4.0F, -0.6545F, 0.1745F, 0.0F));

		PartDefinition Torso = partdefinition.addOrReplaceChild("Torso",
				CubeListBuilder.create().texOffs(18, 53)
						.addBox(-3.0F, 1.0F, 1.75F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(52, 20)
						.addBox(-4.0F, 1.0F, 1.5F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(58, 59)
						.addBox(-3.0F, 1.0F, -2.75F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 43)
						.addBox(-4.0F, 1.0F, -2.5F, 8.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(64, 56)
						.addBox(-3.0F, 3.0F, 1.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(60, 54)
						.addBox(-3.0F, 4.0F, -2.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(70, 42)
						.addBox(-2.0F, 3.0F, -2.75F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition lefttuft_r1 = Torso
				.addOrReplaceChild("lefttuft_r1",
						CubeListBuilder.create().texOffs(0, 58).addBox(-11.5F, -23.5F, -2.5F, 1.0F, 2.0F, 5.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 26.0F, 0.0F, 0.0F, 0.0F, 0.6109F));

		PartDefinition righttuft_r1 = Torso.addOrReplaceChild("righttuft_r1",
				CubeListBuilder.create().texOffs(59, 30).addBox(10.5F, -23.5F, -2.5F, 1.0F, 2.0F, 5.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 26.0F, 0.0F, 0.0F, 0.0F, -0.6109F));

		PartDefinition BackFin_r1 = Torso
				.addOrReplaceChild("BackFin_r1",
						CubeListBuilder.create().texOffs(10, 53).addBox(-1.0F, 0.0F, -4.0F, 2.0F, 6.0F, 4.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 1.75F, 2.0F, 0.5236F, 0.0F, 0.0F));

		PartDefinition Torso_r1 = Torso
				.addOrReplaceChild("Torso_r1",
						CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -26.0F, -2.0F, 8.0F, 12.0F, 4.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 27.0F, 0.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition Tail = Torso.addOrReplaceChild("Tail", CubeListBuilder.create(),
				PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition TailFin_r1 = Tail
				.addOrReplaceChild("TailFin_r1",
						CubeListBuilder.create().texOffs(0, 65).addBox(0.0F, 0.0F, -0.75F, 1.0F, 8.0F, 2.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(-0.5F, 1.75F, 2.0F, 1.789F, 0.0F, 0.0F));

		PartDefinition Base_r1 = Tail.addOrReplaceChild("Base_r1",
				CubeListBuilder.create().texOffs(16, 32)
						.addBox(-0.5F, 5.3462F, -1.8296F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)).texOffs(45, 59)
						.addBox(-0.5F, -2.5538F, -1.8296F, 1.0F, 8.0F, 3.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(0.0F, 5.25F, 17.0F, 1.1345F, 0.0F, 0.0F));

		PartDefinition Base_r2 = Tail.addOrReplaceChild("Base_r2",
				CubeListBuilder.create().texOffs(70, 19)
						.addBox(-0.5F, -8.1668F, -2.1179F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(48, 70)
						.addBox(-0.5F, -6.1668F, 0.8821F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(53, 59)
						.addBox(-0.5F, -6.1668F, -2.1179F, 1.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.25F, 17.0F, -0.8727F, 0.0F, 0.0F));

		PartDefinition Base_r3 = Tail
				.addOrReplaceChild("Base_r3",
						CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -0.3449F, -0.7203F, 2.0F, 5.0F, 2.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 5.0F, 13.0F, 1.4835F, 0.0F, 0.0F));

		PartDefinition Base_r4 = Tail
				.addOrReplaceChild("Base_r4",
						CubeListBuilder.create().texOffs(22, 57).addBox(-1.5F, -1.3563F, -0.6088F, 3.0F, 5.0F, 3.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 4.5F, 9.25F, 1.309F, 0.0F, 0.0F));

		PartDefinition Base_r5 = Tail
				.addOrReplaceChild("Base_r5",
						CubeListBuilder.create().texOffs(66, 6).addBox(-1.0F, -1.075F, -0.625F, 2.0F, 2.0F, 3.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.9599F, 0.0F, 0.0F));

		PartDefinition Base_r6 = Tail
				.addOrReplaceChild("Base_r6",
						CubeListBuilder.create().texOffs(32, 0).addBox(-2.0F, 0.75F, -0.8F, 4.0F, 8.0F, 4.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 1.1781F, 0.0F, 0.0F));

		PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create(),
				PartPose.offset(-5.0F, 0.0F, 0.0F));

		PartDefinition Finger_r1 = RightArm.addOrReplaceChild("Finger_r1",
				CubeListBuilder.create().texOffs(44, 0)
						.addBox(3.0F, 9.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(16, 45)
						.addBox(6.0F, 9.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-6.0F, 1.75F, -3.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition Finger_r2 = RightArm
				.addOrReplaceChild("Finger_r2",
						CubeListBuilder.create().texOffs(44, 2).addBox(3.0F, 9.0F, 1.0F, 1.0F, 1.0F, 1.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(-6.0F, 1.75F, -1.5F, 0.0F, 0.0F, 0.0F));

		PartDefinition Finger_r3 = RightArm
				.addOrReplaceChild("Finger_r3",
						CubeListBuilder.create().texOffs(44, 34).addBox(3.0F, 9.0F, 1.0F, 1.0F, 1.0F, 1.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(-6.0F, 1.75F, 0.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition Spike_r1 = RightArm.addOrReplaceChild("Spike_r1",
				CubeListBuilder.create().texOffs(23, 18)
						.addBox(-0.5F, -3.5F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(42, 70)
						.addBox(-0.5F, -2.5F, -2.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-2.0789F, 4.8746F, 1.1151F, -2.6425F, 0.8346F, 3.1091F));

		PartDefinition RightArm_r1 = RightArm
				.addOrReplaceChild("RightArm_r1",
						CubeListBuilder.create().texOffs(24, 16).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create(),
				PartPose.offset(5.0F, 0.0F, 0.0F));

		PartDefinition Finger_r4 = LeftArm.addOrReplaceChild("Finger_r4",
				CubeListBuilder.create().texOffs(28, 45)
						.addBox(3.0F, 9.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(11, 48)
						.addBox(6.0F, 9.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-4.0F, 1.75F, -3.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition Finger_r5 = LeftArm
				.addOrReplaceChild("Finger_r5",
						CubeListBuilder.create().texOffs(48, 47).addBox(3.0F, 9.0F, 1.0F, 1.0F, 1.0F, 1.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(-1.0F, 1.75F, -1.5F, 0.0F, 0.0F, 0.0F));

		PartDefinition Finger_r6 = LeftArm
				.addOrReplaceChild("Finger_r6",
						CubeListBuilder.create().texOffs(48, 49).addBox(3.0F, 9.0F, 1.0F, 1.0F, 1.0F, 1.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(-1.0F, 1.75F, 0.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition Spike_r2 = LeftArm.addOrReplaceChild("Spike_r2",
				CubeListBuilder.create().texOffs(24, 16)
						.addBox(0.7259F, -2.4212F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(52, 70)
						.addBox(-0.2741F, -1.4212F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(2.6568F, 4.0711F, 1.6568F, -0.4796F, -0.6979F, 0.7102F));

		PartDefinition LeftArm_r1 = LeftArm
				.addOrReplaceChild("LeftArm_r1",
						CubeListBuilder.create().texOffs(0, 32).addBox(0.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(-1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition LeftWing = partdefinition.addOrReplaceChild("LeftWing", CubeListBuilder.create(),
				PartPose.offset(1.0F, 24.0F, 0.0F));

		PartDefinition wingbase_r1 = LeftWing.addOrReplaceChild("wingbase_r1",
				CubeListBuilder.create().texOffs(32, 2)
						.addBox(5.0F, -19.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(62, 37)
						.addBox(1.0F, -23.0F, 0.0F, 5.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(6, 67)
						.addBox(6.0F, -24.0F, 0.0F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(34, 60)
						.addBox(7.0F, -25.0F, 0.0F, 1.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(32, 43)
						.addBox(8.0F, -26.0F, 0.0F, 3.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.6545F, 0.0F));

		PartDefinition RightWing = partdefinition.addOrReplaceChild("RightWing", CubeListBuilder.create(),
				PartPose.offset(-1.0F, 24.0F, 0.0F));

		PartDefinition wingbase_r2 = RightWing.addOrReplaceChild("wingbase_r2",
				CubeListBuilder.create().texOffs(27, 65)
						.addBox(-7.0F, -24.0F, 0.0F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(38, 60)
						.addBox(-8.0F, -25.0F, 0.0F, 1.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 32)
						.addBox(-6.0F, -19.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(61, 62)
						.addBox(-6.0F, -23.0F, 0.0F, 5.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(40, 43)
						.addBox(-11.0F, -26.0F, 0.0F, 3.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.6545F, 0.0F));

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

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
		LeftWing.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		RightWing.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
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