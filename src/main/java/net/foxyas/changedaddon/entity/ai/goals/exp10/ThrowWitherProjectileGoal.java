package net.foxyas.changedaddon.entity.ai.goals.exp10;

import net.foxyas.changedaddon.entity.projectile.WitherParticleProjectile;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ThrowWitherProjectileGoal extends Goal {

    public final Mob holder;
    public final float distance;
    protected final IntProvider cooldownProvider;
    protected final IntProvider maxUseTimesProvider;
    public int cooldown;
    public int maxUseTimes;
    public int usedTimes;
    public int tick;

    public ThrowWitherProjectileGoal(Mob holder, IntProvider cooldownProvider, IntProvider maxUseTimesProvider) {
        super();
        this.holder = holder;
        this.cooldownProvider = cooldownProvider;
        this.maxUseTimesProvider = maxUseTimesProvider;
        this.distance = 25f;
    }

    public ThrowWitherProjectileGoal(Mob holder, IntProvider cooldownProvider, IntProvider maxUseTimesProvider, float distance) {
        super();
        this.holder = holder;
        this.cooldownProvider = cooldownProvider;
        this.maxUseTimesProvider = maxUseTimesProvider;
        this.distance = distance;
    }

    @Override
    public boolean canUse() {
        if (holder.getTarget() == null) {
            return false;
        }
        if (this.cooldown > 0) {
            this.cooldown--;
            return false;
        }

        return (holder.distanceToSqr(holder.getTarget()) >= distance) || holder.getRandom().nextFloat() >= 0.90f;
    }

    @Override
    public void start() {
        super.start();
        maxUseTimes = maxUseTimesProvider.sample(this.holder.getRandom());
    }

    @Override
    public void tick() {
        super.tick();
        tick++;
        LivingEntity target = holder.getTarget();
        if (target == null) return;
        Level mobLevel = holder.level();
        if (usedTimes >= maxUseTimes) return;
        if (mobLevel instanceof ServerLevel level) {
            WitherParticleProjectile witherParticleProjectile = getWitherParticleProjectile(level, target);
            if (tick % 10 == 0) {
                holder.swing(InteractionHand.MAIN_HAND);
                level.addFreshEntity(witherParticleProjectile);
                level.playSound(null, holder, SoundEvents.BLAZE_SHOOT, SoundSource.HOSTILE, 1, 1);
                usedTimes++;
            }
        }
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    private @NotNull WitherParticleProjectile getWitherParticleProjectile(ServerLevel level, LivingEntity target) {
        holder.getLookControl().setLookAt(target, 180f, 180f);
        Vec3 motion = holder.getViewVector(1);
        WitherParticleProjectile witherParticleProjectile = new WitherParticleProjectile(ChangedAddonEntities.WITHER_PARTICLE_PROJECTILE.get(), level);
        witherParticleProjectile.setPos(holder.getEyePosition().add(motion));
        witherParticleProjectile.shoot(motion.x, motion.y, motion.z, 2.25f, 0);
        witherParticleProjectile.setOwner(holder);
        witherParticleProjectile.setCritArrow(holder.getRandom().nextBoolean());
        witherParticleProjectile.setKnockback(2);
        witherParticleProjectile.setBaseDamage(5f);
        return witherParticleProjectile;
    }

    @Override
    public void stop() {
        super.stop();
        cooldown = cooldownProvider.sample(this.holder.getRandom());
        usedTimes = 0;
    }

    @Override
    public boolean canContinueToUse() {
        if (holder.getTarget() == null) {
            return false;
        }
        boolean isDistance = (holder.distanceToSqr(holder.getTarget()) < distance);
        if (isDistance || usedTimes >= maxUseTimes) {
            return false;
        }

        return cooldown <= 0;
    }
}
