
package net.foxyas.changedaddon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.client.model.LatexSquidTigerSharkModel;
import net.foxyas.changedaddon.entity.LatexSquidTigerSharkEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.*;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleSquidDogModel;
import net.ltxprogrammer.changed.entity.beast.LatexSquidDogMale;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class LatexSquidTigerSharkRenderer extends AdvancedHumanoidRenderer<LatexSquidTigerSharkEntity, LatexSquidTigerSharkModel, ArmorLatexMaleSquidDogModel<LatexSquidTigerSharkEntity>> {
	public LatexSquidTigerSharkRenderer(EntityRendererProvider.Context context) {
		super(context, new LatexSquidTigerSharkModel(context.bakeLayer(LatexSquidTigerSharkModel.LAYER_LOCATION)),
				ArmorLatexMaleSquidDogModel::new, ArmorLatexMaleSquidDogModel.INNER_ARMOR, ArmorLatexMaleSquidDogModel.OUTER_ARMOR, 0.65f);
		this.addLayer(new DoubleItemInHandLayer<>(this));
		this.addLayer(new LatexParticlesLayer<>(this, getModel()));
		this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
		this.addLayer(new CustomEyesLayer<>(this, context.getModelSet()));
		//this.addLayer(CustomEyesLayer.builder(this, context.getModelSet()));
				//.withSclera(Color3.fromInt(0x1b1b1b)).withIris(Color3.fromInt(0xdfdfdf)).build());
		this.addLayer(GasMaskLayer.forSnouted(this, context.getModelSet()));
	}

	@Override
	public ResourceLocation getTextureLocation(LatexSquidTigerSharkEntity entity) {
		return new ResourceLocation("changed_addon:textures/entities/latex_tiger_squid_shark.png");
	}

	@Override
	protected void scale(LatexSquidTigerSharkEntity entity, PoseStack pose, float partialTick) {
		float f = 1.0525F;
		pose.scale(1.0525F, 1.0525F, 1.0525F);
	}
}
