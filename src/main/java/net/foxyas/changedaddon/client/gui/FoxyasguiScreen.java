
package net.foxyas.changedaddon.client.gui;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.Minecraft;

import net.foxyas.changedaddon.world.inventory.FoxyasguiMenu;
import net.foxyas.changedaddon.procedures.IfplayerarenttransfurProcedure;
import net.foxyas.changedaddon.network.FoxyasguiButtonMessage;
import net.foxyas.changedaddon.ChangedAddonMod;

import java.util.HashMap;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

public class FoxyasguiScreen extends AbstractContainerScreen<FoxyasguiMenu> {
	private final static HashMap<String, Object> guistate = FoxyasguiMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_trade;
	Button button_i_want_be_transfured_by_you;

	public FoxyasguiScreen(FoxyasguiMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 345;
		this.imageHeight = 177;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("changed_addon:textures/screens/foxyasgui.png");

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
		this.font.draw(poseStack, Component.translatable("gui.changed_addon.foxyasgui.label_hello_im_foxyas_i_dont_want_to"), 5, 4, -12829636);
		this.font.draw(poseStack, Component.translatable("gui.changed_addon.foxyasgui.label_pls_dont_kill_me"), 5, 18, -12829636);
		this.font.draw(poseStack, Component.translatable("gui.changed_addon.foxyasgui.label_i_just_need_2_oranges_and_one_gl"), 5, 32, -12829636);
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
		button_trade = new Button(this.leftPos + 224, this.topPos + 115, 51, 20, Component.translatable("gui.changed_addon.foxyasgui.button_trade"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new FoxyasguiButtonMessage(0, x, y, z));
				FoxyasguiButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		});
		guistate.put("button:button_trade", button_trade);
		this.addRenderableWidget(button_trade);
		button_i_want_be_transfured_by_you = new Button(this.leftPos + 13, this.topPos + 66, 165, 20, Component.translatable("gui.changed_addon.foxyasgui.button_i_want_be_transfured_by_you"), e -> {
			if (IfplayerarenttransfurProcedure.execute(entity)) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new FoxyasguiButtonMessage(1, x, y, z));
				FoxyasguiButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		}) {
			@Override
			public void render(PoseStack ms, int gx, int gy, float ticks) {
				if (IfplayerarenttransfurProcedure.execute(entity))
					super.render(ms, gx, gy, ticks);
			}
		};
		guistate.put("button:button_i_want_be_transfured_by_you", button_i_want_be_transfured_by_you);
		this.addRenderableWidget(button_i_want_be_transfured_by_you);
	}
}
