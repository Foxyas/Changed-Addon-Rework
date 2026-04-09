package net.foxyas.changedaddon.entity.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WitherParticleProjectile extends AbstractGenericParticleProjectile {

    public WitherParticleProjectile(EntityType<? extends AbstractGenericParticleProjectile> type, Level level) {
        super(type, level);
        this.particleOptions = ParticleTypes.DAMAGE_INDICATOR;
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void spawnParticle() {
        super.spawnParticle();
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult pResult) {
        ApplyExplosionParticlesAndDamage();
        super.onHitBlock(pResult);
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult pResult) {
        if (pResult.getEntity().hurtMarked) {
            ApplyExplosionParticlesAndDamage();
        }
        super.onHitEntity(pResult);
    }

    @Override
    protected boolean ignoreBlock(@NotNull BlockState state) {
        return false;
    }

    public void ApplyExplosionParticlesAndDamage() {
        if (!(this.level() instanceof ServerLevel serverLevel))
            return;

        double radius = 1.0f;
        double maxDistance = 6;
        double angleX = 15f;
        double angleY = 15f;

        for (double theta = 0; theta < 360; theta += angleX) {
            double angleTheta = Math.toRadians(theta);
            for (double phi = 0; phi <= 180; phi += angleY) {
                double anglePhi = Math.toRadians(phi);

                double dx = Math.sin(anglePhi) * Math.cos(angleTheta) * radius;
                double dy = Math.cos(anglePhi) * radius;
                double dz = Math.sin(anglePhi) * Math.sin(angleTheta) * radius;
                Vec3 dir = new Vec3(dx, dy, dz);

                Vec3 start = this.position();
                Vec3 end = start.add(dir.scale(maxDistance));
                Vec3 motion = end.subtract(start).normalize();


                ClipContext ctx = new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this);
                BlockHitResult blockHit = serverLevel.clip(ctx);

                Vec3 finalEnd = blockHit.getType() != HitResult.Type.MISS ? blockHit.getLocation() : end;

                serverLevel.sendParticles(ParticleTypes.FLAME, start.x, start.y, start.z, 0, motion.x, motion.y, motion.z, 1);
                serverLevel.sendParticles(ParticleTypes.DAMAGE_INDICATOR, start.x, start.y, start.z, 0, motion.x, motion.y, motion.z, 2);


                AABB aabb = new AABB(start, finalEnd).inflate(0.5);
                List<LivingEntity> entities = serverLevel.getEntitiesOfClass(LivingEntity.class, aabb, e -> !e.is(this));

                for (LivingEntity target : entities) {
                    target.hurt(target.level().damageSources().magic(), 8.0f);
                    target.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 2)); // 5s de wither II
                }
            }
        }
        serverLevel.playSound(null, this, SoundEvents.GENERIC_EXPLODE, SoundSource.AMBIENT, 1, 1);
    }

}
