
package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.KetModel;
import net.ltxprogrammer.changed.client.renderer.LatexHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.EmissiveBodyLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexWolfModel;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.HumanoidModel;

import net.foxyas.changedaddon.entity.KetExperiment009Entity;

public class KetExperiment009Renderer extends LatexHumanoidRenderer<KetExperiment009Entity, KetModel, ArmorLatexWolfModel<KetExperiment009Entity>> {
	public KetExperiment009Renderer(EntityRendererProvider.Context context) {
	super(context, new KetModel(context.bakeLayer(KetModel.LAYER_LOCATION)),
				ArmorLatexWolfModel::new, ArmorLatexWolfModel.INNER_ARMOR, ArmorLatexWolfModel.OUTER_ARMOR, 0.5f);
		this.addLayer(new EmissiveBodyLayer<>(this, new ResourceLocation("changed_addon","textures/entities/ketmodel_glowtexture.png")));
		this.addLayer(new LatexParticlesLayer<>(this, getModel()));
	//  this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer::scleraColor,CustomEyesLayer.fixedColorGlowing(Color3.parseHex("#66FFFF"))));
	}

	@Override
	public ResourceLocation getTextureLocation(KetExperiment009Entity entity) {
		return new ResourceLocation("changed_addon:textures/entities/kettexture.png");
	}

}
