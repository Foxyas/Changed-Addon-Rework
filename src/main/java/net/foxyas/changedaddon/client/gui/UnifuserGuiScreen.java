package net.foxyas.changedaddon.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.foxyas.changedaddon.block.entity.CatalyzerBlockEntity;
import net.foxyas.changedaddon.block.entity.UnifuserBlockEntity;
import net.foxyas.changedaddon.menu.UnifuserGuiMenu;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.items.SlotItemHandler;
import net.zaharenko424.cmrs.client.gui.widget.CyclingSlotBackgroundWidget;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.foxyas.changedaddon.client.gui.util.IconsUtils.DUST_ICON_ITEM;
import static net.foxyas.changedaddon.client.gui.util.IconsUtils.SYRINGE_ICON_ITEM;

public class UnifuserGuiScreen extends AbstractContainerScreen<UnifuserGuiMenu> {

    private static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.parse("changed_addon:textures/screens/containers/unifuser_gui.png");
    public static final List<ResourceLocation> SYRINGE_ICONS = List.of(SYRINGE_ICON_ITEM);
    public static final List<ResourceLocation> EMPTY_ICONS = List.of(SYRINGE_ICON_ITEM, DUST_ICON_ITEM);

    // Widgets
    public final CyclingSlotBackgroundWidget cyclingSlot0BackgroundWidget;
    public final CyclingSlotBackgroundWidget cyclingSlot1BackgroundWidget;
    public final CyclingSlotBackgroundWidget cyclingSlot2BackgroundWidget;
    public final List<CyclingSlotBackgroundWidget> cyclingSlotBackgroundWidgets = new ArrayList<>();
    public ImageButton powerButton;

    // Variables
    private final UnifuserGuiMenu menu;
    private final UnifuserBlockEntity unifuser;
    private final Level level;
    private final BlockPos pos;

    public UnifuserGuiScreen(UnifuserGuiMenu container, Inventory inventory, Component text) {
        super(container, inventory, text);
        this.menu = container;
        this.unifuser = menu.getUnifuser();
        this.level = container.level;
        this.pos = menu.getBlockPos();
        this.imageWidth = 175;
        this.imageHeight = 166;

        this.cyclingSlot0BackgroundWidget = new CyclingSlotBackgroundWidget(menu, menu.getTopSlot().index, EMPTY_ICONS);
        this.cyclingSlot1BackgroundWidget = new CyclingSlotBackgroundWidget(menu, menu.getBottomSlot().index, EMPTY_ICONS);
        this.cyclingSlot2BackgroundWidget = new CyclingSlotBackgroundWidget(menu, menu.getSyringeSlot().index, EMPTY_ICONS);
        cyclingSlotBackgroundWidgets.addAll(List.of(cyclingSlot0BackgroundWidget, cyclingSlot1BackgroundWidget, cyclingSlot2BackgroundWidget));
    }

    @Override
    protected void init() {
        super.init();
        cyclingSlotBackgroundWidgets.forEach(widget -> widget.setScreenPos(new Vec2(leftPos, topPos)));
        cyclingSlotBackgroundWidgets.forEach(this::addRenderableWidget);

        // The position of your button relative to the GUI
        int buttonX = this.leftPos + 165;
        int buttonY = this.topPos + 5;
        int buttonWidth = 5;
        int buttonHeight = 5;

        // Texture offsets (UV coordinates)
        int guiIconsUOffset = this.imageWidth + 1;
        int vOffset = 17; // The Y position of the icon in your texture
        int yDiffTex = 0; // How many pixels to scroll down for the hovered state texture

        this.powerButton = new ImageButton(
                buttonX, buttonY,
                buttonWidth, buttonHeight,
                guiIconsUOffset, vOffset,
                yDiffTex, // If hovered, it will shift down by 5 pixels on the V axis
                BACKGROUND_TEXTURE,
                this.imageWidth + 30, this.imageHeight, // Total texture width and height
                (button) -> {
                    assert this.minecraft != null;
                    assert this.minecraft.gameMode != null;
                    this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, UnifuserGuiMenu.POWER_BUTTON_ID);
                }
        ) {
            @Override
            public void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
                int guiIconsUOffset = UnifuserGuiScreen.this.imageWidth + 1;
                // If startRecipe is true, use U offset (Green/On), else shift by 5 pixels (Red/Off)
                int machineStateU = unifuser.startRecipe ? guiIconsUOffset : guiIconsUOffset + 5;

                this.renderTexture(pGuiGraphics, this.resourceLocation, this.getX(), this.getY(), machineStateU, this.yTexStart, this.yDiffTex, this.width, this.height, this.textureWidth, this.textureHeight);
            }
        };

        this.addRenderableWidget(this.powerButton);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        cyclingSlotBackgroundWidgets.forEach(CyclingSlotBackgroundWidget::tick);
    }

    public static Component getMachineState(Level level, BlockPos pos) {
        MutableComponent blockName = level.getBlockState(pos).getBlock().getName();

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof UnifuserBlockEntity unifuserBlockEntity) {
            return blockName.append(unifuserBlockEntity.startRecipe ? " is activated" : " is deactivated");
        } else if (blockEntity instanceof CatalyzerBlockEntity catalyzerBlockEntity) {
            return blockName.append(catalyzerBlockEntity.startRecipe ? " is activated" : " is deactivated");
        }
        return Component.empty();
    }

    public static Component getRecipeState(LevelAccessor level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity == null) return Component.empty();
        double number = 0;
        if (blockEntity instanceof UnifuserBlockEntity unifuserBlockEntity) {
            number = unifuserBlockEntity.recipeProgress;
        } else if (blockEntity instanceof CatalyzerBlockEntity catalyzerBlockEntity) {
            number = catalyzerBlockEntity.recipeProgress;
        }

        return Component.literal(Math.round(number) + "%");
    }

    @Override
    protected void renderTooltip(@NotNull GuiGraphics pGuiGraphics, int pX, int pY) {
        super.renderTooltip(pGuiGraphics, pX, pY);
        HashMap<Slot, List<Component>> slotToolTips = getSlotToolTips();
        boolean carriedItemIsEmptyOrIsNotSelected = this.menu.getCarried().isEmpty() || (hoveredSlot != null && !this.menu.getCarried().equals(hoveredSlot.getItem()));
        if (carriedItemIsEmptyOrIsNotSelected && this.hoveredSlot != null && !this.hoveredSlot.hasItem()) {
            for (List<Component> slotComponents : slotToolTips.entrySet().stream().filter(entry -> hoveredSlot == entry.getKey()).map(Map.Entry::getValue).toList()) {
                ItemStack itemstack = this.hoveredSlot.getItem();
                pGuiGraphics.renderTooltip(this.font, slotComponents, itemstack.getTooltipImage(), itemstack, pX, pY);
            }
        }
    }

    protected @NotNull HashMap<Slot, List<Component>> getSlotToolTips() {
        return Util.make(new HashMap<>(), map -> {
            map.putIfAbsent(menu.getTopSlot(), List.of(Component.translatable("gui.changed_addon.unifuser_gui.tooltip_place_the_powders")));
            map.putIfAbsent(menu.getBottomSlot(), List.of(Component.translatable("gui.changed_addon.unifuser_gui.tooltip_put_the_second_ingredient")));
            map.putIfAbsent(menu.getSyringeSlot(), List.of(Component.translatable("gui.changed_addon.unifuser_gui.tooltip_place_a_syringe_with_dna")));
        });
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(pGuiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
        guiGraphics.setColor(1, 1, 1, 1);
        guiGraphics.blit(BACKGROUND_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth + 30, this.imageHeight);

        int guiIconsUOffset = this.imageWidth + 1;
        if (unifuser.recipeProgress > 0) {
            int recipeProgress = (int) (28 * (unifuser.recipeProgress / 100));
            guiGraphics.blit(BACKGROUND_TEXTURE, this.leftPos + 75, this.topPos + 40, guiIconsUOffset, 0, recipeProgress, 11, this.imageWidth + 30, this.imageHeight);
        }

        // Slot in: 165x, 5y
        // Colors in:
        // (Green) 176x, 17y
        // (Red) 181x, 17y
        //int machineState = unifuser.startRecipe ? 0 : 5;
        //guiGraphics.blit(BACKGROUND_TEXTURE, this.leftPos + 165, this.topPos + 5, guiIconsUOffset + machineState, 17, 5, 5, this.imageWidth + 30, this.imageHeight);

        RenderSystem.disableBlend();
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics pGuiGraphics, int mouseX, int mouseY) {
        super.renderLabels(pGuiGraphics, mouseX, mouseY);
        if (menu.getUnifuser().isSlotFull(3)) {
            SlotItemHandler outputSlot = (SlotItemHandler) menu.getOutputSlot();
            pGuiGraphics.drawString(font, Component.translatable("gui.changed_addon.unifuser_gui.label_full"), outputSlot.x, outputSlot.y - 10, -12829636, false);
        }
    }

    private ItemStack getBlockItem(int index) {
        return menu.getUnifuser().getItem(index);
    }
}
