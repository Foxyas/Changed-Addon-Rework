package net.foxyas.changedaddon.entity.bosses;

import net.foxyas.changedaddon.entity.ai.goals.abilities.MayCauseGrabDamageGoal;
import net.foxyas.changedaddon.entity.ai.goals.abilities.MayDropGrabbedEntityGoal;
import net.foxyas.changedaddon.entity.ai.goals.abilities.MayGrabTargetGoal;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity.IOverrideAlphaGoals;
import net.foxyas.changedaddon.entity.api.IGrabberEntity;
import net.foxyas.changedaddon.entity.api.IGrabberEntity.IHasGrabberGoals;
import net.foxyas.changedaddon.entity.api.IGrabberEntity.IOverrideGrabberStats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public interface IExp10Exp9Overrides extends IOverrideAlphaGoals, IHasGrabberGoals, IOverrideGrabberStats {

    @Override
    default int getMaxGrabCooldown(int defaultValue) {
        return defaultValue * 2;
    }

    @Override
    default void makeAlphaGoals() {
    }

    @Override
    default void mayRemoveAlphaGoals() {
        IOverrideAlphaGoals.super.mayRemoveAlphaGoals();
    }

    @Override
    default void makeGrabberGoals() {
        if (!(this instanceof Mob mob)) return;
        if (!(this instanceof IGrabberEntity grabber)) return;
        mob.goalSelector.addGoal(0, new MayDropGrabbedEntityGoal(grabber));
        mob.goalSelector.addGoal(0, new MayCauseGrabDamageGoal(grabber));

        // Todo: May Remove This in 0.16.0
        mob.goalSelector.addGoal(20, new MayGrabTargetGoal(grabber) {
            @Override
            public boolean canEntityGrabRightNow(LivingEntity living) {
                if (living instanceof Experiment009BossEntity experiment009Boss) {
                    if (experiment009Boss.isCastingAttack()) {
                        return false;
                    }
                }

                return super.canEntityGrabRightNow(living);
            }
        });
    }
}
