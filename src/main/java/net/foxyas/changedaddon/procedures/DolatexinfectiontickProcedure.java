package net.foxyas.changedaddon.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.Difficulty;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.client.Minecraft;

import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;
import net.foxyas.changedaddon.init.ChangedAddonModGameRules;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class DolatexinfectiontickProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			execute(event, event.player.level, event.player);
		}
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		double PlayerTransfurProgress = 0;
		boolean CanWork = false;
		if (world.getLevelData().getGameRules().getBoolean(ChangedAddonModGameRules.DOLATEXINFECTION) == true) {
			float Player_TransfurProgress = new Object() {
				public float getValue() {
					CompoundTag dataIndex0 = new CompoundTag();
					entity.saveWithoutId(dataIndex0);
					return dataIndex0.getFloat("TransfurProgress");
				}
			}.getValue();
			float mathnumber = 0f;
			switch (world.getDifficulty()) {
				case EASY :
					mathnumber = Player_TransfurProgress * 0.008f;
					break;
				case NORMAL :
					mathnumber = Player_TransfurProgress * 0.009f;
					break;
				case HARD :
					mathnumber = Player_TransfurProgress * 0.01f;
					break;
			}
			if (!(world.getDifficulty() == Difficulty.PEACEFUL) && !(new Object() {
				public boolean checkGamemode(Entity _ent) {
					if (_ent instanceof ServerPlayer _serverPlayer) {
						return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
					} else if (_ent.level.isClientSide() && _ent instanceof Player _player) {
						return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
								&& Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.SPECTATOR;
					}
					return false;
				}
			}.checkGamemode(entity)) && !(new Object() {
				public boolean checkGamemode(Entity _ent) {
					if (_ent instanceof ServerPlayer _serverPlayer) {
						return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
					} else if (_ent.level.isClientSide() && _ent instanceof Player _player) {
						return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null
								&& Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.CREATIVE;
					}
					return false;
				}
			}.checkGamemode(entity)) && !(entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get()) : false) && Player_TransfurProgress > 0) {
				CanWork = true;
			} else {
				CanWork = false;
			}
			if (CanWork == true) {
				CompoundTag dataIndex2 = new CompoundTag();
				entity.saveWithoutId(dataIndex2);
				dataIndex2.putFloat("TransfurProgress", Player_TransfurProgress + mathnumber);
				entity.load(dataIndex2);
			}
		}
	}
}