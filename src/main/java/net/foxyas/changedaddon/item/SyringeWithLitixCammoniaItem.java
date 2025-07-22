
package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.init.ChangedAddonTabs;
import net.foxyas.changedaddon.procedures.SyringeWithLitixCammoniaLivingEntityIsHitWithItemProcedure;
import net.foxyas.changedaddon.procedures.SyringewithlitixcammoniaPlayerFinishesUsingItemProcedure;
import net.ltxprogrammer.changed.item.SpecializedAnimations;
import net.ltxprogrammer.changed.item.Syringe;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class SyringeWithLitixCammoniaItem extends Item implements SpecializedAnimations {
	public SyringeWithLitixCammoniaItem() {
		super(new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON).durability(2)
				.rarity(Rarity.UNCOMMON)
		);
	}

	@Override
	public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemstack) {
		return UseAnim.NONE;
	}

	@Override
	public int getUseDuration(@NotNull ItemStack itemstack) {
		return 20;
	}

	@Override
	public @NotNull ItemStack finishUsingItem(@NotNull ItemStack itemstack, @NotNull Level world, @NotNull LivingEntity entity) {
		ItemStack retval = super.finishUsingItem(itemstack, world, entity);
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();

		SyringewithlitixcammoniaPlayerFinishesUsingItemProcedure.execute(world, x, y, z, entity);
		return retval;
	}

	@Override
	public boolean hurtEnemy(@NotNull ItemStack itemstack, @NotNull LivingEntity entity, @NotNull LivingEntity sourceentity) {
		boolean retval = super.hurtEnemy(itemstack, entity, sourceentity);
		SyringeWithLitixCammoniaLivingEntityIsHitWithItemProcedure.execute(entity, sourceentity, itemstack);
		return retval;
	}

	@Nullable
	public SpecializedAnimations.AnimationHandler getAnimationHandler() {
		return new Syringe.SyringeAnimation(this);
	}
}
