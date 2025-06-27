
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

import net.foxyas.changedaddon.world.inventory.Bookrecipepage6Menu;
import net.foxyas.changedaddon.network.Bookrecipepage6ButtonMessage;
import net.foxyas.changedaddon.ChangedAddonMod;

import java.util.HashMap;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

public class Bookrecipepage6Screen extends AbstractContainerScreen<Bookrecipepage6Menu> {
	private final static HashMap<String, Object> guistate = Bookrecipepage6Menu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_back;
	Button button_close;
	ImageButton imagebutton_recipe_buttom_normal;

	public Bookrecipepage6Screen(Bookrecipepage6Menu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 270;
		this.imageHeight = 144;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("changed_addon:textures/screens/bookrecipepage_6.png");

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

		RenderSystem.setShaderTexture(0, ResourceLocation.parse("changed_addon:textures/screens/unifuserscreen.png"));
		this.blit(ms, this.leftPos + 34, this.topPos + 23, 0, 0, 200, 104, 200, 104);

		RenderSystem.setShaderTexture(0, ResourceLocation.parse("changed_addon:textures/screens/litixcamonia_slot.png"));
		this.blit(ms, this.leftPos + 49, this.topPos + 68, 0, 0, 16, 16, 16, 16);

		RenderSystem.setShaderTexture(0, ResourceLocation.parse("changed_addon:textures/screens/potiwhtlitixcamonia_slot.png"));
		this.blit(ms, this.leftPos + 189, this.topPos + 80, 0, 0, 16, 16, 16, 16);

		RenderSystem.setShaderTexture(0, ResourceLocation.parse("changed_addon:textures/screens/catalyzed_dna_slot.png"));
		this.blit(ms, this.leftPos + 84, this.topPos + 80, 0, 0, 16, 16, 16, 16);

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
		this.font.draw(poseStack, Component.translatable("gui.changed_addon.bookrecipepage_6.label_unifuser"), 45, 35, -16777216);
		this.font.draw(poseStack, Component.translatable("gui.changed_addon.bookrecipepage_6.label_pany_potion"), 7, 9, -16776818);
		this.font.draw(poseStack, Component.translatable("gui.changed_addon.bookrecipepage_6.label_p"), 54, 96, -12829636);
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
		button_back = new Button(this.leftPos + 164, this.topPos + -21, 46, 20, Component.translatable("gui.changed_addon.bookrecipepage_6.button_back"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new Bookrecipepage6ButtonMessage(0, x, y, z));
				Bookrecipepage6ButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		});
		guistate.put("button:button_back", button_back);
		this.addRenderableWidget(button_back);
		button_close = new Button(this.leftPos + 215, this.topPos + -21, 51, 20, Component.translatable("gui.changed_addon.bookrecipepage_6.button_close"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new Bookrecipepage6ButtonMessage(1, x, y, z));
				Bookrecipepage6ButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		});
		guistate.put("button:button_close", button_close);
		this.addRenderableWidget(button_close);
		imagebutton_recipe_buttom_normal = new ImageButton(this.leftPos + 122, this.topPos + 106, 20, 18, 0, 0, 18, ResourceLocation.parse("changed_addon:textures/screens/atlas/imagebutton_recipe_buttom_normal.png"), 20, 36, e -> {
		});
		guistate.put("button:imagebutton_recipe_buttom_normal", imagebutton_recipe_buttom_normal);
		this.addRenderableWidget(imagebutton_recipe_buttom_normal);
	}
}
