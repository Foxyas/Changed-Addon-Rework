package net.foxyas.changedaddon.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.menu.GeneratorGuiMenu;
import net.foxyas.changedaddon.network.packet.GeneratorGuiButtonPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

public class GeneratorguiScreen extends AbstractContainerScreen<GeneratorGuiMenu> {

    private static final ResourceLocation texture = ResourceLocation.parse("changed_addon:textures/screens/generatorgui.png");

    private final Level level;
    private final Player entity;
    private final ImageButton imagebutton_hitbox_16x16;

    public GeneratorguiScreen(GeneratorGuiMenu container, Inventory inventory, Component text) {
        super(container, inventory, text);
        this.level = container.level;
        this.entity = container.entity;
        this.imageWidth = 200;
        this.imageHeight = 99;

        imagebutton_hitbox_16x16 = new ImageButton(this.leftPos + 170, this.topPos + 73, 16, 16, 0, 0, 16, ResourceLocation.parse("changed_addon:textures/screens/atlas/imagebutton_hitbox_16x16.png"), 16, 32, e -> {
            ChangedAddonMod.PACKET_HANDLER.sendToServer(new GeneratorGuiButtonPacket(0, menu.pos));
            GeneratorGuiButtonPacket.handleButtonAction(entity, 0, menu.pos);
        });
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(pGuiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float partialTicks, int gx, int gy) {
        pGuiGraphics.setColor(1, 1, 1, 1);
        pGuiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);

        ResourceLocation stateTexture;
        if (isOn()) {
            stateTexture = ResourceLocation.parse("changed_addon:textures/screens/on.png");
        } else {
            stateTexture = ResourceLocation.parse("changed_addon:textures/screens/off.png");
        }
        pGuiGraphics.blit(stateTexture, this.leftPos + 170, this.topPos + 73, 0, 0, 16, 16, 16, 16);

        RenderSystem.disableBlend();
    }

    private boolean isOn() {
        BlockEntity blockEntity = level.getBlockEntity(menu.pos);
        return blockEntity != null && blockEntity.getPersistentData().getBoolean("turn_on");
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics pGuiGraphics, int mouseX, int mouseY) {
        pGuiGraphics.drawString(font, energyAmount(), 4, 10, -12829636, false);
        pGuiGraphics.drawString(font, "generator is " + (isOn() ? "activated" : "disabled"), 11, 76, -12829636, false);
    }

    private String energyAmount() {
        BlockEntity blockEntity = level.getBlockEntity(menu.pos);
        if (blockEntity == null) return "generator power is 0";

        IEnergyStorage storage = blockEntity.getCapability(ForgeCapabilities.ENERGY).resolve().orElse(null);
        if (storage == null) return "generator power is 0";

        return "generator power is " + storage.getEnergyStored();
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    public void init() {
        super.init();
        imagebutton_hitbox_16x16.setX(this.leftPos + 170);
        imagebutton_hitbox_16x16.setY(this.topPos + 73);

        addRenderableWidget(imagebutton_hitbox_16x16);
    }
}
