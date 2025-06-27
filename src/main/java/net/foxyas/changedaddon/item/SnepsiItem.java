
package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;
import net.foxyas.changedaddon.init.ChangedAddonModTabs;
import net.foxyas.changedaddon.procedures.SnepsiPlayerFinishesUsingItemProcedure;
import net.ltxprogrammer.changed.item.SpecializedItemRendering;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class SnepsiItem extends Item implements SpecializedItemRendering {
    public SnepsiItem() {
        super(new Item.Properties()
                .tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)
                .stacksTo(64)
                .rarity(Rarity.RARE)
                .food(new FoodProperties.Builder()
                        .nutrition(9)
                        .saturationMod(1f)
                        .alwaysEat()
                        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 15, 2), 0.25F)
                        .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 20 * 15, 1), 0.25F)
                        .effect(() -> new MobEffectInstance(ChangedAddonModMobEffects.LATEX_CONTAMINATION.get(), 20 * 15, 3), 0.25F)
                        .build()));
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack itemstack) {
        return UseAnim.DRINK;
    }

    @Override
    public @NotNull SoundEvent getDrinkingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(Component.translatable("item.changed_addon.snepsi.desc"));
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
            new ModelResourceLocation(ResourceLocation.parse("changed_addon", "snepsi_gui"), "inventory");
    private static final ModelResourceLocation HANDMODEL =
            new ModelResourceLocation(ResourceLocation.parse("changed_addon", "snepsi_hand"), "inventory");
    private static final ModelResourceLocation GROUNDMODEL =
            new ModelResourceLocation(ResourceLocation.parse("changed_addon", "snepsi_ground"), "inventory");


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
