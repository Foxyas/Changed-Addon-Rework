package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.foxyas.changedaddon.process.util.PlayerUtil;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class LaethinSyringePlayerFinishesUsingItemProcedure {

    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
        if (entity == null || !(entity instanceof Player player)) return;

        boolean transfur = ProcessTransfur.isPlayerTransfurred(player);
        boolean organic_transfur = ProcessTransfur.isPlayerNotLatex(player);
        var playerVars = entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null)
                .orElse(new ChangedAddonModVariables.PlayerVariables());

        if (!transfur) {
            if (playerVars.showwarns && !entity.level.isClientSide())
                player.displayClientMessage(new TranslatableComponent("changedaddon.untransfur.no_effect"), true);
            return;
        }

        if (organic_transfur) {
            applyMobEffect(entity, ChangedAddonMobEffects.UNTRANSFUR.get(), 1000);
            if (playerVars.showwarns && !entity.level.isClientSide())
                player.displayClientMessage(new TranslatableComponent("changedaddon.untransfur.sloweffect"), true);
            return;
        }

        // Visual feedback
        SummonDripParticlesProcedure.execute(entity);
        PlayerUtil.UnTransfurPlayer(entity);

        // Optional: Reset advancement
        if (playerVars.reset_transfur_advancements) {
            scheduleAdvancementReset(entity, world, 10);
        }

        // Apply blindness/confusion if in survival or adventure
        if (isSurvivalOrAdventure(entity)) {
            applyMobEffect(entity, MobEffects.BLINDNESS, 40);
            applyMobEffect(entity, MobEffects.CONFUSION, 60);
        }

        // Grant untransfur advancement if not already
        grantAdvancementIfNotDone(player, "changed_addon:untransfuradvancement_2");

        // Play sound
        playSound(world, x, y, z, "changed_addon:untransfursound");

        // Return empty syringe unless in creative/spectator
        if (!isCreativeOrSpectator(entity)) {
            ItemHandlerHelper.giveItemToPlayer(player,
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:syringe"))));
        }
    }

    private static void applyMobEffect(Entity entity, MobEffectInstance effect) {
        if (entity instanceof LivingEntity living && !living.level.isClientSide())
            living.addEffect(effect);
    }

    private static void applyMobEffect(Entity entity, MobEffect effect, int duration) {
        applyMobEffect(entity, new MobEffectInstance(effect, duration, 0, false, false));
    }

    private static boolean isSurvivalOrAdventure(Entity entity) {
        if (entity instanceof ServerPlayer serverPlayer)
            return serverPlayer.gameMode.getGameModeForPlayer().isSurvival() || serverPlayer.gameMode.getGameModeForPlayer() == GameType.ADVENTURE;
        return false;
    }

    private static boolean isCreativeOrSpectator(Entity entity) {
        if (entity instanceof ServerPlayer serverPlayer)
            return serverPlayer.gameMode.getGameModeForPlayer() == GameType.CREATIVE || serverPlayer.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
        return false;
    }

    private static void grantAdvancementIfNotDone(ServerPlayer player, String advancementId) {
        Advancement advancement = player.server.getAdvancements().getAdvancement(new ResourceLocation(advancementId));
        if (advancement == null) return;

        AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
        if (!progress.isDone()) {
            for (String criterion : progress.getRemainingCriteria()) {
                player.getAdvancements().award(advancement, criterion);
            }
        }
    }

    private static void grantAdvancementIfNotDone(Player player, String advancementId) {
        if (player instanceof ServerPlayer serverPlayer) {
            grantAdvancementIfNotDone(serverPlayer, advancementId);
        }
    }

    private static void scheduleAdvancementReset(Entity entity, LevelAccessor world, int delayTicks) {
        MinecraftForge.EVENT_BUS.register(new Object() {
            int ticks = 0;

            @SubscribeEvent
            public void tick(TickEvent.ServerTickEvent event) {
                if (event.phase == TickEvent.Phase.END && ++ticks >= delayTicks) {
                    if (!entity.level.isClientSide() && entity.getServer() != null)
                        entity.getServer().getCommands().performCommand(entity.createCommandSourceStack().withSuppressedOutput().withPermission(4),
                                "advancement revoke @s from minecraft:changed/transfur");
                    MinecraftForge.EVENT_BUS.unregister(this);
                }
            }
        });
    }

    private static void playSound(LevelAccessor world, double x, double y, double z, String soundId) {
        if (!(world instanceof Level level)) return;

        var sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(soundId));
        if (sound == null) return;

        if (!level.isClientSide())
            level.playSound(null, new BlockPos(x, y, z), sound, SoundSource.NEUTRAL, 1, 1);
        else
            level.playLocalSound(x, y, z, sound, SoundSource.NEUTRAL, 1, 1, false);
    }
}
