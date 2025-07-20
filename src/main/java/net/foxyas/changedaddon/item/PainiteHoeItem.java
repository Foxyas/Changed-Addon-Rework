
package net.foxyas.changedaddon.item;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.HoeItem;

import net.foxyas.changedaddon.init.ChangedAddonModTabs;
import net.foxyas.changedaddon.init.ChangedAddonItems;

public class PainiteHoeItem extends HoeItem {
	public PainiteHoeItem() {
		super(new Tier() {
			public int getUses() {
				return 3046;
			}

			public float getSpeed() {
				return 15f;
			}

			public float getAttackDamageBonus() {
				return 1f;
			}

			public int getLevel() {
				return 5;
			}

			public int getEnchantmentValue() {
				return 30;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(ChangedAddonItems.PAINITE.get()));
			}
		}, 0, -2f, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON_COMBAT_OPTIONAL).fireResistant());
	}
}
