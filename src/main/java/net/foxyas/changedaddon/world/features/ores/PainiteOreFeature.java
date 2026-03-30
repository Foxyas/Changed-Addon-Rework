package net.foxyas.changedaddon.world.features.ores;

import net.foxyas.changedaddon.init.ChangedAddonGameRules;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

import java.util.Set;

public class PainiteOreFeature extends OreFeature {

    private final Set<ResourceKey<Level>> generate_dimensions = Set.of(Level.OVERWORLD);

    public PainiteOreFeature() {
        super(OreConfiguration.CODEC);
    }

    private static boolean test(LevelAccessor world) {
        return world.getLevelData().getGameRules().getBoolean(ChangedAddonGameRules.PAINITE_GENERATION);
    }

    @Override
    public boolean place(FeaturePlaceContext<OreConfiguration> context) {
        WorldGenLevel world = context.level();
        if (!generate_dimensions.contains(world.getLevel().dimension()))
            return false;
        if (!test(world))
            return false;
        return super.place(context);
    }

}
