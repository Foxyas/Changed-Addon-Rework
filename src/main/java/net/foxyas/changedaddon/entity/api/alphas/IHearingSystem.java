package net.foxyas.changedaddon.entity.api.alphas;

import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface IHearingSystem {

    default @Nullable DynamicGameEventListener<?> getGameEventListener() {
        return null;
    }

    default boolean heardSomethingRecently() {
        return getHeardCooldown() > 0;
    }

    int getHeardCooldown();

    void setHeardCooldown(int value);
}