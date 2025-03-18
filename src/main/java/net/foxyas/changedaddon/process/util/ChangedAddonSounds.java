package net.foxyas.changedaddon.process.util;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class ChangedAddonSounds {

    public static final SoundEvent EXP10_THEME = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ChangedAddonMod.MODID, "experiment10_theme"));
    public static final SoundEvent EXP9_THEME = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ChangedAddonMod.MODID, "music.boss.exp9"));
    public static final SoundEvent HAMMER_SWING = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ChangedAddonMod.MODID, "hammer_swing"));
    public static final SoundEvent HAMMER_GUN_SHOT = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ChangedAddonMod.MODID, "hammer_gun_shot"));
}
