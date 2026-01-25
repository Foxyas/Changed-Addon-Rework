package net.foxyas.changedaddon.block;

import net.foxyas.changedaddon.util.VoxelShapeCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class StackableCanBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final IntegerProperty CANS = IntegerProperty.create("cans", 1, 4);

    protected static final VoxelShape SHAPE_1 =
            Shapes.box(0.375f, 0, 0.375f, 0.625f, 0.4375f, 0.625f);
    protected static final VoxelShape SHAPE_2 = Shapes.or(
            Shapes.box(0.5312f, 0, 0.3438f, 0.7812f, 0.4375f, 0.5938f),
            Shapes.box(0.2188f, 0, 0.2188f, 0.4688f, 0.4375f, 0.4688f));
    protected static final VoxelShape SHAPE_3 = Shapes.or(
            SHAPE_2,
            Shapes.box(0.2188f, 0, 0.5312f, 0.4688f, 0.4375f, 0.7812f));
    protected static final VoxelShape SHAPE_4 = Shapes.or(
            SHAPE_3,
            Shapes.box(0.3438f, 0.4375f, 0.4062f, 0.5938f, 0.875f, 0.6562f));

    protected static final VoxelShapeCache CACHE = new VoxelShapeCache();

    protected StackableCanBlock(Properties pProperties) {
        super(pProperties.color(MaterialColor.COLOR_LIGHT_GRAY)
                .sound(SoundType.METAL)
                .strength(0.1f, 10f)
                .noOcclusion()
                .sound(SoundType.METAL).strength(0.1f, 10f)
                .noOcclusion()
                .isRedstoneConductor((bs, br, bp) -> false));

        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false).setValue(CANS, 1));
    }

    @Override
    public @NotNull VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        int cans = pState.getValue(CANS);
        return CACHE.getShape(pState.getValue(FACING), cans,
            switch (cans) {
                case 2 -> SHAPE_2;
                case 3 -> SHAPE_3;
                case 4 -> SHAPE_4;
                default -> SHAPE_1;
            }
        );
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        BlockState below = level.getBlockState(belowPos);
        return below.isFaceSturdy(level, belowPos, Direction.UP);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean moving) {
        if (!canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
        }
        super.neighborChanged(state, level, pos, block, fromPos, moving);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        BlockState blockstate = level.getBlockState(clickedPos);
        if (blockstate.is(this)) {
            return blockstate.setValue(CANS, Math.min(4, blockstate.getValue(CANS) + 1));
        }

        boolean flag = level.getFluidState(clickedPos).getType() == Fluids.WATER;
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, flag);
    }

    @Override
    public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
        return pUseContext.getItemInHand().is(asItem()) && pState.getValue(CANS) < 4;
    }

    @Override
    public float[] getBeaconColorMultiplier(BlockState state, LevelReader world, BlockPos pos, BlockPos beaconPos) {
        return new float[]{0.6f, 0.6f, 0.6f};
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        return super.updateShape(state, facing, facingState, world, currentPos, facingPos);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return state.getFluidState().isEmpty();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACING, WATERLOGGED, CANS));
    }
}
