package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.block.LuminarCrystalLarge;
import net.foxyas.changedaddon.block.LuminaraLogBlock;
import net.foxyas.changedaddon.block.StackableCanBlock;
import net.foxyas.changedaddon.block.advanced.TimedKeypadBlock;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static net.foxyas.changedaddon.init.ChangedAddonBlocks.*;

public class BlockStateProvider extends net.minecraftforge.client.model.generators.BlockStateProvider {

    private static final ResourceLocation CAN = blockLoc(ChangedAddonMod.resourceLoc("stackable_can"));

    public BlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ChangedAddonMod.MODID, exFileHelper);
    }

    private static ResourceLocation blockLoc(ResourceLocation loc) {
        return ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + loc.getPath());
    }

    private static ResourceLocation blockLoc(ResourceLocation loc, String path) {
        return ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + path + "/" + loc.getPath());
    }

    private static int getXRotation(Direction dir) {
        return switch (dir) {
            case DOWN -> -90;
            case UP -> 90;
            default -> 0;
        };
    }

    private static int getYRotation(Direction dir) {
        return switch (dir) {
            case NORTH -> 180;
            case EAST -> 270;
            case WEST -> 90;
            default -> 0;
        };
    }

    @Override
    protected void registerStatesAndModels() {
        horizontalBlock(ADVANCED_CATALYZER);
        horizontalBlock(ADVANCED_UNIFUSER);
        horizontalBlock(CATALYZER);
        horizontalBlock(UNIFUSER);
        simpleBlock(BLUE_WOLF_CRYSTAL_BLOCK);
        simpleBlock(BLUE_WOLF_CRYSTAL_SMALL);
        simpleBlock(CONTAINMENT_CONTAINER, BlockStateProperties.WATERLOGGED);
        horizontalBlock(DARK_LATEX_PUDDLE);
        simpleBlock(PAINITE_BLOCK);
        simpleBlock(DEEPSLATE_PAINITE_ORE);
        simpleBlock(DORMANT_DARK_LATEX);
        simpleBlock(DORMANT_WHITE_LATEX);
        stackableCan(FOXTA_CAN);
        simpleBlock(GOO_CORE);
        horizontalBlock(INFORMANT_BLOCK);
        simpleBlock(IRIDIUM_BLOCK);
        simpleBlock(DEEPSLATE_IRIDIUM_ORE);
        simpleBlock(LATEX_INSULATOR);
        simpleBlock(LITIX_CAMONIA_FLUID, BlockStateProperties.LEVEL);
        simpleBlock(LUMINARA_BLOOM);
        simpleBlock(ORANGE_WOLF_CRYSTAL_BLOCK);
        simpleBlock(ORANGE_WOLF_CRYSTAL_SMALL);
        simpleBlock(REINFORCED_CROSS_BLOCK);
        simpleBlock(REINFORCED_WALL);
        simpleBlock(REINFORCED_WALL_CAUTION);
        simpleBlock(REINFORCED_WALL_SILVER_STRIPED);
        simpleBlock(REINFORCED_WALL_SILVER_TILED);
        stackableCan(SNEPSI_CAN);
        simpleBlock(WALL_WHITE_CRACKED);
        simpleBlock(WHITE_WOLF_CRYSTAL_BLOCK);
        simpleBlock(WHITE_WOLF_CRYSTAL_SMALL);
        horizontalBlock(WOLF_PLUSHY);
        horizontalBlock(DARK_LATEX_WOLF_PLUSHY);
        simpleBlock(YELLOW_WOLF_CRYSTAL_BLOCK);
        simpleBlock(YELLOW_WOLF_CRYSTAL_SMALL);
        simpleBlock(POTTED_LUMINARA_BLOOM);
        simpleBlock(POTTED_LUMINARA_SAPLING);

        timedKeypad();

        pillarBlockWithVariants(WOLF_CRYSTAL_PILLAR, 2, 0);
        createMultiface(COVER_BLOCK, false);
        createMultiface(DARK_LATEX_COVER_BLOCK, false);
        createMultiface(WHITE_LATEX_COVER_BLOCK, false);

        luminaraPillarBlock(LUMINARA_LOG, "");
        luminaraPillarBlock(STRIPPED_LUMINARA_LOG, "");
        simpleBlock(LUMINARA_LEAVES);

        ConfiguredModel[] model = {new ConfiguredModel(models().getExistingFile(blockLoc(LUMINARA_SAPLING.getId())))};
        getVariantBuilder(LUMINARA_SAPLING.get()).forAllStates(state -> model);

        largeLuminarCrystalAnimatedWithItem();
    }

    private void timedKeypad() {
        ResourceLocation loc = blockLoc(TIMED_KEYPAD.getId());
        ModelFile file = models().getExistingFile(loc);
        ModelFile locked = models().getExistingFile(withSuffix(loc, "_locked"));

        getVariantBuilder(TIMED_KEYPAD.get()).forAllStatesExcept(state ->
                new ConfiguredModel[]{new ConfiguredModel(state.getValue(TimedKeypadBlock.POWERED) ? file : locked, 0,
                        (int) ((state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 270) % 360), false)}
        );
    }

    private ResourceLocation withSuffix(ResourceLocation loc, String suffix) {
        return ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), loc.getPath() + suffix);
    }

    private ResourceLocation withPrefix(ResourceLocation loc, String prefix) {
        return ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), prefix + loc.getPath());
    }

    private ResourceLocation withSuffixOn(ResourceLocation loc, String part, String suffix) {
        String path = loc.getPath();

        if (!path.contains(part)) {
            return loc;
        }

        int index = path.indexOf(part) + part.length();

        String start = path.substring(0, index);
        String end = path.substring(index);

        String newPath = start + end + suffix;

        return ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), newPath);
    }

    private ResourceLocation withPrefixOn(ResourceLocation loc, String part, String prefix) {
        String path = loc.getPath();

        if (!path.contains(part)) {
            return loc;
        }

        int index = path.indexOf(part) + part.length();

        String start = path.substring(0, index);
        String end = path.substring(index);

        String newPath = start + prefix + end;

        return ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), newPath);
    }

    private void simpleBlock(RegistryObject<? extends Block> block, Property<?>... ignore) {
        ConfiguredModel[] model = new ConfiguredModel[]{new ConfiguredModel(models().getExistingFile(blockLoc(block.getId())))};

        getVariantBuilder(block.get()).forAllStatesExcept(state -> model, ignore);
    }

    private void horizontalBlock(RegistryObject<? extends HorizontalDirectionalBlock> block, Property<?>... ignore) {
        ResourceLocation loc = blockLoc(block.getId());
        Block bl = block.get();
        ModelFile file = models().getExistingFile(loc);

        getVariantBuilder(bl).forAllStatesExcept(state ->
                        ConfiguredModel.builder().modelFile(file)
                                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                                .build(),
                ignore);
    }

    private void stackableCan(RegistryObject<? extends StackableCanBlock> can) {
        ResourceLocation loc = blockLoc(can.getId());

        ModelFile[] models = new ModelFile[4];
        for (int i = 0; i < 4; i++) {
            models[i] = models().withExistingParent(withSuffix(loc, "/can_" + (i + 1)).toString(), withSuffix(CAN, "/can_" + (i + 1)))
                    .texture("texture", loc);
        }

        getVariantBuilder(can.get()).forAllStatesExcept(state ->
                        horizontalRotatedModelAr(models[state.getValue(StackableCanBlock.CANS) - 1], state.getValue(StackableCanBlock.FACING)),
                StackableCanBlock.WATERLOGGED);
    }

    @Contract("_, _ -> new")
    private ConfiguredModel @NotNull [] horizontalRotatedModelAr(ModelFile file, Direction direction) {
        return new ConfiguredModel[]{horizontalRotatedModel(file, direction)};
    }

    private ConfiguredModel horizontalRotatedModel(ModelFile file, Direction direction) {
        return switch (direction) {
            case EAST -> new ConfiguredModel(file, 0, 90, false);
            case SOUTH -> new ConfiguredModel(file, 0, 180, false);
            case WEST -> new ConfiguredModel(file, 0, 270, false);
            default -> new ConfiguredModel(file);
        };
    }

    private void simpleWithVariants(RegistryObject<? extends Block> block, int variants, int itemModelIndex) {
        Block b = block.get();
        ResourceLocation loc = withSuffix(blockLoc(block.getId()), "/variant");

        ModelFile[] models = new ModelFile[variants];
        for (int i = 0; i < variants; i++) {
            models[i] = models().getExistingFile(withSuffix(loc, "_" + i));
        }

        ConfiguredModel[] confModels = configure(models, ConfiguredModel::new);

        simpleBlock(b, confModels);
        simpleBlockItem(b, models[itemModelIndex]);
    }

    private void pillarBlockWithVariants(RegistryObject<? extends RotatedPillarBlock> pillar, int variants, int itemModelIndex) {
        RotatedPillarBlock block = pillar.get();
        ResourceLocation loc = withSuffix(blockLoc(pillar.getId()), "/variant");

        ModelFile[] models = new ModelFile[variants];
        for (int i = 0; i < variants; i++) {
            models[i] = models().getExistingFile(withSuffix(loc, "_" + i));
        }

        getVariantBuilder(block).forAllStatesExcept(state ->
                switch (state.getValue(BlockStateProperties.AXIS)) {
                    case Y -> configure(models, ConfiguredModel::new);
                    case Z -> configure(models, model -> new ConfiguredModel(model, 90, 0, false));
                    case X -> configure(models, model -> new ConfiguredModel(model, 90, 90, false));
                }
        );

        simpleBlockItem(block, models[itemModelIndex]);
    }

    private void simplePillarBlock(RegistryObject<? extends RotatedPillarBlock> pillar) {
        simplePillarBlock(pillar, "");
    }

    private void simplePillarBlock(RegistryObject<? extends RotatedPillarBlock> pillar, String customPath) {
        RotatedPillarBlock block = pillar.get();
        ResourceLocation blockId = pillar.getId();

        // Defines the textures paths: "block/your_block_side" and "block/your_block_top"
        ResourceLocation blockLoc = customPath.isEmpty() ? blockLoc(blockId) : blockLoc(blockId, customPath);
        ResourceLocation sideTexture = withSuffix(blockLoc, "_side");
        ResourceLocation topTexture = withSuffix(blockLoc, "_top");

        // Generates the .json model file automatically using forge's cubePillar builder
        ModelFile model = models().cubeColumn(blockId.getPath(), sideTexture, topTexture);

        // Maps the BlockState AXIS property to the correct model rotations
        getVariantBuilder(block).forAllStatesExcept(state ->
                        switch (state.getValue(BlockStateProperties.AXIS)) {
                            case Y -> new ConfiguredModel[]{new ConfiguredModel(model)};
                            case Z -> new ConfiguredModel[]{new ConfiguredModel(model, 90, 0, false)};
                            case X -> new ConfiguredModel[]{new ConfiguredModel(model, 90, 90, false)};
                        },
                BlockStateProperties.WATERLOGGED // Ignores waterlogged property if present to avoid duplicating state variants
        );

        // Automatically generates the item model corresponding to this pillar
        simpleBlockItem(block, model);
    }

    private void luminaraPillarBlock(RegistryObject<? extends RotatedPillarBlock> pillar, String customPath) {
        RotatedPillarBlock block = pillar.get();
        ResourceLocation blockId = pillar.getId();

        ResourceLocation blockLoc = customPath.isEmpty() ? blockLoc(blockId) : blockLoc(blockId, customPath);

        ModelFile defaultModel = models().getExistingFile(blockLoc);

        // Maps the BlockState AXIS property to the correct model rotations
        getVariantBuilder(block).forAllStatesExcept(state -> {
                    ModelFile model = defaultModel;
                    if (state.getValue(LuminaraLogBlock.ACTIVE)) {
//                        String id = pillar.getId().toString();
//                        model = models().cubeColumn("active_" + blockId.getPath(),
//                                withPrefixOn(sideTexture, id, "active_"),
//                                withPrefixOn(topTexture, id, "active_"));
                        model = models().getExistingFile(blockLoc(withPrefix(blockId, "active_")));
                    }

                    return switch (state.getValue(BlockStateProperties.AXIS)) {
                        case Y -> new ConfiguredModel[]{new ConfiguredModel(model)};
                        case Z -> new ConfiguredModel[]{new ConfiguredModel(model, 90, 0, false)};
                        case X -> new ConfiguredModel[]{new ConfiguredModel(model, 90, 90, false)};
                    };
                },
                BlockStateProperties.WATERLOGGED // Ignores waterlogged property if present to avoid duplicating state variants
        );

        // Automatically generates the item model corresponding to this pillar
        simpleBlockItem(block, defaultModel);
    }

    private ConfiguredModel[] configure(ModelFile[] models, Function<ModelFile, ConfiguredModel> config) {
        ConfiguredModel[] out = new ConfiguredModel[models.length];
        for (int i = 0; i < models.length; i++) {
            out[i] = config.apply(models[i]);
        }
        return out;
    }

    private void createMultiface(RegistryObject<? extends Block> block) {
        MultiPartBlockStateBuilder builder = getMultipartBuilder(block.get());
        ResourceLocation loc = blockLoc(block.getId());

        BlockState state = block.get().defaultBlockState();
        ModelFile model = models().getExistingFile(loc);
        for (Direction dir : Direction.values()) {
            BooleanProperty prop = PipeBlock.PROPERTY_BY_DIRECTION.get(dir);
            if (!state.hasProperty(prop)) continue;

            builder.part()
                    .modelFile(model)
                    .rotationX(getXRotation(dir))
                    .rotationY(getYRotation(dir))
                    .addModel()
                    .condition(prop, true);
        }

        itemModels().getBuilder(BuiltInRegistries.ITEM.getKey(block.get().asItem()).getPath()).parent(model);
    }

    private void largeLuminarCrystalWithItem() {
        ResourceLocation loc = blockLoc(LUMINAR_CRYSTAL_LARGE.getId());

        ModelFile top = models().cross(loc + "_top", withSuffix(loc, "_top")).renderType("cutout");
        ModelFile bottom = models().cross(loc + "_bottom", withSuffix(loc, "_bottom")).renderType("cutout");
        ModelFile bottomHearted = models().cross(loc + "_bottom_hearted", withSuffix(loc, "_bottom_hearted")).renderType("cutout");
        getVariantBuilder(LUMINAR_CRYSTAL_LARGE.get()).forAllStatesExcept(state ->
                        new ConfiguredModel[]{rotatedModel(state.getValue(LuminarCrystalLarge.HALF) == Half.TOP ? top :
                                state.getValue(LuminarCrystalLarge.HEARTED) ? bottomHearted : bottom, state.getValue(LuminarCrystalLarge.FACING))}
                , LuminarCrystalLarge.WATERLOGGED);

        itemModels().getBuilder(ChangedAddonItems.LUMINAR_CRYSTAL_LARGE.getId().toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", withSuffix(loc, "_top"));
    }

    private void largeLuminarCrystalAnimatedWithItem() {
        ResourceLocation loc = blockLoc(LUMINAR_CRYSTAL_LARGE.getId());

        ModelFile top = models().cross(loc + "_top", withSuffix(loc, "_top" + "_animated")).renderType("cutout");
        ModelFile bottom = models().cross(loc + "_bottom", withSuffix(loc, "_bottom" + "_animated")).renderType("cutout");
        ModelFile bottomHearted = models().cross(loc + "_bottom_hearted", withSuffix(loc, "_bottom_hearted" + "_animated")).renderType("cutout");
        getVariantBuilder(LUMINAR_CRYSTAL_LARGE.get()).forAllStatesExcept(state ->
                        new ConfiguredModel[]{rotatedModel(state.getValue(LuminarCrystalLarge.HALF) == Half.TOP ? top :
                                state.getValue(LuminarCrystalLarge.HEARTED) ? bottomHearted : bottom, state.getValue(LuminarCrystalLarge.FACING))}
                , LuminarCrystalLarge.WATERLOGGED);

        itemModels().getBuilder(ChangedAddonItems.LUMINAR_CRYSTAL_LARGE.getId().toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", withSuffix(loc, "_top"));
    }

    private ConfiguredModel rotatedModel(ModelFile file, Direction direction) {
        return switch (direction) {
            case UP -> new ConfiguredModel(file, 0, 0, false);
            case DOWN -> new ConfiguredModel(file, 180, 0, false);
            case NORTH -> new ConfiguredModel(file, 90, 0, false);
            case EAST -> new ConfiguredModel(file, 90, 90, false);
            case SOUTH -> new ConfiguredModel(file, 90, 180, false);
            case WEST -> new ConfiguredModel(file, 90, 270, false);
        };
    }

    private void createMultiface(RegistryObject<? extends Block> block, boolean generatedItem) {
        MultiPartBlockStateBuilder builder = getMultipartBuilder(block.get());
        ResourceLocation loc = blockLoc(block.getId());

        BlockState state = block.get().defaultBlockState();
        ModelFile model = models().getExistingFile(loc);
        for (Direction dir : Direction.values()) {
            BooleanProperty prop = PipeBlock.PROPERTY_BY_DIRECTION.get(dir);
            if (!state.hasProperty(prop)) continue;

            builder.part()
                    .modelFile(model)
                    .rotationX(getXRotation(dir))
                    .rotationY(getYRotation(dir))
                    .addModel()
                    .condition(prop, true);
        }

        if (generatedItem)
            itemModels().getBuilder(BuiltInRegistries.ITEM.getKey(block.get().asItem()).getPath()).parent(model);
    }


    public BlockModelBuilder emissiveCube(String name,
                                          ResourceLocation down,
                                          ResourceLocation up,
                                          ResourceLocation north,
                                          ResourceLocation south,
                                          ResourceLocation east,
                                          ResourceLocation west,
                                          ResourceLocation emissive_down,
                                          ResourceLocation emissive_up,
                                          ResourceLocation emissive_north,
                                          ResourceLocation emissive_south,
                                          ResourceLocation emissive_east,
                                          ResourceLocation emissive_west

    ) {
        return models().withExistingParent(name, BlockModelProvider.EMISSIVE_CUBE)
                .texture("down", down)
                .texture("up", up)
                .texture("north", north)
                .texture("south", south)
                .texture("east", east)
                .texture("west", west)
                .texture("emissive_down", down)
                .texture("emissive_up", up)
                .texture("emissive_north", north)
                .texture("emissive_south", south)
                .texture("emissive_east", east)
                .texture("emissive_west", west);
    }


    public BlockModelBuilder emissiveCubeAll(String name, ResourceLocation sides, ResourceLocation glowSides) {
        return models().withExistingParent(name, BlockModelProvider.EMISSIVE_CUBE_ALL)
                .texture("all", sides)
                .texture("all_glow", glowSides);
    }

    public BlockModelBuilder emissiveColumn(String name,
                                            ResourceLocation side,
                                            ResourceLocation glowSide,
                                            ResourceLocation end,
                                            ResourceLocation glowEnd) {
        return models().withExistingParent(name, BlockModelProvider.EMISSIVE_CUBE_ALL)
                .texture("end", end)
                .texture("end_glow", glowEnd)
                .texture("side", side)
                .texture("side_glow", glowSide);
    }

    public BlockModelBuilder emissiveCross(String name, ResourceLocation cross, ResourceLocation glow) {
        return models().withExistingParent(name, BlockModelProvider.EMISSIVE_CUBE_ALL)
                .texture("cross", cross)
                .texture("glow", glow);
    }

}
