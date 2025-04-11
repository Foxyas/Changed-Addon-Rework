package net.foxyas.changedaddon.entity.goals;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class FollowAndLookAtLaser extends Goal {
    private final Mob mob;
    private final double speedModifier;
    @Nullable
    private Vec3 laserTarget;

    public FollowAndLookAtLaser(Mob mob, double speedModifier) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public void setLaserTarget(Vec3 laserTarget) {
        this.laserTarget = laserTarget;
    }

    public void clearLaserTarget() {
        this.laserTarget = null;
    }

    @Override
    public boolean canUse() {
        return laserTarget != null && Math.sqrt(this.mob.distanceToSqr(laserTarget)) < 100; // 100 blocos de alcance
    }

    @Override
    public boolean canContinueToUse() {
        return laserTarget != null && Math.sqrt(this.mob.distanceToSqr(laserTarget)) < 100; // 100 blocos de alcance
    }

    @Override
    public void tick() {
        if (laserTarget == null || mob.getTarget() != null || (mob instanceof ChangedEntity changedEntity && changedEntity.getTarget() != null)) return;

        if (this.mob.distanceToSqr(laserTarget) <= 2) {
            this.mob.getLookControl().setLookAt(laserTarget.x, laserTarget.y, laserTarget.z);
        } else {
            this.mob.getLookControl().setLookAt(laserTarget.x, laserTarget.y, laserTarget.z);
            this.mob.getNavigation().moveTo(laserTarget.x, laserTarget.y, laserTarget.z, speedModifier);
        }
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
    }
}
