
package net.foxyas.changedaddon.block;

import org.checkerframework.checker.units.qual.s;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Containers;
import net.minecraft.core.BlockPos;

import net.foxyas.changedaddon.block.entity.SignalBlockBlockEntity;

import java.util.List;
import java.util.Collections;

public class SignalBlockBlock extends Block implements EntityBlock {
	public SignalBlockBlock() {
		super(BlockBehaviour.Properties.of(Material.STONE).sound(SoundType.STONE).strength(1.5f, 20f).lightLevel(s -> 8).hasPostProcess((bs, br, bp) -> true).emissiveRendering((bs, br, bp) -> true));
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 15;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		List<ItemStack> dropsOriginal = super.getDrops(state, builder);
		if (!dropsOriginal.isEmpty())
			return dropsOriginal;
		return Collections.singletonList(new ItemStack(this, 1));
	}

	@Override
	public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		return tileEntity instanceof MenuProvider menuProvider ? menuProvider : null;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SignalBlockBlockEntity(pos, state);
	}

	@Override
	public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int eventID, int eventParam) {
		super.triggerEvent(state, world, pos, eventID, eventParam);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		return blockEntity == null ? false : blockEntity.triggerEvent(eventID, eventParam);
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof SignalBlockBlockEntity be) {
				Containers.dropContents(world, pos, be);
				world.updateNeighbourForOutputSignal(pos, this);
			}
			super.onRemove(state, world, pos, newState, isMoving);
		}
	}
}
