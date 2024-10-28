package net.foxyas.changedaddon.client.renderer.blockEntitys;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.block.entity.SnepPlushBlockEntity;
import net.minecraft.client.Minecraft;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class SnepPlushBlockEntityRenderer implements BlockEntityRenderer<SnepPlushBlockEntity> {
    private final BlockRenderDispatcher blockRenderer;
    private final ModelPart GlowEye;
    public SnepPlushBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderer = Minecraft.getInstance().getBlockRenderer();
        ModelPart modelpart = context.bakeLayer(SnepPlushExtraModel.LAYER_LOCATION);
        this.GlowEye = modelpart.getChild("glowEye");
    }

    // Classe estática para o modelo extra
    public static class SnepPlushExtraModel {
        public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("changed_addon", "snep_plushe_extra_model"), "main");
        private final ModelPart root;

        public SnepPlushExtraModel(ModelPart root) {
            this.root = root;
        }

        public static LayerDefinition createBodyLayer() {
            MeshDefinition meshDefinition = new MeshDefinition();
            PartDefinition partDefinition = meshDefinition.getRoot();

            // Defina o modelo aqui
            PartDefinition bone = partDefinition.addOrReplaceChild("glowEye",
                    CubeListBuilder.create()
                            .texOffs(0, 0)
                            .addBox(-12.0F, -19.0F, 4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.9F)),
                    PartPose.offset(8.0F, 24.0F, -8.0F)
            );

            return LayerDefinition.create(meshDefinition, 80, 80);
        }
    }

    @Override
    public void render(SnepPlushBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        BlockPos pos = blockEntity.getBlockPos();

        poseStack.pushPose();
        poseStack.translate(pos.getX(), pos.getY(), pos.getZ());

        // Renderize a parte brilhante do bloco
        GlowEye.render(poseStack, bufferSource.getBuffer(RenderType.eyes(new ResourceLocation("changed_addon:textures/blocks/sua_textura_brilhante.png"))), light, overlay); // Renderiza o modelo
        poseStack.popPose();
    }

}
