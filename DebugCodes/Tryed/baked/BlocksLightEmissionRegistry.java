package net.foxyas.changedaddon.client.model.baked;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import static net.foxyas.changedaddon.init.ChangedAddonCodecs.BLOCK_LIGHT_EMISSION_MAP_CODEC;

public class BlocksLightEmissionRegistry extends SimplePreparableReloadListener<Map<ResourceLocation, Integer>> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new Gson();

    protected static final Map<ResourceLocation, Integer> LIGHT_MAP = new HashMap<>();
    private static final String PATH = "block_light_emission";

    public static int getLightEmission(ResourceLocation modelLocation) {
        return LIGHT_MAP.getOrDefault(modelLocation, 0);
    }


    @Override
    protected @NotNull Map<ResourceLocation, Integer> prepare(ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
        Map<ResourceLocation, Integer> foundLights = new HashMap<>();

        // 1. Escaneia todos os arquivos .json dentro de assets/<namespace>/block_light_emission/
        Map<ResourceLocation, Resource> resources = resourceManager.listResources(PATH,
                location -> location.getPath().endsWith(".json"));

        for (Map.Entry<ResourceLocation, Resource> entry : resources.entrySet()) {
            ResourceLocation fileLocation = entry.getKey(); // Ex: changed_addon:block_light_emission/flowers.json
            Resource resource = entry.getValue();

            try (Reader reader = resource.openAsReader()) {
                JsonObject json = GSON.fromJson(reader, JsonObject.class);

                if (json != null) {
                    // 2. O Codec interpreta o JSON inteiro como um mapa de "ResourceLocation : Integer"
                    BLOCK_LIGHT_EMISSION_MAP_CODEC.parse(JsonOps.INSTANCE, json)
                            .resultOrPartial(error -> LOGGER.error("Failed to parse light map file {}: {}", fileLocation, error))
                            .ifPresent(map -> {
                                // 3. Adiciona todas as configurações encontradas dentro desse arquivo no nosso mapa temporário
                                foundLights.putAll(map);
                                LOGGER.info("[Client Only] Loaded {} light definitions from {}", map.size(), fileLocation);
                            });
                }
            } catch (Exception e) {
                LOGGER.error("Failed to load light emission json table from {}", fileLocation, e);
            }
        }

        return foundLights;
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, Integer> objectIn, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
        // 4. Limpa o cache antigo e injeta o super-mapa contendo a união de TODOS os JSONs lidos
        LIGHT_MAP.clear();
        LIGHT_MAP.putAll(objectIn);
        LOGGER.info("[Client Only] Light emission registry reload complete. Total blocks registered: {}", LIGHT_MAP.size());
    }
}