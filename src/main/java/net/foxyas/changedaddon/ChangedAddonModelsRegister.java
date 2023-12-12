package net.foxyas.changedaddon;

import net.foxyas.changedaddon.client.model.ModelFemaleSnowFox;
import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import net.foxyas.changedaddon.init.ChangedAddonModTabs;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.BlockItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.item.ItemProperties;


import net.foxyas.changedaddon.client.model.ModelSnowFox;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class ChangedAddonModelsRegister {
	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ModelSnowFox.LAYER_LOCATION, ModelSnowFox::createBodyLayer);
		event.registerLayerDefinition(ModelFemaleSnowFox.LAYER_LOCATION, ModelFemaleSnowFox::createBodyLayer);
	}
}