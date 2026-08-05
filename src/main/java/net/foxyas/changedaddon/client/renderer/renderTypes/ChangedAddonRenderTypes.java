package net.foxyas.changedaddon.client.renderer.renderTypes;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.Util;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;

import static net.minecraft.client.renderer.RenderType.OutlineProperty.IS_OUTLINE;

// BlakeBr0 Code
// https://github.com/BlakeBr0/Cucumber/blob/1.18/src/main/java/com/blakebr0/cucumber/client/ModRenderTypes.java
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ChangedAddonRenderTypes extends RenderType {

    public static final RenderType QUADS_WITH_TRANSPARENCY = RenderType.create(
            ChangedAddonMod.resourceLocString("quads"),
            DefaultVertexFormat.BLOCK,
            VertexFormat.Mode.QUADS,
            2097152,
            true,
            false,
            RenderType.CompositeState.builder()
                    .setLightmapState(LIGHTMAP)
                    .setShaderState(RENDERTYPE_SOLID_SHADER)
                    .setTransparencyState(RenderStateShard.ADDITIVE_TRANSPARENCY)
                    .setCullState(RenderStateShard.NO_CULL)
                    .setTextureState(BLOCK_SHEET_MIPPED)
                    .createCompositeState(true)
    );
    public static final RenderType QUADS_WITH_TRANSPARENCY_NO_CULL = RenderType.create(
            ChangedAddonMod.resourceLocString("quads_no_cull"),
            DefaultVertexFormat.BLOCK,
            VertexFormat.Mode.QUADS,
            2097152,
            true,
            false,
            RenderType.CompositeState.builder()
                    .setLightmapState(LIGHTMAP)
                    .setShaderState(RENDERTYPE_SOLID_SHADER)
                    .setTransparencyState(RenderStateShard.ADDITIVE_TRANSPARENCY)
                    .setCullState(RenderStateShard.NO_CULL)
                    .setTextureState(BLOCK_SHEET_MIPPED)
                    .createCompositeState(true)
    );
    public static final RenderType QUADS = RenderType.create(
            ChangedAddonMod.resourceLocString("quads"),
            DefaultVertexFormat.BLOCK,
            VertexFormat.Mode.QUADS,
            2097152,
            true,
            false,
            RenderType.CompositeState.builder()
                    .setLightmapState(LIGHTMAP)
                    .setShaderState(RENDERTYPE_SOLID_SHADER)
                    .setTransparencyState(RenderStateShard.NO_TRANSPARENCY)
                    .setCullState(RenderStateShard.NO_CULL)
                    .setTextureState(BLOCK_SHEET_MIPPED)
                    .createCompositeState(true)
    );
    public static final RenderType QUADS_NO_CULL = RenderType.create(
            ChangedAddonMod.resourceLocString("quads_no_cull"),
            DefaultVertexFormat.BLOCK,
            VertexFormat.Mode.QUADS,
            2097152,
            true,
            false,
            RenderType.CompositeState.builder()
                    .setLightmapState(LIGHTMAP)
                    .setShaderState(RENDERTYPE_SOLID_SHADER)
                    .setTransparencyState(RenderStateShard.NO_TRANSPARENCY)
                    .setCullState(RenderStateShard.NO_CULL)
                    .setTextureState(BLOCK_SHEET_MIPPED)
                    .createCompositeState(true)
    );
    public static final BiFunction<ResourceLocation, RenderStateShard.CullStateShard, RenderType> OUTLINE_WITH_DEPTH = Util.memoize((resourceLocation, cullStateShard) ->
            create(ChangedAddonMod.resourceLocString("outline_with_deep_test"),
                    DefaultVertexFormat.POSITION_COLOR_TEX,
                    VertexFormat.Mode.QUADS,
                    256,
                    false,
                    false,
                    RenderType.CompositeState.builder()
                            .setShaderState(RENDERTYPE_OUTLINE_SHADER)
                            .setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false))
                            .setCullState(cullStateShard)
                            .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                            .setOutputState(MAIN_TARGET)
                            .createCompositeState(IS_OUTLINE)));

    public static final CullStateShard REVERSE_CULL_STATE = new RenderStateShard.CullStateShard(true) { // culling invertido
        @Override
        public void setupRenderState() {
            GL11.glCullFace(GL11.GL_FRONT);
        }

        @Override
        public void clearRenderState() {
            GL11.glCullFace(GL11.GL_BACK);
        }
    };
    private static final Function<ResourceLocation, RenderType> GLOW_WITH_DEEP_TEST = Util.memoize((p_173255_) -> {
        RenderStateShard.TextureStateShard renderstateshard$texturestateshard = new RenderStateShard.TextureStateShard(p_173255_,
                false,
                false);

        return create(ChangedAddonMod.resourceLocString("glow_with_deep"),
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                256,
                false,
                true,
                RenderType.CompositeState.builder()
                        .setShaderState(RENDERTYPE_EYES_SHADER)
                        .setTextureState(renderstateshard$texturestateshard)
                        .setTransparencyState(ADDITIVE_TRANSPARENCY)
                        .setWriteMaskState(COLOR_DEPTH_WRITE)
                        .createCompositeState(false));
    });
    private static final Function<ResourceLocation, RenderType> GLOW_WITH_DEEP_TEST_CULL = Util.memoize((p_173255_) -> {
        RenderStateShard.TextureStateShard renderstateshard$texturestateshard = new RenderStateShard.TextureStateShard(p_173255_,
                false,
                false);

        return create(ChangedAddonMod.resourceLocString("glow_with_deep_cull"),
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                256,
                false,
                true,
                RenderType.CompositeState.builder()
                        .setShaderState(RENDERTYPE_EYES_SHADER)
                        .setTextureState(renderstateshard$texturestateshard)
                        .setTransparencyState(ADDITIVE_TRANSPARENCY)
                        .setWriteMaskState(COLOR_DEPTH_WRITE)
                        .setCullState(RenderStateShard.CULL)
                        .createCompositeState(false));
    });
    private static final Function<ResourceLocation, RenderType> GLOW_WITH_NO_TRANSPARENCY = Util.memoize((p_173255_) -> {
        RenderStateShard.TextureStateShard renderstateshard$texturestateshard = new RenderStateShard.TextureStateShard(p_173255_,
                false,
                false);

        return create(ChangedAddonMod.resourceLocString("glow_with_no_transparency"),
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                256,
                false,
                true,
                RenderType.CompositeState.builder()
                        .setShaderState(RENDERTYPE_EYES_SHADER)
                        .setTextureState(renderstateshard$texturestateshard)
                        .setTransparencyState(NO_TRANSPARENCY)
                        .setWriteMaskState(COLOR_WRITE)
                        .createCompositeState(false));
    });
    private static final Function<ResourceLocation, RenderType> GLOW_WITH_NO_TRANSPARENCY_CULL = Util.memoize((p_173255_) -> {
        RenderStateShard.TextureStateShard renderstateshard$texturestateshard = new RenderStateShard.TextureStateShard(p_173255_,
                false,
                false);

        return create(ChangedAddonMod.resourceLocString("glow_with_no_transparency_cull"),
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                256,
                false,
                true,
                RenderType.CompositeState.builder()
                        .setShaderState(RENDERTYPE_EYES_SHADER)
                        .setTextureState(renderstateshard$texturestateshard)
                        .setTransparencyState(NO_TRANSPARENCY)
                        .setWriteMaskState(COLOR_WRITE)
                        .setCullState(RenderStateShard.CULL)
                        .createCompositeState(false));
    });
    private static final Function<ResourceLocation, RenderType> GLOW_CUTOUT =
            Util.memoize((texture) -> {

                RenderStateShard.TextureStateShard tex =
                        new RenderStateShard.TextureStateShard(texture, false, false);

                return RenderType.create(
                        ChangedAddonMod.resourceLocString("glow_cutout"),
                        DefaultVertexFormat.NEW_ENTITY,
                        VertexFormat.Mode.QUADS,
                        256,
                        false,
                        false,
                        RenderType.CompositeState.builder()
                                .setShaderState(RENDERTYPE_ENTITY_CUTOUT_SHADER)
                                .setTextureState(tex)
                                .setTransparencyState(NO_TRANSPARENCY)
                                .setLightmapState(LIGHTMAP)
                                .setOverlayState(OVERLAY)
                                .setCullState(NO_CULL)
                                .setWriteMaskState(COLOR_DEPTH_WRITE)
                                .createCompositeState(true)
                );
            });
    private static final Function<ResourceLocation, RenderType> GLOW_CUTOUT_CULL =
            Util.memoize((texture) -> {

                RenderStateShard.TextureStateShard tex =
                        new RenderStateShard.TextureStateShard(texture, false, false);

                return RenderType.create(
                        ChangedAddonMod.resourceLocString("glow_cutout_cull"),
                        DefaultVertexFormat.NEW_ENTITY,
                        VertexFormat.Mode.QUADS,
                        256,
                        false,
                        false,
                        RenderType.CompositeState.builder()
                                .setShaderState(RENDERTYPE_ENTITY_CUTOUT_SHADER)
                                .setTextureState(tex)
                                .setTransparencyState(NO_TRANSPARENCY)
                                .setLightmapState(LIGHTMAP)
                                .setOverlayState(OVERLAY)
                                .setCullState(CULL)
                                .setWriteMaskState(COLOR_DEPTH_WRITE)
                                .createCompositeState(true)
                );
            });
    private static final TransparencyStateShard GHOST_TRANSPARENCY = new TransparencyStateShard("ghost_transparency",
            () -> {
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(GlStateManager.SourceFactor.CONSTANT_ALPHA, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);
                GL14.glBlendColor(1.0F, 1.0F, 1.0F, 0.25F);
            },
            () -> {
                GL14.glBlendColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.disableBlend();
                RenderSystem.defaultBlendFunc();
            });
    public static final RenderType GHOST = RenderType.create(
            ChangedAddonMod.resourceLocString("ghost"),
            DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 2097152, true, false,
            RenderType.CompositeState.builder()
                    .setLightmapState(LIGHTMAP)
                    .setShaderState(RENDERTYPE_SOLID_SHADER)
                    .setTextureState(BLOCK_SHEET)
                    .setTransparencyState(GHOST_TRANSPARENCY)
                    .createCompositeState(false)
    );
    private static final TransparencyStateShard HOLOGRAM_TRANSPARENCY = new TransparencyStateShard("hologram_transparency",
            () -> {
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(GlStateManager.SourceFactor.CONSTANT_ALPHA, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);
                GL14.glBlendColor(1.0F, 1.0F, 1.0F, 0.5F);
            },
            () -> {
                GL14.glBlendColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.disableBlend();
                RenderSystem.defaultBlendFunc();
            });
    private static final BiFunction<ResourceLocation, Boolean, RenderType> QUADS_NO_CULL_WITH_TEXTURE = Util.memoize((resourceLocation, transparency) -> {
        CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
                .setLightmapState(LIGHTMAP)
                .setShaderState(RENDERTYPE_SOLID_SHADER)
                .setTransparencyState(transparency ? RenderStateShard.TRANSLUCENT_TRANSPARENCY : RenderStateShard.NO_TRANSPARENCY)
                .setCullState(RenderStateShard.NO_CULL)
                .setTextureState(resourceLocation == null ? BLOCK_SHEET_MIPPED : new RenderStateShard.TextureStateShard(resourceLocation, false, false))
                .createCompositeState(true);
        return create(ChangedAddonMod.resourceLocString("quads_no_cull_with_texture"),
                DefaultVertexFormat.BLOCK,
                VertexFormat.Mode.QUADS,
                256,
                true,
                false,
                rendertype$compositestate);
    });
    private static final BiFunction<ResourceLocation, Boolean, RenderType> HOLOGRAM = Util.memoize((resourceLocation, outline) -> {
        CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false))
                .setTransparencyState(HOLOGRAM_TRANSPARENCY)
                .setCullState(NO_CULL)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY)
                .createCompositeState(outline);
        return create(ChangedAddonMod.resourceLocString("hologram"),
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                256,
                true,
                true,
                rendertype$compositestate);
    });
    private static final BiFunction<ResourceLocation, Boolean, RenderType> HOLOGRAM_CULL = Util.memoize((resourceLocation, outline) -> {
        CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false))
                .setTransparencyState(HOLOGRAM_TRANSPARENCY)
                .setCullState(CULL)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY)
                .createCompositeState(outline);
        return create(ChangedAddonMod.resourceLocString("hologram_cull"),
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                256,
                true,
                true,
                rendertype$compositestate);
    });

    private static final Function<ResourceLocation, RenderType> GLOW_ENTITY_DECAL = Util.memoize((p_286171_) -> {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_EYES_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(p_286171_, false, false))
                .setTransparencyState(ADDITIVE_TRANSPARENCY)
                .setWriteMaskState(COLOR_WRITE)
                .setDepthTestState(EQUAL_DEPTH_TEST)
                .setCullState(NO_CULL)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY)
                .createCompositeState(false);
        return RenderType.create(ChangedAddonMod.resourceLocString("glow_entity_decal"), DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, false, rendertype$compositestate);
    });

    private static ShaderInstance TRANSLUCENT_OUTLINE_SHADER;
    public static final BiFunction<ResourceLocation, RenderStateShard.CullStateShard, RenderType> OUTLINE_WITH_TRANSLUCENCY = Util.memoize((resourceLocation, cullStateShard) ->
            create(ChangedAddonMod.resourceLocString("outline_with_translucenty"),
                    DefaultVertexFormat.POSITION_COLOR_TEX,
                    VertexFormat.Mode.QUADS,
                    256,
                    false,
                    false,
                    RenderType.CompositeState.builder()
                            .setShaderState(new ShaderStateShard(() -> TRANSLUCENT_OUTLINE_SHADER))
                            .setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false))
                            .setCullState(cullStateShard)
                            .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                            .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                            .setOutputState(OUTLINE_TARGET)
                            .createCompositeState(IS_OUTLINE)));

    private static final BiFunction<ResourceLocation, Boolean, RenderType> ENTITY_ADDITIVE_TRANSLUCENT = Util.memoize((p_286156_, p_286157_) -> {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(p_286156_, false, false)
                ).setTransparencyState(RenderStateShard.ADDITIVE_TRANSPARENCY)
                .setCullState(NO_CULL)
                .setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(p_286157_);
        return create(ChangedAddonMod.resourceLocString("entity_additive_translucent"), DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, rendertype$compositestate);
    });

    public static RenderType entityAdditiveTranslucent(ResourceLocation pLocation, boolean pOutline) {
        return ENTITY_ADDITIVE_TRANSLUCENT.apply(pLocation, pOutline);
    }

    protected static final Function<Float, RenderStateShard.TransparencyStateShard> DYNAMIC_TRANSPARENCY = (alpha) -> new RenderStateShard.TransparencyStateShard(ChangedAddonMod.resourceLocString("dynamic_transparency"), () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1, 1, 1, alpha);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1, 1, 1, 1);
    });

    private static final BiFunction<ResourceLocation, Float, RenderType> GLOW_DYNAMIC = Util.memoize((texture, alpha) -> {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                .setTransparencyState(DYNAMIC_TRANSPARENCY.apply(alpha))
                .setCullState(CULL)
                .setWriteMaskState(COLOR_DEPTH_WRITE)
                .setOverlayState(OVERLAY)
                .createCompositeState(true);

        return create("glow_dynamic", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, rendertype$compositestate);
    });

    public static final RenderType DYNAMIC_END_PORTAL = RenderType.create(ChangedAddonMod.resourceLocString("dynamic_end_portal"),
            DefaultVertexFormat.POSITION_TEX, // <--- IGUAL AO ARMOR GLINT!
            VertexFormat.Mode.QUADS, 256, false, false,
            RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_END_PORTAL_SHADER) // O shader do portal clássico
                    .setTextureState(RenderStateShard.MultiTextureStateShard.builder()
                            .add(TheEndPortalRenderer.END_SKY_LOCATION, false, false)
                            .add(TheEndPortalRenderer.END_PORTAL_LOCATION, false, false)
                            .build())
                    .setWriteMaskState(COLOR_WRITE)
                    .setCullState(NO_CULL)
                    .setDepthTestState(EQUAL_DEPTH_TEST) // Só renderiza onde já tem pixel desenhado
                    .setTransparencyState(GLINT_TRANSPARENCY)
                    .createCompositeState(false)
    );

    public static final Function<ResourceLocation, RenderType> DYNAMIC_END_PORTAL_TEXTURE = Util.memoize(texture -> {
        return RenderType.create(ChangedAddonMod.resourceLocString("dynamic_end_portal"),
                DefaultVertexFormat.POSITION_TEX, // <--- IGUAL AO ARMOR GLINT!
                VertexFormat.Mode.QUADS, 256, false, false,
                RenderType.CompositeState.builder()
                        .setShaderState(RENDERTYPE_END_PORTAL_SHADER) // O shader do portal clássico
                        .setTextureState(RenderStateShard.MultiTextureStateShard.builder()
                                .add(TheEndPortalRenderer.END_SKY_LOCATION, false, false)
                                .add(TheEndPortalRenderer.END_PORTAL_LOCATION, false, false)
                                .add(texture, false, false)
                                .build())
                        .setWriteMaskState(COLOR_WRITE)
                        .setCullState(NO_CULL)
                        .setDepthTestState(EQUAL_DEPTH_TEST) // Só renderiza onde já tem pixel desenhado
                        .setTransparencyState(GLINT_TRANSPARENCY)
                        .createCompositeState(false)
        );
    });

    private static ShaderInstance DYNAMIC_GALAXY_SHADER;
    public static final Function<ResourceLocation, RenderType> DYNAMIC_GALAXY = Util.memoize(texture -> {
        return RenderType.create(
                ChangedAddonMod.resourceLocString("dynamic_galaxy"),
                DefaultVertexFormat.NEW_ENTITY, // <--- OBRIGATÓRIO PARA ENTIDADES (Contém Lightmap e Overlay)
                VertexFormat.Mode.QUADS, 256, false, false,
                RenderType.CompositeState.builder()
                        .setShaderState(new ShaderStateShard(() -> DYNAMIC_GALAXY_SHADER))
                        .setTextureState(RenderStateShard.MultiTextureStateShard.builder()
                                .add(TheEndPortalRenderer.END_SKY_LOCATION, false, false)
                                .add(TheEndPortalRenderer.END_PORTAL_LOCATION, false, false)
                                .add(texture, false, false) // Sampler2: Sua textura de máscara (WING_GLOW_TEXTURE)
                                .build())
                        .setWriteMaskState(COLOR_WRITE)
                        .setCullState(NO_CULL)
                        .setTransparencyState(NO_TRANSPARENCY)
                        .createCompositeState(false)
        );
    });

    public static final TriFunction<ResourceLocation, ResourceLocation, ResourceLocation, RenderType> DYNAMIC_GALAXY_WITH_CUSTOM_TEXTURE = (galaxyLayer1, galaxyLayer2, galaxyMask) -> {
        return RenderType.create(
                ChangedAddonMod.resourceLocString("dynamic_galaxy"),
                DefaultVertexFormat.NEW_ENTITY, // <--- OBRIGATÓRIO PARA ENTIDADES (Contém Lightmap e Overlay)
                VertexFormat.Mode.QUADS, 256, false, false,
                CompositeState.builder()
                        .setShaderState(new ShaderStateShard(() -> DYNAMIC_GALAXY_SHADER))
                        .setTextureState(MultiTextureStateShard.builder()
                                .add(galaxyLayer1, false, false)
                                .add(galaxyLayer2, false, false)
                                .add(galaxyMask, false, false) // Sampler2: Sua textura de máscara;
                                .build())
                        .setWriteMaskState(COLOR_WRITE)
                        .setCullState(NO_CULL)
                        .setTransparencyState(NO_TRANSPARENCY)
                        .createCompositeState(false)
        );
    };

    // unused, just needed to extend RenderType for protected constants
    private ChangedAddonRenderTypes(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
        super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
    }

    @SubscribeEvent
    public static void onRegisterShaders(RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(event.getResourceProvider(),
                ChangedAddonMod.resourceLoc("translucent_outline"),
                DefaultVertexFormat.POSITION_COLOR_TEX), shader -> TRANSLUCENT_OUTLINE_SHADER = shader);
        event.registerShader(new ShaderInstance(event.getResourceProvider(),
                ChangedAddonMod.resourceLoc("dynamic_galaxy"),
                DefaultVertexFormat.NEW_ENTITY), shader -> DYNAMIC_GALAXY_SHADER = shader); // <--- Ajustado aqui também!
    }

    @Nullable
    public static ShaderInstance getTranslucentOutlineShader() {
        return TRANSLUCENT_OUTLINE_SHADER;
    }

    @Nullable
    public static ShaderInstance getDynamicGalaxyShader() {
        return DYNAMIC_GALAXY_SHADER;
    }

    public static RenderType dynamicEndPortal() {
        return DYNAMIC_END_PORTAL;
    }

    public static RenderType dynamicEndPortal(ResourceLocation resourceLocation) {
        return DYNAMIC_END_PORTAL_TEXTURE.apply(resourceLocation);
    }

    public static RenderType dynamicGalaxy(ResourceLocation resourceLocation) {
        return DYNAMIC_GALAXY.apply(resourceLocation);
    }

    public static RenderType dynamicGalaxyWithTexture(ResourceLocation galaxyLayer1,
                                                      ResourceLocation galaxyLayer2,
                                                      ResourceLocation galaxyMask) {
        return DYNAMIC_GALAXY_WITH_CUSTOM_TEXTURE.apply(galaxyLayer1, galaxyLayer2, galaxyMask);
    }

    public static RenderType glowWithNoTransluced(ResourceLocation location) {
        return GLOW_WITH_NO_TRANSPARENCY.apply(location);
    }

    public static RenderType glowWithNoTranslucedCull(ResourceLocation location) {
        return GLOW_WITH_NO_TRANSPARENCY_CULL.apply(location);
    }

    public static RenderType glowCutout(ResourceLocation location) {
        return GLOW_CUTOUT.apply(location);
    }

    public static RenderType glowCutoutCull(ResourceLocation location) {
        return GLOW_CUTOUT_CULL.apply(location);
    }

    public static RenderType glowWithDepthTest(ResourceLocation location) {
        return GLOW_WITH_DEEP_TEST.apply(location);
    }

    public static RenderType glowWithDepthTestCull(ResourceLocation location) {
        return GLOW_WITH_DEEP_TEST_CULL.apply(location);
    }

    public static RenderType QuadsNoCullTexture(@Nullable ResourceLocation resourceLocation, boolean transparency) {
        return QUADS_NO_CULL_WITH_TEXTURE.apply(resourceLocation, transparency);
    }

    public static RenderType hologram(@NotNull ResourceLocation resourceLocation, boolean outline) {
        return HOLOGRAM.apply(resourceLocation, outline);
    }

    public static RenderType hologramCull(@NotNull ResourceLocation resourceLocation, boolean outline) {
        return HOLOGRAM_CULL.apply(resourceLocation, outline);
    }

    public static RenderType outlineWithDepth(ResourceLocation location) {
        return OUTLINE_WITH_DEPTH.apply(location, REVERSE_CULL_STATE);
    }

    public static RenderType outlineWithDepthFull(ResourceLocation location) {
        return OUTLINE_WITH_DEPTH.apply(location, NO_CULL);
    }

    public static RenderType outlineWithTranslucency(ResourceLocation location) {
        return OUTLINE_WITH_TRANSLUCENCY.apply(location, NO_CULL);
    }

    public static RenderType outlineWithTranslucencyCull(ResourceLocation location) {
        return OUTLINE_WITH_TRANSLUCENCY.apply(location, CULL);
    }

    public static RenderType glowDynamic(ResourceLocation location, float alpha) {
        return GLOW_DYNAMIC.apply(location, alpha);
    }

    public static RenderType glowEntityDecal(ResourceLocation pLocation) {
        return GLOW_ENTITY_DECAL.apply(pLocation);
    }

    public static class ParticleRenderTypes {
        public static final ParticleRenderType OVERLAY = new ParticleRenderType() {
            @Override
            public void begin(BufferBuilder builder, @NotNull TextureManager textureManager) {
                RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.disableDepthTest(); // IGNORA BLOCKS
                builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
            }

            @Override
            public void end(Tesselator tessellator) {
                tessellator.end();
                RenderSystem.enableDepthTest(); // restaura para não quebrar o jogo
            }

            @Override
            public String toString() {
                return "OVERLAY_PARTICLE";
            }
        };

    }
}
