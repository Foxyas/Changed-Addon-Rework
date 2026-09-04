package net.foxyas.changedaddon.entity.ai.goals.exp9;

import net.foxyas.changedaddon.entity.ai.goals.IReactiveGoal;
import net.foxyas.changedaddon.entity.bosses.Experiment009BossEntity;
import net.foxyas.changedaddon.entity.bosses.Experiment009Entity;
import net.foxyas.changedaddon.util.DelayedTask;
import net.foxyas.changedaddon.util.ParticlesUtil;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

public class AoEThunderStrikeGoal extends CastingAttackGoal implements IReactiveGoal.ICancelOnDamageGoal, IAbilityGoal {
    public static final int FAIL_SAFE_TICKS = 600;
    protected final IntProvider cooldownProvider;
    protected final Experiment009Entity experiment009;
    protected final IntProvider damageProvider;
    protected final double jumpPower;
    protected final int duration; // ticks de duração do ataque
    public int cooldown = 0;
    protected int tickCounter;
    protected BlockPos groundPos;
    protected LivingEntity target;
    protected boolean canceled = false;

    public AoEThunderStrikeGoal(Experiment009Entity experiment009, IntProvider cooldownProvider, IntProvider damageProvider, double jumpPower, int duration) {
        this.experiment009 = experiment009;
        this.damageProvider = damageProvider;
        this.jumpPower = jumpPower;
        this.duration = duration;
        this.cooldownProvider = cooldownProvider;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
    }

    @Override
    public boolean isInterruptable() {
        return tickCounter >= FAIL_SAFE_TICKS;
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }
        LivingEntity target = experiment009.getTarget();
        return target != null && target.isAlive() && experiment009.onGround();
    }

    @Override
    public void start() {
        this.experiment009.invulnerableTime = 10;
        this.target = experiment009.getTarget();
        this.groundPos = experiment009.blockPosition();
        this.tickCounter = 0;
        this.setCanceledTo(false);

        // Lança a entidade para cima
        Vec3 velocity = experiment009.position().vectorTo(target.position()).normalize().scale(0.5f);
        experiment009.setDeltaMovement(experiment009.getDeltaMovement().add(velocity.x, jumpPower, velocity.z));
        ChangedSounds.broadcastSound(experiment009, ChangedSounds.CARDBOARD_BOX_OPEN, 1, 1);


        // Slow falling para manter no ar
        experiment009.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, duration + 40, 10, false, false));

        // Evita que tente andar
        experiment009.getNavigation().stop();
        if (experiment009 instanceof Experiment009BossEntity experiment009BossEntity) {
            experiment009BossEntity.setCastingAttack(true);
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (target instanceof Player player) {
            if (player.isCreative() || player.isSpectator()) {
                return false;
            }
        }

        if (this.isCanceled()) {
            return false;
        }


        return target != null && target.isAlive() && tickCounter < duration;
    }

    @Override
    public void tick() {
        tickCounter++;

        Vec3 lookAt = target != null ? target.getEyePosition() : experiment009.getEyePosition().add(0, 1, 0);
        Vec3 deltaMovement = experiment009.getDeltaMovement();
        experiment009.setDeltaMovement(deltaMovement.x, Math.max(0, deltaMovement.y), deltaMovement.z);
        experiment009.getLookControl().setLookAt(lookAt.x, lookAt.y, lookAt.z, 90, 90);
        //pathfinderMob.getNavigation().stop();
        if (experiment009 instanceof Experiment009BossEntity experiment009BossEntity) {
            experiment009BossEntity.setCastingAttack(true);
            experiment009BossEntity.setCastingTicks(0);
        }
        if (tickCounter % 10 != 0) return;
        thunderStorm();
        //pathfinderMob.swing(pathfinderMob.isLeftHanded() ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
        experiment009.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, duration + 40, 10, false, false));

    }

    protected void thunderStorm() {
        Level level = experiment009.level;
        if (!(level instanceof ServerLevel serverLevel)) return;

        boolean hasTarget = experiment009.getTarget() != null;
        int count = hasTarget ? 12 : 7;
        double range = hasTarget ? 10.0 : 20.0;

        for (int i = 0; i < count; i++) {
            double offsetX = experiment009.getRandom().nextGaussian() * range;
            double offsetZ = experiment009.getRandom().nextGaussian() * range;

            int targetX = Mth.floor(experiment009.getX() + offsetX);
            int targetZ = Mth.floor(experiment009.getZ() + offsetZ);

            // Começamos a busca a partir da altura do mob + 5 blocos (para pegar tetos baixos)
            // ou você pode usar serverLevel.getHeight() como ponto de partida se quiser que venha do céu.
            int startY = Mth.floor(experiment009.getY() + 5);
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(targetX, startY, targetZ);

            // Check manual descendo até achar o primeiro bloco sólido (o "teto" ou o "chão")
            // O limite de 20 blocos para baixo evita loops infinitos no vazio
            boolean foundSurface = false;
            for (int y = startY; y > startY - 20; y--) {
                mutablePos.setY(y);
                if (!level.getBlockState(mutablePos).isAir()) {
                    // Achamos um bloco sólido (pode ser o teto ou o chão)
                    foundSurface = true;
                    break;
                }
            }

            if (foundSurface) {
                // O raio spawna exatamente no bloco encontrado
                spawnThunderBolt(mutablePos.immutable());
            }
        }
    }

    public void spawnThunderBolt(BlockPos pos) {
        Level level = experiment009.level();
        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
        if (lightning == null) return;
        lightning.moveTo(pos.getX(), pos.getY(), pos.getZ());
        lightning.setCause((ServerPlayer) experiment009.getUnderlyingPlayer());

        List<BlockPos> conductiveBlocks = findConductiveBlocks(experiment009.level(), lightning.getOnPos(), 16);
        if (!conductiveBlocks.isEmpty()) {
            BlockPos random = Util.getRandom(conductiveBlocks, experiment009.getRandom());
            lightning.moveTo(random, 0, 0);
        }

        if (experiment009 instanceof Experiment009BossEntity boss) {
            lightning.setDamage(damageProvider.sample(experiment009.getRandom()) * boss.getPhase().getDamageModifier(target));
        } else lightning.setDamage(damageProvider.sample(experiment009.getRandom()));

        lightning.setVisualOnly(Experiment009BossEntity.getMetalPercentage(target) <= 0.4f || Experiment009BossEntity.shouldAlwaysDamageEntity(target));


        level.addFreshEntity(lightning);
        ParticlesUtil.sendParticles(level, ParticleTypes.ELECTRIC_SPARK, pos, 0.3f, 0.5f, 0.3f, 5, 1f);
        applyKnockBack(lightning);
    }

    protected void oldSpawn(LightningBolt lightning) {
        DelayedTask delayedTask = DelayedTask.schedule(20, () -> {
            experiment009.level().addFreshEntity(lightning);
            applyKnockBack(lightning);
            experiment009.swing(experiment009.isLeftHanded() ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
            // recoil de knockback para trás
//            if (target != null) {
//                Vec3 dir = pathfinderMob.position().vectorTo(target.position()).normalize().scale(-0.5);
//                pathfinderMob.push(dir.x, dir.y * 1.25f, dir.z);
//            }
        });
    }

    protected boolean isConductive(BlockState state) {
        Block block = state.getBlock();

        return state.is(Tags.Blocks.STORAGE_BLOCKS_COPPER)
                || block == Blocks.EXPOSED_COPPER
                || block == Blocks.WEATHERED_COPPER
                || block == Blocks.OXIDIZED_COPPER
                || block == Blocks.CUT_COPPER
                || state.is(Tags.Blocks.STORAGE_BLOCKS_IRON)
                || state.is(Tags.Blocks.STORAGE_BLOCKS_GOLD)
                || block == Blocks.LIGHTNING_ROD;
    }

    protected List<BlockPos> findConductiveBlocks(Level level, BlockPos center, int radius) {

        List<BlockPos> result = new ArrayList<>();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -2; y <= 2; y++) {
                for (int z = -radius; z <= radius; z++) {

                    BlockPos pos = center.offset(x, y, z);
                    BlockState state = level.getBlockState(pos);

                    if (isConductive(state)) {
                        result.add(pos.immutable());
                    }
                }
            }
        }

        return result;
    }

    public void applyKnockBack(LightningBolt lightning) {
        var list = lightning.getHitEntities().map(e -> e instanceof LivingEntity livingEntity ? livingEntity : null).filter(Objects::nonNull).toList();
//                lightning.level
//                .getEntitiesOfClass(
//                        LivingEntity.class,
//                        getBoundingBoxFromLightningBolt(lightning),
//                        (target) -> !target.is(lightning) && !target.is(experiment009) && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)
//                );

        for (LivingEntity livingEntity : list) {
            Vec3 pushForce = livingEntity.position().subtract(lightning.position()).normalize().scale(0.25f).add(0, livingEntity.onGround() ? 0.25f : 0.05f, 0);
            if (!livingEntity.isBlocking()) {
                if (livingEntity instanceof ServerPlayer serverPlayer) {
                    serverPlayer.push(pushForce.x(), pushForce.y(), pushForce.z());
                    serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(
                            serverPlayer.getId(),
                            serverPlayer.getDeltaMovement())
                    );
                } else {
                    livingEntity.push(pushForce.x(), pushForce.y(), pushForce.z());
                }

                livingEntity.hasImpulse = true;
            }
        }
    }

    public AABB getBoundingBoxFromLightningBolt(LightningBolt bolt) {
        return new AABB(bolt.getX() - 3.0D, bolt.getY() - 3.0D, bolt.getZ() - 3.0D, bolt.getX() + 3.0D, bolt.getY() + 6.0D + 3.0D, bolt.getZ() + 3.0D);
    }

    @Override
    public void stop() {
        if (groundPos != null) {
            experiment009.teleportTo(groundPos.getX() + 0.5, groundPos.getY(), groundPos.getZ() + 0.5);
            if (!experiment009.level().isClientSide()) {
                LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(experiment009.level());
                if (lightning != null) {
                    lightning.moveTo(groundPos.getX() + 0.5, groundPos.getY(), groundPos.getZ() + 0.5);
                    lightning.setCause((ServerPlayer) experiment009.getUnderlyingPlayer());
                    experiment009.level().addFreshEntity(lightning);
                }
            }
        }

        // Remove slow falling
        experiment009.removeEffect(MobEffects.SLOW_FALLING);

        this.target = null;
        this.groundPos = null;
        this.cooldown = cooldownProvider.sample(this.experiment009.getRandom());
        if (experiment009 instanceof Experiment009BossEntity experiment009BossEntity) {
            experiment009BossEntity.setCastingAttack(false);
            experiment009BossEntity.setCastingTicks(0);
        }
        this.tickCounter = 0;
        this.setCanceledTo(false);
    }

    @Override
    public void onHurt(LivingEntity livingEntity, @NotNull DamageSource pDamageSource, float pDamageAmount) {
        if (pDamageAmount >= 8 && tickCounter >= 10 && experiment009.hurtTime > 0) {
            if (pDamageSource.getDirectEntity() instanceof LightningBolt) {
                return;
            }
            ICancelOnDamageGoal.super.onHurt(livingEntity, pDamageSource, pDamageAmount);
        }
    }

    @Override
    public void onDamage(LivingEntity livingEntity, @NotNull DamageSource pDamageSource, float amount, boolean willCauseDamage) {

    }

    @Override
    public void onHeal(LivingEntity livingEntity, float amount) {

    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public void setCanceledTo(boolean canceled) {
        this.canceled = true;
    }
}
