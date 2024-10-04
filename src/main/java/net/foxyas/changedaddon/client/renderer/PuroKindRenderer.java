
package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.PuroKindModel;
import net.foxyas.changedaddon.client.model.PuroKindModel;
import net.foxyas.changedaddon.entity.PuroKindEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;


public class PuroKindRenderer extends AdvancedHumanoidRenderer<PuroKindEntity, PuroKindModel, ArmorLatexMaleWolfModel<PuroKindEntity>> {
	public PuroKindRenderer(EntityRendererProvider.Context context) {
		super(context, new PuroKindModel(context.bakeLayer(PuroKindModel.LAYER_LOCATION)),ArmorLatexMaleWolfModel::new,ArmorLatexMaleWolfModel.INNER_ARMOR,ArmorLatexMaleWolfModel.OUTER_ARMOR, 0.5f);
		this.addLayer(new LatexParticlesLayer<>(this, getModel(), model::isPartNotMask));
		this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
		this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer.fixedColor(Color3.parseHex("#242424")),CustomEyesLayer::glowingIrisColorLeft,CustomEyesLayer::glowingIrisColorRight));
		this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
	}


	@Override
	public ResourceLocation getTextureLocation(PuroKindEntity entity) {
		return new ResourceLocation("changed_addon:textures/entities/puro_kind_texture.png");
	}
}