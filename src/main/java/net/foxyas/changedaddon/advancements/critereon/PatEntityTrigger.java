package net.foxyas.changedaddon.advancements.critereon;

import com.google.gson.JsonObject;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.Emote;
import net.ltxprogrammer.changed.entity.beast.AbstractDarkLatexWolf;
import net.ltxprogrammer.changed.init.ChangedParticles;
import net.ltxprogrammer.changed.process.ProcessEmote;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

public class PatEntityTrigger extends SimpleCriterionTrigger<PatEntityTrigger.TriggerInstance> {

    // Definindo o ID do trigger
    private static final ResourceLocation ID = new ResourceLocation("changed_addon", "pat_entity_trigger");

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    @Override
    protected @NotNull TriggerInstance createInstance(JsonObject p_66248_, EntityPredicate.Composite p_66249_, DeserializationContext p_66250_) {
        // Verificação de presença de cada slot de armadura e desserialização dos predicados
        ItemPredicate headSlot = p_66248_.has("head") ? ItemPredicate.fromJson(p_66248_.get("head")) : ItemPredicate.ANY;
        ItemPredicate chestSlot = p_66248_.has("chest") ? ItemPredicate.fromJson(p_66248_.get("chest")) : ItemPredicate.ANY;
        ItemPredicate legsSlot = p_66248_.has("legs") ? ItemPredicate.fromJson(p_66248_.get("legs")) : ItemPredicate.ANY;
        ItemPredicate feetSlot = p_66248_.has("feets") ? ItemPredicate.fromJson(p_66248_.get("feets")) : ItemPredicate.ANY;

        // Deserializar o tipo de entidade, se presente
        EntityTypePredicate entityType = p_66248_.has("entity_type") ? EntityTypePredicate.fromJson(p_66248_.get("entity_type")) : EntityTypePredicate.ANY;

        // Criar e retornar a instância do TriggerInstance
        return new TriggerInstance(p_66249_, headSlot, chestSlot, legsSlot, feetSlot, entityType);
    }

    public void Trigger(ServerPlayer player,Entity entity){
        this.trigger(player,instance -> instance.matches(player, entity));
    }


    // Classe que define o critério para o trigger
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final ItemPredicate headSlot;
        private final ItemPredicate chestSlot;
        private final ItemPredicate legsSlot;
        private final ItemPredicate feetSlot;
        private final EntityTypePredicate entityType;

        public TriggerInstance(EntityPredicate.Composite playerPredicate, ItemPredicate headSlot, ItemPredicate chestSlot, ItemPredicate legsSlot, ItemPredicate feetSlot, EntityTypePredicate entityType) {
            super(PatEntityTrigger.ID, playerPredicate);
            this.headSlot = headSlot;
            this.chestSlot = chestSlot;
            this.legsSlot = legsSlot;
            this.feetSlot = feetSlot;
            this.entityType = entityType;
        }

        // Método para verificar as condições
        public boolean matches(ServerPlayer player, Entity entity) {
            // Verificando se o jogador usa a armadura correta
            boolean wearingCorrectArmor =
                    headSlot.matches(player.getItemBySlot(EquipmentSlot.HEAD))
                    && chestSlot.matches(player.getItemBySlot(EquipmentSlot.CHEST))
                    && legsSlot.matches(player.getItemBySlot(EquipmentSlot.LEGS))
                    && feetSlot.matches(player.getItemBySlot(EquipmentSlot.FEET));

            // Verificando o tipo de entidade
            boolean correctEntityType = entityType.matches(entity.getType());

            return wearingCorrectArmor && correctEntityType;
        }

        // Serializando o trigger para JSON
        public @NotNull JsonObject serializeToJson(@NotNull SerializationContext context) {
            JsonObject jsonObject = super.serializeToJson(context);
            if (headSlot != null) jsonObject.add("head", headSlot.serializeToJson());
            if (chestSlot != null) jsonObject.add("chest", chestSlot.serializeToJson());
            if (legsSlot != null) jsonObject.add("legs", legsSlot.serializeToJson());
            if (feetSlot != null) jsonObject.add("feets", feetSlot.serializeToJson());
            if (entityType != null) jsonObject.add("entity_type", entityType.serializeToJson());
            return jsonObject;
        }
    }
}
