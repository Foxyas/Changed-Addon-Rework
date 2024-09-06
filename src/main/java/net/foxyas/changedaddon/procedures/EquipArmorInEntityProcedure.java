package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.item.HazmatSuitItem;
import net.ltxprogrammer.changed.entity.LatexEntity;
import net.ltxprogrammer.changed.entity.TamableLatexEntity;
import net.ltxprogrammer.changed.entity.beast.DarkLatexPup;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EquipArmorInEntityProcedure {
    @SubscribeEvent
    public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
        if (event.getPlayer().isShiftKeyDown() && event.getTarget() instanceof LatexEntity latexEntity) {
            ItemStack itemStack = event.getItemStack();
            if (latexEntity instanceof DarkLatexPup) {
                return;
            }
            // Check if the item is an armor piece
            if (itemStack.getItem() instanceof ArmorItem armorItem) {
                if (armorItem instanceof HazmatSuitItem) {
                    return;
                }
                // Proceed only on the server side
                if (!event.getWorld().isClientSide()) {
                    equipOrSwapArmor(latexEntity, itemStack, armorItem, event.getPlayer(), event.getHand());
                }
            }
        }
    }

    public static void equipOrSwapArmor(LatexEntity entity, ItemStack itemStack, ArmorItem armorItem, Player player, InteractionHand hand) {
        // Check if the entity is tamable and has an owner
        if (entity instanceof TamableLatexEntity latexEntity && latexEntity.getOwner() != null && latexEntity.getOwner() == player) {
            EquipmentSlot armorSlot = armorItem.getSlot(); // Determine the correct armor slot
            ItemStack currentArmor = entity.getItemBySlot(armorSlot); // Get the current armor in the slot

            // If the slot is already occupied and the item in the player's hand is a different armor piece
            if (!currentArmor.isEmpty() && !currentArmor.getItem().equals(armorItem)) {
                swapArmor(entity, player, hand, armorSlot, currentArmor, itemStack);
            } else if (entity.getItemBySlot(armorSlot).isEmpty() && itemStack.canEquip(armorSlot, entity)) {
                // If the slot is empty or the armor is different from the current one, equip the new armor
                equipArmor(entity, player, hand, itemStack, armorSlot);
            }
        }
    }

    private static void swapArmor(LatexEntity entity, Player player, InteractionHand hand, EquipmentSlot armorSlot, ItemStack currentArmor, ItemStack newArmor) {
        // Remove the current armor from the slot
        entity.setItemSlot(armorSlot, ItemStack.EMPTY); // Remove the old armor from the slot
        // Equip the new armor
        equipArmor(entity, player, hand, newArmor, armorSlot);

        // Add the removed armor to the player's inventory or to the hand if empty
        if (player.getItemInHand(hand).isEmpty()) {
            player.setItemInHand(hand, currentArmor);
        } else {
            // Add the removed armor to the player's inventory (or to the ground if necessary)
            if (!player.getInventory().add(currentArmor)) {
                player.drop(currentArmor, false); // Drop on the ground if inventory is full
            }
        }
    }

    private static void equipArmor(LatexEntity entity, Player player, InteractionHand hand, ItemStack itemStack, EquipmentSlot armorSlot) {
        // Remove one unit of the new armor and equip it in the entity's slot
        entity.setItemSlot(armorSlot, itemStack.split(1)); // Equip one unit of the new armor
        entity.level.playSound(null, entity.blockPosition(), SoundEvents.ARMOR_EQUIP_GENERIC, SoundSource.PLAYERS, 1.0F, 1.0F); // Armor equip sound

        // Remove the item from the player's hand
        if (!player.level.isClientSide()) {
            // Check if the hand being used is the main or off hand
            if (hand == InteractionHand.MAIN_HAND) {
                player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY); // Clear the main hand item
            } else {
                player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY); // Clear the off hand item
            }
        }
    }
}
