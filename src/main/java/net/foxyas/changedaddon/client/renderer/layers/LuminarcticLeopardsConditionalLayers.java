package net.foxyas.changedaddon.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.entity.defaults.AbstractLuminarcticLeopard;
import net.foxyas.changedaddon.entity.defaults.AbstractLuminarcticLeopard.GlowState;
import net.ltxprogrammer.changed.ability.HypnosisAbility;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.EmissiveBodyLayer;
import net.ltxprogrammer.changed.client.renderer.layers.FirstPersonLayer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.model.geom.EntityModelSet;
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

    // ------------------------------------------------------------------
    // Shared checks
    // ------------------------------------------------------------------

    /** True if the player currently has the Hypnosis ability selected. */
    private static boolean isUsingHypnosis(Player player) {
        var variant = ProcessTransfur.getPlayerTransfurVariant(player);
        if (variant == null) return false;
        var ability = variant.getSelectedAbility();
        return ability != null && ability.ability instanceof HypnosisAbility;
    }

    /** True if the player has Hypnosis selected and is actively holding it. */
    private static boolean isHypnosisActive(Player player) {
        var variant = ProcessTransfur.getPlayerTransfurVariant(player);
        if (variant == null) return false;
        var ability = variant.getSelectedAbility();
        return ability != null
                && ability.ability instanceof HypnosisAbility
                && ability.getController().getHoldTicks() > 0;
    }

    private static boolean hasGlowingMarks(ChangedEntity entity) {
        return entity instanceof AbstractLuminarcticLeopard leopard && leopard.hasGlowingMarks();
    }

    // ------------------------------------------------------------------
    // Body glow
    // ------------------------------------------------------------------

    public static class GlowLayer<M extends AdvancedHumanoidModel<T>, T extends ChangedEntity> extends EmissiveBodyLayer<M, T> implements FirstPersonLayer<T> {
        public GlowLayer(RenderLayerParent<T, M> parent, ResourceLocation emissiveTexture) {
            super(parent, emissiveTexture);
        }

        @Override
        public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            Player player = entity.getUnderlyingPlayer();

            // Player transfurred into a leopard
            if (player != null) {
                if (isUsingHypnosis(player)) {
                    if (isHypnosisActive(player)) {
                        super.render(poseStack, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                    }
                } else if (entity instanceof AbstractLuminarcticLeopard leopard && leopard.hasGlowingMarks()) {
                    renderGlowState(leopard.getGlowStage(), poseStack, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                }
            }

            // Wild leopard that has a target
            if (entity instanceof AbstractLuminarcticLeopard leopard && leopard.getTarget() != null) {
                renderGlowState(leopard.getGlowStage(), poseStack, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            }
        }

        private void renderGlowState(GlowState state, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            switch (state) {
                case PULSE -> {
                    // Smoothly oscillates between 0 and 1; the glow fades from full to dark
                    float pulse = ((float) Math.sin(ageInTicks * 0.1f) + 1f) / 2f;
                    renderTinted(poseStack, bufferSource, packedLight, 1f - pulse);
                }
                case ALWAYS -> renderTinted(poseStack, bufferSource, packedLight, 1f);
                case NONE -> super.render(poseStack, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            }
        }

        private void renderTinted(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, float shade) {
            VertexConsumer consumer = bufferSource.getBuffer(this.renderType());
            this.getParentModel().renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, shade, shade, shade, 1.0f);
        }

        @Override
        public void renderFirstPersonOnArms(PoseStack stack, MultiBufferSource bufferSource, int packedLight, T entity, HumanoidArm arm, PartPose armPose, float partialTick) {
            Player player = entity.getUnderlyingPlayer();

            boolean shouldRender;
            if (player != null) {
                shouldRender = hasGlowingMarks(entity) || isHypnosisActive(player);
            } else {
                shouldRender = entity instanceof AbstractLuminarcticLeopard leopard && leopard.getTarget() != null;
            }

            if (shouldRender) {
                super.renderFirstPersonOnArms(stack, bufferSource, packedLight, entity, arm, armPose, partialTick);
            }
        }
    }

    // ------------------------------------------------------------------
    // Feline eyes (wild leopards only, while they have a target)
    // ------------------------------------------------------------------

    public static class GlowFelineEyesLayer<M extends AdvancedHumanoidModel<T>, T extends ChangedEntity> extends EmissiveBodyLayer<M, T> {
        public GlowFelineEyesLayer(RenderLayerParent<T, M> parent, ResourceLocation emissiveTexture) {
            super(parent, emissiveTexture);
        }

        @Override
        public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (entity.getUnderlyingPlayer() == null
                    && entity instanceof AbstractLuminarcticLeopard leopard
                    && leopard.getTarget() != null) {
                super.render(poseStack, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            }
        }
    }

    // ------------------------------------------------------------------
    // Player custom eyes (normal vs glowing)
    // ------------------------------------------------------------------

    public static class LuminarcticCustomEyesLayer<M extends AdvancedHumanoidModel<T>, T extends ChangedEntity> extends RenderLayer<T, M> {

        private final CustomEyesLayer<M, T> customEyesLayer, customGlowEyesLayer;

        public LuminarcticCustomEyesLayer(RenderLayerParent<T, M> parent, CustomEyesLayer<M, T> customEyesLayer, CustomEyesLayer<M, T> customGlowEyesLayer) {
            super(parent);
            this.customEyesLayer = customEyesLayer;
            this.customGlowEyesLayer = customGlowEyesLayer;
        }

        /** Builds the layer with the normal and glowing eye variants used by both leopards. */
        public static <M extends AdvancedHumanoidModel<T>, T extends ChangedEntity> LuminarcticCustomEyesLayer<M, T> create(RenderLayerParent<T, M> parent, EntityModelSet modelSet) {
            return new LuminarcticCustomEyesLayer<>(parent,
                    new CustomEyesLayer<>(parent, modelSet,
                            CustomEyesLayer::scleraColor,
                            CustomEyesLayer::irisColorLeft,
                            CustomEyesLayer::irisColorRight,
                            CustomEyesLayer::noRender,
                            CustomEyesLayer::noRender),
                    new CustomEyesLayer<>(parent, modelSet,
                            CustomEyesLayer::scleraColor,
                            CustomEyesLayer::glowingIrisColorLeft,
                            CustomEyesLayer::glowingIrisColorRight,
                            CustomEyesLayer::noRender,
                            CustomEyesLayer::noRender));
        }

        @Override
        public void render(@NotNull PoseStack pose, @NotNull MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            Player player = entity.getUnderlyingPlayer();
            if (player == null) return;

            boolean glowing = isUsingHypnosis(player) ? isHypnosisActive(player) : hasGlowingMarks(entity);
            CustomEyesLayer<M, T> layer = glowing ? customGlowEyesLayer : customEyesLayer;
            layer.render(pose, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        }
    }
}