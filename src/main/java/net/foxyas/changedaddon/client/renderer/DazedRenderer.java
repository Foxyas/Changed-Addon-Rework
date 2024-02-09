
package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.DazedLatexModel;
import net.foxyas.changedaddon.entity.DazedEntity;
import net.ltxprogrammer.changed.client.renderer.LatexHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
	public class DazedRenderer extends LatexHumanoidRenderer<DazedEntity, DazedLatexModel, ArmorLatexMaleWolfModel<DazedEntity>> {
		public DazedRenderer(EntityRendererProvider.Context context) {
			super(context, new DazedLatexModel(context.bakeLayer(DazedLatexModel.LAYER_LOCATION)),
					ArmorLatexMaleWolfModel::new, ArmorLatexMaleWolfModel.INNER_ARMOR, ArmorLatexMaleWolfModel.OUTER_ARMOR, 0.5f);
			this.addLayer(new LatexParticlesLayer<>(this, getModel()));
			this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer.fixedColor(Color3.DARK),CustomEyesLayer::irisColor));
		}


		@Override
	public ResourceLocation getTextureLocation(DazedEntity entity) {
		return new ResourceLocation("changed_addon:textures/entities/dazedthing.png");
	}
}
