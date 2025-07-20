package net.foxyas.changedaddon.block.entity;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;

import net.foxyas.changedaddon.init.ChangedAddonBlockEntities;

public class WolfPlushBlockEntity extends BlockEntity {
	private static final String SQUEEZED_TAG = "squeezedTicks";
	public int squeezedTicks;

	public WolfPlushBlockEntity(BlockPos position, BlockState state) {
		super(ChangedAddonBlockEntities.WOLF_PLUSH.get(), position, state);
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		if (compound.contains(SQUEEZED_TAG)) {
			this.squeezedTicks = compound.getInt(SQUEEZED_TAG);
		}
	}

	@Override
	public void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		compound.putInt(SQUEEZED_TAG, this.squeezedTicks);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag() {
		return this.saveWithFullMetadata();
	}

	public boolean isSqueezed() {
		return this.squeezedTicks > 0;
	}

	public void subSqueezedTicks(int i) {
		this.squeezedTicks = this.squeezedTicks - i;
	}
}
