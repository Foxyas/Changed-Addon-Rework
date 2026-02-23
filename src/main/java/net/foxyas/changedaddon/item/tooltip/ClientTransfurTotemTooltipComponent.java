package net.foxyas.changedaddon.item.tooltip;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.foxyas.changedaddon.client.renderer.blockEntitys.InformantBlockEntityRenderer;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.entity.BasicPlayerInfo;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import javax.annotation.Nullable;

public class ClientTransfurTotemTooltipComponent implements ClientTooltipComponent {
    private final TransfurVariant<?> variant;
    private final ItemStack transfurTotemStack;
    private final ChangedEntity entity;

    public ClientTransfurTotemTooltipComponent(TransfurTotemTooltipComponent component) {
        this.variant = component.getVariant();
        this.entity = InformantBlockEntityRenderer.getDisplayEntity(variant);
        this.transfurTotemStack = component.getTransfurTotemStack();
    }

    public static void renderEntityInInventoryFollowsAngleSameRotation(GuiGraphics pGuiGraphics, float pX, float pY, float pScale, float angleXComponent, float angleYComponent, LivingEntity pEntity) {
        float f = angleXComponent;
        float f1 = angleYComponent;
        Quaternionf quaternionZ = (new Quaternionf()).rotateZ((float) Math.PI);
        Quaternionf quaternionX = (new Quaternionf()).rotateX(f1 * 20.0F * ((float) Math.PI / 180F));
        quaternionZ.mul(quaternionX);
        float f2 = pEntity.yBodyRot;
        float f22 = pEntity.yBodyRotO;
        float f3 = pEntity.getYRot();
        float f4 = pEntity.getXRot();
        float f5 = pEntity.yHeadRotO;
        float f6 = pEntity.yHeadRot;
        pEntity.yBodyRot = 180.0F + f * 20.0F;
        pEntity.yBodyRotO = pEntity.yBodyRot;
        pEntity.setYRot(180.0F + f * 20.0F);
        pEntity.setXRot(-f1 * 20.0F);
        pEntity.yHeadRot = pEntity.getYRot();
        pEntity.yHeadRotO = pEntity.getYRot();
        renderEntityInInventory(pGuiGraphics, pX, pY, pScale, quaternionZ, quaternionX, pEntity);
        pEntity.yBodyRot = f2;
        pEntity.yBodyRotO = f22;
        pEntity.setYRot(f3);
        pEntity.setXRot(f4);
        pEntity.yHeadRotO = f5;
        pEntity.yHeadRot = f6;
    }

    public static void renderEntityInInventory(GuiGraphics pGuiGraphics, float pX, float pY, float pScale, Quaternionf pPose, @Nullable Quaternionf pCameraOrientation, LivingEntity pEntity) {
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(pX, pY, 50.0D);
        pGuiGraphics.pose().mulPoseMatrix((new Matrix4f()).scaling(pScale, pScale, -pScale));
        pGuiGraphics.pose().mulPose(pPose);
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        if (pCameraOrientation != null) {
            pCameraOrientation.conjugate();
            entityrenderdispatcher.overrideCameraOrientation(pCameraOrientation);
        }

        entityrenderdispatcher.setRenderShadow(false);
        RenderSystem.runAsFancy(() -> {
            entityrenderdispatcher.render(pEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, pGuiGraphics.pose(), pGuiGraphics.bufferSource(), 15728880);
        });
        pGuiGraphics.flush();
        entityrenderdispatcher.setRenderShadow(true);
        pGuiGraphics.pose().popPose();
        Lighting.setupFor3DItems();
    }

    public static void oldRenderEntityInInventory(float posX, float posY, int scale,
                                                  float mouseX, float mouseY, @NotNull PoseStack poseStack, @NotNull LivingEntity livingEntity) {
        poseStack.pushPose();
        poseStack.translate(posX, posY, 3500.0D); // força ainda mais na frente
        poseStack.scale((float) scale, (float) scale, (float) scale);

        Quaternionf quaternion = Axis.ZP.rotationDegrees(180.0F);
        Quaternionf quaternion1 = Axis.XP.rotationDegrees(mouseY);
        Quaternionf quaternion2 = Axis.YP.rotationDegrees(mouseX);
        quaternion.mul(quaternion1);
        quaternion.mul(quaternion2);
        poseStack.mulPose(quaternion);

        float f2 = livingEntity.yBodyRot;
        float f3 = livingEntity.getYRot();
        float f4 = livingEntity.getXRot();
        float f5 = livingEntity.yHeadRotO;
        float f6 = livingEntity.yHeadRot;

        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion.conjugate();
        quaternion.mul(Axis.ZP.rotationDegrees(180.0F));

        dispatcher.overrideCameraOrientation(quaternion);
        dispatcher.setRenderShadow(false);

        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();

        //noinspection deprecation
        RenderSystem.runAsFancy(() -> dispatcher.render(
                livingEntity, 0.0D, 0.0D, 0.0D,
                0.0F, 1.0F, poseStack, buffer, 15728880));
        buffer.endBatch();

        dispatcher.setRenderShadow(true);
        livingEntity.yBodyRot = f2;
        livingEntity.setYRot(f3);
        livingEntity.setXRot(f4);
        livingEntity.yHeadRotO = f5;
        livingEntity.yHeadRot = f6;
        poseStack.popPose();
        Lighting.setupFor3DItems();
    }

    @Override
    public int getHeight() {
        return 80;
    }

    @Override
    public int getWidth(@NotNull Font font) {
        return 80;
    }

    public TransfurVariant<?> getVariant() {
        return variant;
    }

    public void prepareEntityForRender() {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            BasicPlayerInfo basicPlayerInfo = Changed.config.client.basicPlayerInfo;
            BasicPlayerInfo basicEntityInfo = this.entity.getBasicPlayerInfo();
            basicEntityInfo.copyFrom(basicPlayerInfo);

            this.entity.setCustomName(entity.getType().getDescription());
            this.entity.setCustomNameVisible(true);

            CompoundTag transfurTotemStackDataTag = this.transfurTotemStack.getOrCreateTag();
            if (transfurTotemStackDataTag.contains("TransfurVariantData")) {
                CompoundTag transfurVariantData = transfurTotemStackDataTag.getCompound("TransfurVariantData");
                this.entity.readAdditionalSaveData(transfurVariantData);
                if (transfurTotemStackDataTag.contains("entityData")) {
                    CompoundTag entityData = transfurVariantData.getCompound("entityData");
                    this.entity.readPlayerVariantData(entityData);
                }
            }
        }
    }

    @Override
    public void renderImage(@NotNull Font font, int posX, int posY, @NotNull GuiGraphics guiGraphics) {
        ClientTooltipComponent.super.renderImage(font, posX, posY, guiGraphics);
        if (entity == null)
            return;
        this.prepareEntityForRender();

        float bbMax = Math.max(entity.getBbWidth(), entity.getBbHeight());

        // Ajuste de scale baseado no tamanho do BB
        int baseScale = 30;
        int scale = (int) (baseScale / (bbMax * 0.5));

        // Ajuste de posição para centralizar
        float offsetX = posX + 45; // centro horizontal do tooltip
        float offsetY = posY + 75; // centro vertical do tooltip

        Minecraft mc = Minecraft.getInstance();

        // Rotação animada
        float time = mc.level != null
                ? mc.level.getGameTime() + mc.getFrameTime()
                : 0;

        float spin = (time * 0.25f) % 360;

        //float tickSpin = (mc.player != null ? mc.player.tickCount % 360 : 0);

        renderEntityInInventoryFollowsAngleSameRotation(guiGraphics, offsetX, offsetY, scale, spin, 0, entity);
    }

    @Override
    public void renderText(@NotNull Font font, int posX, int posY,
                           @NotNull Matrix4f matrix,
                           @NotNull MultiBufferSource.BufferSource bufferSource) {
        CompoundTag transfurTotemStackDataTag = this.transfurTotemStack.getTag();
        if (transfurTotemStackDataTag == null || transfurTotemStackDataTag.isEmpty()) return;

        CompoundTag transfurVariantData = transfurTotemStackDataTag.getCompound("TransfurVariantData");
        if (transfurVariantData.isEmpty()) return;

        CompoundTag entityData = transfurVariantData.getCompound("entityData");
        if (entityData.isEmpty() || !entityData.getBoolean("isAlpha")) return;

        font.drawInBatch("Alpha", posX, posY, -1, false, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, LightTexture.FULL_BRIGHT);
    }
}
