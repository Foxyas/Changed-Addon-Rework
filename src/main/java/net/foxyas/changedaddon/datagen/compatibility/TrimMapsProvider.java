package net.foxyas.changedaddon.datagen.compatibility;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class TrimMapsProvider implements DataProvider {
    private final PackOutput.PathProvider pathProvider;

    public TrimMapsProvider(PackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "maps/unchecked");
    }

    protected abstract void buildMaps(MapBuilder builder);

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
        List<CompletableFuture<?>> futures = new ArrayList<>();
        MapBuilder builder = new MapBuilder();

        buildMaps(builder);

        // Gera e salva os arquivos JSON
        builder.files.forEach((location, jsonObject) -> {
            futures.add(DataProvider.saveStable(cache, jsonObject, this.pathProvider.json(location)));
        });

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    @Override
    public @NotNull String getName() {
        return "Trimmed Maps Provider";
    }

    protected static class MapBuilder {
        final Map<ResourceLocation, JsonObject> files = new HashMap<>();

        public void addFile(ResourceLocation fileLocation, Map<String, String> pairs) {
            JsonObject json = new JsonObject();
            JsonObject pairsObj = new JsonObject();
            
            pairs.forEach(pairsObj::addProperty);
            json.add("pairs", pairsObj);
            
            files.put(fileLocation, json);
        }
    }
}