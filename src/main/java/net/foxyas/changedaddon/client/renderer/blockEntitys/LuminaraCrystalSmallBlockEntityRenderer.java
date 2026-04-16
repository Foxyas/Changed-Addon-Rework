package net.foxyas.changedaddon.client.renderer.blockEntitys;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.block.entity.LuminarCrystalSmallBlockEntity;
import net.foxyas.changedaddon.client.renderer.renderTypes.ChangedAddonRenderTypes;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

public class LuminaraCrystalSmallBlockEntityRenderer extends PulseCrystalBlockEntityRenderer<LuminarCrystalSmallBlockEntity> {
    public LuminaraCrystalSmallBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(@NotNull LuminarCrystalSmallBlockEntity pBlockEntity, float pPartialTick, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        if (true) return;
        if (pBlockEntity.getLevel() == null) return;
        BlockState state = pBlockEntity.getBlockState();
        BakedModel model = blockRenderDispatcher.getBlockModel(state);
        if (model == null || blockRenderDispatcher.getBlockModelShaper().getModelManager().getMissingModel() == model) {
            return;
        }
        float totalTicks = pBlockEntity.pulseTicks + pPartialTick;
        float time = totalTicks * 0.1f;
        float alpha = 0.5f + (float) Math.sin(time) * 0.5f;

        pPoseStack.pushPose();
        pPoseStack.scale(1f, 1f, 1f);

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

        pPoseStack.popPose();
    }
}
