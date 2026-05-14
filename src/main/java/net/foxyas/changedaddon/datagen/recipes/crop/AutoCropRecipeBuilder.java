package net.foxyas.changedaddon.datagen.recipes.crop;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.darkhax.bookshelf.api.serialization.Serializers;
import net.darkhax.botanypots.data.displaystate.DisplayState;
import net.darkhax.botanypots.data.displaystate.SimpleDisplayState;
import net.darkhax.botanypots.data.displaystate.TransitionalDisplayState;
import net.darkhax.botanypots.data.recipes.crop.BasicCropSerializer;
import net.darkhax.botanypots.data.recipes.crop.HarvestEntry;
import net.darkhax.botanypots.data.recipes.crop.SerializerHarvestEntry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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

public class AutoCropRecipeBuilder {
    private final Ingredient seed;
    private final List<String> categories = new ArrayList<>();
    private int growthTicks = 1200;
    private int lightLevel = 0;
    private final List<HarvestEntry> drops = new ArrayList<>();
    private final List<DisplayState> displayStates = new ArrayList<>();

    public AutoCropRecipeBuilder(Ingredient seed) {
        this.seed = seed;
    }

    public static AutoCropRecipeBuilder seed(Item item) {
        return new AutoCropRecipeBuilder(Ingredient.of(item));
    }

    public AutoCropRecipeBuilder addCategory(String category) {
        this.categories.add(category);
        return this;
    }

    public AutoCropRecipeBuilder setGrowthTicks(int ticks) {
        this.growthTicks = ticks;
        return this;
    }

    public AutoCropRecipeBuilder setLightLevel(int level) {
        this.lightLevel = level;
        return this;
    }

    public AutoCropRecipeBuilder addDisplayState(DisplayState state) {
        this.displayStates.add(state);
        return this;
    }

    public AutoCropRecipeBuilder addDrop(Item item, float chance, int minRolls, int maxRolls) {
        return this.addDrop(new ItemStack(item), chance, minRolls, maxRolls);
    }

    public AutoCropRecipeBuilder addDrop(ItemStack stack, float chance, int minRolls, int maxRolls) {
        this.drops.add(new HarvestEntry(chance, stack, minRolls, maxRolls));
        return this;
    }

    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept(new Result(id, seed, categories, growthTicks, drops, displayStates, lightLevel));
    }

    private record Result(ResourceLocation id, Ingredient seed, List<String> categories, int growthTicks,
                          List<HarvestEntry> drops, List<DisplayState> displayStates,
                          int lightLevel) implements FinishedRecipe {

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("seed", seed.toJson());

            JsonArray categoryArray = new JsonArray();
            categories.forEach(categoryArray::add);
            json.add("categories", categoryArray);

            json.addProperty("growthTicks", growthTicks);

            // Serialização de drops usando o serializer oficial
            JsonArray dropArray = new JsonArray();
            for (HarvestEntry entry : drops) {
                dropArray.add(SerializerHarvestEntry.SERIALIZER.toJSON(entry));
            }
            json.add("drops", dropArray);

            // Serialização CUSTOM de DisplayState para evitar o objeto "state" extra
            JsonArray displayArray = new JsonArray();
            for (DisplayState state : displayStates) {
                //recursiveDisplayMap(state, displayArray);
                serializeDisplays(displayArray, state);
            }

            json.add("display", displayArray);

            json.addProperty("lightLevel", lightLevel);
        }

//        /**
//         * Mapeador que auto-detecta estados transicionais e os achata em uma lista simples.
//         */
//        private void recursiveDisplayMap(DisplayState state, JsonArray array) {
//            if (state instanceof SimpleDisplayState simple) {
//                array.add(serializeDisplayState(simple));
//            } else if (state instanceof TransitionalDisplayState transitional) {
//                // Se for transicional, chama a função para cada fase interna (recursão)
//                for (DisplayState phase : transitional.phases) {
//                    recursiveDisplayMap(phase, array);
//                }
//            }
//        }

        private void serializeDisplays(JsonArray jsonArray, DisplayState state) {
            if (state instanceof SimpleDisplayState simpleDisplayState) {
                jsonArray.add(serializeDisplayState(simpleDisplayState));
            } else if (state instanceof TransitionalDisplayState transitionalDisplayState) {
                jsonArray.add(serializeTransitionalDisplayState(transitionalDisplayState));
            }
        }

        /**
         * Transforma o DisplayState em um JsonObject contendo apenas 'block' e 'properties'.
         */
        private JsonObject serializeDisplayState(SimpleDisplayState state) {
            JsonObject obj = new JsonObject();

            // Registra o ID do bloco (ex: casualties_cubed:glow_fruit_bush)
            BlockState renderState = state.getRenderState(0);

            JsonElement json = Serializers.BLOCK_STATE.toJSON(renderState);
            if (json != null) {
                return json.getAsJsonObject();
            }

            ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(renderState.getBlock());
            assert blockId != null;
            obj.addProperty("block", blockId.toString());

            // Registra as propriedades (ex: age: 0)
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

        private JsonElement serializeBlockStateFromDisplay(SimpleDisplayState state) {
            BlockState renderState = state.getRenderState(0);
            return Serializers.BLOCK_STATE.toJSON(renderState);
        }

        /**
         * Transforma o DisplayState em um JsonObject contendo apenas 'block' e 'properties'.
         */
        private JsonObject serializeTransitionalDisplayState(TransitionalDisplayState state) {
            JsonObject obj = new JsonObject();
            JsonArray phases = new JsonArray();

            obj.add("type", Serializers.RESOURCE_LOCATION.toJSON(state.getSerializer().getId()));
            for (DisplayState phase : state.phases) {
                if (phase instanceof SimpleDisplayState simpleDisplayState) {
                    phases.add(serializeDisplayState(simpleDisplayState));
                }
            }
            obj.add("phases", phases);

            return obj;
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return BasicCropSerializer.SERIALIZER;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }

    // Classe utilitária interna para lidar com nomes de propriedades
    private static class Util {
        @SuppressWarnings("unchecked")
        public static <T extends Comparable<T>> String getName(Property<T> prop, Comparable<?> value) {
            return prop.getName((T) value);
        }
    }
}