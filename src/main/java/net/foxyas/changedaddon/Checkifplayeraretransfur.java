package net.foxyas.changedaddon.process;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.ltxprogrammer.changed.entity.variant.LatexVariant;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.player.Player;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;

@Mod.EventBusSubscriber
public class Checkifplayeraretransfur {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if(player.isAlive()) {
        ProcessTransfur.ifPlayerLatex(player, (variant) -> {
            player.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.transfur = true;
                capability.syncPlayerVariables(player);
            });
            // Este bloco será executado se o jogador for látex e fornecerá a variante
        }, () -> {
            player.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.transfur = false;
                capability.syncPlayerVariables(player);
            });
            // Este bloco será executado se o jogador não for látex
        });
    }
    else {
    	player.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                capability.transfur = false;
                capability.syncPlayerVariables(player);
            });
    }
   }
}
