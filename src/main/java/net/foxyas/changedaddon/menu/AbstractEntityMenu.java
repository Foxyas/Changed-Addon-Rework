package net.foxyas.changedaddon.menu;

import com.mojang.datafixers.util.Pair;
import net.foxyas.changedaddon.entity.api.ItemHandlerHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//!!! This menu assumes, that getItemHandler() returns CombinedInvWrapper(EntityArmorInvWrapper, EntityHandsInvWrapper, ...) or equivalent
public class AbstractEntityMenu<E extends LivingEntity & ItemHandlerHolder> extends AbstractMenu {

    public static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = new ResourceLocation[]{InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS, InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS, InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE, InventoryMenu.EMPTY_ARMOR_SLOT_HELMET};
    public static final EquipmentSlot[] SLOT_IDS = new EquipmentSlot[]{EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD};
    public static final int TOO_FAR = 64;

    protected final E entity;

    protected AbstractEntityMenu(@Nullable MenuType<?> menuType, int containerId, Inventory playerInv, E entity) {
        this(menuType, containerId, playerInv, entity, 0, 0);
    }

    protected AbstractEntityMenu(@Nullable MenuType<?> menuType, int containerId, Inventory playerInv, E entity, int xOffset, int yOffset) {
        super(menuType, containerId);
        this.entity = entity;
        IItemHandler combinedInv = entity.getItemHandler();

        createPlayerHotbar(playerInv, xOffset, yOffset);
        createPlayerInventory(playerInv, xOffset, yOffset);

        //Armor
        for (int i = 0; i < 4; i++) {
            final EquipmentSlot equipmentslot = SLOT_IDS[i];
            addSlot(new SlotItemHandler(combinedInv, i, 8 + xOffset, 8 + (3 - i) * 18 + yOffset) {

                public int getMaxStackSize() {
                    return 1;
                }

                public boolean mayPlace(@NotNull ItemStack stack) {
                    return stack.canEquip(equipmentslot, entity);
                }

                public boolean mayPickup(Player player) {
                    ItemStack itemstack = getItem();
                    return (itemstack.isEmpty() || player.isCreative() || !EnchantmentHelper.hasBindingCurse(itemstack)) && super.mayPickup(player);
                }

                public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                    return Pair.of(InventoryMenu.BLOCK_ATLAS, TEXTURE_EMPTY_SLOTS[equipmentslot.getIndex()]);
                }
            });
        }

        //Hands
        addSlot(new SlotItemHandler(combinedInv, 4, 77 + xOffset, 8 + 2 * 18 + yOffset));//Main
        addSlot(new SlotItemHandler(combinedInv, 5, 77 + xOffset, 8 + 3 * 18 + yOffset) {
            @Override
            public @NotNull Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
            }
        });//Off
    }

    public E getEntity() {
        return entity;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return player.distanceToSqr(entity) < TOO_FAR;
    }
}
