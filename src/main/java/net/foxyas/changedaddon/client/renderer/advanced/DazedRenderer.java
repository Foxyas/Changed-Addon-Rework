package net.foxyas.changedaddon.client.renderer.advanced;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.client.model.DazedLatexModel;
import net.foxyas.changedaddon.entity.advanced.DazedEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class DazedRenderer extends AdvancedHumanoidRenderer<DazedEntity, DazedLatexModel, ArmorLatexMaleWolfModel<DazedEntity>> {

    public DazedRenderer(EntityRendererProvider.Context context) {
        super(context, new DazedLatexModel(context.bakeLayer(DazedLatexModel.LAYER_LOCATION)),
                ArmorLatexMaleWolfModel.MODEL_SET, 0.5f);
        this.addLayer(new LatexParticlesLayer<>(this, getModel(), model::isPartNotPuddle));
        this.addLayer(new ConditionalCustomEyesLayer<>(this,
                new CustomEyesLayer<>(this, context.getModelSet(),
                        CustomEyesLayer.fixedColor(Color3.DARK),
                        CustomEyesLayer::glowingIrisColorLeft,
                        CustomEyesLayer::glowingIrisColorRight,
                        CustomEyesLayer::noRender,
                        CustomEyesLayer::noRender)));
        this.addLayer(new ConditionalCustomLayers<>(this,
                TransfurCapeLayer.normalCape(this, context.getModelSet()),
                new GasMaskLayer<>(this, context.getModelSet()),
                new LatexParticlesLayer<>(this, getModel(), model::isPartNotPuddle))
        );
    }


    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull DazedEntity entity) {
        if (entity.isMorphed()) {
            return new ResourceLocation("changed_addon:textures/entities/dazed_creature_puddle.png");
        }

        return new ResourceLocation("changed_addon:textures/entities/dazed_creature.png");
    }

    public static class ConditionalCustomEyesLayer<M extends AdvancedHumanoidModel<T>, T extends ChangedEntity> extends RenderLayer<T, M> {

        private final CustomEyesLayer<M, T> customEyesLayer;

        public ConditionalCustomEyesLayer(RenderLayerParent<T, M> parent, CustomEyesLayer<M, T> customEyesLayer) {
            super(parent);
            this.customEyesLayer = customEyesLayer;
        }

        @Override
        public void render(PoseStack pose, MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (entity instanceof DazedEntity dazedEntity
                    && !dazedEntity.isMorphed()) {
                customEyesLayer.render(pose, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            }
        }
    }

    public static class ConditionalCustomLayers<M extends AdvancedHumanoidModel<T>, T extends ChangedEntity> extends RenderLayer<T, M> {

        private final TransfurCapeLayer<T, M> transfurCapeLayer;
        private final GasMaskLayer<T, M> gasMaskLayer;
        private final LatexParticlesLayer<T, M> latexParticlesLayer;

        public ConditionalCustomLayers(
                RenderLayerParent<T, M> parent,
                TransfurCapeLayer<T, M> transfurCapeLayer,
                GasMaskLayer<T, M> gasMaskLayer,
                LatexParticlesLayer<T, M> latexParticlesLayer) {

            super(parent);
            this.transfurCapeLayer = transfurCapeLayer;
            this.gasMaskLayer = gasMaskLayer;
            this.latexParticlesLayer = latexParticlesLayer;
        }

        @Override
        public void render(PoseStack pose, MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (entity instanceof DazedEntity dazedEntity
                    && !dazedEntity.isMorphed()) {
                transfurCapeLayer.render(pose, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                gasMaskLayer.render(pose, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                latexParticlesLayer.render(pose, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            }
        }
    }
}