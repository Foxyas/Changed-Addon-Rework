package net.foxyas.changedaddon.entity.ai.goals.prototype;

import net.foxyas.changedaddon.entity.advanced.PrototypeEntity;
import net.foxyas.changedaddon.util.DelayedTask;
import net.foxyas.changedaddon.util.DynamicClipContext;
import net.ltxprogrammer.changed.init.ChangedBlocks;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class PruningOrangeLeavesGoal extends Goal {

    private static final int searchRange = 10;
    private final PrototypeEntity holder;

    private boolean lock;
    private BlockPos targetLeave;
    private int pruneCooldown;
    private int noPathTimeout;

    public PruningOrangeLeavesGoal(PrototypeEntity holder) {
        this.holder = holder;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (lock) return false;

        return !findShears().isEmpty();
    }

    @Override
    public boolean canContinueToUse() {
        if (targetLeave == null) {
            lock = true;
            new DelayedTask(200, () -> lock = false);
            return false;
        }

        return !findShears().isEmpty();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        findNearbyOrangeLeaves(holder.blockPosition(), holder.getEyePosition());
        if(targetLeave == null) return;

        holder.getNavigation().moveTo(targetLeave.getX(), targetLeave.getY(), targetLeave.getZ(), 0.25f);
    }

    @Override
    public void tick() {
        Level level = holder.level;
        PathNavigation navigation = holder.getNavigation();
        if (targetLeave == null || isBlockInvalid(level.getBlockState(targetLeave))) {
            findNearbyOrangeLeaves(holder.blockPosition(), holder.getEyePosition());
            if (targetLeave == null) return;
            navigation.moveTo(targetLeave.getX(), targetLeave.getY(), targetLeave.getZ(), 0.25f);
        }


        holder.getLookControl().setLookAt(targetLeave.getX(), targetLeave.getY(), targetLeave.getZ(),
                30, 30);

        if (pruneCooldown > 0) {
            pruneCooldown--;
            return;
        }

        if (targetLeave.distSqr(holder.blockPosition()) <= (3.5f * 3.5f)) {
            pruneOrangeLeaves();
            findNearbyOrangeLeaves(holder.blockPosition(), holder.getEyePosition());
            if(targetLeave != null) navigation.moveTo(targetLeave.getX(), targetLeave.getY(), targetLeave.getZ(), 0.25f);
            pruneCooldown = 5;
        }

        if (navigation.isStuck() || (navigation.getPath() != null && !navigation.getPath().canReach())) {
            noPathTimeout--;
            if (noPathTimeout <= 0) {//No path, try again later
                targetLeave = null;
            } else if (noPathTimeout % 25 == 0) navigation.recomputePath();
            return;
        }

        noPathTimeout = 100;
    }

    @Override
    public void stop() {
        holder.getNavigation().stop();
        targetLeave = null;
        pruneCooldown = 0;
        noPathTimeout = 100;
    }

    private ItemStack findShears() {
        ItemStack shears = holder.getMainHandItem();
        if (!shears.isEmpty() && shears.is(Tags.Items.SHEARS)) return shears;

        shears = holder.getOffhandItem();
        return !shears.isEmpty() && shears.is(Tags.Items.SHEARS) ? shears : ItemStack.EMPTY;
    }

    private boolean isBlockInvalid(BlockState state) {
        return !state.is(ChangedBlocks.ORANGE_TREE_LEAVES.get());
    }

    private void findNearbyOrangeLeaves(BlockPos center, Vec3 eyePos) {
        BlockPos closest = null;
        float bestDist = searchRange * searchRange + .01f, dist;
        Level level = holder.level;
        // Evite .toList() para não alocar tudo; itere o stream diretamente
        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-searchRange, -searchRange, -searchRange),
                center.offset(searchRange, searchRange, searchRange))) {
            dist = (float) eyePos.distanceToSqr(Vec3.atCenterOf(pos));
            if (dist >= bestDist || isBlockInvalid(level.getBlockState(pos))) continue;

            BlockHitResult hit = level.clip(eyeContext(pos));
            if (hit.getType() == HitResult.Type.BLOCK && hit.getBlockPos().equals(pos)) {
                bestDist = dist;
                closest = pos.immutable();
            }
        }
        targetLeave = closest;
    }

    private @NotNull ClipContext eyeContext(BlockPos pos) {
        return new DynamicClipContext(
                holder.getEyePosition(),
                Vec3.atCenterOf(pos),
                DynamicClipContext.IGNORE_TRANSLUCENT,
                ClipContext.Fluid.ANY::canPick,
                CollisionContext.of(holder));
    }

    private void pruneOrangeLeaves() {
        ItemStack shears = holder.getMainHandItem();
        InteractionHand hand = InteractionHand.MAIN_HAND;
        if (shears.isEmpty() || !shears.is(Tags.Items.SHEARS)) {
            shears = holder.getOffhandItem();
            if (shears.isEmpty() || !shears.is(Tags.Items.SHEARS)) return;

            hand = InteractionHand.OFF_HAND;
        }

        Level level = holder.level;
        BlockState state = level.getBlockState(targetLeave);
        if (isBlockInvalid(state)) return;

        BlockState newState = Blocks.OAK_LEAVES.defaultBlockState();

        if (state.hasProperty(LeavesBlock.DISTANCE)) {
            newState = newState.setValue(LeavesBlock.DISTANCE, state.getValue(LeavesBlock.DISTANCE));
        }

        if (state.hasProperty(LeavesBlock.PERSISTENT)) {
            newState = newState.setValue(LeavesBlock.PERSISTENT, state.getValue(LeavesBlock.PERSISTENT));
        }

        int fortune = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, shears);
        UniformInt uniformInt;

        if (fortune > 0) {
            uniformInt = UniformInt.of(fortune, 8 * fortune);
        } else {
            uniformInt = UniformInt.of(1, 8);
        }

        ItemStack orangeStack = new ItemStack(ChangedItems.ORANGE.get());
        orangeStack.setCount(uniformInt.sample(holder.getRandom()));
        orangeStack = holder.addToInventory(orangeStack, false);
        if (!orangeStack.isEmpty()) Block.popResource(level, targetLeave, orangeStack);

        shears.hurtAndBreak(1, holder, (prototype) -> {});

        holder.getLookControl().setLookAt(Vec3.atCenterOf(targetLeave));
        holder.swing(hand);

        level.setBlockAndUpdate(targetLeave, newState);
        level.playSound(null, holder, SoundEvents.SNOW_GOLEM_SHEAR, SoundSource.BLOCKS, 1, 1);
    }
}