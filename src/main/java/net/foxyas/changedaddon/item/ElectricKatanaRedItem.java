
package net.foxyas.changedaddon.item;

import net.ltxprogrammer.changed.item.SpecializedItemRendering;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.common.crafting.CompoundIngredient;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;

import net.foxyas.changedaddon.procedures.ElectricKatanaLivingEntityIsHitWithToolProcedure;
import net.foxyas.changedaddon.procedures.ElectricKatanaEntitySwingsItemProcedure;
import net.foxyas.changedaddon.init.ChangedAddonModItems;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ElectricKatanaRedItem extends SwordItem implements SpecializedItemRendering {
	public ElectricKatanaRedItem() {
		super(new Tier() {
			public int getUses() {
				return 1536;
			}

			public float getSpeed() {
				return 4f;
			}

			public float getAttackDamageBonus() {
				return 4f;
			}

			public int getLevel() {
				return 1;
			}

			public int getEnchantmentValue() {
				return 30;
			}

			public Ingredient getRepairIngredient() {
				return CompoundIngredient.of(Ingredient.of(ItemTags.create(new ResourceLocation("changed_addon:tsc_katana_repair"))), Ingredient.of(new ItemStack(ChangedAddonModItems.ELECTRIC_KATANA_RED.get())));
			}
		}, 3, -2.2f, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT));
	}

	private static final ModelResourceLocation GUI_MODEL =
			new ModelResourceLocation(new ResourceLocation("changed_addon","electric_katana_red_item_full"), "inventory");
	private static final ModelResourceLocation HANDLE_MODEL =
			new ModelResourceLocation(new ResourceLocation("changed_addon","electric_katana_red_3d"), "inventory");
	private static final ModelResourceLocation EMISSIVE_MODEL =
			new ModelResourceLocation(new ResourceLocation("changed_addon","electric_katana_red_laser"), "inventory");
	private static final ModelResourceLocation EMISSIVE_GUI_MODEL =
			new ModelResourceLocation(new ResourceLocation("changed_addon","electric_katana_red_glow"), "inventory");

	@Nullable
	@Override
	public ModelResourceLocation getEmissiveModelLocation(ItemStack itemStack, ItemTransforms.TransformType transformType) {
		return transformType == ItemTransforms.TransformType.GUI || transformType == ItemTransforms.TransformType.FIXED ? null
				: transformType == ItemTransforms.TransformType.GROUND ? null : EMISSIVE_MODEL;
	}

	@Override
	public ModelResourceLocation getModelLocation(ItemStack itemStack, ItemTransforms.TransformType transformType) {
		return SpecializedItemRendering.isGUI(transformType) ? GUI_MODEL : HANDLE_MODEL;
	}

	@Override
	public void loadSpecialModels(Consumer<ResourceLocation> consumer) {
		consumer.accept(HANDLE_MODEL);
		consumer.accept(EMISSIVE_MODEL);
		consumer.accept(GUI_MODEL);
		consumer.accept(EMISSIVE_GUI_MODEL);
	}

	@Override
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
		boolean retval = super.hurtEnemy(itemstack, entity, sourceentity);
		ElectricKatanaLivingEntityIsHitWithToolProcedure.execute(entity, sourceentity);
		return retval;
	}

	@Override
	public boolean onEntitySwing(ItemStack itemstack, LivingEntity entity) {
		boolean retval = super.onEntitySwing(itemstack, entity);
		if (entity.swingTime <= 0){
			ElectricKatanaEntitySwingsItemProcedure.execute(entity);
		}
		return retval;
	}
}
