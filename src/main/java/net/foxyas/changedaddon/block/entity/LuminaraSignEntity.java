package net.foxyas.changedaddon.block.entity;

import net.foxyas.changedaddon.init.ChangedAddonBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LuminaraSignEntity extends SignBlockEntity {

    public LuminaraSignEntity(BlockPos pPos, BlockState pBlockState) {
        super(ChangedAddonBlockEntities.LUMINARA_SIGN.get(), pPos, pBlockState);
    }
}
