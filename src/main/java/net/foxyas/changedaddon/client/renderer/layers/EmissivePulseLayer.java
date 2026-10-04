package net.foxyas.changedaddon.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.ltxprogrammer.changed.client.FormRenderHandler;
import net.ltxprogrammer.changed.client.renderer.layers.EmissiveBodyLayer;
import net.ltxprogrammer.changed.client.renderer.layers.FirstPersonLayer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class EmissivePulseLayer<M extends AdvancedHumanoidModel<T>, T extends ChangedEntity> extends EmissiveBodyLayer<M, T> implements FirstPersonLayer<T> {
    private final Predicate<T> pulsePredicate;

    public EmissivePulseLayer(RenderLayerParent<T, M> parent, ResourceLocation emissiveTexture, Predicate<T> pulsePredicate) {
        super(parent, emissiveTexture);
        this.pulsePredicate = pulsePredicate;
    }

    @Override
    public RenderType renderType() {
        return RenderType.entityTranslucentEmissive(this.getEmissiveTexture());
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!pulsePredicate.test(entity)) {
            return;
        }
        VertexConsumer vertexConsumer = bufferSource.getBuffer(this.renderType());
        float pulseFactor = (float) (Math.sin(ageInTicks * 0.1f) + 1) / 2;

        float intensity = 1f;
        float pulse = pulseFactor * intensity;
        float red = 1.0f;// - pulse;
        float green = 1.0f;// - pulse;
        float blue = 1.0f;// - pulse;
        float alpha = 1.0f - pulse;

        this.getParentModel().renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, red, green, blue, alpha);
    }

    @Override
    public void renderFirstPersonOnArms(PoseStack stack, MultiBufferSource bufferSource, int packedLight, T entity, HumanoidArm arm, PartPose armPose, float partialTick) {
        if (!pulsePredicate.test(entity)) {
            return;
        }

        stack.pushPose();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(this.renderType());
        float ageInTicks = entity.tickCount + partialTick;
        float pulseFactor = (float) (Math.sin(ageInTicks * 0.1f) + 1) / 2;

        float intensity = 1f;
        float pulse = pulseFactor * intensity;
        float red = 1.0f;// - pulse;
        float green = 1.0f;// - pulse;
        float blue = 1.0f;// - pulse;
        float alpha = 1.0f - pulse;

        stack.scale(1.0002F, 1.0002F, 1.0002F);
        M armedModel = this.getParentModel();
        ModelPart armPart = armedModel.getArm(arm);
        armPart.loadPose(armPose);
        FormRenderHandler.renderModelPartWithTexture(armedModel.getArm(arm), stack, vertexConsumer, OverlayTexture.NO_OVERLAY, red, green, blue, alpha);

        stack.popPose();

    }
}