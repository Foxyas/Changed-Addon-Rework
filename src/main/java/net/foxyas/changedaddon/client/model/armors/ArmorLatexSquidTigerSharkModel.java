package net.foxyas.changedaddon.client.model.armors;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.client.model.LatexSquidTigerSharkModel;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.model.DoubleArmedModel;
import net.ltxprogrammer.changed.client.renderer.model.armor.*;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ArmorLatexSquidTigerSharkModel<T extends ChangedEntity> extends LatexHumanoidArmorModel<T, ArmorLatexSquidTigerSharkModel<T>> implements DoubleArmedModel<T> {
    //public static final ModelLayerLocation INNER_ARMOR = ArmorModelLayerLocation.createInnerArmorLocation(new ResourceLocation("changed_addon", "squid_tiger_shark_armor")).get();
    //public static final ModelLayerLocation OUTER_ARMOR = ArmorModelLayerLocation.createInnerArmorLocation(new ResourceLocation("changed_addon", "squid_tiger_shark_armor")).get();
    public static final ArmorModelSet<ChangedEntity, ArmorLatexSquidTigerSharkModel<ChangedEntity>> MODEL_SET = ArmorModelSet.of(new ResourceLocation("changed_addon", "armor_squid_tiger_shark"), ArmorLatexSquidTigerSharkModel::createArmorLayer, ArmorLatexSquidTigerSharkModel::new);

    private final ModelPart Head;
    private final ModelPart Torso;
    private final ModelPart LeftLeg;
    private final ModelPart RightLeg;
    private final ModelPart LeftArm;
    private final ModelPart RightArm;
    private final ModelPart LeftArm2;
    private final ModelPart RightArm2;
    private final HumanoidAnimator<T, ArmorLatexSquidTigerSharkModel<T>> animator;

    public ArmorLatexSquidTigerSharkModel(ModelPart modelPart, ArmorModel model) {
        super(modelPart, model);
        this.Head = modelPart.getChild("Head");
        this.Torso = modelPart.getChild("Torso");
        this.LeftLeg = modelPart.getChild("LeftLeg");
        this.RightLeg = modelPart.getChild("RightLeg");
        this.LeftArm = modelPart.getChild("LeftArm");
        this.RightArm = modelPart.getChild("RightArm");
        this.LeftArm2 = modelPart.getChild("LeftArm2");
        this.RightArm2 = modelPart.getChild("RightArm2");

        var leftLowerLeg = LeftLeg.getChild("LeftLowerLeg");
        var leftFoot = leftLowerLeg.getChild("LeftFoot");
        var rightLowerLeg = RightLeg.getChild("RightLowerLeg");
        var rightFoot = rightLowerLeg.getChild("RightFoot");

        /*var Tail = this.Torso.getChild("Tail");
        ModelPart tailPrimary = Tail.getChild("TailPrimary");
        ModelPart tailSecondary = tailPrimary.getChild("TailSecondary");
*/

        var upperRightTentacle = List.of(Torso.getChild("RightUpperTentacle"));
        var upperLeftTentacle = List.of(Torso.getChild("LeftUpperTentacle"));
        var lowerRightTentacle = List.of(Torso.getChild("RightLowerTentacle"));
        var lowerLeftTentacle = List.of(Torso.getChild("LeftLowerTentacle"));

        animator = HumanoidAnimator.of(this).hipOffset(-0.75f).legLength(13.0f)
                .addPreset(LatexSquidTigerSharkModel.CustomHybridAnimation.squidTigerSharkArmorLike(
                        Head, Torso, LeftArm, RightArm, LeftArm2, RightArm2,
                        //Tail, List.of(tailPrimary, tailSecondary),
                        upperLeftTentacle, upperRightTentacle, lowerLeftTentacle, lowerRightTentacle,
                        LeftLeg, leftLowerLeg, leftFoot, leftFoot.getChild("LeftPad"), RightLeg, rightLowerLeg, rightFoot, rightFoot.getChild("RightPad")));
        animator.torsoWidth = 5.2f;
    }

    public static LayerDefinition createArmorLayer(ArmorModel layer) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        //addUnifiedLegs(partdefinition, layer);

        PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offset(-2.5F, 10.5F, 0.0F));

        PartDefinition RightThigh_r1 = RightLeg.addOrReplaceChild("RightThigh_r1", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, layer.altDeformation.extend(0.505F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition RightLowerLeg = RightLeg.addOrReplaceChild("RightLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

        PartDefinition RightCalf_r1 = RightLowerLeg.addOrReplaceChild("RightCalf_r1", CubeListBuilder.create().texOffs(0, 16).addBox(-1.99F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, layer.altDeformation.extend(0.5F)), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

        PartDefinition RightFoot = RightLowerLeg.addOrReplaceChild("RightFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

        PartDefinition RightArch_r1 = RightFoot.addOrReplaceChild("RightArch_r1", CubeListBuilder.create().texOffs(2, 20).addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, layer.altDeformation.extend(0.5F)), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

        PartDefinition RightPad = RightFoot.addOrReplaceChild("RightPad", CubeListBuilder.create().texOffs(21, 21).mirror().addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, layer.altDeformation.extend(0.5F)).mirror(false), PartPose.offset(0.0F, 4.325F, -4.425F));

        PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(2.5F, 10.5F, 0.0F));

        PartDefinition LeftThigh_r1 = LeftLeg.addOrReplaceChild("LeftThigh_r1", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, layer.altDeformation.extend(0.5F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition LeftLowerLeg = LeftLeg.addOrReplaceChild("LeftLowerLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 6.375F, -3.45F));

        PartDefinition LeftCalf_r1 = LeftLowerLeg.addOrReplaceChild("LeftCalf_r1", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.01F, -0.125F, -2.9F, 4.0F, 6.0F, 4.0F, layer.altDeformation.extend(0.5F)).mirror(false), PartPose.offsetAndRotation(0.0F, -2.125F, 1.95F, 0.8727F, 0.0F, 0.0F));

        PartDefinition LeftFoot = LeftLowerLeg.addOrReplaceChild("LeftFoot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.8F, 7.175F));

        PartDefinition LeftArch_r1 = LeftFoot.addOrReplaceChild("LeftArch_r1", CubeListBuilder.create().texOffs(2, 20).mirror().addBox(-2.0F, -8.45F, -0.725F, 4.0F, 6.0F, 3.0F, layer.altDeformation.extend(0.5F)).mirror(false), PartPose.offsetAndRotation(0.0F, 7.075F, -4.975F, -0.3491F, 0.0F, 0.0F));

        PartDefinition LeftPad = LeftFoot.addOrReplaceChild("LeftPad", CubeListBuilder.create().texOffs(21, 21).addBox(-2.0F, 0.0F, -2.5F, 4.0F, 2.0F, 5.0F, layer.altDeformation.extend(0.5F)), PartPose.offset(0.0F, 4.325F, -4.425F));



        PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, layer.deformation.extend(0.25F)), PartPose.offset(0.0F, -2.2F, 0.0F));

        PartDefinition Torso = partdefinition.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.1F, -2.2F, 8.0F, 12.0F, 4.0F, layer.dualDeformation.extend(0.1f)), PartPose.offset(0.0F, -2.2F, 0.0F));

        //PartDefinition Tail = Torso.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offset(0.0F, 10.5F, 0.0F));

        //PartDefinition TailPrimary = Tail.addOrReplaceChild("TailPrimary", CubeListBuilder.create(), PartPose.offset(0.0F, -0.5F, 0.75F));

        //TailPrimary.addOrReplaceChild("Base_r1", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 8.0F, 4.0F, layer.altDeformation.extend(0.505F)), PartPose.offsetAndRotation(0.0F, 1.7091F, 3.8476F, -1.1781F, 0.0F, 3.1416F));

        //PartDefinition TailSecondary = TailPrimary.addOrReplaceChild("TailSecondary", CubeListBuilder.create(), PartPose.offset(0.0F, 3.25F, 7.25F));

        //TailSecondary.addOrReplaceChild("Base_r2", CubeListBuilder.create().texOffs(0, 20).addBox(-1.5F, -1.3563F, -0.6088F, 3.0F, 5.0F, 3.0F, layer.altDeformation.extend(0.5F)), PartPose.offsetAndRotation(0.0F, 1.25F, 1.0F, 1.309F, 0.0F, 0.0F));

        //TailSecondary.addOrReplaceChild("Base_r3", CubeListBuilder.create().texOffs(0, 16).addBox(-0.5F, 5.3462F, -1.8296F, 1.0F, 1.0F, 1.0F, layer.altDeformation.extend(-0.05F + 0.5F)), PartPose.offsetAndRotation(0.0F, 2.0F, 8.75F, 1.1345F, 0.0F, 0.0F));

        PartDefinition RightUpperTentacle = Torso.addOrReplaceChild("RightUpperTentacle", CubeListBuilder.create(), PartPose.offset(-2.5F, 2.7F, 1.0F));

        PartDefinition TentaclePart_r1 = RightUpperTentacle.addOrReplaceChild("TentaclePart_r1", CubeListBuilder.create().texOffs(20, 23).mirror().addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F, layer.deformation.extend(-0.5f)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.192F, -0.4363F, -0.0524F));

        PartDefinition RightLowerTentacle = Torso.addOrReplaceChild("RightLowerTentacle", CubeListBuilder.create(), PartPose.offset(-2.5F, 6.7F, 1.0F));

        PartDefinition TentaclePart_r2 = RightLowerTentacle.addOrReplaceChild("TentaclePart_r2", CubeListBuilder.create().texOffs(20, 23).mirror().addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F, layer.deformation.extend(-0.5f)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.192F, -0.4276F, 0.0524F));

        PartDefinition LeftUpperTentacle = Torso.addOrReplaceChild("LeftUpperTentacle", CubeListBuilder.create(), PartPose.offset(2.5F, 2.7F, 1.0F));

        PartDefinition TentaclePart_r3 = LeftUpperTentacle.addOrReplaceChild("TentaclePart_r3", CubeListBuilder.create().texOffs(20, 23).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F, layer.deformation.extend(-0.5f)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.192F, 0.4363F, 0.0524F));

        PartDefinition LeftLowerTentacle = Torso.addOrReplaceChild("LeftLowerTentacle", CubeListBuilder.create(), PartPose.offset(2.5F, 6.7F, 1.0F));

        PartDefinition TentaclePart_r4 = LeftLowerTentacle.addOrReplaceChild("TentaclePart_r4", CubeListBuilder.create().texOffs(20, 23).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 4.0F, layer.deformation.extend(-0.5f)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.192F, 0.4276F, -0.0524F));

        PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, layer.deformation.extend(0.1F)), PartPose.offset(-5.2F, 3.9F, -0.2F));

        PartDefinition RightArm2 = partdefinition.addOrReplaceChild("RightArm2", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, layer.deformation.extend(0.1F)), PartPose.offset(-5.2F, -0.1F, -0.2F));

        PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-0.8F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, layer.deformation.extend(0.1F)).mirror(false), PartPose.offset(5.0F, 3.9F, -0.2F));

        PartDefinition LeftArm2 = partdefinition.addOrReplaceChild("LeftArm2", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-0.8F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, layer.deformation.extend(0.1F)).mirror(false), PartPose.offset(5.0F, -0.1F, -0.2F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void prepareVisibility(EquipmentSlot armorSlot, ItemStack item) {
        super.prepareVisibility(armorSlot, item);
        if (armorSlot == EquipmentSlot.LEGS) {
            prepareUnifiedLegsForArmor(item, LeftLeg, RightLeg);
        }
    }

    @Override
    public void unprepareVisibility(EquipmentSlot armorSlot, ItemStack item) {
        super.unprepareVisibility(armorSlot, item);
        if (armorSlot == EquipmentSlot.LEGS) {
            prepareUnifiedLegsForArmor(item, LeftLeg, RightLeg);
        }
    }

    @Override
    public void renderForSlot(T entity, RenderLayerParent<? super T, ?> parent, ItemStack stack, EquipmentSlot slot, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        this.scaleForSlot(parent, slot, poseStack);

        switch (slot) {
            case HEAD -> Head.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            case CHEST -> {
                Torso.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                LeftArm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                RightArm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                LeftArm2.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                RightArm2.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            }
            case LEGS -> {
                poseStack.pushPose(); // Salva o estado antes de modificar
                poseStack.scale(0.8F, 0.92F, 0.8F);
                Torso.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                poseStack.popPose(); // Restaura o estado original
                this.scaleForSlot(parent, slot, poseStack); // Aplica a escala padrão para as pernas
                LeftLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                RightLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            }
            case FEET -> {
                LeftLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                RightLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            }
        }

        poseStack.popPose();
    }

    @Override
    public HumanoidAnimator<T, ArmorLatexSquidTigerSharkModel<T>> getAnimator(T entity) {
        return animator;
    }

    public @NotNull ModelPart getArm(HumanoidArm arm) {
        return arm == HumanoidArm.LEFT ? this.LeftArm2 : this.RightArm2;
    }

    public ModelPart getLeg(HumanoidArm leg) {
        return leg == HumanoidArm.LEFT ? this.LeftLeg : this.RightLeg;
    }

    public @NotNull ModelPart getHead() {
        return this.Head;
    }

    public ModelPart getTorso() {
        return Torso;
    }

    @Override
    public void translateToUpperHand(ChangedEntity changedEntity, HumanoidArm arm, PoseStack poseStack) {

    }

    @Override
    public void translateToLowerHand(ChangedEntity changedEntity,HumanoidArm arm, PoseStack poseStack) {

    }

    @Override
    public ModelPart getOtherArm(HumanoidArm arm) {
        return arm == HumanoidArm.LEFT ? this.LeftArm : this.RightArm;
    }
}