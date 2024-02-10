
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
import net.foxyas.changedaddon.item.UnlatexbaseItem;
import net.foxyas.changedaddon.item.UnifuserblockIllustrativeItemItem;
import net.foxyas.changedaddon.item.TransfurTotemItem;
import net.foxyas.changedaddon.item.SyringewithlitixcammoniaItem;
import net.foxyas.changedaddon.item.SyringeItem;
import net.foxyas.changedaddon.item.SpawneggoffoxyasItem;
import net.foxyas.changedaddon.item.PotwithcamoniaItem;
import net.foxyas.changedaddon.item.PainiteSwordItem;
import net.foxyas.changedaddon.item.PainiteShovelItem;
import net.foxyas.changedaddon.item.PainitePickaxeItem;
import net.foxyas.changedaddon.item.PainiteItem;
import net.foxyas.changedaddon.item.PainiteHoeItem;
import net.foxyas.changedaddon.item.PainiteAxeItem;
import net.foxyas.changedaddon.item.PainiteArmorItem;
import net.foxyas.changedaddon.item.OrangejuiceItem;
import net.foxyas.changedaddon.item.LunarroseItem;
import net.foxyas.changedaddon.item.LitixCamoniaSprayItem;
import net.foxyas.changedaddon.item.LitixCamoniaItem;
import net.foxyas.changedaddon.item.LitixCamoniaFluidItem;
import net.foxyas.changedaddon.item.LaethinSyringeItem;
import net.foxyas.changedaddon.item.LaethinItem;
import net.foxyas.changedaddon.item.InpureammoniaItem;
import net.foxyas.changedaddon.item.HazardSuitItem;
import net.foxyas.changedaddon.item.Experiment009recordItem;
import net.foxyas.changedaddon.item.Experiment009dnaItem;
import net.foxyas.changedaddon.item.Experiment009SpawneggItem;
import net.foxyas.changedaddon.item.Experiment009Phase2RecordItem;
import net.foxyas.changedaddon.item.EmptySprayItem;
import net.foxyas.changedaddon.item.DevitemItem;
import net.foxyas.changedaddon.item.Devitem5Item;
import net.foxyas.changedaddon.item.Devitem3Item;
import net.foxyas.changedaddon.item.Devitem1Item;
import net.foxyas.changedaddon.item.DevItem4Item;
import net.foxyas.changedaddon.item.DevItem2Item;
import net.foxyas.changedaddon.item.DescontrolSyringeItem;
import net.foxyas.changedaddon.item.ChangedbookItem;
import net.foxyas.changedaddon.item.CatlyzerblockIllustrativeItemItem;
import net.foxyas.changedaddon.item.CatalyzeddnaItem;
import net.foxyas.changedaddon.item.AmmoniaparticleItem;
import net.foxyas.changedaddon.item.AmmoniaParticlesJeiIllustrativeItem;
import net.foxyas.changedaddon.item.AmmoniaItem;
import net.foxyas.changedaddon.ChangedAddonMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ChangedAddonModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ChangedAddonMod.MODID);
	public static final RegistryObject<Item> CHANGEDBOOK = REGISTRY.register("changedbook", () -> new ChangedbookItem());
	public static final RegistryObject<Item> UNLATEXBASE = REGISTRY.register("unlatexbase", () -> new UnlatexbaseItem());
	public static final RegistryObject<Item> LATEX_INSULATOR = block(ChangedAddonModBlocks.LATEX_INSULATOR, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> IMPUREAMMONIA = REGISTRY.register("impureammonia", () -> new InpureammoniaItem());
	public static final RegistryObject<Item> AMMONIAPARTICLE = REGISTRY.register("ammoniaparticle", () -> new AmmoniaparticleItem());
	public static final RegistryObject<Item> AMMONIA = REGISTRY.register("ammonia", () -> new AmmoniaItem());
	public static final RegistryObject<Item> LITIX_CAMONIA = REGISTRY.register("litix_camonia", () -> new LitixCamoniaItem());
	public static final RegistryObject<Item> LAETHIN = REGISTRY.register("laethin", () -> new LaethinItem());
	public static final RegistryObject<Item> SYRINGE = REGISTRY.register("syringe", () -> new SyringeItem());
	public static final RegistryObject<Item> CATALYZEDDNA = REGISTRY.register("catalyzeddna", () -> new CatalyzeddnaItem());
	public static final RegistryObject<Item> DIFFUSION_SYRINE = REGISTRY.register("diffusion_syrine", () -> new DescontrolSyringeItem());
	public static final RegistryObject<Item> SYRINGEWITHLITIXCAMMONIA = REGISTRY.register("syringewithlitixcammonia", () -> new SyringewithlitixcammoniaItem());
	public static final RegistryObject<Item> LAETHIN_SYRINGE = REGISTRY.register("laethin_syringe", () -> new LaethinSyringeItem());
	public static final RegistryObject<Item> POTWITHCAMONIA = REGISTRY.register("potwithcamonia", () -> new PotwithcamoniaItem());
	public static final RegistryObject<Item> ORANGEJUICE = REGISTRY.register("orangejuice", () -> new OrangejuiceItem());
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
	public static final RegistryObject<Item> EXPERIMENT_009DNA = REGISTRY.register("experiment_009dna", () -> new Experiment009dnaItem());
	public static final RegistryObject<Item> HAZARD_SUIT_HELMET = REGISTRY.register("hazard_suit_helmet", () -> new HazardSuitItem.Helmet());
	public static final RegistryObject<Item> HAZARD_SUIT_CHESTPLATE = REGISTRY.register("hazard_suit_chestplate", () -> new HazardSuitItem.Chestplate());
	public static final RegistryObject<Item> HAZARD_SUIT_LEGGINGS = REGISTRY.register("hazard_suit_leggings", () -> new HazardSuitItem.Leggings());
	public static final RegistryObject<Item> HAZARD_SUIT_BOOTS = REGISTRY.register("hazard_suit_boots", () -> new HazardSuitItem.Boots());
	public static final RegistryObject<Item> DARKLATEXPUDDLE = block(ChangedAddonModBlocks.DARKLATEXPUDDLE, ChangedAddonModTabs.TAB_CHANGED_ADDON);
	public static final RegistryObject<Item> TRANSFUR_TOTEM = REGISTRY.register("transfur_totem", () -> new TransfurTotemItem());
	public static final RegistryObject<Item> LUNARROSE_HELMET = REGISTRY.register("lunarrose_helmet", () -> new LunarroseItem.Helmet());
	public static final RegistryObject<Item> DEVITEM = REGISTRY.register("devitem", () -> new DevitemItem());
	public static final RegistryObject<Item> PAINITE_PICKAXE = REGISTRY.register("painite_pickaxe", () -> new PainitePickaxeItem());
	public static final RegistryObject<Item> PAINITE_AXE = REGISTRY.register("painite_axe", () -> new PainiteAxeItem());
	public static final RegistryObject<Item> PAINITE_SWORD = REGISTRY.register("painite_sword", () -> new PainiteSwordItem());
	public static final RegistryObject<Item> PAINITE_SHOVEL = REGISTRY.register("painite_shovel", () -> new PainiteShovelItem());
	public static final RegistryObject<Item> PAINITE_ARMOR_HELMET = REGISTRY.register("painite_armor_helmet", () -> new PainiteArmorItem.Helmet());
	public static final RegistryObject<Item> PAINITE_ARMOR_CHESTPLATE = REGISTRY.register("painite_armor_chestplate", () -> new PainiteArmorItem.Chestplate());
	public static final RegistryObject<Item> PAINITE_ARMOR_LEGGINGS = REGISTRY.register("painite_armor_leggings", () -> new PainiteArmorItem.Leggings());
	public static final RegistryObject<Item> PAINITE_ARMOR_BOOTS = REGISTRY.register("painite_armor_boots", () -> new PainiteArmorItem.Boots());
	public static final RegistryObject<Item> GENERATOR = block(ChangedAddonModBlocks.GENERATOR, null);
	public static final RegistryObject<Item> DEV_ITEM_2 = REGISTRY.register("dev_item_2", () -> new DevItem2Item());
	public static final RegistryObject<Item> DEVITEM_3 = REGISTRY.register("devitem_3", () -> new Devitem3Item());
	public static final RegistryObject<Item> DEV_ITEM_4 = REGISTRY.register("dev_item_4", () -> new DevItem4Item());
	public static final RegistryObject<Item> EXPERIMENT_009_SPAWN_EGG = REGISTRY.register("experiment_009_spawn_egg", () -> new ForgeSpawnEggItem(ChangedAddonModEntities.EXPERIMENT_009, -1, -2697514, new Item.Properties().tab(null)));
	public static final RegistryObject<Item> EXPERIMENT_009_PHASE_2_SPAWN_EGG = REGISTRY.register("experiment_009_phase_2_spawn_egg",
			() -> new ForgeSpawnEggItem(ChangedAddonModEntities.EXPERIMENT_009_PHASE_2, -1, -16737895, new Item.Properties().tab(null)));
	public static final RegistryObject<Item> CATLYZERBLOCK_ILLUSTRATIVE_ITEM = REGISTRY.register("catlyzerblock_illustrative_item", () -> new CatlyzerblockIllustrativeItemItem());
	public static final RegistryObject<Item> UNIFUSERBLOCK_ILLUSTRATIVE_ITEM = REGISTRY.register("unifuserblock_illustrative_item", () -> new UnifuserblockIllustrativeItemItem());
	public static final RegistryObject<Item> AMMONIA_PARTICLES_JEI_ILLUSTRATIVE = REGISTRY.register("ammonia_particles_jei_illustrative", () -> new AmmoniaParticlesJeiIllustrativeItem());
	public static final RegistryObject<Item> PAINITE_HOE = REGISTRY.register("painite_hoe", () -> new PainiteHoeItem());
	public static final RegistryObject<Item> EXPERIMENT_009_SPAWNEGG = REGISTRY.register("experiment_009_spawnegg", () -> new Experiment009SpawneggItem());
	public static final RegistryObject<Item> DEVITEM_1 = REGISTRY.register("devitem_1", () -> new Devitem1Item());
	public static final RegistryObject<Item> DEVITEM_5 = REGISTRY.register("devitem_5", () -> new Devitem5Item());
	public static final RegistryObject<Item> EXPERIMENT_009_RECORD = REGISTRY.register("experiment_009_record", () -> new Experiment009recordItem());
	public static final RegistryObject<Item> EXPERIMENT_009_PHASE_2_RECORD = REGISTRY.register("experiment_009_phase_2_record", () -> new Experiment009Phase2RecordItem());

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
