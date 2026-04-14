package net.foxyas.changedaddon.entity.ai.goals.exp9;

import net.foxyas.changedaddon.entity.bosses.Experiment009BossEntity;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class SummonLightningGoal extends Goal {

    protected final PathfinderMob holder;
    protected final IntProvider cooldownProvider;
    protected final IntProvider lightningCountProvider;
    protected final IntProvider castDurationProvider;
    protected final IntProvider lightningDelayProvider;
    protected final FloatProvider damageProvider;

    protected LivingEntity target;
    protected int cooldown;
    protected int lightnings;
    protected int castDuration;
    protected int lightningDelay;
    protected Vec3 strikePos;
    protected BlockPos aboveWaterPos;

    public SummonLightningGoal(PathfinderMob holder, IntProvider cooldown, IntProvider lightningCount, IntProvider castDuration, IntProvider lightningDelay, FloatProvider damage) {
        this.holder = holder;
        cooldownProvider = cooldown;
        assert lightningCount.getMinValue() >= 1;
        lightningCountProvider = lightningCount;
        castDurationProvider = castDuration;
        lightningDelayProvider = lightningDelay;
        damageProvider = damage;
    }

    public static void lightning(ServerLevel level, double x, double y, double z, float damage) {
        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
        assert lightning != null;
        lightning.moveTo(x, y, z);
        List<BlockPos> conductiveBlocks = findConductiveBlocks(level, lightning.getOnPos(), 16);
        if (!conductiveBlocks.isEmpty()) {
            BlockPos random = Util.getRandom(conductiveBlocks, level.getRandom());
            lightning.moveTo(random, 0, 0);
        }
        if (damage > 0) {
            lightning.setDamage(damage);
        } else lightning.setVisualOnly(true);
        level.addFreshEntity(lightning);
    }

    protected static boolean isConductive(BlockState state) {
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

    protected static List<BlockPos> findConductiveBlocks(Level level, BlockPos center, int radius) {

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

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }

        target = holder.getTarget();
        return target != null && target.isAlive() && target.onGround();
    }

    @Override
    public boolean canContinueToUse() {
        if (target instanceof Player player) {
            if (player.isCreative() || player.isSpectator()) {
                return false;
            }
        }


        return lightnings > 0 && target.isAlive();
    }

    @Override
    public void start() {
        lightnings = lightningCountProvider.sample(holder.getRandom());
        castDuration = castDurationProvider.sample(holder.getRandom());
        if (holder instanceof Experiment009BossEntity boss) {
            castDuration *= (int) boss.getPhase().getCastModifier();
        }
        holder.level.playSound(null, holder.getX(), holder.getY(), holder.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 1, 1);
        holder.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (lightnings <= 0) return;

        if (!(holder.level instanceof ServerLevel level)) {
            return;
        }

        if (castDuration > 0) {
            castDuration--;

            holder.setDeltaMovement(Vec3.ZERO);
            if (target == null) return;
            //holder.getLookControl().setLookAt(target, 90f, 90f);
            //holder.setYBodyRot(holder.yHeadRot);

            if (holder.tickCount % 2 == 0) {
                level.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                        holder.getX() - 0.5, holder.getY(), holder.getZ() - 0.5,
                        25, 1, 0.1, 1, 0.5);
            }

            if (holder instanceof Experiment009BossEntity exp9) {
                exp9.setCastingAttack(castDuration > 0);
            }

            return;
        }

        RandomSource random = holder.getRandom();
        if (strikePos == null) {
            strikePos = target.position();
            BlockPos pos = new BlockPos(new Vec3i((int) strikePos.x, (int) strikePos.y, (int) strikePos.z));
            if (level.getBlockState(pos).is(Blocks.WATER)) {
                do pos = pos.above();
                while (level.getBlockState(pos).is(Blocks.WATER));
                aboveWaterPos = pos;
            }

            lightningDelay = lightningDelayProvider.sample(random);
        }

        if (lightningDelay > 0) {
            lightningDelay--;

            int gameTime = holder.tickCount;
            if (gameTime % 2 == 0) {
                level.sendParticles(ParticleTypes.ELECTRIC_SPARK, strikePos.x - 1, aboveWaterPos != null ? aboveWaterPos.getY() : strikePos.y, strikePos.z - 1,
                        50, 2, 0.2, 2, 0.5);
            }
            if ((gameTime + 10) % 40 == 0)
                level.playSound(null, strikePos.x, strikePos.y, strikePos.z, SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 0.5f, 1);
            return;
        }

        lightnings--;
        lightning(level, strikePos.x, strikePos.y, strikePos.z, damageProvider.sample(random));
        lightning(level, strikePos.x + 0.75, strikePos.y, strikePos.z + 0.75, damageProvider.sample(random));
        lightning(level, strikePos.x + 0.75, strikePos.y, strikePos.z - 0.75, damageProvider.sample(random));
        lightning(level, strikePos.x - 0.75, strikePos.y, strikePos.z - 0.75, damageProvider.sample(random));
        lightning(level, strikePos.x - 0.75, strikePos.y, strikePos.z + 0.75, damageProvider.sample(random));

        applyKnockBack(AABB.ofSize(strikePos, 16, 16, 16));

        strikePos = null;
        aboveWaterPos = null;
    }

    public void applyKnockBack(AABB hitbox) {
        var list = holder.level()
                .getEntitiesOfClass(
                        LivingEntity.class,
                        hitbox,
                        (target -> !target.is(holder))
                );

        Vec3 direction;
        for (LivingEntity livingEntity : list) {
            direction = livingEntity.position().subtract(strikePos).normalize();

            float strength = 6f / (float) Math.sqrt(livingEntity.distanceToSqr(strikePos));

            livingEntity.push(
                    direction.x * strength,
                    Math.min(Math.max(direction.y, 0.1) * strength * 2, 4f),
                    direction.z * strength
            );

            if (livingEntity instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(
                        serverPlayer.getId(),
                        serverPlayer.getDeltaMovement())
                );
            }
        }
    }

    @Override
    public void stop() {
        target = null;
        cooldown = cooldownProvider.sample(holder.getRandom());
        lightnings = 0;
        strikePos = null;
        aboveWaterPos = null;
        if (holder instanceof Experiment009BossEntity exp9) {
            exp9.setCastingAttack(false);
        }
    }
}
