package net.foxyas.changedaddon.effect.particles;

import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import org.jetbrains.annotations.NotNull;

public class FallingLeafParticle extends TextureSheetParticle {

    private static final float ACCELERATION_SCALE = 0.0025F;
    private static final int INITIAL_LIFETIME = 300;
    private static final int CURVE_ENDPOINT_TIME = 300;
    private float rotSpeed = (float) Math.toRadians(this.random.nextBoolean() ? -30.0 : 30.0);
    private final float spinAcceleration = (float) Math.toRadians(this.random.nextBoolean() ? -5.0 : 5.0);
    private final float windBig;
    private final boolean swirl;
    private final boolean flowAway;
    private final double xaFlowScale;
    private final double zaFlowScale;
    private final double swirlPeriod;

    protected FallingLeafParticle(
            final ClientLevel level,
            final double x,
            final double y,
            final double z,
            final float fallAcceleration,
            final float sideAcceleration,
            final boolean swirl,
            final boolean flowAway,
            final float scale,
            final float startVelocity
    ) {
        super(level, x, y, z);
        this.windBig = sideAcceleration;
        this.swirl = swirl;
        this.flowAway = flowAway;
        this.lifetime = 300;
        this.gravity = fallAcceleration * 1.2F * 0.0025F;
        float size = scale * (this.random.nextBoolean() ? 0.05F : 0.075F);
        this.quadSize = size;
        this.setSize(size, size);
        this.friction = 1.0F;
        this.yd = -startVelocity;
        float particleRandom = this.random.nextFloat();
        this.xaFlowScale = Math.cos(Math.toRadians(particleRandom * 60.0F)) * this.windBig;
        this.zaFlowScale = Math.sin(Math.toRadians(particleRandom * 60.0F)) * this.windBig;
        this.swirlPeriod = Math.toRadians(1000.0F + particleRandom * 3000.0F);
    }

    protected FallingLeafParticle(
            final ClientLevel level,
            final double x,
            final double y,
            final double z,
            final float fallAcceleration,
            final float sideAcceleration,
            final boolean swirl,
            final boolean flowAway,
            final float scale,
            final float startVelocity,
            final Color3 color
    ) {
        super(level, x, y, z);
        this.windBig = sideAcceleration;
        this.swirl = swirl;
        this.flowAway = flowAway;
        this.lifetime = 300;
        this.gravity = fallAcceleration * 1.2F * 0.0025F;
        float size = scale * (this.random.nextBoolean() ? 0.05F : 0.075F);
        this.quadSize = size;
        this.setSize(size, size);
        this.friction = 1.0F;
        this.yd = -startVelocity;
        float particleRandom = this.random.nextFloat();
        this.xaFlowScale = Math.cos(Math.toRadians(particleRandom * 60.0F)) * this.windBig;
        this.zaFlowScale = Math.sin(Math.toRadians(particleRandom * 60.0F)) * this.windBig;
        this.swirlPeriod = Math.toRadians(1000.0F + particleRandom * 3000.0F);
        this.setColor(color.red(), color.green(), color.blue());
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.lifetime-- <= 0) {
            this.remove();
        }

        if (!this.removed) {
            float aliveTicks = 300 - this.lifetime;
            float relativeAge = Math.min(aliveTicks / 300.0F, 1.0F);
            double xa = 0.0;
            double za = 0.0;
            if (this.flowAway) {
                xa += this.xaFlowScale * Math.pow(relativeAge, 1.25);
                za += this.zaFlowScale * Math.pow(relativeAge, 1.25);
            }

            if (this.swirl) {
                xa += relativeAge * Math.cos(relativeAge * this.swirlPeriod) * this.windBig;
                za += relativeAge * Math.sin(relativeAge * this.swirlPeriod) * this.windBig;
            }

            this.xd += xa * 0.0025F;
            this.zd += za * 0.0025F;
            this.yd = this.yd - this.gravity;
            this.rotSpeed = this.rotSpeed + this.spinAcceleration / 20.0F;
            this.oRoll = this.roll;
            this.roll = this.roll + this.rotSpeed / 20.0F;
            this.move(this.xd, this.yd, this.zd);
            if (this.onGround || this.lifetime < 299 && (this.xd == 0.0 || this.zd == 0.0)) {
                this.remove();
            }

            if (!this.removed) {
                this.xd = this.xd * this.friction;
                this.yd = this.yd * this.friction;
                this.zd = this.zd * this.friction;
            }
        }
    }


    public static class Provider implements ParticleProvider<FallingLeafParticleOptions> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(FallingLeafParticleOptions options, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FallingLeafParticle particle = new FallingLeafParticle(level,
                    x,
                    y,
                    z,
                    options.getFallAcceleration(),
                    options.getSideAcceleration(),
                    options.isSwirl(),
                    options.isFlowAway(),
                    options.getScale(),
                    options.getStartVelocity(),
                    options.getColor()
            );
            particle.pickSprite(this.spriteSet);
            return particle;
        }
    }
}