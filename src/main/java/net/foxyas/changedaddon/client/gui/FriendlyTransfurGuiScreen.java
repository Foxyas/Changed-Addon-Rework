
package net.foxyas.changedaddon.client.gui;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.Minecraft;

import net.foxyas.changedaddon.world.inventory.FriendlyTransfurGuiMenu;
import net.foxyas.changedaddon.procedures.FriendlyTransfurGuitextProcedure;
import net.foxyas.changedaddon.network.FriendlyTransfurGuiButtonMessage;
import net.foxyas.changedaddon.ChangedAddonMod;

import java.util.HashMap;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

public class FriendlyTransfurGuiScreen extends AbstractContainerScreen<FriendlyTransfurGuiMenu> {
	private final static HashMap<String, Object> guistate = FriendlyTransfurGuiMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_no;
	Button button_no1;

	public FriendlyTransfurGuiScreen(FriendlyTransfurGuiMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 200;
		this.imageHeight = 100;
	}

	private static final ResourceLocation texture = new ResourceLocation("changed_addon:textures/screens/friendly_transfur_gui.png");

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
		this.font.draw(poseStack,

				FriendlyTransfurGuitextProcedure.execute(world, entity), 8, 6, -12829636);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.friendly_transfur_gui.label_accept"), 80, 39, -12829636);
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
		button_no = new Button(this.leftPos + 119, this.topPos + 56, 35, 20, new TranslatableComponent("gui.changed_addon.friendly_transfur_gui.button_no"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new FriendlyTransfurGuiButtonMessage(0, x, y, z));
				FriendlyTransfurGuiButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		});
		guistate.put("button:button_no", button_no);
		this.addRenderableWidget(button_no);
		button_no1 = new Button(this.leftPos + 40, this.topPos + 56, 35, 20, new TranslatableComponent("gui.changed_addon.friendly_transfur_gui.button_no1"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new FriendlyTransfurGuiButtonMessage(1, x, y, z));
				FriendlyTransfurGuiButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		});
		guistate.put("button:button_no1", button_no1);
		this.addRenderableWidget(button_no1);
	}
}
