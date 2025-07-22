package net.foxyas.changedaddon.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.entity.ReynEntity;
import net.foxyas.changedaddon.entity.ReynEntity;
import net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModelInterface;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ReynModel extends AdvancedHumanoidModel<ReynEntity> implements AdvancedHumanoidModelInterface<ReynEntity,ReynModel> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("changed_addon", "reyn_model"), "main");

    private final ModelPart RightLeg;
    private final ModelPart LeftLeg;
    private final ModelPart RightArm;
    private final ModelPart LeftArm;
    private final ModelPart Head;
    private final ModelPart Torso;
    private final ModelPart Tail;
    private final HumanoidAnimator<ReynEntity, ReynModel> animator;

    public ReynModel(ModelPart root) {
        super(root);
        this.RightLeg = root.getChild("RightLeg");
        this.LeftLeg = root.getChild("LeftLeg");
        this.Head = root.getChild("Head");
        this.Torso = root.getChild("Torso");
        this.Tail = Torso.getChild("Tail");
        this.RightArm = root.getChild("RightArm");
        this.LeftArm = root.getChild("LeftArm");

        var tailPrimary = Tail.getChild("TailPrimary");
        var tailSecondary = tailPrimary.getChild("TailSecondary");
        var tailTertiary = tailSecondary.getChild("TailTertiary");

        var leftLowerLeg = LeftLeg.getChild("LeftLowerLeg");
        var leftFoot = leftLowerLeg.getChild("LeftFoot");
        var rightLowerLeg = RightLeg.getChild("RightLowerLeg");
        var rightFoot = rightLowerLeg.getChild("RightFoot");

        animator = HumanoidAnimator.of(this).hipOffset(-1.5f)
                .addPreset(AnimatorPresets.wolfLike(
                        Head, Head.getChild("LeftEar"), Head.getChild("RightEar"),
                        Torso, LeftArm, RightArm,
                        Tail, List.of(tailPrimary, tailSecondary, tailTertiary),
                        LeftLeg, leftLowerLeg, leftFoot, leftFoot.getChild("LeftPad"), RightLeg, rightLowerLeg, rightFoot, rightFoot.getChild("RightPad")));
    }

    public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(0, 34).addBox(-3.0F, -2.0F, -1.9F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 1.0F, 0.0F));

		PartDefinition Armour3 = RightArm.addOrReplaceChild("Armour3", CubeListBuilder.create().texOffs(56, 13).addBox(-2.0F, -2.1F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.001F)), PartPose.offset(-1.0F, 0.0F, 0.0F));

		PartDefinition ar7 = Armour3.addOrReplaceChild("ar7", CubeListBuilder.create().texOffs(59, 5).mirror().addBox(-3.4829F, -0.8304F, -2.5F, 2.0F, 3.0F, 5.0F, new CubeDeformation(0.001F)).mirror(false), PartPose.offsetAndRotation(0.9F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create(), PartPose.offset(5.0F, 1.0F, 0.0F));

		PartDefinition LeftArm2 = LeftArm.addOrReplaceChild("LeftArm2", CubeListBuilder.create().texOffs(0, 34).mirror().addBox(-1.2F, -2.0F, -1.9F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Armour2 = LeftArm.addOrReplaceChild("Armour2", CubeListBuilder.create().texOffs(43, 55).addBox(-2.2F, -2.1F, -2.3F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.001F)), PartPose.offset(1.0F, 0.0F, 0.0F));

		PartDefinition ar1 = Armour2.addOrReplaceChild("ar1", CubeListBuilder.create().texOffs(59, 5).addBox(1.2981F, -0.9069F, -2.3F, 2.0F, 3.0F, 5.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(-0.9F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition Torso = partdefinition.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(0, 0).addBox(-4.15F, 0.0F, -3.0F, 8.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(38, 36).addBox(-3.65F, 5.6F, -1.4F, 7.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(34, 12).addBox(-4.05F, 11.1F, -1.8F, 8.0F, 2.0F, 4.0F, new CubeDeformation(0.3F))
		.texOffs(0, 12).addBox(-3.9F, 5.0F, 1.4F, 8.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(22, 27).addBox(-3.9F, 0.0F, 3.0F, 8.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition Tail = Torso.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offset(0.0F, 13.3F, 0.0F));

		PartDefinition TailPrimary = Tail.addOrReplaceChild("TailPrimary", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition Base_r1 = TailPrimary.addOrReplaceChild("Base_r1", CubeListBuilder.create().texOffs(0, 50).addBox(-2.0F, 0.75F, -1.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1781F, 0.0F, 0.0F));

		PartDefinition TailSecondary = TailPrimary.addOrReplaceChild("TailSecondary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.25F, 4.5F));

		PartDefinition Base_r2 = TailSecondary.addOrReplaceChild("Base_r2", CubeListBuilder.create().texOffs(28, 0).addBox(-2.5F, -0.45F, -2.1F, 5.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 1.4835F, 0.0F, 0.0F));

		PartDefinition TailTertiary = TailSecondary.addOrReplaceChild("TailTertiary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.75F, 2.5F));

		PartDefinition Base_r3 = TailTertiary.addOrReplaceChild("Base_r3", CubeListBuilder.create().texOffs(25, 58).addBox(-2.0F, -1.2F, -1.95F, 4.0F, 4.0F, 4.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.0F, 1.8326F, 0.0F, 0.0F));

		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offset(0.0F, -0.5F, 0.0F));

		PartDefinition headpart = Head.addOrReplaceChild("headpart", CubeListBuilder.create().texOffs(46, 26).addBox(-3.1F, -5.0F, -1.3F, 6.0F, 2.0F, 4.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.2F, 1.5F, 3.2F, 0.3927F, 0.0F, 0.0F));

		PartDefinition headpart2 = Head.addOrReplaceChild("headpart2", CubeListBuilder.create().texOffs(16, 36).addBox(-2.9F, -7.0F, -2.3F, 6.0F, 3.0F, 5.0F, new CubeDeformation(0.15F)), PartPose.offsetAndRotation(0.0F, 0.5F, 4.0F, 0.5236F, 0.0F, 0.0F));

		PartDefinition headpart3 = Head.addOrReplaceChild("headpart3", CubeListBuilder.create().texOffs(41, 18).addBox(-2.5F, -7.0F, -2.3F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, -0.8F, 4.0F, 0.5236F, 0.0F, 0.0F));

		PartDefinition headmain = Head.addOrReplaceChild("headmain", CubeListBuilder.create().texOffs(0, 0).addBox(-6.1593F, -0.4786F, 5.7012F, 0.0F, 0.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(6.1593F, -0.0214F, -5.7012F));

		PartDefinition Neck = headmain.addOrReplaceChild("Neck", CubeListBuilder.create().texOffs(58, 39).addBox(-3.0F, -3.0F, -0.9F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.1593F, -0.4786F, 5.7012F));

		PartDefinition Head3 = headmain.addOrReplaceChild("Head3", CubeListBuilder.create().texOffs(61, 20).addBox(-3.5F, -4.0F, 0.5F, 7.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.1593F, -0.4786F, 5.7012F));

		PartDefinition Fur = headmain.addOrReplaceChild("Fur", CubeListBuilder.create().texOffs(41, 62).addBox(-2.5F, -4.45F, 0.3F, 5.0F, 4.0F, 2.0F, new CubeDeformation(0.19F)), PartPose.offsetAndRotation(-6.1593F, -0.4786F, 5.7012F, -1.0036F, 0.0F, 0.0F));

		PartDefinition Neck1 = headmain.addOrReplaceChild("Neck1", CubeListBuilder.create().texOffs(21, 12).addBox(-3.0F, -25.0F, 0.1F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0001F)), PartPose.offsetAndRotation(-6.1593F, 22.5214F, 11.7012F, 0.2618F, 0.0F, 0.0F));

		PartDefinition visormain = headmain.addOrReplaceChild("visormain", CubeListBuilder.create(), PartPose.offset(-12.3187F, 0.0F, 0.0F));

		PartDefinition actualvisor = visormain.addOrReplaceChild("actualvisor", CubeListBuilder.create(), PartPose.offset(12.3187F, 0.0F, 0.0F));

		PartDefinition cube_r1 = actualvisor.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition cube = cube_r1.addOrReplaceChild("cube", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube1 = cube_r1.addOrReplaceChild("cube1", CubeListBuilder.create().texOffs(22, 66).addBox(4.9945F, -4.55F, 0.0966F, 3.0F, 5.0F, 1.0F, new CubeDeformation(0.0001F)), PartPose.offset(-12.3187F, 0.0F, 0.0F));

		PartDefinition cube_r3 = cube1.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(64, 45).addBox(-2.2816F, -7.6147F, -3.3837F, 2.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.3593F, 3.0647F, 3.5507F, 0.0F, -0.0436F, 0.0F));

		PartDefinition eyes = actualvisor.addOrReplaceChild("eyes", CubeListBuilder.create(), PartPose.offset(-8.2593F, -4.4786F, 2.7012F));

		PartDefinition visor = actualvisor.addOrReplaceChild("visor", CubeListBuilder.create().texOffs(19, 15).addBox(1.9F, -5.3F, -0.9F, 4.0F, 5.0F, 7.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(-10.1593F, 0.5214F, 0.7012F, 0.4051F, -0.2415F, -0.1022F));

		PartDefinition visor1 = actualvisor.addOrReplaceChild("visor1", CubeListBuilder.create().texOffs(0, 22).addBox(-6.0F, -5.3F, -0.9F, 4.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.1593F, 0.5214F, 0.7012F, 0.4051F, 0.2415F, 0.1022F));

		PartDefinition Separator = visormain.addOrReplaceChild("Separator", CubeListBuilder.create().texOffs(62, 25).addBox(-3.6F, -3.3F, -0.1F, 7.0F, 4.0F, 1.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(6.1593F, -3.9786F, 5.7012F, 0.2618F, 0.0F, 0.0F));

		PartDefinition Screw_r1 = visormain.addOrReplaceChild("Screw_r1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.6593F, -2.9786F, 6.2012F, 0.7854F, 0.0F, 0.0F));

		PartDefinition Screw = Screw_r1.addOrReplaceChild("Screw", CubeListBuilder.create().texOffs(61, 53).mirror().addBox(-0.65F, -1.5F, -1.4F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Screw1 = Screw_r1.addOrReplaceChild("Screw1", CubeListBuilder.create(), PartPose.offset(7.0F, 0.0F, 0.0F));

		PartDefinition Screw_r4 = Screw1.addOrReplaceChild("Screw_r4", CubeListBuilder.create().texOffs(61, 53).addBox(2.6803F, -7.2743F, 1.6505F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.3F, 6.0433F, -2.6505F, -0.0169F, 0.1912F, -0.0889F));

		PartDefinition cube_r2 = visormain.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition Screw_r2 = visormain.addOrReplaceChild("Screw_r2", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.6593F, -2.9786F, 6.2012F, 0.7854F, 0.0F, 0.0F));

		PartDefinition Screw_r3 = visormain.addOrReplaceChild("Screw_r3", CubeListBuilder.create(), PartPose.offsetAndRotation(9.6593F, -2.9786F, 6.2012F, 0.7854F, 0.0F, 0.0F));

		PartDefinition Screw2 = Screw_r3.addOrReplaceChild("Screw2", CubeListBuilder.create(), PartPose.offset(-7.0F, 0.0F, 0.0F));

		PartDefinition actualvisor2 = visormain.addOrReplaceChild("actualvisor2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition LeftEar = Head.addOrReplaceChild("LeftEar", CubeListBuilder.create(), PartPose.offsetAndRotation(2.7F, -6.05F, 0.0F, -0.2618F, -0.0873F, 0.3054F));

		PartDefinition LeftEarPivot = LeftEar.addOrReplaceChild("LeftEarPivot", CubeListBuilder.create().texOffs(0, 76).addBox(-1.1F, -3.2F, -1.0F, 4.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 83).addBox(-0.7F, -1.85F, -0.3F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.04F))
		.texOffs(0, 93).addBox(-1.1F, -4.2F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 91).addBox(-1.1F, -5.2F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 88).addBox(-0.7F, -3.85F, -0.3F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.04F)), PartPose.offsetAndRotation(-0.5F, -1.25F, 0.0F, -0.1309F, -0.5236F, 0.3491F));

		PartDefinition RightEar = Head.addOrReplaceChild("RightEar", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.7F, -6.05F, 0.0F, -0.2618F, 0.0873F, -0.3054F));

		PartDefinition RightEarPivot = RightEar.addOrReplaceChild("RightEarPivot", CubeListBuilder.create().texOffs(10, 76).addBox(-2.9F, -3.2F, -1.0F, 4.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(10, 83).addBox(-2.3F, -1.85F, -0.3F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.04F))
		.texOffs(10, 93).addBox(-1.9F, -4.2F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(10, 91).addBox(-0.9F, -5.2F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(10, 88).addBox(-1.3F, -3.85F, -0.3F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.04F)), PartPose.offsetAndRotation(0.5F, -1.25F, 0.0F, -0.1309F, 0.5236F, -0.3491F));

		PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offset(-2.5F, 10.5F, 0.0F));

		PartDefinition RightThigh_r1 = RightLeg.addOrReplaceChild("RightThigh_r1", CubeListBuilder.create().texOffs(32, 45).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition RightLowerLeg = RightLeg.addOrReplaceChild("RightLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

		PartDefinition RightCalf_r1 = RightLowerLeg.addOrReplaceChild("RightCalf_r1", CubeListBuilder.create().texOffs(48, 45).addBox(-1.99F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

		PartDefinition RightFoot = RightLowerLeg.addOrReplaceChild("RightFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

		PartDefinition RightArch_r1 = RightFoot.addOrReplaceChild("RightArch_r1", CubeListBuilder.create().texOffs(58, 59).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

		PartDefinition RightPad = RightFoot.addOrReplaceChild("RightPad", CubeListBuilder.create().texOffs(55, 32).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

		PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(2.5F, 10.5F, 0.0F));

		PartDefinition LeftThigh_r1 = LeftLeg.addOrReplaceChild("LeftThigh_r1", CubeListBuilder.create().texOffs(16, 44).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition LeftLowerLeg = LeftLeg.addOrReplaceChild("LeftLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

		PartDefinition LeftCalf_r1 = LeftLowerLeg.addOrReplaceChild("LeftCalf_r1", CubeListBuilder.create().texOffs(48, 0).addBox(-2.01F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

		PartDefinition LeftFoot = LeftLowerLeg.addOrReplaceChild("LeftFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

		PartDefinition LeftArch_r1 = LeftFoot.addOrReplaceChild("LeftArch_r1", CubeListBuilder.create().texOffs(0, 59).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

		PartDefinition LeftPad = LeftFoot.addOrReplaceChild("LeftPad", CubeListBuilder.create().texOffs(11, 55).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

		return LayerDefinition.create(meshdefinition, 96, 96);
	}

    @Override
    public void prepareMobModel (ReynEntity p_162861, float p_102862, float p_102863, float p_102864_) {
        this.prepareMobModel(animator, p_162861, p_102862, p_102863, p_102864_);
    }

    @Override
    public void setupHand(ReynEntity entity) {
        animator.setupHand();
    }


    @Override
    public void setupAnim(@NotNull ReynEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animator.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    public ModelPart getArm (HumanoidArm p_102852) {
        return p_102852 == HumanoidArm.LEFT ? this.LeftArm : this.RightArm;
    }

    public ModelPart getHead() {
        return this.Head;
    }

    public ModelPart getTorso() {
        return Torso;
    }

	@Override
	public ModelPart getLeg(HumanoidArm humanoidArm) {
		return humanoidArm == HumanoidArm.LEFT ? this.LeftLeg : this.RightLeg;
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

    @Override
    public HumanoidAnimator<ReynEntity, ReynModel> getAnimator(ReynEntity entity) {
        return animator;
    }
}
