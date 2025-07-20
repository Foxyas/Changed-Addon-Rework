
package net.foxyas.changedaddon.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.network.FightToKeepConsciousnessMinigameButtonMessage;
import net.foxyas.changedaddon.procedures.FightTokeepconsciousnessminigameValueProcedure;
import net.foxyas.changedaddon.world.inventory.FightToKeepConsciousnessMinigameMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.HashMap;

public class FightToKeepConsciousnessMinigameScreen extends AbstractContainerScreen<FightToKeepConsciousnessMinigameMenu> {
	private final static HashMap<String, Object> guistate = FightToKeepConsciousnessMinigameMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_fight;
	Button button_give_up;

	public FightToKeepConsciousnessMinigameScreen(FightToKeepConsciousnessMinigameMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 200;
		this.imageHeight = 166;
	}

	private static final ResourceLocation texture = new ResourceLocation("changed_addon:textures/screens/fight_tokeepconsciousnessminigame.png");

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
		this.font.draw(poseStack, new TranslatableComponent("gui.changed_addon.fight_tokeepconsciousnessminigame.label_fast_you_only_have_4_seconds"), 18, 9, -12829636);
		this.font.draw(poseStack,

				FightTokeepconsciousnessminigameValueProcedure.execute(entity), 74, 53, -12829636);
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
		button_fight = new Button(this.leftPos + 17, this.topPos + 72, 166, 20, new TranslatableComponent("gui.changed_addon.fight_tokeepconsciousnessminigame.button_fight"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new FightToKeepConsciousnessMinigameButtonMessage(0, x, y, z));
				FightToKeepConsciousnessMinigameButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		});
		guistate.put("button:button_fight", button_fight);
		this.addRenderableWidget(button_fight);
		button_give_up = new Button(this.leftPos + 17, this.topPos + 136, 166, 20, new TranslatableComponent("gui.changed_addon.fight_tokeepconsciousnessminigame.button_give_up"), e -> {
			if (true) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new FightToKeepConsciousnessMinigameButtonMessage(1, x, y, z));
				FightToKeepConsciousnessMinigameButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		});
		guistate.put("button:button_give_up", button_give_up);
		this.addRenderableWidget(button_give_up);
	}
}
