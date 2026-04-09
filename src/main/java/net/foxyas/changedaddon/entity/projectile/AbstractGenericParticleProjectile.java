package net.foxyas.changedaddon.entity.projectile;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractGenericParticleProjectile extends ParriableProjectile {

    protected ParticleOptions particleOptions;

    public AbstractGenericParticleProjectile(EntityType<? extends AbstractGenericParticleProjectile> type, Level level) {
        super(type, level);
    }

    public AbstractGenericParticleProjectile(EntityType<? extends AbstractGenericParticleProjectile> type, Level level, LivingEntity shooter, ParticleOptions particle) {
        super(type, shooter, level);
        this.particleOptions = particle;
    }

    @Override
    public boolean discardOnNoDmgImpact() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (!level.isClientSide && particleOptions != null) {
            spawnParticle();
        }
    }

    protected void spawnParticle() {
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(particleOptions, this.getX(), this.getY() + 0.1D, this.getZ(), 0, 0, 0, 0, 1);
        }
    }

    @Override
    protected boolean updateInWaterStateAndDoFluidPushing() {
        return false;
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    @Override
    public boolean hurt(@NotNull DamageSource pSource, float pAmount) {
        if (this.isInvulnerableTo(pSource)) {
            return false;
        } else {
            this.markHurt();
            Entity entity = pSource.getEntity();
            if (entity != null) {
                if (!this.level.isClientSide) {
                    Vec3 vec3 = entity.getLookAngle();
                    this.setDeltaMovement(vec3);
                    this.setOwner(entity);
                }

                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean isAttackable() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return true;
    }
}
