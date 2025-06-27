
package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.SnepsiLeopardModel;
import net.foxyas.changedaddon.entity.SnepsiLeopardEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleCatModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SnepsiLeopardRenderer extends AdvancedHumanoidRenderer<SnepsiLeopardEntity, SnepsiLeopardModel, ArmorLatexMaleCatModel<SnepsiLeopardEntity>> {
	public SnepsiLeopardRenderer(EntityRendererProvider.Context context) {
		super(context, new SnepsiLeopardModel(context.bakeLayer(SnepsiLeopardModel.LAYER_LOCATION)), ArmorLatexMaleCatModel::new, ArmorLatexMaleCatModel.INNER_ARMOR, ArmorLatexMaleCatModel.OUTER_ARMOR, 0.5F);
		//this.addLayer(new LatexParticlesLayer<>(this, this.getModel()));
		this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
		this.addLayer(new CustomEyesLayer<>(this, context.getModelSet()));
		this.addLayer(GasMaskLayer.forSnouted(this, context.getModelSet()));
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(@NotNull SnepsiLeopardEntity entity) {
		return ResourceLocation.parse("changed_addon:textures/entities/snepsi_snow_leopard.png");
	}
}
