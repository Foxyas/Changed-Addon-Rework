package net.foxyas.changedaddon.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.entity.bosses.Experiment10Entity;
import net.ltxprogrammer.changed.client.FormRenderHandler;
import net.ltxprogrammer.changed.client.renderer.layers.FirstPersonLayer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

public class BloodLayer<M extends AdvancedHumanoidModel<T>, T extends Experiment10Entity> extends RenderLayer<T, M> implements FirstPersonLayer<T> {

    public final ResourceLocation bloodTexture;
    private final RenderType renderType;

    public BloodLayer(RenderLayerParent<T, M> p_117346_, ResourceLocation bloodTexture) {
        super(p_117346_);
        renderType = RenderType.entityCutout(bloodTexture);
        this.bloodTexture = bloodTexture;
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.shouldShowGlow()) {
            VertexConsumer vertexconsumer = bufferSource.getBuffer(this.renderType());
            this.getParentModel().renderToBuffer(poseStack, vertexconsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public RenderType renderType() {
        return this.renderType;
    }


    @Override
    public void renderFirstPersonOnArms(PoseStack stack, MultiBufferSource bufferSource, int packedLight, T entity, HumanoidArm arm, PartPose armPose, float partialTick) {
        FirstPersonLayer.super.renderFirstPersonOnArms(stack, bufferSource, packedLight, entity, arm, armPose, partialTick);
        if (!entity.shouldShowGlow()) {
            return;
        }

        stack.pushPose();
        stack.scale(1.0002F, 1.0002F, 1.0002F);
        EntityModel<T> var9 = this.getParentModel();
        if (var9 instanceof AdvancedHumanoidModel<?> armedModel) {
            FormRenderHandler.renderModelPartWithTexture(armedModel.getArm(arm), stack, bufferSource.getBuffer(this.renderType()), 15728880, 1.0F);
        }

        stack.popPose();
    }
}