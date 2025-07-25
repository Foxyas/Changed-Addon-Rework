package net.foxyas.changedaddon.client.renderer.blockEntitys;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.foxyas.changedaddon.block.entity.InformantBlockEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
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

        String data;
        if (!itemStack.isEmpty()) {
            data = itemStack.getOrCreateTag().getString("form");
        } else {
            data = informantBlockEntity.getSelectedForm();
        }
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

        ChangedEntity entity = entityCache.computeIfAbsent(res, loc -> {
            ChangedEntity e = tfVariant.getEntityType().create(level);
            if (e == null) return null;
            e.setNoAi(true);
            e.canUpdate(false);
            e.yHeadRot = 0;
            e.yHeadRotO = 0;
            return e;
        });

        if (entity == null) return;

        assert Minecraft.getInstance().player != null;
        entity.tickCount = Minecraft.getInstance().player.tickCount;

        poseStack.pushPose();
        poseStack.translate(.5, 1.5, .5);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
        poseStack.scale(.5f, .5f, .5f);
        float speed = 1.25f;
        float rotation = (Minecraft.getInstance().player.tickCount * speed + partialTick) % 360;
        poseStack.mulPose(Vector3f.YP.rotationDegrees(rotation));


        //This stuff was used when i try render only the model
        var renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
        if (renderer instanceof AdvancedHumanoidRenderer<?,?,?> advancedHumanoidRenderer) {
            AdvancedHumanoidModel model = advancedHumanoidRenderer.getModel();
            ResourceLocation texture = renderer.getTextureLocation(entity);
            //poseStack.pushPose();
            //poseStack.scale(0.5f, 0.5f, 0.5f);
            //poseStack.translate(1, 3.5, 1);
            //renderer.render(entity, 0, 0, poseStack, bufferSource, light);
            //todo make the entity unable to move
            //This stuff was used when i try render only the model
            poseStack.mulPose(Vector3f.XP.rotationDegrees(180));
            var vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(texture));
            float red, green, blue, alpha;
            red = 1;
            green = 1;
            blue = 1;
            alpha = 0.5f;
            model.prepareMobModel(entity, 1, 1, partialTick);
            model.setupAnim(entity, 1, 1, entity.tickCount, 0, 0);
            model.renderToBuffer(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, red, green, blue, alpha);
            poseStack.popPose();
        }
        //Minecraft.getInstance().getEntityRenderDispatcher().render(entity, 0, 0, 0, 0, partialTick, poseStack, bufferSource, LightTexture.FULL_BRIGHT);

        //poseStack.popPose();

            /*entity.canUpdate(false);
            AdvancedHumanoidRenderer renderer = ModelUtils.getChangedEntityRender(entity);
            if (renderer != null) {
                AdvancedHumanoidModel model = renderer.getModel(entity);
                if (model != null) {
                    ResourceLocation texture = renderer.getTextureLocation(entity);
                    poseStack.pushPose();
                    poseStack.scale(0.5f, 0.5f, 0.5f);
                    poseStack.translate(1, 3.5, 1);
                    renderer.render(entity, 0, 0, poseStack, bufferSource, light);
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
            }*/


    }

    @Override
    public boolean shouldRenderOffScreen(@NotNull InformantBlockEntity p_112306_) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return BlockEntityRenderer.super.getViewDistance();
    }

    @Override
    public boolean shouldRender(@NotNull InformantBlockEntity informantBlockEntity, @NotNull Vec3 vec3) {
        if (informantBlockEntity.getItem(0).isEmpty()) {
            return !informantBlockEntity.getSelectedForm().isEmpty() && BlockEntityRenderer.super.shouldRender(informantBlockEntity, vec3);
        }

        return !informantBlockEntity.getItem(0).isEmpty() && BlockEntityRenderer.super.shouldRender(informantBlockEntity, vec3);
    }
}
