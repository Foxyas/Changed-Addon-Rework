package net.foxyas.changedaddon.entity.ai.goals.generic;

import net.foxyas.changedaddon.util.DynamicClipContext;
import net.ltxprogrammer.changed.block.WhiteLatexTransportInterface;
import net.ltxprogrammer.changed.entity.PlayerDataExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

import java.util.EnumSet;
import java.util.List;

public class LatexPullEntityGoal extends Goal {

    private final Mob holder;
    private final double range;
    private final double strength;

    protected List<LivingEntity> entities;
    protected int blockBreakCooldown;

    public LatexPullEntityGoal(Mob holder, double range, double strength) {
        this.holder = holder;
        this.range = range;
        this.strength = strength;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    /**
     * Checks if one entity (eyeEntity) can see another (targetToSee), using raycasting and FOV.
     *
     * @param eyeEntity   The entity doing the looking.
     * @param targetToSee The target entity being looked at.
     * @param fovDegrees  Field of view angle in degrees (e.g., 90 means 45 degrees to each side).
     * @return true if visible and within FOV, false otherwise.
     */
    public static boolean canHolderSeeOther(LivingEntity eyeEntity, LivingEntity targetToSee, double fovDegrees) {
        Level level = eyeEntity.level;
        if (level != targetToSee.level) return false;

        Vec3 from = eyeEntity.getEyePosition(1.0F);
        Vec3 to = targetToSee.getPosition(1.0F);

        // First, check field of view using dot product
        Vec3 lookVec = eyeEntity.getLookAngle().normalize();
        Vec3 directionToTarget = to.subtract(from).normalize();

        double dot = lookVec.dot(directionToTarget);
        double requiredDot = Math.cos(Math.toRadians(fovDegrees / 2.0));
        if (dot < requiredDot)
            return false; // Outside of FOV

        // Then, raycast from eyeEntity to targetToSee to check if the view is blocked
        HitResult result = level.clip(new DynamicClipContext(
                from, to, DynamicClipContext.IGNORE_TRANSLUCENT, ClipContext.Fluid.NONE::canPick, targetToSee, CollisionContext.of(targetToSee)
        ));

        // If result is MISS or hit point is very close to target, it's considered visible
        return result.getType() == HitResult.Type.MISS ||
                result.getLocation().distanceToSqr(to) < 1.0;
    }

    @Override
    public boolean canUse() {
        entities = holder.level.getEntitiesOfClass(
                LivingEntity.class,
                holder.getBoundingBox().inflate(range),
                e -> e != holder && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(e) && e.isAlive() && (WhiteLatexTransportInterface.isEntityInWhiteLatex(e) || WhiteLatexTransportInterface.isBoundingBoxInWhiteLatex(e).isPresent())
        );
        return !entities.isEmpty();
    }

    @Override
    public void tick() {
        if (blockBreakCooldown > 0)
            blockBreakCooldown--;

        for (LivingEntity target : entities) {
            pullEntity(target);

            if (target.distanceTo(holder) <= 2.0F || (canHolderSeeOther(target, holder, 360) && target.distanceTo(holder) <= 8)) {
                onSuccessfulPull(target);
            }
        }
    }

    @Override
    public void stop() {
        entities = null;
    }

    private void onSuccessfulPull(LivingEntity target) {
        if (blockBreakCooldown <= 0) {
            breakSurroundingBlocks();
            blockBreakCooldown = 20; // 1 segundo
        }

        if (target instanceof PlayerDataExtension player) {
            player.setPlayerMoverType(null);
        }

        target.setPos(holder.position());
        target.setDeltaMovement(Vec3.ZERO);
    }

    private void breakSurroundingBlocks() {
        BlockPos center = holder.blockPosition();
        Level level = holder.level;

        int radius = 3;

        for (BlockPos pos : BlockPos.betweenClosedStream(
                center.offset(-radius, 0, -radius),
                center.offset(radius, 3, radius)).map(BlockPos::immutable).toList()
        ) {
            BlockState state = level.getBlockState(pos);

            if (state.isAir())
                continue;

            if (state.getDestroySpeed(level, pos) < 0)
                continue; // bedrock, barriers, etc

            level.destroyBlock(pos, true, holder);
        }
    }

    private void pullEntity(LivingEntity target) {
        Vec3 moverPos = holder.position().add(0, holder.getBbHeight() * 0.5, 0);
        Vec3 targetPos = target.position().add(0, target.getBbHeight() * 0.5, 0);

        Vec3 direction = moverPos.subtract(targetPos);
        double distance = direction.length();

        if (distance < 0.1) return;

        Vec3 normalized = direction.normalize();

        // força aumenta quanto mais perto
        double pull = strength * (1.0 - Math.min(distance / range, 1.0));

        Vec3 motion = normalized.scale(pull);

        if (target instanceof Player player) {
            player.move(MoverType.SELF, motion);
        } else {
            target.setDeltaMovement(target.getDeltaMovement().add(motion));
        }

        target.hurtMarked = true;
    }
}
