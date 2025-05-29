package net.foxyas.changedaddon.client.renderer.shaders;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Vector3f;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiFunction;
import java.util.function.Function;

public class ChangedAddonRenderTypes {
    protected static final RenderStateShard.TransparencyStateShard ADDITIVE_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("additive_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    protected static final RenderStateShard.TransparencyStateShard NO_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("no_transparency", () -> {
        RenderSystem.disableBlend();
    }, () -> {
    });

    protected static final RenderStateShard.LightmapStateShard LIGHTMAP = new RenderStateShard.LightmapStateShard(true);

    protected static final RenderStateShard.WriteMaskStateShard COLOR_WRITE = new RenderStateShard.WriteMaskStateShard(true, false);

    protected static final RenderStateShard.OverlayStateShard OVERLAY = new RenderStateShard.OverlayStateShard(true);

    protected static final RenderStateShard.CullStateShard NO_CULL = new RenderStateShard.CullStateShard(false);

    protected static final Function<Vector3f, RenderStateShard.ShaderStateShard> RENDERTYPE_LIGHT_RAY_SHADER = Util.memoize(resonance -> new ChangedAddonShaders.PrefixedShaderShard(ChangedAddonShaders::getRenderTypeLightRayShader,
            shader -> ChangedAddonShaders.getUniform(shader, "Color").ifPresent(uniform -> uniform.set(resonance))));

    private static final BiFunction<ResourceLocation, Vector3f, RenderType> LIGHT_RAY = Util.memoize((texture, resonance) -> {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_LIGHT_RAY_SHADER.apply(resonance))
                .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                .setTransparencyState(NO_TRANSPARENCY)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY)
                .setCullState(NO_CULL)
                .createCompositeState(true);
        return RenderType.create("light_ray", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, rendertype$compositestate);
    });

    public static RenderType lightRay(ResourceLocation texture, Vector3f vector3f) {
        return LIGHT_RAY.apply(texture, vector3f);
    }

}