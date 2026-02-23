package net.foxyas.changedaddon.client.gui;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.menu.PrototypeMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class PrototypeMenuScreen extends AbstractContainerScreen<PrototypeMenu> {

    private static final ResourceLocation TEXTURE = ChangedAddonMod.textureLoc("textures/screens/prototype_menu");

    public PrototypeMenuScreen(PrototypeMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(pGuiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(pGuiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics pGuiGraphics, int mouseX, int mouseY) {
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        pGuiGraphics.blit(TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);
        InventoryScreen.renderEntityInInventoryFollowsMouse(pGuiGraphics, i + 51, j + 75, 30, (float) (i + 51) - mouseX, (float) (j + 75 - 50) - mouseY, menu.getEntity());
    }
}
