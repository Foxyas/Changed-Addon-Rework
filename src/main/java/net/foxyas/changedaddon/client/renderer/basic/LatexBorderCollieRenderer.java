package net.foxyas.changedaddon.client.renderer.basic;

import net.foxyas.changedaddon.client.model.simple.LatexBorderCollieModel;
import net.foxyas.changedaddon.entity.simple.LatexBorderCollieEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class LatexBorderCollieRenderer extends AdvancedHumanoidRenderer<LatexBorderCollieEntity, LatexBorderCollieModel> {
    public LatexBorderCollieRenderer(EntityRendererProvider.Context context) {
        super(context, new LatexBorderCollieModel(context.bakeLayer(LatexBorderCollieModel.LAYER_LOCATION)),
                ArmorLatexMaleWolfModel.MODEL_SET, 0.5f);
        this.addLayer(new LatexParticlesLayer<>(this, getModel()));
        this.addLayer(CustomEyesLayer.builder(this, context.getModelSet()).withSclera(CustomEyesLayer::scleraColor).withLeftIris(CustomEyesLayer::irisColorLeft).withRightIris(CustomEyesLayer::irisColorRight).build());
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull LatexBorderCollieEntity entity) {
        return ResourceLocation.parse("changed_addon:textures/entities/latex_border_collie/latex_border_collie.png");
    }
}