package net.foxyas.changedaddon.util;

import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.lang.reflect.Constructor;

@OnlyIn(Dist.CLIENT)
public class ColorDataHelper {
    private static final Constructor<CustomEyesLayer.ColorData> COLOR_DATA_CONSTRUCTOR;

    static {
        try {
            // Retrieve the private constructor: (Color3, float, boolean)
            COLOR_DATA_CONSTRUCTOR = CustomEyesLayer.ColorData.class.getDeclaredConstructor(Color3.class, float.class, boolean.class);
            COLOR_DATA_CONSTRUCTOR.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Failed to locate CustomEyesLayer.ColorData constructor", e);
        }
    }

    public static CustomEyesLayer.ColorData create(Color3 color, float alpha, boolean emissive) {
        try {
            return COLOR_DATA_CONSTRUCTOR.newInstance(color, alpha, emissive);
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate CustomEyesLayer.ColorData via Reflection", e);
        }
    }
}