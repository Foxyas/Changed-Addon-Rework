package net.foxyas.changedaddon.effect.particles;

import net.ltxprogrammer.changed.effect.particle.ColoredParticleOption;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import org.jetbrains.annotations.NotNull;

public class LeafParticle extends TextureSheetParticle {

    private final float rotationSpeed;
    private final float zigzagSpeed;
    private final float zigzagScale;

    protected LeafParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Color3 color) {
        super(level, x, y, z);
        
        this.xd = xSpeed;
        this.yd = ySpeed - 0.05D; // Apply a slight initial downward velocity
        this.zd = zSpeed;
        
        // Random lifespan between 60 and 100 ticks
        this.lifetime = 60 + this.random.nextInt(40);
        
        this.quadSize = 0.1F * (this.random.nextFloat() * 0.5F + 0.8F);
        this.gravity = 0.02F; 
        this.hasPhysics = true; // Respect block collisions

        // Randomize coefficients so leaves don't sync up perfectly
        this.rotationSpeed = (this.random.nextFloat() - 0.5F) * 0.1F;
        this.zigzagSpeed = 0.05F + this.random.nextFloat() * 0.05F;
        this.zigzagScale = 0.02F + this.random.nextFloat() * 0.03F;
        
        this.roll = this.random.nextFloat() * ((float)Math.PI * 2F);

        this.setColor(color.red(), color.green(), color.blue());
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        // Use CUTOUT if your leaf texture features transparency/alpha mask holes
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime || this.onGround) {
            this.remove();
            return;
        }

        this.yd -= this.gravity;

        // --- Zigzag Oscillation Logic ---
        // Sine/Cosine functions map age to smooth coordinate offsets over time
        double movementOffset = Math.sin((double)this.age * this.zigzagSpeed) * this.zigzagScale;
        this.xd += movementOffset;
        this.zd += Math.cos((double)this.age * this.zigzagSpeed) * this.zigzagScale;

        // Limit terminal velocity to simulate wind resistance/gliding
        this.yd = Math.max(this.yd, -0.06D);

        this.move(this.xd, this.yd, this.zd);

        // Standard air friction decay
        this.xd *= 0.98D;
        this.yd *= 0.98D;
        this.zd *= 0.98D;

        // Apply a gentle spin while falling
        this.oRoll = this.roll;
        this.roll += this.rotationSpeed;
    }

    public static class Provider implements ParticleProvider<ColoredParticleOption> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(ColoredParticleOption type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            LeafParticle particle = new LeafParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.getColor());
            particle.pickSprite(this.spriteSet);
            return particle;
        }
    }
}