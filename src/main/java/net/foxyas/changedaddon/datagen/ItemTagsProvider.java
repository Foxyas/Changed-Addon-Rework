package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.init.ChangedTags;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static net.foxyas.changedaddon.init.ChangedAddonItems.*;

public class ItemTagsProvider extends net.minecraft.data.tags.ItemTagsProvider {

    static final TagKey<Item> forgeRawIridium = ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "raw_materials/iridium"));
    static final TagKey<Item> forgeIngotsIridium = ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "ingots/iridium"));
    static final TagKey<Item> forgeStorageBlocksIridium = ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "storage_blocks/iridium"));

    public ItemTagsProvider(DataGenerator generator, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> tagLookup, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator.getPackOutput(), lookupProvider, tagLookup, ChangedAddonMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        tag(ChangedAddonTags.Items.AIR).add(Items.AIR);
        tag(Tags.Items.RAW_MATERIALS).add(RAW_IRIDIUM.get());
        tag(forgeRawIridium).add(RAW_IRIDIUM.get());
        tag(ItemTags.TRIM_MATERIALS).add(IRIDIUM.get(), GOO_CORE_FRAGMENT.get());

        tag(Tags.Items.INGOTS).add(IRIDIUM.get());
        tag(forgeIngotsIridium).add(IRIDIUM.get());

        tag(Tags.Items.STORAGE_BLOCKS).add(IRIDIUM_BLOCK.get());
        tag(forgeStorageBlocksIridium).add(IRIDIUM_BLOCK.get());
        tag(ChangedAddonTags.Items.MAKE_TRANSFUR_SAFE).add(Items.ENCHANTED_GOLDEN_APPLE);
        tag(ChangedAddonTags.Items.STABILIZER_TICKS).add(LUMINARA_BLOOM_PETALS.get());

        tag(ItemTags.PLANKS).add(ChangedAddonItems.LUMINARA_PLANKS.get());
        tag(ItemTags.SLABS).add(LUMINARA_SLAB.get());
        tag(ItemTags.WOODEN_DOORS).add(LUMINARA_DOOR.get());
        tag(ItemTags.WOODEN_TRAPDOORS).add(LUMINARA_TRAPDOOR.get());
        tag(ItemTags.WOODEN_FENCES).add(LUMINARA_FENCE.get());
        tag(ItemTags.FENCE_GATES).add(LUMINARA_FENCE_GATE.get());
        tag(Tags.Items.FENCE_GATES_WOODEN).add(LUMINARA_FENCE_GATE.get());
        tag(ItemTags.SIGNS).add(LUMINARA_SIGN.get(), LUMINARA_HANGING_SIGN.get());
        tag(ItemTags.WOODEN_BUTTONS).add(LUMINARA_BUTTON.get());
        tag(ItemTags.WOODEN_PRESSURE_PLATES).add(LUMINARA_PRESSURE_PLATE.get());
        tag(ChangedAddonTags.Items.LUMINARA_LOGS).add(LUMINARA_LOG.get(), STRIPPED_LUMINARA_LOG.get(), LUMINARA_WOOD.get(), STRIPPED_LUMINARA_WOOD.get());

        tag(ChangedAddonTags.Items.UNIFUSER_RECIPE_CATALYST).add(ChangedAddonItems.CATALYZED_DNA.get(),
                ChangedItems.BLOOD_SYRINGE.get(),
                ChangedItems.LATEX_SYRINGE.get());

        tag(ChangedTags.AccessoryItems.FULL_BODY)
                .add(HAZARD_BODY_SUIT.get());
        tag(ChangedTags.AccessoryItems.BODY).add(
                DYEABLE_TSHIRT.get());
        tag(ChangedTags.AccessoryItems.LEGS).add(
                DYEABLE_SHORTS.get());

        tag(ChangedAddonTags.Items.METAL).add( //TODO add guns?
                Items.IRON_SWORD, Items.IRON_PICKAXE, Items.IRON_AXE, Items.IRON_HOE, Items.IRON_SHOVEL,
                Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS,
                ChangedItems.IRON_QUADRUPEDAL_LEGGINGS.get(), ChangedItems.IRON_QUADRUPEDAL_BOOTS.get(), ChangedItems.IRON_UPPER_ABDOMEN_ARMOR.get(), ChangedItems.IRON_LOWER_ABDOMEN_ARMOR.get(),

                Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS,
                ChangedItems.CHAINMAIL_QUADRUPEDAL_LEGGINGS.get(), ChangedItems.CHAINMAIL_QUADRUPEDAL_BOOTS.get(), ChangedItems.CHAINMAIL_UPPER_ABDOMEN_ARMOR.get(), ChangedItems.CHAINMAIL_LOWER_ABDOMEN_ARMOR.get(),

                Items.GOLDEN_SWORD, Items.GOLDEN_PICKAXE, Items.GOLDEN_AXE, Items.GOLDEN_HOE, Items.GOLDEN_SHOVEL,
                Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS,
                ChangedItems.GOLDEN_QUADRUPEDAL_LEGGINGS.get(), ChangedItems.GOLDEN_QUADRUPEDAL_BOOTS.get(), ChangedItems.GOLDEN_UPPER_ABDOMEN_ARMOR.get(), ChangedItems.GOLDEN_LOWER_ABDOMEN_ARMOR.get(),

                ChangedItems.TSC_BATON.get(), ChangedItems.TSC_SHIELD.get(), ChangedItems.TSC_STAFF.get(), ChangedItems.EXOSKELETON.get(),
                CROWBAR.get());

        tag(ChangedAddonTags.Items.PARTIAL_METAL).add(
                Items.NETHERITE_SWORD, Items.NETHERITE_PICKAXE, Items.NETHERITE_AXE, Items.NETHERITE_HOE, Items.NETHERITE_SHOVEL,
                Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS,
                ChangedItems.NETHERITE_QUADRUPEDAL_LEGGINGS.get(), ChangedItems.NETHERITE_QUADRUPEDAL_BOOTS.get(), ChangedItems.NETHERITE_UPPER_ABDOMEN_ARMOR.get(), ChangedItems.NETHERITE_LOWER_ABDOMEN_ARMOR.get(),

                THE_DECIMATOR.get(), LAETHINMINATOR.get(), LUMINAR_CRYSTAL_SPEAR.get());

        tag(ChangedAddonTags.Items.BLOOD_TYPE_SYRINGE).add(
                ChangedItems.BLOOD_SYRINGE.get(),
                ChangedItems.LATEX_SYRINGE.get());

        tag(ChangedAddonTags.Items.GOOEY).add(
                Items.SLIME_BALL,
                ChangedItems.WHITE_LATEX_GOO.get(),
                ChangedItems.DARK_LATEX_GOO.get());

        tag(ChangedAddonTags.Items.MEAT).add(
                Items.PUFFERFISH,
                Items.MUTTON,
                Items.BEEF,
                Items.RABBIT,
                Items.CHICKEN,
                Items.COD,
                Items.PORKCHOP,
                Items.ROTTEN_FLESH,
                Items.SALMON,
                Items.TROPICAL_FISH);
        tag(ChangedAddonTags.Items.NOT_FOOD).add(
                SNEPSI.get(),
                DIFFUSION_SYRINGE.get(),
                FOXTA.get(),
                POT_WITH_CAMONIA.get(),
                LAETHIN_SYRINGE.get(),
                SYRINGE_WITH_LITIX_CAMMONIA.get(),
                ORANGE_JUICE.get(),
                ChangedItems.WHITE_LATEX_GOO.get(),
                ChangedItems.DARK_LATEX_GOO.get(),
                Items.GOLDEN_APPLE,
                Items.ENCHANTED_GOLDEN_APPLE,
                Items.POTION);

        tag(ChangedAddonTags.Items.SYRINGES).add(
                ChangedItems.SYRINGE.get(),
                SYRINGE.get());
        tag(ChangedAddonTags.Items.UNTRANSFUR_ITEMS).add(
                POT_WITH_CAMONIA.get(),
                SYRINGE_WITH_LITIX_CAMMONIA.get());
        tag(ChangedAddonTags.Items.LATEX_SOLVENT_APPLICABLE).add(
                CRYSTAL_DAGGER_BLACK.get(),
                CRYSTAL_DAGGER_GREEN.get(),
                CRYSTAL_DAGGER_RED.get(),
                ELECTRIC_KATANA.get(),
                ELECTRIC_KATANA_RED.get(),
                THE_DECIMATOR.get(),
                CROWBAR.get(),
                Items.WOODEN_SWORD,
                Items.WOODEN_AXE,
                Items.STONE_SWORD,
                Items.STONE_AXE,
                Items.IRON_SWORD,
                Items.IRON_AXE,
                Items.GOLDEN_SWORD,
                Items.GOLDEN_AXE,
                Items.DIAMOND_SWORD,
                Items.DIAMOND_AXE,
                Items.NETHERITE_SWORD,
                Items.NETHERITE_AXE,
                Items.TRIDENT);

        tag(ChangedAddonTags.Items.TAME_ITEM);

        tag(ItemTags.SMALL_FLOWERS).add(LUMINARA_BLOOM.get());


        tag(ChangedAddonTags.Items.AQUATIC_DIET).add(
                Items.DRIED_KELP,
                Items.COD,
                Items.COOKED_COD,
                Items.SALMON,
                Items.COOKED_SALMON,
                Items.PUFFERFISH,
                Items.TROPICAL_FISH);

        tag(ChangedAddonTags.Items.SHARK_DIET)
                .addTag(ChangedAddonTags.Items.AQUATIC_DIET).remove(Items.DRIED_KELP);

        tag(ChangedAddonTags.Items.CAT_DIET).add(
                Items.COD,
                Items.COOKED_COD,
                Items.SALMON,
                Items.COOKED_SALMON,
                Items.PUFFERFISH,
                Items.TROPICAL_FISH,
                Items.RABBIT,
                Items.COOKED_RABBIT,
                Items.BEEF,
                Items.COOKED_BEEF,
                Items.CHICKEN,
                Items.COOKED_CHICKEN,
                Items.PORKCHOP,
                Items.COOKED_PORKCHOP);

        tag(ChangedAddonTags.Items.DRAGON_DIET).add(
                Items.COD,
                Items.COOKED_COD,
                Items.SALMON,
                Items.COOKED_SALMON,
                Items.PUFFERFISH,
                Items.TROPICAL_FISH,
                Items.RABBIT,
                Items.COOKED_RABBIT,
                Items.BEEF,
                Items.COOKED_BEEF,
                Items.CHICKEN,
                Items.COOKED_CHICKEN,
                Items.PORKCHOP,
                Items.COOKED_PORKCHOP,
                Items.MUTTON,
                Items.COOKED_MUTTON);

        tag(ChangedAddonTags.Items.FOX_DIET).add(
                Items.SWEET_BERRIES,
                Items.GLOW_BERRIES,
                Items.RABBIT,
                Items.COOKED_RABBIT,
                Items.BEEF,
                Items.COOKED_BEEF,
                Items.CHICKEN,
                Items.COOKED_CHICKEN,
                Items.PORKCHOP,
                Items.COOKED_PORKCHOP,
                Items.MUTTON,
                Items.COOKED_MUTTON);

        tag(ChangedAddonTags.Items.SPECIAL_DIET).add(
                ChangedItems.ORANGE.get(),
                FOXTA.get());

        tag(ChangedAddonTags.Items.SWEET_DIET).add(
                Items.COOKIE,
                Items.PUMPKIN_PIE,
                Items.HONEY_BOTTLE,
                Items.GLOW_BERRIES,
                Items.SWEET_BERRIES);

        tag(ChangedAddonTags.Items.WOLF_DIET).add(
                Items.RABBIT,
                Items.COOKED_RABBIT,
                Items.BEEF,
                Items.COOKED_BEEF,
                Items.CHICKEN,
                Items.COOKED_CHICKEN,
                Items.PORKCHOP,
                Items.COOKED_PORKCHOP,
                Items.MUTTON,
                Items.COOKED_MUTTON);

        tag(ItemTags.BEACON_PAYMENT_ITEMS).add(
                PAINITE.get(),
                IRIDIUM.get());

        tag(ChangedAddonTags.Items.UNTRANSFUR_AGENTS).add(ANTI_LATEX_BASE.get());
        tag(ChangedAddonTags.Items.UNTRANSFUR_CATALYZERS).add(LUMINARA_BLOOM_PETALS.get(), LUMINARA_BLOOM.get(), LUMINARA_SAPLING.get());
    }
}
