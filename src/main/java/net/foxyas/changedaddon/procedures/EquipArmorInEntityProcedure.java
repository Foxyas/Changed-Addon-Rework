package net.foxyas.changedaddon.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;

import net.ltxprogrammer.changed.entity.beast.DarkLatexPup;
import net.ltxprogrammer.changed.entity.TamableLatexEntity;
import net.ltxprogrammer.changed.entity.LatexEntity;

import net.foxyas.changedaddon.item.HazmatSuitItem;

@Mod.EventBusSubscriber
public class EquipArmorInEntityProcedure {
	@SubscribeEvent
	public static void onRightClickEntity(PlayerInteractEvent.EntityInteractSpecific event) {
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
			} else {
				if (!event.getWorld().isClientSide()) {
					if (itemStack.isEmpty()) {
						checkClickLocationAndUnequipArmor(latexEntity, event.getPlayer(), event.getHand(), event.getLocalPos());
					}
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
				swapArmor(entity, player, hand, armorSlot, currentArmor, itemStack, armorItem);
			} else if (entity.getItemBySlot(armorSlot).isEmpty() && itemStack.canEquip(armorSlot, entity)) {
				// If the slot is empty or the armor is different from the current one, equip the new armor
				equipArmor(entity, player, hand, itemStack, armorSlot, armorItem);
			}
		}
	}

	private static void swapArmor(LatexEntity entity, Player player, InteractionHand hand, EquipmentSlot armorSlot, ItemStack currentArmor, ItemStack newArmor, ArmorItem armorItemStack) {
		// Remove the current armor from the slot
		entity.setItemSlot(armorSlot, ItemStack.EMPTY); // Remove the old armor from the slot
		// Equip the new armor
		equipArmor(entity, player, hand, newArmor, armorSlot, armorItemStack);
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

	private static void equipArmor(LatexEntity entity, Player player, InteractionHand hand, ItemStack itemStack, EquipmentSlot armorSlot, ArmorItem armorItemStack) {
		// Remove one unit of the new armor and equip it in the entity's slot
		entity.setItemSlot(armorSlot, itemStack.split(1)); // Equip one unit of the new armor
		if (armorItemStack.getEquipSound() != null) {
			entity.level.playSound(null, entity.blockPosition(), armorItemStack.getEquipSound(), SoundSource.PLAYERS, 1.0F, 1.0F); // Armor equip sound
		} else {
			entity.level.playSound(null, entity.blockPosition(), SoundEvents.ARMOR_EQUIP_GENERIC, SoundSource.PLAYERS, 1.0F, 1.0F); // Armor equip sound
		}
		// Remove the item from the player's hand
		if (!player.level.isClientSide()) {
			// Check if the hand being used is the main or offhand
			if (hand == InteractionHand.MAIN_HAND) {
				player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY); // Clear the main hand item
			} else {
				player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY); // Clear the offhand item
			}
		}
	}

	private static void checkClickLocationAndUnequipArmor(LatexEntity entity, Player player, InteractionHand hand, Vec3 clickPos) {
		Vec3 eyePosition = player.getEyePosition(1.0F);
		Vec3 playerLook = player.getViewVector(1.0F);
		Vec3 entityPosition = new Vec3(entity.getX(), entity.getY(), entity.getZ());
		// Compute the vector from player eye position to entity position
		Vec3 toEntity = entityPosition.subtract(eyePosition);
		// Normalize the player look vector and the toEntity vector to compute the projection
		Vec3 normalizedPlayerLook = playerLook.normalize();
		double dotProduct = toEntity.normalize().dot(normalizedPlayerLook);
		// Calculate the height relative based on the dot product and distance
		double distance = toEntity.length(); // Get the distance to the entity
		//toEntity.normalize().y - playerLook.normalize().y;
		double yRelative = clickPos.y;
		// Display the yRelative value for debugging
		//player.displayClientMessage(new TextComponent("Y Relative: " + yRelative), false);
		// Determine which armor slot to unequip based on the yRelative
		EquipmentSlot clickedSlot = determineArmorSlot(yRelative, entity);
		if (clickedSlot != null) {
			ItemStack currentArmor = entity.getItemBySlot(clickedSlot);
			if (!currentArmor.isEmpty()) {
				// Remove the armor from the slot
				entity.setItemSlot(clickedSlot, ItemStack.EMPTY);
				entity.level.playSound(null, entity.blockPosition(), SoundEvents.ARMOR_EQUIP_GENERIC, SoundSource.PLAYERS, 1.0F, 1.0F); // Armor equip sound
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
		}
	}

	private static EquipmentSlot determineArmorSlot(double yClick, Entity entity) {
		double entityHeight = entity.getBbHeight();
		if (entityHeight <= 0) {
			return EquipmentSlot.FEET;
		}
		double relativeHeight = yClick / entityHeight;
		if (relativeHeight > 0.8) {
			return EquipmentSlot.HEAD;
		} else if (relativeHeight > 0.4) {
			return EquipmentSlot.CHEST;
		} else if (relativeHeight > 0.2) {
			return EquipmentSlot.LEGS;
		} else {
			return EquipmentSlot.FEET;
		}
	}
}
