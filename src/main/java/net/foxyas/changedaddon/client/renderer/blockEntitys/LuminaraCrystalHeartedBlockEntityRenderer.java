package net.foxyas.changedaddon.client.renderer.blockEntitys;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.foxyas.changedaddon.block.entity.LuminarCrystalHeartedBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.Random;

public class LuminaraCrystalHeartedBlockEntityRenderer extends SimpleAggedBlockEntityRenderer<LuminarCrystalHeartedBlockEntity> {
    public LuminaraCrystalHeartedBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(@NotNull LuminarCrystalHeartedBlockEntity pBlockEntity, float pPartialTick, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        super.render(pBlockEntity, pPartialTick, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);

        float ageInTicks = pBlockEntity.getAgeTicks() + pPartialTick;
        float pulse = (float) (Math.sin(ageInTicks * 0.1F) * 0.5F + 0.5F);

        if (pulse > 0.1F) {
            // Pegamos a direção do bloco (assumindo que existe a propriedade FACING)
            Direction facing = pBlockEntity.getBlockState().getValue(BlockStateProperties.FACING);
            renderCrystalBeams(pPoseStack, pBuffer, ageInTicks, pulse, facing);
        }
    }

    private void renderCrystalBeams(PoseStack poseStack, MultiBufferSource buffer, float ageInTicks, float pulse, Direction facing) {
        VertexConsumer builder = buffer.getBuffer(RenderType.lightning());

        poseStack.pushPose();

        // Centraliza e aplica o LEVE offset na direção do "facing"
        // 0.05D é um valor pequeno, ajuste conforme necessário
        double offsetX = 0.5D + (facing.getStepX() * -0.25D);
        double offsetY = 0.5D + (facing.getStepY() * -0.25D);
        double offsetZ = 0.5D + (facing.getStepZ() * -0.25D);

        poseStack.translate(offsetX, offsetY, offsetZ);

        Random random = new Random(432L);
        int rayCount = 8;

        for (int i = 0; i < rayCount; i++) {
            poseStack.pushPose();

            poseStack.mulPose(Axis.XP.rotationDegrees(random.nextFloat() * 360.0F + ageInTicks * 2.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360.0F + ageInTicks * 3.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 360.0F + ageInTicks * 1.5F));

            // Comprimento levemente menor para ficarem "mais perto do centro"
            float length = 0.3F + random.nextFloat() * 0.4F * pulse;
            float width = 0.04F * pulse;

            // COR: Vermelho (R: 255, G: 0, B: 0)
            int r = 255, g = 20, b = 20, a = (int) (200 * pulse);

            Matrix4f matrix = poseStack.last().pose();
            renderRayPart(builder, matrix, length, width, r, g, b, a);

            poseStack.popPose();
        }

        poseStack.popPose();
    }

    private void renderRayPart(VertexConsumer builder, Matrix4f matrix, float length, float width, int r, int g, int b, int a) {
        // O alpha (a) 0 nas pontas cria o efeito de fade-out
        builder.vertex(matrix, 0, 0, 0).color(r, g, b, a).endVertex();
        builder.vertex(matrix, -width, length, -width).color(r, g, b, 0).endVertex();
        builder.vertex(matrix, width, length, -width).color(r, g, b, 0).endVertex();

        builder.vertex(matrix, 0, 0, 0).color(r, g, b, a).endVertex();
        builder.vertex(matrix, width, length, -width).color(r, g, b, 0).endVertex();
        builder.vertex(matrix, 0, length, width).color(r, g, b, 0).endVertex();

        builder.vertex(matrix, 0, 0, 0).color(r, g, b, a).endVertex();
        builder.vertex(matrix, 0, length, width).color(r, g, b, 0).endVertex();
        builder.vertex(matrix, -width, length, -width).color(r, g, b, 0).endVertex();
    }
}