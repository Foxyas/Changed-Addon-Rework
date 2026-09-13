package net.foxyas.changedaddon.block;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;

public class LuminaraPetalsBlock extends PinkPetalsBlock {
    public static final BooleanProperty GLOWING = BooleanProperty.create("glowing");

    public LuminaraPetalsBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.PINK_PETALS));

        this.registerDefaultState(this.defaultBlockState().setValue(GLOWING, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(GLOWING);
    }

    @Override
    public @NotNull BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state.setValue(GLOWING, false);
    }
}