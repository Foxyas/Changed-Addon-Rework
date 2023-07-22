
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.foxyas.changedaddon.init;

import org.lwjgl.glfw.GLFW;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;

import net.foxyas.changedaddon.network.WantfriendlygrabMessage;
import net.foxyas.changedaddon.network.SetcangrabonMessage;
import net.foxyas.changedaddon.network.SetassimilatonMessage;
import net.foxyas.changedaddon.network.OpengrabescapeguiMessage;
import net.foxyas.changedaddon.network.OpenGrabRadialMessage;
import net.foxyas.changedaddon.network.OpenExtraDetailsMessage;
import net.foxyas.changedaddon.network.GrabKeybindMessage;
import net.foxyas.changedaddon.network.FriendlyGraboffMessage;
import net.foxyas.changedaddon.network.ActiveFriendlyModeMessage;
import net.foxyas.changedaddon.ChangedAddonMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class ChangedAddonModKeyMappings {
	public static final KeyMapping GRAB_KEYBIND = new KeyMapping("key.changed_addon.grab_keybind", GLFW.GLFW_KEY_G, "key.categories.grab_gui");
	public static final KeyMapping SETASSIMILATON = new KeyMapping("key.changed_addon.setassimilaton", GLFW.GLFW_KEY_F13, "key.categories.grab_gui");
	public static final KeyMapping ACTIVE_FRIENDLY_MODE = new KeyMapping("key.changed_addon.active_friendly_mode", GLFW.GLFW_KEY_F15, "key.categories.grab_gui");
	public static final KeyMapping FRIENDLY_GRABOFF = new KeyMapping("key.changed_addon.friendly_graboff", GLFW.GLFW_KEY_M, "key.categories.grab_gui");
	public static final KeyMapping OPENGRABESCAPEGUI = new KeyMapping("key.changed_addon.opengrabescapegui", GLFW.GLFW_KEY_B, "key.categories.grab_gui");
	public static final KeyMapping SETCANGRABON = new KeyMapping("key.changed_addon.setcangrabon", GLFW.GLFW_KEY_F14, "key.categories.grab_gui");
	public static final KeyMapping WANTFRIENDLYGRAB = new KeyMapping("key.changed_addon.wantfriendlygrab", GLFW.GLFW_KEY_I, "key.categories.grab_gui");
	public static final KeyMapping OPEN_GRAB_RADIAL = new KeyMapping("key.changed_addon.open_grab_radial", GLFW.GLFW_KEY_H, "key.categories.grab_gui");
	public static final KeyMapping OPEN_EXTRA_DETAILS = new KeyMapping("key.changed_addon.open_extra_details", GLFW.GLFW_KEY_O, "key.categories.grab_gui");

	@SubscribeEvent
	public static void registerKeyBindings(FMLClientSetupEvent event) {
		ClientRegistry.registerKeyBinding(GRAB_KEYBIND);
		ClientRegistry.registerKeyBinding(SETASSIMILATON);
		ClientRegistry.registerKeyBinding(ACTIVE_FRIENDLY_MODE);
		ClientRegistry.registerKeyBinding(FRIENDLY_GRABOFF);
		ClientRegistry.registerKeyBinding(OPENGRABESCAPEGUI);
		ClientRegistry.registerKeyBinding(SETCANGRABON);
		ClientRegistry.registerKeyBinding(WANTFRIENDLYGRAB);
		ClientRegistry.registerKeyBinding(OPEN_GRAB_RADIAL);
		ClientRegistry.registerKeyBinding(OPEN_EXTRA_DETAILS);
	}

	@Mod.EventBusSubscriber({Dist.CLIENT})
	public static class KeyEventListener {
		@SubscribeEvent
		public static void onKeyInput(InputEvent.KeyInputEvent event) {
			if (Minecraft.getInstance().screen == null) {
				if (event.getKey() == GRAB_KEYBIND.getKey().getValue()) {
					if (event.getAction() == GLFW.GLFW_PRESS) {
						ChangedAddonMod.PACKET_HANDLER.sendToServer(new GrabKeybindMessage(0, 0));
						GrabKeybindMessage.pressAction(Minecraft.getInstance().player, 0, 0);
					}
				}
				if (event.getKey() == SETASSIMILATON.getKey().getValue()) {
					if (event.getAction() == GLFW.GLFW_PRESS) {
						ChangedAddonMod.PACKET_HANDLER.sendToServer(new SetassimilatonMessage(0, 0));
						SetassimilatonMessage.pressAction(Minecraft.getInstance().player, 0, 0);
					}
				}
				if (event.getKey() == ACTIVE_FRIENDLY_MODE.getKey().getValue()) {
					if (event.getAction() == GLFW.GLFW_PRESS) {
						ChangedAddonMod.PACKET_HANDLER.sendToServer(new ActiveFriendlyModeMessage(0, 0));
						ActiveFriendlyModeMessage.pressAction(Minecraft.getInstance().player, 0, 0);
					}
				}
				if (event.getKey() == FRIENDLY_GRABOFF.getKey().getValue()) {
					if (event.getAction() == GLFW.GLFW_PRESS) {
						ChangedAddonMod.PACKET_HANDLER.sendToServer(new FriendlyGraboffMessage(0, 0));
						FriendlyGraboffMessage.pressAction(Minecraft.getInstance().player, 0, 0);
					}
				}
				if (event.getKey() == OPENGRABESCAPEGUI.getKey().getValue()) {
					if (event.getAction() == GLFW.GLFW_PRESS) {
						ChangedAddonMod.PACKET_HANDLER.sendToServer(new OpengrabescapeguiMessage(0, 0));
						OpengrabescapeguiMessage.pressAction(Minecraft.getInstance().player, 0, 0);
					}
				}
				if (event.getKey() == SETCANGRABON.getKey().getValue()) {
					if (event.getAction() == GLFW.GLFW_PRESS) {
						ChangedAddonMod.PACKET_HANDLER.sendToServer(new SetcangrabonMessage(0, 0));
						SetcangrabonMessage.pressAction(Minecraft.getInstance().player, 0, 0);
					}
				}
				if (event.getKey() == WANTFRIENDLYGRAB.getKey().getValue()) {
					if (event.getAction() == GLFW.GLFW_PRESS) {
						ChangedAddonMod.PACKET_HANDLER.sendToServer(new WantfriendlygrabMessage(0, 0));
						WantfriendlygrabMessage.pressAction(Minecraft.getInstance().player, 0, 0);
					}
				}
				if (event.getKey() == OPEN_GRAB_RADIAL.getKey().getValue()) {
					if (event.getAction() == GLFW.GLFW_PRESS) {
						ChangedAddonMod.PACKET_HANDLER.sendToServer(new OpenGrabRadialMessage(0, 0));
						OpenGrabRadialMessage.pressAction(Minecraft.getInstance().player, 0, 0);
					}
				}
				if (event.getKey() == OPEN_EXTRA_DETAILS.getKey().getValue()) {
					if (event.getAction() == GLFW.GLFW_PRESS) {
						ChangedAddonMod.PACKET_HANDLER.sendToServer(new OpenExtraDetailsMessage(0, 0));
						OpenExtraDetailsMessage.pressAction(Minecraft.getInstance().player, 0, 0);
					}
				}
			}
		}
	}
}
