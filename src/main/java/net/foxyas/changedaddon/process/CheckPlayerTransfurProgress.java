package net.foxyas.changedaddon.process;

import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.player.Player;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;

@Mod.EventBusSubscriber
public class CheckPlayerTransfurProgress {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event){
		Player player = event.player;
		player.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> capability.Progress_Transfur_Number = ProcessTransfur.getPlayerTransfurProgress(player));
	}
}