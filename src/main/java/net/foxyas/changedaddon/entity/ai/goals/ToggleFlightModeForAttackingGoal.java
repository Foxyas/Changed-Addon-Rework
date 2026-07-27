package net.foxyas.changedaddon.entity.ai.goals;

import net.foxyas.changedaddon.entity.api.IFlyableChangedEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class ToggleFlightModeForAttackingGoal<T extends PathfinderMob & IFlyableChangedEntity> extends Goal {
    private final T entity;
    private final double maxGroundDistanceSqr; // Distância limite para preferir voar (ex: 10 blocos = 100.0)
    private final double maxGroundYDiff;       // Diferença de Y limite para preferir voar (ex: 3.0 blocos)

    public ToggleFlightModeForAttackingGoal(T entity, double maxGroundDistance, double maxGroundYDiff) {
        this.entity = entity;
        this.maxGroundDistanceSqr = maxGroundDistance * maxGroundDistance;
        this.maxGroundYDiff = maxGroundYDiff;
        this.setFlags(EnumSet.noneOf(Goal.Flag.class));
    }

    public ToggleFlightModeForAttackingGoal(T entity) {
        this(entity, 10.0D, 3.0D);
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.entity.getTarget();
        if (target == null || !target.isAlive()) {
            return false;
        }

        double distanceSqr = this.entity.distanceToSqr(target);
        double yDiff = Math.abs(target.getY() - this.entity.getY());

        // Se o alvo estiver longe OU muito acima/abaixo, deve MUDAR para modo voo
        if (!this.entity.isFlyingMode()) {
            return distanceSqr > this.maxGroundDistanceSqr || yDiff > this.maxGroundYDiff;
        }

        // Se estiver voando, só DESATIVA o modo voo se estiver no chão e no alcance do ataque Melee
        if (this.entity.isFlyingMode()) {
            boolean isWithinMeleeRange = distanceSqr <= this.entity.getMeleeAttackRangeSqr(target);
            return this.entity.onGround() && isWithinMeleeRange;
        }

        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return false; // Executa instantaneamente no start()
    }

    @Override
    public void start() {
        LivingEntity target = this.entity.getTarget();
        if (target == null || !target.isAlive()) return;

        if (!this.entity.isFlyingMode()) {
            // Decola se o alvo está longe ou alto
            this.entity.setFlyingMode(true);
        } else if (this.entity.onGround()) {
            // Pousa se atingiu o chão e está em alcance
            this.entity.setFlyingMode(false);
        }
    }
}