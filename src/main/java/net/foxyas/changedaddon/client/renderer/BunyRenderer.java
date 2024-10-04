
package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.BunyModel;
import net.foxyas.changedaddon.client.model.BunyModel;
import net.foxyas.changedaddon.entity.BunyEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.HumanoidModel;

import net.foxyas.changedaddon.entity.BunyEntity;

public class BunyRenderer extends AdvancedHumanoidRenderer<BunyEntity, BunyModel, ArmorLatexMaleWolfModel<BunyEntity>> {
	public BunyRenderer(EntityRendererProvider.Context context) {
		super(context, new BunyModel(context.bakeLayer(BunyModel.LAYER_LOCATION)),
			ArmorLatexMaleWolfModel::new, ArmorLatexMaleWolfModel.INNER_ARMOR, ArmorLatexMaleWolfModel.OUTER_ARMOR, 0.5f);
		//this.addLayer(new LatexParticlesLayer<>(this, getModel())); Is Organic .-.
		this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer::scleraColor,CustomEyesLayer::irisColorLeft,CustomEyesLayer::irisColorRight));
	}

	@Override
	public ResourceLocation getTextureLocation(BunyEntity entity) {
		return new ResourceLocation("changed_addon:textures/entities/buny.png");
	}
}