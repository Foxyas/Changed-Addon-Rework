
package net.foxyas.changedaddon.client.gui;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.Minecraft;

import net.foxyas.changedaddon.world.inventory.FoxyasGui2Menu;
import net.foxyas.changedaddon.procedures.IfplayerhaveDealTrueProcedure;
import net.foxyas.changedaddon.procedures.IfplayerHaveDealFalseProcedure;
import net.foxyas.changedaddon.network.FoxyasGui2ButtonMessage;
import net.foxyas.changedaddon.ChangedAddonMod;

import java.util.HashMap;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

public class FoxyasGui2Screen extends AbstractContainerScreen<FoxyasGui2Menu> {
	private final static HashMap<String, Object> guistate = FoxyasGui2Menu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_deal;
	Button button_no;
	Button button_aa;

	public FoxyasGui2Screen(FoxyasGui2Menu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 205;
		this.imageHeight = 166;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("changed_addon:textures/screens/foxyas_gui_2.png");

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
		this.font.draw(poseStack, Component.translatable("gui.changed_addon.foxyas_gui_2.label_oh_human_why_but_if_you_really"), 4, 5, -12829636);
		this.font.draw(poseStack, Component.translatable("gui.changed_addon.foxyas_gui_2.label_it_i_can_do_it"), 5, 17, -12829636);
		if (IfplayerhaveDealTrueProcedure.execute(entity))
			this.font.draw(poseStack, Component.translatable("gui.changed_addon.foxyas_gui_2.label_it_i_can_do_it1"), 8, 36, -12829636);
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
		button_deal = new Button(this.leftPos + 135, this.topPos + 32, 46, 20, Component.translatable("gui.changed_addon.foxyas_gui_2.button_deal"), e -> {
			if (IfplayerHaveDealFalseProcedure.execute(entity)) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new FoxyasGui2ButtonMessage(0, x, y, z));
				FoxyasGui2ButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		}) {
			@Override
			public void render(PoseStack ms, int gx, int gy, float ticks) {
				if (IfplayerHaveDealFalseProcedure.execute(entity))
					super.render(ms, gx, gy, ticks);
			}
		};
		guistate.put("button:button_deal", button_deal);
		this.addRenderableWidget(button_deal);
		button_no = new Button(this.leftPos + 48, this.topPos + 51, 35, 20, Component.translatable("gui.changed_addon.foxyas_gui_2.button_no"), e -> {
			if (IfplayerhaveDealTrueProcedure.execute(entity)) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new FoxyasGui2ButtonMessage(1, x, y, z));
				FoxyasGui2ButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		}) {
			@Override
			public void render(PoseStack ms, int gx, int gy, float ticks) {
				if (IfplayerhaveDealTrueProcedure.execute(entity))
					super.render(ms, gx, gy, ticks);
			}
		};
		guistate.put("button:button_no", button_no);
		this.addRenderableWidget(button_no);
		button_aa = new Button(this.leftPos + 6, this.topPos + 51, 35, 20, Component.translatable("gui.changed_addon.foxyas_gui_2.button_aa"), e -> {
			if (IfplayerhaveDealTrueProcedure.execute(entity)) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new FoxyasGui2ButtonMessage(2, x, y, z));
				FoxyasGui2ButtonMessage.handleButtonAction(entity, 2, x, y, z);
			}
		}) {
			@Override
			public void render(PoseStack ms, int gx, int gy, float ticks) {
				if (IfplayerhaveDealTrueProcedure.execute(entity))
					super.render(ms, gx, gy, ticks);
			}
		};
		guistate.put("button:button_aa", button_aa);
		this.addRenderableWidget(button_aa);
	}
}
