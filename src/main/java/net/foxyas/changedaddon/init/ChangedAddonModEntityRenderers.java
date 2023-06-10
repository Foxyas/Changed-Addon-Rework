
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.foxyas.changedaddon.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.foxyas.changedaddon.client.renderer.PrototypeRenderer;
import net.foxyas.changedaddon.client.renderer.FoxyasRenderer;
import net.foxyas.changedaddon.client.renderer.Experiment009phase2Renderer;
import net.foxyas.changedaddon.client.renderer.Experiment009Renderer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ChangedAddonModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ChangedAddonModEntities.PROTOTYPE.get(), PrototypeRenderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.FOXYAS.get(), FoxyasRenderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.EXPERIMENT_009.get(), Experiment009Renderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.EXPERIMENT_009_PHASE_2.get(), Experiment009phase2Renderer::new);
	}
}
