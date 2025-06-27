package net.foxyas.changedaddon.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.configuration.ChangedAddonClientConfigsConfiguration;
import net.ltxprogrammer.changed.client.FormRenderHandler;
import net.ltxprogrammer.changed.client.renderer.layers.FirstPersonLayer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.BasicPlayerInfo;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;

public class CustomHairColorLayer<M extends AdvancedHumanoidModel<T>, T extends ChangedEntity> extends RenderLayer<T, M> implements FirstPersonLayer<T> {
    private final M model;
    private final RenderType renderTypeDark;
    private final RenderType renderTypeLight;
    private final boolean IsFemaleOrNot;

    public CustomHairColorLayer(RenderLayerParent<T, M> parent, M model, ResourceLocation textureBase) {
        super(parent);
        this.model = model;
        this.renderTypeDark = RenderType.entityCutout(ResourceLocation.parse(textureBase.getNamespace(), textureBase.getPath() + "_dark.png"));
        this.renderTypeLight = RenderType.entityCutout(ResourceLocation.parse(textureBase.getNamespace(), textureBase.getPath() + "_light.png"));
        this.IsFemaleOrNot = textureBase.getPath().contains("female"); //Auto Select
    }

    public CustomHairColorLayer(RenderLayerParent<T, M> parent, M model, ResourceLocation textureBase, boolean Female) {
        super(parent);
        this.model = model;
        this.renderTypeDark = RenderType.entityCutout(ResourceLocation.parse(textureBase.getNamespace(), textureBase.getPath() + "_dark.png"));
        this.renderTypeLight = RenderType.entityCutout(ResourceLocation.parse(textureBase.getNamespace(), textureBase.getPath() + "_light.png"));
        this.IsFemaleOrNot = Female; //Manual Select
    }

    public void render(PoseStack pose, MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!ChangedAddonClientConfigsConfiguration.FEMALE_SNEPS_HAIR.get() && IsFemaleOrNot) {
            return;
        } else if (!ChangedAddonClientConfigsConfiguration.MALE_SNEPS_HAIR.get() && !IsFemaleOrNot) {
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

    public void renderFirstPersonOnArms(PoseStack stack, MultiBufferSource bufferSource, int packedLight, T entity, HumanoidArm arm, PoseStack stackCorrector) {
        if (!ChangedAddonClientConfigsConfiguration.FEMALE_SNEPS_HAIR.get() && IsFemaleOrNot) {
            return;
        } else if (!ChangedAddonClientConfigsConfiguration.MALE_SNEPS_HAIR.get() && !IsFemaleOrNot) {
            return;
        }
        BasicPlayerInfo info = entity.getBasicPlayerInfo();
        Color3 coatColor = info.getHairColor();
        stack.pushPose();
        stack.scale(1.0002F, 1.0002F, 1.0002F);
        FormRenderHandler.renderModelPartWithTexture(this.model.getArm(arm), stackCorrector, stack, bufferSource.getBuffer(this.getRenderTypeForColor(coatColor)), packedLight, coatColor.red(), coatColor.green(), coatColor.blue(), 1.0F);
        stack.popPose();
    }
}
