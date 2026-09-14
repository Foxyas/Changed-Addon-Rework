package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockModelProvider extends net.minecraftforge.client.model.generators.BlockModelProvider {

    public static final ResourceLocation EMISSIVE_CROSS = ChangedAddonMod.resourceLoc("customs/emissive_cross");
    public static final ResourceLocation EMISSIVE_CUBE_ALL = ChangedAddonMod.resourceLoc("customs/emissive_cube_all");
    public static final ResourceLocation EMISSIVE_CUBE_COLUMN = ChangedAddonMod.resourceLoc("customs/emissive_cube_column");
    public static final ResourceLocation EMISSIVE_CUBE = ChangedAddonMod.resourceLoc("customs/emissive_cube");
    public static final ResourceLocation EMISSIVE_POTTED_PLANT = ChangedAddonMod.resourceLoc("customs/emissive_potted_plant");
    public static final ResourceLocation EMISSIVE_FLOWER_BED_1 = ChangedAddonMod.resourceLoc("customs/emissive_flowerbed_1");
    public static final ResourceLocation EMISSIVE_FLOWER_BED_2 = ChangedAddonMod.resourceLoc("customs/emissive_flowerbed_2");
    public static final ResourceLocation EMISSIVE_FLOWER_BED_3 = ChangedAddonMod.resourceLoc("customs/emissive_flowerbed_3");
    public static final ResourceLocation EMISSIVE_FLOWER_BED_4 = ChangedAddonMod.resourceLoc("customs/emissive_flowerbed_4");
    public static final ResourceLocation EMISSIVE_MULTIFACE = ChangedAddonMod.resourceLoc("customs/emissive_multiface");

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
        generateEmissiveFlowerbeds();
        generateEmissiveMultiface();

        withExistingParent(ChangedAddonBlocks.LUMINARA_BLOOM.getId().getPath(), EMISSIVE_CROSS)
                .renderType("minecraft:cutout")
                .texture("cross", ChangedAddonMod.resourceLoc("block/luminara_bloom"))
                .texture("emissive", ChangedAddonMod.resourceLoc("block/luminara_bloom_emissive"))
        ;

        withExistingParent(ChangedAddonBlocks.GOO_CORE.getId().getPath(), EMISSIVE_CUBE_ALL)
                .renderType("minecraft:cutout")
                .texture("all", "changed_addon:block/goocore")
                .texture("all_emissive", "changed_addon:block/goocore_emissive")
                .texture("particle", "changed_addon:block/goocore")
        ;

        withExistingParent(ChangedAddonBlocks.LUMINARA_LOG.getId().getPath(), EMISSIVE_CUBE_COLUMN)
                .renderType("minecraft:cutout")
                .texture("end", "changed_addon:block/luminara_tree/luminara_log_top")
                .texture("end_emissive", "changed_addon:block/luminara_tree/luminara_log_top_emissive")
                .texture("side", "changed_addon:block/luminara_tree/luminara_log_side")
                .texture("side_emissive", "changed_addon:block/luminara_tree/luminara_log_side_emissive")
        ;

        withExistingParent("active_" + ChangedAddonBlocks.LUMINARA_LOG.getId().getPath(), EMISSIVE_CUBE_COLUMN)
                .renderType("minecraft:cutout")
                .texture("end", "changed_addon:block/luminara_tree/active_luminara_log_top")
                .texture("end_emissive", "changed_addon:block/luminara_tree/active_luminara_log_top_emissive")
                .texture("side", "changed_addon:block/luminara_tree/active_luminara_log_side")
                .texture("side_emissive", "changed_addon:block/luminara_tree/active_luminara_log_side_emissive")
        ;

        withExistingParent(ChangedAddonBlocks.STRIPPED_LUMINARA_LOG.getId().getPath(), EMISSIVE_CUBE_COLUMN)
                .renderType("minecraft:cutout")
                .texture("end", "changed_addon:block/luminara_tree/stripped_luminara_log_top")
                .texture("end_emissive", "changed_addon:block/luminara_tree/stripped_luminara_log_top_emissive")
                .texture("side", "changed_addon:block/luminara_tree/stripped_luminara_log_side")
                .texture("side_emissive", "changed_addon:block/luminara_tree/stripped_luminara_log_side_emissive")
        ;

        withExistingParent("active_" + ChangedAddonBlocks.STRIPPED_LUMINARA_LOG.getId().getPath(), EMISSIVE_CUBE_COLUMN)
                .renderType("minecraft:cutout")
                .texture("end", "changed_addon:block/luminara_tree/active_stripped_luminara_log_top")
                .texture("end_emissive", "changed_addon:block/luminara_tree/active_stripped_luminara_log_top_emissive")
                .texture("side", "changed_addon:block/luminara_tree/active_stripped_luminara_log_side")
                .texture("side_emissive", "changed_addon:block/luminara_tree/active_stripped_luminara_log_side_emissive")
        ;

        withExistingParent(ChangedAddonBlocks.LUMINARA_WOOD.getId().getPath(), EMISSIVE_CUBE_COLUMN)
                .renderType("minecraft:cutout")
                .texture("end", "changed_addon:block/luminara_tree/luminara_log_side")
                .texture("end_emissive", "changed_addon:block/luminara_tree/luminara_log_side_emissive")
                .texture("side", "changed_addon:block/luminara_tree/luminara_log_side")
                .texture("side_emissive", "changed_addon:block/luminara_tree/luminara_log_side_emissive")
        ;

        withExistingParent(ChangedAddonBlocks.STRIPPED_LUMINARA_WOOD.getId().getPath(), EMISSIVE_CUBE_COLUMN)
                .renderType("minecraft:cutout")
                .texture("end", "changed_addon:block/luminara_tree/stripped_luminara_log_side")
                .texture("end_emissive", "changed_addon:block/luminara_tree/stripped_luminara_log_side_emissive")
                .texture("side", "changed_addon:block/luminara_tree/stripped_luminara_log_side")
                .texture("side_emissive", "changed_addon:block/luminara_tree/stripped_luminara_log_side_emissive")
        ;

        withExistingParent("active_" + ChangedAddonBlocks.LUMINARA_WOOD.getId().getPath(), EMISSIVE_CUBE_COLUMN)
                .renderType("minecraft:cutout")
                .texture("end", "changed_addon:block/luminara_tree/active_luminara_log_side")
                .texture("end_emissive", "changed_addon:block/luminara_tree/active_luminara_log_side_emissive")
                .texture("side", "changed_addon:block/luminara_tree/active_luminara_log_side")
                .texture("side_emissive", "changed_addon:block/luminara_tree/active_luminara_log_side_emissive")
        ;

        withExistingParent("active_" + ChangedAddonBlocks.STRIPPED_LUMINARA_WOOD.getId().getPath(), EMISSIVE_CUBE_COLUMN)
                .renderType("minecraft:cutout")
                .texture("end", "changed_addon:block/luminara_tree/active_stripped_luminara_log_side")
                .texture("end_emissive", "changed_addon:block/luminara_tree/active_stripped_luminara_log_side_emissive")
                .texture("side", "changed_addon:block/luminara_tree/active_stripped_luminara_log_side")
                .texture("side_emissive", "changed_addon:block/luminara_tree/active_stripped_luminara_log_side_emissive")
        ;

        withExistingParent(ChangedAddonBlocks.LUMINARA_LEAVES.getId().getPath(), EMISSIVE_CUBE_ALL)
                .renderType("minecraft:cutout", "minecraft:cutout")
                .texture("all", "changed_addon:block/luminara_tree/luminara_leaves")
                //.texture("all_emissive", "changed_addon:block/luminara_tree/luminara_leaves_emissive")
                .texture("all_emissive", "changed_addon:block/luminara_tree/empty")
        ;

        withExistingParent(ChangedAddonBlocks.LUMINARA_SAPLING.getId().getPath(), EMISSIVE_CROSS)
                .renderType("minecraft:cutout")
                .texture("cross", ChangedAddonMod.resourceLoc("block/luminara_sapling"))
                .texture("emissive", ChangedAddonMod.resourceLoc("block/luminara_sapling_emissive"))
        ;

        withExistingParent(ChangedAddonBlocks.POTTED_LUMINARA_BLOOM.getId().getPath(), EMISSIVE_POTTED_PLANT)
                .renderType("minecraft:cutout")
                .texture("plant", "changed_addon:block/luminara_bloom")
                .texture("plant_emissive", "changed_addon:block/luminara_bloom_emissive")
        ;

        withExistingParent(ChangedAddonBlocks.POTTED_LUMINARA_SAPLING.getId().getPath(), EMISSIVE_POTTED_PLANT)
                .renderType("minecraft:cutout")
                .texture("plant", "changed_addon:block/luminara_sapling")
                .texture("plant_emissive", "changed_addon:block/luminara_sapling_emissive")
        ;

        withExistingParent(ChangedAddonBlocks.LUMINARA_LICHEN.getId().getPath(), EMISSIVE_MULTIFACE)
                .renderType("minecraft:cutout")
                .texture("base", "changed_addon:block/luminara_lichen")
                .texture("emissive", "changed_addon:block/luminara_lichen_emissive")
        ;

        generateLuminaraPetalsBlockModels();
    }

    protected void generateLuminaraPetalsBlockModels() {
        ResourceLocation[] emissiveFlowerBeds = new ResourceLocation[]{EMISSIVE_FLOWER_BED_1, EMISSIVE_FLOWER_BED_2, EMISSIVE_FLOWER_BED_3, EMISSIVE_FLOWER_BED_4};

        ResourceLocation flowerbed = ChangedAddonMod.resourceLoc("block/luminara_petals");
        ResourceLocation flowerbedGlow = ChangedAddonMod.resourceLoc("block/luminara_petals_emissive");
        ResourceLocation stem = ChangedAddonMod.resourceLoc("block/luminara_petals_stem");
        ResourceLocation stemGlow = ChangedAddonMod.resourceLoc("block/luminara_petals_stem_emissive");

        for (int i = 1; i <= 4; i++) {
            // Modelos com brilho emissivo
            withExistingParent("luminara_petals_" + i + "_emissive", emissiveFlowerBeds[i - 1])
                    .renderType("minecraft:cutout")
                    .texture("flowerbed", flowerbed)
                    .texture("emissive_flowerbed", flowerbedGlow)
                    .texture("stem", stem)
                    .texture("emissive_stem", stemGlow);

            // Modelos normais (utiliza o modelo de flowerbed padrão do Vanilla)
            withExistingParent("luminara_petals_" + i, mcLoc("block/flowerbed_" + i))
                    .renderType("minecraft:cutout")
                    .texture("flowerbed", flowerbed)
                    .texture("stem", stem);
        }
    }


    protected void generateEmissiveCubeColumn() {
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

                // Map all overlay emissive faces to point to the unified "#side_emissive" variable
                .texture("emissive_down", "#end_emissive")
                .texture("emissive_up", "#end_emissive")
                .texture("emissive_north", "#side_emissive")
                .texture("emissive_south", "#side_emissive")
                .texture("emissive_west", "#side_emissive")
                .texture("emissive_east", "#side_emissive");
    }

    protected void generateEmissiveCube() {
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

    protected void generateEmissiveCubeAll() {
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

                // Map all overlay emissive faces to point to the unified "#all_emissive" variable
                .texture("emissive_down", "#all_emissive")
                .texture("emissive_up", "#all_emissive")
                .texture("emissive_north", "#all_emissive")
                .texture("emissive_south", "#all_emissive")
                .texture("emissive_west", "#all_emissive")
                .texture("emissive_east", "#all_emissive");
    }

    protected void generateEmissiveCrop() {
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
                .texture("#emissive")
                .color(0xFFFFFFFF).emissivity(15, 15).ao(false)
                .end()
                .shade(false)
                .face(net.minecraft.core.Direction.SOUTH)
                .uvs(0.0f, 0.0f, 16.0f, 16.0f)
                .texture("#emissive")
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
                .texture("#emissive")
                .color(0xFFFFFFFF).emissivity(15, 15).ao(false) // 15 block light, no ambient occlusion shadows
                .end()
                .shade(false)
                .face(net.minecraft.core.Direction.EAST)
                .uvs(0.0f, 0.0f, 16.0f, 16.0f)
                .texture("#emissive")
                .color(0xFFFFFFFF).emissivity(15, 15).ao(false) // 15 block light, no ambient occlusion shadows
                .end()
                .end();
    }

    protected void generateEmissivePottedPlant() {
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
                .texture("#plant_emissive") // Points to custom emissive texture template variable
                .color(0xFFFFFFFF)
                .emissivity(15, 15) // Dynamic ForgeFaceData emission coordinates
                .ao(false)
                .end()
                .face(Direction.SOUTH)
                .uvs(0.0f, 0.0f, 16.0f, 16.0f)
                .texture("#plant_emissive")
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
                .texture("#plant_emissive")
                .color(0xFFFFFFFF)
                .emissivity(15, 15)
                .ao(false)
                .end()
                .face(Direction.EAST)
                .uvs(0.0f, 0.0f, 16.0f, 16.0f)
                .texture("#plant_emissive")
                .color(0xFFFFFFFF)
                .emissivity(15, 15)
                .ao(false)
                .end()
                .end();
    }

    protected void generateEmissiveFlowerbeds() {
        // Declaração dos 4 arquivos de modelo que o Pink Petals / Flowerbed utiliza
        for (int i = 1; i <= 4; i++) {
            BlockModelBuilder builder = getBuilder("customs/emissive_flowerbed_" + i)
                    .ao(false)
                    .texture("particle", "#flowerbed");

            switch (i) {
                case 1 -> buildFlowerbed1(builder);
                case 2 -> buildFlowerbed2(builder);
                case 3 -> buildFlowerbed3(builder);
                case 4 -> buildFlowerbed4(builder);
            }
        }
    }

    protected void generateEmissiveMultiface() {
        createEmissiveMultifaceModel("customs/emissive_multiface");
    }

    // ==========================================
    // FLOWERBED 1 (3 Flores, Altura ~3.0)
    // ==========================================
    protected void buildFlowerbed1(BlockModelBuilder builder) {
        // --- Camada de Pétalas (Base) ---
        builder.element().from(0.0f, 2.99f, 0.0f).to(8.0f, 2.99f, 8.0f)
                .face(Direction.UP).uvs(0.0f, 0.0f, 8.0f, 8.0f).texture("#flowerbed").end()
                .face(Direction.DOWN).uvs(0.0f, 8.0f, 8.0f, 0.0f).texture("#flowerbed").end();

        // --- Camada de Pétalas (Emissiva Overlay) ---
        builder.element().from(0.0f, 2.99f, 0.0f).to(8.0f, 2.99f, 8.0f).shade(false)
                .face(Direction.UP).uvs(0.0f, 0.0f, 8.0f, 8.0f).texture("#emissive_flowerbed").color(0xFFFFFFFF).emissivity(15, 15).ao(false).end()
                .face(Direction.DOWN).uvs(0.0f, 8.0f, 8.0f, 0.0f).texture("#emissive_flowerbed").color(0xFFFFFFFF).emissivity(15, 15).ao(false).end();

        // Haste 1 (Cruz 1)
        addStemCross(builder, 4.25f, 0.0f, -2.6f, 4.25f, 2.99f, -1.6f, 3.75f, 0.0f, -2.1f, 4.75f, 2.99f, -2.1f, 0, 4, 1, 7, 0, 0, 0);
        // Haste 2 (Cruz 2)
        addStemCross(builder, 4.9f, 0.0f, 2.3f, 4.9f, 2.99f, 3.3f, 4.4f, 0.0f, 2.8f, 5.4f, 2.99f, 2.8f, 0, 4, 1, 7, 0, 0, 0);
        // Haste 3 (Cruz 3)
        addStemCross(builder, 9.15f, 0.0f, -0.45f, 9.15f, 2.99f, 0.55f, 8.65f, 0.0f, 0.05f, 9.65f, 2.99f, 0.05f, 0, 4, 1, 7, 0, 0, 0);
    }

    // ==========================================
    // FLOWERBED 2 (1 Flor, Altura ~1.0)
    // ==========================================
    protected void buildFlowerbed2(BlockModelBuilder builder) {
        // --- Camada de Pétalas (Base) ---
        builder.element().from(0.0f, 1.0f, 8.0f).to(8.0f, 1.0f, 16.0f)
                .face(Direction.UP).uvs(0.0f, 8.0f, 8.0f, 16.0f).texture("#flowerbed").end()
                .face(Direction.DOWN).uvs(0.0f, 16.0f, 8.0f, 8.0f).texture("#flowerbed").end();

        // --- Camada de Pétalas (Emissiva Overlay) ---
        builder.element().from(0.0f, 1.0f, 8.0f).to(8.0f, 1.0f, 16.0f).shade(false)
                .face(Direction.UP).uvs(0.0f, 8.0f, 8.0f, 16.0f).texture("#emissive_flowerbed").color(0xFFFFFFFF).emissivity(15, 15).ao(false).end()
                .face(Direction.DOWN).uvs(0.0f, 16.0f, 8.0f, 8.0f).texture("#emissive_flowerbed").color(0xFFFFFFFF).emissivity(15, 15).ao(false).end();

        // Haste
        addStemCross(builder, 10.65f, 0.0f, 4.75f, 10.65f, 1.0f, 5.75f, 10.15f, 0.0f, 5.25f, 11.15f, 1.0f, 5.25f, 0, 6, 1, 7, 0, 0, 1);
    }

    // ==========================================
    // FLOWERBED 3 (3 Flores, Altura ~2.0)
    // ==========================================
    protected void buildFlowerbed3(BlockModelBuilder builder) {
        // --- Camada de Pétalas (Base) ---
        builder.element().from(8.0f, 2.0f, 8.0f).to(16.0f, 2.0f, 16.0f)
                .face(Direction.UP).uvs(8.0f, 8.0f, 16.0f, 16.0f).texture("#flowerbed").end()
                .face(Direction.DOWN).uvs(8.0f, 16.0f, 16.0f, 8.0f).texture("#flowerbed").end();

        // --- Camada de Pétalas (Emissiva Overlay) ---
        builder.element().from(8.0f, 2.0f, 8.0f).to(16.0f, 2.0f, 16.0f).shade(false)
                .face(Direction.UP).uvs(8.0f, 8.0f, 16.0f, 16.0f).texture("#emissive_flowerbed").color(0xFFFFFFFF).emissivity(15, 15).ao(false).end()
                .face(Direction.DOWN).uvs(8.0f, 16.0f, 16.0f, 8.0f).texture("#emissive_flowerbed").color(0xFFFFFFFF).emissivity(15, 15).ao(false).end();

        // Haste 1
        addStemCross(builder, 18.15f, 0.0f, 1.4f, 18.15f, 2.0f, 2.4f, 17.65f, 0.0f, 1.9f, 18.65f, 2.0f, 1.9f, 0, 5, 1, 7, 0.5f, 0, 0.5f);
        // Haste 2
        addStemCross(builder, 17.65f, 0.0f, -3.35f, 17.65f, 2.0f, -2.35f, 17.15f, 0.0f, -2.85f, 18.15f, 2.0f, -2.85f, 0, 5, 1, 7, 0, 0, 0);
        // Haste 3
        addStemCross(builder, 13.4f, 0.0f, -0.5f, 13.4f, 2.0f, 0.5f, 12.9f, 0.0f, 0.0f, 13.9f, 2.0f, 0.0f, 0, 5, 1, 7, 0, 0, 0);
    }

    // ==========================================
    // FLOWERBED 4 (1 Flor, Altura ~2.0)
    // ==========================================
    protected void buildFlowerbed4(BlockModelBuilder builder) {
        // --- Camada de Pétalas (Base) ---
        builder.element().from(8.0f, 2.0f, 0.0f).to(16.0f, 2.0f, 8.0f)
                .face(Direction.UP).uvs(8.0f, 0.0f, 16.0f, 8.0f).texture("#flowerbed").end()
                .face(Direction.DOWN).uvs(8.0f, 8.0f, 16.0f, 0.0f).texture("#flowerbed").end();

        // --- Camada de Pétalas (Emissiva Overlay) ---
        builder.element().from(8.0f, 2.0f, 0.0f).to(16.0f, 2.0f, 8.0f).shade(false)
                .face(Direction.UP).uvs(8.0f, 0.0f, 16.0f, 8.0f).texture("#emissive_flowerbed").color(0xFFFFFFFF).emissivity(15, 15).ao(false).end()
                .face(Direction.DOWN).uvs(8.0f, 8.0f, 16.0f, 0.0f).texture("#emissive_flowerbed").color(0xFFFFFFFF).emissivity(15, 15).ao(false).end();

        // Haste
        addStemCross(builder, 12.4f, 0.0f, -7.7f, 12.4f, 2.0f, -6.7f, 11.9f, 0.0f, -7.2f, 12.9f, 2.0f, -7.2f, 0, 5, 1, 7, -1, 0, -3);
    }

    // ==========================================
    // HELPER: Constrói a Haste com Base e Camada Emissiva
    // ==========================================
    protected void addStemCross(BlockModelBuilder builder,
                                float x1, float y1, float z1, float x2, float y2, float z2,
                                float x3, float y3, float z3, float x4, float y4, float z4,
                                float u1, float v1, float u2, float v2,
                                float ox, float oy, float oz) {

        // --- Plano Leste/Oeste (Base) ---
        builder.element().from(x1, y1, z1).to(x2, y2, z2)
                .rotation().origin(ox, oy, oz).axis(Direction.Axis.Y).angle(-45.0f).end()
                .face(Direction.EAST).uvs(u1, v1, u2, v2).texture("#stem").tintindex(1).end()
                .face(Direction.WEST).uvs(u1, v1, u2, v2).texture("#stem").tintindex(1).end();

        // --- Plano Leste/Oeste (Overlay Emissivo) ---
        builder.element().from(x1, y1, z1).to(x2, y2, z2).shade(false)
                .rotation().origin(ox, oy, oz).axis(Direction.Axis.Y).angle(-45.0f).end()
                .face(Direction.EAST).uvs(u1, v1, u2, v2).texture("#emissive_stem").color(0xFFFFFFFF).emissivity(15, 15).ao(false).end()
                .face(Direction.WEST).uvs(u1, v1, u2, v2).texture("#emissive_stem").color(0xFFFFFFFF).emissivity(15, 15).ao(false).end();

        // --- Plano Norte/Sul (Base) ---
        builder.element().from(x3, y3, z3).to(x4, y4, z4)
                .rotation().origin(ox, oy, oz).axis(Direction.Axis.Y).angle(-45.0f).end()
                .face(Direction.NORTH).uvs(u1, v1, u2, v2).texture("#stem").tintindex(1).end()
                .face(Direction.SOUTH).uvs(u1, v1, u2, v2).texture("#stem").tintindex(1).end();

        // --- Plano Norte/Sul (Overlay Emissivo) ---
        builder.element().from(x3, y3, z3).to(x4, y4, z4).shade(false)
                .rotation().origin(ox, oy, oz).axis(Direction.Axis.Y).angle(-45.0f).end()
                .face(Direction.NORTH).uvs(u1, v1, u2, v2).texture("#emissive_stem").color(0xFFFFFFFF).emissivity(15, 15).ao(false).end()
                .face(Direction.SOUTH).uvs(u1, v1, u2, v2).texture("#emissive_stem").color(0xFFFFFFFF).emissivity(15, 15).ao(false).end();
    }

    public BlockModelBuilder createEmissiveMultifaceModel(String name) {
        return getBuilder(name)
                .ao(false)
                .texture("particle", "#base")

                // Base Layer (Standard Lighting)
                .element()
                .from(0.0F, 0.0F, 0.1F)
                .to(16.0F, 16.0F, 0.1F)
                .face(Direction.NORTH)
                .uvs(16.0F, 0.0F, 0.0F, 16.0F)
                .texture("#base")
                .end()
                .face(Direction.SOUTH)
                .uvs(0.0F, 0.0F, 16.0F, 16.0F)
                .texture("#base")
                .end()
                .end()

                // Emissive Layer (Rendered slightly higher to prevent Z-fighting)
                .element()
                .from(0.0F, 0.0F, 0.101F)
                .to(16.0F, 16.0F, 0.101F)
                .face(Direction.NORTH)
                .uvs(16.0F, 0.0F, 0.0F, 16.0F)
                .texture("#emissive")
                .emissivity(15, 15) // Marks this face full-bright in Forge
                .end()
                .face(Direction.SOUTH)
                .uvs(0.0F, 0.0F, 16.0F, 16.0F)
                .texture("#emissive")
                .emissivity(15, 15) // Marks this face full-bright in Forge
                .end()
                .end();
    }

    public BlockModelBuilder createEmissiveMultifaceModelWithBaseTexture(String name, ResourceLocation baseTexture, ResourceLocation emissiveTexture) {
        return getBuilder(name)
                .ao(false)
                .texture("particle", baseTexture)
                .texture("base", baseTexture)
                .texture("emissive", emissiveTexture)

                // Base Layer (Standard Lighting)
                .element()
                .from(0.0F, 0.0F, 0.1F)
                .to(16.0F, 16.0F, 0.1F)
                .face(Direction.NORTH)
                .uvs(16.0F, 0.0F, 0.0F, 16.0F)
                .texture("#base")
                .end()
                .face(Direction.SOUTH)
                .uvs(0.0F, 0.0F, 16.0F, 16.0F)
                .texture("#base")
                .end()
                .end()

                // Emissive Layer (Rendered slightly higher to prevent Z-fighting)
                .element()
                .from(0.0F, 0.0F, 0.101F)
                .to(16.0F, 16.0F, 0.101F)
                .face(Direction.NORTH)
                .uvs(16.0F, 0.0F, 0.0F, 16.0F)
                .texture("#emissive")
                .emissivity(15, 15) // Marks this face full-bright in Forge
                .end()
                .face(Direction.SOUTH)
                .uvs(0.0F, 0.0F, 16.0F, 16.0F)
                .texture("#emissive")
                .emissivity(15, 15) // Marks this face full-bright in Forge
                .end()
                .end();
    }
}
