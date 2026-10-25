package net.foxyas.changedaddon.client.renderer.blockEntitys;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.block.entity.AbstractPlushyBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.RenderTypeHelper;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

public class PlushyBlockEntityRenderer<T extends AbstractPlushyBlockEntity> implements BlockEntityRenderer<T> {
    private final BlockRenderDispatcher dispatcher;

    public PlushyBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.dispatcher = ctx.getBlockRenderDispatcher();
    }

    @Override
    public void render(T be, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int light, int overlay) {
        Level level = be.getLevel();
        if (level == null) return;

        // 0 -> 1 -> 0 over the squeak, smoothed
        float p = be.getSqueakController().getSqueezeProgress(partialTick);
        p = p * p * (3f - 2f * p);

        float scaleY = 1f - 0.25f * p;  // squash down
        float scaleXZ = 1f + 0.12f * p; // bulge out

        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);           // pivot: bottom center
        poseStack.scale(scaleXZ, scaleY, scaleXZ);
        poseStack.translate(-0.5, 0, -0.5);
        renderModel(be, partialTick, poseStack, buffer, light, overlay);
        poseStack.popPose();
    }

    protected void renderModel(T be, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int light, int overlay) {
        Level level = be.getLevel();
        if (level == null) return;

        BlockState state = be.getBlockState();
        BlockPos pos = be.getBlockPos();
        BakedModel model = dispatcher.getBlockModel(state);
        RandomSource rand = RandomSource.create();
        long seed = state.getSeed(pos);

        for (RenderType renderType : model.getRenderTypes(state, rand, ModelData.EMPTY)) {
            dispatcher.getModelRenderer().tesselateBlock(
                    level, model, state, pos, poseStack,
                    buffer.getBuffer(RenderTypeHelper.getMovingBlockRenderType(renderType)),
                    false,          // checkSides: false = don't cull faces against neighbors
                    rand, seed, overlay,
                    ModelData.EMPTY, renderType);
        }
    }
}