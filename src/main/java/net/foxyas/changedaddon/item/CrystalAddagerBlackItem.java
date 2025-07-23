
package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.init.ChangedAddonTabs;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

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
		}, 3, -2.4f, new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON));
	}

	@Override
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
		super.hurtEnemy(itemstack, entity, sourceentity);
		int tf = entity.getRandom().nextInt(6);

		TransfurVariant<?> var = switch (tf) {
			case 0 -> ChangedTransfurVariants.Gendered.DARK_LATEX_WOLVES.getMaleVariant();
			case 1 -> ChangedTransfurVariants.Gendered.DARK_LATEX_WOLVES.getFemaleVariant();
			case 2 -> ChangedTransfurVariants.DARK_DRAGON.get();
			case 3 -> ChangedTransfurVariants.DARK_LATEX_WOLF_PUP.get();
			case 4 -> ChangedTransfurVariants.DARK_LATEX_YUFENG.get();
			case 5 -> ChangedTransfurVariants.DARK_LATEX_WOLF_PARTIAL.get();
			default -> throw new IllegalStateException("This should never happen");
		};

		ProcessTransfur.progressTransfur(entity, 3, var, TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));
		return true;
	}
}
