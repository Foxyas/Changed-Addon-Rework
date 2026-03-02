package net.foxyas.changedaddon.mixins.mods.vivecraft;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.extension.RequiredMods;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.vivecraft.api.client.data.RenderPass;
import org.vivecraft.client.ClientVRPlayers;
import org.vivecraft.client_vr.ClientDataHolderVR;

@Mixin(value = AdvancedHumanoidRenderer.class, remap = false)
@RequiredMods("vivecraft")
public class AdvancedHumanoidRendererMixin<T extends ChangedEntity> {

    // Copied From VRPlayerRenderer$setupRotations
    @WrapMethod(method = "setupRotations(Lnet/ltxprogrammer/changed/entity/ChangedEntity;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V")
    private void hook(@NotNull T entity, PoseStack poseStack, float bob, float rotationYaw, float partialTicks, Operation<Void> original) {
        if (entity.getUnderlyingPlayer() instanceof AbstractClientPlayer player) {
            if (ClientDataHolderVR.getInstance().currentPass != RenderPass.GUI && ClientVRPlayers.getInstance().isVRPlayer(player)) {
                if (player == Minecraft.getInstance().player) {
                    rotationYaw = ClientDataHolderVR.getInstance().vrPlayer.getVRDataWorld().getBodyYaw();
                } else {
                    ClientVRPlayers.RotInfo rotInfo = ClientVRPlayers.getInstance().getRotationsForPlayer(player.getUUID());
                    rotationYaw = (180F / (float)Math.PI) * rotInfo.getBodyYawRad();
                }
            }

        }

        original.call(entity, poseStack, bob, rotationYaw, partialTicks);
    }
}
