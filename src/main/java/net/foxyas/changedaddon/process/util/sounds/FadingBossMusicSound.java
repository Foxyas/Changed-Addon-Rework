package net.foxyas.changedaddon.process.util.sounds;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.entity.CustomHandle.IHasBossMusic;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class FadingBossMusicSound extends AbstractTickableSoundInstance {
    private static final float MAX_VOLUME = 1.0f;
    private static final int DEFAULT_FADE_TICKS = 40;
    private final LivingEntity trackedEntity;
    private int fadeInTicks;
    private int fadeOutTicks = -1;
    private boolean stopped = false;
    public SoundEvent currentSound = null;

    public FadingBossMusicSound(SoundEvent soundEvent, LivingEntity entity) {
        super(soundEvent, SoundSource.MASTER);
        this.currentSound = soundEvent;
        this.trackedEntity = entity;
        this.looping = true;
        this.volume = 0.0f;
        this.pitch = 1.0f;
        this.fadeInTicks = DEFAULT_FADE_TICKS;
    }

    public void startFadeOut() {
        this.fadeOutTicks = DEFAULT_FADE_TICKS;
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
        if (trackedEntity == null) {
            startFadeOut();
            this.looping = false;
            this.stopped = true;
            this.stop();
        }

        if (trackedEntity != null && trackedEntity.isAlive()) {
            this.x = trackedEntity.getX();
            this.y = trackedEntity.getY();
            this.z = trackedEntity.getZ();
        } else if (trackedEntity != null && trackedEntity.isDeadOrDying()) {
            startFadeOut();
            this.looping = false;
            this.stopped = true;
            this.stop();
        }

        if (fadeOutTicks >= 0) {
            fadeOutTicks--;
            volume = MAX_VOLUME * (fadeOutTicks / (float) DEFAULT_FADE_TICKS);
            if (fadeOutTicks <= 0) {
                stopped = true;
                this.stop();
            }
        } else if (fadeInTicks > 0) {
            fadeInTicks--;
            volume = MAX_VOLUME * (1.0f - fadeInTicks / (float) DEFAULT_FADE_TICKS);
        } else {
            volume = MAX_VOLUME;
        }
    }

    @Override
    public @NotNull WeighedSoundEvents resolve(SoundManager p_119591_) {
        return super.resolve(p_119591_);
    }
}
