package net.foxyas.changedaddon.client.renderer.layers;

import net.foxyas.changedaddon.util.ColorDataHelper;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer.ColorData;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer.ColorFunction;
import net.ltxprogrammer.changed.entity.BasicPlayerInfo;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PulsingColorFunction<T extends ChangedEntity> implements ColorFunction<T> {

    private final ColorFunction<T> defaultFunction;

    public PulsingColorFunction(ColorFunction<T> defaultFunction) {
        this.defaultFunction = defaultFunction;
    }

    public static <T extends ChangedEntity> PulsingColorFunction<T> fromColorFunction(ColorFunction<T> colorFunction) {
        return new PulsingColorFunction<>(colorFunction);
    }

    @Override
    public @Nullable ColorData getColor(T t, BasicPlayerInfo basicPlayerInfo) {
        ColorData defaultFunctionColor = defaultFunction.getColor(t, basicPlayerInfo);
        if (defaultFunctionColor == null) return null;
        Color3 color = defaultFunctionColor.color;
        int ageInTicks = t.tickCount;
        float pulseFactor = (float) (Math.sin(ageInTicks * 0.1f) + 1) / 2;

        float intensity = 1f;
        float pulse = pulseFactor * intensity;
        float red = color.red(); //- pulse;
        float green = color.green(); //- pulse;
        float blue = color.blue(); //- pulse;
        float alpha = defaultFunctionColor.alpha - pulse;

        ColorData colorData = ColorDataHelper.create(new Color3(red, green, blue), alpha, defaultFunctionColor.emissive);
        if (colorData instanceof ColorDataHelper.IDynamicColorData iDynamicColorData) iDynamicColorData.setModifiedRenderType(RenderType::entityTranslucentEmissive);
        return colorData;
    }

    @Override
    public ColorData apply(T entity, BasicPlayerInfo bpi) {
        return ColorFunction.super.apply(entity, bpi);
    }

    @Override
    public Optional<ColorData> getColorSafe(T entity, BasicPlayerInfo bpi) {
        return ColorFunction.super.getColorSafe(entity, bpi);
    }
}
