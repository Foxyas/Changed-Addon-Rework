
package net.foxyas.changedaddon.entity.ai.goals.luminarcticLeopard;

import net.foxyas.changedaddon.util.FoxyasUtil;
import net.foxyas.changedaddon.util.ParticlesUtil;

import net.foxyas.changedaddon.entity.projectile.LuminarCrystalShardProjectile;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.List;

public class CrystalLeapSmashGoal extends Goal {

    private final Mob mob;
    public int cooldown;
    private LivingEntity target;
    private int leapTicks;
    private boolean wasInAir;

    public CrystalLeapSmashGoal(Mob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        target = mob.getTarget();

        if (cooldown > 0) {
            cooldown--;
            return false;
        }

        if (target == null || !target.isAlive()) return false;
        if (!mob.onGround()) return false;

        return mob.distanceTo(target) >= 3;
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
    public void start() {
        leapTicks = 0;
        wasInAir = false;

        Vec3 dir = target.position()
                .subtract(mob.position())
                .normalize();

        double horizontalBoost = 1.5;
        double verticalBoost = 1.5;

        mob.setDeltaMovement(
                dir.x * horizontalBoost,
                verticalBoost,
                dir.z * horizontalBoost
        );

        mob.getNavigation().stop();
        mob.getLookControl().setLookAt(target, 30.0F, 30.0F);

        mob.level().playSound(
                null,
                mob.blockPosition(),
                SoundEvents.GOAT_LONG_JUMP,
                SoundSource.HOSTILE,
                1.0F,
                0.9F
        );

        mob.level().broadcastEntityEvent(mob, (byte) 4);
        mob.getJumpControl().jump();
    }

    @Override
    public void tick() {
        leapTicks++;

        if (mob.onGround() && wasInAir) {
            performSmash();
            stop();
        }

        if (!mob.onGround()) {
            wasInAir = true;
            mob.getNavigation().stop();

            if (leapTicks >= 40) {
                Vec3 motion = mob.getDeltaMovement();
                double verticalBoost = -0.5f;

                mob.getLookControl().setLookAt(
                        motion.x,
                        verticalBoost,
                        motion.z,
                        180,
                        180
                );

                mob.setDeltaMovement(
                        motion.x,
                        verticalBoost,
                        motion.z
                );
            }
        }
    }

    private void performSmash() {
        BlockPos center = mob.blockPosition();
        int radius = 4;

        // Block destruction effects
        for (BlockPos pos : FoxyasUtil.betweenClosedStreamSphere(
                new AABB(center.below()).inflate(radius, 0, radius)
        ).map(BlockPos::immutable).toList()) {

            if (!isInsideSphere(
                    center.below(),
                    Vec3.atBottomCenterOf(pos),
                    radius
            )) continue;

            BlockState blockState = mob.level().getBlockState(pos);

            mob.level().levelEvent(
                    2001,
                    pos,
                    Block.getId(blockState)
            );

            if (mob.level() instanceof ServerLevel serverLevel) {
                int breakerId = mob.getId() + pos.hashCode();

                serverLevel.destroyBlockProgress(
                        breakerId,
                        pos,
                        5
                );
            }
        }

        // Smash explosion particles
        if (mob.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ParticleTypes.EXPLOSION_EMITTER,
                    center.getX(),
                    center.getY(),
                    center.getZ(),
                    1,
                    0,
                    0,
                    0,
                    1
            );
        }

        // Find entities hit by the smash
        AABB smashArea = mob.getBoundingBox().inflate(4.0D);

        List<LivingEntity> hitEntities = mob.level().getEntitiesOfClass(
                LivingEntity.class,
                smashArea,
                e -> e != mob && e.isAlive()
        );

        for (LivingEntity entity : hitEntities) {
            if (entity.hurt(
                    mob.damageSources().mobAttack(mob),
                    4.0F
            )) {

                // Knockback
                Vec3 knockback = entity.position()
                        .subtract(mob.position())
                        .normalize()
                        .scale(1.2);

                entity.push(
                        knockback.x,
                        0.5,
                        knockback.z
                );

                // Crystal projectile burst
                if (mob.level() instanceof ServerLevel serverLevel) {
                    spawnCrystalBurst(serverLevel, entity);
                }
            }
        }

        mob.level().playSound(
                null,
                mob.blockPosition(),
                SoundEvents.PLAYER_ATTACK_STRONG,
                SoundSource.HOSTILE,
                1.5F,
                0.8F
        );

        wasInAir = false;
    }

    /**
     * Spawns crystal shards around the hit entity,
     * shooting them outward from the smashing mob.
     */
    private void spawnCrystalBurst(
            ServerLevel level,
            LivingEntity entity
    ) {
        Vec3 spawnPos = entity.position().add(
                0,
                entity.getBbHeight() * 0.5,
                0
        );

        Vec3 direction = entity.position()
                .subtract(mob.position())
                .normalize();

        RandomSource random = mob.getRandom();

        int projectileCount = 3;

        for (int i = 0; i < projectileCount; i++) {
            LuminarCrystalShardProjectile projectile =
                    createProjectile(level, spawnPos);

            double spreadX = random.nextDouble() * 0.5 - 0.25;
            double spreadY = random.nextDouble() * 0.5 - 0.1;
            double spreadZ = random.nextDouble() * 0.5 - 0.25;

            projectile.shoot(
                    direction.x + spreadX,
                    direction.y + spreadY,
                    direction.z + spreadZ,
                    1.5F,
                    5.0F
            );

            level.addFreshEntity(projectile);
        }
    }

    /**
     * Creates a Luminar Crystal Shard projectile.
     */
    protected @NotNull LuminarCrystalShardProjectile createProjectile(
            ServerLevel level,
            Vec3 spawnPos
    ) {
        RandomSource random = mob.getRandom();

        LuminarCrystalShardProjectile projectile =
                new LuminarCrystalShardProjectile(
                        ChangedAddonEntities.LUMINAR_CRYSTAL_SHARD.get(),
                        level
                ) {

                    @Override
                    protected boolean canHitEntity(@NotNull Entity entity) {
                        if (entity.is(mob)) {
                            return false;
                        }

                        return super.canHitEntity(entity);
                    }

                    @Override
                    protected SoundEvent getDefaultHitGroundSoundEvent() {
                        return SoundEvents.AMETHYST_BLOCK_BREAK;
                    }

                    @Override
                    protected float getGravity() {
                        return super.getGravity() /
                                Math.max(
                                        0.01f,
                                        (float) this.getDeltaMovement().length()
                                );
                    }

                    @Override
                    public void shoot(
                            double pX,
                            double pY,
                            double pZ,
                            float pVelocity,
                            float pInaccuracy
                    ) {
                        super.shoot(
                                pX,
                                pY,
                                pZ,
                                pVelocity,
                                pInaccuracy
                        );

                        this.setNoGravity(false);
                    }

                    @Override
                    public boolean shouldBreakOnImpact(BlockHitResult hitResult) {
                        return true;
                    }

                    @Override
                    public void breakOnImpact(BlockHitResult hitResult) {
                        super.breakOnImpact(hitResult);

                        ParticlesUtil.sendParticles(
                                this.level,
                                new BlockParticleOption(
                                        ParticleTypes.BLOCK,
                                        ChangedAddonBlocks.LUMINAR_CRYSTAL_SMALL
                                                .get()
                                                .defaultBlockState()
                                ),
                                this.position(),
                                0.05f,
                                0.05f,
                                0.05f,
                                8,
                                0.5f
                        );
                    }
                };

        projectile.setPos(
                spawnPos.x,
                spawnPos.y,
                spawnPos.z
        );

        projectile.setOwner(mob);
        projectile.setCritArrow(random.nextBoolean());
        projectile.setKnockback(2);
        projectile.setBaseDamage(5f);
        projectile.setPierceLevel((byte) random.nextInt(3));

        return projectile;
    }

    @Override
    public boolean canContinueToUse() {
        return leapTicks < 60 && !mob.onGround();
    }

    @Override
    public void stop() {
        if (mob.onGround() && wasInAir) {
            performSmash();
        }

        leapTicks = 0;
        wasInAir = false;
        cooldown = 40;
    }

    /**
     * Checks whether a point is inside a sphere.
     */
    private static boolean isInsideSphere(
            BlockPos center,
            Vec3 point,
            float radius
    ) {
        return center.distToCenterSqr(point) <= radius * radius;
    }
}
