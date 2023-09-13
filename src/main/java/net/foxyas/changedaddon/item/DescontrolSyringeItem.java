
package net.foxyas.changedaddon.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.entity.LivingEntity;

import net.foxyas.changedaddon.procedures.DescontrolSyringePlayerFinishesUsingItemProcedure;
import net.foxyas.changedaddon.init.ChangedAddonModTabs;

public class DescontrolSyringeItem extends Item {
	public DescontrolSyringeItem() {
		super(new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON).stacksTo(16).rarity(Rarity.RARE).food((new FoodProperties.Builder()).nutrition(4).saturationMod(3f).alwaysEat()

				.build()));
	}

	@Override
	public UseAnim getUseAnimation(ItemStack itemstack) {
		return UseAnim.DRINK;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack itemstack, Level world, LivingEntity entity) {
		ItemStack retval = super.finishUsingItem(itemstack, world, entity);
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();

		DescontrolSyringePlayerFinishesUsingItemProcedure.execute(world, x, y, z, entity);
		return retval;
	}
}
