package net.foxyas.changedaddon.block;

import com.google.common.collect.Maps;
import net.minecraft.Util;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
//TODO turn into a block entity
public class LuminaraLichen extends Block implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape UP_AABB = Block.box(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape DOWN_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    protected static final VoxelShape WEST_AABB = Block.box(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
    protected static final VoxelShape EAST_AABB = Block.box(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape NORTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
    protected static final VoxelShape SOUTH_AABB = Block.box(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
    protected static final IntegerProperty FLOWERS_UP = IntegerProperty.create("flowers_up", 0, 4);
    protected static final IntegerProperty FLOWERS_DOWN = IntegerProperty.create("flowers_down", 0, 4);
    protected static final IntegerProperty FLOWERS_WEST = IntegerProperty.create("flowers_west", 0, 4);
    protected static final IntegerProperty FLOWERS_EAST = IntegerProperty.create("flowers_east", 0, 4);
    protected static final IntegerProperty FLOWERS_NORTH = IntegerProperty.create("flowers_north", 0, 4);
    protected static final IntegerProperty FLOWERS_SOUTH = IntegerProperty.create("flowers_south", 0, 4);

    public static final Map<Direction, IntegerProperty> PROPERTY_BY_DIRECTION = Util.make(Maps.newEnumMap(Direction.class), (p_153923_) -> {
        p_153923_.put(Direction.NORTH, FLOWERS_NORTH);
        p_153923_.put(Direction.EAST, FLOWERS_EAST);
        p_153923_.put(Direction.SOUTH, FLOWERS_SOUTH);
        p_153923_.put(Direction.WEST, FLOWERS_WEST);
        p_153923_.put(Direction.UP, FLOWERS_UP);
        p_153923_.put(Direction.DOWN, FLOWERS_DOWN);
    });
    protected static final Map<VoxelShape, IntegerProperty> PROPERTY_BY_SHAPE = Util.make(new HashMap<>(), (p_153923_) -> {
        p_153923_.put(NORTH_AABB, FLOWERS_NORTH);
        p_153923_.put(EAST_AABB, FLOWERS_EAST);
        p_153923_.put(SOUTH_AABB, FLOWERS_SOUTH);
        p_153923_.put(WEST_AABB, FLOWERS_WEST);
        p_153923_.put(UP_AABB, FLOWERS_UP);
        p_153923_.put(DOWN_AABB, FLOWERS_DOWN);
    });

    private static final Map<BlockState, VoxelShape> SHAPE_CACHE = new ConcurrentHashMap<>();

    public LuminaraLichen(Properties pProperties) {
        super(pProperties);

        BlockState defState = defaultBlockState();
        for (IntegerProperty prop : PROPERTY_BY_DIRECTION.values()) {
            defState = defState.setValue(prop, 0);
        }
        registerDefaultState(defState.setValue(WATERLOGGED, false));
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE_CACHE.computeIfAbsent(state, s -> {
            VoxelShape res = Shapes.empty();

            if (state.getValue(FLOWERS_UP) > 0) res = Shapes.joinUnoptimized(res, UP_AABB, BooleanOp.OR);
            if (state.getValue(FLOWERS_WEST) > 0) res = Shapes.joinUnoptimized(res, WEST_AABB, BooleanOp.OR);
            if (state.getValue(FLOWERS_EAST) > 0) res = Shapes.joinUnoptimized(res, EAST_AABB, BooleanOp.OR);
            if (state.getValue(FLOWERS_NORTH) > 0) res = Shapes.joinUnoptimized(res, NORTH_AABB, BooleanOp.OR);
            if (state.getValue(FLOWERS_SOUTH) > 0) res = Shapes.joinUnoptimized(res, SOUTH_AABB, BooleanOp.OR);
            if (state.getValue(FLOWERS_DOWN) > 0) res = Shapes.joinUnoptimized(res, DOWN_AABB, BooleanOp.OR);

            return res;
        });
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos currentPos, @NotNull BlockPos neighborPos) {
        IntegerProperty prop;
        boolean empty = true;
        for (Direction dir : Direction.values()) {
            prop = PROPERTY_BY_DIRECTION.get(dir);
            if (state.getValue(prop) == 0) continue;

            BlockPos rel = currentPos.relative(dir);
            if (!canAttachTo(level, rel, level.getBlockState(rel), dir.getOpposite())) {
                state = state.setValue(prop, 0);
                if (level instanceof Level l) Block.popResource(l, currentPos, new ItemStack(this));
                continue;
            }

            empty = false;
        }

        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
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
        IntegerProperty prop = PROPERTY_BY_DIRECTION.get(dir.getOpposite());

        if (state.is(this)) {
            BlockPos clickedBlock = context.getClickedPos();
            Vec3 clickPos = context.getClickLocation().subtract(clickedBlock.getX(), clickedBlock.getY(), clickedBlock.getZ());
            VoxelShape shape = Shapes.create(new AABB(clickPos, clickPos).inflate(0.01D));
            int flowers;
            for (Map.Entry<VoxelShape, IntegerProperty> entry : PROPERTY_BY_SHAPE.entrySet()) {
                if (!Shapes.joinIsNotEmpty(shape, entry.getKey(), BooleanOp.AND)) continue;

                flowers = state.getValue(entry.getValue());
                if (flowers == 4) return null;

                BlockPos rel = context.getClickedPos().relative(dir.getOpposite());
                if (flowers != 0 || canAttachTo(level, rel, level.getBlockState(rel), dir)) {
                    return state.setValue(entry.getValue(), flowers + 1);
                } else return state;
            }

            return null;
        }

        BlockPos rel = context.getClickedPos().relative(dir.getOpposite());
        if (!canAttachTo(level, rel, level.getBlockState(rel), dir)) return null;

        return super.getStateForPlacement(context).setValue(prop, 1).setValue(WATERLOGGED, level.isWaterAt(context.getClickedPos()));
    }

    public boolean canBeReplaced(@NotNull BlockState state, @NotNull BlockPlaceContext context) {
        if (super.canBeReplaced(state, context)) return true;
        if (!context.getItemInHand().is(asItem())) return false;

        BlockPos clickedBlock = context.getClickedPos();
        Vec3 clickPos = context.getClickLocation().subtract(clickedBlock.getX(), clickedBlock.getY(), clickedBlock.getZ());
        VoxelShape shape = Shapes.create(new AABB(clickPos, clickPos).inflate(0.01D));
        for (Map.Entry<VoxelShape, IntegerProperty> entry : PROPERTY_BY_SHAPE.entrySet()) {
            if (Shapes.joinIsNotEmpty(shape, entry.getKey(), BooleanOp.AND)) {
                return state.getValue(entry.getValue()) < 4;
            }
        }

        return false;
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        for (IntegerProperty prop : PROPERTY_BY_DIRECTION.values()) {
            if (state.getValue(prop) > 0) return true;
        }

        return false;
    }

    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FLOWERS_UP, FLOWERS_NORTH, FLOWERS_EAST, FLOWERS_SOUTH, FLOWERS_WEST, FLOWERS_DOWN, WATERLOGGED));
    }

    public @NotNull BlockState rotate(@NotNull BlockState state, Rotation pRotate) {
        return switch (pRotate) {
            case CLOCKWISE_180 ->
                    state.setValue(FLOWERS_NORTH, state.getValue(FLOWERS_SOUTH)).setValue(FLOWERS_EAST, state.getValue(FLOWERS_WEST)).setValue(FLOWERS_SOUTH, state.getValue(FLOWERS_NORTH)).setValue(FLOWERS_WEST, state.getValue(FLOWERS_EAST));
            case COUNTERCLOCKWISE_90 ->
                    state.setValue(FLOWERS_NORTH, state.getValue(FLOWERS_EAST)).setValue(FLOWERS_EAST, state.getValue(FLOWERS_SOUTH)).setValue(FLOWERS_SOUTH, state.getValue(FLOWERS_WEST)).setValue(FLOWERS_WEST, state.getValue(FLOWERS_NORTH));
            case CLOCKWISE_90 ->
                    state.setValue(FLOWERS_NORTH, state.getValue(FLOWERS_WEST)).setValue(FLOWERS_EAST, state.getValue(FLOWERS_NORTH)).setValue(FLOWERS_SOUTH, state.getValue(FLOWERS_EAST)).setValue(FLOWERS_WEST, state.getValue(FLOWERS_SOUTH));
            default -> state;
        };
    }

    public @NotNull BlockState mirror(@NotNull BlockState state, Mirror pMirror) {
        return switch (pMirror) {
            case LEFT_RIGHT -> state.setValue(FLOWERS_NORTH, state.getValue(FLOWERS_SOUTH)).setValue(FLOWERS_SOUTH, state.getValue(FLOWERS_NORTH));
            case FRONT_BACK -> state.setValue(FLOWERS_EAST, state.getValue(FLOWERS_WEST)).setValue(FLOWERS_WEST, state.getValue(FLOWERS_EAST));
            default -> state;
        };
    }
}
