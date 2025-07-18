
package net.foxyas.changedaddon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.client.model.KetModel;
import net.foxyas.changedaddon.entity.KetExperiment009BossEntity;
import net.foxyas.changedaddon.entity.KetExperiment009Entity;
import net.ltxprogrammer.changed.client.FormRenderHandler;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.FirstPersonLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.Camera;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

public class KetExperiment009Renderer extends AdvancedHumanoidRenderer<KetExperiment009Entity, KetModel, ArmorLatexMaleWolfModel<KetExperiment009Entity>> {
    public KetExperiment009Renderer(EntityRendererProvider.Context context) {
        super(context, new KetModel(context.bakeLayer(KetModel.LAYER_LOCATION)),
                ArmorLatexMaleWolfModel::new, ArmorLatexMaleWolfModel.INNER_ARMOR, ArmorLatexMaleWolfModel.OUTER_ARMOR, 0.5f);
        this.addLayer(new CustomEmissiveBodyLayer<>(this, new ResourceLocation("changed_addon", "textures/entities/ket_glow_layer.png"), 0.7f));
        this.addLayer(new LatexParticlesLayer<>(this, getModel()));
        this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
		this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer::scleraColor, CustomEyesLayer.fixedColorGlowing(Color3.parseHex("#8dcfff")), CustomEyesLayer.fixedColorGlowing(Color3.parseHex("#8dcfff")), CustomEyesLayer::noRender, CustomEyesLayer::noRender));
	}

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull KetExperiment009Entity entity) {
        return new ResourceLocation("changed_addon:textures/entities/ket_texture.png");
    }

    private static class CustomEmissiveBodyLayer<M extends EntityModel<T>, T extends ChangedEntity> extends EyesLayer<T, M> implements FirstPersonLayer<T> {
        private final RenderType renderType;
        private final ResourceLocation emissiveTexture;
        private final float healthThreshold;

        public CustomEmissiveBodyLayer(RenderLayerParent<T, M> p_116964_, ResourceLocation emissiveTexture, float healthThreshold) {
            super(p_116964_);
            this.renderType = RenderType.eyes(emissiveTexture);
            this.emissiveTexture = emissiveTexture;
            this.healthThreshold = healthThreshold;
        }

        public ResourceLocation getEmissiveTexture() {
            return this.emissiveTexture;
        }

        @Override
        public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (entity.getUnderlyingPlayer() == null && entity instanceof KetExperiment009Entity ketExperiment009 && ketExperiment009.isPhase2()) {
                VertexConsumer vertexConsumer = bufferSource.getBuffer(this.renderType());
                this.getParentModel().renderToBuffer(poseStack, vertexConsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }

            if (entity.getUnderlyingPlayer() == null && entity.getHealth() <= entity.getMaxHealth() * healthThreshold) {
                VertexConsumer vertexConsumer = bufferSource.getBuffer(this.renderType());
                this.getParentModel().renderToBuffer(poseStack, vertexConsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            } else if (entity.getUnderlyingPlayer() != null) {
                VertexConsumer vertexConsumer = bufferSource.getBuffer(this.renderType());
                this.getParentModel().renderToBuffer(poseStack, vertexConsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }


        public @NotNull RenderType renderType() {
            return this.renderType;
        }

        @Override
        public void renderFirstPersonOnFace(PoseStack stack, MultiBufferSource bufferSource, int packedLight, T entity, Camera camera) {
            FirstPersonLayer.super.renderFirstPersonOnFace(stack, bufferSource, packedLight, entity, camera);
        }

        @Override
        public void renderFirstPersonOnArms(PoseStack stack, MultiBufferSource bufferSource, int packedLight, T entity, HumanoidArm arm, PoseStack stackCorrector, float partialTick) {
            FirstPersonLayer.super.renderFirstPersonOnArms(stack, bufferSource, packedLight, entity, arm, stackCorrector, partialTick);
            if (entity.getUnderlyingPlayer() != null) {
                stack.pushPose();
                stack.scale(1.0002F, 1.0002F, 1.0002F);
                EntityModel<T> var8 = this.getParentModel();
                if (var8 instanceof AdvancedHumanoidModel<?> armedModel) {
                    FormRenderHandler.renderModelPartWithTexture(armedModel.getArm(arm), stackCorrector, stack, bufferSource.getBuffer(this.renderType()), 15728880, 1.0F);
                }
                stack.popPose();
            }

            if (entity.getUnderlyingPlayer() == null && entity instanceof KetExperiment009BossEntity ketExperiment009 && ketExperiment009.isPhase2()) {
                stack.pushPose();
                stack.scale(1.0002F, 1.0002F, 1.0002F);
                EntityModel<T> var8 = this.getParentModel();
                if (var8 instanceof AdvancedHumanoidModel<?> armedModel) {
                    FormRenderHandler.renderModelPartWithTexture(armedModel.getArm(arm), stackCorrector, stack, bufferSource.getBuffer(this.renderType()), 15728880, 1.0F);
                }
                stack.popPose();
            }

            if (entity.getUnderlyingPlayer() == null && entity.getHealth() <= entity.getMaxHealth() * healthThreshold) {
                stack.pushPose();
                stack.scale(1.0002F, 1.0002F, 1.0002F);
                EntityModel<T> var8 = this.getParentModel();
                if (var8 instanceof AdvancedHumanoidModel<?> armedModel) {
                    FormRenderHandler.renderModelPartWithTexture(armedModel.getArm(arm), stackCorrector, stack, bufferSource.getBuffer(this.renderType()), 15728880, 1.0F);
                }
                stack.popPose();
            }
        }
    }

}


