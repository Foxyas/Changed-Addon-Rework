package net.foxyas.changedaddon.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.FakePlayer;

public class LavaSwimmingTrigger extends SimpleCriterionTrigger<LavaSwimmingTrigger.Instance> {
    private static final ResourceLocation ID = new ResourceLocation("changed_addon", "lava_swimming");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    protected Instance createInstance(JsonObject json, EntityPredicate.Composite playerPredicate, DeserializationContext context) {
        return new Instance(playerPredicate);
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, instance -> true);
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        public Instance(EntityPredicate.Composite playerPredicate) {
            super(ID, playerPredicate);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context) {
            return super.serializeToJson(context);
        }
    }
}
