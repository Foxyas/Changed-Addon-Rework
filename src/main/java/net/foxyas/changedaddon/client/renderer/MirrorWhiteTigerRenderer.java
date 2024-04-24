
package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.ModelMirrorWhiteTiger;
import net.foxyas.changedaddon.entity.MirrorWhiteTigerEntity;
import net.ltxprogrammer.changed.client.renderer.LatexHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleCatModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.HumanoidModel;

import net.foxyas.changedaddon.entity.MirrorWhiteTigerEntity;

public class MirrorWhiteTigerRenderer extends LatexHumanoidRenderer<MirrorWhiteTigerEntity, ModelMirrorWhiteTiger, ArmorLatexMaleCatModel<MirrorWhiteTigerEntity>> {
	public MirrorWhiteTigerRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelMirrorWhiteTiger(context.bakeLayer(ModelMirrorWhiteTiger.LAYER_LOCATION)),
				ArmorLatexMaleCatModel::new, ArmorLatexMaleCatModel.INNER_ARMOR, ArmorLatexMaleCatModel.OUTER_ARMOR, 0.5f);
		//this.addLayer(new LatexParticlesLayer<>(this, getModel()));
		this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
		this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer::scleraColor,CustomEyesLayer::irisColorLeft,CustomEyesLayer::irisColorRight));
		this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
	}

	@Override
	public ResourceLocation getTextureLocation(MirrorWhiteTigerEntity entity) {
		return new ResourceLocation("changed_addon:textures/entities/mirror_white_tiger_female.png");
	}
}
