package net.foxyas.changedaddon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.AbstractArrow;

public class EmptyProjectileRenderer<T extends AbstractArrow> extends EntityRenderer<T> {

    private static final ResourceLocation TEXTURE = ResourceLocation.parse(ChangedAddonMod.MODID, "textures/entities/empty.png");

    public EmptyProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.0F;
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        // Nenhuma renderização do modelo em si, mas pode usar efeito futuro
        // Pode adicionar partículas, brilho etc.

    }

        public ResourceLocation getTextureLocation(T entity) {
        return TEXTURE;
    }
}
