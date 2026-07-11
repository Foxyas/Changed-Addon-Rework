package net.foxyas.changedaddon.event;

import net.foxyas.changedaddon.init.ChangedAddonGameRules;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.network.ChangedAddonVariables.PlayerVariables;
import net.ltxprogrammer.changed.process.TransfurEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class LatexInfectionEvent {

    // ---------------------------------------------
    // Events
    // ---------------------------------------------
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Player player = event.player;
            ChangedAddonVariables.ofPlayerSafe(player)
                    .map(PlayerVariables::getLatexInfection)
                    .ifPresent(latexInfection -> latexInfection.tick(player));
        }
    }

    @SubscribeEvent
    public static void onTransfurAttackPlayer(TransfurEvents.LatexAssimilationDecisionEvent event) {
        LivingEntity target = event.entity;
        if (!(target instanceof Player player)) return;

        if (!player.level().getGameRules().getBoolean(ChangedAddonGameRules.DO_LATEX_INFECTION)) {
            return;
        }

        if (event.getSourceEntity() != target) {

        }

        ChangedAddonVariables.ofPlayerSafe(player)
                .map(PlayerVariables::getLatexInfection)
                .ifPresent(latexInfection -> latexInfection.onTransfurAttack(player, event.getTransfurVariant(), event.getDecision().context()));
    }

}
