package net.foxyas.changedaddon.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.ltxprogrammer.changed.advancements.critereon.TransfurPredicate;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SleepNextAPlushyTrigger extends SimpleCriterionTrigger<SleepNextAPlushyTrigger.Instance> {
    private static final ResourceLocation ID = ChangedAddonMod.resourceLoc("sleep_next_to_plushy");

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    @Override
    protected @NotNull Instance createInstance(@NotNull JsonObject json, @NotNull ContextAwarePredicate playerPredicate, @NotNull DeserializationContext context) {
        // pega o nome se existir
        JsonElement elem = json.get("name");
        String name = (elem != null && elem.isJsonPrimitive() && elem.getAsJsonPrimitive().isString())
                ? elem.getAsString()
                : null;

        // pega o transfurPredicate se existir, senão ANY
        TransfurPredicate transfurPredicate = json.has("transfur")
                ? TransfurPredicate.fromJson(json.get("transfur"))
                : TransfurPredicate.ANY;

        boolean booleanPredicate = json.has("needWakeUp") && json.get("needWakeUp").getAsBoolean();

        return new Instance(playerPredicate, transfurPredicate, booleanPredicate, name);
    }

    public void trigger(ServerPlayer player, String name) {
        this.trigger(player, instance -> instance.matches(name));
    }

    public void trigger(ServerPlayer player, TransfurVariantInstance<?> transfurVariant, @Nullable String name) {
        this.trigger(player, instance -> instance.matches(transfurVariant, name));
    }

    public void trigger(ServerPlayer player, TransfurVariantInstance<?> transfurVariant, @Nullable String name, boolean waked) {
        this.trigger(player, instance -> instance.matches(transfurVariant, name, waked));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        @Nullable
        private final String name;
        private final TransfurPredicate transfurPredicate;
        private final boolean needWakeUp;

        public Instance(@NotNull ContextAwarePredicate playerPredicate, TransfurPredicate transfurPredicate, boolean needWakeUp, @Nullable String name) {
            super(ID, playerPredicate);
            this.transfurPredicate = transfurPredicate != null ? transfurPredicate : TransfurPredicate.ANY;
            this.name = name;
            this.needWakeUp = needWakeUp;
        }

        public boolean matches(TransfurVariantInstance<?> transfurVariantInstance, @Nullable String name, boolean waked) {
            // se não especificou nome no json, só checa o transfur
            if (this.name == null) {
                if (this.needWakeUp && waked) {
                    return this.transfurPredicate.matches(transfurVariantInstance);
                } else if (!this.needWakeUp) {
                    return this.transfurPredicate.matches(transfurVariantInstance);
                }
            }

            if (this.needWakeUp) {
                if (waked) {
                    // se nome foi definido, precisa bater
                    return name != null && name.equals(this.name)
                            && this.transfurPredicate.matches(transfurVariantInstance);
                } else {
                    return false;
                }
            } else {
                // se nome foi definido, precisa bater
                return name != null && name.equals(this.name)
                        && this.transfurPredicate.matches(transfurVariantInstance);
            }
        }

        public boolean matches(TransfurVariantInstance<?> transfurVariantInstance, @Nullable String name) {
            // se não especificou nome no json, só checa o transfur
            if (this.name == null)
                return this.transfurPredicate.matches(transfurVariantInstance);

            // se nome foi definido, precisa bater
            return name != null && name.equals(this.name) && this.transfurPredicate.matches(transfurVariantInstance);
        }

        public boolean matches(@Nullable String name) {
            if (this.name == null)
                return true; // se não especificou nome no json, qualquer nome serve
            return name != null && name.equals(this.name);
        }

        @Override
        public @NotNull JsonObject serializeToJson(@NotNull SerializationContext context) {
            JsonObject jsonObject = super.serializeToJson(context);
            if (name != null && !name.isEmpty())
                jsonObject.addProperty("name", name);
            if (transfurPredicate != null && transfurPredicate != TransfurPredicate.ANY)
                jsonObject.add("transfur", transfurPredicate.serializeToJson());

            jsonObject.addProperty("needWakeUp", needWakeUp);

            return jsonObject;
        }
    }
}
