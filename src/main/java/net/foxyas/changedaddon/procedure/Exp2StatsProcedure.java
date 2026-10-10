// Improved version of Exp2StatsProcedure
package net.foxyas.changedaddon.procedure;

import net.foxyas.changedaddon.entity.simple.Exp2FemaleEntity;
import net.foxyas.changedaddon.entity.simple.Exp2MaleEntity;
import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class Exp2StatsProcedure {

    @SubscribeEvent
    public static void onEntityAttacked(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        Entity attacker = source.getDirectEntity();
        if (attacker == null) return;

        Entity entity = event.getEntity();
        boolean isExp2 = isExp2Form(entity);
        boolean attackerIsExp2 = isExp2Form(attacker);
        boolean entityTransfurred = isTransfurred(entity);
        boolean attackerTransfurred = isTransfurred(attacker);
        boolean entityAirHand = isAirHand(entity);
        boolean attackerAirHand = isAirHand(attacker);
        boolean entityIsExp2Entity = entity instanceof Exp2MaleEntity || entity instanceof Exp2FemaleEntity;
        boolean attackerIsExp2Entity = attacker instanceof Exp2MaleEntity || attacker instanceof Exp2FemaleEntity;

        if (isExp2 && source.is(DamageTypeTags.IS_FIRE)) {
            double result = event.getAmount() / 2;
            result += result * 0.35;
            event.setAmount(Math.round((float) result));
        }

        if (attacker instanceof Player player && isExp2Form(player) && !attackerIsExp2 && isNotCreativeAndSpectator(player)) {
            applyTransfurSickness(entity);
        }

        if (attackerIsExp2 && entityTransfurred && entityAirHand && !isExp2Form(entity) && isNotCreativeAndSpectator(entity)) {
            applyTransfurSickness(entity);
        }

        if (attackerIsExp2Entity && entityTransfurred && entityAirHand && !isExp2Form(entity) && isNotCreativeAndSpectator(entity)) {
            applyTransfurSickness(entity);
        }

        if (entityIsExp2Entity && attackerTransfurred && attackerAirHand && !isExp2Form(attacker) && isNotCreativeAndSpectator(attacker)) {
            applyTransfurSickness(attacker);
        }
    }

    private static boolean isExp2Form(Entity entity) {
        if (entity instanceof Player player && ProcessTransfur.getPlayerTransfurVariant(player) != null) {
            return ProcessTransfur.getPlayerTransfurVariant(player).getFormId().toString().startsWith("changed_addon:form_exp2");
        }
        return false;
    }

    private static boolean isTransfurred(Entity entity) {
        return entity instanceof Player player && ProcessTransfur.isPlayerTransfurred(player);
    }

    private static boolean isAirHand(Entity entity) {
        return entity instanceof LivingEntity living && living.getMainHandItem().getItem() == Blocks.AIR.asItem();
    }

    private static boolean isNotCreativeAndSpectator(Entity entity) {
        return !entity.isSpectator() && (!(entity instanceof Player player) || !player.isCreative());
    }

    private static void applyTransfurSickness(Entity target) {
        if (target instanceof LivingEntity living && !living.level.isClientSide()) {
            living.addEffect(new MobEffectInstance(ChangedAddonMobEffects.TRANSFUR_SICKNESS.get(), 2400, 0, false, false));
        }
    }
}
