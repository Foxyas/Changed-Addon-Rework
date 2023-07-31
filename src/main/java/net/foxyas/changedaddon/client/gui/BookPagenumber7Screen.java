
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

import net.foxyas.changedaddon.world.inventory.BookPagenumber7Menu;
import net.foxyas.changedaddon.network.BookPagenumber7ButtonMessage;
import net.foxyas.changedaddon.ChangedAddonMod;

import java.util.HashMap;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

public class BookPagenumber7Screen extends AbstractContainerScreen<BookPagenumber7Menu> {
	private final static HashMap<String, Object> guistate = BookPagenumber7Menu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_close;

	public BookPagenumber7Screen(BookPagenumber7Menu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 179;
		this.imageHeight = 166;
	}

	private static final ResourceLocation texture = new ResourceLocation("changed_addon:textures/screens/book_pagenumber_7.png");

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

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/organic_form_overlay.png"));
		this.blit(ms, this.leftPos + 6, this.topPos + 6, 0, 0, 16, 16, 16, 16);

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/organic_friendly_mode_off_v2.png"));
		this.blit(ms, this.leftPos + 6, this.topPos + 28, 0, 0, 16, 16, 16, 16);

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/organic_friendly_mode_on_v2.png"));
		this.blit(ms, this.leftPos + 6, this.topPos + 46, 0, 0, 16, 16, 16, 16);

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
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_7.label_normaltransfur_grab_mode_o"), 25, 36, -13421773);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_7.label_e_only_for_player"), 25, 47, -13421773);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_7.label_assimilation_grab_mode"), 25, 12, -12829636);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_7.label_default_off"), 7, 74, -16777216);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_7.label_humans_overlay"), 6, 87, -16724788);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_7.label_organiccant_grab"), 5, 98, -10667514);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_7.label_all_overlays_can_be_turned_on_by"), 5, 118, -15789313);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_7.label_a_command"), 5, 129, -15789313);
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
		button_close = new Button(this.leftPos + 121, this.topPos + 142, 51, 20, new TranslatableComponent("gui.changed_addon.book_pagenumber_7.button_close"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new BookPagenumber7ButtonMessage(0, x, y, z));
				BookPagenumber7ButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		});
		guistate.put("button:button_close", button_close);
		this.addRenderableWidget(button_close);
	}
}
