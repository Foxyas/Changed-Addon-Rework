package net.foxyas.changedaddon.entity.bosses;

import net.foxyas.changedaddon.compatibility.create.IDynamicBeltMovementEntity;
import net.foxyas.changedaddon.entity.api.*;
import net.foxyas.changedaddon.entity.bosses.Experiment009BossEntity.Exp9Phase;
import net.ltxprogrammer.changed.entity.GenderedEntity;
import net.ltxprogrammer.changed.entity.PowderSnowWalkable;

public interface IExp9Logic extends IHasPhases<Exp9Phase>, GenderedEntity, CustomPatReaction, PowderSnowWalkable, IHasBossMusic, ICrawlAndSwimAbleEntity, IGrabberEntity.IConditionalGrabber, IAlphaAbleEntity.CustomAlphaAttributes, IDynamicBeltMovementEntity {

    @Override
    default boolean canBeTransportedByBelts(boolean defaultValue) {
        return false;
    }
}
