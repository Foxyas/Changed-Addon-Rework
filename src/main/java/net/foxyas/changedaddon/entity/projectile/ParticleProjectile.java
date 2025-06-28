package net.foxyas.changedaddon.entity.projectile;

import net.foxyas.changedaddon.registers.ChangedAddonEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class ParticleProjectile extends AbstractGenericParticleProjectile {

    public ParticleProjectile(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
        this.setBaseDamage(1.0f);
        this.setPierceLevel((byte) 0);
        this.setKnockback(0);
        this.setSilent(true);
        this.setNoGravity(true);
    }

    public ParticleProjectile(Level level, LivingEntity target) {
        this(ChangedAddonEntities.PARTICLE_PROJECTILE.get(), level);
        this.target = target;
    }

    public ParticleProjectile(EntityType<? extends AbstractArrow> type, Level level, LivingEntity target) {
        this(type, level);
        this.target = target;
    }

    public ParticleProjectile(EntityType<? extends AbstractArrow> type, double x, double y, double z, Level level) {
        super(type, x, y, z, level);
    }

    public ParticleProjectile(EntityType<? extends AbstractArrow> type, double x, double y, double z, Level level, LivingEntity target) {
        this(type, x, y, z, level);
        this.target = target;
    }

    public ParticleProjectile(EntityType<? extends AbstractArrow> type, LivingEntity shooter, Level level) {
        super(type, shooter, level);
    }

    public ParticleProjectile(EntityType<? extends AbstractArrow> type, LivingEntity shooter, Level level, LivingEntity target) {
        this(type, shooter, level);
        this.target = target;
    }

    public static void init() {
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);
    }
}
