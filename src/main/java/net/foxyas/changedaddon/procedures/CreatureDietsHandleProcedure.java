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

        List<DietType> dietType = determineDietTypes(ChangedEntity, variant);
        if (dietType.isEmpty()) return;

        if (dietType.stream().anyMatch((diet -> diet.isDietItem(item)))) {
            applyFoodEffects(variant, player, item, true);

            world.playSound(null, player, SoundEvents.GENERIC_EAT, SoundSource.MASTER, 1, 1.5f);
            return;
        }

        if(!ChangedAddonServerConfiguration.DEBUFFS.get()
                || item.is(ChangedAddonTags.Items.NOT_FOOD)
                || latexInstance.ageAsVariant >= ChangedAddonServerConfiguration.AGE_NEED.get()) return;

        applyFoodEffects(variant, player, item, false);
        applyDebuffs(player);
        world.playSound(null, player, SoundEvents.GENERIC_EAT, SoundSource.MASTER, 1, 0f);
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

    private static List<DietType> determineDietTypes(ChangedEntity ChangedEntity, TransfurVariant<?> variant) {
        if(variant.is(ChangedAddonTags.TransfurTypes.NO_DIET)) return List.of();

        List<DietType> dietTypes = new ArrayList<>();
        for(DietType diet : DietType.values()){
            if(diet.hasDiet(ChangedEntity, variant)) dietTypes.add(diet);
        }

        return dietTypes;
    }

    private enum DietType {
        AQUATIC(ChangedAddonTags.TransfurTypes.AQUATIC_DIET, ChangedAddonTags.Items.AQUATIC_DIET),
        SHARK(ChangedAddonTags.TransfurTypes.SHARK_DIET, ChangedAddonTags.Items.SHARK_DIET),
        CAT(ChangedAddonTags.TransfurTypes.CAT_DIET, ChangedAddonTags.Items.CAT_DIET),
        DRAGON(ChangedAddonTags.TransfurTypes.DRAGON_DIET, ChangedAddonTags.Items.DRAGON_DIET),
        FOX(ChangedAddonTags.TransfurTypes.FOX_DIET, ChangedAddonTags.Items.FOX_DIET),
        SWEET_TOOTH(ChangedAddonTags.TransfurTypes.SWEET_DIET, ChangedAddonTags.Items.SWEET_DIET),
        WOLF(ChangedAddonTags.TransfurTypes.WOLF_DIET, ChangedAddonTags.Items.WOLF_DIET),
        SPECIAL(ChangedAddonTags.TransfurTypes.SPECIAL_DIET, ChangedAddonTags.Items.SPECIAL_DIET);

        private final TagKey<TransfurVariant<?>> tfTag;
        private final TagKey<Item> dietTag;

        DietType(TagKey<TransfurVariant<?>> tfTag, TagKey<Item> dietTag) {
            this.tfTag = tfTag;
            this.dietTag = dietTag;
        }

        public boolean hasDiet(ChangedEntity entity, TransfurVariant<?> tf){
            if(this == AQUATIC && entity instanceof AquaticEntity && !SHARK.hasDiet(entity, tf)) return true;
            if(this == WOLF && entity instanceof AbstractLatexWolf) return true;

            return tf.is(tfTag);
        }

        public boolean isDietItem(ItemStack item) {
            return item.is(dietTag);
        }
    }
}

