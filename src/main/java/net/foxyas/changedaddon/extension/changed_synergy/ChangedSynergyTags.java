package net.foxyas.changedaddon.extension.changed_synergy;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import net.parkabird.changedsynergy.ChangedSynergyMod;

public class ChangedSynergyTags {

    public static final TagKey<EntityType<?>> SOCIAL_INTERACTION_EXCLUDED = key("social_interaction_excluded");

    private static TagKey<EntityType<?>> key(String path) {
        return TagKey.create(ForgeRegistries.ENTITY_TYPES.getRegistryKey(), ResourceLocation.fromNamespaceAndPath("changed_synergy", path));
    }
}
