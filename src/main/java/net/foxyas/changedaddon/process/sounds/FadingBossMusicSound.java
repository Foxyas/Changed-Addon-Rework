package net.foxyas.changedaddon.process.sounds;

import net.foxyas.changedaddon.configuration.ChangedAddonClientConfiguration;
import net.foxyas.changedaddon.entity.api.IHasBossMusic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class FadingBossMusicSound extends AbstractTickableSoundInstance {
    public final SoundEvent currentSound;
    private final LivingEntity trackedEntity;
    protected boolean fadingOutSound = false;
    private boolean stopped = false;

    public FadingBossMusicSound(SoundEvent soundEvent, LivingEntity entity) {
        super(soundEvent, SoundSource.MASTER, entity.getRandom());
        this.currentSound = soundEvent;
        this.trackedEntity = entity;
        this.looping = true;
        this.volume = 1.0f;
        this.pitch = 1.0f;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void startFadeOut() {
        this.fadingOutSound = true;
    }

    public void stopFadeOut() {
        this.fadingOutSound = false;
    }

    public boolean isStopped() {
        return stopped;
    }

    public LivingEntity getTrackedEntity() {
        return trackedEntity;
    }

    @Override
    public boolean isRelative() {
        return super.isRelative();
    }

    @Override
    public boolean isLooping() {
        return super.isLooping();
    }

    @Override
    public void tick() {
        if (isStopped()) {
            this.stop();
        }

        if (fadingOutSound) {
            this.volume -= 0.025f;
        } else {
            if (trackedEntity instanceof IHasBossMusic iHasBossMusic) {
                this.volume = Math.min(iHasBossMusic.getMusicVolume(), volume + 0.025f);
            } else {
                this.volume = Math.min(1, volume + 0.025f);
            }
        }

        if (trackedEntity == null) {
            startFadeOut();
            this.looping = false;
            this.stopped = true;
            this.stop();
        }

        if (trackedEntity instanceof IHasBossMusic iHasBossMusic) {
            this.pitch = iHasBossMusic.getMusicPitch();
        }

        if (trackedEntity != null && trackedEntity.isAlive()) {
            if (ChangedAddonClientConfiguration.BOSS_MUSIC_LOCATION_TYPE.get() == BossMusicHandler.FollowType.PLAYER_CAMERA) {
                Minecraft minecraft = Minecraft.getInstance();
                Entity cameraEntity = minecraft.cameraEntity;
                if (cameraEntity != null) {
                    this.x = cameraEntity.getX();
                    this.y = cameraEntity.getY();
                    this.z = cameraEntity.getZ();
                    return;
                }
            }

            this.x = trackedEntity.getX();
            this.y = trackedEntity.getY();
            this.z = trackedEntity.getZ();
        } else if (trackedEntity != null && trackedEntity.isDeadOrDying()) {
            startFadeOut();
            this.looping = false;
            this.stopped = true;
            this.stop();
        }
    }

    @Override
    public @NotNull WeighedSoundEvents resolve(@NotNull SoundManager p_119591_) {
        return super.resolve(p_119591_);
    }
}