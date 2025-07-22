package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.BunyModel;
import net.foxyas.changedaddon.entity.BunyEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class BunyRenderer extends AdvancedHumanoidRenderer<BunyEntity, BunyModel, ArmorLatexMaleWolfModel<BunyEntity>> {
    public BunyRenderer(EntityRendererProvider.Context context) {
        super(context, new BunyModel(context.bakeLayer(BunyModel.LAYER_LOCATION)),
                ArmorLatexMaleWolfModel.MODEL_SET, 0.5f);
        //this.addLayer(new LatexParticlesLayer<>(this, getModel())); Is Organic .-.
        this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer::scleraColor, CustomEyesLayer::irisColorLeft, CustomEyesLayer::irisColorRight));
    }

    @Override
    public ResourceLocation getTextureLocation(BunyEntity entity) {
        return new ResourceLocation("changed_addon:textures/entities/buny.png");
    }
}