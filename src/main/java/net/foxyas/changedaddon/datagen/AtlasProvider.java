package net.foxyas.changedaddon.datagen;

import net.minecraft.client.renderer.texture.atlas.sources.PalettedPermutations;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SpriteSourceProvider;

import java.util.List;

public class AtlasProvider extends SpriteSourceProvider {
    public AtlasProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, helper, "minecraft"); // O namespace "minecraft" aqui indica onde o atlas original está
    }

    @Override
    protected void addSources() {
        // --- ATLAS: assets/minecraft/atlases/blocks.json ---
        SourceList blocksAtlas = atlas(ResourceLocation.parse("blocks"));
        blocksAtlas.addSource(new PalettedPermutations(
                List.of(
                        ResourceLocation.parse("trims/items/leggings_trim"),
                        ResourceLocation.parse("trims/items/chestplate_trim"),
                        ResourceLocation.parse("trims/items/helmet_trim"),
                        ResourceLocation.parse("trims/items/boots_trim")
                ),
                ResourceLocation.parse("trims/color_palettes/trim_palette"),
                TrimMaterials.TRIMS
//            Map.of(
//                "iridium", ResourceLocation.parse("changed_addon:trims/color_palettes/iridium")
//            )
        ));

        // --- ATLAS: assets/minecraft/atlases/armor_trims.json ---
        SourceList armorTrimsAtlas = atlas(ResourceLocation.parse("armor_trims"));

        armorTrimsAtlas.addSource(new PalettedPermutations(
                List.of(
                        ResourceLocation.parse("trims/models/armor/coast"),
                        ResourceLocation.parse("trims/models/armor/coast_leggings"),
                        ResourceLocation.parse("trims/models/armor/sentry"),
                        ResourceLocation.parse("trims/models/armor/sentry_leggings"),
                        ResourceLocation.parse("trims/models/armor/dune"),
                        ResourceLocation.parse("trims/models/armor/dune_leggings"),
                        ResourceLocation.parse("trims/models/armor/wild"),
                        ResourceLocation.parse("trims/models/armor/wild_leggings"),
                        ResourceLocation.parse("trims/models/armor/ward"),
                        ResourceLocation.parse("trims/models/armor/ward_leggings"),
                        ResourceLocation.parse("trims/models/armor/eye"),
                        ResourceLocation.parse("trims/models/armor/eye_leggings"),
                        ResourceLocation.parse("trims/models/armor/vex"),
                        ResourceLocation.parse("trims/models/armor/vex_leggings"),
                        ResourceLocation.parse("trims/models/armor/tide"),
                        ResourceLocation.parse("trims/models/armor/tide_leggings"),
                        ResourceLocation.parse("trims/models/armor/snout"),
                        ResourceLocation.parse("trims/models/armor/snout_leggings"),
                        ResourceLocation.parse("trims/models/armor/rib"),
                        ResourceLocation.parse("trims/models/armor/rib_leggings"),
                        ResourceLocation.parse("trims/models/armor/spire"),
                        ResourceLocation.parse("trims/models/armor/spire_leggings"),
                        ResourceLocation.parse("trims/models/armor/wayfinder"),
                        ResourceLocation.parse("trims/models/armor/wayfinder_leggings"),
                        ResourceLocation.parse("trims/models/armor/shaper"),
                        ResourceLocation.parse("trims/models/armor/shaper_leggings"),
                        ResourceLocation.parse("trims/models/armor/silence"),
                        ResourceLocation.parse("trims/models/armor/silence_leggings"),
                        ResourceLocation.parse("trims/models/armor/raiser"),
                        ResourceLocation.parse("trims/models/armor/raiser_leggings"),
                        ResourceLocation.parse("trims/models/armor/host"),
                        ResourceLocation.parse("trims/models/armor/host_leggings")
                ),
                ResourceLocation.parse("trims/color_palettes/trim_palette"),
                TrimMaterials.TRIMS
//                Map.of(
//                        "iridium", ChangedAddonMod.resourceLoc("trims/color_palettes/iridium")
//                )
        ));
    }
}