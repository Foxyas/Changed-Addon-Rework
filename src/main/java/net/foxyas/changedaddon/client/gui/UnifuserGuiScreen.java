package net.foxyas.changedaddon.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.foxyas.changedaddon.block.entity.CatalyzerBlockEntity;
import net.foxyas.changedaddon.block.entity.UnifuserBlockEntity;
import net.foxyas.changedaddon.menu.UnifuserGuiMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class UnifuserGuiScreen extends AbstractContainerScreen<UnifuserGuiMenu> {

    private final UnifuserGuiMenu menu;
    private final Level level;
    private final BlockPos pos;

    public UnifuserGuiScreen(UnifuserGuiMenu container, Inventory inventory, Component text) {
        super(container, inventory, text);
        menu = container;
        this.level = container.level;
        this.pos = menu.getBlockPos();
        this.imageWidth = 200;
        this.imageHeight = 187;
    }

    public static String getMachineState(Level level, BlockPos pos) {
        String block = Component.translatable("block." + ForgeRegistries.BLOCKS.getKey((level.getBlockState(pos)).getBlock()).toString().replace(":", ".")).getString();

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof UnifuserBlockEntity unifuserBlockEntity) {
            return !unifuserBlockEntity.startRecipe ? block + " is deactivated" : block + " is activated";
        } else if (blockEntity instanceof CatalyzerBlockEntity catalyzerBlockEntity) {
            return !catalyzerBlockEntity.startRecipe ? block + " is deactivated" : block + " is activated";
        }
        return "Something ODD Happen..";
    }

    public static String getRecipeState(LevelAccessor level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity == null) return "THIS SHOULD NEVER HAPPEN";
        double number = 0;
        if (blockEntity instanceof UnifuserBlockEntity unifuserBlockEntity) {
            number = unifuserBlockEntity.recipeProgress;
        } else if (blockEntity instanceof CatalyzerBlockEntity catalyzerBlockEntity) {
            number = catalyzerBlockEntity.recipeProgress;
        }

        return Math.round(number) + "%";
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(pGuiGraphics, mouseX, mouseY);
        if (menu.getTopSlot().getItem().isEmpty()) //(menu.isSlotEmpty(36))
            if (mouseX > leftPos + 10 && mouseX < leftPos + 34 && mouseY > topPos + 41 && mouseY < topPos + 65)
                pGuiGraphics.renderTooltip(font, Component.translatable("gui.changed_addon.unifuser_gui.tooltip_place_the_powders"), mouseX, mouseY);
        if (menu.getBottomSlot().getItem().isEmpty())
            if (mouseX > leftPos + 10 && mouseX < leftPos + 34 && mouseY > topPos + 65 && mouseY < topPos + 89)
                pGuiGraphics.renderTooltip(font, Component.translatable("gui.changed_addon.unifuser_gui.tooltip_put_the_second_ingredient"), mouseX, mouseY);
        if (menu.getSyringeSlot().getItem().isEmpty())
            if (mouseX > leftPos + 45 && mouseX < leftPos + 69 && mouseY > topPos + 53 && mouseY < topPos + 77)
                pGuiGraphics.renderTooltip(font, Component.translatable("gui.changed_addon.unifuser_gui.tooltip_place_a_syringe_with_dna"), mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float partialTicks, int gx, int gy) {
        pGuiGraphics.setColor(1, 1, 1, 1);

        pGuiGraphics.blit(ResourceLocation.parse("changed_addon:textures/screens/unifusergui_new.png"), this.leftPos, this.topPos, 0, 0, 200, 187, 200, 187);

        pGuiGraphics.blit(ResourceLocation.parse("changed_addon:textures/screens/empty_bar.png"), this.leftPos + 84, this.topPos + 59, 0, 0, 32, 12, 32, 12);

        int progressInt = (int) (menu.getUnifuser().recipeProgress / 3.57);

        pGuiGraphics.blit(ResourceLocation.parse("changed_addon:textures/screens/bar_full.png"), this.leftPos + 84 + 2, this.topPos + 59 + 2, 0, 0, progressInt, 8, progressInt, 8);

        if (getBlockItem(0).isEmpty()) {
            pGuiGraphics.blit(ResourceLocation.parse("changed_addon:textures/screens/dusts.png"), this.leftPos + 15, this.topPos + 46, 0, 0, 16, 16, 16, 16);
        }
        if (getBlockItem(2).isEmpty()) {
            pGuiGraphics.blit(ResourceLocation.parse("changed_addon:textures/screens/syringe_withlitixcamonia_screen.png"), this.leftPos + 50, this.topPos + 57, 0, 0, 16, 16, 16, 16);
        }

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
