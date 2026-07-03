package net.foxyas.changedaddon.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class SimpleIDTrigger extends SimpleCriterionTrigger<SimpleIDTrigger.SimpleIDTriggerInstance> {

    private static final ResourceLocation ID = ChangedAddonMod.resourceLoc("simple_id_trigger");
    public static final String INVALID_ID = "INVALID-ID";

    @Override
    protected @NotNull SimpleIDTriggerInstance createInstance(@NotNull JsonObject jsonObject, @NotNull ContextAwarePredicate pPredicate, @NotNull DeserializationContext pDeserializationContext) {

        JsonElement elem = jsonObject.get("name");
        String name = (elem != null && elem.isJsonPrimitive() && elem.getAsJsonPrimitive().isString())
                ? elem.getAsString()
                : INVALID_ID;

        if (name.equals(INVALID_ID)) {
            return null;
        }

        return new SimpleIDTriggerInstance(pPredicate, name);
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayer player, String id) {
        this.trigger(player, instance -> instance.matches(player, id));
    }

    public static class SimpleIDTriggerInstance extends AbstractCriterionTriggerInstance {


        protected final String name;

        public SimpleIDTriggerInstance(@NotNull ContextAwarePredicate contextAwarePredicate, String name) {
            super(ID, contextAwarePredicate);
            this.name = name;
        }


        // Method para verificar as condições
        public boolean matches(ServerPlayer player, String id) {
            return this.name.equals(id);
        }
    }
}
