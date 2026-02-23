package net.foxyas.changedaddon.procedure;

import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class LatexVariantAgeIncreaseProcedure {

    @SubscribeEvent
    public static void onUseItemFinish(LivingEntityUseItemEvent.Finish event) {
        Entity entity = event.getEntity();
        if (entity == null) return;

        if (!event.getItem().is(ChangedItems.WHITE_LATEX_GOO.get()) || !(entity instanceof Player player)) return;

        TransfurVariantInstance<?> tf = ProcessTransfur.getPlayerTransfurVariant(player);
        if (tf == null) return;

        if (tf.getFormId().toString().equals("changed:form_dark_latex_pup")) {

            tf.ageAsVariant += 5000;

            if (entity.level instanceof ServerLevel level)
                level.sendParticles(ParticleTypes.HAPPY_VILLAGER, entity.getX(), entity.getY(), entity.getZ(), 5, 0.3, 0.5, 0.3, 1);
        }
    }
}