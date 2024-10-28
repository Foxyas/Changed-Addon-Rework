package net.foxyas.changedaddon.client.renderer.blockEntitys;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.foxyas.changedaddon.block.entity.SnepPlushBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

public class SnepPlushBlockEntityRenderer implements BlockEntityRenderer<SnepPlushBlockEntity> {

    private final BlockRenderDispatcher blockRenderer;
    private final SnepPlushExtraModel snepPlushExtraModel;
    public SnepPlushBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderer = Minecraft.getInstance().getBlockRenderer();
        this.snepPlushExtraModel = new SnepPlushExtraModel(context.bakeLayer(SnepPlushExtraModel.LAYER_LOCATION));
    }

    // Classe estática para o modelo extra
    public static class SnepPlushExtraModel extends Model {
        public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("changed_addon", "snep_plushe_extra_model"), "main");
        private final ModelPart Head;

        public SnepPlushExtraModel(ModelPart root) {
            super(RenderType::eyes);
            this.Head = root.getChild("glowEye");
        }

        public static LayerDefinition createBodyLayer() {
            MeshDefinition meshDefinition = new MeshDefinition();
            PartDefinition partDefinition = meshDefinition.getRoot();

            // Defina o modelo aqui
            PartDefinition glowEye = partDefinition.addOrReplaceChild("glowEye",
                    CubeListBuilder.create()
                            .texOffs(0, 0)
                            .addBox(-12.0F, -19.0F, 4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.9F)),
                    PartPose.offset(8.0F, 24.0F, -8.0F)
            );

            return LayerDefinition.create(meshDefinition, 80, 80);
        }

        @Override
        public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
            Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);   
        }

        public ModelPart getHead() {
            return Head;
        }
    }

    public void render(SnepPlushBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        BlockPos pos = blockEntity.getBlockPos();
        BlockState state = blockEntity.getBlockState();

        poseStack.pushPose();

        // Translade para a posição do bloco
        poseStack.translate(0.5, 0.375, 0.505);

        // Obtenha a rotação do bloco a partir do BlockState e aplique ao poseStack
        if (state.hasProperty(HorizontalDirectionalBlock.FACING)) {
            Direction direction = state.getValue(HorizontalDirectionalBlock.FACING);

            // Aplique a rotação baseada na orientação do bloco
            switch (direction) {
                case NORTH:
                    break; // Sem rotação adicional necessária
                case SOUTH:
                    poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
                    break;
                case WEST:
                    poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
                    break;
                case EAST:
                    poseStack.mulPose(Vector3f.YP.rotationDegrees(-90));
                    break;
            }
        }

        // Renderize a parte brilhante do modelo
        snepPlushExtraModel.getHead().render(
                poseStack,
                bufferSource.getBuffer(RenderType.eyes(new ResourceLocation("changed_addon:textures/blocks/snow_leopard_plush_glow_eye.png"))),
                light,
                overlay
        );

        poseStack.popPose();
    }

}
