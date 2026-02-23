package net.foxyas.changedaddon.block.entity;

import net.foxyas.changedaddon.init.ChangedAddonBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import static net.foxyas.changedaddon.block.interfaces.ConditionalLatexCoverableBlock.NonLatexCoverableBlock;

public class DarkLatexPuddleBlockEntity extends BlockEntity implements NonLatexCoverableBlock {

    public byte cooldown = 0;

    public DarkLatexPuddleBlockEntity(BlockPos position, BlockState state) {
        super(ChangedAddonBlockEntities.DARK_LATEX_PUDDLE.get(), position, state);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putByte("cooldown", cooldown);
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("cooldown")) cooldown = pTag.getByte("cooldown");
    }
}
