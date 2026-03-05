package net.foxyas.changedaddon.datagen.builders;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

public class VariantBuilder {

    private @Nullable String top = null;
    private @Nullable String bottom = null;
    private @Nullable String north = null;
    private @Nullable String south = null;
    private @Nullable String east = null;
    private @Nullable String west = null;
    private @Nullable String extra = null;

    public VariantBuilder top(String model) {
        this.top = model;
        return this;
    }

    public VariantBuilder bottom(String model) {
        this.bottom = model;
        return this;
    }

    public VariantBuilder north(String model) {
        this.north = model;
        return this;
    }

    public VariantBuilder south(String model) {
        this.south = model;
        return this;
    }

    public VariantBuilder east(String model) {
        this.east = model;
        return this;
    }

    public VariantBuilder west(String model) {
        this.west = model;
        return this;
    }

    public VariantBuilder sides(String model) {
        north = south = east = west = model;
        return this;
    }

    public VariantBuilder extra(String model) {
        this.extra = model;
        return this;
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();

        // Alteramos a lógica: se for null, chamamos o método model(null)
        // em vez de usar JsonNull.INSTANCE diretamente.
        json.add("top", model(top));
        json.add("bottom", model(bottom));
        json.add("north", model(north));
        json.add("south", model(south));
        json.add("east", model(east));
        json.add("west", model(west));
        json.add("extra", model(extra));

        return json;
    }

    private JsonObject model(@Nullable String location) {
        JsonObject obj = new JsonObject();
        if (location == null) {
            // Isso força o GSON a renderizar "model": null
            return null;
        } else {
            obj.addProperty("model", location);
        }
        return obj;
    }
}