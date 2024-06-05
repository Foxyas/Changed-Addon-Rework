
package net.foxyas.changedaddon.item;

import net.minecraftforge.common.crafting.CompoundIngredient;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;

import net.foxyas.changedaddon.procedures.ElectricKatanaLivingEntityIsHitWithToolProcedure;
import net.foxyas.changedaddon.procedures.ElectricKatanaEntitySwingsItemProcedure;
import net.foxyas.changedaddon.init.ChangedAddonModItems;

public class ElectricKatanaRedItem extends SwordItem {
	public ElectricKatanaRedItem() {
		super(new Tier() {
			public int getUses() {
				return 1536;
			}

			public float getSpeed() {
				return 4f;
			}

			public float getAttackDamageBonus() {
				return 8f;
			}

			public int getLevel() {
				return 1;
			}

			public int getEnchantmentValue() {
				return 30;
			}

			public Ingredient getRepairIngredient() {
				return CompoundIngredient.of(Ingredient.of(ItemTags.create(new ResourceLocation("changed_addon:tsc_katana_repair"))), Ingredient.of(new ItemStack(ChangedAddonModItems.ELECTRIC_KATANA_RED.get())));
			}
		}, 3, -2.2f, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT));
	}

	@Override
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
		boolean retval = super.hurtEnemy(itemstack, entity, sourceentity);
		ElectricKatanaLivingEntityIsHitWithToolProcedure.execute(entity, sourceentity);
		return retval;
	}

	@Override
	public boolean onEntitySwing(ItemStack itemstack, LivingEntity entity) {
		boolean retval = super.onEntitySwing(itemstack, entity);
		ElectricKatanaEntitySwingsItemProcedure.execute(entity);
		return retval;
	}
}
