package net.foxyas.changedaddon.entity.ai.goals;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jetbrains.annotations.NotNull;

public interface IReactiveGoal {

    interface ICancelOnDamageGoal extends IReactiveGoal {
        @Override
        default void onHurt(LivingEntity livingEntity, @NotNull DamageSource pDamageSource, float pDamageAmount) {
            this.setCanceledTo(true);
        };
    }

    void onHurt(LivingEntity livingEntity, @NotNull DamageSource pDamageSource, float pDamageAmount);
    void onDamage(LivingEntity livingEntity, @NotNull DamageSource pDamageSource, float amount, boolean willCauseDamage);
    void onHeal(LivingEntity livingEntity, float amount);

    boolean isCanceled();
    void setCanceledTo(boolean canceled);

    default void setCanceled() {
        this.setCanceledTo(true);
    }

    default void forceCancelGoal() {
        this.setCanceledTo(true);
        if (this instanceof Goal goal) {
            goal.stop();
        }
    }
}
