package net.foxyas.changedaddon.procedures.blocksHandle;

import com.ibm.icu.impl.Pair;
import net.foxyas.changedaddon.init.ChangedAddonModItems;
import net.ltxprogrammer.changed.block.AbstractLatexBlock;
import net.ltxprogrammer.changed.entity.LatexType;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class BoneMealExpansion {

    @Mod.EventBusSubscriber
    public static class AttemptToApply {

        @SubscribeEvent
        public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
            Player player = event.getPlayer();
            Level level = event.getWorld();
            BlockPos pos = event.getPos();
            ItemStack stack = event.getItemStack();
            BlockState state = level.getBlockState(pos);

            if (!level.isClientSide && stack.is(Items.BONE_MEAL)) {
                if (AbstractLatexBlock.isLatexed(state)) {
                    event.setUseBlock(Event.Result.DENY);
                    event.setUseItem(Event.Result.DENY);

                    // Consome o bone meal
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }

                    // Simula o "crescimento"
                    if (level instanceof ServerLevel serverLevel) {
                        spreadFromSource(serverLevel, pos, serverLevel.getRandom().nextInt(16) + 1);
                    }

                    level.levelEvent(1505, pos, 1); // Partículas de bonemeal
                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true); // impede vanilla
                }
            }
        }
    }

    public static void spreadFromSource(ServerLevel level, BlockPos source, int maxDepth) {
        Set<BlockPos> visited = new HashSet<>();
        Queue<Pair<BlockPos, Integer>> queue = new ArrayDeque<>();

        queue.add(Pair.of(source, 0));
        visited.add(source);

        while (!queue.isEmpty()) {
            var current = queue.poll();
            BlockPos pos = current.first;
            int depth = current.second;

            if (depth > maxDepth) continue;

            BlockState state = level.getBlockState(pos);
            if (!AbstractLatexBlock.isLatexed(state)) continue;

            // Simula "crescimento"
            state.randomTick(level, pos, level.getRandom());
            level.levelEvent(1505, pos, 1); // Partículas

            // Adiciona vizinhos se ainda dentro do limite
            if (depth < maxDepth) {
                for (Direction dir : Direction.values()) {
                    BlockPos neighbor = pos.relative(dir);
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.add(Pair.of(neighbor, depth + 1));
                    }
                }
            }
        }
    }


    public static class BoneMealDispenserHandler {
        public static void registerDispenserBehavior() {
            DispenserBlock.registerBehavior(Items.BONE_MEAL, (source, stack) -> {
                ServerLevel serverLevel = source.getLevel();
                Direction facing = source.getBlockState().getValue(DispenserBlock.FACING);
                BlockPos targetPos = source.getPos().relative(facing);
                BlockState targetState = serverLevel.getBlockState(targetPos);

                boolean success = false;

                if (!serverLevel.isClientSide && !targetState.isAir()) {

                    // Custom latex spreading
                    if (AbstractLatexBlock.isLatexed(targetState)) //noinspection CommentedOutCode
                    {
                        spreadFromSource(serverLevel, targetPos, serverLevel.getRandom().nextInt(16) + 1);

                        /*
                        targetState.randomTick(serverLevel, targetPos, serverLevel.getRandom());
                        for (BlockPos pos : BlockPos.betweenClosedStream(new AABB(targetPos).inflate(16)).toList()) {
                            BlockState posState = serverLevel.getBlockState(pos);
                            if (AbstractLatexBlock.isLatexed(posState)) {
                                posState.randomTick(serverLevel, pos, serverLevel.getRandom());
                                //serverLevel.levelEvent(1505, pos, 1); // Bone meal particles

                            }
                        }
                        */

                        //serverLevel.levelEvent(1505, targetPos, 1); // Bone meal particles
                        success = true;
                    }

                    // If not latexed, try vanilla bonemeal
                    if (!success) {
                        success = BoneMealItem.applyBonemeal(stack, serverLevel, targetPos, FakePlayerFactory.getMinecraft(serverLevel));
                        if (success) {
                            serverLevel.levelEvent(1505, targetPos, 0);
                        }
                    }

                    // Consume 1 bonemeal if applied
                    if (success && !stack.isEmpty()) {
                        stack.shrink(1);
                    }
                }

                return stack;
            });
        }
    }

    public static class GooApplyDispenserHandler {
        public static void registerDispenserBehavior() {
            DispenserBlock.registerBehavior(ChangedItems.DARK_LATEX_GOO.get(), (source, stack) -> {
                ServerLevel serverLevel = source.getLevel();
                Direction facing = source.getBlockState().getValue(DispenserBlock.FACING);
                BlockPos targetPos = source.getPos().relative(facing);
                BlockState targetState = serverLevel.getBlockState(targetPos);

                boolean success = false;

                if (!serverLevel.isClientSide && !targetState.isAir()) {

                    // Custom latex spreading
                    if (targetState.hasProperty(AbstractLatexBlock.COVERED)) {
                        BlockState newState = targetState.setValue(AbstractLatexBlock.COVERED, LatexType.DARK_LATEX);
                        serverLevel.setBlockAndUpdate(targetPos, newState);
                        serverLevel.levelEvent(1505, targetPos, 1); // Bone meal particles
                        newState.randomTick(serverLevel, targetPos, serverLevel.getRandom());
                        success = true;
                    }

                    // Consume 1 bonemeal if applied
                    if (success && !stack.isEmpty()) {
                        stack.shrink(1);
                    }
                }

                return stack;
            });
            DispenserBlock.registerBehavior(ChangedItems.WHITE_LATEX_GOO.get(), (source, stack) -> {
                ServerLevel serverLevel = source.getLevel();
                Direction facing = source.getBlockState().getValue(DispenserBlock.FACING);
                BlockPos targetPos = source.getPos().relative(facing);
                BlockState targetState = serverLevel.getBlockState(targetPos);

                boolean success = false;

                if (!serverLevel.isClientSide && !targetState.isAir()) {

                    // Custom latex spreading
                    if (targetState.hasProperty(AbstractLatexBlock.COVERED)) {
                        BlockState newState = targetState.setValue(AbstractLatexBlock.COVERED, LatexType.WHITE_LATEX);
                        serverLevel.setBlockAndUpdate(targetPos, newState);
                        serverLevel.levelEvent(1505, targetPos, 1); // Bone meal particles
                        newState.randomTick(serverLevel, targetPos, serverLevel.getRandom());
                        success = true;
                    }

                    // Consume 1 bonemeal if applied
                    if (success && !stack.isEmpty()) {
                        stack.shrink(1);
                    }
                }

                return stack;
            });
            DispenserBlock.registerBehavior(ChangedAddonModItems.ANTI_LATEX_BASE.get(), (source, stack) -> {
                ServerLevel serverLevel = source.getLevel();
                Direction facing = source.getBlockState().getValue(DispenserBlock.FACING);
                BlockPos targetPos = source.getPos().relative(facing);
                BlockState targetState = serverLevel.getBlockState(targetPos);

                boolean success = false;

                if (!serverLevel.isClientSide && !targetState.isAir()) {

                    // Custom latex spreading
                    if (AbstractLatexBlock.isLatexed(targetState)) {
                        if (targetState.hasProperty(AbstractLatexBlock.COVERED)) {
                            BlockState newState = targetState.setValue(AbstractLatexBlock.COVERED, LatexType.NEUTRAL);
                            serverLevel.setBlockAndUpdate(targetPos, newState);
                            serverLevel.levelEvent(1505, targetPos, 1); // Bone meal particles
                            success = true;
                        }
                    }

                    // Consume 1 bonemeal if applied
                    if (success && !stack.isEmpty()) {
                        stack.shrink(1);
                    }
                }

                return stack;
            });
        }
    }

}
