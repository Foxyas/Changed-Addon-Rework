package net.foxyas.changedaddon.mobEffects;

import net.foxyas.changedaddon.init.ChangedAddonDamageSources;
import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.init.ChangedAddonSoundEvents;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.procedure.SummonDripParticlesProcedure;
import net.foxyas.changedaddon.util.DelayedTask;
import net.foxyas.changedaddon.util.PlayerUtil;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

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

        if (entity instanceof ChangedEntity livEnt) {
            if (entity.getType().is(ChangedTags.EntityTypes.LATEX)) {
                entity.hurt(ChangedAddonDamageSources.LATEX_SOLVENT.source(level),
                        (float) ((livEnt.hasEffect(ChangedAddonMobEffects.UNTRANSFUR.get()) ? livEnt.getEffect(ChangedAddonMobEffects.UNTRANSFUR.get()).getAmplifier() : 0) + 1));
            }
            return;
        }

        if (!(entity instanceof Player player)) return;
        ChangedAddonVariables.PlayerVariables vars = ChangedAddonVariables.of(player);
        if (vars == null || vars.untransfurProgress < 100) return;

        if (!ProcessTransfur.isPlayerTransfurred(player)) {
            if (vars.showWarns) {
                if (!player.level.isClientSide())
                    player.displayClientMessage(Component.literal((Component.translatable("changed_addon.untransfur.no_effect").getString())), true);
            }
            return;
        }

        SummonDripParticlesProcedure.execute(player);
        PlayerUtil.unTransfurPlayer(player);

        vars.untransfurProgress = 0;
        vars.syncPlayerVariables(entity);

        player.removeEffect(ChangedAddonMobEffects.UNTRANSFUR.get());

        if (vars.resetTransfurAdvancements) new DelayedTask(10, () -> {
            MinecraftServer server = player.getServer();
            if (server != null)
                server.getCommands().performPrefixedCommand(player.createCommandSourceStack().withSuppressedOutput().withPermission(4), "advancement revoke @s from minecraft:changed/transfur");
        });

        if (!(entity instanceof ServerPlayer sPlayer && sPlayer.level instanceof ServerLevel
                && sPlayer.getAdvancements().getOrStartProgress(Objects.requireNonNull(sPlayer.server.getAdvancements().getAdvancement(ResourceLocation.parse("changed_addon:untransfur_advancement")))).isDone())) {
            if (entity instanceof ServerPlayer _player) {
                Advancement _adv = _player.server.getAdvancements().getAdvancement(ResourceLocation.parse("changed_addon:untransfur_advancement"));
                assert _adv != null;
                AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
                if (!_ap.isDone()) {
                    for (String s : _ap.getRemainingCriteria()) _player.getAdvancements().award(_adv, s);
                }
            }
        }

        if (!level.isClientSide && !player.isCreative() && !player.isSpectator()) {
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60, 0, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 40, 0, false, false));
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(), ChangedAddonSoundEvents.UNTRANSFUR.get(), SoundSource.NEUTRAL, 1, 1);
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
