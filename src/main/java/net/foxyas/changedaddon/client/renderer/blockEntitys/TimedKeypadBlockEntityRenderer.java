package net.foxyas.changedaddon.client.renderer.blockEntitys;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.foxyas.changedaddon.block.advanced.TimedKeypadBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TimedKeypadBlockEntityRenderer implements BlockEntityRenderer<TimedKeypadBlockEntity> {

    private final BlockEntityRendererProvider.Context context;

    public TimedKeypadBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    public void render(TimedKeypadBlockEntity blockEntity, float partialTick, PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int light, int overlay) {
        BlockState state = blockEntity.getBlockState();

        poseStack.pushPose();

        if (state.hasProperty(HorizontalDirectionalBlock.FACING)) {
            Direction direction = state.getValue(HorizontalDirectionalBlock.FACING);
            switch (direction) {
                case NORTH -> {
                    poseStack.translate(0.06f, 0.51, 0.85f);
                    poseStack.mulPose(Vector3f.YP.rotationDegrees(-90));
                }
                case SOUTH -> {
                    poseStack.translate(0.94f, 0.51f, 0.15f);
                    poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
                }
                case EAST -> {
                    poseStack.translate(0.15f, 0.51f, 0.06f);
                    poseStack.mulPose(Vector3f.YP.rotationDegrees(-180));
                }
                case WEST -> {
                    poseStack.translate(0.85f, 0.51f, 0.94);
                    poseStack.mulPose(Vector3f.YP.rotationDegrees(0));
                }
            }
        }

        // Renderiza o texto
        String text = "" + blockEntity.getTimer();
        var font = Minecraft.getInstance().font;

        // Ajusta a escala com base na quantidade de dígitos
        int length = text.length();
        float baseScale = 0.01f;
        float scale = baseScale;

        if (length >= 4) {
            scale *= 1 - (1f / length); // Acha mais se for 4+ dígitos
            poseStack.translate(0f, -0.005, 0f);
        } else if (length == 1) {
            scale *= 1.2f; // Aumenta para apenas 1 dígito
        }

        poseStack.pushPose();
        //poseStack.translate(0, 0, -0.501);
        poseStack.scale(scale, -scale, scale);

        font.drawInBatch(
                text,
                -font.width(text) / 2f, 0,
                0xFFFFFF,
                false,
                poseStack.last().pose(),
                bufferSource,
                false,
                0,
                light
        );
        poseStack.popPose();

        poseStack.popPose();
    }


    public BlockEntityRendererProvider.Context getContext() {
        return context;
    }
}
