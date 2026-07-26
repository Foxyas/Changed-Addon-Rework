package net.foxyas.changedaddon.entity.api;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;

public interface IDynamicInventoryRender {

    @OnlyIn(Dist.CLIENT)
    default void onRenderOnInventory(GuiGraphics pGuiGraphics, int pX, int pY, int pScale, Quaternionf pPose, Quaternionf pCameraOrientation, Entity entity) {
    }

    @OnlyIn(Dist.CLIENT)
    default Vec3 getInventoryRenderScale() {
        return new Vec3(1, 1, 1);
    }
}
