
package net.foxyas.changedaddon.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.foxyas.changedaddon.entity.Experiment009Entity;
import net.foxyas.changedaddon.client.model.Modeljack_model;

public class Experiment009Renderer extends MobRenderer<Experiment009Entity, Modeljack_model<Experiment009Entity>> {
	public Experiment009Renderer(EntityRendererProvider.Context context) {
		super(context, new Modeljack_model(context.bakeLayer(Modeljack_model.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(Experiment009Entity entity) {
		return new ResourceLocation("changed_addon:textures/entities/jackmodeltexture.png");
	}
}
