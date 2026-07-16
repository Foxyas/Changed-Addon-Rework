package net.foxyas.changedaddon.entity.api;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public interface ICustomPatReaction {
    default void whenPattedReactionSimple() {
    }

    default void whenPattedReaction(LivingEntity patter, InteractionHand hand) {
    }

    default void whenPattedReactionSpecific(LivingEntity patter, InteractionHand hand, Vec3 pattedLocation) {
    }

    default void whenPatEvent(LivingEntity patter, InteractionHand hand, LivingEntity patTarget) {

    }

    default void whenPatEventSpecific(LivingEntity patter, InteractionHand hand, LivingEntity patTarget, EntityHitResult patResult) {

    }
}
