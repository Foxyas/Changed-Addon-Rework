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

public class ShowAdditionalHealthProcedure {
	public static String execute(LevelAccessor world, Entity entity, HashMap guistate) {
		if (entity == null || guistate == null)
			return "";
		String type_form = "";
		double Hp = 0;
		if (world.isClientSide()) {
			type_form = !(guistate.containsKey("text:form") ? ((EditBox) guistate.get("text:form")).getValue() : "").isEmpty()
					? (guistate.containsKey("text:form") ? ((EditBox) guistate.get("text:form")).getValue() : "")
					: (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(0)).getItem() : ItemStack.EMPTY).getOrCreateTag().getString("form");
			Hp = (double) VariantUtilProcedure.GetExtraHp(type_form);;
			return "" + (new TranslatableComponent("text.changed_addon.additionalHealth").getString()).replace("%s", Hp > 0 ? "\u00A7a+" + Hp / 2 + "\u00A7r" : "\u00A7c-" + Hp / 2 + "\u00A7r");
		}
		return "" + (new TranslatableComponent("text.changed_addon.additionalHealth").getString()).replace("%s", "???");
	}
}
