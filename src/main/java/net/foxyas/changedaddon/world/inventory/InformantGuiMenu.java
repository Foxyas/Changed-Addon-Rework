
package net.foxyas.changedaddon.world.inventory;

import net.foxyas.changedaddon.block.entity.InformantBlockEntity;
import net.foxyas.changedaddon.init.ChangedAddonMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class InformantGuiMenu extends AbstractContainerMenu {

	public final Level level;
	public final Player player;
	private final ContainerLevelAccess access;
	public final InformantBlockEntity blockEntity;
	private final SlotItemHandler slot;

	public InformantGuiMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
		this(id, inv, (InformantBlockEntity) inv.player.level.getBlockEntity(extraData.readBlockPos()));
	}

	public InformantGuiMenu(int id, Inventory inv, InformantBlockEntity blockEntity){
		super(ChangedAddonMenus.INFORMANT_GUI, id);
		player = inv.player;
		level = player.level;

		this.blockEntity = blockEntity;
		access = ContainerLevelAccess.create(level, blockEntity.getBlockPos());

		slot = new SlotItemHandler(blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve().get(), 0, 152, 89);
		addSlot(slot);

		for (int si = 0; si < 3; ++si)
			for (int sj = 0; sj < 9; ++sj)
				addSlot(new Slot(inv, sj + (si + 1) * 9, 8 + sj * 18, 31 + 84 + si * 18));
		for (int si = 0; si < 9; ++si)
			addSlot(new Slot(inv, si, 8 + si * 18, 31 + 142));
	}

	public ItemStack getStackInSlot(){
		return slot.getItem();
	}

	@Override
	public boolean stillValid(@NotNull Player player) {
		return AbstractContainerMenu.stillValid(access, player, blockEntity.getBlockState().getBlock());
	}

	@Override
	public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index < 1) {
				if (!this.moveItemStackTo(itemstack1, 1, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
				slot.onQuickCraft(itemstack1, itemstack);
			} else if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
				if (index < 1 + 27) {
					if (!this.moveItemStackTo(itemstack1, 1 + 27, this.slots.size(), true)) {
						return ItemStack.EMPTY;
					}
				} else {
					if (!this.moveItemStackTo(itemstack1, 1, 1 + 27, false)) {
						return ItemStack.EMPTY;
					}
				}
				return ItemStack.EMPTY;
			}
			if (itemstack1.getCount() == 0) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(playerIn, itemstack1);
		}
		return itemstack;
	}

	@Override
	protected boolean moveItemStackTo(@NotNull ItemStack p_38904_, int p_38905_, int p_38906_, boolean p_38907_) {
		boolean flag = false;
		int i = p_38905_;
		if (p_38907_) {
			i = p_38906_ - 1;
		}
		if (p_38904_.isStackable()) {
			while (!p_38904_.isEmpty()) {
				if (p_38907_) {
					if (i < p_38905_) {
						break;
					}
				} else if (i >= p_38906_) {
					break;
				}
				Slot slot = this.slots.get(i);
				ItemStack itemstack = slot.getItem();
				if (slot.mayPlace(itemstack) && !itemstack.isEmpty() && ItemStack.isSameItemSameTags(p_38904_, itemstack)) {
					int j = itemstack.getCount() + p_38904_.getCount();
					int maxSize = Math.min(slot.getMaxStackSize(), p_38904_.getMaxStackSize());
					if (j <= maxSize) {
						p_38904_.setCount(0);
						itemstack.setCount(j);
						slot.set(itemstack);
						flag = true;
					} else if (itemstack.getCount() < maxSize) {
						p_38904_.shrink(maxSize - itemstack.getCount());
						itemstack.setCount(maxSize);
						slot.set(itemstack);
						flag = true;
					}
				}
				if (p_38907_) {
					--i;
				} else {
					++i;
				}
			}
		}
		if (!p_38904_.isEmpty()) {
			if (p_38907_) {
				i = p_38906_ - 1;
			} else {
				i = p_38905_;
			}
			while (true) {
				if (p_38907_) {
					if (i < p_38905_) {
						break;
					}
				} else if (i >= p_38906_) {
					break;
				}
				Slot slot1 = this.slots.get(i);
				ItemStack itemstack1 = slot1.getItem();
				if (itemstack1.isEmpty() && slot1.mayPlace(p_38904_)) {
					if (p_38904_.getCount() > slot1.getMaxStackSize()) {
						slot1.set(p_38904_.split(slot1.getMaxStackSize()));
					} else {
						slot1.set(p_38904_.split(p_38904_.getCount()));
					}
					slot1.setChanged();
					flag = true;
					break;
				}
				if (p_38907_) {
					--i;
				} else {
					++i;
				}
			}
		}
		return flag;
	}
}
