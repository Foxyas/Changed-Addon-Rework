package net.foxyas.changedaddon.block.entity;

import net.foxyas.changedaddon.init.ChangedAddonBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LuminaraHangingSignEntity extends HangingSignBlockEntity {

    public LuminaraHangingSignEntity(BlockPos pos, BlockState state) {
        super(ChangedAddonBlockEntities.LUMINARA_HANGING_SIGN.get(), pos, state);
    }
}
