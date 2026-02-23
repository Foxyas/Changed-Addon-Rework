package net.foxyas.changedaddon.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MultifaceBlock extends Block {

    public static final BooleanProperty UP = PipeBlock.UP;
    public static final BooleanProperty NORTH = PipeBlock.NORTH;
    public static final BooleanProperty EAST = PipeBlock.EAST;
    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty WEST = PipeBlock.WEST;
    public static final BooleanProperty DOWN = PipeBlock.DOWN;

    private static final VoxelShape UP_AABB = Block.box(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape WEST_AABB = Block.box(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
    private static final VoxelShape EAST_AABB = Block.box(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape NORTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
    private static final VoxelShape SOUTH_AABB = Block.box(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape DOWN_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

    private static final Map<BlockState, VoxelShape> SHAPE_CACHE = new ConcurrentHashMap<>(); //Changed To ConcurrentHashMap to fix `ConcurrentModificationException : null`

    public MultifaceBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState().setValue(UP, false)
                .setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false)
                .setValue(DOWN, false));
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE_CACHE.computeIfAbsent(state, s -> {
            VoxelShape res = Shapes.empty();

            if (state.getValue(UP)) res = Shapes.joinUnoptimized(res, UP_AABB, BooleanOp.OR);
            if (state.getValue(WEST)) res = Shapes.joinUnoptimized(res, WEST_AABB, BooleanOp.OR);
            if (state.getValue(EAST)) res = Shapes.joinUnoptimized(res, EAST_AABB, BooleanOp.OR);
            if (state.getValue(NORTH)) res = Shapes.joinUnoptimized(res, NORTH_AABB, BooleanOp.OR);
            if (state.getValue(SOUTH)) res = Shapes.joinUnoptimized(res, SOUTH_AABB, BooleanOp.OR);
            if (state.getValue(DOWN)) res = Shapes.joinUnoptimized(res, DOWN_AABB, BooleanOp.OR);

            return res;
        });
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos currentPos, @NotNull BlockPos neighborPos) {
        BooleanProperty prop;
        boolean empty = true;
        for (Direction dir : Direction.values()) {
            prop = PipeBlock.PROPERTY_BY_DIRECTION.get(dir);
            if (!state.getValue(prop)) continue;

            BlockPos rel = currentPos.relative(dir);
            if (!canAttachTo(level, rel, level.getBlockState(rel), dir.getOpposite())) {
                state = state.setValue(prop, false);
                if (level instanceof Level l) Block.popResource(l, currentPos, new ItemStack(this));
                continue;
            }

            empty = false;
        }

        return empty ? Blocks.AIR.defaultBlockState() : state;
    }

    protected boolean canAttachTo(LevelAccessor level, BlockPos attachToPos, BlockState attachTo, Direction attachToFace) {
        return Block.isFaceFull(attachTo.getShape(level, attachToPos), attachToFace);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockState state = level.getBlockState(context.getClickedPos());
        Direction dir = context.getClickedFace();
        BooleanProperty prop = PipeBlock.PROPERTY_BY_DIRECTION.get(dir.getOpposite());

        if (state.is(this)) {
            if (state.getValue(prop)) return null;

            BlockPos rel = context.getClickedPos().relative(dir.getOpposite());
            if (canAttachTo(level, rel, level.getBlockState(rel), dir)) state = state.setValue(prop, true);
            return state;
        }

        BlockPos rel = context.getClickedPos().relative(dir.getOpposite());
        if (!canAttachTo(level, rel, level.getBlockState(rel), dir)) return null;

        return super.getStateForPlacement(context).setValue(prop, true);
    }

    public boolean canBeReplaced(@NotNull BlockState state, @NotNull BlockPlaceContext context) {
        if (super.canBeReplaced(state, context)) return true;
        if (!context.getItemInHand().is(asItem())) return false;

        return !state.getValue(PipeBlock.PROPERTY_BY_DIRECTION.get(context.getClickedFace().getOpposite()));
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        for (BooleanProperty prop : PipeBlock.PROPERTY_BY_DIRECTION.values()) {
            if (state.getValue(prop)) return true;
        }

        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(UP, NORTH, EAST, SOUTH, WEST, DOWN));
    }

    public @NotNull BlockState rotate(@NotNull BlockState state, Rotation pRotate) {
        return switch (pRotate) {
            case CLOCKWISE_180 ->
                    state.setValue(NORTH, state.getValue(SOUTH)).setValue(EAST, state.getValue(WEST)).setValue(SOUTH, state.getValue(NORTH)).setValue(WEST, state.getValue(EAST));
            case COUNTERCLOCKWISE_90 ->
                    state.setValue(NORTH, state.getValue(EAST)).setValue(EAST, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(WEST)).setValue(WEST, state.getValue(NORTH));
            case CLOCKWISE_90 ->
                    state.setValue(NORTH, state.getValue(WEST)).setValue(EAST, state.getValue(NORTH)).setValue(SOUTH, state.getValue(EAST)).setValue(WEST, state.getValue(SOUTH));
            default -> state;
        };
    }

    public @NotNull BlockState mirror(@NotNull BlockState state, Mirror pMirror) {
        return switch (pMirror) {
            case LEFT_RIGHT -> state.setValue(NORTH, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(NORTH));
            case FRONT_BACK -> state.setValue(EAST, state.getValue(WEST)).setValue(WEST, state.getValue(EAST));
            default -> state;
        };
    }
}
