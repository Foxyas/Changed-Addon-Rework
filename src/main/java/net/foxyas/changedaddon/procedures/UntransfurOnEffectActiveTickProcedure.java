package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.foxyas.changedaddon.process.util.DelayedTask;
import net.foxyas.changedaddon.process.util.PlayerUtil;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class UntransfurOnEffectActiveTickProcedure {
    public static void execute(Level level, double x, double y, double z, Entity entity) {
        if (entity == null)
            return;
        if (entity instanceof ChangedEntity livEnt) {
            if (entity.getType().is(ChangedTags.EntityTypes.LATEX)) {
                entity.hurt((new DamageSource("latex_solvent")),
                        (float) ((livEnt.hasEffect(ChangedAddonMobEffects.UNTRANSFUR.get()) ? livEnt.getEffect(ChangedAddonMobEffects.UNTRANSFUR.get()).getAmplifier() : 0) + 1));
            }
        }
        if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).UntransfurProgress >= 100 && entity instanceof  Player player) {
            if (ProcessTransfur.isPlayerTransfurred(player)) {
                SummonDripParticlesProcedure.execute(entity);
                PlayerUtil.UnTransfurPlayer(entity);
                entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                    capability.UntransfurProgress = 0;
                    capability.syncPlayerVariables(entity);
                });
                player.removeEffect(ChangedAddonMobEffects.UNTRANSFUR.get());
                if ((player.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).reset_transfur_advancements) {
                    new DelayedTask(10 , () -> {
                        if (!player.level.isClientSide() && player.getServer() != null) {
                            player.getServer().getCommands().performCommand(player.createCommandSourceStack().withSuppressedOutput().withPermission(4), "advancement revoke @s from minecraft:changed/transfur");
                        }
                    });
                }

                if (!(entity instanceof ServerPlayer sPlayer && sPlayer.level instanceof ServerLevel
                        && sPlayer.getAdvancements().getOrStartProgress(Objects.requireNonNull(sPlayer.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:untransfuradvancement")))).isDone())) {
                    if (entity instanceof ServerPlayer _player) {
                        Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:untransfuradvancement"));
                        assert _adv != null;
                        AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
                        if (!_ap.isDone()) {
                            for (String s : _ap.getRemainingCriteria()) _player.getAdvancements().award(_adv, s);
                        }
                    }
                }

                if (!level.isClientSide()) {
                    if(!player.isCreative() && !player.isSpectator()){
                        player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60, 0, false, false));
                        player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 40, 0, false, false));
                    }

                    level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:untransfursound")), SoundSource.NEUTRAL, 1, 1);
                } else {
                    level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:untransfursound")), SoundSource.NEUTRAL, 1, 1, false);
                }
            } else {
                if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).showwarns) {
                    if (!player.level.isClientSide())
                        player.displayClientMessage(new TextComponent((new TranslatableComponent("changedaddon.untransfur.no_effect").getString())), true);
                }
            }
        }
    }
}
