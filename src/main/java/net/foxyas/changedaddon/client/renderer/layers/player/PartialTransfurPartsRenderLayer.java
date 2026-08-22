package net.foxyas.changedaddon.client.renderer.layers.player;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.process.DEBUG;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class PartialTransfurPartsRenderLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public PartialTransfurPartsRenderLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack,
                       @NotNull MultiBufferSource pBuffer,
                       int pPackedLight,
                       @NotNull T pLivingEntity,
                       float pLimbSwing,
                       float pLimbSwingAmount,
                       float pPartialTick,
                       float pAgeInTicks,
                       float pNetHeadYaw,
                       float pHeadPitch) {

        if (!(pLivingEntity instanceof Player player)) {
            return;
        }

        // Obtém a entidade já sincronizada e tickada no ClientTick
        ChangedEntity displayEntity = ClientCacheHandler.ENTITY_CACHE.get(player);
        if (displayEntity == null) {
            return;
        }

        if (!(Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(displayEntity) instanceof AdvancedHumanoidRenderer<?, ?> rawRenderer)) {
            return;
        }

        @SuppressWarnings("unchecked")
        AdvancedHumanoidRenderer<ChangedEntity, ?> renderer = (AdvancedHumanoidRenderer<ChangedEntity, ?>) rawRenderer;
        AdvancedHumanoidModel<ChangedEntity> model = renderer.getModel(displayEntity);

        pPoseStack.pushPose();
//        pPoseStack.translate(0, -0.25f, 0);
        if (player.isShiftKeyDown()) {
            pPoseStack.translate(DEBUG.HeadPosX, 0.65f + DEBUG.HeadPosY, 0.35f + DEBUG.HeadPosZ);
        } else {
            pPoseStack.translate(DEBUG.HeadPosX, -0.25f + DEBUG.HeadPosY, DEBUG.HeadPosZ); // TODO: tweak this to look somehow good.
        }
        model.prepareMobModel(displayEntity, pLimbSwing, pLimbSwingAmount, pPartialTick);
        model.setupAnim(displayEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);

        ModelPart torso = model.getTorso();
        ModelPart tail = null;
        try {
            if (torso.hasChild("Tail")) {
                tail = torso.getChild("Tail");
            } else if (torso.hasChild("tail")) {
                tail = torso.getChild("tail");
            }
        } catch (Exception ignored) {
        }

        if (tail != null) {
            int overlay = LivingEntityRenderer.getOverlayCoords(pLivingEntity, 0.0F);
            tail.render(pPoseStack, pBuffer.getBuffer(RenderType.entityCutoutNoCull(renderer.getTextureLocation(displayEntity))), pPackedLight, overlay);
        }

        pPoseStack.popPose();
    }
}