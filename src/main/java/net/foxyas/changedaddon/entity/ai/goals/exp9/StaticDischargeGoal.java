package net.foxyas.changedaddon.entity.ai.goals.exp9;

import net.foxyas.changedaddon.entity.bosses.Experiment009BossEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.minecraft.tags.BlockTags.FIRE;

@ParametersAreNonnullByDefault
public class StaticDischargeGoal extends Goal {

    protected final PathfinderMob holder;
    protected final IntProvider cooldownProvider;
    protected final float closeEnough;
    protected final float closeEnoughSqr;
    protected final IntProvider castDurationProvider;
    protected final float aoe;
    protected final float aoeSqr;
    protected final FloatProvider damageProvider;

    protected LivingEntity target;
    protected int cooldown;
    protected int castDuration;
    protected boolean triggered;

    public StaticDischargeGoal(PathfinderMob holder, IntProvider cooldown, float closeEnough, IntProvider castDuration, float aoe, FloatProvider damage) {
        this.holder = holder;
        cooldownProvider = cooldown;
        assert closeEnough < aoe;
        this.closeEnough = closeEnough;
        castDurationProvider = castDuration;
        closeEnoughSqr = closeEnough * closeEnough;
        this.aoe = aoe;
        aoeSqr = aoe * aoe;
        damageProvider = damage;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
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
        return target != null && target.isAlive() && target.distanceToSqr(holder) <= closeEnoughSqr;
    }

    @Override
    public boolean canContinueToUse() {
        return castDuration > 0 && target.isAlive();
    }

    @Override
    public void start() {
        castDuration = castDurationProvider.sample(holder.getRandom());
        triggered = false;
        holder.level.playSound(null, holder.getX(), holder.getY(), holder.getZ(), SoundEvents.BEACON_ACTIVATE, SoundSource.MASTER, 100.0F, 0.8F + holder.getRandom().nextFloat() * 0.2F);
    }

    @Override
    public void tick() {
        if (castDuration > 0) {
            castDuration--;
            if (holder instanceof Experiment009BossEntity exp9) {
                exp9.setCastingAttack(castDuration > 0);
            }
            holder.setDeltaMovement(Vec3.ZERO);
            return;
        }

        float delta = 1 - Mth.inverseLerp(castDuration, 0, castDurationProvider.getMaxValue());
        float size = Mth.lerp(delta, 0.1f, 1);
        float doubleSize = size * 2;

        ((ServerLevel) holder.level).sendParticles(ParticleTypes.ELECTRIC_SPARK,
                holder.getX() - size, holder.getY() - size + holder.getBbHeight() / 2, holder.getZ() - size,
                Math.round(Mth.lerp(delta, 20, 160)),
                doubleSize, doubleSize, doubleSize, 0.2);

        if (delta >= 0.9f && !triggered) {
            AABB aabb = AABB.ofSize(holder.position(), aoe * 2, aoe * 2, aoe * 2);
            ServerLevel level = (ServerLevel) holder.level;
            RandomSource random = holder.getRandom();
            BlockPos bossPos = holder.blockPosition();
            for (BlockPos blockPos : BlockPos.betweenClosedStream(bossPos.offset(-16, -16, -16), bossPos.offset(16, 16, 16)).map(BlockPos::immutable).filter(pos -> level.getBlockState(pos).is(FIRE)).toList()) {
                level.removeBlock(blockPos, false);
                level.levelEvent(1009, blockPos, 0);
            }

            level.getEntities(holder, aabb, entity -> entity.distanceToSqr(holder) <= aoeSqr).forEach(entity -> {
                        entity.hurt(target.level().damageSources().lightningBolt(), damageProvider.sample(random));
                        Vec3 direction = entity.position().subtract(holder.position());

                        direction = direction.normalize();

                        float strength = 3 / entity.distanceTo(holder);

                        entity.push(
                                direction.x * strength,
                                direction.y * 0.3f,
                                direction.z * strength
                        );
                        if (entity instanceof ServerPlayer serverPlayer) {
                            serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(
                                    serverPlayer.getId(),
                                    serverPlayer.getDeltaMovement())
                            );
                        }
                    }
            );
            level.playSound(null, holder.getX(), holder.getY(), holder.getZ(), SoundEvents.BEACON_DEACTIVATE, SoundSource.MASTER, 1000.0F, 0.8F + random.nextFloat() * 0.2F);
            triggered = true;
        }
    }


    @Override
    public void stop() {
        target = null;
        cooldown = cooldownProvider.sample(holder.getRandom());
        castDuration = 0;
        if (holder instanceof Experiment009BossEntity exp9) {
            exp9.setCastingAttack(false);
        }
    }
}
