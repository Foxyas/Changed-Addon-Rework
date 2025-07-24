
package net.foxyas.changedaddon.item;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;

import net.foxyas.changedaddon.procedures.TransfurTotemRightClickedProcedure;
import net.foxyas.changedaddon.procedures.TransfurTotemMakeItemGlowProcedure;
import net.foxyas.changedaddon.procedures.TransfurTotemItemInInventoryTickProcedure;
import net.foxyas.changedaddon.init.ChangedAddonTabs;
import org.jetbrains.annotations.NotNull;

public class TransfurTotemItem extends Item {

	public TransfurTotemItem() {
		super(new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON).stacksTo(1).fireResistant().rarity(Rarity.RARE));
	}

	@Override
	public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemstack) {
		return UseAnim.BLOCK;
	}

	@Override
	public boolean hasCraftingRemainingItem() {
		return true;
	}

	@Override
	public ItemStack getContainerItem(ItemStack itemstack) {
		return new ItemStack(this);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isFoil(@NotNull ItemStack itemstack) {
		return TransfurTotemMakeItemGlowProcedure.execute(itemstack);
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player entity, @NotNull InteractionHand hand) {
		InteractionResultHolder<ItemStack> ar = super.use(level, entity, hand);
		ItemStack itemstack = ar.getObject();
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();

		TransfurTotemRightClickedProcedure.execute(level, x, y, z, entity, itemstack);
		return ar;
	}

	@Override
	public void inventoryTick(@NotNull ItemStack itemstack, @NotNull Level world, @NotNull Entity entity, int slot, boolean selected) {
		super.inventoryTick(itemstack, world, entity, slot, selected);
		TransfurTotemItemInInventoryTickProcedure.execute(world, entity.getX(), entity.getY(), entity.getZ(), entity, itemstack);
	}
}
