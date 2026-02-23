package net.foxyas.changedaddon.procedure;

import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class BioSynthStatsProcedure {

    @SubscribeEvent
    public static void onEntityAttacked(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        if (!source.is(DamageTypeTags.IS_FIRE)) return;

        if (event.getEntity() instanceof Player player && ProcessTransfur.getPlayerTransfurVariant(player) != null && ProcessTransfur.getPlayerTransfurVariant(player).getFormId().toString().startsWith("changed_addon:form_biosynth")) {
            float math = event.getAmount() / 2;
            float Phase3Math = math + math * 0.25f;
            event.setAmount(Math.round(Phase3Math));
        }
    }
}
