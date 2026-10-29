package net.foxyas.changedaddon.entity.api;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;

public interface ICustomPatReaction {
    default boolean onPat() {
        return true;
    }

    default boolean onPattedBy(LivingEntity patter, InteractionHand hand) {
        return true;
    }

    default boolean onPattedByIn(LivingEntity patter, InteractionHand hand, EntityHitResult pattedLocation) {
        return true;
    }

    default boolean onPatEntity(LivingEntity patter, InteractionHand hand, LivingEntity patTarget) {
        return true;
    }

    default boolean onPatEntityIn(LivingEntity patter, InteractionHand hand, LivingEntity patTarget, EntityHitResult patResult) {
        return true;
    }
}
