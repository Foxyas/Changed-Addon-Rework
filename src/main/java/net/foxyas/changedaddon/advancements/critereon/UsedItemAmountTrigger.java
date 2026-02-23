package net.foxyas.changedaddon.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UsedItemAmountTrigger
        extends SimpleCriterionTrigger<UsedItemAmountTrigger.Instance> {

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath("changed_addon", "used_item_amount");


    @Override
    protected @NotNull Instance createInstance(@NotNull JsonObject json, @NotNull ContextAwarePredicate pPredicate, @NotNull DeserializationContext pContext) {
        Item item = ForgeRegistries.ITEMS.getValue(
                ResourceLocation.parse(GsonHelper.getAsString(json, "item"))
        );

        int min = GsonHelper.getAsInt(json, "min");
        Integer max = json.has("max") ? GsonHelper.getAsInt(json, "max") : null;

        return new Instance(pPredicate, item, min, max);
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, inst -> inst.matches(player));
    }

    public void trigger(ServerPlayer player, ItemStack itemStack) {
        this.trigger(player, inst -> inst.matches(player, itemStack));
    }

    // --------------------------------------------------

    public static class Instance extends AbstractCriterionTriggerInstance {

        private final Item item;
        private final int min;
        @Nullable
        private final Integer max; // nullable

        public Instance(
                ContextAwarePredicate player,
                Item item,
                int min,
                @Nullable Integer max
        ) {
            super(ID, player);
            this.item = item;
            this.min = min;
            this.max = max;
        }

        public boolean matches(ServerPlayer player) {
            int used = player.getStats()
                    .getValue(Stats.ITEM_USED.get(item));

            if (used < min) return false;
            return max == null || used <= max;
        }

        public boolean matches(ServerPlayer player, ItemStack itemStack) {
            if (itemStack.getItem() != item) {
                return false;
            }

            int used = player.getStats()
                    .getValue(Stats.ITEM_USED.get(itemStack.getItem()));

            if (used < min) return false;
            return max == null || used <= max;
        }

        @Override
        public @NotNull JsonObject serializeToJson(@NotNull SerializationContext context) {
            JsonObject json = super.serializeToJson(context);

            json.addProperty("item",
                    ForgeRegistries.ITEMS.getKey(item).toString());
            json.addProperty("min", min);

            if (max != null) {
                json.addProperty("max", max);
            }

            return json;
        }
    }
}
