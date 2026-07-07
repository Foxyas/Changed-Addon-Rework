package net.foxyas.changedaddon.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.client.model.advanced.LuminaraFlowerBeastModel;
import net.foxyas.changedaddon.client.renderer.advanced.LuminaraFlowerBeastRenderer;
import net.foxyas.changedaddon.client.renderer.renderTypes.ChangedAddonRenderTypes;
import net.foxyas.changedaddon.entity.advanced.LuminaraFlowerBeastEntity;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
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
                    // 1. Pega o buffer da base/outras partes e renderiza
                    VertexConsumer wingRootBuffer = bufferSource.getBuffer(RenderType.entityTranslucent(LuminaraFlowerBeastRenderer.WING_ROOT_TEXTURE));
                    parentModel.renderToBuffer(poseStack, wingRootBuffer, packedLight, overlay, 1, 1, 1, 1.0F);

                    // 2. FORÇA o Minecraft a desenhar o buffer translucido agora mesmo
                    if (bufferSource instanceof BufferSource immediateBuffer) {
                        immediateBuffer.endBatch(RenderType.entityTranslucent(LuminaraFlowerBeastRenderer.WING_ROOT_TEXTURE));
                    }

                    // 3. Pega o seu buffer de galáxia e desenha por cima
                    VertexConsumer wingGlowBuffer = bufferSource.getBuffer(ChangedAddonRenderTypes.dynamicGalaxy(LuminaraFlowerBeastRenderer.WING_GLOW_TEXTURE));
                    parentModel.renderToBuffer(poseStack, wingGlowBuffer, packedLight, overlay, 1, 1, 1, 1.0F);

//                    VertexConsumer wingRootBuffer = bufferSource.getBuffer(RenderType.entityTranslucent(LuminaraFlowerBeastRenderer.WING_ROOT_TEXTURE));
//                    VertexConsumer wingGlowBuffer = bufferSource.getBuffer(RenderType.energySwirl(LuminaraFlowerBeastRenderer.WING_GLOW_TEXTURE, 0, 0));
//
//                    parentModel.renderToBuffer(poseStack, wingRootBuffer, packedLight, overlay, 1, 1, 1, 1.0F);
//                    parentModel.renderToBuffer(poseStack, wingGlowBuffer, packedLight, overlay, 1, 1, 1, 1.0F);
                }
            } else {
                parentModel.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityTranslucent(LuminaraFlowerBeastRenderer.WING_TEXTURE)), packedLight, overlay, 1, 1, 1, 1.0F);
            }
        }

    }
}
