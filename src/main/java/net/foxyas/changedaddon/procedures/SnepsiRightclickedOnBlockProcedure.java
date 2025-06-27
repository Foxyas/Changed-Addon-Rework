package net.foxyas.changedaddon.procedures;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import net.foxyas.changedaddon.init.ChangedAddonModItems;
import net.foxyas.changedaddon.init.ChangedAddonModBlocks;

public class SnepsiRightclickedOnBlockProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate, Direction direction, Entity entity) {
		if (direction == null || entity == null)
			return;
		BlockState block = Blocks.AIR.defaultBlockState();
		ItemStack eventitem = ItemStack.EMPTY;
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == ChangedAddonModItems.SNEPSI.get()
				|| (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem() == ChangedAddonModItems.SNEPSI.get()) {
			eventitem = new ItemStack(ChangedAddonModItems.SNEPSI.get());
		} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == ChangedAddonModItems.FOXTA.get()
				|| (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem() == ChangedAddonModItems.FOXTA.get()) {
			if (!((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == ChangedAddonModItems.SNEPSI.get())) {
				eventitem = new ItemStack(ChangedAddonModItems.FOXTA.get());
			}
		}
		if (entity.isShiftKeyDown()) {
			if (eventitem.getItem() == ChangedAddonModItems.SNEPSI.get()) {
				block = (new Object() {
					public BlockState with(BlockState _bs, Direction newValue) {
						Property<?> _prop = _bs.getBlock().getStateDefinition().getProperty("facing");
						if (_prop instanceof DirectionProperty _dp && _dp.getPossibleValues().contains(newValue))
							return _bs.setValue(_dp, newValue);
						_prop = _bs.getBlock().getStateDefinition().getProperty("axis");
						return _prop instanceof EnumProperty _ep && _ep.getPossibleValues().contains(newValue.getAxis()) ? _bs.setValue(_ep, newValue.getAxis()) : _bs;
					}
				}.with(ChangedAddonModBlocks.SNEPSI_CAN.get().defaultBlockState(), ((entity.getDirection()).getOpposite())));
			} else if (eventitem.getItem() == ChangedAddonModItems.FOXTA.get()) {
				block = (new Object() {
					public BlockState with(BlockState _bs, Direction newValue) {
						Property<?> _prop = _bs.getBlock().getStateDefinition().getProperty("facing");
						if (_prop instanceof DirectionProperty _dp && _dp.getPossibleValues().contains(newValue))
							return _bs.setValue(_dp, newValue);
						_prop = _bs.getBlock().getStateDefinition().getProperty("axis");
						return _prop instanceof EnumProperty _ep && _ep.getPossibleValues().contains(newValue.getAxis()) ? _bs.setValue(_ep, newValue.getAxis()) : _bs;
					}
				}.with(ChangedAddonModBlocks.FOXTA_CAN.get().defaultBlockState(), ((entity.getDirection()).getOpposite())));
			}
			if (direction == Direction.UP) {
				world.setBlock(new BlockPos(x, y + 1, z), block, 3);
				if (blockstate.getBlock() instanceof LiquidBlock) {
					if (blockstate.is(BlockTags.create(ResourceLocation.parse("minecraft:water")))) {
						{
							BlockPos _pos = new BlockPos(x, y + 1, z);
							BlockState _bs = world.getBlockState(_pos);
							if (_bs.getBlock().getStateDefinition().getProperty("waterlogged") instanceof BooleanProperty _booleanProp)
								world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
						}
					}
				}
			} else if (direction == Direction.DOWN) {
				world.setBlock(new BlockPos(x, y - 1, z), block, 3);
				if (blockstate.getBlock() instanceof LiquidBlock) {
					if (blockstate.is(BlockTags.create(ResourceLocation.parse("minecraft:water")))) {
						{
							BlockPos _pos = new BlockPos(x, y - 1, z);
							BlockState _bs = world.getBlockState(_pos);
							if (_bs.getBlock().getStateDefinition().getProperty("waterlogged") instanceof BooleanProperty _booleanProp)
								world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
						}
					}
				}
			}
			if (direction == Direction.NORTH) {
				world.setBlock(new BlockPos(x, y, z - 1), block, 3);
				if (blockstate.getBlock() instanceof LiquidBlock) {
					if (blockstate.is(BlockTags.create(ResourceLocation.parse("minecraft:water")))) {
						{
							BlockPos _pos = new BlockPos(x, y, z - 1);
							BlockState _bs = world.getBlockState(_pos);
							if (_bs.getBlock().getStateDefinition().getProperty("waterlogged") instanceof BooleanProperty _booleanProp)
								world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
						}
					}
				}
			} else if (direction == Direction.SOUTH) {
				world.setBlock(new BlockPos(x, y, z + 1), block, 3);
				if (blockstate.getBlock() instanceof LiquidBlock) {
					if (blockstate.is(BlockTags.create(ResourceLocation.parse("minecraft:water")))) {
						{
							BlockPos _pos = new BlockPos(x, y, z + 1);
							BlockState _bs = world.getBlockState(_pos);
							if (_bs.getBlock().getStateDefinition().getProperty("waterlogged") instanceof BooleanProperty _booleanProp)
								world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
						}
					}
				}
			}
			if (direction == Direction.WEST) {
				world.setBlock(new BlockPos(x - 1, y, z), block, 3);
				if (blockstate.getBlock() instanceof LiquidBlock) {
					if (blockstate.is(BlockTags.create(ResourceLocation.parse("minecraft:water")))) {
						{
							BlockPos _pos = new BlockPos(x - 1, y, z);
							BlockState _bs = world.getBlockState(_pos);
							if (_bs.getBlock().getStateDefinition().getProperty("waterlogged") instanceof BooleanProperty _booleanProp)
								world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
						}
					}
				}
			} else if (direction == Direction.EAST) {
				world.setBlock(new BlockPos(x + 1, y, z), block, 3);
				if (blockstate.getBlock() instanceof LiquidBlock) {
					if (blockstate.is(BlockTags.create(ResourceLocation.parse("minecraft:water")))) {
						{
							BlockPos _pos = new BlockPos(x + 1, y, z);
							BlockState _bs = world.getBlockState(_pos);
							if (_bs.getBlock().getStateDefinition().getProperty("waterlogged") instanceof BooleanProperty _booleanProp)
								world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
						}
					}
				}
			}
		}
		if (entity instanceof Player _player && !_player.level().isClientSide())
			_player.displayClientMessage(new TextComponent(("face2 is " + direction)), false);
	}
}
