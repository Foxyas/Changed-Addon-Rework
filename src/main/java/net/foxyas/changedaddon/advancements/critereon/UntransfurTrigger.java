package net.foxyas.changedaddon.advancements.critereon;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UntransfurTrigger extends SimpleCriterionTrigger<UntransfurTrigger.TriggerInstance> {

    private static final ResourceLocation ID = ChangedAddonMod.resourceLoc("untransfur");

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    @Override
    protected UntransfurTrigger.@NotNull TriggerInstance createInstance(@NotNull JsonObject json, @NotNull ContextAwarePredicate playerPredicate, @NotNull DeserializationContext pDeserializationContext) {
        // Parse mobEffect array (or single element)
        List<ResourceLocation> mobEffects = parseResourceLocationList(json, "mobEffect");

        // Parse item array (or single element)
        List<ResourceLocation> items = parseResourceLocationList(json, "item");

        return new TriggerInstance(playerPredicate, mobEffects, items);
    }

    /**
     * Helper to accept both single string or array of strings for JSON fields.
     */
    private static List<ResourceLocation> parseResourceLocationList(JsonObject json, String fieldName) {
        List<ResourceLocation> list = new ArrayList<>();
        if (json.has(fieldName)) {
            if (json.get(fieldName).isJsonArray()) {
                json.getAsJsonArray(fieldName).forEach(element ->
                        list.add(ResourceLocation.parse(element.getAsString()))
                );
            } else {
                list.add(ResourceLocation.parse(json.get(fieldName).getAsString()));
            }
        }
        return list;
    }

    /**
     * Call this method from your Untransfur code event/action.
     */
    public void trigger(ServerPlayer player, MobEffect activeEffect, ItemStack stackUsed) {
        this.trigger(player, instance -> instance.matches(activeEffect, stackUsed));
    }

    /**
     * Call this method from your Untransfur code event/action.
     */
    public void trigger(ServerPlayer player, MobEffect activeEffect) {
        this.trigger(player, instance -> instance.matches(activeEffect, null));
    }

    /**
     * Call this method from your Untransfur code event/action.
     */
    public void trigger(ServerPlayer player, ItemStack itemStack) {
        this.trigger(player, instance -> instance.matches(null, itemStack));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final List<ResourceLocation> mobEffects;
        private final List<ResourceLocation> items;

        public TriggerInstance(ContextAwarePredicate player, List<ResourceLocation> mobEffects, List<ResourceLocation> items) {
            super(UntransfurTrigger.ID, player);
            this.mobEffects = mobEffects;
            this.items = items;
        }

        public boolean matches(MobEffect activeEffect, ItemStack stackUsed) {
            // Check mob effect condition (if specified)
            boolean effectMatches;
            if (!this.mobEffects.isEmpty() && activeEffect != null) {
                ResourceLocation effectId = ForgeRegistries.MOB_EFFECTS.getKey(activeEffect);
                effectMatches = effectId != null && !this.mobEffects.contains(effectId);
            } else {
                effectMatches = false;
            }

            // Check item condition (if specified)
            boolean itemMatches;
            if (!this.items.isEmpty() && stackUsed != null) {
                if (stackUsed.isEmpty()) return false;
                ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stackUsed.getItem());
                itemMatches = itemId != null && this.items.contains(itemId);
            } else {
                itemMatches = false;
            }

            return itemMatches || effectMatches;
        }

        @Override
        public @NotNull JsonObject serializeToJson(@NotNull SerializationContext context) {
            JsonObject json = super.serializeToJson(context);

            if (!this.mobEffects.isEmpty()) {
                if (this.mobEffects.size() == 1) {
                    json.addProperty("mobEffect", this.mobEffects.get(0).toString());
                } else {
                    JsonArray array = new JsonArray();
                    for (ResourceLocation effect : this.mobEffects) {
                        array.add(effect.toString());
                    }
                    json.add("mobEffect", array);
                }
            }

            if (!this.items.isEmpty()) {
                if (this.items.size() == 1) {
                    json.addProperty("item", this.items.get(0).toString());
                } else {
                    JsonArray array = new JsonArray();
                    for (ResourceLocation item : this.items) {
                        array.add(item.toString());
                    }
                    json.add("item", array);
                }
            }

            return json;
        }
    }
}