package net.foxyas.changedaddon.client.renderer.blockEntitys;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.block.entity.InformantBlockEntity;
import net.foxyas.changedaddon.process.util.ModelUtils;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class InformantBlockEntityRenderer implements BlockEntityRenderer<InformantBlockEntity> {
    private final Map<ResourceLocation, ChangedEntity> entityCache = new HashMap<>();


    public InformantBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(@NotNull InformantBlockEntity informantBlockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int light, int overlay) {
        Level level = informantBlockEntity.getLevel();
        if (level == null) {
            return;
        }

        ItemStack itemStack = informantBlockEntity.getItem(0);
        if (itemStack.isEmpty()) {
            return;
        }

        String data = itemStack.getOrCreateTag().getString("form");
        if (data.isEmpty()) {
            return;
        }

        ResourceLocation res = ResourceLocation.tryParse(data);
        if (res == null) {
            return;
        }

        TransfurVariant<?> tfVariant = ChangedRegistry.TRANSFUR_VARIANT.get().getValue(res);
        if (tfVariant == null) {
            return;
        }

        ChangedEntity changedEntity = entityCache.computeIfAbsent(res, loc -> tfVariant.getEntityType().create(level));

        if (changedEntity != null) {
            changedEntity.canUpdate(false);
            AdvancedHumanoidRenderer renderer = ModelUtils.getChangedEntityRender(changedEntity);
            if (renderer != null) {
                AdvancedHumanoidModel model = renderer.getModel(changedEntity);
                if (model != null) {
                    ResourceLocation texture = renderer.getTextureLocation(changedEntity);
                    poseStack.pushPose();
                    poseStack.scale(0.5f, 0.5f, 0.5f);
                    poseStack.translate(1, 3.5, 1);
                    renderer.render(changedEntity, 0, 0, poseStack, bufferSource, light);
                    poseStack.popPose();
                    //todo make the entity unable to move
                    //This stuff was used when i try render only the model
                    //var vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(texture));
                    //float red, green, blue;
                    //red = 1;
                    //green = 1;
                    //blue = 1;
                    //model.renderToBuffer(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0f);
                }
            }


        }
    }

    @Override
    public boolean shouldRenderOffScreen(@NotNull InformantBlockEntity p_112306_) {
        return BlockEntityRenderer.super.shouldRenderOffScreen(p_112306_);
    }

    @Override
    public int getViewDistance() {
        return BlockEntityRenderer.super.getViewDistance();
    }

    @Override
    public boolean shouldRender(@NotNull InformantBlockEntity informantBlockEntity, @NotNull Vec3 vec3) {
        return !informantBlockEntity.getItem(0).isEmpty() && BlockEntityRenderer.super.shouldRender(informantBlockEntity, vec3);
    }
}
