
package net.foxyas.changedaddon.client.gui;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.Minecraft;

import net.foxyas.changedaddon.world.inventory.GrabclickguiMenu;
import net.foxyas.changedaddon.procedures.ShowstrugglebuttonProcedure;
import net.foxyas.changedaddon.procedures.ShowbreakfreebuttonProcedure;
import net.foxyas.changedaddon.procedures.GrabclickguiValueProcedure;
import net.foxyas.changedaddon.network.GrabclickguiButtonMessage;
import net.foxyas.changedaddon.ChangedAddonMod;

import java.util.HashMap;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

public class GrabclickguiScreen extends AbstractContainerScreen<GrabclickguiMenu> {
	private final static HashMap<String, Object> guistate = GrabclickguiMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_struggle;
	Button button_breakfree;

	public GrabclickguiScreen(GrabclickguiMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 200;
		this.imageHeight = 100;
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
		this.font.draw(poseStack,

				GrabclickguiValueProcedure.execute(entity), 67, 22, -12829636);
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
		button_struggle = new Button(this.leftPos + 65, this.topPos + 36, 67, 20, new TranslatableComponent("gui.changed_addon.grabclickgui.button_struggle"), e -> {
			if (ShowstrugglebuttonProcedure.execute(entity)) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new GrabclickguiButtonMessage(0, x, y, z));
				GrabclickguiButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		}) {
			@Override
			public void render(PoseStack ms, int gx, int gy, float ticks) {
				if (ShowstrugglebuttonProcedure.execute(entity))
					super.render(ms, gx, gy, ticks);
			}
		};
		guistate.put("button:button_struggle", button_struggle);
		this.addRenderableWidget(button_struggle);
		button_breakfree = new Button(this.leftPos + 63, this.topPos + 61, 72, 20, new TranslatableComponent("gui.changed_addon.grabclickgui.button_breakfree"), e -> {
			if (ShowbreakfreebuttonProcedure.execute(entity)) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new GrabclickguiButtonMessage(1, x, y, z));
				GrabclickguiButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		}) {
			@Override
			public void render(PoseStack ms, int gx, int gy, float ticks) {
				if (ShowbreakfreebuttonProcedure.execute(entity))
					super.render(ms, gx, gy, ticks);
			}
		};
		guistate.put("button:button_breakfree", button_breakfree);
		this.addRenderableWidget(button_breakfree);
	}
}
