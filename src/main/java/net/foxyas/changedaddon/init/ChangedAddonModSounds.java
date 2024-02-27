
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.foxyas.changedaddon.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.RegistryEvent;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.HashMap;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonModSounds {
	public static Map<ResourceLocation, SoundEvent> REGISTRY = new HashMap<>();
	static {
		REGISTRY.put(new ResourceLocation("changed_addon", "grab_start_sound"), new SoundEvent(new ResourceLocation("changed_addon", "grab_start_sound")));
		REGISTRY.put(new ResourceLocation("changed_addon", "armor_equip"), new SoundEvent(new ResourceLocation("changed_addon", "armor_equip")));
		REGISTRY.put(new ResourceLocation("changed_addon", "warn"), new SoundEvent(new ResourceLocation("changed_addon", "warn")));
		REGISTRY.put(new ResourceLocation("changed_addon", "experiment009_theme"), new SoundEvent(new ResourceLocation("changed_addon", "experiment009_theme")));
		REGISTRY.put(new ResourceLocation("changed_addon", "experiment009_theme_phase2"), new SoundEvent(new ResourceLocation("changed_addon", "experiment009_theme_phase2")));
		REGISTRY.put(new ResourceLocation("changed_addon", "untransfursound"), new SoundEvent(new ResourceLocation("changed_addon", "untransfursound")));
		REGISTRY.put(new ResourceLocation("changed_addon", "spray.sound"), new SoundEvent(new ResourceLocation("changed_addon", "spray.sound")));
	}

	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
		for (Map.Entry<ResourceLocation, SoundEvent> sound : REGISTRY.entrySet())
			event.getRegistry().register(sound.getValue().setRegistryName(sound.getKey()));
	}
}
