package net.foxyas.changedaddon.client.renderer.blockEntitys;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.block.entity.AggedBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleAggedBlockEntityRenderer<T extends AggedBlockEntity> implements BlockEntityRenderer<T> {

    protected final BlockRenderDispatcher blockRenderDispatcher;

    public SimpleAggedBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderDispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(@NotNull T pBlockEntity, float pPartialTick, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
    }
}
