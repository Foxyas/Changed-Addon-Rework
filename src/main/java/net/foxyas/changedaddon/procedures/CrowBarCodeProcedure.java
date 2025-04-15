package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonModItems;
import net.foxyas.changedaddon.item.CrowBarItem;
import net.ltxprogrammer.changed.block.AbstractLabDoor;
import net.ltxprogrammer.changed.block.AbstractLargeLabDoor;
import net.ltxprogrammer.changed.block.NineSection;
import net.ltxprogrammer.changed.block.QuarterSection;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Random;

@Mod.EventBusSubscriber
public class CrowBarCodeProcedure {
	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		if (event.getHand() != event.getPlayer().getUsedItemHand()) {
			return;
		}
		updateConnectedDoorBlocks(event.getWorld(), event.getPos(), event.getPlayer(), event.getItemStack());
	}

	private static void updateConnectedDoorBlocks(Level world, BlockPos pos, Player player, ItemStack itemStack) {
		BlockState DoorState = world.getBlockState(pos);
		if (!(itemStack.getItem() instanceof CrowBarItem)) {
			return;
		}
		if (DoorState.getBlock() instanceof AbstractLabDoor abstractLabDoor) {
			if ((player.getCooldowns().isOnCooldown(ChangedAddonModItems.CROW_BAR.get()))) {
				return;
			}
			if (abstractLabDoor.openDoor(DoorState, world, pos)) {
				player.getCooldowns().addCooldown(itemStack.getItem(), 60);
			}
		} else if (DoorState.getBlock() instanceof AbstractLargeLabDoor abstractLargeLabDoor) {
			if (DoorState.getValue(AbstractLargeLabDoor.SECTION) != NineSection.CENTER) {
				return;
			} else {
				if ((player.getCooldowns().isOnCooldown(ChangedAddonModItems.CROW_BAR.get()))) {
					return;
				}
				if (abstractLargeLabDoor.openDoor(DoorState, world, pos) && !(player.getCooldowns().isOnCooldown(ChangedAddonModItems.CROW_BAR.get()))) {
					player.getCooldowns().addCooldown(itemStack.getItem(), 60);
				}
			}
		}
	}


	private static void oldexecute(@Nullable Event event, LevelAccessor world, double x, double y, double z, BlockState blockstate, Entity entity) {
		if (entity == null)
			return;
		double sx = 0;
		double sy = 0;
		double sz = 0;
		ItemStack CrowBar = ItemStack.EMPTY;
		CrowBar = new ItemStack(ChangedAddonModItems.CROW_BAR.get());
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == CrowBar.getItem()) {
			if (blockstate.is(BlockTags.create(new ResourceLocation("changed_addon:lab_doors")))) {
				if (!(blockstate.getBlock().getStateDefinition().getProperty("open") instanceof BooleanProperty _getbp5 && blockstate.getValue(_getbp5))) {
					sx = -2;
					for (int index0 = 0; index0 < 5; index0++) {
						sy = -2;
						for (int index1 = 0; index1 < 5; index1++) {
							sz = -2;
							for (int index2 = 0; index2 < 5; index2++) {
								if ((world.getBlockState(new BlockPos(x + sx, y + sy, z + sz))).getBlock() == ForgeRegistries.BLOCKS
										.getValue(new ResourceLocation(((ForgeRegistries.BLOCKS.getKey(blockstate.getBlock()).toString())).toLowerCase(java.util.Locale.ENGLISH)))) {
									{
										BlockPos _pos = new BlockPos(x + sx, y + sy, z + sz);
										BlockState _bs = world.getBlockState(_pos);
										if (_bs.getBlock().getStateDefinition().getProperty("open") instanceof BooleanProperty _booleanProp)
											world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
									}
								}
								sz = sz + 1;
							}
							sy = sy + 1;
						}
						sx = sx + 1;
					}
					if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == CrowBar.getItem()) {
						if (!(new Object() {
							public boolean checkGamemode(Entity _ent) {
								if (_ent instanceof ServerPlayer _serverPlayer) {
									return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
								} else if (_ent.level.isClientSide() && _ent instanceof Player _player) {
									return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
											&& Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.CREATIVE;
								}
								return false;
							}
						}.checkGamemode(entity)) && !(new Object() {
							public boolean checkGamemode(Entity _ent) {
								if (_ent instanceof ServerPlayer _serverPlayer) {
									return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
								} else if (_ent.level.isClientSide() && _ent instanceof Player _player) {
									return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
											&& Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.SPECTATOR;
								}
								return false;
							}
						}.checkGamemode(entity))) {
							{
								ItemStack _ist = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
								if (_ist.hurt(10, new Random(), null)) {
									_ist.shrink(1);
									_ist.setDamageValue(0);
								}
							}
						}
						if (entity instanceof LivingEntity _entity)
							_entity.swing(InteractionHand.MAIN_HAND, true);
					}
				}
			} else if (blockstate.is(BlockTags.create(new ResourceLocation("changed_addon:lab_big_doors")))) {
				if (!(blockstate.getBlock().getStateDefinition().getProperty("open") instanceof BooleanProperty _getbp22 && blockstate.getValue(_getbp22))) {
					if ((blockstate.getBlock().getStateDefinition().getProperty("section") instanceof EnumProperty _getep24 ? blockstate.getValue(_getep24).toString() : "").equals("CENTER")) {
						sx = -2;
						for (int index3 = 0; index3 < 5; index3++) {
							sy = -2;
							for (int index4 = 0; index4 < 5; index4++) {
								sz = -2;
								for (int index5 = 0; index5 < 5; index5++) {
									if ((world.getBlockState(new BlockPos(x + sx, y + sy, z + sz))).getBlock() == ForgeRegistries.BLOCKS
											.getValue(new ResourceLocation(((ForgeRegistries.BLOCKS.getKey(blockstate.getBlock()).toString())).toLowerCase(java.util.Locale.ENGLISH)))) {
										{
											BlockPos _pos = new BlockPos(x + sx, y + sy, z + sz);
											BlockState _bs = world.getBlockState(_pos);
											if (_bs.getBlock().getStateDefinition().getProperty("open") instanceof BooleanProperty _booleanProp)
												world.setBlock(_pos, _bs.setValue(_booleanProp, true), 3);
										}
									}
									sz = sz + 1;
								}
								sy = sy + 1;
							}
							sx = sx + 1;
						}
						if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == CrowBar.getItem()) {
							if (!(new Object() {
								public boolean checkGamemode(Entity _ent) {
									if (_ent instanceof ServerPlayer _serverPlayer) {
										return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
									} else if (_ent.level.isClientSide() && _ent instanceof Player _player) {
										return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
												&& Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.CREATIVE;
									}
									return false;
								}
							}.checkGamemode(entity)) && !(new Object() {
								public boolean checkGamemode(Entity _ent) {
									if (_ent instanceof ServerPlayer _serverPlayer) {
										return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
									} else if (_ent.level.isClientSide() && _ent instanceof Player _player) {
										return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
												&& Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.SPECTATOR;
									}
									return false;
								}
							}.checkGamemode(entity))) {
								{
									ItemStack _ist = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
									if (_ist.hurt(10, new Random(), null)) {
										_ist.shrink(1);
										_ist.setDamageValue(0);
									}
								}
							}
							if (entity instanceof LivingEntity _entity)
								_entity.swing(InteractionHand.MAIN_HAND, true);
						}
					}
				}
			}
		}
	}
}
