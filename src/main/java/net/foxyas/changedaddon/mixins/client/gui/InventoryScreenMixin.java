package net.foxyas.changedaddon.mixins.client.gui;

import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.entity.api.IDynamicInventoryRender;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin {

    @Inject(method = "renderEntityInInventory", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;runAsFancy(Ljava/lang/Runnable;)V", shift = At.Shift.BY))
    private static void scaleDownHook(GuiGraphics pGuiGraphics, int pX, int pY, int pScale, Quaternionf pPose, Quaternionf pCameraOrientation, LivingEntity pEntity, CallbackInfo ci) {
        Entity entity = resolveChangedEntity(pEntity);
        if (entity instanceof IAlphaAbleEntity iAlphaAbleEntity && iAlphaAbleEntity.isAlpha()) {
            float reduction = (1 / iAlphaAbleEntity.alphaScaleForRender());
            pGuiGraphics.pose().scale(reduction, reduction, reduction);
        }

        if (entity instanceof IDynamicInventoryRender inventoryRender) {
            Vec3 inventoryRenderScale = inventoryRender.getInventoryRenderScale();
            pGuiGraphics.pose().scale(
                    (float) inventoryRenderScale.x(),
                    (float) inventoryRenderScale.y(),
                    (float) inventoryRenderScale.z()
            );
            inventoryRender.onRenderOnInventory(pGuiGraphics, pX, pY, pScale, pPose, pCameraOrientation, entity);
        }
    }

    private static Entity resolveChangedEntity(Entity entity) {
        if (entity instanceof Player player) {
            TransfurVariantInstance<?> transfur = ProcessTransfur.getPlayerTransfurVariant(player);
            if (transfur != null) {
                return transfur.getChangedEntity();
            }
        }
        return entity;
    }
}
