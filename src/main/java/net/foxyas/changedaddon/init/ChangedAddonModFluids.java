
/*
 * MCreator note: This file will be REGENERATED on each build.
 */
package net.foxyas.changedaddon.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;

import net.foxyas.changedaddon.fluid.LitixCamoniaFluidFluid;
import net.foxyas.changedaddon.ChangedAddonMod;

public class ChangedAddonModFluids {
	public static final DeferredRegister<Fluid> REGISTRY = DeferredRegister.create(ForgeRegistries.FLUIDS, ChangedAddonMod.MODID);
	public static final RegistryObject<Fluid> LITIX_CAMONIA_FLUID = REGISTRY.register("litix_camonia_fluid", () -> new LitixCamoniaFluidFluid.Source());
	public static final RegistryObject<Fluid> FLOWING_LITIX_CAMONIA_FLUID = REGISTRY.register("flowing_litix_camonia_fluid", () -> new LitixCamoniaFluidFluid.Flowing());

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ClientSideHandler {
		@SubscribeEvent
		public static void clientSetup(FMLClientSetupEvent event) {
			ItemBlockRenderTypes.setRenderLayer(LITIX_CAMONIA_FLUID.get(), renderType -> renderType == RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(FLOWING_LITIX_CAMONIA_FLUID.get(), renderType -> renderType == RenderType.translucent());
		}
	}
}
