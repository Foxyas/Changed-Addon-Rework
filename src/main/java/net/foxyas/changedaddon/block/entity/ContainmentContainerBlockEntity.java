package net.foxyas.changedaddon.block.entity;

import net.foxyas.changedaddon.registers.ChangedAddonRegisters;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ContainmentContainerBlockEntity extends BlockEntity {


	private static final String TRANSFUR_VARIANT = "form";

	@Nullable
	private TransfurVariant<?> transfurVariant;
	//public double poseX;
	//public double poseY;
	//public double poseZ;

	public ContainmentContainerBlockEntity(BlockPos position, BlockState state) {
		super(ChangedAddonRegisters.ChangedAddonBlockEntities.CONTAINMENT_CONTAINER.get(), position, state);
		this.transfurVariant = null;
		//this.poseX = 0;
		//this.poseY = 0;
		//this.poseZ = 0;
	}


	public void setTransfurVariant(@Nullable TransfurVariant<?> variant){
		this.transfurVariant = variant;
	}

	public @Nullable TransfurVariant<?> getTransfurVariant() {
		return transfurVariant;
	}

	public boolean isTransfurNotLatex(@Nullable TransfurVariant<?> variant){
		if (variant == null){
			return true;
		}
		return !variant.getEntityType().is(ChangedTags.EntityTypes.LATEX);
	}

	public boolean isTransfurGasLike(@Nullable TransfurVariant<?> variant){
		if (variant == null){
			return false;
		}
		return !variant.is(ChangedTransfurVariants.GAS_WOLF.get())
				|| variant.getRegistryName().toString().contains("gas");
	}

	@Override
	public void load(@NotNull CompoundTag compound) {
		super.load(compound);

		if (compound.contains(TRANSFUR_VARIANT)){
			ResourceLocation form;
			try {
				form = new ResourceLocation(compound.getString(TRANSFUR_VARIANT));
			} catch (Exception e) {
				 form = new ResourceLocation("");
            }
            if (TransfurVariant.getPublicTransfurVariants().map(TransfurVariant::getRegistryName).anyMatch(form::equals)){
				this.transfurVariant = ChangedRegistry.TRANSFUR_VARIANT.get().getValue(form);
			}
		}

		/*if (compound.contains("PoseX")) {
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
	public void saveAdditional(@NotNull CompoundTag compound) {
		super.saveAdditional(compound);
		if (this.transfurVariant != null && transfurVariant.getRegistryName() != null){
        	compound.putString(TRANSFUR_VARIANT,transfurVariant.getRegistryName().toString());
		}

		//compound.putDouble("PoseX",this.poseX);
		//compound.putDouble("PoseY",this.poseY);
		//compound.putDouble("PoseZ",this.poseZ);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public @NotNull CompoundTag getUpdateTag() {
		return this.saveWithFullMetadata();
	}
}
