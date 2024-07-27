package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.entity.Experiment10Entity;
import net.foxyas.changedaddon.entity.KetExperiment009Entity;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.Music;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.client.Minecraft;

import net.foxyas.changedaddon.entity.Experiment009phase2Entity;
import net.foxyas.changedaddon.entity.Experiment009Entity;
import net.foxyas.changedaddon.configuration.ChangedAddonClientConfigsConfiguration;
import net.foxyas.changedaddon.ChangedAddonMod;

import javax.annotation.Nullable;

import java.util.Comparator;
import java.util.Objects;

@Mod.EventBusSubscriber
public class MusicPlayerProcedure {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            PlayBossMusic(event, event.player.level, event.player);
        }
    }

    public static void PlayBossMusic(@Nullable Event event, LevelAccessor world, Entity entity) {
        if (entity == null) {
            return;
        }

        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        Entity Exp10 = world.getEntitiesOfClass(Experiment10Entity.class, AABB.ofSize(new Vec3(x, y, z), 64, 64, 64), e -> true).stream().sorted(new Object() {
            Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
            }
        }.compareDistOf(x, y, z)).findFirst().orElse(null);
        //Entity Exp10

        Entity Ket = world.getEntitiesOfClass(KetExperiment009Entity.class, AABB.ofSize(new Vec3(x, y, z), 64, 64, 64), e -> true).stream().sorted(new Object() {
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
        boolean KetExp9IsClose = !world.getEntitiesOfClass(KetExperiment009Entity.class, AABB.ofSize(new Vec3(x, y, z), 64, 64, 64), e -> true).isEmpty();
        boolean Exp10Close = !world.getEntitiesOfClass(Experiment10Entity.class, AABB.ofSize(new Vec3(x, y, z), 64, 64, 64), e -> true).isEmpty();


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
                } //If the 009 KET is not alive
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
            } else if (!Exp10Close && isExperiment10ThemePlaying) {
                minecraft.getSoundManager().stop(new ResourceLocation("changed_addon", "experiment10_theme"), SoundSource.MUSIC);
            }

        } // <- This end the client side part,All before that is in client side
    } //End of the Void
}
