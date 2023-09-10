package net.foxyas.changedaddon.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.ItemAttributeModifierEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.EquipmentSlot;

import net.foxyas.changedaddon.init.ChangedAddonModItems;
import net.foxyas.changedaddon.init.ChangedAddonModAttributes;

import javax.annotation.Nullable;

import java.util.UUID;

@Mod.EventBusSubscriber
public class HazarArmorItemTipProcedure {
	@SubscribeEvent
	public static void addAttributeModifier(ItemAttributeModifierEvent event) {
		execute(event, event.getItemStack());
	}

	public static void execute(ItemStack itemstack) {
		execute(null, itemstack);
	}

	private static void execute(@Nullable Event event, ItemStack itemstack) {
		AttributeModifier HazardArmor = null;
		AttributeModifier HazardArmor2 = null;
		AttributeModifier HazardArmor3 = null;
		HazardArmor = new AttributeModifier(UUID.fromString("0-0-0-0-0"), "Hazard Armor Buff", 0.2, AttributeModifier.Operation.ADDITION);
		HazardArmor2 = new AttributeModifier(UUID.fromString("0-0-0-0-1"), "Hazard Armor Buff", 0.15, AttributeModifier.Operation.ADDITION);
		HazardArmor3 = new AttributeModifier(UUID.fromString("0-0-0-0-2"), "Hazard Armor Buff", 0.05, AttributeModifier.Operation.ADDITION);
		if (event instanceof ItemAttributeModifierEvent _event && _event.getSlotType() == EquipmentSlot.HEAD) {
			if (itemstack.getItem() == ChangedAddonModItems.HAZARD_SUIT_HELMET.get()) {
				_event.addModifier(ChangedAddonModAttributes.LATEXRESISTANCE.get(), HazardArmor3);
			}
		}
		if (event instanceof ItemAttributeModifierEvent _event && _event.getSlotType() == EquipmentSlot.CHEST) {
			if (itemstack.getItem() == ChangedAddonModItems.HAZARD_SUIT_CHESTPLATE.get()) {
				_event.addModifier(ChangedAddonModAttributes.LATEXRESISTANCE.get(), HazardArmor);
			}
		}
		if (event instanceof ItemAttributeModifierEvent _event && _event.getSlotType() == EquipmentSlot.LEGS) {
			if (itemstack.getItem() == ChangedAddonModItems.HAZARD_SUIT_LEGGINGS.get()) {
				_event.addModifier(ChangedAddonModAttributes.LATEXRESISTANCE.get(), HazardArmor2);
			}
		}
		if (event instanceof ItemAttributeModifierEvent _event && _event.getSlotType() == EquipmentSlot.FEET) {
			if (itemstack.getItem() == ChangedAddonModItems.HAZARD_SUIT_BOOTS.get()) {
				_event.addModifier(ChangedAddonModAttributes.LATEXRESISTANCE.get(), HazardArmor3);
			}
		}
	}
}
