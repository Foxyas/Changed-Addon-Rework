
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

import net.foxyas.changedaddon.world.inventory.GrabRadialMenuFriendlyMenu;
import net.foxyas.changedaddon.procedures.ShowfriendlymodeofficonProcedure;
import net.foxyas.changedaddon.procedures.ShowfriendlymodeIconProcedure;
import net.foxyas.changedaddon.procedures.ShowOrganicGrabRadialProcedure;
import net.foxyas.changedaddon.procedures.ShowOrganicFriendlyModeonProcedure;
import net.foxyas.changedaddon.procedures.ShowOrganicFriendlyModeOffProcedure;
import net.foxyas.changedaddon.procedures.ShowNoOrganicGrabRadialProcedure;
import net.foxyas.changedaddon.network.GrabRadialMenuFriendlyButtonMessage;
import net.foxyas.changedaddon.ChangedAddonMod;

import java.util.HashMap;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

public class GrabRadialMenuFriendlyScreen extends AbstractContainerScreen<GrabRadialMenuFriendlyMenu> {
	private final static HashMap<String, Object> guistate = GrabRadialMenuFriendlyMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_back;
	ImageButton imagebutton_friendlymodeicon;

	public GrabRadialMenuFriendlyScreen(GrabRadialMenuFriendlyMenu container, Inventory inventory, Component text) {
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
			this.blit(ms, this.leftPos + 25, this.topPos + 17, 0, 0, 122, 118, 122, 118);
		}
		if (ShowOrganicGrabRadialProcedure.execute(entity)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/organic_grab_radial__white.png"));
			this.blit(ms, this.leftPos + 25, this.topPos + 18, 0, 0, 122, 118, 122, 118);
		}
		if (ShowOrganicFriendlyModeOffProcedure.execute(entity)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/organic_friendly_mode_off_v2.png"));
			this.blit(ms, this.leftPos + 95, this.topPos + 28, 0, 0, 16, 16, 16, 16);
		}
		if (ShowOrganicFriendlyModeonProcedure.execute(entity)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/organic_friendly_mode_on_v2.png"));
			this.blit(ms, this.leftPos + 95, this.topPos + 28, 0, 0, 16, 16, 16, 16);
		}
		if (ShowfriendlymodeofficonProcedure.execute(entity)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/friendlymodeofficon.png"));
			this.blit(ms, this.leftPos + 95, this.topPos + 28, 0, 0, 16, 16, 16, 16);
		}
		if (ShowfriendlymodeIconProcedure.execute(entity)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/friendlymodeicon.png"));
			this.blit(ms, this.leftPos + 95, this.topPos + 28, 0, 0, 16, 16, 16, 16);
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
		button_back = new Button(this.leftPos + 63, this.topPos + 65, 46, 20, new TranslatableComponent("gui.changed_addon.grab_radial_menu_friendly.button_back"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new GrabRadialMenuFriendlyButtonMessage(0, x, y, z));
				GrabRadialMenuFriendlyButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		});
		guistate.put("button:button_back", button_back);
		this.addRenderableWidget(button_back);
		imagebutton_friendlymodeicon = new ImageButton(this.leftPos + 95, this.topPos + 28, 16, 16, 0, 0, 16, new ResourceLocation("changed_addon:textures/screens/atlas/imagebutton_friendlymodeicon.png"), 16, 32, e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new GrabRadialMenuFriendlyButtonMessage(1, x, y, z));
				GrabRadialMenuFriendlyButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		});
		guistate.put("button:imagebutton_friendlymodeicon", imagebutton_friendlymodeicon);
		this.addRenderableWidget(imagebutton_friendlymodeicon);
	}
}
