package net.foxyas.changedaddon.entity.CustomHandle;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;

public interface BossWithMusic {
    boolean ShouldPlayMusic();

    default SoundEvent Music(){
        return null;
    }
}
