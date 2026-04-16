package net.foxyas.changedaddon.client.renderer.blockEntitys;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.block.entity.PulseCrystalBlockEntity;
import net.foxyas.changedaddon.client.renderer.renderTypes.ChangedAddonRenderTypes;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

public abstract class PulseCrystalBlockEntityRenderer<T extends PulseCrystalBlockEntity> implements BlockEntityRenderer<T> {

    protected final BlockRenderDispatcher blockRenderDispatcher;

    public PulseCrystalBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderDispatcher = context.getBlockRenderDispatcher();
    }

    protected float getPulseStrength(T blockEntity, float partialTicks) {
        if (blockEntity.getLevel() == null) return 0;
        BlockState state = blockEntity.getBlockState();

        // 1. Cálculo do tempo e alpha baseados no pulseTicks
        float totalTicks = blockEntity.pulseTicks + partialTicks;
        float time = totalTicks * 0.1f;
        return 0.5f + (float) Math.sin(time) * 0.5f;
    }

    @Override
    public void render(@NotNull T pBlockEntity, float pPartialTick, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        if (pBlockEntity.getLevel() == null) return;
        BlockState state = pBlockEntity.getBlockState();
        BakedModel model = blockRenderDispatcher.getBlockModel(state);
        if (model == null || blockRenderDispatcher.getBlockModelShaper().getModelManager().getMissingModel() == model) {
            return;
        }
        float totalTicks = pBlockEntity.pulseTicks + pPartialTick;
        float time = totalTicks * 0.1f;
        float alpha = 0.5f + (float) Math.sin(time) * 0.5f;

        pPoseStack.pushPose(); // Inicia isolamento para esta parte específica
        pPoseStack.scale(1.01f, 1f, 1.01f);

        RenderType pRenderType = ChangedAddonRenderTypes.glowDynamic(InventoryMenu.BLOCK_ATLAS, alpha);

        this.blockRenderDispatcher.getModelRenderer().renderModel(
                pPoseStack.last(),
                pBuffer.getBuffer(pRenderType),
                state,
                model,
                1.0f, 1.0f, 1.0f,
                LightTexture.FULL_BRIGHT,
                pPackedOverlay,
                ModelData.EMPTY,
                pRenderType
        );

        pPoseStack.popPose(); // Finaliza isolamento para não afetar o próximo modelo
    }
}
