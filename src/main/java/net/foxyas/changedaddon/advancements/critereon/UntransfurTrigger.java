package net.foxyas.changedaddon.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public class UntransfurTrigger extends SimpleCriterionTrigger<UntransfurTrigger.TriggerInstance> {

    private static final ResourceLocation ID = ChangedAddonMod.resourceLoc("untransfur");

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    @Override
    protected UntransfurTrigger.@NotNull TriggerInstance createInstance(@NotNull JsonObject json, @NotNull ContextAwarePredicate playerPredicate, @NotNull DeserializationContext deserializationContext) {
        ItemPredicate itemPredicate = ItemPredicate.fromJson(json.get("item"));
        MobEffectsPredicate mobEffectPredicate = MobEffectsPredicate.fromJson(json.get("mob_effect"));

        JsonElement elem = json.get("extra_context");
        Optional<String> extraContext = Optional.ofNullable((elem != null && elem.isJsonPrimitive() && elem.getAsJsonPrimitive().isString()) ? elem.getAsString() : null);

        return new TriggerInstance(playerPredicate, itemPredicate, mobEffectPredicate, extraContext);
    }

    public void trigger(ServerPlayer player, MobEffectInstance activeEffect, ItemStack stackUsed, String context) {
        this.trigger(player, instance -> instance.matches(activeEffect, stackUsed, context));
    }

    public void trigger(ServerPlayer player, MobEffect activeEffect, ItemStack stackUsed) {
        MobEffectInstance effectInstance = activeEffect != null ? player.getEffect(activeEffect) : null;
        this.trigger(player, instance -> instance.matches(effectInstance, stackUsed, null));
    }

    public void trigger(ServerPlayer player, MobEffect activeEffect) {
        MobEffectInstance effectInstance = activeEffect != null ? player.getEffect(activeEffect) : null;
        this.trigger(player, instance -> instance.matches(effectInstance, ItemStack.EMPTY, null));
    }

    public void trigger(ServerPlayer player, ItemStack itemStack) {
        this.trigger(player, instance -> instance.matches(null, itemStack, null));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final ItemPredicate itemPredicate;
        private final MobEffectsPredicate mobEffectPredicate;
        private final Optional<String> extraContext;

        public TriggerInstance(ContextAwarePredicate player, ItemPredicate itemPredicate, MobEffectsPredicate mobEffectPredicate, Optional<String> extraContext) {
            super(UntransfurTrigger.ID, player);
            this.itemPredicate = itemPredicate;
            this.mobEffectPredicate = mobEffectPredicate;
            this.extraContext = extraContext;
        }

        public boolean matches(MobEffectInstance activeEffect, ItemStack stackUsed, String context) {
            if (this.extraContext.isPresent() && (context == null || !this.extraContext.get().equalsIgnoreCase(context))) {
                return false;
            }

            if (this.itemPredicate != ItemPredicate.ANY && (stackUsed == null || !this.itemPredicate.matches(stackUsed))) {
                return false;
            }

            if (this.mobEffectPredicate != MobEffectsPredicate.ANY) {
                if (activeEffect == null) {
                    return false;
                }
                // MobEffectsPredicate tests against player effect maps (Map<MobEffect, MobEffectInstance>)
                if (!this.mobEffectPredicate.matches(Map.of(activeEffect.getEffect(), activeEffect))) {
                    return false;
                }
            }

            return true;
        }

        @Override
        public @NotNull JsonObject serializeToJson(@NotNull SerializationContext context) {
            JsonObject json = super.serializeToJson(context);

            if (this.itemPredicate != ItemPredicate.ANY) {
                json.add("item", this.itemPredicate.serializeToJson());
            }

            if (this.mobEffectPredicate != MobEffectsPredicate.ANY) {
                json.add("mob_effect", this.mobEffectPredicate.serializeToJson());
            }

            this.extraContext.ifPresent(c -> json.addProperty("extra_context", c));

            return json;
        }
    }
}