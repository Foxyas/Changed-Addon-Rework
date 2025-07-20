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

public class ShowAdditionalHealthProcedure {
    public static String execute(LevelAccessor world, Entity entity, HashMap guistate) {
        if (entity == null || guistate == null)
            return "";
        double Hp = 0;
        String type_form = "";
        String Item_form = "";
        boolean canFlyOrGlide = false;
        if (world.isClientSide()) {
            type_form = ((guistate.containsKey("text:form") ? ((EditBox) guistate.get("text:form")).getValue() : "").replace(" ", "")).toLowerCase();
            type_form = type_form.replace(",", "");
            type_form = type_form.replace("\\", "");
            if ((entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(0)).getItem() : ItemStack.EMPTY).getItem() == Blocks.AIR.asItem()) {
                Hp = VariantUtilProcedure.GetExtraHp(type_form, (Player) entity);
                return new TranslatableComponent("text.changed_addon.additionalHealth").getString() + (Hp > 0 ? "\u00A7a+" + Hp / 2 + "\u00A7r" : "\u00A7c" + Hp / 2 + "\u00A7r")
                        + new TranslatableComponent("text.changed_addon.additionalHealth.Hearts").getString();
            }
        }
        if (!((entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(0)).getItem() : ItemStack.EMPTY).getItem() == Blocks.AIR.asItem())) {
            Item_form = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(0)).getItem() : ItemStack.EMPTY).getOrCreateTag().getString("form");
            Hp = VariantUtilProcedure.GetExtraHp(Item_form, (Player) entity);
            return new TranslatableComponent("text.changed_addon.additionalHealth").getString() + (Hp > 0 ? "\u00A7a+" + Hp / 2 + "\u00A7r" : "\u00A7c" + Hp / 2 + "\u00A7r")
                    + new TranslatableComponent("text.changed_addon.additionalHealth.Hearts").getString();
        }
        return (new TranslatableComponent("text.changed_addon.additionalHealth").getString()).replace("%s", "???");
    }
}
