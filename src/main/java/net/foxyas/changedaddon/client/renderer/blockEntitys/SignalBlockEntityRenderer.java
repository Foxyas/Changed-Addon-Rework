package net.foxyas.changedaddon.client.renderer.blockEntitys;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.block.entity.SignalBlockEntity;
import net.foxyas.changedaddon.effect.particles.SignalParticle;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.init.ChangedAddonParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class SignalBlockEntityRenderer implements BlockEntityRenderer<SignalBlockEntity> {

    @OnlyIn(Dist.CLIENT)
    private SignalParticle signalParticle;

    public SignalBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    public void render(SignalBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int light, int overlay) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        // Partícula só se estiver segurando o item
        Item pItem = ChangedAddonItems.SIGNAL_CATCHER.get();
        boolean holding = player.getMainHandItem().is(pItem) || player.getOffhandItem().is(pItem);
        if (signalParticle == null) {
            if (!holding) {
                return;
            }
            BlockPos pos = blockEntity.getBlockPos();
            Particle particle = Minecraft.getInstance().particleEngine.createParticle(
                    ChangedAddonParticleTypes.signal(8, new ItemStack(pItem)),
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0, 0);
            if (particle instanceof SignalParticle sp) signalParticle = sp;
        } else if (!signalParticle.isAlive() || signalParticle.getAge() >= signalParticle.getLifetime()) {
            this.signalParticle = null;
        }
    }

    @Override
    public boolean shouldRenderOffScreen(SignalBlockEntity pBlockEntity) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 512;
    }
}
