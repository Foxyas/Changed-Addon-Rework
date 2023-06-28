
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

import net.foxyas.changedaddon.world.inventory.Bookpagenumber4Menu;
import net.foxyas.changedaddon.network.Bookpagenumber4ButtonMessage;
import net.foxyas.changedaddon.ChangedAddonMod;

import java.util.HashMap;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

public class Bookpagenumber4Screen extends AbstractContainerScreen<Bookpagenumber4Menu> {
	private final static HashMap<String, Object> guistate = Bookpagenumber4Menu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_close;
	Button button_back;

	public Bookpagenumber4Screen(Bookpagenumber4Menu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 400;
		this.imageHeight = 220;
	}

	private static final ResourceLocation texture = new ResourceLocation("changed_addon:textures/screens/bookpagenumber_4.png");

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

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/way.png"));
		this.blit(ms, this.leftPos + 3, this.topPos + 3, 0, 0, 578, 352, 578, 352);

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/way_2.png"));
		this.blit(ms, this.leftPos + 3, this.topPos + 106, 0, 0, 359, 352, 359, 352);

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
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.bookpagenumber_4.label_correct_way"), 123, 184, -16711936);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.bookpagenumber_4.label_incorrect_way"), 112, 32, -3407872);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.bookpagenumber_4.label_with_the_latex_syringe_just_use"), 213, 6, -12829636);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.bookpagenumber_4.label_in_the_rank_right_mouse_button"), 212, 17, -12829636);
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
		button_close = new Button(this.leftPos + 333, this.topPos + 189, 51, 20, new TranslatableComponent("gui.changed_addon.bookpagenumber_4.button_close"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new Bookpagenumber4ButtonMessage(0, x, y, z));
				Bookpagenumber4ButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		});
		guistate.put("button:button_close", button_close);
		this.addRenderableWidget(button_close);
		button_back = new Button(this.leftPos + 280, this.topPos + 189, 46, 20, new TranslatableComponent("gui.changed_addon.bookpagenumber_4.button_back"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new Bookpagenumber4ButtonMessage(1, x, y, z));
				Bookpagenumber4ButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		});
		guistate.put("button:button_back", button_back);
		this.addRenderableWidget(button_back);
	}
}
