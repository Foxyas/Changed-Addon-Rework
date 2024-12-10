
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.foxyas.changedaddon.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.Block;

import net.foxyas.changedaddon.block.entity.WolfPlushBlockEntity;
import net.foxyas.changedaddon.block.entity.UnifuserBlockEntity;
import net.foxyas.changedaddon.block.entity.SnepPlushBlockEntity;
import net.foxyas.changedaddon.block.entity.SignalBlockBlockEntity;
import net.foxyas.changedaddon.block.entity.InformantblockBlockEntity;
import net.foxyas.changedaddon.block.entity.GeneratorBlockEntity;
import net.foxyas.changedaddon.block.entity.DarklatexpuddleBlockEntity;
import net.foxyas.changedaddon.block.entity.ContainmentContainerBlockEntity;
import net.foxyas.changedaddon.block.entity.CatlyzerBlockEntity;
import net.foxyas.changedaddon.block.entity.AdvancedUnifuserBlockEntity;
import net.foxyas.changedaddon.block.entity.AdvancedCatalyzerBlockEntity;
import net.foxyas.changedaddon.ChangedAddonMod;

public class ChangedAddonModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, ChangedAddonMod.MODID);
	public static final RegistryObject<BlockEntityType<?>> CATLYZER = register("catlyzer", ChangedAddonModBlocks.CATLYZER, CatlyzerBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> UNIFUSER = register("unifuser", ChangedAddonModBlocks.UNIFUSER, UnifuserBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> DARKLATEXPUDDLE = register("darklatexpuddle", ChangedAddonModBlocks.DARKLATEXPUDDLE, DarklatexpuddleBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> SIGNAL_BLOCK = register("signal_block", ChangedAddonModBlocks.SIGNAL_BLOCK, SignalBlockBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> INFORMANTBLOCK = register("informantblock", ChangedAddonModBlocks.INFORMANTBLOCK, InformantblockBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> SNEP_PLUSH = register("snep_plush", ChangedAddonModBlocks.SNEP_PLUSH, SnepPlushBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> WOLF_PLUSH = register("wolf_plush", ChangedAddonModBlocks.WOLF_PLUSH, WolfPlushBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> CONTAINMENT_CONTAINER = register("containment_container", ChangedAddonModBlocks.CONTAINMENT_CONTAINER, ContainmentContainerBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> ADVANCED_UNIFUSER = register("advanced_unifuser", ChangedAddonModBlocks.ADVANCED_UNIFUSER, AdvancedUnifuserBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> ADVANCED_CATALYZER = register("advanced_catalyzer", ChangedAddonModBlocks.ADVANCED_CATALYZER, AdvancedCatalyzerBlockEntity::new);
	public static final RegistryObject<BlockEntityType<?>> GENERATOR = register("generator", ChangedAddonModBlocks.GENERATOR, GeneratorBlockEntity::new);

	private static RegistryObject<BlockEntityType<?>> register(String registryname, RegistryObject<Block> block, BlockEntityType.BlockEntitySupplier<?> supplier) {
		return REGISTRY.register(registryname, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
	}
}
