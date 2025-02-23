package net.foxyas.changedaddon.procedures;

import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.UniversalDist;
import net.ltxprogrammer.changed.world.enchantments.LatexProtectionEnchantment;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.GameType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
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
		if (!(entity instanceof Player player)) {
			return;
		}
		double TransfurProgress = ProcessTransfur.getPlayerTransfurProgress(player);
		double TransfurInfectionAttribute = player.getAttribute(ChangedAddonModAttributes.LATEXINFECTION.get()).getValue();
		float Math = (float) (TransfurProgress * TransfurInfectionAttribute / 100);
		var PlayerTolerance = ProcessTransfur.getEntityTransfurTolerance(player);
		if (!(player.isCreative()) && !(player.isSpectator())) {
			if (TransfurInfectionAttribute > 0) {
				if (!ProcessTransfur.isPlayerTransfurred(player)) {
					if (TransfurProgress > 0 && (TransfurProgress + Math) < (PlayerTolerance * 0.995f)) {
						if (entity.isAlive()) {
							AddTransfurProgressProcedure.set(entity, TransfurProgress + Math);
						}
					}
				}
			}
		}
	}
}
