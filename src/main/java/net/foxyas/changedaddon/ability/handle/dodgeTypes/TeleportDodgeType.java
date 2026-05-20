package net.foxyas.changedaddon.ability.handle.dodgeTypes;

import net.foxyas.changedaddon.ability.DodgeAbilityInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class TeleportDodgeType extends DodgeType {

    public static final TeleportDodgeType INSTANCE = new TeleportDodgeType();

    public TeleportDodgeType() {
        super();
    }

    @Override
    public void runDodge(DodgeAbilityInstance dodgeAbilityInstance, LevelAccessor levelAccessor, LivingEntity dodger, Entity attacker, LivingAttackEvent event, double distance, Vec3 dodgePosBehind, boolean causeExhaustion) {
        super.runDodge(dodgeAbilityInstance, levelAccessor, dodger, attacker, event, distance, dodgePosBehind, causeExhaustion);

        if (distance > 2f) {
            randomOffsetTeleport(levelAccessor, dodger);
        } else {
            if (dodger.randomTeleport(dodgePosBehind.x, dodgePosBehind.y, dodgePosBehind.z, true)) {
                // Optional: play sound & particles like Enderman
                levelAccessor.playSound(null, dodger.blockPosition(),
                        SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                if (levelAccessor instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.PORTAL,
                            dodger.getX(), dodger.getY() + 0.5, dodger.getZ(),
                            20, 0.5, 1.0, 0.5, 0.1);
                }
            } else {
                randomOffsetTeleport(levelAccessor, dodger);
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
