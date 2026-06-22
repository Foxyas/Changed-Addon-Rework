package net.foxyas.changedaddon.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.client.model.advanced.LuminaraFlowerBeastModel;
import net.foxyas.changedaddon.client.renderer.advanced.LuminaraFlowerBeastRenderer;
import net.foxyas.changedaddon.entity.advanced.LuminaraFlowerBeastEntity;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class LuminaraBeastWingsConditionalLayer<T extends LuminaraFlowerBeastEntity, M extends AdvancedHumanoidModel<T>> extends RenderLayer<T, M> {

    public LuminaraBeastWingsConditionalLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull T luminaraFlowerBeastEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (luminaraFlowerBeastEntity.isAwakened()) {
            M parentModel = this.getParentModel();
            if (!(parentModel instanceof LuminaraFlowerBeastModel luminaraFlowerBeastModel)) return;

            parentModel.prepareMobModel(luminaraFlowerBeastEntity, limbSwing, limbSwingAmount, partialTicks);
            int overlay = LivingEntityRenderer.getOverlayCoords(luminaraFlowerBeastEntity, 0.0F);

            if (luminaraFlowerBeastEntity.isFlying() || luminaraFlowerBeastEntity.isFallFlying()) {
                boolean hasElytra = luminaraFlowerBeastEntity.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA);
                if (!hasElytra) {
                    VertexConsumer wingRootBuffer = bufferSource.getBuffer(RenderType.entityTranslucent(LuminaraFlowerBeastRenderer.WING_ROOT_TEXTURE));
                    VertexConsumer wingGlowBuffer = bufferSource.getBuffer(RenderType.energySwirl(LuminaraFlowerBeastRenderer.WING_GLOW_TEXTURE, 0, 0));

                    parentModel.renderToBuffer(poseStack, wingRootBuffer, packedLight, overlay, 1, 1, 1, 1.0F);
                    parentModel.renderToBuffer(poseStack, wingGlowBuffer, packedLight, overlay, 1, 1, 1, 1.0F);
                }
            } else {
                parentModel.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityTranslucent(LuminaraFlowerBeastRenderer.WING_TEXTURE)), packedLight, overlay, 1, 1, 1, 1.0F);
            }
        }

    }
}
