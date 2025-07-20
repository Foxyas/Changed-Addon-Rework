
package net.foxyas.changedaddon.item;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;

import net.foxyas.changedaddon.init.ChangedAddonTabs;
import net.foxyas.changedaddon.init.ChangedAddonItems;

public abstract class PainiteArmorItem extends ArmorItem {
	public PainiteArmorItem(EquipmentSlot slot, Item.Properties properties) {
		super(new ArmorMaterial() {
			@Override
			public int getDurabilityForSlot(EquipmentSlot slot) {
				return new int[]{13, 15, 16, 11}[slot.getIndex()] * 40;
			}

			@Override
			public int getDefenseForSlot(EquipmentSlot slot) {
				return new int[]{3, 6, 8, 3}[slot.getIndex()];
			}

			@Override
			public int getEnchantmentValue() {
				return 18;
			}

			@Override
			public SoundEvent getEquipSound() {
				return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.armor.equip_netherite"));
			}

			@Override
			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(ChangedAddonItems.PAINITE.get()), new ItemStack(Items.NETHERITE_INGOT));
			}

			@Override
			public String getName() {
				return "painite_armor";
			}

			@Override
			public float getToughness() {
				return 3f;
			}

			@Override
			public float getKnockbackResistance() {
				return 0.1f;
			}
		}, slot, properties);
	}

	public static class Helmet extends PainiteArmorItem {
		public Helmet() {
			super(EquipmentSlot.HEAD, new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON_COMBAT_OPTIONAL).fireResistant());
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "changed_addon:textures/models/armor/netherite_with_painite_trim__layer_1.png";
		}
	}

	public static class Chestplate extends PainiteArmorItem {
		public Chestplate() {
			super(EquipmentSlot.CHEST, new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON_COMBAT_OPTIONAL).fireResistant());
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "changed_addon:textures/models/armor/netherite_with_painite_trim__layer_1.png";
		}
	}

	public static class Leggings extends PainiteArmorItem {
		public Leggings() {
			super(EquipmentSlot.LEGS, new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON_COMBAT_OPTIONAL).fireResistant());
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "changed_addon:textures/models/armor/netherite_with_painite_trim__layer_2.png";
		}
	}

	public static class Boots extends PainiteArmorItem {
		public Boots() {
			super(EquipmentSlot.FEET, new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON_COMBAT_OPTIONAL).fireResistant());
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "changed_addon:textures/models/armor/netherite_with_painite_trim__layer_1.png";
		}
	}
}
