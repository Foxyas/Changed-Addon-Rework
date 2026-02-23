package net.foxyas.changedaddon.menu;

import com.mojang.datafixers.util.Pair;
import net.foxyas.changedaddon.entity.ai.LatexInventory;
import net.foxyas.changedaddon.entity.api.TamableLatexEntityFavors;
import net.foxyas.changedaddon.init.ChangedAddonMenus;
import net.ltxprogrammer.changed.data.AccessorySlots;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.ClothingShape;
import net.ltxprogrammer.changed.item.ExtendedItemProperties;
import net.ltxprogrammer.changed.world.enchantments.FormFittingEnchantment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.HashMap;
import java.util.Map;

public class TamedLatexInventoryMenu extends AbstractContainerMenu {
    static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = new ResourceLocation[]{
            InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS,
            InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS,
            InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE,
            InventoryMenu.EMPTY_ARMOR_SLOT_HELMET};
    private static final EquipmentSlot[] SLOT_IDS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    public final Player owner;
    private final TamableLatexEntityFavors iTamedLatex;
    public final ChangedEntity tamedLatex;
    public final Inventory inventory;
    public final LatexInventory dlInventory;
    public final AccessorySlots accessorySlots;

    private final Map<Integer, Slot> customSlots = new HashMap<>();

    public TamedLatexInventoryMenu(int id, Player owner, TamableLatexEntityFavors tamedLatex) {
        super(ChangedAddonMenus.TAMED_LATEX_INVENTORY.get(), id);
        this.owner = owner;
        this.tamedLatex = tamedLatex.getSelf();
        this.iTamedLatex = tamedLatex;
        this.inventory = owner.getInventory();
        this.dlInventory = tamedLatex.getInventory();
        this.accessorySlots = AccessorySlots.getForEntity(owner).orElseGet(AccessorySlots::new);
        this.createSlots(inventory, tamedLatex.getInventory());
    }

    public TamedLatexInventoryMenu(int id, Inventory inventory, FriendlyByteBuf extra) {
        this(id, inventory.player, inventory.player.level().getEntity(extra.readInt()) instanceof TamableLatexEntityFavors tamableLatexEntityFavors ? tamableLatexEntityFavors : null);
    }

    public boolean canWear(ItemStack itemStack, EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND)
            return true;
        if (itemStack.isEmpty())
            return true;
        itemStack = FormFittingEnchantment.getFormFitted(tamedLatex, itemStack, slot);
        if (itemStack.getItem() instanceof ExtendedItemProperties wearableItem) {
            if (!wearableItem.allowedInSlot(itemStack, tamedLatex, slot))
                return false;
        } else { // Default expected entity shapes
            var entityShape = tamedLatex.getEntityShape();

            boolean shapeFits = switch (slot) {
                case HEAD -> entityShape.getHeadShape() == ClothingShape.Head.ANTHRO;
                case CHEST -> entityShape.getTorsoShape() == ClothingShape.Torso.ANTHRO;
                case LEGS -> entityShape.getLegsShape() == ClothingShape.Legs.BIPEDAL;
                case FEET -> entityShape.getFeetShape() == ClothingShape.Feet.BIPEDAL;
                default -> true;
            };

            if (!shapeFits)
                return false;
        }

        if (!tamedLatex.isItemAllowedInSlot(itemStack, slot))
            return false;

        return true;
    }

    // 0-3 -> DL armor, 4-30 -> hotbar, 31->39 -> inventory, 40 -> DL offhand, 41+ -> DL inventory
    protected void createSlots(Inventory inv, LatexInventory dlInventory) {
        for (int si = 0; si < 4; ++si) {
            final EquipmentSlot equipmentSlot = SLOT_IDS[si];
            this.addSlot(new Slot(dlInventory, (LatexInventory.INVENTORY_SIZE + 3) - si, 8, 8 + (si * 18)) {
                public int getMaxStackSize() {
                    return 1;
                }

                public boolean mayPlace(ItemStack stack) {
                    return stack.canEquip(equipmentSlot, tamedLatex) && canWear(stack, equipmentSlot);
                }

                public boolean mayPickup(Player player) {
                    ItemStack itemstack = this.getItem();
                    return (itemstack.isEmpty() || player.isCreative() || !EnchantmentHelper.hasBindingCurse(itemstack)) && super.mayPickup(player);
                }

                public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                    return Pair.of(InventoryMenu.BLOCK_ATLAS, TEXTURE_EMPTY_SLOTS[equipmentSlot.getIndex()]);
                }
            });
        }

        for (int si = 0; si < 3; ++si)
            for (int sj = 0; sj < 9; ++sj)
                this.addSlot(new Slot(inv, sj + (si + 1) * 9, 26 + sj * 18, 84 + si * 18));
        for (int si = 0; si < 9; ++si)
            this.addSlot(new Slot(inv, si, 26 + si * 18, 142));

        this.addSlot(new Slot(dlInventory, LatexInventory.SLOT_OFFHAND, 77, 8 + (3 * 18)));

        for (int si = 0; si < 4; ++si)
            for (int sj = 0; sj < 6; ++sj)
                this.addSlot(new Slot(dlInventory, sj + si * 6, 98 + sj * 18, 8 + si * 18));
    }

    @Override
    public boolean stillValid(Player player) {
        return !tamedLatex.isDeadOrDying() && !tamedLatex.isRemoved() && iTamedLatex.getOwner() == player;
    }

    protected boolean canTamedDLWear(ItemStack itemStack, EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND)
            return true;
        itemStack = FormFittingEnchantment.getFormFitted(tamedLatex, itemStack, slot);
        if (itemStack.getItem() instanceof ExtendedItemProperties wearableItem) {
            if (!wearableItem.allowedInSlot(itemStack, tamedLatex, slot))
                return false;
        } else { // Default expected entity shapes
            boolean shapeFits = switch (slot) {
                case HEAD -> tamedLatex.getEntityShape().getHeadShape() == ClothingShape.Head.ANTHRO;
                case CHEST -> tamedLatex.getEntityShape().getTorsoShape() == ClothingShape.Torso.ANTHRO;
                case LEGS -> tamedLatex.getEntityShape().getLegsShape() == ClothingShape.Legs.BIPEDAL;
                case FEET -> tamedLatex.getEntityShape().getFeetShape() == ClothingShape.Feet.BIPEDAL;
                default -> true;
            };

            if (!shapeFits)
                return false;
        }

        if (!tamedLatex.isItemAllowedInSlot(itemStack, slot))
            return false;

        return true;
    }

    public EquipmentSlot denyInvalidArmorSlot(ItemStack itemStack) {
        var slot = Mob.getEquipmentSlotForItem(itemStack);
        if (slot.getType() != EquipmentSlot.Type.ARMOR)
            return slot;

        return canTamedDLWear(itemStack, slot) ? slot : EquipmentSlot.MAINHAND;
    }

    /**
     * Moves the given stack into the first available slots in the range
     *
     * @param stack          item stack
     * @param slotRangeStart range start (inclusive)
     * @param slotRangeEnd   range end (exclusive)
     * @param reversed       iterate in reverse, end to start
     * @return true if the stack was moved partially, or completely. false if no items were moved
     */
    @Override
    protected boolean moveItemStackTo(ItemStack stack, int slotRangeStart, int slotRangeEnd, boolean reversed) {
        return super.moveItemStackTo(stack, slotRangeStart, slotRangeEnd, reversed);
    }

    // 0-3 -> DL armor, 4-30 -> hotbar, 31->39 -> inventory, 40 -> DL offhand, 41+ -> DL inventory
    public ItemStack quickMoveStack(Player player, int slotId) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotId);
        if (slot != null && slot.hasItem()) {
            ItemStack oldStack = slot.getItem();
            stack = oldStack.copy();
            EquipmentSlot equipmentslot = denyInvalidArmorSlot(stack);
            if (slotId < 4) { // Move out of DL armor
                if (!this.moveItemStackTo(oldStack, 41, this.slots.size(), false)) {
                    if (!this.moveItemStackTo(oldStack, 4, 40, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (slotId >= 41) { // Move out of DL inventory
                if (!this.moveItemStackTo(oldStack, 0, 4, false)) { // Move to DL armor first
                    if (!this.moveItemStackTo(oldStack, 4, 40, false)) { // Move to viewer's inventory
                        return ItemStack.EMPTY;
                    }
                }
            } else if (equipmentslot.getType() == EquipmentSlot.Type.ARMOR && !this.slots.get(3 - equipmentslot.getIndex()).hasItem()) { // Move to armor
                int armorSlot = 3 - equipmentslot.getIndex();
                if (!this.moveItemStackTo(oldStack, armorSlot, armorSlot + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(oldStack, 41, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (oldStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (oldStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, oldStack);
            if (slotId == 0) {
                player.drop(oldStack, false);
            }
        }

        return stack;
    }

    public Slot getCustomSlot(int id) {
        return customSlots.get(id);
    }
}
