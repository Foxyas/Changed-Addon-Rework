package net.foxyas.changedaddon.worldgen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import org.jetbrains.annotations.NotNull;

//copy from a_changed
public class ChangedAddonStructures {

    public static final ResourceKey<Structure> DAZED_METEOR = key("dazed_meteor");
    public static final ResourceKey<StructureSet> DAZED_METEOR_SET = setKey("dazed_meteor_set");

    private static @NotNull ResourceKey<Structure> key(String str) {
        return ResourceKey.create(Registries.STRUCTURE, ChangedAddonMod.resourceLoc(str));
    }

    private static ResourceKey<StructureSet> setKey(String str) {
        return ResourceKey.create(Registries.STRUCTURE_SET, ChangedAddonMod.resourceLoc(str));
    }
}
