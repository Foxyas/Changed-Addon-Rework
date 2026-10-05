package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.item.*;
import net.foxyas.changedaddon.item.api.ColorHolder;
import net.foxyas.changedaddon.item.armor.DarkLatexCoatItem;
import net.foxyas.changedaddon.item.armor.HazardBodySuit;
import net.foxyas.changedaddon.item.clothes.DyeableShortsItem;
import net.foxyas.changedaddon.item.clothes.TShirtClothingItem;
import net.foxyas.changedaddon.procedure.DotValueOfViewProcedure;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedLatexTypes;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
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
    public static final RegistryObject<BlockItem> LUMINARA_BLOOM = block(ChangedAddonBlocks.LUMINARA_BLOOM, new Item.Properties().rarity(Rarity.RARE));
    public static final RegistryObject<Item> LUMINARA_BLOOM_PETALS = REGISTRY.register("luminara_bloom_petals", LuminaraBloomPetalsItem::new);
    public static final RegistryObject<BlockItem> LUMINARA_LOG = block(ChangedAddonBlocks.LUMINARA_LOG);
    public static final RegistryObject<BlockItem> STRIPPED_LUMINARA_LOG = block(ChangedAddonBlocks.STRIPPED_LUMINARA_LOG);
    public static final RegistryObject<BlockItem> LUMINARA_WOOD = block(ChangedAddonBlocks.LUMINARA_WOOD);
    public static final RegistryObject<BlockItem> STRIPPED_LUMINARA_WOOD = block(ChangedAddonBlocks.STRIPPED_LUMINARA_WOOD);
    public static final RegistryObject<BlockItem> LUMINARA_PLANKS = block(ChangedAddonBlocks.LUMINARA_PLANKS);
    public static final RegistryObject<BlockItem> LUMINARA_STAIRS = block(ChangedAddonBlocks.LUMINARA_STAIRS);
    public static final RegistryObject<BlockItem> LUMINARA_SLAB = block(ChangedAddonBlocks.LUMINARA_SLAB);
    public static final RegistryObject<BlockItem> LUMINARA_DOOR = block(ChangedAddonBlocks.LUMINARA_DOOR);
    public static final RegistryObject<BlockItem> LUMINARA_TRAPDOOR = block(ChangedAddonBlocks.LUMINARA_TRAPDOOR);
    public static final RegistryObject<BlockItem> LUMINARA_FENCE = block(ChangedAddonBlocks.LUMINARA_FENCE);
    public static final RegistryObject<BlockItem> LUMINARA_FENCE_GATE = block(ChangedAddonBlocks.LUMINARA_FENCE_GATE);
    public static final RegistryObject<SignItem> LUMINARA_SIGN = REGISTRY.register("luminara_sign", () -> new SignItem(new Item.Properties(), ChangedAddonBlocks.LUMINARA_SIGN.get(), ChangedAddonBlocks.LUMINARA_WALL_SIGN.get()));
    public static final RegistryObject<HangingSignItem> LUMINARA_HANGING_SIGN = REGISTRY.register("luminara_hanging_sign", () -> new HangingSignItem(ChangedAddonBlocks.LUMINARA_HANGING_SIGN.get(), ChangedAddonBlocks.LUMINARA_WALL_HANGING_SIGN.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> LUMINARA_BUTTON = block(ChangedAddonBlocks.LUMINARA_BUTTON);
    public static final RegistryObject<BlockItem> LUMINARA_PRESSURE_PLATE = block(ChangedAddonBlocks.LUMINARA_PRESSURE_PLATE);
    public static final RegistryObject<BlockItem> LUMINARA_LEAVES = block(ChangedAddonBlocks.LUMINARA_LEAVES);
    public static final RegistryObject<BlockItem> LUMINARA_PETALS = block(ChangedAddonBlocks.LUMINARA_PETALS);
    public static final RegistryObject<BlockItem> LUMINARA_LICHEN = block(ChangedAddonBlocks.LUMINARA_LICHEN);
    public static final RegistryObject<BlockItem> LUMINARA_SAPLING = block(ChangedAddonBlocks.LUMINARA_SAPLING);
    public static final RegistryObject<Item> BIOMASS = REGISTRY.register("biomass", BiomassItem::new);
    public static final RegistryObject<Item> ANTI_LATEX_BASE = registerWithDesc("anti_latex_base");
    public static final RegistryObject<Item> LUMINARA_BASE = registerWithDesc("luminara_base", new Item.Properties().rarity(Rarity.RARE));
    public static final RegistryObject<Item> STRANGE_COMPOUND_BASE = registerWithDesc("strange_compound_base", new Item.Properties().rarity(Rarity.RARE));
    public static final RegistryObject<Item> IMPURE_AMMONIA = registerSimple("impure_ammonia");
    public static final RegistryObject<Item> AMMONIA_PARTICLE = registerSimple("ammonia_particle");
    public static final RegistryObject<Item> AMMONIA_COMPRESSED = registerSimple("ammonia_compressed");
    public static final RegistryObject<Item> AMMONIA = registerSimple("ammonia");
    public static final RegistryObject<Item> LITIX_CAMMONIA = REGISTRY.register("litix_cammonia", LitixCammoniaItem::new);
    public static final RegistryObject<LaethinItem> LAETHIN = REGISTRY.register("laethin", LaethinItem::new);
    public static final RegistryObject<Item> CATALYZED_DNA = registerSimple("catalyzed_dna", new Item.Properties().stacksTo(1).rarity(Rarity.RARE));

    public static final RegistryObject<Item> SYRINGE = registerSimple("syringe");
    public static final RegistryObject<Item> DIFFUSION_SYRINGE = REGISTRY.register("diffusion_syringe", DiffusionSyringeItem::new);
    public static final RegistryObject<Item> SYRINGE_WITH_LITIX_CAMMONIA = REGISTRY.register("syringe_with_litix_cammonia", SyringeWithLitixCammoniaItem::new);
    public static final RegistryObject<LaethinSyringeItem> LAETHIN_SYRINGE = REGISTRY.register("laethin_syringe", LaethinSyringeItem::new);
    public static final RegistryObject<Item> POT_WITH_CAMMONIA = REGISTRY.register("pot_with_cammonia", PotWithCamnoniaItem::new);
    public static final RegistryObject<AlphaSerumSyringeItem> ALPHA_SERUM_SYRINGE = REGISTRY.register("alpha_serum_syringe", AlphaSerumSyringeItem::new);

    public static final RegistryObject<Item> RAW_IRIDIUM = registerSimple("raw_iridium", new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON));
    public static final RegistryObject<Item> IRIDIUM = REGISTRY.register("iridium", IridiumItem::new);
    public static final RegistryObject<BlockItem> DEEPSLATE_IRIDIUM_ORE = block(ChangedAddonBlocks.DEEPSLATE_IRIDIUM_ORE);
    public static final RegistryObject<BlockItem> IRIDIUM_BLOCK = block(ChangedAddonBlocks.IRIDIUM_BLOCK);

    public static final RegistryObject<Item> PAINITE = registerSimple("painite", new Item.Properties().fireResistant().rarity(Rarity.RARE));
    public static final RegistryObject<Item> ACCESSORIES_CHESTPLATE = REGISTRY.register("accessories_chestplate", AccessoriesItem.Chestplate::new);
    public static final RegistryObject<BlockItem> DEEPSLATE_PAINITE_ORE = block(ChangedAddonBlocks.DEEPSLATE_PAINITE_ORE);
    public static final RegistryObject<BlockItem> PAINITE_BLOCK = block(ChangedAddonBlocks.PAINITE_BLOCK);

    public static final RegistryObject<Item> LITIX_CAMMONIA_FLUID_BUCKET = REGISTRY.register("litix_cammonia_fluid_bucket", LitixCammoniaFluidItem::new);

    public static final RegistryObject<Item> EXPERIMENT_009_DNA = REGISTRY.register("experiment_009_dna", Experiment009DNAItem::new);
    public static final RegistryObject<Item> EXP_9_LATEX_BASE = registerSimple("exp_9_latex_base", new Item.Properties().rarity(Rarity.RARE));
    public static final RegistryObject<Experiment009SpawnerItem> EXP_9_CONTAINMENT_VIAL = REGISTRY.register("exp_9_containment_vial", Experiment009SpawnerItem::new);
    public static final RegistryObject<Item> BLUE_LATEX_GOO = registerSimple("blue_latex_goo", new Item.Properties().rarity(Rarity.RARE));
    public static final RegistryObject<TransfurTotemItem> TRANSFUR_TOTEM = REGISTRY.register("transfur_totem", TransfurTotemItem::new);

    public static final RegistryObject<Item> EXPERIMENT_10_DNA = REGISTRY.register("experiment_10_dna", Experiment10DNAItem::new);
    public static final RegistryObject<Item> EXP_10_LATEX_BASE = registerSimple("exp_10_latex_base", new Item.Properties().rarity(Rarity.RARE));
    public static final RegistryObject<Experiment10SpawnerItem> EXP_10_CONTAINMENT_VIAL = REGISTRY.register("exp_10_containment_vial", Experiment10SpawnerItem::new);
    public static final RegistryObject<Item> RED_LATEX_GOO = registerWithDesc("red_latex_goo", new Item.Properties().fireResistant().rarity(Rarity.RARE));

    // Foods and Drinks
    public static final RegistryObject<Item> ORANGE_JUICE = REGISTRY.register("orange_juice", OrangeJuiceItem::new);
    public static final RegistryObject<Item> SNEPSI = REGISTRY.register("snepsi", SnepsiItem::new);
    public static final RegistryObject<Item> FOXTA = REGISTRY.register("foxta", FoxtaItem::new);
    public static final RegistryObject<Item> GOLDEN_ORANGE = REGISTRY.register("golden_orange", GoldenOrange::new);
    public static final RegistryObject<Item> OPENED_CANNED_SOUP = REGISTRY.register("opened_canned_soup", OpenedCannedSoupItem::new);

    // Remain Items
    public static final RegistryObject<Item> EMPTY_CAN = registerSimple("empty_can");


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

    public static final RegistryObject<Item> LUMINAR_CRYSTAL_SHARD = registerSimple("luminar_crystal_shard", new Item.Properties().fireResistant().rarity(Rarity.RARE));
    public static final RegistryObject<Item> LUMINAR_CRYSTAL_SHARD_HEARTED = registerSimple("luminar_crystal_shard_hearted", new Item.Properties().fireResistant().rarity(Rarity.RARE));
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
    public static final RegistryObject<Item> GOO_CORE_FRAGMENT = registerSimple("goo_core_fragment", new Item.Properties().fireResistant());
    public static final RegistryObject<BlockItem> GOO_CORE = block(ChangedAddonBlocks.GOO_CORE);
    public static final RegistryObject<Item> MEANINGLESS_STRAFE_MUSIC_DISC = REGISTRY.register("meaningless_strafe_music_disc", MeaninglessStrafeMusicDiscItem::new);
    public static final RegistryObject<Item> ELECTRIC_KATANA = REGISTRY.register("electric_katana", ElectricKatanaItem::new);
    public static final RegistryObject<Item> LUMINAR_CRYSTAL_SPEAR = REGISTRY.register("luminar_crystal_spear", LuminarCrystalSpearItem::new);
    public static final RegistryObject<Item> ELECTRIC_KATANA_RED = REGISTRY.register("electric_katana_red", ElectricKatanaRedItem::new);
    public static final RegistryObject<Item> THE_DECIMATOR = REGISTRY.register("the_decimator", TheDecimatorItem::new);
    public static final RegistryObject<Item> CROWBAR = REGISTRY.register("crow_bar", CrowbarItem::new);
    public static final RegistryObject<Item> LAETHINMINATOR = REGISTRY.register("laethinminator", LaethinminatorItem::new);
    public static final RegistryObject<FlamethrowerItem> FLAMETHROWER = REGISTRY.register("flamethrower", FlamethrowerItem::new);
    public static final RegistryObject<Item> CRYSTAL_DAGGER_RED = REGISTRY.register("crystal_dagger_red", CrystalDaggerRedItem::new);
    public static final RegistryObject<Item> CRYSTAL_DAGGER_GREEN = REGISTRY.register("crystal_dagger_green", CrystalDaggerGreenItem::new);
    public static final RegistryObject<Item> CRYSTAL_DAGGER_BLACK = REGISTRY.register("crystal_dagger_black", CrystalDaggerBlackItem::new);
    public static final RegistryObject<Item> EMPTY_SPRAY = REGISTRY.register("empty_spray", EmptySprayItem::new);
    public static final RegistryObject<Item> LITIX_CAMMONIA_SPRAY = REGISTRY.register("litix_cammonia_spray", () -> new SprayItem(ChangedLatexTypes.NONE::get));
    public static final RegistryObject<Item> DARK_LATEX_SPRAY = REGISTRY.register("dark_latex_spray", () -> new SprayItem(ChangedLatexTypes.DARK_LATEX::get));
    public static final RegistryObject<Item> WHITE_LATEX_SPRAY = REGISTRY.register("white_latex_spray", () -> new SprayItem(ChangedLatexTypes.WHITE_LATEX::get));
    public static final RegistryObject<Item> LUNAR_ROSE = REGISTRY.register("lunar_rose", LunarRoseItem::new);
    public static final RegistryObject<Item> CATALYZER_BLOCK_ILLUSTRATIVE_ITEM = registerSimple("catalyzer_block_illustrative_item", new Item.Properties().rarity(Rarity.RARE));
    public static final RegistryObject<Item> UNIFUSER_BLOCK_ILLUSTRATIVE_ITEM = registerSimple("unifuser_block_illustrative_item", new Item.Properties().rarity(Rarity.RARE));
    public static final RegistryObject<Item> SNEP_ICON = REGISTRY.register("snep_icon", SnepIconItem::new);
    public static final RegistryObject<Item> FRIENDLY_GOEY_ICON = REGISTRY.register("friendly_goey_icon", FriendlyGoeyIconItem::new);
    public static final RegistryObject<Item> PAT_ICON = REGISTRY.register("pat_icon", PatIconItem::new);
    public static final RegistryObject<BlockItem> COVER_ITEM = REGISTRY.register("cover", () -> new BlockItem(ChangedAddonBlocks.COVER_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> DARK_LATEX_COVER_ITEM = REGISTRY.register("dark_latex_cover", () -> new BlockItem(ChangedAddonBlocks.DARK_LATEX_COVER_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> WHITE_LATEX_COVER_ITEM = REGISTRY.register("white_latex_cover", () -> new BlockItem(ChangedAddonBlocks.WHITE_LATEX_COVER_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> WOLF_CRYSTAL_PILLAR = block(ChangedAddonBlocks.WOLF_CRYSTAL_PILLAR);
    // --- MOBS SPAWN EGGS ---
    public static final RegistryObject<SpecialSpawnEggItem> SPAWNEGGOFFOXYAS = REGISTRY.register("spawneggoffoxyas", () -> new SpecialSpawnEggItem(ChangedAddonEntities.LATEX_SNOW_FOX_FOXYAS, new Item.Properties()));
    // --- CHANGED ENTITIES SPAWN EGGS ---
    public static final RegistryObject<SpawnEggItem> PROTOTYPE_SPAWN_EGG = registerSpawnEgg("prototype_spawn_egg", ChangedAddonEntities.PROTOTYPE, -5325833, -9306113);
    public static final RegistryObject<SpecialSpawnEggItem> CRAFTABLE_PROTOTYPE_SPAWN_EGG = REGISTRY.register("prototype_spawn_egg_c", () -> new SpecialSpawnEggItem(ChangedAddonEntities.PROTOTYPE, new Item.Properties()));
    public static final RegistryObject<SpawnEggItem> WHITE_FOX_SPAWN_EGG = registerSpawnEgg("white_fox_spawn_egg", ChangedAddonEntities.WHITE_FOX, -1, 0xfD6DDF7);
    public static final RegistryObject<SpawnEggItem> LATEX_SNOW_FOX_MALE_SPAWN_EGG = registerSpawnEgg("latex_snow_fox_male_spawn_egg", ChangedAddonEntities.LATEX_SNOW_FOX_MALE, -1, 0xfD6DDF7);
    public static final RegistryObject<SpawnEggItem> LATEX_SNOW_FOX_FEMALE_SPAWN_EGG = registerSpawnEgg("latex_snow_fox_female_spawn_egg", ChangedAddonEntities.LATEX_SNOW_FOX_FEMALE, -1, 0xfD6DDF7);
    public static final RegistryObject<SpawnEggItem> FOXYAS_SPAWN_EGG = registerSpawnEgg("latex_snow_fox_foxyas_spawn_egg", ChangedAddonEntities.LATEX_SNOW_FOX_FOXYAS, -1, -26215);
    public static final RegistryObject<SpawnEggItem> DAZED_LATEX_SPAWN_EGG = registerSpawnEgg("latex_dazed_spawn_egg", ChangedAddonEntities.DAZED_LATEX, -1, 0xffCFCFCF);
    public static final RegistryObject<SpawnEggItem> BUFF_DAZED_LATEX_SPAWN_EGG = registerSpawnEgg("buff_latex_dazed_spawn_egg", ChangedAddonEntities.BUFF_DAZED_LATEX, -1, 0xffCFCFCF);
    public static final RegistryObject<SpawnEggItem> PURO_KIND_MALE_SPAWN_EGG = registerSpawnEgg("puro_kind_male_spawn_egg", ChangedAddonEntities.PURO_KIND_MALE, 0x393939, 0x303030);
    public static final RegistryObject<SpawnEggItem> PURO_KIND_FEMALE_SPAWN_EGG = registerSpawnEgg("puro_kind_female_spawn_egg", ChangedAddonEntities.PURO_KIND_FEMALE, 0x393939, 0x303030);
    public static final RegistryObject<SpawnEggItem> BUNY_SPAWN_EGG = registerSpawnEgg("buny_spawn_egg", ChangedAddonEntities.BUNY, 0xfee9c8, 0x9c8c73);
    public static final RegistryObject<SpawnEggItem> BIOSYNTH_SNOW_LEOPARD_MALE_SPAWN_EGG = registerSpawnEgg("biosynth_snow_leopard_male_spawn_egg", ChangedAddonEntities.BIOSYNTH_SNOW_LEOPARD_MALE, 0x9C9C9C, 0x292929);
    public static final RegistryObject<SpawnEggItem> BIOSYNTH_SNOW_LEOPARD_FEMALE_SPAWN_EGG = registerSpawnEgg("biosynth_snow_leopard_female_spawn_egg", ChangedAddonEntities.BIOSYNTH_SNOW_LEOPARD_FEMALE, 0x9C9C9C, 0x292929);
    public static final RegistryObject<SpawnEggItem> MIRROR_WHITE_TIGER_SPAWN_EGG = registerSpawnEgg("mirror_white_tiger_spawn_egg", ChangedAddonEntities.MIRROR_WHITE_TIGER, -1, 0xACACAC);
    public static final RegistryObject<SpawnEggItem> WOLFY_SPAWN_EGG = registerSpawnEgg("wolfy_spawn_egg", ChangedAddonEntities.WOLFY, 0x393939, 0x303030);
    public static final RegistryObject<SpawnEggItem> EXP1_MALE_SPAWN_EGG = registerSpawnEgg("exp_1_male_spawn_egg", ChangedAddonEntities.EXP_1_MALE, -1, 0xffb6b9b9);
    public static final RegistryObject<SpawnEggItem> EXP1_FEMALE_SPAWN_EGG = registerSpawnEgg("exp_1_female_spawn_egg", ChangedAddonEntities.EXP_1_FEMALE, -1, 0xffb6b9b9);
    public static final RegistryObject<SpawnEggItem> EXP2_MALE_SPAWN_EGG = registerSpawnEgg("exp_2_male_spawn_egg", ChangedAddonEntities.EXP_2_MALE, 0x9C9C9C, 0x484848);
    public static final RegistryObject<SpawnEggItem> EXP2_FEMALE_SPAWN_EGG = registerSpawnEgg("exp_2_female_spawn_egg", ChangedAddonEntities.EXP_2_FEMALE, 0x9C9C9C, 0x484848);
    public static final RegistryObject<SpawnEggItem> LATEX_FERAL_SNEP_SPAWN_EGG = registerSpawnEgg("latex_ferar_snep_spawn_egg", ChangedAddonEntities.LATEX_FERAL_SNEP, 0x9C9C9C, 0x484848);
    public static final RegistryObject<SpawnEggItem> EXP6_SPAWN_EGG = registerSpawnEgg("exp_6_spawn_egg", ChangedAddonEntities.EXP_6, 0xB2B1B9, 0xCAA2E6);
    public static final RegistryObject<SpawnEggItem> EXP10_SPAWN_EGG = registerSpawnEgg("experiment_10_spawn_egg", ChangedAddonEntities.EXPERIMENT_10, 0x181818, 0xed1c24);
    public static final RegistryObject<SpawnEggItem> EXPERIMENT_009_SPAWN_EGG = registerSpawnEgg("experiment_009_spawn_egg", ChangedAddonEntities.EXPERIMENT_009, 0xE9E9E9, 0x66FFFF);
    public static final RegistryObject<SpawnEggItem> EXPERIMENT_009_BOSS_SPAWN_EGG = registerSpawnEgg("experiment_009_boss_spawn_egg", ChangedAddonEntities.EXPERIMENT_009_BOSS, 0xE9E9E9, 0x66FFFF);
    public static final RegistryObject<SpawnEggItem> EXP10_BOSS_SPAWN_EGG = registerSpawnEgg("experiment_10_boss_spawn_egg", ChangedAddonEntities.EXPERIMENT_10_BOSS, 0x181818, 0xed1c24);
    public static final RegistryObject<SpawnEggItem> PARTIAL_SNOW_LEOPARD_SPAWN_EGG = registerSpawnEgg("latex_snow_leopard_partial_spawn_egg", ChangedAddonEntities.SNOW_LEOPARD_PARTIAL, 0x9C9C9C, 0x484848);
    public static final RegistryObject<SpawnEggItem> REYN_SPAWN_EGG = registerSpawnEgg("reyn_spawn_egg", ChangedAddonEntities.REYN, 0x4C4C4C, 0x464646);
    public static final RegistryObject<SpawnEggItem> LUMINARCTIC_LEOPARD_MALE_SPAWN_EGG = registerSpawnEgg("luminarctic_leopard_male_spawn_egg", ChangedAddonEntities.LUMINARCTIC_LEOPARD_MALE, 0x414141, -1);
    public static final RegistryObject<SpawnEggItem> LUMINARCTIC_FEMALE_LEOPARD_SPAWN_EGG = registerSpawnEgg("luminarctic_leopard_female_spawn_egg", ChangedAddonEntities.LUMINARCTIC_LEOPARD_FEMALE, 0x414141, -1);
    public static final RegistryObject<SpawnEggItem> LATEX_SQUID_TIGER_SHARK_SPAWN_EGG = registerSpawnEgg("latex_squid_tiger_shark_spawn_egg", ChangedAddonEntities.LATEX_SQUID_TIGER_SHARK, 0x969696, Color3.BLACK.toInt());
    public static final RegistryObject<SpawnEggItem> LYNX_SPAWN_EGG = registerSpawnEgg("lynx_spawn_egg", ChangedAddonEntities.LYNX, 0xebd182, 0xeace7a);
    public static final RegistryObject<SpawnEggItem> FOXTA_FOXY_SPAWN_EGG = registerSpawnEgg("foxta_foxy_spawn_egg", ChangedAddonEntities.FOXTA_FOXY, 0xFF8F33, 0xFFBC85);
    public static final RegistryObject<SpawnEggItem> SNEPSI_LEOPARD_SPAWN_EGG = registerSpawnEgg("snepsi_leopard_spawn_egg", ChangedAddonEntities.SNEPSI_LEOPARD, 0x95D161, 0xB5DF90);
    public static final RegistryObject<SpawnEggItem> FENGQI_WOLF_SPAWN_EGG = registerSpawnEgg("fengqi_wolf_spawn_egg", ChangedAddonEntities.FENGQI_WOLF, 0x93c6fd, 0xFAC576);
    public static final RegistryObject<SpawnEggItem> BAGEL_SPAWN_EGG = registerSpawnEgg("bagel_spawn_egg", ChangedAddonEntities.BAGEL, -1, 0xfD6DDF7);
    public static final RegistryObject<SpawnEggItem> LATEX_SNEP_SHARK_SPAWN_EGG = registerSpawnEgg("latex_dragon_snow_leopard_shark_spawn_egg", ChangedAddonEntities.LATEX_DRAGON_SNOW_LEOPARD_SHARK, 0x969696, 0x292929);
    public static final RegistryObject<SpawnEggItem> CRYSTAL_GAS_CAT_MALE_SPAWN_EGG = registerSpawnEgg("crystal_gas_cat_male_spawn_egg", ChangedAddonEntities.CRYSTAL_GAS_CAT_MALE, 0x9c9c9c, 0x262626);
    public static final RegistryObject<SpawnEggItem> CRYSTAL_GAS_CAT_FEMALE_SPAWN_EGG = registerSpawnEgg("crystal_gas_cat_female_spawn_egg", ChangedAddonEntities.CRYSTAL_GAS_CAT_FEMALE, 0x9c9c9c, 0x262626);
    public static final RegistryObject<SpawnEggItem> VOID_FOX_SPAWN_EGG = registerSpawnEgg("void_fox_spawn_egg", ChangedAddonEntities.VOID_FOX, 0x393939, -1);
    public static final RegistryObject<SpawnEggItem> HAYDEN_FENNEC_FOX_SPAWN_EGG = registerSpawnEgg("hayden_fennec_fox_spawn_egg", ChangedAddonEntities.HAYDEN_FENNEC_FOX, 0xF6DC70, 0xF0E4B9);
    public static final RegistryObject<SpawnEggItem> BLUE_LIZARD_SPAWN_EGG = registerSpawnEgg("blue_lizard_spawn_egg", ChangedAddonEntities.BLUE_LIZARD, 0x00F3FF, -1);
    public static final RegistryObject<SpawnEggItem> AVALI_SPAWN_EGG = registerSpawnEgg("avali_spawn_egg", ChangedAddonEntities.AVALI, -1, -1);
    public static final RegistryObject<SpawnEggItem> AVALI_ZERGODMASTER_SPAWN_EGG = registerSpawnEgg("avali_zergodmaster_spawn_egg", ChangedAddonEntities.AVALI_ZERGODMASTER, 0x000000, 0xcfa100);
    public static final RegistryObject<SpawnEggItem> LATEX_KAYLA_SHARK_SPAWN_EGG = registerSpawnEgg("latex_kayla_shark_spawn_egg", ChangedAddonEntities.LATEX_KAYLA_SHARK, 0xce4d62, 0xcb4be9);
    public static final RegistryObject<SpawnEggItem> LATEX_KITSUNE_MALE_SPAWN_EGG = registerSpawnEgg("latex_kitsune_male_spawn_egg", ChangedAddonEntities.LATEX_KITSUNE_MALE, 0xfff6f6, 0xffeeee);
    public static final RegistryObject<SpawnEggItem> LATEX_KITSUNE_FEMALE_SPAWN_EGG = registerSpawnEgg("latex_kitsune_female_spawn_egg", ChangedAddonEntities.LATEX_KITSUNE_FEMALE, 0xfff6f6, 0xffeeee);
    public static final RegistryObject<SpawnEggItem> LATEX_CALICO_CAT_SPAWN_EGG = registerSpawnEgg("latex_calico_cat_spawn_egg", ChangedAddonEntities.LATEX_CALICO_CAT, 0xffece4, 0xd56f53);
    public static final RegistryObject<SpawnEggItem> LATEX_BORDER_COLLIE_SPAWN_EGG = registerSpawnEgg("latex_border_collie_spawn_egg", ChangedAddonEntities.LATEX_BORDER_COLLIE, new Color(24, 24, 30).getRGB(), new Color(255, 255, 255).getRGB());
    public static final RegistryObject<SpawnEggItem> PROTOGEN_SPAWN_EGG = registerSpawnEgg("protogen_spawn_egg", ChangedAddonEntities.PROTOGEN, new Color(255, 255, 255).getRGB(), new Color(0, 196, 255).getRGB());
    public static final RegistryObject<SpawnEggItem> PROTOGEN_0SENIA0_SPAWN_EGG = registerSpawnEgg("protogen_0senia0_spawn_egg", ChangedAddonEntities.PROTOGEN_0SENIA0, 0x4d0ddb, 0x98b440);
    public static final RegistryObject<SpawnEggItem> MONGOOSE_SPAWN_EGG = registerSpawnEgg("mongoose_spawn_egg", ChangedAddonEntities.MONGOOSE, new Color(213, 152, 113).getRGB(), new Color(91, 91, 91).getRGB());
    public static final RegistryObject<SpawnEggItem> BOREALIS_MALE_SPAWN_EGG = registerSpawnEgg("borealis_male_spawn_egg", ChangedAddonEntities.BOREALIS_MALE, new Color(102, 130, 193).getRGB(), new Color(28, 42, 78).getRGB());
    public static final RegistryObject<SpawnEggItem> BOREALIS_FEMALE_SPAWN_EGG = registerSpawnEgg("borealis_female_spawn_egg", ChangedAddonEntities.BOREALIS_FEMALE, new Color(102, 130, 193).getRGB(), new Color(28, 42, 78).getRGB());
    public static final RegistryObject<SpawnEggItem> PINK_CYAN_SKUNK_SPAWN_EGG = registerSpawnEgg("pink_cyan_skunk_spawn_egg", ChangedAddonEntities.PINK_CYAN_SKUNK, new Color(219, 175, 226).getRGB(), new Color(175, 224, 221).getRGB());
    public static final RegistryObject<SpawnEggItem> LATEX_WIND_CAT_MALE_SPAWN_EGG = registerSpawnEgg("latex_wind_cat_male_spawn_egg", ChangedAddonEntities.LATEX_WIND_CAT_MALE, 0xdfe6ec, 0x87a5d4);
    public static final RegistryObject<SpawnEggItem> LATEX_WIND_CAT_FEMALE_SPAWN_EGG = registerSpawnEgg("latex_wind_cat_female_spawn_egg", ChangedAddonEntities.LATEX_WIND_CAT_FEMALE, 0xdfe6ec, 0x87a5d4);
    public static final RegistryObject<SpawnEggItem> LATEX_WHITE_SNOW_LEOPARD_MALE_SPAWN_EGG = registerSpawnEgg("latex_white_snow_leopard_male_spawn_egg", ChangedAddonEntities.LATEX_WHITE_SNOW_LEOPARD_MALE, 0xfbfcff, 0x7c7f88);
    public static final RegistryObject<SpawnEggItem> LATEX_WHITE_SNOW_LEOPARD_FEMALE_SPAWN_EGG = registerSpawnEgg("latex_white_snow_leopard_female_spawn_egg", ChangedAddonEntities.LATEX_WHITE_SNOW_LEOPARD_FEMALE, 0xfbfcff, 0x7c7f88);
    public static final RegistryObject<SpawnEggItem> LATEX_CHEETAH_FEMALE_SPAWN_EGG = registerSpawnEgg("latex_cheetah_female_spawn_egg", ChangedAddonEntities.LATEX_CHEETAH_FEMALE, 0xd8b270, 0x634927);
    public static final RegistryObject<SpawnEggItem> LATEX_CHEETAH_MALE_SPAWN_EGG = registerSpawnEgg("latex_cheetah_male_spawn_egg", ChangedAddonEntities.LATEX_CHEETAH_MALE, 0xd8b270, 0x634927);
    public static final RegistryObject<SpawnEggItem> LUMINARA_FLOWER_BEAST_SPAWN_EGG = registerSpawnEgg("luminara_flower_beast_spawn_egg", ChangedAddonEntities.LUMINARA_FLOWER_BEAST, 0xf5d4ef, 0x241942);
    public static final RegistryObject<SpawnEggItem> DARK_LATEX_YUFENG_QUEEN_SPAWN_EGG = registerSpawnEgg("dark_latex_yufeng_queen_spawn_egg", ChangedAddonEntities.DARK_LATEX_YUFENG_QUEEN, 0x393939, 0xFAFAFA);
    public static final RegistryObject<SpawnEggItem> LUMINARA_CRYSTAL_BEING_FEMALE_SPAWN_EGG = registerSpawnEgg("luminara_crystal_being_female_spawn_egg", ChangedAddonEntities.LUMINARA_CRYSTAL_BEING_FEMALE, 0x06040a, 0xf7d6f1);
    public static final RegistryObject<SpawnEggItem> LUMINARA_CRYSTAL_BEING_MALE_SPAWN_EGG = registerSpawnEgg("luminara_crystal_being_male_spawn_egg", ChangedAddonEntities.LUMINARA_CRYSTAL_BEING_MALE, 0x06040a, 0xf7d6f1);
    // MISC ITEMS
    public static final RegistryObject<Item> DARK_LATEX_COAT = REGISTRY.register("dark_latex_coat",
            () -> new DarkLatexCoatItem(ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> DARK_LATEX_HEAD_CAP = REGISTRY.register("dark_latex_coat_cap",
            () -> new DarkLatexCoatItem(ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<LaserPointerItem> LASER_POINTER = REGISTRY.register("laser_pointer", LaserPointerItem::new);
    //public static final RegistryObject<Item> DYEABLE_SPORTS_BRA = REGISTRY.register("dyeable_sports_bra", DyeableSportsBra::new);
    public static final RegistryObject<TShirtClothingItem> DYEABLE_TSHIRT = REGISTRY.register("dyeable_tshirt", TShirtClothingItem::new);
    public static final RegistryObject<DyeableShortsItem> DYEABLE_SHORTS = REGISTRY.register("dyeable_shorts", DyeableShortsItem::new);
    public static final RegistryObject<HazardBodySuit> HAZARD_BODY_SUIT = REGISTRY.register("hazard_body_suit", HazardBodySuit::new);
    public static final RegistryObject<KeycardItem> KEYCARD_ITEM = REGISTRY.register("keycard", KeycardItem::new);
    public static final RegistryObject<TimedKeypadItem> TIMED_KEYPAD = REGISTRY.register("timed_keypad", TimedKeypadItem::new);
    public static final RegistryObject<BlockItem> HAND_SCANNER = block(ChangedAddonBlocks.HAND_SCANNER);
    public static final RegistryObject<BlockItem> PAWS_SCANNER = block(ChangedAddonBlocks.PAWS_SCANNER);

    @SubscribeEvent
    public static void clientLoad(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(LAETHIN.get(), ChangedAddonMod.resourceLoc("laethin_type"), (itemStackToRender, clientWorld, entity, itemEntityId) -> LaethinItem.getLaethinTypeOfStack(itemStackToRender).getValue());
            ItemProperties.register(LAETHIN_SYRINGE.get(), ChangedAddonMod.resourceLoc("laethin_syringe_type"),
                    (itemStackToRender, clientWorld, entity, itemEntityId) -> LaethinItem.getLaethinTypeOfStack(itemStackToRender).getValue());
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

    private static RegistryObject<Item> registerSimple(String path) {
        return REGISTRY.register(path, () -> new Item(new Item.Properties()));
    }

    private static RegistryObject<Item> registerSimple(String path, Item.Properties properties) {
        return REGISTRY.register(path, () -> new Item(properties));
    }

    private static RegistryObject<Item> registerWithDesc(String path) {
        return REGISTRY.register(path, ItemWithDescription::new);
    }

    private static RegistryObject<Item> registerWithDesc(String path, Item.Properties properties) {
        return REGISTRY.register(path, () -> new ItemWithDescription(properties));
    }

    private static RegistryObject<SpawnEggItem> registerSpawnEgg(String path, RegistryObject<? extends EntityType<? extends Mob>> entity, int bgColor, int highlightColor) {
        return REGISTRY.register(path, () -> new ForgeSpawnEggItem(entity, bgColor, highlightColor, new Item.Properties()));
    }

    private static RegistryObject<BlockItem> block(RegistryObject<? extends Block> block) {
        return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static RegistryObject<BlockItem> blockNoTab(RegistryObject<? extends Block> block) {
        return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static RegistryObject<BlockItem> block(RegistryObject<? extends Block> block, Item.Properties properties) {
        return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), properties));
    }

    public static List<RegistryObject<Item>> getNoTabItems() {
        return List.of(SNEP_ICON, PAT_ICON, FRIENDLY_GOEY_ICON, CATALYZER_BLOCK_ILLUSTRATIVE_ITEM, UNIFUSER_BLOCK_ILLUSTRATIVE_ITEM, CHANGED_BOOK);
    }
}
