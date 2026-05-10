package net.foxyas.changedaddon.item.api;

import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.Nullable;

public interface IDrinkItem {
    @Nullable
    default SoundEvent getDrankSound() {
        return null;
    }
}
