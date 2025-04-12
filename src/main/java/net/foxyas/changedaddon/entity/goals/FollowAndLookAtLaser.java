package net.foxyas.changedaddon.entity.goals;

import net.foxyas.changedaddon.item.LaserPointer;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class FollowAndLookAtLaser extends Goal {
    private final Mob mob;
    private final double speedModifier;
    private LivingEntity laserPlayer;
    @Nullable
    private Vec3 laserTarget;

    public FollowAndLookAtLaser(Mob mob, double speedModifier) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public void setLaserTarget(Vec3 laserTarget, LivingEntity laserPlayer) {
        this.laserPlayer = laserPlayer;
        this.laserTarget = laserTarget;
    }

    public void clearLaserTarget() {
        this.laserTarget = null;
        this.laserPlayer = null;
    }

    private boolean isPlayerUsingLaser() {
        if (laserPlayer == null) return false;
        ItemStack using = laserPlayer.getUseItem();
        return using.getItem() instanceof LaserPointer && laserPlayer.isUsingItem();
    }

    @Override
    public boolean canUse() {
        return laserTarget != null
                && isPlayerUsingLaser()
                && hasLineOfSight(laserTarget)
                && Math.sqrt(mob.distanceToSqr(laserTarget)) < 100;
    }

    @Override
    public boolean canContinueToUse() {
        return laserTarget != null
                && isPlayerUsingLaser()
                && hasLineOfSight(laserTarget)
                && Math.sqrt(mob.distanceToSqr(laserTarget)) < 100;
    }

    private boolean hasLineOfSight(Vec3 target) {
        return mob.level.clip(new ClipContext(
                mob.getEyePosition(), target,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                mob
        )).getType() == HitResult.Type.MISS;
    }

    @Override
    public void start() {
        super.start();
        this.mob.playSound(SoundEvents.CAT_AMBIENT, 1, 1);
    }

    @Override
    public void tick() {
        if (laserTarget == null || !isPlayerUsingLaser()) return;
        if (mob.getTarget() != null || (mob instanceof ChangedEntity ce && ce.getTarget() != null)) return;
        if (!mob.hasLineOfSight(laserPlayer)) return;

        this.mob.getLookControl().setLookAt(laserTarget.x, laserTarget.y, laserTarget.z);

        if (this.mob.distanceToSqr(laserTarget) > 2) {
            this.mob.getNavigation().moveTo(laserTarget.x, laserTarget.y, laserTarget.z, speedModifier);
        } else {
            this.mob.getNavigation().stop();
        }
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
        clearLaserTarget();
    }
}
