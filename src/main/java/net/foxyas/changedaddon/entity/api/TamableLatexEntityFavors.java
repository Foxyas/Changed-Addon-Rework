package net.foxyas.changedaddon.entity.api;

import net.foxyas.changedaddon.entity.ai.*;
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

    void setAttackCondition(LatexAttackCondition cycle);

    LatexAttackType getAttackType();

    void setAttackType(LatexAttackType cycle);

    LatexTargetType getTargetType();

    void setTargetType(LatexTargetType cycle);

    void updateHeldItemChoice();
}
