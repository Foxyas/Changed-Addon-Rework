package net.foxyas.changedaddon.entity.api;

import net.foxyas.changedaddon.entity.ai.*;
import net.foxyas.changedaddon.network.syncher.ChangedAddonEntityDataSerializers;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TamableLatexEntity;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.UUID;

public interface TamableLatexEntityFavors extends TamableLatexEntity {


    int OWNER_HOSTILE_DURATION_TICKS = 600;

    EntityDataAccessor<Byte> DATA_FLAGS = SynchedEntityData.defineId(ChangedEntity.class, EntityDataSerializers.BYTE);
    EntityDataAccessor<Optional<UUID>> DATA_OWNER_UUID = SynchedEntityData.defineId(ChangedEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    EntityDataAccessor<LatexTargetType> DATA_TARGET_TYPE = SynchedEntityData.defineId(ChangedEntity.class, ChangedAddonEntityDataSerializers.LATEX_TARGET_TYPE);
    EntityDataAccessor<LatexAttackType> DATA_ATTACK_TYPE = SynchedEntityData.defineId(ChangedEntity.class, ChangedAddonEntityDataSerializers.LATEX_ATTACK_TYPE);
    EntityDataAccessor<LatexAttackCondition> DATA_ATTACK_CONDITION = SynchedEntityData.defineId(ChangedEntity.class, ChangedAddonEntityDataSerializers.LATEX_ATTACK_CONDITION);
    EntityDataAccessor<LatexFavor> DATA_FAVOR = SynchedEntityData.defineId(ChangedEntity.class, ChangedAddonEntityDataSerializers.LATEX_FAVOR);

    GrabEntityAbilityInstance createGrabAbility();

    LatexInventory createInventory();

    default ChangedEntity getSelf() {
        if (this instanceof ChangedEntity changedEntity) {
            return changedEntity;
        }
        return null;
    }

    default void reassessTameGoals() {}

    void setOwnerUUID(UUID uuid);

    LatexInventory getInventory();

    void setInventory(LatexInventory latexInventory);

    LatexFavor getCurrentFavor();

    GrabEntityAbilityInstance getGrabAbility();

    void setFavor(LatexFavor latexFavor);

    boolean canDoFavor(LatexFavor latexFavor);

    LatexAttackCondition getAttackCondition();

    void setAttackCondition(LatexAttackCondition cycle);

    LatexAttackType getAttackType();

    void setAttackType(LatexAttackType cycle);

    LatexTargetType getTargetType();

    void setTargetType(LatexTargetType cycle);

    void updateHeldItemChoice();

    boolean isInteractingWith(LivingEntity entity);

    void swapSlotWithOffhand(int swapWith);

    int findSlotForTransfur();

    int findSlotForCombat();

    int findSlotForNonCombat();
}
