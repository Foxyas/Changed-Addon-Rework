package net.foxyas.changedaddon.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.init.ChangedTags;
import net.ltxprogrammer.changed.data.AccessorySlotType;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static net.ltxprogrammer.changed.init.ChangedAccessorySlots.*;


public class AccessoryEntityProvider implements DataProvider {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // The Changed Mod Objects are registered too late soo is need to make static supplier a method
    private static final Supplier<AccessorySlotType[]> humanoidSlots = () -> new AccessorySlotType[]{BODY.get(), FULL_BODY.get(), LEGS.get(), HANDS.get(), HEAD.get(), FACE.get(), NECK.get()};

    protected final String modId;
    private final DataGenerator generator;
    private final Map<String, Appender> appenders = new HashMap<>();

    public AccessoryEntityProvider(DataGenerator generator) {
        this(generator, ChangedAddonMod.MODID);
    }

    public AccessoryEntityProvider(DataGenerator generator, String modId) {
        this.generator = generator;
        this.modId = modId;
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
        registerEntityAccessories();

        List<CompletableFuture<?>> list = new ArrayList<>();
        Path basePath = this.generator.getPackOutput().getOutputFolder();
        Appender appender;
        for (Map.Entry<String, Appender> entry : appenders.entrySet()) {
            appender = entry.getValue();

            if (appender.isInvalid()) {
                LOGGER.error("{} Provider: Appender for file {} is missing entities or slots!", getName(), entry.getKey());
                continue;
            }

            Path path = createPath(basePath, entry.getKey());
            list.add(DataProvider.saveStable(cache, appender.toJson(), path));
        }

        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }

    public Appender add(@NotNull String fileName) {
        return appenders.computeIfAbsent(fileName, f -> new Appender());
    }

    protected void registerEntityAccessories() {
        this.add(ChangedTags.AccessoryEntities.HUMANOIDS)
                //.entities(ChangedAddonEntities.canUseAccessories().toArray(new EntityType[0]))
                .entityTypesTag(ChangedAddonTags.EntityTypes.CAN_USE_ACCESSORIES)
                .slots(humanoidSlots.get());
    }

    private Path createPath(Path base, String fileName) {
        return base.resolve("data/" + modId + "/accessories/entities/" + fileName + ".json");
    }

    @Override
    public @NotNull String getName() {
        return "Accessory Entities";
    }

    @ParametersAreNonnullByDefault
    public static class Appender {

        private final Set<EntityType<?>> entities = new HashSet<>();
        private final Set<TagKey<EntityType<?>>> entityTypesTags = new HashSet<>();
        private final Set<AccessorySlotType> slots = new ObjectArraySet<>();

        private Appender() {
        }

        public Appender entity(EntityType<?> entity) {
            entities.add(entity);
            return this;
        }

        public Appender entities(EntityType<?>... entities) {
            Collections.addAll(this.entities, entities);
            return this;
        }

        public Appender entityTypesTag(TagKey<EntityType<?>> entityTypeTagKey) {
            this.entityTypesTags.add(entityTypeTagKey);
            return this;
        }

        @SafeVarargs
        public final Appender entityTypesTags(TagKey<EntityType<?>>... entityTypeTagKey) {
            Collections.addAll(this.entityTypesTags, entityTypeTagKey);
            return this;
        }

        public Appender slot(AccessorySlotType slot) {
            slots.add(slot);
            return this;
        }

        public Appender slots(AccessorySlotType... slots) {
            Collections.addAll(this.slots, slots);
            return this;
        }

        private boolean isInvalid() {
            return (entities.isEmpty() && entityTypesTags.isEmpty()) || slots.isEmpty();
        }

        private JsonObject toJson() {
            JsonObject root = new JsonObject();
            JsonArray entityAr = new JsonArray();
            JsonArray slotAr = new JsonArray();
            root.add("entities", entityAr);
            root.add("slots", slotAr);

            for (EntityType<?> type : entities) {
                entityAr.add(ForgeRegistries.ENTITY_TYPES.getKey(type).toString());
            }

            for (TagKey<EntityType<?>> typeTagKey : entityTypesTags) {
                entityAr.add("#" + typeTagKey.location());
            }

            for (AccessorySlotType slot : slots) {
                slotAr.add(ChangedRegistry.ACCESSORY_SLOTS.getKey(slot).toString());
            }

            return root;
        }
    }
}
