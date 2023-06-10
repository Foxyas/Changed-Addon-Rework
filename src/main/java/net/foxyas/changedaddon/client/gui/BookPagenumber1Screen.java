
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
	Button button_next;
	Button button_close;

	public BookPagenumber1Screen(BookPagenumber1Menu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 423;
		this.imageHeight = 180;
	}

	private static final ResourceLocation texture = new ResourceLocation("changed_addon:textures/screens/book_pagenumber_1.png");

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

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/pixil-frame-0_6.png"));
		this.blit(ms, this.leftPos + 3, this.topPos + 73, 0, 0, 171, 76, 171, 76);

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/unlatexbase_buttonv2.png"));
		this.blit(ms, this.leftPos + 123, this.topPos + 111, 0, 0, 16, 16, 16, 16);

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
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_1.label_look_i_will_explain_some_simple"), 13, 4, -12829636);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_1.label_first_you_have_to_have_a_latex_b"), 3, 14, -12829636);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_1.label_latex_beings"), 4, 24, -12829636);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_1.label_the_craft"), 108, 79, -12829636);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_1.label_after_obtaining_a_latex_base_yo"), 37, 24, -12829636);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_1.label_syringe_with_your_own_blood_and"), 4, 35, -12829636);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_1.label_to_make_a_syringe_you_can_take_a"), 310, 35, -12829636);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_1.label_can_take_a_glass_bottle_and_an_i"), 4, 46, -12829636);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_1.label_recipes_for_each_latex_will_var"), 0, 56, -12829636);
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
		button_next = new Button(this.leftPos + 355, this.topPos + 149, 46, 20, new TranslatableComponent("gui.changed_addon.book_pagenumber_1.button_next"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new BookPagenumber1ButtonMessage(0, x, y, z));
				BookPagenumber1ButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		});
		guistate.put("button:button_next", button_next);
		this.addRenderableWidget(button_next);
		button_close = new Button(this.leftPos + 301, this.topPos + 149, 51, 20, new TranslatableComponent("gui.changed_addon.book_pagenumber_1.button_close"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new BookPagenumber1ButtonMessage(1, x, y, z));
				BookPagenumber1ButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		});
		guistate.put("button:button_close", button_close);
		this.addRenderableWidget(button_close);
	}
}
