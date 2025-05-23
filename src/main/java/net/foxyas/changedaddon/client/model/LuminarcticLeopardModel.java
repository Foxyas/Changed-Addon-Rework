package net.foxyas.changedaddon.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.client.renderer.layers.animation.CarryAbilityAnimation;
import net.foxyas.changedaddon.entity.LuminarcticLeopardEntity;
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

public class LuminarcticLeopardModel extends AdvancedHumanoidModel<LuminarcticLeopardEntity> implements AdvancedHumanoidModelInterface<LuminarcticLeopardEntity,LuminarcticLeopardModel> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("changed_addon", "luminarctic_leopard"), "main");
    private final ModelPart RightLeg;
    private final ModelPart LeftLeg;
    private final ModelPart Head;
    private final ModelPart Torso;
    private final ModelPart RightArm;
    private final ModelPart LeftArm;
    private final ModelPart RightArmFur;
    private final ModelPart LeftArmFur;
    private final ModelPart Tail;
    private final HumanoidAnimator<LuminarcticLeopardEntity, LuminarcticLeopardModel> animator;

    public LuminarcticLeopardModel(ModelPart root) {
        super(root);
        this.RightLeg = root.getChild("RightLeg");
        this.LeftLeg = root.getChild("LeftLeg");
        this.Head = root.getChild("Head");
        this.Torso = root.getChild("Torso");
        this.Tail = Torso.getChild("Tail");
        this.RightArm = root.getChild("RightArm");
        this.LeftArm = root.getChild("LeftArm");
        this.RightArmFur = RightArm.getChild("RightFur");
        this.LeftArmFur = LeftArm.getChild("LeftFur");

        var tailPrimary = Tail.getChild("TailPrimary");
        var tailSecondary = tailPrimary.getChild("TailSecondary");
        var tailTertiary = tailSecondary.getChild("TailTertiary");


        var leftLowerLeg = LeftLeg.getChild("LeftLowerLeg");
        var leftFoot = leftLowerLeg.getChild("LeftFoot");
        var rightLowerLeg = RightLeg.getChild("RightLowerLeg");
        var rightFoot = rightLowerLeg.getChild("RightFoot");

        animator = HumanoidAnimator.of(this).hipOffset(-1.5f)
                .addPreset(AnimatorPresets.catLike(
                        Head, Head.getChild("LeftEar"), Head.getChild("RightEar"),
                        Torso, LeftArm, RightArm,
                        Tail, List.of(tailPrimary, tailSecondary, tailTertiary, tailTertiary.getChild("TailQuaternary")),
                        LeftLeg, leftLowerLeg, leftFoot, leftFoot.getChild("LeftPad"), RightLeg, rightLowerLeg, rightFoot, rightFoot.getChild("RightPad")));
    }

    public boolean isPartNotArmFur(ModelPart part) {
        return LeftArmFur.getAllParts().noneMatch(part::equals) && RightArmFur.getAllParts().noneMatch(part::equals);
    }

    public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.5F, -1.0F, -5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(15, 32).addBox(-2.0F, -3.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));

		PartDefinition Snout_r1 = Head.addOrReplaceChild("Snout_r1", CubeListBuilder.create().texOffs(0, 2).addBox(-1.0F, -29.625F, -0.95F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 26.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

		PartDefinition feline_eyes = Head.addOrReplaceChild("feline_eyes", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_iris = feline_eyes.addOrReplaceChild("right_iris", CubeListBuilder.create().texOffs(41, 93).addBox(-2.0F, -5.1F, -4.399F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.35F))
		.texOffs(41, 93).addBox(-2.0F, -3.9F, -4.399F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.35F))
		.texOffs(41, 93).addBox(-2.0F, -5.0F, -4.35F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.3F))
		.texOffs(41, 93).addBox(-2.0F, -3.8F, -4.448F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F))
		.texOffs(41, 93).addBox(-2.0F, -5.2F, -4.448F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F))
		.texOffs(41, 93).addBox(-2.0F, -5.3F, -4.49F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.45F))
		.texOffs(41, 93).addBox(-2.0F, -3.7F, -4.49F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.45F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_iris = feline_eyes.addOrReplaceChild("left_iris", CubeListBuilder.create().texOffs(45, 93).addBox(-2.0F, -5.1F, -4.399F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.35F))
		.texOffs(45, 93).addBox(-2.0F, -3.9F, -4.399F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.35F))
		.texOffs(45, 93).addBox(-2.0F, -5.0F, -4.35F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.3F))
		.texOffs(45, 93).addBox(-2.0F, -3.8F, -4.448F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F))
		.texOffs(45, 93).addBox(-2.0F, -5.2F, -4.448F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F))
		.texOffs(45, 93).addBox(-2.0F, -5.3F, -4.49F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.45F))
		.texOffs(45, 93).addBox(-2.0F, -3.7F, -4.49F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.45F)), PartPose.offset(3.0F, 0.0F, 0.0F));

		PartDefinition LeftEar = Head.addOrReplaceChild("LeftEar", CubeListBuilder.create(), PartPose.offset(2.5F, -5.0F, 0.0F));

		PartDefinition leftearextra_r1 = LeftEar.addOrReplaceChild("leftearextra_r1", CubeListBuilder.create().texOffs(47, 61).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(-0.0002F)), PartPose.offsetAndRotation(1.8711F, -5.7315F, 0.5F, 0.0F, 0.0F, 0.829F));

		PartDefinition leftear_r1 = LeftEar.addOrReplaceChild("leftear_r1", CubeListBuilder.create().texOffs(46, 61).addBox(-7.75F, -34.75F, -1.0F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.25F, 29.25F, 0.0F, 0.0F, 0.0F, 0.3054F));

		PartDefinition RightEar = Head.addOrReplaceChild("RightEar", CubeListBuilder.create(), PartPose.offset(-3.0F, -5.5F, 0.0F));

		PartDefinition rightearextra_r1 = RightEar.addOrReplaceChild("rightearextra_r1", CubeListBuilder.create().texOffs(61, 48).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(-0.0002F)), PartPose.offsetAndRotation(-1.6211F, -5.2315F, 0.5F, 0.0F, 0.0F, -0.829F));

		PartDefinition rightear_r1 = RightEar.addOrReplaceChild("rightear_r1", CubeListBuilder.create().texOffs(61, 48).addBox(5.75F, -34.75F, -1.0F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 29.75F, 0.0F, 0.0F, 0.0F, -0.3054F));

		PartDefinition Hair = Head.addOrReplaceChild("Hair", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, -34.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F))
		.texOffs(24, 8).addBox(-4.5F, -34.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.3F)), PartPose.offset(0.5F, 26.0F, 0.0F));

		PartDefinition ExtraFur = Head.addOrReplaceChild("ExtraFur", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Right = ExtraFur.addOrReplaceChild("Right", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = Right.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(82, 24).mirror().addBox(-0.5F, -1.0F, -3.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.9107F, -4.1024F, -0.75F, 0.948F, 0.1168F, -0.3325F));

		PartDefinition cube_r2 = Right.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(82, 16).mirror().addBox(0.25F, -0.75F, -3.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-4.5F, -3.0F, 0.0F, 0.9472F, 0.0741F, -0.3414F));

		PartDefinition cube_r3 = Right.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(82, 8).mirror().addBox(0.25F, -0.75F, -3.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-4.5F, -2.5F, 0.0F, 0.4437F, 0.0502F, -0.5043F));

		PartDefinition cube_r4 = Right.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(82, 0).mirror().addBox(0.3F, -0.75F, -3.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-4.75F, -2.25F, -0.75F, 0.4437F, 0.0502F, -0.5043F));

		PartDefinition cube_r5 = Right.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(82, 47).addBox(-0.5F, -1.0F, -3.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.9107F, -2.1024F, 0.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition cube_r6 = Right.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(82, 39).mirror().addBox(0.25F, -0.75F, -3.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-4.5F, -1.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

		PartDefinition Left = ExtraFur.addOrReplaceChild("Left", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r7 = Left.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(72, 26).addBox(-0.5F, -1.0F, -3.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.9107F, -4.1024F, -0.75F, 0.948F, -0.1168F, 0.3325F));

		PartDefinition cube_r8 = Left.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(72, 18).addBox(-1.25F, -0.75F, -3.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(4.5F, -3.0F, 0.0F, 0.9472F, -0.0741F, 0.3414F));

		PartDefinition cube_r9 = Left.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(72, 10).addBox(-1.25F, -0.75F, -3.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(4.5F, -2.5F, 0.0F, 0.4437F, -0.0502F, 0.5043F));

		PartDefinition cube_r10 = Left.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(72, 2).addBox(-1.3F, -0.75F, -3.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(4.75F, -2.25F, -0.75F, 0.4437F, -0.0502F, 0.5043F));

		PartDefinition cube_r11 = Left.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(82, 63).addBox(-0.5F, -1.0F, -3.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.9107F, -2.1024F, 0.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition cube_r12 = Left.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(82, 55).addBox(-1.25F, -0.75F, -3.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(4.5F, -1.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

		PartDefinition Torso = partdefinition.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(28, 28).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(32, 68).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.15F)), PartPose.offset(0.0F, -0.5F, 0.0F));

		PartDefinition Tail = Torso.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offset(0.0F, 10.5F, 0.0F));

		PartDefinition TailPrimary = Tail.addOrReplaceChild("TailPrimary", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition Base_r1 = TailPrimary.addOrReplaceChild("Base_r1", CubeListBuilder.create().texOffs(0, 56).addBox(-2.0F, 0.75F, -1.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.0908F, 0.0F, 0.0F));

		PartDefinition TailSecondary = TailPrimary.addOrReplaceChild("TailSecondary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.25F, 4.5F));

		PartDefinition Base_r2 = TailSecondary.addOrReplaceChild("Base_r2", CubeListBuilder.create().texOffs(48, 0).addBox(-2.5F, -0.45F, -2.5F, 5.0F, 6.0F, 5.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 1.3526F, 0.0F, 0.0F));

		PartDefinition TailTertiary = TailSecondary.addOrReplaceChild("TailTertiary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.75F, 4.5F));

		PartDefinition Base_r3 = TailTertiary.addOrReplaceChild("Base_r3", CubeListBuilder.create().texOffs(0, 32).addBox(-2.5F, 4.55F, -3.3F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.25F, -4.5F, 1.5272F, 0.0F, 0.0F));

		PartDefinition TailQuaternary = TailTertiary.addOrReplaceChild("TailQuaternary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 7.5F));

		PartDefinition Base_r4 = TailQuaternary.addOrReplaceChild("Base_r4", CubeListBuilder.create().texOffs(30, 60).addBox(-2.0F, 5.5F, -3.8F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.15F)), PartPose.offsetAndRotation(0.0F, -1.0F, -5.5F, 1.7017F, 0.0F, 0.0F));

		PartDefinition NeckFur = Torso.addOrReplaceChild("NeckFur", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Back = NeckFur.addOrReplaceChild("Back", CubeListBuilder.create(), PartPose.offset(0.0F, -0.25F, 0.25F));

		PartDefinition fur_r1 = Back.addOrReplaceChild("fur_r1", CubeListBuilder.create().texOffs(49, 85).mirror().addBox(1.6904F, -0.4529F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offsetAndRotation(0.1848F, 2.5802F, 1.292F, 0.5516F, -0.1395F, -2.9193F));

		PartDefinition fur_r2 = Back.addOrReplaceChild("fur_r2", CubeListBuilder.create().texOffs(49, 85).addBox(-3.6904F, -0.4529F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(-0.1848F, 2.5802F, 1.292F, 0.5516F, 0.1395F, 2.9193F));

		PartDefinition fur_r3 = Back.addOrReplaceChild("fur_r3", CubeListBuilder.create().texOffs(49, 85).mirror().addBox(1.6904F, -0.4529F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offsetAndRotation(0.1848F, 3.5802F, 1.292F, 0.5516F, -0.1395F, -2.9193F));

		PartDefinition fur_r4 = Back.addOrReplaceChild("fur_r4", CubeListBuilder.create().texOffs(49, 85).addBox(-3.6904F, -0.4529F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(-0.1848F, 3.5802F, 1.292F, 0.5516F, 0.1395F, 2.9193F));

		PartDefinition fur_r5 = Back.addOrReplaceChild("fur_r5", CubeListBuilder.create().texOffs(51, 84).addBox(-3.6904F, -1.4529F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0652F, 3.0802F, 1.292F, 0.5516F, 0.1395F, 2.9193F));

		PartDefinition fur_r6 = Back.addOrReplaceChild("fur_r6", CubeListBuilder.create().texOffs(51, 84).mirror().addBox(-0.3096F, -1.4529F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-0.0652F, 3.0802F, 1.292F, 0.5516F, -0.1395F, -2.9193F));

		PartDefinition fur_r7 = Back.addOrReplaceChild("fur_r7", CubeListBuilder.create().texOffs(51, 84).mirror().addBox(-0.3096F, -1.4529F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-0.0652F, 2.0802F, 1.292F, 0.5516F, -0.1395F, -2.9193F));

		PartDefinition fur_r8 = Back.addOrReplaceChild("fur_r8", CubeListBuilder.create().texOffs(48, 87).addBox(-1.9107F, 0.0F, -0.75F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(48, 87).addBox(-1.9107F, 1.0F, 0.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-0.0893F, 1.3976F, 2.0F, -0.5672F, 0.0F, 0.0F));

		PartDefinition fur_r9 = Back.addOrReplaceChild("fur_r9", CubeListBuilder.create().texOffs(48, 87).addBox(-1.9107F, 1.0F, 0.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-0.0893F, 0.8976F, 2.0F, -0.5672F, 0.0F, 0.0F));

		PartDefinition fur_r10 = Back.addOrReplaceChild("fur_r10", CubeListBuilder.create().texOffs(51, 84).addBox(-3.6904F, -1.4529F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0652F, 2.0802F, 1.292F, 0.5516F, 0.1395F, 2.9193F));

		PartDefinition Front = NeckFur.addOrReplaceChild("Front", CubeListBuilder.create(), PartPose.offset(0.0F, 0.25F, 0.0F));

		PartDefinition fur_r11 = Front.addOrReplaceChild("fur_r11", CubeListBuilder.create().texOffs(19, 84).addBox(-3.6904F, -1.4529F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0652F, 1.5802F, -1.542F, -0.5516F, -0.1395F, 2.9193F));

		PartDefinition fur_r12 = Front.addOrReplaceChild("fur_r12", CubeListBuilder.create().texOffs(19, 84).mirror().addBox(1.6904F, -0.4529F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offsetAndRotation(0.1848F, 2.0802F, -1.542F, -0.5516F, 0.1395F, -2.9193F));

		PartDefinition fur_r13 = Front.addOrReplaceChild("fur_r13", CubeListBuilder.create().texOffs(19, 84).addBox(-3.6904F, -0.4529F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(-0.1848F, 2.0802F, -1.542F, -0.5516F, -0.1395F, 2.9193F));

		PartDefinition fur_r14 = Front.addOrReplaceChild("fur_r14", CubeListBuilder.create().texOffs(19, 84).mirror().addBox(1.6904F, -0.4529F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offsetAndRotation(0.1848F, 3.0802F, -1.542F, -0.5516F, 0.1395F, -2.9193F));

		PartDefinition fur_r15 = Front.addOrReplaceChild("fur_r15", CubeListBuilder.create().texOffs(19, 84).addBox(-3.6904F, -0.4529F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(-0.1848F, 3.0802F, -1.542F, -0.5516F, -0.1395F, 2.9193F));

		PartDefinition fur_r16 = Front.addOrReplaceChild("fur_r16", CubeListBuilder.create().texOffs(19, 84).addBox(-3.6904F, -1.4529F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0652F, 2.5802F, -1.542F, -0.5516F, -0.1395F, 2.9193F));

		PartDefinition fur_r17 = Front.addOrReplaceChild("fur_r17", CubeListBuilder.create().texOffs(19, 84).mirror().addBox(-0.3096F, -1.4529F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-0.0652F, 2.5802F, -1.542F, -0.5516F, 0.1395F, -2.9193F));

		PartDefinition fur_r18 = Front.addOrReplaceChild("fur_r18", CubeListBuilder.create().texOffs(19, 84).mirror().addBox(-0.3096F, -1.4529F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-0.0652F, 1.5802F, -1.542F, -0.5516F, 0.1395F, -2.9193F));

		PartDefinition fur_r19 = Front.addOrReplaceChild("fur_r19", CubeListBuilder.create().texOffs(20, 84).addBox(-1.9107F, 0.0F, -0.25F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 84).addBox(-1.9107F, 1.0F, -1.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-0.0893F, 0.8976F, -2.25F, 0.5672F, 0.0F, 0.0F));

		PartDefinition fur_r20 = Front.addOrReplaceChild("fur_r20", CubeListBuilder.create().texOffs(20, 84).addBox(-1.9107F, 1.0F, -1.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-0.0893F, 0.3976F, -2.25F, 0.5672F, 0.0F, 0.0F));

		PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(16, 41).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 1.5F, 0.0F));

		PartDefinition RightFur = RightArm.addOrReplaceChild("RightFur", CubeListBuilder.create().texOffs(0, 65).addBox(-13.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.15F)), PartPose.offset(10.0F, 0.0F, 0.0F));

		PartDefinition RightPawBeans = RightArm.addOrReplaceChild("RightPawBeans", CubeListBuilder.create().texOffs(0, 93).mirror().addBox(-2.0F, 9.475F, -0.375F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.075F)).mirror(false)
		.texOffs(0, 89).mirror().addBox(-2.8F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false)
		.texOffs(0, 87).mirror().addBox(-1.5F, 9.475F, -1.875F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false)
		.texOffs(0, 91).mirror().addBox(-0.225F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false), PartPose.offset(0.0F, -0.5F, 0.0F));

		PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(32, 44).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 1.5F, 0.0F));

		PartDefinition LeftFur = LeftArm.addOrReplaceChild("LeftFur", CubeListBuilder.create().texOffs(16, 66).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.15F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition LeftPawBeans = LeftArm.addOrReplaceChild("LeftPawBeans", CubeListBuilder.create().texOffs(8, 93).addBox(0.0F, 9.475F, -0.375F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.075F))
		.texOffs(8, 89).addBox(1.8F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F))
		.texOffs(8, 87).addBox(0.5F, 9.475F, -1.875F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F))
		.texOffs(8, 91).addBox(-0.775F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)), PartPose.offset(0.0F, -0.5F, 0.0F));

		PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offset(-2.5F, 10.5F, 0.0F));

		PartDefinition RightThigh_r1 = RightLeg.addOrReplaceChild("RightThigh_r1", CubeListBuilder.create().texOffs(48, 40).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition RightLowerLeg = RightLeg.addOrReplaceChild("RightLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

		PartDefinition RightCalf_r1 = RightLowerLeg.addOrReplaceChild("RightCalf_r1", CubeListBuilder.create().texOffs(48, 51).addBox(-1.99F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

		PartDefinition RightFoot = RightLowerLeg.addOrReplaceChild("RightFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

		PartDefinition RightArch_r1 = RightFoot.addOrReplaceChild("RightArch_r1", CubeListBuilder.create().texOffs(56, 11).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

		PartDefinition RightPad = RightFoot.addOrReplaceChild("RightPad", CubeListBuilder.create().texOffs(52, 32).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

		PartDefinition RightFootPawBeans = RightPad.addOrReplaceChild("RightFootPawBeans", CubeListBuilder.create().texOffs(16, 93).mirror().addBox(-2.0F, 9.475F, -0.375F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.075F)).mirror(false)
		.texOffs(16, 89).mirror().addBox(-2.8F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false)
		.texOffs(16, 87).mirror().addBox(-1.5F, 9.475F, -1.875F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false)
		.texOffs(16, 91).mirror().addBox(-0.225F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false), PartPose.offset(1.0F, -8.5F, -0.05F));

		PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(2.5F, 10.5F, 0.0F));

		PartDefinition LeftThigh_r1 = LeftLeg.addOrReplaceChild("LeftThigh_r1", CubeListBuilder.create().texOffs(0, 45).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition LeftLowerLeg = LeftLeg.addOrReplaceChild("LeftLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

		PartDefinition LeftCalf_r1 = LeftLowerLeg.addOrReplaceChild("LeftCalf_r1", CubeListBuilder.create().texOffs(48, 22).addBox(-2.01F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

		PartDefinition LeftFoot = LeftLowerLeg.addOrReplaceChild("LeftFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

		PartDefinition LeftArch_r1 = LeftFoot.addOrReplaceChild("LeftArch_r1", CubeListBuilder.create().texOffs(16, 57).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

		PartDefinition LeftPad = LeftFoot.addOrReplaceChild("LeftPad", CubeListBuilder.create().texOffs(24, 0).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

		PartDefinition LeftFootPawBeans = LeftPad.addOrReplaceChild("LeftFootPawBeans", CubeListBuilder.create().texOffs(24, 93).addBox(0.0F, 9.475F, -0.375F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.075F))
		.texOffs(24, 89).addBox(1.8F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F))
		.texOffs(24, 87).addBox(0.5F, 9.475F, -1.875F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F))
		.texOffs(24, 91).addBox(-0.775F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)), PartPose.offset(-1.0F, -8.5F, -0.05F));

		return LayerDefinition.create(meshdefinition, 96, 96);
	}


    @Override
    public void prepareMobModel (LuminarcticLeopardEntity p_162861, float p_102862, float p_102863, float p_102864_) {
        this.prepareMobModel(animator, p_162861, p_102862, p_102863, p_102864_);
    }

  //public PoseStack getPlacementCorrectors(CorrectorType type) {
  //     PoseStack corrector = AdvancedHumanoidModelInterface.super.getPlacementCorrectors(type);
  //     if (type.isArm())
  //         corrector.translate(-0.02f, 0.12f, 0.12f);
  //     return corrector;
  //}

     
    @Override
    public void setupHand() {
        animator.setupHand();
    }


    @Override
    public void setupAnim(@NotNull LuminarcticLeopardEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animator.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        //CarryAbilityAnimation.playAnimation(entity, this);
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
    public HumanoidAnimator<LuminarcticLeopardEntity, LuminarcticLeopardModel> getAnimator() {
        return animator;
    }
}
