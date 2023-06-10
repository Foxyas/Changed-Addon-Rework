
package net.foxyas.changedaddon.block;

import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

import net.foxyas.changedaddon.procedures.LitixCamoniaFluidUpdateTickProcedure;
import net.foxyas.changedaddon.procedures.LitixCamoniaFluidMobplayerCollidesBlockProcedure;
import net.foxyas.changedaddon.init.ChangedAddonModFluids;

import java.util.Random;

public class LitixCamoniaFluidBlock extends LiquidBlock {
	public LitixCamoniaFluidBlock() {
		super(() -> (FlowingFluid) ChangedAddonModFluids.LITIX_CAMONIA_FLUID.get(), BlockBehaviour.Properties.of(Material.WATER).strength(100f)

		);
	}

	@Override
	public void onPlace(BlockState blockstate, Level world, BlockPos pos, BlockState oldState, boolean moving) {
		super.onPlace(blockstate, world, pos, oldState, moving);
		world.scheduleTick(pos, this, 10);
	}

	@Override
	public void tick(BlockState blockstate, ServerLevel world, BlockPos pos, Random random) {
		super.tick(blockstate, world, pos, random);
		LitixCamoniaFluidUpdateTickProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ());
		world.scheduleTick(pos, this, 10);
	}

	@Override
	public void entityInside(BlockState blockstate, Level world, BlockPos pos, Entity entity) {
		super.entityInside(blockstate, world, pos, entity);
		LitixCamoniaFluidMobplayerCollidesBlockProcedure.execute(entity);
	}
}
