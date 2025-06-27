package net.foxyas.changedaddon.effect.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;


import javax.annotation.Nullable;

public class ThunderSparkParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;
    private final int delay;

    public ThunderSparkParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, ThunderSparkOption sparkData, SpriteSet sprites) {
        super(level, x, y, z, dx, dy, dz);
        this.spriteSet = sprites;

        this.setSize(0.3f, 0.3f);

        this.lifetime = (int) Math.max(1, (15 * sparkData.getEnergyCharge()) + (this.random.nextInt(6) - 3));
        /*
         * assert Minecraft.getInstance().player != null;
         * Minecraft.getInstance().player.displayClientMessage(new TextComponent("data = " + sparkData.getEnergyCharge()), true);
         */

        this.gravity = 0f;
        this.hasPhysics = true;

        this.xd = dx * 1;
        this.yd = dy * 1;
        this.zd = dz * 1;
        this.delay = 2 * sparkData.getEnergyCharge();

        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        // Adiciona um pequeno valor aleatório para gerar movimento "levemente aleatório"
        //this.xd += (this.random.nextDouble() - 0.5) * 0.02; // Pequena variação em X
        //this.yd += (this.random.nextDouble() - 0.5) * 0.02; // Pequena variação em Y
        //this.zd += (this.random.nextDouble() - 0.5) * 0.02; // Pequena variação em Z
        this.x += ((this.random.nextDouble() - 0.5) * 0.02) * this.xd; // Pequena variação em X
        this.y += ((this.random.nextDouble() - 0.5) * 0.02) * this.yd; // Pequena variação em Y
        this.z += ((this.random.nextDouble() - 0.5) * 0.02) * this.zd; // Pequena variação em Z

        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            int delay = this.delay; // Define um delay de 3 ticks antes de mudar o sprite
            int spriteIndex = (this.age / delay) % 9; // Alterna baseado no delay
            this.setSprite(this.spriteSet.get(spriteIndex, 9));
        }
    }


    @Override
    public int getLightColor(float p_105562_) {
        return 15728880;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
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
