package net.foxyas.changedaddon.event;

import com.mojang.datafixers.util.Either;
import net.foxyas.changedaddon.init.ChangedAddonGameRules;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.network.ChangedAddonVariables.PlayerVariables;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.ILatexAssimilatedEntity;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.ai.LatexAssimilationDecision;
import net.ltxprogrammer.changed.process.TransfurEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

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

        LatexAssimilationDecision<?> eventDecision = event.getDecision();
        if (eventDecision.latexAssimilateVictimBehavior(player).willAssimilate()) {
            return;
        }

        if (eventDecision.method() != LatexAssimilationDecision.Method.REPLICATION) {
            Either<IAbstractChangedEntity, ILatexAssimilatedEntity> source = eventDecision.context().source();
            if (source != null) {
                Optional<IAbstractChangedEntity> iAbstractChangedEntity = source.left();
                iAbstractChangedEntity.ifPresent(changedEntity -> {
                        LatexAssimilationDecision<?> decision = changedEntity.makeLatexAssimilationDecision(TransfurCause.GRAB_REPLICATE, target);
                    if (decision != null) {
                        event.setDecision(decision);
                        event.setTransfurVariant(decision.transfurVariant());
                    }
                });
            }
        }

        ChangedAddonVariables.ofPlayerSafe(player)
                .map(PlayerVariables::getLatexInfection)
                .ifPresent(latexInfection -> latexInfection.onTransfurAttack(player, event.getTransfurVariant(), eventDecision.context()));
    }

}
