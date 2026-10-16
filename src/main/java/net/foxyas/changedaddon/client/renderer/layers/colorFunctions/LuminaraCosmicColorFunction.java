package net.foxyas.changedaddon.client.renderer.layers.colorFunctions;

import net.foxyas.changedaddon.client.renderer.renderTypes.ChangedAddonRenderTypes;
import net.foxyas.changedaddon.entity.advanced.LuminaraFlowerBeastEntity;
import net.foxyas.changedaddon.util.ColorDataHelper;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer.ColorData;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer.ColorFunction;
import net.ltxprogrammer.changed.entity.BasicPlayerInfo;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.util.Color3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class LuminaraCosmicColorFunction<T extends LuminaraFlowerBeastEntity> implements ColorFunction<T> {

    private final ColorFunction<T> defaultFunction;

    public LuminaraCosmicColorFunction(ColorFunction<T> defaultFunction) {
        this.defaultFunction = defaultFunction;
    }

    public static <T extends LuminaraFlowerBeastEntity> LuminaraCosmicColorFunction<T> fromColorFunction(ColorFunction<T> colorFunction) {
        return new LuminaraCosmicColorFunction<>(colorFunction);
    }

    @Override
    public @Nullable ColorData getColor(T t, BasicPlayerInfo basicPlayerInfo) {
        if (!t.hasCosmicWings()) return defaultFunction.getColor(t, basicPlayerInfo);

        ColorData defaultFunctionColor = defaultFunction.getColor(t, basicPlayerInfo);
        if (defaultFunctionColor == null) return null;
        Color3 color = defaultFunctionColor.color;
        float red = color.red(); //- pulse;
        float green = color.green(); //- pulse;
        float blue = color.blue(); //- pulse;
        float alpha = defaultFunctionColor.alpha;

        ColorData colorData = ColorDataHelper.create(new Color3(red, green, blue), alpha, defaultFunctionColor.emissive);
        if (colorData instanceof ColorDataHelper.IDynamicColorData iDynamicColorData)
            iDynamicColorData.setModifiedRenderType(ChangedAddonRenderTypes::dynamicGalaxy);
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
