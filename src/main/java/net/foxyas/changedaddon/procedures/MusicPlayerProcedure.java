package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.configuration.ChangedAddonClientConfigsConfiguration;
import net.foxyas.changedaddon.entity.*;
import net.foxyas.changedaddon.entity.CustomHandle.BossMusicTheme;
import net.foxyas.changedaddon.entity.CustomHandle.BossWithMusic;
import net.minecraft.client.Minecraft;
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
            //PlayBossMusic(event, event.player.level, event.player);
            newPlayBossMusic(event, event.player.level, event.player);
            //PlayBossMusic2(event, event.player.level, event.player);
        }
    }

    public static void newPlayBossMusic(@Nullable Event event, LevelAccessor world, Entity entity) {
        if (entity == null) {
            return;
        }

        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        // Lista de entidades por tipo
        List<Experiment10BossEntity> exp10Entities = world.getEntitiesOfClass(Experiment10BossEntity.class, AABB.ofSize(new Vec3(x, y, z), 64, 64, 64), e -> true);
        List<AbstractLuminarcticLeopard> LumiEntities = world.getEntitiesOfClass(AbstractLuminarcticLeopard.class, AABB.ofSize(new Vec3(x, y, z), 64, 64, 64), e -> true);
        List<KetExperiment009BossEntity> ketExp9Entities = world.getEntitiesOfClass(KetExperiment009BossEntity.class, AABB.ofSize(new Vec3(x, y, z), 64, 64, 64), e -> true);
        List<Experiment009Entity> exp009Phase1Entities = world.getEntitiesOfClass(Experiment009Entity.class, AABB.ofSize(new Vec3(x, y, z), 64, 64, 64), e -> true);
        List<Experiment009phase2Entity> exp009Phase2Entities = world.getEntitiesOfClass(Experiment009phase2Entity.class, AABB.ofSize(new Vec3(x, y, z), 64, 64, 64), e -> true);

        // Verificações de proximidade
        boolean exp10Close = !exp10Entities.isEmpty();
        boolean LumiClose = !LumiEntities.isEmpty();
        boolean ketExp9Close = !ketExp9Entities.isEmpty();
        boolean exp009Phase1Close = !exp009Phase1Entities.isEmpty();
        boolean exp009Phase2Close = !exp009Phase2Entities.isEmpty();

        if (world.isClientSide() && ChangedAddonClientConfigsConfiguration.MUSICPLAYER.get()) {
            Minecraft minecraft = Minecraft.getInstance();
            MusicManager musicManager = minecraft.getMusicManager();
            SoundManager soundManager = minecraft.getSoundManager();

            // Verificar se o jogador está no modo espectador
            boolean spectator = isSpectator(entity);

            if (spectator) {
                return;
            }

            // Eventos de som
            net.minecraft.sounds.SoundEvent exp009Phase2Music = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ChangedAddonMod.MODID, "experiment009_theme_phase2"));
            net.minecraft.sounds.SoundEvent exp009Music = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ChangedAddonMod.MODID, "experiment009_theme"));
            net.minecraft.sounds.SoundEvent exp10Music = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ChangedAddonMod.MODID, "experiment10_theme"));

            // Instâncias de música
            Music exp009ThemeMusicInstance = new Music(Objects.requireNonNull(exp009Music), 0, 0, true);
            Music exp10ThemeMusicInstance = new Music(Objects.requireNonNull(exp10Music), 0, 0, true);
            Music exp009Phase2ThemeMusicInstance = new Music(Objects.requireNonNull(exp009Phase2Music), 0, 0, true);

            // Verificar se músicas estão tocando
            boolean isExp009ThemePlaying = musicManager.isPlayingMusic(exp009ThemeMusicInstance);
            boolean isExp10ThemePlaying = musicManager.isPlayingMusic(exp10ThemeMusicInstance);
            boolean isExp009Phase2ThemePlaying = musicManager.isPlayingMusic(exp009Phase2ThemeMusicInstance);

            if ((exp009Phase2Close || ketExp9Close)) {
                if (!isExp009Phase2ThemePlaying) {
                    musicManager.startPlaying(exp009Phase2ThemeMusicInstance);
                }

                if (exp009Phase2Entities.stream().anyMatch(Experiment009phase2Entity::isDeadOrDying)
                        || ketExp9Entities.stream().anyMatch(KetExperiment009BossEntity::isDeadOrDying)) {
                    soundManager.stop(new ResourceLocation("changed_addon", "experiment009_theme_phase2"), SoundSource.MUSIC);
                }
            } else if (!exp009Phase2Close && !ketExp9Close && isExp009Phase2ThemePlaying) {
                soundManager.stop(new ResourceLocation("changed_addon", "experiment009_theme_phase2"), SoundSource.MUSIC);
            }

            if (exp009Phase1Close) {
                if (!isExp009ThemePlaying) {
                    musicManager.startPlaying(exp009ThemeMusicInstance);
                }

                if (exp009Phase1Entities.stream().anyMatch(Experiment009Entity::isDeadOrDying)) {
                    soundManager.stop(new ResourceLocation("changed_addon", "experiment009_theme"), SoundSource.MUSIC);
                }
            } else if (!exp009Phase1Close && isExp009ThemePlaying) {
                soundManager.stop(new ResourceLocation("changed_addon", "experiment009_theme"), SoundSource.MUSIC);
            }

            if (exp10Close || LumiClose) {
                if (!isExp10ThemePlaying) {
                    musicManager.startPlaying(exp10ThemeMusicInstance);
                }

                if (exp10Entities.stream().anyMatch(Experiment10BossEntity::isDeadOrDying)
                        || LumiEntities.stream().anyMatch(AbstractLuminarcticLeopard::isDeadOrDying)) {
                    soundManager.stop(new ResourceLocation("changed_addon", "experiment10_theme"), SoundSource.MUSIC);
                }
            } else if (!exp10Close && !LumiClose && isExp10ThemePlaying) {
                soundManager.stop(new ResourceLocation("changed_addon", "experiment10_theme"), SoundSource.MUSIC);
            }
        }
    }

    public static void PlayBossMusic(@Nullable Event event, LevelAccessor world, Entity entity) {
        if (entity == null) {
            return;
        }

        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();


        Entity Exp10 = world.getEntitiesOfClass(Experiment10BossEntity.class, AABB.ofSize(new Vec3(x, y, z), 64, 64, 64), e -> true).stream().sorted(new Object() {
                    Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                        return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                    }
                }.compareDistOf(x, y, z)).findFirst().orElse(null);
                //Entity Exp10

        Entity Lumi = world.getEntitiesOfClass(AbstractLuminarcticLeopard.class, AABB.ofSize(new Vec3(x, y, z), 64, 64, 64), e -> true).stream().sorted(new Object() {
            Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
            }
        }.compareDistOf(x, y, z)).findFirst().orElse(null);

        Entity Ket = world.getEntitiesOfClass(KetExperiment009BossEntity.class, AABB.ofSize(new Vec3(x, y, z), 64, 64, 64), e -> true).stream().sorted(new Object() {
            Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
            }
        }.compareDistOf(x, y, z)).findFirst().orElse(null);
        //Entity Experiment009 KET

        Entity Experiment009 = world.getEntitiesOfClass(Experiment009Entity.class, AABB.ofSize(new Vec3(x, y, z), 64, 64, 64), e -> true).stream().sorted(new Object() {
            Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
            }
        }.compareDistOf(x, y, z)).findFirst().orElse(null);
        //Entity Experiment009Phase1

        Entity Experiment009Phase2 = world.getEntitiesOfClass(Experiment009phase2Entity.class, AABB.ofSize(new Vec3(x, y, z), 64, 64, 64), e -> true).stream().sorted(new Object() {
            Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
            }
        }.compareDistOf(x, y, z)).findFirst().orElse(null);
        //Entity Experiment009Phase2

        boolean Exp009Phase1IsClose = !world.getEntitiesOfClass(Experiment009Entity.class, AABB.ofSize(new Vec3(x, y, z), 64, 64, 64), e -> true).isEmpty();
        boolean Exp009Phase2IsClose = !world.getEntitiesOfClass(Experiment009phase2Entity.class, AABB.ofSize(new Vec3(x, y, z), 64, 64, 64), e -> true).isEmpty();

        boolean KetExp9IsClose = !world.getEntitiesOfClass(KetExperiment009BossEntity.class, AABB.ofSize(new Vec3(x, y, z), 64, 64, 64), e -> true).isEmpty()
                || !world.getEntitiesOfClass(KetExperiment009BossEntity.class, AABB.ofSize(new Vec3(x, y, z), 64, 64, 64), e -> true).isEmpty();

        boolean Exp10Close = !world.getEntitiesOfClass(Experiment10BossEntity.class, AABB.ofSize(new Vec3(x, y, z), 64, 64, 64), e -> true).isEmpty()
                || !world.getEntitiesOfClass(Experiment10BossEntity.class, AABB.ofSize(new Vec3(x, y, z), 64, 64, 64), e -> true).isEmpty();

        boolean LumiClose = !world.getEntitiesOfClass(AbstractLuminarcticLeopard.class, AABB.ofSize(new Vec3(x, y, z), 64, 64, 64), e -> true).isEmpty();



        if (world.isClientSide() && ChangedAddonClientConfigsConfiguration.MUSICPLAYER.get()) {
            boolean canwork = false;

            Minecraft minecraft = Minecraft.getInstance();
            MusicManager musicManager = minecraft.getMusicManager();

            boolean Spectator = new Object() {
                public boolean checkGameMode(Player _player) {
                    if (_player instanceof ServerPlayer _serverPlayer) {
                        return _serverPlayer.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
                    } else if (_player.level.isClientSide()) {
                        return Objects.requireNonNull(Minecraft.getInstance().getConnection()).getPlayerInfo(_player.getGameProfile().getId()) != null
                                && Objects.requireNonNull(Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId())).getGameMode() == GameType.SPECTATOR;
                    }
                    return false;
                }
            }.checkGameMode((Player) entity);


            if (!Spectator) {
                canwork = true;
            }

            //Sound Events
            net.minecraft.sounds.SoundEvent Experiment009Phase2Music = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ChangedAddonMod.MODID, "experiment009_theme_phase2"));
            net.minecraft.sounds.SoundEvent Experiment009Music = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ChangedAddonMod.MODID, "experiment009_theme"));
            net.minecraft.sounds.SoundEvent Experiment10Music = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ChangedAddonMod.MODID, "experiment10_theme"));

            //Music Creator
            Music Experiment009_Theme_MusicInstance = new Music(Objects.requireNonNull(Experiment009Music), 0, 0, true);
            Music Experiment10_Theme_MusicInstance = new Music(Objects.requireNonNull(Experiment10Music), 0, 0, true);
            Music Experiment009_phase2_theme_MusicInstance = new Music(Objects.requireNonNull(Experiment009Phase2Music), 0, 0, true);

            //Check if theme is playing
            boolean isExperiment009ThemePlaying = musicManager.isPlayingMusic(Experiment009_Theme_MusicInstance);
            boolean isExperiment10ThemePlaying = musicManager.isPlayingMusic(Experiment10_Theme_MusicInstance);
            boolean isExperiment009Phase2ThemePlaying = musicManager.isPlayingMusic(Experiment009_phase2_theme_MusicInstance);

            if (Exp009Phase2IsClose || KetExp9IsClose && canwork) {
                if (KetExp9IsClose && !isExperiment009Phase2ThemePlaying) {
                    musicManager.startPlaying(Experiment009_phase2_theme_MusicInstance);
                }
                if (!isExperiment009Phase2ThemePlaying) {
                    musicManager.startPlaying(Experiment009_phase2_theme_MusicInstance);
                } else if (Experiment009Phase2 != null && !Experiment009Phase2.isAlive()) {
                    if (isExperiment009Phase2ThemePlaying) {
                        minecraft.getSoundManager().stop(new ResourceLocation("changed_addon", "experiment009_theme_phase2"), SoundSource.MUSIC);
                    }
                }
                if (Ket != null && !Ket.isAlive()) {
                    if (isExperiment009Phase2ThemePlaying) {
                        minecraft.getSoundManager().stop(new ResourceLocation("changed_addon", "experiment009_theme_phase2"), SoundSource.MUSIC);
                    }
                }
                //If the 009 KET is not alive
            } else if (!Exp009Phase2IsClose && !KetExp9IsClose && isExperiment009Phase2ThemePlaying) {
                minecraft.getSoundManager().stop(new ResourceLocation("changed_addon", "experiment009_theme_phase2"), SoundSource.MUSIC);
            } else if (Exp009Phase1IsClose && canwork) {
                if (!isExperiment009ThemePlaying) {
                    musicManager.startPlaying(Experiment009_Theme_MusicInstance);
                } else if (isExperiment009ThemePlaying && Experiment009 != null) {
                    if (!Experiment009.isAlive()) {
                        minecraft.getSoundManager().stop(new ResourceLocation("changed_addon", "experiment009_theme"), SoundSource.MUSIC);
                    }
                }
            } else if (!Exp009Phase1IsClose && isExperiment009ThemePlaying) {
                minecraft.getSoundManager().stop(new ResourceLocation("changed_addon", "experiment009_theme"), SoundSource.MUSIC);
            } else if (Exp10Close && canwork) {
                if (!isExperiment10ThemePlaying) {
                    musicManager.startPlaying(Experiment10_Theme_MusicInstance);
                } else if (isExperiment10ThemePlaying && Exp10 != null) {
                    if (!Exp10.isAlive()) {
                        minecraft.getSoundManager().stop(new ResourceLocation("changed_addon", "experiment10_theme"), SoundSource.MUSIC);
                    }
                }
            } else if (!Exp10Close && !LumiClose && isExperiment10ThemePlaying) {
                minecraft.getSoundManager().stop(new ResourceLocation("changed_addon", "experiment10_theme"), SoundSource.MUSIC);
            } else if (LumiClose && canwork) {
                if (!isExperiment10ThemePlaying) {
                    musicManager.startPlaying(Experiment10_Theme_MusicInstance);
                } else if (isExperiment10ThemePlaying && Lumi != null) {
                    if (!Lumi.isAlive()) {
                        minecraft.getSoundManager().stop(new ResourceLocation("changed_addon", "experiment10_theme"), SoundSource.MUSIC);
                    }
                }
            }

        } // <- This end the client side part,All before that is in client side
    } //End of the Void

    private static boolean isSpectator(Entity entity) {
        if (!(entity instanceof Player player)) {
            return false;
        }

        if (player instanceof ServerPlayer serverPlayer) {
            return serverPlayer.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
        } else if (player.level.isClientSide()) {
            Minecraft minecraft = Minecraft.getInstance();
            return Objects.requireNonNull(minecraft.getConnection())
                    .getPlayerInfo(player.getGameProfile().getId())
                    .getGameMode() == GameType.SPECTATOR;
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

        ResourceLocation musicResource = new ResourceLocation(ChangedAddonMod.MODID, musicKey);
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
            ResourceLocation musicResource = new ResourceLocation(ChangedAddonMod.MODID, musicKey);
            soundManager.stop(musicResource, SoundSource.MUSIC);
        }
    }


    public static void PlayBossMusic2(@Nullable Event event, LevelAccessor world, Entity entity) {
        if (entity == null || !world.isClientSide() || !(entity instanceof Player player)) return;

        boolean spectator = checkGameMode(player);
        if (!ChangedAddonClientConfigsConfiguration.MUSICPLAYER.get() || spectator) return;

        Minecraft minecraft = Minecraft.getInstance();
        MusicManager musicManager = minecraft.getMusicManager();
        SoundManager soundManager = minecraft.getSoundManager();

        Vec3 playerPos = entity.position();

        // Obter bosses próximos e filtrar pela menor distância
        Optional<LivingEntity> optionalBossEntity = world.getEntitiesOfClass(LivingEntity.class,
                        AABB.ofSize(playerPos, 64, 64, 64))
                .stream()
                .min(Comparator.comparingDouble(boss -> boss.distanceToSqr(playerPos)));

        // Se não houver bosses próximos, interrompe qualquer música do boss
        if (optionalBossEntity.isEmpty()) {
            stopAllBossMusic(soundManager);
            return;
        }

        LivingEntity bossEntity = optionalBossEntity.get();

        if (bossEntity instanceof BossWithMusic bossWithMusic && bossWithMusic.ShouldPlayMusic()) {
            SoundEvent bossMusic = bossWithMusic.BossMusicTheme().getAsSoundEvent();

            if (bossMusic == null) return;

            // Checa se alguma música de boss já está tocando
            if (isAnyBossMusicPlaying(musicManager, BossMusicTheme.values())) {
                return; // Não toca nenhuma nova música
            }

            Music musicInstance = bossWithMusic.BossMusicTheme().getAsMusic();
            ResourceLocation musicResource = ForgeRegistries.SOUND_EVENTS.getKey(bossMusic);

            // Se o boss estiver vivo e a música ainda não estiver tocando, inicia a música
            if (bossEntity.isAlive() && !musicManager.isPlayingMusic(musicInstance)) {
                musicManager.startPlaying(musicInstance);
            }
            // Se o boss morreu ou saiu do alcance, interrompe a música específica
            else if (!bossEntity.isAlive() && musicManager.isPlayingMusic(musicInstance)) {
                soundManager.stop(musicResource, SoundSource.MUSIC);
            }
        }
        /*
        * Hard Coded Entities Bellow
        */

        else if (bossEntity instanceof Experiment009Entity){
            SoundEvent bossMusic = BossMusicTheme.EXP9_PHASE1.getAsSoundEvent();
            Music musicInstance = BossMusicTheme.EXP9_PHASE1.getAsMusic();
            ResourceLocation musicResource = ForgeRegistries.SOUND_EVENTS.getKey(bossMusic);
            if (bossMusic == null) return;

            // Checa se alguma música de boss já está tocando
            if (isAnyBossMusicPlaying(musicManager, BossMusicTheme.values())) {
                return; // Não toca nenhuma nova música
            }
            // Se o boss estiver vivo e a música ainda não estiver tocando, inicia a música
            if (bossEntity.isAlive() && !musicManager.isPlayingMusic(musicInstance)) {
                musicManager.startPlaying(musicInstance);
            }
            // Se o boss morreu ou saiu do alcance, interrompe a música específica
            else if (!bossEntity.isAlive() && musicManager.isPlayingMusic(musicInstance)) {
                soundManager.stop(musicResource, SoundSource.MUSIC);
            }
        } else if (bossEntity instanceof Experiment009phase2Entity) {
            SoundEvent bossMusic = BossMusicTheme.EXP9_PHASE2.getAsSoundEvent();
            Music musicInstance = BossMusicTheme.EXP9_PHASE2.getAsMusic();
            ResourceLocation musicResource = ForgeRegistries.SOUND_EVENTS.getKey(bossMusic);
            if (bossMusic == null) return;

            // Checa se alguma música de boss já está tocando
            if (isAnyBossMusicPlaying(musicManager, BossMusicTheme.values())) {
                return; // Não toca nenhuma nova música
            }
            // Se o boss estiver vivo e a música ainda não estiver tocando, inicia a música
            if (bossEntity.isAlive() && !musicManager.isPlayingMusic(musicInstance)) {
                musicManager.startPlaying(musicInstance);
            }
            // Se o boss morreu ou saiu do alcance, interrompe a música específica
            else if (!bossEntity.isAlive() && musicManager.isPlayingMusic(musicInstance)) {
                soundManager.stop(musicResource, SoundSource.MUSIC);
            }
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
        } else if (_player.level.isClientSide()) {
            return Objects.requireNonNull(Minecraft.getInstance().getConnection()).getPlayerInfo(_player.getGameProfile().getId()) != null
                    && Objects.requireNonNull(Minecraft.getInstance().getConnection().getPlayerInfo(_player.getGameProfile().getId())).getGameMode() == GameType.SPECTATOR;
        }
        return false;
    }

}
