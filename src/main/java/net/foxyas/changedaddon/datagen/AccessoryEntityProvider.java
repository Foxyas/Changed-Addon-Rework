package net.foxyas.changedaddon.datagen;

import com.google.common.collect.Multimap;
import com.google.common.collect.HashMultimap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.ltxprogrammer.changed.Changed;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;


public class AccessoryEntityProvider { /*

//TODO
} implements DataProvider {
    private final DataGenerator generator;
    private final Map<ResourceLocation, Multimap<EntityType<?>, ResourceLocation>> data = new HashMap<>();

    public AccessoryEntityProvider(DataGenerator generator) {
        this.generator = generator;
    }

    public void add(ResourceLocation id, EntityType<?> entityType, ResourceLocation slotId) {
        this.data.computeIfAbsent(id, k -> HashMultimap.create()).put(entityType, slotId);
    }

    @Override
    public void run(HashCache cache) throws IOException {
        Path basePath = this.generator.getOutputFolder().resolve("accessories/entities");

        for (var entry : data.entrySet()) {
            JsonObject root = new JsonObject();
            JsonArray entities = new JsonArray();
            JsonArray slots = new JsonArray();

            entry.getValue().asMap().forEach((entityType, slotSet) -> {
                entities.add(EntityType.getKey(entityType).toString());
                slotSet.forEach(slotId -> slots.add(slotId.toString()));
            });

            root.add("entities", entities);
            root.add("slots", slots);

            Path path = basePath.resolve(entry.getKey().getPath() + ".json");
            DataProvider.save(new Gson().from, cache, root, path);
        }
    }

    @Override
    public String getName() {
        return "Accessory Entities";
    }*/
}
