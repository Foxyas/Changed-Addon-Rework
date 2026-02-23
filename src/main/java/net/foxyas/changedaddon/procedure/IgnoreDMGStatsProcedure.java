package net.foxyas.changedaddon.procedure;

import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class IgnoreDMGStatsProcedure {

    @SubscribeEvent
    public static void onEntityAttacked(LivingAttackEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player player)) return;

        TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
        if (instance == null) return;

        DamageSource damagesource = event.getSource();
        String id = instance.getFormId().toString();
        if (id.startsWith("changed_addon:form_experiment009")) {
            if (damagesource.getMsgId().equals(entity.level.damageSources().lightningBolt().getMsgId())) {
                event.setCanceled(true);
            }
            return;
        }

        if (id.startsWith("changed_addon:form_experiment_10")) {
            if (damagesource.getMsgId().equals(entity.level.damageSources().wither().getMsgId())) {
                event.setCanceled(true);
            }
        }
    }
}
