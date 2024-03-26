package net.foxyas.changedaddon;

import net.foxyas.changedaddon.client.model.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class ChangedAddonModelsRegister {
	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ModelSnowFox.LAYER_LOCATION, ModelSnowFox::createBodyLayer);
		event.registerLayerDefinition(ModelFemaleSnowFox.LAYER_LOCATION, ModelFemaleSnowFox::createBodyLayer);
		event.registerLayerDefinition(DazedLatexModel.LAYER_LOCATION, DazedLatexModel::createBodyLayer);
		event.registerLayerDefinition(PuroKindModel.LAYER_LOCATION, PuroKindModel::createBodyLayer);
		event.registerLayerDefinition(PuroKindFemaleModel.LAYER_LOCATION, PuroKindFemaleModel::createBodyLayer);
		event.registerLayerDefinition(BunyModel.LAYER_LOCATION, BunyModel::createBodyLayer);
		event.registerLayerDefinition(OrganicSnowLeopardMaleModel.LAYER_LOCATION, OrganicSnowLeopardMaleModel::createBodyLayer);
		event.registerLayerDefinition(OrganicSnowLeopardFemaleModel.LAYER_LOCATION, OrganicSnowLeopardFemaleModel::createBodyLayer);
		event.registerLayerDefinition(KetModel.LAYER_LOCATION, KetModel::createBodyLayer);
	}
}