
package net.foxyas.changedaddon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.foxyas.changedaddon.client.model.VoidFoxModel;
import net.foxyas.changedaddon.client.renderer.layers.ParticlesTrailsLayer;
import net.foxyas.changedaddon.entity.VoidFoxEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;

public class VoidFoxRenderer extends AdvancedHumanoidRenderer<VoidFoxEntity, VoidFoxModel, ArmorLatexMaleWolfModel<VoidFoxEntity>> {
    public VoidFoxRenderer(EntityRendererProvider.Context context) {
        super(context, new VoidFoxModel(context.bakeLayer(VoidFoxModel.LAYER_LOCATION)), ArmorLatexMaleWolfModel::new, ArmorLatexMaleWolfModel.INNER_ARMOR, ArmorLatexMaleWolfModel.OUTER_ARMOR, 0.5f);
        this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer::scleraColor, CustomEyesLayer.fixedColorGlowing(Color3.WHITE), CustomEyesLayer.fixedColorGlowing(Color3.WHITE), CustomEyesLayer::noRender, CustomEyesLayer::noRender));
        this.addLayer(new ParticlesTrailsLayer<>(this, 0.25f, ParticleTypes.ASH, ParticleTypes.SOUL_FIRE_FLAME));
    }

    @Override
    public void render(VoidFoxEntity entity, float yRot, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        float dodgeTicks = entity.getDodgingTicks();
        float dodgeProgress = Math.abs(dodgeTicks) / (float) VoidFoxEntity.getMaxDodgingTicks();

        this.model.dodgeProgress = dodgeProgress;
        this.model.partialTicks = partialTicks;
        this.model.isReverse = dodgeTicks < 0;

        // Passa o progresso e direção para o modelo
        if (dodgeProgress > 0) {
            poseStack.pushPose();
            float rotationAngle = 65f * dodgeProgress; // Rotaciona até 90° conforme o progresso da animação
            // Aplica a rotação no eixo X
            if (dodgeTicks > 0) {
                poseStack.mulPose(Vector3f.YP.rotationDegrees(rotationAngle));
            } else {
                poseStack.mulPose(Vector3f.YN.rotationDegrees(rotationAngle));
            }

            // Renderiza o modelo normalmente (chama o super ou código adicional aqui)
            super.render(entity, yRot, partialTicks, poseStack, bufferSource, packedLight);
            poseStack.popPose(); // Restaura a pose original
        } else {
            super.render(entity, yRot, partialTicks, poseStack, bufferSource, packedLight);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(VoidFoxEntity entity) {
        return new ResourceLocation("changed_addon:textures/entities/void_fox_dark.png");
    }
}
