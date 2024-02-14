
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

import net.foxyas.changedaddon.network.SetcangrabonMessage;
import net.foxyas.changedaddon.network.SetassimilatonMessage;
import net.foxyas.changedaddon.network.OpengrabescapeguiMessage;
import net.foxyas.changedaddon.network.OpenExtraDetailsMessage;
import net.foxyas.changedaddon.network.DuctProneMessage;
import net.foxyas.changedaddon.network.ActiveFriendlyModeMessage;
import net.foxyas.changedaddon.ChangedAddonMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class ChangedAddonModKeyMappings {
	public static final KeyMapping SETASSIMILATON = new KeyMapping("key.changed_addon.setassimilaton", GLFW.GLFW_KEY_UNKNOWN, "key.categories.grab_gui") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new SetassimilatonMessage(0, 0));
				SetassimilatonMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping ACTIVE_FRIENDLY_MODE = new KeyMapping("key.changed_addon.active_friendly_mode", GLFW.GLFW_KEY_UNKNOWN, "key.categories.grab_gui") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new ActiveFriendlyModeMessage(0, 0));
				ActiveFriendlyModeMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping OPENGRABESCAPEGUI = new KeyMapping("key.changed_addon.opengrabescapegui", GLFW.GLFW_KEY_B, "key.categories.grab_gui") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new OpengrabescapeguiMessage(0, 0));
				OpengrabescapeguiMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping SETCANGRABON = new KeyMapping("key.changed_addon.setcangrabon", GLFW.GLFW_KEY_UNKNOWN, "key.categories.grab_gui") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new SetcangrabonMessage(0, 0));
				SetcangrabonMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping OPEN_EXTRA_DETAILS = new KeyMapping("key.changed_addon.open_extra_details", GLFW.GLFW_KEY_O, "key.categories.grab_gui") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new OpenExtraDetailsMessage(0, 0));
				OpenExtraDetailsMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping DUCT_PRONE = new KeyMapping("key.changed_addon.duct_prone", GLFW.GLFW_KEY_V, "key.categories.grab_gui") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new DuctProneMessage(0, 0));
				DuctProneMessage.pressAction(Minecraft.getInstance().player, 0, 0);
				DUCT_PRONE_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - DUCT_PRONE_LASTPRESS);
				ChangedAddonMod.PACKET_HANDLER.sendToServer(new DuctProneMessage(1, dt));
				DuctProneMessage.pressAction(Minecraft.getInstance().player, 1, dt);
			}
			isDownOld = isDown;
		}
	};
	private static long DUCT_PRONE_LASTPRESS = 0;

	@SubscribeEvent
	public static void registerKeyBindings(FMLClientSetupEvent event) {
		ClientRegistry.registerKeyBinding(SETASSIMILATON);
		ClientRegistry.registerKeyBinding(ACTIVE_FRIENDLY_MODE);
		ClientRegistry.registerKeyBinding(OPENGRABESCAPEGUI);
		ClientRegistry.registerKeyBinding(SETCANGRABON);
		ClientRegistry.registerKeyBinding(OPEN_EXTRA_DETAILS);
		ClientRegistry.registerKeyBinding(DUCT_PRONE);
	}

	@Mod.EventBusSubscriber({Dist.CLIENT})
	public static class KeyEventListener {
		@SubscribeEvent
		public static void onClientTick(TickEvent.ClientTickEvent event) {
			if (Minecraft.getInstance().screen == null) {
				SETASSIMILATON.consumeClick();
				ACTIVE_FRIENDLY_MODE.consumeClick();
				OPENGRABESCAPEGUI.consumeClick();
				SETCANGRABON.consumeClick();
				OPEN_EXTRA_DETAILS.consumeClick();
				DUCT_PRONE.consumeClick();
			}
		}
	}
}
