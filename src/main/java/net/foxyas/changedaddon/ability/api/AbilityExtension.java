package net.foxyas.changedaddon.ability.api;

import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;

public interface AbilityExtension {

    static AbilityExtension of(AbstractAbilityInstance abilityInstance) {
        return (AbilityExtension) abilityInstance;
    }

    default boolean shouldApplyCooldown() {
        return true;
    }
}
