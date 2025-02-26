
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
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.block.Block;

import net.foxyas.changedaddon.block.YellowWolfCrystalSmallBlock;
import net.foxyas.changedaddon.block.YellowWolfCrystalBlockBlock;
import net.foxyas.changedaddon.block.WolfPlushBlock;
import net.foxyas.changedaddon.block.WhiteWolfCrystalSmallBlock;
import net.foxyas.changedaddon.block.WhiteWolfCrystalBlockBlock;
import net.foxyas.changedaddon.block.UnifuserBlock;
import net.foxyas.changedaddon.block.SnepsiCanBlock;
import net.foxyas.changedaddon.block.SnepPlushBlock;
import net.foxyas.changedaddon.block.SignalBlockBlock;
import net.foxyas.changedaddon.block.ReinforcedwallsilvertiledBlock;
import net.foxyas.changedaddon.block.ReinforcedWallCautionBlock;
import net.foxyas.changedaddon.block.ReinforcedWallBlock;
import net.foxyas.changedaddon.block.ReinforcedSilverStripedWallBlock;
import net.foxyas.changedaddon.block.ReinforcedCrossBlock;
import net.foxyas.changedaddon.block.PainiteOreBlock;
import net.foxyas.changedaddon.block.PainiteBlockBlock;
import net.foxyas.changedaddon.block.OrangeWolfCrystalSmallBlock;
import net.foxyas.changedaddon.block.OrangeWolfCrystalBlockBlock;
import net.foxyas.changedaddon.block.LuminarCrystalSmallBlock;
import net.foxyas.changedaddon.block.LuminarCrystalBlockBlock;
import net.foxyas.changedaddon.block.LitixCamoniaFluidBlock;
import net.foxyas.changedaddon.block.LatexInsulatorBlock;
import net.foxyas.changedaddon.block.IridiumoreBlock;
import net.foxyas.changedaddon.block.IridiumBlockBlock;
import net.foxyas.changedaddon.block.InformantblockBlock;
import net.foxyas.changedaddon.block.GeneratorBlock;
import net.foxyas.changedaddon.block.FoxtaCanBlock;
import net.foxyas.changedaddon.block.DormantWhiteLatexBlock;
import net.foxyas.changedaddon.block.DormantDarkLatexBlock;
import net.foxyas.changedaddon.block.DarklatexpuddleBlock;
import net.foxyas.changedaddon.block.ContainmentContainerBlock;
import net.foxyas.changedaddon.block.CatlyzerBlock;
import net.foxyas.changedaddon.block.BlueWolfCrystalSmallBlock;
import net.foxyas.changedaddon.block.BlueWolfCrystalBlockBlock;
import net.foxyas.changedaddon.block.AdvancedUnifuserBlock;
import net.foxyas.changedaddon.block.AdvancedCatalyzerBlock;
import net.foxyas.changedaddon.ChangedAddonMod;

public class ChangedAddonModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, ChangedAddonMod.MODID);
	public static final RegistryObject<Block> LATEX_INSULATOR = REGISTRY.register("latex_insulator", () -> new LatexInsulatorBlock());
	public static final RegistryObject<Block> IRIDIUM_ORE = REGISTRY.register("iridium_ore", () -> new IridiumoreBlock());
	public static final RegistryObject<Block> IRIDIUM_BLOCK = REGISTRY.register("iridium_block", () -> new IridiumBlockBlock());
	public static final RegistryObject<Block> PAINITE_ORE = REGISTRY.register("painite_ore", () -> new PainiteOreBlock());
	public static final RegistryObject<Block> PAINITE_BLOCK = REGISTRY.register("painite_block", () -> new PainiteBlockBlock());
	public static final RegistryObject<Block> LITIX_CAMONIA_FLUID = REGISTRY.register("litix_camonia_fluid", () -> new LitixCamoniaFluidBlock());
	public static final RegistryObject<Block> CATLYZER = REGISTRY.register("catlyzer", () -> new CatlyzerBlock());
	public static final RegistryObject<Block> UNIFUSER = REGISTRY.register("unifuser", () -> new UnifuserBlock());
	public static final RegistryObject<Block> DARKLATEXPUDDLE = REGISTRY.register("darklatexpuddle", () -> new DarklatexpuddleBlock());
	public static final RegistryObject<Block> SIGNAL_BLOCK = REGISTRY.register("signal_block", () -> new SignalBlockBlock());
	public static final RegistryObject<Block> INFORMANTBLOCK = REGISTRY.register("informantblock", () -> new InformantblockBlock());
	public static final RegistryObject<Block> DORMANT_DARK_LATEX = REGISTRY.register("dormant_dark_latex", () -> new DormantDarkLatexBlock());
	public static final RegistryObject<Block> DORMANT_WHITE_LATEX = REGISTRY.register("dormant_white_latex", () -> new DormantWhiteLatexBlock());
	public static final RegistryObject<Block> SNEP_PLUSH = REGISTRY.register("snep_plush", () -> new SnepPlushBlock());
	public static final RegistryObject<Block> WOLF_PLUSH = REGISTRY.register("wolf_plush", () -> new WolfPlushBlock());
	public static final RegistryObject<Block> CONTAINMENT_CONTAINER = REGISTRY.register("containment_container", () -> new ContainmentContainerBlock());
	public static final RegistryObject<Block> ADVANCED_UNIFUSER = REGISTRY.register("advanced_unifuser", () -> new AdvancedUnifuserBlock());
	public static final RegistryObject<Block> ADVANCED_CATALYZER = REGISTRY.register("advanced_catalyzer", () -> new AdvancedCatalyzerBlock());
	public static final RegistryObject<Block> REINFORCED_WALL = REGISTRY.register("reinforced_wall", () -> new ReinforcedWallBlock());
	public static final RegistryObject<Block> REINFORCED_WALL_SILVER_STRIPED = REGISTRY.register("reinforced_wall_silver_striped", () -> new ReinforcedSilverStripedWallBlock());
	public static final RegistryObject<Block> REINFORCED_WALL_SILVER_TILED = REGISTRY.register("reinforced_wall_silver_tiled", () -> new ReinforcedwallsilvertiledBlock());
	public static final RegistryObject<Block> REINFORCED_WALL_CAUTION = REGISTRY.register("reinforced_wall_caution", () -> new ReinforcedWallCautionBlock());
	public static final RegistryObject<Block> REINFORCED_CROSS_BLOCK = REGISTRY.register("reinforced_cross_block", () -> new ReinforcedCrossBlock());
	public static final RegistryObject<Block> BLUE_WOLF_CRYSTAL_BLOCK = REGISTRY.register("blue_wolf_crystal_block", () -> new BlueWolfCrystalBlockBlock());
	public static final RegistryObject<Block> ORANGE_WOLF_CRYSTAL_BLOCK = REGISTRY.register("orange_wolf_crystal_block", () -> new OrangeWolfCrystalBlockBlock());
	public static final RegistryObject<Block> YELLOW_WOLF_CRYSTAL_BLOCK = REGISTRY.register("yellow_wolf_crystal_block", () -> new YellowWolfCrystalBlockBlock());
	public static final RegistryObject<Block> WHITE_WOLF_CRYSTAL_BLOCK = REGISTRY.register("white_wolf_crystal_block", () -> new WhiteWolfCrystalBlockBlock());
	public static final RegistryObject<Block> YELLOW_WOLF_CRYSTAL_SMALL = REGISTRY.register("yellow_wolf_crystal_small", () -> new YellowWolfCrystalSmallBlock());
	public static final RegistryObject<Block> ORANGE_WOLF_CRYSTAL_SMALL = REGISTRY.register("orange_wolf_crystal_small", () -> new OrangeWolfCrystalSmallBlock());
	public static final RegistryObject<Block> BLUE_WOLF_CRYSTAL_SMALL = REGISTRY.register("blue_wolf_crystal_small", () -> new BlueWolfCrystalSmallBlock());
	public static final RegistryObject<Block> WHITE_WOLF_CRYSTAL_SMALL = REGISTRY.register("white_wolf_crystal_small", () -> new WhiteWolfCrystalSmallBlock());
	public static final RegistryObject<Block> GENERATOR = REGISTRY.register("generator", () -> new GeneratorBlock());
	public static final RegistryObject<Block> FOXTA_CAN = REGISTRY.register("foxta_can", () -> new FoxtaCanBlock());
	public static final RegistryObject<Block> SNEPSI_CAN = REGISTRY.register("snepsi_can", () -> new SnepsiCanBlock());
	public static final RegistryObject<Block> LUMINAR_CRYSTAL_BLOCK = REGISTRY.register("luminar_crystal_block", () -> new LuminarCrystalBlockBlock());
	public static final RegistryObject<Block> LUMINAR_CRYSTAL_SMALL = REGISTRY.register("luminar_crystal_small", () -> new LuminarCrystalSmallBlock());

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ClientSideHandler {
		@SubscribeEvent
		public static void clientSetup(FMLClientSetupEvent event) {
			LatexInsulatorBlock.registerRenderLayer();
			DarklatexpuddleBlock.registerRenderLayer();
			SnepPlushBlock.registerRenderLayer();
			WolfPlushBlock.registerRenderLayer();
			ContainmentContainerBlock.registerRenderLayer();
			YellowWolfCrystalSmallBlock.registerRenderLayer();
			OrangeWolfCrystalSmallBlock.registerRenderLayer();
			BlueWolfCrystalSmallBlock.registerRenderLayer();
			WhiteWolfCrystalSmallBlock.registerRenderLayer();
			FoxtaCanBlock.registerRenderLayer();
			SnepsiCanBlock.registerRenderLayer();
			LuminarCrystalSmallBlock.registerRenderLayer();
		}
	}
}
