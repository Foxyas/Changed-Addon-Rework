package net.foxyas.changedaddon.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.GameType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.client.Minecraft;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.foxyas.changedaddon.init.ChangedAddonModAttributes;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class LatexInfectionTickProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			execute(event, event.player);
		}
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		double TransfurProgress = 0;
		AttributeInstance attributeInstance = ((LivingEntity) entity).getAttribute(ChangedAddonModAttributes.LATEXINFECTION.get());
		double LatexResistence_local_var = attributeInstance.getValue();
		float TransfurProgress_local_var = (float) LatexResistence_local_var;
		TransfurProgress = (entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).Progress_Transfur_Number;
		float PlayerTransfurProgress = (float) TransfurProgress;
		float Math = PlayerTransfurProgress * TransfurProgress_local_var / 100;
		if (!(new Object() {
			public boolean checkGamemode(Entity _ent) {
				if (_ent instanceof ServerPlayer _serverPlayer) {
					return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
				} else if (_ent.level.isClientSide() && _ent instanceof Player _player) {
					return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.CREATIVE;
				}
				return false;
			}
		}.checkGamemode(entity)) && !(new Object() {
			public boolean checkGamemode(Entity _ent) {
				if (_ent instanceof ServerPlayer _serverPlayer) {
					return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
				} else if (_ent.level.isClientSide() && _ent instanceof Player _player) {
					return Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()) != null && Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId()).getGameMode() == GameType.SPECTATOR;
				}
				return false;
			}
		}.checkGamemode(entity))) {
			if (((LivingEntity) entity).getAttribute(ChangedAddonModAttributes.LATEXINFECTION.get()).getValue() > 0) {
				if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur == false) {
					if (new Object() {
						public double getValue() {
							CompoundTag dataIndex3 = new CompoundTag();
							entity.saveWithoutId(dataIndex3);
							return dataIndex3.getDouble("TransfurProgress");
						}
					}.getValue() > 0) {
						if (entity.isAlive()) {
							AddTransfurProgressProcedure.set(entity, PlayerTransfurProgress + Math);
						}
					}
				}
			}
		}
	}
}
