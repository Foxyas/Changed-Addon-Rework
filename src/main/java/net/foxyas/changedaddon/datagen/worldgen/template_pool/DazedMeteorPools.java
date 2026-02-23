package net.foxyas.changedaddon.datagen.worldgen.template_pool;

import com.mojang.datafixers.util.Pair;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.datagen.DatapackEntriesProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

//copy from a_changed
public class DazedMeteorPools {

    public static final ResourceKey<StructureTemplatePool> START = key("meteor");

    public static void bootstrap(@NotNull BootstapContext<StructureTemplatePool> context) {
        HolderGetter<StructureTemplatePool> poolGetter = context.lookup(Registries.TEMPLATE_POOL);
        HolderGetter<StructureProcessorList> processorListGetter = context.lookup(Registries.PROCESSOR_LIST);
        Holder<StructureTemplatePool> empty = poolGetter.getOrThrow(Pools.EMPTY);

        Holder<StructureProcessorList> dazedLatexMeteorProcessors = processorListGetter.getOrThrow(DatapackEntriesProvider.DAZED_METEOR_POLL);

        context.register(START, new StructureTemplatePool(
                empty, List.of(
                Pair.of(StructurePoolElement.single(loc("dazed_latex_meteor"), dazedLatexMeteorProcessors), 1),
                Pair.of(StructurePoolElement.single(loc("dazed_latex_meteor_closed"), dazedLatexMeteorProcessors), 1),
                Pair.of(StructurePoolElement.single(loc("dazed_latex_meteor_side_opened"), dazedLatexMeteorProcessors), 1)
        ),
                StructureTemplatePool.Projection.RIGID
        ));
    }

    private static @NotNull ResourceKey<StructureTemplatePool> key(String str) {
        return ResourceKey.create(Registries.TEMPLATE_POOL, ChangedAddonMod.resourceLoc(str));
    }

    @Contract(pure = true)
    private static @NotNull String loc(String str) {
        return ChangedAddonMod.MODID + ":" + str;
    }
}