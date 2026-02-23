package net.foxyas.changedaddon.util;

import net.minecraft.world.entity.AnimationState;

public class AnimationUtils {

    /**
     * Retorna true se a animação foi parada porque tempo excedeu.
     *
     * @param state   AnimationState da entidade
     * @param seconds Tempo máximo permitido
     */
    public static boolean stopAfterSeconds(AnimationState state, float seconds) {
        if (!state.isStarted()) return false;

        long maxTimeMs = (long) (seconds * 1000L);

        if (state.getAccumulatedTime() >= maxTimeMs) {
            state.stop();
            return true;
        }
        return false;
    }
}
