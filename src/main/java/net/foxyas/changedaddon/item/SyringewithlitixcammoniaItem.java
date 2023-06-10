
package net.foxyas.changedaddon.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.entity.LivingEntity;

import net.foxyas.changedaddon.procedures.SyringewithlitixcammoniaPlayerFinishesUsingItemProcedure;
import net.foxyas.changedaddon.procedures.SyringewithlitixcammoniaLivingEntityIsHitWithItemProcedure;
import net.foxyas.changedaddon.init.ChangedAddonModTabs;

public class SyringewithlitixcammoniaItem extends Item {
	public SyringewithlitixcammoniaItem() {
		super(new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON).stacksTo(32).rarity(Rarity.COMMON).food((new FoodProperties.Builder()).nutrition(4).saturationMod(3f).alwaysEat()

				.build()));
	}

	@Override
	public UseAnim getUseAnimation(ItemStack itemstack) {
		return UseAnim.DRINK;
	}

	@Override
	public int getUseDuration(ItemStack itemstack) {
		return 20;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack itemstack, Level world, LivingEntity entity) {
		ItemStack retval = super.finishUsingItem(itemstack, world, entity);
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();

		SyringewithlitixcammoniaPlayerFinishesUsingItemProcedure.execute(entity);
		return retval;
	}

	@Override
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
		boolean retval = super.hurtEnemy(itemstack, entity, sourceentity);
		SyringewithlitixcammoniaLivingEntityIsHitWithItemProcedure.execute(entity);
		return retval;
	}
}
