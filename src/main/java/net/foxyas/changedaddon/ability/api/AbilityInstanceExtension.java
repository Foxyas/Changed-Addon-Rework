package net.foxyas.changedaddon.ability.api;

import net.minecraft.world.InteractionHand;

public interface AbilityInstanceExtension {

    default void onEntityJump() {

    }

    default void onEntitySwing(InteractionHand hand) {

    }
}
