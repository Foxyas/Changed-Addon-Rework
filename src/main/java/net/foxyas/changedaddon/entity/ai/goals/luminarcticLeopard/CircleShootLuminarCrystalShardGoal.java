package net.foxyas.changedaddon.entity.ai.goals.luminarcticLeopard;

import net.foxyas.changedaddon.entity.ai.goals.IAbilityGoal;
import net.foxyas.changedaddon.entity.projectile.LuminarCrystalShardProjectile;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class CircleShootLuminarCrystalShardGoal extends Goal implements IAbilityGoal {

    public enum OrbitType {
        HORIZONTAL_AROUND, // Anel horizontal completo no nível dos olhos
        VERTICAL_BEHIND,   // Arco de meia-lua vertical atrás das costas do mob
        VERTICAL_CENTER    // Arco de meia-lua vertical centralizado no X/Z do mob
    }

    public final Mob holder;
    public final float distance;
    protected final IntProvider cooldownProvider;
    protected final IntProvider countProvider;

    public int cooldown;
    public int projectileCount;
    public int tick;

    // Configuração
    private static final double CIRCLE_RADIUS = 2.5;
    private static final int CHARGE_DURATION = 20; // Ticks antes de disparar (~1 seg)
    private static final int FIRING_INTERVAL = 4;  // Ticks entre cada disparo

    private final List<LuminarCrystalShardProjectile> spawnedProjectiles = new ArrayList<>();
    private boolean isFullySpawned = false;
    private int currentFireIndex = 0;

    // Tipo de órbita sorteado para a execução atual
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
        this.projectileCount = countProvider.sample(this.holder.getRandom());

        // Escolhe o tipo de órbita aleatoriamente entre os 3 disponíveis
        OrbitType[] types = OrbitType.values();
        this.currentOrbitType = types[holder.getRandom().nextInt(types.length)];

        if (holder.level() instanceof ServerLevel level) {
            spawnCircleProjectiles(level);
        }
    }

    /**
     * Spawna todos os estilhaços de cristal ao redor/atrás da entidade de acordo com o padrão selecionado.
     */
    private void spawnCircleProjectiles(ServerLevel level) {
        for (int i = 0; i < projectileCount; i++) {
            Vec3 spawnPos = calculateProjectilePosition(i, 0);

            LuminarCrystalShardProjectile projectile = new LuminarCrystalShardProjectile(ChangedAddonEntities.LUMINAR_CRYSTAL_SHARD.get(), level);
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

        level.playSound(null, holder, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.HOSTILE, 1.0f, 1.2f);
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
     * Atualiza as posições flutuantes dos projéteis na formação enquanto estão carregando.
     */
    private void updateHoverPositions() {
        for (int i = currentFireIndex; i < spawnedProjectiles.size(); i++) {
            LuminarCrystalShardProjectile projectile = spawnedProjectiles.get(i);
            if (projectile != null && projectile.isAlive()) {
                Vec3 targetPos = calculateProjectilePosition(i, tick);
                projectile.setPos(targetPos.x, targetPos.y, targetPos.z);
                projectile.setDeltaMovement(Vec3.ZERO);
            }
        }
    }

    /**
     * Calcula a posição 3D exata do projétil baseado na OrbitType ativa.
     */
    private Vec3 calculateProjectilePosition(int index, int currentTick) {
        Vec3 headPos = holder.getEyePosition();

        if (this.currentOrbitType == OrbitType.HORIZONTAL_AROUND) {
            // Círculo horizontal completo (360 graus) ao redor da cabeça do mob
            double angleStep = (2 * Math.PI) / projectileCount;
            double angle = (index * angleStep) + (currentTick * 0.05);

            double xOffset = CIRCLE_RADIUS * Math.cos(angle);
            double zOffset = CIRCLE_RADIUS * Math.sin(angle);
            return headPos.add(xOffset, 0, zOffset);
        } else {
            // Arco de meia-lua vertical (0 a PI radianos) no topo para não atingir as pernas
            double angleStep = (projectileCount > 1) ? (Math.PI / (projectileCount - 1)) : 0;
            double baseAngle = index * angleStep;

            // Leve oscilação de flutuação em onda
            double angle = baseAngle + (Math.sin(currentTick * 0.1 + index) * 0.05);

            float yRot = holder.getYRot();
            float radYaw = yRot * Mth.DEG_TO_RAD;

            // Vetor lateral (Right Vector) com base na rotação do mob
            Vec3 rightVec = new Vec3(-Math.cos(radYaw), 0, -Math.sin(radYaw));
            Vec3 upVec = new Vec3(0, 1, 0);

            double offsetX = CIRCLE_RADIUS * Math.cos(angle);
            double offsetY = CIRCLE_RADIUS * Math.sin(angle);

            Vec3 arcCenter = headPos;

            if (this.currentOrbitType == OrbitType.VERTICAL_BEHIND) {
                // Desloca o arco para trás do mob
                Vec3 lookVec = Vec3.directionFromRotation(0, yRot);
                double behindOffset = holder.getBbWidth() + 0.8D;
                arcCenter = headPos.subtract(lookVec.scale(behindOffset));
            }
            // VERTICAL_CENTER usa headPos diretamente (mesmo X/Z do mob)

            return arcCenter
                    .add(rightVec.scale(offsetX))
                    .add(upVec.scale(offsetY));
        }
    }

    private void fireProjectileAtTarget(LuminarCrystalShardProjectile projectile, LivingEntity target) {
        if (holder.level() instanceof ServerLevel level) {
            holder.swing(InteractionHand.MAIN_HAND);

            Vec3 shootDir;
            if (target != null && !target.isDeadOrDying()) {
                shootDir = target.getEyePosition().subtract(projectile.position()).normalize();
            } else {
                shootDir = holder.getLookAngle();
            }

            // Desloca o ponto inicial do disparo para fora da bounding box para evitar auto-dano no disparo
            Vec3 safeFirePos = projectile.position();
            if (holder.getBoundingBox().inflate(0.3D).contains(safeFirePos)) {
                safeFirePos = safeFirePos.add(shootDir.scale(holder.getBbWidth() + 0.6D));
            }

            projectile.setPos(safeFirePos.x, safeFirePos.y, safeFirePos.z);
            projectile.setNoGravity(false);
            
            float inaccuracy = level.getDifficulty() == Difficulty.HARD ? 0.0f : Mth.nextFloat(holder.getRandom(), 0.5f, 1.0f);
            projectile.shoot(shootDir.x, shootDir.y, shootDir.z, 2.25f, inaccuracy);

            level.playSound(null, projectile.getX(), projectile.getY(), projectile.getZ(),
                    SoundEvents.AMETHYST_BLOCK_HIT, SoundSource.HOSTILE, 1.0f, 1.4f);
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
            LuminarCrystalShardProjectile projectile = spawnedProjectiles.get(i);
            if (projectile != null && projectile.isAlive()) {
                projectile.discard();
            }
        }
        spawnedProjectiles.clear();
    }
}