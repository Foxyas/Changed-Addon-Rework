package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockModelProvider extends net.minecraftforge.client.model.generators.BlockModelProvider {

    public static final ResourceLocation EMISSIVE_CROSS = ChangedAddonMod.resourceLoc("customs/emissive_cross");
    public static final ResourceLocation EMISSIVE_CUBE_ALL = ChangedAddonMod.resourceLoc("customs/emissive_cube_all");
    public static final ResourceLocation EMISSIVE_CUBE_COLUMN = ChangedAddonMod.resourceLoc("customs/emissive_cube_column");
    public static final ResourceLocation EMISSIVE_CUBE = ChangedAddonMod.resourceLoc("customs/emissive_cube");
    public static final ResourceLocation EMISSIVE_POTTED_PLANT = ChangedAddonMod.resourceLoc("customs/emissive_potted_plant");

    public BlockModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ChangedAddonMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        generateEmissiveCrop();
        generateEmissiveCube();
        generateEmissiveCubeAll();
        generateEmissiveCubeColumn();
        generateEmissivePottedPlant();

        withExistingParent(ChangedAddonBlocks.LUMINARA_BLOOM.getId().getPath(), EMISSIVE_CROSS)
                .renderType("minecraft:cutout")
                .texture("cross", ChangedAddonMod.resourceLoc("block/luminara_bloom"))
                .texture("glow", ChangedAddonMod.resourceLoc("block/luminara_bloom_emissive"))
        ;

        withExistingParent(ChangedAddonBlocks.GOO_CORE.getId().getPath(), EMISSIVE_CUBE_ALL)
                .renderType("minecraft:cutout")
                .texture("all", "changed_addon:block/goocore")
                .texture("all_glow", "changed_addon:block/goocore_emissive")
                .texture("particle", "changed_addon:block/goocore")
        ;

        withExistingParent(ChangedAddonBlocks.LUMINARA_LOG.getId().getPath(), EMISSIVE_CUBE_COLUMN)
                .renderType("minecraft:cutout")
                .texture("end", "changed_addon:block/luminara_tree/luminara_log_top")
                .texture("end_glow", "changed_addon:block/luminara_tree/luminara_log_top_glow")
                .texture("side", "changed_addon:block/luminara_tree/luminara_log_side")
                .texture("side_glow", "changed_addon:block/luminara_tree/luminara_log_side_glow")
        ;

        withExistingParent("active_" + ChangedAddonBlocks.LUMINARA_LOG.getId().getPath(), EMISSIVE_CUBE_COLUMN)
                .renderType("minecraft:cutout")
                .texture("end", "changed_addon:block/luminara_tree/active_luminara_log_top")
                .texture("end_glow", "changed_addon:block/luminara_tree/active_luminara_log_top_glow")
                .texture("side", "changed_addon:block/luminara_tree/active_luminara_log_side")
                .texture("side_glow", "changed_addon:block/luminara_tree/active_luminara_log_side_glow")
        ;

        withExistingParent(ChangedAddonBlocks.STRIPPED_LUMINARA_LOG.getId().getPath(), EMISSIVE_CUBE_COLUMN)
                .renderType("minecraft:cutout")
                .texture("end", "changed_addon:block/luminara_tree/stripped_luminara_log_top")
                .texture("end_glow", "changed_addon:block/luminara_tree/stripped_luminara_log_top_glow")
                .texture("side", "changed_addon:block/luminara_tree/stripped_luminara_log_side")
                .texture("side_glow", "changed_addon:block/luminara_tree/stripped_luminara_log_side_glow")
        ;

        withExistingParent("active_" + ChangedAddonBlocks.STRIPPED_LUMINARA_LOG.getId().getPath(), EMISSIVE_CUBE_COLUMN)
                .renderType("minecraft:cutout")
                .texture("end", "changed_addon:block/luminara_tree/active_stripped_luminara_log_top")
                .texture("end_glow", "changed_addon:block/luminara_tree/active_stripped_luminara_log_top_glow")
                .texture("side", "changed_addon:block/luminara_tree/active_stripped_luminara_log_side")
                .texture("side_glow", "changed_addon:block/luminara_tree/active_stripped_luminara_log_side_glow")
        ;

        withExistingParent(ChangedAddonBlocks.LUMINARA_WOOD.getId().getPath(), EMISSIVE_CUBE_COLUMN)
                .renderType("minecraft:cutout")
                .texture("end", "changed_addon:block/luminara_tree/luminara_log_side")
                .texture("end_glow", "changed_addon:block/luminara_tree/luminara_log_side_glow")
                .texture("side", "changed_addon:block/luminara_tree/luminara_log_side")
                .texture("side_glow", "changed_addon:block/luminara_tree/luminara_log_side_glow")
        ;

        withExistingParent(ChangedAddonBlocks.STRIPPED_LUMINARA_WOOD.getId().getPath(), EMISSIVE_CUBE_COLUMN)
                .renderType("minecraft:cutout")
                .texture("end", "changed_addon:block/luminara_tree/stripped_luminara_log_side")
                .texture("end_glow", "changed_addon:block/luminara_tree/stripped_luminara_log_side_glow")
                .texture("side", "changed_addon:block/luminara_tree/stripped_luminara_log_side")
                .texture("side_glow", "changed_addon:block/luminara_tree/stripped_luminara_log_side_glow")
        ;

        withExistingParent("active_" + ChangedAddonBlocks.LUMINARA_WOOD.getId().getPath(), EMISSIVE_CUBE_COLUMN)
                .renderType("minecraft:cutout")
                .texture("end", "changed_addon:block/luminara_tree/active_stripped_luminara_log_side")
                .texture("end_glow", "changed_addon:block/luminara_tree/active_stripped_luminara_log_side_glow")
                .texture("side", "changed_addon:block/luminara_tree/active_stripped_luminara_log_side")
                .texture("side_glow", "changed_addon:block/luminara_tree/active_stripped_luminara_log_side_glow")
        ;

        withExistingParent("active_" + ChangedAddonBlocks.STRIPPED_LUMINARA_WOOD.getId().getPath(), EMISSIVE_CUBE_COLUMN)
                .renderType("minecraft:cutout")
                .texture("end", "changed_addon:block/luminara_tree/active_stripped_luminara_log_side")
                .texture("end_glow", "changed_addon:block/luminara_tree/active_stripped_luminara_log_side_glow")
                .texture("side", "changed_addon:block/luminara_tree/active_stripped_luminara_log_side")
                .texture("side_glow", "changed_addon:block/luminara_tree/active_stripped_luminara_log_side_glow")
        ;

        withExistingParent(ChangedAddonBlocks.LUMINARA_LEAVES.getId().getPath(), EMISSIVE_CUBE_ALL)
                .renderType("minecraft:cutout")
                .texture("all", "changed_addon:block/luminara_tree/luminara_leaves")
                //.texture("all_glow", "changed_addon:block/luminara_tree/luminara_leaves_glow")
                .texture("all_glow", "changed_addon:block/luminara_tree/empty")
        ;

        withExistingParent(ChangedAddonBlocks.LUMINARA_SAPLING.getId().getPath(), EMISSIVE_CROSS)
                .renderType("minecraft:cutout")
                .texture("cross", ChangedAddonMod.resourceLoc("block/luminara_sapling"))
                .texture("glow", ChangedAddonMod.resourceLoc("block/luminara_sapling_emissive"))
        ;

        withExistingParent(ChangedAddonBlocks.POTTED_LUMINARA_BLOOM.getId().getPath(), EMISSIVE_POTTED_PLANT)
                .renderType("minecraft:cutout")
                .texture("plant", "changed_addon:block/luminara_bloom")
                .texture("plant_glow", "changed_addon:block/luminara_bloom_emissive")
        ;

        withExistingParent(ChangedAddonBlocks.POTTED_LUMINARA_SAPLING.getId().getPath(), EMISSIVE_POTTED_PLANT)
                .renderType("minecraft:cutout")
                .texture("plant", "changed_addon:block/luminara_sapling")
                .texture("plant_glow", "changed_addon:block/luminara_sapling_emissive")
        ;
    }

    private void generateEmissiveCubeColumn() {
        // Generates the clean utility layout under assets/changed_addon/models/block/customs/emissive_cube_all.json
        getBuilder("customs/emissive_cube_column")
                // Inherit directly from your own directional emissive cube!
                .parent(getExistingFile(EMISSIVE_CUBE))

                // Bind the fallback particle breaking texture variable
                .texture("particle", "#side")

                // Map all base layer directional faces to match logs.
                .texture("down", "#end")
                .texture("up", "#end")
                .texture("north", "#side")
                .texture("south", "#side")
                .texture("west", "#side")
                .texture("east", "#side")

                // Map all overlay emissive faces to point to the unified "#side_glow" variable
                .texture("emissive_down", "#end_glow")
                .texture("emissive_up", "#end_glow")
                .texture("emissive_north", "#side_glow")
                .texture("emissive_south", "#side_glow")
                .texture("emissive_west", "#side_glow")
                .texture("emissive_east", "#side_glow");
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
                .shade(false)
                .face(Direction.DOWN)
                .texture("#emissive_down")
                .uvs(0.0f, 0.0f, 16.0f, 16.0f)
                .cullface(Direction.DOWN)
                .color(0xFFFFFFFF)
                .emissivity(15, 15) // Injects 15 Block Light, 0 Sky Light components via ForgeFaceData
                .ao(false) // Ensures shadows from adjacent solid blocks don't darken the emission
                .end()

                // Up Face Overlay
                .shade(false)
                .face(Direction.UP)
                .uvs(0.0f, 0.0f, 16.0f, 16.0f)
                .texture("#emissive_up")
                .cullface(Direction.UP)
                .color(0xFFFFFFFF)
                .emissivity(15, 15)
                .ao(false)
                .end()

                // North Face Overlay
                .shade(false)
                .face(Direction.NORTH)
                .uvs(0.0f, 0.0f, 16.0f, 16.0f)
                .texture("#emissive_north")
                .cullface(Direction.NORTH)
                .color(0xFFFFFFFF)
                .emissivity(15, 15)
                .ao(false)
                .end()

                // South Face Overlay
                .shade(false)
                .face(Direction.SOUTH)
                .uvs(0.0f, 0.0f, 16.0f, 16.0f)
                .texture("#emissive_south")
                .cullface(Direction.SOUTH)
                .color(0xFFFFFFFF)
                .emissivity(15, 15)
                .ao(false)
                .end()

                // West Face Overlay
                .shade(false)
                .face(Direction.WEST)
                .uvs(0.0f, 0.0f, 16.0f, 16.0f)
                .texture("#emissive_west")
                .cullface(Direction.WEST)
                .color(0xFFFFFFFF)
                .emissivity(15, 15)
                .ao(false)
                .end()

                // East Face Overlay
                .shade(false)
                .face(Direction.EAST)
                .uvs(0.0f, 0.0f, 16.0f, 16.0f)
                .texture("#emissive_east")
                .cullface(Direction.EAST)
                .color(0xFFFFFFFF)
                .emissivity(15, 15)
                .ao(false)
                .end()
                .end();
    }

    private void generateEmissiveCubeAll() {
        // Generates the clean utility layout under assets/changed_addon/models/block/customs/emissive_cube_all.json
        getBuilder("customs/emissive_cube_all")
                // Inherit directly from your own directional emissive cube!
                .parent(getExistingFile(EMISSIVE_CUBE))

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
                .parent(getExistingFile(mcLoc("block/cross"))) // Inherits base settings from minecraft:block/block
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
                .face(net.minecraft.core.Direction.NORTH)
                .uvs(0.0f, 0.0f, 16.0f, 16.0f)
                .texture("#glow")
                .color(0xFFFFFFFF).emissivity(15, 15).ao(false)
                .end()
                .shade(false)
                .face(net.minecraft.core.Direction.SOUTH)
                .uvs(0.0f, 0.0f, 16.0f, 16.0f)
                .texture("#glow")
                .color(0xFFFFFFFF).emissivity(15, 15).ao(false) // 15 block light, no ambient occlusion shadows
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
                .color(0xFFFFFFFF).emissivity(15, 15).ao(false) // 15 block light, no ambient occlusion shadows
                .end()
                .shade(false)
                .face(net.minecraft.core.Direction.EAST)
                .uvs(0.0f, 0.0f, 16.0f, 16.0f)
                .texture("#glow")
                .color(0xFFFFFFFF).emissivity(15, 15).ao(false) // 15 block light, no ambient occlusion shadows
                .end()
                .end();
    }

    private void generateEmissivePottedPlant() {
        // Generates the model under assets/changed_addon/models/block/customs/emissive_potted_plant.json
        getBuilder("customs/emissive_potted_plant")
                .ao(false) // Disable ambient occlusion globally for the model to prevent shading on the flower pot walls
                .texture("particle", "minecraft:block/flower_pot") // Fallback particle texture
                .texture("flowerpot", "minecraft:block/flower_pot") // Links default pot texture variable
                .texture("dirt", "minecraft:block/dirt")           // Links default dirt texture variable

                // ==========================================
                // VANILLA FLOWER POT STRUCTURAL CUBES
                // ==========================================

                // Pot Wall: West
                .element().from(5.0f, 0.0f, 5.0f).to(6.0f, 6.0f, 11.0f)
                .face(Direction.DOWN).uvs(5.0f, 5.0f, 6.0f, 11.0f).texture("#flowerpot").cullface(Direction.DOWN).end()
                .face(Direction.UP).uvs(5.0f, 5.0f, 6.0f, 11.0f).texture("#flowerpot").end()
                .face(Direction.NORTH).uvs(10.0f, 10.0f, 11.0f, 16.0f).texture("#flowerpot").end()
                .face(Direction.SOUTH).uvs(5.0f, 10.0f, 6.0f, 16.0f).texture("#flowerpot").end()
                .face(Direction.WEST).uvs(5.0f, 10.0f, 11.0f, 16.0f).texture("#flowerpot").end()
                .face(Direction.EAST).uvs(5.0f, 10.0f, 11.0f, 16.0f).texture("#flowerpot").end()
                .end()

                // Pot Wall: East
                .element().from(10.0f, 0.0f, 5.0f).to(11.0f, 6.0f, 11.0f)
                .face(Direction.DOWN).uvs(10.0f, 5.0f, 11.0f, 11.0f).texture("#flowerpot").cullface(Direction.DOWN).end()
                .face(Direction.UP).uvs(10.0f, 5.0f, 11.0f, 11.0f).texture("#flowerpot").end()
                .face(Direction.NORTH).uvs(5.0f, 10.0f, 6.0f, 16.0f).texture("#flowerpot").end()
                .face(Direction.SOUTH).uvs(10.0f, 10.0f, 11.0f, 16.0f).texture("#flowerpot").end()
                .face(Direction.WEST).uvs(5.0f, 10.0f, 11.0f, 16.0f).texture("#flowerpot").end()
                .face(Direction.EAST).uvs(5.0f, 10.0f, 11.0f, 16.0f).texture("#flowerpot").end()
                .end()

                // Pot Wall: North
                .element().from(6.0f, 0.0f, 5.0f).to(10.0f, 6.0f, 6.0f)
                .face(Direction.DOWN).uvs(6.0f, 10.0f, 10.0f, 11.0f).texture("#flowerpot").cullface(Direction.DOWN).end()
                .face(Direction.UP).uvs(6.0f, 5.0f, 10.0f, 6.0f).texture("#flowerpot").end()
                .face(Direction.NORTH).uvs(6.0f, 10.0f, 10.0f, 16.0f).texture("#flowerpot").end()
                .face(Direction.SOUTH).uvs(6.0f, 10.0f, 10.0f, 16.0f).texture("#flowerpot").end()
                .end()

                // Pot Wall: South
                .element().from(6.0f, 0.0f, 10.0f).to(10.0f, 6.0f, 11.0f)
                .face(Direction.DOWN).uvs(6.0f, 5.0f, 10.0f, 6.0f).texture("#flowerpot").cullface(Direction.DOWN).end()
                .face(Direction.UP).uvs(6.0f, 10.0f, 10.0f, 11.0f).texture("#flowerpot").end()
                .face(Direction.NORTH).uvs(6.0f, 10.0f, 10.0f, 16.0f).texture("#flowerpot").end()
                .face(Direction.SOUTH).uvs(6.0f, 10.0f, 10.0f, 16.0f).texture("#flowerpot").end()
                .end()

                // Soil / Dirt Plane
                .element().from(6.0f, 0.0f, 6.0f).to(10.0f, 4.0f, 10.0f)
                .face(Direction.DOWN).uvs(6.0f, 12.0f, 10.0f, 16.0f).texture("#flowerpot").cullface(Direction.DOWN).end()
                .face(Direction.UP).uvs(6.0f, 6.0f, 10.0f, 10.0f).texture("#dirt").end()
                .end()

                // ==========================================
                // BASE PLANT LAYER: Non-glowing plant planes
                // ==========================================

                // Plant Diagonal Plane 1 (North-South segment)
                .element().from(2.6f, 4.0f, 8.0f).to(13.4f, 16.0f, 8.0f)
                .rotation().origin(8.0f, 8.0f, 8.0f).axis(Direction.Axis.Y).angle(45.0f).rescale(true).end()
                .shade(false)
                .face(Direction.NORTH).uvs(0.0f, 0.0f, 16.0f, 16.0f).texture("#plant").end()
                .face(Direction.SOUTH).uvs(0.0f, 0.0f, 16.0f, 16.0f).texture("#plant").end()
                .end()

                // Plant Diagonal Plane 2 (West-East segment)
                .element().from(8.0f, 4.0f, 2.6f).to(8.0f, 16.0f, 13.4f)
                .rotation().origin(8.0f, 8.0f, 8.0f).axis(Direction.Axis.Y).angle(45.0f).rescale(true).end()
                .shade(false)
                .face(Direction.WEST).uvs(0.0f, 0.0f, 16.0f, 16.0f).texture("#plant").end()
                .face(Direction.EAST).uvs(0.0f, 0.0f, 16.0f, 16.0f).texture("#plant").end()
                .end()

                // ==========================================
                // GLOW PLANT LAYER: Emissive overlay layers
                // ==========================================

                // Emissive Plant Diagonal Plane 1 (North-South overlay)
                .element().from(2.6f, 4.0f, 8.0f).to(13.4f, 16.0f, 8.0f)
                .rotation().origin(8.0f, 8.0f, 8.0f).axis(Direction.Axis.Y).angle(45.0f).rescale(true).end()
                .shade(false)
                .face(Direction.NORTH)
                .uvs(0.0f, 0.0f, 16.0f, 16.0f)
                .texture("#plant_glow") // Points to custom emissive texture template variable
                .color(0xFFFFFFFF)
                .emissivity(15, 15) // Dynamic ForgeFaceData emission coordinates
                .ao(false)
                .end()
                .face(Direction.SOUTH)
                .uvs(0.0f, 0.0f, 16.0f, 16.0f)
                .texture("#plant_glow")
                .color(0xFFFFFFFF)
                .emissivity(15, 15)
                .ao(false)
                .end()
                .end()

                // Emissive Plant Diagonal Plane 2 (West-East overlay)
                .element().from(8.0f, 4.0f, 2.6f).to(8.0f, 16.0f, 13.4f)
                .rotation().origin(8.0f, 8.0f, 8.0f).axis(Direction.Axis.Y).angle(45.0f).rescale(true).end()
                .shade(false)
                .face(Direction.WEST)
                .uvs(0.0f, 0.0f, 16.0f, 16.0f)
                .texture("#plant_glow")
                .color(0xFFFFFFFF)
                .emissivity(15, 15)
                .ao(false)
                .end()
                .face(Direction.EAST)
                .uvs(0.0f, 0.0f, 16.0f, 16.0f)
                .texture("#plant_glow")
                .color(0xFFFFFFFF)
                .emissivity(15, 15)
                .ao(false)
                .end()
                .end();
    }
}
