package net.foxyas.changedaddon.procedure;

import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class Exp10StatsProcedure {

    @SubscribeEvent
    public static void onEntityAttacked(LivingHurtEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player player && ProcessTransfur.getPlayerTransfurVariant(player) != null && ProcessTransfur.getPlayerTransfurVariant(player).getFormId().toString().startsWith("changed_addon:form_experiment_10")) {
            if (event.getSource().is(DamageTypeTags.IS_FIRE)) {
                float math = event.getAmount() / 2;
                float Phase2Math = math * 0.5f;
                float Phase3Math = math + Phase2Math;
                event.setAmount((Math.round(Phase3Math)));
            }
        }
    }
}
