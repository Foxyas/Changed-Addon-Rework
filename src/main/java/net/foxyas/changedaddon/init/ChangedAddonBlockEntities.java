package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.block.advanced.TimedKeypadBlockEntity;
import net.foxyas.changedaddon.block.entity.*;
import net.foxyas.changedaddon.client.renderer.blockEntitys.ContainmentContainerRenderer;
import net.foxyas.changedaddon.client.renderer.blockEntitys.SnepPlushBlockEntityRenderer;
import net.foxyas.changedaddon.client.renderer.blockEntitys.TimedKeypadBlockEntityRenderer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, ChangedAddonMod.MODID);
    public static final RegistryObject<BlockEntityType<TimedKeypadBlockEntity>> TIMED_KEYPAD_BLOCK_ENTITY = REGISTRY.register("timed_keypad_block_entity", () -> BlockEntityType.Builder.of(TimedKeypadBlockEntity::new, ChangedAddonBlocks.TIMED_KEYPAD.get()).build(null));    public static final RegistryObject<BlockEntityType<?>> CATALYZER = register("catalyzer", ChangedAddonBlocks.CATALYZER, CatalyzerBlockEntity::new);

    private static RegistryObject<BlockEntityType<?>> register(String registryname, RegistryObject<Block> block, BlockEntityType.BlockEntitySupplier<?> supplier) {
        return REGISTRY.register(registryname, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
    }    public static final RegistryObject<BlockEntityType<?>> UNIFUSER = register("unifuser", ChangedAddonBlocks.UNIFUSER, UnifuserBlockEntity::new);

    @SubscribeEvent
    public static void registerBlockEntitiesRender(EntityRenderersEvent.RegisterRenderers event) {
        // Registro do renderizador de BlockEntity
        event.registerBlockEntityRenderer(SNEP_PLUSH.get(), SnepPlushBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(CONTAINMENT_CONTAINER.get(), ContainmentContainerRenderer::new);
        event.registerBlockEntityRenderer(TIMED_KEYPAD_BLOCK_ENTITY.get(), TimedKeypadBlockEntityRenderer::new);
    }    public static final RegistryObject<BlockEntityType<?>> DARKLATEXPUDDLE = register("darklatexpuddle", ChangedAddonBlocks.DARKLATEXPUDDLE, DarklatexpuddleBlockEntity::new);
    public static final RegistryObject<BlockEntityType<?>> SIGNAL_BLOCK = register("signal_block", ChangedAddonBlocks.SIGNAL_BLOCK, SignalBlockBlockEntity::new);
    public static final RegistryObject<BlockEntityType<?>> INFORMANTBLOCK = register("informantblock", ChangedAddonBlocks.INFORMANTBLOCK, InformantblockBlockEntity::new);
    public static final RegistryObject<BlockEntityType<?>> WOLF_PLUSH = register("wolf_plush", ChangedAddonBlocks.WOLF_PLUSH, WolfPlushBlockEntity::new);
    public static final RegistryObject<BlockEntityType<?>> ADVANCED_UNIFUSER = register("advanced_unifuser", ChangedAddonBlocks.ADVANCED_UNIFUSER, AdvancedUnifuserBlockEntity::new);
    public static final RegistryObject<BlockEntityType<?>> ADVANCED_CATALYZER = register("advanced_catalyzer", ChangedAddonBlocks.ADVANCED_CATALYZER, AdvancedCatalyzerBlockEntity::new);
    public static final RegistryObject<BlockEntityType<?>> GENERATOR = register("generator", ChangedAddonBlocks.GENERATOR, GeneratorBlockEntity::new);
    public static final RegistryObject<BlockEntityType<SnepPlushBlockEntity>> SNEP_PLUSH = REGISTRY.register("snep_plushe", () -> BlockEntityType.Builder.of(SnepPlushBlockEntity::new, ChangedAddonBlocks.SNEP_PLUSH.get()).build(null));

    public static final RegistryObject<BlockEntityType<ContainmentContainerBlockEntity>> CONTAINMENT_CONTAINER = REGISTRY.register("containment_container_block_entity", () -> BlockEntityType.Builder.of(ContainmentContainerBlockEntity::new, ChangedAddonBlocks.CONTAINMENT_CONTAINER.get()).build(null));




}
