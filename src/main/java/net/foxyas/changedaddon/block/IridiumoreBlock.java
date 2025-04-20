
package net.foxyas.changedaddon.block;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
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

import net.foxyas.changedaddon.procedures.IridiumoreBlockDestroyedByPlayerProcedure;
import net.foxyas.changedaddon.procedures.IridiumoreBlockDestroyedByExplosionProcedure;
import net.minecraftforge.common.TierSortingRegistry;

public class IridiumoreBlock extends OreBlock {
	public IridiumoreBlock() {
		super(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.QUARTZ).sound(SoundType.STONE).strength(20f, 25f).requiresCorrectToolForDrops(),
				UniformInt.of(20,40)
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
		ItemStack selectedItem = player.getInventory().getSelected();
		if (selectedItem.getItem() instanceof TieredItem tieredItem && tieredItem.isCorrectToolForDrops(selectedItem,state)) {
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
