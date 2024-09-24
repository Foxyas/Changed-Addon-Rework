package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.configuration.ChangedAddonConfigsConfiguration;
import net.foxyas.changedaddon.init.ChangedAddonModConfigs;
import net.foxyas.changedaddon.init.ChangedAddonModGameRules;
import net.foxyas.changedaddon.init.ChangedAddonModItems;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.foxyas.changedaddon.variants.AddonLatexVariant;
import net.ltxprogrammer.changed.entity.LatexEntity;
import net.ltxprogrammer.changed.entity.beast.AbstractLatexWolf;
import net.ltxprogrammer.changed.entity.beast.AquaticEntity;
import net.ltxprogrammer.changed.entity.variant.LatexVariant;
import net.ltxprogrammer.changed.entity.variant.LatexVariantInstance;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber
public class CreatureDietsHandleProcedure {

    @SubscribeEvent
    public static void onUseItemFinish(LivingEntityUseItemEvent.Finish event) {
        if (event == null) return;

        LivingEntity livingEntity = event.getEntityLiving();
        ItemStack item = event.getItem();

        if (!(livingEntity instanceof Player player)) {
            return;
        }

        LatexVariantInstance<?> latexInstance = ProcessTransfur.getPlayerLatexVariant(player);
        if (latexInstance == null) return;


        Level world = player.getLevel();

        if (world.isClientSide) {
            return;
        }

        boolean isForWork = world.getGameRules().getBoolean(ChangedAddonModGameRules.CHANGED_ADDON_CREATURE_DIETS);
        boolean Debuffs;
        Debuffs = ChangedAddonConfigsConfiguration.DEBUFFS.get();

        LatexEntity latexEntity = latexInstance.getLatexEntity();
        LatexVariant<?> variant = latexEntity.getSelfVariant();

        if (!item.isEdible()) return;

        boolean isWarnsOn = player.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null)
                .orElse(new ChangedAddonModVariables.PlayerVariables()).showwarns;

        DietType dietType = determineDietType(latexEntity, variant);
        if (dietType == null) return;

        boolean ShouldGiveDebuffs = (latexInstance.ageAsVariant < ChangedAddonConfigsConfiguration.AGE_NEED.get());

        if (isForWork) {
            if (dietType.isDietItem(item)) {
                applyFoodEffects(player, item, true);
                if (isWarnsOn) {
                    player.displayClientMessage(new TranslatableComponent("changedaddon.diets.good_food"), true);
                }
            } else if (Debuffs) {
                if (!dietType.isNotFoodItem(item) && ShouldGiveDebuffs) {
                    applyFoodEffects(player, item, false);
                    player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 50, 3, false, true, true));
                    player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60, 0, false, true, true,
                            new MobEffectInstance(MobEffects.WEAKNESS, 5 * 20, 2)));
                    if (isWarnsOn) {
                        player.displayClientMessage(new TranslatableComponent("changedaddon.diets.bad_food"), true);
                    }
                }
            }
        }
    }

    private static void applyFoodEffects(Player player, ItemStack item) {
        int additionalFood = 4;
        float additionalSaturation = 3;

        player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() + additionalFood);
        player.getFoodData().setSaturation(player.getFoodData().getSaturationLevel() + additionalSaturation);
    }

    private static void applyFoodEffects(Player player, ItemStack item, boolean isGoodFood) {
        int additionalFood;
        float additionalSaturation;
        if (isGoodFood) {
            additionalFood = Objects.requireNonNull(item.getFoodProperties(player)).getNutrition() / 2;
            additionalSaturation = Objects.requireNonNull(item.getFoodProperties(player)).getSaturationModifier() / 2;
        } else {
            additionalFood = -Objects.requireNonNull(item.getFoodProperties(player)).getNutrition() / 4;
            additionalSaturation = -Objects.requireNonNull(item.getFoodProperties(player)).getSaturationModifier() / 4;
        }
        if (!(player.getFoodData().getFoodLevel() >= 20)) {
            player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() + additionalFood);
        } else if (!(player.getFoodData().getSaturationLevel() >= 20)) {
            player.getFoodData().setSaturation(player.getFoodData().getSaturationLevel() + additionalSaturation);
        }
    }

    private static DietType determineDietType(LatexEntity latexEntity, LatexVariant<?> variant) {
        if (isCatDiet(latexEntity, variant)) return DietType.CAT;
        if (isWolfDiet(latexEntity, variant)) return DietType.WOLF;
        if (isSpecialDiet(variant)) return DietType.SPECIAL;
        if (isFoxDiet(latexEntity, variant)) return DietType.FOX;
        if (isAquaticDiet(latexEntity, variant)) return DietType.AQUATIC;
        if (isDragonDiet(latexEntity, variant)) return DietType.DRAGON;

        return null;
    }

    private static boolean isCatDiet(LatexEntity entity, LatexVariant<?> variant) {
        return entity.getType().getRegistryName().toString().contains("cat") ||
                variant.is(ChangedTags.LatexVariants.CAT_LIKE) ||
                variant.is(ChangedTags.LatexVariants.LEOPARD_LIKE) ||
                variant.is(TagKey.create(ChangedRegistry.LATEX_VARIANT.get().getRegistryKey(),
                        new ResourceLocation("changed_addon:cat_diet")));
    }

    private static boolean isWolfDiet(LatexEntity entity, LatexVariant<?> variant) {
        return entity.getType().getRegistryName().toString().contains("dog") ||
                entity.getType().getRegistryName().toString().contains("wolf") ||
                entity instanceof AbstractLatexWolf ||
                variant.is(ChangedTags.LatexVariants.WOLF_LIKE) ||
                variant.is(TagKey.create(ChangedRegistry.LATEX_VARIANT.get().getRegistryKey(),
                        new ResourceLocation("changed_addon:wolf_diet")));
    }

    private static boolean isSpecialDiet(LatexVariant<?> variant) {
        return variant == AddonLatexVariant.WOLFY ||
                variant.is(AddonLatexVariant.ADDON_PURO_KIND.male()) ||
                variant.is(AddonLatexVariant.ADDON_PURO_KIND.female()) ||
                variant.is(TagKey.create(ChangedRegistry.LATEX_VARIANT.get().getRegistryKey(),
                        new ResourceLocation("changed_addon:special_diet")));
    }

    private static boolean isFoxDiet(LatexEntity entity, LatexVariant<?> variant) {
        return entity.getType().getRegistryName().toString().contains("fox") ||
                variant.is(AddonLatexVariant.EXP1.male()) ||
                variant.is(AddonLatexVariant.EXP1.female()) ||
                variant.is(TagKey.create(ChangedRegistry.LATEX_VARIANT.get().getRegistryKey(),
                        new ResourceLocation("changed_addon:fox_diet")));
    }

    private static boolean isAquaticDiet(LatexEntity entity, LatexVariant<?> variant) {
        return entity instanceof AquaticEntity ||
                variant.is(ChangedTags.LatexVariants.SHARK_LIKE) ||
                variant.is(TagKey.create(ChangedRegistry.LATEX_VARIANT.get().getRegistryKey(),
                        new ResourceLocation("changed:aquatic_like"))) ||
                variant.is(TagKey.create(ChangedRegistry.LATEX_VARIANT.get().getRegistryKey(),
                        new ResourceLocation("changed_addon:aquatic_diet")));
    }

    private static boolean isDragonDiet(LatexEntity entity, LatexVariant<?> variant) {
        return entity.getType().getRegistryName().toString().contains("dragon") ||
                variant.is(TagKey.create(ChangedRegistry.LATEX_VARIANT.get().getRegistryKey(),
                        new ResourceLocation("changed:dragon_like"))) ||
                variant.is(TagKey.create(ChangedRegistry.LATEX_VARIANT.get().getRegistryKey(),
                        new ResourceLocation("changed_addon:dragon_diet")));
    }

    private enum DietType {
        CAT(List.of(
                Items.COD, Items.COOKED_COD, Items.SALMON, Items.COOKED_SALMON,
                Items.PUFFERFISH, Items.TROPICAL_FISH, Items.RABBIT, Items.COOKED_RABBIT, Items.BEEF, Items.COOKED_BEEF,
                Items.CHICKEN, Items.COOKED_CHICKEN, Items.PORKCHOP, Items.COOKED_PORKCHOP
        ), "changed_addon:cat_diet_list"),
        DRAGON(List.of(
                Items.COD, Items.COOKED_COD, Items.SALMON, Items.COOKED_SALMON,
                Items.PUFFERFISH, Items.TROPICAL_FISH, Items.RABBIT, Items.COOKED_RABBIT,
                Items.BEEF, Items.COOKED_BEEF, Items.CHICKEN, Items.COOKED_CHICKEN,
                Items.PORKCHOP, Items.COOKED_PORKCHOP, Items.MUTTON, Items.COOKED_MUTTON
        ), "changed_addon:dragon_diet_list"),
        WOLF(List.of(
                Items.RABBIT, Items.COOKED_RABBIT, Items.BEEF, Items.COOKED_BEEF,
                Items.CHICKEN, Items.COOKED_CHICKEN, Items.PORKCHOP, Items.COOKED_PORKCHOP,
                Items.MUTTON, Items.COOKED_MUTTON
        ), "changed_addon:wolf_diet_list"),
        FOX(List.of(
                Items.SWEET_BERRIES, Items.GLOW_BERRIES, Items.RABBIT, Items.COOKED_RABBIT,
                Items.BEEF, Items.COOKED_BEEF, Items.CHICKEN, Items.COOKED_CHICKEN,
                Items.PORKCHOP, Items.COOKED_PORKCHOP, Items.MUTTON, Items.COOKED_MUTTON
        ), "changed_addon:fox_diet_list"),
        AQUATIC(List.of(
                Items.DRIED_KELP, Items.COD, Items.COOKED_COD, Items.SALMON,
                Items.COOKED_SALMON, Items.PUFFERFISH, Items.TROPICAL_FISH
        ), "changed_addon:aquatic_diet_list"),
        SPECIAL(List.of(
                ChangedItems.ORANGE.get(), ChangedAddonModItems.FOXTA.get()
        ), "changed_addon:special_diet_list");

        private final List<Item> dietItems;
        private final String dietTag;

        DietType(List<Item> dietItems, String dietTag) {
            this.dietItems = dietItems;
            this.dietTag = dietTag;
        }

        public boolean isDietItem(ItemStack item) {
            return dietItems.contains(item.getItem()) ||
                    item.is(ItemTags.create(new ResourceLocation(dietTag)));
        }

        public boolean isNotFoodItem(ItemStack item) {
            List<Item> NonFoodItemslist = List.of(ChangedAddonModItems.FOXTA.get(),
                    ChangedAddonModItems.SNEPSI.get(),
                    ChangedAddonModItems.SYRINGEWITHLITIXCAMMONIA.get(),
                    ChangedAddonModItems.LAETHIN_SYRINGE.get(),
                    ChangedAddonModItems.DIFFUSION_SYRINGE.get());

            if (item.is(Items.ENCHANTED_GOLDEN_APPLE)) {
                return true;
            } else if (item.is(Items.POTION)) {
                return true;
            } else if (NonFoodItemslist.contains(item.getItem())
                    || item.is(ItemTags.create(new ResourceLocation("changed_addon:is_not_food")))) {
                return true;
            } else return item.is(Items.GOLDEN_APPLE);
        }
    }
}

