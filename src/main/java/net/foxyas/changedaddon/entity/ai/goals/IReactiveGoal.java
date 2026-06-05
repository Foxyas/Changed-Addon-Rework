package net.foxyas.changedaddon.entity.ai.goals;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jetbrains.annotations.NotNull;

public interface IReactiveGoal {

    interface ICancelOnDamageGoal extends IReactiveGoal {
        @Override
        default void onHurt(LivingEntity livingEntity, @NotNull DamageSource pDamageSource, float pDamageAmount) {

        };
    }

    void onHurt(LivingEntity livingEntity, @NotNull DamageSource pDamageSource, float pDamageAmount);
    void onDamage(LivingEntity livingEntity, @NotNull DamageSource pDamageSource, float amount);
    void onHeal(LivingEntity livingEntity, float amount);

    boolean isCanceled();
    void setCanceled(boolean canceled);

    default void forceCancelGoal() {
        this.setCanceled(true);
        if (this instanceof Goal goal) {
            goal.stop();
        }
    }
}
