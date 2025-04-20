
package net.foxyas.changedaddon.block;

import net.ltxprogrammer.changed.block.NonLatexCoverableBlock;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;
import java.util.Collections;

public class IridiumBlockBlock extends Block implements NonLatexCoverableBlock {
	public IridiumBlockBlock() {
		super(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.SNOW).sound(SoundType.NETHERITE_BLOCK).strength(30f, 50f).requiresCorrectToolForDrops());
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 15;
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
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		List<ItemStack> dropsOriginal = super.getDrops(state, builder);
		if (!dropsOriginal.isEmpty())
			return dropsOriginal;
		return Collections.singletonList(new ItemStack(this, 1));
	}
}
