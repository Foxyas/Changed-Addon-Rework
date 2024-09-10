
package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.init.ChangedAddonModTabs;
import net.ltxprogrammer.changed.item.SpecializedItemRendering;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class FoxtaItem extends Item implements SpecializedItemRendering {
	public FoxtaItem() {
		super(new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON).stacksTo(64).rarity(Rarity.RARE).food((new FoodProperties.Builder()).nutrition(9).saturationMod(1f).alwaysEat()

				.build()));
	}

	@Override
	public UseAnim getUseAnimation(ItemStack itemstack) {
		return UseAnim.DRINK;
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
		list.add(new TranslatableComponent("item.changed_addon.foxta.desc"));
	}

	private static final ModelResourceLocation GUIMODEL =
			new ModelResourceLocation(new ResourceLocation("changed_addon","foxta_gui"), "inventory");
	private static final ModelResourceLocation HANDMODEL =
			new ModelResourceLocation(new ResourceLocation("changed_addon","foxta_hand"), "inventory");
	private static final ModelResourceLocation GROUNDMODEL =
			new ModelResourceLocation(new ResourceLocation("changed_addon","foxta_ground"), "inventory");


	@Override
	public ModelResourceLocation getModelLocation(ItemStack itemStack, ItemTransforms.TransformType transformType) {
		return transformType == ItemTransforms.TransformType.GUI || transformType == ItemTransforms.TransformType.FIXED ? GUIMODEL
				: transformType == ItemTransforms.TransformType.GROUND ? GROUNDMODEL : HANDMODEL;
	}

	@Override
	public void loadSpecialModels(Consumer<ResourceLocation> consumer) {
			consumer.accept(GUIMODEL);
			consumer.accept(HANDMODEL);
			consumer.accept(GROUNDMODEL);
	}
}
