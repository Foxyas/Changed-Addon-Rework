package net.foxyas.changedaddon.procedure;

import net.foxyas.changedaddon.entity.bosses.Experiment10BossEntity;
import net.foxyas.changedaddon.entity.bosses.Experiment10Entity;
import net.foxyas.changedaddon.variant.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class HurtByExp10Event {

    @SubscribeEvent
    public static void onEntityAttacked(LivingHurtEvent event) {
        Entity attacker = event.getSource().getDirectEntity();
        LivingEntity target = event.getEntity();
        if (!target.level.isClientSide || attacker == null) return;

        int amplifier = -1;

        if (attacker instanceof Experiment10Entity e10) {
            if (e10.getMainHandItem().isEmpty()) {
                amplifier = e10.isPhase2() ? 2 : 0;
            }
        } else if (attacker instanceof Experiment10BossEntity e10Boss) {
            if (e10Boss.getMainHandItem().isEmpty()) {
                amplifier = e10Boss.isPhase2() ? 2 : 0;
            }
        } else {
            if (!(target instanceof Player player)) return;

            TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
            if (instance == null || !instance.is(ChangedAddonTransfurVariants.EXPERIMENT_10)) return;

            if (attacker instanceof LivingEntity living && living.getMainHandItem().isEmpty()) {
                target.addEffect(new MobEffectInstance(MobEffects.WITHER, 90, 0, false, true));
            }
        }

        if (amplifier >= 0) {
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, 90, amplifier, false, true));
        }
    }
}
