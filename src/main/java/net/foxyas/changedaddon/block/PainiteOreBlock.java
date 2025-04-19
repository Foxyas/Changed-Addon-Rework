
package net.foxyas.changedaddon.block;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;

import net.foxyas.changedaddon.procedures.PainiteOreBlockDestroyedByPlayerProcedure;
import net.foxyas.changedaddon.procedures.PainiteOreBlockDestroyedByExplosionProcedure;
import net.minecraftforge.common.TierSortingRegistry;

public class PainiteOreBlock extends OreBlock {

	public PainiteOreBlock() {
		super(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_RED).sound(SoundType.STONE).strength(15f, 25f).requiresCorrectToolForDrops(),
				UniformInt.of(20,35)
				);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
		return true;
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 0;
	}

	@Override
	public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
		if (player.getInventory().getSelected().getItem() instanceof PickaxeItem tieredItem) {
			return TierSortingRegistry.isCorrectTierForDrops(Tiers.NETHERITE, state) || tieredItem.getTier().getLevel() >= 4;
		}
		return false;
	}

	@Override
	public boolean onDestroyedByPlayer(BlockState blockstate, Level world, BlockPos pos, Player entity, boolean willHarvest, FluidState fluid) {
		boolean retval = super.onDestroyedByPlayer(blockstate, world, pos, entity, willHarvest, fluid);
		return retval;
	}

	@Override
	public void wasExploded(Level world, BlockPos pos, Explosion e) {
		super.wasExploded(world, pos, e);
	}
}
