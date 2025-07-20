package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.init.ChangedAddonDamageSources;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class LatexSolventOnEffectActiveTickProcedure {

    @SubscribeEvent
    public static void onPlayerTick(LivingEvent.LivingUpdateEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof ChangedEntity changedEntity && changedEntity.getType().is(ChangedTags.EntityTypes.LATEX)) {
            MobEffectInstance SolventEffectInstace = changedEntity.getEffect(ChangedAddonMobEffects.LATEX_SOLVENT.get());
            if (SolventEffectInstace != null) {
                changedEntity.hurt(ChangedAddonDamageSources.SOLVENT, SolventEffectInstace.getAmplifier() + 2);
            }
        }
    }
}
