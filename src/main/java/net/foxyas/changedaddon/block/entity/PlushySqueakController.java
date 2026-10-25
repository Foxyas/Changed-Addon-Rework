package net.foxyas.changedaddon.block.entity;

import net.minecraft.util.Mth;

public class PlushySqueakController {
    private boolean shouldSqueak;
    private float squeezeProgress;
    private float oSqueezeProgress; // For lerping
    private float squeezeSpeed = 0.2F; // Default speed increment/decrement

    public void tickController() {
        this.oSqueezeProgress = this.squeezeProgress;

        // Use dynamic squeezeSpeed instead of hardcoded 0.2F
        if (this.shouldSqueak && this.squeezeProgress < 1.0F) {
            this.squeezeProgress = Math.min(this.squeezeProgress + this.squeezeSpeed, 1.0F);
        } else if (!this.shouldSqueak && this.squeezeProgress > 0.0F) {
            this.squeezeProgress = Math.max(this.squeezeProgress - this.squeezeSpeed, 0.0F);
        } else if (this.squeezeProgress >= 1.0F) {
            // Automatically flip back once peak squeeze is reached
            this.shouldSqueak = false;
        }
    }

    public float getSqueezeProgress(float partialTicks) {
        return Mth.lerp(partialTicks, this.oSqueezeProgress, this.squeezeProgress);
    }

    public void triggerSqueak() {
        this.triggerSqueak(0.2F);
    }

    public void triggerSqueak(float speed) {
        this.squeezeSpeed = speed;
        this.shouldSqueak = true;
        this.squeezeProgress = 0.0F;
    }
}