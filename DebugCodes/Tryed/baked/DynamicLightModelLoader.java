package net.foxyas.changedaddon.client.model.baked;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonDeserializationContext;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import java.util.HashMap;
import java.util.Map;

@Deprecated() // IS BUGGED NEED FIX
public class DynamicLightModelLoader implements IGeometryLoader<DynamicLightGeometry> {
    public static final DynamicLightModelLoader INSTANCE = new DynamicLightModelLoader();

    @Override
    public DynamicLightGeometry read(JsonObject jsonObject, JsonDeserializationContext context) {
        // 1. Removemos temporariamente a tag "loader" antes de mandar o Gson ler.
        // Isso impede que o Forge intercepte a si mesmo e cause StackOverflow!
        JsonObject cleanJson = jsonObject.deepCopy();
        if (cleanJson.has("loader")) {
            cleanJson.remove("loader");
        }

        // 2. Agora usamos o contexto oficial do jogo com o JSON limpo.
        // Como não há mais a tag "loader" neste JsonObject, o Minecraft lê as heranças,
        // texturas e parents perfeitamente via Resource Pack SEM gerar loop infinito!
        BlockModel baseModel = context.deserialize(cleanJson, BlockModel.class);

        // 3. Mapeia: Índice do Elemento -> Nível de Brilho (0 a 15)
        Map<Integer, Integer> elementLightMap = new HashMap<>();

        // Lemos a tag "elements" do jsonObject original, onde a nossa propriedade ainda existe
        if (jsonObject.has("elements")) {
            JsonArray elements = jsonObject.getAsJsonArray("elements");
            for (int i = 0; i < elements.size(); i++) {
                JsonObject element = elements.get(i).getAsJsonObject();

                if (element.has("light_emission")) {
                    JsonElement lightElement = element.get("light_emission");
                    int lightValue = 0;

                    if (lightElement.isJsonPrimitive() && lightElement.getAsJsonPrimitive().isNumber()) {
                        lightValue = Math.max(0, Math.min(15, lightElement.getAsInt()));
                    } else if (lightElement.isJsonPrimitive() && lightElement.getAsJsonPrimitive().isBoolean()) {
                        lightValue = lightElement.getAsBoolean() ? 15 : 0;
                    }

                    if (lightValue > 0) {
                        elementLightMap.put(i, lightValue);
                    }
                }
            }
        }

        return new DynamicLightGeometry(baseModel, elementLightMap);
    }
}