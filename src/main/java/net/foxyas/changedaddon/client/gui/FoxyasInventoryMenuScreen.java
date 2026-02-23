package net.foxyas.changedaddon.client.gui;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.menu.FoxyasInventoryMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class FoxyasInventoryMenuScreen extends AbstractContainerScreen<FoxyasInventoryMenu> {

    private static final ResourceLocation TEXTURE = ChangedAddonMod.textureLoc("textures/screens/foxyas_menu");
    private static final ResourceLocation TEXTURE_INV = ChangedAddonMod.textureLoc("textures/screens/foxyas_menu_inventory");

    public FoxyasInventoryMenuScreen(FoxyasInventoryMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(pGuiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics pGuiGraphics, int mouseX, int mouseY) {
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = this.leftPos + FoxyasInventoryMenu.X_OFFSET;
        int j = this.topPos + FoxyasInventoryMenu.Y_OFFSET;

        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        pGuiGraphics.blit(TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);
        InventoryScreen.renderEntityInInventoryFollowsMouse(pGuiGraphics, i + 51, j + 75, 30, (float) (i + 51) - mouseX, (float) (j + 75 - 50) - mouseY, menu.getEntity());


        int ExtraInvVHeight = 81;
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        pGuiGraphics.blit(TEXTURE_INV, i + 176, j + 2, 0, 0, this.imageWidth, ExtraInvVHeight);

    }

    public int getLeftPos() {
        return getGuiLeft() + FoxyasInventoryMenu.X_OFFSET;
    }

    public int getTopPos() {
        return getGuiTop() + FoxyasInventoryMenu.Y_OFFSET;
    }
}
