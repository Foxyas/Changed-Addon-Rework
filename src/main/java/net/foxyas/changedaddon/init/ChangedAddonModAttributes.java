/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.foxyas.changedaddon.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.EntityType;

import net.foxyas.changedaddon.ChangedAddonMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonModAttributes {
	public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, ChangedAddonMod.MODID);
	public static final RegistryObject<Attribute> LATEXRESISTANCE = ATTRIBUTES.register("latexresistance", () -> (new RangedAttribute("attribute." + ChangedAddonMod.MODID + ".latexresistance", 0, 0, 100)).setSyncable(true));
	public static final RegistryObject<Attribute> LATEXINFECTION = ATTRIBUTES.register("latexinfection", () -> (new RangedAttribute("attribute." + ChangedAddonMod.MODID + ".latexinfection", 0, 0, 100)).setSyncable(true));

	@SubscribeEvent
	public static void register(FMLConstructModEvent event) {
		event.enqueueWork(() -> {
			ATTRIBUTES.register(FMLJavaModLoadingContext.get().getModEventBus());
		});
	}

	@SubscribeEvent
	public static void addAttributes(EntityAttributeModificationEvent event) {
		event.add(EntityType.PLAYER, LATEXRESISTANCE.get());
		event.add(EntityType.PLAYER, LATEXINFECTION.get());
	}

	@Mod.EventBusSubscriber
	private class Utils {
		@SubscribeEvent
		public static void persistAttributes(PlayerEvent.Clone event) {
			Player oldP = event.getOriginal();
			Player newP = (Player) event.getEntity();
			newP.getAttribute(LATEXRESISTANCE.get()).setBaseValue(oldP.getAttribute(LATEXRESISTANCE.get()).getBaseValue());
			newP.getAttribute(LATEXINFECTION.get()).setBaseValue(oldP.getAttribute(LATEXINFECTION.get()).getBaseValue());
		}
	}
}
