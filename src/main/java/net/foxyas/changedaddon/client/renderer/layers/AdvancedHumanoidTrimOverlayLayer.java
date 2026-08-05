package net.foxyas.changedaddon.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.client.renderer.renderTypes.ChangedAddonRenderTypes;
import net.ltxprogrammer.changed.client.FormRenderHandler;
import net.ltxprogrammer.changed.client.renderer.layers.FirstPersonLayer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.BasicPlayerInfo;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;


//TODO: How would that actually work?
//All Entities Would need a specific trim or should use just use a generic model similar to how armors are made?.
//We need to decide that before continuing this.
public class AdvancedHumanoidTrimOverlayLayer<M extends AdvancedHumanoidModel<T>, T extends ChangedEntity> extends RenderLayer<T, M> implements FirstPersonLayer<T> {
    protected final RenderLayerParent<T, M> parentRender;
    protected boolean glowingDecal;

    public AdvancedHumanoidTrimOverlayLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
        this.parentRender = pRenderer;
        this.glowingDecal = false;
    }

    public AdvancedHumanoidTrimOverlayLayer(RenderLayerParent<T, M> pRenderer, boolean glowingDecal) {
        this(pRenderer);
        this.glowingDecal = glowingDecal;
    }

    public AdvancedHumanoidTrimOverlayLayer<M, T> withGlowDecal() {
        this.glowingDecal = true;
        return this;
    }

    public AdvancedHumanoidTrimOverlayLayer<M, T> withoutGlowDecal() {
        this.glowingDecal = false;
        return this;
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, @NotNull T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        M parentModel = this.getParentModel();
        parentModel.renderToBuffer(pPoseStack,
                pBuffer.getBuffer(this.renderType(pLivingEntity)),
                pPackedLight,
                LivingEntityRenderer.getOverlayCoords(pLivingEntity, 0.0F),
                1,
                1,
                1,
                1);
    }

    protected RenderType renderType(T entity) {
        ResourceLocation entityTexture = getTextureLocation(entity);
        return this.glowingDecal ? ChangedAddonRenderTypes.glowEntityDecal(entityTexture) : RenderType.entityDecal(entityTexture);
    }

    @Override
    public void renderFirstPersonOnArms(PoseStack stack, MultiBufferSource bufferSource, int packedLight, T changedEntity, HumanoidArm arm, PartPose armPose, float partialTick) {
        stack.pushPose();
        stack.scale(1.0002F, 1.0002F, 1.0002F);
        var model = this.getParentModel();
        BasicPlayerInfo basicPlayerInfo = changedEntity.getBasicPlayerInfo();
        Color3 hairColor = basicPlayerInfo.getHairColor();
        ModelPart armPart = model.getArm(arm);
        armPart.loadPose(armPose);
        FormRenderHandler.renderModelPartWithTexture(model.getArm(arm),
                stack,
                bufferSource.getBuffer(this.renderType(changedEntity)),
                packedLight,
                hairColor.red(),
                hairColor.green(),
                hairColor.blue(),
                1.0F);
        stack.popPose();
    }
}
