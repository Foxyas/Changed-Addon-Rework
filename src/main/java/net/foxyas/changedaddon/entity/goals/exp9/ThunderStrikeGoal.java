package net.foxyas.changedaddon.entity.goals.exp9;

import net.foxyas.changedaddon.init.ChangedAddonParticleTypes;
import net.foxyas.changedaddon.util.DelayedTask;
import net.foxyas.changedaddon.util.ParticlesUtil;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class ThunderStrikeGoal extends Goal {
    protected final IntProvider cooldownProvider;
    private final PathfinderMob pathfinderMob;
    private final IntProvider damageProvider;
    private final double jumpPower;
    private final int duration; // ticks de duração do ataque
    public int cooldown = 0;
    private int tickCounter;
    private BlockPos groundPos;
    private LivingEntity target;

    public ThunderStrikeGoal(PathfinderMob pathfinderMob, IntProvider cooldownProvider, IntProvider damageProvider, double jumpPower, int duration) {
        this.pathfinderMob = pathfinderMob;
        this.damageProvider = damageProvider;
        this.jumpPower = jumpPower;
        this.duration = duration;
        this.cooldownProvider = cooldownProvider;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }
        LivingEntity target = pathfinderMob.getTarget();
        return target != null && target.isAlive() && pathfinderMob.onGround();
    }

    @Override
    public void start() {
        this.target = pathfinderMob.getTarget();
        this.groundPos = pathfinderMob.blockPosition();
        this.tickCounter = 0;

        // Lança a entidade para cima
        Vec3 velocity = pathfinderMob.position().vectorTo(target.position()).normalize().scale(0.5f);
        pathfinderMob.setDeltaMovement(pathfinderMob.getDeltaMovement().add(velocity.x, jumpPower, velocity.z));
        ChangedSounds.broadcastSound(pathfinderMob, ChangedSounds.CARDBOARD_BOX_OPEN, 1, 1);


        // Slow falling para manter no ar
        pathfinderMob.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, duration + 40, 10, false, false));

        // Evita que tente andar
        pathfinderMob.getNavigation().stop();
    }

    @Override
    public boolean canContinueToUse() {
        return target != null && target.isAlive() && tickCounter < duration;
    }

    @Override
    public void tick() {
        tickCounter++;

        if (target != null) {
            // olha para o alvo
            pathfinderMob.getLookControl().setLookAt(target, 30.0F, 30.0F);
            pathfinderMob.getNavigation().stop();

            if (tickCounter % 10 == 0) { // a cada 1/2s lança um raio
                LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(pathfinderMob.level());
                if (lightning != null) {
                    lightning.setVisualOnly(true);
                    lightning.moveTo(target.position());
                    if (pathfinderMob instanceof ChangedEntity changedEntity) {
                        lightning.setCause((ServerPlayer) changedEntity.getUnderlyingPlayer());
                    }

                    List<BlockPos> conductiveBlocks = findConductiveBlocks(pathfinderMob.level(), lightning.getOnPos(), 4);
                    if (!conductiveBlocks.isEmpty()) {
                        BlockPos random = Util.getRandom(conductiveBlocks, pathfinderMob.getRandom());
                        lightning.moveTo(random, 0, 0);
                    }

                    lightning.setDamage(damageProvider.sample(pathfinderMob.getRandom()));
                    ParticlesUtil.sendParticles(pathfinderMob.level(), ChangedAddonParticleTypes.thunderSpark(5), lightning.getEyePosition(), 0.3f, 0.3f, 0.3f, 25, 0.25f);
                    DelayedTask.schedule(10, () -> {
                        pathfinderMob.level().addFreshEntity(lightning);
                        applyKnockBack(lightning);
                        pathfinderMob.swing(pathfinderMob.isLeftHanded() ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
                        // recoil de knockback para trás
                        if (target != null) {
                            Vec3 dir = pathfinderMob.position().vectorTo(target.position()).normalize().scale(-0.5);
                            pathfinderMob.push(dir.x, dir.y * 1.25f, dir.z);
                        }
                    });
                    pathfinderMob.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, duration + 40, 10, false, false));
                }
            }
        }
    }

    protected boolean isConductive(BlockState state) {
        Block block = state.getBlock();

        return block == Blocks.COPPER_BLOCK
                || block == Blocks.EXPOSED_COPPER
                || block == Blocks.WEATHERED_COPPER
                || block == Blocks.OXIDIZED_COPPER
                || block == Blocks.CUT_COPPER
                || block == Blocks.IRON_BLOCK
                || block == Blocks.GOLD_BLOCK
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
        var list = lightning.level
                .getEntitiesOfClass(
                        LivingEntity.class,
                        getBoundingBoxFromLightningBolt(lightning).inflate(8),
                        (target) -> !target.is(lightning) && !target.is(pathfinderMob)
                );

        for (LivingEntity livingEntity : list) {
            Vec3 pushForce = livingEntity.position().subtract(lightning.position()).normalize().scale(0.75f).multiply(1f, 1.75f, 1f);
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
            }
        }
    }

    public AABB getBoundingBoxFromLightningBolt(LightningBolt bolt) {
        return new AABB(bolt.getX() - 15.0D, bolt.getY() - 15.0D, bolt.getZ() - 15.0D, bolt.getX() + 15.0D, bolt.getY() + 6.0D + 15.0D, bolt.getZ() + 15.0D);
    }

    @Override
    public void stop() {
        if (groundPos != null) {
            pathfinderMob.teleportTo(groundPos.getX() + 0.5, groundPos.getY(), groundPos.getZ() + 0.5);
            LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(pathfinderMob.level());
            if (lightning != null) {
                lightning.moveTo(groundPos.getX() + 0.5, groundPos.getY(), groundPos.getZ() + 0.5);
                if (pathfinderMob instanceof ChangedEntity changedEntity) {
                    lightning.setCause((ServerPlayer) changedEntity.getUnderlyingPlayer());
                }
                pathfinderMob.level().addFreshEntity(lightning);
            }
        }

        // Remove slow falling
        pathfinderMob.removeEffect(MobEffects.SLOW_FALLING);

        this.target = null;
        this.groundPos = null;
        this.cooldown = cooldownProvider.sample(this.pathfinderMob.getRandom());
    }
}
