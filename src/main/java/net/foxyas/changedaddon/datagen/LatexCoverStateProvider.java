package net.foxyas.changedaddon.datagen;

import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import net.foxyas.changedaddon.datagen.builders.CoverStateBuilder;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.minecraft.Util;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class LatexCoverStateProvider implements DataProvider {
    protected final PackOutput output;
    protected final String modid;

    private final Map<ResourceLocation, CoverStateBuilder> blocks = new HashMap<>();

    public LatexCoverStateProvider(PackOutput output, ExistingFileHelper helper, String modid) {
        this.output = output;
        this.modid = modid;
    }

    protected void registerStatesAndModels() {
        simpleNoModelBlock(ChangedAddonBlocks.FOXTA_CAN.get());
    }

    public CoverStateBuilder block(Block block) {
        ResourceLocation id = ForgeRegistries.BLOCKS.getKey(block);
        CoverStateBuilder builder = new CoverStateBuilder(id);
        blocks.put(id, builder);
        return builder;
    }

    public CoverStateBuilder simpleBlock(Block block) {
        CoverStateBuilder coverStateBuilder = block(block);
        coverStateBuilder.variant("");
        return coverStateBuilder;
    }

    public CoverStateBuilder simpleNoModelBlock(Block block) {
        CoverStateBuilder builder = block(block);

        List<Property<?>> properties = new ArrayList<>(block.defaultBlockState().getProperties());

        buildCombinations(builder, properties, 0, new LinkedHashMap<>());

        return builder;
    }

    private void buildCombinations(CoverStateBuilder builder,
                                   List<Property<?>> properties,
                                   int index,
                                   Map<Property<?>, Comparable<?>> current) {

        if (index >= properties.size()) {
            String predicate = buildPredicate(current);
            builder.variant(predicate).sides(null);
            return;
        }

        Property<?> property = properties.get(index);

        for (Comparable<?> value : property.getPossibleValues()) {
            current.put(property, value);
            buildCombinations(builder, properties, index + 1, current);
        }

        current.remove(property);
    }

    private String buildPredicate(Map<Property<?>, Comparable<?>> values) {
        return values.entrySet().stream()
                .map(entry -> entry.getKey().getName() + "=" + entry.getValue())
                .collect(Collectors.joining(","));
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
        registerStatesAndModels();

        Path root = output.getOutputFolder();

        ArrayList<CompletableFuture<?>> completed = new ArrayList<>();

        for (var entry : blocks.entrySet()) {
            ResourceLocation id = entry.getKey();
            JsonObject json = entry.getValue().toJson();

            Path path = root.resolve(
                    "assets/" + id.getNamespace() + "/latex_cover_model_blockstates/" + id.getPath() + ".json"
            );

            completed.add(saveStable(cache, json, path));
        }
        return CompletableFuture.allOf(completed.toArray(new CompletableFuture[0]));
    }

    public static CompletableFuture<?> saveStable(CachedOutput pOutput, JsonElement pJson, Path pPath) {
        return CompletableFuture.runAsync(() -> {
            try {
                ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
                HashingOutputStream hashingoutputstream = new HashingOutputStream(Hashing.sha1(), bytearrayoutputstream);

                try (JsonWriter jsonwriter = new JsonWriter(new OutputStreamWriter(hashingoutputstream, StandardCharsets.UTF_8))) {
                    jsonwriter.setSerializeNulls(true);
                    jsonwriter.setIndent("  ");
                    GsonHelper.writeValue(jsonwriter, pJson, KEY_COMPARATOR);
                }

                pOutput.writeIfNeeded(pPath, bytearrayoutputstream.toByteArray(), hashingoutputstream.hash());
            } catch (IOException ioexception) {
                LOGGER.error("Failed to save file to {}", pPath, ioexception);
            }

        }, Util.backgroundExecutor());
    }

    @Override
    public @NotNull String getName() {
        return "Latex Cover States";
    }
}