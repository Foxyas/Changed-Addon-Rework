package net.foxyas.changedaddon.block.entity;

import net.foxyas.changedaddon.registers.ChangedAddonRegisters;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SnepPlushBlockEntity extends BlockEntity {
    private static final String SQUEEZED_TAG = "squeezedTicks";
    public int squeezedTicks;
    //public double poseX;
    //public double poseY;
    //public double poseZ;


    public SnepPlushBlockEntity(BlockPos position, BlockState state) {
        super(ChangedAddonRegisters.ChangedAddonBlockEntities.SNEP_PLUSH.get(), position, state);
        this.squeezedTicks = 0;
        //this.poseX = 0;
        //this.poseY = 0;
        //this.poseZ = 0;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        if (compound.contains(SQUEEZED_TAG)) {
            this.squeezedTicks = compound.getInt(SQUEEZED_TAG);
        }

		/*
		*
		if (compound.contains("PoseX")) {
			this.poseX = compound.getDouble("PoseX");
		}

		if (compound.contains("PoseY")) {
			this.poseY = compound.getDouble("PoseY");
		}

		if (compound.contains("PoseZ")) {
			this.poseZ = compound.getDouble("PoseZ");
		}
		*/
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt(SQUEEZED_TAG, this.squeezedTicks);
        //compound.putDouble("PoseX",this.poseX);
        ///compound.putDouble("PoseY",this.poseY);
        //compound.putDouble("PoseZ",this.poseZ);
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
