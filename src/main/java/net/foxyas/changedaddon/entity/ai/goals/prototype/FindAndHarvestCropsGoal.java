package net.foxyas.changedaddon.entity.ai.goals.prototype;

import net.foxyas.changedaddon.entity.advanced.PrototypeEntity;
import net.foxyas.changedaddon.init.ChangedAddonSoundEvents;
import net.foxyas.changedaddon.util.DelayedTask;
import net.ltxprogrammer.changed.entity.Emote;
import net.ltxprogrammer.changed.init.ChangedParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;
import java.util.List;

public class FindAndHarvestCropsGoal extends Goal {

    private static final int searchRange = 10;

    private final PrototypeEntity entity;
    private final PathNavigation navigation;

    private boolean lock;
    private boolean pendingEffects = true;
    private BlockPos targetCropPos;
    private int harvestCooldown;
    private int noPathTimeout;

    public FindAndHarvestCropsGoal(PrototypeEntity entity) {
        this.entity = entity;
        this.navigation = entity.getNavigation();
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if(lock) return false;

        // Can only harvest if inventory not full and there's a mature crop nearby
        return entity.hasSpaceInInvOrHands() && entity.getHarvestsTimes() < PrototypeEntity.MAX_HARVEST_TIMES;
    }

    @Override
    public boolean canContinueToUse() {
        if(targetCropPos == null){
            lock = true;
            new DelayedTask(200, ()-> lock = false);
            return false;
        }

        return entity.hasSpaceInInvOrHands() && entity.getHarvestsTimes() < PrototypeEntity.MAX_HARVEST_TIMES;
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    protected int adjustedTickDelay(int pAdjustment) {
        return 0;
    }

    @Override
    public void start() {
        Level level = entity.level();
        findNearbyCrop(level, entity.blockPosition());
        if (targetCropPos == null) return;

        navigation.moveTo(targetCropPos.getX() + 0.5, targetCropPos.getY(), targetCropPos.getZ() + 0.5, 0.25f);
    }

    @Override
    public void tick() {
        Level level = entity.level();
        if(targetCropPos == null || isBlockInvalid(level.getBlockState(targetCropPos))){// Try to find crop
            findNearbyCrop(level, entity.blockPosition());
            if(targetCropPos == null) return;//cancel goal - no crops
            navigation.moveTo(targetCropPos.getX() + 0.5, targetCropPos.getY(), targetCropPos.getZ() + 0.5, 0.25f);
        }

        entity.getLookControl().setLookAt(
                targetCropPos.getX(), targetCropPos.getY(), targetCropPos.getZ(),
                30.0F, // yaw change speed (degrees per tick)
                30.0F  // pitch change speed
        );

        if(harvestCooldown > 0){
            harvestCooldown--;
            return;
        }

        if (entity.blockPosition().closerThan(targetCropPos, 2.5)) {
            harvestCrop((ServerLevel) level);
            findNearbyCrop(level, entity.blockPosition()); // Look for new crop
            if(targetCropPos != null) navigation.moveTo(targetCropPos.getX() + 0.5, targetCropPos.getY(), targetCropPos.getZ() + 0.5, 0.25f);
            harvestCooldown = 5;
        }

        if (navigation.isStuck() || (navigation.getPath() != null && !navigation.getPath().canReach())) {
            noPathTimeout--;
            if (noPathTimeout <= 0) {//No path, try again later
                targetCropPos = null;
            } else if (noPathTimeout % 25 == 0) navigation.recomputePath();
            return;
        }

        noPathTimeout = 100;

        if (pendingEffects) {
            pendingEffects = false;

            level.playSound(null, entity.blockPosition(), ChangedAddonSoundEvents.PROTOTYPE_IDEA.get(), SoundSource.MASTER, 1, 1);

            ((ServerLevel)level).sendParticles(ChangedParticles.emote(entity, Emote.IDEA),
                    entity.getX(), entity.getY() + entity.getDimensions(entity.getPose()).height + 0.65, entity.getZ(),
                    1, 0, 0, 0, 0);
        }
    }

    @Override
    public void stop() {
        entity.getNavigation().stop();
        pendingEffects = true;
        targetCropPos = null;
        harvestCooldown = 0;
        noPathTimeout = 100;
    }

    private boolean isBlockInvalid(BlockState state){
        return !(state.getBlock() instanceof CropBlock crop) || !crop.isMaxAge(state);
    }

    private void findNearbyCrop(Level level, BlockPos center) {
        BlockPos closestCrop = null;
        float closestDist = searchRange * searchRange + .01f, dist;
        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-searchRange, -searchRange, -searchRange), center.offset(searchRange, searchRange, searchRange))) {
            dist = (float) pos.distSqr(center);
            if (dist >= closestDist || isBlockInvalid(level.getBlockState(pos))) continue;

            closestDist = dist;
            closestCrop = pos.immutable();
        }
        targetCropPos = closestCrop;
    }

    private void harvestCrop(ServerLevel level) {
        BlockState state = level.getBlockState(targetCropPos);
        if (isBlockInvalid(state)) return;

        this.entity.getLookControl().setLookAt(
                targetCropPos.getX(), targetCropPos.getY(), targetCropPos.getZ(),
                30.0F, // yaw change speed (degrees per tick)
                30.0F  // pitch change speed
        );
        entity.swing(entity.isLeftHanded() ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);

        // Drop items naturally (simulate player breaking)
        ItemStack tool = entity.getMainHandItem();
        List<ItemStack> drops = Block.getDrops(state, level, targetCropPos, level.getBlockEntity(targetCropPos), entity, tool);
        for (ItemStack stack : drops) {
            stack = entity.addToInventory(stack, false);
            if (stack.isEmpty()) continue;

            Block.popResource(level, targetCropPos, stack);
        }

        // Replant at age 0
        level.setBlock(targetCropPos, ((CropBlock)state.getBlock()).getStateForAge(0), 3);
        level.levelEvent(null, 2001, targetCropPos, Block.getId(state));//Particles
        level.playSound(null, targetCropPos, state.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1, 1);
        entity.addHarvestsTime();
    }
}