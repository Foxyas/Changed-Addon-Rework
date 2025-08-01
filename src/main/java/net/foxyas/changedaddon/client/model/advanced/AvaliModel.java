package net.foxyas.changedaddon.client.model.advanced;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.client.model.animations.AvaliFallFlyAnimator;
import net.foxyas.changedaddon.client.model.animations.AvaliUpperBodyInitAnimator;
import net.foxyas.changedaddon.entity.advanced.AvaliEntity;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.arm.ArmBobAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.arm.ArmRideAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.arm.ArmSwimAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.upperbody.*;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModelInterface;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

import static net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets.dragonBipedal;
import static net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets.dragonTail;

public class AvaliModel extends AdvancedHumanoidModel<AvaliEntity> implements AdvancedHumanoidModelInterface<AvaliEntity, AvaliModel> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("changed_addon", "avali_model"), "main");
    private final ModelPart Head;
    private final ModelPart Torso;
    private final ModelPart RightArm;
    private final ModelPart LeftArm;
    private final ModelPart RightLeg;
    private final ModelPart LeftLeg;
    private final HumanoidAnimator<AvaliEntity, AvaliModel> animator;
    public AvaliModel(ModelPart root) {
        super(root);
        this.Head = root.getChild("Head");
        this.Torso = root.getChild("Torso");
        ModelPart tail = this.Torso.getChild("Tail");
        ModelPart tailPrimary = tail.getChild("TailPrimary");
        ModelPart tailSecondary = tailPrimary.getChild("TailSecondary");
        ModelPart tailTertiary = tailSecondary.getChild("TailTertiary");
        ModelPart tailQuaternary = tailTertiary.getChild("TailQuaternary");
        this.RightArm = root.getChild("RightArm");
        this.LeftArm = root.getChild("LeftArm");
        this.RightLeg = root.getChild("RightLeg");
        ModelPart rightLowerLeg = this.RightLeg.getChild("RightLowerLeg");
        ModelPart rightFoot = rightLowerLeg.getChild("RightFoot");
        ModelPart rightPad = rightFoot.getChild("RightPad");
        this.LeftLeg = root.getChild("LeftLeg");
        ModelPart leftLowerLeg = this.LeftLeg.getChild("LeftLowerLeg");
        ModelPart leftFoot = leftLowerLeg.getChild("LeftFoot");
        ModelPart leftPad = leftFoot.getChild("LeftPad");

        animator = HumanoidAnimator.of(this).hipOffset(-1.5f)
                .addPreset(AvaliAnimationPresets.AvaliLike(
                        Head,
                        Torso, LeftArm, RightArm,
                        tail, List.of(tailPrimary, tailSecondary, tailTertiary, tailQuaternary),
                        LeftLeg, leftLowerLeg, leftFoot, leftPad, RightLeg, rightLowerLeg, rightFoot, rightPad));

    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(66, 49).addBox(-2.0F, -3.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));

        PartDefinition Snout_r1 = Head.addOrReplaceChild("Snout_r1", CubeListBuilder.create().texOffs(38, 29).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, -1.2F, -4.1075F, -0.3054F, 0.0F, 0.0F));

        PartDefinition CenterFeathers = Head.addOrReplaceChild("CenterFeathers", CubeListBuilder.create(), PartPose.offset(0.0F, -2.5F, 0.0F));

        PartDefinition RightFeathers = Head.addOrReplaceChild("RightFeathers", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.1987F, -7.5771F, 3.4555F, -0.1309F, -0.0436F, 0.0F));

        PartDefinition cube_r1 = RightFeathers.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(8, 78).mirror().addBox(0.5F, -2.5F, -2.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.8515F, 3.8588F, 1.5442F, -0.8724F, -0.22F, -0.1369F));

        PartDefinition cube_r2 = RightFeathers.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(32, 9).mirror().addBox(0.5F, -3.5F, -2.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.817F, 3.0809F, 0.9167F, -0.8724F, -0.22F, -0.1369F));

        PartDefinition cube_r3 = RightFeathers.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(46, 74).mirror().addBox(0.5F, -4.5F, -2.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.7825F, 2.303F, 0.2893F, -0.8724F, -0.22F, -0.1369F));

        PartDefinition cube_r4 = RightFeathers.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(34, 74).mirror().addBox(0.5F, -5.5F, -2.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.748F, 1.5252F, -0.3382F, -0.8724F, -0.22F, -0.1369F));

        PartDefinition cube_r5 = RightFeathers.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(30, 74).mirror().addBox(0.5F, -6.5F, -2.5F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.7134F, 0.7473F, -0.9657F, -0.8724F, -0.22F, -0.1369F));

        PartDefinition LeftFeathers = Head.addOrReplaceChild("LeftFeathers", CubeListBuilder.create(), PartPose.offsetAndRotation(4.1987F, -7.5771F, 3.4555F, -0.1309F, 0.0436F, 0.0F));

        PartDefinition cube_r6 = LeftFeathers.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(8, 78).addBox(-1.5F, -2.5F, -2.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.8515F, 3.8588F, 1.5442F, -0.8724F, 0.22F, 0.1369F));

        PartDefinition cube_r7 = LeftFeathers.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(32, 9).addBox(-1.5F, -3.5F, -2.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.817F, 3.0809F, 0.9167F, -0.8724F, 0.22F, 0.1369F));

        PartDefinition cube_r8 = LeftFeathers.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(46, 74).addBox(-1.5F, -4.5F, -2.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.7825F, 2.303F, 0.2893F, -0.8724F, 0.22F, 0.1369F));

        PartDefinition cube_r9 = LeftFeathers.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(34, 74).addBox(-1.5F, -5.5F, -2.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.748F, 1.5252F, -0.3382F, -0.8724F, 0.22F, 0.1369F));

        PartDefinition cube_r10 = LeftFeathers.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(30, 74).addBox(-1.5F, -6.5F, -2.5F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.7134F, 0.7473F, -0.9657F, -0.8724F, 0.22F, 0.1369F));

        PartDefinition RightSideFeathers = Head.addOrReplaceChild("RightSideFeathers", CubeListBuilder.create(), PartPose.offset(-4.3271F, -1.787F, 2.7907F));

        PartDefinition cube_r11 = RightSideFeathers.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(76, 22).addBox(-1.5F, 1.5F, -2.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.8955F, -3.3311F, 1.1892F, -0.3371F, -0.3194F, 0.0701F));

        PartDefinition cube_r12 = RightSideFeathers.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(64, 0).addBox(-1.5F, 1.5F, -2.5F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.933F, -2.3825F, 0.8752F, -0.3371F, -0.3194F, 0.0701F));

        PartDefinition cube_r13 = RightSideFeathers.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(28, 61).addBox(-1.5F, 1.5F, -2.5F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.008F, -0.4852F, 0.247F, -0.3371F, -0.3194F, 0.0701F));

        PartDefinition cube_r14 = RightSideFeathers.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(38, 20).addBox(-1.5F, 1.5F, -2.5F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.9705F, -1.4338F, 0.5611F, -0.3371F, -0.3194F, 0.0701F));

        PartDefinition LeftSideFeathers = Head.addOrReplaceChild("LeftSideFeathers", CubeListBuilder.create(), PartPose.offset(4.3271F, -1.787F, 2.7907F));

        PartDefinition cube_r15 = LeftSideFeathers.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(42, 61).addBox(0.5F, 1.5F, -2.5F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.008F, -0.4852F, 0.247F, -0.3371F, 0.3194F, -0.0701F));

        PartDefinition cube_r16 = LeftSideFeathers.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(14, 41).addBox(0.5F, 1.5F, -2.5F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.9705F, -1.4338F, 0.5611F, -0.3371F, 0.3194F, -0.0701F));

        PartDefinition cube_r17 = LeftSideFeathers.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(64, 25).addBox(0.5F, 1.5F, -2.5F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.933F, -2.3825F, 0.8752F, -0.3371F, 0.3194F, -0.0701F));

        PartDefinition cube_r18 = LeftSideFeathers.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(76, 26).addBox(0.5F, 1.5F, -2.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.8955F, -3.3311F, 1.1892F, -0.3371F, 0.3194F, -0.0701F));

        PartDefinition Torso = partdefinition.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));

        PartDefinition Tail = Torso.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offset(0.0F, 11.0F, 0.0F));

        PartDefinition Base_r1 = Tail.addOrReplaceChild("Base_r1", CubeListBuilder.create().texOffs(0, 50).addBox(-2.0F, -2.9F, 0.4F, 4.0F, 4.0F, 4.0F, new CubeDeformation(-0.975F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition TailPrimary = Tail.addOrReplaceChild("TailPrimary", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.5F, 3.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition Base_r2 = TailPrimary.addOrReplaceChild("Base_r2", CubeListBuilder.create().texOffs(32, 0).addBox(-1.5F, -1.4F, -2.7F, 3.0F, 3.0F, 6.0F, new CubeDeformation(-0.525F)), PartPose.offsetAndRotation(0.0F, 1.1F, 2.4F, -0.3927F, 0.0F, 0.0F));

        PartDefinition TailSecondary = TailPrimary.addOrReplaceChild("TailSecondary", CubeListBuilder.create(), PartPose.offset(0.0F, 2.0F, 4.5F));

        PartDefinition Base_r3 = TailSecondary.addOrReplaceChild("Base_r3", CubeListBuilder.create().texOffs(14, 32).addBox(-1.5F, -13.225F, 6.6F, 3.0F, 3.0F, 6.0F, new CubeDeformation(-0.575F)), PartPose.offsetAndRotation(0.0F, 10.9F, -8.4F, -0.1309F, 0.0F, 0.0F));

        PartDefinition TailTertiary = TailSecondary.addOrReplaceChild("TailTertiary", CubeListBuilder.create().texOffs(48, 68).addBox(-0.5F, -0.4304F, 0.7522F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.17F)), PartPose.offset(0.0F, 0.7F, 4.0F));

        PartDefinition TailQuaternary = TailTertiary.addOrReplaceChild("TailQuaternary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.4F, 4.6F));

        PartDefinition Base = TailQuaternary.addOrReplaceChild("Base", CubeListBuilder.create(), PartPose.offset(0.0F, -0.6557F, 2.2595F));

        PartDefinition Base_r4 = Base.addOrReplaceChild("Base_r4", CubeListBuilder.create().texOffs(78, 49).addBox(-0.5F, -0.58F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.07F)), PartPose.offsetAndRotation(0.0F, 0.1523F, -0.6189F, 1.5708F, -0.3349F, 1.5708F));

        PartDefinition Base_r5 = Base.addOrReplaceChild("Base_r5", CubeListBuilder.create().texOffs(58, 78).addBox(-0.5F, -0.58F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.067F)), PartPose.offsetAndRotation(0.0F, -2.3948F, 4.5036F, 1.5708F, -0.6839F, 1.5708F));

        PartDefinition Base_r6 = Base.addOrReplaceChild("Base_r6", CubeListBuilder.create().texOffs(50, 78).addBox(-0.5F, -0.58F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.067F)), PartPose.offsetAndRotation(0.0F, -6.4538F, 8.5375F, 1.5708F, -1.033F, 1.5708F));

        PartDefinition Base_r7 = Base.addOrReplaceChild("Base_r7", CubeListBuilder.create().texOffs(78, 58).addBox(-0.5F, -0.58F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.067F)), PartPose.offsetAndRotation(0.0F, -4.2069F, 6.6577F, 1.5708F, -0.8584F, 1.5708F));

        PartDefinition Base_r8 = Base.addOrReplaceChild("Base_r8", CubeListBuilder.create().texOffs(54, 78).addBox(-0.5F, -0.58F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.067F)), PartPose.offsetAndRotation(0.0F, -0.9245F, 2.1289F, 1.5708F, -0.5094F, 1.5708F));

        PartDefinition Left = TailQuaternary.addOrReplaceChild("Left", CubeListBuilder.create(), PartPose.offset(0.0F, -0.6557F, 2.2595F));

        PartDefinition feathers_r1 = Left.addOrReplaceChild("feathers_r1", CubeListBuilder.create().texOffs(8, 48).addBox(-0.54F, 14.72F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)), PartPose.offsetAndRotation(0.0F, 4.7671F, 4.1395F, 2.1914F, 1.074F, -0.8881F));

        PartDefinition feathers_r2 = Left.addOrReplaceChild("feathers_r2", CubeListBuilder.create().texOffs(76, 34).addBox(-0.54F, 13.82F, -1.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.05F))
                .texOffs(78, 43).addBox(-0.54F, 12.92F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.05F))
                .texOffs(56, 25).addBox(-0.54F, 12.02F, -2.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(-0.05F))
                .texOffs(50, 74).addBox(-0.54F, 11.12F, -3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(-0.05F)), PartPose.offsetAndRotation(0.0F, 3.7214F, 2.4277F, 2.0378F, 0.9598F, -1.0191F));

        PartDefinition feathers_r3 = Left.addOrReplaceChild("feathers_r3", CubeListBuilder.create().texOffs(70, 17).addBox(-0.54F, 10.22F, -3.5F, 1.0F, 1.0F, 4.0F, new CubeDeformation(-0.05F))
                .texOffs(70, 67).addBox(-0.54F, 9.32F, -4.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(-0.05F))
                .texOffs(0, 66).addBox(-0.54F, 8.42F, -4.5F, 1.0F, 1.0F, 5.0F, new CubeDeformation(-0.05F)), PartPose.offsetAndRotation(0.0F, 2.3809F, 0.9223F, 1.9059F, 0.8064F, -1.1213F));

        PartDefinition feathers_r4 = Left.addOrReplaceChild("feathers_r4", CubeListBuilder.create().texOffs(66, 43).addBox(-0.54F, 7.52F, -5.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(-0.05F))
                .texOffs(12, 67).addBox(-0.54F, 6.62F, -5.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(-0.05F))
                .texOffs(66, 37).addBox(-0.54F, 5.72F, -4.5F, 1.0F, 1.0F, 5.0F, new CubeDeformation(-0.05F))
                .texOffs(10, 73).addBox(-0.54F, 4.82F, -4.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(-0.05F)), PartPose.offsetAndRotation(0.0F, 1.1168F, 0.1405F, 1.8112F, 0.6467F, -1.1845F));

        PartDefinition feathers_r5 = Left.addOrReplaceChild("feathers_r5", CubeListBuilder.create().texOffs(70, 72).addBox(-0.54F, 3.92F, -3.5F, 1.0F, 1.0F, 4.0F, new CubeDeformation(-0.05F))
                .texOffs(74, 77).addBox(-0.54F, 3.02F, -3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(-0.05F))
                .texOffs(0, 77).addBox(-0.54F, 2.12F, -2.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(-0.05F))
                .texOffs(78, 40).addBox(-0.54F, 1.22F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.05F)), PartPose.offsetAndRotation(0.0F, 0.2479F, -0.0416F, 1.7372F, 0.4837F, -1.2242F));

        PartDefinition feathers_r6 = Left.addOrReplaceChild("feathers_r6", CubeListBuilder.create().texOffs(70, 22).addBox(-0.54F, 0.32F, -1.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.05F))
                .texOffs(0, 48).addBox(-0.54F, -0.58F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.675F, 0.3188F, -1.2486F));

        PartDefinition Right = TailQuaternary.addOrReplaceChild("Right", CubeListBuilder.create(), PartPose.offset(0.0F, -0.6557F, 2.2595F));

        PartDefinition feathers_r7 = Right.addOrReplaceChild("feathers_r7", CubeListBuilder.create().texOffs(64, 6).addBox(-0.5F, -0.58F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.067F)), PartPose.offsetAndRotation(0.0F, -9.0482F, 9.989F, 1.5708F, 1.1726F, -1.5708F));

        PartDefinition feathers_r8 = Right.addOrReplaceChild("feathers_r8", CubeListBuilder.create().texOffs(78, 37).addBox(-0.46F, 1.22F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.05F))
                .texOffs(76, 30).addBox(-0.46F, 2.12F, -2.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(-0.05F))
                .texOffs(66, 77).addBox(-0.46F, 3.02F, -3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(-0.05F))
                .texOffs(72, 53).addBox(-0.46F, 3.92F, -3.5F, 1.0F, 1.0F, 4.0F, new CubeDeformation(-0.05F)), PartPose.offsetAndRotation(0.0F, 0.2479F, -0.0416F, 1.7372F, -0.4837F, 1.2242F));

        PartDefinition feathers_r9 = Right.addOrReplaceChild("feathers_r9", CubeListBuilder.create().texOffs(0, 72).addBox(-0.46F, 9.32F, -4.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(-0.05F))
                .texOffs(68, 12).addBox(-0.46F, 10.22F, -3.5F, 1.0F, 1.0F, 4.0F, new CubeDeformation(-0.05F))
                .texOffs(64, 31).addBox(-0.46F, 8.42F, -4.5F, 1.0F, 1.0F, 5.0F, new CubeDeformation(-0.05F)), PartPose.offsetAndRotation(0.0F, 2.3809F, 0.9223F, 1.9059F, -0.8064F, 1.1213F));

        PartDefinition feathers_r10 = Right.addOrReplaceChild("feathers_r10", CubeListBuilder.create().texOffs(66, 74).addBox(-0.46F, 14.72F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)), PartPose.offsetAndRotation(0.0F, 4.7671F, 4.1395F, 2.1914F, -1.074F, 0.8881F));

        PartDefinition feathers_r11 = Right.addOrReplaceChild("feathers_r11", CubeListBuilder.create().texOffs(78, 12).addBox(-0.46F, 13.82F, -1.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.05F))
                .texOffs(78, 46).addBox(-0.46F, 12.92F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.05F))
                .texOffs(76, 0).addBox(-0.46F, 12.02F, -2.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(-0.05F))
                .texOffs(58, 74).addBox(-0.46F, 11.12F, -3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(-0.05F)), PartPose.offsetAndRotation(0.0F, 3.7214F, 2.4277F, 2.0378F, -0.9598F, 1.0191F));

        PartDefinition feathers_r12 = Right.addOrReplaceChild("feathers_r12", CubeListBuilder.create().texOffs(36, 68).addBox(-0.46F, 7.52F, -5.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(-0.05F))
                .texOffs(24, 68).addBox(-0.46F, 6.62F, -5.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(-0.05F))
                .texOffs(20, 74).addBox(-0.46F, 4.82F, -4.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(-0.05F))
                .texOffs(68, 6).addBox(-0.46F, 5.72F, -4.5F, 1.0F, 1.0F, 5.0F, new CubeDeformation(-0.05F)), PartPose.offsetAndRotation(0.0F, 1.1168F, 0.1405F, 1.8112F, -0.6467F, 1.1845F));

        PartDefinition feathers_r13 = Right.addOrReplaceChild("feathers_r13", CubeListBuilder.create().texOffs(4, 48).addBox(-0.46F, -0.58F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F))
                .texOffs(72, 58).addBox(-0.46F, 0.32F, -1.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.05F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.675F, -0.3188F, 1.2486F));

        PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 1.5F, 0.0F));

        PartDefinition RightArmFeathers = RightArm.addOrReplaceChild("RightArmFeathers", CubeListBuilder.create(), PartPose.offset(-0.5478F, 0.6459F, 1.7291F));

        PartDefinition feathers_r14 = RightArmFeathers.addOrReplaceChild("feathers_r14", CubeListBuilder.create().texOffs(12, 78).addBox(0.0F, 0.25F, 0.0F, 0.0F, 1.0F, 3.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-0.0272F, -2.8281F, -0.1545F, -0.0436F, 0.1745F, 0.0F));

        PartDefinition feathers_r15 = RightArmFeathers.addOrReplaceChild("feathers_r15", CubeListBuilder.create().texOffs(38, 74).addBox(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 4.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-0.0795F, -1.8743F, -0.4506F, -0.1745F, 0.1745F, 0.0F));

        PartDefinition feathers_r16 = RightArmFeathers.addOrReplaceChild("feathers_r16", CubeListBuilder.create().texOffs(68, 61).addBox(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 5.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-0.1317F, -0.9206F, -0.7467F, -0.2182F, 0.1745F, 0.0F));

        PartDefinition feathers_r17 = RightArmFeathers.addOrReplaceChild("feathers_r17", CubeListBuilder.create().texOffs(14, 59).addBox(0.0F, 0.25F, 0.0F, 0.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0832F, 5.1316F, 0.4718F, -0.5524F, 0.3034F, -0.0137F));

        PartDefinition feathers_r18 = RightArmFeathers.addOrReplaceChild("feathers_r18", CubeListBuilder.create().texOffs(58, 53).addBox(0.0F, 0.25F, 0.0F, 0.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.031F, 6.0853F, 0.1757F, -0.6135F, 0.3034F, -0.0137F));

        PartDefinition feathers_r19 = RightArmFeathers.addOrReplaceChild("feathers_r19", CubeListBuilder.create().texOffs(0, 58).addBox(0.0F, 0.25F, 0.0F, 0.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0212F, 7.039F, -0.1205F, -0.6906F, 0.3865F, -0.042F));

        PartDefinition feathers_r20 = RightArmFeathers.addOrReplaceChild("feathers_r20", CubeListBuilder.create().texOffs(56, 17).addBox(0.0F, 0.25F, 0.0F, 0.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0735F, 7.9927F, -0.4166F, -0.7605F, 0.3865F, -0.042F));

        PartDefinition feathers_r21 = RightArmFeathers.addOrReplaceChild("feathers_r21", CubeListBuilder.create().texOffs(54, 9).addBox(0.0F, 0.25F, 1.0F, 0.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1958F, 3.5764F, -1.1105F, -0.4451F, 0.2618F, 0.0F));

        PartDefinition feathers_r22 = RightArmFeathers.addOrReplaceChild("feathers_r22", CubeListBuilder.create().texOffs(44, 53).addBox(0.0F, 0.25F, 0.75F, 0.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1436F, 2.6227F, -0.8144F, -0.4189F, 0.2618F, 0.0F));

        PartDefinition feathers_r23 = RightArmFeathers.addOrReplaceChild("feathers_r23", CubeListBuilder.create().texOffs(30, 53).addBox(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1044F, 1.9074F, -0.5923F, -0.3578F, 0.2182F, 0.0F));

        PartDefinition feathers_r24 = RightArmFeathers.addOrReplaceChild("feathers_r24", CubeListBuilder.create().texOffs(56, 61).addBox(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0522F, 0.9537F, -0.2961F, -0.3316F, 0.2182F, 0.0F));

        PartDefinition feathers_r25 = RightArmFeathers.addOrReplaceChild("feathers_r25", CubeListBuilder.create().texOffs(60, 68).addBox(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3054F, 0.2182F, 0.0F));

        PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(24, 16).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 1.5F, 0.0F));

        PartDefinition LeftArmFeathers = LeftArm.addOrReplaceChild("LeftArmFeathers", CubeListBuilder.create(), PartPose.offset(0.5478F, 0.6459F, 1.7291F));

        PartDefinition feathers_r26 = LeftArmFeathers.addOrReplaceChild("feathers_r26", CubeListBuilder.create().texOffs(12, 78).mirror().addBox(0.0F, 0.25F, 0.0F, 0.0F, 1.0F, 3.0F, new CubeDeformation(-0.01F)).mirror(false), PartPose.offsetAndRotation(0.0272F, -2.8281F, -0.1545F, -0.0436F, -0.1745F, 0.0F));

        PartDefinition feathers_r27 = LeftArmFeathers.addOrReplaceChild("feathers_r27", CubeListBuilder.create().texOffs(38, 74).mirror().addBox(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 4.0F, new CubeDeformation(-0.01F)).mirror(false), PartPose.offsetAndRotation(0.0795F, -1.8743F, -0.4506F, -0.1745F, -0.1745F, 0.0F));

        PartDefinition feathers_r28 = LeftArmFeathers.addOrReplaceChild("feathers_r28", CubeListBuilder.create().texOffs(68, 61).mirror().addBox(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 5.0F, new CubeDeformation(-0.01F)).mirror(false), PartPose.offsetAndRotation(0.1317F, -0.9206F, -0.7467F, -0.2182F, -0.1745F, 0.0F));

        PartDefinition feathers_r29 = LeftArmFeathers.addOrReplaceChild("feathers_r29", CubeListBuilder.create().texOffs(14, 59).mirror().addBox(0.0F, 0.25F, 0.0F, 0.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.0832F, 5.1316F, 0.4718F, -0.5524F, -0.3034F, 0.0137F));

        PartDefinition feathers_r30 = LeftArmFeathers.addOrReplaceChild("feathers_r30", CubeListBuilder.create().texOffs(58, 53).mirror().addBox(0.0F, 0.25F, 0.0F, 0.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.031F, 6.0853F, 0.1757F, -0.6135F, -0.3034F, 0.0137F));

        PartDefinition feathers_r31 = LeftArmFeathers.addOrReplaceChild("feathers_r31", CubeListBuilder.create().texOffs(0, 58).mirror().addBox(0.0F, 0.25F, 0.0F, 0.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0212F, 7.039F, -0.1205F, -0.6906F, -0.3865F, 0.042F));

        PartDefinition feathers_r32 = LeftArmFeathers.addOrReplaceChild("feathers_r32", CubeListBuilder.create().texOffs(56, 17).mirror().addBox(0.0F, 0.25F, 0.0F, 0.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0735F, 7.9927F, -0.4166F, -0.7605F, -0.3865F, 0.042F));

        PartDefinition feathers_r33 = LeftArmFeathers.addOrReplaceChild("feathers_r33", CubeListBuilder.create().texOffs(54, 9).mirror().addBox(0.0F, 0.25F, 1.0F, 0.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.1958F, 3.5764F, -1.1105F, -0.4451F, -0.2618F, 0.0F));

        PartDefinition feathers_r34 = LeftArmFeathers.addOrReplaceChild("feathers_r34", CubeListBuilder.create().texOffs(44, 53).mirror().addBox(0.0F, 0.25F, 0.75F, 0.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.1436F, 2.6227F, -0.8144F, -0.4189F, -0.2618F, 0.0F));

        PartDefinition feathers_r35 = LeftArmFeathers.addOrReplaceChild("feathers_r35", CubeListBuilder.create().texOffs(30, 53).mirror().addBox(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.1044F, 1.9074F, -0.5923F, -0.3578F, -0.2182F, 0.0F));

        PartDefinition feathers_r36 = LeftArmFeathers.addOrReplaceChild("feathers_r36", CubeListBuilder.create().texOffs(56, 61).mirror().addBox(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0522F, 0.9537F, -0.2961F, -0.3316F, -0.2182F, 0.0F));

        PartDefinition feathers_r37 = LeftArmFeathers.addOrReplaceChild("feathers_r37", CubeListBuilder.create().texOffs(60, 68).mirror().addBox(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3054F, -0.2182F, 0.0F));

        PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offset(-2.5F, 10.5F, 0.0F));

        PartDefinition RightThigh_r1 = RightLeg.addOrReplaceChild("RightThigh_r1", CubeListBuilder.create().texOffs(32, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition RightLowerLeg = RightLeg.addOrReplaceChild("RightLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

        PartDefinition RightCalf_r1 = RightLowerLeg.addOrReplaceChild("RightCalf_r1", CubeListBuilder.create().texOffs(32, 43).addBox(-1.99F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

        PartDefinition RightFoot = RightLowerLeg.addOrReplaceChild("RightFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

        PartDefinition RightArch_r1 = RightFoot.addOrReplaceChild("RightArch_r1", CubeListBuilder.create().texOffs(50, 0).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(-0.155F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

        PartDefinition RightPad = RightFoot.addOrReplaceChild("RightPad", CubeListBuilder.create().texOffs(48, 39).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(-0.15F)), PartPose.offset(0.0F, 4.325F, -4.425F));

        PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(2.5F, 10.5F, 0.0F));

        PartDefinition LeftThigh_r1 = LeftLeg.addOrReplaceChild("LeftThigh_r1", CubeListBuilder.create().texOffs(38, 9).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition LeftLowerLeg = LeftLeg.addOrReplaceChild("LeftLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

        PartDefinition LeftCalf_r1 = LeftLowerLeg.addOrReplaceChild("LeftCalf_r1", CubeListBuilder.create().texOffs(48, 29).addBox(-2.01F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

        PartDefinition LeftFoot = LeftLowerLeg.addOrReplaceChild("LeftFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

        PartDefinition LeftArch_r1 = LeftFoot.addOrReplaceChild("LeftArch_r1", CubeListBuilder.create().texOffs(16, 50).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(-0.155F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

        PartDefinition LeftPad = LeftFoot.addOrReplaceChild("LeftPad", CubeListBuilder.create().texOffs(48, 46).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(-0.15F)), PartPose.offset(0.0F, 4.325F, -4.425F));

        return LayerDefinition.create(meshdefinition, 96, 96);
    }

    @Override
    public void prepareMobModel(AvaliEntity p_162861, float p_102862, float p_102863, float p_102864_) {
        this.prepareMobModel(animator, p_162861, p_102862, p_102863, p_102864_);
    }

    /* public PoseStack getPlacementCorrectors(CorrectorType type) {
         PoseStack corrector = AdvancedHumanoidModelInterface.super.getPlacementCorrectors(type);
         if (type.isArm())
             corrector.translate(-0.02f, 0.12f, 0.12f);
         return corrector;
     }
  */
    @Override
    public void setupHand(AvaliEntity entity) {
        animator.setupHand();
    }

    @Override
    public void setupAnim(@NotNull AvaliEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animator.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    public ModelPart getArm(HumanoidArm p_102852) {
        return p_102852 == HumanoidArm.LEFT ? this.LeftArm : this.RightArm;
    }

    public ModelPart getHead() {
        return this.Head;
    }

    public ModelPart getTorso() {
        return Torso;
    }

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
    public HumanoidAnimator<AvaliEntity, AvaliModel> getAnimator(AvaliEntity entity) {
        return animator;
    }

    public static class AvaliAnimationPresets {
        public static <T extends ChangedEntity, M extends AdvancedHumanoidModel<T>> Consumer<HumanoidAnimator<T, M>> AvaliLike(ModelPart head, ModelPart torso, ModelPart leftArm, ModelPart rightArm, ModelPart tail, List<ModelPart> tailJoints, ModelPart leftLeg, ModelPart leftLegLower, ModelPart leftFoot, ModelPart leftPad, ModelPart rightLeg, ModelPart rightLegLower, ModelPart rightFoot, ModelPart rightPad) {
            return (animator) -> animator.addPreset(dragonBipedal(leftLeg, leftLegLower, leftFoot, leftPad, rightLeg, rightLegLower, rightFoot, rightPad)).addPreset(AvaliUpperBody(head, torso, leftArm, rightArm)).addPreset(dragonTail(tail, tailJoints)).addAnimator(new DragonHeadInitAnimator<>(head)).addAnimator(new ArmSwimAnimator<>(leftArm, rightArm)).addAnimator(new ArmBobAnimator<>(leftArm, rightArm)).addAnimator(new ArmRideAnimator<>(leftArm, rightArm)).addAnimator(new AvaliFallFlyAnimator<>(rightArm, leftArm));
        }

        public static <T extends ChangedEntity, M extends AdvancedHumanoidModel<T>> Consumer<HumanoidAnimator<T, M>> AvaliUpperBody(ModelPart head, ModelPart torso, ModelPart leftArm, ModelPart rightArm) {
            return (animator) -> animator.setupHands(1, leftArm, rightArm).addAnimator(new HoldEntityAnimator<>(head, torso, leftArm, rightArm)).addAnimator(new AvaliUpperBodyInitAnimator<>(head, torso, leftArm, rightArm)).addAnimator(new DragonUpperBodyCrouchAnimator<>(head, torso, leftArm, rightArm)).addAnimator(new DragonUpperBodyAttackAnimator<>(head, torso, leftArm, rightArm)).addAnimator(new DragonUpperBodyStandAnimator<>(head, torso, leftArm, rightArm));
        }
    }
}