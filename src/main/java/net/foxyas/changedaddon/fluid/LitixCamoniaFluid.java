
package net.foxyas.changedaddon.fluid;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonFluids;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.jetbrains.annotations.NotNull;

public abstract class LitixCamoniaFluid extends ForgeFlowingFluid {

	public static final ForgeFlowingFluid.Properties PROPERTIES = new ForgeFlowingFluid.Properties(ChangedAddonFluids.LITIX_CAMONIA_FLUID,
			ChangedAddonFluids.FLOWING_LITIX_CAMONIA_FLUID,
			FluidAttributes.builder(ChangedAddonMod.resourceLoc("blocks/ammoniafluid"), ChangedAddonMod.resourceLoc("blocks/ammoniafluid"))
				.sound(SoundEvents.BUCKET_EMPTY))
				.explosionResistance(100f)
				.slopeFindDistance(2)
				.bucket(ChangedAddonItems.LITIX_CAMONIA_FLUID_BUCKET)
				.block(ChangedAddonBlocks.LITIX_CAMONIA_FLUID);

	private LitixCamoniaFluid() {
		super(PROPERTIES);
	}

	public static class Source extends LitixCamoniaFluid {

		public int getAmount(@NotNull FluidState state) {
			return 8;
		}

		public boolean isSource(@NotNull FluidState state) {
			return true;
		}
	}

	public static class Flowing extends LitixCamoniaFluid {

		protected void createFluidStateDefinition(StateDefinition.@NotNull Builder<Fluid, FluidState> builder) {
			super.createFluidStateDefinition(builder);
			builder.add(LEVEL);
		}

		public int getAmount(FluidState state) {
			return state.getValue(LEVEL);
		}

		public boolean isSource(@NotNull FluidState state) {
			return false;
		}
	}
}
