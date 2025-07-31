package net.foxyas.changedaddon.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonSounds {
    public static Map<ResourceLocation, SoundEvent> REGISTRY = new HashMap<>();

    static {
        REGISTRY.put(new ResourceLocation("changed_addon", "armor_equip"), new SoundEvent(new ResourceLocation("changed_addon", "armor_equip")));
        REGISTRY.put(new ResourceLocation("changed_addon", "warn"), new SoundEvent(new ResourceLocation("changed_addon", "warn")));
        REGISTRY.put(new ResourceLocation("changed_addon", "untransfursound"), new SoundEvent(new ResourceLocation("changed_addon", "untransfursound")));
        REGISTRY.put(new ResourceLocation("changed_addon", "spray.sound"), new SoundEvent(new ResourceLocation("changed_addon", "spray.sound")));
        REGISTRY.put(new ResourceLocation("changed_addon", "experiment10_theme"), new SoundEvent(new ResourceLocation("changed_addon", "experiment10_theme")));
        REGISTRY.put(new ResourceLocation("changed_addon", "block.plushes.sfx"), new SoundEvent(new ResourceLocation("changed_addon", "block.plushes.sfx")));
        REGISTRY.put(new ResourceLocation("changed_addon", "hammer_swing"), new SoundEvent(new ResourceLocation("changed_addon", "hammer_swing")));
        REGISTRY.put(new ResourceLocation("changed_addon", "hammer_gun_shot"), new SoundEvent(new ResourceLocation("changed_addon", "hammer_gun_shot")));
        REGISTRY.put(new ResourceLocation("changed_addon", "music.boss.luminarctic_leopard"), new SoundEvent(new ResourceLocation("changed_addon", "music.boss.luminarctic_leopard")));
        REGISTRY.put(new ResourceLocation("changed_addon", "music.boss.exp9"), new SoundEvent(new ResourceLocation("changed_addon", "music.boss.exp9")));
        REGISTRY.put(new ResourceLocation("changed_addon", "gecko_sound"), new SoundEvent(new ResourceLocation("changed_addon", "gecko_sound")));
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        for (Map.Entry<ResourceLocation, SoundEvent> sound : REGISTRY.entrySet())
            event.getRegistry().register(sound.getValue().setRegistryName(sound.getKey()));
    }
}
