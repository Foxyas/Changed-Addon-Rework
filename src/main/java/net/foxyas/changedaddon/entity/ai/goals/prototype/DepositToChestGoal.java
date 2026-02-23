package net.foxyas.changedaddon.entity.ai.goals.prototype;

import net.foxyas.changedaddon.entity.advanced.PrototypeEntity;
import net.foxyas.changedaddon.util.DelayedTask;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class DepositToChestGoal extends Goal {

    private final PrototypeEntity holder;
    private final int range;

    private boolean lock;
    private BlockPos chestPos;
    private int noPathTimeout;

    public DepositToChestGoal(PrototypeEntity holder, int range){
        this.holder = holder;
        this.range = range;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean canUse() {
        if(lock) return false;

        return holder.wantsToDeposit() || holder.getHarvestsTimes() >= PrototypeEntity.MAX_HARVEST_TIMES;
    }

    @Override
    public void start() {
        tryFindNearbyChest(holder.level);
        if(chestPos != null) holder.getNavigation().moveTo(chestPos.getX() + 0.5, chestPos.getY(), chestPos.getZ() + 0.5, 0.25f);
    }

    @Override
    public boolean canContinueToUse() {
        return chestPos != null && noPathTimeout > 0 && super.canContinueToUse();
    }

    @Override
    public void tick() {
        Level level = holder.level;
        PathNavigation navigation = holder.getNavigation();
        if(chestPos == null || !(level.getBlockEntity(chestPos) instanceof ChestBlockEntity)) {
            tryFindNearbyChest(level);
            if(chestPos == null) {
                lock = true;
                new DelayedTask(200, ()-> lock = false);
                return;
            }
            navigation.moveTo(chestPos.getX(), chestPos.getY(), chestPos.getZ(), .25);
        }

        if(!(level.getBlockEntity(chestPos) instanceof ChestBlockEntity)){
            chestPos = null;
            return;
        }

        if(holder.blockPosition().closerThan(chestPos, 2.5)){
            depositToChest((ServerLevel) level);
            chestPos = null;
            return;
        }

        holder.getLookControl().setLookAt(
                chestPos.getX(), chestPos.getY(), chestPos.getZ(),
                30.0F, // yaw change speed (degrees per tick)
                30.0F  // pitch change speed
        );

        if(navigation.isStuck() || (navigation.getPath() != null && !navigation.getPath().canReach())){
            noPathTimeout--;
            if(noPathTimeout <= 0){//No path, try again later
                chestPos = null;
                lock = true;
                new DelayedTask(200, ()-> lock = false);
            } else if (noPathTimeout % 25 == 0) navigation.recomputePath();
            return;
        }

        noPathTimeout = 100;
    }

    @Override
    public void stop() {
        holder.getNavigation().stop();
        chestPos = null;
        noPathTimeout = 100;
    }

    private void tryFindNearbyChest(Level level) {
        IItemHandler handsInv = holder.getHandsAndInv();
        PrototypeEntity.DepositType depositType = holder.getDepositType();
        List<ItemStack> carriedItems = new ArrayList<>();
        for (int i = 0; i < handsInv.getSlots(); i++) {
            ItemStack stack = handsInv.getStackInSlot(i);
            if (!stack.isEmpty() && depositType.test(stack)) carriedItems.add(stack.copy());
        }

        BlockPos center = holder.blockPosition(), closestChest = null, bestChest = null;
        double closestDist = Double.MAX_VALUE, bestDist = closestDist;
        double dist;
        boolean isFull, potentiallyBest;

        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-range, -range, -range), center.offset(range, range, range))) {
            if(!(level.getBlockEntity(pos) instanceof ChestBlockEntity chest)) continue;

            dist = pos.distSqr(center);
            if(dist >= bestDist) continue;

            isFull = true;
            potentiallyBest = false;
            for (int slot = 0; slot < chest.getContainerSize(); slot++) {
                ItemStack chestItem = chest.getItem(slot);
                if(chestItem.isEmpty()) {//If not full
                    isFull = false;

                    if(dist < closestDist) {
                        closestDist = dist;
                        closestChest = pos.immutable();
                    }

                    if(potentiallyBest){
                        bestDist = dist;
                        bestChest = pos.immutable();
                        break;
                    }
                    continue;
                }

                for (ItemStack carried : carriedItems) {
                    if(!ItemStack.isSameItemSameTags(carried, chestItem)) continue;

                    if(chestItem.getCount() >= chestItem.getMaxStackSize() && isFull) {
                        potentiallyBest = true;
                        break;
                    }

                    bestDist = dist;
                    bestChest = pos.immutable();
                    break;
                }
                if(pos.equals(bestChest)) break;
            }
        }

        chestPos = bestChest != null ? bestChest : closestChest;
    }

    private void depositToChest(ServerLevel level) {
        if(!(level.getBlockEntity(chestPos) instanceof ChestBlockEntity chest)) {
            chestPos = null;
            return;
        }

        boolean anyInserted = false;
        ItemStack stack, remainder;
        IItemHandler handsInv = holder.getHandsAndInv();
        IItemHandler handler = chest.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().orElseThrow();
        for(int i = 0; i < handsInv.getSlots(); i++){
            stack = handsInv.getStackInSlot(i);
            if(stack.isEmpty() || !holder.getDepositType().test(stack)) continue;

            remainder = ItemHandlerHelper.insertItem(handler, stack, false);
            if(remainder == stack) continue;

            anyInserted = true;
            handsInv.extractItem(i, stack.getCount() - remainder.getCount(), false);
        }

        if(!anyInserted) return;

        holder.lookAt(EntityAnchorArgument.Anchor.FEET, new Vec3(chestPos.getX(), chestPos.getY() - 1, chestPos.getZ()));
        holder.swing(holder.isLeftHanded() ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
        holder.setHarvestsTimes(0);
        chest.startOpen(FakePlayerFactory.getMinecraft(level));
        chest.triggerEvent(1, 1);
        chest.setChanged();
        level.playSound(null, chestPos, SoundEvents.CHEST_OPEN, SoundSource.BLOCKS, 0.25f, 1);
    }
}