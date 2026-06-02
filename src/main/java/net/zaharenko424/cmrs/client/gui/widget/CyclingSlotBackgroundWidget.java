package net.zaharenko424.cmrs.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class CyclingSlotBackgroundWidget extends Widget {
    private static final int ICON_CHANGE_TICK_RATE = 30;
    private static final int ICON_SIZE = 16;
    private static final int ICON_TRANSITION_TICK_DURATION = 4;

    private Vec2 screenPos = Vec2.ZERO;
    private final AbstractContainerMenu menu;
    private final int slotIndex;
    private List<ResourceLocation> icons;
    private int tick;
    private int iconIndex;

    public CyclingSlotBackgroundWidget(AbstractContainerMenu menu, int pSlotIndex, List<ResourceLocation> icons) {
        super();
        this.menu = menu;
        this.slotIndex = pSlotIndex;
        this.icons = icons;
        moveToSlotPos(slotIndex);
        this.setOrigin(0, 0, 0);
    }

    public List<ResourceLocation> getIcons() {
        return icons;
    }

    public void setAndResetIconsList(List<ResourceLocation> icons) {
        this.icons = icons;
        this.resetIconsList();
    }

    public void resetIconsList() {
        this.iconIndex = 0;
        this.tick = 0;
    }

    public void tick() {
        if (!this.icons.isEmpty() && ++this.tick % ICON_CHANGE_TICK_RATE == 0) {
            this.iconIndex = (this.iconIndex + 1) % this.icons.size();
        }
    }

    public CyclingSlotBackgroundWidget setScreenPos(Vec2 screenPos) {
        this.screenPos = screenPos;
        return this;
    }

    public CyclingSlotBackgroundWidget setIcons(List<ResourceLocation> icons) {
        this.icons = icons;
        return this;
    }

    public CyclingSlotBackgroundWidget setOrigin(float x, float y, float z) {
        super.setOrigin(x, y, z);
        return this;
    }

    public CyclingSlotBackgroundWidget moveToSlotPos(Slot slot) {
        this.setOrigin(slot.x, slot.y, this.getOrigin().z);
        return this;
    }

    public CyclingSlotBackgroundWidget moveToSlotPos(int slotIndex) {
        return moveToSlotPos(this.menu.getSlot(slotIndex));
    }

    private void renderIcon(Slot pSlot, ResourceLocation pIcon, float pAlpha, GuiGraphics pGuiGraphics, int pX, int pY) {
        if (pAlpha <= 0.0F) return;

        RenderSystem.enableBlend();
        pGuiGraphics.setColor(1f, 1f, 1f, pAlpha);

        pGuiGraphics.blit(pIcon, pX + pSlot.x, pY + pSlot.y, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);

        pGuiGraphics.setColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();
    }

    private float getIconTransitionTransparency(float pPartialTick) {
        // FIXED: Smoothed out transition calculation based on the duration window
        float timePassed = (float) (this.tick % ICON_CHANGE_TICK_RATE) + pPartialTick;
        return Math.min(timePassed, (float) ICON_TRANSITION_TICK_DURATION) / (float) ICON_TRANSITION_TICK_DURATION;
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        int pX = (int) this.screenPos.x;
        int pY = (int) this.screenPos.y;

        Slot slot = menu.getSlot(this.slotIndex);
        if (!this.icons.isEmpty() && !slot.hasItem()) {
            // Only transition if there are multiple icons and at least one cycle occurred
            boolean shouldTransition = this.icons.size() > 1 && this.tick >= ICON_CHANGE_TICK_RATE;
            float alpha = shouldTransition ? this.getIconTransitionTransparency(pPartialTick) : 1.0F;

            // If the current icon is fading in, we must fade out the previous icon
            if (alpha < 1.0F) {
                int previousIndex = Math.floorMod(this.iconIndex - 1, this.icons.size());
                this.renderIcon(slot, this.icons.get(previousIndex), 1.0F - alpha, pGuiGraphics, pX, pY);
            }

            this.renderIcon(slot, this.icons.get(this.iconIndex), alpha, pGuiGraphics, pX, pY);
        }
    }
}