package net.foxyas.changedaddon.effect.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleOptions;

import javax.annotation.Nullable;

public class ThunderSparkParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    public ThunderSparkParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, ThunderSparkOption sparkData, SpriteSet sprites) {
        super(level, x, y, z, dx, dy, dz);
        this.sprites = sprites;
        this.setSprite(sprites.get(sparkData.getVariant(), 3)); // 3 é o número total de variações
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<ThunderSparkOption> {
        protected final SpriteSet sprite;

        public Provider(SpriteSet p_106394_) {
            this.sprite = p_106394_;
        }

        @Nullable
        @Override
        public Particle createParticle(ThunderSparkOption type, ClientLevel level, double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new ThunderSparkParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type, sprite);
        }
    }

}
