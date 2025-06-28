package net.foxyas.changedaddon.entity.goals;

import net.foxyas.changedaddon.entity.projectile.ParticleProjectile;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class ProjectileAttackGoal extends Goal {

    private final Mob mob;
    private final EntityType<? extends ParticleProjectile> projectileType;

    private final int attackCooldownMax = 60;  // Ticks entre ataques (~3 segundos)
    private final int prepareDelayMax = 20;    // Tempo de "preparo" antes de atacar (~1 segundo)

    private int attackCooldown = 0;
    private int prepareDelay = 0;

    public ProjectileAttackGoal(Mob mob, EntityType<? extends ParticleProjectile> projectileType) {
        this.mob = mob;
        this.projectileType = projectileType;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = mob.getTarget();
        return target != null && target.isAlive() && target.distanceTo(mob) >= 2.5f;
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void start() {
        prepareDelay = prepareDelayMax;
    }

    @Override
    public void stop() {
        prepareDelay = 0;
    }

    @Override
    public void tick() {
        LivingEntity target = mob.getTarget();
        if (target == null) return;

        mob.getLookControl().setLookAt(target, 30.0F, 30.0F);

        if (attackCooldown > 0) {
            attackCooldown--;
            if (mob.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.ENCHANT, mob.getX(), mob.getEyeY(), mob.getZ(), 4, 0.25, 0.5, 0.25, 0.5f);
                serverLevel.sendParticles(ParticleTypes.END_ROD, mob.getX(), mob.getEyeY(), mob.getZ(), 4, 0.25, 0.5, 0.25, 0.05f);
            }
            return;
        }

        if (prepareDelay > 0) {
            prepareDelay--;
            return;
        }

        // Disparar projétil
        shootProjectile(target);
        attackCooldown = attackCooldownMax;
        prepareDelay = prepareDelayMax;
    }

    private void shootProjectile(LivingEntity target) {
        Level level = mob.level;
        if (!level.isClientSide) {
            spawnProjectilesInCircle(mob, target, new Vec3(0, 0.5, 0), 2, 8);
        }
    }

    public void spawnProjectilesInCircle(LivingEntity entity, LivingEntity target, Vec3 offset, double radius, int count) {
        Vec3 center = entity.getEyePosition().add(offset); // centro do círculo com offset

        for (int i = 0; i < count; i++) {
            double angle = 2 * Math.PI * i / count;
            Vec3 dir = new Vec3(1, 0, 0).zRot((float) angle)
                    .yRot((float) Math.toRadians(-target.getYRot()))
                    .normalize();
            Vec3 spawnPos = center.add(dir.scale(radius));

            // Spawn do projétil

            ParticleProjectile projectile = new ParticleProjectile(projectileType, mob, mob.level(), target);
            projectile.setPos(spawnPos);
            projectile.setNoGravity(true);
            projectile.setOwner(mob);

            Vec3 direction = target.getEyePosition(1.0F).subtract(mob.getEyePosition(0.5f)).normalize();
            projectile.shoot(direction.x, direction.y, direction.z, 1.5f, 0.0f);
            mob.swing(InteractionHand.MAIN_HAND);

            entity.level.addFreshEntity(projectile);
        }
    }


}
