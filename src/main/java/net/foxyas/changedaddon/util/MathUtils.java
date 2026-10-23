package net.foxyas.changedaddon.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class MathUtils {

    /**
     * Calculates the progress ratio between a min and max value.
     * Formula: (value - min) / (max - min)
     */
    public static float calculateProgress(float value, float min, float max) {
        if (Float.compare(max, min) == 0) {
            return 0.0f; // Prevent division by zero
        }

        float progress = (value - min) / (max - min);

        // Clamps between 0.0 and 1.0 to stay within bounds
        return Math.max(0.0f, Math.min(1.0f, progress));
    }

    /**
     * Calculates the dot product between the entity's look direction
     * and the direction from the entity's position to a target position.
     *
     * @param entity   The entity whose look direction will be used.
     * @param position The world position to compare against.
     * @return Dot product value:
     * > 0 if the position is in front of the entity,
     * < 0 if behind,
     * 0 if exactly to the side.
     */
    public static double dotEntityLookingToPos(Entity entity, Vec3 position) {
        // Entity's look direction (normalized)
        Vec3 lookDir = entity.getLookAngle().normalize();

        // Direction vector from entity's position to the target position (normalized)
        Vec3 toTarget = position.subtract(entity.position()).normalize();

        // Dot product between the two direction vectors
        return lookDir.dot(toTarget);
    }

    /**
     * Calculates the dot product between the entity's look direction
     * and the direction from the entity's position to a target position.
     *
     * @param entity   The entity whose look direction will be used.
     * @param position The world position to compare against.
     * @return Dot product value:
     * > 0 if the position is in front of the entity,
     * < 0 if behind,
     * 0 if exactly to the side.
     */
    public static double dotEntityLookingToPos(LivingEntity entity, Vec3 position) {
        // Entity's look direction (normalized)
        Vec3 lookDir = entity.getLookAngle().normalize();

        // Direction vector from entity's position to the target position (normalized)
        Vec3 toTarget = position.subtract(entity.position()).normalize();

        // Dot product between the two direction vectors
        return lookDir.dot(toTarget);
    }

    /**
     * Calculates the dot product between the entity's look direction
     * and the direction from the entity's position to a target position.
     *
     * @param entity   The entity whose look direction will be used.
     * @param position The world position to compare against.
     * @return Dot product value:
     * > 0 if the position is in front of the entity,
     * < 0 if behind,
     * 0 if exactly to the side.
     */
    public static double dotEntityLookingToEntity(LivingEntity entity, LivingEntity position) {
        // Entity's look direction (normalized)
        Vec3 lookDir = entity.getLookAngle().normalize();

        // Direction vector from entity's position to the target position (normalized)
        Vec3 toTarget = position.position().subtract(entity.position()).normalize();

        // Dot product between the two direction vectors
        return lookDir.dot(toTarget);
    }
}
