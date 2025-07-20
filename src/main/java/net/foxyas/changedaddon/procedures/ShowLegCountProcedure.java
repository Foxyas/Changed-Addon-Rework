package net.foxyas.changedaddon.procedures;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ShowLegCountProcedure {
    public static String execute(LevelAccessor world, Entity entity, HashMap guistate) {
        if (entity == null || guistate == null)
            return "";
        double legs = 0;
        String type_form = "";
        String Item_form = "";
        if (world.isClientSide()) {
            type_form = ((guistate.containsKey("text:form") ? ((EditBox) guistate.get("text:form")).getValue() : "").replace(" ", "")).toLowerCase();
            type_form = type_form.replace(",", "");
            type_form = type_form.replace("\\", "");
            if ((entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(0)).getItem() : ItemStack.EMPTY).getItem() == Blocks.AIR.asItem()) {
                int LegsAmount = VariantUtilProcedure.GetLegs(type_form);
                return new TranslatableComponent("text.changed_addon.legs").getString() + LegsAmount;
            }
        }
        if (!((entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(0)).getItem() : ItemStack.EMPTY).getItem() == Blocks.AIR.asItem())) {
            Item_form = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(0)).getItem() : ItemStack.EMPTY).getOrCreateTag().getString("form");
            int LegsAmount = VariantUtilProcedure.GetLegs(Item_form);
            return new TranslatableComponent("text.changed_addon.legs").getString() + LegsAmount;
        }
        return (new TranslatableComponent("text.changed_addon.legs").getString()).replace("%s", "???");
    }
}
