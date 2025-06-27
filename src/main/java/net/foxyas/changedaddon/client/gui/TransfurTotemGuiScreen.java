
package net.foxyas.changedaddon.client.gui;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.Minecraft;

import net.foxyas.changedaddon.world.inventory.TransfurTotemGuiMenu;

import java.util.HashMap;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

public class TransfurTotemGuiScreen extends AbstractContainerScreen<TransfurTotemGuiMenu> {
	private final static HashMap<String, Object> guistate = TransfurTotemGuiMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	ImageButton imagebutton_transfur_totem_button;

	public TransfurTotemGuiScreen(TransfurTotemGuiMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 125;
		this.imageHeight = 70;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("changed_addon:textures/screens/transfur_totem_gui.png");

	@Override
	public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTicks);
		this.renderTooltip(ms, mouseX, mouseY);
	}

	@Override
	protected void renderBg(PoseStack ms, float partialTicks, int gx, int gy) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShaderTexture(0, texture);
		this.blit(ms, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		RenderSystem.disableBlend();
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	public void containerTick() {
		super.containerTick();
	}

	@Override
	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
		this.font.draw(poseStack, Component.translatable("gui.changed_addon.transfur_totem_gui.label_link_your_form"), 6, 6, -16777216);
		this.font.draw(poseStack, Component.translatable("gui.changed_addon.transfur_totem_gui.label_press_the_totem_icon"), 0, 18, -16777216);
	}

	@Override
	public void onClose() {
		super.onClose();
		Minecraft.getInstance().keyboardHandler.setSendRepeatsToGui(false);
	}

	@Override
	public void init() {
		super.init();
		this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
		imagebutton_transfur_totem_button = new ImageButton(this.leftPos + 54, this.topPos + 37, 18, 18, 0, 0, 18, ResourceLocation.parse("changed_addon:textures/screens/atlas/imagebutton_transfur_totem_button.png"), 18, 36, e -> {
		});
		guistate.put("button:imagebutton_transfur_totem_button", imagebutton_transfur_totem_button);
		this.addRenderableWidget(imagebutton_transfur_totem_button);
	}
}
