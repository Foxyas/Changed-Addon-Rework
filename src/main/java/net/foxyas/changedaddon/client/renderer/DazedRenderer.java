
package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.DazedLatexModel;
import net.foxyas.changedaddon.entity.DazedEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import static net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer.fixedColorGlowing;

public class DazedRenderer extends AdvancedHumanoidRenderer<DazedEntity, DazedLatexModel, ArmorLatexMaleWolfModel<DazedEntity>> {
		public DazedRenderer(EntityRendererProvider.Context context) {
			super(context, new DazedLatexModel(context.bakeLayer(DazedLatexModel.LAYER_LOCATION)),
			ArmorLatexMaleWolfModel::new, ArmorLatexMaleWolfModel.INNER_ARMOR, ArmorLatexMaleWolfModel.OUTER_ARMOR, 0.5f);
			this.addLayer(new LatexParticlesLayer<>(this, getModel()));
			this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(),
					CustomEyesLayer.fixedColor(Color3.DARK),
					CustomEyesLayer::glowingIrisColorLeft,
					CustomEyesLayer::glowingIrisColorRight,
					CustomEyesLayer::noRender,
					CustomEyesLayer::noRender));
			this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
			this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
		}


		@Override
	public ResourceLocation getTextureLocation(DazedEntity entity) {
		return new ResourceLocation("changed_addon:textures/entities/dazedthing.png");
	}
}