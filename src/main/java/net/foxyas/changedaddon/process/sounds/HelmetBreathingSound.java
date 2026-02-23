package net.foxyas.changedaddon.process.sounds;

import net.foxyas.changedaddon.client.gui.overlays.HazardSuitHelmetOverlay;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class HelmetBreathingSound extends AbstractTickableSoundInstance {
    private final Player player;

    public HelmetBreathingSound(SoundEvent sound, Player player) {
        super(sound, SoundSource.PLAYERS, player.getRandom());
        this.player = player;
        this.looping = true;
        this.delay = 20;
        this.volume = 0.6f;
        this.pitch = 0.25f;
    }

    protected static Vec3 getMouthPosition(Player player) {
        Vec3 view = player.getLookAngle();
        Vec3 eyePosition = player.getEyePosition();
        return eyePosition.subtract(0, 0.25, 0).add(view.scale(0.05f));
        // Just for details.
    }

    public void forceStop() {
        this.stop();
    }

    @Override
    public void tick() {
        if (player == null || !player.isAlive()) {
            this.stop();
            return;
        }

        Vec3 mouthPosition = getMouthPosition(player);
        this.x = mouthPosition.x();
        this.y = mouthPosition.y();
        this.z = mouthPosition.z();

        if (!HazardSuitHelmetOverlay.shouldPlayOverlaySound(player)) {
            this.stop();
        }
    }
}
