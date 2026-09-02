package net.foxyas.changedaddon.util;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Utility class to generate relative 3D vectors based on angle ranges,
 * spin intensities, and radius float providers.
 */
public final class AngleGetter {

    private AngleGetter() {
        // Utility class - static methods only
    }

    /**
     * Iterates through radius steps, pitch/yaw angles, and rotational spins
     * to compute relative direction vectors.
     *
     * @param xAngleRange                Pitch range (degrees, 0 to 360)
     * @param yAngleRange                Yaw range (degrees, maps to -180 to 180)
     * @param xAngleSpinIntensity        Spin multiplier around Pitch (X axis)
     * @param yAngleSpinIntensity        Spin multiplier around Yaw (Y axis)
     * @param minRadius                  Minimum distance from center
     * @param maxRadius                  Maximum distance from center
     * @param radiusStep                 Distance increment per iteration
     * @param whatTodoWithAngleRelatives Consumer that receives each generated relative Vec3
     */
    public static void forAnglesDo(
            double xAngleRange,
            double yAngleRange,
            double xAngleSpinIntensity,
            double yAngleSpinIntensity,
            double minRadius,
            double maxRadius,
            double radiusStep,
            Consumer<Vec3> whatTodoWithAngleRelatives
    ) {
        // Clamp and normalize input ranges
        double maxPitch = Math.min(Math.abs(xAngleRange), 360.0);

        // Clamp Y angle to max 360, but offset center to start at -180
        double rawYRange = Math.min(Math.abs(yAngleRange), 360.0);
        double startYaw = -rawYRange / 2.0;
        double endYaw = rawYRange / 2.0;

        // Step increments for angle loops (adjust resolution here if needed)
        double pitchStep = 10.0;
        double yawStep = 10.0;

        // Loop 1: Radius Provider (Expansion/Distance)
        for (double radius = minRadius; radius <= maxRadius; radius += Math.max(0.1, radiusStep)) {

            // Loop 2: Pitch/X-Angle Loop (0 to X Range)
            for (double pitch = 0; pitch <= maxPitch; pitch += pitchStep) {

                // Loop 3: Yaw/Y-Angle Loop (-180 to 180 adjusted range)
                for (double yaw = startYaw; yaw <= endYaw; yaw += yawStep) {

                    // Loop 4: Pitch Spin Iteration
                    double totalPitchSpin = 360.0 * xAngleSpinIntensity;
                    double pitchSpinStep = totalPitchSpin > 0 ? 45.0 : 360.0; // avoid div by 0

                    for (double pSpin = 0; pSpin <= totalPitchSpin; pSpin += pitchSpinStep) {

                        // Loop 5: Yaw Spin Iteration
                        double totalYawSpin = 360.0 * yAngleSpinIntensity;
                        double yawSpinStep = totalYawSpin > 0 ? 45.0 : 360.0;

                        for (double ySpin = -180.0; ySpin <= (180.0 + totalYawSpin); ySpin += yawSpinStep) {

                            // Combine base angles with spin offsets
                            double finalPitch = pitch + pSpin;
                            double finalYaw = yaw + ySpin;

                            // Convert degrees to radians for trigonometric functions
                            double pitchRad = Math.toRadians(finalPitch);
                            double yawRad = Math.toRadians(finalYaw);

                            // Calculate 3D sphere coordinates
                            double x = -Math.sin(yawRad) * Math.cos(pitchRad) * radius;
                            double y = -Math.sin(pitchRad) * radius;
                            double z = Math.cos(yawRad) * Math.cos(pitchRad) * radius;

                            // Emit vector relative to origin (0, 0, 0)
                            whatTodoWithAngleRelatives.accept(new Vec3(x, y, z));
                        }
                    }
                }
            }
        }
    }

    /**
     * Iterates through radius steps, pitch/yaw angles, and rotational spins
     * to compute relative direction vectors.
     *
     * @param xAngleRange         Pitch range (degrees, 0 to 360)
     * @param yAngleRange         Yaw range (degrees, maps to -180 to 180)
     * @param xAngleSpinIntensity Spin multiplier around Pitch (X axis)
     * @param yAngleSpinIntensity Spin multiplier around Yaw (Y axis)
     * @param minRadius           Minimum distance from center
     * @param maxRadius           Maximum distance from center
     * @param radiusStep          Distance increment per iteration
     * @return A list of angle relative Vectors
     */
    public static List<Vec3> getAnglesRelatives(
            double xAngleRange,
            double yAngleRange,
            double xAngleSpinIntensity,
            double yAngleSpinIntensity,
            double minRadius,
            double maxRadius,
            double radiusStep
    ) {
        List<Vec3> angleRelatives = new ArrayList<>();
        // Clamp and normalize input ranges
        double maxPitch = Math.min(Math.abs(xAngleRange), 360.0);

        // Clamp Y angle to max 360, but offset center to start at -180
        double rawYRange = Math.min(Math.abs(yAngleRange), 360.0);
        double startYaw = -rawYRange / 2.0;
        double endYaw = rawYRange / 2.0;

        // Step increments for angle loops (adjust resolution here if needed)
        double pitchStep = 10.0;
        double yawStep = 10.0;

        // Loop 1: Radius Provider (Expansion/Distance)
        for (double radius = minRadius; radius <= maxRadius; radius += Math.max(0.1, radiusStep)) {

            // Loop 2: Pitch/X-Angle Loop (0 to X Range)
            for (double pitch = 0; pitch <= maxPitch; pitch += pitchStep) {

                // Loop 3: Yaw/Y-Angle Loop (-180 to 180 adjusted range)
                for (double yaw = startYaw; yaw <= endYaw; yaw += yawStep) {

                    // Loop 4: Pitch Spin Iteration
                    double totalPitchSpin = 360.0 * xAngleSpinIntensity;
                    double pitchSpinStep = totalPitchSpin > 0 ? 45.0 : 360.0; // avoid div by 0

                    for (double pSpin = 0; pSpin <= totalPitchSpin; pSpin += pitchSpinStep) {

                        // Loop 5: Yaw Spin Iteration
                        double totalYawSpin = 360.0 * yAngleSpinIntensity;
                        double yawSpinStep = totalYawSpin > 0 ? 45.0 : 360.0;

                        for (double ySpin = -180.0; ySpin <= (180.0 + totalYawSpin); ySpin += yawSpinStep) {

                            // Combine base angles with spin offsets
                            double finalPitch = pitch + pSpin;
                            double finalYaw = yaw + ySpin;

                            // Convert degrees to radians for trigonometric functions
                            double pitchRad = Math.toRadians(finalPitch);
                            double yawRad = Math.toRadians(finalYaw);

                            // Calculate 3D sphere coordinates
                            double x = -Math.sin(yawRad) * Math.cos(pitchRad) * radius;
                            double y = -Math.sin(pitchRad) * radius;
                            double z = Math.cos(yawRad) * Math.cos(pitchRad) * radius;
                            angleRelatives.add(new Vec3(x, y, z));
                        }
                    }
                }
            }
        }
        return angleRelatives;
    }
}