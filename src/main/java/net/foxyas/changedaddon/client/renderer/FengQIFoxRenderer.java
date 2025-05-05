
package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.FengQIFoxModel;
import net.foxyas.changedaddon.entity.FengQIFoxEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class FengQIFoxRenderer extends AdvancedHumanoidRenderer<FengQIFoxEntity, FengQIFoxModel, ArmorLatexMaleWolfModel<FengQIFoxEntity>> {
	public FengQIFoxRenderer(EntityRendererProvider.Context context) {
		super(context, new FengQIFoxModel(context.bakeLayer(FengQIFoxModel.LAYER_LOCATION)), ArmorLatexMaleWolfModel::new, ArmorLatexMaleWolfModel.INNER_ARMOR, ArmorLatexMaleWolfModel.OUTER_ARMOR, 0.5F);
		//this.addLayer(new LatexParticlesLayer<>(this, this.getModel()));
		this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
		this.addLayer(new CustomEyesLayer<>(this, context.getModelSet()));
		this.addLayer(GasMaskLayer.forSnouted(this, context.getModelSet()));
	}

	@Override
	public ResourceLocation getTextureLocation(FengQIFoxEntity entity) {
		return new ResourceLocation("changed_addon:textures/entities/feng_qi_fox.png");
	}
}
