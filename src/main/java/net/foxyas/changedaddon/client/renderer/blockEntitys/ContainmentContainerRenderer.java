package net.foxyas.changedaddon.client.renderer.blockEntitys;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.block.entity.ContainmentContainerBlockEntity;
import net.foxyas.changedaddon.block.entity.SnepPlushBlockEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.Minecraft;
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
import net.minecraft.world.phys.Vec3;

public class ContainmentContainerRenderer implements BlockEntityRenderer<ContainmentContainerBlockEntity> {

    private final ContainmentContainerRenderer.FluidModelPart fluidModel;

    public static class FluidModelPart extends Model {
        public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("changed_addon", "containment_container_fluid"), "main");
        private final ModelPart LatexLiquidFill;

        public FluidModelPart(ModelPart root) {
            super(RenderType::eyes);
            this.LatexLiquidFill = root.getChild("LatexLiquidFill");
        }

        public static LayerDefinition createBodyLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();

            PartDefinition LatexLiquidFill = partdefinition.addOrReplaceChild("LatexLiquidFill", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -12.0F, -2.0F, 4.0F, 16.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

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

    public ContainmentContainerRenderer(BlockEntityRendererProvider.Context context) {
        this.fluidModel = new ContainmentContainerRenderer.FluidModelPart(context.bakeLayer(FluidModelPart.LAYER_LOCATION));
    }

    @Override
    public void render(ContainmentContainerBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        poseStack.pushPose();

        // Translade para a posição do bloco
        poseStack.translate(0.5, 0, 0.5);

        TransfurVariant<?> variantColorGet = blockEntity.getTransfurVariant();

        if (variantColorGet != null){
            Color3 firstColor = variantColorGet.getColors().getFirst();
            Color3 secondColor = variantColorGet.getColors().getSecond();
            this.fluidModel.renderToBuffer(
                    poseStack,
                    bufferSource.getBuffer(RenderType.entityCutout(new ResourceLocation("changed_addon:textures/blocks/containment_container_fluid_color1.png"))),
                    light,
                    overlay,firstColor.red(),firstColor.green(),firstColor.blue(),1
            );
            this.fluidModel.renderToBuffer(
                    poseStack,
                    bufferSource.getBuffer(RenderType.entityCutout(new ResourceLocation("changed_addon:textures/blocks/containment_container_fluid_color2.png"))),
                    light,
                    overlay,secondColor.red(),secondColor.green(),secondColor.blue(),1
            );
        }
        poseStack.popPose();
    }
}
