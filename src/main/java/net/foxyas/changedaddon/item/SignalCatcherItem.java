
package net.foxyas.changedaddon.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;

import net.foxyas.changedaddon.procedures.SignalCatcherOnPlayerStoppedUsingProcedure;
import net.foxyas.changedaddon.procedures.SignalBlockFeatureProcedure;
import net.foxyas.changedaddon.init.ChangedAddonModTabs;

public class SignalCatcherItem extends Item {
	public SignalCatcherItem() {
		super(new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON).stacksTo(64).rarity(Rarity.COMMON));
	}

	@Override
	public int getUseDuration(ItemStack itemstack) {
		return 15;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
		InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
		ItemStack itemstack = ar.getObject();
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();
		entity.startUsingItem(hand);
		return ar;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack itemstack, Level world, LivingEntity entity) {
		ItemStack retval = super.finishUsingItem(itemstack, world, entity);
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();

		SignalBlockFeatureProcedure.execute(world, x, y, z, entity, itemstack);
		return retval;
	}

	@Override
	public void releaseUsing(ItemStack itemstack, Level world, LivingEntity entity, int time) {
		SignalCatcherOnPlayerStoppedUsingProcedure.execute(entity, itemstack);
	}
}
