package net.foxyas.changedaddon.entity.ai.goals.luminarcticLeopard;

import net.foxyas.changedaddon.entity.ai.goals.IAbilityGoal;
import net.foxyas.changedaddon.entity.projectile.LuminarCrystalShardProjectile;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.util.EntityUtil;
import net.foxyas.changedaddon.util.ParticlesUtil;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class CircleShootLuminarCrystalShardGoal extends Goal implements IAbilityGoal {

    public enum OrbitType {
        HORIZONTAL_AROUND, // Full horizontal ring centered on eye level
        VERTICAL_BEHIND,   // Vertical half-circle arc shifted behind the mob
        VERTICAL_CENTER    // Vertical half-circle arc centered directly at the mob's X/Z
    }

    public final Mob holder;
    public final float distance;
    protected final IntProvider cooldownProvider;
    protected final IntProvider countProvider;

    public int cooldown;
    public int projectileCount;
    public int tick;

    // Configuration
    private static final double CIRCLE_RADIUS = 2.5;
    private static final int CHARGE_DURATION = 20; // Ticks before firing starts (~1 sec)
    private static final int FIRING_INTERVAL = 4;  // Ticks between firing each projectile

    private final List<LuminarCrystalShardProjectile> spawnedProjectiles = new ArrayList<>();
    private boolean isFullySpawned = false;
    private int currentFireIndex = 0;

    // Orbit mode randomly assigned per usage
    private OrbitType currentOrbitType = OrbitType.HORIZONTAL_AROUND;

    public CircleShootLuminarCrystalShardGoal(Mob holder, IntProvider cooldownProvider, IntProvider countProvider) {
        this(holder, cooldownProvider, countProvider, 25f);
    }

    public CircleShootLuminarCrystalShardGoal(Mob holder, IntProvider cooldownProvider, IntProvider countProvider, float distance) {
        super();
        this.holder = holder;
        this.cooldownProvider = cooldownProvider;
        this.countProvider = countProvider;
        this.distance = distance;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
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
        this.tick = 0;
        this.currentFireIndex = 0;
        this.isFullySpawned = false;
        this.spawnedProjectiles.clear();
        this.projectileCount = getCountProvider().sample(this.holder.getRandom());

        // Pick randomly among all 3 orbit types
        OrbitType[] types = OrbitType.values();
        this.currentOrbitType = types[holder.getRandom().nextInt(types.length)];

        if (holder.level() instanceof ServerLevel level) {
            spawnCircleProjectiles(level);
        }
    }

    /**
     * Spawns all projectiles based on the selected orbit strategy.
     */
    private void spawnCircleProjectiles(ServerLevel level) {
        for (int i = 0; i < projectileCount; i++) {
            Vec3 spawnPos = calculateProjectilePosition(i, 0);

            LuminarCrystalShardProjectile projectile = createProjectile(level, spawnPos);


            projectile.setDeltaMovement(Vec3.ZERO);
            LivingEntity target = holder.getTarget();
            if (target != null) {
                EntityUtil.makeEntityForceFaceAt(projectile, target.getEyePosition(0));
            }

            level.addFreshEntity(projectile);
            spawnedProjectiles.add(projectile);
        }

        level.playSound(null, holder, SoundEvents.WITHER_SPAWN, SoundSource.HOSTILE, 0.5f, 1.5f);
        isFullySpawned = true;
    }

    protected @NotNull LuminarCrystalShardProjectile createProjectile(ServerLevel level, Vec3 spawnPos) {
        RandomSource random = holder.getRandom();

        LuminarCrystalShardProjectile projectile = new LuminarCrystalShardProjectile(ChangedAddonEntities.LUMINAR_CRYSTAL_SHARD.get(), level) {
            @Override
            protected boolean canHitEntity(@NotNull Entity entity) {
                if (entity.is(holder)) {
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
                return super.getGravity() / Math.max(0.01f, (float) this.getDeltaMovement().length());
            }

            @Override
            public void shoot(double pX, double pY, double pZ, float pVelocity, float pInaccuracy) {
                super.shoot(pX, pY, pZ, pVelocity, pInaccuracy);
                this.setNoGravity(false);
            }

            @Override
            public boolean shouldBreakOnImpact(BlockHitResult hitResult) {
                return true;
            }

            @Override
            public void breakOnImpact(BlockHitResult hitResult) {
                super.breakOnImpact(hitResult);
                ParticlesUtil.sendParticles(this.level, new BlockParticleOption(ParticleTypes.BLOCK, ChangedAddonBlocks.LUMINAR_CRYSTAL_SMALL.get().defaultBlockState()), this.position(), 0.05f, 0.05f, 0.05f, 8, 0.5f);
            }
        };

        projectile.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
        projectile.setOwner(holder);
        projectile.setNoGravity(true);
        projectile.setCritArrow(random.nextBoolean());
        projectile.setKnockback(2);
        projectile.setBaseDamage(5f);
        projectile.setPierceLevel((byte) (random.nextInt(3)));

        return projectile;
    }

    @Override
    public void tick() {
        super.tick();
        tick++;
        PathNavigation navigation = holder.getNavigation();
        Path path = navigation.getPath();
        if (path != null && navigation.isInProgress()) {
            holder.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 1, true, false, false));
        }

        LivingEntity target = holder.getTarget();
        if (target != null) {
            holder.getLookControl().setLookAt(target, 180f, 180f);
        }

        if (tick < getChargeDuration()) {
            updateHoverPositions();
            return;
        }

        if ((tick - getChargeDuration()) % getFiringIntervale() == 0 && currentFireIndex < spawnedProjectiles.size()) {
            LuminarCrystalShardProjectile projectile = spawnedProjectiles.get(currentFireIndex);

            if (projectile != null && projectile.isAlive()) {
                fireProjectileAtTarget(projectile, target);
            }

            currentFireIndex++;
        }
    }

    protected int getChargeDuration() {
        return CHARGE_DURATION;
    }

    protected int getFiringIntervale() {
        return FIRING_INTERVAL;
    }

    /**
     * Updates un-launched projectile locations in orbit while charging, using smooth motion.
     */
    private void updateHoverPositions() {
        for (int i = currentFireIndex; i < spawnedProjectiles.size(); i++) {
            LuminarCrystalShardProjectile projectile = spawnedProjectiles.get(i);
            if (projectile != null && projectile.isAlive()) {
                Vec3 targetPos = calculateProjectilePosition(i, tick);

                // Calcula a velocidade necessária para deslizar suavemente até a posição alvo (lerp/atração)
                Vec3 currentPos = projectile.position();
                Vec3 movement = targetPos.subtract(currentPos);

                // Fator de suavização (quanto maior, mais rápido ele alcança o ponto, 0.4f é um bom meio termo fluido)
                if (movement.lengthSqr() > 0.0001D) {
                    projectile.setDeltaMovement(movement.scale(0.4D));
                } else {
                    projectile.setDeltaMovement(Vec3.ZERO);
                    projectile.setPos(targetPos.x, targetPos.y, targetPos.z);
                }

                LivingEntity target = holder.getTarget();
                if (target != null) {
                    EntityUtil.makeEntityForceFaceAt(projectile, target.getEyePosition(0));
                }
            }
        }
    }

    /**
     * Calculates the exact position for each projectile depending on the active OrbitType.
     */
    private Vec3 calculateProjectilePosition(int index, int currentTick) {
        Vec3 headPos = holder.getEyePosition(0);

        if (this.currentOrbitType == OrbitType.HORIZONTAL_AROUND) {
            // Full 360-degree circle horizontally around the mob
            double angleStep = (2 * Math.PI) / projectileCount;
            double angle = (index * angleStep) + (currentTick * 0.05);

            double xOffset = CIRCLE_RADIUS * Math.cos(angle);
            double zOffset = CIRCLE_RADIUS * Math.sin(angle);
            return headPos.add(xOffset, 0, zOffset);
        } else {
            // Half-circle arc above/around eye level (0 to Math.PI radians) to avoid bottom collisions
            double angleStep = (projectileCount > 1) ? (Math.PI / (projectileCount - 1)) : 0;
            double baseAngle = index * angleStep;

            // Slight floating wave oscillation during charge tick
            double angle = baseAngle + (Math.sin(currentTick * 0.1 + index) * 0.05);

            float yRot = holder.getYRot();
            float radYaw = yRot * Mth.DEG_TO_RAD;

            // Horizontal right vector relative to body facing direction
            Vec3 rightVec = new Vec3(-Math.cos(radYaw), 0, -Math.sin(radYaw));
            Vec3 upVec = new Vec3(0, 1, 0);

            double offsetX = CIRCLE_RADIUS * Math.cos(angle);
            double offsetY = CIRCLE_RADIUS * Math.sin(angle);

            Vec3 arcCenter = headPos;

            if (this.currentOrbitType == OrbitType.VERTICAL_BEHIND) {
                // Offset backward relative to look vector
                Vec3 lookVec = Vec3.directionFromRotation(0, yRot);
                double behindOffset = holder.getBbWidth() + 0.8D;
                arcCenter = headPos.subtract(lookVec.scale(behindOffset));
            }

            return arcCenter
                    .add(rightVec.scale(offsetX))
                    .add(upVec.scale(offsetY));
        }
    }

    private void fireProjectileAtTarget(LuminarCrystalShardProjectile projectile, LivingEntity target) {
        if (holder.level() instanceof ServerLevel level) {
            holder.swing(InteractionHand.MAIN_HAND);

            Vec3 startPos = projectile.position();
            Vec3 shootDir;

            if (target != null && !target.isDeadOrDying()) {
                shootDir = target.getEyePosition().subtract(startPos).normalize();
            } else {
                shootDir = holder.getLookAngle();
            }

            // Expanded bounding box check: prevents self-hits regardless of origin angle
            AABB inflatedBox = holder.getBoundingBox().inflate(0.5D);
            Vec3 safeFirePos = startPos;

            // Step along the shooting path; if it crosses or starts inside the owner's hitbox,
            // push the launch origin out past the far edge of the mob
            if (inflatedBox.contains(startPos) || intersectsBoundingBox(startPos, shootDir, inflatedBox)) {
                double safeDistance = holder.getBbWidth() + 1.2D;
                safeFirePos = holder.getEyePosition().add(shootDir.scale(safeDistance));
            }

            projectile.setPos(safeFirePos.x, safeFirePos.y, safeFirePos.z);

            // Keeps noGravity true so the projectiles fly straight without dipping quickly
            projectile.setNoGravity(true);

            float inaccuracy = level.getDifficulty() == Difficulty.HARD ? 0.0f : Mth.nextFloat(holder.getRandom(), 0.5f, 1.0f);
            projectile.shoot(shootDir.x, shootDir.y, shootDir.z, 2.0f, inaccuracy);

            level.playSound(null, projectile.getX(), projectile.getY(), projectile.getZ(),
                    SoundEvents.BLAZE_SHOOT, SoundSource.HOSTILE, 1.0f, 1.0f);
        }
    }

    /**
     * Ray-checks if the line from firing position toward direction intersects the mob's bounding box.
     */
    private boolean intersectsBoundingBox(Vec3 origin, Vec3 dir, AABB box) {
        Vec3 end = origin.add(dir.scale(CIRCLE_RADIUS * 2));
        return box.clip(origin, end).isPresent();
    }

    @Override
    public boolean canContinueToUse() {
        if (holder.getTarget() == null) {
            return false;
        }
        return currentFireIndex < spawnedProjectiles.size();
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public void stop() {
        super.stop();
        this.cooldown = getCooldownProvider().sample(this.holder.getRandom());

        for (int i = currentFireIndex; i < spawnedProjectiles.size(); i++) {
            LuminarCrystalShardProjectile projectile = spawnedProjectiles.get(i);
            if (projectile != null && projectile.isAlive()) {
                projectile.discard();
            }
        }
        spawnedProjectiles.clear();
    }

    public IntProvider getCooldownProvider() {
        return cooldownProvider;
    }

    public IntProvider getCountProvider() {
        return countProvider;
    }
}