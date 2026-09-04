package net.foxyas.changedaddon.procedure;

import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.variant.TransfurSoundsDetails.TransfurSoundAction;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class TransfursExtraSoundDetailsProcedure {

    @SubscribeEvent
    public static void onChat(ServerChatEvent event) {
        ServerPlayer player = event.getPlayer();
        String text = event.getMessage().getString();

        if (!ProcessTransfur.isPlayerTransfurred(player)) return;

        ChangedAddonVariables.PlayerVariables vars =
                ChangedAddonVariables.nonNullOf(player);

        if (vars.isActInCooldown()) return;

        for (TransfurSoundAction action : TransfurSoundAction.values()) {

            if (!action.matchesChat(text)) continue;
            if (!action.canUse(player)) continue;

            action.playAndApplyCooldown(player);
            return; // só um som por mensagem
        }
    }
}
