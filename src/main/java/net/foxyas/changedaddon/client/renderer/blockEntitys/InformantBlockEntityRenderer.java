package net.foxyas.changedaddon.client.renderer.blockEntitys;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.block.entity.InformantBlockEntity;
import net.foxyas.changedaddon.client.renderer.renderTypes.ChangedAddonRenderTypes;
import net.foxyas.changedaddon.mixins.client.renderer.LivingEntityRendererAccessor;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InformantBlockEntityRenderer implements BlockEntityRenderer<InformantBlockEntity> {

    private static final ResourceLocation TEX = ChangedAddonMod.textureLoc("textures/entities/dummy");
    private static final Map<TransfurVariant<?>, ChangedEntity> entityCache = new HashMap<>();

    public InformantBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @ApiStatus.Internal
    public static ChangedEntity getDisplayEntity(TransfurVariant<?> tf) {
        if (tf == null) return null;
        return entityCache.computeIfAbsent(tf, var -> {
            assert Minecraft.getInstance().level != null;
            ChangedEntity e = tf.getEntityType().create(Minecraft.getInstance().level);
            if (e == null) return null;
            e.setNoAi(true);
            e.canUpdate(false);
            e.yHeadRot = 0;
            e.yHeadRotO = 0;
            return e;
        });
    }

    @Override
    public void render(@NotNull InformantBlockEntity informantBlockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int light, int overlay) {
        Level level = informantBlockEntity.getLevel();
        if (level == null) {
            return;
        }

        TransfurVariant<?> tfVariant = informantBlockEntity.getDisplayTf();
        ChangedEntity entity = getDisplayEntity(tfVariant);

        boolean dummy = entity == null;
        if (dummy) entity = getDisplayEntity(ChangedTransfurVariants.CRYSTAL_WOLF_HORNED.get());

        if (!(Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity) instanceof AdvancedHumanoidRenderer<? super ChangedEntity, ?> renderer))
            return;

        assert Minecraft.getInstance().player != null;
        entity.tickCount = Minecraft.getInstance().player.tickCount;

        poseStack.pushPose();
        poseStack.translate(.5, 1.75, .5);
        poseStack.mulPose(Axis.YP.rotationDegrees(90));
        poseStack.scale(.5f, .5f, .5f);
        float speed = 1.25f;
        float rotation = (Minecraft.getInstance().player.tickCount * speed + partialTick) % 360;
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        poseStack.mulPose(Axis.XP.rotationDegrees(180));

        AdvancedHumanoidModel<? super ChangedEntity> model = renderer.getModel();
        ResourceLocation texture = renderer.getTextureLocation(entity);
        var vertexConsumer = bufferSource.getBuffer(dummy ? RenderType.entitySolid(TEX) : ChangedAddonRenderTypes.hologramCull(texture, true));

        float ageInTicks = entity.tickCount + partialTick;
        model.prepareMobModel(entity, 0, 0, partialTick);
        model.setupAnim(entity, 0, 0, ageInTicks, 0, 0);
        model.renderToBuffer(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        if (renderer instanceof LivingEntityRendererAccessor livingEntityRendererAccessor) {
            List<RenderLayer<LivingEntity, EntityModel<LivingEntity>>> layers = livingEntityRendererAccessor.getLayers();
            if (layers != null && !layers.isEmpty()) {
                for (RenderLayer<LivingEntity, EntityModel<LivingEntity>> layer : layers) {
                    layer.render(poseStack, bufferSource, LightTexture.FULL_BRIGHT, entity, 0, 0, partialTick, ageInTicks, 0, 0);
                }
            }
        }

        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(@NotNull InformantBlockEntity p_112306_) {
        return true;
    }
}
