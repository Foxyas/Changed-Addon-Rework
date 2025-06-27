
package net.foxyas.changedaddon.client.gui;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
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
	ImageButton imagebutton_ammoniabutton;
	ImageButton imagebutton_impureammoniawithslot;
	ImageButton imagebutton_unlatexbasebutton_slot;
	ImageButton imagebutton_ammoniaparticle_slot;
	ImageButton imagebutton_potiwhtlitixcamonia_slot;
	ImageButton imagebutton_syringewithlitixcamonia_slot;
	ImageButton imagebutton_litixcamoniabutton;
	ImageButton imagebutton_catalyzed_dna_slot;
	ImageButton imagebutton_laething_syringe_slot;

	public BookPagenumber2Screen(BookPagenumber2Menu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 270;
		this.imageHeight = 144;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("changed_addon:textures/screens/book_pagenumber_2.png");

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
		this.font.draw(poseStack, Component.translatable("gui.changed_addon.book_pagenumber_2.label_ammonia"), 36, 17, -12829636);
		this.font.draw(poseStack, Component.translatable("gui.changed_addon.book_pagenumber_2.label_click_in_item_for_recipe"), 3, 3, -16777216);
		this.font.draw(poseStack, Component.translatable("gui.changed_addon.book_pagenumber_2.label_impure_ammonia"), 35, 36, -12829636);
		this.font.draw(poseStack, Component.translatable("gui.changed_addon.book_pagenumber_2.label_anti_latex_base"), 35, 56, -12829636);
		this.font.draw(poseStack, Component.translatable("gui.changed_addon.book_pagenumber_2.label_ammonia_particle"), 35, 76, -12829636);
		this.font.draw(poseStack, Component.translatable("gui.changed_addon.book_pagenumber_2.label_pot_with_litixcamonia"), 35, 94, -12829636);
		this.font.draw(poseStack, Component.translatable("gui.changed_addon.book_pagenumber_2.label_syringe_with_litixcamonia"), 35, 114, -12829636);
		this.font.draw(poseStack, Component.translatable("gui.changed_addon.book_pagenumber_2.label_litixcamonia"), 150, 16, -12829636);
		this.font.draw(poseStack, Component.translatable("gui.changed_addon.book_pagenumber_2.label_catalyzed_dna"), 150, 36, -12829636);
		this.font.draw(poseStack, Component.translatable("gui.changed_addon.book_pagenumber_2.label_laethin_syringe"), 150, 55, -12829636);
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
		button_close = new Button(this.leftPos + 215, this.topPos + -21, 51, 20, Component.translatable("gui.changed_addon.book_pagenumber_2.button_close"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new BookPagenumber2ButtonMessage(0, x, y, z));
				BookPagenumber2ButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		});
		guistate.put("button:button_close", button_close);
		this.addRenderableWidget(button_close);
		button_back = new Button(this.leftPos + 165, this.topPos + -21, 46, 20, Component.translatable("gui.changed_addon.book_pagenumber_2.button_back"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new BookPagenumber2ButtonMessage(1, x, y, z));
				BookPagenumber2ButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		});
		guistate.put("button:button_back", button_back);
		this.addRenderableWidget(button_back);
		imagebutton_ammoniabutton = new ImageButton(this.leftPos + 16, this.topPos + 13, 16, 16, 0, 0, 16, ResourceLocation.parse("changed_addon:textures/screens/atlas/imagebutton_ammoniabutton.png"), 16, 32, e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new BookPagenumber2ButtonMessage(2, x, y, z));
				BookPagenumber2ButtonMessage.handleButtonAction(entity, 2, x, y, z);
			}
		});
		guistate.put("button:imagebutton_ammoniabutton", imagebutton_ammoniabutton);
		this.addRenderableWidget(imagebutton_ammoniabutton);
		imagebutton_impureammoniawithslot = new ImageButton(this.leftPos + 16, this.topPos + 33, 16, 16, 0, 0, 16, ResourceLocation.parse("changed_addon:textures/screens/atlas/imagebutton_impureammoniawithslot.png"), 16, 32, e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new BookPagenumber2ButtonMessage(3, x, y, z));
				BookPagenumber2ButtonMessage.handleButtonAction(entity, 3, x, y, z);
			}
		});
		guistate.put("button:imagebutton_impureammoniawithslot", imagebutton_impureammoniawithslot);
		this.addRenderableWidget(imagebutton_impureammoniawithslot);
		imagebutton_unlatexbasebutton_slot = new ImageButton(this.leftPos + 16, this.topPos + 53, 16, 16, 0, 0, 16, ResourceLocation.parse("changed_addon:textures/screens/atlas/imagebutton_unlatexbasebutton_slot.png"), 16, 32, e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new BookPagenumber2ButtonMessage(4, x, y, z));
				BookPagenumber2ButtonMessage.handleButtonAction(entity, 4, x, y, z);
			}
		});
		guistate.put("button:imagebutton_unlatexbasebutton_slot", imagebutton_unlatexbasebutton_slot);
		this.addRenderableWidget(imagebutton_unlatexbasebutton_slot);
		imagebutton_ammoniaparticle_slot = new ImageButton(this.leftPos + 16, this.topPos + 72, 16, 16, 0, 0, 16, ResourceLocation.parse("changed_addon:textures/screens/atlas/imagebutton_ammoniaparticle_slot.png"), 16, 32, e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new BookPagenumber2ButtonMessage(5, x, y, z));
				BookPagenumber2ButtonMessage.handleButtonAction(entity, 5, x, y, z);
			}
		});
		guistate.put("button:imagebutton_ammoniaparticle_slot", imagebutton_ammoniaparticle_slot);
		this.addRenderableWidget(imagebutton_ammoniaparticle_slot);
		imagebutton_potiwhtlitixcamonia_slot = new ImageButton(this.leftPos + 16, this.topPos + 91, 16, 16, 0, 0, 16, ResourceLocation.parse("changed_addon:textures/screens/atlas/imagebutton_potiwhtlitixcamonia_slot.png"), 16, 32, e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new BookPagenumber2ButtonMessage(6, x, y, z));
				BookPagenumber2ButtonMessage.handleButtonAction(entity, 6, x, y, z);
			}
		});
		guistate.put("button:imagebutton_potiwhtlitixcamonia_slot", imagebutton_potiwhtlitixcamonia_slot);
		this.addRenderableWidget(imagebutton_potiwhtlitixcamonia_slot);
		imagebutton_syringewithlitixcamonia_slot = new ImageButton(this.leftPos + 16, this.topPos + 111, 16, 16, 0, 0, 16, ResourceLocation.parse("changed_addon:textures/screens/atlas/imagebutton_syringewithlitixcamonia_slot.png"), 16, 32, e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new BookPagenumber2ButtonMessage(7, x, y, z));
				BookPagenumber2ButtonMessage.handleButtonAction(entity, 7, x, y, z);
			}
		});
		guistate.put("button:imagebutton_syringewithlitixcamonia_slot", imagebutton_syringewithlitixcamonia_slot);
		this.addRenderableWidget(imagebutton_syringewithlitixcamonia_slot);
		imagebutton_litixcamoniabutton = new ImageButton(this.leftPos + 132, this.topPos + 13, 16, 16, 0, 0, 16, ResourceLocation.parse("changed_addon:textures/screens/atlas/imagebutton_litixcamoniabutton.png"), 16, 32, e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new BookPagenumber2ButtonMessage(8, x, y, z));
				BookPagenumber2ButtonMessage.handleButtonAction(entity, 8, x, y, z);
			}
		});
		guistate.put("button:imagebutton_litixcamoniabutton", imagebutton_litixcamoniabutton);
		this.addRenderableWidget(imagebutton_litixcamoniabutton);
		imagebutton_catalyzed_dna_slot = new ImageButton(this.leftPos + 132, this.topPos + 33, 16, 16, 0, 0, 16, ResourceLocation.parse("changed_addon:textures/screens/atlas/imagebutton_catalyzed_dna_slot.png"), 16, 32, e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new BookPagenumber2ButtonMessage(9, x, y, z));
				BookPagenumber2ButtonMessage.handleButtonAction(entity, 9, x, y, z);
			}
		});
		guistate.put("button:imagebutton_catalyzed_dna_slot", imagebutton_catalyzed_dna_slot);
		this.addRenderableWidget(imagebutton_catalyzed_dna_slot);
		imagebutton_laething_syringe_slot = new ImageButton(this.leftPos + 132, this.topPos + 52, 16, 16, 0, 0, 16, ResourceLocation.parse("changed_addon:textures/screens/atlas/imagebutton_laething_syringe_slot.png"), 16, 32, e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new BookPagenumber2ButtonMessage(10, x, y, z));
				BookPagenumber2ButtonMessage.handleButtonAction(entity, 10, x, y, z);
			}
		});
		guistate.put("button:imagebutton_laething_syringe_slot", imagebutton_laething_syringe_slot);
		this.addRenderableWidget(imagebutton_laething_syringe_slot);
	}
}
