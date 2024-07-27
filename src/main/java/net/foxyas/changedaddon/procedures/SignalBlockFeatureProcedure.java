package net.foxyas.changedaddon.procedures;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.core.BlockPos;
import net.foxyas.changedaddon.init.ChangedAddonModItems;
import net.foxyas.changedaddon.init.ChangedAddonModBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SignalBlockFeatureProcedure {

	private static final int LARGE_SEARCH_RADIUS = 128;
	private static final int SMALL_SEARCH_RADIUS = 32;
	private static final int MAX_FOUND_BLOCKS = 10;

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;

		if (isSignalCatcherInHand(entity)) {
			Player player = (Player) entity;
			if (!player.getCooldowns().isOnCooldown(ChangedAddonModItems.SIGNAL_CATCHER.get())) {
				int radius = player.isShiftKeyDown() ? LARGE_SEARCH_RADIUS : SMALL_SEARCH_RADIUS;
				int cooldown = player.isShiftKeyDown() ? 100 : 60;
				searchSignalBlock(world, x, y, z, player, itemstack, radius, cooldown);
			}
		}
	}

	private static boolean isSignalCatcherInHand(Entity entity) {
		LivingEntity livingEntity = (LivingEntity) entity;
		return livingEntity.getMainHandItem().getItem() == ChangedAddonModItems.SIGNAL_CATCHER.get() || livingEntity.getOffhandItem().getItem() == ChangedAddonModItems.SIGNAL_CATCHER.get();
	}

	private static void searchSignalBlock(LevelAccessor world, double x, double y, double z, Player player, ItemStack itemstack, int radius, int cooldown) {
		int startX = (int) x - radius;
		int endX = (int) x + radius;
		int startY = (int) y - radius;
		int endY = (int) y + radius;
		int startZ = (int) z - radius;
		int endZ = (int) z + radius;

		List<BlockPos> foundPositions = new ArrayList<>();

		if (!world.isClientSide()) {
			for (int sx = startX; sx <= endX; sx++) {
				for (int sy = startY; sy <= endY; sy++) {
					for (int sz = startZ; sz <= endZ; sz++) {
						BlockPos currentPos = new BlockPos(sx, sy, sz);
						if (world.getBlockState(currentPos).getBlock() == ChangedAddonModBlocks.SIGNAL_BLOCK.get()) {
							foundPositions.add(currentPos);
							// Only update player state for the first found block
							if (foundPositions.size() == 1) {
								updatePlayerState(player, itemstack, sx, sy, sz, cooldown);
							}
							// Break loop if the number of found blocks exceeds MAX_FOUND_BLOCKS
							if (foundPositions.size() >= MAX_FOUND_BLOCKS) {
								break;
							}
						}
					}
					if (foundPositions.size() == MAX_FOUND_BLOCKS) {
						break;
					}
				}
				if (foundPositions.size() == MAX_FOUND_BLOCKS) {
					break;
				}
			}
		}

		if (!foundPositions.isEmpty()) {
			displayFoundLocations(player, foundPositions);
			playSounds(world, x, y, z, foundPositions.get(0));
		} else if (!player.level.isClientSide()) {
			player.displayClientMessage(new TextComponent("No Signal Block Found"), false);
		}
	}

	private static void updatePlayerState(Player player, ItemStack itemstack, int x, int y, int z, int cooldown) {
		itemstack.getOrCreateTag().putDouble("x", x);
		itemstack.getOrCreateTag().putDouble("y", y);
		itemstack.getOrCreateTag().putDouble("z", z);
		itemstack.getOrCreateTag().putBoolean("set", true);
		player.getCooldowns().addCooldown(itemstack.getItem(), cooldown);
	}

	private static void displayFoundLocations(Player player, List<BlockPos> positions) {
		StringBuilder message = new StringBuilder("Signal Blocks found at:\n");
		for (int i = 0; i < positions.size(); i++) {
			BlockPos pos = positions.get(i);
			message.append("Block ").append(i + 1).append(": [").append(pos.getX()).append(", ").append(pos.getY()).append(", ").append(pos.getZ()).append("]\n");
			if (i < positions.size() - 1) {
				message.append("; ");
			}
		}
		player.displayClientMessage(new TextComponent(message.toString()), false);
	}

	private static void playSounds(LevelAccessor world, double x, double y, double z, BlockPos foundPos) {
		playSound(world, foundPos, "block.conduit.deactivate", SoundSource.BLOCKS, 1.5f);
		playSound(world, new BlockPos(x, y, z), "block.conduit.activate", SoundSource.PLAYERS, 1.5f);
	}

	private static void playSound(LevelAccessor world, BlockPos pos, String soundEvent, SoundSource source, float volume) {
		if (world instanceof Level level) {
			ResourceLocation soundLocation = new ResourceLocation(soundEvent);
			if (!level.isClientSide()) {
				level.playSound(null, pos, Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(soundLocation)), source, volume, 0);
			} else {
				level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(soundLocation)), source, volume, 0, false);
			}
		}
	}
}
