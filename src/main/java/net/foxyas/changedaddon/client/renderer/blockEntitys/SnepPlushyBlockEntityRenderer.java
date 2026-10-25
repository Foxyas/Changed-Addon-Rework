package net.foxyas.changedaddon.client.renderer.blockEntitys;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.block.entity.SnepPlushyBlockEntity;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class SnepPlushyBlockEntityRenderer extends PlushyBlockEntityRenderer<SnepPlushyBlockEntity> {

    private final SnepPlushExtraModel snepPlushExtraModel;

    public SnepPlushyBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        this.snepPlushExtraModel = new SnepPlushExtraModel(context.bakeLayer(SnepPlushExtraModel.LAYER_LOCATION));
    }

    @Override
    protected void renderModel(SnepPlushyBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int light, int overlay) {
        super.renderModel(blockEntity, partialTick, poseStack, bufferSource, light, overlay);
        BlockState state = blockEntity.getBlockState();
        if (!blockEntity.glowingEyes) return;

        poseStack.pushPose();

        // Translade para a posição do bloco
        poseStack.translate(0.501, 0.375, 0.5);

        // Obtenha a rotação do bloco a partir do BlockState e aplique ao poseStack
        if (state.hasProperty(HorizontalDirectionalBlock.FACING)) {
            Direction direction = state.getValue(HorizontalDirectionalBlock.FACING);

            // Aplique a rotação baseada na orientação do bloco
            switch (direction) {
                case NORTH:
                    break; // Sem rotação adicional necessária
                case SOUTH:
                    poseStack.mulPose(Axis.YP.rotationDegrees(180));
                    break;
                case WEST:
                    poseStack.mulPose(Axis.YP.rotationDegrees(90));
                    break;
                case EAST:
                    poseStack.mulPose(Axis.YP.rotationDegrees(-90));
                    break;
            }
        }

        // Renderize a parte brilhante do modelo
        snepPlushExtraModel.getHead().render(
                poseStack,
                bufferSource.getBuffer(RenderType.eyes(ResourceLocation.parse("changed_addon:textures/block/snow_leopard_plushy/snow_leopard_plushy_glow_eye.png"))),
                light,
                overlay
        );
        poseStack.popPose();
    }

    // Classe estática para o modelo extra
    public static class SnepPlushExtraModel extends Model {
        public static final ModelLayerLocation LAYER_LOCATION = ChangedAddonMod.layerLocation(("snep_plushe_extra_model"), "main");
        private final ModelPart Head;

        public SnepPlushExtraModel(ModelPart root) {
            super(RenderType::eyes);
            this.Head = root.getChild("glowEye");
        }

        public static LayerDefinition createBodyLayer() {
            MeshDefinition meshDefinition = new MeshDefinition();
            PartDefinition partDefinition = meshDefinition.getRoot();

            // Defina o modelo aqui
            partDefinition.addOrReplaceChild("glowEye",
                    CubeListBuilder.create()
                            .texOffs(0, 0)
                            .addBox(-11.85F, -22.7F, 4.7F, 6.8F, 7.0F, 8.0F, new CubeDeformation(-0.9F)),
                    PartPose.offset(8.0F, 24.0F, -8.0F)
            );

            return LayerDefinition.create(meshDefinition, 80, 80);
        }

        @Override
        public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
            Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        }

        public ModelPart getHead() {
            return Head;
        }
    }

}
