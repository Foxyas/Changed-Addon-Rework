package net.foxyas.changedaddon.entity.ai.goals.exp10;

import net.foxyas.changedaddon.client.particle.EntityModelFadeParticleOptions;
import net.foxyas.changedaddon.entity.ai.goals.IAbilityGoal;
import net.foxyas.changedaddon.init.ChangedAddonParticleTypes;
import net.foxyas.changedaddon.util.ParticlesUtil;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.minecraft.core.particles.DustParticleOptions;
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
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.awt.*;
import java.util.EnumSet;

public class ClawsComboAttackGoal extends Goal implements IAbilityGoal {

    public static final Color FADE_COLOR = new Color(96, 96, 96);
    private static final DustParticleOptions PARTICLE = new DustParticleOptions(new Vector3f(1, 1, 1), 1);
    protected final PathfinderMob holder;
    protected final RandomSource random;
    protected final IntProvider cooldownProvider;
    protected final IntProvider attackCountProvider;
    protected final IntProvider castDurationProvider;
    protected final FloatProvider damageProvider;
    protected final DamageSource source;
    protected LivingEntity target;
    protected int cooldown;
    protected int attacks;
    protected Vec3 attackPos;
    protected int castDuration;
    protected int wasBlocked;

    public ClawsComboAttackGoal(PathfinderMob holder, IntProvider cooldown, IntProvider attackCount, IntProvider castDuration, FloatProvider damage) {
        this.holder = holder;
        random = holder.getRandom();
        cooldownProvider = cooldown;
        attackCountProvider = attackCount;
        castDurationProvider = castDuration;
        damageProvider = damage;

        source = new DamageSource(holder.level().damageSources().mobAttack(holder).typeHolder(), holder, holder, attackPos);

        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
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
        return target.isAlive() && attacks > 0;
    }

    @Override
    public void start() {
        attacks = attackCountProvider.sample(random);
        castDuration = castDurationProvider.sample(random);
        pickAttackPos();

        holder.getNavigation().stop();
        if (target.isRemoved() && target.isDeadOrDying()) return;
        holder.getLookControl().setLookAt(target);
    }

    protected void pickAttackPos() {
        attackPos = target.position();
    }

    @Override
    public void tick() {
        if (attacks <= 0) return;

        if (target != null) {
            if (target.isRemoved() && target.isDeadOrDying()) return;
            holder.getLookControl().setLookAt(target, 180f, 180f);
        }

        if (wasBlocked > 0) {
            wasBlocked--;

            ((ServerLevel) holder.level).sendParticles(PARTICLE, holder.getX() - 1, holder.getY() - 1 + holder.getBbHeight() / 2, holder.getZ() - 1,
                    30 * wasBlocked / 30, 2, 2, 2, 0.3);

            if (wasBlocked == 0) pickAttackPos();
            return;
        }

        if (castDuration > 0) {
            castDuration--;
            return;
        }

        attacks--;

        this.doPreDashEffect(); // Effect stuff
        holder.teleportTo(attackPos.x, attackPos.y, attackPos.z);
        this.doPostDashEffect(); // Effect stuff
        holder.swing(InteractionHand.MAIN_HAND);

        holder.level.playSound(null, holder, ChangedSounds.CARDBOARD_BOX_OPEN.get(), SoundSource.HOSTILE, 1.0f, 1.0f);

        if (attacks == 0) {
            applyKnockbackAndHurt(6, 2, 3);
        } else {
            castDuration = castDurationProvider.sample(random);
            if (applyKnockbackAndHurt(4, 1, 1)) {
                wasBlocked = 60;
            } else pickAttackPos();
        }
    }

    @Override
    public void stop() {
        target = null;
        cooldown = cooldownProvider.sample(random);
        attacks = 0;
        attackPos = null;
        castDuration = 0;
        wasBlocked = 0;
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
            blocked = livingEntity.isDamageSourceBlocked(source) || isTargetDoingCorrectSwingParry(target, source);

            if (livingEntity.hurt(source, damageProvider.sample(random) * damageMul)) {
                holder.getLookControl().setLookAt(livingEntity, 30, 30);
                doClawsAttackEffect();
            }

            direction = livingEntity.position().subtract(attackPos).normalize();
            knockback = dist == 0 ? radius : (radius / dist) * knockbackMul;
            if (blocked) {
                knockback *= 0.25f;
                anyBlocked = true;
            }

            livingEntity.push(
                    direction.x * knockback,
                    direction.y * knockback * 0.025,
                    direction.z * knockback
            );
        }

        return anyBlocked;
    }

    public void doClawsAttackEffect() {// Efeito visual
        double d0 = (double) (-Mth.sin(holder.getYRot() * 0.017453292F)) * 1;
        double d1 = (double) Mth.cos(holder.getYRot() * 0.017453292F) * 1;
        if (holder.level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, holder.getX() + d0, holder.getY(0.5), holder.getZ() + d1, 0, d0, 0.0, d1, 0.0);
            serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, holder.getX() + d0, holder.getY(0.6), holder.getZ() + d1, 0, d0, 0.0, d1, 0.0);
            serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, holder.getX() + d0, holder.getY(0.7), holder.getZ() + d1, 0, d0, 0.0, d1, 0.0);
            holder.level.playSound(null, holder.getX(), holder.getY(), holder.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1f, 0.75f);
        }
    }

    public void doPreDashEffect() {// Efeito visual
        if (holder.level instanceof ServerLevel serverLevel) {
            EntityModelFadeParticleOptions entityModelFadeParticleOptions = ChangedAddonParticleTypes.entityModelFade(holder, FADE_COLOR.getRGB(), 0.25f);

            Vec3 holderPosition = new Vec3(holder.getX(), holder.getY(0.5f), holder.getZ());
            Vec3 motion = attackPos.subtract(holderPosition).normalize();
            Vec3 offsetPos = new Vec3(0.25, 0.25f, 0.25f);
            Vec3 particlePos = holderPosition; //holder.position().add(0, 1.425f, 0);


            ParticlesUtil.sendParticlesWithMotionAndOffset(serverLevel, PARTICLE, holderPosition, offsetPos, motion, Vec3.ZERO, 4, 0.05f);
            ParticlesUtil.sendParticles(serverLevel, entityModelFadeParticleOptions, particlePos, motion, 0, 0.1f);
            holder.level.playSound(null, holder.getX(), holder.getY(), holder.getZ(), ChangedSounds.CARDBOARD_BOX_OPEN.get(), SoundSource.PLAYERS, 1f, 0.75f);
        }
    }

    public void doPostDashEffect() {// Efeito visual
        if (holder.level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(PARTICLE, holder.getX(), holder.getY(0.5f), holder.getZ(), 4, 0.25, 0.25f, 0.25f, 0.05);
            holder.level.playSound(null, holder.getX(), holder.getY(), holder.getZ(), ChangedSounds.CARDBOARD_BOX_OPEN.get(), SoundSource.PLAYERS, 1f, 0.75f);
        }
    }

    public int getCurrentSwingDurationFor(LivingEntity self) {
        if (MobEffectUtil.hasDigSpeed(self)) {
            return 6 - (1 + MobEffectUtil.getDigSpeedAmplification(self));
        } else {
            return self.hasEffect(MobEffects.DIG_SLOWDOWN) ? 6 + (1 + self.getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier()) * 2 : 6;
        }
    }

    protected boolean isTargetDoingCorrectSwingParry(LivingEntity target, DamageSource damageSource) {
        return isTargetDoingCorrectSwingParry(target, damageSource, 0.5f, 0.5f);
    }

    protected boolean isTargetDoingCorrectSwingParry(LivingEntity target, DamageSource damageSource, float viewPrecision, float swingPrecision) {
        // 1. Checa se o alvo está executando um swing/ataque no momento
        // swingPrecision entre 0.0f e 1.0f (ex: 0.5f = primeiros 50% do swing)
        int maxDuration = getCurrentSwingDurationFor(target);
        float allowedParryTicks = maxDuration * swingPrecision;

        // Se o swingTime passou da janela permitida, falha o parry
        if (!target.swinging || target.swingTime > allowedParryTicks) {
            return false;
        }

        Vec3 sourcePosition = damageSource.getSourcePosition();
        if (sourcePosition != null) {
            // 2. Vetor para onde o jogador está olhando (visão)
            Vec3 viewVector = target.getViewVector(1.0F);

            // 3. Vetor que vai do JOGADOR para a FONTE do dano
            Vec3 targetToSource = sourcePosition.subtract(target.getEyePosition());

            // Se quiser ignorar a diferença de altura (parry 2D/horizontal):
            // viewVector = new Vec3(viewVector.x, 0.0D, viewVector.z).normalize();
            // targetToSource = new Vec3(targetToSource.x, 0.0D, targetToSource.z).normalize();

            viewVector = viewVector.normalize();
            targetToSource = targetToSource.normalize();

            // 4. Produto escalar (dot product):
            //  1.0 = olhando EXATAMENTE para a fonte
            //  0.0 = olhando 90 graus para o lado
            // -1.0 = olhando de costas para a fonte
            double dotProduct = targetToSource.dot(viewVector);

            return dotProduct >= viewPrecision;
        }

        return false;
    }
}
