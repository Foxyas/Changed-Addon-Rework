package net.foxyas.changedaddon.ability;

import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;

public class GenericLeapDiveAbility extends AbstractAbility<GenericLeapDiveAbilityInstance> {

    public GenericLeapDiveAbility() {
        super(GenericLeapDiveAbilityInstance::new);
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        return 60;
    }
}