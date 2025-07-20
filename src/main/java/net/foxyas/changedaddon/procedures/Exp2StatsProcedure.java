// Improved version of Exp2StatsProcedure
package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.entity.Exp2FemaleEntity;
import net.foxyas.changedaddon.entity.Exp2MaleEntity;
import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.Objects;

@Mod.EventBusSubscriber
public class Exp2StatsProcedure {

    @SubscribeEvent
    public static void onEntityAttacked(LivingHurtEvent event) {
        Entity entity = event.getEntity();
        if (entity != null) {
            execute(event, event.getSource(), entity, event.getSource().getDirectEntity(), event.getAmount());
        }
    }

    public static void execute(DamageSource source, Entity entity, Entity attacker, double amount) {
        execute(null, source, entity, attacker, amount);
    }

    private static void execute(@Nullable Event event, DamageSource source, Entity entity, Entity attacker, double amount) {
        if (entity == null || attacker == null) return;

        boolean isExp2 = isExp2Form(entity);
        boolean attackerIsExp2 = isExp2Form(attacker);
        boolean entityTransfurred = isTransfurred(entity);
        boolean attackerTransfurred = isTransfurred(attacker);
        boolean entityAirHand = isAirHand(entity);
        boolean attackerAirHand = isAirHand(attacker);
        boolean entityIsExp2Entity = entity instanceof Exp2MaleEntity || entity instanceof Exp2FemaleEntity;
        boolean attackerIsExp2Entity = attacker instanceof Exp2MaleEntity || attacker instanceof Exp2FemaleEntity;

        if (isExp2 && source.isFire()) {
            double result = amount / 2;
            result += result * 0.35;
            if (event instanceof LivingHurtEvent hurtEvent) hurtEvent.setAmount(Math.round((float) result));
        }

        if (attacker instanceof Player player && isExp2Form(player) && !attackerIsExp2 && !isCreativeOrSpectator(player)) {
            applyTransfurSickness(entity);
        }

        if (attackerIsExp2 && entityTransfurred && entityAirHand && !isExp2Form(entity) && !isCreativeOrSpectator(entity)) {
            applyTransfurSickness(entity);
        }

        if (attackerIsExp2Entity && entityTransfurred && entityAirHand && !isExp2Form(entity) && !isCreativeOrSpectator(entity)) {
            applyTransfurSickness(entity);
        }

        if (entityIsExp2Entity && attackerTransfurred && attackerAirHand && !isExp2Form(attacker) && !isCreativeOrSpectator(attacker)) {
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

    private static boolean isCreativeOrSpectator(Entity entity) {
        if (entity instanceof ServerPlayer serverPlayer) {
            GameType type = serverPlayer.gameMode.getGameModeForPlayer();
            return type == GameType.CREATIVE || type == GameType.SPECTATOR;
        } else if (entity instanceof Player player && entity.level.isClientSide()) {
            var info = Objects.requireNonNull(Minecraft.getInstance().getConnection()).getPlayerInfo(player.getGameProfile().getId());
            return info != null && (info.getGameMode() == GameType.CREATIVE || info.getGameMode() == GameType.SPECTATOR);
        }
        return false;
    }

    private static void applyTransfurSickness(Entity target) {
        if (target instanceof LivingEntity living && !living.level.isClientSide()) {
            living.addEffect(new MobEffectInstance(ChangedAddonMobEffects.TRANSFUR_SICKNESS.get(), 2400, 0, false, false));
        }
    }
}
