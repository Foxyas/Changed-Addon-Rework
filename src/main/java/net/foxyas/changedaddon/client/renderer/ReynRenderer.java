package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.ReynModel;
import net.foxyas.changedaddon.client.renderer.layers.CustomDisplay;
import net.foxyas.changedaddon.entity.simple.ReynEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ReynRenderer extends AdvancedHumanoidRenderer<ReynEntity, ReynModel, ArmorLatexMaleWolfModel<ReynEntity>> {
    public ReynRenderer(EntityRendererProvider.Context context) {
        super(context, new ReynModel(context.bakeLayer(ReynModel.LAYER_LOCATION)), ArmorLatexMaleWolfModel.MODEL_SET, 0.5f);
        this.addLayer(new CustomDisplay<>(this, getModel(), new ResourceLocation("changed_addon:textures/entities/reyn_eye_display.png"), new ResourceLocation("changed_addon:textures/entities/reyn_display.png"), true));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(ReynEntity entity) {
        return new ResourceLocation("changed_addon:textures/entities/reyn.png");
    }
}
