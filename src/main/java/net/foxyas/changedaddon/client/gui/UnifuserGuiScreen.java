package net.foxyas.changedaddon.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.foxyas.changedaddon.block.entity.CatalyzerBlockEntity;
import net.foxyas.changedaddon.block.entity.UnifuserBlockEntity;
import net.foxyas.changedaddon.menu.UnifuserGuiMenu;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
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

        this.cyclingSlot0BackgroundWidget = new CyclingSlotBackgroundWidget(menu, menu.getTopSlot().getSlotIndex(), EMPTY_ICONS);
        this.cyclingSlot1BackgroundWidget = new CyclingSlotBackgroundWidget(menu, menu.getBottomSlot().getSlotIndex(), EMPTY_ICONS);
        this.cyclingSlot2BackgroundWidget = new CyclingSlotBackgroundWidget(menu, menu.getSyringeSlot().getSlotIndex(), EMPTY_ICONS);
        cyclingSlotBackgroundWidgets.addAll(List.of(cyclingSlot0BackgroundWidget, cyclingSlot1BackgroundWidget, cyclingSlot2BackgroundWidget));
    }

    @Override
    protected void init() {
        super.init();
        cyclingSlotBackgroundWidgets.forEach(widget -> widget.setScreenPos(new Vec2(leftPos, topPos)));
        cyclingSlotBackgroundWidgets.forEach(this::addRenderableWidget);
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
        if (carriedItemIsEmptyOrIsNotSelected && this.hoveredSlot != null) {
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

        if (unifuser.recipeProgress > 0) {
            int recipeProgress = (int) (28 * (unifuser.recipeProgress / 100));
            guiGraphics.blit(BACKGROUND_TEXTURE, this.leftPos + 75, this.topPos + 40, this.imageWidth + 1, 0, recipeProgress, 11, this.imageWidth + 30, this.imageHeight);
        }

//        guiGraphics.blit(ResourceLocation.parse("changed_addon:textures/screens/unifusergui_new.png"), this.leftPos, this.topPos, 0, 0, 200, 187, 200, 187);
//
//        guiGraphics.blit(ResourceLocation.parse("changed_addon:textures/screens/empty_bar.png"), this.leftPos + 84, this.topPos + 59, 0, 0, 32, 12, 32, 12);
//
//        int progressInt = (int) (menu.getUnifuser().recipeProgress / 3.57);
//
//        guiGraphics.blit(ResourceLocation.parse("changed_addon:textures/screens/bar_full.png"), this.leftPos + 84 + 2, this.topPos + 59 + 2, 0, 0, progressInt, 8, progressInt, 8);

        RenderSystem.disableBlend();
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics pGuiGraphics, int mouseX, int mouseY) {
        pGuiGraphics.drawString(font, getMachineState(level, pos), 9, 10, -12829636, false);
        if (menu.getUnifuser().isSlotFull(3))
            pGuiGraphics.drawString(font, Component.translatable("gui.changed_addon.unifuser_gui.label_full"), 153, 78, -12829636, false);
        pGuiGraphics.drawString(font, getRecipeState(level, pos), 89, 47, -12829636, false);
    }

    private ItemStack getBlockItem(int index) {
        return menu.getUnifuser().getItem(index);
    }
}
