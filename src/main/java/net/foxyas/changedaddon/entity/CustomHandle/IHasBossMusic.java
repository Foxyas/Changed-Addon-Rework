package net.foxyas.changedaddon.entity.CustomHandle;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public interface IHasBossMusic {
    @Nullable ResourceLocation getBossMusic();

    default int getMusicRange() {
        return 64; // distância padrão
    }

    LivingEntity getSelf();
}
