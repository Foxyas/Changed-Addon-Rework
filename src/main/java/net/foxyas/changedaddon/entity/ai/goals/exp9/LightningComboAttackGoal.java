package net.foxyas.changedaddon.entity.ai.goals.exp9;

import net.foxyas.changedaddon.entity.bosses.Experiment009BossEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class LightningComboAttackGoal extends Goal {

    protected final PathfinderMob holder;
    protected final RandomSource random;
    protected final IntProvider cooldownProvider;
    protected final IntProvider attackCountProvider;
    protected final IntProvider castDurationProvider;
    protected final FloatProvider damageProvider;
    protected DamageSource source;

    protected LivingEntity target;
    protected int cooldown;
    protected int attacks;
    protected Vec3 attackPos;
    protected int aboveWaterY;
    protected int castDuration;
    protected int wasBlocked;

    public LightningComboAttackGoal(PathfinderMob holder, IntProvider cooldown, IntProvider attackCount, IntProvider castDuration, FloatProvider damage) {
        this.holder = holder;
        random = holder.getRandom();
        cooldownProvider = cooldown;
        attackCountProvider = attackCount;
        castDurationProvider = castDuration;
        damageProvider = damage;


        adjustDamageSource(holder);

        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    private DamageSource adjustDamageSource(PathfinderMob holder) {
        Holder<DamageType> damageTypeHolder = holder.level().damageSources().lightningBolt().typeHolder();
        source = new DamageSource(damageTypeHolder, holder, holder, attackPos);
        return source;
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }

        target = holder.getTarget();

        return target != null && target.isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        if (target instanceof Player player) {
            if (player.isCreative() || player.isSpectator()) {
                return false;
            }
        }

        return target.isAlive() && attacks > 0;
    }

    @Override
    public void start() {
        attacks = attackCountProvider.sample(random);
        castDuration = castDurationProvider.sample(random);
        pickAttackPos();

        holder.getNavigation().stop();
        holder.getLookControl().setLookAt(target);
    }

    protected void pickAttackPos() {
        attackPos = target.position();
        Level level = holder.level;
        BlockPos pos = new BlockPos((int) attackPos.x, (int) attackPos.y, (int) attackPos.z);
        if (level.getBlockState(pos).is(Blocks.WATER)) {
            do pos = pos.above();
            while (level.getBlockState(pos).is(Blocks.WATER));
            aboveWaterY = pos.getY();
        } else aboveWaterY = Integer.MAX_VALUE;
    }

    @Override
    public void tick() {
        if (attacks <= 0) return;

        Level level = holder.level;

        if (target != null) {
            holder.getLookControl().setLookAt(target, 30, 30);
        }

        if (wasBlocked > 0) {
            wasBlocked--;

            ((ServerLevel) level).sendParticles(ParticleTypes.ELECTRIC_SPARK, holder.getX() - 1.5, holder.getY() - 1.5 + holder.getBbHeight() / 2, holder.getZ() - 1.5,
                    50 * wasBlocked / 30, 3, 3, 3, 0.5);

            if (wasBlocked == 0) pickAttackPos();
            return;
        }

        if (castDuration > 0) {
            castDuration--;

            if (holder instanceof Experiment009BossEntity exp9) {
                exp9.setCastingAttack(castDuration > 0);
            }

            if (holder.tickCount % 2 == 0) {
                if (aboveWaterY != Integer.MAX_VALUE)
                    ((ServerLevel) level).sendParticles(ParticleTypes.ELECTRIC_SPARK, attackPos.x - 1, aboveWaterY, attackPos.z - 1,
                            50, 2, 0.2, 2, 0.5);

                ((ServerLevel) level).sendParticles(ParticleTypes.ELECTRIC_SPARK, attackPos.x - 1, attackPos.y, attackPos.z - 1,
                        50, 2, 0.2, 2, 0.5);
            }

            return;
        }

        attacks--;

        holder.teleportTo(attackPos.x, attackPos.y, attackPos.z);
        holder.swing(InteractionHand.MAIN_HAND);

        SummonLightningGoal.lightning(holder.level, attackPos.x, attackPos.y, attackPos.z, 1);
        if (attacks == 0) {
            SummonLightningGoal.lightning(level, attackPos.x + 0.75, attackPos.y, attackPos.z + 0.75, 0);
            SummonLightningGoal.lightning(level, attackPos.x + 0.75, attackPos.y, attackPos.z - 0.75, 0);
            SummonLightningGoal.lightning(level, attackPos.x - 0.75, attackPos.y, attackPos.z - 0.75, 0);
            SummonLightningGoal.lightning(level, attackPos.x - 0.75, attackPos.y, attackPos.z + 0.75, 0);
            applyKnockbackAndHurt(6, 2, 3);
        } else {
            castDuration = castDurationProvider.sample(random);
            if (applyKnockbackAndHurt(4, 1, 1)) {
                wasBlocked = 60;
            } else pickAttackPos();
        }
    }

    protected boolean applyKnockbackAndHurt(float radius, float damageMul, float knockbackMul) {
        Level level = holder.level;
        float diameter = radius * 2;
        float radiusSqr = radius * radius;
        var list = level.getNearbyEntities(
                LivingEntity.class,
                TargetingConditions.forCombat().selector(target -> !target.is(holder)),
                holder, AABB.ofSize(attackPos, diameter, diameter, diameter)
        );

        boolean anyBlocked = false, blocked;
        float dist, knockback;
        Vec3 direction;
        for (LivingEntity livingEntity : list) {
            dist = (float) livingEntity.distanceToSqr(attackPos);
            if (dist > radiusSqr) continue;

            dist = Mth.sqrt(dist);
            blocked = livingEntity.isDamageSourceBlocked(adjustDamageSource(holder));

            livingEntity.hurt(adjustDamageSource(holder), damageProvider.sample(random) * damageMul);//hurt anyway to damage shield

            direction = livingEntity.position().subtract(attackPos).normalize();
            knockback = radius / dist * knockbackMul;
            if (blocked) {
                knockback *= 0.25f;
                anyBlocked = true;
            }

            livingEntity.push(
                    direction.x * knockback,
                    direction.y * knockback,
                    direction.z * knockback
            );
        }

        if (anyBlocked) {
            level.playSound(null, holder, SoundEvents.TOTEM_USE, SoundSource.MASTER, 10, 1);

            knockback = knockbackMul * 0.5f;
            direction = holder.getLookAngle().reverse();
            holder.push(
                    direction.x * knockback,
                    direction.y * knockback,
                    direction.z * knockback
            );
        }

        return anyBlocked;
    }

    @Override
    public void stop() {
        target = null;
        cooldown = cooldownProvider.sample(random);
        attacks = 0;
        attackPos = null;
        castDuration = 0;
        wasBlocked = 0;
        if (holder instanceof Experiment009BossEntity exp9) {
            exp9.setCastingAttack(false);
        }
    }
}
