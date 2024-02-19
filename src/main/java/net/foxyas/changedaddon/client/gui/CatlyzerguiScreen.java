
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

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

import net.foxyas.changedaddon.world.inventory.CatlyzerguiMenu;
import net.foxyas.changedaddon.procedures.ShowfullbarProcedure;
import net.foxyas.changedaddon.procedures.Show90porcentbarProcedure;
import net.foxyas.changedaddon.procedures.Show75porcentbarProcedure;
import net.foxyas.changedaddon.procedures.Show50porcentbarProcedure;
import net.foxyas.changedaddon.procedures.Show25porcentbarProcedure;
import net.foxyas.changedaddon.procedures.Show10porcentbarProcedure;
import net.foxyas.changedaddon.procedures.Show0porcentbarProcedure;
import net.foxyas.changedaddon.procedures.RecipeProgressProcedure;
import net.foxyas.changedaddon.procedures.IfisEmptyProcedure;
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
	ImageButton imagebutton_20221106_142902;
	ImageButton imagebutton_impureammoniawithslot;
	ImageButton imagebutton_catalyzed_dna_slot;

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
		if (IfShowCatlyzerRecipePage1Procedure.execute(entity))
			if (mouseX > leftPos + -60 && mouseX < leftPos + -36 && mouseY > topPos + 147 && mouseY < topPos + 171)
				this.renderTooltip(ms, new TranslatableComponent("gui.changed_addon.catlyzergui.tooltip_display_recipe_of_syringe_with_c"), mouseX, mouseY);
		if (IfShowCatlyzerRecipePage1Procedure.execute(entity))
			if (mouseX > leftPos + -84 && mouseX < leftPos + -60 && mouseY > topPos + 147 && mouseY < topPos + 171)
				this.renderTooltip(ms, new TranslatableComponent("gui.changed_addon.catlyzergui.tooltip_display_recipe_of_ammonia_partic"), mouseX, mouseY);
		if (IfShowCatlyzerRecipePage1Procedure.execute(entity))
			if (mouseX > leftPos + -108 && mouseX < leftPos + -84 && mouseY > topPos + 147 && mouseY < topPos + 171)
				this.renderTooltip(ms, new TranslatableComponent("gui.changed_addon.catlyzergui.tooltip_display_recipe_of_ammonia_powder"), mouseX, mouseY);
		if (IfisEmptyProcedure.execute(entity))
			if (mouseX > leftPos + 18 && mouseX < leftPos + 42 && mouseY > topPos + 40 && mouseY < topPos + 64)
				this.renderTooltip(ms, new TranslatableComponent("gui.changed_addon.catlyzergui.tooltip_put_the_powders_or_syringe"), mouseX, mouseY);
		if (mouseX > leftPos + 87 && mouseX < leftPos + 111 && mouseY > topPos + 61 && mouseY < topPos + 85)
			this.renderTooltip(ms, new TranslatableComponent("gui.changed_addon.catlyzergui.tooltip_display_recipes"), mouseX, mouseY);
	}

	@Override
	protected void renderBg(PoseStack ms, float partialTicks, int gx, int gy) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShaderTexture(0, texture);
		this.blit(ms, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
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

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/catlyzergui_new.png"));
		this.blit(ms, this.leftPos + 0, this.topPos + 0, 0, 0, 200, 170, 200, 170);

		double math = 0;
		double progress = 0;
		math = 100 / 32;
		progress = (new Object() {
			public double getValue(LevelAccessor world, BlockPos pos, String tag) {
				BlockEntity blockEntity = world.getBlockEntity(pos);
				if (blockEntity != null)
					return blockEntity.getTileData().getDouble(tag);
				return -1;
			}
		}.getValue(world, new BlockPos(x, y, z), "recipe_progress")) / 3.57;

		int progressint = (int) progress;
		
		if (true) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/empty_bar.png"));
			this.blit(ms, this.leftPos + 83, this.topPos + 46, 0, 0, 32, 12, 32, 12);
		}
				if (true) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/bar_full.png"));
			this.blit(ms, this.leftPos + 83+2, this.topPos + 46+2, 0, 0, progressint, 8, progressint, 8);
		}

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/dusts.png"));
		this.blit(ms, this.leftPos + 23, this.topPos + 45, 0, 0, 16, 16, 16, 16);

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
		this.font.draw(poseStack,

				RecipeProgressProcedure.execute(world, x, y, z), 90, 34, -12829636);
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
		imagebutton_knowledge_book = new ImageButton(this.leftPos + 89, this.topPos + 65, 20, 18, 0, 0, 18, new ResourceLocation("changed_addon:textures/screens/atlas/imagebutton_knowledge_book.png"), 20, 36, e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new CatlyzerguiButtonMessage(0, x, y, z));
				CatlyzerguiButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		});
		guistate.put("button:imagebutton_knowledge_book", imagebutton_knowledge_book);
		this.addRenderableWidget(imagebutton_knowledge_book);
		imagebutton_20221106_142902 = new ImageButton(this.leftPos + -104, this.topPos + 152, 16, 16, 0, 0, 16, new ResourceLocation("changed_addon:textures/screens/atlas/imagebutton_20221106_142902.png"), 16, 32, e -> {
			if (IfShowCatlyzerRecipePage1Procedure.execute(entity)) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new CatlyzerguiButtonMessage(1, x, y, z));
				CatlyzerguiButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		}) {
			@Override
			public void render(PoseStack ms, int gx, int gy, float ticks) {
				if (IfShowCatlyzerRecipePage1Procedure.execute(entity))
					super.render(ms, gx, gy, ticks);
			}
		};
		guistate.put("button:imagebutton_20221106_142902", imagebutton_20221106_142902);
		this.addRenderableWidget(imagebutton_20221106_142902);
		imagebutton_impureammoniawithslot = new ImageButton(this.leftPos + -80, this.topPos + 152, 16, 16, 0, 0, 16, new ResourceLocation("changed_addon:textures/screens/atlas/imagebutton_impureammoniawithslot.png"), 16, 32, e -> {
			if (IfShowCatlyzerRecipePage1Procedure.execute(entity)) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new CatlyzerguiButtonMessage(2, x, y, z));
				CatlyzerguiButtonMessage.handleButtonAction(entity, 2, x, y, z);
			}
		}) {
			@Override
			public void render(PoseStack ms, int gx, int gy, float ticks) {
				if (IfShowCatlyzerRecipePage1Procedure.execute(entity))
					super.render(ms, gx, gy, ticks);
			}
		};
		guistate.put("button:imagebutton_impureammoniawithslot", imagebutton_impureammoniawithslot);
		this.addRenderableWidget(imagebutton_impureammoniawithslot);
		imagebutton_catalyzed_dna_slot = new ImageButton(this.leftPos + -56, this.topPos + 152, 16, 16, 0, 0, 16, new ResourceLocation("changed_addon:textures/screens/atlas/imagebutton_catalyzed_dna_slot.png"), 16, 32, e -> {
			if (IfShowCatlyzerRecipePage1Procedure.execute(entity)) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new CatlyzerguiButtonMessage(3, x, y, z));
				CatlyzerguiButtonMessage.handleButtonAction(entity, 3, x, y, z);
			}
		}) {
			@Override
			public void render(PoseStack ms, int gx, int gy, float ticks) {
				if (IfShowCatlyzerRecipePage1Procedure.execute(entity))
					super.render(ms, gx, gy, ticks);
			}
		};
		guistate.put("button:imagebutton_catalyzed_dna_slot", imagebutton_catalyzed_dna_slot);
		this.addRenderableWidget(imagebutton_catalyzed_dna_slot);
	}
}
