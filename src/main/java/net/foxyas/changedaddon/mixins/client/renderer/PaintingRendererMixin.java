package net.foxyas.changedaddon.mixins.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.init.ChangedAddonPaintingVariants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PaintingRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.PaintingTextureManager;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PaintingRenderer.class)
public abstract class PaintingRendererMixin extends EntityRenderer<Painting> {

    @Unique
    public boolean shouldGlow = false;

    protected PaintingRendererMixin(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Shadow
    protected abstract void renderPainting(PoseStack pPoseStack, VertexConsumer p_115560_, Painting pPainting, int p_115562_, int p_115563_, TextureAtlasSprite p_115564_, TextureAtlasSprite p_115565_);

    @Inject(method = "render(Lnet/minecraft/world/entity/decoration/Painting;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/PaintingRenderer;renderPainting(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/entity/decoration/Painting;IILnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V", shift = At.Shift.AFTER))
    private void customGlowRender(Painting pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci) {
        PaintingVariant motive = pEntity.getVariant().get();
        shouldGlow = ChangedAddonPaintingVariants.glowPaintings().contains(motive);
        if (shouldGlow) {
            PaintingTextureManager paintingtexturemanager = Minecraft.getInstance().getPaintingTextures();
            VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.eyes(this.getTextureLocation(pEntity)));
            this.renderPainting(pMatrixStack, vertexconsumer, pEntity, motive.getWidth(), motive.getHeight(), paintingtexturemanager.get(motive), paintingtexturemanager.getBackSprite());
        }
    }

}
