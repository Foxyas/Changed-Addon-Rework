package net.foxyas.changedaddon.entity.CustomHandle;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public interface IHasBossMusic {
    ResourceLocation getBossMusic();

    default int getMusicRange() {
        return 64; // distância padrão
    }

    LivingEntity getSelf();
}
