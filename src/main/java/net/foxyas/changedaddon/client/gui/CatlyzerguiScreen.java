
package net.foxyas.changedaddon.client.gui;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.Minecraft;

import net.foxyas.changedaddon.world.inventory.CatlyzerguiMenu;
import net.foxyas.changedaddon.procedures.ShowfullbarProcedure;
import net.foxyas.changedaddon.procedures.Show90porcentbarProcedure;
import net.foxyas.changedaddon.procedures.Show75porcentbarProcedure;
import net.foxyas.changedaddon.procedures.Show50porcentbarProcedure;
import net.foxyas.changedaddon.procedures.Show25porcentbarProcedure;
import net.foxyas.changedaddon.procedures.Show10porcentbarProcedure;
import net.foxyas.changedaddon.procedures.Show0porcentbarProcedure;
import net.foxyas.changedaddon.procedures.IfShowUnifuserRecipesProcedure;
import net.foxyas.changedaddon.procedures.IfShowCatlyzerRecipePage1Procedure;
import net.foxyas.changedaddon.procedures.IfBlockisfullProcedure;
import net.foxyas.changedaddon.procedures.CatlyzerguiValueProcedure;
import net.foxyas.changedaddon.procedures.BlockstartinfoProcedure;
import net.foxyas.changedaddon.network.CatlyzerguiButtonMessage;
import net.foxyas.changedaddon.ChangedAddonMod;

import java.util.HashMap;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

public class CatlyzerguiScreen extends AbstractContainerScreen<CatlyzerguiMenu> {
	private final static HashMap<String, Object> guistate = CatlyzerguiMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	ImageButton imagebutton_knowledge_book;

	public CatlyzerguiScreen(CatlyzerguiMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 200;
		this.imageHeight = 170;
	}

	private static final ResourceLocation texture = new ResourceLocation("changed_addon:textures/screens/catlyzergui.png");

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
		if (Show10porcentbarProcedure.execute(world, x, y, z)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/bar_10_percent.png"));
			this.blit(ms, this.leftPos + 67, this.topPos + 19, 0, 0, 64, 64, 64, 64);
		}
		if (Show25porcentbarProcedure.execute(world, x, y, z)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/bar_25_percent.png"));
			this.blit(ms, this.leftPos + 67, this.topPos + 19, 0, 0, 64, 64, 64, 64);
		}
		if (Show50porcentbarProcedure.execute(world, x, y, z)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/bar_50_percent.png"));
			this.blit(ms, this.leftPos + 67, this.topPos + 19, 0, 0, 64, 64, 64, 64);
		}
		if (Show75porcentbarProcedure.execute(world, x, y, z)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/bar_75_percent.png"));
			this.blit(ms, this.leftPos + 67, this.topPos + 19, 0, 0, 64, 64, 64, 64);
		}
		if (Show90porcentbarProcedure.execute(world, x, y, z)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/bar_90_percent.png"));
			this.blit(ms, this.leftPos + 67, this.topPos + 19, 0, 0, 64, 64, 64, 64);
		}
		if (ShowfullbarProcedure.execute(world, x, y, z)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/barr_full.png"));
			this.blit(ms, this.leftPos + 67, this.topPos + 19, 0, 0, 64, 64, 64, 64);
		}
		if (Show0porcentbarProcedure.execute(world, x, y, z)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/bar_0_percent.png"));
			this.blit(ms, this.leftPos + 67, this.topPos + 19, 0, 0, 64, 64, 64, 64);
		}
		if (IfShowUnifuserRecipesProcedure.execute(entity)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/unifuserextragui.png"));
			this.blit(ms, this.leftPos + -111, this.topPos + -7, 0, 0, 110, 187, 110, 187);
		}
		if (IfShowCatlyzerRecipePage1Procedure.execute(entity)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/catalyzeddna_recipe.png"));
			this.blit(ms, this.leftPos + -107, this.topPos + 0, 0, 0, 100, 45, 100, 45);
		}
		if (IfShowCatlyzerRecipePage1Procedure.execute(entity)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/ammoniapowderrecipe.png"));
			this.blit(ms, this.leftPos + -107, this.topPos + 47, 0, 0, 100, 45, 100, 45);
		}
		if (IfShowCatlyzerRecipePage1Procedure.execute(entity)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/ammoniarecipe.png"));
			this.blit(ms, this.leftPos + -107, this.topPos + 94, 0, 0, 100, 45, 100, 45);
		}
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
		this.font.draw(poseStack,

				CatlyzerguiValueProcedure.execute(world, x, y, z), 6, 8, -12829636);
		this.font.draw(poseStack,

				BlockstartinfoProcedure.execute(world, x, y, z), 6, 20, -12829636);
		if (IfBlockisfullProcedure.execute(world, x, y, z))
			this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.catlyzergui.label_full"), 151, 65, -12829636);
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
		imagebutton_knowledge_book = new ImageButton(this.leftPos + 90, this.topPos + 67, 20, 18, 0, 0, 18, new ResourceLocation("changed_addon:textures/screens/atlas/imagebutton_knowledge_book.png"), 20, 36, e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new CatlyzerguiButtonMessage(0, x, y, z));
				CatlyzerguiButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		});
		guistate.put("button:imagebutton_knowledge_book", imagebutton_knowledge_book);
		this.addRenderableWidget(imagebutton_knowledge_book);
	}
}
