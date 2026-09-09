package net.foxyas.changedaddon.block;

import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class StrippedLuminaraWoodBlock extends LuminaraWoodBlock {

    public StrippedLuminaraWoodBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable BlockState getStripedVariant(BlockState state) {
        return null;
    }
}
