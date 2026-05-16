package net.foxyas.changedaddon.datagen.recipes.crop;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.darkhax.bookshelf.api.serialization.Serializers;
import net.darkhax.botanypots.data.displaystate.DisplayState;
import net.darkhax.botanypots.data.displaystate.SimpleDisplayState;
import net.darkhax.botanypots.data.displaystate.TransitionalDisplayState;
import net.darkhax.botanypots.data.recipes.soil.BasicSoilSerializer;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class AutoSoilRecipeBuilder {
    private final Ingredient input;
    private DisplayState displayState;
    private float growthModifier = 1.0f;
    private final List<String> categories = new ArrayList<>();
    private int lightLevel = 0;

    public AutoSoilRecipeBuilder(Ingredient input) {
        this.input = input;
    }

    public static AutoSoilRecipeBuilder input(Item item) {
        return new AutoSoilRecipeBuilder(Ingredient.of(item));
    }

    public AutoSoilRecipeBuilder setDisplay(DisplayState state) {
        this.displayState = state;
        return this;
    }

    public AutoSoilRecipeBuilder setGrowthModifier(float modifier) {
        this.growthModifier = modifier;
        return this;
    }

    public AutoSoilRecipeBuilder addCategory(String category) {
        this.categories.add(category);
        return this;
    }

    public AutoSoilRecipeBuilder setLightLevel(int level) {
        this.lightLevel = level;
        return this;
    }

    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept(new Result(id, input, displayState, growthModifier, categories, lightLevel));
    }

    private record Result(ResourceLocation id, Ingredient input, DisplayState displayState, 
                          float growthModifier, List<String> categories, int lightLevel) implements FinishedRecipe {

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("input", input.toJson());
            
            // Reutilizando a lógica de display do AutoCrop para evitar o "state" extra
            json.add("display", serializeDisplays(displayState));

            json.addProperty("growthModifier", growthModifier);

            JsonArray categoryArray = new JsonArray();
            categories.forEach(categoryArray::add);
            json.add("categories", categoryArray);

            json.addProperty("lightLevel", lightLevel);
        }

        private JsonElement serializeDisplays(DisplayState state) {
            if (state instanceof SimpleDisplayState simple) {
                return serializeDisplayState(simple);
            } else if (state instanceof TransitionalDisplayState transitional) {
                return serializeTransitionalDisplayState(transitional);
            }
            return new JsonObject();
        }

        private JsonObject serializeDisplayState(SimpleDisplayState state) {
            BlockState renderState = state.getRenderState(0);
            JsonElement json = Serializers.BLOCK_STATE.toJSON(renderState);
            
            if (json != null && json.isJsonObject()) {
                return json.getAsJsonObject();
            }

            JsonObject obj = new JsonObject();
            ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(renderState.getBlock());
            obj.addProperty("block", blockId != null ? blockId.toString() : "minecraft:air");

            if (!renderState.getProperties().isEmpty()) {
                JsonObject propsObj = new JsonObject();
                for (Map.Entry<Property<?>, Comparable<?>> entry : renderState.getValues().entrySet()) {
                    Property<?> prop = entry.getKey();
                    propsObj.addProperty(prop.getName(), Util.getName(prop, entry.getValue()));
                }
                obj.add("properties", propsObj);
            }
            return obj;
        }

        private JsonObject serializeTransitionalDisplayState(TransitionalDisplayState state) {
            JsonObject obj = new JsonObject();
            JsonArray phases = new JsonArray();
            obj.add("type", Serializers.RESOURCE_LOCATION.toJSON(state.getSerializer().getId()));
            for (DisplayState phase : state.phases) {
                if (phase instanceof SimpleDisplayState simple) {
                    phases.add(serializeDisplayState(simple));
                }
            }
            obj.add("phases", phases);
            return obj;
        }

        @Override
        public ResourceLocation getId() { return id; }

        @Override
        public RecipeSerializer<?> getType() { 
            return BasicSoilSerializer.SERIALIZER; 
        }

        @Nullable @Override public JsonObject serializeAdvancement() { return null; }
        @Nullable @Override public ResourceLocation getAdvancementId() { return null; }
    }

    private static class Util {
        @SuppressWarnings("unchecked")
        public static <T extends Comparable<T>> String getName(Property<T> prop, Comparable<?> value) {
            return prop.getName((T) value);
        }
    }
}