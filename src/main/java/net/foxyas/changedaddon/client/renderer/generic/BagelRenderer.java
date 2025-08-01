package net.foxyas.changedaddon.client.renderer.generic;

import net.foxyas.changedaddon.client.model.BagelModel;
import net.foxyas.changedaddon.entity.simple.BagelEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.*;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class BagelRenderer extends AdvancedHumanoidRenderer<BagelEntity, BagelModel, ArmorLatexMaleWolfModel<BagelEntity>> {
    public BagelRenderer(EntityRendererProvider.Context context) {
        super(context, new BagelModel(context.bakeLayer(BagelModel.LAYER_LOCATION)), ArmorLatexMaleWolfModel.MODEL_SET, 0.5f);
        this.addLayer(new LatexParticlesLayer<>(this, getModel()));
        this.addLayer(new CustomEyesLayer<>(this, context.getModelSet()));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
        this.addLayer(new EmissiveBodyLayer<>(this, new ResourceLocation("changed_addon:textures/entities/bagel_transfur_glow.png")));
    }

    @Override
    public ResourceLocation getTextureLocation(BagelEntity entity) {
        return new ResourceLocation("changed_addon:textures/entities/bagel_transfur.png");
    }
}