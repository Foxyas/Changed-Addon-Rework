package net.foxyas.changedaddon.block.interfaces;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface IStrippableLog {

    @Nullable
    BlockState getStripedVariant(BlockState state); // Blockstate here is 50% of the time useless, but we keep it for dynamic features.
}
