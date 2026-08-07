package net.foxyas.changedaddon.entity.defaults;

import net.foxyas.changedaddon.entity.ai.goals.generic.LookAndFollowTradingPlayerSink;
import net.foxyas.changedaddon.entity.ai.goals.generic.TradeWithPlayerGoal;
import net.foxyas.changedaddon.entity.api.ItemHandlerHolder;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.EntityArmorInvWrapper;
import net.minecraftforge.items.wrapper.EntityHandsInvWrapper;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Predicate;

public abstract class AbstractTraderChangedEntityWithInventory extends AbstractTraderChangedEntity implements InventoryCarrier, MenuProvider, ItemHandlerHolder {

    // Fields
    protected final SimpleContainer inventory;
    protected final MenuProvider menuProvider = new MenuProvider() {
        @Override
        public @NotNull Component getDisplayName() {
            return AbstractTraderChangedEntityWithInventory.this.getDisplayName();
        }

        @Override
        public @NotNull AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
            return AbstractTraderChangedEntityWithInventory.this.createMenu(i, inventory, player);
        }
    };

    protected final CombinedInvWrapper combinedInv;

    public AbstractTraderChangedEntityWithInventory(EntityType<? extends AbstractTraderChangedEntityWithInventory> type, Level world, int slots) {
        super(type, world);
        this.inventory = new SimpleContainer(slots);
        combinedInv = new CombinedInvWrapper(new EntityArmorInvWrapper(this), new EntityHandsInvWrapper(this), new InvWrapper(inventory));
        calculateNextReset();
    }

    @Nullable
    protected static EquipmentSlot getEquipmentSlot(int pIndex) {
        if (pIndex == 100 + EquipmentSlot.HEAD.getIndex()) {
            return EquipmentSlot.HEAD;
        } else if (pIndex == 100 + EquipmentSlot.CHEST.getIndex()) {
            return EquipmentSlot.CHEST;
        } else if (pIndex == 100 + EquipmentSlot.LEGS.getIndex()) {
            return EquipmentSlot.LEGS;
        } else if (pIndex == 100 + EquipmentSlot.FEET.getIndex()) {
            return EquipmentSlot.FEET;
        } else if (pIndex == 98) {
            return EquipmentSlot.MAINHAND;
        } else {
            return pIndex == 99 ? EquipmentSlot.OFFHAND : null;
        }
    }

    @Override
    public IItemHandler getItemHandler() {
        return combinedInv;
    }

    @Override
    protected float getEquipmentDropChance(@NotNull EquipmentSlot pSlot) {
        return super.getEquipmentDropChance(pSlot);
    }

    @Override
    protected void dropAllDeathLoot(@NotNull DamageSource pDamageSource) {
        super.dropAllDeathLoot(pDamageSource);

        if (!inventory.isEmpty()) dropInventoryItems();
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();

        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            if (equipmentSlot.getType() == EquipmentSlot.Type.HAND) {
                ItemStack stack = this.getItemBySlot(equipmentSlot);
                if (!stack.isEmpty()) {
                    ItemEntity itemEntity = new ItemEntity(level, this.getX(), this.getY() + 0.5, this.getZ(), stack.copy());
                    itemEntity.setDeltaMovement(
                            (random.nextDouble() - 0.5) * 0.2,
                            0.2,
                            (random.nextDouble() - 0.5) * 0.2
                    );
                    level.addFreshEntity(itemEntity);
                    this.setItemSlot(equipmentSlot, ItemStack.EMPTY);
                }
            }
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("Inventory", inventory.createTag());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.inventory.fromTag(tag.getList("Inventory", 10));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new TradeWithPlayerGoal(this));
        this.goalSelector.addGoal(3, new LookAndFollowTradingPlayerSink(this, 0.25f));
    }

    @Override
    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        return super.mobInteract(player, hand);
    }

    @Override
    public @NotNull SimpleContainer getInventory() {
        return inventory;
    }

    @Override
    public @NotNull SlotAccess getSlot(int slot) {
        if (getEquipmentSlot(slot) == null) {
            if (slot >= 0 && slot < this.inventory.getContainerSize()) {
                return SlotAccess.forContainer(this.inventory, slot);
            }
        }

        return super.getSlot(slot);
    }

    // MenuProvider implementation
    @Override
    public @NotNull Component getDisplayName() {
        return this.getName();
    }

    public MenuProvider getMenuProvider() {
        return menuProvider;
    }

    @Override
    public @NotNull AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inv, @NotNull Player player) {
        return super.createMenu(containerId, inv, player);
    }

    // Inventory management
    public boolean isInventoryFull() {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (inventory.getItem(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean isInventoryAndHandsFull() {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (inventory.getItem(i).isEmpty()) {
                return false;
            }
        }

        if (this.getMainHandItem().isEmpty()) {
            return false;
        } else return !this.getOffhandItem().isEmpty();
    }

    public boolean isInventoryFull(Predicate<NonNullList<ItemStack>> listPredicate) {
        NonNullList<ItemStack> itemStacks = this.getInventoryItems();
        return listPredicate.test(itemStacks);
    }

    public NonNullList<ItemStack> getInventoryItems() {
        NonNullList<ItemStack> itemStacks = NonNullList.create();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            itemStacks.add(inventory.getItem(i));
        }
        return itemStacks;
    }

    public void addToInventory(ItemStack stack) {
        for (int i = 0; i < getInventory().getContainerSize(); i++) {
            ItemStack slot = getInventory().getItem(i);
            if (slot.isEmpty()) {
                getInventory().setItem(i, stack.copy());
                stack.setCount(0);
                return;
            } else if (ItemStack.isSameItemSameTags(slot, stack)) {
                int canAdd = Math.min(slot.getMaxStackSize() - slot.getCount(), stack.getCount());
                slot.grow(canAdd);
                stack.shrink(canAdd);
                if (stack.isEmpty()) return;
            }
        }
        if (this.isInventoryFull()) {
            for (EquipmentSlot equipmentSlot : Arrays.stream(EquipmentSlot.values()).filter((equipmentSlot -> equipmentSlot.getType() == EquipmentSlot.Type.HAND)).toList()) {
                ItemStack itemStack = this.getItemBySlot(equipmentSlot);
                if (itemStack.isEmpty()) {
                    this.setItemSlot(equipmentSlot, stack);
                } else if (ItemStack.isSameItemSameTags(itemStack, stack)) {
                    itemStack.grow(1);
                    stack.shrink(1);
                }
            }
        }
    }

    private void dropInventoryItems() {
        Level level = this.level;
        if (level.isClientSide) return;

        for (int i = 0; i < this.inventory.getContainerSize(); i++) {
            ItemStack stack = this.inventory.getItem(i);
            if (!stack.isEmpty()) {
                ItemEntity itemEntity = new ItemEntity(level, this.getX(), this.getY() + 0.5, this.getZ(), stack.copy());
                itemEntity.setDeltaMovement(
                        (random.nextDouble() - 0.5) * 0.2,
                        0.2,
                        (random.nextDouble() - 0.5) * 0.2
                );
                level.addFreshEntity(itemEntity);
                this.inventory.setItem(i, ItemStack.EMPTY);
            }
        }
        this.inventory.setChanged();
    }
}
