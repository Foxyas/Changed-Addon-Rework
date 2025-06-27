package net.foxyas.changedaddon.procedures;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.gui.components.EditBox;

import java.util.function.Supplier;
import java.util.Map;
import java.util.HashMap;

public class ShowCanGlideAndFlyProcedure {
	public static String execute(LevelAccessor world, Entity entity, HashMap guistate) {
		if (entity == null || guistate == null)
			return "";
		double JumpLevel = 0;
		boolean canFlyOrGlide = false;
		String type_form = "";
		String Item_form = "";
		if (world.isClientSide()) {
			type_form = ((guistate.containsKey("text:form") ? ((EditBox) guistate.get("text:form")).getValue() : "").replace(" ", "")).toLowerCase();
			type_form = type_form.replace(",", "");
			type_form = type_form.replace("\\", "");
			if ((entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(0)).getItem() : ItemStack.EMPTY).getItem() == Blocks.AIR.asItem()) {
				canFlyOrGlide = VariantUtilProcedure.CanGlideandFly(type_form);;
				return Component.translatable("text.changed_addon.canGlide/Fly").getString() + "" + (canFlyOrGlide == true ? "\u00A7a" + canFlyOrGlide + "\u00A7r" : "\u00A7c" + canFlyOrGlide + "\u00A7r");
			}
		}
		if (!((entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(0)).getItem() : ItemStack.EMPTY).getItem() == Blocks.AIR.asItem())) {
			Item_form = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(0)).getItem() : ItemStack.EMPTY).getOrCreateTag().getString("form");
			canFlyOrGlide = VariantUtilProcedure.CanGlideandFly(Item_form);;
			return Component.translatable("text.changed_addon.canGlide/Fly").getString() + "" + (canFlyOrGlide == true ? "\u00A7a" + canFlyOrGlide + "\u00A7r" : "\u00A7c" + canFlyOrGlide + "\u00A7r");
		}
		return (Component.translatable("text.changed_addon.canGlide/Fly").getString()).replace("%s", "???");
	}
}
