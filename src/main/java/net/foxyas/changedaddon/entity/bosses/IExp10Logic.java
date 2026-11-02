package net.foxyas.changedaddon.entity.bosses;

import net.foxyas.changedaddon.ability.api.GrabEntityAbilityExtensor;
import net.foxyas.changedaddon.compatibility.create.IDynamicBeltMovementEntity;
import net.foxyas.changedaddon.entity.api.*;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.entity.GenderedEntity;
import net.ltxprogrammer.changed.entity.PowderSnowWalkable;

public interface IExp10Logic extends IExp10Exp9Overrides, ISpeakableEntity, GenderedEntity, ICustomPatReaction, PowderSnowWalkable, IHasBossMusic, ICrawlAndSwimAbleEntity, IGrabberEntity.IConditionalGrabber, IAlphaAbleEntity.IOverrideAlphaAttributes, IDynamicBeltMovementEntity {

    @Override
    default boolean canBeTransportedByBelts(boolean defaultValue) {
        return false;
    }

    @Override
    default void onGrabAbilityCreated(GrabEntityAbilityInstance instance) {
        if (instance instanceof GrabEntityAbilityExtensor extensor) {
            extensor.setTransfurDamageMode(false);
        }
    }
}
