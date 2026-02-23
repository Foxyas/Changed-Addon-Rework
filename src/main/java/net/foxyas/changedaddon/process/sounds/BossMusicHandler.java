package net.foxyas.changedaddon.process.sounds;

import net.foxyas.changedaddon.entity.api.IHasBossMusic;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

@OnlyIn(Dist.CLIENT)
public class BossMusicHandler {

    private static final Minecraft mc = Minecraft.getInstance();
    // Agora suportando vários bosses
    private static final Map<IHasBossMusic, FadingBossMusicSound> activeBosses = new HashMap<>();

    public static void tick(ClientLevel level) {
        if (mc.player == null || level == null) return;

        // Guardar os bosses que estão ativos nesse tick
        Set<IHasBossMusic> nearbyBosses = new HashSet<>();

        for (Entity entity : level.entitiesForRendering()) {
            if (!(entity instanceof IHasBossMusic boss)) continue;
            if (entity instanceof ChangedEntity changedEntity && changedEntity.getUnderlyingPlayer() != null) continue;
            if (!entity.isAlive()) continue;

            double distSq = entity.distanceToSqr(mc.player);
            if (distSq <= boss.getMusicRange() * boss.getMusicRange()) {
                nearbyBosses.add(boss);

                // Se não temos som ativo para esse boss, toca
                if (!activeBosses.containsKey(boss)) {
                    playMusic(boss);
                } else {
                    FadingBossMusicSound sound = activeBosses.get(boss);

                    // Retoca caso tenha parado
                    if (!mc.getSoundManager().isActive(sound) || sound.isStopped()) {
                        playMusic(boss);
                    }
                    // Se a música mudou (ex: fase nova)
                    else if (sound.getLocation() != boss.getBossMusic()) {
                        stopMusic(boss);
                        playMusic(boss);
                    }
                }
            }
        }

        // Agora vamos limpar bosses que não estão mais válidos
        Iterator<Map.Entry<IHasBossMusic, FadingBossMusicSound>> it = activeBosses.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<IHasBossMusic, FadingBossMusicSound> entry = it.next();
            IHasBossMusic boss = entry.getKey();
            FadingBossMusicSound sound = entry.getValue();

            if (!nearbyBosses.contains(boss) || boss.getSelf().isDeadOrDying()) {
                stopMusic(boss);
                it.remove();
            }
        }
    }

    private static void playMusic(IHasBossMusic boss) {
        if (boss == null) return;
        ResourceLocation music = boss.getBossMusic();
        SoundEvent event = ForgeRegistries.SOUND_EVENTS.getValue(music);
        if (event == null) return;

        FadingBossMusicSound sound = new FadingBossMusicSound(event, boss.getSelf());
        sound.setPitch(boss.getMusicPitch());
        sound.setVolume(boss.getMusicVolume());
        mc.getSoundManager().play(sound);
        activeBosses.put(boss, sound);
    }

    private static void stopMusic(IHasBossMusic boss) {
        FadingBossMusicSound sound = activeBosses.get(boss);
        if (sound != null) {
            sound.startFadeOut();
        }
    }

    public enum FollowType {
        BOSS,
        PLAYER_CAMERA
    }
}
