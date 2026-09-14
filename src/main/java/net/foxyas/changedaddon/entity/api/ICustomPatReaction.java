package net.foxyas.changedaddon.entity.api;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public interface ICustomPatReaction {
    default boolean whenPattedReactionSimple() {
        return true;
    }

    default boolean whenPattedReaction(LivingEntity patter, InteractionHand hand) {
        return true;
    }

    default boolean whenPattedReactionSpecific(LivingEntity patter, InteractionHand hand, Vec3 pattedLocation) {
        return true;
    }

    default boolean whenPatEvent(LivingEntity patter, InteractionHand hand, LivingEntity patTarget) {
        return true;
    }

    default boolean whenPatEventSpecific(LivingEntity patter, InteractionHand hand, LivingEntity patTarget, EntityHitResult patResult) {
        return true;
    }
}
