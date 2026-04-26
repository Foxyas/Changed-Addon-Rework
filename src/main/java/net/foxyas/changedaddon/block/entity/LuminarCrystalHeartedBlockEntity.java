package net.foxyas.changedaddon.block.entity;

import net.foxyas.changedaddon.block.LuminarCrystalSmall;
import net.foxyas.changedaddon.init.ChangedAddonBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LuminarCrystalHeartedBlockEntity extends AggedBlockEntity {
    public LuminarCrystalHeartedBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ChangedAddonBlockEntities.LUMINAR_CRYSTAL_HEARTED.get(), pPos, pBlockState);
    }

    @Override
    public void tick(Level level, BlockPos pPos, BlockState state) {
        super.tick(level, pPos, state);
        if (!state.hasProperty(LuminarCrystalSmall.HEARTED) || !state.getValue(LuminarCrystalSmall.HEARTED)) {
            this.setRemoved();
        }
    }

    public int getAgeTicks() {
        return this.age;
    }
}
