package net.foxyas.changedaddon.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ModelFlickerLayer<M extends AdvancedHumanoidModel<T>, T extends ChangedEntity> extends RenderLayer<T, M> {

    public ModelFlickerLayer(RenderLayerParent<T, M> parent) {
        super(parent);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        float healthRatio = entity.computeHealthRatio();
        if (healthRatio >= 0.5f) return;

        float intensity = 1.0f - healthRatio;
        float flickerSpeed = 40f + 90f * intensity;
        float flicker = (float) Math.sin((entity.tickCount + partialTicks) * flickerSpeed) * intensity * 0.05f;

        poseStack.pushPose();
        poseStack.translate(flicker, flicker * (1 + 0.25f), flicker);

        // Obtenha o modelo e textura do parent
        M model = this.getParentModel();
        ResourceLocation texture = this.getTextureLocation(entity);

        VertexConsumer buffer = bufferSource.getBuffer(RenderType.energySwirl(texture, 0, 0));
        float alpha = 0.3f;

        model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
        model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        model.renderToBuffer(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, alpha);

        poseStack.popPose();
    }
}
