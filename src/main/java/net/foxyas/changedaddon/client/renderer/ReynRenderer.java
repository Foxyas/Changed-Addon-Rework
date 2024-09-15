
package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.ReynModel;
import net.foxyas.changedaddon.client.renderer.layers.CustomEyeDisplay;
import net.foxyas.changedaddon.entity.ReynEntity;
import net.ltxprogrammer.changed.client.renderer.LatexHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.HumanoidModel;

import net.foxyas.changedaddon.entity.ReynEntity;

public class ReynRenderer extends LatexHumanoidRenderer<ReynEntity, ReynModel, ArmorLatexMaleWolfModel<ReynEntity>> {
	public ReynRenderer(EntityRendererProvider.Context context) {
		super(context, new ReynModel(context.bakeLayer(ReynModel.LAYER_LOCATION)), ArmorLatexMaleWolfModel::new,ArmorLatexMaleWolfModel.INNER_ARMOR,ArmorLatexMaleWolfModel.OUTER_ARMOR, 0.5f);
		this.addLayer(new CustomEyeDisplay<>(this, getModel(), new ResourceLocation("changed_addon:textures/entities/reyn_eye_display.png"), new ResourceLocation("changed_addon:textures/entities/reyn_display.png"), true));
		this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
	}

	@Override
	public ResourceLocation getTextureLocation(ReynEntity entity) {
		return new ResourceLocation("changed_addon:textures/entities/reyn.png");
	}
}
