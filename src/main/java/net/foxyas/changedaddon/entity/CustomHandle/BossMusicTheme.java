package net.foxyas.changedaddon.entity.CustomHandle;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.IExtensibleEnum;
import net.minecraftforge.registries.ForgeRegistries;

public enum BossMusicTheme{
        EXP9_PHASE1("exp9_phase2", ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ChangedAddonMod.MODID, "experiment009_theme"))),
        EXP9_PHASE2("exp9_phase2", ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ChangedAddonMod.MODID, "experiment009_theme_phase2"))),
        EXP10("exp10", ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ChangedAddonMod.MODID, "experiment10_theme")));

        private final SoundEvent soundEvent;
        private final String ID;

        BossMusicTheme(String name, SoundEvent soundEvent) {
            this.soundEvent = soundEvent;
            this.ID = name;
        }

        public String getIDName() {
            return ID;
        }

        public SoundEvent getAsSoundEvent() {
            return soundEvent;
        }

        public Music getAsMusic() {
            return new Music(this.soundEvent, 1, 1, true);
        }
}