
package net.foxyas.changedaddon.item;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.stats.StatsCounter;
import net.minecraft.stats.Stats;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.Advancement;

import net.ltxprogrammer.changed.item.SpecializedItemRendering;

import net.foxyas.changedaddon.init.ChangedAddonModTabs;
import net.foxyas.changedaddon.init.ChangedAddonModItems;

import java.util.function.Consumer;
import java.util.List;


public class FoxtaItem extends Item implements SpecializedItemRendering {

    public FoxtaItem() {
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
        list.add(new TranslatableComponent("item.changed_addon.foxta.desc"));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemstack, Level world, LivingEntity entity) {
        ItemStack retval = super.finishUsingItem(itemstack, world, entity);
        if (entity instanceof ServerPlayer serverPlayer) {
            StatsCounter stats = serverPlayer.getStats();
            // Distância percorrida no ar
            int Foxta_Drink_Amount = stats.getValue(Stats.ITEM_USED.get(ChangedAddonModItems.SNEPSI.get()));
            if (Foxta_Drink_Amount >= 100) {
                Advancement _adv = serverPlayer.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:foxta_adctive"));
                assert _adv != null;
                AdvancementProgress _ap = serverPlayer.getAdvancements().getOrStartProgress(_adv);
                if (!_ap.isDone()) {
                    for (String string : _ap.getRemainingCriteria()) serverPlayer.getAdvancements().award(_adv, string);
                }
            }
            //serverPlayer.displayClientMessage(new TextComponent("Drink this = " + Snepsi_Drink_Amount),false);
        }

        return retval;
    }


    private static final ModelResourceLocation GUIMODEL =
            new ModelResourceLocation(new ResourceLocation("changed_addon", "foxta_gui"), "inventory");
    private static final ModelResourceLocation HANDMODEL =
            new ModelResourceLocation(new ResourceLocation("changed_addon", "foxta_hand"), "inventory");
    private static final ModelResourceLocation GROUNDMODEL =
            new ModelResourceLocation(new ResourceLocation("changed_addon", "foxta_ground"), "inventory");


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
