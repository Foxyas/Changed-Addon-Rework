package net.foxyas.changedaddon.ability.api;

import org.lwjgl.glfw.GLFW;

public final class KeyPressingUtils {

    // Private constructor to prevent instantiation
    private KeyPressingUtils() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static boolean isShiftModifier(int modifiers) {
        return (modifiers & GLFW.GLFW_MOD_SHIFT) != 0;
    }

    public static boolean isControlModifier(int modifiers) {
        return (modifiers & GLFW.GLFW_MOD_CONTROL) != 0;
    }

    public static boolean isAltModifier(int modifiers) {
        return (modifiers & GLFW.GLFW_MOD_ALT) != 0;
    }

    public static boolean isSuperModifier(int modifiers) {
        return (modifiers & GLFW.GLFW_MOD_SUPER) != 0;
    }

    public static boolean hasNoModifiers(int modifiers) {
        return modifiers == 0;
    }
}