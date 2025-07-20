package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class BioSynthStatsProcedure {
    @SubscribeEvent
    public static void onEntityAttacked(LivingHurtEvent event) {
        Entity entity = event.getEntity();
        if (entity != null) {
            execute(event, event.getSource(), entity, event.getAmount());
        }
    }

    private static void execute(LivingHurtEvent event, DamageSource damagesource, Entity entity, double amount) {
        if (entity == null)
            return;
        double math;
        double Phase2Math;
        double Phase3Math;
        if (entity instanceof Player player && ProcessTransfur.getPlayerTransfurVariant(player) != null && ProcessTransfur.getPlayerTransfurVariant(player).getFormId().toString().startsWith("changed_addon:form_biosynth")) {
            if ((damagesource).isFire()) {
                math = amount / 2;
                Phase2Math = math * 0.25;
                Phase3Math = math + Phase2Math;
                event.setAmount((Math.round(Phase3Math)));
            }
        }
    }
}
