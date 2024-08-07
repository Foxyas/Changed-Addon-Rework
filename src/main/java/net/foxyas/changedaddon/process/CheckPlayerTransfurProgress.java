package net.foxyas.changedaddon.process;

import net.ltxprogrammer.changed.entity.variant.LatexVariantInstance;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.ltxprogrammer.changed.entity.variant.LatexVariant;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.player.Player;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;

@Mod.EventBusSubscriber
public class CheckPlayerTransfurProgress {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event){
		Player player = event.player;
		ProcessTransfur.TransfurProgress Progress = ProcessTransfur.getPlayerTransfurProgress(player);
		if (Progress != null) {
			player.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> capability.LatexForm_ProgressTransfur = ProcessTransfur.getPlayerTransfurProgress(player).variant().toString());
			player.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> capability.Progress_Transfur_Number = ProcessTransfur.getPlayerTransfurProgress(player).progress());
		} else {
			player.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> capability.LatexForm_ProgressTransfur = "null");
			player.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> capability.Progress_Transfur_Number = 0);
		}

	}
}