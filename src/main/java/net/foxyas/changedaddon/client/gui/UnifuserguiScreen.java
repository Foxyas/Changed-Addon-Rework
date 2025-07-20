
package net.foxyas.changedaddon.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.procedures.*;
import net.foxyas.changedaddon.world.inventory.UnifuserGuiMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class UnifuserguiScreen extends AbstractContainerScreen<UnifuserGuiMenu> {
	private final Level world;
	private final int x, y, z;
	private final Player entity;

	public UnifuserguiScreen(UnifuserGuiMenu container, Inventory inventory, Component text) {
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
	}

	@Override
	protected void renderBg(PoseStack ms, float partialTicks, int gx, int gy) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/unifusergui_new.png"));
		blit(ms, this.leftPos, this.topPos, 0, 0, 200, 187, 200, 187);

		BlockEntity be = world.getBlockEntity(new BlockPos(x, y, z));
		int progressint = be != null ? (int) (be.getTileData().getDouble("recipe_progress") / 3.57) : -1;

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/empty_bar.png"));
		blit(ms, this.leftPos + 84, this.topPos + 59, 0, 0, 32, 12, 32, 12);

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/bar_full.png"));
		blit(ms, this.leftPos + 84+2, this.topPos + 59+2, 0, 0, progressint, 8, progressint, 8);

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/dusts.png"));
		blit(ms, this.leftPos + 15, this.topPos + 46, 0, 0, 16, 16, 16, 16);

		RenderSystem.setShaderTexture(0, new ResourceLocation("changed_addon:textures/screens/syringe_withlitixcamonia_screen.png"));
		blit(ms, this.leftPos + 50, this.topPos + 57, 0, 0, 16, 16, 16, 16);

		RenderSystem.disableBlend();
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
		this.font.draw(poseStack, BlockstartinfoProcedure.execute(world, x, y, z), 9, 10, -12829636);
		if (IfBlockisfullProcedure.execute(world, x, y, z))
			this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.unifusergui.label_full"), 153, 78, -12829636);
		this.font.draw(poseStack, RecipeProgressProcedure.execute(world, x, y, z), 89, 47, -12829636);
	}
}
