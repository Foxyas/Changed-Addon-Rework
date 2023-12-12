package net.foxyas.changedaddon;


import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import net.foxyas.changedaddon.init.ChangedAddonModItems;
import net.foxyas.changedaddon.init.ChangedAddonModTabs;
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

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonRegisters extends ChangedAddonModItems {
	public static final RegistryObject<Item> LATEX_SNOW_FOX_SPAWN_EGG = REGISTRY.register("latex_snow_fox_spawn_egg",
			() -> new ForgeSpawnEggItem(ChangedAddonModEntities.LATEX_SNOW_FOX, 0xFFFFFFF,	0xffb6b9b9, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));
	public static final RegistryObject<Item> LATEX_SNOW_FOX_FEMALE_SPAWN_EGG = REGISTRY.register("latex_snow_fox_female_spawn_egg",
			() -> new ForgeSpawnEggItem(ChangedAddonModEntities.LATEX_SNOW_FOX_FEMALE, 0xFFFFFFF, 0xffb6b9b9, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));
}

