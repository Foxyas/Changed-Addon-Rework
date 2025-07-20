package net.foxyas.changedaddon.block.entity;

import net.ltxprogrammer.changed.block.NonLatexCoverableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.Capability;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import net.foxyas.changedaddon.init.ChangedAddonBlockEntities;

import javax.annotation.Nullable;

public class DarklatexpuddleBlockEntity extends BlockEntity implements NonLatexCoverableBlock {
	public DarklatexpuddleBlockEntity(BlockPos position, BlockState state) {
		super(ChangedAddonBlockEntities.DARKLATEXPUDDLE.get(), position, state);
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
	}

	@Override
	public void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag() {
		return this.saveWithFullMetadata();
	}


	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
		return super.getCapability(capability, facing);
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
	}
}
