package net.foxyas.changedaddon.client.renderer.advanced;

import net.foxyas.changedaddon.client.model.BagelModel;
import net.foxyas.changedaddon.client.model.advanced.AvaliModel;
import net.foxyas.changedaddon.client.renderer.layers.AvaliColorsLayer;
import net.foxyas.changedaddon.entity.BagelEntity;
import net.foxyas.changedaddon.entity.advanced.AvaliEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.*;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleDragonModel;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class AvaliRenderer extends AdvancedHumanoidRenderer<AvaliEntity, AvaliModel, ArmorLatexMaleDragonModel<AvaliEntity>> {
    public AvaliRenderer(EntityRendererProvider.Context context) {
        super(context, new AvaliModel(context.bakeLayer(AvaliModel.LAYER_LOCATION)), ArmorLatexMaleDragonModel::new, ArmorLatexMaleDragonModel.INNER_ARMOR, ArmorLatexMaleDragonModel.OUTER_ARMOR, 0.5f);
        //this.addLayer(new LatexParticlesLayer<>(this, getModel()));
        this.addLayer(new CustomEyesLayer<>(this, context.getModelSet()));
        this.addLayer(new AvaliColorsLayer<>(this, this.getModel(), new ResourceLocation("changed_addon:textures/entities/avali_%s/avali_%s_primary.png"), 0));
        this.addLayer(new AvaliColorsLayer<>(this, this.getModel(), new ResourceLocation("changed_addon:textures/entities/avali_%s/avali_%s_secondary.png"), 1));
        this.addLayer(new AvaliColorsLayer<>(this, this.getModel(), new ResourceLocation("changed_addon:textures/entities/avali_%s/avali_%s_stripes.png"), 2));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(AvaliEntity entity) {
        return new ResourceLocation("changed_addon:textures/entities/avali.png");
    }
}
