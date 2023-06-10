
package net.foxyas.changedaddon.fluid;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fluids.FluidAttributes;

import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.resources.ResourceLocation;

import net.foxyas.changedaddon.init.ChangedAddonModItems;
import net.foxyas.changedaddon.init.ChangedAddonModFluids;
import net.foxyas.changedaddon.init.ChangedAddonModBlocks;

public abstract class LitixCamoniaFluidFluid extends ForgeFlowingFluid {
	public static final ForgeFlowingFluid.Properties PROPERTIES = new ForgeFlowingFluid.Properties(ChangedAddonModFluids.LITIX_CAMONIA_FLUID, ChangedAddonModFluids.FLOWING_LITIX_CAMONIA_FLUID,
			FluidAttributes.builder(new ResourceLocation("changed_addon:blocks/24"), new ResourceLocation("changed_addon:blocks/24"))

					.sound(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.bucket.empty"))))
			.explosionResistance(100f)

			.bucket(ChangedAddonModItems.LITIX_CAMONIA_FLUID_BUCKET).block(() -> (LiquidBlock) ChangedAddonModBlocks.LITIX_CAMONIA_FLUID.get());

	private LitixCamoniaFluidFluid() {
		super(PROPERTIES);
	}

	public static class Source extends LitixCamoniaFluidFluid {
		public Source() {
			super();
		}

		public int getAmount(FluidState state) {
			return 8;
		}

		public boolean isSource(FluidState state) {
			return true;
		}
	}

	public static class Flowing extends LitixCamoniaFluidFluid {
		public Flowing() {
			super();
		}

		protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
			super.createFluidStateDefinition(builder);
			builder.add(LEVEL);
		}

		public int getAmount(FluidState state) {
			return state.getValue(LEVEL);
		}

		public boolean isSource(FluidState state) {
			return false;
		}
	}
}
