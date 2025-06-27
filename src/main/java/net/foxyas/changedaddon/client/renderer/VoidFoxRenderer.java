
package net.foxyas.changedaddon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.foxyas.changedaddon.client.model.VoidFoxModel;
import net.foxyas.changedaddon.client.renderer.layers.ModelFlickerLayer;
import net.foxyas.changedaddon.client.renderer.layers.ParticlesTrailsLayer;
import net.foxyas.changedaddon.entity.VoidFoxEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class VoidFoxRenderer extends AdvancedHumanoidRenderer<VoidFoxEntity, VoidFoxModel, ArmorLatexMaleWolfModel<VoidFoxEntity>> {
    public VoidFoxRenderer(EntityRendererProvider.Context context) {
        super(context, new VoidFoxModel(context.bakeLayer(VoidFoxModel.LAYER_LOCATION)), ArmorLatexMaleWolfModel::new, ArmorLatexMaleWolfModel.INNER_ARMOR, ArmorLatexMaleWolfModel.OUTER_ARMOR, 0.5f);
        this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer::scleraColor, CustomEyesLayer.fixedColorGlowing(Color3.WHITE), CustomEyesLayer.fixedColorGlowing(Color3.WHITE), CustomEyesLayer::noRender, CustomEyesLayer::noRender));
        this.addLayer(new ParticlesTrailsLayer<>(this, 0.025f, ParticleTypes.ASH));
        this.addLayer(new ParticlesTrailsLayer<>(this, 0.0025f, ParticleTypes.END_ROD));
        this.addLayer(new ModelFlickerLayer<>(this));
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
        /*if (this.entityRenderDispatcher.distanceToSqr(entity) < 4096.0D) {
            // Monta os Component exatamente como você queria
            List<Component> debugLines = List.of(
                    new TextComponent("HP: " + entity.getHealth())
                            .withStyle(style -> style.withColor(ChatFormatting.RED).withBold(true)),

                    new TextComponent("Phase: " + entity.getAttackInUse())
                            .withStyle(style -> style.withColor(ChatFormatting.YELLOW)),

                    new TextComponent("Attack1Cooldown: " + entity.getAttack1Cooldown())
                            .withStyle(style -> style.withColor(ChatFormatting.AQUA)),

                    new TextComponent("Attack2Cooldown: " + entity.getAttack2Cooldown())
                            .withStyle(style -> style.withColor(ChatFormatting.AQUA)),

                    new TextComponent("Attack3Cooldown: " + entity.getAttack3Cooldown())
                            .withStyle(style -> style.withColor(ChatFormatting.AQUA))
            );

            renderDebugText(debugLines, poseStack, bufferSource, entity, packedLight);
        }*/

    private void renderDebugText(List<Component> lines, PoseStack poseStack, MultiBufferSource bufferSource, LivingEntity entity, int packedLight) {
        Font font = Minecraft.getInstance().font;
        float scale = 0.025F;
        int lineHeight = 10;
        int totalHeight = lines.size() * lineHeight;
        double yOffset = entity.getBbHeight() + 0.5D + lines.size() * 0.1f;

        poseStack.pushPose();
        poseStack.translate(0.0D, yOffset, 0.0D);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation()); // vira para o jogador
        poseStack.scale(-scale, -scale, scale); // escala pequena

        for (int i = 0; i < lines.size(); i++) {
            Component line = lines.get(i);
            float y = -totalHeight / 2.0F + i * lineHeight;
            font.drawInBatch(line,
                    -font.width("HP: " + entity.getHealth()) / 2.0F,
                    y,                          // Y da linha
                    0xFFFFFF,                   // Cor base (pode ser qualquer, será sobrescrita por .withStyle)
                    false,                      // Sem sombra
                    poseStack.last().pose(),    // Matrix do PoseStack
                    bufferSource,               // Buffer vindo do método render
                    false,                      // seeThrough (normalmente false)
                    0,                          // background color (0 = transparente)
                    packedLight                 // luz do ambiente
            );
        }

        poseStack.popPose();
    }


        public ResourceLocation getTextureLocation(VoidFoxEntity entity) {
        return ResourceLocation.parse("changed_addon:textures/entities/void_fox_dark.png");
    }
}
