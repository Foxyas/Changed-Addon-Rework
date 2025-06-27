
package net.foxyas.changedaddon.client.gui;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.Minecraft;

import net.foxyas.changedaddon.world.inventory.TranfurSoundsGuiMenu;
import net.foxyas.changedaddon.procedures.ReturnTypeTransfurSoundProcedure;
import net.foxyas.changedaddon.procedures.IfDogLatexProcedure;
import net.foxyas.changedaddon.procedures.IfCatlatexProcedure;
import net.foxyas.changedaddon.procedures.FoxyasGuiEntityDisplayProcedure;
import net.foxyas.changedaddon.procedures.CooldownResetProcedure;
import net.foxyas.changedaddon.procedures.CanRoarProcedure;
import net.foxyas.changedaddon.network.TranfurSoundsGuiButtonMessage;
import net.foxyas.changedaddon.ChangedAddonMod;

import java.util.HashMap;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

public class TranfurSoundsGuiScreen extends AbstractContainerScreen<TranfurSoundsGuiMenu> {
	private final static HashMap<String, Object> guistate = TranfurSoundsGuiMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_1;
	Button button_2;
	Button button_3;
	Button button_4;
	Button button_5;
	Button button_6;
	Button button_7;
	Button button_cooldown;
	Button button_61;

	public TranfurSoundsGuiScreen(TranfurSoundsGuiMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 176;
		this.imageHeight = 150;
	}

	@Override
	public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTicks);
		if (FoxyasGuiEntityDisplayProcedure.execute(entity) instanceof LivingEntity livingEntity) {
			InventoryScreen.renderEntityInInventory(this.leftPos + 89, this.topPos + 133, 30, (float) Math.atan((this.leftPos + 89 - mouseX) / 40.0), (float) Math.atan((this.topPos + 83 - mouseY) / 40.0), livingEntity);
		}
		this.renderTooltip(ms, mouseX, mouseY);
	}

	@Override
	protected void renderBg(PoseStack ms, float partialTicks, int gx, int gy) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
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
		this.font.draw(poseStack, Component.translatable("gui.changed_addon.tranfur_sounds_gui.label_transfursounds"), 49, -24, -1);
		this.font.draw(poseStack,

				ReturnTypeTransfurSoundProcedure.execute(entity), 36, -11, -12829636);
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
		button_1 = new Button(this.leftPos + 4, this.topPos + 6, 30, 20, Component.translatable("gui.changed_addon.tranfur_sounds_gui.button_1"), e -> {
			if (IfCatlatexProcedure.execute(entity)) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new TranfurSoundsGuiButtonMessage(0, x, y, z));
				TranfurSoundsGuiButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		}) {
			@Override
			public void render(PoseStack ms, int gx, int gy, float ticks) {
				if (IfCatlatexProcedure.execute(entity))
					super.render(ms, gx, gy, ticks);
			}
		};
		guistate.put("button:button_1", button_1);
		this.addRenderableWidget(button_1);
		button_2 = new Button(this.leftPos + 4, this.topPos + 28, 30, 20, Component.translatable("gui.changed_addon.tranfur_sounds_gui.button_2"), e -> {
			if (IfCatlatexProcedure.execute(entity)) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new TranfurSoundsGuiButtonMessage(1, x, y, z));
				TranfurSoundsGuiButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		}) {
			@Override
			public void render(PoseStack ms, int gx, int gy, float ticks) {
				if (IfCatlatexProcedure.execute(entity))
					super.render(ms, gx, gy, ticks);
			}
		};
		guistate.put("button:button_2", button_2);
		this.addRenderableWidget(button_2);
		button_3 = new Button(this.leftPos + 138, this.topPos + 7, 30, 20, Component.translatable("gui.changed_addon.tranfur_sounds_gui.button_3"), e -> {
			if (IfDogLatexProcedure.execute(entity)) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new TranfurSoundsGuiButtonMessage(2, x, y, z));
				TranfurSoundsGuiButtonMessage.handleButtonAction(entity, 2, x, y, z);
			}
		}) {
			@Override
			public void render(PoseStack ms, int gx, int gy, float ticks) {
				if (IfDogLatexProcedure.execute(entity))
					super.render(ms, gx, gy, ticks);
			}
		};
		guistate.put("button:button_3", button_3);
		this.addRenderableWidget(button_3);
		button_4 = new Button(this.leftPos + 138, this.topPos + 29, 30, 20, Component.translatable("gui.changed_addon.tranfur_sounds_gui.button_4"), e -> {
			if (IfDogLatexProcedure.execute(entity)) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new TranfurSoundsGuiButtonMessage(3, x, y, z));
				TranfurSoundsGuiButtonMessage.handleButtonAction(entity, 3, x, y, z);
			}
		}) {
			@Override
			public void render(PoseStack ms, int gx, int gy, float ticks) {
				if (IfDogLatexProcedure.execute(entity))
					super.render(ms, gx, gy, ticks);
			}
		};
		guistate.put("button:button_4", button_4);
		this.addRenderableWidget(button_4);
		button_5 = new Button(this.leftPos + 138, this.topPos + 51, 30, 20, Component.translatable("gui.changed_addon.tranfur_sounds_gui.button_5"), e -> {
			if (IfDogLatexProcedure.execute(entity)) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new TranfurSoundsGuiButtonMessage(4, x, y, z));
				TranfurSoundsGuiButtonMessage.handleButtonAction(entity, 4, x, y, z);
			}
		}) {
			@Override
			public void render(PoseStack ms, int gx, int gy, float ticks) {
				if (IfDogLatexProcedure.execute(entity))
					super.render(ms, gx, gy, ticks);
			}
		};
		guistate.put("button:button_5", button_5);
		this.addRenderableWidget(button_5);
		button_6 = new Button(this.leftPos + 4, this.topPos + 50, 30, 20, Component.translatable("gui.changed_addon.tranfur_sounds_gui.button_6"), e -> {
			if (IfCatlatexProcedure.execute(entity)) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new TranfurSoundsGuiButtonMessage(5, x, y, z));
				TranfurSoundsGuiButtonMessage.handleButtonAction(entity, 5, x, y, z);
			}
		}) {
			@Override
			public void render(PoseStack ms, int gx, int gy, float ticks) {
				if (IfCatlatexProcedure.execute(entity))
					super.render(ms, gx, gy, ticks);
			}
		};
		guistate.put("button:button_6", button_6);
		this.addRenderableWidget(button_6);
		button_7 = new Button(this.leftPos + 4, this.topPos + 72, 40, 20, Component.translatable("gui.changed_addon.tranfur_sounds_gui.button_7"), e -> {
			if (IfCatlatexProcedure.execute(entity)) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new TranfurSoundsGuiButtonMessage(6, x, y, z));
				TranfurSoundsGuiButtonMessage.handleButtonAction(entity, 6, x, y, z);
			}
		}) {
			@Override
			public void render(PoseStack ms, int gx, int gy, float ticks) {
				if (IfCatlatexProcedure.execute(entity))
					super.render(ms, gx, gy, ticks);
			}
		};
		guistate.put("button:button_7", button_7);
		this.addRenderableWidget(button_7);
		button_cooldown = new Button(this.leftPos + 47, this.topPos + 5, 77, 20, Component.translatable("gui.changed_addon.tranfur_sounds_gui.button_cooldown"), e -> {
			if (CooldownResetProcedure.execute(entity)) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new TranfurSoundsGuiButtonMessage(7, x, y, z));
				TranfurSoundsGuiButtonMessage.handleButtonAction(entity, 7, x, y, z);
			}
		}) {
			@Override
			public void render(PoseStack ms, int gx, int gy, float ticks) {
				if (CooldownResetProcedure.execute(entity))
					super.render(ms, gx, gy, ticks);
			}
		};
		guistate.put("button:button_cooldown", button_cooldown);
		this.addRenderableWidget(button_cooldown);
		button_61 = new Button(this.leftPos + 4, this.topPos + 94, 30, 20, Component.translatable("gui.changed_addon.tranfur_sounds_gui.button_61"), e -> {
			if (CanRoarProcedure.execute(entity)) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new TranfurSoundsGuiButtonMessage(8, x, y, z));
				TranfurSoundsGuiButtonMessage.handleButtonAction(entity, 8, x, y, z);
			}
		}) {
			@Override
			public void render(PoseStack ms, int gx, int gy, float ticks) {
				if (CanRoarProcedure.execute(entity))
					super.render(ms, gx, gy, ticks);
			}
		};
		guistate.put("button:button_61", button_61);
		this.addRenderableWidget(button_61);
	}
}
