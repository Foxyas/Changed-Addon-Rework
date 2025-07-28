package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.init.ChangedAddonGameRules;
import net.foxyas.changedaddon.process.variantsExtraStats.FormDietEvent;
import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.beast.AbstractLatexWolf;
import net.ltxprogrammer.changed.entity.beast.AquaticEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class CreatureDietsHandleProcedure {

    @SubscribeEvent
    public static void onUseItemFinish(LivingEntityUseItemEvent.Finish event) {
        if (event == null) return;

        LivingEntity livingEntity = event.getEntityLiving();
        ItemStack item = event.getItem();
        if(!item.isEdible()) return;

        if (!(livingEntity instanceof Player player)) {
            return;
        }

        TransfurVariantInstance<?> latexInstance = ProcessTransfur.getPlayerTransfurVariant(player);
        if (latexInstance == null) return;

        Level world = player.getLevel();

        if (world.isClientSide) {
            return;
        }

        if(!world.getGameRules().getBoolean(ChangedAddonGameRules.CHANGED_ADDON_CREATURE_DIETS)) return;

        ChangedEntity ChangedEntity = latexInstance.getChangedEntity();
        TransfurVariant<?> variant = ChangedEntity.getSelfVariant();

        List<DietType> dietType = determineDietsType(ChangedEntity, variant);
        if (dietType.isEmpty()) return;

        if (dietType.stream().anyMatch((diet -> diet.isDietItem(item)))) {
            applyFoodEffects(variant, player, item, true);
            if (!world.isClientSide()) {
                world.playSound(null, player, SoundEvents.GENERIC_EAT, SoundSource.MASTER, 1, 1.5f);
            }
            return;
        }

        if(!ChangedAddonServerConfiguration.DEBUFFS.get()
                || item.is(ChangedAddonTags.Items.NOT_FOOD)
                || latexInstance.ageAsVariant >= ChangedAddonServerConfiguration.AGE_NEED.get()) return;

        applyFoodEffects(variant, player, item, false);
        applyDebuffs(player);
        if (!world.isClientSide()) {
            world.playSound(null, player, SoundEvents.GENERIC_EAT, SoundSource.MASTER, 1, 0f);
        }
    }

    private static void applyFoodEffects(Player player, ItemStack item) {
        int additionalFood = 4;
        float additionalSaturation = 3;

        player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() + additionalFood);
        player.getFoodData().setSaturation(player.getFoodData().getSaturationLevel() + additionalSaturation);
    }

    private static void applyDebuffs(Player player) {
        player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 60, 3, false, true, true));
        player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 160, 0, false, true, true,
                new MobEffectInstance(MobEffects.WEAKNESS, 5 * 20, 2)));
    }

    private static void applyFoodEffects(TransfurVariant<?> variant, Player player, ItemStack item, boolean isGoodFood) {
        int additionalFood;
        float additionalSaturation;
        FoodProperties properties = item.getFoodProperties(player);
        assert properties != null;//already checked isEdible so should be fine

        if (isGoodFood) {
            additionalFood = properties.getNutrition() / 2;
            additionalSaturation = properties.getSaturationModifier() / 2;
        } else {
            additionalFood = -properties.getNutrition() / 4;
            additionalSaturation = -properties.getSaturationModifier() / 4;
        }

        FormDietEvent formDietEvent = new FormDietEvent(variant, player, isGoodFood, item, additionalFood, additionalSaturation);
        if (!MinecraftForge.EVENT_BUS.post(formDietEvent)) {
            additionalFood = formDietEvent.additionalFood;
            additionalSaturation = formDietEvent.additionalSaturation;
            player.getFoodData().eat(additionalFood, additionalSaturation);
        }
    }

    private static List<DietType> determineDietsType(ChangedEntity ChangedEntity, TransfurVariant<?> variant) {
        if(variant.is(ChangedAddonTags.TransfurTypes.NO_DIET)) return List.of();

        List<DietType> dietTypeList = new ArrayList<>();
        if (isCatDiet(ChangedEntity, variant)) dietTypeList.add(DietType.CAT);
        if (isWolfDiet(ChangedEntity, variant)) dietTypeList.add(DietType.WOLF);
        if (isSpecialDiet(variant)) dietTypeList.add(DietType.SPECIAL);
        if (isFoxDiet(ChangedEntity, variant)) dietTypeList.add(DietType.FOX);
        if (isAquaticDiet(ChangedEntity, variant)) dietTypeList.add(DietType.AQUATIC);
        if (isSharkDiet(ChangedEntity, variant)) dietTypeList.add(DietType.SHARK);
        if (isDragonDiet(ChangedEntity, variant)) dietTypeList.add(DietType.DRAGON);
        if (isSweetTooth(ChangedEntity, variant)) dietTypeList.add(DietType.SWEET_TOOTH);

        return dietTypeList;
    }

    private static boolean isCatDiet(ChangedEntity entity, TransfurVariant<?> variant) {
        return entity.getType().getRegistryName().toString().contains("cat") ||
                variant.is(ChangedAddonTags.TransfurTypes.CAT_LIKE) ||
                variant.is(ChangedAddonTags.TransfurTypes.LEOPARD_LIKE) ||
                variant.is(ChangedAddonTags.TransfurTypes.CAT_DIET);
    }

    private static boolean isWolfDiet(ChangedEntity entity, TransfurVariant<?> variant) {
        return entity.getType().getRegistryName().toString().contains("dog") ||
                entity.getType().getRegistryName().toString().contains("wolf") ||
                entity instanceof AbstractLatexWolf ||
                variant.is(ChangedAddonTags.TransfurTypes.WOLF_LIKE) ||
                variant.is(ChangedAddonTags.TransfurTypes.WOLF_DIET);
    }

    private static boolean isSpecialDiet(TransfurVariant<?> variant) {
        return variant == ChangedAddonTransfurVariants.WOLFY.get() ||
                variant.is(ChangedAddonTransfurVariants.Gendered.ADDON_PURO_KIND.getMaleVariant()) ||
                variant.is(ChangedAddonTransfurVariants.Gendered.ADDON_PURO_KIND.getFemaleVariant()) ||
                variant.is(ChangedAddonTags.TransfurTypes.SPECIAL_DIET);
    }

    private static boolean isFoxDiet(ChangedEntity entity, TransfurVariant<?> variant) {
        return entity.getType().getRegistryName().toString().contains("fox") ||
                variant.is(ChangedAddonTransfurVariants.Gendered.EXP1.getMaleVariant()) ||
                variant.is(ChangedAddonTransfurVariants.Gendered.EXP1.getFemaleVariant()) ||
                variant.is(ChangedAddonTags.TransfurTypes.FOX_LIKE) ||
                variant.is(ChangedAddonTags.TransfurTypes.FOX_DIET);
    }

    private static boolean isAquaticDiet(ChangedEntity entity, TransfurVariant<?> variant) {
        return entity instanceof AquaticEntity ||
                variant.is(ChangedAddonTags.TransfurTypes.AQUATIC_LIKE) ||
                variant.is(ChangedAddonTags.TransfurTypes.AQUATIC_DIET);
    }

    private static boolean isSharkDiet(ChangedEntity entity, TransfurVariant<?> variant) {
        return variant.is(ChangedAddonTags.TransfurTypes.SHARK_LIKE) ||
                variant.is(ChangedAddonTags.TransfurTypes.SHARK_DIET);
    }

    private static boolean isDragonDiet(ChangedEntity entity, TransfurVariant<?> variant) {
        return entity.getType().getRegistryName().toString().contains("dragon") ||
                variant.is(ChangedAddonTags.TransfurTypes.DRAGON_LIKE) ||
                variant.is(ChangedAddonTags.TransfurTypes.DRAGON_DIET);
    }

    private static boolean isSweetTooth(ChangedEntity entity, TransfurVariant<?> variant) {
        return variant.is(ChangedAddonTags.TransfurTypes.SWEET_DIET);
    }

    private enum DietType {
        AQUATIC(ChangedAddonTags.Items.AQUATIC_DIET),
        SHARK(ChangedAddonTags.Items.SHARK_DIET),
        CAT(ChangedAddonTags.Items.CAT_DIET),
        DRAGON(ChangedAddonTags.Items.DRAGON_DIET),
        FOX(ChangedAddonTags.Items.FOX_DIET),
        SWEET_TOOTH(ChangedAddonTags.Items.SWEET_DIET),
        WOLF(ChangedAddonTags.Items.WOLF_DIET),
        SPECIAL(ChangedAddonTags.Items.SPECIAL_DIET);

        private final TagKey<Item> dietTag;

        DietType(TagKey<Item> dietTag) {
            this.dietTag = dietTag;
        }

        public boolean isDietItem(ItemStack item) {
            //Registry.ITEM.getTag(dietTag).get().stream().count(); //TODO check empty?
            return item.is(dietTag);
        }
    }
}

