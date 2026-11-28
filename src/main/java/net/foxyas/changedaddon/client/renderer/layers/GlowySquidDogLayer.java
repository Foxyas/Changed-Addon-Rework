package net.foxyas.changedaddon.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.renderer.renderTypes.ChangedAddonRenderTypes;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.beast.AbstractLatexSquidDog;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class GlowySquidDogLayer<T extends AbstractLatexSquidDog, M extends AdvancedHumanoidModel<T>>
        extends RenderLayer<T, M> {

    // Ajuste aqui a intensidade do efeito (0.0 a 1.0)
    private static final float ALPHA = 0.2f;

    public GlowySquidDogLayer(RenderLayerParent<T, M> parent) {
        super(parent);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, @NotNull T entity,
                       float limbSwing, float limbSwingAmount, float partialTicks,
                       float ageInTicks, float netHeadYaw, float headPitch) {
        if (!isGlowy(entity) || entity.isInvisible()) return;

        ResourceLocation texture = getResourceLocation(entity);

        // O model do parent já foi animado (setupAnim) antes das layers rodarem.
        this.getParentModel().renderToBuffer(
                poseStack,
                buffer.getBuffer(ChangedAddonRenderTypes.dynamicGalaxyMix(texture)),
                LightTexture.FULL_BRIGHT,
                LivingEntityRenderer.getOverlayCoords(entity, 0.0F),
                1.0F, 1.0F, 1.0F, ALPHA);
    }

    public static <T extends AbstractLatexSquidDog> @NotNull ResourceLocation getResourceLocation(T entity) {
        return ChangedAddonMod.entityTextureLoc(
                "glowing_latex_squid_dogs/latex_squid_dog_" + entity.getGender().getSerializedName());
    }

    public static boolean isGlowy(AbstractLatexSquidDog entity) {
        Component name = entity.getCustomName();
        return name != null && "glowy".equalsIgnoreCase(name.getString());
    }
}