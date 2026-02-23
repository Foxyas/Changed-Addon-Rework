package net.foxyas.changedaddon.entity.api;

import net.foxyas.changedaddon.entity.ai.*;
import net.foxyas.changedaddon.entity.ai.LatexAttackType;
import net.foxyas.changedaddon.entity.ai.LatexAttackType;
import net.foxyas.changedaddon.entity.ai.LatexInventory;
import net.foxyas.changedaddon.entity.ai.LatexTargetType;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TamableLatexEntity;

public interface TamableLatexEntityFavors extends TamableLatexEntity {

    GrabEntityAbilityInstance createGrabAbility();

    LatexInventory createInventory();

    default ChangedEntity getSelf() {
        if (this instanceof ChangedEntity changedEntity) {
            return changedEntity;
        }
        return null;
    }

    LatexInventory getInventory();

    LatexFavor getCurrentFavor();

    GrabEntityAbilityInstance getGrabAbility();

    void setFavor(LatexFavor latexFavor);

    boolean canDoFavor(LatexFavor latexFavor);

    LatexAttackCondition getAttackCondition();

    LatexAttackType getAttackType();

    LatexTargetType getTargetType();

    void setTargetType(LatexTargetType cycle);

    void setAttackType(LatexAttackType cycle);

    void updateHeldItemChoice();

    void setAttackCondition(LatexAttackCondition cycle);
}
