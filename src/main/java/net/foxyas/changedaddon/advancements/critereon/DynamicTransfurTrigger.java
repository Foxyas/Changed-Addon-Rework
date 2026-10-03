package net.foxyas.changedaddon.advancements.critereon;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.foxyas.changedaddon.advancements.critereon.api.DynamicTransfurPredicate;
import net.ltxprogrammer.changed.advancements.critereon.TransfurPredicate;
import net.ltxprogrammer.changed.advancements.critereon.TransfurTrigger;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DynamicTransfurTrigger extends SimpleCriterionTrigger<DynamicTransfurTrigger.TriggerInstance> {

    private static final ResourceLocation ID = ResourceLocation.parse("changed_addon:dynamic_transfur");

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    @Override
    public @NotNull TriggerInstance createInstance(JsonObject json, @NotNull ContextAwarePredicate playerPredicate, @NotNull DeserializationContext adapter) {
        List<DynamicTransfurPredicate> forms = new ArrayList<>();

        if (json.has("forms")) {
            JsonArray array = GsonHelper.getAsJsonArray(json, "forms");
            for (JsonElement element : array) {
                forms.add(DynamicTransfurPredicate.jsonElement(element));
            }
        }

        return new TriggerInstance(playerPredicate, forms);
    }

    public void trigger(ServerPlayer player, TransfurVariantInstance<?> instance) {
        this.trigger(player, triggerInstance -> triggerInstance.matches(instance));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final List<DynamicTransfurPredicate> forms;

        public TriggerInstance(ContextAwarePredicate player, List<DynamicTransfurPredicate> forms) {
            super(DynamicTransfurTrigger.ID, player);
            this.forms = forms;
        }

        public static TriggerInstance transfurredInto(DynamicTransfurPredicate... forms) {
            return new TriggerInstance(ContextAwarePredicate.ANY, Arrays.stream(forms).toList());
        }

        public boolean matches(TransfurVariantInstance<?> instance) {
            if (instance == null) return false;

            if (!this.forms.isEmpty()) {
                boolean formMatched = false;
                for (DynamicTransfurPredicate entry : this.forms) {
                    if (entry.matches(instance)) {
                        formMatched = true;
                        break;
                    }
                }
                return formMatched;
            }

            return true;
        }

        @Override
        public @NotNull JsonObject serializeToJson(@NotNull SerializationContext context) {
            JsonObject json = super.serializeToJson(context);

            if (!this.forms.isEmpty()) {
                JsonArray jsonArray = new JsonArray();
                for (DynamicTransfurPredicate form : this.forms) {
                    jsonArray.add(form.toJson());
                }
                json.add("forms", jsonArray);
            }

            return json;
        }
    }
}