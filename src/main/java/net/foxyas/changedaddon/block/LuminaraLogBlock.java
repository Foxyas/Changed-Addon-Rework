package net.foxyas.changedaddon.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class LuminaraLogBlock extends RotatedPillarBlock {

    public LuminaraLogBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor((blockState) -> blockState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.TERRACOTTA_PURPLE : MapColor.COLOR_PURPLE)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F)
                .sound(SoundType.CHERRY_WOOD)
                .ignitedByLava());
    }
}
