package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.init.ChangedTagsExtension;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import static net.foxyas.changedaddon.init.ChangedAddonItems.*;

public class ItemTagsProvider extends net.minecraft.data.tags.ItemTagsProvider {

    static final TagKey<Item> forgeRawIridium = ItemTags.create(new ResourceLocation("forge", "raw_materials/iridium"));
    static final TagKey<Item> forgeIngotsIridium = ItemTags.create(new ResourceLocation("forge", "ingots/iridium"));
    static final TagKey<Item> forgeStorageBlocksIridium = ItemTags.create(new ResourceLocation("forge", "storage_blocks/iridium"));

    public ItemTagsProvider(DataGenerator generator, BlockTagsProvider blocks, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, blocks, ChangedAddonMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(Tags.Items.RAW_MATERIALS).add(RAW_IRIDIUM.get());
        tag(forgeRawIridium).add(RAW_IRIDIUM.get());

        tag(Tags.Items.INGOTS).add(IRIDIUM.get());
        tag(forgeIngotsIridium).add(IRIDIUM.get());

        tag(Tags.Items.STORAGE_BLOCKS).add(IRIDIUM_BLOCK.get());
        tag(forgeStorageBlocksIridium).add(IRIDIUM_BLOCK.get());



        tag(ChangedTagsExtension.AccessoryItemsTags.BODY).add(
                DYEABLE_SHIRT.get());
        tag(ChangedTagsExtension.AccessoryItemsTags.LEGS).add(
                DYEABLE_SHORTS.get());

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
                ORANGEJUICE.get(),
                ChangedItems.WHITE_LATEX_GOO.get(),
                ChangedItems.DARK_LATEX_GOO.get(),
                Items.GOLDEN_APPLE,
                Items.ENCHANTED_GOLDEN_APPLE,
                Items.POTION);

        tag(ChangedAddonTags.Items.SYRINGES).add(
                ChangedItems.SYRINGE.get(),
                SYRINGE.get());
        tag(ChangedAddonTags.Items.UNTRANSFURS).add(
                POT_WITH_CAMONIA.get(),
                SYRINGE_WITH_LITIX_CAMMONIA.get());
        tag(ChangedAddonTags.Items.LATEX_SOLVENT_APPLICABLE).add(
                CRYSTAL_DAGGER_BLACK.get(),
                CRYSTAL_DAGGER_GREEN.get(),
                CRYSTAL_DAGGER_RED.get(),
                ELECTRIC_KATANA.get(),
                ELECTRIC_KATANA_RED.get(),
                THE_DECIMATOR.get(),
                PAINITE_SWORD.get(),
                PAINITE_AXE.get(),
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


        tag(ChangedAddonTags.Items.AQUATIC_DIET).add(
                Items.DRIED_KELP,
                Items.COD,
                Items.COOKED_COD,
                Items.SALMON,
                Items.COOKED_SALMON,
                Items.PUFFERFISH,
                Items.TROPICAL_FISH);

        tag(ChangedAddonTags.Items.SHARK_DIET);

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
    }
}
