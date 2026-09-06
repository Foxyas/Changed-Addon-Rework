package net.foxyas.changedaddon.ability.handle.dodgeTypes;

import com.mojang.datafixers.util.Either;
import net.foxyas.changedaddon.ability.DodgeAbilityInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;

public class TeleportDodgeType extends DodgeType {

    public static final TeleportDodgeType INSTANCE = new TeleportDodgeType();

    public TeleportDodgeType() {
        super();
    }

    @Override
    public void applyDodgeMovement(DodgeAbilityInstance dodgeAbilityInstance, LivingEntity dodger, Either<DamageSource, Projectile> sourceProjectileEither, boolean causeExhaustion) {
        super.applyDodgeMovement(dodgeAbilityInstance, dodger, sourceProjectileEither, causeExhaustion);
        Level level = dodger.level();

        // Map both DamageSource and Projectile to an Entity (or null if no direct entity)
        Entity sourceEntity = sourceProjectileEither.map(
                damageSource -> damageSource.getDirectEntity() != null ? damageSource.getDirectEntity() : damageSource.getEntity(),
                projectile -> projectile
        );

        // If there is no entity to dodge away from, default to random teleport or exit
        if (sourceEntity == null) {
            randomOffsetTeleport(level, dodger);
            return;
        }

        boolean isFar = dodger.distanceTo(sourceEntity) > 2.0f;

        if (isFar) {
            randomOffsetTeleport(level, dodger);
        } else {
            // Calculate position behind the attacker/projectile
            Vec3 attackerPos = sourceEntity.position();
            Vec3 lookDirection = sourceEntity.getLookAngle();
            double distanceBehind = 1.5; // Adjust distance behind the entity as needed

            Vec3 dodgePosBehind = attackerPos.subtract(lookDirection.scale(distanceBehind));

            if (dodger.randomTeleport(dodgePosBehind.x, dodgePosBehind.y, dodgePosBehind.z, true)) {
                level.playSound(null, dodger.blockPosition(),
                        SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.PORTAL,
                            dodger.getX(), dodger.getY() + 0.5, dodger.getZ(),
                            20, 0.5, 1.0, 0.5, 0.1);
                }
            } else {
                randomOffsetTeleport(level, dodger);
            }
        }
    }

    private void randomOffsetTeleport(LevelAccessor levelAccessor, LivingEntity dodger) {
        // Random offset values
        double maxDistance = 16.0; // maximum distance for teleport
        double dx = (dodger.getRandom().nextDouble() - 0.5) * 2 * maxDistance;
        double dz = (dodger.getRandom().nextDouble() - 0.5) * 2 * maxDistance;
        double dy = (dodger.getRandom().nextInt(16) - 8); // vertical offset -8 to +7

        // Calculate target position
        BlockPos targetPos = new BlockPos((int) (dodger.getX() + dx), (int) (dodger.getY() + dy), (int) (dodger.getZ() + dz));
        if (dodger.randomTeleport(targetPos.getX(), targetPos.getY(), targetPos.getZ(), true)) {
            // Optional: play sound & particles like Enderman
            levelAccessor.playSound(null, dodger.blockPosition(),
                    SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
            if (levelAccessor instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.PORTAL,
                        dodger.getX(), dodger.getY() + 0.5, dodger.getZ(),
                        20, 0.5, 1.0, 0.5, 0.1);
            }
        }
    }
}
