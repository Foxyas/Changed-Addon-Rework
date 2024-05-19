package net.foxyas.changedaddon.procedures;

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

public class ShowLegCountProcedure {
	public static String execute(LevelAccessor world, Entity entity, HashMap guistate) {
		if (entity == null || guistate == null)
			return "";
		String type_form = "";
		double legs = 0;
		if (world.isClientSide()) {
			type_form = !(guistate.containsKey("text:form") ? ((EditBox) guistate.get("text:form")).getValue() : "").isEmpty()
					? (guistate.containsKey("text:form") ? ((EditBox) guistate.get("text:form")).getValue() : "")
					: (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(0)).getItem() : ItemStack.EMPTY).getOrCreateTag().getString("form");
			legs = (double) VariantUtilProcedure.GetLegs(type_form);;
			return (new TranslatableComponent("text.changed_addon.legs").getString()).replace("%s", "" + legs * 1);
		}
		return (new TranslatableComponent("text.changed_addon.legs").getString()).replace("%s", "???");
	}
}
