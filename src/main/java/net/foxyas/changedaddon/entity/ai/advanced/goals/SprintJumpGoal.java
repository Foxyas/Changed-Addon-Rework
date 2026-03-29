package net.foxyas.changedaddon.entity.ai.advanced.goals;

import net.foxyas.changedaddon.mixins.entity.LivingEntityAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;

public class SprintJumpGoal extends Goal {
    private final Mob mob;
    private LivingEntity target;
    private final double sprintDistanceThreshold = 5.0; // Distância para começar a correr

    public SprintJumpGoal(Mob mob) {
        this.mob = mob;
        // Usamos MOVE e JUMP para não conflitar com outras IAs de movimento
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        this.target = this.mob.getTarget();
        if (this.target == null || !this.target.isAlive()) {
            return false;
        }
        Path path = this.mob.getNavigation().getPath();
        if (path != null && !path.canReach()) {
            return false;
        }

        double distance = this.mob.distanceToSqr(this.target);

        // Ativa se o alvo estiver longe OU se o alvo estiver correndo e pulando
        boolean targetIsSprinting = this.target.isSprinting();
        boolean targetIsJumping = this.target instanceof LivingEntityAccessor accessor ? accessor.isJumping() : !this.target.onGround(); // Aproximação para pulo

        return distance > (sprintDistanceThreshold * sprintDistanceThreshold)
                || (targetIsSprinting && targetIsJumping);
    }

    @Override
    public void start() {
        this.mob.setSprinting(true);
    }

    @Override
    public void stop() {
        this.mob.setSprinting(false);
    }

    @Override
    public void tick() {
        if (this.target == null) return;

        // Mantém o Sprint ativo
        this.mob.setSprinting(true);

        // Lógica de Pulo: Pula se estiver no chão e se movendo em direção ao alvo
        if (this.mob.onGround() && this.mob.getNavigation().isInProgress()) {
            // Pequena variação aleatória para o pulo não parecer robótico
            if (this.mob.getRandom().nextFloat() < 0.1F) {
                this.mob.getJumpControl().jump();
            }
        }

        // Se chegar muito perto, para de dar sprint jump para atacar normalmente
        if (this.mob.distanceToSqr(this.target) < 4.0D) {
            this.mob.setSprinting(false);
        }
    }
}