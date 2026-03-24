package net.foxyas.changedaddon.client.gui;

import net.foxyas.changedaddon.block.entity.CatalyzerBlockEntity;
import net.foxyas.changedaddon.menu.CatalyzerGuiMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.foxyas.changedaddon.client.gui.UnifuserGuiScreen.getMachineState;

public class CatalyzerGuiScreen extends AbstractContainerScreen<CatalyzerGuiMenu> {

    private static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.parse("changed_addon:textures/screens/catalyzer_gui_new.png");

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
        this.imageWidth = 175;
        this.imageHeight = 166;
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(pGuiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(@NotNull GuiGraphics pGuiGraphics, int pX, int pY) {
        super.renderTooltip(pGuiGraphics, pX, pY);
        if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot == menu.getLeftSlot() && !this.hoveredSlot.hasItem()) {
            ItemStack itemstack = this.hoveredSlot.getItem();
            pGuiGraphics.renderTooltip(this.font, List.of(Component.translatable("gui.changed_addon.catalyzer_gui.tooltip_put_the_powders_or_syringe")), itemstack.getTooltipImage(), itemstack, pX, pY);
        }
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
        guiGraphics.setColor(1, 1, 1, 1);
        guiGraphics.blit(BACKGROUND_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth + 30, this.imageHeight);

        if (catalyzer.recipeProgress > 0) {
            int recipeProgress = (int) (28 * (catalyzer.recipeProgress / 100));
            guiGraphics.blit(BACKGROUND_TEXTURE, this.leftPos + 76, this.topPos + 46, this.imageWidth + 1, 0, recipeProgress, 11, this.imageWidth + 30, this.imageHeight);
        }

        if (catalyzer.nitrogenPower > 0) {
            int nitrogenProgress = (int) (18 * (catalyzer.nitrogenPower / 200));
            guiGraphics.blit(BACKGROUND_TEXTURE, this.leftPos + 79, this.topPos + 35, this.imageWidth + 1, 12, nitrogenProgress, 4, this.imageWidth + 30, this.imageHeight);
        }

        if (catalyzer.getItem(0).isEmpty()) {
            assert this.minecraft != null;
            assert this.minecraft.level != null;
            long gameTime = this.minecraft.level.getGameTime();
            int animationPeriod = 40; // ticks (2 segundos)
            boolean showingSyringe = (gameTime % animationPeriod) < (animationPeriod / 2);

            ResourceLocation icon = showingSyringe
                    ? ResourceLocation.parse("changed_addon:textures/screens/syringes.png")
                    : ResourceLocation.parse("changed_addon:textures/screens/dusts.png");

            int yOffset = showingSyringe ? 0 : 1;

            SlotItemHandler leftSlot = menu.getLeftSlot();
            guiGraphics.blit(icon, leftPos + leftSlot.x, topPos + leftSlot.y + yOffset, 0, 0, 16, 16, 16, 16);
        }
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics pGuiGraphics, int mouseX, int mouseY) {
        super.renderLabels(pGuiGraphics, mouseX, mouseY);
        pGuiGraphics.drawString(font, getMachineState(level, pos), titleLabelX, titleLabelY + 10, -12829636, false);
        if (catalyzer.isSlotFull(1)) {
            SlotItemHandler rightSlot = menu.getOutputSlot();
            pGuiGraphics.drawString(font, Component.translatable("gui.changed_addon.catalyzer_gui.label_full"), rightSlot.x, rightSlot.y - 10, -12829636, false);
        }
        //pGuiGraphics.drawString(font, getRecipeState(level, pos), 90, 34, -12829636, false);
    }
}
