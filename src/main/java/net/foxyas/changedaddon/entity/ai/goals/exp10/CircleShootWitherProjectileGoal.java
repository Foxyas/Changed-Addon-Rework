package net.foxyas.changedaddon.entity.ai.goals.exp10;

import net.foxyas.changedaddon.entity.ai.goals.IAbilityGoal;
import net.foxyas.changedaddon.entity.projectile.WitherParticleProjectile;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class CircleShootWitherProjectileGoal extends Goal implements IAbilityGoal {

    public enum OrbitType {
        HORIZONTAL_AROUND, // Círculo horizontal ao redor do mob
        VERTICAL_BEHIND    // Círculo vertical atrás das costas do mob
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
    private static final double BEHIND_OFFSET = 1.2; // Distância para trás da cabeça quando em modo VERTICAL
    private static final int CHARGE_DURATION = 20; // Ticks antes de disparar (~1 sec)
    private static final int FIRING_INTERVAL = 4;  // Ticks entre cada projétil

    private final List<WitherParticleProjectile> spawnedProjectiles = new ArrayList<>();
    private boolean isFullySpawned = false;
    private int currentFireIndex = 0;

    // Tipo de órbita sorteado para a execução atual
    private OrbitType currentOrbitType = OrbitType.HORIZONTAL_AROUND;

    public CircleShootWitherProjectileGoal(Mob holder, IntProvider cooldownProvider, IntProvider countProvider) {
        this(holder, cooldownProvider, countProvider, 25f);
    }

    public CircleShootWitherProjectileGoal(Mob holder, IntProvider cooldownProvider, IntProvider countProvider, float distance) {
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
        this.projectileCount = countProvider.sample(this.holder.getRandom());

        // Escolhe o tipo de órbita aleatoriamente (50% de chance para cada)
        this.currentOrbitType = holder.getRandom().nextBoolean() ? OrbitType.HORIZONTAL_AROUND : OrbitType.VERTICAL_BEHIND;

        if (holder.level() instanceof ServerLevel level) {
            spawnCircleProjectiles(level);
        }
    }

    /**
     * Spawns all projectiles around or behind the mob according to the orbit type.
     */
    private void spawnCircleProjectiles(ServerLevel level) {
        for (int i = 0; i < projectileCount; i++) {
            Vec3 spawnPos = calculateProjectilePosition(i, 0);

            WitherParticleProjectile projectile = new WitherParticleProjectile(ChangedAddonEntities.WITHER_PARTICLE_PROJECTILE.get(), level);
            projectile.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
            projectile.setOwner(holder);
            projectile.setNoGravity(true);
            projectile.setCritArrow(holder.getRandom().nextBoolean());
            projectile.setKnockback(2);
            projectile.setBaseDamage(5f);

            projectile.setDeltaMovement(Vec3.ZERO);

            level.addFreshEntity(projectile);
            spawnedProjectiles.add(projectile);
        }

        level.playSound(null, holder, SoundEvents.WITHER_SPAWN, SoundSource.HOSTILE, 0.5f, 1.5f);
        isFullySpawned = true;
    }

    @Override
    public void tick() {
        super.tick();
        tick++;

        LivingEntity target = holder.getTarget();
        if (target != null) {
            holder.getLookControl().setLookAt(target, 180f, 180f);
        }

        if (tick < CHARGE_DURATION) {
            updateHoverPositions();
            return;
        }

        if ((tick - CHARGE_DURATION) % FIRING_INTERVAL == 0 && currentFireIndex < spawnedProjectiles.size()) {
            WitherParticleProjectile projectile = spawnedProjectiles.get(currentFireIndex);

            if (projectile != null && projectile.isAlive()) {
                fireProjectileAtTarget(projectile, target);
            }

            currentFireIndex++;
        }
    }

    /**
     * Keeps surviving un-launched projectiles in formation around/behind the entity while charging.
     */
    private void updateHoverPositions() {
        for (int i = currentFireIndex; i < spawnedProjectiles.size(); i++) {
            WitherParticleProjectile projectile = spawnedProjectiles.get(i);
            if (projectile != null && projectile.isAlive()) {
                Vec3 targetPos = calculateProjectilePosition(i, tick);
                projectile.setPos(targetPos.x, targetPos.y, targetPos.z);
                projectile.setDeltaMovement(Vec3.ZERO);
            }
        }
    }

    /**
     * Calcule a posição 3D exata de um projétil baseado no tipo de órbita ativo.
     */
    private Vec3 calculateProjectilePosition(int index, int currentTick) {
        Vec3 headPos = holder.getEyePosition();
        double angleStep = (2 * Math.PI) / projectileCount;
        double angle = (index * angleStep) + (currentTick * 0.05); // Rotação suave ao longo do tempo

        if (this.currentOrbitType == OrbitType.HORIZONTAL_AROUND) {
            // Órbita Horizontal Padrão
            double xOffset = CIRCLE_RADIUS * Math.cos(angle);
            double zOffset = CIRCLE_RADIUS * Math.sin(angle);
            return headPos.add(xOffset, 0, zOffset);
        } else {
            // Órbita Vertical Atrás das Costas do Mob
            float yRot = holder.getYRot(); // Rotação horizontal do mob
            Vec3 lookVec = Vec3.directionFromRotation(0, yRot); // Vetor da olhada horizontal

            // Posição central do círculo (Deslocado ligeiramente para trás do mob)
            Vec3 circleCenter = headPos.subtract(lookVec.scale(BEHIND_OFFSET));

            // Vetor lateral (Right Vector) em relação para onde o mob está olhando
            float radYaw = yRot * Mth.DEG_TO_RAD;
            Vec3 rightVec = new Vec3(-Math.cos(radYaw), 0, -Math.sin(radYaw));
            Vec3 upVec = new Vec3(0, 1, 0); // Eixo vertical

            double offsetX = CIRCLE_RADIUS * Math.cos(angle);
            double offsetY = CIRCLE_RADIUS * Math.sin(angle);

            // Combina os vetores para formar o plano vertical
            return circleCenter
                    .add(rightVec.scale(offsetX))
                    .add(upVec.scale(offsetY));
        }
    }

    private void fireProjectileAtTarget(WitherParticleProjectile projectile, LivingEntity target) {
        if (holder.level() instanceof ServerLevel level) {
            holder.swing(InteractionHand.MAIN_HAND);

            Vec3 shootDir;
            if (target != null && !target.isDeadOrDying()) {
                shootDir = target.getEyePosition().subtract(projectile.position()).normalize();
            } else {
                shootDir = holder.getLookAngle();
            }

            projectile.setNoGravity(false);
            projectile.shoot(shootDir.x, shootDir.y, shootDir.z, 2.25f, 1.0f);

            level.playSound(null, projectile.getX(), projectile.getY(), projectile.getZ(),
                    SoundEvents.BLAZE_SHOOT, SoundSource.HOSTILE, 1.0f, 1.0f);
        }
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
        this.cooldown = cooldownProvider.sample(this.holder.getRandom());

        for (int i = currentFireIndex; i < spawnedProjectiles.size(); i++) {
            WitherParticleProjectile projectile = spawnedProjectiles.get(i);
            if (projectile != null && projectile.isAlive()) {
                projectile.discard();
            }
        }
        spawnedProjectiles.clear();
    }
}