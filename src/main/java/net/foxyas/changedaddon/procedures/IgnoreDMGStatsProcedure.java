package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class IgnoreDMGStatsProcedure {
    @SubscribeEvent
    public static void onEntityAttacked(LivingAttackEvent event) {
        Entity entity = event.getEntity();
        if (entity != null) {
            execute(event, event.getSource(), entity);
        }
    }

    private static void execute(@Nullable Event event, DamageSource damagesource, Entity entity) {
        if (entity == null)
            return;
        if (entity instanceof Player player) {
            TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
            if (instance != null) {
                if (instance.getFormId().toString().startsWith("changed_addon:form_ket_experiment009")) {
                    if ((damagesource).getMsgId().equals(DamageSource.LIGHTNING_BOLT.getMsgId())) {
                        if (event != null && event.isCancelable()) {
                            event.setCanceled(true);
                        }
                    }
                } else if (instance.getFormId().toString().startsWith("changed_addon:form_experiment_10")) {
                    if ((damagesource).getMsgId().equals(DamageSource.WITHER.getMsgId())) {
                        if (event != null && event.isCancelable()) {
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }
}
