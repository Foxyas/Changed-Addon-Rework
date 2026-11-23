package net.foxyas.changedaddon.advancements.critereon.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.util.GsonHelper;

public class AlphaStatePredicate {
    private final MinMaxBounds.Doubles scale;
    private final Boolean active;

    public AlphaStatePredicate(MinMaxBounds.Doubles scale, Boolean active) {
        this.scale = scale;
        this.active = active;
    }

    public static AlphaStatePredicate fromJson(JsonElement element) {
        if (element == null || element.isJsonNull()) {
            return new AlphaStatePredicate(MinMaxBounds.Doubles.ANY, null);
        }

        JsonObject json = element.getAsJsonObject();
        MinMaxBounds.Doubles scale = MinMaxBounds.Doubles.fromJson(json.get("scale"));
        
        Boolean active = json.has("active") ? GsonHelper.getAsBoolean(json, "active") : null;

        return new AlphaStatePredicate(scale, active);
    }

    public boolean matches(float scaleValue, boolean activeValue) {
        if (!this.scale.matches(scaleValue)) {
            return false;
        }
        if (this.active != null && this.active != activeValue) {
            return false;
        }
        return true;
    }

    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        if (!this.scale.isAny()) {
            json.add("scale", this.scale.serializeToJson());
        }
        if (this.active != null) {
            json.addProperty("active", this.active);
        }
        return json;
    }
}