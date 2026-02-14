package net.foxyas.changedaddon.procedure.blocksHandle;

import com.mojang.datafixers.util.Pair;
import net.foxyas.changedaddon.event.LatexTypePlayerEvent;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.entity.latex.LatexType;
import net.ltxprogrammer.changed.entity.latex.SpreadingLatexType;
import net.ltxprogrammer.changed.init.ChangedLatexTypes;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.world.LatexCoverState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

public class LatexBonemealAndDispenserHandler {

    /* -------------------------------------------------------------------------
     *  Core – proper latex application on a block FACE
     * ------------------------------------------------------------------------- */
    public static boolean tryApplyLatexAnyFace(
            ServerLevel level,
            BlockPos pos,
            LatexType latexType
    ) {
        // Only spreading latex types are supported
        if (!(latexType instanceof SpreadingLatexType spreading))
            return false;

        // Latex must be applied into air
        if (!level.getBlockState(pos).isAir())
            return false;

        // Try all possible faces
        for (Direction face : Direction.values()) {
            if (tryApplyLatex(level, pos, face, spreading)) {
                return true; // Stop on first valid face
            }
        }

        return false;
    }


    private static boolean tryApplyLatex(
            ServerLevel level,
            BlockPos pos,
            Direction face,
            SpreadingLatexType spreading
    ) {
        BlockState state = level.getBlockState(pos);

        // Latex is applied INTO air
        if (!state.isAir())
            return false;

        // Get the supporting block
        BlockPos supportPos = pos.relative(face.getOpposite());
        BlockState supportState = level.getBlockState(supportPos);

        // Check if latex can exist on that support face
        if (!SpreadingLatexType.canExistOnSurface(
                level,
                supportPos,
                supportState,
                pos.relative(face),
                level.getBlockState(pos.relative(face)),
                face
        )) {
            return false;
        }

        LatexCoverState originalCover = LatexCoverState.getAt(level, pos);

        var plannedCover = spreading.spreadState(
                level,
                pos,
                spreading.sourceCoverState()
        );

        var event = new SpreadingLatexType.CoveringBlockEvent(
                spreading,
                state,
                state,
                plannedCover,
                pos,
                level
        );

        spreading.defaultCoverBehavior(event);

        if (Changed.postModEvent(event))
            return false;

        if (event.originalState == event.getPlannedState()
                && event.plannedCoverState == originalCover)
            return false;

        level.setBlockAndUpdate(pos, event.getPlannedState());
        LatexCoverState.setAtAndUpdate(level, pos, event.plannedCoverState);

        level.levelEvent(1505, pos, 1);
        return true;
    }


    /* -------------------------------------------------------------------------
     *  Player – Bonemeal interaction
     * ------------------------------------------------------------------------- */
    @Mod.EventBusSubscriber
    public static class PlayerBonemealHandler {

        @SubscribeEvent
        public static void onRightClickBlock(LatexTypePlayerEvent.RightClick event) {
            Level level = event.getLevel();
            Player player = event.getPlayer();
            InteractionHand hand = event.getHand();
            ItemStack stack = player.getItemInHand(hand);

            if (level.isClientSide)
                return;

            if (!stack.is(Items.BONE_MEAL))
                return;

            BlockHitResult hitResult = event.getHitResult();

            BlockPos pos = hitResult.getBlockPos();

            // Determine latex type present on the clicked surface
            LatexCoverState latexState = event.getLatexState();
            boolean spread = trySpread(latexState, true, event.getRandom(), pos, level);
            if (spread) {
                if (!player.getAbilities().instabuild)
                    stack.shrink(1);

                // Cancel vanilla bonemeal behavior
                event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
                event.setCanceled(true);
                player.swing(hand, true);
            }
        }
    }

    /* -------------------------------------------------------------------------
     *  Dispenser – Bonemeal behavior
     * ------------------------------------------------------------------------- */
    public static void registerBonemealDispenser() {
        DispenserBlock.registerBehavior(Items.BONE_MEAL, (source, stack) -> {
            ServerLevel level = source.getLevel();
            Direction facing = source.getBlockState().getValue(DispenserBlock.FACING);

            BlockPos supportPos = source.getPos().relative(facing);
            boolean success = spreadFromSource(level, supportPos, 1, true, level.getRandom(), 1f);

            if (success && !stack.isEmpty())
                stack.shrink(1);

            return stack;
        });
    }

    public static boolean trySpread(LatexCoverState state, RandomSource random, BlockPos blockPos, Level level) {
        return trySpread(state, false, random, blockPos, level);
    }

    public static boolean trySpread(LatexCoverState state, boolean replace, RandomSource random, BlockPos blockPos, Level level) {
        boolean success = false;
        if (!(state.getType() instanceof SpreadingLatexType latexType)) return false;
        if (!latexType.canSpread(state)) return false;

        for (Direction checkDir : Direction.values()) {
            BlockPos.MutableBlockPos checkPos = blockPos.relative(checkDir).mutable();

            BlockState checkState = level.getBlockState(checkPos);
            LatexCoverState checkCoverState = LatexCoverState.getAt(level, checkPos);

            LatexType type = checkCoverState.getType();
            boolean hostileReplace = replace && type.isHostileTo(latexType);

            boolean isAirOrLessThanSpreadOrHostileTo = checkCoverState.isAir()
                    || (checkCoverState.is(latexType)) && checkCoverState.getValue(SpreadingLatexType.SATURATION) > state.getValue(SpreadingLatexType.SATURATION) + 1
                    || hostileReplace;

            if (!checkState.is(ChangedTags.Blocks.DENY_LATEX_COVER) && !checkState.isCollisionShapeFullBlock(level, checkPos) && isAirOrLessThanSpreadOrHostileTo) {
                if (checkPos.subtract(blockPos).getY() > 0 && random.nextInt(3) > 0)
                    continue;

                if (Arrays.stream(Direction.values()).noneMatch(direction -> SpreadingLatexType.canExistOnSurface(level, checkPos, level.getBlockState(checkPos.relative(direction)), checkPos.relative(direction.getOpposite()), level.getBlockState(checkPos.relative(direction.getOpposite())), direction.getOpposite())))
                    continue;

                var event = new SpreadingLatexType.CoveringBlockEvent(latexType,
                        checkState, checkState, latexType.spreadState(level, checkPos, state), checkPos, level);
                latexType.defaultCoverBehavior(event);
                if (Changed.postModEvent(event))
                    continue;

                level.setBlockAndUpdate(checkPos, event.getPlannedState());
                LatexCoverState.setAtAndUpdate(level, checkPos, event.plannedCoverState);
                level.levelEvent(1505, checkPos, 1); // particles

                event.getPostProcess().accept(level, checkPos);
                success = true;
            }
        }
        return success;
    }

    public static boolean spreadFromSource(
            ServerLevel level,
            BlockPos source,
            int maxDepth,
            boolean replaceOthers,
            RandomSource random,
            float chance
    ) {
        boolean spread = false;

        Set<BlockPos> visited = new HashSet<>();
        Queue<Pair<BlockPos, Integer>> queue = new ArrayDeque<>();

        queue.add(Pair.of(source, 0));
        visited.add(source);
        LatexCoverState mainSource = LatexCoverState.getAt(level, source);


        while (!queue.isEmpty()) {
            var current = queue.poll();
            BlockPos pos = current.getFirst();
            int depth = current.getSecond();

            if (depth > maxDepth)
                continue;

            LatexCoverState state = LatexCoverState.getAt(level, pos);
            if (state.isAir() || !state.is(mainSource.getType()))
                continue;

            if (!(state.getType() instanceof SpreadingLatexType spreading))
                continue;

            // Chance check
            if (random.nextFloat() > chance)
                continue;

            /* -------------------------------------------------
             * 2. Try spreading to neighbors
             * ------------------------------------------------- */
            boolean didSpread = trySpread(state, replaceOthers, random, pos, level);
            spread |= didSpread;

            if (didSpread) level.levelEvent(1505, pos, 1); // particles

            /* -------------------------------------------------
             * BFS expansion
             * ------------------------------------------------- */
            if (depth < maxDepth) {
                for (Direction dir : Direction.values()) {
                    BlockPos next = pos.relative(dir);
                    if (!visited.contains(next)) {
                        visited.add(next);
                        queue.add(Pair.of(next, depth + (didSpread ? 1 : 0)));
                    }
                }
            }
        }

        return spread;
    }

    public record LatexNode(BlockPos pos, LatexCoverState state) {
    }

    public static Set<LatexNode> collectConnectedLatex(
            ServerLevel level,
            BlockPos source,
            int maxDepth
    ) {
        Set<BlockPos> visited = new HashSet<>();
        Set<LatexNode> result = new HashSet<>();

        Queue<Pair<BlockPos, Integer>> queue = new ArrayDeque<>();
        queue.add(Pair.of(source, 0));
        visited.add(source);

        LatexCoverState sourceState = LatexCoverState.getAt(level, source);
        if (sourceState.isAir())
            return result;

        while (!queue.isEmpty()) {
            var entry = queue.poll();
            BlockPos pos = entry.getFirst();
            int depth = entry.getSecond();

            if (depth > maxDepth)
                continue;

            LatexCoverState state = LatexCoverState.getAt(level, pos);
            if (state.isAir() || !state.is(sourceState.getType()))
                continue;

            if (!(state.getType() instanceof SpreadingLatexType))
                continue;

            result.add(new LatexNode(pos, state));

            for (Direction dir : Direction.values()) {
                BlockPos next = pos.relative(dir);
                if (visited.add(next)) {
                    queue.add(Pair.of(next, depth + 1));
                }
            }
        }

        return result;
    }

    public static boolean cleanLatexPositions(
            ServerLevel level,
            Set<LatexNode> latexNodes
    ) {
        boolean cleaned = false;

        for (LatexNode node : latexNodes) {
            LatexCoverState state = node.state();
            if (!(state.getType() instanceof SpreadingLatexType))
                continue;

            int sat = state.getValue(SpreadingLatexType.SATURATION);

            if (sat >= 15) {
                state.randomTick(level, node.pos, level.getRandom());
                continue;
            }

            LatexCoverState newState = state.trySetValue(SpreadingLatexType.SATURATION, Math.min(sat + 1, 15));
            LatexCoverState.setAtAndUpdate(level, node.pos(), newState);
            LatexCoverState at = LatexCoverState.getAt(level, node.pos);
            if (at.getValue(SpreadingLatexType.SATURATION) >= 15)
                at.randomTick(level, node.pos, level.getRandom()); // natural degradation

            cleaned = at.isAir() || at.getValue(SpreadingLatexType.SATURATION) >= 15;
        }

        return cleaned;
    }


    public static boolean cleanFromSourceCorners(
            ServerLevel level,
            BlockPos source,
            int maxDepth
    ) {
        LatexCoverState sourceState = LatexCoverState.getAt(level, source);
        if (sourceState.isAir())
            return false;

        Set<LatexNode> all = collectConnectedLatex(level, source, maxDepth);
        if (all.isEmpty()) return false;
        cleanLatexPositions(level, all);


        return true;
    }

    public static boolean cleanLatexPositionsFromWeak(
            ServerLevel level,
            Set<LatexNode> latexNodes
    ) {
        int highest = -1;

        for (LatexNode node : latexNodes) {
            if (!(node.state().getType() instanceof SpreadingLatexType))
                continue;

            int sat = node.state().getValue(SpreadingLatexType.SATURATION);

            // nunca remove source
            if (sat == 0)
                continue;

            if (sat > highest) {
                highest = sat;
            }
        }

        if (highest < 0)
            return false;

        boolean cleaned = false;

        for (LatexNode node : latexNodes) {
            if (!(node.state().getType() instanceof SpreadingLatexType))
                continue;

            int sat = node.state().getValue(SpreadingLatexType.SATURATION);

            if (sat != highest)
                continue;

            LatexCoverState.setAtAndUpdate(
                    level,
                    node.pos(),
                    ChangedLatexTypes.NONE.get().defaultCoverState()
            );

            cleaned = true;
        }

        return cleaned;
    }


    public static boolean spreadFromSourceWaves(
            ServerLevel level,
            BlockPos source,
            int maxDepth,
            RandomSource random,
            float chance
    ) {
        boolean spread = false;

        Set<BlockPos> visited = new HashSet<>();
        visited.add(source);

        int depth = 0;
        Queue<BlockPos> current = new ArrayDeque<>();
        Queue<BlockPos> next = new ArrayDeque<>();

        current.add(source);
        visited.add(source);

        while (!current.isEmpty() && depth <= maxDepth) {
            boolean anySpread = false;

            while (!current.isEmpty()) {
                BlockPos pos = current.poll();
                LatexCoverState state = LatexCoverState.getAt(level, pos);

                if (state.isAir() || !(state.getType() instanceof SpreadingLatexType))
                    continue;

                if (random.nextFloat() > chance)
                    continue;

                boolean didSpread = trySpread(state, random, pos, level);
                anySpread |= didSpread;
                spread = anySpread;

                if (didSpread) {
                    level.levelEvent(1505, pos, 1);
                }

                for (Direction dir : Direction.values()) {
                    BlockPos nextPos = pos.relative(dir);
                    if (visited.add(nextPos)) {
                        next.add(nextPos);
                    }
                }
            }

            if (!anySpread)
                break;


            Queue<BlockPos> tmp = current;
            current = next;
            next = tmp;
            next.clear();

            depth++;
        }

        return spread;
    }


    /* -------------------------------------------------------------------------
     *  Dispenser – Latex goo application
     * ------------------------------------------------------------------------- */
    public static void registerGooDispenser(LatexType latexType, ItemStack gooItem) {
        DispenserBlock.registerBehavior(gooItem.getItem(), (source, stack) -> {
            ServerLevel level = source.getLevel();
            Direction facing = source.getBlockState()
                    .getValue(DispenserBlock.FACING);

            BlockPos targetPos = source.getPos().relative(facing);

            boolean applied = tryApplyLatexAnyFace(
                    level,
                    targetPos,
                    latexType
            );

            if (applied && !stack.isEmpty()) {
                stack.shrink(1);
            }

            return stack;
        });
    }

    /* -------------------------------------------------------------------------
     *  Dispenser – Anti-latex removal
     * ------------------------------------------------------------------------- */
    public static void registerAntiLatexDispenser(ItemStack item) {
        DispenserBlock.registerBehavior(item.getItem(), (source, stack) -> {
            ServerLevel level = source.getLevel();
            Direction facing = source.getBlockState()
                    .getValue(DispenserBlock.FACING);

            BlockPos pos = source.getPos().relative(facing);

            LatexCoverState cover = LatexCoverState.getAt(level, pos);

            // Remove any existing latex cover
            if (!cover.is(ChangedLatexTypes.NONE.get())) {
                cleanFromSourceCorners(level, pos, 24);
//                LatexCoverState.setAtAndUpdate(
//                        level,
//                        pos,
//                        ChangedLatexTypes.NONE.get().defaultCoverState()
//                );

                level.levelEvent(1505, pos, 1);

                if (!stack.isEmpty())
                    stack.shrink(1);
            }

            return stack;
        });
    }
}
