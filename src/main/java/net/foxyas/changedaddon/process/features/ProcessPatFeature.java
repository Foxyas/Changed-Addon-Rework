package net.foxyas.changedaddon.process.features;

import net.foxyas.changedaddon.init.ChangedAddonCriteriaTriggers;
import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.Emote;
import net.ltxprogrammer.changed.init.ChangedParticles;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

public class ProcessPatFeature {

    public static void spawnEmote(LivingEntity patter, LivingEntity target, ServerLevel level) {
        if (target instanceof Player targetPl && !ProcessTransfur.isPlayerTransfurred(targetPl)) return;

        if (target instanceof ChangedEntity changedEntity) {
            if (changedEntity.getTarget() == patter) return;

            if (PatFeatureHandle.shouldBeConfused(patter, changedEntity)) {
                level.sendParticles(ChangedParticles.emote(target, Emote.CONFUSED),
                        target.getX(), target.getY() + (double) target.getDimensions(target.getPose()).height + 0.65, target.getZ(),
                        0, 0, 0, 0, 0);
                return;
            }
        }

        level.sendParticles(ChangedParticles.emote(target, Emote.HEART),
                target.getX(), target.getY() + (double) target.getDimensions(target.getPose()).height + 0.65, target.getZ(),
                0, 0, 0, 0, 0);
    }

    public static class GlobalPatReactionEvent extends Event {
        public final LivingEntity patter;
        public final LivingEntity target;
        public final LevelAccessor world;
        public final InteractionHand hand;
        @Nullable
        public final Vec3 pattedLocation;

        public GlobalPatReactionEvent(LevelAccessor world, LivingEntity patter, InteractionHand hand, LivingEntity target, @Nullable Vec3 pattedLocation) {
            this.patter = patter;
            this.target = target;
            this.world = world;
            this.hand = hand;
            this.pattedLocation = pattedLocation;
        }

        @Nullable
        public Vec3 getPattedLocation() {
            return pattedLocation;
        }

        public boolean isCancelable() {
            return true;
        }
    }

    @Mod.EventBusSubscriber
    public static class HandleGlobalPatReaction {

        @SubscribeEvent
        public static void HandlePat(GlobalPatReactionEvent event) {
            LivingEntity patter = event.patter;
            LivingEntity target = event.target;

            if (!(patter.level instanceof ServerLevel level)) return;
            if (!(patter instanceof Player player)) return;

            if (target instanceof ChangedEntity changedEntity && !ProcessTransfur.isPlayerTransfurred(player)) {
                if (!PatFeatureHandle.shouldBeConfused(player, changedEntity)) {
                    RandomSource random = changedEntity.getRandom();
                    if (random.nextFloat() <= 0.0005f) {
                        changedEntity.addEffect(new MobEffectInstance(ChangedAddonMobEffects.PACIFIED.get(), 600, 0, true, false, true), player);
                        if (player instanceof ServerPlayer serverPlayer) {
                            ChangedAddonCriteriaTriggers.PAT_ENTITY_TRIGGER.trigger(serverPlayer, changedEntity, "paticifier");
                        }
                    }
                }
            }

            player.displayClientMessage(Component.translatable("key.changed_addon.pat_message", target.getDisplayName().getString()), true);
            if (target instanceof Player targetPlayer) {
                targetPlayer.displayClientMessage(Component.translatable("key.changed_addon.pat_received", player.getDisplayName().getString()), true);
            }

            spawnEmote(player, target, level);
        }
    }

}
