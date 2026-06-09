package net.foxyas.changedaddon.world.features.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

public record TreeWithFlowersFeatureConfiguration(
        TreeConfiguration treeConfig, 
        RandomPatchConfiguration flowerConfig
) implements FeatureConfiguration {

    public static final Codec<TreeWithFlowersFeatureConfiguration> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(
                TreeConfiguration.CODEC.fieldOf("tree").forGetter(TreeWithFlowersFeatureConfiguration::treeConfig),
                RandomPatchConfiguration.CODEC.fieldOf("flowers").forGetter(TreeWithFlowersFeatureConfiguration::flowerConfig)
        ).apply(instance, TreeWithFlowersFeatureConfiguration::new);
    });
}