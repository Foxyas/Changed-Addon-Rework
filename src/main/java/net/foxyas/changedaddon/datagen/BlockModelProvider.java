package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockModelProvider extends net.minecraftforge.client.model.generators.BlockModelProvider {
    public BlockModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ChangedAddonMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        generateEmissiveCrop();
        generateEmissiveCube();
        generateEmissiveCubeAll();

        withExistingParent(ChangedAddonBlocks.LUMINARA_BLOOM.getId().getPath(), ChangedAddonMod.resourceLoc("customs/emissive_cross"))
                .renderType("minecraft:cutout")
                .texture("cross", ChangedAddonMod.resourceLoc("block/luminara_bloom"))
                .texture("glow", ChangedAddonMod.resourceLoc("block/luminara_bloom_emissive"))
        ;

        withExistingParent(ChangedAddonBlocks.GOO_CORE.getId().getPath(), ChangedAddonMod.resourceLoc("customs/emissive_cube_all"))
                .renderType("minecraft:cutout")
                .texture("all", "changed_addon:block/goocore")
                .texture("all_glow", "changed_addon:block/goocore_emissive")
                .texture("particle", "changed_addon:block/goocore")
        ;
    }

    private void generateEmissiveCube() {
        // Generates the model layout under assets/changed_addon/models/block/customs/emissive_cube.json
        getBuilder("customs/emissive_cube")
                .parent(getExistingFile(mcLoc("block/block"))) // Inherits base settings from minecraft:block/block
                .ao(true) // Keeps ambient occlusion active for the base non-glowing block skin
                .texture("particle", "#north") // Fallback break particle binding

                // ==========================================
                // BASE LAYER: Standard non-glowing solid cube
                // ==========================================
                .element()
                .from(0.0f, 0.0f, 0.0f)
                .to(16.0f, 16.0f, 16.0f)
                // Dynamically sets up down, up, north, south, west, east pointing to standard variables
                .face(Direction.DOWN).texture("#down").cullface(Direction.DOWN).end()
                .face(Direction.UP).texture("#up").cullface(Direction.UP).end()
                .face(Direction.NORTH).texture("#north").cullface(Direction.NORTH).end()
                .face(Direction.SOUTH).texture("#south").cullface(Direction.SOUTH).end()
                .face(Direction.WEST).texture("#west").cullface(Direction.WEST).end()
                .face(Direction.EAST).texture("#east").cullface(Direction.EAST).end()
                .end()

                // ==========================================
                // GLOW LAYER: Emissive overlay cube elements
                // ==========================================
                .element()
                .from(0.0f, 0.0f, 0.0f)
                .to(16.0f, 16.0f, 16.0f)
                .shade(false) // Completely bypass directional shading shadows for full emissive layers

                // Down Face Overlay
                .face(Direction.DOWN)
                .texture("#emissive_down")
                .cullface(Direction.DOWN)
                .color(0xFFFFFFFF)
                .emissivity(15, 0) // Injects 15 Block Light, 0 Sky Light components via ForgeFaceData
                .ao(false) // Ensures shadows from adjacent solid blocks don't darken the emission
                .end()

                // Up Face Overlay
                .face(Direction.UP)
                .texture("#emissive_up")
                .cullface(Direction.UP)
                .color(0xFFFFFFFF)
                .emissivity(15, 0)
                .ao(false)
                .end()

                // North Face Overlay
                .face(Direction.NORTH)
                .texture("#emissive_north")
                .cullface(Direction.NORTH)
                .color(0xFFFFFFFF)
                .emissivity(15, 0)
                .ao(false)
                .end()

                // South Face Overlay
                .face(Direction.SOUTH)
                .texture("#emissive_south")
                .cullface(Direction.SOUTH)
                .color(0xFFFFFFFF)
                .emissivity(15, 0)
                .ao(false)
                .end()

                // West Face Overlay
                .face(Direction.WEST)
                .texture("#emissive_west")
                .cullface(Direction.WEST)
                .color(0xFFFFFFFF)
                .emissivity(15, 0)
                .ao(false)
                .end()

                // East Face Overlay
                .face(Direction.EAST)
                .texture("#emissive_east")
                .cullface(Direction.EAST)
                .color(0xFFFFFFFF)
                .emissivity(15, 0)
                .ao(false)
                .end()
                .end();
    }

    private void generateEmissiveCubeAll() {
        // Generates the clean utility layout under assets/changed_addon/models/block/customs/emissive_cube_all.json
        getBuilder("customs/emissive_cube_all")
                // Inherit directly from your own directional emissive cube!
                .parent(getExistingFile(ChangedAddonMod.resourceLoc("customs/emissive_cube")))

                // Bind the fallback particle breaking texture variable
                .texture("particle", "#all")

                // Map all base layer directional faces to point to the unified "#all" variable
                .texture("down", "#all")
                .texture("up", "#all")
                .texture("north", "#all")
                .texture("south", "#all")
                .texture("west", "#all")
                .texture("east", "#all")

                // Map all overlay emissive faces to point to the unified "#all_glow" variable
                .texture("emissive_down", "#all_glow")
                .texture("emissive_up", "#all_glow")
                .texture("emissive_north", "#all_glow")
                .texture("emissive_south", "#all_glow")
                .texture("emissive_west", "#all_glow")
                .texture("emissive_east", "#all_glow");
    }

    private void generateEmissiveCrop() {
        getBuilder("customs/emissive_cross")
                .ao(false) // Disable ambient occlusion for the entire model layout
                .texture("particle", "#cross") // Set up the fallback particle link pointing to the base #cross texture

                // ==========================================
                // BASE LAYER: Standard non-glowing elements
                // ==========================================

                // Diagonal Plane 1 (North-South cross segment)
                .element()
                .from(0.8f, 0.0f, 8.0f)
                .to(15.2f, 16.0f, 8.0f)
                .rotation()
                .origin(8.0f, 8.0f, 8.0f)
                .axis(net.minecraft.core.Direction.Axis.Y)
                .angle(45.0f)
                .rescale(true)
                .end()
                .shade(false)
                .face(net.minecraft.core.Direction.NORTH).uvs(0.0f, 0.0f, 16.0f, 16.0f).texture("#cross").end()
                .face(net.minecraft.core.Direction.SOUTH).uvs(0.0f, 0.0f, 16.0f, 16.0f).texture("#cross").end()
                .end()

                // Diagonal Plane 2 (West-East cross segment)
                .element()
                .from(8.0f, 0.0f, 0.8f)
                .to(8.0f, 16.0f, 15.2f)
                .rotation()
                .origin(8.0f, 8.0f, 8.0f)
                .axis(net.minecraft.core.Direction.Axis.Y)
                .angle(45.0f)
                .rescale(true)
                .end()
                .shade(false)
                .face(net.minecraft.core.Direction.WEST).uvs(0.0f, 0.0f, 16.0f, 16.0f).texture("#cross").end()
                .face(net.minecraft.core.Direction.EAST).uvs(0.0f, 0.0f, 16.0f, 16.0f).texture("#cross").end()
                .end()

                // ==========================================
                // GLOW LAYER: Emissive ForgeFaceData elements
                // ==========================================

                // Emissive Diagonal Plane 1 (North-South overlay)
                .element()
                .from(0.8f, 0.0f, 8.0f)
                .to(15.2f, 16.0f, 8.0f)
                .rotation()
                .origin(8.0f, 8.0f, 8.0f)
                .axis(net.minecraft.core.Direction.Axis.Y)
                .angle(45.0f)
                .rescale(true)
                .end()
                .shade(false)
                .face(net.minecraft.core.Direction.NORTH).uvs(0.0f, 0.0f, 16.0f, 16.0f).texture("#glow").color(0xFFFFFFFF).emissivity(15, 0).ao(false).end()
                .shade(false)
                .face(net.minecraft.core.Direction.SOUTH)
                .uvs(0.0f, 0.0f, 16.0f, 16.0f)
                .texture("#glow")
                .color(0xFFFFFFFF).emissivity(15, 0).ao(false) // 15 block light, no ambient occlusion shadows
                .end()
                .end()

                // Emissive Diagonal Plane 2 (West-East overlay)
                .element()
                .from(8.0f, 0.0f, 0.8f)
                .to(8.0f, 16.0f, 15.2f)
                .rotation()
                .origin(8.0f, 8.0f, 8.0f)
                .axis(net.minecraft.core.Direction.Axis.Y)
                .angle(45.0f)
                .rescale(true)
                .end()
                .shade(false)
                .face(net.minecraft.core.Direction.WEST)
                .uvs(0.0f, 0.0f, 16.0f, 16.0f)
                .texture("#glow")
                .color(0xFFFFFFFF).emissivity(15, 0).ao(false) // 15 block light, no ambient occlusion shadows
                .end()
                .shade(false)
                .face(net.minecraft.core.Direction.EAST)
                .uvs(0.0f, 0.0f, 16.0f, 16.0f)
                .texture("#glow")
                .color(0xFFFFFFFF).emissivity(15, 0).ao(false) // 15 block light, no ambient occlusion shadows
                .end()
                .end();
    }
}
