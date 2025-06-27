package net.foxyas.changedaddon.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;

import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.Screen;

import net.foxyas.changedaddon.init.ChangedAddonModItems;

import javax.annotation.Nullable;

import java.util.List;

@Mod.EventBusSubscriber
public class SignalCatherToolTipProcedure {
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event) {
		execute(event, event.getPlayer(), event.getItemStack(), event.getToolTip());
	}

	public static void execute(Entity entity, ItemStack itemstack, List<Component> tooltip) {
		execute(null, entity, itemstack, tooltip);
	}

	private static void execute(@Nullable Event event, Entity entity, ItemStack itemstack, List<Component> tooltip) {
		if (entity == null || tooltip == null)
			return;
		double motionZ = 0;
		double deltaZ = 0;
		double distance = 0;
		double deltaX = 0;
		double motionY = 0;
		double deltaY = 0;
		double motionX = 0;
		double maxSpeed = 0;
		double speed = 0;
		if (itemstack.getItem() == ChangedAddonModItems.SIGNAL_CATCHER.get()) {
			deltaX = itemstack.getOrCreateTag().getDouble("x") - entity.getX();
			deltaY = itemstack.getOrCreateTag().getDouble("y") - entity.getY();
			deltaZ = itemstack.getOrCreateTag().getDouble("z") - entity.getZ();
			distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
			if (!Screen.hasShiftDown()) {
				tooltip.add(new TextComponent("Hold \u00A76<Shift>\u00A7r for Info"));
			} else {
				tooltip.add(new TextComponent("Hold \u00A7b<Right Click>\u00A7r For scan a 32 blocks area"));
				tooltip.add(new TextComponent("Hold \u00A7c<Shift + Right Click>\u00A7r For Super scan and scan a 120 blocks area"));
			}
			tooltip.add(new TextComponent(("\u00A7oCords \u00A7l" + itemstack.getOrCreateTag().getDouble("x") + " " + itemstack.getOrCreateTag().getDouble("y") + " " + itemstack.getOrCreateTag().getDouble("z"))));
			if (itemstack.getOrCreateTag().getBoolean("set") == true) {
				tooltip.add(new TextComponent(("\u00A7oDistance \u00A7l" + Math.round(distance))));
			}
		}
	}
}
