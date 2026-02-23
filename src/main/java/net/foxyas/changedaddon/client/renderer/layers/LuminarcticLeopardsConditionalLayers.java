package net.foxyas.changedaddon.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.entity.bosses.LuminarcticLeopardMaleEntity;
import net.foxyas.changedaddon.entity.defaults.AbstractLuminarcticLeopard;
import net.ltxprogrammer.changed.ability.HypnosisAbility;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.EmissiveBodyLayer;
import net.ltxprogrammer.changed.client.renderer.layers.FirstPersonLayer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class LuminarcticLeopardsConditionalLayers {
    public static class GlowLayer<M extends AdvancedHumanoidModel<T>, T extends ChangedEntity> extends EmissiveBodyLayer<M, T> implements FirstPersonLayer<T> {
        public GlowLayer(RenderLayerParent<T, M> p_116964_, ResourceLocation emissiveTexture) {
            super(p_116964_, emissiveTexture);
        }

        @Override
        public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (entity.getUnderlyingPlayer() != null) {
                Player player = entity.getUnderlyingPlayer();
                var instance = ProcessTransfur.getPlayerTransfurVariant(player).getSelectedAbility();
                if (instance != null && instance.ability instanceof HypnosisAbility) {
                    if (instance.getController().getHoldTicks() > 0) {
                        super.render(poseStack, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                    }
                } else if (entity instanceof AbstractLuminarcticLeopard LUMI && LUMI.isActivatedAbility()) {
                    if (LUMI.getGlowStage() == AbstractLuminarcticLeopard.GLOW_PULSE) {
                        VertexConsumer vertexConsumer = bufferSource.getBuffer(this.renderType());
                        float pulseFactor = (float) (Math.sin(ageInTicks * 0.1f) + 1) / 2; // Varia entre 0 e 1 suavemente, simulando o efeito de pulsar

                        // Calculando a cor escurecida
                        float intensity = 1f; // O quão escuro pode ficar
                        float red = 1.0f - pulseFactor * intensity;
                        float green = 1.0f - pulseFactor * intensity;
                        float blue = 1.0f - pulseFactor * intensity;

                        // Aplicando o ajuste de cor nos olhos (com alpha e o efeito de pulsar)
                        this.getParentModel().renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0f);
                    } else if (LUMI.getGlowStage() == AbstractLuminarcticLeopard.GLOW_ALWAYS) {
                        VertexConsumer vertexConsumer = bufferSource.getBuffer(this.renderType());
                        float red = 1.0f;
                        float green = 1.0f;
                        float blue = 1.0f;

                        // Aplicando o ajuste de cor nos olhos (com alpha e o efeito de pulsar)
                        this.getParentModel().renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0f);
                    } else {
                        super.render(poseStack, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                    }
                }
            }

            if (entity instanceof AbstractLuminarcticLeopard WILD_LUMI && WILD_LUMI.getTarget() != null) {
                // Aplicando o ajuste de cor nos olhos (com alpha e o efeito de pulsar)
                if (WILD_LUMI.getGlowStage() == AbstractLuminarcticLeopard.GLOW_PULSE) {
                    VertexConsumer vertexConsumer = bufferSource.getBuffer(this.renderType());
                    float pulseFactor = (float) (Math.sin(ageInTicks * 0.1f) + 1) / 2; // Varia entre 0 e 1 suavemente, simulando o efeito de pulsar

                    // Calculando a cor escurecida
                    float intensity = 1f; // O quão escuro pode ficar
                    float red = 1.0f - pulseFactor * intensity;
                    float green = 1.0f - pulseFactor * intensity;
                    float blue = 1.0f - pulseFactor * intensity;

                    // Aplicando o ajuste de cor nos olhos (com alpha e o efeito de pulsar)
                    this.getParentModel().renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0f);
                } else if (WILD_LUMI.getGlowStage() == AbstractLuminarcticLeopard.GLOW_ALWAYS) {
                    VertexConsumer vertexConsumer = bufferSource.getBuffer(this.renderType());
                    float red = 1.0f;
                    float green = 1.0f;
                    float blue = 1.0f;

                    // Aplicando o ajuste de cor nos olhos (com alpha e o efeito de pulsar)
                    this.getParentModel().renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0f);
                } else {
                    super.render(poseStack, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                }
            }
        }

        @Override
        public void renderFirstPersonOnArms(PoseStack stack, MultiBufferSource bufferSource, int packedLight, T entity, HumanoidArm arm, PartPose armPose, float partialTick) {
            //super.renderFirstPersonOnArms(stack, bufferSource, packedLight, entity, arm, armPose,  partialTick);
            if (entity.getUnderlyingPlayer() != null) {
                Player player = entity.getUnderlyingPlayer();
                var instance = ProcessTransfur.getPlayerTransfurVariant(player).getSelectedAbility();
                if (entity instanceof AbstractLuminarcticLeopard LUMI && LUMI.isActivatedAbility()) {
                    super.renderFirstPersonOnArms(stack, bufferSource, packedLight, entity, arm, armPose,  partialTick);
                } else if (instance != null && instance.ability instanceof HypnosisAbility) {
                    if (instance.getController().getHoldTicks() > 0) {
                        super.renderFirstPersonOnArms(stack, bufferSource, packedLight, entity, arm, armPose,  partialTick);
                    }
                }
            } else {
                if (entity instanceof AbstractLuminarcticLeopard WILD_LUMI && WILD_LUMI.getTarget() != null) {
                    super.renderFirstPersonOnArms(stack, bufferSource, packedLight, entity, arm, armPose,  partialTick);
                }
            }

        }
    }

    public static class GlowFelineEyesLayer<M extends AdvancedHumanoidModel<T>, T extends ChangedEntity> extends EmissiveBodyLayer<M, T> {
        public GlowFelineEyesLayer(RenderLayerParent<T, M> p_116964_, ResourceLocation emissiveTexture) {
            super(p_116964_, emissiveTexture);
        }

        @Override
        public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (entity.getUnderlyingPlayer() != null) {
                return;
            }
            if (entity instanceof LuminarcticLeopardMaleEntity WILD_LUMI && WILD_LUMI.getTarget() != null) {
                super.render(poseStack, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            }
        }
    }

    public static class LuminarcticCustomEyesLayer<M extends AdvancedHumanoidModel<T>, T extends ChangedEntity> extends RenderLayer<T, M> {

        private final CustomEyesLayer<M, T> customEyesLayer, customGlowEyesLayer;

        public LuminarcticCustomEyesLayer(RenderLayerParent<T, M> parent, CustomEyesLayer<M, T> customEyesLayer, CustomEyesLayer<M, T> customGlowEyesLayer) {
            super(parent);
            this.customEyesLayer = customEyesLayer;
            this.customGlowEyesLayer = customGlowEyesLayer;
        }

        @Override
        public void render(@NotNull PoseStack pose, @NotNull MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (entity.getUnderlyingPlayer() != null) {
                Player player = entity.getUnderlyingPlayer();
                var instance = ProcessTransfur.getPlayerTransfurVariant(player).getSelectedAbility();
                if (instance != null && instance.ability instanceof HypnosisAbility) {
                    if (instance.getController().getHoldTicks() > 0) {
                        this.customGlowEyesLayer.render(pose, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                    } else {
                        this.customEyesLayer.render(pose, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                    }
                } else if (entity instanceof LuminarcticLeopardMaleEntity LUMI && LUMI.isActivatedAbility()) {
                    this.customGlowEyesLayer.render(pose, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                } else {
                    customEyesLayer.render(pose, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                }
            }
        }
    }
}