package net.foxyas.changedaddon.procedures;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.gui.components.EditBox;

import java.util.function.Supplier;
import java.util.Map;
import java.util.HashMap;

public class ShowLandSpeedProcedure {
	public static String execute(LevelAccessor world, Entity entity, HashMap guistate) {
		if (entity == null || guistate == null)
			return "";
		double landSpeed = 0;
		String type_form = "";
		String Item_form = "";
		if (world.isClientSide()) {
			type_form = ((guistate.containsKey("text:form") ? ((EditBox) guistate.get("text:form")).getValue() : "").replace(" ", "")).toLowerCase();
			type_form = type_form.replace(",", "");
			type_form = type_form.replace("\\", "");
			if ((entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(0)).getItem() : ItemStack.EMPTY).getItem() == Blocks.AIR.asItem()) {
				landSpeed = (double) VariantUtilProcedure.GetLandSpeed(type_form, (Player) entity);
				return new TranslatableComponent("text.changed_addon.landspeed").getString() + "" + (landSpeed >= 0 ? "\u00A7a+" + (float) (landSpeed * 100) + "%" : "\u00A7c" + (float) (landSpeed * 100) + "%");
			}
		}
		if (!((entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(0)).getItem() : ItemStack.EMPTY).getItem() == Blocks.AIR.asItem())) {
			Item_form = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(0)).getItem() : ItemStack.EMPTY).getOrCreateTag().getString("form");
			landSpeed = (double) VariantUtilProcedure.GetLandSpeed(Item_form, (Player) entity);
			return new TranslatableComponent("text.changed_addon.landspeed").getString() + "" + (landSpeed >= 0 ? "\u00A7a+" + (float) (landSpeed * 100) + "%" : "\u00A7c" + (float) (landSpeed * 100) + "%");
		}
		return (new TranslatableComponent("text.changed_addon.landspeed").getString()).replace("%s", "???");
	}
}
