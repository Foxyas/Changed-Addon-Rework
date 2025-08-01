package net.foxyas.changedaddon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.client.model.Experiment10Model;
import net.foxyas.changedaddon.entity.bosses.Experiment10Entity;
import net.ltxprogrammer.changed.client.FormRenderHandler;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.*;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexFemaleCatModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

public class Experiment10Renderer extends AdvancedHumanoidRenderer<Experiment10Entity, Experiment10Model, ArmorLatexFemaleCatModel<Experiment10Entity>> {
    public Experiment10Renderer(EntityRendererProvider.Context context) {
        super(context, new Experiment10Model(context.bakeLayer(Experiment10Model.LAYER_LOCATION)),
                ArmorLatexFemaleCatModel.MODEL_SET, 0.5f);
        this.addLayer(new EmissiveBodyLayer<>(this, new ResourceLocation("changed_addon", "textures/entities/experiment_10/experiment_10_glow.png")));
        this.addLayer(new LatexParticlesLayer<>(this, getModel()));
        this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
        this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer::scleraColor, CustomEyesLayer::glowingIrisColorLeft, CustomEyesLayer::glowingIrisColorRight, CustomEyesLayer::noRender, CustomEyesLayer::noRender));
        this.addLayer(new BloodLayer<>(this, new ResourceLocation("changed_addon:textures/entities/experiment_10/experiment_10_phase2.png")));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Experiment10Entity entity) {
        return new ResourceLocation("changed_addon:textures/entities/experiment_10/experiment_10.png");
    }

    public static class BloodLayer<M extends AdvancedHumanoidModel<T>, T extends ChangedEntity> extends RenderLayer<T, M> implements FirstPersonLayer<T> {

        public final ResourceLocation bloodTexture;
        private final RenderType renderType;

        public BloodLayer(RenderLayerParent<T, M> p_117346_, ResourceLocation bloodTexture) {
            super(p_117346_);
            renderType = RenderType.entityCutout(bloodTexture);
            this.bloodTexture = bloodTexture;
        }

        @Override
        public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i, @NotNull T t, float v, float v1, float v2, float v3, float v4, float v5) {
            if (t instanceof Experiment10Entity experiment10BossEntity && experiment10BossEntity.isPhase2()) {
                VertexConsumer vertexconsumer = multiBufferSource.getBuffer(this.renderType());
                this.getParentModel().renderToBuffer(poseStack, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }

        public RenderType renderType() {
            return this.renderType;
        }


        public void renderFirstPersonOnArms(PoseStack stack, MultiBufferSource bufferSource, int packedLight, T entity, HumanoidArm arm, PoseStack stackCorrector, float partialTick) {
            if (entity instanceof Experiment10Entity experiment10BossEntity && experiment10BossEntity.isPhase2()) {
                stack.pushPose();
                stack.scale(1.0002F, 1.0002F, 1.0002F);
                EntityModel<T> var9 = this.getParentModel();
                if (var9 instanceof AdvancedHumanoidModel<?> armedModel) {
                    FormRenderHandler.renderModelPartWithTexture(armedModel.getArm(arm), stackCorrector, stack, bufferSource.getBuffer(this.renderType()), 15728880, 1.0F);
                }

                stack.popPose();
            }
        }
    }
}
