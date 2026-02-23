package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.block.advanced.TimedKeypadBlockEntity;
import net.foxyas.changedaddon.block.debug.entity.StructureSpawnerBlockEntity;
import net.foxyas.changedaddon.block.entity.*;
import net.foxyas.changedaddon.client.renderer.blockEntitys.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ChangedAddonMod.MODID);

    //Non generic Ones
    public static final RegistryObject<BlockEntityType<TimedKeypadBlockEntity>> TIMED_KEYPAD_BLOCK_ENTITY = REGISTRY.register("timed_keypad_block_entity", () -> BlockEntityType.Builder.of(TimedKeypadBlockEntity::new, ChangedAddonBlocks.TIMED_KEYPAD.get()).build(null));

    private static RegistryObject<BlockEntityType<?>> registerGeneric(String registryName, RegistryObject<? extends Block> block, BlockEntityType.BlockEntitySupplier<?> supplier) {
        return REGISTRY.register(registryName, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
    }    public static final RegistryObject<BlockEntityType<?>> CATALYZER = registerGeneric("catalyzer_block_entity", ChangedAddonBlocks.CATALYZER, CatalyzerBlockEntity::new);

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String registryName, RegistryObject<? extends Block> block, BlockEntityType.BlockEntitySupplier<T> supplier) {
        return REGISTRY.register(registryName, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
    }    public static final RegistryObject<BlockEntityType<?>> UNIFUSER = registerGeneric("unifuser_block_entity", ChangedAddonBlocks.UNIFUSER, UnifuserBlockEntity::new);

    @SubscribeEvent
    public static void registerBlockEntitiesRender(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(CONTAINMENT_CONTAINER.get(), ContainmentContainerRenderer::new);
        event.registerBlockEntityRenderer(SNEP_PLUSHY.get(), SnepPlushyBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(CONTAINMENT_CONTAINER.get(), ContainmentContainerRenderer::new);
        event.registerBlockEntityRenderer(TIMED_KEYPAD_BLOCK_ENTITY.get(), TimedKeypadBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(INFORMANT_BLOCK.get(), InformantBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(SIGNAL_BLOCK.get(), SignalBlockEntityRenderer::new);
    }    public static final RegistryObject<BlockEntityType<?>> DARK_LATEX_PUDDLE = registerGeneric("dark_latex_puddle_block_entity", ChangedAddonBlocks.DARK_LATEX_PUDDLE, DarkLatexPuddleBlockEntity::new);
    public static final RegistryObject<BlockEntityType<SignalBlockEntity>> SIGNAL_BLOCK = register("signal_block_block_entity", ChangedAddonBlocks.SIGNAL_BLOCK, SignalBlockEntity::new);
    public static final RegistryObject<BlockEntityType<?>> ADVANCED_UNIFUSER = registerGeneric("advanced_unifuser_block_entity", ChangedAddonBlocks.ADVANCED_UNIFUSER, AdvancedUnifuserBlockEntity::new);
    public static final RegistryObject<BlockEntityType<?>> ADVANCED_CATALYZER = registerGeneric("advanced_catalyzer_block_entity", ChangedAddonBlocks.ADVANCED_CATALYZER, AdvancedCatalyzerBlockEntity::new);
    public static final RegistryObject<BlockEntityType<?>> GENERATOR = registerGeneric("generator_block_entity", ChangedAddonBlocks.GENERATOR, GeneratorBlockEntity::new);

    public static final RegistryObject<BlockEntityType<SnepPlushyBlockEntity>> SNEP_PLUSHY = REGISTRY.register("snep_plushy_block_entity", () -> BlockEntityType.Builder.of(SnepPlushyBlockEntity::new, ChangedAddonBlocks.SNEP_PLUSHY.get()).build(null));
    public static final RegistryObject<BlockEntityType<WolfPlushyBlockEntity>> WOLF_PLUSHY = REGISTRY.register("wolf_plushy_block_entity", () -> BlockEntityType.Builder.of(WolfPlushyBlockEntity::new, ChangedAddonBlocks.WOLF_PLUSHY.get()).build(null));
    public static final RegistryObject<BlockEntityType<DarkLatexWolfPlushyBlockEntity>> DARK_LATEX_WOLF_PLUSHY = REGISTRY.register("dark_latex_wolf_plushy_block_entity", () -> BlockEntityType.Builder.of(DarkLatexWolfPlushyBlockEntity::new, ChangedAddonBlocks.DARK_LATEX_WOLF_PLUSHY.get()).build(null));

    public static final RegistryObject<BlockEntityType<InformantBlockEntity>> INFORMANT_BLOCK = REGISTRY.register("informant_block_block_entity", () -> BlockEntityType.Builder.of(InformantBlockEntity::new, ChangedAddonBlocks.INFORMANT_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<ContainmentContainerBlockEntity>> CONTAINMENT_CONTAINER = REGISTRY.register("containment_container_block_entity", () -> BlockEntityType.Builder.of(ContainmentContainerBlockEntity::new, ChangedAddonBlocks.CONTAINMENT_CONTAINER.get()).build(null));
    public static final RegistryObject<BlockEntityType<StructureSpawnerBlockEntity>> STRUCTURE_SPAWNER = REGISTRY.register("structure_spawner", () -> BlockEntityType.Builder.of(StructureSpawnerBlockEntity::new, ChangedAddonBlocks.STRUCTURE_SPAWNER.get()).build(null));


    //Stop breaking the lines on this code







}
