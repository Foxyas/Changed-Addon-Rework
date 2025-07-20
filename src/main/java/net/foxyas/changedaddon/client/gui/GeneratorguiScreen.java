
package net.foxyas.changedaddon.client.gui;

import net.foxyas.changedaddon.network.GeneratorGuiButtonMessage;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.Minecraft;

import net.foxyas.changedaddon.world.inventory.GeneratorGuiMenu;
import net.foxyas.changedaddon.procedures.IfisturnonProcedure;
import net.foxyas.changedaddon.procedures.IfisturnoffProcedure;
import net.foxyas.changedaddon.procedures.GeneratorguiValueProcedure;
import net.foxyas.changedaddon.procedures.GeneratorguiValue2Procedure;
import net.foxyas.changedaddon.ChangedAddonMod;

import java.util.HashMap;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

public class GeneratorguiScreen extends AbstractContainerScreen<GeneratorGuiMenu> {
	private final static HashMap<String, Object> guistate = GeneratorGuiMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	ImageButton imagebutton_hitbox_16x16;

	public GeneratorguiScreen(GeneratorGuiMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 200;
		this.imageHeight = 99;
	}

	private static final ResourceLocation texture = new ResourceLocation("changed_addon:textures/screens/generatorgui.png");

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
		if (IfisturnonProcedure.execute(world, x, y, z)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/on.png"));
			this.blit(ms, this.leftPos + 170, this.topPos + 73, 0, 0, 16, 16, 16, 16);
		}
		if (IfisturnoffProcedure.execute(world, x, y, z)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/off.png"));
			this.blit(ms, this.leftPos + 170, this.topPos + 73, 0, 0, 16, 16, 16, 16);
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
		this.font.draw(poseStack,

				GeneratorguiValueProcedure.execute(world, x, y, z), 4, 10, -12829636);
		this.font.draw(poseStack,

				GeneratorguiValue2Procedure.execute(world, x, y, z), 11, 76, -12829636);
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
		imagebutton_hitbox_16x16 = new ImageButton(this.leftPos + 170, this.topPos + 73, 16, 16, 0, 0, 16, new ResourceLocation("changed_addon:textures/screens/atlas/imagebutton_hitbox_16x16.png"), 16, 32, e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new GeneratorGuiButtonMessage(0, x, y, z));
				GeneratorGuiButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		});
		guistate.put("button:imagebutton_hitbox_16x16", imagebutton_hitbox_16x16);
		this.addRenderableWidget(imagebutton_hitbox_16x16);
	}
}
