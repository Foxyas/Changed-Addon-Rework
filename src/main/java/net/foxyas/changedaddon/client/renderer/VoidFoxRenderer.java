package net.foxyas.changedaddon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.client.model.VoidFoxModel;
import net.foxyas.changedaddon.client.renderer.layers.EntityOutlineLayer;
import net.foxyas.changedaddon.client.renderer.layers.ModelFlickerLayer;
import net.foxyas.changedaddon.client.renderer.layers.ParticlesTrailsLayer;
import net.foxyas.changedaddon.entity.bosses.VoidFoxEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.EmissiveBodyLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class VoidFoxRenderer extends AdvancedHumanoidRenderer<VoidFoxEntity, VoidFoxModel> {
    public VoidFoxRenderer(EntityRendererProvider.Context context) {
        super(context, new VoidFoxModel(context.bakeLayer(VoidFoxModel.LAYER_LOCATION)), ArmorLatexMaleWolfModel.MODEL_SET, 0.5f);
        this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer::scleraColor, CustomEyesLayer.fixedColorGlowing(Color3.WHITE), CustomEyesLayer.fixedColorGlowing(Color3.WHITE), CustomEyesLayer::noRender, CustomEyesLayer::noRender));
        this.addLayer(new EmissiveBodyLayer<>(this, ResourceLocation.parse("changed_addon:textures/entities/void_fox/void_fox_glowing_layer.png")));
        this.addLayer(new ParticlesTrailsLayer<>(this, 0.025f, ParticleTypes.ASH));
        this.addLayer(new ParticlesTrailsLayer<>(this, 0.0025f, ParticleTypes.END_ROD));
        this.addLayer(new ModelFlickerLayer<>(this));
        this.addLayer(new EntityOutlineLayer<>(this, this::getOutlineTextureLocation));
    }

    @Override
    public void render(@NotNull VoidFoxEntity entity, float yRot, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, yRot, partialTicks, poseStack, bufferSource, packedLight);
    }


    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull VoidFoxEntity entity) {
        return ResourceLocation.parse("changed_addon:textures/entities/void_fox/void_fox_new.png");
    }


    public @NotNull ResourceLocation getOutlineTextureLocation(@NotNull VoidFoxEntity entity) {
        return ResourceLocation.parse("changed_addon:textures/entities/void_fox/void_fox_new_outline.png");
    }
}
