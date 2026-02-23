package net.foxyas.changedaddon.datagen.customData;

import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AdvancementWriter {

    public List<CompletableFuture<?>> completableFutureList = new ArrayList<>();

    public AdvancementWriter() {
    }

    public static CompletableFuture<?> writeSingle(
            CachedOutput cache,
            PackOutput output,
            ResourceLocation id,
            Advancement.Builder builder) {
        try {
            JsonObject json = builder.serializeToJson();

            Path path = output.getOutputFolder()
                    .resolve("data")
                    .resolve(id.getNamespace())
                    .resolve("advancements")
                    .resolve(id.getPath() + ".json");

            return DataProvider.saveStable(cache, json, path);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write advancement " + id, e);
        }
    }

    public static CompletableFuture<?> writeSingle(
            CachedOutput cache,
            PackOutput output,
            String group,
            ResourceLocation id,
            Advancement.Builder builder) {
        try {
            JsonObject json = builder.serializeToJson();

            Path path = output.getOutputFolder()
                    .resolve("data")
                    .resolve(id.getNamespace())
                    .resolve("advancements")
                    .resolve(group)
                    .resolve(id.getPath() + ".json");

            return DataProvider.saveStable(cache, json, path);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write advancement " + id, e);
        }
    }

    public static CompletableFuture<?> writeSingle(
            CachedOutput cache,
            PackOutput output,
            Path groupPaths,
            ResourceLocation id,
            Advancement.Builder builder) {
        try {
            JsonObject json = builder.serializeToJson();

            Path path = output.getOutputFolder()
                    .resolve("data")
                    .resolve(id.getNamespace())
                    .resolve("advancements")
                    .resolve(groupPaths)
                    .resolve(id.getPath() + ".json");

            return DataProvider.saveStable(cache, json, path);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write advancement " + id, e);
        }
    }

    public void write(
            CachedOutput cache,
            PackOutput output,
            ResourceLocation id,
            Advancement.Builder builder) {
        try {
            JsonObject json = builder.serializeToJson();

            Path path = output.getOutputFolder()
                    .resolve("data")
                    .resolve(id.getNamespace())
                    .resolve("advancements")
                    .resolve(id.getPath() + ".json");

            completableFutureList.add(DataProvider.saveStable(cache, json, path));
        } catch (Exception e) {
            throw new RuntimeException("Failed to write advancement " + id, e);
        }
    }

    public void write(
            CachedOutput cache,
            PackOutput output,
            String group,
            ResourceLocation id,
            Advancement.Builder builder) {
        try {
            JsonObject json = builder.serializeToJson();

            Path path = output.getOutputFolder()
                    .resolve("data")
                    .resolve(id.getNamespace())
                    .resolve("advancements")
                    .resolve(group)
                    .resolve(id.getPath() + ".json");

            completableFutureList.add(DataProvider.saveStable(cache, json, path));
        } catch (Exception e) {
            throw new RuntimeException("Failed to write advancement " + id, e);
        }
    }

    public void write(
            CachedOutput cache,
            PackOutput output,
            Path groupPaths,
            ResourceLocation id,
            Advancement.Builder builder) {
        try {
            JsonObject json = builder.serializeToJson();

            Path path = output.getOutputFolder()
                    .resolve("data")
                    .resolve(id.getNamespace())
                    .resolve("advancements")
                    .resolve(groupPaths)
                    .resolve(id.getPath() + ".json");

            completableFutureList.add(DataProvider.saveStable(cache, json, path));
        } catch (Exception e) {
            throw new RuntimeException("Failed to write advancement " + id, e);
        }
    }
}
