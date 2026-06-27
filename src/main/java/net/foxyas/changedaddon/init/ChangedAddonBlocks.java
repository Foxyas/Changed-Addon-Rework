package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.block.*;
import net.foxyas.changedaddon.block.MultifaceBlock;
import net.foxyas.changedaddon.block.advanced.HandScanner;
import net.foxyas.changedaddon.block.advanced.PawsScanner;
import net.foxyas.changedaddon.block.advanced.TimedKeypadBlock;
import net.foxyas.changedaddon.block.debug.StructureSpawnerBlock;
import net.foxyas.changedaddon.block.entity.LuminaraHangingSignEntity;
import net.foxyas.changedaddon.block.entity.LuminaraSignEntity;
import net.ltxprogrammer.changed.init.ChangedLatexTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class ChangedAddonBlocks {

    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, ChangedAddonMod.MODID);

    public static final RegistryObject<Block> LATEX_INSULATOR = REGISTRY.register("latex_insulator", LatexInsulatorBlock::new);
    public static final RegistryObject<Block> DEEPSLATE_IRIDIUM_ORE = REGISTRY.register("deepslate_iridium_ore", IridiumOreBlock::new);
    public static final RegistryObject<Block> IRIDIUM_BLOCK = REGISTRY.register("iridium_block", IridiumBlock::new);
    public static final RegistryObject<Block> DEEPSLATE_PAINITE_ORE = REGISTRY.register("deepslate_painite_ore", DeepslatePainiteOreBlock::new);
    public static final RegistryObject<Block> PAINITE_BLOCK = REGISTRY.register("painite_block", PainiteBlock::new);
    public static final RegistryObject<LiquidBlock> LITIX_CAMONIA_FLUID = REGISTRY.register("litix_camonia_fluid", LitixCamoniaFluidBlock::new);
    public static final RegistryObject<CatalyzerBlock> CATALYZER = REGISTRY.register("catalyzer", CatalyzerBlock::new);
    public static final RegistryObject<UnifuserBlock> UNIFUSER = REGISTRY.register("unifuser", UnifuserBlock::new);
    public static final RegistryObject<DarkLatexPuddleBlock> DARK_LATEX_PUDDLE = REGISTRY.register("dark_latex_puddle", DarkLatexPuddleBlock::new);
    public static final RegistryObject<Block> SIGNAL_BLOCK = REGISTRY.register("signal_block", SignalBlock::new);
    public static final RegistryObject<InformantBlock> INFORMANT_BLOCK = REGISTRY.register("informant_block", InformantBlock::new);
    public static final RegistryObject<Block> DORMANT_DARK_LATEX = REGISTRY.register("dormant_dark_latex", DormantDarkLatexBlock::new);
    public static final RegistryObject<Block> DORMANT_WHITE_LATEX = REGISTRY.register("dormant_white_latex", DormantWhiteLatexBlock::new);
    public static final RegistryObject<SnepPlushyBlock> SNEP_PLUSHY = REGISTRY.register("snep_plushy", SnepPlushyBlock::new);
    public static final RegistryObject<WolfPlushyBlock> WOLF_PLUSHY = REGISTRY.register("wolf_plushy", WolfPlushyBlock::new);
    public static final RegistryObject<DarkLatexWolfPlushyBlock> DARK_LATEX_WOLF_PLUSHY = REGISTRY.register("dark_latex_wolf_plushy", DarkLatexWolfPlushyBlock::new);
    public static final RegistryObject<Block> CONTAINMENT_CONTAINER = REGISTRY.register("containment_container", ContainmentContainerBlock::new);
    public static final RegistryObject<AdvancedUnifuserBlock> ADVANCED_UNIFUSER = REGISTRY.register("advanced_unifuser", AdvancedUnifuserBlock::new);
    public static final RegistryObject<AdvancedCatalyzerBlock> ADVANCED_CATALYZER = REGISTRY.register("advanced_catalyzer", AdvancedCatalyzerBlock::new);
    public static final RegistryObject<Block> REINFORCED_WALL = REGISTRY.register("reinforced_wall", ReinforcedWallBlock::new);
    public static final RegistryObject<Block> REINFORCED_WALL_SILVER_STRIPED = REGISTRY.register("reinforced_wall_silver_striped", ReinforcedSilverStripedWallBlock::new);
    public static final RegistryObject<Block> REINFORCED_WALL_SILVER_TILED = REGISTRY.register("reinforced_wall_silver_tiled", ReinforcedWallSilverTiledBlock::new);
    public static final RegistryObject<Block> REINFORCED_WALL_CAUTION = REGISTRY.register("reinforced_wall_caution", ReinforcedWallCautionBlock::new);
    public static final RegistryObject<Block> REINFORCED_CROSS_BLOCK = REGISTRY.register("reinforced_cross_block", ReinforcedCrossBlock::new);
    public static final RegistryObject<Block> WALL_WHITE_CRACKED = REGISTRY.register("wall_white_cracked", WallWhiteCrackedBlock::new);
    public static final RegistryObject<Block> BLUE_WOLF_CRYSTAL_BLOCK = REGISTRY.register("blue_wolf_crystal_block", BlueWolfCrystalBlockBlock::new);
    public static final RegistryObject<Block> ORANGE_WOLF_CRYSTAL_BLOCK = REGISTRY.register("orange_wolf_crystal_block", OrangeWolfCrystalBlockBlock::new);
    public static final RegistryObject<Block> YELLOW_WOLF_CRYSTAL_BLOCK = REGISTRY.register("yellow_wolf_crystal_block", YellowWolfCrystalBlockBlock::new);
    public static final RegistryObject<Block> WHITE_WOLF_CRYSTAL_BLOCK = REGISTRY.register("white_wolf_crystal_block", WhiteWolfCrystalBlockBlock::new);
    public static final RegistryObject<LuminarCrystalBlock> LUMINAR_CRYSTAL_BLOCK = REGISTRY.register("luminar_crystal_block", LuminarCrystalBlock::new);
    public static final RegistryObject<LuminarCrystalSmall> LUMINAR_CRYSTAL_SMALL = REGISTRY.register("luminar_crystal_small", LuminarCrystalSmall::new);
    public static final RegistryObject<LuminarCrystalLarge> LUMINAR_CRYSTAL_LARGE = REGISTRY.register("luminar_crystal_large", () -> new LuminarCrystalLarge(BlockBehaviour.Properties.of()));
    public static final RegistryObject<Block> YELLOW_WOLF_CRYSTAL_SMALL = REGISTRY.register("yellow_wolf_crystal_small", YellowWolfCrystalSmallBlock::new);
    public static final RegistryObject<Block> ORANGE_WOLF_CRYSTAL_SMALL = REGISTRY.register("orange_wolf_crystal_small", OrangeWolfCrystalSmallBlock::new);
    public static final RegistryObject<Block> BLUE_WOLF_CRYSTAL_SMALL = REGISTRY.register("blue_wolf_crystal_small", BlueWolfCrystalSmallBlock::new);
    public static final RegistryObject<Block> WHITE_WOLF_CRYSTAL_SMALL = REGISTRY.register("white_wolf_crystal_small", WhiteWolfCrystalSmallBlock::new);
    public static final RegistryObject<Block> GOO_CORE = REGISTRY.register("goo_core", GooCoreBlock::new);
    public static final RegistryObject<FoxtaCanBlock> FOXTA_CAN = REGISTRY.register("foxta_can", FoxtaCanBlock::new);
    public static final RegistryObject<SnepsiCanBlock> SNEPSI_CAN = REGISTRY.register("snepsi_can", SnepsiCanBlock::new);
    public static final RegistryObject<TimedKeypadBlock> TIMED_KEYPAD = REGISTRY.register("timed_keypad", TimedKeypadBlock::new);
    public static final RegistryObject<HandScanner> HAND_SCANNER = REGISTRY.register("hand_scanner", HandScanner::new);
    public static final RegistryObject<PawsScanner> PAWS_SCANNER = REGISTRY.register("paws_scanner", PawsScanner::new);
    public static final RegistryObject<LuminaraBloomFlowerBlock> LUMINARA_BLOOM = REGISTRY.register("luminara_bloom", LuminaraBloomFlowerBlock::new);
    public static final RegistryObject<PottedLuminaraBloomFlowerBlock> POTTED_LUMINARA_BLOOM = REGISTRY.register("potted_luminara_bloom", PottedLuminaraBloomFlowerBlock::new);
    public static final RegistryObject<LuminaraLogBlock> LUMINARA_LOG = REGISTRY.register("luminara_log", LuminaraLogBlock::new);
    public static final RegistryObject<StrippedLuminaraLogBlock> STRIPPED_LUMINARA_LOG = REGISTRY.register("stripped_luminara_log", StrippedLuminaraLogBlock::new);
    public static final RegistryObject<LuminaraWoodBlock> LUMINARA_WOOD = REGISTRY.register("luminara_wood", () -> new LuminaraWoodBlock(BlockBehaviour.Properties.copy(Blocks.CHERRY_WOOD)));
    public static final RegistryObject<StrippedLuminaraWoodBlock> STRIPPED_LUMINARA_WOOD = REGISTRY.register("stripped_luminara_wood", () -> new StrippedLuminaraWoodBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_CHERRY_WOOD)));

    public static final BlockSetType LUMINARA_BLOCK_SET_TYPE = BlockSetType.register(new BlockSetType(ChangedAddonMod.resourceLocString("luminara"),
            true,
            SoundType.CHERRY_WOOD,
            SoundEvents.CHERRY_WOOD_DOOR_CLOSE,
            SoundEvents.CHERRY_WOOD_DOOR_OPEN,
            SoundEvents.CHERRY_WOOD_TRAPDOOR_CLOSE,
            SoundEvents.CHERRY_WOOD_TRAPDOOR_OPEN,
            SoundEvents.CHERRY_WOOD_PRESSURE_PLATE_CLICK_OFF,
            SoundEvents.CHERRY_WOOD_PRESSURE_PLATE_CLICK_ON,
            SoundEvents.CHERRY_WOOD_BUTTON_CLICK_OFF,
            SoundEvents.CHERRY_WOOD_BUTTON_CLICK_ON)
    );
    public static final WoodType LUMINARA_WOOD_TYPE = WoodType.register(new WoodType(ChangedAddonMod.resourceLocString("luminara"),
            LUMINARA_BLOCK_SET_TYPE)
    );

    public static final BlockBehaviour.Properties LUMINARA_PLANK_PROPERTIES = BlockBehaviour.Properties.copy(Blocks.CHERRY_PLANKS);
    public static final RegistryObject<Block> LUMINARA_PLANKS = REGISTRY.register("luminara_planks", () -> new Block(LUMINARA_PLANK_PROPERTIES));
    public static final RegistryObject<StairBlock> LUMINARA_STAIRS = REGISTRY.register("luminara_stairs", () -> new StairBlock(() -> LUMINARA_PLANKS.get().defaultBlockState(), LUMINARA_PLANK_PROPERTIES));
    public static final RegistryObject<SlabBlock> LUMINARA_SLAB = REGISTRY.register("luminara_slab", () -> new SlabBlock(LUMINARA_PLANK_PROPERTIES));
    public static final RegistryObject<DoorBlock> LUMINARA_DOOR = REGISTRY.register("luminara_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.CHERRY_DOOR), BlockSetType.CHERRY));
    public static final RegistryObject<TrapDoorBlock> LUMINARA_TRAPDOOR = REGISTRY.register("luminara_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.CHERRY_TRAPDOOR), BlockSetType.CHERRY));
    public static final RegistryObject<FenceBlock> LUMINARA_FENCE = REGISTRY.register("luminara_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.CHERRY_FENCE)));
    public static final RegistryObject<FenceGateBlock> LUMINARA_FENCE_GATE = REGISTRY.register("luminara_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.CHERRY_FENCE_GATE), LUMINARA_WOOD_TYPE));
    public static final RegistryObject<StandingSignBlock> LUMINARA_SIGN = REGISTRY.register("luminara_sign", () -> new StandingSignBlock(BlockBehaviour.Properties.copy(Blocks.CHERRY_SIGN), LUMINARA_WOOD_TYPE) {
        @Override
        public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
            return new LuminaraSignEntity(pPos, pState);
        }
    });
    public static final RegistryObject<WallSignBlock> LUMINARA_WALL_SIGN = REGISTRY.register("luminara_wall_sign", () -> new WallSignBlock(BlockBehaviour.Properties.copy(Blocks.CHERRY_WALL_SIGN), LUMINARA_WOOD_TYPE) {
        @Override
        public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
            return new LuminaraSignEntity(pPos, pState);
        }
    });
    public static final RegistryObject<CeilingHangingSignBlock> LUMINARA_HANGING_SIGN = REGISTRY.register("luminara_hanging_sign", () -> new CeilingHangingSignBlock(BlockBehaviour.Properties.copy(Blocks.CHERRY_HANGING_SIGN), LUMINARA_WOOD_TYPE) {
        @Override
        public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
            return new LuminaraHangingSignEntity(pPos, pState);
        }
    });
    public static final RegistryObject<WallHangingSignBlock> LUMINARA_WALL_HANGING_SIGN = REGISTRY.register("luminara_wall_hanging_sign", () -> new WallHangingSignBlock(BlockBehaviour.Properties.copy(Blocks.CHERRY_WALL_HANGING_SIGN), LUMINARA_WOOD_TYPE) {
        @Override
        public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
            return new LuminaraHangingSignEntity(pPos, pState);
        }
    });
    public static final RegistryObject<ButtonBlock> LUMINARA_BUTTON = REGISTRY.register("luminara_button", () -> new ButtonBlock(BlockBehaviour.Properties.copy(Blocks.CHERRY_BUTTON), BlockSetType.CHERRY, 30, true));
    public static final RegistryObject<PressurePlateBlock> LUMINARA_PRESSURE_PLATE = REGISTRY.register("luminara_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.CHERRY_PRESSURE_PLATE), BlockSetType.CHERRY));

    public static final RegistryObject<LuminaraLeavesBlock> LUMINARA_LEAVES = REGISTRY.register("luminara_leaves", LuminaraLeavesBlock::new);
    public static final RegistryObject<LuminaraSapling> LUMINARA_SAPLING = REGISTRY.register("luminara_sapling", LuminaraSapling::new);
    public static final RegistryObject<PottedLuminaraSaplingBlock> POTTED_LUMINARA_SAPLING = REGISTRY.register("potted_luminara_sapling", PottedLuminaraSaplingBlock::new);
    public static final RegistryObject<MultifaceBlock> COVER_BLOCK = REGISTRY.register("cover_block", () -> new MultifaceBlock(BlockBehaviour.Properties.copy(Blocks.VINE).mapColor(MapColor.TERRACOTTA_BLACK)) {
        @Override
        public boolean skipRendering(@NotNull BlockState pState, @NotNull BlockState pAdjacentBlockState, @NotNull Direction pSide) {
            return pAdjacentBlockState.is(this) || super.skipRendering(pState, pAdjacentBlockState, pSide);
        }
    });
    public static final RegistryObject<LatexCoverBlock> DARK_LATEX_COVER_BLOCK = REGISTRY.register("dark_latex_cover_block", () -> new LatexCoverBlock(BlockBehaviour.Properties.of()
            .noOcclusion()
            .dynamicShape()
            .mapColor(MapColor.COLOR_BLACK)
            .sound(SoundType.SLIME_BLOCK), ChangedLatexTypes.DARK_LATEX::get) {
    });

    public static final RegistryObject<LatexCoverBlock> WHITE_LATEX_COVER_BLOCK = REGISTRY.register("white_latex_cover_block", () -> new WhiteLatexCoverBlock(BlockBehaviour.Properties.of()
            .noOcclusion()
            .dynamicShape()
            .mapColor(MapColor.TERRACOTTA_WHITE)
            .sound(SoundType.SLIME_BLOCK)) {
    });

    public static final RegistryObject<WolfCrystalPillar> WOLF_CRYSTAL_PILLAR = REGISTRY.register("wolf_crystal_pillar", WolfCrystalPillar::new);

    public static final RegistryObject<StructureSpawnerBlock> STRUCTURE_SPAWNER = REGISTRY.register("structure_spawner", StructureSpawnerBlock::new);


    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class CommonSideHandler {

        @SubscribeEvent
        public static void commonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(() -> {
                if (LUMINARA_BLOOM.getId() != null) {
                    ((FlowerPotBlock) Blocks.FLOWER_POT)
                            .addPlant(LUMINARA_BLOOM.getId(), POTTED_LUMINARA_BLOOM);
                }
            });
        }
    }
}
