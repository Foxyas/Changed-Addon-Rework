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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class HyperGigaCrystalAttackGoal extends Goal implements IAbilityGoal {

    public final Mob holder;
    public final float distance;
    protected final IntProvider cooldownProvider;

    public int cooldown;
    public int tick;

    // Configurações do Ataque Hiper Giga
    private static final double ORBIT_RADIUS = 2.0;
    private static final int CHARGE_DURATION = 60; // 3 segundos inteiros de canalização épica
    private LuminarCrystalShardProjectile hyperCrystal = null;

    public HyperGigaCrystalAttackGoal(Mob holder, IntProvider cooldownProvider) {
        this(holder, cooldownProvider, 25f);
    }

    public HyperGigaCrystalAttackGoal(Mob holder, IntProvider cooldownProvider, float distance) {
        super();
        this.holder = holder;
        this.cooldownProvider = cooldownProvider;
        this.distance = distance;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = holder.getTarget();
        if (target == null || !target.isAlive()) {
            return false;
        }
        if (this.cooldown > 0) {
            this.cooldown--;
            return false;
        }

        // Chance menor de ativar por ser um ataque ultimate/especial pesado
        return holder.distanceToSqr(target) >= distance && holder.getRandom().nextFloat() < 0.15f;
    }

    @Override
    public void start() {
        super.start();
        this.tick = 0;

        if (holder.level() instanceof ServerLevel level) {
            // Spawn do Hyper Giga Projétil central flutuante
            Vec3 spawnPos = holder.getEyePosition(0).add(0, ORBIT_RADIUS, 0);
            this.hyperCrystal = createHyperCrystal(level, spawnPos);
            level.addFreshEntity(this.hyperCrystal);

            // Som de início de canalização pesada (ex: Wither spawn / Warden charge / Beacons)
            level.playSound(null, holder, SoundEvents.BEACON_ACTIVATE, SoundSource.HOSTILE, 1.0f, 0.5f);
            level.playSound(null, holder, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.HOSTILE, 1.5f, 0.3f);
        }
    }

    protected @NotNull LuminarCrystalShardProjectile createHyperCrystal(ServerLevel level, Vec3 spawnPos) {
        RandomSource random = holder.getRandom();

        LuminarCrystalShardProjectile projectile = new LuminarCrystalShardProjectile(ChangedAddonEntities.LUMINAR_CRYSTAL_SHARD.get(), level) {
            @Override
            protected boolean canHitEntity(@NotNull Entity entity) {
                if (entity.is(holder)) return false;
                return super.canHitEntity(entity);
            }

            @Override
            protected float getGravity() {
                return 0.005f; // Absolutamente linha reta, sem queda de gravidade durante o tiro principal
            }

            @Override
            public void shoot(double pX, double pY, double pZ, float pVelocity, float pInaccuracy) {
                super.shoot(pX, pY, pZ, pVelocity, pInaccuracy);
                this.setNoGravity(true);
            }

            @Override
            public boolean shouldBreakOnImpact(BlockHitResult hitResult) {
                return true;
            }

            @Override
            public void breakOnImpact(BlockHitResult hitResult) {
                super.breakOnImpact(hitResult);
                // Explosão de partículas massiva ao impactar
                if (this.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 2, 0, 0, 0, 1);
                    ParticlesUtil.sendParticles(serverLevel, new BlockParticleOption(ParticleTypes.BLOCK, ChangedAddonBlocks.LUMINAR_CRYSTAL_SMALL.get().defaultBlockState()), this.position(), 0.2f, 0.2f, 0.2f, 30, 1.0f);
                }
            }
        };

        projectile.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
        projectile.setOwner(holder);
        projectile.setNoGravity(true);
        projectile.setCritArrow(true);
        projectile.setKnockback(4); // Knockback brutal
        projectile.setShouldSpawnCrystalsNearby(true);
        projectile.setBreakOnImpact(true);

        // Dano altíssimo característico de um Hyper Giga Ataque
        projectile.setBaseDamage(16.0f);
        projectile.setPierceLevel((byte) 5); // Perfura múltiplos alvos pelo caminho

        return projectile;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        tick++;

        // Trava e desacelera o mob durante o carregamento do Hyper Ataque
        PathNavigation navigation = holder.getNavigation();
        if (navigation.getPath() != null && navigation.isInProgress()) {
            holder.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 3, true, false, false));
        }

        LivingEntity target = holder.getTarget();
        if (target != null) {
            holder.getLookControl().setLookAt(target, 30f, 30f);
        }

        // Atualiza a posição de órbita do Hyper Crystal rodando ao redor da cabeça do mob enquanto carrega
        if (hyperCrystal != null && hyperCrystal.isAlive() && tick < CHARGE_DURATION) {
            Vec3 headPos = holder.getEyePosition(0);
            double angle = tick * 0.15D; // Velocidade de rotação da órbita aumentando
            double x = headPos.x + (Math.cos(angle) * ORBIT_RADIUS);
            double z = headPos.z + (Math.sin(angle) * ORBIT_RADIUS);
            double y = headPos.y + 1.0D + (Math.sin(tick * 0.2D) * 0.2D); // Flutuação vertical leve

            hyperCrystal.setPos(x, y, z);
            hyperCrystal.setDeltaMovement(Vec3.ZERO);
            hyperCrystal.hasImpulse = true;

            if (target != null) {
                EntityUtil.makeEntityForceFaceAt(hyperCrystal, target.position());
            }

            hyperCrystal.setScale(Math.max(1, 8 * ((float) tick / CHARGE_DURATION)));

            if (holder.level() instanceof ServerLevel serverLevel && tick % 4 == 0) {
                serverLevel.sendParticles(ParticleTypes.END_ROD, x, y, z, 2, 0.1, 0.1, 0.1, 0.02);
                serverLevel.sendParticles(ParticleTypes.ENCHANT, headPos.x, headPos.y, headPos.z, 5, 0.5, 0.5, 0.5, 0.1);
            }
        }

        if (tick == CHARGE_DURATION && hyperCrystal != null && hyperCrystal.isAlive()) {
            fireHyperCrystal(target);
        }
    }

    private void fireHyperCrystal(LivingEntity target) {
        if (!(holder.level() instanceof ServerLevel level)) return;

        Vec3 startPos = hyperCrystal.position();
        Vec3 shootDir;

        if (target != null && !target.isDeadOrDying()) {
            shootDir = target.getEyePosition().subtract(startPos).normalize();
        } else {
            shootDir = holder.getLookAngle();
        }

        // Som de disparo massivo e único
        level.playSound(null, holder.getX(), holder.getY(), holder.getZ(), SoundEvents.WARDEN_SONIC_BOOM, SoundSource.HOSTILE, 1.2f, 0.8f);
        holder.swing(InteractionHand.MAIN_HAND);

        // Lança o Hyper Crystal em alta velocidade direto no alvo
        float inaccuracy = level.getDifficulty() == Difficulty.HARD ? 0.0f : 0.2f;
        hyperCrystal.shoot(shootDir.x, shootDir.y, shootDir.z, 3.0f, inaccuracy);
        hyperCrystal.hasImpulse = true;

        // Libera a referência para o goal saber que o projétil já foi disparado
        hyperCrystal = null;
    }

    @Override
    public boolean canContinueToUse() {
        // Continua rodando até disparar o projétil e o tempo limite passar
        return tick <= CHARGE_DURATION + 10;
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public void stop() {
        super.stop();
        // Cooldown longo para um ataque tão poderoso (ex: 8 a 12 segundos)
        this.cooldown = cooldownProvider.sample(this.holder.getRandom());

        // Se o mob for interrompido antes de disparar, o cristal some de forma segura
        if (hyperCrystal != null && hyperCrystal.isAlive()) {
            hyperCrystal.discard();
            hyperCrystal = null;
        }
    }
}