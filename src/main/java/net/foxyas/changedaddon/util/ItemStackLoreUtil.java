package net.foxyas.changedaddon.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ItemStackLoreUtil {

    /**
     * Appends one or more lore lines to an existing ItemStack without overwriting existing lore.
     */
    public static ItemStack addLore(ItemStack stack, Component... lines) {
        if (stack.isEmpty() || lines.length == 0) return stack;

        CompoundTag displayTag = stack.getOrCreateTagElement("display");
        ListTag loreList = displayTag.getList("Lore", Tag.TAG_STRING);

        for (Component line : lines) {
            String json = Component.Serializer.toJson(line);
            loreList.add(StringTag.valueOf(json));
        }

        displayTag.put("Lore", loreList);
        return stack;
    }

    /**
     * Replaces all existing lore on the ItemStack with the provided lines.
     */
    public static ItemStack setLore(ItemStack stack, Component... lines) {
        if (stack.isEmpty()) return stack;

        CompoundTag displayTag = stack.getOrCreateTagElement("display");
        ListTag loreList = new ListTag();

        for (Component line : lines) {
            String json = Component.Serializer.toJson(line);
            loreList.add(StringTag.valueOf(json));
        }

        displayTag.put("Lore", loreList);
        return stack;
    }

    /**
     * Overload for setLore using a List of Components.
     */
    public static ItemStack setLore(ItemStack stack, List<Component> lines) {
        return setLore(stack, lines.toArray(new Component[0]));
    }

    /**
     * Clears all lore from the ItemStack.
     */
    public static ItemStack clearLore(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("display", Tag.TAG_COMPOUND)) {
            CompoundTag displayTag = stack.getTagElement("display");
            if (displayTag != null) {
                displayTag.remove("Lore");
            }
        }
        return stack;
    }
}