
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

import net.foxyas.changedaddon.world.inventory.UnifuserguiMenu;
import net.foxyas.changedaddon.procedures.ShowfullbarProcedure;
import net.foxyas.changedaddon.procedures.Show90porcentbarProcedure;
import net.foxyas.changedaddon.procedures.Show75porcentbarProcedure;
import net.foxyas.changedaddon.procedures.Show50porcentbarProcedure;
import net.foxyas.changedaddon.procedures.Show25porcentbarProcedure;
import net.foxyas.changedaddon.procedures.Show10porcentbarProcedure;
import net.foxyas.changedaddon.procedures.Show0porcentbarProcedure;
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
	ImageButton imagebutton_litixcamoniabutton;
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

	private static final ResourceLocation texture = new ResourceLocation("changed_addon:textures/screens/unifusergui.png");

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
			this.blit(ms, this.leftPos + 67, this.topPos + 6, 0, 0, 64, 64, 64, 64);
		}
		if (Show25porcentbarProcedure.execute(world, x, y, z)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/bar_25_percent.png"));
			this.blit(ms, this.leftPos + 67, this.topPos + 6, 0, 0, 64, 64, 64, 64);
		}
		if (Show50porcentbarProcedure.execute(world, x, y, z)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/bar_50_percent.png"));
			this.blit(ms, this.leftPos + 67, this.topPos + 6, 0, 0, 64, 64, 64, 64);
		}
		if (Show75porcentbarProcedure.execute(world, x, y, z)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/bar_75_percent.png"));
			this.blit(ms, this.leftPos + 67, this.topPos + 6, 0, 0, 64, 64, 64, 64);
		}
		if (Show90porcentbarProcedure.execute(world, x, y, z)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/bar_90_percent.png"));
			this.blit(ms, this.leftPos + 67, this.topPos + 6, 0, 0, 64, 64, 64, 64);
		}
		if (ShowfullbarProcedure.execute(world, x, y, z)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/barr_full.png"));
			this.blit(ms, this.leftPos + 67, this.topPos + 6, 0, 0, 64, 64, 64, 64);
		}
		if (Show0porcentbarProcedure.execute(world, x, y, z)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/bar_0_percent.png"));
			this.blit(ms, this.leftPos + 67, this.topPos + 6, 0, 0, 64, 64, 64, 64);
		}

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/dusts.png"));
		this.blit(ms, this.leftPos + 15, this.topPos + 46, 0, 0, 16, 16, 16, 16);

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/syringe_withlitixcamonia_screen.png"));
		this.blit(ms, this.leftPos + 50, this.topPos + 57, 0, 0, 16, 16, 16, 16);

		if (IfShowUnifuserRecipesProcedure.execute(entity)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/unifuserextragui.png"));
			this.blit(ms, this.leftPos + -111, this.topPos + 0, 0, 0, 110, 187, 110, 187);
		}
		if (IfShowUnifuserRecipesPage1Procedure.execute(entity)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/untransfurrecipe.png"));
			this.blit(ms, this.leftPos + -106, this.topPos + 6, 0, 0, 100, 46, 100, 46);
		}
		if (IfShowUnifuserRecipesPage1Procedure.execute(entity)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/potwithlitixcamoniarecipe.png"));
			this.blit(ms, this.leftPos + -106, this.topPos + 56, 0, 0, 100, 45, 100, 45);
		}
		if (IfShowUnifuserRecipesPage1Procedure.execute(entity)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/litixcamoniarecipe_new.png"));
			this.blit(ms, this.leftPos + -106, this.topPos + 104, 0, 0, 100, 47, 100, 47);
		}
		if (IfShowUnifuserRecipes2Procedure.execute(entity)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/multiplylitixcamoniarecipe.png"));
			this.blit(ms, this.leftPos + -106, this.topPos + 6, 0, 0, 100, 47, 100, 47);
		}
		if (IfShowUnifuserRecipesProcedure.execute(entity)) {
			RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/small_gray_bar.png"));
			this.blit(ms, this.leftPos + -102, this.topPos + 162, 0, 0, 88, 20, 88, 20);
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

				BlockstartinfoProcedure.execute(world, x, y, z), 5, 6, -12829636);
		if (IfBlockisfullProcedure.execute(world, x, y, z))
			this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.unifusergui.label_full"), 153, 78, -12829636);
		if (IfShowUnifuserRecipesProcedure.execute(entity))
			this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.unifusergui.label_next"), -83, 168, -1);
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
		imagebutton_litixcamoniabutton = new ImageButton(this.leftPos + 91, this.topPos + 83, 20, 18, 0, 0, 18, new ResourceLocation("changed_addon:textures/screens/atlas/imagebutton_litixcamoniabutton.png"), 20, 36, e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new UnifuserguiButtonMessage(0, x, y, z));
				UnifuserguiButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		});
		guistate.put("button:imagebutton_litixcamoniabutton", imagebutton_litixcamoniabutton);
		this.addRenderableWidget(imagebutton_litixcamoniabutton);
		imagebutton_hitbox_88x17 = new ImageButton(this.leftPos + -102, this.topPos + 162, 88, 17, 0, 0, 17, new ResourceLocation("changed_addon:textures/screens/atlas/imagebutton_hitbox_88x17.png"), 88, 34, e -> {
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
		guistate.put("button:imagebutton_hitbox_88x17", imagebutton_hitbox_88x17);
		this.addRenderableWidget(imagebutton_hitbox_88x17);
	}
}
