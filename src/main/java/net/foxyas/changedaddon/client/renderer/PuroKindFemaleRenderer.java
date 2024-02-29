
package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.PuroKindFemaleModel;
import net.foxyas.changedaddon.entity.PuroKindFemaleEntity;
import net.ltxprogrammer.changed.client.renderer.LatexHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;


public class PuroKindFemaleRenderer extends LatexHumanoidRenderer<PuroKindFemaleEntity, PuroKindFemaleModel, ArmorLatexMaleWolfModel<PuroKindFemaleEntity>> {
	public PuroKindFemaleRenderer(EntityRendererProvider.Context context) {
		super(context, new PuroKindFemaleModel(context.bakeLayer(PuroKindFemaleModel.LAYER_LOCATION)),ArmorLatexMaleWolfModel::new,ArmorLatexMaleWolfModel.INNER_ARMOR,ArmorLatexMaleWolfModel.OUTER_ARMOR, 0.5f);
		this.addLayer(new LatexParticlesLayer<>(this, getModel()));
		this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer.fixedColor(Color3.parseHex("#242424")),CustomEyesLayer::glowingIrisColor));
	}


	@Override
	public ResourceLocation getTextureLocation(PuroKindFemaleEntity entity) {
		return new ResourceLocation("changed_addon:textures/entities/puro_kind_female_texture.png");
	}
}