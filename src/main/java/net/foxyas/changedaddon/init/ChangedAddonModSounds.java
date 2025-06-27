
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
		REGISTRY.put(ResourceLocation.parse("changed_addon", "armor_equip"), new SoundEvent(ResourceLocation.parse("changed_addon", "armor_equip")));
		REGISTRY.put(ResourceLocation.parse("changed_addon", "warn"), new SoundEvent(ResourceLocation.parse("changed_addon", "warn")));
		REGISTRY.put(ResourceLocation.parse("changed_addon", "untransfursound"), new SoundEvent(ResourceLocation.parse("changed_addon", "untransfursound")));
		REGISTRY.put(ResourceLocation.parse("changed_addon", "spray.sound"), new SoundEvent(ResourceLocation.parse("changed_addon", "spray.sound")));
		REGISTRY.put(ResourceLocation.parse("changed_addon", "experiment10_theme"), new SoundEvent(ResourceLocation.parse("changed_addon", "experiment10_theme")));
		REGISTRY.put(ResourceLocation.parse("changed_addon", "block.plushes.sfx"), new SoundEvent(ResourceLocation.parse("changed_addon", "block.plushes.sfx")));
		REGISTRY.put(ResourceLocation.parse("changed_addon", "hammer_swing"), new SoundEvent(ResourceLocation.parse("changed_addon", "hammer_swing")));
		REGISTRY.put(ResourceLocation.parse("changed_addon", "hammer_gun_shot"), new SoundEvent(ResourceLocation.parse("changed_addon", "hammer_gun_shot")));
		REGISTRY.put(ResourceLocation.parse("changed_addon", "music.boss.luminarctic_leopard"), new SoundEvent(ResourceLocation.parse("changed_addon", "music.boss.luminarctic_leopard")));
		REGISTRY.put(ResourceLocation.parse("changed_addon", "music.boss.exp9"), new SoundEvent(ResourceLocation.parse("changed_addon", "music.boss.exp9")));
	}

	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
		for (Map.Entry<ResourceLocation, SoundEvent> sound : REGISTRY.entrySet())
			event.getRegistry().register(sound.getValue().setRegistryName(sound.getKey()));
	}
}
