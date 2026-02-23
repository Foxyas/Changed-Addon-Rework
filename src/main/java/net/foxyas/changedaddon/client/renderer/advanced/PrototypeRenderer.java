package net.foxyas.changedaddon.client.renderer.advanced;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.model.PrototypeModel;
import net.foxyas.changedaddon.client.renderer.layers.DynamicEmissiveBodyLayer;
import net.foxyas.changedaddon.entity.advanced.PrototypeEntity;
import net.ltxprogrammer.changed.client.FormRenderHandler;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.ltxprogrammer.changed.entity.BasicPlayerInfo;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

public class PrototypeRenderer extends AdvancedHumanoidRenderer<PrototypeEntity, PrototypeModel> {
    public PrototypeRenderer(EntityRendererProvider.Context context) {
        super(context, new PrototypeModel(context.bakeLayer(PrototypeModel.LAYER_LOCATION)),
                ArmorLatexMaleWolfModel.MODEL_SET, 0.5f);
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        this.addLayer(getEmissiveBaseLayer());
        this.addLayer(new DynamicEmissiveBodyLayer<>(this, ChangedAddonMod.textureLoc("textures/entities/prototype/prototype_glowing_layer")));
        this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
    }

    private @NotNull DynamicEmissiveBodyLayer<PrototypeModel, PrototypeEntity> getEmissiveBaseLayer() {
        return new DynamicEmissiveBodyLayer<>(this, ChangedAddonMod.textureLoc("textures/entities/prototype/prototype_glowing_layer"), DynamicEmissiveBodyLayer.GLOW_BASE) {

            @Override
            public void render(@NotNull PoseStack pMatrixStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, @NotNull PrototypeEntity changedEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
                BasicPlayerInfo basicPlayerInfo = changedEntity.getBasicPlayerInfo();
                Color3 hairColor = basicPlayerInfo.getHairColor();
                VertexConsumer vertexconsumer = pBuffer.getBuffer(this.renderType());
                this.getParentModel().renderToBuffer(pMatrixStack, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, hairColor.red(), hairColor.green(), hairColor.blue(), 0.5F);
            }

            @Override
            public void renderFirstPersonOnArms(PoseStack stack, MultiBufferSource bufferSource, int packedLight, PrototypeEntity changedEntity, HumanoidArm arm, PartPose armPose, float partialTick) {
                stack.pushPose();
                stack.scale(1.0002F, 1.0002F, 1.0002F);
                AdvancedHumanoidModel<PrototypeEntity> parentModel = this.getParentModel();
                BasicPlayerInfo basicPlayerInfo = changedEntity.getBasicPlayerInfo();
                Color3 hairColor = basicPlayerInfo.getHairColor();
                ModelPart armPart = parentModel.getArm(arm);
                armPart.loadPose(armPose);
                FormRenderHandler.renderModelPartWithTexture(parentModel.getArm(arm), stack, bufferSource.getBuffer(this.renderType()), 15728880, hairColor.red(), hairColor.green(), hairColor.blue(), 0.5F);
                stack.popPose();
            }
        };
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull PrototypeEntity entity) {
        return ChangedAddonMod.textureLoc("textures/entities/prototype/prototype_base_layer");
    }
}
