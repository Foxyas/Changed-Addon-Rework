package net.foxyas.changedaddon.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.ltxprogrammer.changed.client.FormRenderHandler;
import net.ltxprogrammer.changed.client.renderer.layers.FirstPersonLayer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.BasicPlayerInfo;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class DynamicEmissiveBodyLayer<M extends EntityModel<T>, T extends ChangedEntity> extends EyesLayer<T, M> implements FirstPersonLayer<T> {
    public static final Function<ResourceLocation, RenderType> GLOW_BASE = RenderType::entityTranslucentCull;
    private final RenderType renderType;
    private final ResourceLocation emissiveTexture;

    public DynamicEmissiveBodyLayer(RenderLayerParent<T, M> p_116964_, ResourceLocation emissiveTexture) {
        super(p_116964_);
        this.renderType = RenderType.eyes(emissiveTexture);
        this.emissiveTexture = emissiveTexture;
    }

    public DynamicEmissiveBodyLayer(RenderLayerParent<T, M> p_116964_, ResourceLocation emissiveTexture, Function<ResourceLocation, RenderType> renderTypeFunction) {
        super(p_116964_);
        this.renderType = renderTypeFunction.apply(emissiveTexture);
        this.emissiveTexture = emissiveTexture;
    }

    public ResourceLocation getEmissiveTexture() {
        return this.emissiveTexture;
    }

    @Override
    public void render(@NotNull PoseStack pMatrixStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, @NotNull T changedEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        BasicPlayerInfo basicPlayerInfo = changedEntity.getBasicPlayerInfo();
        Color3 hairColor = basicPlayerInfo.getHairColor();
        VertexConsumer vertexconsumer = pBuffer.getBuffer(this.renderType());
        this.getParentModel().renderToBuffer(pMatrixStack, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, hairColor.red(), hairColor.green(), hairColor.blue(), 1.0F);
    }

    public @NotNull RenderType renderType() {
        return this.renderType;
    }

    @Override
    public void renderFirstPersonOnArms(PoseStack stack, MultiBufferSource bufferSource, int packedLight, T changedEntity, HumanoidArm arm, PartPose armPose, float partialTick) {
        stack.pushPose();
        stack.scale(1.0002F, 1.0002F, 1.0002F);
        EntityModel<T> var10 = this.getParentModel();
        BasicPlayerInfo basicPlayerInfo = changedEntity.getBasicPlayerInfo();
        Color3 hairColor = basicPlayerInfo.getHairColor();
        if (var10 instanceof AdvancedHumanoidModel<?> armedModel) {
            ModelPart armPart = armedModel.getArm(arm);
            armPart.loadPose(armPose);
            FormRenderHandler.renderModelPartWithTexture(armedModel.getArm(arm), stack, bufferSource.getBuffer(this.renderType()), 15728880, hairColor.red(), hairColor.green(), hairColor.blue(), 1.0F);
        }

        stack.popPose();
    }
}
