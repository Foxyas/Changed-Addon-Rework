package net.foxyas.changedaddon.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.item.ItemStack;

import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.Screen;

import net.foxyas.changedaddon.init.ChangedAddonModItems;

import javax.annotation.Nullable;

import java.util.List;

@Mod.EventBusSubscriber
public class ShowTransfurTotemItemtipProcedure {
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event) {
		execute(event, event.getItemStack(), event.getToolTip());
	}

	public static void execute(ItemStack itemstack, List<Component> tooltip) {
		execute(null, itemstack, tooltip);
	}

	private static void execute(@Nullable Event event, ItemStack itemstack, List<Component> tooltip) {
		if (tooltip == null)
			return;
		String ID = "";
		if (itemstack.getItem() == ChangedAddonModItems.TRANSFUR_TOTEM.get()) {
			if ((itemstack.getOrCreateTag().getString("form")).isEmpty()) {
				tooltip.add(Component.literal("\u00A76No Form Linked"));
			} else {
				if (Screen.hasShiftDown() && !Screen.hasAltDown() && !Screen.hasControlDown()) {
					tooltip.add(Component.literal(("\u00A76Form=" + itemstack.getOrCreateTag().getString("form"))));
				} else if (Screen.hasAltDown() && Screen.hasControlDown()) {
					tooltip.add(Component.literal((Component.translatable("item.changed_addon.transfur_totem.desc_1").getString())));
				} else {
					ID = net.ltxprogrammer.changed.item.Syringe.getVariantDescriptionId(itemstack);
					tooltip.add(Component.literal(("\u00A76(" + Component.translatable(ID).getString() + ")")));
				}
			}
		}
	}
}
