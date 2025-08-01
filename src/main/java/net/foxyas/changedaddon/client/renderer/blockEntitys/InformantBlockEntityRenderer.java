package net.foxyas.changedaddon.client.renderer.blockEntitys;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.foxyas.changedaddon.block.entity.InformantBlockEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class InformantBlockEntityRenderer implements BlockEntityRenderer<InformantBlockEntity> {

    private final Map<TransfurVariant<?>, ChangedEntity> entityCache = new HashMap<>();

    public InformantBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(@NotNull InformantBlockEntity informantBlockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int light, int overlay) {
        Level level = informantBlockEntity.getLevel();
        if (level == null) {
            return;
        }

        TransfurVariant<?> tfVariant = informantBlockEntity.getDisplayTf();
        if (tfVariant == null) {
            return;
        }

        ChangedEntity entity = entityCache.computeIfAbsent(tfVariant, var -> {
            ChangedEntity e = tfVariant.getEntityType().create(level);
            if (e == null) return null;
            e.setNoAi(true);
            e.canUpdate(false);
            e.yHeadRot = 0;
            e.yHeadRotO = 0;
            return e;
        });

        if (entity == null) return;
        if(!(Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity) instanceof AdvancedHumanoidRenderer<?,?,?> renderer)) return;

        assert Minecraft.getInstance().player != null;
        entity.tickCount = Minecraft.getInstance().player.tickCount;

        poseStack.pushPose();
        poseStack.translate(.5, 1.75, .5);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
        poseStack.scale(.5f, .5f, .5f);
        float speed = 1.25f;
        float rotation = (Minecraft.getInstance().player.tickCount * speed + partialTick) % 360;
        poseStack.mulPose(Vector3f.YP.rotationDegrees(rotation));
        poseStack.mulPose(Vector3f.XP.rotationDegrees(180));

        //This stuff was used when i try render only the model
        AdvancedHumanoidModel model = renderer.getModel();
        ResourceLocation texture = ((LivingEntityRenderer)renderer).getTextureLocation(entity);
        var vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(texture));
        float alpha = 0.5f;

        model.prepareMobModel(entity, 0, 0, partialTick);
        model.setupAnim(entity, 0, 0, entity.tickCount + partialTick, 0, 0);
        model.renderToBuffer(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 1, 1, 1, alpha);

        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(@NotNull InformantBlockEntity p_112306_) {
        return true;
    }

    @Override
    public boolean shouldRender(@NotNull InformantBlockEntity informantBlockEntity, @NotNull Vec3 vec3) {
        return informantBlockEntity.getDisplayTf() != null
                && BlockEntityRenderer.super.shouldRender(informantBlockEntity, vec3);
    }
}
