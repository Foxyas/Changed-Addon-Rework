
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

import net.foxyas.changedaddon.world.inventory.Bookrecipepage8Menu;
import net.foxyas.changedaddon.network.Bookrecipepage8ButtonMessage;
import net.foxyas.changedaddon.ChangedAddonMod;

import java.util.HashMap;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

public class Bookrecipepage8Screen extends AbstractContainerScreen<Bookrecipepage8Menu> {
	private final static HashMap<String, Object> guistate = Bookrecipepage8Menu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_back;
	Button button_close;

	public Bookrecipepage8Screen(Bookrecipepage8Menu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 145;
		this.imageHeight = 144;
	}

	private static final ResourceLocation texture = new ResourceLocation("changed_addon:textures/screens/bookrecipepage_8.png");

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

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/impureammoniacraft.png"));
		this.blit(ms, this.leftPos + 17, this.topPos + 47, 0, 0, 100, 52, 100, 52);

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
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.bookrecipepage_8.label_f_flowers_or_plants"), 9, 5, -16777216);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.bookrecipepage_8.label_s_seeds"), 9, 15, -16777216);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.bookrecipepage_8.label_m_meat"), 9, 25, -16777216);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.bookrecipepage_8.label_m"), 41, 83, -16777114);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.bookrecipepage_8.label_m1"), 26, 68, -16777114);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.bookrecipepage_8.label_m2"), 41, 54, -16777114);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.bookrecipepage_8.label_s"), 41, 68, -256);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.bookrecipepage_8.label_3"), 100, 85, -1);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.bookrecipepage_8.label_f"), 26, 83, -16711732);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.bookrecipepage_8.label_f1"), 27, 54, -16711732);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.bookrecipepage_8.label_f2"), 56, 54, -16711732);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.bookrecipepage_8.label_f3"), 56, 83, -16711732);
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
		button_back = new Button(this.leftPos + 38, this.topPos + -23, 46, 20, new TranslatableComponent("gui.changed_addon.bookrecipepage_8.button_back"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new Bookrecipepage8ButtonMessage(0, x, y, z));
				Bookrecipepage8ButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		});
		guistate.put("button:button_back", button_back);
		this.addRenderableWidget(button_back);
		button_close = new Button(this.leftPos + 89, this.topPos + -23, 51, 20, new TranslatableComponent("gui.changed_addon.bookrecipepage_8.button_close"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new Bookrecipepage8ButtonMessage(1, x, y, z));
				Bookrecipepage8ButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		});
		guistate.put("button:button_close", button_close);
		this.addRenderableWidget(button_close);
	}
}
