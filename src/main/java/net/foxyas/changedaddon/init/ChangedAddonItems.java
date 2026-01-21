package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.item.*;
import net.foxyas.changedaddon.item.api.ColorHolder;
import net.foxyas.changedaddon.item.armor.DarkLatexCoatItem;
import net.foxyas.changedaddon.item.armor.HazardBodySuit;
import net.foxyas.changedaddon.item.clothes.DyeableShortsItem;
import net.foxyas.changedaddon.item.clothes.TShirtClothingItem;
import net.foxyas.changedaddon.procedure.DotValueOfViewProcedure;
import net.foxyas.changedaddon.procedure.LaethinPropertyValueProviderProcedure;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedLatexTypes;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.awt.*;
import java.util.List;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ChangedAddonItems {

    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ChangedAddonMod.MODID);

    public static final RegistryObject<Item> CHANGED_BOOK = REGISTRY.register("changedbook", ChangedBookItem::new);
    public static final RegistryObject<Item> LUMINARA_BLOOM = block(ChangedAddonBlocks.LUMINARA_BLOOM, new Item.Properties().rarity(Rarity.RARE));
    public static final RegistryObject<Item> LUMINARA_BLOOM_PETALS = REGISTRY.register("luminara_bloom_petals", LuminaraBloomPetalsItem::new);
    public static final RegistryObject<Item> BIOMASS = REGISTRY.register("biomass", BiomassItem::new);
    public static final RegistryObject<Item> ANTI_LATEX_BASE = REGISTRY.register("anti_latex_base", UnlatexbaseItem::new);
    public static final RegistryObject<Item> IMPURE_AMMONIA = REGISTRY.register("impure_ammonia", ImpureAmmoniaItem::new);
    public static final RegistryObject<Item> AMMONIA_PARTICLE = REGISTRY.register("ammonia_particle", AmmoniaParticleItem::new);
    public static final RegistryObject<Item> AMMONIA_COMPRESSED = REGISTRY.register("ammonia_compressed", AmmoniaCompressedItem::new);
    public static final RegistryObject<Item> AMMONIA = REGISTRY.register("ammonia", AmmoniaItem::new);
    public static final RegistryObject<Item> LITIX_CAMONIA = REGISTRY.register("litix_camonia", LitixCamoniaItem::new);
    public static final RegistryObject<Item> LAETHIN = REGISTRY.register("laethin", LaethinItem::new);
    public static final RegistryObject<Item> CATALYZED_DNA = REGISTRY.register("catalyzed_dna", CatalyzedDNAItem::new);

    public static final RegistryObject<Item> SYRINGE = REGISTRY.register("syringe", SyringeItem::new);
    public static final RegistryObject<Item> DIFFUSION_SYRINGE = REGISTRY.register("diffusion_syringe", DiffusionSyringeItem::new);
    public static final RegistryObject<Item> SYRINGE_WITH_LITIX_CAMMONIA = REGISTRY.register("syringe_with_litix_cammonia", SyringeWithLitixCammoniaItem::new);
    public static final RegistryObject<Item> LAETHIN_SYRINGE = REGISTRY.register("laethin_syringe", LaethinSyringeItem::new);
    public static final RegistryObject<Item> POT_WITH_CAMONIA = REGISTRY.register("pot_with_camonia", PotWithCamoniaItem::new);

    public static final RegistryObject<Item> RAW_IRIDIUM = REGISTRY.register("raw_iridium", RawIridiumItem::new);
    public static final RegistryObject<Item> IRIDIUM = REGISTRY.register("iridium", IridiumItem::new);
    public static final RegistryObject<BlockItem> DEEPSLATE_IRIDIUM_ORE = block(ChangedAddonBlocks.DEEPSLATE_IRIDIUM_ORE);
    public static final RegistryObject<BlockItem> IRIDIUM_BLOCK = block(ChangedAddonBlocks.IRIDIUM_BLOCK);

    public static final RegistryObject<Item> PAINITE = REGISTRY.register("painite", PainiteGemItem::new);
    public static final RegistryObject<Item> ACCESSORIES_CHESTPLATE = REGISTRY.register("accessories_chestplate", AccessoriesItem.Chestplate::new);
    public static final RegistryObject<BlockItem> PAINITE_ORE = block(ChangedAddonBlocks.DEEPSLATE_PAINITE_ORE);
    public static final RegistryObject<BlockItem> PAINITE_BLOCK = block(ChangedAddonBlocks.PAINITE_BLOCK);

    public static final RegistryObject<Item> LITIX_CAMONIA_FLUID_BUCKET = REGISTRY.register("litix_camonia_fluid_bucket", LitixCamoniaFluidItem::new);

    public static final RegistryObject<Item> EXPERIMENT_009_DNA = REGISTRY.register("experiment_009_dna", Experiment009dnaItem::new);
    public static final RegistryObject<Item> EXP_9_LATEX_BASE = REGISTRY.register("exp_9_latex_base", Exp9LatexBaseItem::new);
    public static final RegistryObject<Item> EXP_9_CONTAINMENT_VIAL = REGISTRY.register("exp_9_containment_vial", Experiment009SpawnerItem::new);
    public static final RegistryObject<TransfurTotemItem> TRANSFUR_TOTEM = REGISTRY.register("transfur_totem", TransfurTotemItem::new);

    public static final RegistryObject<Item> EXPERIMENT_10_DNA = REGISTRY.register("experiment_10_dna", Experiment10DnaItem::new);
    public static final RegistryObject<Item> EXP_10_LATEX_BASE = REGISTRY.register("exp_10_latex_base", Exp10LatexBaseItem::new);
    public static final RegistryObject<Item> EXP_10_CONTAINMENT_VIAL = REGISTRY.register("exp_10_containment_vial", Experiment10SpawnerItem::new);
    public static final RegistryObject<Item> RED_LATEX_GOO = REGISTRY.register("red_latex_goo", RedLatexGooItem::new);

    // Foods and Drinks
    public static final RegistryObject<Item> ORANGE_JUICE = REGISTRY.register("orange_juice", OrangeJuiceItem::new);
    public static final RegistryObject<Item> SNEPSI = REGISTRY.register("snepsi", SnepsiItem::new);
    public static final RegistryObject<Item> FOXTA = REGISTRY.register("foxta", FoxtaItem::new);
    public static final RegistryObject<Item> GOLDEN_ORANGE = REGISTRY.register("golden_orange", GoldenOrange::new);
    public static final RegistryObject<Item> OPENED_CANNED_SOUP = REGISTRY.register("opened_canned_soup", OpenedCannedSoupItem::new);

    // Remain Items
    public static final RegistryObject<Item> EMPTY_CAN = REGISTRY.register("empty_can", EmptyCanItem::new);


    public static final RegistryObject<BlockItem> SNEP_PLUSHY = block(ChangedAddonBlocks.SNEP_PLUSHY);
    public static final RegistryObject<BlockItem> WOLF_PLUSHY = block(ChangedAddonBlocks.WOLF_PLUSHY);
    public static final RegistryObject<DarkLatexWolfPlushyItem> DARK_LATEX_WOLF_PLUSH = REGISTRY.register("dark_latex_wolf_plushy", DarkLatexWolfPlushyItem::new);
    public static final RegistryObject<BlockItem> CATALYZER = block(ChangedAddonBlocks.CATALYZER);
    public static final RegistryObject<BlockItem> UNIFUSER = block(ChangedAddonBlocks.UNIFUSER);
    public static final RegistryObject<BlockItem> ADVANCED_UNIFUSER = block(ChangedAddonBlocks.ADVANCED_UNIFUSER);
    public static final RegistryObject<BlockItem> ADVANCED_CATALYZER = block(ChangedAddonBlocks.ADVANCED_CATALYZER);
    public static final RegistryObject<BlockItem> REINFORCED_WALL = block(ChangedAddonBlocks.REINFORCED_WALL);
    public static final RegistryObject<BlockItem> REINFORCED_WALL_SILVER_STRIPED = block(ChangedAddonBlocks.REINFORCED_WALL_SILVER_STRIPED);
    public static final RegistryObject<BlockItem> REINFORCED_WALL_SILVER_TILED = block(ChangedAddonBlocks.REINFORCED_WALL_SILVER_TILED);
    public static final RegistryObject<BlockItem> REINFORCED_WALL_CAUTION = block(ChangedAddonBlocks.REINFORCED_WALL_CAUTION);
    public static final RegistryObject<BlockItem> REINFORCED_CROSS_BLOCK = block(ChangedAddonBlocks.REINFORCED_CROSS_BLOCK);
    public static final RegistryObject<BlockItem> WALL_WHITE_CRACKED = block(ChangedAddonBlocks.WALL_WHITE_CRACKED);
    public static final RegistryObject<BlockItem> CONTAINMENT_CONTAINER = block(ChangedAddonBlocks.CONTAINMENT_CONTAINER);

    public static final RegistryObject<BlockItem> LATEX_INSULATOR = block(ChangedAddonBlocks.LATEX_INSULATOR);
    public static final RegistryObject<BlockItem> DARK_LATEX_PUDDLE = block(ChangedAddonBlocks.DARK_LATEX_PUDDLE);
    public static final RegistryObject<BlockItem> DORMANT_DARK_LATEX = block(ChangedAddonBlocks.DORMANT_DARK_LATEX);
    public static final RegistryObject<BlockItem> DORMANT_WHITE_LATEX = block(ChangedAddonBlocks.DORMANT_WHITE_LATEX);
    public static final RegistryObject<BlockItem> SIGNAL_BLOCK = block(ChangedAddonBlocks.SIGNAL_BLOCK);
    public static final RegistryObject<Item> SIGNAL_CATCHER = REGISTRY.register("signal_catcher", SignalCatcherItem::new);
    public static final RegistryObject<TranslatorItem> TRANSLATOR = REGISTRY.register("translator", TranslatorItem::new);
    public static final RegistryObject<BlockItem> INFORMANT_BLOCK = block(ChangedAddonBlocks.INFORMANT_BLOCK);

    public static final RegistryObject<Item> LUMINAR_CRYSTAL_SHARD = REGISTRY.register("luminar_crystal_shard", LuminarCrystalShardItem::new);
    public static final RegistryObject<Item> LUMINAR_CRYSTAL_SHARD_HEARTED = REGISTRY.register("luminar_crystal_shard_hearted", LuminarCrystalShardHeartedItem::new);
    public static final RegistryObject<BlockItem> LUMINAR_CRYSTAL_SMALL = block(ChangedAddonBlocks.LUMINAR_CRYSTAL_SMALL);
    public static final RegistryObject<BlockItem> LUMINAR_CRYSTAL_LARGE = block(ChangedAddonBlocks.LUMINAR_CRYSTAL_LARGE);
    public static final RegistryObject<BlockItem> LUMINAR_CRYSTAL_BLOCK = block(ChangedAddonBlocks.LUMINAR_CRYSTAL_BLOCK);

    public static final RegistryObject<Item> YELLOW_WOLF_CRYSTAL_FRAGMENT = REGISTRY.register("yellow_wolf_crystal_fragment", YellowWolfCrystalFragmentItem::new);
    public static final RegistryObject<BlockItem> YELLOW_WOLF_CRYSTAL_SMALL = block(ChangedAddonBlocks.YELLOW_WOLF_CRYSTAL_SMALL);
    public static final RegistryObject<BlockItem> YELLOW_WOLF_CRYSTAL_BLOCK = block(ChangedAddonBlocks.YELLOW_WOLF_CRYSTAL_BLOCK);

    public static final RegistryObject<Item> ORANGE_WOLF_CRYSTAL_FRAGMENT = REGISTRY.register("orange_wolf_crystal_fragment", OrangeWolfCrystalFragmentItem::new);
    public static final RegistryObject<BlockItem> ORANGE_WOLF_CRYSTAL_SMALL = block(ChangedAddonBlocks.ORANGE_WOLF_CRYSTAL_SMALL);
    public static final RegistryObject<BlockItem> ORANGE_WOLF_CRYSTAL_BLOCK = block(ChangedAddonBlocks.ORANGE_WOLF_CRYSTAL_BLOCK);

    public static final RegistryObject<Item> WHITE_WOLF_CRYSTAL_FRAGMENT = REGISTRY.register("white_wolf_crystal_fragment", WhiteWolfCrystalFragmentItem::new);
    public static final RegistryObject<BlockItem> WHITE_WOLF_CRYSTAL_SMALL = block(ChangedAddonBlocks.WHITE_WOLF_CRYSTAL_SMALL);
    public static final RegistryObject<BlockItem> WHITE_WOLF_CRYSTAL_BLOCK = block(ChangedAddonBlocks.WHITE_WOLF_CRYSTAL_BLOCK);

    public static final RegistryObject<Item> BLUE_WOLF_CRYSTAL_FRAGMENT = REGISTRY.register("blue_wolf_crystal_fragment", BlueWolfCrystalFragmentItem::new);
    public static final RegistryObject<BlockItem> BLUE_WOLF_CRYSTAL_SMALL = block(ChangedAddonBlocks.BLUE_WOLF_CRYSTAL_SMALL);
    public static final RegistryObject<BlockItem> BLUE_WOLF_CRYSTAL_BLOCK = block(ChangedAddonBlocks.BLUE_WOLF_CRYSTAL_BLOCK);
    public static final RegistryObject<Item> GOO_CORE_FRAGMENT = REGISTRY.register("goo_core_fragment", GooCoreFragmentItem::new);
    public static final RegistryObject<BlockItem> GOO_CORE = block(ChangedAddonBlocks.GOO_CORE);
    public static final RegistryObject<Item> ELECTRIC_KATANA = REGISTRY.register("electric_katana", ElectricKatanaItem::new);
    public static final RegistryObject<Item> ELECTRIC_KATANA_RED = REGISTRY.register("electric_katana_red", ElectricKatanaRedItem::new);
    public static final RegistryObject<Item> MEANINGLESS_STRAFE_MUSIC_DISC = REGISTRY.register("meaningless_strafe_music_disc", MeaninglessStrafeMusicDiscItem::new);
    public static final RegistryObject<Item> LUMINAR_CRYSTAL_SPEAR = REGISTRY.register("luminar_crystal_spear", LuminarCrystalSpearItem::new);
    public static final RegistryObject<Item> THE_DECIMATOR = REGISTRY.register("the_decimator", TheDecimatorItem::new);
    public static final RegistryObject<Item> CROWBAR = REGISTRY.register("crow_bar", CrowbarItem::new);
    public static final RegistryObject<Item> LAETHINMINATOR = REGISTRY.register("laethinminator", LaethinminatorItem::new);
    public static final RegistryObject<Item> CRYSTAL_DAGGER_RED = REGISTRY.register("crystal_dagger_red", CrystalAddagerRedItem::new);
    public static final RegistryObject<Item> CRYSTAL_DAGGER_GREEN = REGISTRY.register("crystal_dagger_green", CrystalAddagerGreenItem::new);
    public static final RegistryObject<Item> CRYSTAL_DAGGER_BLACK = REGISTRY.register("crystal_dagger_black", CrystalAddagerBlackItem::new);
    public static final RegistryObject<Item> EMPTY_SPRAY = REGISTRY.register("empty_spray", EmptySprayItem::new);
    public static final RegistryObject<Item> LITIX_CAMONIA_SPRAY = REGISTRY.register("litix_camonia_spray", () -> new SprayItem(ChangedLatexTypes.NONE::get));
    public static final RegistryObject<Item> DARK_LATEX_SPRAY = REGISTRY.register("dark_latex_spray", () -> new SprayItem(ChangedLatexTypes.DARK_LATEX::get));
    public static final RegistryObject<Item> WHITE_LATEX_SPRAY = REGISTRY.register("white_latex_spray", () -> new SprayItem(ChangedLatexTypes.WHITE_LATEX::get));
    public static final RegistryObject<Item> LUNAR_ROSE = REGISTRY.register("lunar_rose", LunarRoseItem::new);

    public static final RegistryObject<Item> GENERATOR = blockNoTab(ChangedAddonBlocks.GENERATOR);
    public static final RegistryObject<Item> CATALYZER_BLOCK_ILLUSTRATIVE_ITEM = REGISTRY.register("catalyzer_block_illustrative_item", CatalyzerBlockIllustrativeItemItem::new);
    public static final RegistryObject<Item> UNIFUSER_BLOCK_ILLUSTRATIVE_ITEM = REGISTRY.register("unifuser_block_illustrative_item", UnifuserBlockIllustrativeItemItem::new);
    public static final RegistryObject<Item> SNEP_ICON = REGISTRY.register("snep_icon", SnepIconItem::new);
    public static final RegistryObject<Item> FRIENDLY_GOEY_ICON = REGISTRY.register("friendly_goey_icon", FriendlyGoeyIconItem::new);
    public static final RegistryObject<Item> PAT_ICON = REGISTRY.register("pat_icon", PatIconItem::new);

    public static final RegistryObject<BlockItem> COVER_ITEM = REGISTRY.register("cover", () -> new BlockItem(ChangedAddonBlocks.COVER_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> DARK_LATEX_COVER_ITEM = REGISTRY.register("dark_latex_cover", () -> new BlockItem(ChangedAddonBlocks.DARK_LATEX_COVER_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> WHITE_LATEX_COVER_ITEM = REGISTRY.register("white_latex_cover", () -> new BlockItem(ChangedAddonBlocks.WHITE_LATEX_COVER_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> WOLF_CRYSTAL_PILLAR = block(ChangedAddonBlocks.WOLF_CRYSTAL_PILLAR);

    // --- MOBS SPAWN EGGS ---
    public static final RegistryObject<Item> ERIK_SPAWN_EGG = REGISTRY.register("erik_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.ERIK, -1, -1, new Item.Properties()));
    public static final RegistryObject<Item> SPAWNEGGOFFOXYAS = REGISTRY.register("spawneggoffoxyas", SpawnEggOfFoxyasItem::new);

    // --- CHANGED ENTITIES SPAWN EGGS ---
    public static final RegistryObject<Item> PROTOTYPE_SPAWN_EGG = REGISTRY.register("prototype_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.PROTOTYPE, new Color(-5325833).getRGB(), new Color(-9306113).getRGB(), new Item.Properties()));
    public static final RegistryObject<Item> LATEX_SNOW_FOX_MALE_SPAWN_EGG = REGISTRY.register("latex_snow_fox_male_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LATEX_SNOW_FOX_MALE, 0xFFFFFFF, 0xfD6DDF7, new Item.Properties()));
    public static final RegistryObject<Item> LATEX_SNOW_FOX_FEMALE_SPAWN_EGG = REGISTRY.register("latex_snow_fox_female_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LATEX_SNOW_FOX_FEMALE, 0xFFFFFFF, 0xfD6DDF7, new Item.Properties()));
    public static final RegistryObject<Item> FOXYAS_SPAWN_EGG = REGISTRY.register("latex_snow_fox_foxyas_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LATEX_SNOW_FOX_FOXYAS, -1, -26215, new Item.Properties()));
    public static final RegistryObject<Item> DAZED_LATEX_SPAWN_EGG = REGISTRY.register("latex_dazed_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.DAZED_LATEX, 0xFFFFFFF, 0xffCFCFCF, new Item.Properties()));
    public static final RegistryObject<Item> PURO_KIND_MALE_SPAWN_EGG = REGISTRY.register("puro_kind_male_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.PURO_KIND_MALE, Color3.getColor("#393939").toInt(), Color3.getColor("#303030").toInt(), new Item.Properties()));
    public static final RegistryObject<Item> PURO_KIND_FEMALE_SPAWN_EGG = REGISTRY.register("puro_kind_female_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.PURO_KIND_FEMALE, Color3.getColor("#393939").toInt(), Color3.getColor("#303030").toInt(), new Item.Properties()));
    public static final RegistryObject<Item> BUNY_SPAWN_EGG = REGISTRY.register("buny_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.BUNY, Color3.getColor("#fee9c8").toInt(), Color3.getColor("#9c8c73").toInt(), new Item.Properties()));
    public static final RegistryObject<Item> ORGANIC_SNOW_LEOPARD_MALE_SPAWN_EGG = REGISTRY.register("snow_leopard_male_organic_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.SNOW_LEOPARD_MALE_ORGANIC, Color3.getColor("#9C9C9C").toInt(), Color3.getColor("#292929").toInt(), new Item.Properties()));
    public static final RegistryObject<Item> ORGANIC_SNOW_LEOPARD_FEMALE_SPAWN_EGG = REGISTRY.register("snow_leopard_female_organic_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.SNOW_LEOPARD_FEMALE_ORGANIC, Color3.getColor("#9C9C9C").toInt(), Color3.getColor("#292929").toInt(), new Item.Properties()));
    public static final RegistryObject<Item> MIRROR_WHITE_TIGER_SPAWN_EGG = REGISTRY.register("mirror_white_tiger_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.MIRROR_WHITE_TIGER, Color3.getColor("#FFFFFF").toInt(), Color3.getColor("#ACACAC").toInt(), new Item.Properties()));
    public static final RegistryObject<Item> WOLFY_SPAWN_EGG = REGISTRY.register("wolfy_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.WOLFY, Color3.getColor("#393939").toInt(), Color3.getColor("#303030").toInt(), new Item.Properties()));
    public static final RegistryObject<Item> EXP1_MALE_SPAWN_EGG = REGISTRY.register("exp_1_male_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.EXP_1_MALE, 0xFFFFFFF, 0xffb6b9b9, new Item.Properties()));
    public static final RegistryObject<Item> EXP1_FEMALE_SPAWN_EGG = REGISTRY.register("exp_1_female_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.EXP_1_FEMALE, 0xFFFFFFF, 0xffb6b9b9, new Item.Properties()));
    public static final RegistryObject<Item> EXP2_MALE_SPAWN_EGG = REGISTRY.register("exp_2_male_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.EXP_2_MALE, Color3.getColor("#9C9C9C").toInt(), Color3.getColor("#484848").toInt(), new Item.Properties()));
    public static final RegistryObject<Item> EXP2_FEMALE_SPAWN_EGG = REGISTRY.register("exp_2_female_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.EXP_2_FEMALE, Color3.getColor("#9C9C9C").toInt(), Color3.getColor("#484848").toInt(), new Item.Properties()));
    public static final RegistryObject<Item> LATEX_SNEP_SPAWN_EGG = REGISTRY.register("latex_snep_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LATEX_SNEP, Color3.getColor("#9C9C9C").toInt(), Color3.getColor("#484848").toInt(), new Item.Properties()));
    public static final RegistryObject<Item> EXP6_SPAWN_EGG = REGISTRY.register("exp_6_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.EXP_6, Color3.getColor("#B2B1B9").toInt(), Color3.getColor("#CAA2E6").toInt(), new Item.Properties()));
    public static final RegistryObject<Item> EXP10_SPAWN_EGG = REGISTRY.register("experiment_10_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.EXPERIMENT_10, Color3.getColor("#181818").toInt(), Color3.getColor("#ed1c24").toInt(), new Item.Properties()));
    public static final RegistryObject<Item> EXPERIMENT_009_SPAWN_EGG = REGISTRY.register("experiment_009_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.EXPERIMENT_009, Color3.getColor("#E9E9E9").toInt(), Color3.getColor("#66FFFF").toInt(), new Item.Properties()));
    public static final RegistryObject<Item> EXPERIMENT_009_BOSS_SPAWN_EGG = REGISTRY.register("experiment_009_boss_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.EXPERIMENT_009_BOSS, Color3.getColor("#E9E9E9").toInt(), Color3.getColor("#66FFFF").toInt(), new Item.Properties()));
    public static final RegistryObject<Item> EXP10_BOSS_SPAWN_EGG = REGISTRY.register("experiment_10_boss_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.EXPERIMENT_10_BOSS, Color3.getColor("#181818").toInt(), Color3.getColor("#ed1c24").toInt(), new Item.Properties()));
    public static final RegistryObject<Item> PARTIAL_SNOW_LEOPARD_SPAWN_EGG = REGISTRY.register("latex_snow_leopard_partial_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.SNOW_LEOPARD_PARTIAL, Color3.getColor("#9C9C9C").toInt(), Color3.getColor("#484848").toInt(), new Item.Properties()));
    public static final RegistryObject<Item> REYN_SPAWN_EGG = REGISTRY.register("reyn_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.REYN, Color3.getColor("#4C4C4C").toInt(), Color3.getColor("#464646").toInt(), new Item.Properties()));
    public static final RegistryObject<Item> LUMINARCTIC_LEOPARD_MALE_SPAWN_EGG = REGISTRY.register("luminarctic_leopard_male_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LUMINARCTIC_LEOPARD_MALE, Color3.getColor("#414141").toInt(), Color3.getColor("#FFFFFF").toInt(), new Item.Properties()));
    public static final RegistryObject<Item> LUMINARCTIC_FEMALE_LEOPARD_SPAWN_EGG = REGISTRY.register("luminarctic_leopard_female_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LUMINARCTIC_LEOPARD_FEMALE, Color3.getColor("#414141").toInt(), Color3.getColor("#FFFFFF").toInt(), new Item.Properties()));
    public static final RegistryObject<Item> LATEX_SQUID_TIGER_SHARK_SPAWN_EGG = REGISTRY.register("latex_squid_tiger_shark_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LATEX_SQUID_TIGER_SHARK, Color3.getColor("#969696").toInt(), Color3.BLACK.toInt(), new Item.Properties()));
    public static final RegistryObject<Item> LYNX_SPAWN_EGG = REGISTRY.register("lynx_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LYNX, Color3.getColor("#ebd182").toInt(), Color3.getColor("#eace7a").toInt(), new Item.Properties()));
    public static final RegistryObject<Item> FOXTA_FOXY_SPAWN_EGG = REGISTRY.register("foxta_foxy_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.FOXTA_FOXY, Color3.getColor("#FF8F33").toInt(), Color3.getColor("#FFBC85").toInt(), new Item.Properties()));
    public static final RegistryObject<Item> SNEPSI_LEOPARD_SPAWN_EGG = REGISTRY.register("snepsi_leopard_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.SNEPSI_LEOPARD, Color3.getColor("#95D161").toInt(), Color3.getColor("#B5DF90").toInt(), new Item.Properties()));
    public static final RegistryObject<Item> FENGQI_WOLF_SPAWN_EGG = REGISTRY.register("fengqi_wolf_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.FENGQI_WOLF, Color3.getColor("#93c6fd").toInt(), Color3.getColor("#FAC576").toInt(), new Item.Properties()));
    public static final RegistryObject<Item> BAGEL_SPAWN_EGG = REGISTRY.register("bagel_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.BAGEL, 0xFFFFFFF, 0xfD6DDF7, new Item.Properties()));
    public static final RegistryObject<Item> LATEX_SNEP_SHARK_SPAWN_EGG = REGISTRY.register("latex_dragon_snow_leopard_shark_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LATEX_DRAGON_SNOW_LEOPARD_SHARK, 0x969696, 0x292929, new Item.Properties()));
    public static final RegistryObject<Item> CRYSTAL_GAS_CAT_MALE_SPAWN_EGG = REGISTRY.register("crystal_gas_cat_male_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.CRYSTAL_GAS_CAT_MALE, 0x9c9c9c, 0x262626, new Item.Properties()));
    public static final RegistryObject<Item> CRYSTAL_GAS_CAT_FEMALE_SPAWN_EGG = REGISTRY.register("crystal_gas_cat_female_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.CRYSTAL_GAS_CAT_FEMALE, 0x9c9c9c, 0x262626, new Item.Properties()));
    public static final RegistryObject<Item> VOID_FOX_SPAWN_EGG = REGISTRY.register("void_fox_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.VOID_FOX, 0x393939, 0xffffff, new Item.Properties()));
    public static final RegistryObject<Item> HAYDEN_FENNEC_FOX_SPAWN_EGG = REGISTRY.register("hayden_fennec_fox_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.HAYDEN_FENNEC_FOX, 0xF6DC70, 0xF0E4B9, new Item.Properties()));
    public static final RegistryObject<Item> BLUE_LIZARD_SPAWN_EGG = REGISTRY.register("blue_lizard_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.BLUE_LIZARD, 0x00F3FF, 0xffffff, new Item.Properties()));
    public static final RegistryObject<Item> AVALI_SPAWN_EGG = REGISTRY.register("avali_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.AVALI, 0xffffff, 0xffffff, new Item.Properties()));
    public static final RegistryObject<Item> AVALI_ZERGODMASTER_SPAWN_EGG = REGISTRY.register("avali_zergodmaster_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.AVALI_ZERGODMASTER, 0x000000, 0xcfa100, new Item.Properties()));
    public static final RegistryObject<Item> LATEX_KAYLA_SHARK_SPAWN_EGG = REGISTRY.register("latex_kayla_shark_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LATEX_KAYLA_SHARK, 0xce4d62, 0xcb4be9, new Item.Properties()));
    public static final RegistryObject<Item> LATEX_KITSUNE_MALE_SPAWN_EGG = REGISTRY.register("latex_kitsune_male_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LATEX_KITSUNE_MALE, 0xfff6f6, 0xffeeee, new Item.Properties()));
    public static final RegistryObject<Item> LATEX_KITSUNE_FEMALE_SPAWN_EGG = REGISTRY.register("latex_kitsune_female_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LATEX_KITSUNE_FEMALE, 0xfff6f6, 0xffeeee, new Item.Properties()));
    public static final RegistryObject<Item> LATEX_CALICO_CAT_SPAWN_EGG = REGISTRY.register("latex_calico_cat_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LATEX_CALICO_CAT, 0xffece4, 0xd56f53, new Item.Properties()));
    public static final RegistryObject<Item> LATEX_BORDER_COLLIE_SPAWN_EGG = REGISTRY.register("latex_border_collie_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LATEX_BORDER_COLLIE, new Color(24, 24, 30).getRGB(), new Color(255, 255, 255).getRGB(), new Item.Properties()));
    public static final RegistryObject<Item> PROTOGEN_SPAWN_EGG = REGISTRY.register("protogen_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.PROTOGEN, new Color(255, 255, 255).getRGB(), new Color(0, 196, 255).getRGB(), new Item.Properties()));
    public static final RegistryObject<Item> PROTOGEN_0SENIA0_SPAWN_EGG = REGISTRY.register("protogen_0senia0_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.PROTOGEN_0SENIA0, Color3.getColor("#4d0ddb").toInt(), Color3.getColor("#98b440").toInt(), new Item.Properties()));
    public static final RegistryObject<Item> MONGOOSE_SPAWN_EGG = REGISTRY.register("mongoose_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.MONGOOSE, new Color(213, 152, 113).getRGB(), new Color(91, 91, 91).getRGB(), new Item.Properties()));
    public static final RegistryObject<Item> BOREALIS_MALE_SPAWN_EGG = REGISTRY.register("borealis_male_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.BOREALIS_MALE, new Color(102, 130, 193).getRGB(), new Color(28, 42, 78).getRGB(), new Item.Properties()));
    public static final RegistryObject<Item> BOREALIS_FEMALE_SPAWN_EGG = REGISTRY.register("borealis_female_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.BOREALIS_FEMALE, new Color(102, 130, 193).getRGB(), new Color(28, 42, 78).getRGB(), new Item.Properties()));
    public static final RegistryObject<Item> PINK_CYAN_SKUNK_SPAWN_EGG = REGISTRY.register("pink_cyan_skunk_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.PINK_CYAN_SKUNK, new Color(219, 175, 226).getRGB(), new Color(175, 224, 221).getRGB(), new Item.Properties()));
    public static final RegistryObject<Item> LATEX_WIND_CAT_MALE_SPAWN_EGG = REGISTRY.register("latex_wind_cat_male_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LATEX_WIND_CAT_MALE, Color3.getColor("#dfe6ec").toInt(), Color3.getColor("#87a5d4").toInt(), new Item.Properties()));
    public static final RegistryObject<Item> LATEX_WIND_CAT_FEMALE_SPAWN_EGG = REGISTRY.register("latex_wind_cat_female_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LATEX_WIND_CAT_FEMALE, Color3.getColor("#dfe6ec").toInt(), Color3.getColor("#87a5d4").toInt(), new Item.Properties()));
    public static final RegistryObject<Item> LATEX_WHITE_SNOW_LEOPARD_MALE_SPAWN_EGG = REGISTRY.register("latex_white_snow_leopard_male_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LATEX_WHITE_SNOW_LEOPARD_MALE, Color3.getColor("#fbfcff").toInt(), Color3.getColor("#7c7f88").toInt(), new Item.Properties()));
    public static final RegistryObject<Item> LATEX_WHITE_SNOW_LEOPARD_FEMALE_SPAWN_EGG = REGISTRY.register("latex_white_snow_leopard_female_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LATEX_WHITE_SNOW_LEOPARD_FEMALE, Color3.getColor("#fbfcff").toInt(), Color3.getColor("#7c7f88").toInt(), new Item.Properties()));
    public static final RegistryObject<Item> LATEX_CHEETAH_FEMALE_SPAWN_EGG = REGISTRY.register("latex_cheetah_female_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LATEX_CHEETAH_FEMALE, Color3.getColor("#d8b270").toInt(), Color3.getColor("#634927").toInt(), new Item.Properties()));
    public static final RegistryObject<Item> LATEX_CHEETAH_MALE_SPAWN_EGG = REGISTRY.register("latex_cheetah_male_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LATEX_CHEETAH_MALE, Color3.getColor("#d8b270").toInt(), Color3.getColor("#634927").toInt(), new Item.Properties()));
    public static final RegistryObject<Item> LUMINARA_FLOWER_BEAST_SPAWN_EGG = REGISTRY.register("luminara_flower_beast_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LUMINARA_FLOWER_BEAST, Color3.getColor("#f5d4ef").toInt(), Color3.getColor("#241942").toInt(), new Item.Properties()));


    // MISC ITEMS
    public static final RegistryObject<Item> DARK_LATEX_COAT = REGISTRY.register("dark_latex_coat",
            () -> new DarkLatexCoatItem(ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> DARK_LATEX_HEAD_CAP = REGISTRY.register("dark_latex_coat_cap",
            () -> new DarkLatexCoatItem(ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<LaserPointerItem> LASER_POINTER = REGISTRY.register("laser_pointer", LaserPointerItem::new);
    //public static final RegistryObject<Item> DYEABLE_SPORTS_BRA = REGISTRY.register("dyeable_sports_bra", DyeableSportsBra::new);
    public static final RegistryObject<TShirtClothingItem> DYEABLE_TSHIRT = REGISTRY.register("dyeable_tshirt", TShirtClothingItem::new);
    public static final RegistryObject<DyeableShortsItem> DYEABLE_SHORTS = REGISTRY.register("dyeable_shorts", DyeableShortsItem::new);
    public static final RegistryObject<Item> HAZARD_BODY_SUIT = REGISTRY.register("hazard_body_suit", HazardBodySuit::new);


    public static final RegistryObject<KeycardItem> KEYCARD_ITEM = REGISTRY.register("keycard", KeycardItem::new);
    public static final RegistryObject<TimedKeypadItem> TIMED_KEYPAD = REGISTRY.register("timed_keypad", TimedKeypadItem::new);
    public static final RegistryObject<Item> HAND_SCANNER = RegisterBlockItem(REGISTRY, ChangedAddonBlocks.HAND_SCANNER);
    public static final RegistryObject<Item> PAWS_SCANNER = RegisterBlockItem(REGISTRY, ChangedAddonBlocks.PAWS_SCANNER);

    @SubscribeEvent
    public static void clientLoad(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(LAETHIN.get(), ChangedAddonMod.resourceLoc("laethin_type"), (itemStackToRender, clientWorld, entity, itemEntityId) -> LaethinPropertyValueProviderProcedure.execute(itemStackToRender));
            ItemProperties.register(LAETHIN_SYRINGE.get(), ChangedAddonMod.resourceLoc("laethin_syringe_type"),
                    (itemStackToRender, clientWorld, entity, itemEntityId) -> LaethinPropertyValueProviderProcedure.execute(itemStackToRender));
            ItemProperties.register(TRANSFUR_TOTEM.get(), ChangedAddonMod.resourceLoc("transfur_totem_glowtick"), (itemStackToRender, clientWorld, entity, itemEntityId) -> TransfurTotemItem.itemPropertyFunc(entity));
            ItemProperties.register(SIGNAL_CATCHER.get(), ChangedAddonMod.resourceLoc("signal_catcher_dot_value"), (itemStackToRender, clientWorld, entity, itemEntityId) -> (float) DotValueOfViewProcedure.execute(entity, itemStackToRender));
            ItemProperties.register(SIGNAL_CATCHER.get(), ChangedAddonMod.resourceLoc("signal_catcher_cord_set"), (stack, level, entity, itemEntityId) -> {
                CompoundTag tag = stack.getTag();
                return tag != null && tag.contains("x") && tag.contains("y") && tag.contains("z") ? 1 : 0;
            });
            ItemProperties.register(HAND_SCANNER.get(), ChangedAddonMod.resourceLoc("transfur_lock"), (itemStackToRender, clientWorld, entity, itemEntityId) -> {
                if ((entity instanceof Player player && ProcessTransfur.isPlayerTransfurred(player))
                        || entity instanceof ChangedEntity) {
                    return 1f;
                }
                return 0f;
            });
            ItemProperties.register(LUMINAR_CRYSTAL_SPEAR.get(), ResourceLocation.parse("throwing"),
                    (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
        });
    }

    @SubscribeEvent
    public static void onItemColorsInit(RegisterColorHandlersEvent.Item event) {
        for (RegistryObject<Item> itemRegistryObject : REGISTRY.getEntries()) {
            if (itemRegistryObject.isPresent() && itemRegistryObject.get() instanceof ColorHolder colorHolder) {
                colorHolder.registerCustomColors(event, itemRegistryObject);
            }
        }
    }

    private static RegistryObject<BlockItem> block(RegistryObject<? extends Block> block) {
        return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static RegistryObject<Item> blockNoTab(RegistryObject<? extends Block> block) {
        return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static RegistryObject<Item> block(RegistryObject<Block> block, Item.Properties properties) {
        return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), properties));
    }

    private static RegistryObject<Item> RegisterBlockItem(DeferredRegister<Item> registry, RegistryObject<Block> block) {
        return registry.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static RegistryObject<Item> RegisterBlockItem(DeferredRegister<Item> registry, String id, RegistryObject<Block> block) {
        return registry.register(id, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static RegistryObject<Item> RegisterBlockItem(RegistryObject<Block> block) {
        return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static List<RegistryObject<Item>> getNoTabItems() {
        return List.of(GENERATOR, SNEP_ICON, PAT_ICON, FRIENDLY_GOEY_ICON);
    }
}
