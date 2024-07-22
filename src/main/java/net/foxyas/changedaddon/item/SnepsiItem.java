
package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.ltxprogrammer.changed.item.SpecializedItemRendering;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Component;

import net.foxyas.changedaddon.procedures.SnepsiPlayerFinishesUsingItemProcedure;
import net.foxyas.changedaddon.init.ChangedAddonModTabs;

import java.util.List;
import java.util.function.Consumer;

public class SnepsiItem extends Item implements SpecializedItemRendering {
	public SnepsiItem() {
		super(new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON).stacksTo(64).rarity(Rarity.RARE).food((new FoodProperties.Builder()).nutrition(6).saturationMod(1f).alwaysEat()

				.build()));
	}

	@Override
	public UseAnim getUseAnimation(ItemStack itemstack) {
		return UseAnim.DRINK;
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
		list.add(new TranslatableComponent("item.changed_addon.snepsi.desc"));
	}

	@Override
	public ItemStack finishUsingItem(ItemStack itemstack, Level world, LivingEntity entity) {
		ItemStack retval = super.finishUsingItem(itemstack, world, entity);
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();

		SnepsiPlayerFinishesUsingItemProcedure.execute(entity, itemstack);
		return retval;
	}

	private static final ModelResourceLocation GUIMODEL =
			new ModelResourceLocation(new ResourceLocation("changed_addon","snepsi_gui"), "inventory");
	private static final ModelResourceLocation HANDMODEL =
			new ModelResourceLocation(new ResourceLocation("changed_addon","snepsi_hand"), "inventory");
	private static final ModelResourceLocation GROUNDMODEL =
			new ModelResourceLocation(new ResourceLocation("changed_addon","snepsi"), "inventory");


	@Override
	public ModelResourceLocation getModelLocation(ItemStack itemStack, ItemTransforms.TransformType transformType) {
		return transformType == ItemTransforms.TransformType.GUI || transformType == ItemTransforms.TransformType.FIXED ? GUIMODEL : transformType == ItemTransforms.TransformType.GROUND ? GROUNDMODEL : HANDMODEL;
	}

	@Override
	public void loadSpecialModels(Consumer<ResourceLocation> consumer) {
		consumer.accept(GUIMODEL);
		consumer.accept(HANDMODEL);
		consumer.accept(GROUNDMODEL);
	}
}
