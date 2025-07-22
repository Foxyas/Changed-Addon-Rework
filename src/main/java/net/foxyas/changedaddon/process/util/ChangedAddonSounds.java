package net.foxyas.changedaddon.process.util;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class ChangedAddonSounds {

    public static final SoundEvent EXP10_THEME = ForgeRegistries.SOUND_EVENTS.getValue(ChangedAddonMod.resourceLoc("experiment10_theme"));
    public static final SoundEvent EXP9_THEME = ForgeRegistries.SOUND_EVENTS.getValue(ChangedAddonMod.resourceLoc("music.boss.exp9"));
    public static final SoundEvent HAMMER_SWING = ForgeRegistries.SOUND_EVENTS.getValue(ChangedAddonMod.resourceLoc("hammer_swing"));
    public static final SoundEvent HAMMER_GUN_SHOT = ForgeRegistries.SOUND_EVENTS.getValue(ChangedAddonMod.resourceLoc("hammer_gun_shot"));
}
