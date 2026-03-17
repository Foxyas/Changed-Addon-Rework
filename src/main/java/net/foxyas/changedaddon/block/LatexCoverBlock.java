package net.foxyas.changedaddon.block;

import net.foxyas.changedaddon.block.interfaces.RenderLayerProvider;
import net.foxyas.changedaddon.fluid.LitixCamoniaFluid;
import net.ltxprogrammer.changed.block.NonLatexCoverableBlock;
import net.ltxprogrammer.changed.entity.LatexType;
import net.ltxprogrammer.changed.init.ChangedGameRules;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Random;

public class LatexCoverBlock extends MultifaceBlock implements NonLatexCoverableBlock, RenderLayerProvider {

    private final LatexType latexType;
    public static final IntegerProperty DECAY_PROGRESS = IntegerProperty.create("decay_progress", 0, 15);
    public static final Integer MAX_DECAY = Collections.max(DECAY_PROGRESS.getPossibleValues());
    public static final BooleanProperty CAN_DECAY = BooleanProperty.create("can_decay");


    public LatexCoverBlock(Properties pProperties, LatexType latexType) {
        super(pProperties);
        this.latexType = latexType;
        registerDefaultState(defaultBlockState().setValue(DECAY_PROGRESS, 0).setValue(CAN_DECAY, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(DECAY_PROGRESS, CAN_DECAY);
    }

    @Override
    public boolean skipRendering(@NotNull BlockState pState, BlockState pAdjacentBlockState, @NotNull Direction pSide) {
        return pAdjacentBlockState.is(this) || super.skipRendering(pState, pAdjacentBlockState, pSide);
    }

    public LatexType getLatexType() {
        return latexType;
    }

    @Override
    public boolean isRandomlyTicking(@NotNull BlockState state) {
        return true;
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction,
                                           @NotNull BlockState neighborState, @NotNull LevelAccessor level,
                                           @NotNull BlockPos currentPos, @NotNull BlockPos neighborPos) {
        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    @Override
    public void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block blockFrom, @NotNull BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, blockFrom, fromPos, isMoving);

        // Verifica se o bloco vizinho tem água
        BlockState neighborState = level.getBlockState(fromPos);
        if (neighborState.getFluidState().isSource()
                || neighborState.getFluidState().is(Fluids.WATER)
                || neighborState.getFluidState().getType() instanceof LitixCamoniaFluid) {
            // Efeito visual opcional
            level.levelEvent(2001, pos, Block.getId(state)); // partículas de quebra
            if (neighborState.getFluidState().getType() instanceof LitixCamoniaFluid) {
                level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH,
                        SoundSource.BLOCKS, 0.5f, 1.0f);
            }

            // Remove o bloco sem dropar item
            level.destroyBlock(pos, false);
        }
    }


    private static final Direction[] DIRECTIONS = Direction.values();

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull Random random) {
        int growthRate = level.getGameRules()
                .getInt(ChangedGameRules.RULE_LATEX_GROWTH_RATE);

        if (growthRate <= 0) return;

        spread(state, level, pos);
    }

    public void spread(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos) {
        BooleanProperty prop, propRelative;
        BlockPos posRelative, attachToPos;
        BlockState stateRelative, attachToState, old, toPlace;
        for (Direction dir : DIRECTIONS) {
            prop = PipeBlock.PROPERTY_BY_DIRECTION.get(dir);
            if (!state.getValue(prop)) continue;

            for (Direction relative : DIRECTIONS) {
                if (relative == dir || relative == dir.getOpposite()) continue;
                propRelative = PipeBlock.PROPERTY_BY_DIRECTION.get(relative);
                if (state.getValue(propRelative)) continue;

                posRelative = pos.relative(relative);
                stateRelative = level.getBlockState(posRelative);

                if (!canSpreadTo(level, state, pos, posRelative, stateRelative, relative.getOpposite())) continue;

                if (cantReplace(stateRelative)) {
                    //try cover Direction relative of this
                    if (!canAttachTo(level, posRelative, stateRelative, relative.getOpposite())) continue;

                    level.setBlockAndUpdate(pos, state
                            .setValue(propRelative, true)
                            .setValue(CAN_DECAY, state.getValue(CAN_DECAY))
                            .setValue(DECAY_PROGRESS, state.getValue(CAN_DECAY) ? (Math.min(MAX_DECAY, state.getValue(DECAY_PROGRESS) + 1)) : state.getValue(DECAY_PROGRESS))
                    );
                } else {
                    //try to create a new cover block on BlockPos posRelative
                    attachToPos = pos.relative(dir).relative(relative);
                    attachToState = level.getBlockState(attachToPos);

                    if (cantReplace(attachToState)) {
                        if (!canAttachTo(level, attachToPos, attachToState, dir.getOpposite())) continue;

                        old = level.getBlockState(posRelative);//make sure that if there is a cover already, it is taken into account
                        toPlace = (old.is(this) && old.getBlock() instanceof LatexCoverBlock latexCoverBlock && latexCoverBlock.getLatexType() == this.getLatexType() ? old : defaultBlockState()).setValue(prop, true);
                        toPlace = toPlace.setValue(CAN_DECAY, state.getValue(CAN_DECAY));
                        if (toPlace.getValue(CAN_DECAY))
                            toPlace = toPlace.setValue(DECAY_PROGRESS, Math.min(MAX_DECAY, state.getValue(DECAY_PROGRESS) + 1));
                        if (old == toPlace) continue;

                        if (level.setBlockAndUpdate(posRelative, toPlace)) {
                            if (state.getValue(CAN_DECAY)) {
                                level.setBlockAndUpdate(pos, state.setValue(DECAY_PROGRESS, Math.min(MAX_DECAY, state.getValue(DECAY_PROGRESS) + 1)));
                            }
                        }
                        return;
                    }

                    attachToPos = pos.relative(dir);
                    if (!canAttachTo(level, attachToPos, level.getBlockState(attachToPos), relative)) continue;

                    posRelative = pos.relative(dir).relative(relative);
                    old = level.getBlockState(posRelative);//make sure that if there is a cover already, it is taken into account
                    toPlace = (old.is(this) && old.getBlock() instanceof LatexCoverBlock latexCoverBlock && latexCoverBlock.getLatexType() == this.getLatexType() ? old : defaultBlockState()).setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(relative.getOpposite()), true);
                    toPlace = toPlace.setValue(CAN_DECAY, state.getValue(CAN_DECAY));
                    if (toPlace.getValue(CAN_DECAY))
                        toPlace = toPlace.setValue(DECAY_PROGRESS, Math.min(MAX_DECAY, state.getValue(DECAY_PROGRESS) + 1));
                    if (old == toPlace) continue;

                    if (level.setBlockAndUpdate(posRelative, toPlace)) {
                        if (state.getValue(CAN_DECAY)) {
                            level.setBlockAndUpdate(pos, state.setValue(DECAY_PROGRESS, Math.min(MAX_DECAY, state.getValue(DECAY_PROGRESS) + 1)));
                        }
                    }
                }

                return;
            }
        }
    }

    @Override
    protected boolean canAttachTo(LevelAccessor level, BlockPos attachToPos, BlockState attachTo, Direction attachToFace) {
        if (attachTo.getBlock() instanceof NonLatexCoverableBlock) {
            return false;
        }

        return super.canAttachTo(level, attachToPos, attachTo, attachToFace);
    }

    public boolean canSpreadTo(LevelAccessor level,
                                  @NotNull BlockState currentState, @NotNull BlockPos currentPos, BlockPos attachToPos,
                                  BlockState attachTo,
                                  Direction attachToFace) {
        if (!(level instanceof ServerLevel serverLevel))
            return false;

        if (!serverLevel.isAreaLoaded(attachToPos, 3))
            return false;

        int growthRate = serverLevel.getGameRules()
                .getInt(ChangedGameRules.RULE_LATEX_GROWTH_RATE);

        if (growthRate <= 0)
            return false;

        if (currentState.hasProperty(CAN_DECAY) && currentState.getValue(CAN_DECAY)) {
            if (currentState.hasProperty(DECAY_PROGRESS)) {
                Integer value = currentState.getValue(DECAY_PROGRESS);
                int max = MAX_DECAY;
                if (value + 1 > max) {
                    return false;
                }
            }
        }

        // chance baseada na gamerule
        return serverLevel.random.nextInt(100) < growthRate;
    }


    protected boolean cantReplace(BlockState state) {
        return !state.getMaterial().isReplaceable();// || state.is(Blocks.REDSTONE_WIRE);
    }
}
