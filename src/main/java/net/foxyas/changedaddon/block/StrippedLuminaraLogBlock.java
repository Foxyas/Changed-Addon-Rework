package net.foxyas.changedaddon.block;

import net.foxyas.changedaddon.block.interfaces.IStrippableLog;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

public class StrippedLuminaraLogBlock extends LuminaraLogBlock {

    public StrippedLuminaraLogBlock() {
        super(Properties.of()
                .mapColor((blockState) -> blockState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.COLOR_LIGHT_GRAY : MapColor.TERRACOTTA_LIGHT_GRAY)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F)
                .sound(SoundType.CHERRY_WOOD)
                .ignitedByLava());
    }
}
