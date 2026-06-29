package net.foxyas.changedaddon.util;

import java.util.function.Function;

public class MathFormulasUtil {

    /**
     * Main method: Maps a time value (0 to 1) to a range [min, max] using a specific easing function.
     */
    public static float lerpEase(float t, float min, float max, EasingType type) {
        float easedTime = type.apply(Math.max(0, Math.min(1, t)));
        return min + (max - min) * easedTime;
    }

    public enum EasingType {
        /** Linear: Constant speed. */
        LINEAR(t -> t),

        /** Starts slow, ends fast. Ideal for acceleration or falling objects. */
        QUAD_IN(t -> t * t),

        /** Starts fast, ends slow. Ideal for braking or UI elements appearing. */
        QUAD_OUT(t -> 1 - (1 - t) * (1 - t)),

        /** Smooth at both start and end. Ideal for organic movements or breathing effects. */
        QUAD_IN_OUT(t -> t < 0.5f ? 2 * t * t : 1 - (float) Math.pow(-2 * t + 2, 2) / 2),

        /** "Sling" effect: Goes slightly backward before moving forward. */
        BACK_IN(t -> 2.70158f * t * t * t - 1.70158f * t * t),

        /** "Spring" effect: Over shoots the target value and settles back at the end. */
        BACK_OUT(t -> {
            float c1 = 1.70158f;
            float c3 = c1 + 1;
            return 1 + c3 * (float)Math.pow(t - 1, 3) + c1 * (float)Math.pow(t - 1, 2);
        }),

        /** Circular: Smoothness based on a circular arc. */
        CIRC_OUT(t -> (float) Math.sqrt(1 - Math.pow(t - 1, 2)));

        private final Function<Float, Float> formula;

        EasingType(Function<Float, Float> formula) {
            this.formula = formula;
        }

        public float apply(float t) {
            return formula.apply(t);
        }
    }
}