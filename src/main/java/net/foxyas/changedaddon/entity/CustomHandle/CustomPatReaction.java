package net.foxyas.changedaddon.entity.CustomHandle;

import net.minecraft.world.entity.player.Player;

public interface CustomPatReaction {
    default void WhenPattedReaction() {
    }

    default void WhenPattedReaction(Player target) {
    }
}
