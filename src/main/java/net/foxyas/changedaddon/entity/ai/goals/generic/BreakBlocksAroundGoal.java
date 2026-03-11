package net.foxyas.changedaddon.entity.ai.goals.generic;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;

public class BreakBlocksAroundGoal extends Goal {
    private static final int FRUSTRATION_THRESHOLD = 100 * 0; //FIXME: remove *0 when tests are done
    private final Mob holder;
    protected int frustrationTicks = 0;
    private int breakCooldown = 0;

    public BreakBlocksAroundGoal(Mob holder) {
        this.holder = holder;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!holder.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            return false;
        }

        boolean takingDamage = holder.getCombatTracker().takingDamage;

        if (takingDamage && holder.getTarget() == null) {
            frustrationTicks++;
        } else if (frustrationTicks > 0) {
            frustrationTicks--;
        }

        if (frustrationTicks >= FRUSTRATION_THRESHOLD) {
            frustrationTicks = FRUSTRATION_THRESHOLD / 2;
            return true;
        }

        if (holder.getTarget() == null) {
            BlockPos eyePos = holder.blockPosition().above(Mth.floor(holder.getEyeHeight()));
            if (!holder.level.getBlockState(eyePos).isAir()) {
                this.frustrationTicks++;
            }
            return false;
        }

        PathNavigation pathNavigation = holder.getNavigation();

        Path holderPath = pathNavigation.getPath();
        if (holderPath != null && holderPath.getEndNode() != null && holderPath.getEndNode().asVec3().distanceTo(holder.getTarget().position()) <= 1.5 && !holderPath.canReach()) {
            this.frustrationTicks++;
        }

        if (!holder.isAlive() || breakCooldown > 0) {
            if (breakCooldown > 0) {
                tickCooldown();
            }

            return false;
        }

        if (holder.getDeltaMovement().length() < 0.05) {
            if (holder.horizontalCollision || holder.verticalCollision) {
                frustrationTicks++;
            }
        }

        return false;
    }

    @Override
    public void tick() {
        if (!(holder.level instanceof ServerLevel serverLevel)) return;
        if (breakCooldown > 0) {
            tickCooldown();
            return;
        }

        BlockPos mobPos = holder.blockPosition();
        int horizontalRadius = 3;
        int verticalRadius = 3;

        int suppedCooldown = 0;
        for (BlockPos pos : BlockPos.betweenClosedStream(
                mobPos.offset(-horizontalRadius, 0, -horizontalRadius),
                mobPos.offset(horizontalRadius, verticalRadius, horizontalRadius)).map(BlockPos::immutable).filter(pos -> {
            int xi = pos.getX() - mobPos.getX();
            int yi = pos.getY() - mobPos.getY();
            int zi = pos.getZ() - mobPos.getZ();

            double distanceSq =
                    (xi * xi) / (double) (horizontalRadius * horizontalRadius) +
                            (yi * yi) / (double) (verticalRadius * verticalRadius) +
                            (zi * zi) / (double) (horizontalRadius * horizontalRadius);

            return distanceSq <= 1.0;
        }).toList()) {

            if (pos.equals(mobPos.below())) continue;

            var state = serverLevel.getBlockState(pos);
            if (!state.isAir() && state.getDestroySpeed(serverLevel, pos) >= 0) {
                if ((!state.is(Blocks.BEDROCK) || !state.is(BlockTags.WITHER_IMMUNE)) && !(state.getBlock() instanceof LiquidBlock)) {
                    serverLevel.destroyBlock(pos, true, holder);
                    suppedCooldown += 5;
                }
            }
        }
        breakCooldown = suppedCooldown;
    }

    public void tickCooldown() {
        if (breakCooldown > 0) breakCooldown--;
    }
}
