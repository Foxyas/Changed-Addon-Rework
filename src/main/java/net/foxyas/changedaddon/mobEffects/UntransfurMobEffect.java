package net.foxyas.changedaddon.mobEffects;

import com.google.common.collect.Iterables;
import net.foxyas.changedaddon.init.ChangedAddonDamageSources;
import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.procedure.SummonDripParticlesProcedure;
import net.foxyas.changedaddon.util.DelayedTask;
import net.foxyas.changedaddon.util.PlayerUtil;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.Util;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UntransfurMobEffect extends MobEffect {

    public UntransfurMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -1);
    }

    @Override
    public @NotNull String getDescriptionId() {
        return "effect.changed_addon.untransfur";
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        Level level = entity.level;

        if (entity instanceof ChangedEntity changedEntity) {
            if (changedEntity.getType().is(ChangedTags.EntityTypes.LATEX) && changedEntity.hasEffect(this)) {
                float currentHealth = changedEntity.getHealth();

                if (currentHealth <= 1.0F) {
                    return;
                }

                float damageToDeal = amplifier;

                if (currentHealth - damageToDeal <= 0) {
                    damageToDeal = currentHealth - 1.0F;
                }

                changedEntity.hurt(ChangedAddonDamageSources.LATEX_SOLVENT.source(level), damageToDeal);
            }
            return;
        }

        if (!(entity instanceof Player player)) return;
        ChangedAddonVariables.PlayerVariables vars = ChangedAddonVariables.of(player);
        if (vars == null || vars.untransfurProgress < 100) return;

        if (!ProcessTransfur.isPlayerTransfurred(player)) {
            if (vars.showWarns) {
                if (!player.level.isClientSide())
                    player.displayClientMessage(Component.literal((Component.translatable("effect.changed_addon.untransfur.no_effect").getString())), true);
            }
            return;
        }

        SummonDripParticlesProcedure.execute(player);

        vars.untransfurProgress = 0;
        vars.syncPlayerVariables(entity);

        player.removeEffect(ChangedAddonMobEffects.UNTRANSFUR.get());

        if (vars.resetTransfurAdvancements) new DelayedTask(10, () -> {
            removePlayerTransfurAdvancements(player);
        });

        grandPlayerUntransfurAdvancement(player);
        PlayerUtil.unTransfurPlayerAndPlaySound(player, !player.isCreative() && !player.isSpectator());
    }

    public static void grandPlayerUntransfurAdvancement(Player player) {
        if (player instanceof ServerPlayer sPlayer) {
            if (sPlayer.level instanceof ServerLevel) {
                Advancement advancement = sPlayer.server.getAdvancements().getAdvancement(ResourceLocation.parse("changed_addon:untransfur_advancement"));
                if (!sPlayer.getAdvancements().getOrStartProgress(Objects.requireNonNull(advancement)).isDone()) {
                    AdvancementProgress advancementProgress = sPlayer.getAdvancements().getOrStartProgress(advancement);
                    if (!advancementProgress.isDone()) {
                        for (String s : advancementProgress.getRemainingCriteria()) {
                            sPlayer.getAdvancements().award(advancement, s);
                        }
                    }
                }
            }
        }
    }

    public static void removePlayerTransfurAdvancements(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            Advancement rootAdvancement = serverPlayer.server.getAdvancements().getAdvancement(ResourceLocation.parse("minecraft:changed/transfur"));
            if (rootAdvancement != null) {
                List<Advancement> advancementsToRemove = Util.make(new ArrayList<>(), list -> {
                    list.add(rootAdvancement);
                    Iterables.addAll(list, rootAdvancement.getChildren());
                });
                PlayerAdvancements playerAdvancements = serverPlayer.getAdvancements();
                for (Advancement advancement : advancementsToRemove) {
                    AdvancementProgress advancementProgress = playerAdvancements.getOrStartProgress(advancement);
                    if (!advancementProgress.hasProgress() || !advancementProgress.isDone()) {
                        continue;
                    }
                    Iterable<String> completedCriteria = advancementProgress.getCompletedCriteria();
                    for (String criterion : completedCriteria) {
                        playerAdvancements.revoke(advancement, criterion);
                    }
                }
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Mod.EventBusSubscriber
    public static class EventHandler {

        @SubscribeEvent
        public static void onEntityEndSleep(PlayerWakeUpEvent event) {
            Entity entity = event.getEntity();
            Level level = entity.level;

            if (!level.isDay() || !(entity instanceof Player player)
                    || !player.hasEffect(ChangedAddonMobEffects.UNTRANSFUR.get())) return;

            new DelayedTask(5, () -> player.getCapability(ChangedAddonVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(cap -> {
                if (ProcessTransfur.isPlayerTransfurred(player) && player.isSleepingLongEnough()) {
                    cap.untransfurProgress += 50;
                    cap.syncPlayerVariables(player);
                }
            }));
        }
    }
}
