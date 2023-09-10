
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
	ImageButton imagebutton_recipe_buttom_normal;

	public Bookpagenumber4Screen(Bookpagenumber4Menu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 270;
		this.imageHeight = 144;
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

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/catalyzer_screen.png"));
		this.blit(ms, this.leftPos + 34, this.topPos + 31, 0, 0, 200, 86, 200, 86);

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/catalyzed_dna_slot.png"));
		this.blit(ms, this.leftPos + 187, this.topPos + 75, 0, 0, 16, 16, 16, 16);

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
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.bookpagenumber_4.label_n_any_latex_or_blood_syringe"), 17, 12, -16777216);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.bookpagenumber_4.label_n"), 62, 78, -16729089);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.bookpagenumber_4.label_catalyzer"), 46, 41, -16777216);
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
		button_close = new Button(this.leftPos + 215, this.topPos + -21, 51, 20, new TranslatableComponent("gui.changed_addon.bookpagenumber_4.button_close"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new Bookpagenumber4ButtonMessage(0, x, y, z));
				Bookpagenumber4ButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		});
		guistate.put("button:button_close", button_close);
		this.addRenderableWidget(button_close);
		button_back = new Button(this.leftPos + 165, this.topPos + -21, 46, 20, new TranslatableComponent("gui.changed_addon.bookpagenumber_4.button_back"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new Bookpagenumber4ButtonMessage(1, x, y, z));
				Bookpagenumber4ButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		});
		guistate.put("button:button_back", button_back);
		this.addRenderableWidget(button_back);
		imagebutton_recipe_buttom_normal = new ImageButton(this.leftPos + 123, this.topPos + 95, 20, 18, 0, 0, 18, new ResourceLocation("changed_addon:textures/screens/atlas/imagebutton_recipe_buttom_normal.png"), 20, 36, e -> {
		});
		guistate.put("button:imagebutton_recipe_buttom_normal", imagebutton_recipe_buttom_normal);
		this.addRenderableWidget(imagebutton_recipe_buttom_normal);
	}
}
