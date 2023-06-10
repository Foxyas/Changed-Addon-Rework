package net.foxyas.changedaddon.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
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

// Made with Blockbench 4.6.0
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
public class Modelcustom_model<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("changed_addon", "modelcustom_model"), "main");
	public final ModelPart RightLeg;
	public final ModelPart LeftLeg;
	public final ModelPart Head;
	public final ModelPart Body;
	public final ModelPart RightArm;
	public final ModelPart LeftArm;

	public Modelcustom_model(ModelPart root) {
		this.RightLeg = root.getChild("RightLeg");
		this.LeftLeg = root.getChild("LeftLeg");
		this.Head = root.getChild("Head");
		this.Body = root.getChild("Body");
		this.RightArm = root.getChild("RightArm");
		this.LeftArm = root.getChild("LeftArm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offset(-2.75F, 10.0F, 0.0F));
		PartDefinition Toe_r1 = RightLeg.addOrReplaceChild("Toe_r1", CubeListBuilder.create().texOffs(34, 2).addBox(-4.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.02F)).texOffs(36, 15)
				.addBox(-2.5F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.02F)).texOffs(16, 36).addBox(-1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.02F)),
				PartPose.offsetAndRotation(2.0F, 13.0F, -2.25F, -0.6109F, 0.0F, 0.0F));
		PartDefinition Toe_r2 = RightLeg.addOrReplaceChild("Toe_r2",
				CubeListBuilder.create().texOffs(27, 36).addBox(-1.0F, -3.0F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.01F)).texOffs(36, 17).addBox(-2.5F, -3.0F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.01F)).texOffs(19, 37)
						.addBox(-4.0F, -3.0F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.01F)).texOffs(43, 0).addBox(-4.0F, -4.0F, -1.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(2.0F, 16.0F, -0.75F, 0.0F, 0.0F, 0.0F));
		PartDefinition RightLowerLeg_r1 = RightLeg.addOrReplaceChild("RightLowerLeg_r1", CubeListBuilder.create().texOffs(28, 55).addBox(-2.0F, -7.5F, -1.0F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.01F)),
				PartPose.offsetAndRotation(0.0F, 14.25F, -1.25F, -0.5236F, 0.0F, 0.0F));
		PartDefinition RightMidLeg_r1 = RightLeg.addOrReplaceChild("RightMidLeg_r1", CubeListBuilder.create().texOffs(24, 44).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.25F, -1.5F, 0.5672F, 0.0F, 0.0F));
		PartDefinition RightUpperLeg_r1 = RightLeg.addOrReplaceChild("RightUpperLeg_r1", CubeListBuilder.create().texOffs(49, 6).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));
		PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(2.75F, 10.0F, 0.0F));
		PartDefinition Toe_r3 = LeftLeg.addOrReplaceChild("Toe_r3", CubeListBuilder.create().texOffs(24, 2).addBox(-2.5F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.02F)).texOffs(24, 16)
				.addBox(-1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.02F)).texOffs(34, 0).addBox(-4.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.02F)),
				PartPose.offsetAndRotation(2.0F, 13.0F, -2.25F, -0.6109F, 0.0F, 0.0F));
		PartDefinition Toe_r4 = LeftLeg.addOrReplaceChild("Toe_r4",
				CubeListBuilder.create().texOffs(24, 18).addBox(-1.0F, -3.0F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.01F)).texOffs(0, 32).addBox(-2.5F, -3.0F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.01F)).texOffs(0, 34)
						.addBox(-4.0F, -3.0F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.01F)).texOffs(32, 10).addBox(-4.0F, -4.0F, -1.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(2.0F, 16.0F, -0.75F, 0.0F, 0.0F, 0.0F));
		PartDefinition LeftLowerLeg_r1 = LeftLeg.addOrReplaceChild("LeftLowerLeg_r1", CubeListBuilder.create().texOffs(42, 58).addBox(-2.0F, -7.5F, -1.0F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.01F)),
				PartPose.offsetAndRotation(0.0F, 14.25F, -1.25F, -0.5236F, 0.0F, 0.0F));
		PartDefinition LeftMidLeg_r1 = LeftLeg.addOrReplaceChild("LeftMidLeg_r1", CubeListBuilder.create().texOffs(40, 44).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.25F, -1.5F, 0.5672F, 0.0F, 0.0F));
		PartDefinition LeftUpperLeg_r1 = LeftLeg.addOrReplaceChild("LeftUpperLeg_r1", CubeListBuilder.create().texOffs(50, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));
		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, 0.0F));
		PartDefinition lefteye_r1 = Head.addOrReplaceChild("lefteye_r1", CubeListBuilder.create().texOffs(77, 36).addBox(1.0F, -5.0F, -4.0001F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(73, 36)
				.addBox(-3.0F, -5.0F, -4.0001F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition wisker_r1 = Head.addOrReplaceChild("wisker_r1", CubeListBuilder.create().texOffs(77, 9).addBox(2.875F, -1.0F, -4.0F, 4.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0136F, -0.167F, 0.0927F));
		PartDefinition wisker_r2 = Head.addOrReplaceChild("wisker_r2", CubeListBuilder.create().texOffs(77, 9).addBox(-6.875F, -1.0F, -4.0F, 4.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0136F, 0.167F, -0.0927F));
		PartDefinition wisker_r3 = Head.addOrReplaceChild("wisker_r3", CubeListBuilder.create().texOffs(71, 9).addBox(2.875F, -1.0F, -4.0F, 3.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.2182F, -0.1745F));
		PartDefinition wisker_r4 = Head.addOrReplaceChild("wisker_r4", CubeListBuilder.create().texOffs(71, 9).addBox(-5.875F, -1.0F, -4.0F, 3.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.2182F, 0.1745F));
		PartDefinition ear_r1 = Head.addOrReplaceChild("ear_r1", CubeListBuilder.create().texOffs(33, 1).addBox(1.6F, -30.0F, 13.0F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 26.0F, 0.0F, 0.4363F, -0.1745F, -0.1745F));
		PartDefinition ear_r2 = Head.addOrReplaceChild("ear_r2", CubeListBuilder.create().texOffs(17, 33).addBox(-3.6F, -30.0F, 13.0F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 26.0F, 0.0F, 0.4363F, 0.1745F, 0.1745F));
		PartDefinition Snout_r1 = Head.addOrReplaceChild("Snout_r1", CubeListBuilder.create().texOffs(18, 42).addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-1.5F, -2.0F, -7.0F, 0.0F, -0.2182F, 0.0F));
		PartDefinition Snout_r2 = Head.addOrReplaceChild("Snout_r2", CubeListBuilder.create().texOffs(61, 54).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(1.5F, -2.0F, -7.0F, 0.0F, 0.2182F, 0.0F));
		PartDefinition Snout_r3 = Head.addOrReplaceChild("Snout_r3", CubeListBuilder.create().texOffs(49, 24).addBox(-1.5F, -26.75F, -8.25F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 27.0F, 0.0F, -0.0873F, 0.0F, 0.0F));
		PartDefinition Snout_r4 = Head.addOrReplaceChild("Snout_r4", CubeListBuilder.create().texOffs(57, 0).addBox(-1.5F, -29.0F, -7.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 27.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition Fins = Head.addOrReplaceChild("Fins", CubeListBuilder.create(), PartPose.offset(0.0F, 27.0F, 0.375F));
		PartDefinition Base_r1 = Fins
				.addOrReplaceChild(
						"Base_r1", CubeListBuilder.create().texOffs(68, 20).addBox(-1.9935F, -0.2986F, 10.3383F, 5.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(68, 24)
								.addBox(-0.9935F, -0.2986F, 13.3383F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(68, 27).addBox(3.0065F, -0.2986F, 12.3383F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.5F, -36.25F, -0.75F, -0.354F, 0.1841F, 0.1175F));
		PartDefinition Base_r2 = Fins
				.addOrReplaceChild(
						"Base_r2", CubeListBuilder.create().texOffs(76, 27).addBox(-4.0052F, -0.2986F, 12.3394F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(78, 24)
								.addBox(-3.0052F, -0.2986F, 13.3394F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(84, 20).addBox(-3.0052F, -0.2986F, 10.3394F, 5.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(-0.5F, -36.25F, -0.75F, -0.348F, -0.1991F, -0.1592F));
		PartDefinition Base_r3 = Fins.addOrReplaceChild("Base_r3", CubeListBuilder.create().texOffs(12, 32).addBox(-2.1808F, -1.0F, -0.5736F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(3.5F, -27.75F, -2.0F, -0.5236F, 0.9599F, -3.1416F));
		PartDefinition Base_r4 = Fins.addOrReplaceChild("Base_r4", CubeListBuilder.create().texOffs(27, 32).addBox(-2.1808F, -1.0F, -0.5736F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-3.5F, -27.75F, -2.0F, 0.5236F, 0.9599F, 0.0F));
		PartDefinition Base_r5 = Fins.addOrReplaceChild("Base_r5", CubeListBuilder.create().texOffs(24, 0).addBox(0.325F, -2.3434F, 6.5373F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -34.375F, -1.0F, -0.2694F, -0.256F, -0.7312F));
		PartDefinition Base_r6 = Fins.addOrReplaceChild("Base_r6", CubeListBuilder.create().texOffs(24, 0).addBox(-1.925F, -1.0934F, 3.0373F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -34.375F, -1.0F, 0.0F, 0.0F, -0.7854F));
		PartDefinition Base_r7 = Fins.addOrReplaceChild("Base_r7", CubeListBuilder.create().texOffs(51, 16).addBox(-1.65F, -1.3434F, -1.2127F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -32.875F, 0.0F, 0.3876F, 0.3614F, -0.7137F));
		PartDefinition endfinR = Fins.addOrReplaceChild("endfinR", CubeListBuilder.create(), PartPose.offset(-0.5F, -36.25F, -0.75F));
		PartDefinition endfinL = Fins.addOrReplaceChild("endfinL", CubeListBuilder.create(), PartPose.offset(0.5F, -36.25F, -0.75F));
		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, 0.0F));
		PartDefinition BackFin_r1 = Body.addOrReplaceChild("BackFin_r1", CubeListBuilder.create().texOffs(58, 42).addBox(-1.0F, 0.0F, -4.0F, 2.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 1.75F, 2.0F, 0.829F, 0.0F, 0.0F));
		PartDefinition fluff_r1 = Body.addOrReplaceChild("fluff_r1",
				CubeListBuilder.create().texOffs(28, 64).addBox(1.5F, -12.0F, -2.15F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(7, 76).addBox(1.5F, -8.0F, -2.15F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(29, 76)
						.addBox(-1.5F, -5.0F, -2.2F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(46, 76).addBox(-1.5F, -12.0F, -2.2F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(31, 64)
						.addBox(0.5F, -12.0F, -2.2F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(32, 76).addBox(0.5F, -5.0F, -2.2F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(45, 76)
						.addBox(0.5F, -10.0F, -2.15F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(59, 72).addBox(-0.5F, -12.0F, -2.2F, 1.0F, 11.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(40, 76)
						.addBox(-1.5F, -10.0F, -2.15F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(46, 76).addBox(-2.5F, -12.0F, -2.175F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 76)
						.addBox(-2.5F, -8.0F, -2.15F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 16).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 13.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition Tail = Body.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));
		PartDefinition TailFin_r1 = Tail.addOrReplaceChild("TailFin_r1", CubeListBuilder.create().texOffs(18, 64).addBox(0.0F, 6.0F, -7.25F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.5F, 1.75F, 2.0F, 2.138F, 0.0F, 0.0F));
		PartDefinition TailFin_r2 = Tail.addOrReplaceChild("TailFin_r2", CubeListBuilder.create().texOffs(8, 62).addBox(0.0F, -0.75F, 0.0F, 1.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.5F, 1.75F, 2.0F, 1.789F, 0.0F, 0.0F));
		PartDefinition Base_r8 = Tail.addOrReplaceChild("Base_r8",
				CubeListBuilder.create().texOffs(24, 0).addBox(-0.5F, 5.3462F, -1.8296F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)).texOffs(56, 58).addBox(-0.5F, -2.5538F, -1.8296F, 1.0F, 8.0F, 3.0F, new CubeDeformation(-0.05F)),
				PartPose.offsetAndRotation(0.0F, 5.75F, 20.125F, 1.1345F, 0.0F, 0.0F));
		PartDefinition Base_r9 = Tail.addOrReplaceChild("Base_r9",
				CubeListBuilder.create().texOffs(24, 64).addBox(-0.5F, -4.1668F, 2.8821F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 64).addBox(-0.5F, -4.1668F, 1.8821F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(14, 64)
						.addBox(-0.5F, -6.1668F, 0.8821F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 62).addBox(-0.5F, -8.1668F, -2.1179F, 1.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.75F, 20.125F, -0.8727F, 0.0F, 0.0F));
		PartDefinition Base_r10 = Tail.addOrReplaceChild("Base_r10", CubeListBuilder.create().texOffs(71, 0).addBox(-1.0F, -2.3449F, -0.7203F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 5.5F, 16.125F, 1.4835F, 0.0F, 0.0F));
		PartDefinition Base_r11 = Tail.addOrReplaceChild("Base_r11", CubeListBuilder.create().texOffs(70, 10).addBox(-1.5F, -1.2313F, -0.6088F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 4.5F, 9.25F, 1.309F, 0.0F, 0.0F));
		PartDefinition Base_r12 = Tail.addOrReplaceChild("Base_r12", CubeListBuilder.create().texOffs(61, 5).addBox(-1.0F, -1.075F, -0.625F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.9599F, 0.0F, 0.0F));
		PartDefinition Base_r13 = Tail.addOrReplaceChild("Base_r13", CubeListBuilder.create().texOffs(34, 32).addBox(-2.0F, 0.75F, -0.8F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 1.1781F, 0.0F, 0.0F));
		PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm",
				CubeListBuilder.create().texOffs(24, 16).addBox(-3.0F, -2.0F, -1.7F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(40, 15).addBox(-3.0F, 9.75F, -0.2F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(18, 42)
						.addBox(-3.0F, 9.75F, 1.3F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 42).addBox(0.0F, 9.75F, -1.7F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 37)
						.addBox(-3.0F, 9.75F, -1.7F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-5.0F, 1.0F, -0.3F));
		PartDefinition rightfin = RightArm.addOrReplaceChild("rightfin", CubeListBuilder.create(), PartPose.offset(-1.0F, 5.0F, 0.0F));
		PartDefinition Spike_r1 = rightfin.addOrReplaceChild("Spike_r1",
				CubeListBuilder.create().texOffs(0, 48).addBox(-5.5F, -13.5F, 12.1F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 49).addBox(-5.5F, -16.5F, 14.1F, 1.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(6.0F, 18.0F, 0.0F, 0.6109F, -0.3927F, 0.0F));
		PartDefinition Spike_r2 = rightfin.addOrReplaceChild("Spike_r2", CubeListBuilder.create().texOffs(50, 49).addBox(-5.5F, -13.8F, 21.3F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(6.0F, 18.0F, 0.0F, 0.7418F, -0.3927F, 0.0F));
		PartDefinition Spike_r3 = rightfin.addOrReplaceChild("Spike_r3",
				CubeListBuilder.create().texOffs(40, 10).addBox(-5.5F, -19.8F, 13.8F, 1.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(0, 16).addBox(-5.5F, -19.8F, 20.8F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(6.0F, 18.0F, 0.0F, 0.2618F, -0.3927F, 0.0F));
		PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create(), PartPose.offset(5.0F, 1.0F, -0.3F));
		PartDefinition Finger_r1 = LeftArm.addOrReplaceChild("Finger_r1",
				CubeListBuilder.create().texOffs(28, 42).addBox(3.0F, 9.0F, 1.3F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(43, 5).addBox(6.0F, 9.0F, 1.3F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-4.0F, 0.75F, -3.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition Finger_r2 = LeftArm.addOrReplaceChild("Finger_r2", CubeListBuilder.create().texOffs(43, 10).addBox(3.0F, 9.0F, 1.3F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-1.0F, 0.75F, -1.5F, 0.0F, 0.0F, 0.0F));
		PartDefinition Finger_r3 = LeftArm.addOrReplaceChild("Finger_r3", CubeListBuilder.create().texOffs(18, 44).addBox(3.0F, 9.0F, 1.3F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-1.0F, 0.75F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition LeftArm_r1 = LeftArm.addOrReplaceChild("LeftArm_r1", CubeListBuilder.create().texOffs(0, 32).addBox(0.0F, -2.0F, -1.7F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition leftfin = LeftArm.addOrReplaceChild("leftfin", CubeListBuilder.create(), PartPose.offset(1.0F, 5.0F, 0.0F));
		PartDefinition Spike_r4 = leftfin.addOrReplaceChild("Spike_r4",
				CubeListBuilder.create().texOffs(10, 42).addBox(4.5F, -16.5F, 14.1F, 1.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(40, 21).addBox(4.5F, -13.5F, 12.1F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-6.0F, 18.0F, 0.0F, 0.6109F, 0.3927F, 0.0F));
		PartDefinition Spike_r5 = leftfin.addOrReplaceChild("Spike_r5",
				CubeListBuilder.create().texOffs(20, 16).addBox(4.5F, -19.8F, 20.8F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(40, 21).addBox(4.5F, -19.8F, 13.8F, 1.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-6.0F, 18.0F, 0.0F, 0.2618F, 0.3927F, 0.0F));
		PartDefinition Spike_r6 = leftfin.addOrReplaceChild("Spike_r6", CubeListBuilder.create().texOffs(14, 55).addBox(4.5F, -13.8F, 21.3F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-6.0F, 18.0F, 0.0F, 0.7418F, 0.3927F, 0.0F));
		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		RightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		LeftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		RightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		LeftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.LeftLeg.xRot = Mth.cos(limbSwing * 1.0F) * -1.0F * limbSwingAmount;
		this.RightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
		this.Head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.Head.xRot = headPitch / (180F / (float) Math.PI);
		this.RightLeg.xRot = Mth.cos(limbSwing * 1.0F) * 1.0F * limbSwingAmount;
		this.LeftArm.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
	}
}
