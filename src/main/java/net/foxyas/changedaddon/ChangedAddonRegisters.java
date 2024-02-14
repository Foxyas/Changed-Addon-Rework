package net.foxyas.changedaddon;


import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import net.foxyas.changedaddon.init.ChangedAddonModItems;
import net.foxyas.changedaddon.init.ChangedAddonModTabs;
import net.ltxprogrammer.changed.world.features.structures.FacilityPieces;
import net.ltxprogrammer.changed.world.features.structures.facility.FacilityRoomPiece;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonRegisters extends ChangedAddonModItems {
	public static final RegistryObject<Item> LATEX_SNOW_FOX_SPAWN_EGG = REGISTRY.register("latex_snow_fox_spawn_egg",
			() -> new ForgeSpawnEggItem(ChangedAddonModEntities.LATEX_SNOW_FOX, 0xFFFFFFF,	0xffb6b9b9, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));
	public static final RegistryObject<Item> LATEX_SNOW_FOX_FEMALE_SPAWN_EGG = REGISTRY.register("latex_snow_fox_female_spawn_egg",
			() -> new ForgeSpawnEggItem(ChangedAddonModEntities.LATEX_SNOW_FOX_FEMALE, 0xFFFFFFF, 0xffb6b9b9, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));
	public static final RegistryObject<Item> DAZED_LATEX_SPAWN_EGG = REGISTRY.register("latex_dazed_spawn_egg",
			() -> new ForgeSpawnEggItem(ChangedAddonModEntities.DAZED, 0xFFFFFFF, 0xffCFCFCF, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON)));
}


@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
class ChangedAddonFacilityPieces extends FacilityPieces {
	static {
		FacilityPieces.ROOMS.register(new FacilityRoomPiece(new ResourceLocation("changed_addon:experiment_009_facility_piece"),  new ResourceLocation("changed_addon:chests/destroy_structure_experiment_009_loot")));
	}
}
