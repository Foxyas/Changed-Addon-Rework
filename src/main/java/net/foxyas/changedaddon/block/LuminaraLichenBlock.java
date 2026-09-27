package net.foxyas.changedaddon.block;

import net.foxyas.changedaddon.block.advanced.MultifaceBlock;
import net.ltxprogrammer.changed.block.AbstractLatexBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
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

public class LuminaraLichenBlock extends MultifaceBlock implements SimpleWaterloggedBlock, BonemealableBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty GLOWING = BooleanProperty.create("glowing");

    private final MultifaceSpreader spreader = new MultifaceSpreader(new DefaultSpreaderConfig(this));

    public static class DefaultSpreaderConfig implements MultifaceSpreader.SpreadConfig {
        protected final MultifaceBlock block;

        public DefaultSpreaderConfig(MultifaceBlock pBlock) {
            this.block = pBlock;
        }

        @Override
        @Nullable
        public BlockState getStateForPlacement(@NotNull BlockState pCurrentState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull Direction pLookingDirection) {
            BooleanProperty prop = MultifaceBlock.getFaceProperty(pLookingDirection);
            if (prop == null) return null;

            // Se o bloco no destino já for este bloco, mantemos o estado atual, caso contrário usamos o defaultState
            BlockState targetState = pCurrentState.is(this.block) ? pCurrentState : this.block.defaultBlockState();

            // Se a face já estiver ativada, cancela o placement nessa face
            if (targetState.hasProperty(prop) && targetState.getValue(prop)) {
                return null;
            }

            // Retorna o estado atualizado com a nova face ativada
            return targetState.setValue(prop, true);
        }

        protected boolean stateCanBeReplaced(BlockGetter pLevel, BlockPos pPos, BlockPos pSpreadPos, Direction pDirection, BlockState pState) {
            return pState.isAir() || pState.is(this.block) || (pState.is(Blocks.WATER) && pState.getFluidState().isSource());
        }

        @Override
        public boolean canSpreadInto(BlockGetter pLevel, @NotNull BlockPos pPos, MultifaceSpreader.SpreadPos pSpreadPos) {
            BlockState targetState = pLevel.getBlockState(pSpreadPos.pos());

            if (!this.stateCanBeReplaced(pLevel, pPos, pSpreadPos.pos(), pSpreadPos.face(), targetState)) {
                return false;
            }

            if (targetState.is(this.block) && MultifaceBlock.hasFace(targetState, pSpreadPos.face())) {
                return false;
            }

            BlockPos attachToPos = pSpreadPos.pos().relative(pSpreadPos.face());
            BlockState attachToState = pLevel.getBlockState(attachToPos);

            if (pLevel instanceof LevelAccessor accessor) {
                return this.block.canAttachTo(accessor, attachToPos, attachToState, pSpreadPos.face().getOpposite());
            }

            return Block.isFaceFull(attachToState.getShape(pLevel, attachToPos), pSpreadPos.face().getOpposite());
        }
    }

    public LuminaraLichenBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.GLOW_LICHEN)
                .noCollission()
                .strength(0.2F)
                .sound(SoundType.GLOW_LICHEN)
                .dynamicShape().ignitedByLava().pushReaction(PushReaction.DESTROY));
        registerDefaultState(defaultBlockState().setValue(GLOWING, false).setValue(WATERLOGGED, false));
    }

    public boolean isValidBonemealTarget(@NotNull LevelReader pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, boolean pIsClient) {
        return Direction.stream().anyMatch((dir) -> this.spreader.canSpreadInAnyDirection(pState, pLevel, pPos, dir.getOpposite()));
    }

    public boolean isBonemealSuccess(@NotNull Level pLevel, @NotNull RandomSource pRandom, @NotNull BlockPos pPos, @NotNull BlockState pState) {
        return true;
    }

    public void performBonemeal(@NotNull ServerLevel pLevel, @NotNull RandomSource pRandom, @NotNull BlockPos pPos, @NotNull BlockState pState) {
        this.spreader.spreadFromRandomFaceTowardRandomDirection(pState, pLevel, pPos, pRandom);
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        return super.canSurvive(state, level, pos);
    }

    @Override
    public boolean canAttachTo(LevelAccessor level, BlockPos attachToPos, BlockState attachTo, Direction attachToFace) {
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

    public @NotNull FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(WATERLOGGED, GLOWING));
    }
}
