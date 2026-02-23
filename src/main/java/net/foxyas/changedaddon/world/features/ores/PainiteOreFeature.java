package net.foxyas.changedaddon.world.features.ores;

import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonGameRules;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class PainiteOreFeature extends OreFeature {

    public static final Set<ResourceLocation> GENERATE_BIOMES = null;
    public static PainiteOreFeature FEATURE = null;
    public static Holder<ConfiguredFeature<?, ?>> CONFIGURED_FEATURE = null;
    public static Holder<PlacedFeature> PLACED_FEATURE = null;
    private final Set<ResourceKey<Level>> generate_dimensions = Set.of(Level.OVERWORLD);

    public PainiteOreFeature() {
        super(OreConfiguration.CODEC);
    }

    public static PainiteOreFeature feature() {
        FEATURE = new PainiteOreFeature();

        // Usando os alvos vanilla (stone + deepslate)
        List<OreConfiguration.TargetBlockState> targets = List.of(OreConfiguration.target(PainiteOreFeatureRuleTest.INSTANCE, ChangedAddonBlocks.DEEPSLATE_PAINITE_ORE.get().defaultBlockState()));
        CONFIGURED_FEATURE = Holder.direct(new ConfiguredFeature<>(Feature.ORE,
                new OreConfiguration(targets, 6) // vein size
        ));
        PLACED_FEATURE = Holder.direct(new PlacedFeature(CONFIGURED_FEATURE, List.of(CountPlacement.of(2), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(-60), VerticalAnchor.absolute(-45)), BiomeFilter.biome())));

        //CONFIGURED_FEATURE = FeatureUtils.register("changed_addon:painite_ore", FEATURE, new OreConfiguration(PainiteOreFeatureRuleTest.INSTANCE, ChangedAddonBlocks.DEEPSLATE_PAINITE_ORE.get().defaultBlockState(), 8));
        //PLACED_FEATURE = PlacementUtils.register("changed_addon:painite_ore", CONFIGURED_FEATURE, List.of(CountPlacement.of(2), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(-60), VerticalAnchor.absolute(-45)), BiomeFilter.biome()));
        return FEATURE;
    }

    public static Holder<PlacedFeature> placedFeature() {
        return PLACED_FEATURE;
    }

    private static boolean execute(LevelAccessor world, int x, int y, int z) {
        if (world.getLevelData().getGameRules().getBoolean(ChangedAddonGameRules.PAINITE_GENERATION)) {
            return !(world.isEmptyBlock(new BlockPos(x + 1, y, z)) && world.isEmptyBlock(new BlockPos(x - 1, y, z)) && world.isEmptyBlock(new BlockPos(x, y + 1, z)) && world.isEmptyBlock(new BlockPos(x, y - 1, z))
                    && world.isEmptyBlock(new BlockPos(x, y, z + 1)) && world.isEmptyBlock(new BlockPos(x, y, z - 1)));
        }
        return false;
    }

    public boolean place(FeaturePlaceContext<OreConfiguration> context) {
        WorldGenLevel world = context.level();
        if (!generate_dimensions.contains(world.getLevel().dimension()))
            return false;
        int x = context.origin().getX();
        int y = context.origin().getY();
        int z = context.origin().getZ();
        if (!execute(world, x, y, z))
            return false;
        return super.place(context);
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class PainiteOreFeatureRuleTest extends RuleTest {
        public static final PainiteOreFeatureRuleTest INSTANCE = new PainiteOreFeatureRuleTest();
        private static final com.mojang.serialization.Codec<PainiteOreFeatureRuleTest> CODEC = com.mojang.serialization.Codec.unit(() -> INSTANCE);
        private static final RuleTestType<PainiteOreFeatureRuleTest> CUSTOM_MATCH = () -> CODEC;

        @SubscribeEvent
        public static void init(FMLCommonSetupEvent event) {
            Registry.register(BuiltInRegistries.RULE_TEST, ResourceLocation.parse("changed_addon:painite_ore_match"), CUSTOM_MATCH);
        }

        public boolean test(BlockState blockstate, @NotNull RandomSource random) {
            return Objects.equals(Blocks.DEEPSLATE, blockstate.getBlock());
        }

        protected @NotNull RuleTestType<?> getType() {
            return CUSTOM_MATCH;
        }
    }
}
