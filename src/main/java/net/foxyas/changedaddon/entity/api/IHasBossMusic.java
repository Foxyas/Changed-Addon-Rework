package net.foxyas.changedaddon.entity.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public interface IHasBossMusic {
    @Nullable ResourceLocation getBossMusic();

    default int getMusicRange() {
        return 64; // distância padrão
    }

    default float getMusicVolume() {
        return 1;
    }

    default float getMusicPitch() {
        return 1;
    }

    LivingEntity getSelf();
}
