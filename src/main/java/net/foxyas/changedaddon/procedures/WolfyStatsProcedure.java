package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class WolfyStatsProcedure {
    @SubscribeEvent
    public static void onEntityAttacked(LivingAttackEvent event) {
        Entity entity = event.getEntity();
        if (event != null && entity != null) {
            execute(event, event.getSource(), entity);
        }
    }

    public static void execute(DamageSource damagesource, Entity entity) {
        execute(null, damagesource, entity);
    }

    private static void execute(@Nullable Event event, DamageSource damagesource, Entity entity) {
        if (entity == null)
            return;
        if (entity instanceof Player player) {
            TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
            if (instance.getFormId().toString().equals("changed_addon:form_wolfy")) {
                if ((damagesource) instanceof EntityDamageSource _entityDamageSource && _entityDamageSource.isThorns()) {
                    if (event != null && event.isCancelable()) {
                        event.setCanceled(true);
                    }
                } else if ((damagesource).isFire()) {
                    if (event != null && event.isCancelable()) {
                        event.setCanceled(true);
                    }
                } else if ((damagesource).isExplosion()) {
                    if (event != null && event.isCancelable()) {
                        event.setCanceled(true);
                    }
                } else if ((damagesource) == DamageSource.LIGHTNING_BOLT) {
                    if (event != null && event.isCancelable()) {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
}
