package net.foxyas.changedaddon.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.configuration.ChangedAddonClientConfiguration;
import net.ltxprogrammer.changed.client.FormRenderHandler;
import net.ltxprogrammer.changed.client.renderer.layers.FirstPersonLayer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.BasicPlayerInfo;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

public class CustomHairColorLayer<M extends AdvancedHumanoidModel<T>, T extends ChangedEntity> extends RenderLayer<T, M> implements FirstPersonLayer<T> {
    private final M model;
    private final RenderType renderTypeDark;
    private final RenderType renderTypeLight;
    private final boolean isFemale;

    public CustomHairColorLayer(RenderLayerParent<T, M> parent, M model, ResourceLocation textureBase) {
        super(parent);
        this.model = model;
        this.renderTypeDark = RenderType.entityCutout(ResourceLocation.fromNamespaceAndPath(textureBase.getNamespace(), textureBase.getPath() + "_dark.png"));
        this.renderTypeLight = RenderType.entityCutout(ResourceLocation.fromNamespaceAndPath(textureBase.getNamespace(), textureBase.getPath() + "_light.png"));
        this.isFemale = textureBase.getPath().contains("female"); //Auto Select
    }

    public CustomHairColorLayer(RenderLayerParent<T, M> parent, M model, ResourceLocation textureBase, boolean female) {
        super(parent);
        this.model = model;
        this.renderTypeDark = RenderType.entityCutout(ResourceLocation.fromNamespaceAndPath(textureBase.getNamespace(), textureBase.getPath() + "_dark.png"));
        this.renderTypeLight = RenderType.entityCutout(ResourceLocation.fromNamespaceAndPath(textureBase.getNamespace(), textureBase.getPath() + "_light.png"));
        this.isFemale = female; //Manual Select
    }

    public void render(@NotNull PoseStack pose, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!ChangedAddonClientConfiguration.FEMALE_SNEPS_HAIR.get() && isFemale) {
            return;
        } else if (!ChangedAddonClientConfiguration.MALE_SNEPS_HAIR.get() && !isFemale) {
            return;
        }

        if (!entity.isInvisible()) {
            BasicPlayerInfo info = entity.getBasicPlayerInfo();
            Color3 coatColor = info.getHairColor();
            int overlay = LivingEntityRenderer.getOverlayCoords(entity, 0.0F);
            this.model.renderToBuffer(pose, bufferSource.getBuffer(this.getRenderTypeForColor(coatColor)), packedLight, overlay, coatColor.red(), coatColor.green(), coatColor.blue(), 1.0F);
        }
    }

    public RenderType getRenderTypeForColor(Color3 color) {
        return color.brightness() < 0.5F ? this.renderTypeDark : this.renderTypeLight;
    }

    @Override
    public void renderFirstPersonOnArms(PoseStack stack, MultiBufferSource bufferSource, int packedLight, T entity, HumanoidArm arm, PartPose armPose, float partialTick) {
        FirstPersonLayer.super.renderFirstPersonOnArms(stack, bufferSource, packedLight, entity, arm, armPose, partialTick);
        if (!ChangedAddonClientConfiguration.FEMALE_SNEPS_HAIR.get() && isFemale) {
            return;
        } else if (!ChangedAddonClientConfiguration.MALE_SNEPS_HAIR.get() && !isFemale) {
            return;
        }
        BasicPlayerInfo info = entity.getBasicPlayerInfo();
        Color3 coatColor = info.getHairColor();
        stack.pushPose();
        stack.scale(1.0002F, 1.0002F, 1.0002F);
        FormRenderHandler.renderModelPartWithTexture(this.model.getArm(arm), stack, bufferSource.getBuffer(this.getRenderTypeForColor(coatColor)), packedLight, coatColor.red(), coatColor.green(), coatColor.blue(), 1.0F);
        stack.popPose();
    }
}
