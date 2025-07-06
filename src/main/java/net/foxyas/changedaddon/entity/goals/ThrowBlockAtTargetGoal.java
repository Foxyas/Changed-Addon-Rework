package net.foxyas.changedaddon.entity.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.EnumSet;

public class ThrowBlockAtTargetGoal extends Goal {

    private final Mob mob;
    private LivingEntity target;
    private int cooldown = 0;

    public ThrowBlockAtTargetGoal(Mob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }

        target = mob.getTarget();
        return target != null && target.isAlive() && mob.distanceTo(target) < 8;
    }

    @Override
    public void start() {
        BlockPos below = mob.blockPosition().below();
        Level level = mob.level;

        BlockState blockState = level.getBlockState(below);
        boolean weakBlock = TierSortingRegistry.isCorrectTierForDrops(Tiers.IRON, blockState);

        if (!blockState.isAir() && weakBlock && level instanceof ServerLevel serverLevel) {
            // Remove the block
            level.setBlockAndUpdate(below, Blocks.AIR.defaultBlockState());

            // Create FallingBlock
            FallingBlockEntity fallingBlock = FallingBlockEntity.fall(
                    serverLevel,
                    new BlockPos(mob.position().add(0, 1, 0)),
                    blockState
            );

            // Set velocity toward target
            Vec3 direction = target.position().subtract(mob.position()).normalize().scale(0.8);
            fallingBlock.setDeltaMovement(direction.x, 0.4, direction.z);

            // Optional: make it hurt
            fallingBlock.setHurtsEntities(2.0F, 4);  // Damage multiplier and max damage

            // Add to world
            serverLevel.addFreshEntity(fallingBlock);
        }

        mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
        mob.getNavigation().stop();
        cooldown = 60;
    }

    private void onFallingBlockHit(FallingBlockEntity fallingBlock) {
        Level level = fallingBlock.level;
        BlockPos pos = fallingBlock.blockPosition();

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.getEntitiesOfClass(LivingEntity.class, fallingBlock.getBoundingBox(), e -> !e.isSpectator() && e.isAlive())
                    .forEach(entity -> {
                        entity.hurt(DamageSource.mobAttack(mob), 4.0F);
                    });
        }
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }
}
