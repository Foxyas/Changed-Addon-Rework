package net.foxyas.changedaddon.advancements.critereon;

import com.google.gson.JsonObject;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class HoldingItemsTrigger extends SimpleCriterionTrigger<HoldingItemsTrigger.Instance> {
    private static final ResourceLocation ID = ChangedAddonMod.resourceLoc("holding_items");

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    @Override
    protected @NotNull Instance createInstance(@NotNull JsonObject json, @NotNull ContextAwarePredicate playerPredicate, @NotNull DeserializationContext context) {
        ItemPredicate targetA = ItemPredicate.fromJson(json.get("main_hand"));
        ItemPredicate targetB = ItemPredicate.fromJson(json.get("off_hand"));

        LogicOp op = LogicOp.fromString(GsonHelper.getAsString(json, "operator", "AND"));
        boolean allowReversed = GsonHelper.getAsBoolean(json, "allow_reversed", false);

        return new Instance(playerPredicate, targetA, targetB, op, allowReversed);
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, instance ->
                instance.matches(player)
        );
    }

    public enum LogicOp {
        AND, OR, XOR, NAND, NOR, XNOR;

        public static LogicOp fromString(String name) {
            try {
                return LogicOp.valueOf(name.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                return AND;
            }
        }

        public boolean evaluate(boolean a, boolean b) {
            return switch (this) {
                case AND -> a && b;
                case OR -> a || b;
                case XOR -> a ^ b;
                case NAND -> !(a && b);
                case NOR -> !(a || b);
                case XNOR -> !(a ^ b);
            };
        }
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        private final ItemPredicate mainHand;
        private final ItemPredicate offHand;
        private final LogicOp operator;
        private final boolean allowReversed;

        public Instance(@NotNull ContextAwarePredicate playerPredicate, ItemPredicate mainHand, ItemPredicate offHand, LogicOp operator, boolean allowReversed) {
            super(ID, playerPredicate);
            this.mainHand = mainHand != null ? mainHand : ItemPredicate.ANY;
            this.offHand = offHand != null ? offHand : ItemPredicate.ANY;
            this.operator = operator != null ? operator : LogicOp.AND;
            this.allowReversed = allowReversed;
        }

        public boolean matches(ServerPlayer player) {
            ItemStack main = player.getMainHandItem();
            ItemStack off = player.getOffhandItem();

            // Direct check: Mainhand evaluates against A, Offhand evaluates against B
            boolean directMatch = evaluateHands(main, off);
            if (directMatch) {
                return true;
            }

            // Swapped check: Mainhand evaluates against B, Offhand evaluates against A
            if (this.allowReversed) {
                return evaluateHands(off, main);
            }

            return false;
        }

        private boolean evaluateHands(ItemStack handForA, ItemStack handForB) {
            boolean matchA = this.mainHand.matches(handForA);
            boolean matchB = this.offHand.matches(handForB);
            return this.operator.evaluate(matchA, matchB);
        }

        @Override
        public @NotNull JsonObject serializeToJson(@NotNull SerializationContext context) {
            JsonObject json = super.serializeToJson(context);
            if (this.mainHand != ItemPredicate.ANY) {
                json.add("main_hand", this.mainHand.serializeToJson());
            }
            if (this.offHand != ItemPredicate.ANY) {
                json.add("off_hand", this.offHand.serializeToJson());
            }
            json.addProperty("operator", this.operator.name());
            if (this.allowReversed) {
                json.addProperty("allow_reversed", true);
            }
            return json;
        }


        /**
         * Matches using any logic operator and optional hand flipping.
         */
        public static Instance of(ItemPredicate mainHand, ItemPredicate offHand, LogicOp op, boolean allowReversed) {
            return new Instance(ContextAwarePredicate.ANY, mainHand, offHand, op, allowReversed);
        }

        /**
         * Default helper: Checks Predicate A in Mainhand and Predicate B in Offhand (AND logic).
         */
        public static Instance holdingBoth(ItemPredicate mainHand, ItemPredicate offHand, boolean allowReversed) {
            return of(mainHand, offHand, LogicOp.AND, allowReversed);
        }

        /**
         * Simple shortcut: Checks Predicate A in Mainhand and Predicate B in Offhand with allowReversed enabled.
         */
        public static Instance holdingBoth(ItemPredicate mainHand, ItemPredicate offHand) {
            return holdingBoth(mainHand, offHand, true);
        }

        /**
         * Checks if player holds either A OR B anywhere across main/off hand.
         */
        public static Instance holdingEither(ItemPredicate mainHand, ItemPredicate offHand) {
            return of(mainHand, offHand, LogicOp.OR, true);
        }

        /**
         * Checks XOR condition (must hold A or B, but NOT both).
         */
        public static Instance holdingExclusive(ItemPredicate mainHand, ItemPredicate offHand) {
            return of(mainHand, offHand, LogicOp.XOR, true);
        }
    }
}