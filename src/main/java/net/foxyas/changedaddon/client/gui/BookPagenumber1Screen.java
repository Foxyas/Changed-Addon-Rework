
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

import net.foxyas.changedaddon.world.inventory.BookPagenumber1Menu;
import net.foxyas.changedaddon.network.BookPagenumber1ButtonMessage;
import net.foxyas.changedaddon.ChangedAddonMod;

import java.util.HashMap;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

public class BookPagenumber1Screen extends AbstractContainerScreen<BookPagenumber1Menu> {
	private final static HashMap<String, Object> guistate = BookPagenumber1Menu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_close;
	Button button_changed_addon_overlays;
	Button button_changed_addon_recipes;

	public BookPagenumber1Screen(BookPagenumber1Menu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 200;
		this.imageHeight = 90;
	}

	private static final ResourceLocation texture = new ResourceLocation("changed_addon:textures/screens/book_pagenumber_1.png");

	@Override
	public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTicks);
		this.renderTooltip(ms, mouseX, mouseY);
		if (mouseX > leftPos + 143 && mouseX < leftPos + 167 && mouseY > topPos + 43 && mouseY < topPos + 67)
			this.renderTooltip(ms, new TranslatableComponent("gui.changed_addon.book_pagenumber_1.tooltip_show_the_recipes_of_changed_addo3"), mouseX, mouseY);
		if (mouseX > leftPos + 152 && mouseX < leftPos + 176 && mouseY > topPos + 17 && mouseY < topPos + 41)
			this.renderTooltip(ms, new TranslatableComponent("gui.changed_addon.book_pagenumber_1.tooltip_will_show_information_about_chan"), mouseX, mouseY);
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
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_1.label_empty"), 152, 51, -16777216);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_1.label_empty1"), 161, 24, -16777216);
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
		button_close = new Button(this.leftPos + 146, this.topPos + 95, 51, 20, new TranslatableComponent("gui.changed_addon.book_pagenumber_1.button_close"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new BookPagenumber1ButtonMessage(0, x, y, z));
				BookPagenumber1ButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		});
		guistate.put("button:button_close", button_close);
		this.addRenderableWidget(button_close);
		button_changed_addon_overlays = new Button(this.leftPos + 5, this.topPos + 19, 145, 20, new TranslatableComponent("gui.changed_addon.book_pagenumber_1.button_changed_addon_overlays"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new BookPagenumber1ButtonMessage(1, x, y, z));
				BookPagenumber1ButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		});
		guistate.put("button:button_changed_addon_overlays", button_changed_addon_overlays);
		this.addRenderableWidget(button_changed_addon_overlays);
		button_changed_addon_recipes = new Button(this.leftPos + 5, this.topPos + 46, 134, 20, new TranslatableComponent("gui.changed_addon.book_pagenumber_1.button_changed_addon_recipes"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new BookPagenumber1ButtonMessage(2, x, y, z));
				BookPagenumber1ButtonMessage.handleButtonAction(entity, 2, x, y, z);
			}
		});
		guistate.put("button:button_changed_addon_recipes", button_changed_addon_recipes);
		this.addRenderableWidget(button_changed_addon_recipes);
	}
}
