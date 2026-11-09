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

import static net.foxyas.changedaddon.block.LuminarCrystalSmall.CAN_SURVIVE_ANYWHERE;
import static net.foxyas.changedaddon.block.LuminarCrystalSmall.FACING;

/**
 * Crystal path attack.
 * <p>
 * The holder channels for a moment and raises a straight line of crystals toward its target.
 * Each crystal hurts any living entity (except the holder) standing on its spot at the moment
 * it is placed. The crystals are permanent: they are a consequence of the mob existing.
 */
public class CrystalPathAttackGoal extends Goal {

    private static final double SPACING = 1.0D;       // blocks between crystals
    private static final int STEP_INTERVAL = 2;       // ticks between each crystal being raised
    private static final double HIT_INFLATE = 0.6D;   // how far around the crystal the hit reaches
    private static final int MAX_STEP_UP = 2;         // how much terrain the path can climb per crystal
    private static final int MAX_STEP_DOWN = 4;       // how far the path will look down for ground

    private final Mob holder;
    private final IntProvider cooldownProvider;
    private final IntProvider lengthProvider;
    private final double minDistanceSqr;
    private final FloatProvider damageProvider;

    private long nextUseTime = 0;
    private long nextStepTime = 0;
    private int totalSteps = 0;
    private int stepsDone = 0;
    private Vec3 startPos = Vec3.ZERO;
    private Vec3 direction = Vec3.ZERO;

    /**
     * @param cooldownProvider ticks to wait after the attack ends before it can be used again
     * @param lengthProvider   how many crystals the path has
     * @param minDistance      the target must be at least this far (in blocks) for the attack to start
     * @param damageProvider   damage dealt to entities hit when a crystal is placed
     */
    public CrystalPathAttackGoal(Mob holder, IntProvider cooldownProvider, IntProvider lengthProvider, double minDistance, FloatProvider damageProvider) {
        this.holder = holder;
        this.cooldownProvider = cooldownProvider;
        this.lengthProvider = lengthProvider;
        this.minDistanceSqr = minDistance * minDistance;
        this.damageProvider = damageProvider;
        // No flags: the holder is only held in place manually while casting.
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
        return stepsDone < totalSteps;
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
        this.stepsDone = 0;
        this.totalSteps = Math.max(1, lengthProvider.sample(holder.getRandom()));
        this.nextStepTime = holder.level().getGameTime();
        this.startPos = holder.position();
        this.direction = computeDirection();
    }

    @Override
    public void stop() {
        this.totalSteps = 0;
        this.stepsDone = 0;
        this.nextUseTime = holder.level().getGameTime() + cooldownProvider.sample(holder.getRandom());
    }

    @Override
    public void tick() {
        if (!(holder.level() instanceof ServerLevel level)) {
            return;
        }

        // Hold still and face the target while casting
        holder.getNavigation().stop();
        LivingEntity target = holder.getTarget();
        if (target != null) {
            holder.getLookControl().setLookAt(target, 30.0F, 30.0F);
        }

        long now = level.getGameTime();
        if (now >= nextStepTime) {
            raiseCrystal(level, stepsDone);
            stepsDone++;
            nextStepTime = now + STEP_INTERVAL;
        }
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

    private void raiseCrystal(ServerLevel level, int index) {
        Vec3 point = startPos.add(direction.scale((index + 1) * SPACING));
        BlockPos ground = findPlacement(level, BlockPos.containing(point.x, startPos.y, point.z));
        if (ground == null) {
            return; // nowhere to put a crystal on this step, the path just skips it
        }

        BlockState crystal = ChangedAddonBlocks.LUMINAR_CRYSTAL_SMALL.get().defaultBlockState()
                .setValue(FACING, Direction.UP)
                .setValue(CAN_SURVIVE_ANYWHERE, true);

        level.setBlock(ground, crystal, 3);

        // Placement sound: a cluster "thunk" layered with a higher chime
        level.playSound(null, ground, SoundEvents.AMETHYST_CLUSTER_PLACE, SoundSource.HOSTILE, 1.0f, 0.9f);
        level.playSound(null, ground, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.HOSTILE, 0.8f, 1.2f);

        hurtAround(level, ground);
    }

    /** Hurts everything except the holder around the crystal that was just placed. */
    private void hurtAround(ServerLevel level, BlockPos pos) {
        DamageSource source = level.damageSources().indirectMagic(holder, holder);
        AABB hitBox = new AABB(pos).inflate(HIT_INFLATE);
        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, hitBox, e -> e != holder && e.isAlive() && !e.isSpectator())) {
            entity.hurt(source, damageProvider.sample(holder.getRandom()));
        }
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
}