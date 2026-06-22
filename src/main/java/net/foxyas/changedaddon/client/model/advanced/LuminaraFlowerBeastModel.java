package net.foxyas.changedaddon.client.model.advanced;

// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.model.animations.ChangedAddonAnimationsPresets;
import net.foxyas.changedaddon.client.model.animations.DragonBigWingCreativeFlyAnimator;
import net.foxyas.changedaddon.entity.advanced.LuminaraFlowerBeastEntity;
import net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.tail.DragonTailCreativeFlyAnimator;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets.dragonTail;

public class LuminaraFlowerBeastModel extends AdvancedHumanoidModel<LuminaraFlowerBeastEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = ChangedAddonMod.layerLocation("luminara_flower_beast", "main");
    private final ModelPart Head;
    private final ModelPart LeftEar;
    private final ModelPart LeftEarPivot;
    private final ModelPart RightEar;
    private final ModelPart RightEarPivot;
    private final ModelPart Torso;
    private final ModelPart rightWing;
    private final ModelPart rightWingRoot;
    private final ModelPart rightSecondaries;
    private final ModelPart rightTertiaries;
    private final ModelPart reftWing;
    private final ModelPart leftWingRoot;
    private final ModelPart leftSecondaries;
    private final ModelPart leftTertiaries;
    private final ModelPart bigTail;
    private final ModelPart bigTailPrimary;
    private final ModelPart bigTailSecondary;
    private final ModelPart bigTailTertiary;
    private final ModelPart bigTailQuaternary;
    private final ModelPart tipFlowerTail;
    private final ModelPart tipFlowerTailPrimary;
    private final ModelPart tipFlowerTailSecondary;
    private final ModelPart tipFlowerTailTertiary;
    private final ModelPart tail;
    private final ModelPart tailPrimary;
    private final ModelPart tailSecondary;
    private final ModelPart tailTertiary;
    private final ModelPart RightArm;
    private final ModelPart LeftArm;
    private final ModelPart RightLeg;
    private final ModelPart RightLowerLeg;
    private final ModelPart RightFoot;
    private final ModelPart RightPad;
    private final ModelPart LeftLeg;
    private final ModelPart LeftLowerLeg;
    private final ModelPart LeftFoot;
    private final ModelPart LeftPad;
    private final HumanoidAnimator<LuminaraFlowerBeastEntity, LuminaraFlowerBeastModel> animatorHyperWingedForm;
    private final HumanoidAnimator<LuminaraFlowerBeastEntity, LuminaraFlowerBeastModel> animatorWingedForm;
    private final HumanoidAnimator<LuminaraFlowerBeastEntity, LuminaraFlowerBeastModel> animatorNormalForm;
    public boolean shouldHaveBigWings = false;

    public LuminaraFlowerBeastModel(ModelPart root) {
        super(root);
        this.Head = root.getChild("Head");
        this.LeftEar = this.Head.getChild("LeftEar");
        this.LeftEarPivot = this.LeftEar.getChild("LeftEarPivot");
        this.RightEar = this.Head.getChild("RightEar");
        this.RightEarPivot = this.RightEar.getChild("RightEarPivot");
        this.Torso = root.getChild("Torso");
        this.rightWing = this.Torso.getChild("RightWing");
        this.rightWingRoot = this.rightWing.getChild("rightWingRoot");
        this.rightSecondaries = this.rightWingRoot.getChild("rightSecondaries");
        this.rightTertiaries = this.rightSecondaries.getChild("rightTertiaries");
        this.reftWing = this.Torso.getChild("LeftWing");
        this.leftWingRoot = this.reftWing.getChild("leftWingRoot");
        this.leftSecondaries = this.leftWingRoot.getChild("leftSecondaries");
        this.leftTertiaries = this.leftSecondaries.getChild("leftTertiaries");
        this.bigTail = this.Torso.getChild("BigTail");
        this.bigTailPrimary = this.bigTail.getChild("BigTailPrimary");
        this.bigTailSecondary = this.bigTailPrimary.getChild("BigTailSecondary");
        this.bigTailTertiary = this.bigTailSecondary.getChild("BigTailTertiary");
        this.bigTailQuaternary = this.bigTailTertiary.getChild("BigTailQuaternary");
        this.tipFlowerTail = this.bigTailQuaternary.getChild("TipFlowerTail");
        this.tipFlowerTailPrimary = this.tipFlowerTail.getChild("TipFlowerTailPrimary");
        this.tipFlowerTailSecondary = this.tipFlowerTailPrimary.getChild("TipFlowerTailSecondary");
        this.tipFlowerTailTertiary = this.tipFlowerTailSecondary.getChild("TipFlowerTailTertiary");
        this.tail = this.Torso.getChild("Tail");
        this.tailPrimary = this.tail.getChild("TailPrimary");
        this.tailSecondary = this.tailPrimary.getChild("TailSecondary");
        this.tailTertiary = this.tailSecondary.getChild("TailTertiary");
        this.RightArm = root.getChild("RightArm");
        this.LeftArm = root.getChild("LeftArm");
        this.RightLeg = root.getChild("RightLeg");
        this.RightLowerLeg = this.RightLeg.getChild("RightLowerLeg");
        this.RightFoot = this.RightLowerLeg.getChild("RightFoot");
        this.RightPad = this.RightFoot.getChild("RightPad");
        this.LeftLeg = root.getChild("LeftLeg");
        this.LeftLowerLeg = this.LeftLeg.getChild("LeftLowerLeg");
        this.LeftFoot = this.LeftLowerLeg.getChild("LeftFoot");
        this.LeftPad = this.LeftFoot.getChild("LeftPad");

        this.animatorNormalForm = HumanoidAnimator.of(this).hipOffset(-1.5F).addPreset(AnimatorPresets.dragonLike(this.Head,
                this.Torso,
                this.LeftArm,
                this.RightArm,
                this.tail,
                List.of(tailPrimary, tailSecondary, tailTertiary),
                this.LeftLeg,
                LeftLowerLeg,
                LeftFoot,
                LeftPad,
                this.RightLeg,
                RightLowerLeg,
                RightFoot,
                RightPad)
        );

        this.animatorWingedForm = HumanoidAnimator.of(this).hipOffset(-1.5F).addPreset(AnimatorPresets.wingedDragonLike(this.Head,
                        this.Torso,
                        this.LeftArm,
                        this.RightArm,
                        this.tail,
                        List.of(tailPrimary, tailSecondary, tailTertiary),
                        this.LeftLeg,
                        LeftLowerLeg,
                        LeftFoot,
                        LeftPad,
                        this.RightLeg,
                        RightLowerLeg,
                        RightFoot,
                        RightPad,
                        leftWingRoot,
                        leftSecondaries,
                        leftTertiaries,
                        rightWingRoot,
                        rightSecondaries,
                        rightTertiaries)
                ).addPreset(
                        dragonTail(bigTail, List.of(bigTailPrimary,
                                bigTailSecondary,
                                bigTailTertiary,
                                bigTailQuaternary,
                                tipFlowerTail,
                                tipFlowerTailPrimary,
                                tipFlowerTailSecondary,
                                tipFlowerTailTertiary)))
                .addAnimator(new DragonTailCreativeFlyAnimator<>(bigTail, List.of(bigTailPrimary,
                        bigTailSecondary,
                        bigTailTertiary,
                        bigTailQuaternary,
                        tipFlowerTail,
                        tipFlowerTailPrimary,
                        tipFlowerTailSecondary,
                        tipFlowerTailTertiary))
                ).addAnimator(new DragonBigWingCreativeFlyAnimator<>(leftWingRoot,
                        leftSecondaries,
                        leftTertiaries,
                        rightWingRoot,
                        rightSecondaries,
                        rightTertiaries));


        this.animatorHyperWingedForm = HumanoidAnimator.of(this).hipOffset(-1.5F).addPreset(ChangedAddonAnimationsPresets.bigWingedDragonLike(this.Head,
                        this.Torso,
                        this.LeftArm,
                        this.RightArm,
                        this.tail,
                        List.of(tailPrimary, tailSecondary, tailTertiary),
                        this.LeftLeg,
                        LeftLowerLeg,
                        LeftFoot,
                        LeftPad,
                        this.RightLeg,
                        RightLowerLeg,
                        RightFoot,
                        RightPad,
                        leftWingRoot,
                        leftSecondaries,
                        leftTertiaries,
                        rightWingRoot,
                        rightSecondaries,
                        rightTertiaries)
                ).addPreset(
                        dragonTail(bigTail, List.of(bigTailPrimary,
                                bigTailSecondary,
                                bigTailTertiary,
                                bigTailQuaternary,
                                tipFlowerTail,
                                tipFlowerTailPrimary,
                                tipFlowerTailSecondary,
                                tipFlowerTailTertiary)))
                .addAnimator(new DragonTailCreativeFlyAnimator<>(bigTail, List.of(bigTailPrimary,
                        bigTailSecondary,
                        bigTailTertiary,
                        bigTailQuaternary,
                        tipFlowerTail,
                        tipFlowerTailPrimary,
                        tipFlowerTailSecondary,
                        tipFlowerTailTertiary))
                ).addAnimator(new DragonBigWingCreativeFlyAnimator<>(leftWingRoot,
                        leftSecondaries,
                        leftTertiaries,
                        rightWingRoot,
                        rightSecondaries,
                        rightTertiaries));

    }

    @SuppressWarnings("unused")
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(15, 32).addBox(-2.0F, -3.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(24, 22).addBox(-1.5F, -1.0F, -5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));

        PartDefinition Part_r1 = Head.addOrReplaceChild("Part_r1", CubeListBuilder.create().texOffs(8, 6).mirror().addBox(0.0F, -2.0F, 0.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offsetAndRotation(-3.75F, -0.75F, -1.75F, -0.0033F, -0.5732F, -0.1554F));

        PartDefinition Part_r2 = Head.addOrReplaceChild("Part_r2", CubeListBuilder.create().texOffs(8, 11).mirror().addBox(0.0F, -2.0F, 0.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-4.0F, -2.5F, -1.5F, 0.0F, -0.5672F, 0.0F));

        PartDefinition Part_r3 = Head.addOrReplaceChild("Part_r3", CubeListBuilder.create().texOffs(8, 2).mirror().addBox(0.0F, -1.0F, 0.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-3.25F, -0.25F, -2.0F, -0.0501F, -0.5681F, -0.3109F));

        PartDefinition Horn_r1 = Head.addOrReplaceChild("Horn_r1", CubeListBuilder.create().texOffs(32, 11).mirror().addBox(-1.0F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.05F)).mirror(false), PartPose.offsetAndRotation(-1.25F, -7.5F, -2.5F, 0.6684F, -0.2539F, -0.3733F));

        PartDefinition Part_r4 = Head.addOrReplaceChild("Part_r4", CubeListBuilder.create().texOffs(0, 2).addBox(-1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(3.25F, -0.25F, -2.0F, -0.0501F, 0.5681F, 0.3109F));

        PartDefinition Part_r5 = Head.addOrReplaceChild("Part_r5", CubeListBuilder.create().texOffs(0, 6).addBox(-1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(3.75F, -0.75F, -1.75F, -0.0033F, 0.5732F, 0.1554F));

        PartDefinition Part_r6 = Head.addOrReplaceChild("Part_r6", CubeListBuilder.create().texOffs(0, 11).addBox(-1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(4.0F, -2.5F, -1.5F, 0.0F, 0.5672F, 0.0F));

        PartDefinition Horn_r2 = Head.addOrReplaceChild("Horn_r2", CubeListBuilder.create().texOffs(22, 11).addBox(0.0F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.05F)), PartPose.offsetAndRotation(1.25F, -7.5F, -2.5F, 0.6684F, 0.2539F, 0.3733F));

        PartDefinition LeftEar = Head.addOrReplaceChild("LeftEar", CubeListBuilder.create(), PartPose.offsetAndRotation(2.75F, -7.25F, 0.25F, -0.5401F, -0.2332F, 0.1457F));

        PartDefinition LeftEarPivot = LeftEar.addOrReplaceChild("LeftEarPivot", CubeListBuilder.create().texOffs(24, 16).addBox(-1.1F, -2.2F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(24, 12).addBox(-1.35F, -3.95F, -1.25F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.25F))
                .texOffs(26, 10).addBox(-1.1F, -4.7F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(27, 8).addBox(-1.1F, -5.2F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -1.25F, 0.0F, -0.1309F, -0.5236F, 0.3491F));

        PartDefinition RightEar = Head.addOrReplaceChild("RightEar", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.75F, -7.25F, 0.25F, -0.5401F, 0.2332F, -0.1457F));

        PartDefinition RightEarPivot = RightEar.addOrReplaceChild("RightEarPivot", CubeListBuilder.create().texOffs(34, 16).mirror().addBox(-1.9F, -2.2F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(34, 12).mirror().addBox(-1.65F, -3.95F, -1.25F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.25F)).mirror(false)
                .texOffs(36, 10).mirror().addBox(-0.9F, -4.7F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(37, 8).mirror().addBox(0.1F, -5.2F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.5F, -1.25F, 0.0F, -0.1309F, 0.5236F, -0.3491F));

        PartDefinition Torso = partdefinition.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(28, 28).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));

        PartDefinition cube_r1 = Torso.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(69, 32).mirror().addBox(-1.5F, 0.0F, -3.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offsetAndRotation(-1.5F, -0.25F, 2.0F, -2.0944F, 0.2618F, 3.1416F));

        PartDefinition cube_r2 = Torso.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(69, 24).mirror().addBox(-1.5F, 0.0F, -3.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offsetAndRotation(-1.5F, -0.25F, -2.0F, 1.0472F, 0.2618F, 0.0F));

        PartDefinition cube_r3 = Torso.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(69, 28).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(1.5F, -0.25F, 2.0F, -2.0944F, -0.2618F, -3.1416F));

        PartDefinition cube_r4 = Torso.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(69, 20).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(1.5F, -0.25F, -2.0F, 1.0472F, -0.2618F, 0.0F));

        PartDefinition RightWing = Torso.addOrReplaceChild("RightWing", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, 5.0F, 2.0F, 0.0F, 0.48F, 0.0F));

        PartDefinition rightWingRoot = RightWing.addOrReplaceChild("rightWingRoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r5 = rightWingRoot.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(61, 52).addBox(-25.975F, -4.475F, 1.65F, 7.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 20.0F, -2.0F, 0.0F, 0.0F, 1.2654F));

        PartDefinition cube_r6 = rightWingRoot.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(47, 54).addBox(-25.075F, -12.7F, 1.2F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 20.0F, -2.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition cube_r7 = rightWingRoot.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(53, 63).addBox(-12.775F, -19.75F, 1.2F, 5.0F, 2.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(2.0F, 20.0F, -2.0F, 0.0F, 0.0F, 0.3491F));

        PartDefinition rightSecondaries = rightWingRoot.addOrReplaceChild("rightSecondaries", CubeListBuilder.create().texOffs(64, 44).addBox(-0.2F, -0.475F, -0.3F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(-7.3F, -7.0F, -0.5F, 0.0F, 0.0F, 0.5236F));

        PartDefinition cube_r8 = rightSecondaries.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(68, 45).addBox(1.025F, -22.55F, 1.2F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.3F, 27.0F, -1.5F, 0.0F, 0.0F, -0.48F));

        PartDefinition cube_r9 = rightSecondaries.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(32, 65).addBox(-22.4F, 10.625F, 1.651F, 9.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.3F, 27.0F, -1.5F, 0.0F, 0.0F, 1.8326F));

        PartDefinition cube_r10 = rightSecondaries.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(56, 57).addBox(-24.525F, -13.85F, 1.648F, 9.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.3F, 27.0F, -1.5F, 0.0F, 0.0F, 0.7418F));

        PartDefinition rightTertiaries = rightSecondaries.addOrReplaceChild("rightTertiaries", CubeListBuilder.create(), PartPose.offsetAndRotation(0.3F, 0.0F, 0.0F, 0.0F, 0.0F, 0.9599F));

        PartDefinition cube_r11 = rightTertiaries.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(72, 44).addBox(2.3F, -22.5F, 1.2F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, 27.0F, -1.5F, 0.0F, 0.0F, -0.5236F));

        PartDefinition cube_r12 = rightTertiaries.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(58, 66).addBox(-10.15F, -26.2F, 1.2F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(9.0F, 27.0F, -1.5F, 0.0F, 0.0F, 0.0436F));

        PartDefinition cube_r13 = rightTertiaries.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(32, 70).addBox(-25.125F, -10.525F, 1.649F, 9.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, 27.0F, -1.5F, 0.0F, 0.0F, 0.8727F));

        PartDefinition LeftWing = Torso.addOrReplaceChild("LeftWing", CubeListBuilder.create(), PartPose.offsetAndRotation(2.0F, 5.0F, 2.0F, 0.0F, -0.48F, 0.0F));

        PartDefinition leftWingRoot = LeftWing.addOrReplaceChild("leftWingRoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r14 = leftWingRoot.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(18, 66).addBox(18.975F, -4.475F, 1.65F, 7.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 20.0F, -2.0F, 0.0F, 0.0F, -1.2654F));

        PartDefinition cube_r15 = leftWingRoot.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(27, 62).addBox(19.075F, -12.7F, 1.2F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 20.0F, -2.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition cube_r16 = leftWingRoot.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(41, 62).addBox(7.775F, -19.75F, 1.2F, 5.0F, 2.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-2.0F, 20.0F, -2.0F, 0.0F, 0.0F, -0.3491F));

        PartDefinition leftSecondaries = leftWingRoot.addOrReplaceChild("leftSecondaries", CubeListBuilder.create().texOffs(76, 44).addBox(-0.8F, -0.475F, -0.3F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(7.3F, -7.0F, -0.5F, 0.0F, 0.0F, -0.5236F));

        PartDefinition cube_r17 = leftSecondaries.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(54, 66).addBox(-2.025F, -22.55F, 1.2F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.3F, 27.0F, -1.5F, 0.0F, 0.0F, 0.48F));

        PartDefinition cube_r18 = leftSecondaries.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(0, 66).addBox(15.525F, -13.85F, 1.648F, 9.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.3F, 27.0F, -1.5F, 0.0F, 0.0F, -0.7418F));

        PartDefinition cube_r19 = leftSecondaries.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(38, 57).addBox(13.4F, 10.625F, 1.651F, 9.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.3F, 27.0F, -1.5F, 0.0F, 0.0F, -1.8326F));

        PartDefinition leftTertiaries = leftSecondaries.addOrReplaceChild("leftTertiaries", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.3F, 0.0F, 0.0F, 0.0F, 0.0F, -0.9599F));

        PartDefinition cube_r20 = leftTertiaries.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(80, 44).addBox(-3.3F, -22.5F, 1.2F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, 27.0F, -1.5F, 0.0F, 0.0F, 0.5236F));

        PartDefinition cube_r21 = leftTertiaries.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(0, 72).addBox(16.125F, -10.525F, 1.649F, 9.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, 27.0F, -1.5F, 0.0F, 0.0F, -0.8727F));

        PartDefinition cube_r22 = leftTertiaries.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(50, 65).addBox(9.15F, -26.2F, 1.2F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(-9.0F, 27.0F, -1.5F, 0.0F, 0.0F, -0.0436F));

        PartDefinition BigTail = Torso.addOrReplaceChild("BigTail", CubeListBuilder.create(), PartPose.offset(0.0F, 11.0F, 0.0F));

        PartDefinition BigTailPrimary = BigTail.addOrReplaceChild("BigTailPrimary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Base_r1 = BigTailPrimary.addOrReplaceChild("Base_r1", CubeListBuilder.create().texOffs(0, 77).addBox(-2.0F, -2.9F, 0.4F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition BigTailSecondary = BigTailPrimary.addOrReplaceChild("BigTailSecondary", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 3.5F));

        PartDefinition Base_r2 = BigTailSecondary.addOrReplaceChild("Base_r2", CubeListBuilder.create().texOffs(16, 76).addBox(-1.5F, -1.4F, -2.7F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 1.0F, 2.5F, -0.3927F, 0.0F, 0.0F));

        PartDefinition BigTailTertiary = BigTailSecondary.addOrReplaceChild("BigTailTertiary", CubeListBuilder.create(), PartPose.offset(0.0F, 2.5F, 5.0F));

        PartDefinition Base_r3 = BigTailTertiary.addOrReplaceChild("Base_r3", CubeListBuilder.create().texOffs(0, 85).addBox(-1.5F, -13.225F, 6.6F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.08F)), PartPose.offsetAndRotation(0.0F, 10.5F, -8.5F, -0.1309F, 0.0F, 0.0F));

        PartDefinition BigTailQuaternary = BigTailTertiary.addOrReplaceChild("BigTailQuaternary", CubeListBuilder.create(), PartPose.offset(0.0F, 0.5F, 4.5F));

        PartDefinition Base_r4 = BigTailQuaternary.addOrReplaceChild("Base_r4", CubeListBuilder.create().texOffs(19, 85).addBox(-1.0F, -10.45F, 13.5F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.45F)), PartPose.offsetAndRotation(0.0F, 10.0F, -13.0F, 0.0436F, 0.0F, 0.0F));

        PartDefinition TipFlowerTail = BigTailQuaternary.addOrReplaceChild("TipFlowerTail", CubeListBuilder.create().texOffs(0, 32).addBox(-1.5F, -1.75F, 0.75F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.05F, 3.2F, 0.0436F, 0.0F, 0.0F));

        PartDefinition cube_r23 = TipFlowerTail.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(70, 12).mirror().addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(-0.15F)).mirror(false), PartPose.offsetAndRotation(-0.5F, -1.25F, 1.25F, -0.4234F, 0.0689F, 2.4629F));

        PartDefinition cube_r24 = TipFlowerTail.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(70, 16).mirror().addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(-0.15F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 0.0F, 1.25F, -0.4329F, -0.0195F, 1.2073F));

        PartDefinition cube_r25 = TipFlowerTail.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(14, 36).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-0.75F, 1.0F, 2.0F, 0.3864F, 0.4021F, -0.1204F));

        PartDefinition cube_r26 = TipFlowerTail.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(8, 41).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-1.5F, -0.75F, 2.0F, -0.5051F, 0.5086F, -0.6993F));

        PartDefinition cube_r27 = TipFlowerTail.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(8, 32).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.75F, 1.0F, 2.0F, 0.3864F, -0.4021F, 0.1204F));

        PartDefinition cube_r28 = TipFlowerTail.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(8, 35).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(1.5F, -0.75F, 2.0F, -0.5051F, -0.5086F, 0.6993F));

        PartDefinition cube_r29 = TipFlowerTail.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(8, 38).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0F, -1.75F, 2.0F, -0.4582F, 0.4417F, 0.6846F));

        PartDefinition cube_r30 = TipFlowerTail.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(70, 8).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.5F, -1.25F, 1.25F, -0.4234F, -0.0689F, -2.4629F));

        PartDefinition cube_r31 = TipFlowerTail.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(70, 4).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(1.0F, 0.0F, 1.25F, -0.4329F, 0.0195F, -1.2073F));

        PartDefinition cube_r32 = TipFlowerTail.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(70, 0).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.0F, 0.5F, 1.25F, -0.5236F, 0.0F, 0.0F));

        PartDefinition TipFlowerTailPrimary = TipFlowerTail.addOrReplaceChild("TipFlowerTailPrimary", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -0.25F, 1.5F, 0.1309F, 0.0F, 0.0F));

        PartDefinition cube_r33 = TipFlowerTailPrimary.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(77, 5).addBox(-0.5F, 0.5F, -1.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.0F, -1.5F, 0.25F, 0.0873F, 0.0F, 0.0F));

        PartDefinition cube_r34 = TipFlowerTailPrimary.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(77, 0).addBox(-0.5F, 0.5F, -1.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.0F, -0.75F, 0.25F, -0.0873F, 0.0F, 0.0F));

        PartDefinition TipFlowerTailSecondary = TipFlowerTailPrimary.addOrReplaceChild("TipFlowerTailSecondary", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -0.0785F, 2.9888F, 0.1745F, 0.0F, 0.0F));

        PartDefinition cube_r35 = TipFlowerTailSecondary.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(83, 1).addBox(-0.5F, 0.5F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(0.0F, -0.4215F, 0.5112F, -0.0873F, 0.0F, 0.0F));

        PartDefinition cube_r36 = TipFlowerTailSecondary.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(83, 6).addBox(-0.5F, 0.5F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(0.0F, -1.6715F, 0.7612F, 0.0873F, 0.0F, 0.0F));

        PartDefinition TipFlowerTailTertiary = TipFlowerTailSecondary.addOrReplaceChild("TipFlowerTailTertiary", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 1.25F, 0.3491F, 0.0F, 0.0F));

        PartDefinition cube_r37 = TipFlowerTailTertiary.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(77, 15).addBox(-0.5F, 0.5F, -1.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, -0.4215F, 0.2612F, -0.0873F, 0.0F, 0.0F));

        PartDefinition cube_r38 = TipFlowerTailTertiary.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(82, 16).addBox(-0.5F, -0.35F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.0F, 0.5785F, 1.5112F, 0.1309F, 0.0F, 0.0F));

        PartDefinition cube_r39 = TipFlowerTailTertiary.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(82, 12).addBox(-0.5F, -0.55F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.0F, -0.6715F, 2.2612F, 0.3054F, 0.0F, 0.0F));

        PartDefinition cube_r40 = TipFlowerTailTertiary.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(77, 11).addBox(-0.5F, 0.5F, -1.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, -1.6715F, 1.0112F, 0.0873F, 0.0F, 0.0F));

        PartDefinition Tail = Torso.addOrReplaceChild("Tail", CubeListBuilder.create().texOffs(0, 32).addBox(-1.5F, -1.75F, 0.75F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.5F, 0.75F));

        PartDefinition cube_r41 = Tail.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(70, 16).mirror().addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(-0.15F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 0.0F, 1.25F, -0.4329F, -0.0195F, 1.2073F));

        PartDefinition cube_r42 = Tail.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(70, 12).mirror().addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(-0.15F)).mirror(false), PartPose.offsetAndRotation(-0.5F, -1.25F, 1.25F, -0.4234F, 0.0689F, 2.4629F));

        PartDefinition cube_r43 = Tail.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(14, 36).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-0.75F, 1.0F, 2.0F, 0.3864F, 0.4021F, -0.1204F));

        PartDefinition cube_r44 = Tail.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(8, 41).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-1.5F, -0.75F, 2.0F, -0.5051F, 0.5086F, -0.6993F));

        PartDefinition cube_r45 = Tail.addOrReplaceChild("cube_r45", CubeListBuilder.create().texOffs(8, 32).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.75F, 1.0F, 2.0F, 0.3864F, -0.4021F, 0.1204F));

        PartDefinition cube_r46 = Tail.addOrReplaceChild("cube_r46", CubeListBuilder.create().texOffs(8, 35).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(1.5F, -0.75F, 2.0F, -0.5051F, -0.5086F, 0.6993F));

        PartDefinition cube_r47 = Tail.addOrReplaceChild("cube_r47", CubeListBuilder.create().texOffs(8, 38).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0F, -1.75F, 2.0F, -0.4582F, 0.4417F, 0.6846F));

        PartDefinition cube_r48 = Tail.addOrReplaceChild("cube_r48", CubeListBuilder.create().texOffs(70, 8).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.5F, -1.25F, 1.25F, -0.4234F, -0.0689F, -2.4629F));

        PartDefinition cube_r49 = Tail.addOrReplaceChild("cube_r49", CubeListBuilder.create().texOffs(70, 4).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(1.0F, 0.0F, 1.25F, -0.4329F, 0.0195F, -1.2073F));

        PartDefinition cube_r50 = Tail.addOrReplaceChild("cube_r50", CubeListBuilder.create().texOffs(70, 0).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.0F, 0.5F, 1.25F, -0.5236F, 0.0F, 0.0F));

        PartDefinition TailPrimary = Tail.addOrReplaceChild("TailPrimary", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -0.25F, 1.5F, 0.1309F, 0.0F, 0.0F));

        PartDefinition cube_r51 = TailPrimary.addOrReplaceChild("cube_r51", CubeListBuilder.create().texOffs(77, 5).addBox(-0.5F, 0.5F, -1.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.0F, -1.5F, 0.25F, 0.0873F, 0.0F, 0.0F));

        PartDefinition cube_r52 = TailPrimary.addOrReplaceChild("cube_r52", CubeListBuilder.create().texOffs(77, 0).addBox(-0.5F, 0.5F, -1.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.0F, -0.75F, 0.25F, -0.0873F, 0.0F, 0.0F));

        PartDefinition TailSecondary = TailPrimary.addOrReplaceChild("TailSecondary", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -0.0785F, 2.9888F, 0.1745F, 0.0F, 0.0F));

        PartDefinition cube_r53 = TailSecondary.addOrReplaceChild("cube_r53", CubeListBuilder.create().texOffs(83, 1).addBox(-0.5F, 0.5F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(0.0F, -0.4215F, 0.5112F, -0.0873F, 0.0F, 0.0F));

        PartDefinition cube_r54 = TailSecondary.addOrReplaceChild("cube_r54", CubeListBuilder.create().texOffs(83, 6).addBox(-0.5F, 0.5F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(0.0F, -1.6715F, 0.7612F, 0.0873F, 0.0F, 0.0F));

        PartDefinition TailTertiary = TailSecondary.addOrReplaceChild("TailTertiary", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 1.25F, 0.3491F, 0.0F, 0.0F));

        PartDefinition cube_r55 = TailTertiary.addOrReplaceChild("cube_r55", CubeListBuilder.create().texOffs(77, 15).addBox(-0.5F, 0.5F, -1.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, -0.4215F, 0.2612F, -0.0873F, 0.0F, 0.0F));

        PartDefinition cube_r56 = TailTertiary.addOrReplaceChild("cube_r56", CubeListBuilder.create().texOffs(82, 16).addBox(-0.5F, -0.35F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.0F, 0.5785F, 1.5112F, 0.1309F, 0.0F, 0.0F));

        PartDefinition cube_r57 = TailTertiary.addOrReplaceChild("cube_r57", CubeListBuilder.create().texOffs(82, 12).addBox(-0.5F, -0.55F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.0F, -0.6715F, 2.2612F, 0.3054F, 0.0F, 0.0F));

        PartDefinition cube_r58 = TailTertiary.addOrReplaceChild("cube_r58", CubeListBuilder.create().texOffs(77, 11).addBox(-0.5F, 0.5F, -1.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, -1.6715F, 1.0112F, 0.0873F, 0.0F, 0.0F));

        PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(16, 40).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 1.5F, 0.0F));

        PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(0, 44).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 1.5F, 0.0F));

        PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offset(-2.5F, 10.5F, 0.0F));

        PartDefinition Part_r7 = RightLeg.addOrReplaceChild("Part_r7", CubeListBuilder.create().texOffs(50, 15).mirror().addBox(0.0F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, 3.75F, -0.75F, -0.2132F, 0.0469F, 0.2132F));

        PartDefinition Part_r8 = RightLeg.addOrReplaceChild("Part_r8", CubeListBuilder.create().texOffs(50, 11).mirror().addBox(0.0F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, 1.25F, -0.25F, -0.211F, 0.056F, 0.2559F));

        PartDefinition RightThigh_r1 = RightLeg.addOrReplaceChild("RightThigh_r1", CubeListBuilder.create().texOffs(48, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition RightLowerLeg = RightLeg.addOrReplaceChild("RightLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

        PartDefinition RightCalf_r1 = RightLowerLeg.addOrReplaceChild("RightCalf_r1", CubeListBuilder.create().texOffs(48, 40).addBox(-1.99F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

        PartDefinition RightFoot = RightLowerLeg.addOrReplaceChild("RightFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

        PartDefinition RightArch_r1 = RightFoot.addOrReplaceChild("RightArch_r1", CubeListBuilder.create().texOffs(56, 11).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

        PartDefinition RightPad = RightFoot.addOrReplaceChild("RightPad", CubeListBuilder.create().texOffs(52, 32).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

        PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(2.5F, 10.5F, 0.0F));

        PartDefinition Part_r9 = LeftLeg.addOrReplaceChild("Part_r9", CubeListBuilder.create().texOffs(44, 15).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 3.75F, -0.75F, -0.2132F, -0.0469F, -0.2132F));

        PartDefinition Part_r10 = LeftLeg.addOrReplaceChild("Part_r10", CubeListBuilder.create().texOffs(44, 11).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 1.25F, -0.25F, -0.211F, -0.056F, -0.2559F));

        PartDefinition LeftThigh_r1 = LeftLeg.addOrReplaceChild("LeftThigh_r1", CubeListBuilder.create().texOffs(32, 44).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition LeftLowerLeg = LeftLeg.addOrReplaceChild("LeftLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

        PartDefinition LeftCalf_r1 = LeftLowerLeg.addOrReplaceChild("LeftCalf_r1", CubeListBuilder.create().texOffs(48, 22).addBox(-2.01F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

        PartDefinition LeftFoot = LeftLowerLeg.addOrReplaceChild("LeftFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

        PartDefinition LeftArch_r1 = LeftFoot.addOrReplaceChild("LeftArch_r1", CubeListBuilder.create().texOffs(13, 57).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

        PartDefinition LeftPad = LeftFoot.addOrReplaceChild("LeftPad", CubeListBuilder.create().texOffs(24, 0).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.325F, -4.425F));

        return LayerDefinition.create(meshdefinition, 96, 96);
    }

    @Override
    public boolean shouldPartTransfur(ModelPart part) {
        if (hiddenPartsByDefault().contains(part) && !part.visible) {
            return false;
        }
        return super.shouldPartTransfur(part);
    }

    public List<ModelPart> hiddenPartsByDefault() {
        return List.of(this.bigTail, this.rightWing, this.reftWing);
    }

    public @NotNull ModelPart getArm(HumanoidArm p_102852) {
        return p_102852 == HumanoidArm.LEFT ? this.LeftArm : this.RightArm;
    }

    public @NotNull ModelPart getHead() {
        return this.Head;
    }

    public ModelPart getTorso() {
        return Torso;
    }

    public ModelPart getLeg(HumanoidArm humanoidArm) {
        return humanoidArm == HumanoidArm.LEFT ? this.LeftLeg : this.RightLeg;
    }

    public ModelPart getWing(HumanoidArm arm) {
        return arm == HumanoidArm.LEFT ? this.reftWing : this.rightWing;
    }

    public ModelPart getWingRoot(HumanoidArm arm) {
        return arm == HumanoidArm.LEFT ? this.leftWingRoot : this.rightWingRoot;
    }

    @Override
    public void setupHand(LuminaraFlowerBeastEntity entity) {
        this.getAnimator(entity).setupHand();
    }

    @Override
    public void setupAnim(@NotNull LuminaraFlowerBeastEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.shouldHaveBigWings = entity.isHyperAwakened();
        this.getAnimator(entity).setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    @Override
    public void prepareMobModel(@NotNull LuminaraFlowerBeastEntity entity, float limbSwing, float limbSwingAmount, float partialTicks) {
        super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
        this.handleVisibility(entity);
    }

    public void handleVisibility(@NotNull LuminaraFlowerBeastEntity entity) {
        this.bigTail.visible = entity.isAwakened();
        this.reftWing.visible = entity.isAwakened();
        this.rightWing.visible = entity.isAwakened();
        this.tail.visible = !entity.isAwakened();
        this.shouldHaveBigWings = entity.isHyperAwakened();
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        if (!this.shouldHaveBigWings) {
            Torso.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        } else {
            setWingsVisibility(false);
            Torso.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
            setWingsVisibility(true);

            renderBigWings(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        }
        RightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        LeftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        RightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        LeftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    private void renderBigWings(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        poseStack.scale(1.5f, 1.5f, 1.5f);

        poseStack.pushPose();
        reftWing.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();

        poseStack.pushPose();
        rightWing.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();

        poseStack.popPose();
    }

    private void setWingsVisibility(boolean wingsVisibility) {
        reftWing.visible = wingsVisibility;
        rightWing.visible = wingsVisibility;
    }

    @Override
    public HumanoidAnimator<LuminaraFlowerBeastEntity, LuminaraFlowerBeastModel> getAnimator(LuminaraFlowerBeastEntity luminaraFlowerBeastEntity) {
        return luminaraFlowerBeastEntity.isHyperAwakened() ? animatorHyperWingedForm : luminaraFlowerBeastEntity.isAwakened() ? animatorWingedForm : animatorNormalForm;
    }
}