package net.foxyas.changedaddon.client.gui;

import net.foxyas.changedaddon.block.entity.CatalyzerBlockEntity;
import net.foxyas.changedaddon.menu.CatalyzerGuiMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.items.SlotItemHandler;
import net.zaharenko424.cmrs.client.gui.widget.CyclingSlotBackgroundWidget;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

import static net.foxyas.changedaddon.client.gui.UnifuserGuiScreen.getMachineState;
import static net.foxyas.changedaddon.client.gui.util.IconsUtils.DUST_ICON_ITEM;
import static net.foxyas.changedaddon.client.gui.util.IconsUtils.SYRINGE_ICON_ITEM;

public class CatalyzerGuiScreen extends AbstractContainerScreen<CatalyzerGuiMenu> {

    private static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.parse("changed_addon:textures/screens/containers/catalyzer_gui.png");
    public static final List<ResourceLocation> EMPTY_ICONS = List.of(SYRINGE_ICON_ITEM, DUST_ICON_ITEM);

    // Widgets
    public final CyclingSlotBackgroundWidget cyclingSlot0BackgroundWidget;

    // Variables
    private final Level level;
    private final CatalyzerGuiMenu menu;
    private final CatalyzerBlockEntity catalyzer;
    private final BlockPos pos;

    public CatalyzerGuiScreen(CatalyzerGuiMenu container, Inventory inventory, Component text) {
        super(container, inventory, text);
        this.level = container.level;
        this.menu = container;
        this.pos = menu.getBlockPos();
        this.catalyzer = menu.getCatalyzer();
        this.imageWidth = 175;
        this.imageHeight = 166;

        this.cyclingSlot0BackgroundWidget = new CyclingSlotBackgroundWidget(menu, menu.getLeftSlot().index, EMPTY_ICONS);
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(pGuiGraphics, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        cyclingSlot0BackgroundWidget.setScreenPos(new Vec2(leftPos, topPos));
        addRenderableWidget(cyclingSlot0BackgroundWidget);
    }

    @Override
    protected void renderTooltip(@NotNull GuiGraphics pGuiGraphics, int pX, int pY) {
        super.renderTooltip(pGuiGraphics, pX, pY);
        boolean carriedItemIsEmptyOrIsNotSelected = this.menu.getCarried().isEmpty() || (hoveredSlot != null && !this.menu.getCarried().equals(hoveredSlot.getItem()));
        if (carriedItemIsEmptyOrIsNotSelected && this.hoveredSlot != null && this.hoveredSlot == menu.getLeftSlot() && !this.hoveredSlot.hasItem()) {
            ItemStack itemstack = this.hoveredSlot.getItem();
            pGuiGraphics.renderTooltip(this.font, List.of(Component.translatable("gui.changed_addon.catalyzer_gui.tooltip_put_the_powders_or_syringe")), itemstack.getTooltipImage(), itemstack, pX, pY);
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        cyclingSlot0BackgroundWidget.tick();
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
        guiGraphics.setColor(1, 1, 1, 1);
        guiGraphics.blit(BACKGROUND_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth + 30, this.imageHeight);

        int guiIconsUOffset = this.imageWidth + 1;
        if (catalyzer.recipeProgress > 0) {
            int recipeProgress = Mth.clamp((int) (28 * (catalyzer.recipeProgress / 100)), 0, 100);
            guiGraphics.blit(BACKGROUND_TEXTURE, this.leftPos + 76, this.topPos + 46, guiIconsUOffset, 0, recipeProgress, 11, this.imageWidth + 30, this.imageHeight);
        }

        if (catalyzer.nitrogenPower > 0) {
            int nitrogenProgress = Mth.clamp((int) (18 * (catalyzer.nitrogenPower / 200)), 0, 100);
            guiGraphics.blit(BACKGROUND_TEXTURE, this.leftPos + 79, this.topPos + 35, guiIconsUOffset, 12, nitrogenProgress, 4, this.imageWidth + 30, this.imageHeight);
        }

        // Slot in: 165x, 5y
        // Colors in:
        // (Green) 176x, 17y
        // (Red) 181x, 17y
        int machineState = catalyzer.startRecipe ? 0 : 5;
        guiGraphics.blit(BACKGROUND_TEXTURE, this.leftPos + 165, this.topPos + 5, guiIconsUOffset + machineState, 16, 5, 5, this.imageWidth + 30, this.imageHeight);

//        Slots Index debug Code. is useful
//        for (Slot slot : menu.slots) {
//            guiGraphics.drawString(font, "" + slot.index, leftPos + slot.x, topPos + slot.y, Color.WHITE.getRGB(), false);
//        }
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics pGuiGraphics, int mouseX, int mouseY) {
        super.renderLabels(pGuiGraphics, mouseX, mouseY);
//        pGuiGraphics.drawString(font, getMachineState(level, pos), titleLabelX, titleLabelY + 10, -12829636, false);
        if (catalyzer.isSlotFull(1)) {
            SlotItemHandler rightSlot = (SlotItemHandler) menu.getOutputSlot();
            pGuiGraphics.drawString(font, Component.translatable("gui.changed_addon.catalyzer_gui.label_full"), rightSlot.x, rightSlot.y - 10, -12829636, false);
        }
    }
}
