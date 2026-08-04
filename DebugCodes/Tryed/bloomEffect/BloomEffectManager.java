package net.foxyas.changedaddon.client;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL30;

//Todo: finish this;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, value = Dist.CLIENT)
public class BloomEffectManager {
    private static PostChain bloomChain;
    private static RenderTarget entityBloomTarget;
    private static RenderTarget bloomFinalTarget;

    public static void init() {
        Minecraft mc = Minecraft.getInstance();
        if (bloomChain != null) {
            bloomChain.close();
        }

        try {
            bloomChain = new PostChain(
                    mc.getTextureManager(),
                    mc.getResourceManager(),
                    mc.getMainRenderTarget(),
                    ChangedAddonMod.resourceLoc("shaders/post/wip/bloom.json")
            );
            bloomChain.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());

            entityBloomTarget = bloomChain.getTempTarget("bloom_raw");
            bloomFinalTarget = bloomChain.getTempTarget("bloom_final");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static RenderTarget getEntityTarget() {
        return entityBloomTarget;
    }

    public static void render(float partialTick) {
        if (bloomChain == null) return;

        // FPS fix: skip the whole chain on frames where nothing needs bloom.
        if (!SelectiveBloomRenderer.hasQueued()) return;

        // Copy depth so terrain still occludes the glow.
        RenderTarget mainTarget = Minecraft.getInstance().getMainRenderTarget();
        if (entityBloomTarget != null) {
            GlStateManager._glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, mainTarget.frameBufferId);
            GlStateManager._glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, entityBloomTarget.frameBufferId);
            GlStateManager._glBlitFrameBuffer(
                    0, 0, mainTarget.width, mainTarget.height,
                    0, 0, entityBloomTarget.width, entityBloomTarget.height,
                    GL30.GL_DEPTH_BUFFER_BIT, GL30.GL_NEAREST
            );
            GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, mainTarget.frameBufferId);
        }

        // Explicit, isolated re-render of only the flagged entities.
        SelectiveBloomRenderer.flushTo(entityBloomTarget);

        // Run blur + composite.
        bloomChain.process(partialTick);

        if (bloomFinalTarget != null) {
            GlStateManager._glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, bloomFinalTarget.frameBufferId);
            GlStateManager._glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, mainTarget.frameBufferId);
            GlStateManager._glBlitFrameBuffer(
                    0, 0, bloomFinalTarget.width, bloomFinalTarget.height,
                    0, 0, mainTarget.width, mainTarget.height,
                    GL30.GL_COLOR_BUFFER_BIT, GL30.GL_NEAREST
            );
            GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, mainTarget.frameBufferId);
        }
    }

    public static void resize(int width, int height) {
        if (bloomChain != null) {
            bloomChain.resize(width, height);
        }
    }

    public static void prepareEntityBuffer() {
        if (entityBloomTarget != null) {
            entityBloomTarget.setClearColor(0.0F, 0.0F, 0.0F, 0.0F);
            entityBloomTarget.clear(Minecraft.ON_OSX);
        }
    }

    private static boolean shouldApplyBloom(LivingEntity entity) {
        return entity.getMainHandItem().is(Items.DEBUG_STICK);
    }

    @SubscribeEvent
    public static void debug(InputEvent.Key event) {
        if (event.getKey() == GLFW.GLFW_KEY_LEFT || event.getKey() == GLFW.GLFW_KEY_RIGHT) {
            BloomEffectManager.init();
        }
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY) {
            prepareEntityBuffer();
        }
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            render(event.getPartialTick());
        }
    }

    // No more redirecting the live draw — just queue for the later explicit pass.
    @SubscribeEvent
    public static void onRenderLivingPre(RenderLivingEvent.Pre<?, ?> event) {
        LivingEntity living = event.getEntity();
        if (shouldApplyBloom(living)) {
            SelectiveBloomRenderer.queue(living, event.getPartialTick());
        }
    }
}