package net.foxyas.changedaddon.block;

import net.foxyas.changedaddon.block.interfaces.IStrippableLog;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LuminaraLogBlock extends RotatedPillarBlock implements IStrippableLog {

    public static BooleanProperty ACTIVE = BooleanProperty.create("active");

    protected LuminaraLogBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ACTIVE, false));
    }

    public LuminaraLogBlock() {
        this(BlockBehaviour.Properties.of()
                .mapColor((blockState) -> blockState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.TERRACOTTA_PURPLE : MapColor.COLOR_PURPLE)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F)
                .sound(SoundType.CHERRY_WOOD)
                .ignitedByLava());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(ACTIVE);
    }

    @Override
    public @NotNull BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
        return super.getStateForPlacement(pContext).setValue(ACTIVE, pContext.getPlayer() != null && pContext.getPlayer().isShiftKeyDown());
    }

    @Override
    public @Nullable BlockState getStripedVariant(BlockState originalState) {
        StrippedLuminaraLogBlock block = ChangedAddonBlocks.STRIPPED_LUMINARA_LOG.get();
        return block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, originalState.getValue(RotatedPillarBlock.AXIS)).setValue(ACTIVE, originalState.getValue(ACTIVE));
    }
}
