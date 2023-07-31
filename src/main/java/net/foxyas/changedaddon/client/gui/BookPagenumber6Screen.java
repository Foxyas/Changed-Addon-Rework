
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

import net.foxyas.changedaddon.world.inventory.BookPagenumber6Menu;
import net.foxyas.changedaddon.network.BookPagenumber6ButtonMessage;
import net.foxyas.changedaddon.ChangedAddonMod;

import java.util.HashMap;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

public class BookPagenumber6Screen extends AbstractContainerScreen<BookPagenumber6Menu> {
	private final static HashMap<String, Object> guistate = BookPagenumber6Menu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_close;
	Button button_next;

	public BookPagenumber6Screen(BookPagenumber6Menu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 179;
		this.imageHeight = 166;
	}

	private static final ResourceLocation texture = new ResourceLocation("changed_addon:textures/screens/book_pagenumber_6.png");

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

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/assimilation.png"));
		this.blit(ms, this.leftPos + 6, this.topPos + 10, 0, 0, 16, 16, 16, 16);

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/grab_overlay_v2.png"));
		this.blit(ms, this.leftPos + 6, this.topPos + 28, 0, 0, 16, 16, 16, 16);

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/friendlymodeicon.png"));
		this.blit(ms, this.leftPos + 7, this.topPos + 53, 0, 0, 16, 16, 16, 16);

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/friendlymodeofficon.png"));
		this.blit(ms, this.leftPos + 7, this.topPos + 71, 0, 0, 16, 16, 16, 16);

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/friendly_transfur_true.png"));
		this.blit(ms, this.leftPos + 5, this.topPos + 97, 0, 0, 18, 18, 18, 18);

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/friendly_transfur_false.png"));
		this.blit(ms, this.leftPos + 5, this.topPos + 116, 0, 0, 18, 18, 18, 18);

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
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_6.label_normaltransfur_grab_mode_o"), 25, 28, -13421773);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_6.label_e_only_for_player"), 25, 38, -13421773);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_6.label_assimilation_grab_mode"), 25, 12, -12829636);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_6.label_these_are_the_modes_of_friendly"), 23, 55, -13421773);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_6.label_friendly_modes"), 23, 65, -13421773);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_6.label_n_this_are_want_or_not"), 23, 76, -13421773);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_6.label_ab"), 23, 86, -13421773);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_6.label_these_are_the_friendly_transf"), 23, 100, -13421773);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_6.label_ansfur_modes"), 24, 110, -13421773);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_6.label_mode_on"), 24, 120, -13421773);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_6.label_he_grabs_mode"), 24, 130, -13421773);
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
		button_close = new Button(this.leftPos + 121, this.topPos + 142, 51, 20, new TranslatableComponent("gui.changed_addon.book_pagenumber_6.button_close"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new BookPagenumber6ButtonMessage(0, x, y, z));
				BookPagenumber6ButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		});
		guistate.put("button:button_close", button_close);
		this.addRenderableWidget(button_close);
		button_next = new Button(this.leftPos + 65, this.topPos + 142, 46, 20, new TranslatableComponent("gui.changed_addon.book_pagenumber_6.button_next"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new BookPagenumber6ButtonMessage(1, x, y, z));
				BookPagenumber6ButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		});
		guistate.put("button:button_next", button_next);
		this.addRenderableWidget(button_next);
	}
}
