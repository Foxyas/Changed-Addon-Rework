package net.foxyas.changedaddon.mixins.client.renderer.test;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.client.renderer.depth_outline.DepthOutline;
import net.foxyas.changedaddon.client.renderer.depth_outline.GlowHandles;
import net.foxyas.changedaddon.client.renderer.depth_outline.OutlineHandle;
import net.foxyas.changedaddon.client.renderer.depth_outline.SilhouetteSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Final @Shadow private Minecraft minecraft;
    @Shadow public abstract boolean shouldShowEntityOutlines();
    @Shadow protected abstract void renderEntity(Entity e, double x, double y, double z, float pt,
                                                 PoseStack ps, MultiBufferSource buf);

    @Inject(method = "initOutline", at = @At("TAIL"))
    private void glowdepth$init(CallbackInfo ci) { DepthOutline.init(minecraft); }

    @Inject(method = "resize", at = @At("TAIL"))
    private void glowdepth$resize(int w, int h, CallbackInfo ci) { DepthOutline.resize(w, h); }

    @Inject(method = "close", at = @At("HEAD"))
    private void glowdepth$close(CallbackInfo ci) { DepthOutline.close(); }

    @Redirect(method = "renderLevel",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/LevelRenderer;renderEntity(Lnet/minecraft/world/entity/Entity;DDDFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V"))
    private void glowdepth$renderEntity(LevelRenderer self, Entity e, double x, double y, double z,
                                        float pt, PoseStack ps, MultiBufferSource buf) {
        OutlineHandle h = GlowHandles.get(e);
        if (h != null && h.enabled() && shouldShowEntityOutlines()) {
            SilhouetteSource src = DepthOutline.source(minecraft);
            if (src != null) {
                int c = h.color() != -1 ? h.color() : e.getTeamColor();
                src.setColor(FastColor.ARGB32.red(c), FastColor.ARGB32.green(c), FastColor.ARGB32.blue(c));
                buf = new SilhouetteSource.Tee(buf, src);
            }
        }
        renderEntity(e, x, y, z, pt, ps, buf); // UMA chamada só
    }

    @Inject(method = "renderLevel", at = @At("TAIL"))
    private void glowdepth$flush(CallbackInfo ci) { DepthOutline.flush(minecraft); }

    // HEAD: o nosso entra primeiro, o vanilla cola depois e cobre
    @Inject(method = "doEntityOutline", at = @At("HEAD"))
    private void glowdepth$blit(CallbackInfo ci) { DepthOutline.blit(minecraft); }
}