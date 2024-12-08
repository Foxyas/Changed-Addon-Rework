
package net.foxyas.changedaddon.enchantment;

import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.entity.EquipmentSlot;

public class ChangedLureEnchantment extends Enchantment {
	public ChangedLureEnchantment(EquipmentSlot... slots) {
		super(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.FISHING_ROD, slots);
	}
}
