package net.foxyas.changedaddon.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

public class PatEntityTrigger extends SimpleCriterionTrigger<PatEntityTrigger.TriggerInstance> {

    // Definindo o ID do trigger
    private static final ResourceLocation ID = ChangedAddonMod.resourceLoc("pat_entity_trigger");

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    @Override
    protected @NotNull TriggerInstance createInstance(JsonObject jsonObject, EntityPredicate.Composite p_66249_, DeserializationContext p_66250_) {
        // Verificação de presença de cada slot de armadura e desserialização dos predicados
        ItemPredicate headSlot = jsonObject.has("head") ? ItemPredicate.fromJson(jsonObject.get("head")) : ItemPredicate.ANY;
        ItemPredicate chestSlot = jsonObject.has("chest") ? ItemPredicate.fromJson(jsonObject.get("chest")) : ItemPredicate.ANY;
        ItemPredicate legsSlot = jsonObject.has("legs") ? ItemPredicate.fromJson(jsonObject.get("legs")) : ItemPredicate.ANY;
        ItemPredicate feetSlot = jsonObject.has("feets") ? ItemPredicate.fromJson(jsonObject.get("feets")) : ItemPredicate.ANY;

        // Deserializar o tipo de entidade, se presente
        EntityTypePredicate entityType = jsonObject.has("entity_type") ? EntityTypePredicate.fromJson(jsonObject.get("entity_type")) : EntityTypePredicate.ANY;
        JsonElement elem = jsonObject.get("name");
        String name = (elem != null && elem.isJsonPrimitive() && elem.getAsJsonPrimitive().isString())
                ? elem.getAsString()
                : "";

        if (!name.isEmpty()) {
            return new TriggerInstance(p_66249_, name, headSlot, chestSlot, legsSlot, feetSlot, entityType);

        }
        // Criar e retornar a instância do TriggerInstance
        return new TriggerInstance(p_66249_, headSlot, chestSlot, legsSlot, feetSlot, entityType);
    }

    public void Trigger(ServerPlayer player, Entity toPatTarget) {
        this.trigger(player, instance -> instance.matches(player, toPatTarget));
    }

    public void Trigger(ServerPlayer player, Entity toPatTarget, String name) {
        this.trigger(player, instance -> instance.matches(player, toPatTarget, name));
    }


    // Classe que define o critério para o trigger
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final ItemPredicate headSlot;
        private final ItemPredicate chestSlot;
        private final ItemPredicate legsSlot;
        private final ItemPredicate feetSlot;
        private final EntityTypePredicate entityType;
        private final String name;

        public TriggerInstance(EntityPredicate.Composite playerPredicate, String name, ItemPredicate headSlot, ItemPredicate chestSlot, ItemPredicate legsSlot, ItemPredicate feetSlot, EntityTypePredicate entityType) {
            super(PatEntityTrigger.ID, playerPredicate);
            this.name = name;
            this.headSlot = headSlot;
            this.chestSlot = chestSlot;
            this.legsSlot = legsSlot;
            this.feetSlot = feetSlot;
            this.entityType = entityType;
        }

        public TriggerInstance(EntityPredicate.Composite playerPredicate, ItemPredicate headSlot, ItemPredicate chestSlot, ItemPredicate legsSlot, ItemPredicate feetSlot, EntityTypePredicate entityType) {
            super(PatEntityTrigger.ID, playerPredicate);
            this.name = null;
            this.headSlot = headSlot;
            this.chestSlot = chestSlot;
            this.legsSlot = legsSlot;
            this.feetSlot = feetSlot;
            this.entityType = entityType;
        }

        // Method para verificar as condições
        public boolean matches(ServerPlayer player, Entity toPatTarget) {
            // Verificando se o jogador usa a armadura correta
            boolean wearingCorrectArmor =
                    headSlot.matches(player.getItemBySlot(EquipmentSlot.HEAD))
                            && chestSlot.matches(player.getItemBySlot(EquipmentSlot.CHEST))
                            && legsSlot.matches(player.getItemBySlot(EquipmentSlot.LEGS))
                            && feetSlot.matches(player.getItemBySlot(EquipmentSlot.FEET));

            if (toPatTarget == null) {
                return wearingCorrectArmor;
            }

            // Verificando o tipo de entidade
            boolean correctEntityType = entityType.matches(toPatTarget.getType());

            return wearingCorrectArmor && correctEntityType;
        }

        // Method para verificar as condições
        public boolean matches(ServerPlayer player, Entity toPatTarget, String name) {
            // Verificando se o jogador usa a armadura correta
            boolean wearingCorrectArmor =
                    headSlot.matches(player.getItemBySlot(EquipmentSlot.HEAD))
                            && chestSlot.matches(player.getItemBySlot(EquipmentSlot.CHEST))
                            && legsSlot.matches(player.getItemBySlot(EquipmentSlot.LEGS))
                            && feetSlot.matches(player.getItemBySlot(EquipmentSlot.FEET));

            if (toPatTarget == null) {
                return name.equals(this.name) && wearingCorrectArmor;
            }

            // Verificando o tipo de entidade
            boolean correctEntityType = entityType.matches(toPatTarget.getType());

            return name.equals(this.name) && (wearingCorrectArmor && correctEntityType);
        }

        // Serializando o trigger para JSON
        public @NotNull JsonObject serializeToJson(@NotNull SerializationContext context) {
            JsonObject jsonObject = super.serializeToJson(context);
            if (headSlot != null) jsonObject.add("head", headSlot.serializeToJson());
            if (chestSlot != null) jsonObject.add("chest", chestSlot.serializeToJson());
            if (legsSlot != null) jsonObject.add("legs", legsSlot.serializeToJson());
            if (feetSlot != null) jsonObject.add("feets", feetSlot.serializeToJson());
            if (entityType != null) jsonObject.add("entity_type", entityType.serializeToJson());
            if (name != null && !name.isEmpty()) jsonObject.addProperty("name", name);
            return jsonObject;
        }
    }
}
