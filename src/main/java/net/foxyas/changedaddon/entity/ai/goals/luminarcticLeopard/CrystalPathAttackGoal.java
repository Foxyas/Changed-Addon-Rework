package net.foxyas.changedaddon.entity.ai.goals.luminarcticLeopard;

import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.foxyas.changedaddon.block.LuminarCrystalSmall.CAN_SURVIVE_ANYWHERE;
import static net.foxyas.changedaddon.block.LuminarCrystalSmall.FACING;

/**
 * Crystal path attack.
 * <p>
 * The holder channels for a moment and raises a straight line of crystals toward its target.
 * The crystals stay in place for {@code lifetimeTicks} and hurt any living entity that touches
 * them (except the holder). When they expire, or the goal is interrupted, they are removed.
 */
public class CrystalPathAttackGoal extends Goal {

    private static final double SPACING = 1.0D;       // blocks between crystals
    private static final int STEP_INTERVAL = 2;       // ticks between each crystal being raised
    private static final int HIT_COOLDOWN = 20;       // ticks before the same entity can be hit again
    private static final int MAX_STEP_UP = 2;         // how much terrain the path can climb per crystal
    private static final int MAX_STEP_DOWN = 4;       // how far the path will look down for ground

    private record CrystalNode(BlockPos pos, long expireTime) {
    }

    private final Mob holder;
    private final IntProvider cooldownProvider;
    private final IntProvider lengthProvider;
    private final double minDistanceSqr;
    private final FloatProvider damageProvider;
    private final int lifetimeTicks;

    private final List<CrystalNode> nodes = new ArrayList<>();
    private final Map<UUID, Long> lastHit = new HashMap<>();

    private long nextUseTime = 0;
    private long nextStepTime = 0;
    private int totalSteps = 0;
    private int stepsDone = 0;
    private Vec3 startPos = Vec3.ZERO;
    private Vec3 direction = Vec3.ZERO;

    /**
     * @param cooldownProvider ticks to wait after the attack fully ends before it can be used again
     * @param lengthProvider   how many crystals the path has
     * @param minDistance      the target must be at least this far (in blocks) for the attack to start
     * @param damageProvider   damage dealt when an entity touches a crystal
     * @param lifetimeTicks    how long each crystal stays before disappearing
     */
    public CrystalPathAttackGoal(Mob holder, IntProvider cooldownProvider, IntProvider lengthProvider, double minDistance, FloatProvider damageProvider, int lifetimeTicks) {
        this.holder = holder;
        this.cooldownProvider = cooldownProvider;
        this.lengthProvider = lengthProvider;
        this.minDistanceSqr = minDistance * minDistance;
        this.damageProvider = damageProvider;
        this.lifetimeTicks = lifetimeTicks;
        // No flags: after the path is cast the holder is free to keep fighting while the crystals linger.
    }

    // ------------------------------------------------------------------
    // Goal lifecycle
    // ------------------------------------------------------------------

    @Override
    public boolean canUse() {
        if (holder.level().getGameTime() < nextUseTime) {
            return false;
        }
        LivingEntity target = holder.getTarget();
        if (target == null || !target.isAlive() || !holder.getSensing().hasLineOfSight(target)) {
            return false;
        }
        return holder.distanceToSqr(target) >= minDistanceSqr && holder.getRandom().nextFloat() < 0.30f;
    }

    @Override
    public boolean canContinueToUse() {
        return stepsDone < totalSteps || !nodes.isEmpty();
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
    public void start() {
        this.nodes.clear();
        this.lastHit.clear();
        this.stepsDone = 0;
        this.totalSteps = Math.max(1, lengthProvider.sample(holder.getRandom()));
        this.nextStepTime = holder.level().getGameTime();
        this.startPos = holder.position();
        this.direction = computeDirection();
    }

    @Override
    public void stop() {
        if (holder.level() instanceof ServerLevel level) {
            nodes.forEach(node -> removeCrystal(level, node));
        }
        this.nodes.clear();
        this.lastHit.clear();
        this.totalSteps = 0;
        this.stepsDone = 0;
        this.nextUseTime = holder.level().getGameTime() + cooldownProvider.sample(holder.getRandom());
    }

    @Override
    public void tick() {
        if (!(holder.level() instanceof ServerLevel level)) {
            return;
        }
        long now = level.getGameTime();

        // Casting phase: hold still, face the target, raise one crystal every few ticks
        if (stepsDone < totalSteps) {
            holder.getNavigation().stop();
            LivingEntity target = holder.getTarget();
            if (target != null) {
                holder.getLookControl().setLookAt(target, 30.0F, 30.0F);
            }

            if (now >= nextStepTime) {
                raiseCrystal(level, stepsDone, now);
                stepsDone++;
                nextStepTime = now + STEP_INTERVAL;
            }
        }

        hurtTouchingEntities(level, now);
        removeExpired(level, now);
    }

    // ------------------------------------------------------------------
    // Path
    // ------------------------------------------------------------------

    private Vec3 computeDirection() {
        LivingEntity target = holder.getTarget();
        Vec3 dir = target != null ? target.position().subtract(holder.position()) : holder.getLookAngle();
        dir = new Vec3(dir.x, 0, dir.z);
        if (dir.lengthSqr() < 1.0E-4) {
            Vec3 look = holder.getLookAngle();
            dir = new Vec3(look.x, 0, look.z);
        }
        return dir.lengthSqr() < 1.0E-4 ? new Vec3(0, 0, 1) : dir.normalize();
    }

    private void raiseCrystal(ServerLevel level, int index, long now) {
        Vec3 point = startPos.add(direction.scale((index + 1) * SPACING));
        BlockPos ground = findPlacement(level, BlockPos.containing(point.x, startPos.y, point.z));
        if (ground == null) {
            return; // nowhere to put a crystal on this step, the path just skips it
        }

        BlockState crystal = ChangedAddonBlocks.LUMINAR_CRYSTAL_SMALL.get().defaultBlockState()
                .setValue(FACING, Direction.UP)
                .setValue(CAN_SURVIVE_ANYWHERE, true);

        level.setBlock(ground, crystal, 3);
        level.playSound(null, ground, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.8f, 1.2f);
        nodes.add(new CrystalNode(ground, now + lifetimeTicks));
    }

    /** Finds an empty block with solid ground below, near the given position. Null if none. */
    @Nullable
    private static BlockPos findPlacement(ServerLevel level, BlockPos base) {
        BlockPos pos = base;

        for (int up = 0; up < MAX_STEP_UP && !level.isEmptyBlock(pos); up++) {
            pos = pos.above();
        }
        if (!level.isEmptyBlock(pos)) {
            return null;
        }

        for (int down = 0; down < MAX_STEP_DOWN && level.isEmptyBlock(pos.below()); down++) {
            pos = pos.below();
        }

        BlockPos below = pos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP) ? pos : null;
    }

    // ------------------------------------------------------------------
    // Damage and cleanup
    // ------------------------------------------------------------------

    private void hurtTouchingEntities(ServerLevel level, long now) {
        DamageSource source = level.damageSources().indirectMagic(holder, holder);

        for (CrystalNode node : nodes) {
            AABB box = new AABB(node.pos()).inflate(0.1D);
            for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, box, e -> e != holder && e.isAlive() && !e.isSpectator())) {
                Long last = lastHit.get(entity.getUUID());
                if ((last == null || now - last >= HIT_COOLDOWN) && entity.hurt(source, damageProvider.sample(holder.getRandom()))) {
                    lastHit.put(entity.getUUID(), now);
                }
            }
        }
    }

    private void removeExpired(ServerLevel level, long now) {
        Iterator<CrystalNode> it = nodes.iterator();
        while (it.hasNext()) {
            CrystalNode node = it.next();
            boolean stillThere = level.getBlockState(node.pos()).is(ChangedAddonBlocks.LUMINAR_CRYSTAL_SMALL.get());
            if (!stillThere) {
                it.remove(); // broken by someone else
            } else if (now >= node.expireTime()) {
                removeCrystal(level, node);
                it.remove();
            }
        }
    }

    private void removeCrystal(ServerLevel level, CrystalNode node) {
        if (level.getBlockState(node.pos()).is(ChangedAddonBlocks.LUMINAR_CRYSTAL_SMALL.get())) {
            level.removeBlock(node.pos(), false);
            level.playSound(null, node.pos(), SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.BLOCKS, 0.6f, 1.0f);
        }
    }
}