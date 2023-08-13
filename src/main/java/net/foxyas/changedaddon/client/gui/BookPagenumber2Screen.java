
package net.foxyas.changedaddon.client.gui;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.Minecraft;

import net.foxyas.changedaddon.world.inventory.BookPagenumber2Menu;
import net.foxyas.changedaddon.network.BookPagenumber2ButtonMessage;
import net.foxyas.changedaddon.ChangedAddonMod;

import java.util.HashMap;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

public class BookPagenumber2Screen extends AbstractContainerScreen<BookPagenumber2Menu> {
	private final static HashMap<String, Object> guistate = BookPagenumber2Menu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_close;
	Button button_back;
	Button button_next;
	ImageButton imagebutton_20221106_142902;

	public BookPagenumber2Screen(BookPagenumber2Menu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 418;
		this.imageHeight = 180;
	}

	private static final ResourceLocation texture = new ResourceLocation("changed_addon:textures/screens/book_pagenumber_2.png");

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

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/to_do_with_syring.png"));
		this.blit(ms, this.leftPos + 241, this.topPos + 27, 0, 0, 171, 76, 171, 76);

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
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_2.label_to_purify_the_latex_syringes_jus"), 5, 6, -12829636);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_2.label_untransfur_items"), 4, 49, -12829636);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_2.label_i_was_about_to_forget_but_we_cre"), 5, 17, -12829636);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_2.label_this_item_can_make_you_back_for"), 5, 28, -12829636);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_2.label_the_recipe_for_the_main_item_sho"), 0, 39, -12829636);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_2.label_click_on_the_litixcamonia_recipe"), 3, 61, -12829636);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_2.label_on_how_to_craft_with_litixcamon"), 0, 71, -12829636);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.book_pagenumber_2.label_click_here"), 73, 98, -16777216);
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
		button_close = new Button(this.leftPos + 359, this.topPos + 152, 51, 20, new TranslatableComponent("gui.changed_addon.book_pagenumber_2.button_close"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new BookPagenumber2ButtonMessage(0, x, y, z));
				BookPagenumber2ButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		});
		guistate.put("button:button_close", button_close);
		this.addRenderableWidget(button_close);
		button_back = new Button(this.leftPos + 364, this.topPos + 131, 46, 20, new TranslatableComponent("gui.changed_addon.book_pagenumber_2.button_back"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new BookPagenumber2ButtonMessage(1, x, y, z));
				BookPagenumber2ButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		});
		guistate.put("button:button_back", button_back);
		this.addRenderableWidget(button_back);
		button_next = new Button(this.leftPos + 308, this.topPos + 152, 46, 20, new TranslatableComponent("gui.changed_addon.book_pagenumber_2.button_next"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new BookPagenumber2ButtonMessage(2, x, y, z));
				BookPagenumber2ButtonMessage.handleButtonAction(entity, 2, x, y, z);
			}
		});
		guistate.put("button:button_next", button_next);
		this.addRenderableWidget(button_next);
		imagebutton_20221106_142902 = new ImageButton(this.leftPos + 5, this.topPos + 81, 200, 94, 0, 0, 94, new ResourceLocation("changed_addon:textures/screens/atlas/imagebutton_20221106_142902.png"), 200, 188, e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new BookPagenumber2ButtonMessage(3, x, y, z));
				BookPagenumber2ButtonMessage.handleButtonAction(entity, 3, x, y, z);
			}
		});
		guistate.put("button:imagebutton_20221106_142902", imagebutton_20221106_142902);
		this.addRenderableWidget(imagebutton_20221106_142902);
	}
}
