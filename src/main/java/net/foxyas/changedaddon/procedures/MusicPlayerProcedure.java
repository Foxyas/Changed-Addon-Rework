package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.configuration.ChangedAddonClientConfigsConfiguration;
import net.foxyas.changedaddon.entity.*;
import net.foxyas.changedaddon.entity.CustomHandle.BossMusicTheme;
import net.foxyas.changedaddon.entity.CustomHandle.BossWithMusic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Mod.EventBusSubscriber
public class MusicPlayerProcedure {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            newPlayBossMusic(event.player.level, event.player);
        }
    }

    public static void newPlayBossMusic(LevelAccessor world, Player player) {
        if (player == null) {
            return;
        }

        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();

        // Lista de entidades por tipo
        List<Experiment10BossEntity> exp10Entities = world.getEntitiesOfClass(Experiment10BossEntity.class, AABB.ofSize(new Vec3(x, y, z), 64, 64, 64), e -> true);
        List<AbstractLuminarcticLeopard> LumiEntities = world.getEntitiesOfClass(AbstractLuminarcticLeopard.class, AABB.ofSize(new Vec3(x, y, z), 64, 64, 64), e -> true);
        List<KetExperiment009BossEntity> ketExp9Entities = world.getEntitiesOfClass(KetExperiment009BossEntity.class, AABB.ofSize(new Vec3(x, y, z), 64, 64, 64), e -> true);

        // Verificações de proximidade
        boolean exp10Close = !exp10Entities.isEmpty();
        boolean LumiClose = !LumiEntities.isEmpty();
        boolean ketExp9Close = !ketExp9Entities.isEmpty();

        if (world.isClientSide() && ChangedAddonClientConfigsConfiguration.MUSICPLAYER.get()) {
            Minecraft minecraft = Minecraft.getInstance();
            MusicManager musicManager = minecraft.getMusicManager();
            SoundManager soundManager = minecraft.getSoundManager();

            // Verificar se o jogador está no modo espectador
            boolean spectator = isSpectator(player);

            if (spectator) {
                return;
            }

            // Eventos de som
            SoundEvent exp009Music = ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse(ChangedAddonMod.MODID, "music.boss.exp9"));
            SoundEvent exp10Music = ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse(ChangedAddonMod.MODID, "experiment10_theme"));
            SoundEvent LumiMusic = ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse(ChangedAddonMod.MODID, "music.boss.luminarctic_leopard"));

            // Instâncias de música
            Music exp10ThemeMusicInstance = new Music(Objects.requireNonNull(exp10Music), 0, 0, true);
            Music exp009Phase2ThemeMusicInstance = new Music(Objects.requireNonNull(exp009Music), 0, 0, true);
            Music LumiThemeMusicInstance = new Music(Objects.requireNonNull(LumiMusic), 0, 0, true);

            // Verificar se músicas estão tocando
            boolean isExp10ThemePlaying = musicManager.isPlayingMusic(exp10ThemeMusicInstance);
            boolean isExp009Phase2ThemePlaying = musicManager.isPlayingMusic(exp009Phase2ThemeMusicInstance);
            boolean isLumiThemePlaying = musicManager.isPlayingMusic(LumiThemeMusicInstance);


            if ((ketExp9Close)) {
                if (!isExp009Phase2ThemePlaying) {
                    if (!exp10Close && !LumiClose) {
                        musicManager.startPlaying(exp009Phase2ThemeMusicInstance);
                    }
                }

                if (ketExp9Entities.stream().anyMatch(KetExperiment009BossEntity::isDeadOrDying)) {
                    soundManager.stop(ResourceLocation.parse("changed_addon", "music.boss.exp9"), SoundSource.MUSIC);
                }
            } else if (isExp009Phase2ThemePlaying) {
                soundManager.stop(ResourceLocation.parse("changed_addon", "music.boss.exp9"), SoundSource.MUSIC);
            }

            if (exp10Close) {
                if (!isExp10ThemePlaying) {
                    if (!ketExp9Close) {
                        musicManager.startPlaying(exp10ThemeMusicInstance);
                    }
                }

                if (exp10Entities.stream().anyMatch(LivingEntity::isDeadOrDying)) {
                    soundManager.stop(ResourceLocation.parse("changed_addon", "experiment10_theme"), SoundSource.MUSIC);
                }
            } else if (isExp10ThemePlaying) {
                soundManager.stop(ResourceLocation.parse("changed_addon", "experiment10_theme"), SoundSource.MUSIC);
            }

            if (LumiClose && LumiEntities.stream().anyMatch((e) -> e.getTarget() == player)) {
                if (!isLumiThemePlaying) {
                    if (!exp10Close && !ketExp9Close)  {
                        musicManager.startPlaying(LumiThemeMusicInstance);
                    }
                }

                if (LumiEntities.stream().anyMatch(LivingEntity::isDeadOrDying)) {
                    soundManager.stop(ResourceLocation.parse("changed_addon", "music.boss.luminarctic_leopard"), SoundSource.MUSIC);
                }
            } else if (isLumiThemePlaying) {
                soundManager.stop(ResourceLocation.parse("changed_addon", "music.boss.luminarctic_leopard"), SoundSource.MUSIC);
            }
        }
    }

    private static boolean isSpectator(Entity entity) {
        if (!(entity instanceof Player player)) {
            return false;
        }

        if (player instanceof ServerPlayer serverPlayer) {
            return serverPlayer.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
        } else if (player.level().isClientSide()) {
            Minecraft minecraft = Minecraft.getInstance();
            ClientPacketListener connection = minecraft.getConnection();
            if (connection != null){
                var f = connection
                        .getPlayerInfo(player.getGameProfile().getId());
                return f != null && f.getGameMode() == GameType.SPECTATOR;
            }
        }

        return false;
    }

    private static List<LivingEntity> getNearbyBosses(LevelAccessor world, Vec3 position, double radius) {
        return world.getEntitiesOfClass(LivingEntity.class, AABB.ofSize(position, radius, radius, radius))
                .stream()
                .sorted(Comparator.comparingDouble(e -> e.distanceToSqr(position)))
                .toList();
    }

    private static void handleBossMusic(String musicKey, Entity boss) {
        Minecraft minecraft = Minecraft.getInstance();
        MusicManager musicManager = minecraft.getMusicManager();
        SoundManager soundManager = minecraft.getSoundManager();

        ResourceLocation musicResource = ResourceLocation.parse(ChangedAddonMod.MODID, musicKey);
        SoundEvent soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(musicResource);

        if (soundEvent == null) {
            return; // Se o evento de som não existir, sai.
        }

        Music bossMusic = new Music(soundEvent, 0, 0, true);

        // Se o boss está vivo e a música não está tocando, começa a tocar
        if (boss.isAlive() && !musicManager.isPlayingMusic(bossMusic)) {
            musicManager.startPlaying(bossMusic);
        } else if (!boss.isAlive()) { // Para a música se o boss morrer
            soundManager.stop(musicResource, SoundSource.MUSIC);
        }
    }

    private static void stopAllBossMusic() {
        Minecraft minecraft = Minecraft.getInstance();
        SoundManager soundManager = minecraft.getSoundManager();

        // Lista de músicas para parar
        String[] bossMusicKeys = {
                "experiment009_theme",
                "experiment009_theme_phase2",
                "experiment10_theme"
        };

        for (String musicKey : bossMusicKeys) {
            ResourceLocation musicResource = ResourceLocation.parse(ChangedAddonMod.MODID, musicKey);
            soundManager.stop(musicResource, SoundSource.MUSIC);
        }
    }

    private static void stopAllBossMusic(SoundManager soundManager) {
        // Itera pelos temas registrados nos enums e interrompe qualquer música de boss
        for (BossMusicTheme theme : BossMusicTheme.values()) {
            ResourceLocation resource = ForgeRegistries.SOUND_EVENTS.getKey(theme.getAsSoundEvent());
            if (resource != null) {
                soundManager.stop(resource, SoundSource.MUSIC);
            }
        }
    }

    /**
     * Verifica se alguma música dos temas de boss está tocando.
     *
     * @param musicManager Gerenciador de música.
     * @param themes Lista de temas.
     * @return Verdadeiro se alguma música estiver tocando.
     */
    private static boolean isAnyBossMusicPlaying(MusicManager musicManager, BossMusicTheme[] themes) {
        for (BossMusicTheme theme : themes) {
            Music musicInstance = theme.getAsMusic();
            if (musicManager.isPlayingMusic(musicInstance)) {
                return true;
            }
        }
        return false;
    }


    public static boolean checkGameMode(Player _player) {
        if (_player instanceof ServerPlayer _serverPlayer) {
            return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
        } else if (_player.level().isClientSide()) {
            return Objects.requireNonNull(Minecraft.getInstance().getConnection()).getPlayerInfo(_player.getGameProfile().getId()) != null
                    && Objects.requireNonNull(Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId())).getGameMode() == GameType.SPECTATOR;
        }
        return false;
    }

}
