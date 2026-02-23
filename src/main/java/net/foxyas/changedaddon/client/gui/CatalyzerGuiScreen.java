package net.foxyas.changedaddon.client.gui;

import net.foxyas.changedaddon.block.entity.CatalyzerBlockEntity;
import net.foxyas.changedaddon.menu.CatalyzerGuiMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static net.foxyas.changedaddon.client.gui.UnifuserGuiScreen.getMachineState;
import static net.foxyas.changedaddon.client.gui.UnifuserGuiScreen.getRecipeState;

public class CatalyzerGuiScreen extends AbstractContainerScreen<CatalyzerGuiMenu> {

    private static final ResourceLocation texture = ResourceLocation.parse("changed_addon:textures/screens/catalyzer_gui_new.png");

    private final Level level;
    private final CatalyzerGuiMenu menu;
    private final CatalyzerBlockEntity catalyzer;
    private final BlockPos pos;

    public CatalyzerGuiScreen(CatalyzerGuiMenu container, Inventory inventory, Component text) {
        super(container, inventory, text);
        this.level = container.level;
        menu = container;
        pos = menu.getBlockPos();
        catalyzer = menu.getCatalyzer();
        this.imageWidth = 200;
        this.imageHeight = 170;
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(pGuiGraphics, mouseX, mouseY);
        if (menu.isSlotEmpty(36))
            if (mouseX > leftPos + 18 && mouseX < leftPos + 42 && mouseY > topPos + 40 && mouseY < topPos + 64)
                pGuiGraphics.renderTooltip(font, Component.translatable("gui.changed_addon.catalyzer_gui.tooltip_put_the_powders_or_syringe"), mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
        guiGraphics.setColor(1, 1, 1, 1);
        guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);

        ResourceLocation texture2 = ResourceLocation.parse("changed_addon:textures/screens/empty_bar.png");
        guiGraphics.blit(texture2, this.leftPos + 83, this.topPos + 46, 0, 0, 32, 12, 32, 12);

        int progressInt = (int) (catalyzer.recipeProgress / 3.57);

        ResourceLocation texture3 = ResourceLocation.parse("changed_addon:textures/screens/bar_full.png");
        guiGraphics.blit(texture3, this.leftPos + 83 + 2, this.topPos + 46 + 2, 0, 0, progressInt, 8, progressInt, 8);

        if (catalyzer.getItem(0).isEmpty()) {
            assert this.minecraft != null;
            assert this.minecraft.level != null;
            long gameTime = this.minecraft.level.getGameTime();
            int animationPeriod = 40; // ticks (2 segundos)
            boolean showSyringe = (gameTime % animationPeriod) < (animationPeriod / 2);

            ResourceLocation icon = showSyringe
                    ? ResourceLocation.parse("changed_addon:textures/screens/syringes.png")
                    : ResourceLocation.parse("changed_addon:textures/screens/dusts.png");

            int yOffset = showSyringe ? 44 : 45;

            guiGraphics.blit(icon, this.leftPos + 23, this.topPos + yOffset, 0, 0, 16, 16, 16, 16);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int mouseX, int mouseY) {
        pGuiGraphics.drawString(font, "Nitrogen % = " + catalyzer.nitrogenPower + "%", 6, 8, -12829636, false);
        pGuiGraphics.drawString(font,
                getMachineState(level, pos), 6, 20, -12829636, false);
        if (catalyzer.isSlotFull(1))
            pGuiGraphics.drawString(font, Component.translatable("gui.changed_addon.catalyzer_gui.label_full"), 151, 65, -12829636, false);
        pGuiGraphics.drawString(font,
                getRecipeState(level, pos), 90, 34, -12829636, false);
    }
}
