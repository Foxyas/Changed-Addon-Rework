package net.foxyas.changedaddon.client.renderer.depth_outline;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public final class DepthOutline {

    /** Depth test bias. 0/0 worked best with armor. */
    public static float biasFactor = 0.0F, biasUnits = 0.0F;

    private static PostChain chain;
    private static RenderTarget target;
    private static SilhouetteSource source;
    private static boolean queued, readyToBlit, failed;

    public static RenderTarget target() { return target; }
    public static void markQueued() { queued = true; }

    public static void init(Minecraft mc) {
        close();
        failed = false;
        try {
            chain = new PostChain(mc.getTextureManager(), mc.getResourceManager(), mc.getMainRenderTarget(),
                    ChangedAddonMod.resourceLoc("shaders/post/depth_entity_outline.json"));
            chain.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());
            target = chain.getTempTarget("final");
            source = new SilhouetteSource();
        } catch (Exception e) {
            ChangedAddonMod.LOGGER.error("[DepthOutline] init failed", e);
            close();
            failed = true;
        }
        mc.getMainRenderTarget().bindWrite(false);
    }

    /** Lazy init: does not rely only on initOutline having been called. */
    @Nullable
    public static SilhouetteSource source(Minecraft mc) {
        if (chain == null && !failed) init(mc);
        return source;
    }

    public static void resize(int w, int h) { if (chain != null) chain.resize(w, h); }

    public static void close() {
        if (chain != null) chain.close();
        chain = null; target = null; source = null;
    }

    /** End of renderLevel: the main depth already holds terrain, entities and translucents. */
    public static void flush(Minecraft mc) {
        readyToBlit = false;
        if (!queued || chain == null) { queued = false; return; }
        queued = false;
        target.clear(Minecraft.ON_OSX);
        target.copyDepthFrom(mc.getMainRenderTarget());
        source.drawAll();
        chain.process(mc.getFrameTime());
        mc.getMainRenderTarget().bindWrite(false);
        readyToBlit = true;
    }

    /** Start of doEntityOutline: vanilla pastes its own AFTER this, so vanilla ends up on top. */
    public static void blit(Minecraft mc) {
        if (!readyToBlit) return;
        readyToBlit = false;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        target.blitToScreen(mc.getWindow().getWidth(), mc.getWindow().getHeight(), false);
        RenderSystem.disableBlend();
    }
}