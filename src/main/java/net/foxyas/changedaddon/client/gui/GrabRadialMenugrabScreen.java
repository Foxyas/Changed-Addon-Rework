
package net.foxyas.changedaddon.client.gui;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.Minecraft;

import net.foxyas.changedaddon.world.inventory.GrabRadialMenugrabMenu;
import net.foxyas.changedaddon.procedures.ShowcangrabiconProcedure;
import net.foxyas.changedaddon.procedures.ShowassimilationoffbuttonProcedure;
import net.foxyas.changedaddon.procedures.ShowassimilationiconProcedure;
import net.foxyas.changedaddon.procedures.ShowOrganicGrabRadialProcedure;
import net.foxyas.changedaddon.procedures.ShowNoOrganicGrabRadialProcedure;
import net.foxyas.changedaddon.procedures.ShocantgrabiconProcedure;
import net.foxyas.changedaddon.network.GrabRadialMenugrabButtonMessage;
import net.foxyas.changedaddon.ChangedAddonMod;

import java.util.HashMap;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

public class GrabRadialMenugrabScreen extends AbstractContainerScreen<GrabRadialMenugrabMenu> {
	private final static HashMap<String, Object> guistate = GrabRadialMenugrabMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_back;
	ImageButton imagebutton_hitbox_16x16;
	ImageButton imagebutton_hitbox_16x161;

	public GrabRadialMenugrabScreen(GrabRadialMenugrabMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 176;
		this.imageHeight = 150;
	}

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
		if (ShowNoOrganicGrabRadialProcedure.execute(entity)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/grab_radial_menu.png"));
			this.blit(ms, this.leftPos + 25, this.topPos + 18, 0, 0, 122, 118, 122, 118);
		}
		if (ShowOrganicGrabRadialProcedure.execute(entity)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/organic_grab_radial__white.png"));
			this.blit(ms, this.leftPos + 25, this.topPos + 18, 0, 0, 122, 118, 122, 118);
		}
		if (ShowcangrabiconProcedure.execute(entity)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/cant_grab_icon_outline.png"));
			this.blit(ms, this.leftPos + 60, this.topPos + 28, 0, 0, 16, 16, 16, 16);
		}
		if (ShocantgrabiconProcedure.execute(entity)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/can_grab_icon_outline.png"));
			this.blit(ms, this.leftPos + 60, this.topPos + 28, 0, 0, 16, 16, 16, 16);
		}
		if (ShowassimilationiconProcedure.execute(entity)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/canassimilation_outline.png"));
			this.blit(ms, this.leftPos + 95, this.topPos + 27, 0, 0, 16, 16, 16, 16);
		}
		if (ShowassimilationoffbuttonProcedure.execute(entity)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/cantassimilation_outline.png"));
			this.blit(ms, this.leftPos + 95, this.topPos + 27, 0, 0, 16, 16, 16, 16);
		}
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
		button_back = new Button(this.leftPos + 64, this.topPos + 65, 46, 20, new TranslatableComponent("gui.changed_addon.grab_radial_menugrab.button_back"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new GrabRadialMenugrabButtonMessage(0, x, y, z));
				GrabRadialMenugrabButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		});
		guistate.put("button:button_back", button_back);
		this.addRenderableWidget(button_back);
		imagebutton_hitbox_16x16 = new ImageButton(this.leftPos + 60, this.topPos + 28, 16, 16, 0, 0, 16, new ResourceLocation("changed_addon:textures/screens/atlas/imagebutton_hitbox_16x16.png"), 16, 32, e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new GrabRadialMenugrabButtonMessage(1, x, y, z));
				GrabRadialMenugrabButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		});
		guistate.put("button:imagebutton_hitbox_16x16", imagebutton_hitbox_16x16);
		this.addRenderableWidget(imagebutton_hitbox_16x16);
		imagebutton_hitbox_16x161 = new ImageButton(this.leftPos + 95, this.topPos + 27, 16, 16, 0, 0, 16, new ResourceLocation("changed_addon:textures/screens/atlas/imagebutton_hitbox_16x161.png"), 16, 32, e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new GrabRadialMenugrabButtonMessage(2, x, y, z));
				GrabRadialMenugrabButtonMessage.handleButtonAction(entity, 2, x, y, z);
			}
		});
		guistate.put("button:imagebutton_hitbox_16x161", imagebutton_hitbox_16x161);
		this.addRenderableWidget(imagebutton_hitbox_16x161);
	}
}
