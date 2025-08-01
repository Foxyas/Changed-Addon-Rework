package net.foxyas.changedaddon.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.entity.simple.HaydenFennecFoxEntity;
import net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModelInterface;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HaydenFennecFoxModel extends AdvancedHumanoidModel<HaydenFennecFoxEntity> implements AdvancedHumanoidModelInterface<HaydenFennecFoxEntity, HaydenFennecFoxModel> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ChangedAddonMod.resourceLoc("hayden_fennec_fox"), "main");
    private final ModelPart RightLeg;
    private final ModelPart LeftLeg;
    private final ModelPart RightArm;
    private final ModelPart LeftArm;
    private final ModelPart Head;
    private final ModelPart Torso;
    private final ModelPart Tail;
    private final HumanoidAnimator<HaydenFennecFoxEntity, HaydenFennecFoxModel> animator;

    public HaydenFennecFoxModel(ModelPart root) {
        super(root);
        this.RightLeg = root.getChild("RightLeg");
        this.LeftLeg = root.getChild("LeftLeg");
        this.Head = root.getChild("Head");
        this.Torso = root.getChild("Torso");
        this.Tail = this.Torso.getChild("Tail");
        this.RightArm = root.getChild("RightArm");
        this.LeftArm = root.getChild("LeftArm");
        ModelPart tailPrimary = this.Tail.getChild("TailPrimary");
        ModelPart tailSecondary = tailPrimary.getChild("TailSecondary");
        ModelPart tailTertiary = tailSecondary.getChild("TailTertiary");
        ModelPart leftLowerLeg = this.LeftLeg.getChild("LeftLowerLeg");
        ModelPart leftFoot = leftLowerLeg.getChild("LeftFoot");
        ModelPart rightLowerLeg = this.RightLeg.getChild("RightLowerLeg");
        ModelPart rightFoot = rightLowerLeg.getChild("RightFoot");
        this.animator = HumanoidAnimator.of(this).hipOffset(-1.5F).addPreset(AnimatorPresets.wolfLike(this.Head, this.Head.getChild("LeftEar"), this.Head.getChild("RightEar"), this.Torso, this.LeftArm, this.RightArm, this.Tail, List.of(tailPrimary, tailSecondary, tailTertiary), this.LeftLeg, leftLowerLeg, leftFoot, leftFoot.getChild("LeftPad"), this.RightLeg, rightLowerLeg, rightFoot, rightFoot.getChild("RightPad")));
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(76, 40).addBox(-2.0F, -3.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(80, 50).addBox(-1.5F, -1.0F, -5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));

        PartDefinition Snout_r1 = Head.addOrReplaceChild("Snout_r1", CubeListBuilder.create().texOffs(50, 81).addBox(-1.0F, -29.625F, -0.95F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 26.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition RightEar = Head.addOrReplaceChild("RightEar", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.1F, -7.65F, -1.0F, -0.2618F, 0.0873F, -0.3054F));

        PartDefinition RightEarPivot = RightEar.addOrReplaceChild("RightEarPivot", CubeListBuilder.create().texOffs(76, 32).addBox(-2.9F, -4.2F, -1.0F, 4.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(70, 77).addBox(-2.3F, -1.85F, -0.3F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.04F))
                .texOffs(80, 52).addBox(-1.9F, -5.2F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(34, 81).addBox(-0.9F, -6.2F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(80, 70).addBox(-1.3F, -3.85F, -0.3F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.04F)), PartPose.offsetAndRotation(0.5F, -1.15F, 0.0F, -0.1309F, 0.5236F, -0.3491F));

        PartDefinition furRight = RightEarPivot.addOrReplaceChild("furRight", CubeListBuilder.create(), PartPose.offset(-0.5F, 1.75F, 0.3F));

        PartDefinition cube_r1 = furRight.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(22, 81).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.3002F)), PartPose.offsetAndRotation(-0.9F, -1.1F, -0.9F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r2 = furRight.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(16, 81).addBox(-1.0F, -2.0F, -1.0415F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.3002F)), PartPose.offsetAndRotation(0.2F, -1.1F, -0.3F, 0.0F, 0.8727F, 0.0F));

        PartDefinition cube_r3 = furRight.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(80, 73).addBox(-1.0F, -2.0F, -1.3415F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.3002F)), PartPose.offsetAndRotation(-1.2F, -1.1F, -0.1F, 0.0F, -0.8727F, 0.0F));

        PartDefinition cube_r4 = furRight.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(22, 81).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.3001F)), PartPose.offsetAndRotation(-0.9F, -0.7F, -1.2F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r5 = furRight.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(22, 81).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(-0.9F, -0.3F, -1.5F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r6 = furRight.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(16, 81).addBox(-1.0F, -2.0F, -0.9877F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.3001F)), PartPose.offsetAndRotation(0.3F, -0.7F, -0.5F, 0.0F, 0.8727F, 0.0F));

        PartDefinition cube_r7 = furRight.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(16, 81).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.4F, -0.2F, -0.6F, 0.0F, 0.8727F, 0.0F));

        PartDefinition cube_r8 = furRight.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(80, 73).addBox(-1.0F, -2.0F, -1.2876F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.3001F)), PartPose.offsetAndRotation(-1.3F, -0.7F, -0.3F, 0.0F, -0.8727F, 0.0F));

        PartDefinition cube_r9 = furRight.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(80, 73).addBox(-1.0F, -2.0F, -1.3F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(-1.4F, -0.2F, -0.4F, 0.0F, -0.8727F, 0.0F));

        PartDefinition LeftEar = Head.addOrReplaceChild("LeftEar", CubeListBuilder.create(), PartPose.offsetAndRotation(3.1F, -7.55F, -1.0F, -0.2618F, -0.0873F, 0.3054F));

        PartDefinition LeftEarPivot = LeftEar.addOrReplaceChild("LeftEarPivot", CubeListBuilder.create().texOffs(60, 77).addBox(-1.1F, -4.2F, -1.0F, 4.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(78, 77).addBox(-0.7F, -1.85F, -0.3F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.04F))
                .texOffs(80, 68).addBox(-1.1F, -5.2F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(40, 81).addBox(-1.1F, -6.2F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 81).addBox(-0.7F, -3.85F, -0.3F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.04F)), PartPose.offsetAndRotation(-0.5F, -1.25F, 0.0F, -0.1309F, -0.5236F, 0.3491F));

        PartDefinition furLeft = LeftEarPivot.addOrReplaceChild("furLeft", CubeListBuilder.create(), PartPose.offset(0.5F, 1.65F, 0.3F));

        PartDefinition cube_r10 = furLeft.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(22, 81).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.3002F)).mirror(false), PartPose.offsetAndRotation(0.9F, -1.1F, -0.9F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r11 = furLeft.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(16, 81).mirror().addBox(-1.0F, -2.0F, -1.0415F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.3002F)).mirror(false), PartPose.offsetAndRotation(-0.2F, -1.1F, -0.3F, 0.0F, -0.8727F, 0.0F));

        PartDefinition cube_r12 = furLeft.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(80, 73).mirror().addBox(-1.0F, -2.0F, -1.3415F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.3002F)).mirror(false), PartPose.offsetAndRotation(1.2F, -1.1F, -0.1F, 0.0F, 0.8727F, 0.0F));

        PartDefinition cube_r13 = furLeft.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(22, 81).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.3001F)).mirror(false), PartPose.offsetAndRotation(0.9F, -0.7F, -1.2F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r14 = furLeft.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(22, 81).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.3F)).mirror(false), PartPose.offsetAndRotation(0.9F, -0.3F, -1.5F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r15 = furLeft.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(16, 81).mirror().addBox(-1.0F, -2.0F, -0.9877F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.3001F)).mirror(false), PartPose.offsetAndRotation(-0.3F, -0.7F, -0.5F, 0.0F, -0.8727F, 0.0F));

        PartDefinition cube_r16 = furLeft.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(16, 81).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.3F)).mirror(false), PartPose.offsetAndRotation(-0.4F, -0.2F, -0.6F, 0.0F, -0.8727F, 0.0F));

        PartDefinition cube_r17 = furLeft.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(80, 73).mirror().addBox(-1.0F, -2.0F, -1.2876F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.3001F)).mirror(false), PartPose.offsetAndRotation(1.3F, -0.7F, -0.3F, 0.0F, 0.8727F, 0.0F));

        PartDefinition cube_r18 = furLeft.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(80, 73).mirror().addBox(-1.0F, -2.0F, -1.3F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.3F)).mirror(false), PartPose.offsetAndRotation(1.4F, -0.2F, -0.4F, 0.0F, 0.8727F, 0.0F));

        PartDefinition Hair = Head.addOrReplaceChild("Hair", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F))
                .texOffs(0, 32).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Torso = partdefinition.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));

        PartDefinition TorsoLayer = Torso.addOrReplaceChild("TorsoLayer", CubeListBuilder.create().texOffs(32, 16).addBox(-4.0F, -26.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.075F))
                .texOffs(32, 85).addBox(-4.0F, -26.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.095F)), PartPose.offset(0.0F, 26.0F, 0.0F));

        PartDefinition Tail = Torso.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offset(0.0F, 10.0F, 0.4F));

        PartDefinition TailPrimary = Tail.addOrReplaceChild("TailPrimary", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.1F, 0.1309F, 0.0F, 0.0F));

        PartDefinition Base_r1 = TailPrimary.addOrReplaceChild("Base_r1", CubeListBuilder.create().texOffs(64, 68).addBox(-2.0F, 0.75F, -1.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.1781F, 0.0F, 0.0F));

        PartDefinition TailSecondary = TailPrimary.addOrReplaceChild("TailSecondary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.25F, 5.6F));

        PartDefinition Base_r2 = TailSecondary.addOrReplaceChild("Base_r2", CubeListBuilder.create().texOffs(32, 32).addBox(-2.5F, -0.45F, -2.0F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.7F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 1.4835F, 0.0F, 0.0F));

        PartDefinition TailTertiary = TailSecondary.addOrReplaceChild("TailTertiary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.75F, 7.1F));

        PartDefinition Base_r3 = TailTertiary.addOrReplaceChild("Base_r3", CubeListBuilder.create().texOffs(72, 7).addBox(-2.0F, -0.7F, -1.95F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 1.8326F, 0.0F, 0.0F));

        PartDefinition TailQuaternary = TailTertiary.addOrReplaceChild("TailQuaternary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 1.0F));

        PartDefinition Base_r4 = TailQuaternary.addOrReplaceChild("Base_r4", CubeListBuilder.create().texOffs(72, 15).addBox(-2.0F, -1.2F, -1.95F, 4.0F, 4.0F, 4.0F, new CubeDeformation(-0.45F)), PartPose.offsetAndRotation(0.0F, -0.7F, 3.0F, 2.0071F, 0.0F, 0.0F));

        PartDefinition Bandana = Torso.addOrReplaceChild("Bandana", CubeListBuilder.create(), PartPose.offset(0.0F, -0.3F, 0.0F));

        PartDefinition neck = Bandana.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(52, 32).addBox(-4.0F, -28.3F, -2.0F, 8.0F, 2.0F, 4.0F, new CubeDeformation(0.25F))
                .texOffs(52, 38).addBox(-4.0F, -26.0F, -2.0F, 8.0F, 2.0F, 4.0F, new CubeDeformation(0.15F)), PartPose.offset(0.0F, 28.3F, 0.0F));

        PartDefinition knot = Bandana.addOrReplaceChild("knot", CubeListBuilder.create().texOffs(48, 32).addBox(-1.0F, -28.5F, 2.01F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.25F)), PartPose.offset(0.0F, 28.3F, 0.0F));

        PartDefinition right = knot.addOrReplaceChild("right", CubeListBuilder.create(), PartPose.offset(-0.9F, -26.5F, 5.0F));

        PartDefinition cube_r19 = right.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(72, 32).addBox(-1.0F, -2.0F, -0.99F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.9599F, 0.0F));

        PartDefinition cube_r20 = right.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(72, 37).addBox(-1.0F, -1.0F, -0.99F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -0.5F, 1.0F, 0.0F, -0.9599F, 0.0F));

        PartDefinition left = knot.addOrReplaceChild("left", CubeListBuilder.create(), PartPose.offset(2.4F, -27.0F, 6.0F));

        PartDefinition cube_r21 = left.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(74, 37).addBox(1.0F, -1.0F, -0.99F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.9599F, 0.0F));

        PartDefinition cube_r22 = left.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(72, 30).addBox(1.0F, -2.0F, -0.99F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 0.5F, -1.0F, 0.0F, 0.9599F, 0.0F));

        PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(0, 46).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 1.5F, 0.0F));

        PartDefinition RightArmLayer = RightArm.addOrReplaceChild("RightArmLayer", CubeListBuilder.create().texOffs(32, 45).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.025F))
                .texOffs(16, 84).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(1.0F, 0.0F, 0.0F));

        PartDefinition RightArmPawBeans = RightArm.addOrReplaceChild("RightArmPawBeans", CubeListBuilder.create().texOffs(80, 44).addBox(-2.0F, 9.525F, -0.6175F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(82, 54).addBox(-2.0F, 9.6F, -2.575F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F))
                .texOffs(82, 56).addBox(-1.0F, 9.6F, -2.575F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F))
                .texOffs(82, 58).addBox(-0.075F, 9.6F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F))
                .texOffs(82, 58).addBox(-2.925F, 9.6F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)), PartPose.offset(0.0F, -0.5F, 0.55F));

        PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(16, 46).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 1.5F, 0.0F));

        PartDefinition LeftArmLayer = LeftArm.addOrReplaceChild("LeftArmLayer", CubeListBuilder.create().texOffs(48, 45).addBox(0.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.025F))
                .texOffs(0, 84).addBox(0.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(-1.0F, 0.0F, 0.0F));

        PartDefinition LeftArmPawBeans = LeftArm.addOrReplaceChild("LeftArmPawBeans", CubeListBuilder.create().texOffs(8, 79).addBox(0.0F, 9.525F, -0.6175F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(54, 81).addBox(1.0F, 9.6F, -2.575F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F))
                .texOffs(8, 82).addBox(0.0F, 9.6F, -2.575F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F))
                .texOffs(12, 82).addBox(-0.925F, 9.6F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F))
                .texOffs(12, 82).addBox(1.925F, 9.6F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)), PartPose.offset(0.0F, -0.5F, 0.55F));

        PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(2.5F, 10.5F, 0.0F));

        PartDefinition LeftThighLayer_r1 = LeftLeg.addOrReplaceChild("LeftThighLayer_r1", CubeListBuilder.create().texOffs(32, 61).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.005F))
                .texOffs(56, 11).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition LeftLowerLeg = LeftLeg.addOrReplaceChild("LeftLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

        PartDefinition LeftCalfLayer_r1 = LeftLowerLeg.addOrReplaceChild("LeftCalfLayer_r1", CubeListBuilder.create().texOffs(16, 62).addBox(-2.01F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.005F))
                .texOffs(0, 62).addBox(-2.01F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

        PartDefinition LeftFoot = LeftLowerLeg.addOrReplaceChild("LeftFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

        PartDefinition LeftArchLayer_r1 = LeftFoot.addOrReplaceChild("LeftArchLayer_r1", CubeListBuilder.create().texOffs(32, 72).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.01F))
                .texOffs(72, 23).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

        PartDefinition LeftPad = LeftFoot.addOrReplaceChild("LeftPad", CubeListBuilder.create().texOffs(64, 61).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 72).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.005F)), PartPose.offset(0.0F, 4.325F, -4.425F));

        PartDefinition LeftPawBeans = LeftPad.addOrReplaceChild("LeftPawBeans", CubeListBuilder.create().texOffs(80, 47).mirror().addBox(-2.0F, 9.475F, -0.575F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.075F)).mirror(false)
                .texOffs(82, 60).mirror().addBox(-2.0F, 9.6F, -2.575F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)).mirror(false)
                .texOffs(82, 62).mirror().addBox(-1.0F, 9.6F, -2.575F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)).mirror(false)
                .texOffs(82, 64).mirror().addBox(-0.075F, 9.6F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)).mirror(false)
                .texOffs(82, 64).mirror().addBox(-2.925F, 9.6F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)).mirror(false), PartPose.offset(1.0F, -8.5F, 0.15F));

        PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offset(-2.5F, 10.5F, 0.0F));

        PartDefinition RightThighLayer_r1 = RightLeg.addOrReplaceChild("RightThighLayer_r1", CubeListBuilder.create().texOffs(48, 61).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.005F))
                .texOffs(56, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition RightLowerLeg = RightLeg.addOrReplaceChild("RightLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

        PartDefinition RightCalfLayer_r1 = RightLowerLeg.addOrReplaceChild("RightCalfLayer_r1", CubeListBuilder.create().texOffs(64, 44).addBox(-1.99F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.005F))
                .texOffs(56, 22).addBox(-1.99F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

        PartDefinition RightFoot = RightLowerLeg.addOrReplaceChild("RightFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

        PartDefinition RightArchLayer_r1 = RightFoot.addOrReplaceChild("RightArchLayer_r1", CubeListBuilder.create().texOffs(46, 72).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.01F))
                .texOffs(18, 72).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

        PartDefinition RightPad = RightFoot.addOrReplaceChild("RightPad", CubeListBuilder.create().texOffs(64, 54).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(72, 0).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.005F)), PartPose.offset(0.0F, 4.325F, -4.425F));

        PartDefinition RightPawBeans = RightPad.addOrReplaceChild("RightPawBeans", CubeListBuilder.create().texOffs(80, 47).addBox(0.0F, 9.475F, -0.575F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.075F))
                .texOffs(82, 60).addBox(1.0F, 9.6F, -2.575F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F))
                .texOffs(82, 62).addBox(0.0F, 9.6F, -2.575F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F))
                .texOffs(82, 64).addBox(-0.925F, 9.6F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F))
                .texOffs(82, 64).addBox(1.925F, 9.6F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F)), PartPose.offset(-1.0F, -8.5F, 0.15F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    public void prepareMobModel(@NotNull HaydenFennecFoxEntity p_102861_, float p_102862_, float p_102863_, float p_102864_) {
        this.prepareMobModel(this.animator, p_102861_, p_102862_, p_102863_, p_102864_);
    }

    public void setupHand(HaydenFennecFoxEntity entity) {
        this.animator.setupHand();
    }

    public void setupAnim(@NotNull HaydenFennecFoxEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.animator.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    public @NotNull ModelPart getArm(HumanoidArm p_102852_) {
        return p_102852_ == HumanoidArm.LEFT ? this.LeftArm : this.RightArm;
    }

    public ModelPart getLeg(HumanoidArm p_102852_) {
        return p_102852_ == HumanoidArm.LEFT ? this.LeftLeg : this.RightLeg;
    }

    public @NotNull ModelPart getHead() {
        return this.Head;
    }

    public ModelPart getTorso() {
        return this.Torso;
    }

    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.RightLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.LeftLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.Head.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.Torso.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.RightArm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.LeftArm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public HumanoidAnimator<HaydenFennecFoxEntity, HaydenFennecFoxModel> getAnimator(HaydenFennecFoxEntity entity) {
        return this.animator;
    }
}
