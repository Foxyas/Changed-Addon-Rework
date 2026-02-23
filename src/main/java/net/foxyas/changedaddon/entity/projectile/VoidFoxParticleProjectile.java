package net.foxyas.changedaddon.entity.projectile;

import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class VoidFoxParticleProjectile extends AbstractVoidFoxParticleProjectile {

    public VoidFoxParticleProjectile(EntityType<? extends AbstractVoidFoxParticleProjectile> type, Level level) {
        super(type, level);
        this.setBaseDamage(1.0f);
        this.setPierceLevel((byte) 0);
        this.setKnockback(0);
        this.setSilent(true);
        this.setNoGravity(true);
    }

    public VoidFoxParticleProjectile(Level level, LivingEntity target) {
        this(ChangedAddonEntities.PARTICLE_PROJECTILE.get(), level);
        this.target = target;
    }

    public VoidFoxParticleProjectile(EntityType<? extends AbstractVoidFoxParticleProjectile> type, Level level, LivingEntity target) {
        this(type, level);
        this.target = target;
    }

    public VoidFoxParticleProjectile(EntityType<? extends AbstractVoidFoxParticleProjectile> type, double x, double y, double z, Level level) {
        super(type, x, y, z, level);
    }

    public VoidFoxParticleProjectile(EntityType<? extends AbstractVoidFoxParticleProjectile> type, double x, double y, double z, Level level, LivingEntity target) {
        this(type, x, y, z, level);
        this.target = target;
    }

    public VoidFoxParticleProjectile(EntityType<? extends AbstractVoidFoxParticleProjectile> type, LivingEntity shooter, Level level) {
        super(type, shooter, level);
    }

    public VoidFoxParticleProjectile(EntityType<? extends AbstractVoidFoxParticleProjectile> type, LivingEntity shooter, Level level, LivingEntity target) {
        this(type, shooter, level);
        this.target = target;
    }


    @Override
    public boolean discardOnNoDmgImpact() {
        return true;
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);
    }

    @Override
    protected boolean ignoreBlock(@NotNull BlockState state) {
        return false;
    }

    @Override
    public boolean isPickable() {
        return super.isPickable();
    }

    @Override
    public boolean isInvulnerableTo(DamageSource pSource) {
        if (pSource.getEntity() instanceof Projectile) {
            return true;
        }
        return super.isInvulnerableTo(pSource);
    }
}
