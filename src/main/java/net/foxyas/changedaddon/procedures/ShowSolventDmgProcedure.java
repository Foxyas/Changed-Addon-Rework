package net.foxyas.changedaddon.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.Screen;

import net.foxyas.changedaddon.init.ChangedAddonModEnchantments;

import javax.annotation.Nullable;

import java.util.List;

@Mod.EventBusSubscriber
public class ShowSolventDmgProcedure {
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
		double EnchantLevel = 0;
		double math = 0;
		double othermath = 0;
		EnchantLevel = EnchantmentHelper.getItemEnchantmentLevel(ChangedAddonModEnchantments.SOLVENT.get(), itemstack);
		if (EnchantLevel == 0) {
			othermath = 0.5;
		} else {
			othermath = EnchantLevel + 1;
		}
		if (EnchantLevel == 1) {
			math = 1;
		} else if (EnchantLevel == 0) {
			math = 0.5;
		} else if (EnchantLevel == 2) {
			math = 1.5;
		} else if (EnchantLevel == 3) {
			math = 2;
		} else if (EnchantLevel == 4) {
			math = 2.5;
		} else if (EnchantLevel == 5) {
			math = 3;
		} else {
			math = EnchantLevel * 0.5 + 0.5;
		}
		if (!(itemstack.getItem() instanceof BowItem) && !(itemstack.getItem() instanceof CrossbowItem)) {
			if (EnchantmentHelper.getItemEnchantmentLevel(ChangedAddonModEnchantments.SOLVENT.get(), itemstack) != 0) {
				if (Screen.hasShiftDown()) {
					tooltip.add(new TextComponent(("\u00A7r\u00A7e+" + math + "\u00A7r \u00A7nLatex Solvent Damage")));
				} else {
					tooltip.add(new TextComponent("Press \u00A7e<Shift>\u00A7r for show tooltip"));
				}
			}
		}
	}
}
