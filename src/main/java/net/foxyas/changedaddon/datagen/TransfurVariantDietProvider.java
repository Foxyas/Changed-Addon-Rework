package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.init.ChangedAddonTransfurDiets;
import net.foxyas.changedaddon.init.ChangedAddonTransfurVariants;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.FoodDietEntry;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.MobEffectHolder;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.TransfurVariantDiet;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;

public class TransfurVariantDietProvider {

    protected final String modid;

    public TransfurVariantDietProvider(String modid) {
        this.modid = modid;
    }

    public void bootstrap(BootstapContext<TransfurVariantDiet> context) {

        // Hayden Diet
        registerDiet(context, "hayden_diet",
                TransfurVariantDiet.builder()
                        .variant(ChangedAddonTransfurVariants.HAYDEN_FENNEC_FOX.get())
                        .food(FoodDietEntry.builder()
                                .item(Items.COOKIE)
                                .hunger(4)
                                .saturation(8)
                                .effect(MobEffectHolder.builder(new MobEffectInstance(MobEffects.REGENERATION, 40, 0)).build())
                                .sick(false)
                                .build())
        );

        // Canine Diet
        registerDiet(context, "canine_diet",
                TransfurVariantDiet.builder()
                        .variant(ChangedAddonTags.TransfurVariants.HAS_CANINE_DIET)
                        .defaultOffDietEffects()
                        .food(FoodDietEntry.builder()
                                .tag(ChangedAddonTags.Items.WOLF_DIET)
                                .hunger(2.0f, 4.0f)
                                .saturation(4.0f)
                                .effect(MobEffectHolder.builder(new MobEffectInstance(MobEffects.REGENERATION, 40, 0)).chance(0.5f).build())
                                .build())
        );

        // Aquatic Diet
        registerDiet(context, "aquatic_diet",
                TransfurVariantDiet.builder()
                        .variant(ChangedAddonTags.TransfurVariants.HAS_AQUATIC_DIET)
                        .defaultOffDietEffects()
                        .food(FoodDietEntry.builder()
                                .tag(ChangedAddonTags.Items.AQUATIC_DIET)
                                .hunger(2.0f, 4.0f)
                                .saturation(4.0f)
                                .effect(MobEffectHolder.builder(new MobEffectInstance(MobEffects.REGENERATION, 40, 0)).chance(0.5f).build())
                                .build())
        );

        // Shark Diet
        registerDiet(context, "shark_diet",
                TransfurVariantDiet.builder()
                        .variant(ChangedAddonTags.TransfurVariants.HAS_SHARK_DIET)
                        .defaultOffDietEffects()
                        .food(FoodDietEntry.builder()
                                .tag(ChangedAddonTags.Items.SHARK_DIET)
                                .hunger(2.0f, 4.0f)
                                .saturation(4.0f)
                                .effect(MobEffectHolder.builder(new MobEffectInstance(MobEffects.REGENERATION, 40, 0)).chance(0.5f).build())
                                .build())
        );

        // Feline Diet
        registerDiet(context, "feline_diet",
                TransfurVariantDiet.builder()
                        .variant(ChangedAddonTags.TransfurVariants.HAS_FELINE_DIET)
                        .defaultOffDietEffects()
                        .food(FoodDietEntry.builder()
                                .tag(ChangedAddonTags.Items.CAT_DIET)
                                .hunger(2.0f, 4.0f)
                                .saturation(4.0f)
                                .effect(MobEffectHolder.builder(new MobEffectInstance(MobEffects.REGENERATION, 40, 0)).chance(0.5f).build())
                                .build())
        );

        // Dragon Diet
        registerDiet(context, "dragon_diet",
                TransfurVariantDiet.builder()
                        .variant(ChangedAddonTags.TransfurVariants.HAS_DRACONIC_DIET)
                        .defaultOffDietEffects()
                        .food(FoodDietEntry.builder()
                                .tag(ChangedAddonTags.Items.DRAGON_DIET)
                                .hunger(2.0f, 4.0f)
                                .saturation(4.0f)
                                .effect(MobEffectHolder.builder(new MobEffectInstance(MobEffects.REGENERATION, 40, 0)).chance(0.5f).build())
                                .build())
        );

        // Fox Diet
        registerDiet(context, "fox_diet",
                TransfurVariantDiet.builder()
                        .variant(ChangedAddonTags.TransfurVariants.HAS_FOX_DIET)
                        .defaultOffDietEffects()
                        .food(FoodDietEntry.builder()
                                .tag(ChangedAddonTags.Items.FOX_DIET)
                                .hunger(2.0f, 4.0f)
                                .saturation(4.0f)
                                .effect(MobEffectHolder.builder(new MobEffectInstance(MobEffects.REGENERATION, 40, 0)).chance(0.5f).build())
                                .build())
        );

        // Special Diet
        registerDiet(context, "special_diet",
                TransfurVariantDiet.builder()
                        .variant(ChangedAddonTags.TransfurVariants.HAS_SPECIAL_DIET)
                        .defaultOffDietEffects()
                        .food(FoodDietEntry.builder()
                                .tag(ChangedAddonTags.Items.SPECIAL_DIET)
                                .hunger(2.0f, 4.0f)
                                .saturation(4.0f)
                                .effect(MobEffectHolder.builder(new MobEffectInstance(MobEffects.REGENERATION, 40, 0)).chance(0.5f).build())
                                .build())
        );

        // Sweet Tooth Diet
        registerDiet(context, "sweet_tooth",
                TransfurVariantDiet.builder()
                        .variant(ChangedAddonTags.TransfurVariants.HAS_SWEET_DIET)
                        .defaultOffDietEffects()
                        .food(FoodDietEntry.builder()
                                .tag(ChangedAddonTags.Items.SWEET_DIET)
                                .hunger(2.0f, 4.0f)
                                .saturation(4.0f)
                                .effects(
                                        MobEffectHolder.builder(new MobEffectInstance(MobEffects.REGENERATION, 40, 0)).chance(0.5f).build(),
                                        MobEffectHolder.builder(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 0)).chance(0.5f).build()
                                )
                                .build())
        );
    }

    // --- Helper de Registro no Datagen ---

    protected void registerDiet(BootstapContext<TransfurVariantDiet> context, String name, TransfurVariantDiet.Builder builder) {
        ResourceKey<TransfurVariantDiet> key = ResourceKey.create(
                ChangedAddonTransfurDiets.TRANSFUR_VARIANT_DIET_KEY,
                ResourceLocation.fromNamespaceAndPath(this.modid, name)
        );
        context.register(key, builder.build());
    }
}