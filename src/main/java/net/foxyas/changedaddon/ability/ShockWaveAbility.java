package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.effect.particles.ThunderParticleOptions;
import net.foxyas.changedaddon.init.ChangedAddonParticleTypes;
import net.foxyas.changedaddon.init.ChangedAddonTransfurVariants;
import net.foxyas.changedaddon.util.FoxyasUtil;
import net.foxyas.changedaddon.util.ParticlesUtil;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.ltxprogrammer.changed.entity.animation.StunAnimationParameters;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedAnimationEvents;
import net.ltxprogrammer.changed.init.ChangedEffects;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ShockWaveAbility extends SimpleAbility {

    public ShockWaveAbility() {
        super();
    }

    @Override
    public Component getAbilityName(IAbstractChangedEntity entity) {
        return Component.translatable("ability.changed_addon.shock_wave");
    }

    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        return entity.getFoodLevel() >= 10 && !entity.getEntity().isSpectator();
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        TransfurVariant<?> Variant = entity.getChangedEntity().getSelfVariant();
        if (Variant == ChangedAddonTransfurVariants.EXPERIMENT_009_BOSS.get()) {
            return 60;
        }
        return 100;
    }

    @Override
    public int getChargeTime(IAbstractChangedEntity entity) {
        TransfurVariant<?> Variant = entity.getChangedEntity().getSelfVariant();
        if (Variant == ChangedAddonTransfurVariants.EXPERIMENT_009_BOSS.get()) {
            return 16;
        }
        return 30;
    }

    public UseType getUseType(IAbstractChangedEntity entity) {
        return UseType.CHARGE_TIME;
    }

    @Override
    public void startUsing(IAbstractChangedEntity entity) {
        super.startUsing(entity);
        runAbility(entity.getEntity());
    }

    @Override
    public void tick(IAbstractChangedEntity entity) {
        super.tick(entity);
    }

    public void runAbility(LivingEntity user) {
        Level level = user.level();
        if (level.isClientSide()) return;

        MobEffectInstance pEffectInstance = new MobEffectInstance(ChangedEffects.SHOCK.get(), 60, 0, true, false, true);

        // Using Set instead of List: contains() is O(1)
        Set<BlockPos> connectedFluids = FoxyasUtil.getConnectedFluids(level, user.blockPosition(), 128)
                .stream().map(BlockPos::immutable).collect(Collectors.toSet());

        float maxRadius = 16.0f;
        List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, user.getBoundingBox().inflate(maxRadius), EntitySelector.NO_CREATIVE_OR_SPECTATOR.and((entity) -> {
            if (entity.is(user)) return false;
            if (!(entity instanceof LivingEntity livingEntity)) return false;
            return livingEntity.canBeAffected(pEffectInstance);
        }));

        // Sphere center: middle of the user's body
        Vec3 center = user.position().add(0, user.getBbHeight() / 2.0, 0);
        float step = 1.0f;

        for (float currentRadius = 1.0f; currentRadius <= maxRadius; currentRadius += step) {

            // 1. Spherical particle shell at the current radius
            if (currentRadius % 10 == 0) spawnLightingSphere(level, center, maxRadius);

            // 2. Entities caught by the current shell
            for (LivingEntity nearbyEntity : nearbyEntities) {
                // Use the center of the entity's body for 3D checks
                Vec3 entityCenter = nearbyEntity.position().add(0, nearbyEntity.getBbHeight() / 2.0, 0);

                // Is it inside the current wave "slice"? (inside current sphere, but outside the previous one)
                if (!isInsideSphere(center, entityCenter, currentRadius)) continue;
                if (isInsideSphere(center, entityCenter, currentRadius - step)) continue;

                boolean inConnectedFluid = connectedFluids.contains(nearbyEntity.blockPosition());

                // Outside connected fluids: only needs to be inside the sphere (already ensured above).
                // Inside connected fluids: also affected (water conducts the shock).
                if (!inConnectedFluid && !isInsideSphere(center, entityCenter, currentRadius)) continue;

                if (nearbyEntity.addEffect(pEffectInstance, user)) {
                    ChangedAnimationEvents.broadcastEntityAnimation(nearbyEntity, ChangedAnimationEvents.SHOCK_STUN.get(), StunAnimationParameters.INSTANCE);
                    level.playSound(null, nearbyEntity, ChangedSounds.TSC_WEAPON_SHOCK.get(), SoundSource.HOSTILE, 1, 1);
                }
            }
        }
    }

    /**
     * Simple check: is the point inside the sphere defined by `center` and `radius`?
     */
    private static boolean isInsideSphere(Vec3 center, Vec3 point, float radius) {
        return center.distanceToSqr(point) <= radius * radius;
    }

    /**
     * Spawns a spherical shell of particles using Fibonacci lattice distribution (uniform points).
     */
    private static void spawnSphereShell(Level level, Vec3 center, float radius) {
        int latSteps = 6;
//                Math.max(6, (int) (radius * 1.5f)); // more rings for bigger spheres

        for (int i = 0; i <= latSteps; i++) {
            float phi = Mth.PI * i / latSteps;             // 0 -> PI (includes both poles)
            float sinPhi = Mth.sin(phi);
            float cosPhi = Mth.cos(phi);

            // fewer points on rings near the poles, so they don't pile up
            int lonSteps = Math.max(1, (int) (latSteps * 2 * sinPhi));

            for (int j = 0; j < lonSteps; j++) {
                float theta = Mth.TWO_PI * j / lonSteps;

                Vec3 dir = new Vec3(sinPhi * Mth.cos(theta), cosPhi, sinPhi * Mth.sin(theta)); // unit vector
                Vec3 pos = center.add(dir.scale(radius));

                ParticlesUtil.sendParticlesWithMotion(
                        level,
                        ParticleTypes.ELECTRIC_SPARK,
                        pos,
                        Vec3.ZERO,
                        dir.scale(0.05), // outward; use dir.scale(-0.05) for inward
                        1,
                        0.75f
                );
            }
        }

//        // Quantity proportional to the sphere's surface area (4πr²), with a minimum cap
//        int points = (int) Math.max(12, 4.0 * Math.PI * radius * radius * 0.12);
//        double goldenAngle = Math.PI * (3.0 - Math.sqrt(5.0));
//
//        for (int i = 0; i < points; i++) {
//            double y = 1.0 - (2.0 * (i + 0.5) / points); // from 1 to -1
//            double ringRadius = Math.sqrt(1.0 - y * y);
//            double theta = goldenAngle * i;
//
//            Vec3 dir = new Vec3(Math.cos(theta) * ringRadius, y, Math.sin(theta) * ringRadius);
//            Vec3 pos = center.add(dir.scale(radius));
//
//            ParticlesUtil.sendParticlesWithMotion(level,
//                    ParticleTypes.ELECTRIC_SPARK,
//                    pos,
//                    Vec3.ZERO,
//                    dir.scale(0.05), // slight outward motion following the expansion
//                    1,
//                    0.1f);
//        }
    }

    /**
     * Spawns a spherical lighting
     */
    private static void spawnLightingSphere(Level level, Vec3 center, float maxRadius) {
        int latSteps = 6;
//                Math.max(6, (int) (radius * 1.5f)); // more rings for bigger spheres

        for (int i = 0; i <= latSteps; i++) {
            float phi = Mth.PI * i / latSteps;             // 0 -> PI (includes both poles)
            float sinPhi = Mth.sin(phi);
            float cosPhi = Mth.cos(phi);

            // fewer points on rings near the poles, so they don't pile up
            int lonSteps = Math.max(1, (int) (latSteps * 2 * sinPhi));

            for (int j = 0; j < lonSteps; j++) {
                float theta = Mth.TWO_PI * j / lonSteps;

                Vec3 dir = new Vec3(sinPhi * Mth.cos(theta), cosPhi, sinPhi * Mth.sin(theta)); // unit vector
                Vec3 pos = center.add(dir.scale(maxRadius));

                ThunderParticleOptions bolt = ChangedAddonParticleTypes.thunderBolt(
                        20f,                           // speed
                        true,                           // rooted: the origin stays at the user
                        true,                           // shakyBody
                        2,                              // bodyShakeFrequency (ticks)
                        new Vector3f(0.8f, 0.8f, 0.8f), // shake
                        new Vector3f(0.45f, 0.85f, 1f), // color (light blue)
                        12,                             // lifetime (ticks)
                        0.08f                           // size
                );

                ParticlesUtil.sendParticlesWithMotion(
                        level,
                        bolt,
                        center,
                        Vec3.ZERO,
                        pos,
                        1,
                        1f
                );
            }
        }
    }
}