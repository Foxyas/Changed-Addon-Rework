package net.foxyas.changedaddon.block.entity;

import net.foxyas.changedaddon.init.ChangedAddonBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class LuminarCrystalSmallBlockEntity extends PulseCrystalBlockEntity {
    public LuminarCrystalSmallBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ChangedAddonBlockEntities.LUMINAR_CRYSTAL_SMALL.get(), pPos, pBlockState);
    }
}
