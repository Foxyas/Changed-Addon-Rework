
package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.FoxtaFoxyModel;
import net.foxyas.changedaddon.entity.FoxtaFoxyEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class FoxtaFoxyRenderer extends AdvancedHumanoidRenderer<FoxtaFoxyEntity, FoxtaFoxyModel, ArmorLatexMaleWolfModel<FoxtaFoxyEntity>> {
	public FoxtaFoxyRenderer(EntityRendererProvider.Context context) {
		super(context, new FoxtaFoxyModel(context.bakeLayer(FoxtaFoxyModel.LAYER_LOCATION)), ArmorLatexMaleWolfModel::new, ArmorLatexMaleWolfModel.INNER_ARMOR, ArmorLatexMaleWolfModel.OUTER_ARMOR, 0.5F);
		//this.addLayer(new LatexParticlesLayer<>(this, this.getModel()));
		this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
		this.addLayer(new CustomEyesLayer<>(this, context.getModelSet()));
		this.addLayer(GasMaskLayer.forSnouted(this, context.getModelSet()));
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(@NotNull FoxtaFoxyEntity entity) {
		return new ResourceLocation("changed_addon:textures/entities/foxta_fox.png");
	}
}
