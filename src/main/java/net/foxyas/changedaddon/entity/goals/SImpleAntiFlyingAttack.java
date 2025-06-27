package net.foxyas.changedaddon.entity.goals;

import net.foxyas.changedaddon.entity.VoidFoxEntity;
import net.foxyas.changedaddon.entity.projectile.ParticleProjectile;
import net.foxyas.changedaddon.registers.ChangedAddonEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

public class SImpleAntiFlyingAttack extends Goal {
    private final Mob attacker;
    private LivingEntity target;
    private int ticks = 0;
    private final int delay;
    private final float minRange;
    private final float maxRange;
    private final float damage;
    private final EntityType<? extends AbstractArrow> projectileType = ChangedAddonEntities.PARTICLE_PROJECTILE.get();

    public SImpleAntiFlyingAttack(Mob attacker, float minRange, float maxRange, float damage, int delay) {
        this.attacker = attacker;
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.damage = damage;
        this.delay = delay;

        //setFlags(EnumSet.of(Flag.TARGET, Flag.MOVE));
    }

    public Mob getAttacker() {
        return attacker;
    }

    public LivingEntity getTarget() {
        return target;
    }

    public float getMinRange() {
        return minRange;
    }

    public float getMaxRange() {
        return maxRange;
    }

    @Override
    public boolean canUse() {
        this.target = attacker.getTarget();
        if (target instanceof Player player) {
            if (player.isCreative() || player.isSpectator()) {
                return false;
            }
            return player.getAbilities().flying && attacker.distanceTo(player) <= maxRange;
        }
        return target != null && !target.isOnGround() &&
                attacker.distanceTo(target) >= minRange && attacker.distanceTo(target) <= maxRange;
    }

    @Override
    public boolean canContinueToUse() {
        if (target instanceof Player player) {
            if (player.isCreative() || player.isSpectator()) {
                return false;
            }
            return player.isAlive() && !player.isOnGround();
        }
        return target != null && target.isAlive() && !target.isOnGround();
    }

    @Override
    public void start() {
        shootProjectile(target);
        attacker.level().playSound(null, attacker, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.HOSTILE, 2, 1);
        if (attacker.getEyePosition().distanceTo(target.getEyePosition()) <= 2f) {
            teleportAndKnockbackInAir(1);
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    protected int adjustedTickDelay(int delay) {
        return super.adjustedTickDelay(delay);
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public void tick() {
        ticks++;
        if (ticks % delay == 0) {
            if (target instanceof Player player && player.isAlive()
                    && !player.isOnGround() && player.getAbilities().flying) {
                if (attacker.distanceTo(target) >= 2) {
                    shootProjectile(target);
                    attacker.level().playSound(null, attacker, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.HOSTILE, 2, 1);
                } else if (attacker.distanceTo(target) <= 2 && attacker.isOnGround()) {
                    slam();
                }
            } else if (target instanceof Player player && player.isAlive()
                    && !player.isOnGround() && player.isFallFlying()) {
                if (attacker.getEyePosition().distanceTo(target.getEyePosition()) <= 2f && attacker.isOnGround()) {
                    slam();
                } else {
                    shootProjectile(target);
                    attacker.level().playSound(null, attacker, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.HOSTILE, 2, 1);
                }
            }
            if (!target.isOnGround()) {
                if (attacker.getEyePosition().distanceTo(target.getEyePosition()) <= 2f && attacker.isOnGround()) {
                    slam();
                } else {
                    shootProjectile(target);
                    attacker.level().playSound(null, attacker, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.HOSTILE, 2, 1);
                }
            }
        } else {
            attacker.getNavigation().stop();
            attacker.getLookControl().setLookAt(target, 30.0F, 30.0F);
            if (attacker.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.ENCHANT, attacker.getX(), attacker.getEyeY(), attacker.getZ(), 4, 0.25, 0.5, 0.25, 0.5);
                serverLevel.sendParticles(ParticleTypes.END_ROD, attacker.getX(), attacker.getEyeY(), attacker.getZ(), 4, 0.25, 0.5, 0.25, 0.05f);
            }
        }
    }

    @Override
    public void stop() {
        if (!attacker.isOnGround()) {
            BlockPos pos = attacker.blockPosition();
            int groundY = attacker.level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ());
            attacker.teleportTo(pos.getX() + 0.5, groundY + 0.5, pos.getZ() + 0.5);
            spawnImpactEffect(attacker.position(), 3);
            spawnImpactParticleEffect(target.position(), 2);
            //target.teleportTo(attacker.getX(), attacker.getY(), attacker.getZ());
        }
    }

    private void removeIframesFromTarget() {
        target.invulnerableTime = 0;
        target.hurtDuration = 1;
        target.hurtDir = 1;
        target.hurtTime = 1;
    }

    private void teleportAndKnockbackInAir(float strength) {
        if (target == null) return;
        attacker.teleportTo(target.getX(), target.getY(), target.getZ());
        Vec3 knockDir = attacker.getLookAngle().scale(strength).add(0, 0.2, 0);
        target.setDeltaMovement(knockDir);
        attacker.swing(InteractionHand.MAIN_HAND);
        removeIframesFromTarget();
        if (!target.isBlocking()) {
            target.hurt(DamageSource.mobAttack(attacker), damage / 2);
        } else {
            target.level().playSound(null, target, SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1, 1);
        }
        applySlowFalling(target);
        spawnImpactEffect(target.position(), 0);
        spawnImpactParticleEffect(target.position(), 0);
    }

    private void slam() {
        if (target == null) return;
        if (target instanceof Player player) {
            if (player.getAbilities().flying) {
                player.getAbilities().flying = false;
            }
        }
        attacker.teleportTo(target.getX(), target.getY(), target.getZ());
        target.setDeltaMovement(0, -4, 0);
        attacker.swing(InteractionHand.MAIN_HAND);
        removeIframesFromTarget();
        if (!target.isBlocking()) {
            target.hurt(DamageSource.mobAttack(attacker), damage);
        } else {
            target.level().playSound(null, target, SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1, 1);
        }
        spawnImpactEffect(target.position(), 2);
        spawnImpactParticleEffect(target.position(), 2);
        removeSlowFalling();
    }

    private void removeSlowFalling() {
        target.removeEffect(MobEffects.SLOW_FALLING);
        attacker.removeEffect(MobEffects.SLOW_FALLING);
    }

    private void applySlowFalling(LivingEntity entity) {
        MobEffectInstance slowFalling = new MobEffectInstance(MobEffects.SLOW_FALLING, 40, 0, false, false);
        entity.addEffect(slowFalling);
        attacker.addEffect(slowFalling);
    }

    private void spawnImpactEffect(Vec3 pos, int type) {
        if (attacker.level instanceof ServerLevel serverLevel) {
            /*if (type <= impactParticle.length) {
                serverLevel.sendParticles(impactParticle[type], pos.x, pos.y, pos.z, 10, 0.5, 0.5, 0.5, 0);
            }*/
            attacker.playSound(SoundEvents.PLAYER_ATTACK_CRIT, 2, 1);
        }
    }

    private void spawnImpactParticleEffect(Vec3 pos, int type) {
        if (attacker.level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.FLASH, pos.x, pos.y, pos.z, 3, 0, 0, 0, 0);
            if (type == 0 && attacker instanceof VoidFoxEntity voidFoxEntity) {
                if (voidFoxEntity.getMainHandItem().isEmpty()) {
                    voidFoxEntity.doClawsAttackEffect();
                }
            }
        }
    }


    protected void shootProjectile(LivingEntity target) {
        Level level = attacker.level;
        switch (attacker.getRandom().nextInt(3)) {
            case 0 -> spawnProjectilesInCircle(attacker, target, new Vec3(0, 0.5, 0), 2, 4, level);
            case 1 -> spawnProjectilesInXCircleTargetInPlayer(attacker, target, new Vec3(0, 0, 0), 2, 8, level);
            case 2 -> spawnProjectilesInXCircleTargetInPlayerPos(attacker, target, new Vec3(0, 0, 0), 2, 8, level);
        }
    }

    public void spawnProjectilesInCircle(LivingEntity entity, LivingEntity target, Vec3 offset, double radius, int count, Level level) {
        Vec3 center = entity.getEyePosition().add(offset); // centro do círculo com offset

        for (int i = 0; i < count; i++) {
            double angle = 2 * Math.PI * i / count;
            Vec3 dir = new Vec3(1, 0, 0).zRot((float) angle)
                    .yRot((float) Math.toRadians(-attacker.getYRot()))
                    .normalize();
            Vec3 spawnPos = center.add(dir.scale(radius));

            // Spawn do projétil
            if (!level.isClientSide()) {
                ParticleProjectile projectile = new ParticleProjectile(projectileType, attacker, attacker.level(), target);
                projectile.setPos(spawnPos);
                projectile.setNoGravity(true);
                projectile.setOwner(attacker);
                projectile.teleport = false;

                Vec3 direction = target.getEyePosition(1.0F).subtract(attacker.getEyePosition(0.5f)).normalize();
                projectile.shoot(direction.x, direction.y, direction.z, 1.5f, 0.0f);


                entity.level.addFreshEntity(projectile);
            }
        }
        attacker.swing(InteractionHand.MAIN_HAND);
    }

    public void spawnProjectilesInXCircleTargetInPlayer(LivingEntity entity, LivingEntity target, Vec3 offset, double radius, int count, Level level) {
        Vec3 center = target.getEyePosition().add(offset); // centro do círculo com offset

        for (int i = 0; i < count; i++) {
            double angle = 2 * Math.PI * i / count;
            Vec3 dir = new Vec3(1, 0, 0).yRot((float) angle)
                    .normalize();
            Vec3 spawnPos = center.add(dir.scale(radius));

            // Spawn do projétil
            if (!level.isClientSide()) {
                ParticleProjectile projectile = new ParticleProjectile(projectileType, attacker, attacker.level(), target);
                projectile.setPos(spawnPos);
                projectile.setNoGravity(true);
                projectile.setOwner(attacker);
                projectile.teleport = false;

                Vec3 direction = target.getEyePosition(1.0F).subtract(attacker.getEyePosition(0.5f)).normalize();
                projectile.shoot(direction.x, direction.y, direction.z, 1.5f, 0.0f);


                entity.level.addFreshEntity(projectile);
            }
        }
        attacker.swing(InteractionHand.MAIN_HAND);
    }

    public void spawnProjectilesInXCircleTargetInPlayerPos(LivingEntity entity, LivingEntity target, Vec3 offset, double radius, int count, Level level) {
        Vec3 center = target.getEyePosition().add(offset); // centro do círculo com offset

        for (int i = 0; i < count; i++) {
            double angle = 2 * Math.PI * i / count;
            Vec3 dir = new Vec3(1, 0, 0).xRot((float) angle)
                    .normalize();
            Vec3 spawnPos = center.add(dir.scale(radius));

            // Spawn do projétil
            if (!level.isClientSide()) {
                ParticleProjectile projectile = new ParticleProjectile(projectileType, attacker, attacker.level(), null);
                projectile.setTargetPos(target.position());
                projectile.setPos(spawnPos);
                projectile.setNoGravity(true);
                projectile.setOwner(attacker);
                projectile.teleport = false;

                Vec3 direction = target.getEyePosition(1.0F).subtract(attacker.getEyePosition(0.5f)).normalize();
                projectile.shoot(direction.x, direction.y, direction.z, 1.5f, 0.0f);


                entity.level.addFreshEntity(projectile);
            }
        }
        attacker.swing(InteractionHand.MAIN_HAND);
    }
}
