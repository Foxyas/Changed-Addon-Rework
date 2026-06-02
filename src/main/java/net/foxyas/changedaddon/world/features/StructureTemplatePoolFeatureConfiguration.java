package net.foxyas.changedaddon.world.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public record StructureTemplatePoolFeatureConfiguration(Holder<StructureTemplatePool> templatePool) implements FeatureConfiguration {
    // O Codec traduz o objeto Java em dados lidos pelo sistema do jogo
    public static final Codec<StructureTemplatePoolFeatureConfiguration> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    StructureTemplatePool.CODEC.fieldOf("template_pool").forGetter(StructureTemplatePoolFeatureConfiguration::templatePool)
            ).apply(instance, StructureTemplatePoolFeatureConfiguration::new)
    );

}