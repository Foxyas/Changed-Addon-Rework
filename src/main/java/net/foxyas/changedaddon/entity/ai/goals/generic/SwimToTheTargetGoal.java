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
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (target == null) return;
        this.mob.getNavigation().moveTo(target, speedModifier);
    }
}