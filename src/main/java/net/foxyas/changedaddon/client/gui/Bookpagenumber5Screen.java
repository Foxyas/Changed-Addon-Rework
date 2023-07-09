
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

import net.foxyas.changedaddon.world.inventory.Bookpagenumber5Menu;
import net.foxyas.changedaddon.network.Bookpagenumber5ButtonMessage;
import net.foxyas.changedaddon.ChangedAddonMod;

import java.util.HashMap;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

public class Bookpagenumber5Screen extends AbstractContainerScreen<Bookpagenumber5Menu> {
	private final static HashMap<String, Object> guistate = Bookpagenumber5Menu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_back;

	public Bookpagenumber5Screen(Bookpagenumber5Menu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 420;
		this.imageHeight = 204;
	}

	private static final ResourceLocation texture = new ResourceLocation("changed_addon:textures/screens/bookpagenumber_5.png");

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

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/untransfurrecipe.png"));
		this.blit(ms, this.leftPos + 3, this.topPos + 5, 0, 0, 200, 93, 200, 93);

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/potwithlitixcamoniarecipe.png"));
		this.blit(ms, this.leftPos + 2, this.topPos + 100, 0, 0, 200, 90, 200, 90);

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/unlatexbase_craft.png"));
		this.blit(ms, this.leftPos + 212, this.topPos + 125, 0, 0, 121, 64, 121, 64);

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/catalyzeddna_recipe.png"));
		this.blit(ms, this.leftPos + 208, this.topPos + 15, 0, 0, 200, 90, 200, 90);

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
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.bookpagenumber_5.label_unifuser"), 8, 19, -12829636);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.bookpagenumber_5.label_unifuser1"), 8, 126, -12829636);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.bookpagenumber_5.label_craft_table"), 214, 112, -12829636);
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
		button_back = new Button(this.leftPos + 354, this.topPos + 167, 46, 20, new TranslatableComponent("gui.changed_addon.bookpagenumber_5.button_back"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new Bookpagenumber5ButtonMessage(0, x, y, z));
				Bookpagenumber5ButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		});
		guistate.put("button:button_back", button_back);
		this.addRenderableWidget(button_back);
	}
}
