package net.foxyas.changedaddon.process.util.sounds;

import net.foxyas.changedaddon.entity.customHandle.IHasBossMusic;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

@OnlyIn(value = Dist.CLIENT)
public class BossMusicHandler {
    private static final Minecraft mc = Minecraft.getInstance();

    private static IHasBossMusic currentBoss;
    private static FadingBossMusicSound currentSound;

    public static void tick(ClientLevel level) {
        if (mc.player == null || level == null) return;

        if (currentBoss != null && currentSound != null) {
            if (currentBoss.getSelf().isDeadOrDying() && (mc.getSoundManager().isActive(currentSound) || !currentSound.isStopped())) {
                stopMusic();
            }

            if (currentBoss != null && currentSound != null && currentSound.getLocation() != currentBoss.getBossMusic()) {
                stopMusic();
                playMusic(currentBoss);
            }
        }

        IHasBossMusic closestBoss = null;
        double closestDistanceSq = Double.MAX_VALUE;

        for (Entity entity : level.entitiesForRendering()) {
            if (!(entity instanceof IHasBossMusic boss)) continue;
            if (entity instanceof ChangedEntity changedEntity && changedEntity.getUnderlyingPlayer() != null) continue;
            if (!entity.isAlive()) continue;

            double distSq = entity.distanceToSqr(mc.player);
            if (distSq <= boss.getMusicRange() * boss.getMusicRange() && distSq < closestDistanceSq) {
                closestDistanceSq = distSq;
                closestBoss = boss;
            }
        }

        if (closestBoss != null) {
            if (closestBoss != currentBoss) {
                stopMusic();
                playMusic(closestBoss);
            } else if (currentSound != null && (!mc.getSoundManager().isActive(currentSound) || currentSound.isStopped())) {
                // Retoca a música caso ela tenha parado
                playMusic(closestBoss);
            }
        } else if (currentBoss != null) {
            stopMusic();
        }
    }

    private static void playMusic(IHasBossMusic boss) {
        if (boss == null) {
            return;
        }
        ResourceLocation music = boss.getBossMusic();
        SoundEvent event = ForgeRegistries.SOUND_EVENTS.getValue(music);

        if (event == null) return;

        currentSound = new FadingBossMusicSound(event, boss.getSelf());
        mc.getSoundManager().play(currentSound);
        currentBoss = boss;
    }

    public static void stopMusic() {
        if (currentSound != null) {
            currentSound.startFadeOut();
        }
        currentSound = null;
        currentBoss = null;
    }
}
