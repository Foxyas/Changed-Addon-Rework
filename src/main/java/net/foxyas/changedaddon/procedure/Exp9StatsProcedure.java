package net.foxyas.changedaddon.procedure;

import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class Exp9StatsProcedure {

    @SubscribeEvent
    public static void onEntityAttacked(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        if (!hasKetExperiment009Form(entity)) return;

        DamageSource source = event.getSource();
        boolean reduceDamage =
                source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) ||
                        source.is(DamageTypeTags.IS_FIRE) ||
                        (source.is(DamageTypeTags.IS_FIRE) && entity.isOnFire());

        if (reduceDamage) {
            event.setAmount(Math.round(event.getAmount() / 2f));
        }
    }

    private static boolean hasKetExperiment009Form(Entity entity) {
        return entity instanceof Player player && ProcessTransfur.getPlayerTransfurVariant(player) != null && ProcessTransfur.getPlayerTransfurVariant(player).getFormId().toString().startsWith("changed_addon:form_experiment009");
    }
}
