package net.foxyas.changedaddon.entity.bosses;

import net.foxyas.changedaddon.compatibility.create.IDynamicBeltMovementEntity;
import net.foxyas.changedaddon.entity.api.*;
import net.ltxprogrammer.changed.entity.GenderedEntity;
import net.ltxprogrammer.changed.entity.PowderSnowWalkable;

public interface IExp10Logic extends ISpeakableEntity, GenderedEntity, ICustomPatReaction, PowderSnowWalkable, IHasBossMusic, ICrawlAndSwimAbleEntity, IGrabberEntity.IConditionalGrabber, IAlphaAbleEntity.CustomAlphaAttributes, IDynamicBeltMovementEntity {

    @Override
    default boolean canBeTransportedByBelts(boolean defaultValue) {
        return false;
    }
}
