package net.foxyas.changedaddon.entity.ai.goals.generic;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

import java.util.EnumSet;

public class SwimToTheTargetGoal extends Goal {
    protected final PathfinderMob mob;
    protected final float speedModifier;

    public SwimToTheTargetGoal(PathfinderMob mob, float speedModifier) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        // Importante: MOVE e LOOK são essenciais para navegação fluida
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.mob.getTarget();
        // Só ativa se tiver alvo e estiver na água (ou se o alvo estiver na água)
        return target != null && target.isAlive() && (target.isSwimming() || target.distanceToSqr(mob) >= 6) && this.mob.isInWater();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void start() {
        this.mob.setPose(Pose.SWIMMING);
        this.mob.setSwimming(true); // Ativa a animação de nado
        this.mob.getNavigation().stop();
    }

    @Override
    public void stop() {
        this.mob.setPose(Pose.STANDING);
        this.mob.setSwimming(false);
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (target == null) return;
        this.mob.getNavigation().stop();

        if ((target.isSwimming() || target.distanceToSqr(mob) >= 6)) {
            this.mob.setPose(Pose.SWIMMING);
            this.mob.setSwimming(true); // Ativa a animação de nado
        } else if (mob.isUnderWater()) {
            this.mob.setPose(Pose.SWIMMING);
            this.mob.setSwimming(true); // Ativa a animação de nado
        } else {
            this.mob.setPose(Pose.STANDING);
            this.mob.setSwimming(true); // Ativa a animação de nado
        }

        // Olha para o alvo enquanto nada
        this.mob.getLookControl().setLookAt(target, 90f, 90f);
        this.mob.yBodyRotO = this.mob.getYHeadRot();

        Vec3 movementDir = target
                .position()
                .subtract(mob.position())
                .normalize();

        float appliedSpeed = mob.isEyeInFluid(FluidTags.WATER) ? speedModifier : speedModifier * 0.25F;

        float swimSpeed = (float) (mob.getMoveControl().getSpeedModifier() * mob.getAttributeValue(ForgeMod.SWIM_SPEED.get()));
        mob.setSpeed(swimSpeed);
        mob.setDeltaMovement(movementDir.scale(appliedSpeed));
    }
}