
package net.foxyas.changedaddon.item;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.LivingEntity;

import net.foxyas.changedaddon.procedures.CrystalAddagerBlackLivingEntityIsHitWithToolProcedure;
import net.foxyas.changedaddon.init.ChangedAddonModTabs;

public class CrystalAddagerBlackItem extends SwordItem {
	public CrystalAddagerBlackItem() {
		super(new Tier() {
			public int getUses() {
				return 524;
			}

			public float getSpeed() {
				return 4f;
			}

			public float getAttackDamageBonus() {
				return 3f;
			}

			public int getLevel() {
				return 1;
			}

			public int getEnchantmentValue() {
				return 20;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of();
			}
		}, 3, -2.4f, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON));
	}

	@Override
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
		boolean retval = super.hurtEnemy(itemstack, entity, sourceentity);
		CrystalAddagerBlackLivingEntityIsHitWithToolProcedure.execute(entity);
		return retval;
	}
}
