package net.foxyas.changedaddon.process;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.ltxprogrammer.changed.entity.variant.LatexVariant;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.player.Player;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;

@Mod.EventBusSubscriber
public class CheckPlayerLatexForm {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Player player = event.player;
            ProcessTransfur.ifPlayerLatex(player, variant -> {
                player.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                    capability.LatexForm = variant.getFormId().toString();
                    capability.syncPlayerVariables(player);
                });

            }, () -> {
            	player.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                    capability.LatexForm = "";
                    capability.syncPlayerVariables(player);
                });
            	
            });

  		          if (ProcessTransfur.isPlayerOrganic(player)) {
  			  player.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
    	      capability.organic_transfur = true;
              capability.syncPlayerVariables(player);
			    });
	} else {
    		  player.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
              capability.organic_transfur = false;
              capability.syncPlayerVariables(player);
    			});
			}
        }
    } 
}
