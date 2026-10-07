package net.foxyas.changedaddon.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AngleUtils {

    /**
     * Calculates the dot product between an entity's look vector and the direction towards a target position.
     *
     * @param entity The entity looking at the target.
     * @param target The world position to check alignment against.
     * @return The dot product (-1.0 to 1.0): 1.0 means looking directly at the target, 0.0 is perpendicular (90°), and -1.0 is facing directly away. Returns 0.0 if parameters are null.
     */
    public static double getLookAlignment(@Nullable Entity entity, @Nullable Vec3 target) {
        if (entity == null || target == null) {
            return 0.0;
        }

        // Normalized view vector of the entity
        Vec3 lookView = entity.getLookAngle();

        // Direction vector from the entity's eyes to the target position (normalized)
        Vec3 toTarget = target.subtract(entity.getEyePosition()).normalize();

        return lookView.dot(toTarget);
    }

    /**
     * Helper method to extract stored target coordinates from an ItemStack's NBT tag and calculate look alignment.
     *
     * @param entity    The entity looking at the target position.
     * @param itemStack The ItemStack containing saved "x", "y", "z" NBT coordinates.
     * @return The dot product (-1.0 to 1.0), or 0.0 if the item, tag, or entity is invalid.
     */
    public static double getLookAlignmentToItem(@Nullable Entity entity, @NotNull ItemStack itemStack) {
        if (entity == null || itemStack.isEmpty() || !itemStack.hasTag()) {
            return 0.0;
        }

        CompoundTag tag = itemStack.getTag();
        if (tag == null || !tag.contains("x") || !tag.contains("y") || !tag.contains("z")) {
            return 0.0;
        }

        Vec3 targetPos = new Vec3(
                tag.getDouble("x"),
                tag.getDouble("y"),
                tag.getDouble("z")
        );

        return getLookAlignment(entity, targetPos);
    }
}