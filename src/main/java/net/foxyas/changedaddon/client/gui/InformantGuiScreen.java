
package net.foxyas.changedaddon.client.gui;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.Minecraft;

import net.foxyas.changedaddon.world.inventory.InformantGuiMenu;
import net.foxyas.changedaddon.procedures.ShowSwimSpeedProcedure;
import net.foxyas.changedaddon.procedures.ShowLegCountProcedure;
import net.foxyas.changedaddon.procedures.ShowLandSpeedProcedure;
import net.foxyas.changedaddon.procedures.ShowJumpStrengthProcedure;
import net.foxyas.changedaddon.procedures.ShowCanGlideAndFlyProcedure;
import net.foxyas.changedaddon.procedures.ShowAdditionalHealthProcedure;
import net.foxyas.changedaddon.procedures.IfisEmptyProcedure;

import java.util.HashMap;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

public class InformantGuiScreen extends AbstractContainerScreen<InformantGuiMenu> {
	private final static HashMap<String, Object> guistate = InformantGuiMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	EditBox form;

	public InformantGuiScreen(InformantGuiMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 176;
		this.imageHeight = 195;
	}

	private static final ResourceLocation texture = new ResourceLocation("changed_addon:textures/screens/informant_gui.png");

	@Override
	public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTicks);
		form.render(ms, mouseX, mouseY, partialTicks);
		this.renderTooltip(ms, mouseX, mouseY);
		if (mouseX > leftPos + 4 && mouseX < leftPos + 28 && mouseY > topPos + 4 && mouseY < topPos + 28)
			this.renderTooltip(ms, new TranslatableComponent("gui.changed_addon.informant_gui.tooltip_type_the_form"), mouseX, mouseY);
		if (IfisEmptyProcedure.execute(entity))
			if (mouseX > leftPos + 147 && mouseX < leftPos + 171 && mouseY > topPos + 4 && mouseY < topPos + 28)
				this.renderTooltip(ms, new TranslatableComponent("gui.changed_addon.informant_gui.tooltip_put_a_syringe_with_a_form"), mouseX, mouseY);
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
		if (form.isFocused())
			return form.keyPressed(key, b, c);
		return super.keyPressed(key, b, c);
	}

	@Override
	public void containerTick() {
		super.containerTick();
		form.tick();
	}

	@Override
	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
		this.font.draw(poseStack,

				ShowLandSpeedProcedure.execute(world, entity, guistate), 5, 44, -12829636);
		this.font.draw(poseStack,

				ShowSwimSpeedProcedure.execute(world, entity, guistate), 5, 57, -12829636);
		this.font.draw(poseStack,

				ShowAdditionalHealthProcedure.execute(world, entity, guistate), 5, 31, -12829636);
		this.font.draw(poseStack,

				ShowLegCountProcedure.execute(world, entity, guistate), 5, 94, -12829636);
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.informant_gui.label_empty"), 13, 10, -12829636);
		this.font.draw(poseStack,

				ShowJumpStrengthProcedure.execute(world, entity, guistate), 5, 69, -12829636);
		this.font.draw(poseStack,

				ShowCanGlideAndFlyProcedure.execute(world, entity, guistate), 5, 82, -12829636);
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
		form = new EditBox(this.font, this.leftPos + 27, this.topPos + 5, 120, 20, new TranslatableComponent("gui.changed_addon.informant_gui.form")) {
			{
				setSuggestion(new TranslatableComponent("gui.changed_addon.informant_gui.form").getString());
			}

			@Override
			public void insertText(String text) {
				super.insertText(text);
				if (getValue().isEmpty())
					setSuggestion(new TranslatableComponent("gui.changed_addon.informant_gui.form").getString());
				else
					setSuggestion(null);
			}

			@Override
			public void moveCursorTo(int pos) {
				super.moveCursorTo(pos);
				if (getValue().isEmpty())
					setSuggestion(new TranslatableComponent("gui.changed_addon.informant_gui.form").getString());
				else
					setSuggestion(null);
			}
		};
		form.setMaxLength(32767);
		guistate.put("text:form", form);
		this.addWidget(this.form);
	}
}
