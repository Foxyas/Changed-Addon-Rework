package net.foxyas.changedaddon.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.entity.FemaleLuminarcticLeopardEntity;
import net.ltxprogrammer.changed.client.animations.Limb;
import net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModelInterface;
import net.ltxprogrammer.changed.client.tfanimations.HelperModel;
import net.ltxprogrammer.changed.client.tfanimations.TransfurHelper;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LuminarcticFemaleLeopardModel extends AdvancedHumanoidModel<FemaleLuminarcticLeopardEntity> implements AdvancedHumanoidModelInterface<FemaleLuminarcticLeopardEntity, LuminarcticFemaleLeopardModel> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("changed_addon", "luminarctic_female_leopard"), "main");
    private final ModelPart RightLeg;
    private final ModelPart LeftLeg;
    private final ModelPart Head;
    private final ModelPart Torso;
    private final ModelPart RightArm;
    private final ModelPart LeftArm;
    private final ModelPart RightArmFur;
    private final ModelPart LeftArmFur;
    private final ModelPart Tail;
    private final HumanoidAnimator<FemaleLuminarcticLeopardEntity, LuminarcticFemaleLeopardModel> animator;

    public float dodgeProgress = 0;
    public float partialTicks = 0;
    public boolean isReverse = false;

    public LuminarcticFemaleLeopardModel(ModelPart root) {
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

    @Nullable
    @Override
    public HelperModel getTransfurHelperModel(Limb limb) {
        if (limb == Limb.TORSO)
            return TransfurHelper.getFeminineTorsoAlt();
        return super.getTransfurHelperModel(limb);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(24, 11).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(24, 0).addBox(-2.0F, -3.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.5F, -1.0F, -5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Snout_r1 = Head.addOrReplaceChild("Snout_r1", CubeListBuilder.create().texOffs(4, 2).addBox(-1.0F, -29.625F, -0.95F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 26.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition feline_eyes = Head.addOrReplaceChild("feline_eyes", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_iris = feline_eyes.addOrReplaceChild("left_iris", CubeListBuilder.create().texOffs(44, 84).addBox(-2.0F, -5.1F, -4.401F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.35F))
                .texOffs(44, 84).addBox(-2.0F, -3.9F, -4.401F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.35F))
                .texOffs(44, 84).addBox(-2.0F, -5.0F, -4.35F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.3F))
                .texOffs(44, 84).addBox(-2.0F, -3.8F, -4.45F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F))
                .texOffs(44, 84).addBox(-2.0F, -5.2F, -4.45F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F))
                .texOffs(44, 84).addBox(-2.0F, -5.3F, -4.501F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.45F))
                .texOffs(44, 84).addBox(-2.0F, -3.7F, -4.501F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.45F)), PartPose.offset(3.0F, 0.0F, 0.0F));

        PartDefinition right_iris = feline_eyes.addOrReplaceChild("right_iris", CubeListBuilder.create().texOffs(40, 84).addBox(-2.0F, -5.1F, -4.401F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.35F))
                .texOffs(40, 84).addBox(-2.0F, -3.9F, -4.401F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.35F))
                .texOffs(40, 84).addBox(-2.0F, -5.0F, -4.35F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.3F))
                .texOffs(40, 84).addBox(-2.0F, -3.8F, -4.45F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F))
                .texOffs(40, 84).addBox(-2.0F, -5.2F, -4.45F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.4F))
                .texOffs(40, 84).addBox(-2.0F, -5.3F, -4.501F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.45F))
                .texOffs(40, 84).addBox(-2.0F, -3.7F, -4.501F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.45F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition RightEar = Head.addOrReplaceChild("RightEar", CubeListBuilder.create(), PartPose.offset(-2.5F, -5.0F, 0.0F));

        PartDefinition rightear_r1 = RightEar.addOrReplaceChild("rightear_r1", CubeListBuilder.create().texOffs(68, 10).mirror().addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(-0.0002F)).mirror(false), PartPose.offsetAndRotation(-1.7353F, -5.4987F, 0.5F, 0.0F, 0.0F, -1.2654F));

        PartDefinition rightear_r2 = RightEar.addOrReplaceChild("rightear_r2", CubeListBuilder.create().texOffs(0, 66).addBox(5.55F, -34.45F, -1.0F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, 29.25F, 0.0F, 0.0F, 0.0F, -0.3054F));

        PartDefinition LeftEar = Head.addOrReplaceChild("LeftEar", CubeListBuilder.create(), PartPose.offset(2.5F, -5.0F, 0.0F));

        PartDefinition leftear_r1 = LeftEar.addOrReplaceChild("leftear_r1", CubeListBuilder.create().texOffs(68, 10).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(-0.0002F)), PartPose.offsetAndRotation(1.7353F, -5.4987F, 0.5F, 0.0F, 0.0F, 1.2654F));

        PartDefinition leftear_r2 = LeftEar.addOrReplaceChild("leftear_r2", CubeListBuilder.create().texOffs(65, 3).addBox(-7.55F, -34.45F, -1.0F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 29.25F, 0.0F, 0.0F, 0.0F, 0.3054F));

        PartDefinition Hair = Head.addOrReplaceChild("Hair", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -33.0F, -4.0F, 8.0F, 11.0F, 8.0F, new CubeDeformation(0.2F))
                .texOffs(0, 19).addBox(-4.0F, -33.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.35F)), PartPose.offset(0.0F, 25.0F, 0.0F));

        PartDefinition CheekCrystals = Head.addOrReplaceChild("CheekCrystals", CubeListBuilder.create(), PartPose.offset(0.0F, 0.3F, 0.0F));

        PartDefinition RightCheekCrystal = CheekCrystals.addOrReplaceChild("RightCheekCrystal", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = RightCheekCrystal.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(64, 89).addBox(-0.5F, -1.0F, -3.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.9107F, -0.8024F, -0.5F, -1.5708F, -0.4363F, 0.0F));

        PartDefinition cube_r2 = RightCheekCrystal.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(58, 92).mirror().addBox(0.25F, -0.75F, -3.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-4.5F, 0.3F, -0.5F, -1.5708F, -0.4363F, 0.0F));

        PartDefinition RightCrystal = RightCheekCrystal.addOrReplaceChild("RightCrystal", CubeListBuilder.create(), PartPose.offset(0.0F, 24.2F, 1.0F));

        PartDefinition RightCrystal_r1 = RightCrystal.addOrReplaceChild("RightCrystal_r1", CubeListBuilder.create().texOffs(76, 93).addBox(-3.9135F, -28.8F, -3.8937F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.8F, -1.0F, 0.0F, 1.1345F, 0.0F));

        PartDefinition LeftCheekCrystal = CheekCrystals.addOrReplaceChild("LeftCheekCrystal", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r3 = LeftCheekCrystal.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(76, 89).addBox(-0.5F, -1.0F, -3.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.9107F, -0.8024F, -0.5F, -1.5708F, 0.4363F, 0.0F));

        PartDefinition cube_r4 = LeftCheekCrystal.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(70, 92).addBox(-1.25F, -0.75F, -3.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(4.5F, 0.3F, -0.5F, -1.5708F, 0.4363F, 0.0F));

        PartDefinition LeftCrystal = LeftCheekCrystal.addOrReplaceChild("LeftCrystal", CubeListBuilder.create(), PartPose.offset(-1.5F, 24.2F, 0.6F));

        PartDefinition LeftCrystal_r1 = LeftCrystal.addOrReplaceChild("LeftCrystal_r1", CubeListBuilder.create().texOffs(64, 93).addBox(1.0845F, -28.8F, -3.7813F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 0.8F, -0.6F, 0.0F, -1.1345F, 0.0F));

        PartDefinition Torso = partdefinition.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(0, 37).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(-4.0F, 4.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(-0.3F))
                .texOffs(42, 27).addBox(-4.0F, 8.6F, -2.0F, 8.0F, 3.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Tail = Torso.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offset(0.0F, 10.0F, 0.0F));

        PartDefinition TailPrimary = Tail.addOrReplaceChild("TailPrimary", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition Base_r1 = TailPrimary.addOrReplaceChild("Base_r1", CubeListBuilder.create().texOffs(0, 57).addBox(-2.0F, 0.75F, -1.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.0908F, 0.0F, 0.0F));

        PartDefinition TailSecondary = TailPrimary.addOrReplaceChild("TailSecondary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.25F, 4.5F));

        PartDefinition Base_r2 = TailSecondary.addOrReplaceChild("Base_r2", CubeListBuilder.create().texOffs(0, 46).addBox(-2.5F, -0.45F, -2.5F, 5.0F, 6.0F, 5.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 1.3526F, 0.0F, 0.0F));

        PartDefinition TailTertiary = TailSecondary.addOrReplaceChild("TailTertiary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.75F, 4.5F));

        PartDefinition Base_r3 = TailTertiary.addOrReplaceChild("Base_r3", CubeListBuilder.create().texOffs(27, 32).addBox(-2.5F, 4.55F, -3.3F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.25F, -4.5F, 1.5272F, 0.0F, 0.0F));

        PartDefinition TailQuaternary = TailTertiary.addOrReplaceChild("TailQuaternary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 7.5F));

        PartDefinition Base_r4 = TailQuaternary.addOrReplaceChild("Base_r4", CubeListBuilder.create().texOffs(60, 63).addBox(-2.0F, 5.5F, -3.8F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.15F)), PartPose.offsetAndRotation(0.0F, -1.0F, -5.5F, 1.7017F, 0.0F, 0.0F));

        PartDefinition NeckFur = Torso.addOrReplaceChild("NeckFur", CubeListBuilder.create(), PartPose.offset(0.0F, -0.5F, 0.0F));

        PartDefinition Back = NeckFur.addOrReplaceChild("Back", CubeListBuilder.create(), PartPose.offset(0.0F, -0.25F, 0.25F));

        PartDefinition fur_r1 = Back.addOrReplaceChild("fur_r1", CubeListBuilder.create().texOffs(25, 74).mirror().addBox(1.6904F, -0.4529F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offsetAndRotation(0.1848F, 2.5802F, 1.292F, 0.5516F, -0.1395F, -2.9193F));

        PartDefinition fur_r2 = Back.addOrReplaceChild("fur_r2", CubeListBuilder.create().texOffs(25, 74).addBox(-3.6904F, -0.4529F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(-0.1848F, 2.5802F, 1.292F, 0.5516F, 0.1395F, 2.9193F));

        PartDefinition fur_r3 = Back.addOrReplaceChild("fur_r3", CubeListBuilder.create().texOffs(25, 74).mirror().addBox(1.6904F, -0.4529F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offsetAndRotation(0.1848F, 3.5802F, 1.292F, 0.5516F, -0.1395F, -2.9193F));

        PartDefinition fur_r4 = Back.addOrReplaceChild("fur_r4", CubeListBuilder.create().texOffs(25, 74).addBox(-3.6904F, -0.4529F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(-0.1848F, 3.5802F, 1.292F, 0.5516F, 0.1395F, 2.9193F));

        PartDefinition fur_r5 = Back.addOrReplaceChild("fur_r5", CubeListBuilder.create().texOffs(27, 73).addBox(-3.6904F, -1.4529F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0652F, 3.0802F, 1.292F, 0.5516F, 0.1395F, 2.9193F));

        PartDefinition fur_r6 = Back.addOrReplaceChild("fur_r6", CubeListBuilder.create().texOffs(27, 73).mirror().addBox(-0.3096F, -1.4529F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-0.0652F, 3.0802F, 1.292F, 0.5516F, -0.1395F, -2.9193F));

        PartDefinition fur_r7 = Back.addOrReplaceChild("fur_r7", CubeListBuilder.create().texOffs(27, 73).mirror().addBox(-0.3096F, -1.4529F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-0.0652F, 2.0802F, 1.292F, 0.5516F, -0.1395F, -2.9193F));

        PartDefinition fur_r8 = Back.addOrReplaceChild("fur_r8", CubeListBuilder.create().texOffs(24, 76).addBox(-1.9107F, 0.0F, -0.75F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 76).addBox(-1.9107F, 1.0F, 0.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-0.0893F, 1.3976F, 2.0F, -0.5672F, 0.0F, 0.0F));

        PartDefinition fur_r9 = Back.addOrReplaceChild("fur_r9", CubeListBuilder.create().texOffs(24, 76).addBox(-1.9107F, 1.0F, 0.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-0.0893F, 0.8976F, 2.0F, -0.5672F, 0.0F, 0.0F));

        PartDefinition fur_r10 = Back.addOrReplaceChild("fur_r10", CubeListBuilder.create().texOffs(27, 73).addBox(-3.6904F, -1.4529F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0652F, 2.0802F, 1.292F, 0.5516F, 0.1395F, 2.9193F));

        PartDefinition Front = NeckFur.addOrReplaceChild("Front", CubeListBuilder.create(), PartPose.offset(0.0F, 0.75F, -0.25F));

        PartDefinition fur_r11 = Front.addOrReplaceChild("fur_r11", CubeListBuilder.create().texOffs(31, 66).addBox(-3.6904F, -1.4529F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0652F, 1.5802F, -1.542F, -0.5516F, -0.1395F, 2.9193F));

        PartDefinition fur_r12 = Front.addOrReplaceChild("fur_r12", CubeListBuilder.create().texOffs(31, 66).mirror().addBox(1.6904F, -0.4529F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offsetAndRotation(0.1848F, 2.0802F, -1.542F, -0.5516F, 0.1395F, -2.9193F));

        PartDefinition fur_r13 = Front.addOrReplaceChild("fur_r13", CubeListBuilder.create().texOffs(31, 66).addBox(-3.6904F, -0.4529F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(-0.1848F, 2.0802F, -1.542F, -0.5516F, -0.1395F, 2.9193F));

        PartDefinition fur_r14 = Front.addOrReplaceChild("fur_r14", CubeListBuilder.create().texOffs(31, 66).mirror().addBox(1.6904F, -0.4529F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offsetAndRotation(0.1848F, 3.0802F, -1.542F, -0.5516F, 0.1395F, -2.9193F));

        PartDefinition fur_r15 = Front.addOrReplaceChild("fur_r15", CubeListBuilder.create().texOffs(31, 66).addBox(-3.6904F, -0.4529F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(-0.1848F, 3.0802F, -1.542F, -0.5516F, -0.1395F, 2.9193F));

        PartDefinition fur_r16 = Front.addOrReplaceChild("fur_r16", CubeListBuilder.create().texOffs(31, 66).addBox(-3.6904F, -1.4529F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0652F, 2.5802F, -1.542F, -0.5516F, -0.1395F, 2.9193F));

        PartDefinition fur_r17 = Front.addOrReplaceChild("fur_r17", CubeListBuilder.create().texOffs(31, 66).mirror().addBox(-0.3096F, -1.4529F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-0.0652F, 2.5802F, -1.542F, -0.5516F, 0.1395F, -2.9193F));

        PartDefinition fur_r18 = Front.addOrReplaceChild("fur_r18", CubeListBuilder.create().texOffs(31, 66).mirror().addBox(-0.3096F, -1.4529F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-0.0652F, 1.5802F, -1.542F, -0.5516F, 0.1395F, -2.9193F));

        PartDefinition fur_r19 = Front.addOrReplaceChild("fur_r19", CubeListBuilder.create().texOffs(32, 66).addBox(-1.9107F, 0.0F, -0.25F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(32, 66).addBox(-1.9107F, 1.0F, -1.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-0.0893F, 0.8976F, -2.25F, 0.5672F, 0.0F, 0.0F));

        PartDefinition fur_r20 = Front.addOrReplaceChild("fur_r20", CubeListBuilder.create().texOffs(32, 66).addBox(-1.9107F, 1.0F, -1.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-0.0893F, 0.3976F, -2.25F, 0.5672F, 0.0F, 0.0F));

        PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(43, 41).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition RightFur = RightArm.addOrReplaceChild("RightFur", CubeListBuilder.create().texOffs(80, 44).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.15F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition RightPawBeans = RightArm.addOrReplaceChild("RightPawBeans", CubeListBuilder.create().texOffs(0, 93).mirror().addBox(-2.0F, 9.475F, -0.375F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.075F)).mirror(false)
                .texOffs(0, 89).mirror().addBox(-2.8F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false)
                .texOffs(0, 87).mirror().addBox(-1.5F, 9.475F, -1.875F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false)
                .texOffs(0, 91).mirror().addBox(-0.225F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false), PartPose.offset(0.0F, -0.5F, 0.0F));

        PartDefinition RightArmCrystals = RightArm.addOrReplaceChild("RightArmCrystals", CubeListBuilder.create(), PartPose.offsetAndRotation(0.5F, 0.5F, 0.0F, 0.0F, 0.0F, -0.2618F));

        PartDefinition CrystalZAxis_r1 = RightArmCrystals.addOrReplaceChild("CrystalZAxis_r1", CubeListBuilder.create().texOffs(56, 69).addBox(0.0F, -4.0F, -2.0F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.3F, -0.9F, 0.0F, 0.0F, 0.0F, -0.6109F));

        PartDefinition CrystalXAxis_r1 = RightArmCrystals.addOrReplaceChild("CrystalXAxis_r1", CubeListBuilder.create().texOffs(56, 69).addBox(0.0F, -4.0F, -2.0F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.3F, -0.9F, 0.0F, 0.0F, 1.5708F, -0.6109F));

        PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(20, 45).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 2.0F, 0.0F));

        PartDefinition LeftFur = LeftArm.addOrReplaceChild("LeftFur", CubeListBuilder.create().texOffs(80, 26).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.15F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition LeftPawBeans = LeftArm.addOrReplaceChild("LeftPawBeans", CubeListBuilder.create().texOffs(8, 93).mirror().addBox(0.0F, 9.975F, -0.375F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.075F)).mirror(false)
                .texOffs(8, 89).mirror().addBox(1.8F, 9.975F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false)
                .texOffs(8, 87).mirror().addBox(0.5F, 9.975F, -1.875F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false)
                .texOffs(8, 91).mirror().addBox(-0.775F, 9.975F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));

        PartDefinition LeftArmCrystals = LeftArm.addOrReplaceChild("LeftArmCrystals", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.5F, 0.5F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition CrystalZAxis_r2 = LeftArmCrystals.addOrReplaceChild("CrystalZAxis_r2", CubeListBuilder.create().texOffs(56, 73).mirror().addBox(0.0F, -4.0F, -2.0F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.3F, -0.9F, 0.0F, 0.0F, 0.0F, 0.6109F));

        PartDefinition CrystalXAxis_r2 = LeftArmCrystals.addOrReplaceChild("CrystalXAxis_r2", CubeListBuilder.create().texOffs(56, 73).mirror().addBox(0.0F, -4.0F, -2.0F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.3F, -0.9F, 0.0F, 0.0F, -1.5708F, 0.6109F));

        PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offset(-2.4F, 10.5F, 0.0F));

        PartDefinition RightThigh_r1 = RightLeg.addOrReplaceChild("RightThigh_r1", CubeListBuilder.create().texOffs(55, 34).addBox(-1.9F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(-0.1F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition RightLowerLeg = RightLeg.addOrReplaceChild("RightLowerLeg", CubeListBuilder.create(), PartPose.offset(-0.1F, 6.375F, -3.45F));

        PartDefinition RightCalf_r1 = RightLowerLeg.addOrReplaceChild("RightCalf_r1", CubeListBuilder.create().texOffs(56, 17).addBox(-1.9F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

        PartDefinition RightFoot = RightLowerLeg.addOrReplaceChild("RightFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

        PartDefinition RightArch_r1 = RightFoot.addOrReplaceChild("RightArch_r1", CubeListBuilder.create().texOffs(46, 63).addBox(-1.9F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

        PartDefinition RightPad = RightFoot.addOrReplaceChild("RightPad", CubeListBuilder.create().texOffs(59, 45).addBox(-1.9F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

        PartDefinition RightFootPawBeans = RightPad.addOrReplaceChild("RightFootPawBeans", CubeListBuilder.create().texOffs(16, 93).mirror().addBox(-2.0F, 9.475F, -0.375F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.075F)).mirror(false)
                .texOffs(16, 89).mirror().addBox(-2.8F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false)
                .texOffs(16, 87).mirror().addBox(-1.5F, 9.475F, -1.875F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false)
                .texOffs(16, 91).mirror().addBox(-0.225F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false), PartPose.offset(1.0F, -8.5F, -0.05F));

        PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(2.4F, 10.5F, 0.0F));

        PartDefinition LeftThigh_r1 = LeftLeg.addOrReplaceChild("LeftThigh_r1", CubeListBuilder.create().texOffs(52, 6).addBox(-2.1F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.1F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition LeftLowerLeg = LeftLeg.addOrReplaceChild("LeftLowerLeg", CubeListBuilder.create(), PartPose.offset(0.1F, 6.375F, -3.45F));

        PartDefinition LeftCalf_r1 = LeftLowerLeg.addOrReplaceChild("LeftCalf_r1", CubeListBuilder.create().texOffs(55, 53).addBox(-2.1F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

        PartDefinition LeftFoot = LeftLowerLeg.addOrReplaceChild("LeftFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

        PartDefinition LeftArch_r1 = LeftFoot.addOrReplaceChild("LeftArch_r1", CubeListBuilder.create().texOffs(16, 61).addBox(-2.1F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

        PartDefinition LeftPad = LeftFoot.addOrReplaceChild("LeftPad", CubeListBuilder.create().texOffs(31, 57).addBox(-2.1F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

        PartDefinition LeftFootPawBeans = LeftPad.addOrReplaceChild("LeftFootPawBeans", CubeListBuilder.create().texOffs(24, 93).mirror().addBox(0.0F, 9.475F, -0.375F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.075F)).mirror(false)
                .texOffs(24, 89).mirror().addBox(1.8F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false)
                .texOffs(24, 87).mirror().addBox(0.5F, 9.475F, -1.875F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false)
                .texOffs(24, 91).mirror().addBox(-0.775F, 9.475F, -1.625F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.075F)).mirror(false), PartPose.offset(-1.0F, -8.5F, -0.05F));

        return LayerDefinition.create(meshdefinition, 96, 96);
    }

    @Override
    public void prepareMobModel(FemaleLuminarcticLeopardEntity p_162861, float p_102862, float p_102863, float p_102864_) {
        this.prepareMobModel(animator, p_162861, p_102862, p_102863, p_102864_);

		// Cabeça
		this.Head.x = 0;
		this.Head.y = 0;
		this.Head.z = 0;
		this.Head.xRot = 0;
		this.Head.yRot = 0;
		this.Head.zRot = 0;

		// Tronco
		this.Torso.x = 0;
		this.Torso.y = 0;
		this.Torso.z = 0;
		this.Torso.xRot = 0;
		this.Torso.yRot = 0;
		this.Torso.zRot = 0;

		// Braço esquerdo
		this.LeftArm.x = 0;
		this.LeftArm.y = 0;
		this.LeftArm.z = 0;
		this.LeftArm.xRot = 0;
		this.LeftArm.yRot = 0;
		this.LeftArm.zRot = 0;

		// Braço direito
		this.RightArm.x = 0;
		this.RightArm.y = 0;
		this.RightArm.z = 0;
		this.RightArm.xRot = 0;
		this.RightArm.yRot = 0;
		this.RightArm.zRot = 0;

		// Perna esquerda
		this.LeftLeg.x = 0;
		this.LeftLeg.y = 0;
		this.LeftLeg.z = 0;
		this.LeftLeg.xRot = 0;
		this.LeftLeg.yRot = 0;
		this.LeftLeg.zRot = 0;

		// Perna direita
		this.RightLeg.x = 0;
		this.RightLeg.y = 0;
		this.RightLeg.z = 0;
		this.RightLeg.xRot = 0;
		this.RightLeg.yRot = 0;
		this.RightLeg.zRot = 0;
	}

    //public PoseStack getPlacementCorrectors(CorrectorType type) {
    //     PoseStack corrector = AdvancedHumanoidModelInterface.super.getPlacementCorrectors(type);
    //     if (type.isArm())
    //         corrector.translate(-0.02f, 0.12f, 0.12f);
    //     return corrector;
    //}


    @Override
    public void setupHand(FemaleLuminarcticLeopardEntity entity) {
        animator.setupHand();
    }


    @Override
    public void setupAnim(@NotNull FemaleLuminarcticLeopardEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        animator.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		if (dodgeProgress > 0) {
			playDodgeAnim(entity.getDodgeType(), dodgeProgress);
		}

        //CarryAbilityAnimation.playAnimation(entity, this);
    }

	public void playDodgeAnim(int dodgeType, float progress) {
		if (dodgeType == 1) {
			// Head
			this.Head.xRot = (float) (Math.toRadians(0.0F) * progress) * (isReverse ? -1 : 1);
			this.Head.yRot = (float) (Math.toRadians(-35.0F) * progress) * (isReverse ? -1 : 1);
			this.Head.zRot = (float) (Math.toRadians(0.0F) * progress) * (isReverse ? -1 : 1);

			// RightArm
			this.RightArm.xRot = (float) (Math.toRadians(-58.2981F) * progress);
			this.RightArm.yRot = (float) (Math.toRadians(72.5894F) * progress);
			this.RightArm.zRot = (float) (Math.toRadians(13.897F) * progress);
			this.RightArm.x += -0.3F * progress;
			this.RightArm.y += -1.0F * progress;
			this.RightArm.z += -1.4F * progress;

			// LeftArm
			this.LeftArm.xRot = (float) Math.toRadians(-58.2981F) * progress;
			this.LeftArm.yRot = (float) Math.toRadians(-72.5894F) * progress;
			this.LeftArm.zRot = (float) Math.toRadians(-13.897F) * progress;
			this.LeftArm.x += -0.3F * progress;
			this.LeftArm.y += -1.0F * progress;
			this.LeftArm.z += -1.4F * progress;

			// RightLeg
			this.RightLeg.xRot += (float) Math.toRadians(0.0F) * progress;
			this.RightLeg.yRot += (float) Math.toRadians(5.0F) * progress;
			this.RightLeg.zRot += (float) Math.toRadians(0.0F) * progress;

			// LeftLeg
			this.LeftLeg.xRot += (float) Math.toRadians(0.0F) * progress;
			this.LeftLeg.yRot += (float) Math.toRadians(-32.5F) * progress;
			this.LeftLeg.zRot += (float) Math.toRadians(0.0F) * progress ;

			// Torso
			this.Torso.xRot += (float) Math.toRadians(0.0F) * progress;
			this.Torso.yRot += (float) Math.toRadians(0.0F) * progress;
			this.Torso.zRot += (float) Math.toRadians(0.0F) * progress;

		} else if (dodgeType == 2) {
			// Head
			this.Head.xRot += (float) Math.toRadians(16.0F) * progress;
			this.Head.yRot += (float) Math.toRadians(14.0F) * progress * (isReverse ? -1 : 1);
			this.Head.zRot += (float) Math.toRadians(-3.0F) * progress * (isReverse ? -1 : 1);
			this.Head.x += 0.0F * progress;
			this.Head.y += 0.0F * progress;
			this.Head.z += 0.25F * progress;

			// Torso
			this.Torso.xRot += (float) Math.toRadians(-7.0F) * progress;
			this.Torso.yRot += (float) Math.toRadians(10.0F) * progress;
			this.Torso.zRot += (float) Math.toRadians(0.0F) * progress;
			this.Torso.x += 0.0F * progress;
			this.Torso.y += 0.0F * progress;
			this.Torso.z += 0.2F * progress;

			// RightArm
			this.RightArm.xRot += (float) Math.toRadians(10.0F) * progress;
			this.RightArm.yRot += (float) Math.toRadians(10.0F) * progress;
			this.RightArm.zRot += (float) Math.toRadians(-5.0F) * progress;
			this.RightArm.x += 0.0F * progress;
			this.RightArm.y += 0.0F * progress;
			this.RightArm.z += 1.0F * progress;

			// LeftArm
			this.LeftArm.xRot += (float) Math.toRadians(-16.0F) * progress;
			this.LeftArm.yRot += (float) Math.toRadians(-7.0F) * progress;
			this.LeftArm.zRot += (float) Math.toRadians(0.0F) * progress;
			this.LeftArm.x += 0.0F * progress;
			this.LeftArm.y += 0.0F * progress;
			this.LeftArm.z += -2.0F * progress;

			// RightLeg
			this.RightLeg.xRot += (float) Math.toRadians(15.0F) * progress;
			this.RightLeg.yRot += (float) Math.toRadians(0.0F) * progress;
			this.RightLeg.zRot += (float) Math.toRadians(0.0F) * progress;
			this.RightLeg.x += 0.0F * progress;
			this.RightLeg.y += 0.0F * progress;
			this.RightLeg.z += -0.9F * progress;

			// LeftLeg
			this.LeftLeg.xRot += (float) Math.toRadians(4.0F) * progress;
			this.LeftLeg.yRot += (float) Math.toRadians(0.0F) * progress;
			this.LeftLeg.zRot += (float) Math.toRadians(0.0F) * progress;
			this.LeftLeg.x += 0.0F * progress;
			this.LeftLeg.y += 0.0F * progress;
			this.LeftLeg.z += -1.9F * progress;
		} else {
			// Head
			this.Head.xRot += (float) (Math.toRadians(0.0F) * progress) * (isReverse ? -1 : 1);
			this.Head.yRot += (float) (Math.toRadians(-35.0F) * progress) * (isReverse ? -1 : 1);
			this.Head.zRot += (float) (Math.toRadians(0.0F) * progress) * (isReverse ? -1 : 1);

			// RightArm
			this.RightArm.xRot += (float) (Math.toRadians(-58.2981F) * progress);
			this.RightArm.yRot += (float) (Math.toRadians(72.5894F) * progress);
			this.RightArm.zRot += (float) (Math.toRadians(13.897F) * progress);
			this.RightArm.x += -0.3F * progress;
			this.RightArm.y += -1.0F * progress;
			this.RightArm.z += -1.4F * progress;

			// LeftArm
			this.LeftArm.xRot += (float) Math.toRadians(-58.2981F) * progress;
			this.LeftArm.yRot += (float) Math.toRadians(-72.5894F) * progress;
			this.LeftArm.zRot += (float) Math.toRadians(-13.897F) * progress;
			this.LeftArm.x += -0.3F * progress;
			this.LeftArm.y += -1.0F * progress;
			this.LeftArm.z += -1.4F * progress;

			// RightLeg
			this.RightLeg.xRot += (float) Math.toRadians(0.0F) * progress;
			this.RightLeg.yRot += (float) Math.toRadians(5.0F) * progress;
			this.RightLeg.zRot += (float) Math.toRadians(0.0F) * progress;

			// LeftLeg
			this.LeftLeg.xRot += (float) Math.toRadians(0.0F) * progress;
			this.LeftLeg.yRot += (float) Math.toRadians(-32.5F) * progress;
			this.LeftLeg.zRot += (float) Math.toRadians(0.0F) * progress;

			// Torso
			this.Torso.xRot += (float) Math.toRadians(0.0F) * progress;
			this.Torso.yRot += (float) Math.toRadians(0.0F) * progress;
			this.Torso.zRot += (float) Math.toRadians(0.0F) * progress;
		}
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
    public HumanoidAnimator<FemaleLuminarcticLeopardEntity, LuminarcticFemaleLeopardModel> getAnimator(FemaleLuminarcticLeopardEntity entity) {
        return animator;
    }
}
