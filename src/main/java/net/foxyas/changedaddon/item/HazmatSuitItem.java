
package net.foxyas.changedaddon.item;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.Minecraft;

import net.foxyas.changedaddon.procedures.HazmatSuitTickUsedProcedure;
import net.foxyas.changedaddon.init.ChangedAddonModTabs;
import net.foxyas.changedaddon.client.model.ModelHazmat_Suit;

import java.util.Map;
import java.util.Collections;

public abstract class HazmatSuitItem extends ArmorItem {
	public HazmatSuitItem(EquipmentSlot slot, Item.Properties properties) {
		super(new ArmorMaterial() {
			@Override
			public int getDurabilityForSlot(EquipmentSlot slot) {
				return new int[]{13, 15, 16, 11}[slot.getIndex()] * 25;
			}

			@Override
			public int getDefenseForSlot(EquipmentSlot slot) {
				return new int[]{1, 4, 5, 1}[slot.getIndex()];
			}

			@Override
			public int getEnchantmentValue() {
				return 9;
			}

			@Override
			public SoundEvent getEquipSound() {
				return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:armor_equip"));
			}

			@Override
			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(Items.LEATHER), new ItemStack(Items.IRON_INGOT));
			}

			@Override
			public String getName() {
				return "hazmat_suit";
			}

			@Override
			public float getToughness() {
				return 0f;
			}

			@Override
			public float getKnockbackResistance() {
				return 0f;
			}
		}, slot, properties);
	}

	public static class Helmet extends HazmatSuitItem {
		public Helmet() {
			super(EquipmentSlot.HEAD, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON));
		}

		public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.IItemRenderProperties> consumer) {
			consumer.accept(new IItemRenderProperties() {
				@Override
				public HumanoidModel getArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
					HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(),
							Map.of("head", new ModelHazmat_Suit(Minecraft.getInstance().getEntityModels().bakeLayer(ModelHazmat_Suit.LAYER_LOCATION)).Head, "hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "body",
									new ModelPart(Collections.emptyList(), Collections.emptyMap()), "right_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "left_arm",
									new ModelPart(Collections.emptyList(), Collections.emptyMap()), "right_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "left_leg",
									new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
					armorModel.crouching = living.isShiftKeyDown();
					armorModel.riding = defaultModel.riding;
					armorModel.young = living.isBaby();
					return armorModel;
				}
			});
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "changed_addon:textures/entities/hazmat_suit.png";
		}

		@Override
		public void onArmorTick(ItemStack itemstack, Level world, Player entity) {
			HazmatSuitTickUsedProcedure.execute(entity, itemstack);
		}
	}

	public static class Chestplate extends HazmatSuitItem {
		public Chestplate() {
			super(EquipmentSlot.CHEST, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON));
		}

		public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.IItemRenderProperties> consumer) {
			consumer.accept(new IItemRenderProperties() {
				@Override
				@OnlyIn(Dist.CLIENT)
				public HumanoidModel getArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
					HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(), Map.of("body", new ModelHazmat_Suit(Minecraft.getInstance().getEntityModels().bakeLayer(ModelHazmat_Suit.LAYER_LOCATION)).Body, "left_arm",
							new ModelHazmat_Suit(Minecraft.getInstance().getEntityModels().bakeLayer(ModelHazmat_Suit.LAYER_LOCATION)).LeftArm, "right_arm",
							new ModelHazmat_Suit(Minecraft.getInstance().getEntityModels().bakeLayer(ModelHazmat_Suit.LAYER_LOCATION)).RightArm, "head", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "hat",
							new ModelPart(Collections.emptyList(), Collections.emptyMap()), "right_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "left_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
					armorModel.crouching = living.isShiftKeyDown();
					armorModel.riding = defaultModel.riding;
					armorModel.young = living.isBaby();
					return armorModel;
				}
			});
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "changed_addon:textures/entities/hazmat_suit.png";
		}

		@Override
		public void onArmorTick(ItemStack itemstack, Level world, Player entity) {
			HazmatSuitTickUsedProcedure.execute(entity, itemstack);
		}
	}

	public static class Leggings extends HazmatSuitItem {
		public Leggings() {
			super(EquipmentSlot.LEGS, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON));
		}

		public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.IItemRenderProperties> consumer) {
			consumer.accept(new IItemRenderProperties() {
				@Override
				@OnlyIn(Dist.CLIENT)
				public HumanoidModel getArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
					HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(),
							Map.of("left_leg", new ModelHazmat_Suit(Minecraft.getInstance().getEntityModels().bakeLayer(ModelHazmat_Suit.LAYER_LOCATION)).RightLeg, "right_leg",
									new ModelHazmat_Suit(Minecraft.getInstance().getEntityModels().bakeLayer(ModelHazmat_Suit.LAYER_LOCATION)).LeftLeg, "head", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "hat",
									new ModelPart(Collections.emptyList(), Collections.emptyMap()), "body", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "right_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
									"left_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
					armorModel.crouching = living.isShiftKeyDown();
					armorModel.riding = defaultModel.riding;
					armorModel.young = living.isBaby();
					return armorModel;
				}
			});
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "changed_addon:textures/entities/hazmat_suit.png";
		}

		@Override
		public void onArmorTick(ItemStack itemstack, Level world, Player entity) {
			HazmatSuitTickUsedProcedure.execute(entity, itemstack);
		}
	}

	public static class Boots extends HazmatSuitItem {
		public Boots() {
			super(EquipmentSlot.FEET, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON));
		}

		public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.IItemRenderProperties> consumer) {
			consumer.accept(new IItemRenderProperties() {
				@Override
				@OnlyIn(Dist.CLIENT)
				public HumanoidModel getArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
					HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(),
							Map.of("left_leg", new ModelHazmat_Suit(Minecraft.getInstance().getEntityModels().bakeLayer(ModelHazmat_Suit.LAYER_LOCATION)).LeftLeg, "right_leg",
									new ModelHazmat_Suit(Minecraft.getInstance().getEntityModels().bakeLayer(ModelHazmat_Suit.LAYER_LOCATION)).RightLeg, "head", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "hat",
									new ModelPart(Collections.emptyList(), Collections.emptyMap()), "body", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "right_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
									"left_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
					armorModel.crouching = living.isShiftKeyDown();
					armorModel.riding = defaultModel.riding;
					armorModel.young = living.isBaby();
					return armorModel;
				}
			});
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "changed_addon:textures/entities/hazmat_suit_boots.png";
		}

		@Override
		public void onArmorTick(ItemStack itemstack, Level world, Player entity) {
			HazmatSuitTickUsedProcedure.execute(entity, itemstack);
		}
	}
}
