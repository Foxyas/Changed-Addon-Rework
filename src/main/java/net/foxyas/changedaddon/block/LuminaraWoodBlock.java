package net.foxyas.changedaddon.block;

import net.foxyas.changedaddon.block.interfaces.IStrippableLog;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class LuminaraWoodBlock extends LuminaraLogBlock implements IStrippableLog {

    public LuminaraWoodBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable BlockState getStripedVariant(BlockState state) {
        return ChangedAddonBlocks.STRIPPED_LUMINARA_WOOD.get().defaultBlockState();
    }
}
