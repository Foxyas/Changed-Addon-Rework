package net.foxyas.changedaddon;

import net.foxyas.changedaddon.client.model.ModelFemaleSnowFox;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.foxyas.changedaddon.client.model.ModelSnowFox;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class ChangedAddonModelsRegister {
	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ModelSnowFox.LAYER_LOCATION, ModelSnowFox::createBodyLayer);
		event.registerLayerDefinition(ModelFemaleSnowFox.LAYER_LOCATION, ModelFemaleSnowFox::createBodyLayer);
	}
}
