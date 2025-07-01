package net.foxyas.changedaddon.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.configuration.ChangedAddonClientConfigsConfiguration;
import net.foxyas.changedaddon.entity.advanced.AvaliEntity;
import net.ltxprogrammer.changed.client.FormRenderHandler;
import net.ltxprogrammer.changed.client.renderer.layers.FirstPersonLayer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.BasicPlayerInfo;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

public class AvaliColorsLayer<M extends AdvancedHumanoidModel<T>, T extends ChangedEntity> extends RenderLayer<T, M> implements FirstPersonLayer<T> {
    private final M model;
    private final ResourceLocation layerTexture;
    private final int layer;

    public AvaliColorsLayer(RenderLayerParent<T, M> parent, M model, ResourceLocation layerTexture, int layer) {
        super(parent);
        this.model = model;
        this.layerTexture = layerTexture;
        this.layer = layer;
    }

    public void render(@NotNull PoseStack pose, @NotNull MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entity.isInvisible()) {
            if (entity instanceof AvaliEntity avaliEntity) {
                ResourceLocation texture = getTextureStyle(avaliEntity);
                Color3 bodyColor = avaliEntity.getColor(layer);
                int overlay = LivingEntityRenderer.getOverlayCoords(entity, 0.0F);
                this.model.renderToBuffer(pose, bufferSource.getBuffer(RenderType.entityCutoutNoCull(texture)), packedLight, overlay, bodyColor.red(), bodyColor.green(), bodyColor.blue(), 1.0F);
            }
        }
    }

    private ResourceLocation getTextureStyle(AvaliEntity avaliEntity) {
        if (this.layerTexture.toString().contains("%s")) {
            return new ResourceLocation(layerTexture.toString().replace("%s", avaliEntity.getStyleOfColor()));
        }
        return layerTexture;
    }

    @Override
    public void renderFirstPersonOnArms(PoseStack stack, MultiBufferSource bufferSource, int packedLight, T entity, HumanoidArm arm, PoseStack stackCorrector, float partialTick) {
        if (entity instanceof AvaliEntity avaliEntity) {
            ResourceLocation texture = getTextureStyle(avaliEntity);
            Color3 bodyColor = avaliEntity.getColor(layer);
            stack.pushPose();
            stack.scale(1.0002F, 1.0002F, 1.0002F);
            FormRenderHandler.renderModelPartWithTexture(this.model.getArm(arm), stackCorrector, stack, bufferSource.getBuffer(RenderType.entityCutoutNoCull(texture)), packedLight, bodyColor.red(), bodyColor.green(), bodyColor.blue(), 1.0F);
            stack.popPose();
        }
    }
}
