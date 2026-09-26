package net.foxyas.changedaddon.block;

import net.ltxprogrammer.changed.block.AbstractLatexBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LuminaraLichenBlock extends MultifaceBlock implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty GLOWING = BooleanProperty.create("glowing");

    public LuminaraLichenBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.GLOW_LICHEN)
                .noCollission()
                .strength(0.2F)
                .sound(SoundType.GLOW_LICHEN)
                .dynamicShape().ignitedByLava().pushReaction(PushReaction.DESTROY));
        registerDefaultState(defaultBlockState().setValue(GLOWING, false).setValue(WATERLOGGED, false));
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        return super.canSurvive(state, level, pos);
    }

    @Override
    protected boolean canAttachTo(LevelAccessor level, BlockPos attachToPos, BlockState attachTo, Direction attachToFace) {
        boolean superCanAttachTo = super.canAttachTo(level, attachToPos, attachTo, attachToFace);
        if (attachTo.getBlock() instanceof AbstractLatexBlock) {
            return superCanAttachTo;
        }
        return superCanAttachTo;
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos currentPos, @NotNull BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state != null) {
            Level level = context.getLevel();
            BlockPos clickedPos = context.getClickedPos();
            boolean startGlowing = (level.canSeeSky(clickedPos) && level.isNight()) || (level.getMaxLocalRawBrightness(clickedPos) <= 4);
            return state.setValue(WATERLOGGED, level.isWaterAt(clickedPos)).setValue(GLOWING, startGlowing);
        } else {
            return null;
        }
    }

    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(WATERLOGGED, GLOWING));
    }
}
