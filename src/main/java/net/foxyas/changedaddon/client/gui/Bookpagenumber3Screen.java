
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

import net.foxyas.changedaddon.world.inventory.Bookpagenumber3Menu;
import net.foxyas.changedaddon.network.Bookpagenumber3ButtonMessage;
import net.foxyas.changedaddon.ChangedAddonMod;

import java.util.HashMap;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

public class Bookpagenumber3Screen extends AbstractContainerScreen<Bookpagenumber3Menu> {
	private final static HashMap<String, Object> guistate = Bookpagenumber3Menu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_close;
	Button button_back;
	Button button_next;

	public Bookpagenumber3Screen(Bookpagenumber3Menu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 418;
		this.imageHeight = 220;
	}

	private static final ResourceLocation texture = new ResourceLocation("changed_addon:textures/screens/bookpagenumber_3.png");

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

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/craft4.png"));
		this.blit(ms, this.leftPos + 206, this.topPos + 4, 0, 0, 150, 70, 150, 70);

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/litixcamoniarecipe.png"));
		this.blit(ms, this.leftPos + 2, this.topPos + 2, 0, 0, 200, 94, 200, 94);

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/ammoniapowderrecipe.png"));
		this.blit(ms, this.leftPos + 3, this.topPos + 99, 0, 0, 200, 77, 200, 77);

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/ammoniarecipe.png"));
		this.blit(ms, this.leftPos + 206, this.topPos + 76, 0, 0, 200, 78, 200, 78);

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/unlatexbase_buttonv2.png"));
		this.blit(ms, this.leftPos + 17, this.topPos + 72, 0, 0, 16, 16, 16, 16);

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
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.bookpagenumber_3.label_fflowers"), 283, 7, -12829636);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.bookpagenumber_3.label_mmeat"), 248, 6, -12829636);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.bookpagenumber_3.label_sseed"), 211, 6, -12829636);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.bookpagenumber_3.label_unifuser"), 8, 5, -12829636);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.bookpagenumber_3.label_catlyzer"), 8, 106, -12829636);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.bookpagenumber_3.label_catlyzer1"), 212, 81, -12829636);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.bookpagenumber_3.label_for_turn_on_the_catlyzer_and_uni"), 4, 183, -16777216);
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
		button_close = new Button(this.leftPos + 361, this.topPos + 30, 51, 20, new TranslatableComponent("gui.changed_addon.bookpagenumber_3.button_close"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new Bookpagenumber3ButtonMessage(0, x, y, z));
				Bookpagenumber3ButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		});
		guistate.put("button:button_close", button_close);
		this.addRenderableWidget(button_close);
		button_back = new Button(this.leftPos + 366, this.topPos + 5, 46, 20, new TranslatableComponent("gui.changed_addon.bookpagenumber_3.button_back"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new Bookpagenumber3ButtonMessage(1, x, y, z));
				Bookpagenumber3ButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		});
		guistate.put("button:button_back", button_back);
		this.addRenderableWidget(button_back);
		button_next = new Button(this.leftPos + 356, this.topPos + 191, 46, 20, new TranslatableComponent("gui.changed_addon.bookpagenumber_3.button_next"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new Bookpagenumber3ButtonMessage(2, x, y, z));
				Bookpagenumber3ButtonMessage.handleButtonAction(entity, 2, x, y, z);
			}
		});
		guistate.put("button:button_next", button_next);
		this.addRenderableWidget(button_next);
	}
}
