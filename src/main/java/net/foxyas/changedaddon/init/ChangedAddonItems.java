
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.renderer.items.LaserItemDynamicRender;
import net.foxyas.changedaddon.item.*;
import net.foxyas.changedaddon.item.armor.DyeableShorts;
import net.foxyas.changedaddon.procedures.DotValueOfViewProcedure;
import net.foxyas.changedaddon.procedures.IsSignalCatcherCordsSetProcedure;
import net.foxyas.changedaddon.procedures.LaethinPropertyValueProviderProcedure;
import net.foxyas.changedaddon.procedures.TransfurTotemItemInInventoryProcedure;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.awt.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ChangedAddonItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ChangedAddonMod.MODID);

	public static final RegistryObject<Item> CHANGED_BOOK = REGISTRY.register("changedbook", ChangedBookItem::new);

	public static final RegistryObject<Item> ANTI_LATEX_BASE = REGISTRY.register("anti_latex_base", UnlatexbaseItem::new);

	public static final RegistryObject<Item> LATEX_INSULATOR = block(ChangedAddonBlocks.LATEX_INSULATOR, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> IMPURE_AMMONIA = REGISTRY.register("impure_ammonia", InpureammoniaItem::new);

	public static final RegistryObject<Item> AMMONIA_PARTICLE = REGISTRY.register("ammonia_particle", AmmoniaParticleItem::new);

	public static final RegistryObject<Item> AMMONIA_COMPRESSED = REGISTRY.register("ammonia_compressed", AmmoniaCompressedItem::new);

	public static final RegistryObject<Item> AMMONIA = REGISTRY.register("ammonia", AmmoniaItem::new);

	public static final RegistryObject<Item> LITIX_CAMONIA = REGISTRY.register("litix_camonia", LitixCamoniaItem::new);

	public static final RegistryObject<Item> LAETHIN = REGISTRY.register("laethin", LaethinItem::new);

	public static final RegistryObject<Item> SYRINGE = REGISTRY.register("syringe", SyringeItem::new);

	public static final RegistryObject<Item> CATALYZEDDNA = REGISTRY.register("catalyzeddna", CatalyzedDNAItem::new);

	public static final RegistryObject<Item> DIFFUSION_SYRINGE = REGISTRY.register("diffusion_syringe", DiffusionSyringeItem::new);

	public static final RegistryObject<Item> SYRINGEWITHLITIXCAMMONIA = REGISTRY.register("syringewithlitixcammonia", SyringewithlitixcammoniaItem::new);

	public static final RegistryObject<Item> LAETHIN_SYRINGE = REGISTRY.register("laethin_syringe", LaethinSyringeItem::new);

	public static final RegistryObject<Item> POTWITHCAMONIA = REGISTRY.register("potwithcamonia", PotWithCamoniaItem::new);

	public static final RegistryObject<Item> ORANGEJUICE = REGISTRY.register("orangejuice", OrangejuiceItem::new);

	public static final RegistryObject<Item> RAW_IRIDIUM = REGISTRY.register("raw_iridium", RawIridiumItem::new);

	public static final RegistryObject<Item> IRIDIUM = REGISTRY.register("iridium", IridiumItem::new);

	public static final RegistryObject<Item> IRIDIUM_ORE = block(ChangedAddonBlocks.IRIDIUM_ORE, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> IRIDIUM_BLOCK = block(ChangedAddonBlocks.IRIDIUM_BLOCK, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> PAINITE = REGISTRY.register("painite", PainiteItem::new);

	public static final RegistryObject<Item> PAINITE_ORE = block(ChangedAddonBlocks.PAINITE_ORE, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> PAINITE_BLOCK = block(ChangedAddonBlocks.PAINITE_BLOCK, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> PROTOTYPE_SPAWN_EGG = REGISTRY.register("prototype_spawn_egg",
			() -> new ForgeSpawnEggItem(ChangedAddonEntities.PROTOTYPE, -5325833, -9306113, new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> FOXYAS_SPAWN_EGG = REGISTRY.register("foxyas_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.FOXYAS, -1, -26215, new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> SPAWNEGGOFFOXYAS = REGISTRY.register("spawneggoffoxyas", SpawnEggOfFoxyasItem::new);

	public static final RegistryObject<Item> LITIX_CAMONIA_SPRAY = REGISTRY.register("litix_camonia_spray", LitixCamoniaSprayItem::new);

	public static final RegistryObject<Item> EMPTY_SPRAY = REGISTRY.register("empty_spray", EmptySprayItem::new);

	public static final RegistryObject<Item> LITIX_CAMONIA_FLUID_BUCKET = REGISTRY.register("litix_camonia_fluid_bucket", LitixCamoniaFluidItem::new);

	public static final RegistryObject<Item> CATALYZER = block(ChangedAddonBlocks.CATALYZER, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> UNIFUSER = block(ChangedAddonBlocks.UNIFUSER, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> HAZARD_SUIT_HELMET = REGISTRY.register("hazard_suit_helmet", HazardSuitItem.Helmet::new);

	public static final RegistryObject<Item> HAZARD_SUIT_CHESTPLATE = REGISTRY.register("hazard_suit_chestplate", HazardSuitItem.Chestplate::new);

	public static final RegistryObject<Item> HAZARD_SUIT_LEGGINGS = REGISTRY.register("hazard_suit_leggings", HazardSuitItem.Leggings::new);

	public static final RegistryObject<Item> HAZARD_SUIT_BOOTS = REGISTRY.register("hazard_suit_boots", HazardSuitItem.Boots::new);

	public static final RegistryObject<Item> DARK_LATEX_PUDDLE = block(ChangedAddonBlocks.DARKLATEXPUDDLE, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> LUNAR_ROSE_HELMET = REGISTRY.register("lunarrose_helmet", LunarroseItem.Helmet::new);

	public static final RegistryObject<Item> EXPERIMENT_009_DNA = REGISTRY.register("experiment_009dna", Experiment009dnaItem::new);

	public static final RegistryObject<Item> EXP_9_LATEX_BASE = REGISTRY.register("exp_9_latex_base", Exp9LatexBaseItem::new);

	public static final RegistryObject<Item> EXP_9_CONTAINMENT_VIAL = REGISTRY.register("exp_9_containment_vial", Experiment009SpawneggItem::new);

	public static final RegistryObject<Item> TRANSFUR_TOTEM = REGISTRY.register("transfur_totem", TransfurTotemItem::new);

	public static final RegistryObject<Item> SIGNAL_BLOCK = block(ChangedAddonBlocks.SIGNAL_BLOCK, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> SIGNAL_CATCHER = REGISTRY.register("signal_catcher", SignalCatcherItem::new);

	public static final RegistryObject<Item> EXPERIMENT_10_DNA = REGISTRY.register("experiment_10_dna", Experiment10DnaItem::new);

	public static final RegistryObject<Item> EXP_10_LATEX_BASE = REGISTRY.register("exp_10_latex_base", Exp10LatexBaseItem::new);

	public static final RegistryObject<Item> EXP_10_CONTAINMENT_VIAL = REGISTRY.register("exp_10_containment_vial", BossExperiment10SpawnEggItem::new);

	public static final RegistryObject<Item> RED_LATEX_GOO = REGISTRY.register("red_latex_goo", RedLatexGooItem::new);

	public static final RegistryObject<Item> INFORMANT_BLOCK = block(ChangedAddonBlocks.INFORMANTBLOCK, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> ACCESSORIES_CHESTPLATE = REGISTRY.register("accessories_chestplate", AccessoriesItem.Chestplate::new);

	public static final RegistryObject<Item> CRYSTAL_DAGGER_RED = REGISTRY.register("crystal_dagger_red", CrystalAddagerRedItem::new);

	public static final RegistryObject<Item> CRYSTAL_DAGGER_GREEN = REGISTRY.register("crystal_dagger_green", CrystalAddagerGreenItem::new);

	public static final RegistryObject<Item> CRYSTAL_DAGGER_BLACK = REGISTRY.register("crystal_dagger_black", CrystalAddagerBlackItem::new);

	public static final RegistryObject<Item> LAETHINMINATOR = REGISTRY.register("laethinminator", LaethinminatorItem::new);

	public static final RegistryObject<Item> CROW_BAR = REGISTRY.register("crow_bar", CrowbarItem::new);

	public static final RegistryObject<Item> SNEPSI = REGISTRY.register("snepsi", SnepsiItem::new);

	public static final RegistryObject<Item> FOXTA = REGISTRY.register("foxta", FoxtaItem::new);

	public static final RegistryObject<Item> DORMANT_DARK_LATEX = block(ChangedAddonBlocks.DORMANT_DARK_LATEX, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> DORMANT_WHITE_LATEX = block(ChangedAddonBlocks.DORMANT_WHITE_LATEX, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> DARK_LATEX_SPRAY = REGISTRY.register("dark_latex_spray", DarkLatexSprayItem::new);

	public static final RegistryObject<Item> WHITE_LATEX_SPRAY = REGISTRY.register("white_latex_spray", WhiteLatexSprayItem::new);

	public static final RegistryObject<Item> HAZMAT_SUIT_HELMET = REGISTRY.register("hazmat_suit_helmet", HazmatSuitItem.Helmet::new);

	public static final RegistryObject<Item> HAZMAT_SUIT_CHESTPLATE = REGISTRY.register("hazmat_suit_chestplate", HazmatSuitItem.Chestplate::new);

	public static final RegistryObject<Item> HAZMAT_SUIT_LEGGINGS = REGISTRY.register("hazmat_suit_leggings", HazmatSuitItem.Leggings::new);

	public static final RegistryObject<Item> HAZMAT_SUIT_BOOTS = REGISTRY.register("hazmat_suit_boots", HazmatSuitItem.Boots::new);

	public static final RegistryObject<Item> SNEP_PLUSH = block(ChangedAddonBlocks.SNEP_PLUSH, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> WOLF_PLUSH = block(ChangedAddonBlocks.WOLF_PLUSH, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> BIOMASS = REGISTRY.register("biomass", BiomassItem::new);

	public static final RegistryObject<Item> CONTAINMENT_CONTAINER = block(ChangedAddonBlocks.CONTAINMENT_CONTAINER, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> ADVANCED_UNIFUSER = block(ChangedAddonBlocks.ADVANCED_UNIFUSER, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> ADVANCED_CATALYZER = block(ChangedAddonBlocks.ADVANCED_CATALYZER, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> REINFORCED_WALL = block(ChangedAddonBlocks.REINFORCED_WALL, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> REINFORCED_WALL_SILVER_STRIPED = block(ChangedAddonBlocks.REINFORCED_WALL_SILVER_STRIPED, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> REINFORCED_WALL_SILVER_TILED = block(ChangedAddonBlocks.REINFORCED_WALL_SILVER_TILED, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> REINFORCED_WALL_CAUTION = block(ChangedAddonBlocks.REINFORCED_WALL_CAUTION, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> REINFORCED_CROSS_BLOCK = block(ChangedAddonBlocks.REINFORCED_CROSS_BLOCK, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> WALL_WHITE_CRACKED = block(ChangedAddonBlocks.WALL_WHITE_CRACKED, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> WALL_WHITE_STAIR = block(ChangedAddonBlocks.WALL_WHITE_STAIR, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> WALL_WHITE_SLAB = block(ChangedAddonBlocks.WALL_WHITE_SLAB, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> BLUE_WOLF_CRYSTAL_BLOCK = block(ChangedAddonBlocks.BLUE_WOLF_CRYSTAL_BLOCK, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> ORANGE_WOLF_CRYSTAL_BLOCK = block(ChangedAddonBlocks.ORANGE_WOLF_CRYSTAL_BLOCK, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> YELLOW_WOLF_CRYSTAL_BLOCK = block(ChangedAddonBlocks.YELLOW_WOLF_CRYSTAL_BLOCK, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> WHITE_WOLF_CRYSTAL_BLOCK = block(ChangedAddonBlocks.WHITE_WOLF_CRYSTAL_BLOCK, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> LUMINAR_CRYSTAL_BLOCK = block(ChangedAddonBlocks.LUMINAR_CRYSTAL_BLOCK, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> LUMINAR_CRYSTAL_SMALL = block(ChangedAddonBlocks.LUMINAR_CRYSTAL_SMALL, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> YELLOW_WOLF_CRYSTAL_SMALL = block(ChangedAddonBlocks.YELLOW_WOLF_CRYSTAL_SMALL, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> ORANGE_WOLF_CRYSTAL_SMALL = block(ChangedAddonBlocks.ORANGE_WOLF_CRYSTAL_SMALL, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> BLUE_WOLF_CRYSTAL_SMALL = block(ChangedAddonBlocks.BLUE_WOLF_CRYSTAL_SMALL, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> WHITE_WOLF_CRYSTAL_SMALL = block(ChangedAddonBlocks.WHITE_WOLF_CRYSTAL_SMALL, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> BLUE_WOLF_CRYSTAL_FRAGMENT = REGISTRY.register("blue_wolf_crystal_fragment", BlueWolfCrystalFragmentItem::new);

	public static final RegistryObject<Item> ORANGE_WOLF_CRYSTAL_FRAGMENT = REGISTRY.register("orange_wolf_crystal_fragment", OrangeWolfCrystalFragmentItem::new);

	public static final RegistryObject<Item> YELLOW_WOLF_CRYSTAL_FRAGMENT = REGISTRY.register("yellow_wolf_crystal_fragment", YellowWolfCrystalFragmentItem::new);

	public static final RegistryObject<Item> WHITE_WOLF_CRYSTAL_FRAGMENT = REGISTRY.register("white_wolf_crystal_fragment", WhiteWolfCrystalFragmentItem::new);

	public static final RegistryObject<Item> LUMINAR_CRYSTAL_SHARD = REGISTRY.register("luminar_crystal_shard", LuminarCrystalShardItem::new);

	public static final RegistryObject<Item> LUMINAR_CRYSTAL_SHARD_HEARTED = REGISTRY.register("luminar_crystal_shard_hearted", LuminarCrystalShardHeartedItem::new);

	public static final RegistryObject<Item> GOO_CORE = block(ChangedAddonBlocks.GOO_CORE, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> GOO_CORE_FRAGMENT = REGISTRY.register("goo_core_fragment", GooCoreFragmentItem::new);

	public static final RegistryObject<Item> MEANINGLESS_STRAFE_MUSIC_DISC = REGISTRY.register("meaningless_strafe_music_disc", MeaninglessStrafeMusicDiscItem::new);

	public static final RegistryObject<Item> LUMINAR_CRYSTAL_SPEAR = REGISTRY.register("luminar_crystal_spear", LuminarCrystalSpearItem::new);

	public static final RegistryObject<Item> ELECTRIC_KATANA = REGISTRY.register("electric_katana", ElectricKatanaItem::new);

	public static final RegistryObject<Item> ELECTRIC_KATANA_RED = REGISTRY.register("electric_katana_red", ElectricKatanaRedItem::new);

	public static final RegistryObject<Item> PAINITE_SWORD = REGISTRY.register("painite_sword", PainiteSwordItem::new);

	public static final RegistryObject<Item> PAINITE_PICKAXE = REGISTRY.register("painite_pickaxe", PainitePickaxeItem::new);

	public static final RegistryObject<Item> PAINITE_AXE = REGISTRY.register("painite_axe", PainiteAxeItem::new);

	public static final RegistryObject<Item> PAINITE_SHOVEL = REGISTRY.register("painite_shovel", PainiteShovelItem::new);

	public static final RegistryObject<Item> PAINITE_HOE = REGISTRY.register("painite_hoe", PainiteHoeItem::new);

	public static final RegistryObject<Item> PAINITE_ARMOR_HELMET = REGISTRY.register("painite_armor_helmet", PainiteArmorItem.Helmet::new);

	public static final RegistryObject<Item> PAINITE_ARMOR_CHESTPLATE = REGISTRY.register("painite_armor_chestplate", PainiteArmorItem.Chestplate::new);

	public static final RegistryObject<Item> PAINITE_ARMOR_LEGGINGS = REGISTRY.register("painite_armor_leggings", PainiteArmorItem.Leggings::new);

	public static final RegistryObject<Item> PAINITE_ARMOR_BOOTS = REGISTRY.register("painite_armor_boots", PainiteArmorItem.Boots::new);

	public static final RegistryObject<Item> THE_DECIMATOR = REGISTRY.register("the_decimator", TheDecimatorItem::new);

	public static final RegistryObject<Item> GENERATOR = block(ChangedAddonBlocks.GENERATOR, null);

	public static final RegistryObject<Item> CATALYZER_BLOCK_ILLUSTRATIVE_ITEM = REGISTRY.register("catalyzer_block_illustrative_item", CatalyzerBlockIllustrativeItemItem::new);

	public static final RegistryObject<Item> UNIFUSER_BLOCK_ILLUSTRATIVE_ITEM = REGISTRY.register("unifuser_block_illustrative_item", UnifuserblockIllustrativeItemItem::new);

	public static final RegistryObject<Item> AMMONIA_PARTICLES_JEI_ILLUSTRATIVE = REGISTRY.register("ammonia_particles_jei_illustrative", AmmoniaParticlesJeiIllustrativeItem::new);

	public static final RegistryObject<Item> ERIK_SPAWN_EGG = REGISTRY.register("erik_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.ERIK, -1, -1, new Item.Properties().tab(null)));

	public static final RegistryObject<Item> FOXTA_CAN = block(ChangedAddonBlocks.FOXTA_CAN, null);

	public static final RegistryObject<Item> SNEPSI_CAN = block(ChangedAddonBlocks.SNEPSI_CAN, null);

	public static final RegistryObject<Item> SNEP_ICON = REGISTRY.register("snep_icon", SnepIconItem::new);

	public static final RegistryObject<Item> FRIENDLY_GOEY_ICON = REGISTRY.register("friendly_goey_icon", FriendlyGoeyIconItem::new);

	public static final RegistryObject<Item> PAT_ICON = REGISTRY.register("pat_icon", PatIconItem::new);

	public static final RegistryObject<Item> GOLDEN_ORANGE = REGISTRY.register("golden_orange", GoldenOrange::new);

	public static final RegistryObject<Item> LATEX_SNOW_FOX_SPAWN_EGG = REGISTRY.register("latex_snow_fox_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LATEX_SNOW_FOX, 0xFFFFFFF, 0xfD6DDF7, new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> LATEX_SNOW_FOX_FEMALE_SPAWN_EGG = REGISTRY.register("latex_snow_fox_female_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LATEX_SNOW_FOX_FEMALE, 0xFFFFFFF, 0xfD6DDF7, new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> DAZED_LATEX_SPAWN_EGG = REGISTRY.register("latex_dazed_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.DAZED, 0xFFFFFFF, 0xffCFCFCF, new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> PURO_KIND = REGISTRY.register("puro_kind_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.PURO_KIND, Color3.getColor("#393939").toInt(), Color3.getColor("#303030").toInt(), new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> PURO_KIND_FEMALE = REGISTRY.register("puro_kind_female_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.PURO_KIND_FEMALE, Color3.getColor("#393939").toInt(), Color3.getColor("#303030").toInt(), new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> BUNY = REGISTRY.register("buny_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.BUNY, Color3.getColor("#fee9c8").toInt(), Color3.getColor("#9c8c73").toInt(), new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> ORGANIC_SNOW_LEOPARD_MALE_SPAWN_EGG = REGISTRY.register("snow_leopard_male_organic_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.SNOW_LEOPARD_MALE_ORGANIC, Color3.getColor("#9C9C9C").toInt(), Color3.getColor("#292929").toInt(), new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> ORGANIC_SNOW_LEOPARD_FEMALE_SPAWN_EGG = REGISTRY.register("snow_leopard_female_organic_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.SNOW_LEOPARD_FEMALE_ORGANIC, Color3.getColor("#9C9C9C").toInt(), Color3.getColor("#292929").toInt(), new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> MIRROR_WHITE_TIGER = REGISTRY.register("mirror_white_tiger_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.MIRROR_WHITE_TIGER, Color3.getColor("#FFFFFF").toInt(), Color3.getColor("#ACACAC").toInt(), new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> WOLFY_SPAWN_EGG = REGISTRY.register("wolfy_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.WOLFY, Color3.getColor("#393939").toInt(), Color3.getColor("#303030").toInt(), new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> EXP1_MALE_SPAWN_EGG = REGISTRY.register("exp_1_male_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.EXP_1_MALE, 0xFFFFFFF, 0xffb6b9b9, new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> EXP1_FEMALE_SPAWN_EGG = REGISTRY.register("exp_1_female_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.EXP_1_FEMALE, 0xFFFFFFF, 0xffb6b9b9, new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> EXP2_MALE_SPAWN_EGG = REGISTRY.register("exp_2_male_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.EXP_2_MALE, Color3.getColor("#9C9C9C").toInt(), Color3.getColor("#484848").toInt(), new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> EXP2_FEMALE_SPAWN_EGG = REGISTRY.register("exp_2_female_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.EXP_2_FEMALE, Color3.getColor("#9C9C9C").toInt(), Color3.getColor("#484848").toInt(), new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> LATEX_SNEP_SPAWN_EGG = REGISTRY.register("latex_snep_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LATEX_SNEP, Color3.getColor("#9C9C9C").toInt(), Color3.getColor("#484848").toInt(), new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> EXP6_SPAWN_EGG = REGISTRY.register("exp_6_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.EXP_6, Color3.getColor("#B2B1B9").toInt(), Color3.getColor("#CAA2E6").toInt(), new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> EXP10_SPAWN_EGG = REGISTRY.register("experiment_10_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.EXPERIMENT_10, Color3.getColor("#181818").toInt(), Color3.getColor("#ed1c24").toInt(), new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> EXP10_BOSS_SPAWN_EGG = REGISTRY.register("experiment_10_boss_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.EXPERIMENT_10_BOSS, Color3.getColor("#181818").toInt(), Color3.getColor("#ed1c24").toInt(), new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> KET_SPAWN_EGG = REGISTRY.register("ket_experiment_009_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.KET_EXPERIMENT_009, Color3.getColor("#E9E9E9").toInt(), Color3.getColor("#66FFFF").toInt(), new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> KET_BOSS_SPAWN_EGG = REGISTRY.register("ket_experiment_009_boss_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.KET_EXPERIMENT_009_BOSS, Color3.getColor("#E9E9E9").toInt(), Color3.getColor("#66FFFF").toInt(), new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> PARTIAL_SNOW_LEOPARD_SPAWN_EGG = REGISTRY.register("latex_snow_leopard_partial_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.SNOW_LEOPARD_PARTIAL, Color3.getColor("#9C9C9C").toInt(), Color3.getColor("#484848").toInt(), new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> REYN_TRANSFUR_SPAWN_EGG = REGISTRY.register("reyn_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.REYN, Color3.getColor("#4C4C4C").toInt(), Color3.getColor("#464646").toInt(), new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> LUMINARCTIC_LEOPARD_SPAWN_EGG = REGISTRY.register("luminarctic_leopard_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LUMINARCTIC_LEOPARD, Color3.getColor("#414141").toInt(), Color3.getColor("#FFFFFF").toInt(), new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> LUMINARCTIC_FEMALE_LEOPARD_SPAWN_EGG = REGISTRY.register("female_luminarctic_leopard_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.FEMALE_LUMINARCTIC_LEOPARD, Color3.getColor("#414141").toInt(), Color3.getColor("#FFFFFF").toInt(), new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> LATEX_SQUID_TIGER_SHARK_SPAWN_EGG = REGISTRY.register("latex_squid_tiger_shark_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LATEX_SQUID_TIGER_SHARK, Color3.getColor("#969696").toInt(), Color3.BLACK.toInt(), new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> LYNX_SPAWN_EGG = REGISTRY.register("lynx_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LYNX, Color3.getColor("#ebd182").toInt(), Color3.getColor("eace7a").toInt(), new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> FOXTA_FOXY_SPAWN_EGG = REGISTRY.register("foxta_foxy_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.FOXTA_FOXY, Color3.getColor("#FF8F33").toInt(), Color3.getColor("#FFBC85").toInt(), new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> SNEPSI_LEOPARD_SPAWN_EGG = REGISTRY.register("snepsi_leopard_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.SNEPSI_LEOPARD, Color3.getColor("#95D161").toInt(), Color3.getColor("#B5DF90").toInt(), new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> FENGQI_WOLF_SPAWN_EGG = REGISTRY.register("fengqi_wolf_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.FENGQI_WOLF, Color3.getColor("#93c6fd").toInt(), Color3.getColor("#FAC576").toInt(), new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> BAGEL_SPAWN_EGG = REGISTRY.register("bagel_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.BAGEL, 0xFFFFFFF, 0xfD6DDF7, new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> LATEX_SNEP_SHARK_SPAWN_EGG = REGISTRY.register("latex_dragon_snow_leopard_shark_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LATEX_DRAGON_SNOW_LEOPARD_SHARK, 0x969696, 0x292929, new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> CRYSTAL_GAS_CAT_MALE_SPAWN_EGG = REGISTRY.register("crystal_gas_cat_male_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.CRYSTAL_GAS_CAT_MALE, 0x9c9c9c, 0x262626, new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> CRYSTAL_GAS_CAT_FEMALE_SPAWN_EGG = REGISTRY.register("crystal_gas_cat_female_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.CRYSTAL_GAS_CAT_FEMALE, 0x9c9c9c, 0x262626, new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> VOID_FOX_SPAWN_EGG = REGISTRY.register("void_fox_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.VOID_FOX, 0x393939, 0xffffff, new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> HAYDEN_FENNEC_FOX_SPAWN_EGG = REGISTRY.register("hayden_fennec_fox_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.HAYDEN_FENNEC_FOX, 0xF6DC70, 0xF0E4B9, new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> BLUE_LIZARD_SPAWN_EGG = REGISTRY.register("blue_lizard_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.BLUE_LIZARD, 0x00F3FF, 0xffffff, new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> AVALI_SPAWN_EGG = REGISTRY.register("avali_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.AVALI, 0xffffff, 0xffffff, new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> LATEX_KITSUNE_MALE_SPAWN_EGG = REGISTRY.register("latex_kitsune_male_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LATEX_KITSUNE_MALE, 0xfff6f6, 0xffeeee, new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> LATEX_KITSUNE_FEMALE_SPAWN_EGG = REGISTRY.register("latex_kitsune_female_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LATEX_KITSUNE_FEMALE, 0xfff6f6, 0xffeeee, new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> LATEX_CALICO_CAT_MALE_SPAWN_EGG = REGISTRY.register("latex_calico_cat_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.LATEX_CALICO_CAT, 0xffece4, 0xd56f53, new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> PROTOGEN_SPAWN_EGG = REGISTRY.register("protogen_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonEntities.PROTOGEN, new Color(255,255,255).getRGB(), new Color(0, 196, 255).getRGB(), new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> DARK_LATEX_COAT = REGISTRY.register("dark_latex_coat",
			() -> new DarkLatexCoatItem(EquipmentSlot.CHEST, new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> DARK_LATEX_HEAD_CAP = REGISTRY.register("dark_latex_coat_cap",
			() -> new DarkLatexCoatItem.HeadPart(EquipmentSlot.HEAD, new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON)));

	public static final RegistryObject<Item> LASER_POINTER = REGISTRY.register("laser_pointer", LaserPointer::new);

	public static final RegistryObject<Item> DYEABLE_SHORTS = REGISTRY.register("dyeable_shorts", DyeableShorts::new);

	public static final RegistryObject<Item> TIMED_KEYPAD = RegisterBlockItem(REGISTRY, ChangedAddonBlocks.TIMED_KEYPAD, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> HAND_SCANNER = RegisterBlockItem(REGISTRY, ChangedAddonBlocks.HAND_SCANNER, ChangedAddonTabs.TAB_CHANGED_ADDON);

	public static final RegistryObject<Item> PAWS_SCANNER = RegisterBlockItem(REGISTRY, ChangedAddonBlocks.PAWS_SCANNER, ChangedAddonTabs.TAB_CHANGED_ADDON);

	@SubscribeEvent
	public static void clientLoad(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			ItemProperties.register(LAETHIN.get(), new ResourceLocation("changed_addon:laethin_type"), (itemStackToRender, clientWorld, entity, itemEntityId) -> (float) LaethinPropertyValueProviderProcedure.execute(itemStackToRender));
			ItemProperties.register(LAETHIN_SYRINGE.get(), new ResourceLocation("changed_addon:laethin_syringe_type"),
					(itemStackToRender, clientWorld, entity, itemEntityId) -> (float) LaethinPropertyValueProviderProcedure.execute(itemStackToRender));
			ItemProperties.register(TRANSFUR_TOTEM.get(), new ResourceLocation("changed_addon:transfur_totem_glowtick"), (itemStackToRender, clientWorld, entity, itemEntityId) -> (float) TransfurTotemItemInInventoryProcedure.execute(entity));
			ItemProperties.register(SIGNAL_CATCHER.get(), new ResourceLocation("changed_addon:signal_catcher_dot_value"), (itemStackToRender, clientWorld, entity, itemEntityId) -> (float) DotValueOfViewProcedure.execute(entity, itemStackToRender));
			ItemProperties.register(SIGNAL_CATCHER.get(), new ResourceLocation("changed_addon:signal_catcher_cord_set"), (itemStackToRender, clientWorld, entity, itemEntityId) -> (float) IsSignalCatcherCordsSetProcedure.execute(itemStackToRender));
			ItemProperties.register(LUMINAR_CRYSTAL_SPEAR.get(), new ResourceLocation("throwing"),
					(stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
			LaserItemDynamicRender.DynamicLaserColor(LASER_POINTER);
		});
	}

	private static RegistryObject<Item> block(RegistryObject<Block> block, CreativeModeTab tab) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
	}

	private static RegistryObject<Item> RegisterBlockItem(DeferredRegister<Item> REGISTRY, RegistryObject<Block> block, CreativeModeTab tab) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
	}

	private static RegistryObject<Item> RegisterBlockItem(DeferredRegister<Item> REGISTRY, String id, RegistryObject<Block> block, CreativeModeTab tab) {
		return REGISTRY.register(id, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
	}

	private static RegistryObject<Item> RegisterBlockItem(RegistryObject<Block> block, CreativeModeTab tab) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
	}
}
