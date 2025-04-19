
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

import net.foxyas.changedaddon.world.inventory.UnifuserguiMenu;
import net.foxyas.changedaddon.procedures.RecipeProgressProcedure;
import net.foxyas.changedaddon.procedures.Ifisempty2Procedure;
import net.foxyas.changedaddon.procedures.IfisEmptyProcedure;
import net.foxyas.changedaddon.procedures.IfisEmpty3Procedure;
import net.foxyas.changedaddon.procedures.IfShowUnifuserRecipesProcedure;
import net.foxyas.changedaddon.procedures.IfShowUnifuserRecipesPage1Procedure;
import net.foxyas.changedaddon.procedures.IfShowUnifuserRecipes2Procedure;
import net.foxyas.changedaddon.procedures.IfBlockisfullProcedure;
import net.foxyas.changedaddon.procedures.BlockstartinfoProcedure;
import net.foxyas.changedaddon.network.UnifuserguiButtonMessage;
import net.foxyas.changedaddon.ChangedAddonMod;

import java.util.HashMap;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

public class UnifuserguiScreen extends AbstractContainerScreen<UnifuserguiMenu> {
	private final static HashMap<String, Object> guistate = UnifuserguiMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	ImageButton imagebutton_recipe_buttom_normal;
	ImageButton imagebutton_litixcamonia_slot;
	ImageButton imagebutton_potiwhtlitixcamonia_slot;
	ImageButton imagebutton_syringewithlitixcamonia_slot;
	ImageButton imagebutton_litixcamonia_slot1;
	ImageButton imagebutton_hitbox_88x17;

	public UnifuserguiScreen(UnifuserguiMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 200;
		this.imageHeight = 187;
	}

	@Override
	public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTicks);
		this.renderTooltip(ms, mouseX, mouseY);
		if (IfisEmptyProcedure.execute(entity))
			if (mouseX > leftPos + 10 && mouseX < leftPos + 34 && mouseY > topPos + 41 && mouseY < topPos + 65)
				this.renderTooltip(ms, new TranslatableComponent("gui.changed_addon.unifusergui.tooltip_place_the_powders"), mouseX, mouseY);
		if (IfisEmpty3Procedure.execute(entity))
			if (mouseX > leftPos + 45 && mouseX < leftPos + 69 && mouseY > topPos + 53 && mouseY < topPos + 77)
				this.renderTooltip(ms, new TranslatableComponent("gui.changed_addon.unifusergui.tooltip_place_a_syringe_with_dna"), mouseX, mouseY);
		if (Ifisempty2Procedure.execute(entity))
			if (mouseX > leftPos + 10 && mouseX < leftPos + 34 && mouseY > topPos + 65 && mouseY < topPos + 89)
				this.renderTooltip(ms, new TranslatableComponent("gui.changed_addon.unifusergui.tooltip_put_the_second_ingredient"), mouseX, mouseY);
		if (IfShowUnifuserRecipesProcedure.execute(entity))
			if (mouseX > leftPos + -106 && mouseX < leftPos + -82 && mouseY > topPos + 3 && mouseY < topPos + 27)
				this.renderTooltip(ms, new TranslatableComponent("gui.changed_addon.unifusergui.tooltip_display_litixcamonia_recipe"), mouseX, mouseY);
		if (IfShowUnifuserRecipesProcedure.execute(entity))
			if (mouseX > leftPos + -82 && mouseX < leftPos + -58 && mouseY > topPos + 3 && mouseY < topPos + 27)
				this.renderTooltip(ms, new TranslatableComponent("gui.changed_addon.unifusergui.tooltip_display_pot_with_litixcamonia_r"), mouseX, mouseY);
		if (IfShowUnifuserRecipesProcedure.execute(entity))
			if (mouseX > leftPos + -58 && mouseX < leftPos + -34 && mouseY > topPos + 3 && mouseY < topPos + 27)
				this.renderTooltip(ms, new TranslatableComponent("gui.changed_addon.unifusergui.tooltip_display_litixcamonia_recipe1"), mouseX, mouseY);
		if (IfShowUnifuserRecipesProcedure.execute(entity))
			if (mouseX > leftPos + -34 && mouseX < leftPos + -10 && mouseY > topPos + 3 && mouseY < topPos + 27)
				this.renderTooltip(ms, new TranslatableComponent("gui.changed_addon.unifusergui.tooltip_display_litixcamonia_multiplier"), mouseX, mouseY);
		if (mouseX > leftPos + 88 && mouseX < leftPos + 112 && mouseY > topPos + 76 && mouseY < topPos + 100)
			this.renderTooltip(ms, new TranslatableComponent("gui.changed_addon.unifusergui.tooltip_display_recipes"), mouseX, mouseY);
	}

	@Override
	protected void renderBg(PoseStack ms, float partialTicks, int gx, int gy) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		if (IfShowUnifuserRecipesProcedure.execute(entity)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/unifuserextragui.png"));
			this.blit(ms, this.leftPos + -111, this.topPos + 0, 0, 0, 110, 187, 110, 187);
		}

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/unifusergui_new.png"));
		this.blit(ms, this.leftPos + 0, this.topPos + 0, 0, 0, 200, 187, 200, 187);

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
			this.blit(ms, this.leftPos + 84, this.topPos + 57, 0, 0, 32, 12, 32, 12);
		}
		if (true) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/bar_full.png"));
			this.blit(ms, this.leftPos + 84+2, this.topPos + 57+2, 0, 0, progressint, 8, progressint, 8);
		}

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/dusts.png"));
		this.blit(ms, this.leftPos + 15, this.topPos + 46, 0, 0, 16, 16, 16, 16);

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/syringe_withlitixcamonia_screen.png"));
		this.blit(ms, this.leftPos + 50, this.topPos + 57, 0, 0, 16, 16, 16, 16);

		if (IfShowUnifuserRecipesPage1Procedure.execute(entity)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/litixcamoniadisplayrecipe.png"));
			this.blit(ms, this.leftPos + -108, this.topPos + 34, 0, 0, 100, 52, 100, 52);
		}
		if (IfShowUnifuserRecipesPage1Procedure.execute(entity)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/litixcamoniamultiplierrecipe.png"));
			this.blit(ms, this.leftPos + -108, this.topPos + 88, 0, 0, 100, 52, 100, 52);
		}
		if (IfShowUnifuserRecipesProcedure.execute(entity)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/small_gray_bar.png"));
			this.blit(ms, this.leftPos + -102, this.topPos + 158, 0, 0, 88, 20, 88, 20);
		}
		if (IfShowUnifuserRecipes2Procedure.execute(entity)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/syringelitixcamoniadisplayrecipe.png"));
			this.blit(ms, this.leftPos + -108, this.topPos + 34, 0, 0, 100, 52, 100, 52);
		}
		if (IfShowUnifuserRecipes2Procedure.execute(entity)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/potwithlitixcamoniadisplayrecipe.png"));
			this.blit(ms, this.leftPos + -108, this.topPos + 88, 0, 0, 100, 52, 100, 52);
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

				BlockstartinfoProcedure.execute(world, x, y, z), 9, 10, -12829636);
		if (IfBlockisfullProcedure.execute(world, x, y, z))
			this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.unifusergui.label_full"), 153, 78, -12829636);
		this.font.draw(poseStack,

				RecipeProgressProcedure.execute(world, x, y, z), 89, 47, -12829636);
		if (IfShowUnifuserRecipesProcedure.execute(entity))
			this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.unifusergui.label_3"), -26, 14, -1);
		if (IfShowUnifuserRecipesProcedure.execute(entity))
			this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.unifusergui.label_next_page"), -82, 165, -1);
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
		imagebutton_recipe_buttom_normal = new ImageButton(this.leftPos + 90, this.topPos + 80, 20, 18, 0, 0, 18, new ResourceLocation("changed_addon:textures/screens/atlas/imagebutton_recipe_buttom_normal.png"), 20, 36, e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new UnifuserguiButtonMessage(0, x, y, z));
				UnifuserguiButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		});
		guistate.put("button:imagebutton_recipe_buttom_normal", imagebutton_recipe_buttom_normal);
		this.addRenderableWidget(imagebutton_recipe_buttom_normal);
		imagebutton_litixcamonia_slot = new ImageButton(this.leftPos + -102, this.topPos + 7, 16, 16, 0, 0, 16, new ResourceLocation("changed_addon:textures/screens/atlas/imagebutton_litixcamonia_slot.png"), 16, 32, e -> {
			if (IfShowUnifuserRecipesProcedure.execute(entity)) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new UnifuserguiButtonMessage(1, x, y, z));
				UnifuserguiButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		}) {
			@Override
			public void render(PoseStack ms, int gx, int gy, float ticks) {
				if (IfShowUnifuserRecipesProcedure.execute(entity))
					super.render(ms, gx, gy, ticks);
			}
		};
		guistate.put("button:imagebutton_litixcamonia_slot", imagebutton_litixcamonia_slot);
		this.addRenderableWidget(imagebutton_litixcamonia_slot);
		imagebutton_potiwhtlitixcamonia_slot = new ImageButton(this.leftPos + -79, this.topPos + 7, 16, 16, 0, 0, 16, new ResourceLocation("changed_addon:textures/screens/atlas/imagebutton_potiwhtlitixcamonia_slot.png"), 16, 32, e -> {
			if (IfShowUnifuserRecipesProcedure.execute(entity)) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new UnifuserguiButtonMessage(2, x, y, z));
				UnifuserguiButtonMessage.handleButtonAction(entity, 2, x, y, z);
			}
		}) {
			@Override
			public void render(PoseStack ms, int gx, int gy, float ticks) {
				if (IfShowUnifuserRecipesProcedure.execute(entity))
					super.render(ms, gx, gy, ticks);
			}
		};
		guistate.put("button:imagebutton_potiwhtlitixcamonia_slot", imagebutton_potiwhtlitixcamonia_slot);
		this.addRenderableWidget(imagebutton_potiwhtlitixcamonia_slot);
		imagebutton_syringewithlitixcamonia_slot = new ImageButton(this.leftPos + -55, this.topPos + 7, 16, 16, 0, 0, 16, new ResourceLocation("changed_addon:textures/screens/atlas/imagebutton_syringewithlitixcamonia_slot.png"), 16, 32, e -> {
			if (IfShowUnifuserRecipesProcedure.execute(entity)) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new UnifuserguiButtonMessage(3, x, y, z));
				UnifuserguiButtonMessage.handleButtonAction(entity, 3, x, y, z);
			}
		}) {
			@Override
			public void render(PoseStack ms, int gx, int gy, float ticks) {
				if (IfShowUnifuserRecipesProcedure.execute(entity))
					super.render(ms, gx, gy, ticks);
			}
		};
		guistate.put("button:imagebutton_syringewithlitixcamonia_slot", imagebutton_syringewithlitixcamonia_slot);
		this.addRenderableWidget(imagebutton_syringewithlitixcamonia_slot);
		imagebutton_litixcamonia_slot1 = new ImageButton(this.leftPos + -32, this.topPos + 7, 16, 16, 0, 0, 16, new ResourceLocation("changed_addon:textures/screens/atlas/imagebutton_litixcamonia_slot1.png"), 16, 32, e -> {
			if (IfShowUnifuserRecipesProcedure.execute(entity)) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new UnifuserguiButtonMessage(4, x, y, z));
				UnifuserguiButtonMessage.handleButtonAction(entity, 4, x, y, z);
			}
		}) {
			@Override
			public void render(PoseStack ms, int gx, int gy, float ticks) {
				if (IfShowUnifuserRecipesProcedure.execute(entity))
					super.render(ms, gx, gy, ticks);
			}
		};
		guistate.put("button:imagebutton_litixcamonia_slot1", imagebutton_litixcamonia_slot1);
		this.addRenderableWidget(imagebutton_litixcamonia_slot1);
		imagebutton_hitbox_88x17 = new ImageButton(this.leftPos + -102, this.topPos + 158, 88, 20, 0, 0, 20, new ResourceLocation("changed_addon:textures/screens/atlas/imagebutton_hitbox_88x17.png"), 88, 40, e -> {
			if (IfShowUnifuserRecipesProcedure.execute(entity)) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new UnifuserguiButtonMessage(5, x, y, z));
				UnifuserguiButtonMessage.handleButtonAction(entity, 5, x, y, z);
			}
		}) {
			@Override
			public void render(PoseStack ms, int gx, int gy, float ticks) {
				if (IfShowUnifuserRecipesProcedure.execute(entity))
					super.render(ms, gx, gy, ticks);
			}
		};
		guistate.put("button:imagebutton_hitbox_88x17", imagebutton_hitbox_88x17);
		this.addRenderableWidget(imagebutton_hitbox_88x17);
	}
}
