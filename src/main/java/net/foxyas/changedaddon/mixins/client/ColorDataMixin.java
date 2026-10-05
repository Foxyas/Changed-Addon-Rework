package net.foxyas.changedaddon.mixins.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.foxyas.changedaddon.util.ColorDataHelper;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.checkerframework.common.aliasing.qual.Unique;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Function;

@Mixin(value = CustomEyesLayer.ColorData.class, remap = false)
public class ColorDataMixin implements ColorDataHelper.IDynamicColorData {

    @Unique
    private Function<ResourceLocation, RenderType> modifiedRenderType;

    public ColorDataMixin(Color3 color, float alpha, boolean emissive) {
        super();
    }

    @ModifyReturnValue(method = "getRenderType", at = @At("RETURN"))
    protected RenderType mayGetModifiedRenderType(RenderType original, ResourceLocation texture) {
        return modifiedRenderType == null || modifiedRenderType.apply(texture) == null ? original : getModifiedRenderType(texture);
    }

    @Override
    public @Nullable RenderType getModifiedRenderType(ResourceLocation texture) {
        return modifiedRenderType.apply(texture);
    }

    @Override
    public void setModifiedRenderType(Function<ResourceLocation, RenderType> renderType) {
        modifiedRenderType = renderType;
    }
}
