
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.foxyas.changedaddon.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.BlockItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.item.ItemProperties;

import net.foxyas.changedaddon.procedures.TransfurTotemItemInInventoryProcedure;
import net.foxyas.changedaddon.procedures.LaethinPropertyValueProviderProcedure;
import net.foxyas.changedaddon.item.YellowWolfCrystalFragmentItem;
import net.foxyas.changedaddon.item.WhiteWolfCrystalFragmentItem;
import net.foxyas.changedaddon.item.WhiteLatexSprayItem;
import net.foxyas.changedaddon.item.UnlatexbaseItem;
import net.foxyas.changedaddon.item.UnifuserblockIllustrativeItemItem;
import net.foxyas.changedaddon.item.TransfurTotemItem;
import net.foxyas.changedaddon.item.TheDecimatorItem;
import net.foxyas.changedaddon.item.SyringewithlitixcammoniaItem;
import net.foxyas.changedaddon.item.SyringeItem;
import net.foxyas.changedaddon.item.SpawneggoffoxyasItem;
import net.foxyas.changedaddon.item.SnepsiItem;
import net.foxyas.changedaddon.item.SnepIconItem;
import net.foxyas.changedaddon.item.SignalCatcherItem;
import net.foxyas.changedaddon.item.RawIridiumItem;
import net.foxyas.changedaddon.item.PotwithcamoniaItem;
import net.foxyas.changedaddon.item.PatIconItem;
import net.foxyas.changedaddon.item.PainiteSwordItem;
import net.foxyas.changedaddon.item.PainiteShovelItem;
import net.foxyas.changedaddon.item.PainitePickaxeItem;
import net.foxyas.changedaddon.item.PainiteItem;
import net.foxyas.changedaddon.item.PainiteHoeItem;
import net.foxyas.changedaddon.item.PainiteAxeItem;
import net.foxyas.changedaddon.item.PainiteArmorItem;
import net.foxyas.changedaddon.item.OrangejuiceItem;
import net.foxyas.changedaddon.item.OrangeWolfCrystalFragmentItem;
import net.foxyas.changedaddon.item.LunarroseItem;
import net.foxyas.changedaddon.item.LuminarCrystalShardItem;
import net.foxyas.changedaddon.item.LitixCamoniaSprayItem;
import net.foxyas.changedaddon.item.LitixCamoniaItem;
import net.foxyas.changedaddon.item.LitixCamoniaFluidItem;
import net.foxyas.changedaddon.item.LaethinSyringeItem;
import net.foxyas.changedaddon.item.LaethinItem;
import net.foxyas.changedaddon.item.IridiumItem;
import net.foxyas.changedaddon.item.InpureammoniaItem;
import net.foxyas.changedaddon.item.HazmatSuitItem;
import net.foxyas.changedaddon.item.HazardSuitItem;
import net.foxyas.changedaddon.item.FriendlyGoeyIconItem;
import net.foxyas.changedaddon.item.FoxtaItem;
import net.foxyas.changedaddon.item.Experiment10DnaItem;
import net.foxyas.changedaddon.item.Experiment009dnaItem;
import net.foxyas.changedaddon.item.Experiment009SpawneggItem;
import net.foxyas.changedaddon.item.Exp9LatexBaseItem;
import net.foxyas.changedaddon.item.Exp10LatexBaseItem;
import net.foxyas.changedaddon.item.EmptySprayItem;
import net.foxyas.changedaddon.item.ElectricKatanaRedItem;
import net.foxyas.changedaddon.item.ElectricKatanaItem;
import net.foxyas.changedaddon.item.DiffusionSyringeItem;
import net.foxyas.changedaddon.item.DarkLatexSprayItem;
import net.foxyas.changedaddon.item.CrystalAddagerRedItem;
import net.foxyas.changedaddon.item.CrystalAddagerGreenItem;
import net.foxyas.changedaddon.item.CrystalAddagerBlackItem;
import net.foxyas.changedaddon.item.CrowBarItem;
import net.foxyas.changedaddon.item.ChangedbookItem;
import net.foxyas.changedaddon.item.CatlyzerblockIllustrativeItemItem;
import net.foxyas.changedaddon.item.CatalyzeddnaItem;
import net.foxyas.changedaddon.item.BossExperiment10SpawnEggItem;
import net.foxyas.changedaddon.item.BlueWolfCrystalFragmentItem;
import net.foxyas.changedaddon.item.BiomassItem;
import net.foxyas.changedaddon.item.AmmoniaparticleItem;
import net.foxyas.changedaddon.item.AmmoniaParticlesJeiIllustrativeItem;
import net.foxyas.changedaddon.item.AmmoniaItem;
import net.foxyas.changedaddon.item.AmmoniaCompressedItem;
import net.foxyas.changedaddon.item.AccessoriesItem;
import net.foxyas.changedaddon.ChangedAddonMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ChangedAddonModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ChangedAddonMod.MODID);
	public static final RegistryObject<Item> CHANGEDBOOK = REGISTRY.register("changedbook", () -> new ChangedbookItem());
	public static final RegistryObject<Item> UNLATEXBASE = REGISTRY.register("unlatexbase", () -> new UnlatexbaseItem());
	public static final RegistryObject<Item> LATEX_INSULATOR = block(ChangedAddonModBlocks.LATEX_INSULATOR, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> IMPUREAMMONIA = REGISTRY.register("impureammonia", () -> new InpureammoniaItem());
	public static final RegistryObject<Item> AMMONIAPARTICLE = REGISTRY.register("ammoniaparticle", () -> new AmmoniaparticleItem());
	public static final RegistryObject<Item> AMMONIA_COMPRESSED = REGISTRY.register("ammonia_compressed", () -> new AmmoniaCompressedItem());
	public static final RegistryObject<Item> AMMONIA = REGISTRY.register("ammonia", () -> new AmmoniaItem());
	public static final RegistryObject<Item> LITIX_CAMONIA = REGISTRY.register("litix_camonia", () -> new LitixCamoniaItem());
	public static final RegistryObject<Item> LAETHIN = REGISTRY.register("laethin", () -> new LaethinItem());
	public static final RegistryObject<Item> SYRINGE = REGISTRY.register("syringe", () -> new SyringeItem());
	public static final RegistryObject<Item> CATALYZEDDNA = REGISTRY.register("catalyzeddna", () -> new CatalyzeddnaItem());
	public static final RegistryObject<Item> DIFFUSION_SYRINGE = REGISTRY.register("diffusion_syringe", () -> new DiffusionSyringeItem());
	public static final RegistryObject<Item> SYRINGEWITHLITIXCAMMONIA = REGISTRY.register("syringewithlitixcammonia", () -> new SyringewithlitixcammoniaItem());
	public static final RegistryObject<Item> LAETHIN_SYRINGE = REGISTRY.register("laethin_syringe", () -> new LaethinSyringeItem());
	public static final RegistryObject<Item> POTWITHCAMONIA = REGISTRY.register("potwithcamonia", () -> new PotwithcamoniaItem());
	public static final RegistryObject<Item> ORANGEJUICE = REGISTRY.register("orangejuice", () -> new OrangejuiceItem());
	public static final RegistryObject<Item> RAW_IRIDIUM = REGISTRY.register("raw_iridium", () -> new RawIridiumItem());
	public static final RegistryObject<Item> IRIDIUM = REGISTRY.register("iridium", () -> new IridiumItem());
	public static final RegistryObject<Item> IRIDIUM_ORE = block(ChangedAddonModBlocks.IRIDIUM_ORE, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> IRIDIUM_BLOCK = block(ChangedAddonModBlocks.IRIDIUM_BLOCK, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> PAINITE = REGISTRY.register("painite", () -> new PainiteItem());
	public static final RegistryObject<Item> PAINITE_ORE = block(ChangedAddonModBlocks.PAINITE_ORE, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> PAINITE_BLOCK = block(ChangedAddonModBlocks.PAINITE_BLOCK, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> PROTOTYPE_SPAWN_EGG = REGISTRY.register("prototype_spawn_egg",
			() -> new ForgeSpawnEggItem(ChangedAddonModEntities.PROTOTYPE, -5325833, -9306113, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));
	public static final RegistryObject<Item> FOXYAS_SPAWN_EGG = REGISTRY.register("foxyas_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.FOXYAS, -1, -26215, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));
	public static final RegistryObject<Item> SPAWNEGGOFFOXYAS = REGISTRY.register("spawneggoffoxyas", () -> new SpawneggoffoxyasItem());
	public static final RegistryObject<Item> LITIX_CAMONIA_SPRAY = REGISTRY.register("litix_camonia_spray", () -> new LitixCamoniaSprayItem());
	public static final RegistryObject<Item> EMPTY_SPRAY = REGISTRY.register("empty_spray", () -> new EmptySprayItem());
	public static final RegistryObject<Item> LITIX_CAMONIA_FLUID_BUCKET = REGISTRY.register("litix_camonia_fluid_bucket", () -> new LitixCamoniaFluidItem());
	public static final RegistryObject<Item> CATLYZER = block(ChangedAddonModBlocks.CATLYZER, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> UNIFUSER = block(ChangedAddonModBlocks.UNIFUSER, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> HAZARD_SUIT_HELMET = REGISTRY.register("hazard_suit_helmet", () -> new HazardSuitItem.Helmet());
	public static final RegistryObject<Item> HAZARD_SUIT_CHESTPLATE = REGISTRY.register("hazard_suit_chestplate", () -> new HazardSuitItem.Chestplate());
	public static final RegistryObject<Item> HAZARD_SUIT_LEGGINGS = REGISTRY.register("hazard_suit_leggings", () -> new HazardSuitItem.Leggings());
	public static final RegistryObject<Item> HAZARD_SUIT_BOOTS = REGISTRY.register("hazard_suit_boots", () -> new HazardSuitItem.Boots());
	public static final RegistryObject<Item> DARKLATEXPUDDLE = block(ChangedAddonModBlocks.DARKLATEXPUDDLE, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> LUNARROSE_HELMET = REGISTRY.register("lunarrose_helmet", () -> new LunarroseItem.Helmet());
	public static final RegistryObject<Item> EXPERIMENT_009DNA = REGISTRY.register("experiment_009dna", () -> new Experiment009dnaItem());
	public static final RegistryObject<Item> EXP_9_LATEX_BASE = REGISTRY.register("exp_9_latex_base", () -> new Exp9LatexBaseItem());
	public static final RegistryObject<Item> EXPERIMENT_009_SPAWNEGG = REGISTRY.register("experiment_009_spawnegg", () -> new Experiment009SpawneggItem());
	public static final RegistryObject<Item> TRANSFUR_TOTEM = REGISTRY.register("transfur_totem", () -> new TransfurTotemItem());
	public static final RegistryObject<Item> SIGNAL_BLOCK = block(ChangedAddonModBlocks.SIGNAL_BLOCK, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> SIGNAL_CATCHER = REGISTRY.register("signal_catcher", () -> new SignalCatcherItem());
	public static final RegistryObject<Item> EXPERIMENT_10_DNA = REGISTRY.register("experiment_10_dna", () -> new Experiment10DnaItem());
	public static final RegistryObject<Item> EXP_10_LATEX_BASE = REGISTRY.register("exp_10_latex_base", () -> new Exp10LatexBaseItem());
	public static final RegistryObject<Item> BOSS_EXPERIMENT_10_SPAWN_EGG = REGISTRY.register("boss_experiment_10_spawn_egg", () -> new BossExperiment10SpawnEggItem());
	public static final RegistryObject<Item> INFORMANTBLOCK = block(ChangedAddonModBlocks.INFORMANTBLOCK, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> ACCESSORIES_CHESTPLATE = REGISTRY.register("accessories_chestplate", () -> new AccessoriesItem.Chestplate());
	public static final RegistryObject<Item> CRYSTAL_DAGGER_RED = REGISTRY.register("crystal_dagger_red", () -> new CrystalAddagerRedItem());
	public static final RegistryObject<Item> CRYSTAL_DAGGER_GREEN = REGISTRY.register("crystal_dagger_green", () -> new CrystalAddagerGreenItem());
	public static final RegistryObject<Item> CRYSTAL_DAGGER_BLACK = REGISTRY.register("crystal_dagger_black", () -> new CrystalAddagerBlackItem());
	public static final RegistryObject<Item> CROW_BAR = REGISTRY.register("crow_bar", () -> new CrowBarItem());
	public static final RegistryObject<Item> SNEPSI = REGISTRY.register("snepsi", () -> new SnepsiItem());
	public static final RegistryObject<Item> FOXTA = REGISTRY.register("foxta", () -> new FoxtaItem());
	public static final RegistryObject<Item> DORMANT_DARK_LATEX = block(ChangedAddonModBlocks.DORMANT_DARK_LATEX, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> DORMANT_WHITE_LATEX = block(ChangedAddonModBlocks.DORMANT_WHITE_LATEX, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> DARK_LATEX_SPRAY = REGISTRY.register("dark_latex_spray", () -> new DarkLatexSprayItem());
	public static final RegistryObject<Item> WHITE_LATEX_SPRAY = REGISTRY.register("white_latex_spray", () -> new WhiteLatexSprayItem());
	public static final RegistryObject<Item> HAZMAT_SUIT_HELMET = REGISTRY.register("hazmat_suit_helmet", () -> new HazmatSuitItem.Helmet());
	public static final RegistryObject<Item> HAZMAT_SUIT_CHESTPLATE = REGISTRY.register("hazmat_suit_chestplate", () -> new HazmatSuitItem.Chestplate());
	public static final RegistryObject<Item> HAZMAT_SUIT_LEGGINGS = REGISTRY.register("hazmat_suit_leggings", () -> new HazmatSuitItem.Leggings());
	public static final RegistryObject<Item> HAZMAT_SUIT_BOOTS = REGISTRY.register("hazmat_suit_boots", () -> new HazmatSuitItem.Boots());
	public static final RegistryObject<Item> SNEP_PLUSH = block(ChangedAddonModBlocks.SNEP_PLUSH, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> WOLF_PLUSH = block(ChangedAddonModBlocks.WOLF_PLUSH, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> BIOMASS = REGISTRY.register("biomass", () -> new BiomassItem());
	public static final RegistryObject<Item> CONTAINMENT_CONTAINER = block(ChangedAddonModBlocks.CONTAINMENT_CONTAINER, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> ADVANCED_UNIFUSER = block(ChangedAddonModBlocks.ADVANCED_UNIFUSER, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> ADVANCED_CATALYZER = block(ChangedAddonModBlocks.ADVANCED_CATALYZER, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> REINFORCED_WALL = block(ChangedAddonModBlocks.REINFORCED_WALL, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> REINFORCED_WALL_SILVER_STRIPED = block(ChangedAddonModBlocks.REINFORCED_WALL_SILVER_STRIPED, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> REINFORCED_WALL_SILVER_TILED = block(ChangedAddonModBlocks.REINFORCED_WALL_SILVER_TILED, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> REINFORCED_WALL_CAUTION = block(ChangedAddonModBlocks.REINFORCED_WALL_CAUTION, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> REINFORCED_CROSS_BLOCK = block(ChangedAddonModBlocks.REINFORCED_CROSS_BLOCK, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> BLUE_WOLF_CRYSTAL_BLOCK = block(ChangedAddonModBlocks.BLUE_WOLF_CRYSTAL_BLOCK, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> ORANGE_WOLF_CRYSTAL_BLOCK = block(ChangedAddonModBlocks.ORANGE_WOLF_CRYSTAL_BLOCK, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> YELLOW_WOLF_CRYSTAL_BLOCK = block(ChangedAddonModBlocks.YELLOW_WOLF_CRYSTAL_BLOCK, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> WHITE_WOLF_CRYSTAL_BLOCK = block(ChangedAddonModBlocks.WHITE_WOLF_CRYSTAL_BLOCK, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> LUMINAR_CRYSTAL_BLOCK = block(ChangedAddonModBlocks.LUMINAR_CRYSTAL_BLOCK, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> LUMINAR_CRYSTAL_SMALL = block(ChangedAddonModBlocks.LUMINAR_CRYSTAL_SMALL, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> YELLOW_WOLF_CRYSTAL_SMALL = block(ChangedAddonModBlocks.YELLOW_WOLF_CRYSTAL_SMALL, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> ORANGE_WOLF_CRYSTAL_SMALL = block(ChangedAddonModBlocks.ORANGE_WOLF_CRYSTAL_SMALL, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> BLUE_WOLF_CRYSTAL_SMALL = block(ChangedAddonModBlocks.BLUE_WOLF_CRYSTAL_SMALL, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> WHITE_WOLF_CRYSTAL_SMALL = block(ChangedAddonModBlocks.WHITE_WOLF_CRYSTAL_SMALL, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> BLUE_WOLF_CRYSTAL_FRAGMENT = REGISTRY.register("blue_wolf_crystal_fragment", () -> new BlueWolfCrystalFragmentItem());
	public static final RegistryObject<Item> ORANGE_WOLF_CRYSTAL_FRAGMENT = REGISTRY.register("orange_wolf_crystal_fragment", () -> new OrangeWolfCrystalFragmentItem());
	public static final RegistryObject<Item> YELLOW_WOLF_CRYSTAL_FRAGMENT = REGISTRY.register("yellow_wolf_crystal_fragment", () -> new YellowWolfCrystalFragmentItem());
	public static final RegistryObject<Item> WHITE_WOLF_CRYSTAL_FRAGMENT = REGISTRY.register("white_wolf_crystal_fragment", () -> new WhiteWolfCrystalFragmentItem());
	public static final RegistryObject<Item> LUMINAR_CRYSTAL_SHARD = REGISTRY.register("luminar_crystal_shard", () -> new LuminarCrystalShardItem());
	public static final RegistryObject<Item> ELECTRIC_KATANA = REGISTRY.register("electric_katana", () -> new ElectricKatanaItem());
	public static final RegistryObject<Item> ELECTRIC_KATANA_RED = REGISTRY.register("electric_katana_red", () -> new ElectricKatanaRedItem());
	public static final RegistryObject<Item> PAINITE_SWORD = REGISTRY.register("painite_sword", () -> new PainiteSwordItem());
	public static final RegistryObject<Item> PAINITE_PICKAXE = REGISTRY.register("painite_pickaxe", () -> new PainitePickaxeItem());
	public static final RegistryObject<Item> PAINITE_AXE = REGISTRY.register("painite_axe", () -> new PainiteAxeItem());
	public static final RegistryObject<Item> PAINITE_SHOVEL = REGISTRY.register("painite_shovel", () -> new PainiteShovelItem());
	public static final RegistryObject<Item> PAINITE_HOE = REGISTRY.register("painite_hoe", () -> new PainiteHoeItem());
	public static final RegistryObject<Item> PAINITE_ARMOR_HELMET = REGISTRY.register("painite_armor_helmet", () -> new PainiteArmorItem.Helmet());
	public static final RegistryObject<Item> PAINITE_ARMOR_CHESTPLATE = REGISTRY.register("painite_armor_chestplate", () -> new PainiteArmorItem.Chestplate());
	public static final RegistryObject<Item> PAINITE_ARMOR_LEGGINGS = REGISTRY.register("painite_armor_leggings", () -> new PainiteArmorItem.Leggings());
	public static final RegistryObject<Item> PAINITE_ARMOR_BOOTS = REGISTRY.register("painite_armor_boots", () -> new PainiteArmorItem.Boots());
	public static final RegistryObject<Item> GENERATOR = block(ChangedAddonModBlocks.GENERATOR, null);
	public static final RegistryObject<Item> EXPERIMENT_009_SPAWN_EGG = REGISTRY.register("experiment_009_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.EXPERIMENT_009, -1, -2697514, new Item.Properties().tab(null)));
	public static final RegistryObject<Item> EXPERIMENT_009_PHASE_2_SPAWN_EGG = REGISTRY.register("experiment_009_phase_2_spawn_egg",
			() -> new ForgeSpawnEggItem(ChangedAddonModEntities.EXPERIMENT_009_PHASE_2, -1, -16724788, new Item.Properties().tab(null)));
	public static final RegistryObject<Item> CATLYZERBLOCK_ILLUSTRATIVE_ITEM = REGISTRY.register("catlyzerblock_illustrative_item", () -> new CatlyzerblockIllustrativeItemItem());
	public static final RegistryObject<Item> UNIFUSERBLOCK_ILLUSTRATIVE_ITEM = REGISTRY.register("unifuserblock_illustrative_item", () -> new UnifuserblockIllustrativeItemItem());
	public static final RegistryObject<Item> AMMONIA_PARTICLES_JEI_ILLUSTRATIVE = REGISTRY.register("ammonia_particles_jei_illustrative", () -> new AmmoniaParticlesJeiIllustrativeItem());
	public static final RegistryObject<Item> ERIK_SPAWN_EGG = REGISTRY.register("erik_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.ERIK, -1, -1, new Item.Properties().tab(null)));
	public static final RegistryObject<Item> FOXTA_CAN = block(ChangedAddonModBlocks.FOXTA_CAN, null);
	public static final RegistryObject<Item> SNEPSI_CAN = block(ChangedAddonModBlocks.SNEPSI_CAN, null);
	public static final RegistryObject<Item> SNEP_ICON = REGISTRY.register("snep_icon", () -> new SnepIconItem());
	public static final RegistryObject<Item> FRIENDLY_GOEY_ICON = REGISTRY.register("friendly_goey_icon", () -> new FriendlyGoeyIconItem());
	public static final RegistryObject<Item> PAT_ICON = REGISTRY.register("pat_icon", () -> new PatIconItem());
	public static final RegistryObject<Item> GOO_CORE = block(ChangedAddonModBlocks.GOO_CORE, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> THE_DECIMATOR = REGISTRY.register("the_decimator", () -> new TheDecimatorItem());

	@SubscribeEvent
	public static void clientLoad(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			ItemProperties.register(LAETHIN.get(), new ResourceLocation("changed_addon:laethin_type"), (itemStackToRender, clientWorld, entity, itemEntityId) -> (float) LaethinPropertyValueProviderProcedure.execute(itemStackToRender));
			ItemProperties.register(LAETHIN_SYRINGE.get(), new ResourceLocation("changed_addon:laethin_syringe_type"),
					(itemStackToRender, clientWorld, entity, itemEntityId) -> (float) LaethinPropertyValueProviderProcedure.execute(itemStackToRender));
			ItemProperties.register(TRANSFUR_TOTEM.get(), new ResourceLocation("changed_addon:transfur_totem_glowtick"), (itemStackToRender, clientWorld, entity, itemEntityId) -> (float) TransfurTotemItemInInventoryProcedure.execute(entity));
		});
	}

	private static RegistryObject<Item> block(RegistryObject<Block> block, CreativeModeTab tab) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
	}
}
