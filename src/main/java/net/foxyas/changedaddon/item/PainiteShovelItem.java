
package net.foxyas.changedaddon.item;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;

import net.foxyas.changedaddon.init.ChangedAddonModItems;

public class PainiteShovelItem extends ShovelItem {
	public PainiteShovelItem() {
		super(new Tier() {
			public int getUses() {
				return 3026;
			}

			public float getSpeed() {
				return 12f;
			}

			public float getAttackDamageBonus() {
				return 4.75f;
			}

			public int getLevel() {
				return 5;
			}

			public int getEnchantmentValue() {
				return 30;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(ChangedAddonModItems.PAINITE.get()));
			}
		}, 1, -2.8f, new Item.Properties().tab(null).fireResistant());
	}
}
