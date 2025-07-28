package net.foxyas.changedaddon.datagen;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.ltxprogrammer.changed.data.AccessorySlotType;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;


public class AccessoryEntityProvider implements DataProvider {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new Gson();
    private final DataGenerator generator;
    protected final String modId;
    private final Map<String, Appender> appenders = new HashMap<>();

    public AccessoryEntityProvider(DataGenerator generator) {
        this(generator, ChangedAddonMod.MODID);
    }

    public AccessoryEntityProvider(DataGenerator generator, String modId) {
        this.generator = generator;
        this.modId = modId;
    }

    @Override
    public void run(@NotNull HashCache cache) {
        registerEntityAccessories();

        Path basePath = this.generator.getOutputFolder();
        Appender appender;
        for(Map.Entry<String, Appender> entry : appenders.entrySet()){
            appender = entry.getValue();

            if(appender.isInvalid()){
                LOGGER.error("{} Provider: Appender for file {} is missing entities or slots!", getName(), entry.getKey());
                continue;
            }

            Path path = createPath(basePath, entry.getKey());
            try {
                DataProvider.save(GSON, cache, appender.toJson(), path);
            } catch (IOException e) {
                LOGGER.error("Couldn't save accessory entities to file {}", path, e);
            }
        }
    }

    public Appender add(@NotNull String fileName){
        return appenders.computeIfAbsent(fileName, f -> new Appender());
    }

    protected void registerEntityAccessories(){

    }

    private Path createPath(Path base, String fileName){
        return base.resolve("data/" + modId + "/accessories/entities/" + fileName + ".json");
    }

    @Override
    public @NotNull String getName() {
        return "Accessory Entities";
    }

    @ParametersAreNonnullByDefault
    public static class Appender {

        private final Set<EntityType<?>> entities = new HashSet<>();
        private final Set<AccessorySlotType> slots = new ObjectArraySet<>();

        private Appender(){}

        public Appender entity(EntityType<?> entity){
            entities.add(entity);
            return this;
        }

        public Appender entities(EntityType<?> ... entities){
            Collections.addAll(this.entities, entities);
            return this;
        }

        public Appender slot(AccessorySlotType slot){
            slots.add(slot);
            return this;
        }

        public Appender slots(AccessorySlotType... slots){
            Collections.addAll(this.slots, slots);
            return this;
        }

        private boolean isInvalid(){
            return entities.isEmpty() || slots.isEmpty();
        }

        private JsonObject toJson(){
            JsonObject root = new JsonObject();
            JsonArray entityAr = new JsonArray();
            JsonArray slotAr = new JsonArray();
            root.add("entities", entityAr);
            root.add("slots", slotAr);

            for(EntityType<?> type : entities){
                entityAr.add(type.getRegistryName().toString());
            }

            for(AccessorySlotType slot : slots){
                slotAr.add(slot.getRegistryName().toString());
            }

            return root;
        }
    }
}
