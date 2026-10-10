package net.foxyas.changedaddon.init;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.process.bestiary.BestiaryEntry;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.TransfurVariantDiet;
import net.minecraft.core.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public abstract class ChangedAddonRegistry<T> implements Registry<T> {

    /**
     * This is a Util/Creation Class used to create or "point" to the registries.
     * We use a "RegistryHolder" to hold the registry key and values.
     * We Recomend Checking if the registryHolder uses a "Per level" registry before trying to reach for any value.
     * */

    private static final Logger LOGGER = LogManager.getLogger(ChangedAddonRegistry.class);
    private static final int MAX_VAR_INT = Integer.MAX_VALUE - 1;
    private static final HashMap<ResourceKey<Registry<?>>, Supplier<IForgeRegistry<?>>> REGISTRY_HOLDERS = new HashMap<>();

    public static final RegistryHolder<TransfurVariantDiet> TRANSFUR_VARIANT_DIETS = createDataDrivenHolder(ChangedAddonTransfurDiets.TRANSFUR_VARIANT_DIET_KEY);
    public static final RegistryHolder<BestiaryEntry> BESTIARY_ENTRIES = createDataDrivenHolder(ChangedAddonBestiaryEntries.BESTIARY_ENTRIES_KEY);

    private static <T> @NotNull RegistryHolder<T> createDataDrivenHolder(ResourceKey<Registry<T>> key) {
        return new RegistryHolder<>(key, true);
    }

    private static <T> void createRegistry(NewRegistryEvent event, ResourceKey<? extends Registry<T>> key) {
        createRegistry(event, key, null, null);
    }

    private static <T> void createRegistry(NewRegistryEvent event, ResourceKey<? extends Registry<T>> key,
                                           @Nullable Consumer<RegistryBuilder<T>> additionalBuilder,
                                           @Nullable Consumer<IForgeRegistry<T>> onFill) {
        var builder = makeRegistry(key);
        if (additionalBuilder != null)
            additionalBuilder.accept(builder);
        Supplier<IForgeRegistry<T>> holder = event.create(builder, onFill);
        REGISTRY_HOLDERS.put((ResourceKey) key, holder::get);
        LOGGER.info("Created registry {}", key);
    }

    private static <T> ResourceKey<Registry<T>> createDataDrivenRegistry(DataPackRegistryEvent.NewRegistry event, ResourceKey<Registry<T>> registryKey, Codec<T> codec, @Nullable Codec<T> networkCodec) {
        event.dataPackRegistry(registryKey, codec, networkCodec);
        return registryKey;
    }

    private static <T> ResourceKey<Registry<T>> createDataDrivenRegistry(DataPackRegistryEvent.NewRegistry event, RegistryHolder<T> registryKey, Codec<T> codec, @Nullable Codec<T> networkCodec) {
        return createDataDrivenRegistry(event, registryKey.key, codec, networkCodec);
    }

    static <T> Class<T> c(Class<?> cls) {
        return (Class<T>) cls;
    }

    private static <T> RegistryBuilder<T> makeRegistry(ResourceKey<? extends Registry<T>> key) {
        return RegistryBuilder.<T>of(key.location()).setMaxID(MAX_VAR_INT);
    }

    private ChangedAddonRegistry() {
    }

    private static <T> ResourceKey<Registry<T>> registryKey(String name) {
        return ResourceKey.createRegistryKey(ChangedAddonMod.resourceLoc(name));
    }

    public static class RegistryHolder<T> implements Supplier<IForgeRegistry<T>> {
        protected boolean isDataDriven = false;
        protected final ResourceKey<Registry<T>> key;
        protected final IdMap<T> idMap = new IdMap<>() {
            @Override
            public int getId(@NotNull T value) {
                return RegistryHolder.this.getID(value);
            }

            @Override
            public @org.jetbrains.annotations.Nullable T byId(int id) {
                return RegistryHolder.this.getValue(id);
            }

            @Override
            public int size() {
                return RegistryHolder.this.get().getValues().size();
            }

            @Override
            public @NotNull Iterator<T> iterator() {
                return RegistryHolder.this.get().getValues().iterator();
            }
        };
        protected final HolderLookup.RegistryLookup<T> lookup = new HolderLookup.RegistryLookup<>() {
            @Override
            public @NotNull ResourceKey<? extends Registry<? extends T>> key() {
                return RegistryHolder.this.key;
            }

            @Override
            public @NotNull Lifecycle registryLifecycle() {
                return Lifecycle.stable();
            }

            @Override
            public @NotNull Stream<Holder.Reference<T>> listElements() {
                final var raw = getRaw();
                return raw.getKeys().stream().map(raw::getHolder).map(holder -> (Holder.Reference<T>) holder.get());
            }

            @Override
            public @NotNull Stream<HolderSet.Named<T>> listTags() {
                final var raw = getRaw();
                final var tags = raw.tags();
                return tags.getTagNames().map(tagKey -> {
                    var tag = tags.getTag(tagKey);
                    var set = HolderSet.emptyNamed(this, tagKey);
                    set.bind(tag.stream().map(raw::getHolder).filter(Optional::isPresent).map(Optional::get).toList());
                    return set;
                });
            }

            @Override
            public @NotNull Optional<Holder.Reference<T>> get(@NotNull ResourceKey<T> resourceKey) {
                return (Optional<Holder.Reference<T>>) (Object) getRaw().getHolder(resourceKey);
            }

            @Override
            public @NotNull Optional<HolderSet.Named<T>> get(@NotNull TagKey<T> tagKey) {
                final var raw = getRaw();
                var tag = raw.tags().getTag(tagKey);
                var set = HolderSet.emptyNamed(this, tagKey);
                set.bind(tag.stream().map(raw::getHolder).filter(Optional::isPresent).map(Optional::get).toList());
                return Optional.of(set);
            }
        };

        public RegistryHolder(ResourceKey<Registry<T>> key) {
            this.key = key;
            this.isDataDriven = false;
        }

        public RegistryHolder(ResourceKey<Registry<T>> key, boolean isDataDriven) {
            this.key = key;
            this.isDataDriven = isDataDriven;
        }

        public boolean isDataDrivenRegistry() {
            return isDataDriven;
        }

        public RegistryHolder<T> asDataDrivenOnly() {
            this.isDataDriven = true;
            return this;
        }

        public ResourceKey<Registry<T>> getRegistryKey() {
            return key;
        }

        public ResourceLocation getKey(T value) {
            return get().getKey(value);
        }

        public Optional<ResourceLocation> getKeySafe(T value) {
            return Optional.ofNullable(get().getKey(value));
        }

        public @Nullable T getValue(ResourceLocation key) {
            return get().getValue(key);
        }

        public Set<ResourceLocation> getKeys() {
            return get().getKeys();
        }

        public int getID(T value) {
            return getRaw().getID(value);
        }

        public T getValue(int id) {
            return getRaw().getValue(id);
        }

        public Optional<Holder<T>> getHolder(T value) {
            return get().getHolder(value);
        }

        public Optional<Holder<T>> getHolder(ResourceLocation name) {
            return get().getHolder(name);
        }

        public Optional<Holder<T>> getHolder(ResourceKey<T> name) {
            return get().getHolder(name);
        }

        public void writeRegistryObject(FriendlyByteBuf buffer, T value) {
            buffer.writeInt(getRaw().getID(value));
        }

        public T readRegistryObject(FriendlyByteBuf buffer) {
            return getRaw().getValue(buffer.readInt());
        }

        @Override
        public IForgeRegistry<T> get() {
            if (isDataDrivenRegistry()) {
                throw new UnsupportedOperationException("Cannot direct access data-driven registry '" + key.location() + "'. A Level or RegistryAccess instance is required.");
            }
            if (REGISTRY_HOLDERS.isEmpty())
                throw new IllegalStateException("Cannot access registries before creation");
            return (IForgeRegistry<T>) REGISTRY_HOLDERS.get(key).get();
        }

        public ForgeRegistry<T> getRaw() {
            if (isDataDrivenRegistry()) {
                throw new UnsupportedOperationException("Cannot direct access data-driven registry '" + key.location() + "'. A Level or RegistryAccess instance is required.");
            }
            if (REGISTRY_HOLDERS.isEmpty())
                throw new IllegalStateException("Cannot access registries before creation");
            return (ForgeRegistry<T>) REGISTRY_HOLDERS.get(key).get();
        }

        public DeferredRegister<T> createDeferred(String modId) {
            return DeferredRegister.create(key, modId);
        }

        public ResourceKey<T> createResourceKey(ResourceLocation resourceLocation) {
            return ResourceKey.create(key, resourceLocation);
        }

        public IdMap<T> asIdMap() {
            return idMap;
        }

        public HolderLookup<T> asLookup() {
            return lookup;
        }
    }

    private static class ClearableObjectIntIdentityMap<I> extends IdMapper<I> {
        void clear() {
            this.tToId.clear();
            this.idToT.clear();
            this.nextId = 0;
        }

        @SuppressWarnings("unused")
        void remove(I key) {
            boolean hadId = this.tToId.containsKey(key);
            int prev = this.tToId.removeInt(key);
            if (hadId) {
                this.idToT.set(prev, null);
            }
        }
    }
}