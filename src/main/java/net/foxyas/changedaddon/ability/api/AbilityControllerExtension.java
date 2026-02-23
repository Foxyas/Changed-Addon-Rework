package net.foxyas.changedaddon.ability.api;

import net.ltxprogrammer.changed.ability.AbstractAbility;

public interface AbilityControllerExtension {

    static AbilityControllerExtension of(AbstractAbility.Controller controller) {
        return (AbilityControllerExtension) controller;
    }

    void resetCooldown();

    default boolean shouldApplyCooldown() {
        return true;
    }
}
