
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.foxyas.changedaddon.init;

import org.lwjgl.glfw.GLFW;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;

import net.foxyas.changedaddon.network.TurnOffTransfurMessage;
import net.foxyas.changedaddon.network.PatKeyMessage;
import net.foxyas.changedaddon.network.OpenStruggleMenuMessage;
import net.foxyas.changedaddon.network.OpenExtraDetailsMessage;
import net.foxyas.changedaddon.network.LeapKeyMessage;
import net.foxyas.changedaddon.ChangedAddonMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class ChangedAddonKeyMappings {
	public static final KeyMapping OPEN_EXTRA_DETAILS = new KeyMapping("key.changed_addon.open_extra_details", GLFW.GLFW_KEY_UNKNOWN, "key.categories.changed_addon") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new OpenExtraDetailsMessage(0, 0));
				//OpenExtraDetailsMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping LEAP_KEY = new KeyMapping("key.changed_addon.leap_key", GLFW.GLFW_KEY_UNKNOWN, "key.categories.changed_addon") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new LeapKeyMessage(0, 0));
				LeapKeyMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping TURN_OFF_TRANSFUR = new KeyMapping("key.changed_addon.turn_off_transfur", GLFW.GLFW_KEY_UNKNOWN, "key.categories.changed_addon") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new TurnOffTransfurMessage(0, 0));
				TurnOffTransfurMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping PAT_KEY = new KeyMapping("key.changed_addon.pat_key", GLFW.GLFW_KEY_UNKNOWN, "key.categories.changed_addon") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new PatKeyMessage(0, 0));
				PatKeyMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping OPEN_STRUGGLE_MENU = new KeyMapping("key.changed_addon.open_struggle_menu", GLFW.GLFW_KEY_B, "key.categories.changed_addon") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new OpenStruggleMenuMessage(0, 0));
				OpenStruggleMenuMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};

	@SubscribeEvent
	public static void registerKeyBindings(FMLClientSetupEvent event) {
		ClientRegistry.registerKeyBinding(OPEN_EXTRA_DETAILS);
		ClientRegistry.registerKeyBinding(LEAP_KEY);
		ClientRegistry.registerKeyBinding(TURN_OFF_TRANSFUR);
		ClientRegistry.registerKeyBinding(PAT_KEY);
		ClientRegistry.registerKeyBinding(OPEN_STRUGGLE_MENU);
	}

	@Mod.EventBusSubscriber({Dist.CLIENT})
	public static class KeyEventListener {
		@SubscribeEvent
		public static void onClientTick(TickEvent.ClientTickEvent event) {
			if (Minecraft.getInstance().screen == null) {
				OPEN_EXTRA_DETAILS.consumeClick();
				LEAP_KEY.consumeClick();
				TURN_OFF_TRANSFUR.consumeClick();
				PAT_KEY.consumeClick();
				OPEN_STRUGGLE_MENU.consumeClick();
			}
		}
	}
}
