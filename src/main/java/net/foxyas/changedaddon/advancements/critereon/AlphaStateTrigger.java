package net.foxyas.changedaddon.advancements.critereon;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.foxyas.changedaddon.advancements.critereon.api.AlphaStatePredicate;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlphaStateTrigger extends SimpleCriterionTrigger<AlphaStateTrigger.TriggerInstance> {

    private static final ResourceLocation ID = ResourceLocation.parse("changed_addon:alpha_state");

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    @Override
    public @NotNull TriggerInstance createInstance(JsonObject json, @NotNull ContextAwarePredicate playerPredicate, @NotNull DeserializationContext adapter) {
        // Complex support: can parse an array of alpha state criteria, 
        // while also falling back or handling a single object for the simple version.
        List<AlphaStatePredicate> states = new ArrayList<>();

        if (json.has("states")) {
            JsonArray array = GsonHelper.getAsJsonArray(json, "states");
            for (JsonElement element : array) {
                states.add(AlphaStatePredicate.fromJson(element));
            }
        } else if (json.has("state")) {
            states.add(AlphaStatePredicate.fromJson(json.get("state")));
        }

        return new TriggerInstance(playerPredicate, states);
    }

    public void trigger(ServerPlayer player, float scale, boolean active) {
        this.trigger(player, triggerInstance -> triggerInstance.matches(scale, active));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final List<AlphaStatePredicate> states;

        public TriggerInstance(ContextAwarePredicate player, List<AlphaStatePredicate> states) {
            super(AlphaStateTrigger.ID, player);
            this.states = states;
        }

        // Simple helper factory
        public static TriggerInstance simple(AlphaStatePredicate state) {
            return new TriggerInstance(ContextAwarePredicate.ANY, List.of(state));
        }

        // Complex helper factory (accepts multiple criteria rules)
        public static TriggerInstance complex(AlphaStatePredicate... states) {
            return new TriggerInstance(ContextAwarePredicate.ANY, Arrays.stream(states).toList());
        }

        public boolean matches(float scale, boolean active) {
            if (this.states.isEmpty()) {
                return true;
            }

            // Complex matching logic: matches if ANY of the specified state rules evaluate to true
            for (AlphaStatePredicate state : this.states) {
                if (state.matches(scale, active)) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public @NotNull JsonObject serializeToJson(@NotNull SerializationContext context) {
            JsonObject json = super.serializeToJson(context);

            if (this.states.size() == 1) {
                json.add("state", this.states.get(0).toJson());
            } else if (!this.states.isEmpty()) {
                JsonArray jsonArray = new JsonArray();
                for (AlphaStatePredicate state : this.states) {
                    jsonArray.add(state.toJson());
                }
                json.add("states", jsonArray);
            }

            return json;
        }
    }
}