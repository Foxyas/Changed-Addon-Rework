package net.foxyas.changedaddon.client.renderer.blockEntitys;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.block.entity.ContainmentContainerBlockEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ContainmentContainerRenderer implements BlockEntityRenderer<ContainmentContainerBlockEntity> {

    private final ContainmentContainerRenderer.FluidModelPart fluidModel;

    public ContainmentContainerRenderer(BlockEntityRendererProvider.Context context) {
        this.fluidModel = new ContainmentContainerRenderer.FluidModelPart(context.bakeLayer(FluidModelPart.LAYER_LOCATION));
    }

    @Override
    public void render(ContainmentContainerBlockEntity blockEntity, float partialTick, PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int light, int overlay) {
        poseStack.pushPose();

        // Translade para a posição do bloco
        poseStack.translate(0.5, -0.505, 0.5);

        TransfurVariant<?> variantColorGet = blockEntity.getTransfurVariant();

        if (variantColorGet != null) {
            Color3 firstColor = variantColorGet.getColors().getFirst();
            Color3 secondColor = variantColorGet.getColors().getSecond();
            RenderType renderType1 = RenderType.entityTranslucent(new ResourceLocation("changed_addon:textures/blocks/containment_container_fluid_color1.png"));
            RenderType renderType2 = RenderType.entityTranslucent(new ResourceLocation("changed_addon:textures/blocks/containment_container_fluid_color2.png"));
            RenderType glowRenderType2 = RenderType.eyes(new ResourceLocation("changed_addon:textures/blocks/containment_container_fluid_color2.png"));
            this.fluidModel.renderToBuffer(
                    poseStack,
                    bufferSource.getBuffer(renderType1),
                    light,
                    overlay, firstColor.red(), firstColor.green(), firstColor.blue(), 1
            );
            if (variantColorGet.is(ChangedAddonTags.TransfurTypes.GLOWING)) {
                /*
                * Minecraft minecraft = Minecraft.getInstance();
                * OutlineBufferSource outlineBufferSource = minecraft.renderBuffers().outlineBufferSource();
                * Color3 color = variantColorGet.getColors().getSecond();
                * int red = (int) (color.red() * 255);
                * int green = (int) (color.green() * 255);
                * int blue = (int) (color.blue() * 255);
                // Definir a cor do contorno diretamente
                * outlineBufferSource.setColor(red, green, blue, 255);
                */

                this.fluidModel.renderToBuffer(
                        poseStack,
                        bufferSource.getBuffer(RenderType.entityCutout(new ResourceLocation("changed_addon:textures/blocks/containment_container_fluid_full.png"))), // Apenas linhas do contorno
                        light,
                        overlay,
                        secondColor.red(),
                        secondColor.green(),
                        secondColor.blue(),
                        1
                );
                this.fluidModel.renderToBuffer(
                        poseStack,
                        bufferSource.getBuffer(glowRenderType2), // Apenas linhas do contorno
                        light,
                        overlay,
                        secondColor.red(),
                        secondColor.green(),
                        secondColor.blue(),
                        1
                );
            } else {
                this.fluidModel.renderToBuffer(
                        poseStack,
                        bufferSource.getBuffer(renderType2),
                        light,
                        overlay, secondColor.red(), secondColor.green(), secondColor.blue(), 1
                );
            }
        }
        poseStack.popPose();
    }

    @Override
    public int getViewDistance() {
        return 128;
    }

    public static class FluidModelPart extends Model {
        public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ChangedAddonMod.resourceLoc("containment_container_fluid"), "main");
        private final ModelPart LatexLiquidFill;

        public FluidModelPart(ModelPart root) {
            super(RenderType::eyes);
            this.LatexLiquidFill = root.getChild("LatexLiquidFill");
        }

        public static LayerDefinition createBodyLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();

            PartDefinition LatexLiquidFill = partdefinition.addOrReplaceChild("LatexLiquidFill", CubeListBuilder.create().texOffs(0, 8).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

            return LayerDefinition.create(meshdefinition, 32, 32);
        }

        @Override
        public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
            LatexLiquidFill.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        }

        public ModelPart getLatexLiquidFill() {
            return LatexLiquidFill;
        }
    }
}
