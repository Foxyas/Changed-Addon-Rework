package net.foxyas.changedaddon.client.renderer.basic;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.model.simple.LatexKaylaSharkModel;
import net.foxyas.changedaddon.entity.simple.LatexKaylaSharkEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.*;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexFemaleSharkModel;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

public class LatexKaylaSharkRenderer extends AdvancedHumanoidRenderer<LatexKaylaSharkEntity, LatexKaylaSharkModel> {

    public static final ResourceLocation EMISSIVE_TEXTURE = ChangedAddonMod.textureLoc("textures/entities/latex_kayla_shark/latex_kayla_shark_emissive");
    public static final ResourceLocation RESOURCE_LOCATION = ChangedAddonMod.textureLoc("textures/entities/latex_kayla_shark/latex_kayla_shark");

    public LatexKaylaSharkRenderer(EntityRendererProvider.Context context) {
        super(context, new LatexKaylaSharkModel(context.bakeLayer(LatexKaylaSharkModel.LAYER_LOCATION)), ArmorLatexFemaleSharkModel.MODEL_SET, 0.5f);
        this.addLayer(new LatexParticlesLayer<>(this, this.model));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(),
                CustomEyesLayer.fixedColor(Color3.parseHex("#060606")),
                CustomEyesLayer.fixedColorGlowing(Color3.parseHex("#4cc4f5")),
                CustomEyesLayer.fixedColorGlowing(Color3.parseHex("#a81dc8")),
                CustomEyesLayer::noRender,
                CustomEyesLayer::noRender
        ));
        this.addLayer(GasMaskLayer.forSnouted(this, context.getModelSet()));
        this.addLayer(new EmissiveBodyLayer<>(this, EMISSIVE_TEXTURE) {
            @Override
            public void renderFirstPersonOnArms(PoseStack stack, MultiBufferSource bufferSource, int packedLight, LatexKaylaSharkEntity entity, HumanoidArm arm, PartPose armPose, float partialTick) {
                if (entity.getGlowingState())
                    super.renderFirstPersonOnArms(stack, bufferSource, packedLight, entity, arm, armPose, partialTick);
            }

            @Override
            public void render(@NotNull PoseStack pMatrixStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, @NotNull LatexKaylaSharkEntity pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
                if (pLivingEntity.getGlowingState())
                    super.render(pMatrixStack, pBuffer, pPackedLight, pLivingEntity, pLimbSwing, pLimbSwingAmount, pPartialTicks, pAgeInTicks, pNetHeadYaw, pHeadPitch);
            }
        });
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull LatexKaylaSharkEntity p_114482_) {
        return RESOURCE_LOCATION;
    }
}
