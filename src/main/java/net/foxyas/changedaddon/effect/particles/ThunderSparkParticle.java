package net.foxyas.changedaddon.effect.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ThunderSparkParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;
    private final int delay;

    public ThunderSparkParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, ThunderSparkOption sparkData, SpriteSet sprites) {
        super(level, x, y, z, dx, dy, dz);
        this.spriteSet = sprites;
        this.age = 0;

        this.setSize(0.3f, 0.3f);

        this.lifetime = Math.max(1, (15 * sparkData.getEnergyCharge()) + (this.random.nextInt(6) - 3));
        /*
         * assert Minecraft.getInstance().player != null;
         * Minecraft.getInstance().player.displayClientMessage(Component.literal("data = " + sparkData.getEnergyCharge()), true);
         */

        this.gravity = 0f;
        this.hasPhysics = true;

        this.xd = dx * 1;
        this.yd = dy * 1;
        this.zd = dz * 1;
        this.delay = sparkData.getEnergyCharge() == 0 ? 2 : 2 * sparkData.getEnergyCharge();

        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        this.x += ((this.random.nextDouble() - 0.5) * 0.02) * this.xd; // Pequena variação em X
        this.y += ((this.random.nextDouble() - 0.5) * 0.02) * this.yd; // Pequena variação em Y
        this.z += ((this.random.nextDouble() - 0.5) * 0.02) * this.zd; // Pequena variação em Z

        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            int currentFrame = (this.age / this.delay);
            this.setSprite(this.spriteSet.get(currentFrame % 9, 9)); // Only use this if your particle JSON strictly has 9 textures

            // Fade out opacity smoothly over lifetime
            this.alpha = 1.0f - ((float) this.age / (float) this.lifetime);
        }
    }

    @Override
    public int getLightColor(float pPartialTick) {
        return 15728880;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<ThunderSparkOption> {
        protected final SpriteSet sprite;

        public Provider(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull ThunderSparkOption type, @NotNull ClientLevel level, double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new ThunderSparkParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type, sprite);
        }
    }

}
