package net.foxyas.changedaddon.block;

import net.ltxprogrammer.changed.block.AbstractLatexBlock;
import net.ltxprogrammer.changed.block.WhiteLatexTransportInterface;
import net.ltxprogrammer.changed.entity.LatexType;
import net.ltxprogrammer.changed.init.ChangedBlocks;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

public class DormantWhiteLatexBlock extends AbstractLatexBlock implements WhiteLatexTransportInterface {
    public DormantWhiteLatexBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ).sound(SoundType.SLIME_BLOCK).strength(1.0F, 4.0F).noOcclusion().isViewBlocking(ChangedBlocks::never).isSuffocating(ChangedBlocks::never), LatexType.WHITE_LATEX,
                ChangedItems.WHITE_LATEX_GOO);
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos position, @NotNull RandomSource random) {
    }
}
