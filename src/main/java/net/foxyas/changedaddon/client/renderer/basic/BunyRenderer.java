package net.foxyas.changedaddon.client.renderer.basic;

import net.foxyas.changedaddon.client.model.BunyModel;
import net.foxyas.changedaddon.entity.simple.BunyEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BunyRenderer extends AdvancedHumanoidRenderer<BunyEntity, BunyModel> {
    public BunyRenderer(EntityRendererProvider.Context context) {
        super(context, new BunyModel(context.bakeLayer(BunyModel.LAYER_LOCATION)),
                ArmorLatexMaleWolfModel.MODEL_SET, 0.5f);
        //this.addLayer(new LatexParticlesLayer<>(this, getModel())); Is Organic .-.
        this.addLayer(CustomEyesLayer.builder(this, context.getModelSet()).withSclera(CustomEyesLayer::scleraColor).withLeftIris(CustomEyesLayer::irisColorLeft).withRightIris(CustomEyesLayer::irisColorRight).build());
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull BunyEntity entity) {
        return ResourceLocation.parse("changed_addon:textures/entities/buny.png");
    }
}