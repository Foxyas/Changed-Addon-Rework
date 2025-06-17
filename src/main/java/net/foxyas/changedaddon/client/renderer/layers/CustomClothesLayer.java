package net.foxyas.changedaddon.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.ltxprogrammer.changed.client.FormRenderHandler;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.FirstPersonLayer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.client.renderer.entity.LivingEntityRenderer.getOverlayCoords;

public class CustomClothesLayer<E extends ChangedEntity, M extends AdvancedHumanoidModel<E>> extends RenderLayer<E, M> implements FirstPersonLayer<E> {
    private final M model;
    private final ResourceLocation clothesTexture;

    public CustomClothesLayer(RenderLayerParent<E, M> p_117346_, M model, ResourceLocation clothesTexture) {
        super(p_117346_);
        this.model = model;
        this.clothesTexture = clothesTexture;
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull E entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
        model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        ResourceLocation accessoryTexture = getClothTexture();
        if (accessoryTexture == null) return;
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(accessoryTexture));
        Color3 color = getClothColor(entity);
        float alpha = getClothAlpha(entity);
        float zFightOffset = CustomEyesLayer.getZFightingOffset(entity);
        poseStack.pushPose();
        model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
        model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        poseStack.scale(1.0025f + zFightOffset, 1.0025f + zFightOffset, 1.0025f + zFightOffset);
        if (color != null) {
            model.renderToBuffer(poseStack, vertexConsumer, packedLight, getOverlayCoords(entity, 0.0f), color.red(), color.green(), color.blue(), alpha);
        } else {
            model.renderToBuffer(poseStack, vertexConsumer, packedLight, getOverlayCoords(entity, 0.0f), 1.0F, 1.0F, 1.0F, 1.0F);
        }
        poseStack.popPose();


        //OLD
        /*for (EquipmentSlot slot : EquipmentSlot.values()) {
            model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
            model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            ResourceLocation accessoryTexture = getClothTexture();
            if (accessoryTexture == null) continue;
            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(accessoryTexture));
            Color3 color = getClothColor(entity);
            float alpha = getClothAlpha(entity);
            float zFightOffset = CustomEyesLayer.getZFightingOffset(entity);
            if (slot == EquipmentSlot.CHEST) {
                poseStack.pushPose();
                model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
                model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                poseStack.scale(1.0025f + zFightOffset, 1.0025f + zFightOffset, 1.0025f + zFightOffset);
                poseStack.pushPose();
                poseStack.translate(-0.003f, 0, 0);
                poseStack.scale(1.005f, 1.005f, 1.005f);
                if (color != null) {
                    model.getArm(HumanoidArm.LEFT).render(poseStack, vertexConsumer, packedLight, getOverlayCoords(entity, 0.0f), color.red(), color.green(), color.blue(), alpha);
                } else {
                    model.getArm(HumanoidArm.LEFT).render(poseStack, vertexConsumer, packedLight, getOverlayCoords(entity, 0.0f), 1.0F, 1.0F, 1.0F, 1.0F);
                }
                poseStack.popPose();
                poseStack.pushPose();
                poseStack.translate(0.003f, 0, 0);
                poseStack.scale(1.005f, 1.005f, 1.005f);
                if (color != null) {
                    model.getArm(HumanoidArm.RIGHT).render(poseStack, vertexConsumer, packedLight, getOverlayCoords(entity, 0.0f), color.red(), color.green(), color.blue(), alpha);
                } else {
                    model.getArm(HumanoidArm.RIGHT).render(poseStack, vertexConsumer, packedLight, getOverlayCoords(entity, 0.0f), 1.0F, 1.0F, 1.0F, 1.0F);
                }
                poseStack.popPose();
                List<ModelPart> modelPartStream = List.of(model.getLeg(HumanoidArm.LEFT), model.getLeg(HumanoidArm.RIGHT), model.getTorso(), model.getHead());
                for (ModelPart part : modelPartStream) {
                    if (color != null) {
                        part.render(poseStack, vertexConsumer, packedLight, getOverlayCoords(entity, 0.0f), color.red(), color.green(), color.blue(), alpha);
                    } else {
                        part.render(poseStack, vertexConsumer, packedLight, getOverlayCoords(entity, 0.0f), 1.0F, 1.0F, 1.0F, 1.0F);
                    }
                }
                poseStack.popPose();
            } else {
                poseStack.pushPose();
                model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
                model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                poseStack.scale(1.0025f + zFightOffset, 1.0025f + zFightOffset, 1.0025f + zFightOffset);
                if (color != null) {
                    model.renderToBuffer(poseStack, vertexConsumer, packedLight, getOverlayCoords(entity, 0.0f), color.red(), color.green(), color.blue(), alpha);
                } else {
                    model.renderToBuffer(poseStack, vertexConsumer, packedLight, getOverlayCoords(entity, 0.0f), 1.0F, 1.0F, 1.0F, 1.0F);
                }
                poseStack.popPose();
            }
        }*/
    }

    @Override
    public void renderFirstPersonOnArms(PoseStack stack, MultiBufferSource bufferSource, int packedLight, E entity, HumanoidArm arm, PoseStack stackCorrector, float partialTick) {
        stack.pushPose();
        stack.scale(1.0002F, 1.0002F, 1.0002F);
        ResourceLocation accessoryTexture = getClothTexture();
        if (accessoryTexture == null) {
            return;
        }
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(accessoryTexture));
        Color3 color = getClothColor(entity);
        if (color == null) {
            color = Color3.WHITE;
        }
        float alpha = getClothAlpha(entity);
        FormRenderHandler.renderModelPartWithTexture(this.model.getArm(arm), stackCorrector, stack, vertexConsumer, packedLight, color.red(), color.green(), color.blue(), alpha);
        stack.popPose();
    }

    private float getClothAlpha(@NotNull E entity) {
        return 1;
    }

    private Color3 getClothColor(E entity) {
        return Color3.WHITE;
    }

    private ResourceLocation getClothTexture() {
        return this.clothesTexture;
    }
}